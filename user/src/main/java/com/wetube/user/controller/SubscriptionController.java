package com.wetube.user.controller;


import com.wetube.user.dto.IdsDto;
import com.wetube.user.dto.SubscriptionStatusDto;
import com.wetube.user.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subs")
public class SubscriptionController {

private final SubscriptionService subscriptionService;

public SubscriptionController(SubscriptionService subscriptionService){
    this.subscriptionService=subscriptionService;
}

@PostMapping("/{channelId}/toggle")
    public ResponseEntity<Void> toggleSubscription(@PathVariable Long channelId, @RequestHeader(value = "X-User-Id") Long subscriberId){
    boolean status=subscriptionService.toggleSubscription(channelId, subscriberId);
    if (status){
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }else {
        return ResponseEntity.noContent().build();
    }
}

@GetMapping("/{channelId}/count")
    public ResponseEntity<Long> countSubscriptors(@PathVariable Long channelId){
    return ResponseEntity.ok(subscriptionService.countSubscriptions(channelId));
}

@GetMapping("/{channelId}/status")
    public ResponseEntity<SubscriptionStatusDto> getChannelStatus(@PathVariable Long channelId, @RequestHeader(value = "X-User-Id", defaultValue = "0") Long subscriberId){
    return ResponseEntity.ok(subscriptionService.getChannelStatus(channelId, subscriberId));
}

@GetMapping("/my-subscriptions")
    public ResponseEntity<IdsDto> getSubscriptionsByUser(@RequestHeader("X-User-Id") Long subscriberId){
    return ResponseEntity.ok(subscriptionService.getSubscriptionsByUser(subscriberId));
}

}
