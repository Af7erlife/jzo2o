package com.jzo2o.customer.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.api.publics.MapApi;
import com.jzo2o.api.publics.dto.response.LocationResDTO;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.CollUtils;
import com.jzo2o.common.utils.NumberUtils;
import com.jzo2o.common.utils.StringUtils;
import com.jzo2o.enums.customer.LogicEnum;
import com.jzo2o.customer.mapper.AddressBookMapper;
import com.jzo2o.customer.model.domain.AddressBook;
import com.jzo2o.customer.model.dto.request.AddressBookPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.AddressBookUpsertReqDTO;
import com.jzo2o.customer.service.IAddressBookService;
import com.jzo2o.mysql.utils.PageUtils;
import com.jzo2o.mysql.utils.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 地址薄 服务实现类
 * </p>
 *
 * @author itcast
 * @since 2023-07-06
 */
@Service
@Slf4j
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements IAddressBookService {

    @Resource
    private MapApi mapApi;

    @Override
    public List<AddressBookResDTO> getByUserIdAndCity(Long userId, String city) {

        List<AddressBook> addressBooks = lambdaQuery()
                .eq(AddressBook::getUserId, userId)
                .eq(AddressBook::getCity, city)
                .list();
        if(CollUtils.isEmpty(addressBooks)) {
            return new ArrayList<>();
        }
        return BeanUtils.copyToList(addressBooks, AddressBookResDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(Long userId,AddressBookUpsertReqDTO addressBookUpsertReqDTO) {
        //如果新增地址设为默认，取消其他默认地址
        if (LogicEnum.YES.getStatus() == addressBookUpsertReqDTO.getIsDefault()) {
            SpringBeanUtil.getBean(IAddressBookService.class).cancelDefault(userId);
        }

        AddressBook addressBook = BeanUtil.toBean(addressBookUpsertReqDTO, AddressBook.class);
        addressBook.setUserId(userId);

        //组装详细地址
        String completeAddress = addressBookUpsertReqDTO.getProvince() +
                addressBookUpsertReqDTO.getCity() +
                addressBookUpsertReqDTO.getCounty() +
                addressBookUpsertReqDTO.getAddress();

        //如果请求体中没有经纬度，需要调用第三方api根据详细地址获取经纬度
        if(ObjectUtil.isEmpty(addressBookUpsertReqDTO.getLocation())){
            //远程请求高德获取经纬度
            LocationResDTO locationDto = mapApi.getLocationByAddress(completeAddress);
            //经纬度(字符串格式：经度,纬度),经度在前，纬度在后
            String location = locationDto.getLocation();
            addressBookUpsertReqDTO.setLocation(location);
        }

        if(StringUtils.isNotEmpty(addressBookUpsertReqDTO.getLocation())) {
            // 经度
            addressBook.setLon(NumberUtils.parseDouble(addressBookUpsertReqDTO.getLocation().split(",")[0]));
            // 纬度
            addressBook.setLat(NumberUtils.parseDouble(addressBookUpsertReqDTO.getLocation().split(",")[1]));
        }
        return baseMapper.insert(addressBook);
    }

    @Override
    public PageResult<AddressBookResDTO> queryPage(AddressBookPageQueryReqDTO addressBookPageQueryReqDTO) {
        Page<AddressBook> page = PageUtils.parsePageQuery(addressBookPageQueryReqDTO,AddressBook.class);
        LambdaQueryWrapper<AddressBook> queryWrapper = Wrappers.<AddressBook>lambdaQuery()
                .eq(AddressBook::getUserId,addressBookPageQueryReqDTO.getUserId())
                .eq(AddressBook::getIsDeleted,LogicEnum.NO.getStatus());
        Page<AddressBook> addressBookPage = baseMapper.selectPage(page, queryWrapper);
        return PageUtils.toPage(addressBookPage, AddressBookResDTO.class);
    }

    @Override
    public AddressBookResDTO queryById(Long id) {
        AddressBook addressBook = baseMapper.selectById(id);
        return BeanUtils.toBean(addressBook,AddressBookResDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(AddressBookUpsertReqDTO addressBookUpsertReqDTO, Long id) {
        AddressBook addressBook = baseMapper.selectById(id);
        if(LogicEnum.YES.getStatus() == addressBookUpsertReqDTO.getIsDefault()) {//如果更新为默认，则取消其他的默认
            SpringBeanUtil.getBean(IAddressBookService.class).cancelDefault(addressBook.getUserId());
        }
        addressBook = BeanUtils.toBean(addressBookUpsertReqDTO, AddressBook.class);
        addressBook.setId(id);

        //组装详细地址
        String completeAddress = addressBookUpsertReqDTO.getProvince() +
                addressBookUpsertReqDTO.getCity() +
                addressBookUpsertReqDTO.getCounty() +
                addressBookUpsertReqDTO.getAddress();

        //如果请求体中没有经纬度，需要调用第三方api根据详细地址获取经纬度
        if(ObjectUtil.isEmpty(addressBookUpsertReqDTO.getLocation())){
            //远程请求高德获取经纬度
            LocationResDTO locationDto = mapApi.getLocationByAddress(completeAddress);
            //经纬度(字符串格式：经度,纬度),经度在前，纬度在后
            String location = locationDto.getLocation();
            addressBookUpsertReqDTO.setLocation(location);
        }

        if(StringUtils.isNotEmpty(addressBookUpsertReqDTO.getLocation())) {
            // 经度
            addressBook.setLon(NumberUtils.parseDouble(addressBookUpsertReqDTO.getLocation().split(",")[0]));
            // 纬度
            addressBook.setLat(NumberUtils.parseDouble(addressBookUpsertReqDTO.getLocation().split(",")[1]));
        }

        int rows = baseMapper.updateById(addressBook);
        if(rows == 0){
            log.info("未查询到该地址信息{}数据！",id);
        }
        return rows;
    }

    @Override
    @Transactional
    public int deleteByIds(List<Long> ids) {
        int rows = baseMapper.deleteBatchIds(ids);
        if(rows != ids.size()){
            log.info("未完全删除完成！");
        }
        return rows;
    }

    @Override
    @Transactional
    public int setDefault(Long id, Integer flag) {
        AddressBook addressBook = baseMapper.selectById(id);
        if(ObjectUtil.isEmpty(addressBook)){
            throw new ForbiddenOperationException("不存在该id的地址！");
        }
        if(flag == LogicEnum.YES.getStatus()){//如果设置为默认，则取消其他的默认
            SpringBeanUtil.getBean(IAddressBookService.class).cancelDefault(addressBook.getUserId());
        }
        AddressBook updateAddressBook = new AddressBook();
        updateAddressBook.setId(id);
        updateAddressBook.setIsDefault(flag);
        int rows = baseMapper.updateById(updateAddressBook);
        if(rows == 0){
            log.info("未更新成功{}！",id);
        }
        log.info("更新成功{}！",id);
        return rows;
    }

    @Override
    public AddressBookResDTO getUserDefaultAddress(Long userId) {
        AddressBook addressBook = lambdaQuery().eq(AddressBook::getUserId,userId).eq(AddressBook::getIsDefault,LogicEnum.YES.getStatus()).one();
        return BeanUtils.toBean(addressBook,AddressBookResDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelDefault(Long userId) {
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId, userId);
        AddressBook addressBook = new AddressBook();
        addressBook.setIsDefault(LogicEnum.NO.getStatus());
        int rows = baseMapper.update(addressBook,updateWrapper);
        if(rows == 0){
            log.info("用户{}未有默认地址需删除！",userId);
        }
    }
}
