package jit.gxw.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaultCompleteDTO implements Serializable {

    private Long id;

    //故障原因
    private String reason;

    //解决办法
    private String solution;

    //维修费用
    private BigDecimal cost;

}
