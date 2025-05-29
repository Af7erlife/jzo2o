package com.jzo2o.foundations.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.ObjectUtils;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.enums.StatusEnum;
import com.jzo2o.foundations.mapper.RegionMapper;
import com.jzo2o.foundations.mapper.ServeItemMapper;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import com.jzo2o.mysql.utils.PageHelperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 * 服务表 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-04-28
 */
@Service
@Slf4j
public class ServeServiceImpl extends ServiceImpl<ServeMapper, Serve> implements IServeService {

    @Resource
    ServeItemMapper serveItemMapper;

    @Resource
    RegionMapper regionMapper;

    public PageResult<ServeResDTO> page(ServePageQueryReqDTO reqDTO) {
        //调用mapper查询数据，这里由于继承了ServiceImpl<ServeMapper, Serve>，使用baseMapper相当于使用ServeMapper
        PageResult<ServeResDTO> serveResDTOPageResult = PageHelperUtils.selectPage(reqDTO, () -> baseMapper.queryServeListByRegionId(reqDTO.getRegionId()));
        return serveResDTOPageResult;
    }

    @Override
    @Transactional
    public void batchAdd(List<ServeUpsertReqDTO> serveUpsertReqDTOList) {
        if(serveUpsertReqDTOList == null || serveUpsertReqDTOList.isEmpty()){
            throw new ForbiddenOperationException("添加服务列表为空!");
        }
        for(ServeUpsertReqDTO reqDTO : serveUpsertReqDTOList){
            //1.校验服务项是否为启用状态，不是启用状态不能新增
            ServeItem serveItem = serveItemMapper.selectById(reqDTO.getServeItemId());
            if(ObjectUtils.isEmpty(serveItem) || !FoundationStatusEnum.ENABLE.equals(serveItem.getActiveStatus())){
                throw new ForbiddenOperationException("服务不存在或服务未启用，无法添加到区域！");
            }
            //2.校验是否重复新增
            Integer count = lambdaQuery()
                    .eq(Serve::getRegionId, reqDTO.getRegionId())
                    .eq(Serve::getServeItemId, reqDTO.getServeItemId())
                    .count();
            if(count>0){
                throw new ForbiddenOperationException(serveItem.getName()+"服务已存在");
            }

            //3.新增服务
            Serve serve = BeanUtil.toBean(reqDTO, Serve.class);
            Region region = regionMapper.selectById(reqDTO.getRegionId());//根据区域id获取区域
            serve.setCityCode(region.getCityCode());
            baseMapper.insert(serve);
        }
    }

    /**
     * 更新价格
     * @param id    服务id
     * @param price 价格
     */
    @Override
    @Transactional
    public Serve update(Long id, BigDecimal price) {
        boolean isUpdate = lambdaUpdate().eq(Serve::getId, id).set(Serve::getPrice,price).update();
        if(!isUpdate){
            throw new CommonException("修改价格失败！");
        }
        return baseMapper.selectById(id);
    }

    @Override
    @Transactional
    public Serve onSale(Long id) {
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtils.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在！");
        }
        Integer status = serve.getSaleStatus();
        if(!(status == FoundationStatusEnum.INIT.getStatus() || status == FoundationStatusEnum.DISABLE.getStatus())){
            throw new ForbiddenOperationException("草稿或下架状态才能上架！");
        }
        //服务项id
        Long serveItemId = serve.getServeItemId();
        ServeItem serveItem = serveItemMapper.selectById(serveItemId);
        if(ObjectUtil.isNull(serveItem)){
            throw new ForbiddenOperationException("所属服务项不存在");
        }
        //服务项的启用状态
        Integer activeStatus = serveItem.getActiveStatus();
        //服务项为启用状态方可上架
        if (!(FoundationStatusEnum.ENABLE.getStatus()==activeStatus)) {
            throw new ForbiddenOperationException("服务项为启用状态方可上架");
        }
        //更新上架状态
        boolean update = lambdaUpdate()
                .eq(Serve::getId, id)
                .set(Serve::getSaleStatus, FoundationStatusEnum.ENABLE.getStatus())
                .update();
        if(!update){
            throw new CommonException("启动服务失败");
        }
        return baseMapper.selectById(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtils.isNull(serve)){
            throw new ForbiddenOperationException("不存在该id的服务！");
        }
        Integer status = serve.getSaleStatus();
        //草稿状态才能删除
        if(status != FoundationStatusEnum.INIT.getStatus()){
            throw new ForbiddenOperationException("只能删除草稿状态的服务！");
        }
        int delete = baseMapper.deleteById(id);
        if(delete > 0 ){
            log.info("删除成功！");
        }else {
            throw new ForbiddenOperationException("删除失败！");
        }
    }

    @Override
    @Transactional
    public Serve offSale(Long id) {
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtils.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在！");
        }
        Integer status = serve.getSaleStatus();
        if(!(FoundationStatusEnum.ENABLE.getStatus()==status)){
            throw new ForbiddenOperationException("只能下架处在上架状态的服务！");
        }
        boolean update = lambdaUpdate().eq(Serve::getId, id).set(Serve::getSaleStatus,FoundationStatusEnum.DISABLE.getStatus()).update();
        if(!update){
            throw new CommonException("下架服务失败");
        }
        return baseMapper.selectById(id);
    }

    @Override
    @Transactional
    public Serve onHot(Long id){
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtils.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在！");
        }
        Integer isHot = serve.getIsHot();
        if(isHot == StatusEnum.YES.getCode()){
            throw new ForbiddenOperationException("该服务已经是热门了！");
        }
        Integer status = serve.getSaleStatus();
        if(!(FoundationStatusEnum.ENABLE.getStatus()==status)){
            throw new ForbiddenOperationException("需在上架状态才能修改热门！");
        }
        boolean update = lambdaUpdate().eq(Serve::getId,id).set(Serve::getIsHot,StatusEnum.YES.getCode()).update();
        if(!update){
            throw new CommonException("更新热门失败！");
        }
        return baseMapper.selectById(id);
    }

    @Override
    @Transactional
    public Serve offHot(Long id){
        Serve serve = baseMapper.selectById(id);
        if(ObjectUtils.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在！");
        }
        Integer isHot = serve.getIsHot();
        if(isHot == StatusEnum.NO.getCode()){
            throw new ForbiddenOperationException("该服务已经取消热门了！");
        }
        Integer status = serve.getSaleStatus();
        if(!(FoundationStatusEnum.ENABLE.getStatus()==status)){
            throw new ForbiddenOperationException("需在上架状态才能修改热门！");
        }
        boolean update = lambdaUpdate().eq(Serve::getId,id).set(Serve::getIsHot,StatusEnum.NO.getCode()).update();
        if(!update){
            throw new CommonException("更新热门失败！");
        }
        return baseMapper.selectById(id);
    }

    @Override
    public List<Serve> queryServeByRegionIdAndSaleStatus(Long regionId, Integer saleStatus) {
        LambdaQueryWrapper<Serve> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Serve::getRegionId, regionId);
        queryWrapper.eq(Serve::getSaleStatus, saleStatus);
        return baseMapper.selectList(queryWrapper);
    }
}
