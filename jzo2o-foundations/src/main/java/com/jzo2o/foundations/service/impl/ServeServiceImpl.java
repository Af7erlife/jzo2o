package com.jzo2o.foundations.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import com.jzo2o.mysql.utils.PageHelperUtils;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 服务表 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-04-28
 */
@Service
public class ServeServiceImpl extends ServiceImpl<ServeMapper, Serve> implements IServeService {

    public PageResult<ServeResDTO> page(ServePageQueryReqDTO reqDTO) {
        //调用mapper查询数据，这里由于继承了ServiceImpl<ServeMapper, Serve>，使用baseMapper相当于使用ServeMapper
        PageResult<ServeResDTO> serveResDTOPageResult = PageHelperUtils.selectPage(reqDTO, () -> baseMapper.queryServeListByRegionId(reqDTO.getRegionId()));
        return serveResDTOPageResult;
    }
}
