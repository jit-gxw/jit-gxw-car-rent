package jit.gxw.service;


import jit.gxw.dto.ReletConditionSearchDTO;
import jit.gxw.dto.ReletPageQueryDTO;
import jit.gxw.dto.ReletRejectionDTO;
import jit.gxw.dto.ReletSubmitDTO;
import jit.gxw.result.PageResult;

public interface ReletService {

    /**
     * 用户发起续租
     * @param reletSubmitDTO
     */
    void submit(ReletSubmitDTO reletSubmitDTO);

    /**
     * 条件搜索续租单
     * @param reletConditionSearchDTO
     * @return
     */
    PageResult conditionSearch(ReletConditionSearchDTO reletConditionSearchDTO);

    /**
     * 拒绝续租
     * @param reletRejectionDTO
     */
    void rejection(ReletRejectionDTO reletRejectionDTO);

    /**
     * 通过申请
     * @param id
     */
    void pass(Long id);

    /**
     * 根据id删除续租单
     * @param id
     */
    void delete(Long id);

    /**
     * 用户分页查询
     * @param reletPageQueryDTO
     */
    PageResult pageQuery(ReletPageQueryDTO reletPageQueryDTO);

    /**
     * 用户取消续租
     * @param id
     */
    void cancel(Long id);

    /**
     * 获取待审核的续租单数量
     * @return
     */
    Integer getStatusNumber();
}
