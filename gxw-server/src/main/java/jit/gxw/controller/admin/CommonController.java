package jit.gxw.controller.admin;


import com.aliyuncs.exceptions.ClientException;
import jit.gxw.constant.MessageConstant;
import jit.gxw.context.BaseContext;
import jit.gxw.result.Result;
import jit.gxw.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(tags ="通用接口")
@Slf4j
@PreAuthorize("hasAuthority('admin:common')")
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 上传文件
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public Result<String> upload(MultipartFile file){
        log.info("上传文件：{}",file);

        try {
            String originalFilename = file.getOriginalFilename();
            String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = "EmployeeId:"+ BaseContext.getCurrentId().toString()+"::" +UUID.randomUUID().toString() + substring;


            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        } catch (ClientException | IOException e) {
            log.error("文件上传失败:{}",e);
        }


        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
