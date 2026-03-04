package com.wetube.user.service;

import com.wetube.user.dto.SubscriptionStatusDto;
import com.wetube.user.entity.SubscriptionEntity;
import com.wetube.user.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceImplTest {

@Mock
    private SubscriptionRepository repository;

@InjectMocks
    private SubscriptionServiceImpl service;

@Test
    void toggleSubscription_NewSubscription_ReturnsTrue(){
    //simulamos que no existe una subscripcion previa
when(repository.findBySubscriberIdAndChannelId(1L, 100L))
        .thenReturn(Optional.empty());

//ejecutamos el toggle
    boolean result=service.toggleSubscription(100L, 1L);

    //verificaciones
    assertTrue(result);
    verify(repository, times(1)).save(any(SubscriptionEntity.class));
}

@Test
    void toggleSubscription_existingSubscription_DeleteAndReturnsFalse(){
    //simulamos que ya existe la subscripcion
    SubscriptionEntity existing=new SubscriptionEntity(1L, 1L, 100L);
    when(repository.findBySubscriberIdAndChannelId(1L, 100L)).thenReturn(Optional.of(existing));

    boolean result=service.toggleSubscription(100L, 1L);

    //verificaciones
    assertFalse(result);
    verify(repository, times(1)).delete(existing);
}

@Test
    void getChannelStatus_ShouldReturnCorrectStatus_ForAuthenticatedUser(){
    when(repository.countByChannelId(100L)).thenReturn(15L);
    when(repository.existsBySubscriberIdAndChannelId(1L, 100L)).thenReturn(true);

    //llamada a el metodo
    SubscriptionStatusDto status=service.getChannelStatus(100L, 1L);

    //validaciones
    assertEquals(15L, status.getTotalSubscriptions());
    assertTrue(status.isSubscriptionByUser());
}

@Test
    void getChannelStatus_ShouldReturnFalse_IfGestUser(){
    when(repository.countByChannelId(100L)).thenReturn(15L);

    SubscriptionStatusDto status=service.getChannelStatus(100L, 0L);

    //validaciones
    assertEquals(15L, status.getTotalSubscriptions());
    assertFalse(status.isSubscriptionByUser());
}

@Test
    void shouldReturnThrows_WhenChannelIdIsInvalid(){
    assertThrows(ResponseStatusException.class, () -> service.countSubscriptions(0L));
}

}
