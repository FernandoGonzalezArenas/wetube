package com.wetube.user.controller;


import com.wetube.user.dto.SubscriptionStatusDto;
import com.wetube.user.service.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(SubscriptionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SubscriptionControllerTest {

@Autowired
    private MockMvc mockMvc;

@MockBean
    private SubscriptionService service;

@Test
    void toggleSubscription_endpoint_ReturnsCreated() throws Exception{
when(service.toggleSubscription(anyLong(), anyLong())).thenReturn(true);
mockMvc.perform(post("/subs/100/toggle")
        .header("X-User-Id", 1L))
        .andExpect(status().isCreated());
}

@Test
    void toggleSubscription_endpoint_ReturnsNoContent() throws Exception{
    when(service.toggleSubscription(anyLong(), anyLong())).thenReturn(false);
    mockMvc.perform(post("/subs/100/toggle")
            .header("X-User-Id", 1L))
            .andExpect(status().isNoContent());
}

@Test
    void getChannelStatus_ShouldReturnOkWithJSON() throws Exception{
    SubscriptionStatusDto status=new SubscriptionStatusDto(10L, true);
    when(service.getChannelStatus(100L, 1L)).thenReturn(status);

    mockMvc.perform(get("/subs/100/status")
                    .header("X-User-Id", 1L))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalSubscriptions").value(10L))
            .andExpect(jsonPath("$.subscriptionByUser").value(true));
}

}
