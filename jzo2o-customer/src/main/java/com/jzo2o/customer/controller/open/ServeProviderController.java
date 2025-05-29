package com.jzo2o.customer.controller.open;

import com.jzo2o.customer.model.dto.request.InstitutionRegisterReqDTO;
import com.jzo2o.customer.model.dto.request.InstitutionResetPasswordReqDTO;
import com.jzo2o.customer.model.dto.response.LoginResDTO;
import com.jzo2o.customer.service.IServeProviderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("ServeProviderController")
@RequestMapping("/open/serve-provider")
@Api(tags = "服务提供接口")
public class ServeProviderController {
    @Resource
    IServeProviderService serveProviderService;

    @PostMapping("/institution/register")
    @ApiOperation("机构端-注册")
    public void register(@RequestBody InstitutionRegisterReqDTO reqDTO) {
        serveProviderService.register(reqDTO);
    }

}
