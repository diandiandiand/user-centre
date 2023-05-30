package com.liyaoyun.usercentre.pojo.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.util.PrimitiveIterator;

/**
 * 返回给前端的对象
 */
@Data
public class UserRegisterRequset  implements Serializable{

    private String userAccount;
    private String userPassword;
    private String checkPassword;



}
