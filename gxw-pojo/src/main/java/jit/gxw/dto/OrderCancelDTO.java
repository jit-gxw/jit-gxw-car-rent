package jit.gxw.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderCancelDTO implements Serializable {

    private Long id;
    //订单取消原因
    private String cancelReason;
}
