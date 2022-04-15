package com.mo.easybuy.config;

import com.mo.easybuy.interceptor.MyIntercept;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * author mozihao
 * create 2022-03-07 23:49
 * Description
 */
@Configuration
public class LoginConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new MyIntercept());
        System.out.println("拦截器注册成功！！！");
        registration.addPathPatterns("/**");                      //所有http://localhost:8080/easybuy路径都被拦截,/不会拦截
        registration.excludePathPatterns(                         //添加不拦截路径
                "/getCode",          //验证码
                "/toregister",       //去注册
                "/registerUser",      //注册处理
                "/forgetPassword",     //忘记密码
                "/updatePassword",     //处理忘记密码
                "/login",            //登录
                "/doLogin",    //处理登录
                "/doLoginSave",//处理登录
                "/getUserName",//通过用户名查询用户信息
                "/getEmail",//通过email查询用户信息
                "/**/*.html",            //html静态资源
                "/**/*.js",              //js静态资源
                "/**/*.css",             //css静态资源
                "/**/*.woff",
                "/**/*.ttf",
                "/images/*"
        );
    }
}
