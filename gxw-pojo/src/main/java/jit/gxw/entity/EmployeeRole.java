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
public class EmployeeRole implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long employeeId;

    private Long roleId;
}
