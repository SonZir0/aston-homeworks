package org.example.advices;

import org.example.controller.UserController;
import org.example.mapper.UserHateoasModelAssembler;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserNotFoundAdviceTest {

    @MockitoBean
    UserService userService;
    @MockitoBean
    UserHateoasModelAssembler modelAssembler;

    @Autowired
    MockMvc mockMvc;

    @Test
    void userNotFoundHandlerTest_HandlesExceptionsInControllers() throws Exception {
        when(userService.findUserById(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Не удалось найти пользователя с ID: 999"));
        verify(userService, times(1)).findUserById(999L);
    }
}