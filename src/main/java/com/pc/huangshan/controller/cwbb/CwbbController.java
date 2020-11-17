package com.pc.huangshan.controller.cwbb;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.StringUtil;
import com.pc.huangshan.Mapper.CwbbMapper;
import com.pc.huangshan.common.emnu.OrderTypeEnum;
import com.pc.huangshan.model.cwbb.CwTj;
import com.pc.huangshan.model.cwbb.Cwbb;
import com.pc.huangshan.utlis.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.*;

/**
 * 财务报表
 */
@Controller
@RequestMapping("/cwbb")

public class CwbbController {

    @Resource
    CwbbMapper cwbbMapper;


    @RequestMapping("/show")
    public String show(Model model){
        model.addAttribute("1","22");
        return "cw/pay_way";
    }


    @RequestMapping("/show_special")
    public String showSpecial(Model model){
        model.addAttribute("1","22");
        return "cw/pay_way_special";
    }
    private ExecutorService executorService = Executors.newFixedThreadPool(30);
    Logger logger= LoggerFactory.getLogger(CwbbController.class);

    @RequestMapping("/list")
    @ResponseBody
    public Map<String,Object> list(Model model, HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        String st = request.getParameter("startTime");
        String et = request.getParameter("endTime");
        if(StringUtil.isEmpty(st)||StringUtil.isEmpty(et)){
            map.put("code","999");
            map.put("msg","请选择开始时间和结束时间");
            return map;
        }
        long start = System.currentTimeMillis();
        String startTime=st+" 00:00:00";
        String endTime=et+" 23:59:59";
        //授信支付
        List<Integer> distrubitId=cwbbMapper.selectOTAId(354);//OTA
        List<Integer> zkId=cwbbMapper.selectZkId();//直客


        List<Integer> ydzx=cwbbMapper.selectOTAId(307);//预订中心
        List<Integer> ptcs=cwbbMapper.selectOTAId(293);//平台测试
        List<Integer> tm=cwbbMapper.selectOTAId(19);//途马(智慧旅游的前身)
        List<Integer> sdcs=cwbbMapper.selectOTAId(294);//新深大测试分销商


        List<Integer> allId=cwbbMapper.selectAllDistributorId();//所有的
        allId.removeAll(distrubitId);
        allId.removeAll(zkId);
        allId.removeAll(ydzx);
        allId.removeAll(ptcs);
        allId.removeAll(tm);
        allId.removeAll(sdcs);//所有的分销商id  去掉以上的就是 其他的统计项

        Future<List<Cwbb>> ticketFuture = executorService.submit(() -> getListCwbb(startTime,endTime, OrderTypeEnum.TICKET.getName(),distrubitId,zkId,allId));
        Future<List<Cwbb>> hotelFuture = executorService.submit(() -> getListCwbb(startTime,endTime,OrderTypeEnum.HOTEL.getName(),distrubitId,zkId,allId));
        Future<List<Cwbb>> shopFuture = executorService.submit(() -> getListCwbb(startTime,endTime,OrderTypeEnum.SHOP.getName(),distrubitId,zkId,allId));

        Future<List<Cwbb>> repastFuture = executorService.submit(() -> getListCwbb(startTime,endTime,OrderTypeEnum.REPAST.getName(),distrubitId,zkId,allId));

        Future<List<Cwbb>> comboFuture = executorService.submit(() -> getListCwbb(startTime,endTime,OrderTypeEnum.COMBO.getName(),distrubitId,zkId,allId));
        Future<List<Cwbb>> carFuture = executorService.submit(() -> getListCwbb(startTime,endTime,OrderTypeEnum.CAR.getName(),distrubitId,zkId,allId));
        Future<List<Cwbb>> busFuture = executorService.submit(() -> getListCwbb(startTime,endTime,OrderTypeEnum.BUS.getName(),distrubitId,zkId,allId));
        Future<List<Cwbb>> guideFuture = executorService.submit(() -> getListCwbb(startTime,endTime,OrderTypeEnum.GUIDE.getName(),distrubitId,zkId,allId));
        Future<List<Cwbb>> routeFuture = executorService.submit(() -> getListCwbb(startTime,endTime,OrderTypeEnum.ROUTE.getName(),distrubitId,zkId,allId));
        List<Cwbb> cwbbs=new ArrayList<>();
        try {
            cwbbs.add(new Cwbb(OrderTypeEnum.TICKET.getName()));
            cwbbs.addAll(ticketFuture.get());
            cwbbs.add(new Cwbb(OrderTypeEnum.SHOP.getName()));
            cwbbs.addAll(shopFuture.get());
            cwbbs.add(new Cwbb(OrderTypeEnum.REPAST.getName()));
            cwbbs.addAll(repastFuture.get());
            cwbbs.add(new Cwbb(OrderTypeEnum.HOTEL.getName()));
            cwbbs.addAll(hotelFuture.get());
            cwbbs.add(new Cwbb(OrderTypeEnum.COMBO.getName()));
            cwbbs.addAll(comboFuture.get());
            cwbbs.add(new Cwbb(OrderTypeEnum.CAR.getName()));
            cwbbs.addAll(carFuture.get());
            cwbbs.add(new Cwbb(OrderTypeEnum.BUS.getName()));
            cwbbs.addAll(busFuture.get());
            cwbbs.add(new Cwbb(OrderTypeEnum.GUIDE.getName()));
            cwbbs.addAll(guideFuture.get());
            cwbbs.add(new Cwbb(OrderTypeEnum.ROUTE.getName()));
            cwbbs.addAll(routeFuture.get());

            long end  = System.currentTimeMillis();
            logger.info("【统计支付方式请求耗时】"+((end-start)/1000)+"秒");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        PageInfo pageInfo = new PageInfo(cwbbs);
        map.put("code",0);
        map.put("msg","操作成功");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 统计
     * @param startTime 开始时间
     * @param endTime //结束时间
     * @param type //统计类型
     * @param otaIds //ota 分销商idList
     * @param zkIds //直客分销商idList
     * @param qtIds//其他 分销商idList
     * @return 统计结果
     */
    private List<Cwbb> getListCwbb(String startTime, String endTime, String type,List<Integer> otaIds,List<Integer> zkIds,List<Integer> qtIds) {


        String st = DateUtils.getMonthFirstDateByDate(startTime, "yyyy-MM-dd");
        String start=st+" 00:00:00";

        String et = DateUtils.getMonthLastDateByDate(startTime, "yyyy-MM-dd");
        String end=et+" 23:59:59";
        String ys = DateUtils.getYearFirstDateByDate(endTime, "yyyy-MM-dd");
        String yearStart = ys+" 00:00:00";
//        String ye = DateUtils.getYearLastDateByDate(endTime, "yyyy-MM-dd");
        String yearEnd = endTime+" 23:59:59";
        List<CwTj> yearcwTjs =cwbbMapper.listTjType(yearStart, yearEnd, type);//总额年度统计



        List<Cwbb> list=new ArrayList<>();

        if(OrderTypeEnum.TICKET.getName().equals(type)){
            Collections.sort(yearcwTjs, (o1, o2) -> {
                int diff = o1.getTickeSort() - o2.getTickeSort();
                if(diff<0){
                    return 1;
                }else if(diff>0){
                    return -1;
                }else {
                    return 0;
                }
            });
        }else if(OrderTypeEnum.SHOP.getName().equals(type)){
            Collections.sort(yearcwTjs, (o1, o2) -> {
                int diff = o1.getShopSort() - o2.getShopSort();
                if(diff<0){
                    return 1;
                }else if(diff>0){
                    return -1;
                }else {
                    return 0;
                }
            });
        }else if(OrderTypeEnum.HOTEL.getName().equals(type)){
            Collections.sort(yearcwTjs, (o1, o2) -> {
                int diff = o1.getHotelSort() - o2.getHotelSort();
                if(diff<0){
                    return 1;
                }else if(diff>0){
                    return -1;
                }else {
                    return 0;
                }
            });
        }

        List<CwTj> listOtA=new ArrayList<>();//ota
        List<CwTj> zk=new ArrayList<>();//直客
        List<CwTj> qt=new ArrayList<>();//其他

        Future<List<Cwbb>> listFuture = executorService.submit(() -> {
            List<Cwbb> listZffs = cwbbMapper.listByType(startTime, endTime, type);
            return listZffs;
        });

        Future<List<CwTj>> oTAFuture = executorService.submit(() -> {
            List<CwTj> listOtA1 =cwbbMapper.listSxZf(startTime,endTime,otaIds,type);
            return listOtA1;
        });
        Future<List<CwTj>> zkFuture = executorService.submit(() -> {
            List<CwTj> zk1 =cwbbMapper.listSxZf(startTime,endTime,zkIds, type);
            return zk1;
        });
        Future<List<CwTj>> qtFuture = executorService.submit(() -> {
            List<CwTj> qt1 =cwbbMapper.listSxZf(startTime,endTime,qtIds, type);
            return qt1;
        });


        List<CwTj> dqBtc=new ArrayList<>();

        Future<List<CwTj>> dqBtcFuture = executorService.submit(() -> {
            List<CwTj> dqBtc1 =cwbbMapper.listBtCType(startTime, endTime,type);//当期按B2C统计
            return dqBtc1;
        });






        List<CwTj> monthCwtj=new ArrayList<>();
        Future<List<CwTj>> monthCwtjFuture = executorService.submit(() -> {
            List<CwTj> monthCwtj1 =cwbbMapper.listTjType(start, end,type);//总额月度统计
            return monthCwtj1;
        });

        List<CwTj> monthBtc=new ArrayList<>();
        Future<List<CwTj>> monthBtcFuture = executorService.submit(() -> {
            List<CwTj> monthBtc1 =cwbbMapper.listBtCType(start, end, type);//月度B2C统计
            return monthBtc1;
        });





        List<CwTj> yearBtc=new ArrayList<>();//年度B2C统计

        Future<List<CwTj>> yearBtcFuture = executorService.submit(() -> {
            List<CwTj> yearBtc1 =cwbbMapper.listBtCType(yearStart, yearEnd, type);//年度B2C统计
            return yearBtc1;
        });


        try {
            list=listFuture.get();
            listOtA=oTAFuture.get();
            zk=zkFuture.get();
            qt=qtFuture.get();
            dqBtc=dqBtcFuture.get();
            monthCwtj=monthCwtjFuture.get();
            monthBtc=monthBtcFuture.get();
            yearBtc=yearBtcFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        List<Cwbb> result=new ArrayList<>();

        for (int i = 0; i < yearcwTjs.size(); i++) {
            CwTj cwtj = yearcwTjs.get(i);
            String supplyName = cwtj.getSupplyName();
            Cwbb cwbb=new Cwbb();
            for (int j = 0; j < list.size(); j++) {
                Cwbb cw = list.get(j);
                if(supplyName.equals(cw.getSupplyName())){
                    BeanUtils.copyProperties(cw,cwbb);
                }
            }
            cwbb.setSupplyName(cwtj.getSupplyName());
            cwbb.setYear(cwtj.getTotalSum());

            for (int j = 0; j < monthCwtj.size(); j++) {
                CwTj cwTj = monthCwtj.get(j);
                if(supplyName.equals(cwTj.getSupplyName())){
                    Double totalSum = cwTj.getTotalSum();
                    cwbb.setMonth(totalSum);
                }
            }



            for (int j = 0; j < dqBtc.size(); j++) {
                CwTj cwTj = dqBtc.get(j);
                if(supplyName.equals(cwTj.getSupplyName())){
                    Double totalSum = cwTj.getTotalSum();
                    cwbb.setBtwocsum(totalSum);
                }
            }

            for (int j = 0; j < monthBtc.size(); j++) {
                CwTj cwTj = monthBtc.get(j);
                if(supplyName.equals(cwTj.getSupplyName())){
                    Double totalSum = cwTj.getTotalSum();
                    cwbb.setBtwocmonth(totalSum);
                }
            }
            for (int j = 0; j < yearBtc.size(); j++) {
                CwTj cwTj = yearBtc.get(j);
                if(supplyName.equals(cwTj.getSupplyName())){
                    Double totalSum = cwTj.getTotalSum();
                    cwbb.setBtwocyear(totalSum);
                }
            }


            for (int j = 0; j < listOtA.size(); j++) {
                CwTj cwTj = listOtA.get(j);
                if(supplyName.equals(cwTj.getSupplyName())){
                    Double totalSum = cwTj.getTotalSum();
                    cwbb.setOTA(totalSum);
                }
            }
            for (int j = 0; j < zk.size(); j++) {
                CwTj cwTj = zk.get(j);
                if(supplyName.equals(cwTj.getSupplyName())){
                    Double totalSum = cwTj.getTotalSum();
                    cwbb.setZk(totalSum);
                }
            }
            for (int j = 0; j < qt.size(); j++) {
                CwTj cwTj = qt.get(j);
                if(supplyName.equals(cwTj.getSupplyName())){
                    Double totalSum = cwTj.getTotalSum();
                    cwbb.setSxqt(totalSum);
                }
            }
            result.add(cwbb);
        }

        return result;
    }


    /**
     * 内部检票报表
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/list_special")
    @ResponseBody
    public Map<String,Object> listSpecial(Model model, HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        String st = request.getParameter("startTime");
        String et = request.getParameter("endTime");
        if(StringUtil.isEmpty(st)||StringUtil.isEmpty(et)){
            map.put("code","999");
            map.put("msg","请选择开始时间和结束时间");
            return map;
        }
        long start = System.currentTimeMillis();
        String startTime=st+" 00:00:00";
        String endTime=et+" 23:59:59";
        //授信支付
        List<Integer> distrubitId=cwbbMapper.selectOTAId(354);//OTA
        List<Integer> zkId=cwbbMapper.selectZkId();//直客


        List<Integer> ydzx=cwbbMapper.selectOTAId(307);//预订中心
        List<Integer> ptcs=cwbbMapper.selectOTAId(293);//平台测试
        List<Integer> tm=cwbbMapper.selectOTAId(19);//途马(智慧旅游的前身)
        List<Integer> sdcs=cwbbMapper.selectOTAId(294);//新深大测试分销商


        List<Integer> allId=cwbbMapper.selectAllDistributorId();//所有的
        allId.removeAll(distrubitId);
        allId.removeAll(zkId);
        allId.removeAll(ydzx);
        allId.removeAll(ptcs);
        allId.removeAll(tm);
        allId.removeAll(sdcs);//所有的分销商id  去掉以上的就是 其他的统计项

        Future<List<Cwbb>> listFuture = executorService.submit(() -> getListCwbbSpecial(startTime,endTime,distrubitId,zkId,allId));
        List<Cwbb> cwbbs=new ArrayList<>();
        try {
            cwbbs.addAll(listFuture.get());
            long end  = System.currentTimeMillis();
            logger.info("【统计支付方式请求耗时】"+((end-start)/1000)+"秒");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        PageInfo pageInfo = new PageInfo(cwbbs);
        map.put("code",0);
        map.put("msg","操作成功");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }



    /**
     * 内部检票统计
     * @param startTime 开始时间
     * @param endTime //结束时间
     * @param otaIds //ota 分销商idList
     * @param zkIds //直客分销商idList
     * @param qtIds//其他 分销商idList
     * @return 统计结果
     */
    private List<Cwbb> getListCwbbSpecial(String startTime, String endTime,List<Integer> otaIds,List<Integer> zkIds,List<Integer> qtIds) {

        List<String> supplyNames=Arrays.asList("黄山风景区","黄山风景区观光缆车","黄山风景区云谷索道","黄山风景区太平索道","黄山风景区玉屏索道"
        ,"黄山雲亼","黄山狮林大酒店","黄山玉屏楼宾馆","黄山白云宾馆","黄山西海饭店");
        String st = DateUtils.getMonthFirstDateByDate(startTime, "yyyy-MM-dd");
        String start=st+" 00:00:00";

        String et = DateUtils.getMonthLastDateByDate(startTime, "yyyy-MM-dd");
        String end=et+" 23:59:59";
        String ys = DateUtils.getYearFirstDateByDate(endTime, "yyyy-MM-dd");
        String yearStart = ys+" 00:00:00";
        String ye = DateUtils.getYearLastDateByDate(endTime, "yyyy-MM-dd");
        String yearEnd = ye+" 23:59:59";

        List<CwTj> yearcwTjs =cwbbMapper.listTjBySupplyNames(yearStart, yearEnd, supplyNames);//总额年度统计



        List<Cwbb> list=new ArrayList<>();


        List<CwTj> listOtA=new ArrayList<>();//ota
        List<CwTj> zk=new ArrayList<>();//直客
        List<CwTj> qt=new ArrayList<>();//其他

        Future<List<Cwbb>> listFuture = executorService.submit(() -> {
            List<Cwbb> listZffs = cwbbMapper.listBySupplyNames(startTime, endTime, supplyNames);
            return listZffs;
        });

        Future<List<CwTj>> oTAFuture = executorService.submit(() -> {
            List<CwTj> listOtA1 =cwbbMapper.listSxZfBySupplyNames(startTime,endTime,otaIds,supplyNames);
            return listOtA1;
        });
        Future<List<CwTj>> zkFuture = executorService.submit(() -> {
            List<CwTj> zk1 =cwbbMapper.listSxZfBySupplyNames(startTime,endTime,zkIds, supplyNames);
            return zk1;
        });
        Future<List<CwTj>> qtFuture = executorService.submit(() -> {
            List<CwTj> qt1 =cwbbMapper.listSxZfBySupplyNames(startTime,endTime,qtIds, supplyNames);
            return qt1;
        });


        List<CwTj> dqBtc=new ArrayList<>();

        Future<List<CwTj>> dqBtcFuture = executorService.submit(() -> {
            List<CwTj> dqBtc1 =cwbbMapper.listBtCBySupplyNames(startTime, endTime,supplyNames);//当期按B2C统计
            return dqBtc1;
        });






        List<CwTj> monthCwtj=new ArrayList<>();
        Future<List<CwTj>> monthCwtjFuture = executorService.submit(() -> {
            List<CwTj> monthCwtj1 =cwbbMapper.listTjBySupplyNames(start, end,supplyNames);//总额月度统计
            return monthCwtj1;
        });

        List<CwTj> monthBtc=new ArrayList<>();
        Future<List<CwTj>> monthBtcFuture = executorService.submit(() -> {
            List<CwTj> monthBtc1 =cwbbMapper.listBtCBySupplyNames(start, end, supplyNames);//月度B2C统计
            return monthBtc1;
        });





        List<CwTj> yearBtc=new ArrayList<>();//年度B2C统计

        Future<List<CwTj>> yearBtcFuture = executorService.submit(() -> {
            List<CwTj> yearBtc1 =cwbbMapper.listBtCBySupplyNames(yearStart, yearEnd, supplyNames);//年度B2C统计
            return yearBtc1;
        });


        try {
            list=listFuture.get();
            listOtA=oTAFuture.get();
            zk=zkFuture.get();
            qt=qtFuture.get();
            dqBtc=dqBtcFuture.get();
            monthCwtj=monthCwtjFuture.get();
            monthBtc=monthBtcFuture.get();
            yearBtc=yearBtcFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        List<Cwbb> result=new ArrayList<>();

        for (int i = 0; i < yearcwTjs.size(); i++) {
            CwTj cwtj = yearcwTjs.get(i);
            String supplyName = cwtj.getSupplyName();
            Cwbb cwbb=new Cwbb();
            for (int j = 0; j < list.size(); j++) {
                Cwbb cw = list.get(j);
                if(supplyName.equals(cw.getSupplyName())){
                    BeanUtils.copyProperties(cw,cwbb);
                }
            }
            cwbb.setSupplyName(cwtj.getSupplyName());
            cwbb.setYear(cwtj.getTotalSum());

            for (int j = 0; j < monthCwtj.size(); j++) {
                CwTj cwTj = monthCwtj.get(j);
                if(supplyName.equals(cwTj.getSupplyName())){
                    Double totalSum = cwTj.getTotalSum();
                    cwbb.setMonth(totalSum);
                }
            }



            for (int j = 0; j < dqBtc.size(); j++) {
                CwTj cwTj = dqBtc.get(j);
                if(supplyName.equals(cwTj.getSupplyName())){
                    Double totalSum = cwTj.getTotalSum();
                    cwbb.setBtwocsum(totalSum);
                }
            }

            for (int j = 0; j < monthBtc.size(); j++) {
                CwTj cwTj = monthBtc.get(j);
                if(supplyName.equals(cwTj.getSupplyName())){
                    Double totalSum = cwTj.getTotalSum();
                    cwbb.setBtwocmonth(totalSum);
                }
            }
            for (int j = 0; j < yearBtc.size(); j++) {
                CwTj cwTj = yearBtc.get(j);
                if(supplyName.equals(cwTj.getSupplyName())){
                    Double totalSum = cwTj.getTotalSum();
                    cwbb.setBtwocyear(totalSum);
                }
            }


            for (int j = 0; j < listOtA.size(); j++) {
                CwTj cwTj = listOtA.get(j);
                if(supplyName.equals(cwTj.getSupplyName())){
                    Double totalSum = cwTj.getTotalSum();
                    cwbb.setOTA(totalSum);
                }
            }
            for (int j = 0; j < zk.size(); j++) {
                CwTj cwTj = zk.get(j);
                if(supplyName.equals(cwTj.getSupplyName())){
                    Double totalSum = cwTj.getTotalSum();
                    cwbb.setZk(totalSum);
                }
            }
            for (int j = 0; j < qt.size(); j++) {
                CwTj cwTj = qt.get(j);
                if(supplyName.equals(cwTj.getSupplyName())){
                    Double totalSum = cwTj.getTotalSum();
                    cwbb.setSxqt(totalSum);
                }
            }
            result.add(cwbb);
        }

        return result;
    }
}
