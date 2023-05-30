package com.liyaoyun.usercentre;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.liyaoyun.usercentre.mapper")
public class UserCentreApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCentreApplication.class, args);
    }

}
