package com.bulkpurchase.web.service.verification;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.entity.user.VerificationToken;
import com.bulkpurchase.domain.enums.UserStatus;
import com.bulkpurchase.domain.service.VerificationTokenService;
import com.bulkpurchase.domain.service.user.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
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
                        "   <div style='max-width: 600px; margin: 20px auto; border: 1px solid #ddd; padding: 20px; background-color: #f0f8f7;'>" +
                        "       <h2 style='color: #2e8b57; text-align: center;'>Seed 회원 인증</h2>" +
                        "       <p style='font-size: 16px; text-align: center;'>" +
                        "           안녕하세요, <strong>" + user.getRealName() + "</strong>님! Seed에 오신 것을 환영합니다.<br>" +
                        "           계정 활성화를 위해 아래의 버튼을 클릭해 주세요." +
                        "       </p>" +
                        "       <div style='text-align: center; margin: 40px 0;'>" +
                        "           <a href='http://localhost:9090/verify/result?token=" + token + "'" +
                        "              style='display: inline-block; background-color: #2e8b57; color: #ffffff; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>" +
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
    public void sendUserIdEmail(User user) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("admin@seed.com");
        helper.setTo(user.getEmail());
        helper.setSubject(user.getRealName() + "님, Seed 아이디 찾기 결과입니다.");

        String mailContent =
                "<html>" +
                        "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                        "   <div style='max-width: 600px; margin: 20px auto; border: 1px solid #ddd; padding: 20px;'>" +
                        "       <h2 style='color: #007bff; text-align: center;'>Seed 아이디 찾기</h2>" +
                        "       <p style='font-size: 16px;'>" +
                        "           안녕하세요, <strong>" + user.getRealName() + "</strong>님! 귀하의 Seed 아이디는 다음과 같습니다:<br>" +
                        "           <strong>" + user.getUsername() + "</strong><br>" +
                        "           보안을 위해 비밀번호는 공개하지 않으며, 비밀번호 찾기 기능을 이용해 주시길 바랍니다." +
                        "       </p>" +
                        "       <div style='text-align: center; margin: 40px 0;'>" +
                        "           <a href='http://localhost:9090/login'" +
                        "              style='display: inline-block; background-color: #007bff; color: #ffffff; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>" +
                        "               로그인 페이지로 이동" +
                        "           </a>" +
                        "       </div>" +
                        "   </div>" +
                        "   <footer style='text-align: center; font-size: 12px; color: #777;'>" +
                        "       이 메일은 Seed 아이디 찾기 요청 과정 중 생성되었습니다. 문의사항이 있는 경우, 고객 지원 센터에 연락해 주세요.<br>" +
                        "       &copy; Seed All Rights Reserved" +
                        "   </footer>" +
                        "</body>" +
                        "</html>";

        helper.setText(mailContent, true); // true를 설정하여 HTML 컨텐츠로 인식하도록 합니다.

        mailSender.send(message);
    }

    public VerificationToken createVerificationToken(User user) {
        VerificationToken myToken = null;
        String token = UUID.randomUUID().toString();
        Optional<VerificationToken> tokenOpt = verificationTokenService.findByUserUserID(user.getUserID());
        if (tokenOpt.isEmpty()) {
            myToken = new VerificationToken();
            myToken.setUser(user);
            myToken.setToken(token);
            myToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // 24시간 후 만료
        } else {
            myToken = tokenOpt.get();
            myToken.setToken(token);
            myToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // 24시간 후 만료
        }
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
