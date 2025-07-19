package org.example.consumer;

import org.example.dto.NotificationMessageDto;
import org.example.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer для получения уведомлений о действиях с пользователями.
 * <p>
 * Получает сообщения из Kafka-топика "user-events" и отправляет email-письмо
 * пользователю в зависимости от типа операции (создание или удаление аккаунта).
 * </p>
 */
@Component
public class NotificationKafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(NotificationKafkaConsumer.class);

    private final EmailService emailService;

    public NotificationKafkaConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Метод-обработчик Kafka сообщений.
     * <p>
     * Получает объект {@link NotificationMessageDto} из Kafka, анализирует тип операции,
     * формирует текст уведомления и отправляет email соответствующему пользователю.
     * Если операция не поддерживается (не "CREATE" или "DELETE"), письмо не отправляется.
     * </p>
     *
     * @param dto сообщение из Kafka с информацией об операции и email пользователя
     */
    @KafkaListener(topics = "user-events")
    public void listen(NotificationMessageDto dto) {
        log.info("Consumed from Kafka: {}", dto);
        String op = dto.getOperation();
        String email = dto.getEmail();

        String text;
        if ("CREATE".equalsIgnoreCase(op)) {
            text = "Здравствуйте! Ваш аккаунт на сайте был успешно создан.";
        } else if ("DELETE".equalsIgnoreCase(op)) {
            text = "Здравствуйте! Ваш аккаунт был удалён.";
        } else {
            log.warn("Unknown operation '{}', skipping", op);
            return;
        }

        emailService.send(email, text);
        log.info("Sent email to {}", email);
    }
}
