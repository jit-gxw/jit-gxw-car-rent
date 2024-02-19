package jit.gxw.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClassificationPageQueryDTO implements Serializable {

    //页码
    private int page;

    //每页记录数
    private int pageSize;

    //分类名称
    private String name;

    //车辆分类状态
    private Integer status;

}
