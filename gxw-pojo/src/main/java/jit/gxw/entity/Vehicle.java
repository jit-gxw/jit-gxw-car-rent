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
public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    //车牌号码
    private String licensePlateNumber;

    //车辆名称
    private String name;

    //车辆类型id
    private Long classificationId;

    //押金
    private BigDecimal cashPledge;

    //座位数
    private String seating;

    //图片路径
    private String image;

    //状态	0正常1禁止2使用3维修4报废
    private Integer status;

    //日租价格
    private BigDecimal priceDay;

    //月租价格
    private BigDecimal priceMonth;

    //车辆描述
    private String description;

    //购入价格
    private BigDecimal buyingPrice;

    //购入时间
    private LocalDateTime createTime;

    //最后修改时间
    private LocalDateTime updateTime;

    //创建人id
    private Long createUser;

    //最后修改人id
    private Long updateUser;

    //逻辑删除
    private Integer isDel;
}
