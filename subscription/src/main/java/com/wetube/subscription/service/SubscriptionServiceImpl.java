package com.wetube.subscription.service;

import com.wetube.subscription.dto.IdsDto;
import com.wetube.subscription.dto.SubscriptionStatusDto;
import com.wetube.subscription.dto.UserPrincipal;
import com.wetube.subscription.entity.SubscriptionEntity;
import com.wetube.subscription.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public boolean toggleSubscription(Long channelId){
        System.out.println("entrando en el metodo toggle del backend con el ChannelId = "+ channelId);
        if (channelId<=0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "problema con el ID de el canal");
        }

        UserPrincipal principal=(UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long subscriberId=principal.userId();
System.out.println("el ID de el usuario actual dentro de el metodo toggle es = " + subscriberId);

        Optional<SubscriptionEntity> existingSubscription=subscriptionRepository.findBySubscriberIdAndChannelId(subscriberId, channelId);
        if (existingSubscription.isPresent()){
            subscriptionRepository.delete(existingSubscription.get());
            return false;
        }else {
            try {
                SubscriptionEntity newSub = SubscriptionEntity.builder()
                        .subscriberId(subscriberId)
                        .channelId(channelId)
                        .build();
                subscriptionRepository.save(newSub);
                return true;
            }catch (DataIntegrityViolationException e){
                return true;
            }
        }
    }

    @Override
    public boolean hasUserSubscription(Long channelId){
        if (channelId<=0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "problema con el ID de el canal");
        }
var auth=SecurityContextHolder.getContext().getAuthentication();

        if (auth==null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")){
            return false;
        }
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        Long subscriberId = principal.userId();

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
    public SubscriptionStatusDto getChannelStatus(Long channelId){
        System.out.println("entrando en el metodo status de subscription en el backend con el ChannelId = " + channelId);

        boolean statusSubscription=hasUserSubscription(channelId);
        Long totalSubs=countSubscriptions(channelId);

        return SubscriptionStatusDto.builder()
                .subscriptionByUser(statusSubscription)
                .totalSubscriptions(totalSubs)
                .build();
    }

    @Override
    public IdsDto getSubscriptionsByUser(Long userId){

        //se obtiene la lista de los canales a los que sigue el usuario
        List<Long> ressult=subscriptionRepository.findBySubscriberId(userId);

        //retornamos los ids en el DTO
        return new IdsDto(ressult);
    }

    @Override
    @Transactional
    public void deleteSubscriptionsOfUser(Long userId){
subscriptionRepository.deleteSubscriptorsOfChannelId(userId);
subscriptionRepository.deleteSubscriptionsByChannelId(userId);
    }

}
