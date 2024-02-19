package jit.gxw.mapper;


import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {

    /**
     * 根据用户id查询权限信息
     * @param userId
     * @return
     */
    List<String> selectPermsByUserId(Long userId);
}
