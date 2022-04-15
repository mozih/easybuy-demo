package com.mo.easybuy.controller;


import com.mo.easybuy.pojo.User;
import com.mo.easybuy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mo
 * @since 2022-02-28
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    //跳转到登录页面
    @RequestMapping("/login")
    public String login(){
        return "login";
    }
    //登录的具体方法
    @RequestMapping("/doLogin")
    public String doLogin(String username,
                          String userPassword,
                          String valideCode,
                          Integer rem_u,
                          HttpSession session,
                          Model model){
        //校验验证码
        String rancomCode = (String) session.getAttribute("vrifyCode");
        if (!rancomCode.equals(valideCode)){
            //验证码不正确
            model.addAttribute("errorMsg","验证码不正确");
            return "login";
        }
        User user = this.userService.selectUserByNameAndPass(username,userPassword);
        //查询到了账号和密码
        if (user != null){
            //判断是否下次自动登录
            if (rem_u == 1){
                //下次自动登录操作
            }
            //将用户存到session域中
            session.setAttribute("user",user);
            return "redirect:/index";
        }
        model.addAttribute("errorMsg", "账号或密码不正确");
        return "login";
    }

//    @RequestMapping("/user")
//    @ResponseBody
//    public String findAllUser(){
//        return this.userService.queryAllUser().toString();
//    }

//********************注册**********************************
    @RequestMapping("/toregister")
    public String toregisiter(){
        return "register";
    }

    @RequestMapping("/registerUser")
    public String reregisterUser(User user, Model model){
        System.out.println(user);
        if (this.userService.registerUser(user) >= 1){
            model.addAttribute("errorMsg","注册成功");
        }else {
            model.addAttribute("errorMsg","注册失败");
        }
        return "successPage";
    }
//*******************忘记密码************************************
    @RequestMapping("/forgetPassword")
    public String forgetPassword(){
        return "forgetPassword";
    }

    @RequestMapping("/updatePassword")
    public String updatePassword(User user, String checkCode,HttpSession session,Model model){
//        System.out.println(user);
        //校验验证码
        String rancomCode = (String) session.getAttribute("vrifyCode");
        if (!rancomCode.equals(checkCode)){
            //验证码不正确
            model.addAttribute("errorMsg","验证码不正确");
            return "forgetPassword";
        }
        if (userService.updateUserByEmail(user) >=1){
            model.addAttribute("errorMsg", "修改密码成功");
            return "successPage";
        }else {
            model.addAttribute("errorMsg", "修改密码失败");
            return "successPage";
        }
    }
    //*******************登出账号**********************
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "index";
    }
}
