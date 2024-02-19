package jit.gxw.mapper;

import com.github.pagehelper.Page;
import jit.gxw.dto.ClassificationPageQueryDTO;
import jit.gxw.entity.Classification;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClassificationMapper {

    /**
     * 分类分页查询
     * @param classificationPageQueryDTO
     * @return
     */
    Page<Classification> pageQuery(ClassificationPageQueryDTO classificationPageQueryDTO);

    /**
     * 新增分类
     * @param classification
     */
    @Insert("insert into classification( name, sort, status, create_time, update_time, create_user, update_user)" +
            " VALUES" +
            " ( #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Classification classification);

    void update(Classification classification);

    /**
     * 根据id删除分类
     * @param id
     */
    @Delete("delete from classification where id=#{id}")
    void deleteById(Long id);

    /**
     * 用户查询分类
     * @return
     */
    @Select("select id,name,sort from classification where status=0 order by sort asc , create_time desc")
    List<Classification> list();
}
