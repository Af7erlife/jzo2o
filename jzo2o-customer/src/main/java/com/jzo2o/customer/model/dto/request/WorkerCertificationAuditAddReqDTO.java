package com.jzo2o.customer.model.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 服务人员申请资质认证
 *
 * @author itcast
 * @create 2023/9/6 10:24
 **/
@Data
@ApiModel("服务人员认证申请请求体")
public class WorkerCertificationAuditAddReqDTO {
    @ApiModelProperty(value = "服务人员id", required = false)
    private Long serveProviderId;

    @ApiModelProperty(value = "姓名", required = true)
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    @NotBlank(message = "姓名不能为空")
    private String name;

    @ApiModelProperty(value = "身份证号", required = true)
    @Size(max = 50, message = "身份证号应为18位！")
    @NotBlank(message = "身份证号不能为空")
    private String idCardNo;

    @ApiModelProperty(value = "身份证正面", required = true)
    @NotBlank(message = "身份证正面不能为空")
    private String frontImg;

    @ApiModelProperty(value = "身份证反面", required = true)
    @NotBlank(message = "身份证反面不能为空")
    private String backImg;

    @ApiModelProperty(value = "证明资料", required = true)
    @NotBlank(message = "证明资料不能为空")
    private String certificationMaterial;
}
