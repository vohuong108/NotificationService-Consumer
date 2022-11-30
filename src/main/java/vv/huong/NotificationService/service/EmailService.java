package vv.huong.NotificationService.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import vv.huong.NotificationService.model.MessageDTO;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

public interface EmailService {
    void sendEmail(MessageDTO messageDTO);
}

@Service
@RequiredArgsConstructor
@Slf4j
class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    @Async
    public void sendEmail(MessageDTO messageDTO) {
        try {
            log.info("START... SENDING EMAIL");

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());

            //load template email with content
            Context context = new Context();
            context.setVariable("name", messageDTO.getToName());
            context.setVariable("content", messageDTO.getContent());
            String html = templateEngine.process("welcome-email", context);

            //send email
            helper.setTo(messageDTO.getTo());
            helper.setText(html, true);
            helper.setSubject(messageDTO.getSubject());
            helper.setFrom(from);
            javaMailSender.send(message);

            log.info("END... EMAIL SENT SUCCESS");
        } catch (Exception ignored) {
            log.error("ERROR: {}", ignored.getMessage());
        }


    }
}
