package jit.gxw.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ReletPageQueryDTO implements Serializable {


    private int page;
    //每页显示记录数
    private int pageSize;

}
