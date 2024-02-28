package com.bulkpurchase.web.service.verification;

import com.bulkpurchase.domain.entity.user.VerificationToken;
import com.bulkpurchase.domain.service.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class EmailTokenManageService {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final VerificationTokenService verificationTokenService;

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        LocalDateTime currentTime = LocalDateTime.now();
        String formattedTime = currentTime.format(formatter);
        System.out.println("현재 시간: " + formattedTime);

        verificationTokenService.deleteExpiredTokens();

    }
}