package jit.gxw.service.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jit.gxw.constant.ApplyStatusConstant;
import jit.gxw.constant.MessageConstant;
import jit.gxw.constant.OrderStatusConstant;
import jit.gxw.constant.TwoStatusConstant;
import jit.gxw.context.BaseContext;
import jit.gxw.dto.*;
import jit.gxw.entity.Orders;
import jit.gxw.entity.Relet;
import jit.gxw.exception.DeletionNotAllowedException;
import jit.gxw.exception.OrderBusinessException;
import jit.gxw.exception.ReletBusinessException;
import jit.gxw.mapper.EmployeeMapper;
import jit.gxw.mapper.OrderMapper;
import jit.gxw.mapper.ReletMapper;
import jit.gxw.result.PageResult;
import jit.gxw.service.ReletService;
import jit.gxw.vo.OrderUserVO;
import jit.gxw.vo.ReletVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

@Slf4j
@Service
public class ReletServiceImpl implements ReletService {
    @Autowired
    private ReletMapper reletMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private EmployeeMapper employeeMapper;


    /**
     * 用户发起续租
     * @param reletSubmitDTO
     */
    @Override
    public void submit(ReletSubmitDTO reletSubmitDTO) {
        //查询用户订单状态
        Orders userOrder = orderMapper.selectById(reletSubmitDTO.getOrdersId());
        if(
                Objects.equals(userOrder.getStatus(), OrderStatusConstant.RETURNED)
                ||Objects.equals(userOrder.getStatus(), OrderStatusConstant.WAITINGFORPAYMENT)
                        ||Objects.equals(userOrder.getStatus(), OrderStatusConstant.COMPLETED)
                        ||Objects.equals(userOrder.getStatus(), OrderStatusConstant.CANCELED)
        ){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }else if(userOrder.getReletStatus()==1){
            throw new ReletBusinessException("同一订单只能续租一次！");
        }
        //查询是否已有待审核的续租单
        Relet reletCof=reletMapper.selectByOrderIdWithWait(userOrder.getId());
        if(reletCof!=null){
            throw new ReletBusinessException(MessageConstant.ALREADY_EXISTS);
        }

        //查询续租时间是否冲突
        Orders orders = Orders.builder()
                .id(userOrder.getId())
                .vehicleId(userOrder.getVehicleId())
                .collectionTime(userOrder.getReturnTime().plusMinutes(1))
                .returnTime(reletSubmitDTO.getReturnTime())
                .build();
        Orders selectByVehicleIdAndTimeForConfirm = orderMapper.selectByVehicleIdAndTimeForConfirm(orders);
        if(selectByVehicleIdAndTimeForConfirm!=null){
            throw new OrderBusinessException(MessageConstant.APPOINTMENT_TIME_CONFLICT);
        //查询续租还车时间是否正确
        }else if(userOrder.getReturnTime().isAfter(reletSubmitDTO.getReturnTime())){
            throw new ReletBusinessException("续租还车时间错误");

        }        //设置属性
        Relet relet = Relet.builder()
                .ordersId(userOrder.getId())
                .number(userOrder.getNumber())
                .status(ApplyStatusConstant.TOBEREVIEWED)
                .returnTime(reletSubmitDTO.getReturnTime())
                .reletInfo(reletSubmitDTO.getReletInfo())
                .reletTime(LocalDateTime.now())
                .build();
        //插入数据库
        reletMapper.insert(relet);
    }

