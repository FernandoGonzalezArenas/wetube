package com.teakter.subscription.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionStatusDto {

    @Schema(description = "numero de subscriptores de un canal", example = "267")
    private Long totalSubscriptions;

    @Schema(description = "estado booleano de subscripcion", example = "true")
    private boolean subscriptionByUser;

}
