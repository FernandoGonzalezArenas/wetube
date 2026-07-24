package com.teakter.subscription.config;

import com.teakter.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsumerRabbitListener {

    private final SubscriptionService subscriptionService;

    @RabbitListener(queues = "user.subs.delete.queue")
    public void deleteSubscriptionsOfUser(Long userId){

        //llamamos a el metodo para borrar las subscripciones
subscriptionService.deleteSubscriptionsOfUser(userId);
    }


}
