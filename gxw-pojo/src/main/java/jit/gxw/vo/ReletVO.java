package jit.gxw.vo;


import jit.gxw.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReletVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    //订单id
    private Long ordersId;

    //订单号
    private String number;

    //续租状态	0待审核1已审核2审核不通过3已取消
    private Integer status;

    //续租还车时间
    private LocalDateTime returnTime;

    //审核人id
    private Long employeeId;

    //审核人姓名
    private String employeeName;

    //续租信息
    private String reletInfo;

    //续租时间
    private LocalDateTime reletTime;

    //续租取消原因
    private String cancelReason;

    //审核不通过原因
    private String rejectionReason;

    //续租取消时间
    private LocalDateTime cancelTime;

    //审核通过时间
    private LocalDateTime passTime;

    //逻辑删除
    private Integer isDel;
    //订单
    private Orders order;



}
