package com.teakter.subscription.service;


import com.teakter.subscription.dto.IdsDto;
import com.teakter.subscription.dto.SubscriptionStatusDto;

public interface SubscriptionService {

    boolean toggleSubscription(Long channelId);
    boolean hasUserSubscription(Long channelId);
    Long countSubscriptions(Long channelId);
    SubscriptionStatusDto getChannelStatus(Long channelId);
    IdsDto getSubscriptionsByUser(Long userId);
    void deleteSubscriptionsOfUser(Long userId);

}
