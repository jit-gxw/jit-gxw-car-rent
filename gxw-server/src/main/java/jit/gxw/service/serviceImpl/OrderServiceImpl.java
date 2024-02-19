package jit.gxw.service.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jit.gxw.constant.*;
import jit.gxw.context.BaseContext;
import jit.gxw.dto.OrderAmountDTO;
import jit.gxw.dto.OrderCancelDTO;
import jit.gxw.dto.OrderPageQueryDTO;
import jit.gxw.dto.OrderUserPageDTO;
import jit.gxw.entity.Orders;
import jit.gxw.entity.Relet;
import jit.gxw.entity.Vehicle;
import jit.gxw.exception.DeletionNotAllowedException;
import jit.gxw.exception.OrderBusinessException;
import jit.gxw.mapper.OrderMapper;
import jit.gxw.mapper.ReletMapper;
import jit.gxw.mapper.VehicleMapper;
import jit.gxw.result.PageResult;
import jit.gxw.service.OrderService;
import jit.gxw.utils.CalculateRentalPrice;
import jit.gxw.vo.OrderUserVO;
import jit.gxw.vo.OrderStatisticsVO;
import jit.gxw.vo.VehicleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CalculateRentalPrice calculateRentalPrice;
    @Autowired
    private VehicleMapper vehicleMapper;
    @Autowired
    private ReletMapper reletMapper;

    /**
     * 分页查询订单信息
     * @param orderPageQueryDTO
     * @return
     */
    @Override
    public PageResult conditionSearch(OrderPageQueryDTO orderPageQueryDTO) {
        PageHelper.startPage(orderPageQueryDTO.getPage(),orderPageQueryDTO.getPageSize());
        Page<Orders> page=orderMapper.pageQuery(orderPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 订单现金支付
     * @param id
     */
    @Override
    public void paymentByCash(Long id) {
        //查询订单状态
        Orders order=orderMapper.selectById(id);
        //判断是否有订单
        if(order==null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);

        }
        //判断订单状态
        if(Objects.equals(order.getStatus(), OrderStatusConstant.PENDINGDEPOSIT)){
            //支付押金
            //设置属性
            order.setStatus(OrderStatusConstant.WAITINGFORCOLLECTION);
            order.setCashPledgeStatus(PayStatusConstant.PAID);
            //修改订单
            orderMapper.update(order);
        }else if(Objects.equals(order.getStatus(), OrderStatusConstant.WAITINGFORPAYMENT)){
            //支付订单
            //设置属性
            order.setStatus(OrderStatusConstant.COMPLETED);
            order.setPayStatus(PayStatusConstant.PAID);
            order.setCashPledgeStatus(PayStatusConstant.REFUNDED);
            order.setCompletionTime(LocalDateTime.now());
            //修改订单
            orderMapper.update(order);
        }else {
            //状态错误
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }



    }

    /**
     * 确认租车
     * @param id
     */
    @Override
    @Transactional
    public void confirm(Long id) {
        //查询订单是否存在 状态是否正确
        Orders orders = orderMapper.selectById(id);
        if(orders==null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }else if(!Objects.equals(orders.getStatus(), OrderStatusConstant.WAITINGFORCOLLECTION)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        //设置属性
        orders.setStatus(OrderStatusConstant.RENTING);
        orders.setCollectionTime(LocalDateTime.now());
        //修改订单信息
        orderMapper.update(orders);
        //设置车辆状态
        VehicleVO vehicleVO = vehicleMapper.selectByidWithClassName(orders.getVehicleId());
        //判断车辆是否可用
        if (!Objects.equals(vehicleVO.getStatus(), VehicleStatusConstant.ENABLE)){
            throw new OrderBusinessException(MessageConstant.VEHICLE_ON_NOT_ENABLE);
        }
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleVO,vehicle);
        vehicle.setStatus(VehicleStatusConstant.USED);
        //修改车辆状态
        vehicleMapper.update(vehicle);
    }
    /**
     * 确认还车
     * @param id
     */
    @Override
    @Transactional
    public void returnVehicle(Long id) {
        //查询订单是否存在 状态是否正确
        Orders orders = orderMapper.selectById(id);
        if(orders==null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }else if(!Objects.equals(orders.getStatus(), OrderStatusConstant.RENTING)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        //查看是否有未审核的续租单
        Relet relet = reletMapper.selectByOrderIdWithWait(orders.getId());
        if(relet!=null){
            //自动取消续租单
            //设置属性
            relet.setStatus(ApplyStatusConstant.CANCELED);
            relet.setCancelReason("已还车");
            relet.setCancelTime(LocalDateTime.now());
            //更新续租表
            reletMapper.update(relet);
        }
        //设置属性
        orders.setStatus(OrderStatusConstant.RETURNED);
        orders.setReturnTime(LocalDateTime.now());
        //查询订单车辆的费用
        VehicleVO vehicleVO = vehicleMapper.selectByidWithClassName(orders.getVehicleId());
        //计算租车费用
        BigDecimal amount=calculateRentalPrice.calculateRentalPrice(
                orders.getCollectionTime(),
                orders.getReturnTime(),
                vehicleVO.getPriceDay(),
                vehicleVO.getPriceMonth()
        );
        orders.setAmount(amount);
        //修改订单信息
        orderMapper.update(orders);
        //修改车辆属性
        //判断车辆状态是否正常
        if (!Objects.equals(vehicleVO.getStatus(), VehicleStatusConstant.USED)){
            throw new OrderBusinessException(MessageConstant.VEHICLE_STATUS_ERROR);
        }
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(vehicleVO,vehicle);
        vehicle.setStatus(VehicleStatusConstant.ENABLE);
        //修改车辆状态
        vehicleMapper.update(vehicle);
    }

    /**
     * 确认最终订单金额
     * @param orderAmountDTO
     */
    @Override
    public void amount(OrderAmountDTO orderAmountDTO) {
        //查询订单是否存在 状态是否正确
        Orders orders = orderMapper.selectById(orderAmountDTO.getId());
        if(orders==null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }else if(!Objects.equals(orders.getStatus(), OrderStatusConstant.RETURNED)){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        //设置订单属性
        orders.setStatus(OrderStatusConstant.WAITINGFORPAYMENT);
        orders.setExtraCharges(orderAmountDTO.getExtraCharges());
        orders.setAmount(orders.getAmount().add(orderAmountDTO.getExtraCharges()));
        //修改订单
        orderMapper.update(orders);
    }
    /**
     * 取消订单
     * @param orderCancelDTO
     */
    @Override
    @Transactional
    public void cancel(OrderCancelDTO orderCancelDTO) {
        //查询订单是否存在
        Orders orders = orderMapper.selectById(orderCancelDTO.getId());
        if(orders==null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        //查看是否有未审核的续租单
        Relet relet = reletMapper.selectByOrderIdWithWait(orders.getId());
        if(relet!=null){
            //自动取消续租单
            //设置属性
            relet.setStatus(ApplyStatusConstant.CANCELED);
            relet.setCancelReason("订单已被商家取消");
            relet.setCancelTime(LocalDateTime.now());
            //更新续租表
            reletMapper.update(relet);
        }

        if (Objects.equals(orders.getStatus(), OrderStatusConstant.PENDINGDEPOSIT)){
            //待付押金
            //设置属性
            orders.setStatus(OrderStatusConstant.CANCELED);
            orders.setCancelTime(LocalDateTime.now());
            orders.setCancelReason(orderCancelDTO.getCancelReason());
        }else if(Objects.equals(orders.getStatus(), OrderStatusConstant.WAITINGFORCOLLECTION)){
            //待取车
            //设置属性
            orders.setStatus(OrderStatusConstant.CANCELED);
            orders.setCancelTime(LocalDateTime.now());
            orders.setCancelReason(orderCancelDTO.getCancelReason());
            //退还押金
            orders.setCashPledgeStatus(PayStatusConstant.REFUNDED);
        }else if(Objects.equals(orders.getStatus(), OrderStatusConstant.RETURNED)){
            //已还车
            //设置属性
            orders.setStatus(OrderStatusConstant.CANCELED);
            orders.setCancelTime(LocalDateTime.now());
            orders.setCancelReason(orderCancelDTO.getCancelReason());
            //退还押金
            orders.setCashPledgeStatus(PayStatusConstant.REFUNDED);
        }else if(Objects.equals(orders.getStatus(), OrderStatusConstant.WAITINGFORPAYMENT)){
            //待付款
            //设置属性
            orders.setStatus(OrderStatusConstant.CANCELED);
            orders.setCancelTime(LocalDateTime.now());
            orders.setCancelReason(orderCancelDTO.getCancelReason());
            //退还押金
            orders.setCashPledgeStatus(PayStatusConstant.REFUNDED);
        }else if(Objects.equals(orders.getStatus(), OrderStatusConstant.COMPLETED)
                &&Objects.equals(orders.getPayStatus(), PayStatusConstant.PAID)){
            //已完成，进行订单退款
            //设置属性
            orders.setStatus(OrderStatusConstant.CANCELED);
            orders.setCancelTime(LocalDateTime.now());
            orders.setCancelReason(orderCancelDTO.getCancelReason());
            //退还订单金额
            orders.setPayStatus(PayStatusConstant.REFUNDED);
        }
        else {
            // 状态不正确
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        //修改订单表
        orderMapper.update(orders);

    }

    /**
     * 根据id删除订单
     * @param id
     */
    @Override
    public void delete(Long id) {
        //查询订单是否存在
        Orders orders = orderMapper.selectById(id);
        if(orders==null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }else if(
                Objects.equals(orders.getStatus(), OrderStatusConstant.PENDINGDEPOSIT)
                ||Objects.equals(orders.getStatus(), OrderStatusConstant.WAITINGFORCOLLECTION)
                        ||Objects.equals(orders.getStatus(), OrderStatusConstant.RENTING)
                        ||Objects.equals(orders.getStatus(), OrderStatusConstant.RETURNED)
                        ||Objects.equals(orders.getStatus(), OrderStatusConstant.WAITINGFORPAYMENT)

        ){
            throw new DeletionNotAllowedException(MessageConstant.ORDER_ON_NOT_COMPLETE);
        }
        //设置属性
        orders.setIsDel(TwoStatusConstant.DISABLE);
        //逻辑删除订单
        orderMapper.update(orders);
    }

    /**
     * 各个状态的订单数量统计
     * @return
     */
    @Override
    public OrderStatisticsVO statistics() {
        // 根据状态，分别查询数量
        Integer pendingDeposit=orderMapper.countStatus(OrderStatusConstant.PENDINGDEPOSIT);
        Integer waitingForCollection=orderMapper.countStatus(OrderStatusConstant.WAITINGFORCOLLECTION);
        Integer renting=orderMapper.countStatus(OrderStatusConstant.RENTING);
        Integer returned=orderMapper.countStatus(OrderStatusConstant.RETURNED);
        Integer waitingForPayment=orderMapper.countStatus(OrderStatusConstant.WAITINGFORPAYMENT);

        //封装OrderStatisticsVO
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setPendingDeposit(pendingDeposit);
        orderStatisticsVO.setWaitingForCollection(waitingForCollection);
        orderStatisticsVO.setRenting(renting);
        orderStatisticsVO.setReturned(returned);
        orderStatisticsVO.setWaitingForPayment(waitingForPayment);

        return orderStatisticsVO;
    }

    /**
     * 用户历史订单查询
     * @param orderUserPageDTO
     * @return
     */
    @Override
    public PageResult historyOrders(OrderUserPageDTO orderUserPageDTO) {
        PageHelper.startPage(orderUserPageDTO.getPage(),orderUserPageDTO.getPageSize());
        orderUserPageDTO.setUserId(BaseContext.getCurrentId());
        if(orderUserPageDTO.getStatus()==7){
            orderUserPageDTO.setStatus(null);
        }
        Page<OrderUserVO> page=orderMapper.historyOrders(orderUserPageDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 用户查询订单详情
     * @param id
     * @return
     */
    @Override
    public OrderUserVO orderDetail(Long id) {


        return orderMapper.selectByIdAndUserId(id,BaseContext.getCurrentId());
    }

    /**
     * 取消订单
     * @param id
     */
    @Override
    @Transactional
    public void cancelWithUser(Long id) {
        //查询订单信息
        Orders orders = orderMapper.selectById(id);
        //查看是否有未审核的续租单
        Relet relet = reletMapper.selectByOrderIdWithWait(orders.getId());
        if(relet!=null){
            //自动取消续租单
            //设置属性
            relet.setStatus(ApplyStatusConstant.CANCELED);
            relet.setCancelReason("订单已被用户取消");
            relet.setCancelTime(LocalDateTime.now());
            //更新续租表
            reletMapper.update(relet);
        }
        //判断订单状态
        if(Objects.equals(orders.getStatus(), OrderStatusConstant.PENDINGDEPOSIT)){
            //待付押金
            orders.setStatus(OrderStatusConstant.CANCELED);
            orders.setCancelTime(LocalDateTime.now());
            orders.setCancelReason(MessageConstant.USER_CANCEL);

        }else if(
                Objects.equals(orders.getStatus(), OrderStatusConstant.WAITINGFORCOLLECTION)
        ){
            //待取车
            orders.setStatus(OrderStatusConstant.CANCELED);
            orders.setCancelTime(LocalDateTime.now());
            orders.setCancelReason(MessageConstant.USER_CANCEL);
            //退还押金
            orders.setCashPledgeStatus(PayStatusConstant.REFUNDED);

        }else {
            //其他状态
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);

        }
        //更新订单表
        orderMapper.update(orders);


    }

    /**
     * 用户支付订单
     * @param number
     */
    @Override
    public void payment(String number) {
        //查询订单
        Orders order=orderMapper.selectByNumber(number);
        //判断订单状态
        if(Objects.equals(order.getStatus(), OrderStatusConstant.PENDINGDEPOSIT)){
            //支付押金
            //设置属性
            order.setStatus(OrderStatusConstant.WAITINGFORCOLLECTION);

            //支付
            order.setCashPledgeStatus(PayStatusConstant.PAID);

        }else if(Objects.equals(order.getStatus(), OrderStatusConstant.WAITINGFORPAYMENT)){
            //支付订单
            //设置属性
            order.setStatus(OrderStatusConstant.COMPLETED);

            //支付
            order.setPayStatus(PayStatusConstant.PAID);
            order.setCashPledgeStatus(PayStatusConstant.REFUNDED);
            order.setCompletionTime(LocalDateTime.now());

        }else {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);

        }
        //修改订单表
        orderMapper.update(order);
    }


}
