package com.wetube.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long targetId; //id de el recurso solicitado

@Enumerated(EnumType.STRING)
private ReportType type; //tipo de el recurso

    private Long reporterId; //id de el usuario que reporta
    private String reason; //motivo

    @Builder.Default
@Enumerated(EnumType.STRING)
    private ReportStatus status=ReportStatus.PENDING;
private LocalDateTime createdAt;

@PrePersist
    protected void onCreate(){
    this.createdAt=LocalDateTime.now();
}
}
