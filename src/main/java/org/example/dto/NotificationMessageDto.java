package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) для передачи уведомлений о событиях с пользователями через Kafka.
 * <p>
 * Используется для передачи информации о типе операции (создание/удаление пользователя)
 * и email, на который будет отправлено уведомление.
 * </p>
 */
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessageDto {
    private String operation;
    private String email;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
