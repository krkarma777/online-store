package com.bulkpurchase.web.controller;

import com.bulkpurchase.domain.service.user.RewardPointService;
import com.bulkpurchase.domain.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class RewardPointController {

    private final UserService userService;
    private final RewardPointService rewardPointService;

//    @GetMapping("/reward/insert")
//    public String
}
