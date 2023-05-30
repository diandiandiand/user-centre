package com.liyaoyun.usercentre.pojo.domain.request;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String userAccount;
    private String userPassword;

}
