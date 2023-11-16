package com.example.demo.service.impl;

import com.example.demo.model.Joke;
import com.example.demo.repository.JokeRepository;
import com.example.demo.service.EMailService;
import com.example.demo.service.FormattingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
public class EMailServiceImpl implements EMailService {

    private final JokeRepository jokeRepository;
    private final FormattingService formattingService;
    private final JavaMailSender mailSender;
    private final String fromEmail;
    private final String toEmail;

    public EMailServiceImpl(JavaMailSender mailSender,
                            JokeRepository jokeRepository,
                            FormattingService formattingService,
                            @Value("${spring.mail.username}") String fromEmail,
                            @Value("${email-service.recipient}") String toEmail) {
        this.mailSender = mailSender;
        this.jokeRepository = jokeRepository;
        this.formattingService = formattingService;
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
    }

    @Override
    public void send(Integer id) {
        Joke joke = new Joke(jokeRepository.getReferenceById(id));
        sendEmail(joke);
    }

    @Override
    public void send(Joke joke) {
        sendEmail(joke);
    }

    private void sendEmail(Joke joke) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Joke service test");
            helper.setText(formattingService.beautifyJoke(joke));
            mailSender.send(message);
        } catch (MessagingException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
