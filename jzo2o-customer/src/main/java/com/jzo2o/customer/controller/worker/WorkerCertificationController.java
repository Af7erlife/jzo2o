package com.jzo2o.customer.controller.worker;

import com.jzo2o.customer.model.dto.request.WorkerCertificationAuditAddReqDTO;
import com.jzo2o.customer.service.IWorkerCertificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 服务人员认证信息表 前端控制器
 * </p>
 *
 * @author author
 * @since 2025-05-29
 */
@RestController
@RequestMapping("/worker-certification-audit")
@Api(value = "服务人员认证接口")
public class WorkerCertificationController {
    @Resource
    IWorkerCertificationService workerCertificationService;

    @PostMapping
    @ApiOperation("服务端提交认证申请")
    public void upsert(WorkerCertificationAuditAddReqDTO reqDTO) {
        workerCertificationService.upsert(reqDTO);
    }

}
