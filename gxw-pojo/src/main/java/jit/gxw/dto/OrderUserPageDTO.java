package jit.gxw.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class OrderUserPageDTO implements Serializable {

    //订单状态0待付押金1待取车2租赁中3已还车4待付款5已完成6已取消
    private Integer status;
    //页码
    private int page;
    //每页显示记录数
    private int pageSize;
    //用户id
    private Long userId;

}
