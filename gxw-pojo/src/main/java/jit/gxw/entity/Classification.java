package jit.gxw.entity;


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
public class Classification implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    //分类名称
    private String name;

    //排序字段
    private Integer sort;

    //状态	0启用 1禁用
    private Integer status;

    //创建时间
    private LocalDateTime createTime;

    //最后修改时间
    private LocalDateTime updateTime;

    //创建人id
    private Long createUser;

    //最后修改人id
    private Long updateUser;
}
