package jit.gxw.service;

import jit.gxw.dto.VehicleListDTO;
import jit.gxw.dto.VehiclePageQueryDTO;
import jit.gxw.entity.Vehicle;
import jit.gxw.result.PageResult;
import jit.gxw.vo.VehicleVO;

import java.util.List;

public interface VehicleService {

    /**
     * 分页查询车辆
     * @param vehiclePageQueryDTO
     * @return
     */
    PageResult pageQuery(VehiclePageQueryDTO vehiclePageQueryDTO);

    /**
     * 添加车辆
     * @param vehicle
     */
    void save(Vehicle vehicle);

    /**
     * 根据id查询车辆
     * @param id
     * @return
     */
    VehicleVO getByIdmWithClassName(Long id);

    /**
     * 修改车辆信息
     * @param vehicleVO
     */
    void updata(VehicleVO vehicleVO);

    /**
     * 设置车辆状态
     * @param status
     * @param id
     */
    void setStatus(Integer status, Long id);

    /**
     * 根据id删除车辆
     * @param id
     */
    void deleteById(Long id);

    /**
     * 用户查询车辆
     * @param vehicleListDTO
     * @return
     */
    List<Vehicle> list(VehicleListDTO vehicleListDTO);

    /**
     * 用户根据id查询车辆
     * @param id
     * @return
     */
    VehicleVO getByIdForUser(Long id);
}
