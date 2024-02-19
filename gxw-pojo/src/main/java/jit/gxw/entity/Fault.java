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
public class Fault implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    //车辆id
    private Long vehicleId;

    //车牌号码
    private String licensePlateNumber;

    //报修人角色
    private Integer informantRole;

    //报修人id
    private Long informantId;

    //报修人名字
    private String informantName;

    //报修原因
    private String information;

    //维修状态	0已报修1维修中2维修完成3维修失败
    private Integer status;

    //维修人员id
    private Long employeeId;

    //维修人员姓名
    private String employeeName;

    //故障原因
    private String reason;

    //报修时间
    private LocalDateTime notificationTime;

    //维修时间
    private LocalDateTime repairTime;

    //完成时间
    private LocalDateTime completionTime;

    //解决办法
    private String solution;

    //维修费用
    private BigDecimal cost;

    //逻辑删除
    private Integer isDel;



}
