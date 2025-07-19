package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.NotificationMessageDto;
import org.example.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST-контроллер для отправки email-уведомлений пользователям.
 * <p>Позволяет отправить письмо с уведомлением напрямую через HTTP API.</p>
 */
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Отправить уведомление пользователю на email через HTTP API.
     *
     * @param dto DTO с информацией об операции и email пользователя
     * @return HTTP 200 OK, если письмо отправлено
     */
    @PostMapping
    public ResponseEntity<Void> send(@RequestBody @Valid NotificationMessageDto dto) {
        String text = dto.getOperation().equalsIgnoreCase("CREATE")
                ? "Здравствуйте! Ваш аккаунт на сайте был успешно создан."
                : "Здравствуйте! Ваш аккаунт был удалён.";
        emailService.send(dto.getEmail(), text);
        return ResponseEntity.ok().build();
    }
}
