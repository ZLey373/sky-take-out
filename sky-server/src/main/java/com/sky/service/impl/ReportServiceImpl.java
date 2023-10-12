package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: ZLey
 * @description
 * @date: 2023/10/12 15:23
 */
@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public TurnoverReportVO getTurnoverPerDay(LocalDate begin, LocalDate end) {
        List<LocalDate> Timelist=new ArrayList<>();
        while(!begin.equals(end)){
            begin=begin.plusDays(1);
            Timelist.add(begin);
        }
        String s = StringUtils.join(Timelist, ",");

        List<Double> turnOverlist=new ArrayList<>();
        for (LocalDate time : Timelist) {
            LocalDateTime beginTime = LocalDateTime.of(time, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(time, LocalTime.MAX);
            Map map = new HashMap();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status", Orders.COMPLETED);
            Double turnOver=orderMapper.getSumAmount(map);
            turnOver=turnOver==null?0:turnOver;
            turnOverlist.add(turnOver);
        }
        String t = StringUtils.join(turnOverlist, ",");


        return TurnoverReportVO.builder()
                .dateList(s)
                .turnoverList(t)
                .build();
    }
}
