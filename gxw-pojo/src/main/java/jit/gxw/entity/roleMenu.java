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
public class roleMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long roleId;

    private Long menuId;
}
