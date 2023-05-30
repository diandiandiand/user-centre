package com.liyaoyun.usercentre.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.liyaoyun.usercentre.pojo.domain.User;

import javax.servlet.http.HttpServletRequest;

/**
* @author ASUS
* @description 针对表【user】的数据库操作Service
* @createDate 2023-04-12 19:32:12
*/
public interface UserService extends IService<User> {
    /**
     *
     * @param userAccount
     * @param userPassword
     * @param checkPassword   校验密码
     * @return  用户id
     */


        long userregister(String userAccount,String userPassword,String checkPassword);

        User userlogin(String userAccount,String userPassword,HttpServletRequest Request);

        int userlogout(HttpServletRequest request);

    User getsafetyUser(User user);
}
