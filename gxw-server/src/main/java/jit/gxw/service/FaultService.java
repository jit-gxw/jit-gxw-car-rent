package jit.gxw.service;

import jit.gxw.dto.FaultCompleteDTO;
import jit.gxw.dto.FaultDTO;
import jit.gxw.dto.FaultPageQueryDTO;
import jit.gxw.dto.FaultStartDTO;
import jit.gxw.result.PageResult;

public interface FaultService {


    /**
     * 故障报修
     * @param faultDTO
     */
    void submit(FaultDTO faultDTO);

    /**
     * 故障信息搜索
     * @param faultPageQueryDTO
     * @return
     */
    PageResult conditionSearch(FaultPageQueryDTO faultPageQueryDTO);

    /**
     * 开始维修
     * @param faultStartDTO
     */
    void startRepair(FaultStartDTO faultStartDTO);

    /**
     * 完成维修
     * @param faultCompleteDTO
     */
    void complete(FaultCompleteDTO faultCompleteDTO);

    /**
     * 维修失败
     * @param faultCompleteDTO
     */
    void fail(FaultCompleteDTO faultCompleteDTO);

    /**
     * 根据id删除故障单
     * @param id
     */
    void deleteById(Long id);
}
