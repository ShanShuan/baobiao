package com.pc.huangshan.sercrity;

import com.github.pagehelper.StringUtil;
import com.pc.huangshan.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Component
public class AdminLoginInterceptor implements HandlerInterceptor {
    private static final String TOKEN_KEY = "token";
    Logger logger= LoggerFactory.getLogger(AdminLoginInterceptor.class);
    //    在请求处理之前调用,只有返回true才会执行请求
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
//        得到session
        HttpSession session = httpServletRequest.getSession(true);
        logger.info("【uri】--"+httpServletRequest.getRequestURI());
        logger.info("【url】--"+httpServletRequest.getRequestURL());
//        得到对象
        UserInfo user = (UserInfo) session.getAttribute("user");
//        判断对象是否存在
        if(user!=null){
            return true;
        }else{

//            String token = getToken(httpServletRequest);
//            if(StringUtil.isEmpty(token)){
//                不存在则跳转到登录页
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/login/welcome");
                return false;
//            }
        }
    }

    //    试图渲染之后执行
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }



    /**
     * 根据参数或者header获取token
     *
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        String token = request.getParameter(TOKEN_KEY);
        if (StringUtils.isEmpty(token)) {
            token = request.getHeader(TOKEN_KEY);
        }
        if (StringUtils.isEmpty(token)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (TOKEN_KEY.equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }
        return token;
    }
    //    在请求处理之后,视图渲染之前执行
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

}
