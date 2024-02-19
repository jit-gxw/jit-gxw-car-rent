package jit.gxw.service;

import jit.gxw.dto.ClassificationDTO;
import jit.gxw.dto.ClassificationPageQueryDTO;
import jit.gxw.entity.Classification;
import jit.gxw.result.PageResult;

import java.util.List;

public interface ClassificationService {

    /**
     * 分类分页查询
     * @param classificationPageQueryDTO
     * @return
     */
    PageResult pageQuery(ClassificationPageQueryDTO classificationPageQueryDTO);

    /**
     * 新增分类
     * @param classificationDTO
     */
    void save(ClassificationDTO classificationDTO);

    /**
     * 修改分类
     * @param classificationDTO
     */
    void update(ClassificationDTO classificationDTO);

    /**
     * 设置分类状态
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据id删除分类
     * @param id
     */
    void deleteById(Long id);

    /**
     * 用户查询分类
     * @return
     */
    List<Classification> list();
}
