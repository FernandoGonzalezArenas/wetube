package com.wetube.admin.service;

import com.wetube.admin.client.UserClient;
import com.wetube.admin.client.VideoClient;
import com.wetube.admin.config.RabbitMQConfig;
import com.wetube.admin.dto.ReportDetailDto;
import com.wetube.admin.dto.UserPrincipal;
import com.wetube.admin.dto.VideoMetadataDto;
import com.wetube.admin.entity.AdminEntity;
import com.wetube.admin.entity.ReportEntity;
import com.wetube.admin.entity.ReportStatus;
import com.wetube.admin.entity.ReportType;
import com.wetube.admin.repository.AdminRepository;
import com.wetube.admin.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

@Mock
    private VideoClient videoClient;
@Mock
    private UserClient userClient;
@Mock
    private AdminRepository adminRepository;
@Mock
    private ReportRepository reportRepository;
@Mock
    private RabbitTemplate rabbitTemplate;

@InjectMocks
    private AdminServiceImpl adminService;

@BeforeEach
    void setup(){
    UserPrincipal principal=new UserPrincipal(1L, "adminUser");
    var auth=new UsernamePasswordAuthenticationToken(principal, null, null);
    SecurityContextHolder.getContext().setAuthentication(auth);
}
@Test
    void moderateUser_ShouldBanUserAndNotify(){
    Long targetId=99L;
    String reason="spam detectado";

    adminService.moderateUser(targetId, reason);

verify(userClient, times(1)).banUserInternal(targetId);
    ArgumentCaptor<AdminEntity> adminCaptor=ArgumentCaptor.forClass(AdminEntity.class);
    verify(adminRepository, times(1)).save(adminCaptor.capture());
    assertEquals("BAN_USER", adminCaptor.getValue().getActionType());
    assertEquals(targetId, adminCaptor.getValue().getTargetId());
    assertEquals(1L, adminCaptor.getValue().getAdminId());
    verify(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.ADMIN_EXCHANGE),
            eq(RabbitMQConfig.USER_BAN_RK),
            eq(targetId));
}

@Test
    void moderateVideo_ShouldDeleteVideoAndNotify(){
    Long videoId=500L;
    String reason="contenido inapropiado";

    adminService.moderateVideo(videoId, reason);

    verify(videoClient, times(1)).deleteVideoInternal(videoId);

    verify(rabbitTemplate).convertAndSend(
            eq(RabbitMQConfig.ADMIN_EXCHANGE),
            eq(RabbitMQConfig.VIDEO_DELETE_RK),
            eq(videoId));
}

@Test
    void shouldCreateReport(){
    ReportType type=ReportType.VIDEO;
    Long targetId=5L;
    String reason="contenido inapropiado";

    adminService.createReport(type, targetId, reason);

    ArgumentCaptor<ReportEntity> reportCaptor=ArgumentCaptor.forClass(ReportEntity.class);

    verify(reportRepository, times(1)).save(reportCaptor.capture());
    assertEquals(1L, reportCaptor.getValue().getReporterId());
}

@Test
    void shouldGetPendingReports(){
    VideoMetadataDto videoMetadataDto=VideoMetadataDto.builder()
                    .title("mi video")
                            .description("la descripcion")
                                    .videoUrl("url_del_video")
                                            .thumbnailUrl("thumbnail.jpg")
                                                    .build();
when(videoClient.getVideoDetails(anyLong())).thenReturn(videoMetadataDto);

    ReportEntity r1=ReportEntity.builder()
            .targetId(20L)
            .type(ReportType.VIDEO)
            .reporterId(1L)
            .reason("violencia")
            .status(ReportStatus.PENDING)
            .createdAt(LocalDateTime.now())
            .build();
    ReportEntity r2=ReportEntity.builder()
            .targetId(30L)
            .type(ReportType.VIDEO)
            .reporterId(1L)
            .reason("violencia")
            .status(ReportStatus.PENDING)
            .createdAt(LocalDateTime.now())
            .build();
when(reportRepository.findByStatus(ReportStatus.PENDING)).thenReturn(List.of(r1, r2));

List<ReportDetailDto> reports=adminService.getPendingReports();

assertEquals(2, reports.size());
assertTrue(reports.stream().anyMatch(r -> r.getTargetId().equals(20L)));
assertTrue(reports.stream().anyMatch(r -> r.getTargetId().equals(30L)));
}

@Test
    void shouldMarkDismissReport(){
    ReportEntity r1=ReportEntity.builder()
            .targetId(20L)
            .type(ReportType.VIDEO)
            .reporterId(1L)
            .reason("violencia")
            .status(ReportStatus.PENDING)
            .createdAt(LocalDateTime.now())
            .build();
when(reportRepository.findById(1L)).thenReturn(Optional.of(r1));

adminService.dismissReport(1L);

assertEquals(ReportStatus.DISMISSED, r1.getStatus());
verify(reportRepository, times(1)).save(r1);
}

@Test
    void dismissReport_ShouldThrowNotFound_WhenIdDoesNotExist(){
    when(reportRepository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(ResponseStatusException.class, () ->{
        adminService.dismissReport(999L);
    });
}

}
