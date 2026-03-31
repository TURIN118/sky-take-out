package com.sky.service;

import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;

/**
 * 创建人：  @author WNJ
 * 创建时间: 2026-03-31 12:37
 * 项目名称: sky-take-out
 * 文件名称: ReportService
 * Copyright:2016-2026
 */
public interface ReportService {

    /**
     * 统计时间区间内的营业额数据
     *
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
}
