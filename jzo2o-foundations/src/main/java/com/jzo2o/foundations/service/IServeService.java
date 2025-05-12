package com.jzo2o.foundations.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
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
    /**
     * 分页查询
     * @param reqDTO
     * @return
     */
    PageResult<ServeResDTO> page(ServePageQueryReqDTO reqDTO);

    /**
     * 批量新增
     * @param serveUpsertReqDTOList 批量新增数据
     */
    void batchAdd(List<ServeUpsertReqDTO> serveUpsertReqDTOList);

    /**
     * 服务价格修改
     *
     * @param id    服务id
     * @param price 价格
     * @return 服务
     */
    Serve update(Long id, BigDecimal price);

    /**
     * 上架商品
     * @param id
     * @return
     */
    Serve onSale(Long id);

    /**
     * 删除指定服务
     * @param id
     */
    void delete(Long id);

    /**
     * 下架服务
     * @param id
     */
    Serve offSale(Long id);

    Serve onHot(Long id);

    Serve offHot(Long id);

    /**
     * 根据服务区域id和状态查询服务
     * @param regionId
     * @param saleStatus
     * @return
     */
    List<Serve> queryServeByRegionIdAndSaleStatus(Long regionId, Integer saleStatus);
}
