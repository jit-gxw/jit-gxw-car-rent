package jit.gxw.service.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jit.gxw.constant.MessageConstant;
import jit.gxw.constant.VehicleStatusConstant;
import jit.gxw.context.BaseContext;
import jit.gxw.dto.VehicleListDTO;
import jit.gxw.dto.VehiclePageQueryDTO;
import jit.gxw.entity.Vehicle;
import jit.gxw.exception.DeletionNotAllowedException;
import jit.gxw.exception.StatusNotAllowedException;
import jit.gxw.mapper.OrderMapper;
import jit.gxw.mapper.VehicleMapper;
import jit.gxw.result.PageResult;
import jit.gxw.service.VehicleService;
import jit.gxw.vo.VehicleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleMapper vehicleMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderMapper orderMapper;


    @Override
    public PageResult pageQuery(VehiclePageQueryDTO vehiclePageQueryDTO) {
        PageHelper.startPage(vehiclePageQueryDTO.getPage(),vehiclePageQueryDTO.getPageSize());
        Page<VehicleVO> page=vehicleMapper.pageQuery(vehiclePageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }
    /**
     * 添加车辆
     * @param vehicle
     */
    @Override
    public void save(Vehicle vehicle) {
        //设置属性
        vehicle.setStatus(VehicleStatusConstant.DISABLE);
        vehicle.setCreateTime(LocalDateTime.now());
        vehicle.setCreateUser(BaseContext.getCurrentId());

        vehicle.setUpdateTime(LocalDateTime.now());
        vehicle.setUpdateUser(BaseContext.getCurrentId());
        //添加车辆
        vehicleMapper.insert(vehicle);
    }
    /**
     * 根据id查询车辆
     * @param id
     * @return
     */
    @Override
    public VehicleVO getByIdmWithClassName(Long id) {
        return vehicleMapper.selectByidWithClassName(id);
    }

    /**
     * 修改车辆信息
     * @param vehicleVO
     */
    @Override
    public void updata(VehicleVO vehicleVO) {
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleVO,vehicle);

        //设置属性
        vehicle.setUpdateTime(LocalDateTime.now());
        vehicle.setUpdateUser(BaseContext.getCurrentId());

        vehicleMapper.update(vehicle);
    }
    /**
     * 设置车辆状态
     * @param status
     * @param id
     */
    @Override
    public void setStatus(Integer status, Long id) {
        //查询该车辆状态
        VehicleVO vehicleVO = vehicleMapper.selectByidWithClassName(id);
        if (vehicleVO.getStatus()==2||vehicleVO.getStatus()==3||vehicleVO.getStatus()==4){
            throw new StatusNotAllowedException(MessageConstant.VEHICLE_NOT_ON_ENABLE);

        }
        //修改状态
        Vehicle vehicle = Vehicle.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId()).build();
        vehicleMapper.update(vehicle);
    }
    /**
     * 根据id删除车辆
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        //查询车辆状态
        VehicleVO vehicleVO = vehicleMapper.selectByidWithClassName(id);
        //判断是否可以删除
        if(vehicleVO.getStatus()==0||vehicleVO.getStatus()==2||vehicleVO.getStatus()==3){
            throw new DeletionNotAllowedException(MessageConstant.VEHICLE_ON_ENABLE);
        }
        //逻辑删除
        vehicleMapper.update(Vehicle.builder().id(id).isDel(1).build());


    }

    /**
     * 用户查询车辆
     * @param vehicleListDTO
     * @return
     */
    @Override
    public List<Vehicle> list(VehicleListDTO vehicleListDTO) {
        //查询所有正常状态车辆
        String key="VehicleByClass::vehicle_"+vehicleListDTO.getClassificationId();
        //查询redis中是否有车辆信息
        List<Vehicle> list=(List<Vehicle>)redisTemplate.opsForValue().get(key);
        if (list==null){
            //如果没有查询数据库并添加redis
            Vehicle vehicle = Vehicle.builder()
                    .classificationId(vehicleListDTO.getClassificationId())
                    .status(VehicleStatusConstant.ENABLE)
                    .build();
            list=vehicleMapper.selectForUser(vehicle);
            //存入redis中
            log.info("存入redis{}",key);
            redisTemplate.opsForValue().set(key,list);
        }
        //根据租赁日期排除车辆
        //根据日期查询订单返回车辆id
        List<Long> idList=orderMapper.selectVehicelIdByTime(vehicleListDTO.getBegin(),vehicleListDTO.getEnd());

        //根据id排除list中的车辆
        Iterator<Vehicle> iterator = list.iterator();
        while (iterator.hasNext()) {
            Vehicle vehicle = iterator.next();
            for (Long l : idList) {
                if (Objects.equals(vehicle.getId(), l)) {
                    iterator.remove();
                }
            }
        }


        //返回车辆结果
        return list;
    }

    /**
     * 用户根据id查询车辆
     * @param id
     * @return
     */
    @Override
    public VehicleVO getByIdForUser(Long id) {
        return vehicleMapper.selectByIdForUser(id);
    }
}
