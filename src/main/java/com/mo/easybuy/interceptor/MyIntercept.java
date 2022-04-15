package com.mo.easybuy.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @date 2021/12/21 - 19:51
 */
//创建拦截器，拦截控制器方法的执行
@Component
public class MyIntercept implements HandlerInterceptor{

    /**
     * SpringMVC中的拦截器有三个抽象方法：
     *
     * preHandle：控制器方法执行之前执行preHandle()，其boolean类型的返回值表示是否拦截或放行，返回true为放行，即调用控制器方法；返回false表示拦截，即不调用控制器方法
     *
     * postHandle：控制器方法执行之后执行postHandle()
     *
     * afterComplation：处理完视图和模型数据，渲染视图完毕之后执行afterComplation()
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//        String uri = request.getRequestURI();
//        if (uri.equals("/easybuy") || uri.equals("/login") || uri.equals("/doLogin")){
//            return true;
//        }

        Object user = request.getSession().getAttribute("user");
        System.out.println("拦截成功");
        if (user != null){
            System.out.println("-------------------成功跳转");
            return true;
        }
        System.out.println(request.getContextPath());
//        System.out.println(request.getRequestURI());
//        System.out.println("-------------------到登录去");
        response.sendRedirect(request.getContextPath()+"/login");
        return false;
    }
}
