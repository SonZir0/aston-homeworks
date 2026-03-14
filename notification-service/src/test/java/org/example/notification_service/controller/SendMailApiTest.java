package org.example.notification_service.controller;

import org.example.notification_service.dto.SendEmailRequestDto;
import org.example.notification_service.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SendMailApi.class)
class SendMailApiTest {
    @MockitoBean
    EmailService emailService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    SendEmailRequestDto[] testDataArr = {
            new SendEmailRequestDto(
                    "jane@schmidt",
                    "Account status",
                    "Your account's been successfully created!"),
            new SendEmailRequestDto(
                    "mark@heckler",
                    "Once in a lifetime opportunity!",
                    "Check out in the attached link!")
    };

    @Test
    void sendMailToTest_CallsMailServiceToSendEmail() throws Exception {
        mockMvc.perform(post("/api/sendMailTo")
                        .content(objectMapper.writeValueAsString(testDataArr[1]))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(emailService, times(1)).sendEmail(
                "mark@heckler",
                "Once in a lifetime opportunity!",
                "Check out in the attached link!");
    }
}