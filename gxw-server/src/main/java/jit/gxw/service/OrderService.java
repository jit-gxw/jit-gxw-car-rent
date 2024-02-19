package jit.gxw.service;

import jit.gxw.dto.OrderAmountDTO;
import jit.gxw.dto.OrderCancelDTO;
import jit.gxw.dto.OrderPageQueryDTO;
import jit.gxw.dto.OrderUserPageDTO;
import jit.gxw.result.PageResult;
import jit.gxw.vo.OrderStatisticsVO;
import jit.gxw.vo.OrderUserVO;

public interface OrderService {

    /**
     * 分页查询订单信息
     * @param orderPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrderPageQueryDTO orderPageQueryDTO);

    /**
     * 订单现金支付
     * @param id
     */
    void paymentByCash(Long id);

    /**
     * 确认租车
     * @param id
     */
    void confirm(Long id);

    /**
     * 确认还车
     * @param id
     */
    void returnVehicle(Long id);

    /**
     * 确认最终订单金额
     * @param orderAmountDTO
     */
    void amount(OrderAmountDTO orderAmountDTO);

    /**
     * 取消订单
     * @param orderCancelDTO
     */
    void cancel(OrderCancelDTO orderCancelDTO);

    /**
     * 根据id删除订单
     * @param id
     */
    void delete(Long id);

    /**
     * 各个状态的订单数量统计
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * 用户历史订单查询
     * @param orderUserPageDTO
     * @return
     */
    PageResult historyOrders(OrderUserPageDTO orderUserPageDTO);

    /**
     * 用户查询订单详情
     * @param id
     * @return
     */
    OrderUserVO orderDetail(Long id);

    /**
     * 用户取消订单
     * @param id
     */
    void cancelWithUser(Long id);

    /**
     * 用户支付订单
     * @param number
     */
    void payment(String number);
}
