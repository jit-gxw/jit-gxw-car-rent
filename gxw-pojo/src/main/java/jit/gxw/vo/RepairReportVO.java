package jit.gxw.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepairReportVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;



    //车辆名称
    private String name;

    //维修次数
    private Long number;
}
