package com.wetube.user.repository;

import com.wetube.user.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    Optional<SubscriptionEntity> findBySubscriberIdAndChannelId(Long subscriberId, Long channelId);
    Long countByChannelId(Long channelId);
    boolean existsBySubscriberIdAndChannelId(Long subscriberId, Long channelId);
    @Query("SELECT s.channelId FROM SubscriptionEntity s WHERE s.subscriberId = :subscriberId")
    List<Long> findBySubscriberId(Long subscriberId);
}
