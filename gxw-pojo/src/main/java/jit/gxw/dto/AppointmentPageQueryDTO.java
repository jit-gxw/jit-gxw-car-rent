package jit.gxw.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AppointmentPageQueryDTO implements Serializable {

    //最早预约单创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;
    //最晚预约单创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    //预约用户姓名
    private String userName;
    //预约车牌号码
    private String licensePlateNumber;
    //审核人姓名
    private String employeeName;
    //预约状态：0待审核1已审核2审核不通过3已取消
    private Integer status;
    //页码
    private int page;
    //每页显示记录数
    private int pageSize;

}
