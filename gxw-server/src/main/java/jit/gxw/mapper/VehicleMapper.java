package jit.gxw.mapper;

import com.github.pagehelper.Page;
import jit.gxw.dto.VehiclePageQueryDTO;
import jit.gxw.entity.Vehicle;
import jit.gxw.vo.VehicleVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


import java.util.List;

@Mapper
public interface VehicleMapper {

    /**
     * 根据分类查询车辆
     * @param classificationId
     * @return
     */
    @Select("select * from vehicle where classification_id=#{classificationId}")
    List<Vehicle> getByClassificationId(Long classificationId);

    /**
     * 车辆分页查询
     * @param vehiclePageQueryDTO
     * @return
     */
    Page<VehicleVO> pageQuery(VehiclePageQueryDTO vehiclePageQueryDTO);

    /**
     * 添加车辆
     * @param vehicle
     */
    @Insert("insert into vehicle (license_plate_number, name, classification_id,cash_pledge,seating ,image,status,price_day, price_month, description, buying_price, create_time, update_time, create_user, update_user)" +
            "values (#{licensePlateNumber},#{name},#{classificationId},#{cashPledge},#{seating},#{image},#{status},#{priceDay},#{priceMonth},#{description},#{buyingPrice},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insert(Vehicle vehicle);

    /**
     * 根据id查询车辆详细和车辆分类名称
     * @param id
     * @return
     */
    @Select("select vehicle.*,classification.name as classificationName from vehicle left join classification on vehicle.classification_id = classification.id where vehicle.id=#{id}")
    VehicleVO selectByidWithClassName(Long id);

    /**
     * 修改车辆信息
     * @param vehicle
     */
    void update(Vehicle vehicle);

    /**
     * 删除车辆
     * @param id
     */
    @Delete("delete from vehicle where id=#{id}")
    void deleteById(Long id);

    /**
     * 根据分类id和状态查询车辆
     * @param vehicle
     * @return
     */
    @Select("select * from vehicle where classification_id=#{classificationId} and status=#{status} and is_del=0")
    List<Vehicle> selectForUser(Vehicle vehicle);
    @Select("select vehicle.*,classification.name as classificationName from vehicle left join classification on vehicle.classification_id = classification.id where vehicle.id=#{id} and vehicle.is_del=0 and vehicle.status =0")
    VehicleVO selectByIdForUser(Long id);
}
