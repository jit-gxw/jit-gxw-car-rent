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
public class VehicleReportVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;



    //车辆名称
    private String name;

    //租赁次数
    private Long value;
}