    /**
     * 条件搜索续租单
     * @param reletConditionSearchDTO
     * @return
     */
    @Override
    public PageResult conditionSearch(ReletConditionSearchDTO reletConditionSearchDTO) {
        PageHelper.startPage(reletConditionSearchDTO.getPage(), reletConditionSearchDTO.getPageSize());
        Page<ReletVO> page=reletMapper.selectWithCondition(reletConditionSearchDTO);
        Page<Orders> orders = orderMapper.pageQuery(new OrderPageQueryDTO());
        //查询order表赋值
        for (ReletVO reletVO : page.getResult()) {
            for (Orders order : orders) {
                if(Objects.equals(reletVO.getOrdersId(), order.getId())){
                    reletVO.setOrder(order);
                    break;
                }
            }
        }

        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 拒绝续租
     * @param reletRejectionDTO
     */
    @Override
    public void rejection(ReletRejectionDTO reletRejectionDTO) {
        //判断续租单状态
        Relet relet=reletMapper.selectById(reletRejectionDTO.getId());
        if(relet==null){
            throw new ReletBusinessException(MessageConstant.RELET_NOT_FOUND);

        }else if(!Objects.equals(relet.getStatus(), ApplyStatusConstant.TOBEREVIEWED)){
            throw new ReletBusinessException(MessageConstant.RELET_STATUS_ERROR);
        }

        //设置属性
        relet.setStatus(ApplyStatusConstant.NOPASS);
        relet.setEmployeeId(BaseContext.getCurrentId());
        relet.setEmployeeName(employeeMapper.getById(BaseContext.getCurrentId()).getName());
        relet.setCancelReason(MessageConstant.NO_PASS);
        relet.setRejectionReason(reletRejectionDTO.getRejectionReason());
        relet.setCancelTime(LocalDateTime.now());
        //修改续租表
        reletMapper.update(relet);
    }

    /**
     * 通过申请
     * @param id
     */
    @Override
    @Transactional
    public void pass(Long id) {
        //判断续租单状态
        Relet relet=reletMapper.selectById(id);
        if(relet==null){
            throw new ReletBusinessException(MessageConstant.RELET_NOT_FOUND);

        }else if(!Objects.equals(relet.getStatus(), ApplyStatusConstant.TOBEREVIEWED)){
            throw new ReletBusinessException(MessageConstant.RELET_STATUS_ERROR);
        }
        //判断订单状态
        Orders order = orderMapper.selectById(relet.getOrdersId());
        if(
                Objects.equals(order.getStatus(), OrderStatusConstant.RETURNED)
                ||Objects.equals(order.getStatus(), OrderStatusConstant.WAITINGFORPAYMENT)
                        ||Objects.equals(order.getStatus(), OrderStatusConstant.COMPLETED)
                        ||Objects.equals(order.getStatus(), OrderStatusConstant.CANCELED)
        ){
            throw new ReletBusinessException("订单已不可续租");

        }
        //修改订单表
        order.setReletStatus(TwoStatusConstant.DISABLE);
        order.setReletId(relet.getId());
        order.setReturnTime(relet.getReturnTime());
        orderMapper.update(order);
        //修改续租表
        relet.setStatus(ApplyStatusConstant.REVIEWED);
        relet.setEmployeeId(BaseContext.getCurrentId());
        relet.setEmployeeName(employeeMapper.getById(BaseContext.getCurrentId()).getName());
        relet.setPassTime(LocalDateTime.now());
        reletMapper.update(relet);

    }

    /**
     * id删除续租单
     * @param id
     */
    @Override
    public void delete(Long id) {
        //判断续租单状态
        Relet relet=reletMapper.selectById(id);
        if(relet==null){
            throw new ReletBusinessException(MessageConstant.RELET_NOT_FOUND);

        }else if(Objects.equals(relet.getStatus(), ApplyStatusConstant.TOBEREVIEWED)){
            throw new DeletionNotAllowedException(MessageConstant.RELET_STATUS_ERROR);
        }
        //逻辑删除
        relet.setIsDel(TwoStatusConstant.DISABLE);
        reletMapper.update(relet);
    }

    /**
     * 用户分页查询
     * @param reletPageQueryDTO
     */
    @Override
    public PageResult pageQuery(ReletPageQueryDTO reletPageQueryDTO) {
        PageHelper.startPage(reletPageQueryDTO.getPage(), reletPageQueryDTO.getPageSize());
        Page<ReletVO> page=reletMapper.pageQuery(BaseContext.getCurrentId());
        //赋值order
        OrderUserPageDTO orderUserPageDTO = new OrderUserPageDTO();
        orderUserPageDTO.setUserId(BaseContext.getCurrentId());
        Page<OrderUserVO> orderUserVOS = orderMapper.historyOrders(orderUserPageDTO);
        for (ReletVO reletVO : page) {
            for (OrderUserVO orderUserVO : orderUserVOS) {
                if(Objects.equals(reletVO.getOrdersId(), orderUserVO.getId())){
                    Orders order=new Orders();
                    BeanUtils.copyProperties(orderUserVO,order);
                    reletVO.setOrder(order);
                    break;
                }
            }
        }

        return new PageResult(page.getTotal(), page.getResult());

    }

    /**
     * 用户取消续租
     * @param id
     */
    @Override
    public void cancel(Long id) {
        //判断续租单状态
        Relet relet=reletMapper.selectById(id);
        if(relet==null){
            throw new ReletBusinessException(MessageConstant.RELET_NOT_FOUND);

        }else if(!Objects.equals(relet.getStatus(), ApplyStatusConstant.TOBEREVIEWED)){
            throw new DeletionNotAllowedException(MessageConstant.RELET_STATUS_ERROR);
        }
        //设置属性
        relet.setStatus(ApplyStatusConstant.CANCELED);
        relet.setCancelReason(MessageConstant.USER_CANCEL);
        relet.setCancelTime(LocalDateTime.now());
        //更新续租表
        reletMapper.update(relet);
    }

    /**
     * 获取待审核的续租单数量
     * @return
     */
    @Override
    public Integer getStatusNumber() {
        return reletMapper.selectStatusNumber();
    }
}
