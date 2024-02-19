package jit.gxw.controller.user;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jit.gxw.dto.ClassificationDTO;
import jit.gxw.dto.ClassificationPageQueryDTO;
import jit.gxw.entity.Classification;
import jit.gxw.result.PageResult;
import jit.gxw.result.Result;
import jit.gxw.service.ClassificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController("UserClassificationController")
@RequestMapping("/user/classification")
@Api(tags = "查询车辆分类相关接口")
public class ClassificationController {
    @Autowired
    private ClassificationService classificationService;

    @GetMapping("/list")
    @ApiOperation("用户查询分类")
    @Cacheable(cacheNames = "Classification")
    public Result<List<Classification>> list(){
        log.info("用户查询分类");
        List<Classification> list =classificationService.list();

        return Result.success(list);
    }

}
