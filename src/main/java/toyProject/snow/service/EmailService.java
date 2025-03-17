package toyProject.snow.service;

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
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(toEmail);
//        message.setSubject("Snowball 임시 비밀번호 발급");
//        message.setText("임시 비밀번호 : " + tempPassword + "\n 로그인 후 비밀번호를 변경해주세요");
//
//        mailSender.send(message);


        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("knjou92@naver.com");  // 받을 이메일 주소
            message.setSubject("테스트 이메일");
            message.setText("이것은 테스트 이메일입니다.");

            mailSender.send(message);
            System.out.println("이메일 전송 성공!");
        } catch (Exception e) {
            System.out.println("이메일 전송 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
