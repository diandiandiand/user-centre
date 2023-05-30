package com.liyaoyun.usercentre.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyaoyun.usercentre.common.ErrorCode;
import com.liyaoyun.usercentre.constant.UserConstant;
import com.liyaoyun.usercentre.exception.BusinessException;
import com.liyaoyun.usercentre.pojo.domain.User;
import com.liyaoyun.usercentre.service.UserService;

import com.liyaoyun.usercentre.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author ASUS
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-04-12 19:32:12
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService , UserConstant {
    @Resource
    UserMapper userMapper;   //  注入接口

    //对密码进行加密加盐,校验密码的时候需要加上盐,再进行校验
    private static final  String salt="li";



    //注册用户
    @Override
    public long userregister(String userAccount, String userPassword, String checkPassword) {


        //进行账号进行校验
        if(StringUtils.isAllBlank(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if(userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        }
        if (userPassword.length()<8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
        }

        //校验特殊字符
           //这是不可以被创建的字符类型
        String validpattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validpattern).matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"含有特殊字符");
        }

        //校验两次密码是否相同
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不相同");
        }
        //账号不能重复
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        QueryWrapper<User> userAccount1 = userQueryWrapper.eq("userAccount", userAccount);
        long count = this.count(userAccount1);
        if (count>0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号重复");
        }


        String encryptPassword = DigestUtils.md5DigestAsHex((salt + userPassword).getBytes());

        //插入数据,将注册的用户存入数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean save = this.save(user);//保存到数据库
        if (!save){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"存入数据库失败");
        }


        return user.getId();
    }




    @Override
    public User userlogin(String userAccount, String userPassword, HttpServletRequest Request) {
        //进行账号进行校验
        if(StringUtils.isAllBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if(userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号长度过短");
        }
        if (userPassword.length()<8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码过短");
        }

        //校验特殊字符
        //这是可以被创建的字符类型
        String validpattern = "[\\u00A0\\s\"`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validpattern).matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"含有特殊字符");
        }

        //密码进行加密加盐
        String encryptPassword = DigestUtils.md5DigestAsHex((salt + userPassword).getBytes());
        //用户是否存在
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("userAccount", userAccount);
        //到数据库进行查询userAccount字段,再与形参userAccount进行比较,相等的存入userQueryWrapper
        userQueryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(userQueryWrapper);
        if (user==null){
            log.info("登录失败，没有找到用户");
            throw new BusinessException(ErrorCode.NULL_ERROR,"没有找到用户");
        }

        User getsafetyUser = getsafetyUser(user);

//记录用户的登录态,存了登录态之后,我们可以通过session查询到这个用户的相关信息,如属性信息
        Request.getSession().setAttribute(USER_LOGIN_KEY,getsafetyUser);
        return getsafetyUser;
    }

    @Override
    public int userlogout(HttpServletRequest request) {
            request.getSession().removeAttribute(USER_LOGIN_KEY);
            return 1;
    }


    @Override
    public User getsafetyUser(User user){
        //用户脱敏
        if (user==null){
            throw new BusinessException(ErrorCode.NULL_ERROR,"用户为空");
        }
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUserName(user.getUserName());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(0);
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setUserRole(user.getUserRole());
        return safetyUser;
    }

}




