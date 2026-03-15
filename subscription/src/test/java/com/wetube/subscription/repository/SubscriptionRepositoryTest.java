package com.wetube.subscription.repository;

import com.wetube.subscription.entity.SubscriptionEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class SubscriptionRepositoryTest {

    @Autowired
    private SubscriptionRepository repository;

    @Test
    void findBySubscriberId_ReturnsList(){
        //guardar datos reales en la base en memoria H2
        repository.save(SubscriptionEntity.builder()
                .subscriberId(1L)
                .channelId(100L)
                .build());
        repository.save(SubscriptionEntity.builder()
                .subscriberId(1L)
                .channelId(200L)
                .build());

        List<Long> ids=repository.findBySubscriberId(1L);

        //validaciones
        assertEquals(2, ids.size());
        assertTrue(ids.contains(100L));
    }

}
