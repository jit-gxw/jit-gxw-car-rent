package jit.gxw.service;

import jit.gxw.vo.RepairReportVO;
import jit.gxw.vo.TurnoverReportVO;
import jit.gxw.vo.UserReportVO;
import jit.gxw.vo.VehicleReportVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


public interface ReportService {

    /**
     * 统计指定时间区间内的用户数据
     * @param begin
     * @param end
     * @return
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计租赁数据
     * @return
     */
    List<VehicleReportVO> getReletStatistics();
    /**
     * 统计指定时间区间内的营业额数据
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计维修数据
     * @return
     */
    List<RepairReportVO> getrepairStatistics();
}
