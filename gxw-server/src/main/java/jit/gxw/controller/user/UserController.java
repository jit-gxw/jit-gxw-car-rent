package jit.gxw.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jit.gxw.constant.JwtClaimsConstant;
import jit.gxw.context.BaseContext;
import jit.gxw.dto.UserDTO;
import jit.gxw.dto.UserLoginDTO;
import jit.gxw.entity.User;
import jit.gxw.properties.JwtProperties;
import jit.gxw.result.Result;
import jit.gxw.service.UserService;
import jit.gxw.utils.JwtUtil;
import jit.gxw.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController("UserUserController")
@RequestMapping("/user/user")
@Api(tags = "用户端用户接口")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 测试
     * @return
     */
    @GetMapping("/test")
    public Result test1(){

        return Result.success();
    }

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("微信登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        log.info("code:{}",userLoginDTO.getCode());

        //微信登录
        User user= userService.wxLogin(userLoginDTO);

        //为微信用户生成jwt令牌
        Map<String, Object> clams=new HashMap<>();
        clams.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), clams);
        log.info("用户jwt令牌是：{}",token);


        UserLoginVO userLoginVO = new UserLoginVO();
        //拷贝属性
        BeanUtils.copyProperties(user,userLoginVO);
        userLoginVO.setToken(token);

        return Result.success(userLoginVO);
    }

    /**
     * 查看个人信息
     *
     * @return
     */
    @ApiOperation("查看个人信息")
    @GetMapping
    public Result<User> searchInfo(){
        log.info("查看个人信息：{}", BaseContext.getCurrentId());


        return Result.success(
                userService.searchInfo(BaseContext.getCurrentId())
        );
    }

    /**
     * 修改个人信息
     * @param userDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改个人信息")
    public Result update(@RequestBody UserDTO userDTO){
        log.info("修改个人信息：{}",userDTO);
        userService.update(userDTO);
        return Result.success();
    }
}
