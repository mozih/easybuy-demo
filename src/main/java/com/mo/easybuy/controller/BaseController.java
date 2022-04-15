package com.mo.easybuy.controller;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ICaptcha;
import com.mo.easybuy.pojo.User;
import com.mo.easybuy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mo
 * @since 2022-03-03
 */
@RestController
public class BaseController {
    @Autowired
    private UserService userService;

    //获取验证码
    @RequestMapping("/getCode")
    public void getCode(HttpServletResponse response, HttpSession session) throws IOException {
        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        ICaptcha captcha = CaptchaUtil.createLineCaptcha(200, 100,4,100);
        ServletOutputStream outputStream = response.getOutputStream();
        session.setAttribute("vrifyCode",captcha.getCode());
        captcha.write(outputStream);
        outputStream.close();
    }
    @RequestMapping("/getUserName")
    public String getUserName(String userName){
        User user = this.userService.selectUserByuserName(userName);
        if (null != user){
            return "1";
        }
        return "0";
    }
    @RequestMapping("/getEmail")
    public String getEmail(String email){
        User user = this.userService.selectUserByEmail(email);
        if (null != user){
            return "1";
        }
        return "0";
    }
}
