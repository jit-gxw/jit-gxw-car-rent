package jit.gxw.mapper;

import com.github.pagehelper.Page;
import jit.gxw.dto.OrderPageQueryDTO;
import jit.gxw.dto.OrderUserPageDTO;
import jit.gxw.entity.Orders;
import jit.gxw.vo.OrderUserVO;
import jit.gxw.vo.VehicleReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * 根据租赁日期查询车辆id
     * @param begin
     * @param end
     * @return
     */
    List<Long> selectVehicelIdByTime(LocalDateTime begin, LocalDateTime end);

    /**
     * 根据车辆id；name，取车还车时间查询对应订单
     * @param build
     * @return
     */
    List<Orders> selectForConfirm(Orders build);

    /**
     * 根据用户id查询是否有未完成订单
     * @param userId
     * @return
     */
    @Select("select * from orders where user_id=#{userId} and (status=0 or status=1 or status=2 or status=3 or status=4) and is_del=0")
    Orders selectByUserIdForConfirm(Long userId);

    /**
     * 根据车辆id和租用时间查询订单信息 判断是否有冲突
     * @param build
     * @return
     */
    Orders selectByVehicleIdAndTimeForConfirm(Orders build);

    /**
     * 生成订单
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 分页查询订单信息
     * @param orderPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrderPageQueryDTO orderPageQueryDTO);

    /**
     * 根据订单id查询订单
     * @param id
     * @return
     */
    @Select("select * from orders where id=#{id} and is_del=0")
    Orders selectById(Long id);

    /**
     * 更新订单
     * @param order
     */
    void update(Orders order);
    /**
     * 根据状态统计订单数量
     * @param status
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     * 查询用户历史订单
     * @param orderUserPageDTO
     * @return
     */

    Page<OrderUserVO> historyOrders(OrderUserPageDTO orderUserPageDTO);

    /**
     * 根据id和用户id查询订单
     * @param id
     * @param currentId
     * @return
     */
    @Select("select orders.*,vehicle.image from orders left join vehicle on orders.vehicle_id = vehicle.id where orders.id=#{id} and user_id=#{currentId} and orders.is_del=0")
    OrderUserVO selectByIdAndUserId(Long id, Long currentId);

    /**
     * 根据订单号查询订单
     * @param number
     * @return
     */
    @Select("select * from orders where number=#{number} and is_del=0")
    Orders selectByNumber(String number);

    List<VehicleReportVO> getReletStatistics();
    /**
     * 根据动态条件来统计营业额数据
     * @param map
     * @return
     */
    Double sumByMap(Map map);
}
