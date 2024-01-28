package com.bulkpurchase.domain.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {
    private Long userId;
    private String username;
    private String email;
    private String password;
    private String userType;
    private String userGrade;
    private Date createDate;
}
