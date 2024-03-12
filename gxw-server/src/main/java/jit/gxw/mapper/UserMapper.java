package jit.gxw.mapper;

import com.github.pagehelper.Page;
import jit.gxw.dto.UserPageQueryDTO;
import jit.gxw.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from user where openid=#{openid}")
    User getByOpenId(String openid);

    /**
     * 插入用户数据
     * @param user
     */
    void insert(User user);

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    @Select("SELECT * from user where id = #{id} and is_del=0")
    User selectById(Long id);

    /**
     * 更新用户信息
     * @param user
     */
    void update(User user);

    /**
     * 分页查询用户信息
     * @param userPageQueryDTO
     * @return
     */
    Page<User> page(UserPageQueryDTO userPageQueryDTO);
    /**
     * 根据动态条件来统计用户数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
