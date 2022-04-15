package com.mo.easybuy.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 开启异常
 * author mozihao
 * create 2022-03-04 17:03
 * Description
 */
@Component
public class MyHandlerException implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMsg","未知异常");
        modelAndView.setViewName("errorPage");
        System.out.println("捕获到异常");
        return modelAndView;
    }
}
