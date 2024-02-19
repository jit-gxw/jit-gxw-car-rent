package jit.gxw.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jit.gxw.dto.AppointmentPageQueryDTO;
import jit.gxw.dto.AppointmentRejectionDTO;
import jit.gxw.result.PageResult;
import jit.gxw.result.Result;
import jit.gxw.service.AppointmentService;
import jit.gxw.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController("AppointmentController")
@RequestMapping("/admin/appointment")
@Api(tags = "管理端预约接口")
@Slf4j
@PreAuthorize("hasAuthority('admin:appointment')")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    /**
     *
     * 搜索预约信息
     * @param appointmentPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("分页查询预约信息")
    public Result<PageResult> conditionSearch(AppointmentPageQueryDTO appointmentPageQueryDTO){
        log.info("分页查询预约信息：{}",appointmentPageQueryDTO);
        PageResult pageResult=appointmentService.conditionSearch(appointmentPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 拒绝预约
     * @param appointmentRejectionDTO
     * @return
     */
    @PutMapping("/rejection")
    @ApiOperation("拒绝预约")
    public Result rejection(@RequestBody AppointmentRejectionDTO appointmentRejectionDTO){
        log.info("拒绝预约：{}",appointmentRejectionDTO);
        appointmentService.rejection(appointmentRejectionDTO);
        return Result.success();
    }

    /**
     * 通过审核
     * @param id
     * @return
     */
    @PutMapping("/pass")
    @ApiOperation("通过审核")
    public Result<OrderVO> pass(Long id){
        log.info("通过审核{}",id);
        OrderVO orderVO=appointmentService.pass(id);
        return Result.success(orderVO);
    }

    @DeleteMapping
    @ApiOperation("删除预约")
    public Result delete(Long id){
        log.info("删除预约单{}",id);
        appointmentService.delete(id);
        return Result.success();
    }

    @GetMapping("/number")
    @ApiOperation("获取待审核订单的数量")
    public Result<Integer> getStatusNumber(){
        log.info("获取待审核预约的数量");
        Integer number=appointmentService.getStatusNumber();
        return Result.success(number);
    }

}
