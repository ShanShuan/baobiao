package com.pc.huangshan.controller.cwbb;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.pc.huangshan.Mapper.OrderMapper;
import com.pc.huangshan.common.emnu.OrderTypeEnum;
import com.pc.huangshan.model.cwbb.OrderDO;
import com.pc.huangshan.model.cwbb.PartCheckDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 订单列表
 */
@Controller
@RequestMapping("/order")
@Slf4j
public class OrderController {
    private ExecutorService executorService = Executors.newFixedThreadPool(10);
    @Resource
    OrderMapper orderMapper;

    @RequestMapping("/show")
    public ModelAndView show(ModelAndView modelAndView){
        modelAndView.setViewName("cw/order_list");
        return modelAndView;
    }





    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> list(Model model, HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        String st = request.getParameter("startTime");
        String et = request.getParameter("endTime");
        int pageNo = Integer.parseInt(request.getParameter("page"));
        int pageSize = Integer.parseInt(request.getParameter("limit"));
        if(StringUtil.isEmpty(st)){
            map.put("code","999");
            map.put("msg","请选择游玩开始时间");
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
        List<OrderDO> list =orderMapper.list(startTime,endTime, null);
        PageInfo<OrderDO> pageInfo = new PageInfo<>(list);
        map.put("code",0);
        map.put("msg","操作成功");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * @return
     * @name 风险预警  -- 预警监控  --- 导出接口
     */
    @RequestMapping("/exportTable")
    @ResponseBody
    public String  exportTable(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Map<String,Object> map=new HashMap<>();
        String st = request.getParameter("startTime");
        String et = request.getParameter("endTime");
        String title = request.getParameter("title");
        if(StringUtil.isEmpty(st)){
            map.put("code","999");
            map.put("msg","请选择游玩开始时间");
            return null;
        }
        String startTime="";
        if(!StringUtil.isEmpty(st)){
            startTime=st+" 00:00:00";
        }
        String endTime="";
        if(!StringUtil.isEmpty(et)){
            endTime=et+" 23:59:59";
        }

        String finalStartTime = startTime;
        String finalEndTime = endTime;
        Future<List<OrderDO>> ticketFuture = executorService.submit(() -> getList(finalStartTime, finalEndTime, OrderTypeEnum.TICKET.getName()));
        Future<List<OrderDO>> hotelFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.HOTEL.getName()));
        Future<List<OrderDO>> shopFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.SHOP.getName()));

        Future<List<OrderDO>> repastFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.REPAST.getName()));

        Future<List<OrderDO>> comboFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.COMBO.getName()));
        Future<List<OrderDO>> carFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.CAR.getName()));
        Future<List<OrderDO>> busFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.BUS.getName()));
        Future<List<OrderDO>> guideFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.GUIDE.getName()));
        Future<List<OrderDO>> routeFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.ROUTE.getName()));
        List<OrderDO> cwbbs=new ArrayList<>();
        try {
            cwbbs.addAll(ticketFuture.get());
            cwbbs.addAll(shopFuture.get());
            cwbbs.addAll(repastFuture.get());
            cwbbs.addAll(hotelFuture.get());
            cwbbs.addAll(comboFuture.get());
            cwbbs.addAll(carFuture.get());
            cwbbs.addAll(busFuture.get());
            cwbbs.addAll(guideFuture.get());
            cwbbs.addAll(routeFuture.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
        SXSSFWorkbook sxwb = null;
        FileOutputStream out = null;
        String dateTime = new Date().getTime()+"";
        String fileName = "order_" + dateTime + ".xlsx";
        try {
            sxwb = exportTable(cwbbs,title);
            out= new FileOutputStream("E://"+fileName);
            sxwb.write(out);
            out.flush();//刷新
            out.close();//关闭
            sxwb.close();
            return fileName;
        } catch (IOException e) {
            log.error("订单导出报告--导出失败：", e);
            return null;
        } finally {
            try {
               if(sxwb!=null) sxwb.close();
               if(out!=null) out.close();
            } catch (Exception ignored) {

            }
        }
    }

    @RequestMapping(value = "/downLoad")
    public ResponseEntity<byte[]>  downLoad(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String method = request.getMethod();
        log.info("下载文件请求方法："+method);
        String name = request.getParameter("name");
        if(StringUtil.isEmpty(name)){
            return null;
        }
        FileInputStream inputStream=null;
        ByteArrayOutputStream out = null;
        File file = new File("E://" + name);
        try {
            //获取要下载的文件输入流
            inputStream = new FileInputStream(file);
            int len = 0;
            //创建数据缓冲区
            byte[] buffer = new byte[1024];
            //通过response对象获取outputStream流
            out = new ByteArrayOutputStream();
            //将FileInputStream流写入到buffer缓冲区
            while((len = inputStream.read(buffer)) > 0) {
                //使用OutputStream将缓冲区的数据输出到浏览器
                out.write(buffer,0,len);
            }
            //这一步走完，将文件传入OutputStream中后，页面就会弹出下载框

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        } finally {
            try {
                if (out != null)
                    out.close();
                if(inputStream!=null)
                    inputStream.close();
//                if(file!=null)file.delete();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }

        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", URLEncoder.encode(name, "UTF-8"));

        return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.CREATED);
    }

//    /**
//     * @return
//     * @name 风险预警  -- 预警监控  --- 导出接口
//     */
//    @RequestMapping("/exportTable")
//    @ResponseBody
//    public Map<String,Object>  exportTable(HttpServletRequest request, HttpServletResponse response) throws IOException {
//
//        Map<String,Object> map=new HashMap<>();
//        String st = request.getParameter("startTime");
//        String et = request.getParameter("endTime");
//        String title = request.getParameter("title");
//        if(StringUtil.isEmpty(st)){
//            map.put("code","999");
//            map.put("msg","请选择游玩开始时间");
//            return null;
//        }
//        String startTime="";
//        if(!StringUtil.isEmpty(st)){
//            startTime=st+" 00:00:00";
//        }
//        String endTime="";
//        if(!StringUtil.isEmpty(et)){
//            endTime=et+" 23:59:59";
//        }
//
//        String finalStartTime = startTime;
//        String finalEndTime = endTime;
//        Future<List<OrderDO>> ticketFuture = executorService.submit(() -> getList(finalStartTime, finalEndTime, OrderTypeEnum.TICKET.getName()));
//        Future<List<OrderDO>> hotelFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.HOTEL.getName()));
//        Future<List<OrderDO>> shopFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.SHOP.getName()));
//
//        Future<List<OrderDO>> repastFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.REPAST.getName()));
//
//        Future<List<OrderDO>> comboFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.COMBO.getName()));
//        Future<List<OrderDO>> carFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.CAR.getName()));
//        Future<List<OrderDO>> busFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.BUS.getName()));
//        Future<List<OrderDO>> guideFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.GUIDE.getName()));
//        Future<List<OrderDO>> routeFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.ROUTE.getName()));
//        List<OrderDO> cwbbs=new ArrayList<>();
//        try {
//            cwbbs.addAll(ticketFuture.get());
//            cwbbs.addAll(shopFuture.get());
//            cwbbs.addAll(repastFuture.get());
//            cwbbs.addAll(hotelFuture.get());
//            cwbbs.addAll(comboFuture.get());
//            cwbbs.addAll(carFuture.get());
//            cwbbs.addAll(busFuture.get());
//            cwbbs.addAll(guideFuture.get());
//            cwbbs.addAll(routeFuture.get());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//            return null;
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//
//        PageInfo<OrderDO> pageInfo = new PageInfo<>(cwbbs);
//        map.put("code",0);
//        map.put("msg","操作成功");
//        map.put("count",pageInfo.getTotal());
//        map.put("data",pageInfo.getList());
//        return map;
//
//    }



    private SXSSFWorkbook exportTable(List<OrderDO> cwbbs, String title) {
        SXSSFWorkbook sxwb = new SXSSFWorkbook(-1);
        CellStyle style = sxwb.createCellStyle();
        Font font = sxwb.createFont();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        font.setBold(true);
        font.setFontName("微软雅黑");
        style.setFont(font);
        float  size = cwbbs.size();
        double c=size /1049076f;
        int page = (int)Math.ceil(c);

        for (int j = 1; j <=page ; j++) {
            Sheet sheet = sxwb.createSheet("第"+j+"页");
            int row = 0;
            Row row1 = sheet.createRow(row);
            Cell cell = row1.createCell(0);
            cell.setCellValue(title);
            CellRangeAddress region = new CellRangeAddress(0, 0, 0, 20);
            sheet.addMergedRegion(region);
            cell.setCellStyle(style);

            row++;
            insetTitile(sheet, row);
            int start=(j-1)*1049076;
            int end =j*1049076;
            if(end>=cwbbs.size()){
                end=cwbbs.size();
            }
            row++;
            for (int i = start; i <end ; i++,row++) {
                OrderDO orderDO = cwbbs.get(i);
                insetData( sheet, row,orderDO);
            }
        }


        return sxwb;
    }

    void insetTitile(Sheet sheet,int row){
        Row rowMenu = sheet.createRow(row);
        rowMenu.createCell(0).setCellValue("业务类型");
        rowMenu.createCell(1).setCellValue("订单号");
        rowMenu.createCell(2).setCellValue("订单状态");
        rowMenu.createCell(3).setCellValue("支付时间");
        rowMenu.createCell(4).setCellValue("支付类型");
        rowMenu.createCell(5).setCellValue("产品名称");
        rowMenu.createCell(6).setCellValue("销售单价");
        rowMenu.createCell(7).setCellValue("销售价");
        rowMenu.createCell(8).setCellValue("数量");
        rowMenu.createCell(9).setCellValue("同步状态");
        rowMenu.createCell(10).setCellValue("优惠金额");
        rowMenu.createCell(11).setCellValue("支付金额");
        rowMenu.createCell(12).setCellValue("结算单价");
        rowMenu.createCell(13).setCellValue("结算金额");
        rowMenu.createCell(14).setCellValue("毛利");
        rowMenu.createCell(15).setCellValue("联系人");
        rowMenu.createCell(16).setCellValue("联系人电话");
        rowMenu.createCell(17).setCellValue("抵离日期");
        rowMenu.createCell(18).setCellValue("礼包内容");
        rowMenu.createCell(19).setCellValue("备忘录");
        rowMenu.createCell(20).setCellValue("分销商");
    }







    void insetData(Sheet sheet,int row,OrderDO order){
        Row rowMenu = sheet.createRow(row);
        rowMenu.createCell(0).setCellValue(order.getOrderType());
        rowMenu.createCell(1).setCellValue(order.getOrderNo());
        rowMenu.createCell(2).setCellValue(order.getOrderStatus());
        rowMenu.createCell(3).setCellValue(order.getPayTime());
        rowMenu.createCell(4).setCellValue(order.getPayType());
        rowMenu.createCell(5).setCellValue(order.getOrderInfo());
        rowMenu.createCell(6).setCellValue(order.getOiPrice());
        rowMenu.createCell(7).setCellValue(order.getOrderSum());
        rowMenu.createCell(8).setCellValue(order.getAmount());
        rowMenu.createCell(9).setCellValue(order.getSyncStatus());
        rowMenu.createCell(10).setCellValue(order.getCouponSum());
        rowMenu.createCell(11).setCellValue(order.getPaySum());
        rowMenu.createCell(12).setCellValue(order.getOdPrice());
        rowMenu.createCell(13).setCellValue(order.getJsSum());
        rowMenu.createCell(14).setCellValue(order.getMl());
        rowMenu.createCell(15).setCellValue(order.getLinkName());
        rowMenu.createCell(16).setCellValue(order.getLinkMobile());
        rowMenu.createCell(17).setCellValue(order.getStartDate());
        rowMenu.createCell(18).setCellValue(order.getOrderGift());
        rowMenu.createCell(19).setCellValue(order.getNoteBook());
        rowMenu.createCell(20).setCellValue(order.getDistributorName());
    }

    /**
     * 统计
     * @param startTime 开始时间
     * @param endTime //结束时间
     * @param type //统计类型
     * @return 统计结果
     */
    private List<OrderDO> getList(String startTime, String endTime,String type) {
       return orderMapper.list(startTime,endTime, type);
    }

}
