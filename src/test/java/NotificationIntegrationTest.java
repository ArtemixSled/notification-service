import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.internet.MimeMessage;
import org.example.dto.NotificationMessageDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Интеграционный тест для проверки цепочки "Kafka → Email".
 * <p>
 * Тестирует функциональность notification-сервиса: при получении сообщения из Kafka
 * пользователю должно отправляться email-уведомление.
 * </p>
 *
 * <ul>
 *     <li>Поднимает контейнер Kafka через Testcontainers</li>
 *     <li>Использует GreenMail для эмуляции SMTP-сервера</li>
 *     <li>Тестирует отправку email по событию CREATE</li>
 * </ul>
 */
@SpringBootTest(classes = org.example.NotificationServiceApplication.class)
@Testcontainers
class NotificationIntegrationTest {

    @Container
    static KafkaContainer kafka = new KafkaContainer();

    static final GreenMail greenMail;

    static {
        greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();
    }

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> greenMail.getSmtp().getPort());
    }

    @Autowired
    KafkaTemplate<String, NotificationMessageDto> kafkaTemplate;

    @Test
    void whenKafkaMessage_thenEmailIsSent() throws Exception {
        NotificationMessageDto dto = new NotificationMessageDto("CREATE", "test@example.com");
        kafkaTemplate.send("user-events", dto);
        greenMail.waitForIncomingEmail(5000, 1);
        MimeMessage[] received = greenMail.getReceivedMessages();
        assertThat(received).hasSize(1);
        assertThat(received[0].getAllRecipients()[0].toString()).isEqualTo("test@example.com");
        assertThat(received[0].getSubject()).isEqualTo("Уведомление");
        assertThat(received[0].getContent().toString()).contains("успешно создан");
    }

    @AfterAll
    static void stopMail() {
        if (greenMail != null) greenMail.stop();
    }
}
