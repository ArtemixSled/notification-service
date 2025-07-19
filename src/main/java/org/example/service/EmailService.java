package org.example.service;

/**
 * Сервис для отправки email-сообщений пользователям.
 * <p>
 * Абстракция над механизмом отправки почтовых уведомлений.
 * Реализации могут использовать SMTP, сторонние сервисы и т.д.
 * </p>
 */
public interface EmailService {
    void send(String to, String text);
}
