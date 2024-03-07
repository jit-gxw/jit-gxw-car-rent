package jit.gxw.controller.admin;


import io.swagger.annotations.Api;
import jit.gxw.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/report")
@Api(tags = "管理端数据统计接口")
@Slf4j
@PreAuthorize("hasAuthority('admin:report')")
public class ReportController {
    @Autowired
    private ReportService reportService;



}
