package com.wetube.video.client;

import com.wetube.video.dto.IdsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user")
public interface SubscriptionsClient {

@GetMapping("/subs/my-subscriptions")
    IdsDto getSubscriptionsByUser(@RequestHeader("X-User-Id") Long subscriberId);

}
