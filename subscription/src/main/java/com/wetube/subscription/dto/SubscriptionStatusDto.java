package com.wetube.subscription.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionStatusDto {

    private Long totalSubscriptions;
    private boolean subscriptionByUser;

}
