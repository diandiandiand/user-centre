package com.liyaoyun.usercentre.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liyaoyun.usercentre.common.BaseResponse;
import com.liyaoyun.usercentre.common.ErrorCode;
import com.liyaoyun.usercentre.common.Resultutils;
import com.liyaoyun.usercentre.constant.UserConstant;
import com.liyaoyun.usercentre.exception.BusinessException;
import com.liyaoyun.usercentre.pojo.domain.User;
import com.liyaoyun.usercentre.pojo.domain.request.UserLoginRequest;
import com.liyaoyun.usercentre.pojo.domain.request.UserRegisterRequset;
import com.liyaoyun.usercentre.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class Usercontroller implements UserConstant {
    @Resource
    private UserService userService;  //注入接口


    @PostMapping("/register")    //网址请求到这里
    public BaseResponse<Long> func(@RequestBody UserRegisterRequset userRegisterRequset){
                                  //@RequestBody表示会将请求体赋值给userRegisterRequset,请求体是表单的信息
        if(userRegisterRequset==null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求为空");
        }

        long userregister = userService.userregister(userRegisterRequset.getUserAccount(), userRegisterRequset.getUserPassword(), userRegisterRequset.getCheckPassword());
//        return new BaseResponse<>(0,userregister,"ok");

        return Resultutils.success(userregister);
    }


    @PostMapping("/login")
    public BaseResponse<User> func2(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest Request){
        //@RequestBody表示会将请求体赋值给userLoginRequest
        if(userLoginRequest==null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求为空");
        }
        User userlogin = userService.userlogin(userLoginRequest.getUserAccount(), userLoginRequest.getUserPassword(), Request);
        return Resultutils.success(userlogin);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> func3( HttpServletRequest Request){
        if (Request==null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"请求为空");
        }
        int userlogout = userService.userlogout(Request);//永远返回1
        return Resultutils.success(userlogout);
    }


    @GetMapping("/search")  //查询用户，仅管理员可以操作
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request){
        if (!isadmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH,"不是管理员,没有权限");
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)){
            userQueryWrapper.like("username",username);   //模糊查询
        }

        List<User> list = userService.list(userQueryWrapper);
        //以下代码是表示将list里的setUserPassword设为空,因为从数据库查询到的user密码还是会发送到前端
        List<User> collect = list.stream().map(user -> {
            user.setUserPassword(null);
            return userService.getsafetyUser(user);
        }).collect(Collectors.toList());
        return Resultutils.success(collect);
    }



    @PostMapping("/delete")   //仅管理员操作
    public BaseResponse<Boolean> deleteuser(@RequestBody int id,HttpServletRequest request){
        if (!isadmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH,"不是管理员,没有权限");
        }
        if (id<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"没有该用户");
        }
        boolean b = userService.removeById(id);
        return Resultutils.success(b);
    }


    @GetMapping("/current")
    public BaseResponse<User> getcurrentuser(HttpServletRequest Request){
        Object attribute = Request.getSession().getAttribute(USER_LOGIN_KEY);
        User currentuser = (User) attribute;
        if (currentuser==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN,"请先登录");
        }
        Long id = currentuser.getId();
        User dbuser = userService.getById(id);
        User getsafetyUser = userService.getsafetyUser(dbuser);
        return Resultutils.success(getsafetyUser);
    }





    private boolean isadmin(HttpServletRequest request){
        /**
         * 是否为管理员
         */
        Object userobj = request.getSession().getAttribute(USER_LOGIN_KEY);
        User user=(User) userobj;
        //判断是不是管理员
        if (user==null|| user.getUserRole()==default_role){
            throw new BusinessException(ErrorCode.NO_AUTH,"不是管理员,没有权限");
        }
        return true;
    }

}
