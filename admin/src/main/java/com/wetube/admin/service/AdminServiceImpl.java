package com.wetube.admin.service;

import com.wetube.admin.client.UserClient;
import com.wetube.admin.client.VideoClient;
import com.wetube.admin.config.RabbitMQConfig;
import com.wetube.admin.dto.ReportDetailDto;
import com.wetube.admin.dto.UserPrincipal;
import com.wetube.admin.dto.UserProfileDto;
import com.wetube.admin.dto.VideoMetadataDto;
import com.wetube.admin.entity.AdminEntity;
import com.wetube.admin.entity.ReportEntity;
import com.wetube.admin.entity.ReportStatus;
import com.wetube.admin.entity.ReportType;
import com.wetube.admin.repository.AdminRepository;
import com.wetube.admin.repository.ReportRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final VideoClient videoClient;
    private final UserClient userClient;
    private final AdminRepository adminRepository;
    private final ReportRepository reportRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    @CircuitBreaker(name = "video", fallbackMethod = "fallbackModerateVideo")
    public void moderateVideo(Long videoId, String reason){
//obtener el id de el administrador guardado en el contexto desde el gateway
        UserPrincipal principal=(UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long adminId=principal.userId();

        //borrar el video fisicamente de forma sincrona con feignClient
        videoClient.deleteVideoInternal(videoId);
System.out.println("operacion de eliminacion de video realizada correctamente | microservicio admin");

        //auditoría: registrar en la base de datos quien realiza la accion de borrado
        AdminEntity action=AdminEntity.builder()
                .adminId(adminId)
                .targetId(videoId)
                .reason(reason)
                .actionType("DELETE_VIDEO")
                .build();
        adminRepository.save(action);

        //accion asincrona, notificar eliminacion de comentarios y likes de el video eliminado
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ADMIN_EXCHANGE, RabbitMQConfig.VIDEO_DELETE_RK,
                videoId);
    }

    public void fallbackModerateVideo(Long videoId, String reason, Throwable throwable){
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "el servicio de video no esta disponible, no se pudo borrar el video");
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "user", fallbackMethod = "fallbackModerateUser")
    public void moderateUser(Long userId, String reason){
        UserPrincipal principal=(UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long adminId=principal.userId();

        userClient.banUserInternal(userId);
System.out.println("operacion de baneo de usuario realizada correctamente | microservicio admin");

        AdminEntity action=AdminEntity.builder()
                .adminId(adminId)
                .targetId(userId)
                .reason(reason)
                .actionType("BAN_USER")
                .build();
        adminRepository.save(action);

        rabbitTemplate.convertAndSend(RabbitMQConfig.ADMIN_EXCHANGE, RabbitMQConfig.USER_BAN_RK,
                userId);
    }

    public void fallbackModerateUser(Long userId, String reason, Throwable throwable){
        throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "el servicio user no esta disponible, no se pudo banear la cuenta");
    }

    public void createReport(ReportType type, Long targetId, String reason){
        UserPrincipal principal=(UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long reporterId=principal.userId();

        ReportEntity report=ReportEntity.builder()
                .type(type)
                .targetId(targetId)
                .reporterId(reporterId)
                .reason(reason)
                .build();
        reportRepository.save(report);
    }

    @CircuitBreaker(name = "video", fallbackMethod = "fallbackGetReports")
    public List<ReportDetailDto> getPendingReports(){
        return reportRepository.findByStatus(ReportStatus.PENDING).stream()
                .map(report -> {
                    ReportDetailDto dto=ReportDetailDto.builder()
                            .reportId(report.getId())
                            .type(report.getType())
                            .targetId(report.getTargetId())
                            .status(report.getStatus().name())
                            .reason(report.getReason())
                            .createdAt(report.getCreatedAt())
                            .build();

if (report.getType()== ReportType.VIDEO){
    fillVideoDetails(dto, report.getTargetId());
}else if (report.getType()==ReportType.USER){
    fillUserDetails(dto, report.getTargetId());
}

                    return dto;
                } ).toList();
    }

    public List<ReportDetailDto> fallbackGetReports(Throwable throwable){
        return List.of(ReportDetailDto.builder()
                .videoTitle("video no obtenido, sucedio un error")
                .build());
    }

    //descartar un reporte si no hay nada malo
    public void dismissReport(Long reportId){
        ReportEntity report=reportRepository.findById(reportId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        report.setStatus(ReportStatus.DISMISSED);
        reportRepository.save(report);
    }

    private void fillVideoDetails(ReportDetailDto dto, Long videoId){
        VideoMetadataDto video=videoClient.getVideoDetails(videoId);
        if (video!=null){
            dto.setVideoTitle(video.getTitle());
            dto.setVideoDescription(video.getDescription());
            dto.setVideoUrl(video.getVideoUrl());
            dto.setThumbnailUrl(video.getThumbnailUrl());
        }
    }

    private void fillUserDetails(ReportDetailDto dto, Long userId){
        UserProfileDto profileDto=userClient.getProfileUser(userId);
        if (profileDto!=null){
            dto.setTargetUsername(profileDto.getUsername());
        }
    }

}
