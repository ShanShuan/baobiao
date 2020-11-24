package com.pc.huangshan.controller.cwbb;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.pc.huangshan.Mapper.OrderMapper;
import com.pc.huangshan.common.emnu.OrderTypeEnum;
import com.pc.huangshan.model.cwbb.OrderComboDetailDO;
import com.pc.huangshan.model.cwbb.OrderComboInfoDO;
import com.pc.huangshan.model.cwbb.OrderDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
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

    /**
     * 订单列表
     * @param modelAndView
     * @return
     */
    @RequestMapping("/show")
    public ModelAndView show(ModelAndView modelAndView){
        modelAndView.setViewName("cw/order_list");
        return modelAndView;
    }

    /**
     * 套票订单列表
     * @param modelAndView
     * @return
     */
    @RequestMapping("/combo")
    public ModelAndView combo(ModelAndView modelAndView){
        modelAndView.setViewName("cw/order_combo_list");
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
        String idCard = request.getParameter("idCard");
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
        List<OrderDO> list =orderMapper.list(startTime,endTime, null,idCard);
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
        String idCard = request.getParameter("idCard");
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
        Future<List<OrderDO>> ticketFuture = executorService.submit(() -> getList(finalStartTime, finalEndTime, OrderTypeEnum.TICKET.getName(),idCard));
        Future<List<OrderDO>> hotelFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.HOTEL.getName(),idCard));
        Future<List<OrderDO>> shopFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.SHOP.getName(),idCard));

        Future<List<OrderDO>> repastFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.REPAST.getName(),idCard));

        Future<List<OrderDO>> comboFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.COMBO.getName(),idCard));
        Future<List<OrderDO>> carFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.CAR.getName(),idCard));
        Future<List<OrderDO>> busFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.BUS.getName(),idCard));
        Future<List<OrderDO>> guideFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.GUIDE.getName(),idCard));
        Future<List<OrderDO>> routeFuture = executorService.submit(() -> getList(finalStartTime,finalEndTime,OrderTypeEnum.ROUTE.getName(),idCard));
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
                if(file!=null)file.delete();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }

        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", URLEncoder.encode(name, "UTF-8"));

        return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.CREATED);
    }


    /**
     * 套票订单下载
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/downLoadCombo")
    public ResponseEntity<byte[]>  downLoadCombo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String method = request.getMethod();
        log.info("下载文件请求方法："+method);
        String name = request.getParameter("name");
        if(StringUtil.isEmpty(name)) {
            return null;
        }
        String st = request.getParameter("startTime");
        String et = request.getParameter("endTime");
        if(StringUtil.isEmpty(st)){
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


        List<OrderComboInfoDO> list =orderMapper.listCombo(startTime,endTime);

        list.stream().forEach(a->{
            String orderNo=a.getOrderNo();
            List<OrderComboDetailDO> orderComboDetailDOs=orderMapper.selectOrderComboDetailByOrderNo(orderNo);
            a.setComboDetailDOs(orderComboDetailDOs);
        });

        SXSSFWorkbook sxwb = null;
        ByteArrayOutputStream out  = new ByteArrayOutputStream();;
        try {
            sxwb = exportComboTable(list);
            sxwb.write(out);
            out.flush();//刷新
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(name, "UTF-8"));

            return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.CREATED);
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
            CellRangeAddress region = new CellRangeAddress(0, 0, 0, 22);
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
        rowMenu.createCell(21).setCellValue("身份证号码");
        rowMenu.createCell(22).setCellValue("退单数量");
        rowMenu.createCell(23).setCellValue("退单金额");
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
        rowMenu.createCell(21).setCellValue(order.getIdCard());
        rowMenu.createCell(22).setCellValue(order.getRefundNum());
        rowMenu.createCell(23).setCellValue(order.getRefundSum());
    }

    /**
     * 统计
     * @param startTime 开始时间
     * @param endTime //结束时间
     * @param type //统计类型
     * @return 统计结果
     */
    private List<OrderDO> getList(String startTime, String endTime,String type,String idCard) {
       return orderMapper.list(startTime,endTime, type,idCard);
    }


    /**
     * 套票订单导出
     * @param list
     * @return
     */
    private SXSSFWorkbook exportComboTable(List<OrderComboInfoDO> list) {
        SXSSFWorkbook sxwb = new SXSSFWorkbook(-1);
        CellStyle style = sxwb.createCellStyle();
        Font font = sxwb.createFont();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);
        font.setBold(true);
        font.setFontName("微软雅黑");
        style.setFont(font);
        Sheet sheet = sxwb.createSheet();
        int row = 0;
        Row row1 = sheet.createRow(row);
        Cell cell = row1.createCell(0);
        cell.setCellValue("订单报表");
        CellRangeAddress region = new CellRangeAddress(0, 0, 0, 23);
        sheet.addMergedRegion(region);
        cell.setCellStyle(style);
        row++;
        Row rowTitle = sheet.createRow(row);
        Cell rowTitle0 = rowTitle.createCell(0);
        rowTitle0.setCellValue("业务类型");

        Cell rowTitle1 = rowTitle.createCell(1);
        rowTitle1.setCellValue("订单号");

        Cell rowTitle2 = rowTitle.createCell(2);
        rowTitle2.setCellValue("订单来源");

        Cell rowTitle3 = rowTitle.createCell(3);
        rowTitle3.setCellValue("订单状态");

        Cell rowTitle4 = rowTitle.createCell(4);
        rowTitle4.setCellValue("支付时间");


        Cell rowTitle5 = rowTitle.createCell(5);
        rowTitle5.setCellValue("支付类型");


        Cell rowTitle6 = rowTitle.createCell(6);
        rowTitle6.setCellValue("产品名称");


        Cell rowTitle7 = rowTitle.createCell(7);
        rowTitle7.setCellValue("销售价（非单价）");


        Cell rowTitle8 = rowTitle.createCell(8);
        rowTitle8.setCellValue("数量");



        Cell rowTitle9 = rowTitle.createCell(9);
        rowTitle9.setCellValue("退单价格");


        Cell rowTitle10 = rowTitle.createCell(10);
        rowTitle10.setCellValue("退单数量");


        Cell rowTitle11 = rowTitle.createCell(11);
        rowTitle11.setCellValue("退单金额");





        Cell rowTitle12 = rowTitle.createCell(12);
        rowTitle12.setCellValue("同步状态");


        Cell rowTitle13 = rowTitle.createCell(13);
        rowTitle13.setCellValue("优惠金额");


        Cell rowTitle14 = rowTitle.createCell(14);
        rowTitle14.setCellValue("订单总价");



        Cell rowTitle15 = rowTitle.createCell(15);
        rowTitle15.setCellValue("联系人");


        Cell rowTitle16 = rowTitle.createCell(16);
        rowTitle16.setCellValue("联系电话");


        Cell rowTitle17 = rowTitle.createCell(17);
        rowTitle17.setCellValue("收货地址");


        Cell rowTitle18 = rowTitle.createCell(18);
        rowTitle18.setCellValue("抵离日期");



        Cell rowTitle19 = rowTitle.createCell(19);
        rowTitle19.setCellValue("礼包内容");


        Cell rowTitle20 = rowTitle.createCell(20);
        rowTitle20.setCellValue("团号");

        Cell rowTitle21 = rowTitle.createCell(21);
        rowTitle21.setCellValue("备忘录");

        Cell rowTitle22 = rowTitle.createCell(22);
        rowTitle22.setCellValue("分销商");

        Cell rowTitle23 = rowTitle.createCell(23);
        rowTitle23.setCellValue("自由行");

        for (int i = 0; i < list.size(); i++) {
            OrderComboInfoDO orderComboInfoDO = list.get(i);
            List<OrderComboDetailDO> comboDetailDOs = orderComboInfoDO.getComboDetailDOs();
            row++;

            int lines = comboDetailDOs.size();
            if(row+lines-1<=row){
                log.info("row:"+row);
                log.info("lines:"+lines);
            }
            Row sheetRow = sheet.createRow(row);
            Cell cell0 = sheetRow.createCell(0);
            cell0.setCellValue(orderComboInfoDO.getOrderType());
            if(lines>1) {
                CellRangeAddress rangeAddress0 = new CellRangeAddress(row, row + lines - 1, 0, 0);
                sheet.addMergedRegion(rangeAddress0);
            }


            Cell cell1 = sheetRow.createCell(1);
            cell1.setCellValue(orderComboInfoDO.getOrderNo());
            if(lines>1) {
                CellRangeAddress rangeAddress1 = new CellRangeAddress(row, row + lines - 1, 1, 1);
                sheet.addMergedRegion(rangeAddress1);
            }

            Cell cell2 = sheetRow.createCell(2);
            cell2.setCellValue(orderComboInfoDO.getWayType());
            if(lines>1) {
                CellRangeAddress rangeAddress2 = new CellRangeAddress(row, row + lines - 1, 2, 2);
                sheet.addMergedRegion(rangeAddress2);
            }


            Cell cell3 = sheetRow.createCell(3);
            cell3.setCellValue(orderComboInfoDO.getOrderStatus());
            if(lines>1) {
                CellRangeAddress rangeAddress3 = new CellRangeAddress(row, row + lines - 1, 3, 3);
                sheet.addMergedRegion(rangeAddress3);
            }


            Cell cell4 = sheetRow.createCell(4);
            cell4.setCellValue(orderComboInfoDO.getPayTime());
            if(lines>1) {
                CellRangeAddress rangeAddress4 = new CellRangeAddress(row, row + lines - 1, 4, 4);
                sheet.addMergedRegion(rangeAddress4);
            }


            Cell cell5 = sheetRow.createCell(5);
            cell5.setCellValue(orderComboInfoDO.getPayType());
            if(lines>1) {
                CellRangeAddress rangeAddress5 = new CellRangeAddress(row, row + lines - 1, 5, 5);
                sheet.addMergedRegion(rangeAddress5);
            }


            for (int j = 0; j < lines; j++) {
                OrderComboDetailDO orderComboDetailDO = comboDetailDOs.get(j);
                Cell cell6;
                if (j == 0) {
                    cell6= sheetRow.createCell(6);
                } else {
                    Row rowLine;
                    rowLine=sheet.getRow(row + j);
                    if(rowLine==null) rowLine= sheet.createRow(row + j);
                    cell6 = rowLine.createCell(6);
                }
                cell6.setCellValue(orderComboDetailDO.getOrderInfo());
            }


            for (int j = 0; j < lines; j++) {
                OrderComboDetailDO orderComboDetailDO = comboDetailDOs.get(j);
                Cell cell9;
                if (j == 0) {
                    cell9 = sheetRow.createCell(7);
                } else {
                    Row rowLine;
                    rowLine=sheet.getRow(row + j);
                    if(rowLine==null) rowLine= sheet.createRow(row + j);
                    cell9 = rowLine.createCell(7);
                }
                cell9.setCellValue(orderComboDetailDO.getOdPrice());
            }


            for (int j = 0; j < lines; j++) {
                OrderComboDetailDO orderComboDetailDO = comboDetailDOs.get(j);
                Cell cell9;
                if (j == 0) {
                    cell9 = sheetRow.createCell(8);
                } else {
                    Row rowLine;
                    rowLine=sheet.getRow(row + j);
                    if(rowLine==null) rowLine= sheet.createRow(row + j);
                    cell9 = rowLine.createCell(8);
                }
                cell9.setCellValue(orderComboDetailDO.getAmount());
            }



            for (int j = 0; j < lines; j++) {
                OrderComboDetailDO orderComboDetailDO = comboDetailDOs.get(j);
                Cell cell9;
                if (j == 0) {
                    cell9 = sheetRow.createCell(9);
                } else {
                    Row rowLine;
                    rowLine=sheet.getRow(row + j);
                    if(rowLine==null) rowLine= sheet.createRow(row + j);
                    cell9 = rowLine.createCell(9);
                }
                cell9.setCellValue(orderComboDetailDO.getRefundPrice());
            }



            for (int j = 0; j < lines; j++) {
                OrderComboDetailDO orderComboDetailDO = comboDetailDOs.get(j);
                Cell cell9;
                if (j == 0) {
                    cell9 = sheetRow.createCell(10);
                } else {
                    Row rowLine;
                    rowLine=sheet.getRow(row + j);
                    if(rowLine==null) rowLine= sheet.createRow(row + j);
                    cell9 = rowLine.createCell(10);
                }
                cell9.setCellValue(orderComboDetailDO.getRefundNum());
            }



            Cell cell11 = sheetRow.createCell(11);
            cell11.setCellValue(orderComboInfoDO.getRefundSum());
            if(lines>1) {
                CellRangeAddress rangeAddress11 = new CellRangeAddress(row, row + lines - 1, 11, 11);
                sheet.addMergedRegion(rangeAddress11);
            }




            for (int j = 0; j < lines; j++) {
                Cell cell9;
                if (j == 0) {
                    cell9 = sheetRow.createCell(12);
                } else {
                    Row rowLine;
                    rowLine=sheet.getRow(row + j);
                    if(rowLine==null) rowLine= sheet.createRow(row + j);
                    cell9 = rowLine.createCell(12);
                }
                cell9.setCellValue(orderComboInfoDO.getSyncStatus());
            }

            Cell cell13 = sheetRow.createCell(13);
            cell13.setCellValue(orderComboInfoDO.getCouponSum());
            if(lines>1) {
                CellRangeAddress rangeAddress13 = new CellRangeAddress(row, row + lines - 1, 13, 13);
                sheet.addMergedRegion(rangeAddress13);
            }

            Cell cell14 = sheetRow.createCell(14);
            cell14.setCellValue(orderComboInfoDO.getOrderSum());
            if(lines>1) {
                CellRangeAddress rangeAddress14 = new CellRangeAddress(row, row + lines - 1, 14, 14);
                sheet.addMergedRegion(rangeAddress14);
            }

            Cell cell15 = sheetRow.createCell(15);
            cell15.setCellValue(orderComboInfoDO.getLinkName());
            if(lines>1) {
                CellRangeAddress rangeAddress15 = new CellRangeAddress(row, row + lines - 1, 15, 15);
                sheet.addMergedRegion(rangeAddress15);
            }

            Cell cell16 = sheetRow.createCell(16);
            cell16.setCellValue(orderComboInfoDO.getLinkMobile());
            if(lines>1) {
                CellRangeAddress rangeAddress16 = new CellRangeAddress(row, row + lines - 1, 16, 16);
                sheet.addMergedRegion(rangeAddress16);
            }


            Cell cell17 = sheetRow.createCell(17);
            cell17.setCellValue(orderComboInfoDO.getLinkAddr());
            if(lines>1) {
                CellRangeAddress rangeAddress17 = new CellRangeAddress(row, row + lines - 1, 17, 17);
                sheet.addMergedRegion(rangeAddress17);
            }


            Cell cell18 = sheetRow.createCell(18);
            if (lines > 0) {
                OrderComboDetailDO orderComboDetailDO = comboDetailDOs.get(0);
                cell18.setCellValue(orderComboDetailDO.getStartDate());

            } else {
                cell18.setCellValue("");
            }
            if(lines>1) {
                CellRangeAddress rangeAddress18 = new CellRangeAddress(row, row + lines - 1, 18, 18);
                sheet.addMergedRegion(rangeAddress18);
            }


            Cell cell19 = sheetRow.createCell(19);
            cell19.setCellValue(orderComboInfoDO.getOrderGift());
            if(lines>1) {
                CellRangeAddress rangeAddress19 = new CellRangeAddress(row, row + lines - 1, 19, 19);
                sheet.addMergedRegion(rangeAddress19);
            }


            Cell cell20 = sheetRow.createCell(20);
            cell20.setCellValue(orderComboInfoDO.getTeamNo());
            if(lines>1) {
                CellRangeAddress rangeAddress20 = new CellRangeAddress(row, row + lines - 1, 20, 20);
                sheet.addMergedRegion(rangeAddress20);
            }


            Cell cell21 = sheetRow.createCell(21);
            cell21.setCellValue(orderComboInfoDO.getNoteBook());
            if(lines>1) {
                CellRangeAddress rangeAddress21 = new CellRangeAddress(row, row + lines - 1, 21, 21);
                sheet.addMergedRegion(rangeAddress21);
            }


            Cell cell22 = sheetRow.createCell(22);
            cell22.setCellValue(orderComboInfoDO.getDistributorName());
            if(lines>1) {
                CellRangeAddress rangeAddress22 = new CellRangeAddress(row, row + lines - 1, 22, 22);
                sheet.addMergedRegion(rangeAddress22);
            }

            Cell cell23 = sheetRow.createCell(23);
            cell23.setCellValue(orderComboInfoDO.getGroupName());
            if(lines>1) {
                CellRangeAddress rangeAddress23 = new CellRangeAddress(row, row + lines - 1, 23, 23);
                sheet.addMergedRegion(rangeAddress23);
            }
            row = row + lines-1;
        }
        return sxwb;
    }
}
