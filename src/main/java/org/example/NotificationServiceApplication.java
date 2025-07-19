package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Главный класс приложения Notification Service.
 * <p>
 * Запускает Spring Boot приложение, включающее интеграцию с Kafka для обработки событий уведомлений.
 * </p>
 *
 * <ul>
 *     <li>Аннотация {@link SpringBootApplication} включает автоматическую конфигурацию Spring Boot.</li>
 *     <li>Аннотация {@link EnableKafka} активирует поддержку Kafka Listener'ов.</li>
 * </ul>
 */
@SpringBootApplication
@EnableKafka
public class NotificationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
