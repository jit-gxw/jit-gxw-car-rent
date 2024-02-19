package jit.gxw.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OrderPaymentDTO implements Serializable {

    //最早订单创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;
    //最晚订单创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    //订单号
    private String number;
    //手机号
    private String phone;
    //订单状态0待付押金1待取车2租赁中3已还车4待付款5已完成6已取消
    private Integer status;
    //页码
    private int page;
    //每页显示记录数
    private int pageSize;

}
