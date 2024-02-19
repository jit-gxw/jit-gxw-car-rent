package jit.gxw.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ReletConditionSearchDTO implements Serializable {



    //订单号
    private String number;
    //审核人姓名
    private String employeeName;
    //预约状态：0待审核1已审核2审核不通过3已取消
    private Integer status;
    //页码
    private int page;
    //每页显示记录数
    private int pageSize;

}
