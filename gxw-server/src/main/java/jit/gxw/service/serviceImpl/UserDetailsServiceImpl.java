package jit.gxw.service.serviceImpl;

import jit.gxw.constant.MessageConstant;
import jit.gxw.entity.Employee;
import jit.gxw.exception.AccountNotFoundException;
import jit.gxw.mapper.EmployeeMapper;
import jit.gxw.mapper.MenuMapper;
import jit.gxw.security.LoginUser;
import jit.gxw.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //查询用户信息
        Employee employee = employeeMapper.getByUsername(username);

        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //查询对应的权限信息
        List<String> list=menuMapper.selectPermsByUserId(employee.getId());
        log.info("当前权限信息为：{}",list);
        //封装


        return new LoginUser(employee,list);
    }
}
