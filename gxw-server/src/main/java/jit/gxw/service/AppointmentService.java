package jit.gxw.service;

import jit.gxw.dto.AppointmentPageQueryDTO;
import jit.gxw.dto.AppointmentRejectionDTO;
import jit.gxw.dto.AppointmentSubmitDTO;
import jit.gxw.dto.AppointmentUserPageQueryDTO;
import jit.gxw.result.PageResult;
import jit.gxw.vo.OrderVO;

public interface AppointmentService {
    /**
     * 用户发起预约
     * @param appointmentSubmitDTO
     */
    void submit(AppointmentSubmitDTO appointmentSubmitDTO);

    /**
     * 搜索预约信息
     * @param appointmentPageQueryDTO
     * @return
     */
    PageResult conditionSearch(AppointmentPageQueryDTO appointmentPageQueryDTO);

    /**
     * 拒绝预约
     * @param appointmentRejectionDTO
     */
    void rejection(AppointmentRejectionDTO appointmentRejectionDTO);

    /**
     * 通过审核
     * @param id
     * @return
     */
    OrderVO pass(Long id);

    /**
     * 删除预约
     * @param id
     */
    void delete(Long id);

    /**
     * 用户取消预约
     * @param id
     */
    void cancel(Long id);

    /**
     * 用户分页查询本人预约单
     * @param appointmentUserPageQueryDTO
     * @return
     */
    PageResult pageQuery(AppointmentUserPageQueryDTO appointmentUserPageQueryDTO);

    /**
     * 获取待审核预约的数量
     * @return
     */
    Integer getStatusNumber();
}
