package com.jzo2o.foundations.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务表 服务类
 * </p>
 *
 * @author author
 * @since 2025-04-28
 */
public interface IServeService extends IService<Serve> {
    PageResult<ServeResDTO> page(ServePageQueryReqDTO reqDTO);
}
