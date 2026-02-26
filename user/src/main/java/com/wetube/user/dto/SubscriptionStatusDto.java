package com.wetube.user.dto;

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
