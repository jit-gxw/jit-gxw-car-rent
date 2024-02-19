package jit.gxw.service.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jit.gxw.constant.FaultStatusConstant;
import jit.gxw.constant.InformantRoleConstant;
import jit.gxw.constant.MessageConstant;
import jit.gxw.constant.VehicleStatusConstant;
import jit.gxw.context.BaseContext;
import jit.gxw.dto.FaultCompleteDTO;
import jit.gxw.dto.FaultDTO;
import jit.gxw.dto.FaultPageQueryDTO;
import jit.gxw.dto.FaultStartDTO;
import jit.gxw.entity.Fault;
import jit.gxw.entity.Vehicle;
import jit.gxw.exception.BaseException;
import jit.gxw.mapper.EmployeeMapper;
import jit.gxw.mapper.FaultMapper;
import jit.gxw.mapper.VehicleMapper;
import jit.gxw.result.PageResult;
import jit.gxw.service.FaultService;
import jit.gxw.vo.VehicleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
public class FaultServiceImpl implements FaultService {
    @Autowired
    private FaultMapper faultMapper;
    @Autowired
    private VehicleMapper vehicleMapper;
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 故障报修
     * @param faultDTO
     */
    @Transactional
    @Override
    public void submit(FaultDTO faultDTO) {
        Fault fault = new Fault();
        BeanUtils.copyProperties(faultDTO,fault);
        //查看车辆状态
        VehicleVO vehicleVO = vehicleMapper.selectByidWithClassName(fault.getVehicleId());
        if(vehicleVO.getStatus()==VehicleStatusConstant.REPAIRED){
            throw new BaseException(MessageConstant.VEHICLE_ON_REPAIRED);
        }

        //设置属性
        fault.setLicensePlateNumber(vehicleVO.getLicensePlateNumber());
        fault.setInformantRole(InformantRoleConstant.EMPLOYEE);
        fault.setInformantId(BaseContext.getCurrentId());
        fault.setStatus(FaultStatusConstant.DECLARED);
        fault.setNotificationTime(LocalDateTime.now());
        //插入报修表
        faultMapper.insert(fault);
        //修改车辆状态
        vehicleMapper.update(Vehicle.builder()
                .id(faultDTO.getVehicleId())
                .status(VehicleStatusConstant.REPAIRED)
                .build()
        );

    }

    /**
     * 故障详细搜索
     * @param faultPageQueryDTO
     * @return
     */
    @Override
    public PageResult conditionSearch(FaultPageQueryDTO faultPageQueryDTO) {
        PageHelper.startPage(faultPageQueryDTO.getPage(), faultPageQueryDTO.getPageSize());
        Page<Fault> page= faultMapper.pageQuery(faultPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 开始维修
     * @param faultStartDTO
     */
    @Override
    public void startRepair(FaultStartDTO faultStartDTO) {
        //查看故障单状态

        Fault ft=faultMapper.slectById(faultStartDTO.getId());
        if (Objects.equals(ft.getStatus(), FaultStatusConstant.DECLARED)){
            Fault fault = Fault.builder()
                    .id(faultStartDTO.getId())
                    .status(FaultStatusConstant.REPAIRING)
                    .employeeId(BaseContext.getCurrentId())
                    .employeeName(
                            employeeMapper.getById(BaseContext.getCurrentId()).getName()
                    )
                    .repairTime(LocalDateTime.now())
                    .build();
            faultMapper.update(fault);
        }else {
            throw new BaseException("故障单状态错误");
        }
    }

    /**
     * 完成维修
     * @param faultCompleteDTO
     */
    @Override
    @Transactional
    public void complete(FaultCompleteDTO faultCompleteDTO) {
        //查看故障单状态
        Fault ft=faultMapper.slectById(faultCompleteDTO.getId());
        if(!Objects.equals(ft.getEmployeeId(), BaseContext.getCurrentId())){
            throw new BaseException("开始维修人员需与完成维修人员一致！");
        }
        if (Objects.equals(ft.getStatus(), FaultStatusConstant.REPAIRING)){
            Fault fault = Fault.builder()
                    .id(faultCompleteDTO.getId())
                    .status(FaultStatusConstant.COMPLETED)
                    .reason(faultCompleteDTO.getReason())
                    .completionTime(LocalDateTime.now())
                    .solution(faultCompleteDTO.getSolution())
                    .cost(faultCompleteDTO.getCost())
                    .build();
            faultMapper.update(fault);
            //设置车辆状态
            vehicleMapper.update(Vehicle.builder()
                    .id(ft.getVehicleId())
                    .status(VehicleStatusConstant.DISABLE)
                    .build()
            );
        }else {
            throw new BaseException("故障单状态错误");
        }
    }

    /**
     * 维修失败
     * @param faultCompleteDTO
     */
    @Override
    @Transactional
    public void fail(FaultCompleteDTO faultCompleteDTO) {
        //查看故障单状态
        Fault ft=faultMapper.slectById(faultCompleteDTO.getId());
        if(!Objects.equals(ft.getEmployeeId(), BaseContext.getCurrentId())){
            throw new BaseException("开始维修人员需与完成维修人员一致！");
        }
        if (Objects.equals(ft.getStatus(), FaultStatusConstant.REPAIRING)){
            Fault fault = Fault.builder()
                    .id(faultCompleteDTO.getId())
                    .status(FaultStatusConstant.FAILED)
                    .reason(faultCompleteDTO.getReason())
                    .completionTime(LocalDateTime.now())
                    .cost(faultCompleteDTO.getCost())
                    .build();
            faultMapper.update(fault);
            //设置车辆状态
            vehicleMapper.update(Vehicle.builder()
                    .id(ft.getVehicleId())
                    .status(VehicleStatusConstant.SCRAPPED)
                    .build()
            );
        }else {
            throw new BaseException("故障单状态错误");
        }
    }

    @Override
    public void deleteById(Long id) {
        //检查故障单状态
        Fault fault = faultMapper.slectById(id);
        if(Objects.equals(fault.getStatus(), FaultStatusConstant.COMPLETED) ||
                Objects.equals(fault.getStatus(), FaultStatusConstant.FAILED)){
            fault.setIsDel(1);
            faultMapper.update(fault);
        }else {
            throw new BaseException("维修未完成，无法删除");
        }
    }
}
