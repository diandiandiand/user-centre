package com.liyaoyun.usercentre.service;

import com.liyaoyun.usercentre.pojo.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class UserServiceTest {
    @Resource
    private UserService userService;
    @Test
    public void testadduser(){  //添加用户测试
        User user = new User();
        user.setUserName("liyaoyun");
        user.setUserAccount("123456");
        user.setAvatarUrl("E:\\IDEA\\user-centre\\src\\main\\java\\com\\liyaoyun\\usercentre\\avater\\1.jpg");
        user.setGender(0);
        user.setUserPassword("111");
        user.setPhone("187");
        user.setEmail("198");


        boolean save = userService.save(user);
        Assertions.assertTrue(save);
        System.out.println(save);

    }

    @Test
    void userregister() {//注册测试
        String userAccount="diandian";
        String password="12345678";
        String checkpassword="12345678";
        long result = userService.userregister(userAccount, password, checkpassword);
//        Assertions.assertEquals(-1,result);
        System.out.println(result);
    }
}