package com.pc.huangshan.controller;

import com.pc.huangshan.model.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/index")
public class IndexController {



    @RequestMapping("/home")
    public String xx(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        UserInfo userInfo= (UserInfo) session.getAttribute("user");
        model.addAttribute("name",userInfo.getRealName());
        return "home";
    }


}
