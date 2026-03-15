package com.wetube.subscription.controller;


import com.wetube.subscription.dto.IdsDto;
import com.wetube.subscription.dto.SubscriptionStatusDto;
import com.wetube.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subs")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/{channelId}/toggle")
    public ResponseEntity<Void> toggleSubscription(@PathVariable Long channelId){
        boolean status=subscriptionService.toggleSubscription(channelId);
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
    public ResponseEntity<SubscriptionStatusDto> getChannelStatus(@PathVariable Long channelId){
        return ResponseEntity.ok(subscriptionService.getChannelStatus(channelId));
    }

    @GetMapping("/my-subscriptions")
    public ResponseEntity<IdsDto> getSubscriptionsByUser(){
        return ResponseEntity.ok(subscriptionService.getSubscriptionsByUser());
    }

}
