package jit.gxw.mapper;

import com.github.pagehelper.Page;
import jit.gxw.dto.FaultPageQueryDTO;
import jit.gxw.entity.Fault;
import jit.gxw.vo.RepairReportVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FaultMapper {

    /**
     * 新增报修单
     * @param fault
     */
    void insert(Fault fault);

    /**
     * 搜索故障信息
     * @param faultPageQueryDTO
     * @return
     */
    Page<Fault> pageQuery(FaultPageQueryDTO faultPageQueryDTO);

    /**
     * 更新故障信息
     * @param fault
     */
    void update(Fault fault);

    /**
     * 根据id查询故障单
     * @param id
     * @return
     */
    @Select("select * from fault where id=#{id}")
    Fault slectById(Long id);

    /**
     * 获取维修数据
     * @return
     */
    List<RepairReportVO> getrepairStatistics();
}
