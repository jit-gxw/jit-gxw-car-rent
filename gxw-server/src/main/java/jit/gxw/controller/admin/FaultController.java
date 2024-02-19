package jit.gxw.controller.admin;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jit.gxw.dto.*;
import jit.gxw.entity.Fault;
import jit.gxw.entity.Vehicle;
import jit.gxw.result.PageResult;
import jit.gxw.result.Result;
import jit.gxw.service.FaultService;
import jit.gxw.service.VehicleService;
import jit.gxw.vo.VehicleVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/admin/fault")
@Api(tags ="故障管理相关接口")
public class FaultController {
    @Autowired
    private FaultService faultService;

    /**
     * 故障报修
     * @param faultDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation("故障报修")
    @PreAuthorize("hasAuthority('admin:common')")
    @CacheEvict(cacheNames = "VehicleByClass",allEntries = true)
    public Result submit(@RequestBody FaultDTO faultDTO){
        log.info("故障报修：{}",faultDTO);
        faultService.submit(faultDTO);

        return Result.success();
    }

    /**
     * 故障信息搜索
     * @param faultPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("故障信息搜索")
    @PreAuthorize("hasAuthority('admin:fault')")
    public Result<PageResult> conditionSearch(FaultPageQueryDTO faultPageQueryDTO){
        log.info("搜索故障信息：{}",faultPageQueryDTO);
        PageResult pageResult=faultService.conditionSearch(faultPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 开始维修
     * @param faultStartDTO
     * @return
     */
    @PutMapping("/startRepair")
    @ApiOperation("开始维修")
    @PreAuthorize("hasAuthority('admin:fault')")
    public Result startRepair(@RequestBody FaultStartDTO faultStartDTO){
        log.info("开始维修：{}",faultStartDTO);
        faultService.startRepair(faultStartDTO);
        return Result.success();
    }

    /**
     * 完成维修
     * @param faultCompleteDTO
     * @return
     */
    @PutMapping("/complete")
    @ApiOperation("完成维修")
    @PreAuthorize("hasAuthority('admin:fault')")
    @CacheEvict(cacheNames = "VehicleByClass",allEntries = true)
    public Result complete(@RequestBody FaultCompleteDTO faultCompleteDTO){
        log.info("完成维修：{}",faultCompleteDTO);
        faultService.complete(faultCompleteDTO);
        return Result.success();
    }

    /**
     * 维修失败
     * @param faultCompleteDTO
     * @return
     */
    @PutMapping("/fail")
    @ApiOperation("维修失败")
    @PreAuthorize("hasAuthority('admin:fault')")
    public Result fail(@RequestBody FaultCompleteDTO faultCompleteDTO){
        log.info("维修失败：{}",faultCompleteDTO);
        faultService.fail(faultCompleteDTO);
        return Result.success();
    }

    /**
     * 根据id删除故障单
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据id删除故障单")
    @PreAuthorize("hasAuthority('admin:fault')")
    public Result deleteById(Long id){
        log.info("根据id删除故障单:{}",id);
        faultService.deleteById(id);
        return Result.success();
    }



}
