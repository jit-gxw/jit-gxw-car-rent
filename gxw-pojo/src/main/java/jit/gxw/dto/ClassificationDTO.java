package jit.gxw.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClassificationDTO implements Serializable {

    //主键
    private Long id;

    //分类名称
    private String name;

    //排序
    private Integer sort;

}
