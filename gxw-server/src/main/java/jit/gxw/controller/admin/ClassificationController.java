package jit.gxw.controller.admin;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jit.gxw.dto.ClassificationDTO;
import jit.gxw.dto.ClassificationPageQueryDTO;
import jit.gxw.result.PageResult;
import jit.gxw.result.Result;
import jit.gxw.service.ClassificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/classification")
@Api(tags = "车辆分类管理相关接口")
public class ClassificationController {
    @Autowired
    private ClassificationService classificationService;


    /**
     * 分类分页查询
     * @param classificationPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    @PreAuthorize("hasAuthority('admin:common')")
    public Result<PageResult> page(ClassificationPageQueryDTO classificationPageQueryDTO){
        log.info("分页查询：{}", classificationPageQueryDTO);
        PageResult pageResult = classificationService.pageQuery(classificationPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 新增分类
     * @param classificationDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增分类")
    @PreAuthorize("hasAuthority('admin:classification')")
    public Result<String> save(@RequestBody ClassificationDTO classificationDTO){
        log.info("新增分类：{}", classificationDTO);
        classificationService.save(classificationDTO);
        return Result.success();
    }


    /**
     * 修改分类
     * @param classificationDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改分类")
    @PreAuthorize("hasAuthority('admin:classification')")
    @CacheEvict(cacheNames = "Classification",allEntries = true)
    public Result<String> update(@RequestBody ClassificationDTO classificationDTO){
        log.info("修改分类：{}",classificationDTO);
        classificationService.update(classificationDTO);
        return Result.success();
    }

    /**
     * 启用、禁用分类
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用分类")
    @PreAuthorize("hasAuthority('admin:classification')")
    @CacheEvict(cacheNames = "Classification",allEntries = true)
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id){
        classificationService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除分类")
    @PreAuthorize("hasAuthority('admin:classification')")
    @CacheEvict(cacheNames = "Classification",allEntries = true)
    public Result<String> deleteById(Long id){
        log.info("删除分类：{}", id);
        classificationService.deleteById(id);
        return Result.success();
    }









}
