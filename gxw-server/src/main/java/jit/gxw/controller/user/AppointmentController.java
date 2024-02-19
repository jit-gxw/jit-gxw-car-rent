package jit.gxw.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jit.gxw.context.BaseContext;
import jit.gxw.dto.AppointmentSubmitDTO;
import jit.gxw.dto.AppointmentUserPageQueryDTO;
import jit.gxw.entity.Appointment;
import jit.gxw.result.PageResult;
import jit.gxw.result.Result;
import jit.gxw.service.AppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("UserAppointmentController")
@RequestMapping("/user/appointment")
@Api(tags = "用户端预约接口")
@Slf4j
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    /**
     * 发起预约
     * @param appointmentSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation("用户发起预约")
    public Result submit(@RequestBody AppointmentSubmitDTO appointmentSubmitDTO){

        log.info("发起预约：{}",appointmentSubmitDTO);
        appointmentService.submit(appointmentSubmitDTO);
        return Result.success();
    }

    /**
     * 取消预约
     * @param id
     * @return
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("取消预约")
    public Result cancel(@PathVariable Long id){
        log.info("用户取消订单{}",id);
        appointmentService.cancel(id);
        return Result.success();
    }

    /**
     * 用户分页查询本人预约单
     * @param appointmentUserPageQueryDTO
     * @return
     */
    @GetMapping
    @ApiOperation("分页查询本人预约单")
    public Result<PageResult> pageQuery(AppointmentUserPageQueryDTO appointmentUserPageQueryDTO){
        log.info("用户分页查询本人预约单:{}", BaseContext.getCurrentId());
        log.info("用户分页查询本人预约单分页:{}", appointmentUserPageQueryDTO);
        PageResult pageResult=appointmentService.pageQuery(appointmentUserPageQueryDTO);

        return Result.success(pageResult);

    }



}
