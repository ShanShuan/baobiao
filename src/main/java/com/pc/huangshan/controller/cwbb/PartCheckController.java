package com.pc.huangshan.controller.cwbb;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.pc.huangshan.Mapper.CwbbMapper;
import com.pc.huangshan.Mapper.PartCheckMapper;
import com.pc.huangshan.model.cwbb.PartCheckDO;
import com.pc.huangshan.model.cwbb.PartCheckStatisticsDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 部分检票
 */
@Controller
@RequestMapping("/part_check")

public class PartCheckController {
    Logger logger= LoggerFactory.getLogger(PartCheckController.class);
    @Resource
    PartCheckMapper partCheckMapper;


    @RequestMapping("/show")
    public String show(Model model){
        model.addAttribute("1","22");
        return "cw/part_check";
    }


    @RequestMapping("/statistics")
    public String statistics(Model model){
        model.addAttribute("1","22");
        return "cw/part_check_statistics";
    }


    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> checkLogin(Model model, HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        String st = request.getParameter("startTime");
        String et = request.getParameter("endTime");
        String orderNo = request.getParameter("orderNo");
        int pageNo = Integer.parseInt(request.getParameter("page"));
        int pageSize = Integer.parseInt(request.getParameter("limit"));
        if(StringUtil.isEmpty(st)&&StringUtil.isEmpty(orderNo)){
            map.put("code","999");
            map.put("msg","请选择开始时间");
            return map;
        }
        String startTime="";
        if(!StringUtil.isEmpty(st)){
            startTime=st+" 00:00:00";
        }
        String endTime="";
        if(!StringUtil.isEmpty(et)){
            endTime=et+" 23:59:59";
        }

        PageHelper.startPage(pageNo,pageSize);
        List<PartCheckDO> list =partCheckMapper.list(startTime,endTime,orderNo);
        PageInfo<PartCheckDO> pageInfo = new PageInfo<>(list);
        map.put("code",0);
        map.put("msg","操作成功");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    @RequestMapping("/listStatistics")
    @ResponseBody
    public Map<String,Object> listStatistics(Model model, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        String st = request.getParameter("startTime");
        String et = request.getParameter("endTime");
        if (StringUtil.isEmpty(st) || StringUtil.isEmpty(et)) {
            map.put("code", "999");
            map.put("msg", "请选择开始时间和结束时间");
            return map;
        }
        String startTime = st + " 00:00:00";
        String endTime = et + " 23:59:59";
        List<PartCheckStatisticsDO> list=partCheckMapper.listStatistics(startTime,endTime);
        map.put("code",0);
        map.put("msg","操作成功");
        map.put("count",list.size());
        map.put("data",list);
        return map;
    }
}
