package com.wetube.admin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "admin-actions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminEntity {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

private Long adminId; //el id de el administrador que viene de el gateway
    private String actionType; //accion realizada como DELETE_VIDEO o BAN_USER
    private Long targetId; //el id de el recurso afectado
    private String reason; //justificacion de la accion realizada
    private LocalDateTime timestamp; //fecha de la accion

    @PrePersist
    protected void onCreate(){
        timestamp=LocalDateTime.now();
    }

}
