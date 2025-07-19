package org.example.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса {@link EmailService} для отправки email-уведомлений пользователям.
 * <p>
 * Использует {@link JavaMailSender} из Spring для отправки простых текстовых сообщений.
 * </p>
 */
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Отправляет простое текстовое письмо на указанный email с темой "Уведомление".
     *
     * @param to   адрес электронной почты получателя
     * @param text текст письма
     */
    @Override
    public void send(String to, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Уведомление");
        msg.setText(text);
        mailSender.send(msg);
    }
}
