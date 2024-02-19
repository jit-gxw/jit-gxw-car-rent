package jit.gxw.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jit.gxw.dto.OrderPaymentDTO;
import jit.gxw.dto.OrderUserPageDTO;
import jit.gxw.entity.Orders;
import jit.gxw.result.PageResult;
import jit.gxw.result.Result;
import jit.gxw.service.OrderService;
import jit.gxw.vo.OrderUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("UserOrderController")
@RequestMapping("/user/order")
@Api(tags = "管理端订单接口")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 历史订单查询
     * @param orderUserPageDTO
     * @return
     */
    @GetMapping("historyOrders")
    @ApiOperation("用户历史订单查询")
    public Result<PageResult> historyOrders(OrderUserPageDTO orderUserPageDTO){
        log.info("用户历史订单查询：{}",orderUserPageDTO);
        PageResult pageResult=orderService.historyOrders(orderUserPageDTO);
        return Result.success(pageResult);
    }

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderUserVO> orderDetail(@PathVariable Long id){
        log.info("查询订单详情：{}",id);
        OrderUserVO orderUserVO=orderService.orderDetail(id);
        return Result.success(orderUserVO);
    }

    /**
     * 取消订单
     * @param id
     * @return
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result cancel(@PathVariable Long id){
        log.info("取消订单：{}",id);
        orderService.cancelWithUser(id);
        return Result.success();
    }

    /**
     * 支付订单
     * @param orderPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("支付订单")
    public Result payment(@RequestBody OrderPaymentDTO orderPaymentDTO){
        String number= orderPaymentDTO.getNumber();
        log.info("用户支付订单：{}",number);
        orderService.payment(number);
        return Result.success();
    }

}
