package jit.gxw.controller.admin;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jit.gxw.dto.VehiclePageQueryDTO;
import jit.gxw.entity.Vehicle;
import jit.gxw.result.PageResult;
import jit.gxw.result.Result;
import jit.gxw.service.VehicleService;
import jit.gxw.vo.VehicleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/admin/vehicle")
@Api(tags ="车辆管理相关接口")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;


    /**
     * 菜品分页查询
     * @param vehiclePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询车辆")
    @PreAuthorize("hasAuthority('admin:common')")
    public Result<PageResult> page(VehiclePageQueryDTO vehiclePageQueryDTO){
        log.info("车辆分页查询：{}",vehiclePageQueryDTO);
        PageResult pageResult=vehicleService.pageQuery(vehiclePageQueryDTO);

        return Result.success(pageResult);
    }

    @PostMapping
    @ApiOperation("增加车辆")
    @PreAuthorize("hasAuthority('admin:vehicle')")
    @CacheEvict(cacheNames = "VehicleByClass",allEntries = true)
    public Result save(@RequestBody Vehicle vehicle){
        log.info("添加车辆：{}",vehicle);

        vehicleService.save(vehicle);

        return Result.success();
    }

    /**
     * 根据id查询车辆
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询车辆")
    @PreAuthorize("hasAuthority('admin:vehicle')")
    public Result<VehicleVO> getById(@PathVariable Long id){
        log.info("根据id查询车辆：{}",id);
        VehicleVO vehicleVO=vehicleService.getByIdmWithClassName(id);
        return Result.success(vehicleVO);
    }

    /**
     * 根据id修改车辆信息
     * @param vehicleVO
     * @return
     */
    @PutMapping
    @ApiOperation("根据id修改车辆信息")
    @PreAuthorize("hasAuthority('admin:vehicle')")
    @CacheEvict(cacheNames = "VehicleByClass",allEntries = true)
    public Result update(@RequestBody VehicleVO vehicleVO){
        log.info("根据id修改车辆信息：{}",vehicleVO);
        vehicleService.updata(vehicleVO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("设置车辆状态")
    @PreAuthorize("hasAuthority('admin:vehicle')")
    @CacheEvict(cacheNames = "VehicleByClass",allEntries = true)
    public Result setStatus(@PathVariable Integer status,Long id){
        log.info("设置车辆：{}状态：{}",id,status);
        vehicleService.setStatus(status,id);


        return Result.success();
    }
    @ApiOperation("删除车辆")
    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:vehicle')")
    @CacheEvict(cacheNames = "VehicleByClass",allEntries = true)
    public Result deleteById(Long id){
        log.info("删除车辆：{}",id);
        vehicleService.deleteById(id);

        return Result.success();
    }

}
