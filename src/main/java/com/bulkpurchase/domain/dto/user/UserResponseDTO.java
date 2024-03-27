package com.bulkpurchase.domain.dto.user;

import com.bulkpurchase.domain.entity.user.User;
import com.bulkpurchase.domain.enums.UserRole;
import com.bulkpurchase.domain.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserResponseDTO {


    private Long userID;

    private String username;

    private String email;

    private String realName;

    private String password;

    private UserRole role;

    private String userGrade;

    private String phoneNumber;

    private String address;

    private String detailAddress;

    private String zipCode;

    private Date createDate;

    private UserStatus status;

    public UserResponseDTO(User user) {
        this.userID = user.getUserID();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.realName = user.getRealName();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.userGrade = user.getUserGrade();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.detailAddress = user.getDetailAddress();
        this.zipCode = user.getZipCode();
        this.createDate = user.getCreateDate();
        this.status = user.getStatus();
    }
}
