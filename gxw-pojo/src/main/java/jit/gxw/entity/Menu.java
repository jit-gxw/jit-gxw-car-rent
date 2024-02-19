package jit.gxw.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    //权限名称
    private String menuName;
    //路由地址
    private String path;
    //组件路径
    private String component;
    //权限标识
    private String perms;
}
