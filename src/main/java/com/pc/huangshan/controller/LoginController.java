package com.pc.huangshan.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.pc.huangshan.dao.GoodsInfoMapper;
import com.pc.huangshan.dao.ParkInfoMapper;
import com.pc.huangshan.dao.UserInfoMapper;
import com.pc.huangshan.model.ParkInfo;
import com.pc.huangshan.model.UserInfo;
import com.pc.huangshan.utlis.Md5Util;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/login")
public class LoginController {

    @Resource
    UserInfoMapper userInfoMapper;

    @RequestMapping("welcome")
    public String test(Model model){
//        return "new_login";
        return "login";
    }


    @RequestMapping("loginOut")
    public String loginOut(Model model,HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return "login";
    }

    @RequestMapping("checkLogin")
    @ResponseBody
    public Map<String,Object> checkLogin(Model model, HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        String username = request.getParameter("username");
        String userpassword = request.getParameter("userpassword");
        if(StringUtil.isEmpty(username)||StringUtil.isEmpty(userpassword)){
            map.put("code","999");
            map.put("msg","账号密码不能为空");
            return map;
        }
        UserInfo userInfo = userInfoMapper.findByUserName(username);
        if(userInfo==null){
            map.put("code","999");
            map.put("msg","账号不存在");
            return map;
        }
        String accPass = userInfo.getAccPass();
        if(StringUtil.isNotEmpty(accPass)&&accPass.equals(Md5Util.md5(userpassword))){
            request.getSession().setAttribute("user",userInfo);
            map.put("code","0");
            return map;
        }else{
            map.put("code","999");
            map.put("msg","账号密码不正确");
            return map;
        }

    }

}
