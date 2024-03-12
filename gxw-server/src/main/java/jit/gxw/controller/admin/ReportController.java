package jit.gxw.controller.admin;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jit.gxw.result.Result;
import jit.gxw.service.ReportService;
import jit.gxw.vo.RepairReportVO;
import jit.gxw.vo.TurnoverReportVO;
import jit.gxw.vo.UserReportVO;
import jit.gxw.vo.VehicleReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/report")
@Api(tags = "管理端数据统计接口")
@Slf4j
@PreAuthorize("hasAuthority('admin:report')")
public class ReportController {
    @Autowired
    private ReportService reportService;

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/userStatistics")
    @ApiOperation("用户统计")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end){
        log.info("用户数据统计：{}，{}",begin,end);

        return Result.success(reportService.getUserStatistics(begin,end));
    }

    /**
     * 获取租赁数据
     * @return
     */
    @GetMapping("/reletStatistics")
    @ApiOperation("租赁数据统计")
    public Result<List<VehicleReportVO>> reletStatistics(){

        log.info("租赁数据统计");
        return Result.success(reportService.getReletStatistics());
    }

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            LocalDate end){

        log.info("营业额数据统计：{}，{}",begin,end);


        return Result.success(reportService.getTurnoverStatistics(begin,end));
    }

    /**
     * 获取维修数据
     * @return
     */
    @GetMapping("/repair")
    @ApiOperation("维修数据统计")
    public Result<List<RepairReportVO>> repairStatistics(){

        log.info("维修数据统计");
        return Result.success(reportService.getrepairStatistics());
    }


}
