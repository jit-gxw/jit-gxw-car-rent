package jit.gxw.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class VehiclePageQueryDTO implements Serializable {

    //页码
    private int page;

    //每页记录数
    private int pageSize;

    //分类id
    private Long classificationId;

    //车牌号码
    private String licensePlateNumber;

    //车辆名称
    private String name;

    //车辆状态
    private Integer status;

}
