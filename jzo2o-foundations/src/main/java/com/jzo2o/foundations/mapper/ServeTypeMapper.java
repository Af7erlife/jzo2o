package com.jzo2o.foundations.mapper;

import com.jzo2o.foundations.model.domain.ServeType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jzo2o.foundations.model.dto.response.serveTypeListResDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务类型表 Mapper 接口
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */
public interface ServeTypeMapper extends BaseMapper<ServeType> {
    List<serveTypeListResDTO> queryServeTypeListByRegionId(@Param("regionId") Long regionId);

}
