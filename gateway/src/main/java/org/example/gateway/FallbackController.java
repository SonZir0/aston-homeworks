package org.example.gateway;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    @RequestMapping("/fallback")
    public ResponseEntity<String> fallback() {
        return new ResponseEntity<>("Сервис временно недоступен. Пожалуйста, попробуйте позже.",
                HttpStatus.SERVICE_UNAVAILABLE);
    }
}
