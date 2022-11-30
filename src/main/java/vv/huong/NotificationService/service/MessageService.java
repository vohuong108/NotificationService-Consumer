package vv.huong.NotificationService.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import vv.huong.NotificationService.model.MessageDTO;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {
    private final EmailService emailService;

    @KafkaListener(id = "notificationGroup", topics = "notification")
    public void listen(MessageDTO messageDTO) {
        log.info("Received: {}", messageDTO.getTo());
        emailService.sendEmail(messageDTO);
    }
}
