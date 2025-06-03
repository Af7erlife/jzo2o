package com.jzo2o.foundations.service;

import com.jzo2o.api.foundations.dto.response.RegionSimpleResDTO;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;
import com.jzo2o.foundations.model.dto.response.serveTypeListResDTO;

import java.util.List;

public interface HomeService {
    /**
     * 根据区域id获取服务图标信息
     *
     * @param regionId 区域id
     * @return 服务图标列表
     */
    List<ServeCategoryResDTO> queryServeIconCategoryByRegionIdCache(Long regionId);

    /**
     * 根据区域id查询服务类型
     * @param regionId
     * @return
     */
    List<serveTypeListResDTO> queryServeTypeList(Long regionId);

   void refreshRegionRelateCaches(Long regionId);

    List<RegionSimpleResDTO> activeRegionCache();

    /**
     * 根据区域id查询热门服务列表
     *
     * @param regionId 区域id
     * @return 服务列表
     */
    List<ServeAggregationSimpleResDTO> findHotServeListByRegionIdCache(Long regionId);

    Serve queryServeByIdCache(Long id);

    ServeItem queryServeItemByIdCache(Long id);
}
