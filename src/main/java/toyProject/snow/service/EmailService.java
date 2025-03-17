package toyProject.snow.service;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    private EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void sendTemporaryPasswordByEmail(String toEmail, String tempPassword){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Snowball 임시 비밀번호 발급");
        message.setText("임시 비밀번호 : " + tempPassword + "\n 로그인 후 비밀번호를 변경해주세요");

        mailSender.send(message);
    }
}
