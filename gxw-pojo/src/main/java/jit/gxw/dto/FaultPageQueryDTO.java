package jit.gxw.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FaultPageQueryDTO implements Serializable {

    //日志报修开始时间时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;

    //日志报修结束时间时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    //车牌号码
    private String licensePlateNumber;

    //报修人角色
    private Integer informantRole;

    //维修状态	0已报修1维修中2维修完成3维修失败
    private Integer status;

    //页码
    private int page;

    //每页显示记录数
    private int pageSize;

}
