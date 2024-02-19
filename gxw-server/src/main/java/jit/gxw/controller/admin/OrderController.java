package jit.gxw.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jit.gxw.dto.*;
import jit.gxw.result.PageResult;
import jit.gxw.result.Result;
import jit.gxw.service.AppointmentService;
import jit.gxw.service.OrderService;
import jit.gxw.vo.OrderStatisticsVO;
import jit.gxw.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController("OrderController")
@RequestMapping("/admin/order")
@Api(tags = "管理端订单接口")
@Slf4j
@PreAuthorize("hasAuthority('admin:order')")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 分页查询订单信息
     * @param orderPageQueryDTO
     * @return
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("分页查询订单信息")
    public Result<PageResult> conditionSearch(OrderPageQueryDTO orderPageQueryDTO){
        log.info("分页查询订单信息：{}",orderPageQueryDTO);
        PageResult pageResult=orderService.conditionSearch(orderPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 订单现金支付
     * @param id
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单现金支付")
    public Result paymentByCash(Long id){
        log.info("订单现金支付：",id);
        orderService.paymentByCash(id);
        return Result.success();
    }

    /**
     * 确认租车
     * @param id
     * @return
     */
    @PutMapping("/confirm")
    @ApiOperation("确认租车")
    public Result confirm(Long id){
        log.info("确认租车{}",id);
        orderService.confirm(id);
        return Result.success();
    }

    /**
     * 确认还车
     * @param id
     * @return
     */
    @PutMapping("/returnVehicle")
    @ApiOperation("确认还车")
    public Result returnVehicle(Long id){
        log.info("确认还车{}",id);
        orderService.returnVehicle(id);
        return Result.success();
    }

    /**
     * 确认最终订单金额
     * @param orderAmountDTO
     * @return
     */
    @PutMapping("/amount")
    @ApiOperation("确认最终订单金额")
    public Result amount(@RequestBody OrderAmountDTO orderAmountDTO){
        log.info("确认最终订单金额{}",orderAmountDTO);
        orderService.amount(orderAmountDTO);
        return Result.success();
    }

    /**
     * 取消订单
     * @param orderCancelDTO
     * @return
     */
    @PutMapping("/cancel")
    @ApiOperation("取消订单")
    public Result cancel(@RequestBody OrderCancelDTO orderCancelDTO){
        log.info("取消订单：{}",orderCancelDTO.getId());
        orderService.cancel(orderCancelDTO);
        return Result.success();
    }

    /**
     * 根据id删除订单
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据id删除订单")
    public Result delete(Long id){
        log.info("根据id删除订单：{}",id);
        orderService.delete(id);
        return Result.success();
    }

    /**
     * 各个状态的订单数量统计
     *
     * @return
     */
    @GetMapping("/statistics")
    @ApiOperation("各个状态的订单数量统计")
    public Result<OrderStatisticsVO> statistics() {
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }





}
