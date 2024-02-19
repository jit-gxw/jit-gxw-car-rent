package jit.gxw.mapper;

import com.github.pagehelper.Page;
import jit.gxw.dto.AppointmentPageQueryDTO;
import jit.gxw.dto.AppointmentSubmitDTO;
import jit.gxw.entity.Appointment;
import jit.gxw.vo.AppointmentVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.awt.image.PixelGrabber;

@Mapper
public interface AppointmentMapper {
    /**
     * 将预约信息插入数据库
     * @param appointment
     */
    @Insert("insert into appointment (user_id, vehicle_id, vehicle_name,license_plate_number,collection_time, return_time, appointment_info, appointment_time, user_name, id_number, licence_id, phone,status) " +
            "VALUES (#{userId},#{vehicleId},#{vehicleName},#{licensePlateNumber},#{collectionTime},#{returnTime},#{appointmentInfo},#{appointmentTime},#{userName},#{idNumber},#{licenceId},#{phone},#{status})")
    void insert(Appointment appointment);

    /**
     * 根据用户id查询是否有未完成预约
     * @param userId
     * @return
     */
    @Select("select * from appointment where user_id=#{userId} and status=0")
    Appointment selectByUserIdForConfirm(Long userId);

    /**
     * 条件查询预约信息
     * @param appointmentPageQueryDTO
     * @return
     */
    Page<Appointment> pageQuery(AppointmentPageQueryDTO appointmentPageQueryDTO);

    /**
     * 更新预约信息
     * @param appointment
     */
    void update(Appointment appointment);

    /**
     * 根据id查询预约单
     * @param id
     * @return
     */
    @Select("select * from appointment where id=#{id}")
    Appointment getById(Long id);

    /**
     * 根据id查询预约单
     * @param id
     * @return
     */
    @Select("select * from appointment where id=#{id}")
    Appointment selectById(Long id);

    /**
     * 根据用户id查询预约单
     * @param UserId
     * @return
     */
    @Select("select *,vehicle.image,vehicle.price_day,vehicle.price_month from appointment left outer join vehicle on appointment.vehicle_id = vehicle.id where user_id=#{UserId} and appointment.is_del=0 order by appointment_time desc")
    Page<AppointmentVO> pageQueryByUserId(Long UserId);

    /**
     * 搜索代预约状态的记录数
     * @return
     */
    @Select("select count(*) from appointment where status=0 and is_del=0")
    Integer selectStatusNumber();
}
