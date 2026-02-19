package org.example.controller;

import org.example.dto.UserDto;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockitoBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    UserDto[] testDataArr = {
            new UserDto(
                    1,
                    "Jane Schmidt",
                    "jane@schmidt",
                    25,
                    LocalDate.parse("2021-09-01")),
            new UserDto(
                    1,
                    "Mark Heckler",
                    "mark@heckler",
                    41,
                    LocalDate.parse("2011-12-12")),
            new UserDto(
                    3,
                    "Kylo Ren",
                    "kylo@ren",
                    19,
                    LocalDate.parse("2016-08-07"))
    };

    @Test
    void addNewUserTest_ShouldAddNewUser() throws Exception {
        Mockito.when(userService.addNewUser(any())).thenReturn(testDataArr[1]);

        mockMvc.perform(post("/api/users")
                        .content(objectMapper.writeValueAsString(testDataArr[1]))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testDataArr[1].name()))
                .andExpect(jsonPath("$.email").value(testDataArr[1].email()))
                .andExpect(jsonPath("$.age").value(testDataArr[1].age()));

        verify(userService, times(1)).addNewUser(any());
    }

    @Test
    void updateUserRecordTest_ReturnsUpdatedUserOnSuccess() throws Exception {
        Mockito.when(userService.updateUserWithId(eq(3L), any())).thenReturn(Optional.of(testDataArr[0]));

        mockMvc.perform(put("/api/users/3")
                        .content(objectMapper.writeValueAsString(testDataArr[0]))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testDataArr[0].name()))
                .andExpect(jsonPath("$.email").value(testDataArr[0].email()))
                .andExpect(jsonPath("$.age").value(testDataArr[0].age()));

        verify(userService, times(1)).updateUserWithId(eq(3L), any());
    }

    @Test
    void updateUserRecordTest_EmptyResponseIfNoUserFound() throws Exception {
        Mockito.when(userService.updateUserWithId(eq(999L), any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/users/999")
                        .content(objectMapper.writeValueAsString(testDataArr[0]))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        verify(userService, times(1)).updateUserWithId(eq(999L), any());
    }

    @Test
    void findUserByIdTest_ShouldReturnUser() throws Exception {
        Mockito.when(userService.findUserById(1L)).thenReturn(Optional.of(testDataArr[0]));
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jane Schmidt"))
                .andExpect(jsonPath("$.email").value("jane@schmidt"))
                .andExpect(jsonPath("$.age").value(25))
                .andExpect(jsonPath("$.createdAt").value("2021-09-01"));

        verify(userService, times(1)).findUserById(1L);
    }

    @Test
    void findUserByIdTest_EmptyIfUserDoesNotExist() throws Exception {
        Mockito.when(userService.findUserById(999L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());

        verify(userService, times(1)).findUserById(999L);
    }

    @Test
    void getListOfUsersTest_ShouldGetAllUsers() throws Exception {
        Mockito.when(userService.getListOfUsers()).thenReturn(List.of(testDataArr));
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));

        verify(userService, times(1)).getListOfUsers();
    }

    @Test
    void removeUserRecordByIdTest_IsIdempotent() throws Exception {
        mockMvc.perform(delete("/api/users/2"))
                .andExpect(status().isOk());
        mockMvc.perform(delete("/api/users/2"))
                .andExpect(status().isOk());

        verify(userService, times(2)).removeUserById(2);
    }
}
