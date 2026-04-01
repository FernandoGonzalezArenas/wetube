package com.wetube.admin.controller;

import com.wetube.admin.dto.AuditorDto;
import com.wetube.admin.dto.ReportDetailDto;
import com.wetube.admin.entity.ReportType;
import com.wetube.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

private final AdminService adminService;

@DeleteMapping("/videos/{videoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteVideo(@PathVariable Long videoId, @RequestBody AuditorDto datos){
    adminService.moderateVideo(videoId, datos.getReason());
    return ResponseEntity.ok("video eliminado por administracion");
}

@DeleteMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> banUser(@PathVariable Long userId, @RequestBody AuditorDto datos){
    adminService.moderateUser(userId, datos.getReason());
    return ResponseEntity.ok("usuario baneado por administracion");
}

@PostMapping("/reports/{type}/{targetId}")
    public ResponseEntity<String> createReport(@PathVariable ReportType type,  @PathVariable Long targetId, @RequestBody AuditorDto datos){
    adminService.createReport(type, targetId, datos.getReason());
    return ResponseEntity.ok("reporte creado y enviado a revision");
}

@GetMapping("/reports/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReportDetailDto>> getReports(){
    return ResponseEntity.ok(adminService.getPendingReports());
}

@PatchMapping("/reports/{reportId}/dismiss")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> dismiss(@PathVariable Long reportId){
    adminService.dismissReport(reportId);
    return ResponseEntity.ok("reporte descartado por el administrador");
}

}
