package com.pc.huangshan.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.huangshan.dao.GoodsInfoMapper;
import com.pc.huangshan.dao.ParkInfoMapper;
import com.pc.huangshan.model.ParkInfo;
import com.pc.huangshan.model.UserInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
