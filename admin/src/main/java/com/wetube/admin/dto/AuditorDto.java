package com.wetube.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.retry.annotation.Backoff;

@Data
@Backoff
@AllArgsConstructor
@NoArgsConstructor
public class AuditorDto {

private String reason;


}
