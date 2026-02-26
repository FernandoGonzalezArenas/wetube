package com.wetube.user.service;

import com.wetube.user.dto.SubscriptionStatusDto;
import com.wetube.user.dto.IdsDto;

public interface SubscriptionService {

boolean toggleSubscription(Long channelId, Long subscriberId);
boolean hasUserSubscription(Long channelId, Long subscriberId);
Long countSubscriptions(Long channelId);
SubscriptionStatusDto getChannelStatus(Long channelId, Long subscriberId);
IdsDto getSubscriptionsByUser(Long subscriberId);
}
