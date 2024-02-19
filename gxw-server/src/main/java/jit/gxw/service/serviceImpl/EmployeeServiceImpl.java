package jit.gxw.service.serviceImpl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jit.gxw.constant.JwtClaimsConstant;
import jit.gxw.constant.MessageConstant;
import jit.gxw.constant.PasswordConstant;
import jit.gxw.constant.TwoStatusConstant;
import jit.gxw.context.BaseContext;
import jit.gxw.dto.EmployeeDTO;
import jit.gxw.dto.EmployeeLoginDTO;
import jit.gxw.dto.EmployeePageQueryDTO;
import jit.gxw.dto.PasswordEditDTO;
import jit.gxw.entity.Employee;
import jit.gxw.entity.EmployeeRole;
import jit.gxw.exception.*;
import jit.gxw.mapper.EmployeeMapper;
import jit.gxw.properties.JwtProperties;
import jit.gxw.result.PageResult;
import jit.gxw.security.LoginUser;
import jit.gxw.service.EmployeeService;
import jit.gxw.utils.JwtUtil;
import jit.gxw.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private EmployeeMapper employeeMapper;
    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */

    @Override
    public EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO) {

        UsernamePasswordAuthenticationToken authenticationToken=
                new UsernamePasswordAuthenticationToken
                        (employeeLoginDTO.getUsername(),employeeLoginDTO.getPassword());

        //进行用户认证
        Authentication authenticate= null;
        try {
            authenticate = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
            //认证没通过
            throw new AccountNotFoundException(MessageConstant.LOGIN_FAILED);
        }
        //强转为loginuser类
        LoginUser loginUser=(LoginUser)authenticate.getPrincipal();

        if(loginUser.getEmployee().getIsDel()==TwoStatusConstant.DISABLE){
            //员工已删除
            throw new AccountNotFoundException("没有该账户");
        }else if(loginUser.getEmployee().getStatus()== TwoStatusConstant.DISABLE){
            //员工已锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);

        }
        //认证通过 生成jwt
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, loginUser.getEmployee().getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);
        log.info("jwt令牌为：{}",token);
        //将用户信息存入redis中
        //构造redis中的key
        String key="login:"+loginUser.getEmployee().getId();
        //将登录用户类存入redis
        try {
            redisTemplate.opsForValue().set(key,loginUser,120, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new BaseException("Redis未开启");
        }


        return EmployeeLoginVO.builder()
                .id(loginUser.getEmployee().getId())
                .userName(loginUser.getEmployee().getUsername())
                .name(loginUser.getEmployee().getName())
                .token(token)
                //添加角色id
                .roleId(employeeMapper.getRoleIdByUserId(loginUser.getEmployee().getId()))
                .build();
    }

    /**
     * 员工登出
     */
    @Override
    public void logout() {
        //获取用户id
        Long empId = BaseContext.getCurrentId();
        log.info("员工登出：{}",empId);
        //redis中的值
        try {
            redisTemplate.delete("login:"+empId);
        } catch (Exception e) {
            throw new BaseException("登出失败");
        }
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //开始分页
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());

        Page<Employee> page=employeeMapper.pageQuery(employeePageQueryDTO);

        return new PageResult(page.getTotal(),page.getResult());
    }
    /**
     * 新增员工
     * @param employeeDTO
     */
    @Transactional
    @Override
    public void save(EmployeeDTO employeeDTO) {
        //添加员工表
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);

        //设置属性
        employee.setStatus(TwoStatusConstant.ENABLE);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        employee.setPassword(bCryptPasswordEncoder.encode(PasswordConstant.DEFAULT_PASSWORD));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //设置当前记录创建/修改人id
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.insert(employee);
        //添加员工角色表
        EmployeeRole employeeRole = EmployeeRole.builder()
                .employeeId(employee.getId())
                .roleId(employeeDTO.getRoleId()).build();
        employeeMapper.insertEmpIdAndRoleId(employeeRole);


    }
    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @Override
    public Employee getById(Long id) {
        Employee employee = employeeMapper.getById(id);
        employee.setPassword("*******");
        return employee;
    }

    @Override
    @Transactional
    public void update(EmployeeDTO employeeDTO) {
        //更新员工表
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.update(employee);
        //更新员工角色表
        employeeMapper.updateEmpRole(EmployeeRole.builder()
                .employeeId(employeeDTO.getId())
                .roleId(employeeDTO.getRoleId()).build());

    }
    /**
     * 启用禁用员工
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Employee employee = Employee.builder()
                .status(status).id(id).build();

        employeeMapper.update(employee);
    }

    /**
     * 逻辑删除员工
     * @param id
     */
    @Override
    public void deleteById(Long id) {


        employeeMapper.setIsDel(id);
    }
    /**
     * 修改密码
     * @param passwordEditDTO
     */
    @Override
    public void editPassword(PasswordEditDTO passwordEditDTO) {
        //判断是否是本人操纵
        if(passwordEditDTO.getEmpId()!=BaseContext.getCurrentId()){

            throw new BaseException("非本人操作!");

        }

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        //校验密码
        Employee employee = employeeMapper.getById(passwordEditDTO.getEmpId());
        if(!bCryptPasswordEncoder.matches(passwordEditDTO.getOldPassword(), employee.getPassword())){
            //验证不通过
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        //修改密码
        try {
            employee.setPassword(bCryptPasswordEncoder.encode(passwordEditDTO.getNewPassword()));
            employeeMapper.update(employee);
        } catch (Exception e) {
            throw new PasswordErrorException("修改密码失败");
        }
    }

}
