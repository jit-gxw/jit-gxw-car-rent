package jit.gxw.controller.admin;
/**
 * 员工管理
 */


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jit.gxw.constant.JwtClaimsConstant;
import jit.gxw.dto.EmployeeDTO;
import jit.gxw.dto.EmployeeLoginDTO;
import jit.gxw.dto.EmployeePageQueryDTO;
import jit.gxw.dto.PasswordEditDTO;
import jit.gxw.entity.Employee;
import jit.gxw.properties.JwtProperties;
import jit.gxw.result.PageResult;
import jit.gxw.result.Result;
import jit.gxw.service.EmployeeService;
import jit.gxw.service.serviceImpl.UserDetailsServiceImpl;
import jit.gxw.utils.JwtUtil;
import jit.gxw.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/employee")
@Api(tags = "员工管理相关接口")
public class EmployeeController {


    @Autowired
    private EmployeeService employeeService;



    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("登录员工信息：{}",employeeLoginDTO);
        //登录
        return Result.success(employeeService.login(employeeLoginDTO));

    }
    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("员工登出")
    @PreAuthorize("hasAuthority('admin:common')")
    public Result<String> logout() {
        employeeService.logout();
        return Result.success();
    }


    @GetMapping("/test")
    @PreAuthorize("hasAuthority('admin:employee')")
    public Result test(){

        return Result.success();
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @ApiOperation("员工分页查询")
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('admin:employee')")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工查询参数为:{}",employeePageQueryDTO);

        PageResult pageResult=employeeService.pageQuery(employeePageQueryDTO);

        return Result.success(pageResult);
    }


    /**
     * 新增员工
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增员工")
    @PreAuthorize("hasAuthority('admin:employee')")
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        log.info("新增员工：{}",employeeDTO);

        //调用service
        employeeService.save(employeeDTO);

        return Result.success();
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工")
    @PreAuthorize("hasAuthority('admin:employee')")
    public Result<Employee> getById(@PathVariable Long id){
        Employee employee=employeeService.getById(id);
        return Result.success(employee);
    }

    /**
     * 更新员工
     * @param employeeDTO
     * @return
     */
    @ApiOperation("更新员工")
    @PutMapping
    @PreAuthorize("hasAuthority('admin:employee')")
    public Result update(@RequestBody EmployeeDTO employeeDTO){
        log.info("更新员工：{}",employeeDTO);
        employeeService.update(employeeDTO);

        return Result.success();
    }

    /**
     * 启用禁用员工
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启动禁用员工")
    @PreAuthorize("hasAuthority('admin:employee')")
    public Result startOrStop(@PathVariable Integer status,Long id){
        employeeService.startOrStop(status,id);
        log.info("启用禁用员工id：{}",id);
        return Result.success();
    }

    /**
     * 逻辑删除员工
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("逻辑删除员工")
    @PreAuthorize("hasAuthority('admin:employee')")
    public Result deleteById(Long id){
        log.info("删除员工id：{}",id);
        employeeService.deleteById(id);
        return Result.success();
    }

    /**
     * 修改密码
     * @param passwordEditDTO
     * @return
     */
    @ApiOperation("修改密码")
    @PutMapping("/editPassword")
    @PreAuthorize("hasAuthority('admin:common')")
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO){
        log.info("修改密码：{}",passwordEditDTO);
        employeeService.editPassword(passwordEditDTO);
        return Result.success();
    }





}
