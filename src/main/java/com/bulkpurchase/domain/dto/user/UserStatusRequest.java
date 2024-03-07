package com.bulkpurchase.domain.dto.user;

import com.bulkpurchase.domain.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserStatusRequest {
    private Long userID;
    private UserStatus status;
}
