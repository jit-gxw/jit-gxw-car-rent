package jit.gxw.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jit.gxw.dto.AppointmentPageQueryDTO;
import jit.gxw.dto.AppointmentSubmitDTO;
import jit.gxw.dto.ReletPageQueryDTO;
import jit.gxw.dto.ReletSubmitDTO;
import jit.gxw.result.PageResult;
import jit.gxw.result.Result;
import jit.gxw.service.ReletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController("UserReletController")
@RequestMapping("/user/relet")
@Api(tags = "用户端续租接口")
@Slf4j
public class ReletController {

    @Autowired
    private ReletService reletService;

    /**
     * 用户发起续租
     * @param reletSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation("用户发起续租")
    public Result submit(@RequestBody ReletSubmitDTO reletSubmitDTO){

        log.info("发起续租：{}",reletSubmitDTO);
        reletService.submit(reletSubmitDTO);
        return Result.success();
    }

    /**
     * 用户分页查询
     * @param reletPageQueryDTO
     * @return
     */
    @GetMapping
    @ApiOperation("用户分页查询")
    public Result<PageResult> pageQuery(ReletPageQueryDTO reletPageQueryDTO){
        log.info("用户分页查询{}",reletPageQueryDTO);
        PageResult pageResult=reletService.pageQuery(reletPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 用户取消续租
     * @param id
     * @return
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("取消续租")
    public Result cancel(@PathVariable Long id){
        log.info("用户取消续租{}",id);
        reletService.cancel(id);
        return Result.success();
    }




}
