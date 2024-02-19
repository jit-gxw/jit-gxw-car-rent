package jit.gxw.controller.user;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jit.gxw.dto.VehicleListDTO;
import jit.gxw.dto.VehiclePageQueryDTO;
import jit.gxw.entity.Vehicle;
import jit.gxw.result.PageResult;
import jit.gxw.result.Result;
import jit.gxw.service.VehicleService;
import jit.gxw.vo.VehicleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("UserVehicleController")
@Slf4j
@RequestMapping("/user/vehicle")
@Api(tags ="查询车辆相关接口")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/list")
    @ApiOperation("用户查询租赁车辆")
    public Result<List<Vehicle>> list(VehicleListDTO vehicleListDTO){
        log.info("查询租赁车辆：{}",vehicleListDTO);
        List<Vehicle> list =vehicleService.list(vehicleListDTO);
        return Result.success(list);
    }

    /**
     * 用户根据id查询车辆
     * @param id
     * @return
     */
    @GetMapping
    @ApiOperation("用户根据id查询车辆")
    public Result<VehicleVO> getVehicleById(Long id){
        log.info("用户查询车辆：{}",id);

        return Result.success(
                vehicleService.getByIdForUser(id)
        );
    }

}
