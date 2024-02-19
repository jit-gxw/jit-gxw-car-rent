package jit.gxw.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    //订单号
        private String number;

    //订单状态	0待付押金1待取车2租赁中3已还车4待付款5已完成6已取消
    private Integer status;

    //是否续租	0无续租1有续租
    private Integer reletStatus;

    //预约单id
    private Long appointmentId;

    //续租单id
    private Long reletId;

    //下单用户id
    private Long userId;

    //车辆id
    private Long vehicleId;

    //车辆名称
    private String vehicleName;

    //车牌号码
    private String licensePlateNumber;

    //车辆押金
    private BigDecimal cashPledge;

    //取车时间
    private LocalDateTime collectionTime;

    //还车时间
    private LocalDateTime returnTime;

    //备注
    private String remark;

    //支付状态 0未支付 1已支付 2退款
    private Integer payStatus;

    //押金状态 0未支付 1已支付 2退款
    private Integer cashPledgeStatus;

    //额外费用
    private BigDecimal extraCharges;

    //实收金额
    private BigDecimal amount;

    //用户姓名
    private String userName;

    //身份证号
    private String idNumber;

    //驾驶证号
    private String licenceId;

    //手机号
    private String phone;

    //订单取消时间
    private LocalDateTime cancelTime;

    //订单取消原因
    private String cancelReason;

    //下单时间
    private LocalDateTime orderTime;
    //完成时间
    private  LocalDateTime completionTime;

    //逻辑删除
    private Integer isDel;



}
