package jit.gxw.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jit.gxw.constant.JwtClaimsConstant;
import jit.gxw.context.BaseContext;
import jit.gxw.dto.UserDTO;
import jit.gxw.dto.UserLoginDTO;
import jit.gxw.dto.UserPageQueryDTO;
import jit.gxw.entity.User;
import jit.gxw.properties.JwtProperties;
import jit.gxw.result.PageResult;
import jit.gxw.result.Result;
import jit.gxw.service.UserService;
import jit.gxw.utils.JwtUtil;
import jit.gxw.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.Map;

@RestController("AdminUserController")
@RequestMapping("/admin/user")
@Api(tags = "管理端用户接口")
@Slf4j
@PreAuthorize("hasAuthority('admin:user')")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户分页查询
     * @param userPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询用户信息")
    public Result<PageResult> page(UserPageQueryDTO userPageQueryDTO){
        log.info("用户分页查询：",userPageQueryDTO);
        PageResult pageResult=userService.pageQuert(userPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("根据id删除用户")
    public Result deleteById(Long id){
        log.info("删除用户：{}",id);
        userService.deleteById(id);
        return Result.success();
    }
    @PostMapping("/status/{status}")
    @ApiOperation("设置用户状态")
    public Result setUserStatus(@PathVariable Integer status,Long id){

        log.info("设置用户{}状态{}",id,status);
        userService.setUserStatus(status,id);
        return Result.success();
    }

}
