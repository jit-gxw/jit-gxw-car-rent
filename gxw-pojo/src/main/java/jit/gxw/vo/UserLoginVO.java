package jit.gxw.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO implements Serializable {

    private Long id;

    //微信用户唯一标识
    private String openid;
    //用户状态	0正常1禁止2欠费
    private Integer status;

    //姓名
    private String name;

    //手机号
    private String phone;

    //性别 0 女 1 男
    private String sex;

    //身份证号
    private String idNumber;

    //驾驶证号
    private String licenceId;

    //头像
    private String avatar;

    //注册时间
    private LocalDateTime createTime;
    //修改时间
    private LocalDateTime updateTime;
    //逻辑删除
    private Integer isDel;
    //令牌
    private String token;

}
