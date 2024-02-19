package jit.gxw.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jit.gxw.dto.AppointmentPageQueryDTO;
import jit.gxw.dto.AppointmentRejectionDTO;
import jit.gxw.dto.ReletConditionSearchDTO;
import jit.gxw.dto.ReletRejectionDTO;
import jit.gxw.result.PageResult;
import jit.gxw.result.Result;
import jit.gxw.service.AppointmentService;
import jit.gxw.service.ReletService;
import jit.gxw.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController("ReletController")
@RequestMapping("/admin/relet")
@Api(tags = "管理端续租接口")
@Slf4j
@PreAuthorize("hasAuthority('admin:relet')")
public class ReletController {
    @Autowired
    private ReletService reletService;

    /**
     * 条件搜索续租单
     * @param reletConditionSearchDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("搜索续租单")
    public Result<PageResult> conditionSearch(ReletConditionSearchDTO reletConditionSearchDTO){
        log.info("条件搜索续租单：{}",reletConditionSearchDTO);
        PageResult pageResult=reletService.conditionSearch(reletConditionSearchDTO);
        return Result.success(pageResult);
    }

    /**
     * 拒绝续租
     * @param reletRejectionDTO
     * @return
     */
    @PutMapping("/rejection")
    @ApiOperation("拒绝续租")
    public Result rejection(@RequestBody ReletRejectionDTO reletRejectionDTO){
        log.info("拒绝续租：{}",reletRejectionDTO);
        reletService.rejection(reletRejectionDTO);
        return Result.success();
    }

    /**
     * 通过申请
     * @param reletRejectionDTO
     * @return
     */
    @PutMapping("/pass")
    @ApiOperation("通过申请")
    public Result pass(@RequestBody ReletRejectionDTO reletRejectionDTO){
        Long id = reletRejectionDTO.getId();
        log.info("通过申请{}", id);
        reletService.pass(id);
        return Result.success();
    }

    /**
     * 根据id删除续租单
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据id删除续租单")
    public Result delete(Long id){
        log.info("根据id删除续租单{}",id);
        reletService.delete(id);

        return Result.success();
    }

    /**
     * 获取待审核的续租单数量
     * @return
     */
    @GetMapping("/number")
    @ApiOperation("获取待审核的数量")
    public Result<Integer> getStatusNumber(){
        log.info("获取待审核续租的数量");
        Integer number=reletService.getStatusNumber();
        return Result.success(number);
    }

}
