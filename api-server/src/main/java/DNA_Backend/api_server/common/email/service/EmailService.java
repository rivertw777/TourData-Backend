package DNA_Backend.api_server.common.email.service;

import static DNA_Backend.api_server.common.email.exception.EmailExceptionMessage.SEND_MAIL_FAILED;

import DNA_Backend.api_server.common.exception.DnaApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;

    public void sendEmail(String email, String subject, String text) {
        SimpleMailMessage emailForm = createEmailForm(email, subject, text);
        try {
            emailSender.send(emailForm);
        } catch (RuntimeException e) {
            throw new DnaApplicationException(SEND_MAIL_FAILED.getMessage());
        }
    }

    private SimpleMailMessage createEmailForm(String email, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);
        return message;
    }

}
