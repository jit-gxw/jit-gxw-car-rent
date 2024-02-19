package jit.gxw.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import jit.gxw.constant.MessageConstant;
import jit.gxw.constant.TwoStatusConstant;
import jit.gxw.constant.UserStatusConstant;
import jit.gxw.context.BaseContext;
import jit.gxw.dto.UserDTO;
import jit.gxw.dto.UserLoginDTO;
import jit.gxw.dto.UserPageQueryDTO;
import jit.gxw.entity.User;
import jit.gxw.exception.LoginFailedException;
import jit.gxw.mapper.UserMapper;
import jit.gxw.properties.WeChatProperties;
import jit.gxw.result.PageResult;
import jit.gxw.service.UserService;
import jit.gxw.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;

    //微信服务接口地址
    public static final String WX_LOGIN="https://api.weixin.qq.com/sns/jscode2session";
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        String openid = getOpenId(userLoginDTO.getCode());

        //判断openid是否为空，如果为空登录失败
        if (openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //判断是否为新用户
        User user= userMapper.getByOpenId(openid);
        //如果是新用户自动完成注册
        if (user==null){
            user= User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }else if(Objects.equals(user.getStatus(), UserStatusConstant.DISABLE)){
            throw new LoginFailedException(MessageConstant.ACCOUNT_LOCKED);
        }


        //返回用户对象
        return user;
    }

    /**
     * 查看个人信息
     * @param id
     * @return
     */
    @Override
    public User searchInfo(Long id) {
        return userMapper.selectById(id);
    }

    /**
     * 修改个人信息
     * @param userDTO
     */
    @Override
    public void update(UserDTO userDTO) {
        User user = new User();
        //拷贝属性
        BeanUtils.copyProperties(userDTO,user);
        //设置属性
        user.setId(BaseContext.getCurrentId());
        user.setUpdateTime(LocalDateTime.now());

        userMapper.update(user);
    }

    /**
     * 用户分页查询
     * @param userPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuert(UserPageQueryDTO userPageQueryDTO) {
        PageHelper.startPage(userPageQueryDTO.getPage(), userPageQueryDTO.getPageSize());
        Page<User> page=userMapper.page(userPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据id删除用户
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        User user = User.builder()
                .id(id)
                .isDel(TwoStatusConstant.DISABLE)
                .updateTime(LocalDateTime.now())
                .build();
        userMapper.update(user);
    }

    /**
     * 设置用户状态
     * @param status
     * @param id
     */
    @Override
    public void setUserStatus(Integer status, Long id) {
        User user = User.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .build();
        userMapper.update(user);
    }

    /**
     * 调用微信接口服务，获取openid
     * @param code
     * @return
     */

    private String getOpenId(String code){
        //调用微信接口，获得用户openid
        Map<String, String> map=new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(WX_LOGIN, map);

        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getString("openid");
    }
}
