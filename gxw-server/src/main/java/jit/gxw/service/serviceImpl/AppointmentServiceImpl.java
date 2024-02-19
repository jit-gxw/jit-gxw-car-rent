package jit.gxw.service.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jit.gxw.constant.*;
import jit.gxw.context.BaseContext;
import jit.gxw.dto.AppointmentPageQueryDTO;
import jit.gxw.dto.AppointmentRejectionDTO;
import jit.gxw.dto.AppointmentSubmitDTO;
import jit.gxw.dto.AppointmentUserPageQueryDTO;
import jit.gxw.entity.Appointment;
import jit.gxw.entity.Orders;
import jit.gxw.entity.User;
import jit.gxw.exception.AppointmentBusinessException;
import jit.gxw.exception.DeletionNotAllowedException;
import jit.gxw.exception.OrderBusinessException;
import jit.gxw.mapper.*;
import jit.gxw.result.PageResult;
import jit.gxw.service.AppointmentService;
import jit.gxw.utils.CalculateRentalPrice;
import jit.gxw.vo.AppointmentVO;
import jit.gxw.vo.OrderVO;
import jit.gxw.vo.VehicleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentMapper appointmentMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private VehicleMapper vehicleMapper;
    @Autowired
    private CalculateRentalPrice calculateRentalPrice;

    /**
     * 用户发起预约
     * @param appointmentSubmitDTO
     */
    @Override
    public void submit(AppointmentSubmitDTO appointmentSubmitDTO) {
        //查询该车预约时间是否冲突
        Orders build = Orders.builder()
                .vehicleId(appointmentSubmitDTO.getVehicleId())
                .collectionTime(appointmentSubmitDTO.getCollectionTime())
                .returnTime(appointmentSubmitDTO.getReturnTime())
                .build();
        List<Orders> list=orderMapper.selectForConfirm(build);
        log.info("{}",list.size());
        if(!list.isEmpty()){
            //冲突
            throw new OrderBusinessException(MessageConstant.APPOINTMENT_TIME_CONFLICT);
        }
        //查询用户是否有未完成的预约和订单
        //查询预约单
        if(appointmentMapper.selectByUserIdForConfirm(BaseContext.getCurrentId())!=null){

            throw new AppointmentBusinessException("存在待审核预约！");
        }else if (orderMapper.selectByUserIdForConfirm(BaseContext.getCurrentId())!=null){
            //是否有订单
            throw new OrderBusinessException("存在未完成订单！");

        }
        //判断用户信息是否正确
        User user = userMapper.selectById(BaseContext.getCurrentId());
        if(
                user.getStatus()==1
                ||user.getName()==null
                ||user.getIdNumber()==null
                ||user.getLicenceId()==null
                ||user.getPhone()==null
        ){
            throw new AppointmentBusinessException(MessageConstant.USER_INFORMATION_IS_INCOMPLETE);
        }
        //设置属性

        VehicleVO vehicleVO = vehicleMapper.selectByidWithClassName(appointmentSubmitDTO.getVehicleId());
        Appointment appointment = new Appointment();
        BeanUtils.copyProperties(appointmentSubmitDTO,appointment);
        appointment.setUserId(user.getId());
        appointment.setVehicleName(vehicleVO.getName());
        appointment.setLicensePlateNumber(vehicleVO.getLicensePlateNumber());
        appointment.setUserName(user.getName());
        appointment.setIdNumber(user.getIdNumber());
        appointment.setLicenceId(user.getLicenceId());
        appointment.setPhone(user.getPhone());
        appointment.setStatus(ApplyStatusConstant.TOBEREVIEWED);
        appointment.setAppointmentTime(LocalDateTime.now());


        //插入数据库
        appointmentMapper.insert(appointment);

    }

    /**
     * 搜索预约信息
     * @param appointmentPageQueryDTO
     * @return
     */
    @Override
    public PageResult conditionSearch(AppointmentPageQueryDTO appointmentPageQueryDTO) {
        PageHelper.startPage(appointmentPageQueryDTO.getPage(),appointmentPageQueryDTO.getPageSize());
        Page<Appointment> page=appointmentMapper.pageQuery(appointmentPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 拒绝预约
     * @param appointmentRejectionDTO
     */
    @Override
    public void rejection(AppointmentRejectionDTO appointmentRejectionDTO) {
        //判断状态预约状态
        if(
                appointmentMapper.getById(appointmentRejectionDTO.getId()).getStatus()!=0
        ){
                throw new AppointmentBusinessException(MessageConstant.APPOINTMENT_STATUS_ERROR);
        }
        Appointment appointment = new Appointment();
        //设置属性
        BeanUtils.copyProperties(appointmentRejectionDTO,appointment);

        appointment.setEmployeeId(BaseContext.getCurrentId());
        appointment.setEmployeeName(
                employeeMapper.getById(BaseContext.getCurrentId()).getName()
        );
        appointment.setStatus(ApplyStatusConstant.NOPASS);
        appointment.setCancelReason(MessageConstant.NO_PASS);
        appointment.setCancelTime(LocalDateTime.now());

        //更新表
        appointmentMapper.update(appointment);
    }

    /**
     * 通过审核
     * @param id
     * @return
     */
    @Override
    @Transactional(noRollbackForClassName="AppointmentBusinessException")
    public OrderVO pass(Long id) {
        //判断预约单状态
        Appointment appointment = appointmentMapper.getById(id);
        if(appointment.getStatus()!=0){
            throw new AppointmentBusinessException(MessageConstant.APPOINTMENT_STATUS_ERROR);
        }else if(//判断预约车辆是否可用
                orderMapper.selectByVehicleIdAndTimeForConfirm(
                        Orders.builder()
                                .vehicleId(appointment.getVehicleId())
                                .collectionTime(appointment.getCollectionTime())
                                .returnTime(appointment.getReturnTime())
                                .build()
                )!=null
        ){
            //被占用
            //不通过审核
            rejection(
                    AppointmentRejectionDTO.builder()
                            .id(id)
                            .rejectionReason(MessageConstant.APPOINTMENT_TIME_CONFLICT)
                            .build()
            );
            throw new AppointmentBusinessException(MessageConstant.APPOINTMENT_TIME_CONFLICT);
        }

        //设置预约属性
        appointment.setEmployeeId(BaseContext.getCurrentId());
        appointment.setEmployeeName(
                employeeMapper.getById(BaseContext.getCurrentId()).getName()
        );
        appointment.setStatus(ApplyStatusConstant.REVIEWED);
        appointment.setPassTime(LocalDateTime.now());

        //生成订单
        //计算预计费用
        VehicleVO vehicleVO = vehicleMapper.selectByidWithClassName(appointment.getVehicleId());
        BigDecimal amount = calculateRentalPrice.calculateRentalPrice(appointment.getCollectionTime(), appointment.getReturnTime(), vehicleVO.getPriceDay(), vehicleVO.getPriceMonth());

        Orders orders = Orders.builder()
                .number(String.valueOf(System.currentTimeMillis())
                        + String.format("%03d", appointment.getUserId())
                        + String.format("%03d", appointment.getVehicleId())
                        + String.format("%02d", BaseContext.getCurrentId()))
                .status(OrderStatusConstant.PENDINGDEPOSIT)
                .appointmentId(appointment.getId())
                .userId(appointment.getUserId())
                .vehicleId(appointment.getVehicleId())
                .vehicleName(appointment.getVehicleName())
                .licensePlateNumber(appointment.getLicensePlateNumber())
                .cashPledge(vehicleVO.getCashPledge())
                .collectionTime(appointment.getCollectionTime())
                .returnTime(appointment.getReturnTime())
                .remark(appointment.getAppointmentInfo())
                .payStatus(PayStatusConstant.NONPAYMENT)
                .cashPledgeStatus(PayStatusConstant.NONPAYMENT)
                .userName(appointment.getUserName())
                .idNumber(appointment.getIdNumber())
                .licenceId(appointment.getLicenceId())
                .phone(appointment.getPhone())
                .orderTime(LocalDateTime.now())
                .amount(amount)
                .build();
        //插入订单
        orderMapper.insert(orders);
        //更新预约单
        appointment.setNumber(orders.getNumber());
        appointmentMapper.update(appointment);
        //返回订单信息
        return OrderVO.builder()
                .id(orders.getId())
                .number(orders.getNumber())
                .build();
    }

    /**
     * 删除预约
     * @param id
     */
    @Override
    public void delete(Long id) {
        //查看预约状态
        Appointment appointment=appointmentMapper.selectById(id);
        if(appointment.getStatus()==0){
            throw new DeletionNotAllowedException(MessageConstant.APPOINTMENT_ON_TOBEREVIEWED);
        }
        appointment.setIsDel(TwoStatusConstant.DISABLE);
        //逻辑删除
        appointmentMapper.update(appointment);
    }

    /**
     * 用户取消预约
     * @param id
     */
    @Override
    public void cancel(Long id) {
        //查看预约单是否正常
        Appointment appointment = appointmentMapper.selectByUserIdForConfirm(BaseContext.getCurrentId());
        if(appointment==null||!Objects.equals(appointment.getId(), id)){
            throw new AppointmentBusinessException(MessageConstant.APPOINTMENT_NOT_FOUND);

        }
        //设置属性
        appointment.setStatus(ApplyStatusConstant.CANCELED);
        appointment.setCancelReason(MessageConstant.USER_CANCEL);
        appointment.setCancelTime(LocalDateTime.now());
        //修改预约单
        appointmentMapper.update(appointment);

    }

    /**
     * 用户分页查询本人预约单
     * @param appointmentUserPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(AppointmentUserPageQueryDTO appointmentUserPageQueryDTO) {
        PageHelper.startPage(appointmentUserPageQueryDTO.getPage(),appointmentUserPageQueryDTO.getPageSize());
        Page<AppointmentVO> page=appointmentMapper.pageQueryByUserId(BaseContext.getCurrentId());
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 获取待审核预约的数量
     * @return
     */
    @Override
    public Integer getStatusNumber() {


        return appointmentMapper.selectStatusNumber();
    }
}
