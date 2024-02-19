package jit.gxw.mapper;

import com.github.pagehelper.Page;
import jit.gxw.dto.ReletConditionSearchDTO;
import jit.gxw.entity.Relet;
import jit.gxw.vo.ReletVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReletMapper {

    /**
     * 插入续租
     * @param relet
     */
    void insert(Relet relet);

    /**
     * 根据订单id查询续租单
     * @param orderId
     * @return
     */
    @Select("select * from relet where orders_id=#{orderId} and is_del=0 and status=0")
    Relet selectByOrderIdWithWait(Long orderId);

    /**
     * 分页条件查询续租单
     * @param reletConditionSearchDTO
     * @return
     */
    Page<ReletVO> selectWithCondition(ReletConditionSearchDTO reletConditionSearchDTO);

    /**
     * 根据id查询续租单
     * @param id
     * @return
     */
    @Select("select * from relet where id=#{id} and is_del=0")
    Relet selectById(Long id);

    /**
     * 更新续租表
     * @param relet
     */
    void update(Relet relet);
    @Select("select relet.* from relet left join orders on relet.orders_id = orders.id where orders.user_id=#{id} and relet.is_del=0 order by relet_time desc")
    Page<ReletVO> pageQuery(Long id);

    /**
     * 获取待审核的续租单数量
     * @return
     */
    @Select("select count(*) from relet where status=0 and is_del=0")
    Integer selectStatusNumber();
}
