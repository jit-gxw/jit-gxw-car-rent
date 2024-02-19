package jit.gxw.vo;


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
public class AppointmentVO implements Serializable {

    private Long id;

    //预约用户id
    private Long userId;

    //车辆id
    private Long vehicleId;

    //车辆名称
    private String vehicleName;

    //车牌号码
    private String licensePlateNumber;
    //车辆图片
    private String image;

    //日租价格
    private BigDecimal priceDay;

    //月租价格
    private BigDecimal priceMonth;

    //审核人id
    private Long employeeId;

    //审核人姓名
    private String employeeName;

    //预约状态	0待审核1已审核2审核不通过3已取消
    private Integer status;

    //预约取车时间
    private LocalDateTime collectionTime;

    //预约还车时间
    private LocalDateTime returnTime;

    //预约信息
    private String appointmentInfo;

    //预约时间
    private LocalDateTime appointmentTime;

    //用户姓名
    private String userName;

    //身份证号
    private String idNumber;

    //驾驶证号
    private String licenceId;

    //手机号
    private String phone;

    //预约取消原因
    private String cancelReason;

    //审核不通过原因
    private String rejectionReason;

    //预约取消时间
    private LocalDateTime cancelTime;

    //审核通过时间
    private LocalDateTime passTime;
    //对应订单
    private String number;

    //逻辑删除
    private Integer isDel;



}
