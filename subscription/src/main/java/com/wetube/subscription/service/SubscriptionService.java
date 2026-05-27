package com.wetube.subscription.service;


import com.wetube.subscription.dto.IdsDto;
import com.wetube.subscription.dto.SubscriptionStatusDto;

public interface SubscriptionService {

    boolean toggleSubscription(Long channelId);
    boolean hasUserSubscription(Long channelId);
    Long countSubscriptions(Long channelId);
    SubscriptionStatusDto getChannelStatus(Long channelId);
    IdsDto getSubscriptionsByUser(Long userId);
    void deleteSubscriptionsOfUser(Long userId);

}
