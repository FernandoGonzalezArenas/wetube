package com.wetube.subscription.repository;

import com.wetube.subscription.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    Optional<SubscriptionEntity> findBySubscriberIdAndChannelId(Long subscriberId, Long channelId);
    Long countByChannelId(Long channelId);
    boolean existsBySubscriberIdAndChannelId(Long subscriberId, Long channelId);
    @Query("SELECT s.channelId FROM SubscriptionEntity s WHERE s.subscriberId = :subscriberId")
    List<Long> findBySubscriberId(Long subscriberId);
    @Modifying
    @Query("DELETE FROM SubscriptionEntity s WHERE s.channelId = :userId")
    void deleteSubscriptorsOfChannelId(Long userId);

    @Modifying
    @Query("DELETE FROM SubscriptionEntity s WHERE s.subscriberId = :userId")
    void deleteSubscriptionsByChannelId(Long userId);
}
