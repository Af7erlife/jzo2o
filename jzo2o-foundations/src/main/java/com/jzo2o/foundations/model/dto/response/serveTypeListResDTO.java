package com.jzo2o.foundations.model.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("服务类型列表响应值")
public class serveTypeListResDTO {
    /*** 主键*/
    @ApiModelProperty("主键")
    private Long serveTypeId;
    /*** 服务类型图片*/
    @ApiModelProperty("服务类型图片")

    private String serveTypeImg;
    /*** 服务类型名称*/
    @ApiModelProperty("服务类型名称")
    private String serveTypeName;
    /*** 服务类型排序字段*/
    @ApiModelProperty("排序字段")
    private Integer serveTypeSortNum;
}