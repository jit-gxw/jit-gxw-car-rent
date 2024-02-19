package jit.gxw.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderAmountDTO implements Serializable {

    private Long id;
    //额外费用
    private BigDecimal extraCharges;
}
