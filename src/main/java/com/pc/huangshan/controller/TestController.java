package com.pc.huangshan.controller;

import com.pc.huangshan.dao.GoodsInfoMapper;
import com.pc.huangshan.dao.ParkInfoMapper;
import com.pc.huangshan.model.GoodsInfo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pc.huangshan.model.ParkInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class TestController {

    @Resource
    GoodsInfoMapper goodsInfoMapper;
    @Resource
    ParkInfoMapper parkInfoMapper;

    @RequestMapping("test")
    public String test(Model model){
        model.addAttribute("msg","我是个测试信息");
        return "test";
    }

    @RequestMapping("xx")
    public String xx(Model model){
        model.addAttribute("msg","我是个测试信息");
        return "layui_test";
    }

    @RequestMapping("hh")
    public String hh(Model model){
        model.addAttribute("msg","我是个测试信息");
        return "hh";
    }

    @RequestMapping("goods")
    @ResponseBody
    public Map<String,Object> goods(int page, int limit){

        //在查询操作之前使用PageHelper进行分页，传入页码以及分页的大小
        PageHelper.startPage(page,limit);

        //在startPage后面紧跟的查询是一个分页查询
        List<ParkInfo> list = new ArrayList<>();
        list=parkInfoMapper.selectAll();
//        list=goodsInfoMapper.selectAll();
//        if(type.equals("0"))
//            list = userService.selectByUserName(content);
        //查询之后使用PageInfo对象对数据进行包装
        PageInfo pageInfo = new PageInfo(list);
        Map<String,Object> map = new HashMap<>();
        map.put("code",0);
        map.put("msg","操作成功");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        System.out.println(map);
        return map;
    }
}
