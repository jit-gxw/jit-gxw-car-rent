package jit.gxw.service;

import jit.gxw.dto.UserDTO;
import jit.gxw.dto.UserLoginDTO;
import jit.gxw.dto.UserPageQueryDTO;
import jit.gxw.entity.User;
import jit.gxw.result.PageResult;

public interface UserService {
    /**
     * 微信用户登录
     * @param userLoginDTO
     * @return
     */
    User wxLogin(UserLoginDTO userLoginDTO);

    /**
     * 查看个人信息
     * @param id
     * @return
     */
    User searchInfo(Long id);

    /**
     * 修改个人信息
     * @param userDTO
     */
    void update(UserDTO userDTO);

    /**
     * 用户分页查询
     * @param userPageQueryDTO
     * @return
     */
    PageResult pageQuert(UserPageQueryDTO userPageQueryDTO);

    /**
     * 根据id删除用户
     * @param id
     */
    void deleteById(Long id);

    /**
     * 设置用户状态
     * @param status
     * @param id
     */
    void setUserStatus(Integer status, Long id);
}
