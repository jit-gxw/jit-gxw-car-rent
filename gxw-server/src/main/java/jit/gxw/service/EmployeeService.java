package jit.gxw.service;

import jit.gxw.dto.EmployeeDTO;
import jit.gxw.dto.EmployeeLoginDTO;
import jit.gxw.dto.EmployeePageQueryDTO;
import jit.gxw.dto.PasswordEditDTO;
import jit.gxw.entity.Employee;
import jit.gxw.result.PageResult;
import jit.gxw.vo.EmployeeLoginVO;

public interface EmployeeService {
    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO);


    /**
     * 员工登出
     */
    void logout();


    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);


    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    Employee getById(Long id);

    /**
     * 更新员工
     * @param employeeDTO
     */
    void update(EmployeeDTO employeeDTO);
    /**
     * 启用禁用员工
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);


    /**
     * 逻辑删除员工
     * @param id
     */
    void deleteById(Long id);

    /**
     * 修改密码
     * @param passwordEditDTO
     */
    void editPassword(PasswordEditDTO passwordEditDTO);
}
