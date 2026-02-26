package com.wetube.user.service;

import com.wetube.user.dto.SubscriptionStatusDto;
import com.wetube.user.dto.IdsDto;
import com.wetube.user.entity.SubscriptionEntity;
import com.wetube.user.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository){
        this.subscriptionRepository=subscriptionRepository;
    }

    @Override
    @Transactional
    public boolean toggleSubscription(Long channelId, Long subscriberId){
        if (channelId<=0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "problema con el ID de el canal");
        }
        Optional<SubscriptionEntity> existingSubscription=subscriptionRepository.findBySubscriberIdAndChannelId(subscriberId, channelId);
        if (existingSubscription.isPresent()){
            subscriptionRepository.delete(existingSubscription.get());
            return false;
        }else {
            SubscriptionEntity newSub= SubscriptionEntity.builder()
                    .subscriberId(subscriberId)
                    .channelId(channelId)
                    .build();
            subscriptionRepository.save(newSub);
            return true;
        }
    }

    @Override
    public boolean hasUserSubscription(Long channelId, Long subscriberId){
if (channelId<=0){
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "problema con el ID de el canal");
}
        if (subscriberId==0){
            return false;
        }
            return subscriptionRepository.existsBySubscriberIdAndChannelId(subscriberId, channelId);
    }

    @Override
    public Long countSubscriptions(Long channelId){
if (channelId<=0){
    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "problema con el ID de el canal");
}
return subscriptionRepository.countByChannelId(channelId);
    }

    @Override
    public SubscriptionStatusDto getChannelStatus(Long channelId, Long subscriberId){
        boolean statusSubscription=hasUserSubscription(channelId, subscriberId);
        Long totalSubs=countSubscriptions(channelId);

        return SubscriptionStatusDto.builder()
                .subscriptionByUser(statusSubscription)
                .totalSubscriptions(totalSubs)
                .build();
    }

    @Override
    public IdsDto getSubscriptionsByUser(Long subscriberId){
        //se obtiene la lista de los canales a los que sigue el usuario
        List<Long> ressult=subscriptionRepository.findBySubscriberId(subscriberId);

        //retornamos los ids en el DTO
        return new IdsDto(ressult);
    }

}
