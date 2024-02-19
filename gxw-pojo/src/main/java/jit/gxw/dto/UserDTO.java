package jit.gxw.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * C端用户登录
 */
@Data
public class UserDTO implements Serializable {

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


}
