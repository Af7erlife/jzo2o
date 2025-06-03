package com.jzo2o.foundations.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.jzo2o.api.foundations.dto.response.RegionSimpleResDTO;
import com.jzo2o.foundations.constants.RedisConstants;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.mapper.ServeTypeMapper;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;
import com.jzo2o.foundations.model.dto.response.ServeSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.serveTypeListResDTO;
import com.jzo2o.foundations.service.*;
import com.jzo2o.mysql.utils.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class HomeServiceImpl implements HomeService {
    @Resource
    private ServeMapper serveMapper;

    @Resource
    private IRegionService regionService;

    @Resource
    private IServeService serveService;

    @Resource
    private IServeTypeService serveTypeService;

    @Resource
    private ServeTypeMapper serveTypeMapper;

    @Resource
    private IServeItemService serveItemService;
    /**
     * 首页查询相关功能
     *
     * @author itcast
     * @create 2023/8/21 10:57
     **/
    @Caching(
            cacheable = {
                    //result为null时,属于缓存穿透情况，缓存时间30分钟
                    @Cacheable(value = RedisConstants.CacheName.SERVE_ICON, key = "#regionId", unless = "#result.size() != 0", cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES),
                    //result不为null时,永久缓存
                    @Cacheable(value = RedisConstants.CacheName.SERVE_ICON, key = "#regionId", unless = "#result.size() == 0", cacheManager = RedisConstants.CacheManager.FOREVER)
            }
    )
    public List<ServeCategoryResDTO> queryServeIconCategoryByRegionIdCache(Long regionId) {
        //1.校验当前城市是否为启用状态
        Region region = regionService.getById(regionId);
        if (ObjectUtil.isEmpty(region) || ObjectUtil.equal(FoundationStatusEnum.DISABLE.getStatus(), region.getActiveStatus())) {
            return Collections.emptyList();
        }

        //2.根据城市编码查询所有的服务图标
        List<ServeCategoryResDTO> list = serveMapper.findServeIconCategoryByRegionId(regionId);
        if (ObjectUtil.isEmpty(list)) {
            return Collections.emptyList();
        }

        //3.服务类型取前两个，每个类型下服务项取前4个
        //list的截止下标
        int endIndex = list.size() >= 2 ? 2 : list.size();
        List<ServeCategoryResDTO> serveCategoryResDTOS = new ArrayList<>(list.subList(0, endIndex));
        serveCategoryResDTOS.forEach(v -> {
            List<ServeSimpleResDTO> serveResDTOList = v.getServeResDTOList();
            //serveResDTOList的截止下标
            int endIndex2 = serveResDTOList.size() >= 4 ? 4 : serveResDTOList.size();
            List<ServeSimpleResDTO> serveSimpleResDTOS = new ArrayList<>(serveResDTOList.subList(0, endIndex2));
            v.setServeResDTOList(serveSimpleResDTOS);
        });

        return serveCategoryResDTOS;
    }


    @Override
    @Cacheable(value = RedisConstants.CacheName.SERVE_TYPE,key = "#regionId",cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES)
    public List<serveTypeListResDTO> queryServeTypeList(Long regionId) {
        return serveTypeMapper.queryServeTypeListByRegionId(regionId);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = RedisConstants.CacheName.SERVE_ICON, key = "#id", beforeInvocation = true),
            @CacheEvict(value = RedisConstants.CacheName.HOT_SERVE, key = "#id", beforeInvocation = true),
            @CacheEvict(value = RedisConstants.CacheName.SERVE_TYPE, key = "#id", beforeInvocation = true)})
    public void refreshRegionRelateCaches(Long regionId) {
        // 更新每个区域对应的首页服务列表
        serveService.getFirstPageServeList(regionId);

        // 更新每个区域对应的服务类型列表
        serveTypeService.getServeTypeList(regionId);

        // 更新每个区域对应的热门服务列表
        SpringBeanUtil.getBean(HomeService.class).findHotServeListByRegionIdCache(regionId);
    }


    @Override
    public List<RegionSimpleResDTO> activeRegionCache() {
        return regionService.queryActiveRegionList();
    }

    /**
     * 根据区域id查询热门服务列表
     *
     * @param regionId 区域id
     * @return 服务列表
     */
    @Override
    @Caching(
            cacheable = {
                    //result为null时,属于缓存穿透情况，缓存时间30分钟
                    @Cacheable(value = RedisConstants.CacheName.HOT_SERVE, key = "#regionId", unless = "#result.size() != 0", cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES),
                    //result不为null时,永久缓存
                    @Cacheable(value = RedisConstants.CacheName.HOT_SERVE, key = "#regionId", unless = "#result.size() == 0", cacheManager = RedisConstants.CacheManager.FOREVER)
            }
    )
    public List<ServeAggregationSimpleResDTO> findHotServeListByRegionIdCache(Long regionId) {
        //1.校验当前城市是否为启用状态
        Region region = regionService.getById(regionId);
        if (ObjectUtil.equal(FoundationStatusEnum.DISABLE.getStatus(), region.getActiveStatus())) {
            return Collections.emptyList();
        }

        //2.根据城市编码查询热门服务
        List<ServeAggregationSimpleResDTO> list = serveService.findHotServeListByRegionId(regionId);
        if (ObjectUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list;
    }

    /**
     * 根据id查询区域服务信息
     *
     * @param id 服务id
     * @return 服务
     */
    @Override
    @Cacheable(value = RedisConstants.CacheName.SERVE, key = "#id", cacheManager = RedisConstants.CacheManager.ONE_DAY)
    public Serve queryServeByIdCache(Long id) {
        return serveService.getById(id);
    }

    /**
     * 根据id查询服务项
     *
     * @param id 服务项id
     * @return 服务项
     */
    @Override
    @Cacheable(value = RedisConstants.CacheName.SERVE_ITEM, key = "#id", cacheManager = RedisConstants.CacheManager.ONE_DAY)
    public ServeItem queryServeItemByIdCache(Long id) {
        return serveItemService.getById(id);
    }
}
