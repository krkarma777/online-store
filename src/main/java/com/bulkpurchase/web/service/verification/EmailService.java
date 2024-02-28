package com.bulkpurchase.web.service.verification;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.entity.user.VerificationToken;
import com.bulkpurchase.domain.enums.UserStatus;
import com.bulkpurchase.domain.repository.VerificationTokenRepository;
import com.bulkpurchase.domain.service.VerificationTokenService;
import com.bulkpurchase.domain.service.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final VerificationTokenService verificationTokenService;
    private final UserService userService;

    public void sendVerificationEmail(User user) throws MessagingException {
        String token = createVerificationToken(user).getToken();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("admin@seed.com");
        helper.setTo(user.getEmail());
        helper.setSubject(user.getRealName() + "님, Seed 인증 메일입니다.");

        String mailContent =
                "<html>" +
                        "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                        "   <div style='max-width: 600px; margin: 20px auto; border: 1px solid #ddd; padding: 20px;'>" +
                        "       <h2 style='color: #007bff; text-align: center;'>Seed 회원 인증</h2>" +
                        "       <p style='font-size: 16px;'>" +
                        "           안녕하세요, <strong>" + user.getRealName() + "</strong>님! Seed에 오신 것을 환영합니다.<br>" +
                        "           계정 활성화를 위해 아래의 버튼을 클릭해 주세요." +
                        "       </p>" +
                        "       <div style='text-align: center; margin: 40px 0;'>" +
                        "           <a href='http://localhost:9090/verify/result?token=" + token + "'" +
                        "              style='display: inline-block; background-color: #007bff; color: #ffffff; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>" +
                        "               계정 활성화" +
                        "           </a>" +
                        "       </div>" +
                        "       <p style='font-size: 14px; color: #666;'>" +
                        "           만약 위의 버튼이 작동하지 않는다면, 아래의 링크를 복사하여 브라우저에 붙여넣기 해주세요:<br>" +
                        "           <a href='http://localhost:9090/verify/result?token=" + token + "' style='color: #007bff;'>http://localhost:9090/verify/result?token=" + token + "</a>" +
                        "       </p>" +
                        "   </div>" +
                        "   <footer style='text-align: center; font-size: 12px; color: #777;'>" +
                        "       이 메일은 Seed 회원가입 과정 중 생성되었습니다. 문의사항이 있는 경우, 고객 지원 센터에 연락해 주세요.<br>" +
                        "       &copy; Seed All Rights Reserved" +
                        "   </footer>" +
                        "</body>" +
                        "</html>";

        helper.setText(mailContent, true); // true를 설정하여 HTML 컨텐츠로 인식하도록 합니다.

        mailSender.send(message);
    }

    public VerificationToken createVerificationToken(User user) {
        VerificationToken myToken = new VerificationToken();
        myToken.setUser(user);
        String token = UUID.randomUUID().toString();
        myToken.setToken(token);
        myToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // 24시간 후 만료
        return verificationTokenService.save(myToken);
    }

    public boolean verifyToken(String token) {
        VerificationToken verificationToken = verificationTokenService.findByToken(token).orElse(null);
        if (verificationToken == null || verificationToken.isExpired()) {
            return false;
        }
        User user = verificationToken.getUser();
        user.setStatus(UserStatus.ACTIVE);
        userService.save(user);
        verificationTokenService.delete(verificationToken);
        return true;
    }
}
