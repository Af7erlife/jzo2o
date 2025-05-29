package com.jzo2o.customer.service;

import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.domain.AddressBook;
import com.jzo2o.customer.model.dto.request.AddressBookPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.AddressBookUpsertReqDTO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 地址薄 服务类
 * </p>
 *
 * @author itcast
 * @since 2023-07-06
 */
public interface IAddressBookService extends IService<AddressBook> {

    /**
     * 根据用户id和城市编码获取地址
     *
     * @param userId 用户id
     * @param cityCode 城市编码
     * @return 地址编码
     */
    List<AddressBookResDTO> getByUserIdAndCity(Long userId, String cityCode);

    int add(Long userId,AddressBookUpsertReqDTO addressBookUpsertReqDTO);

    PageResult<AddressBookResDTO> queryPage(AddressBookPageQueryReqDTO addressBookPageQueryReqDTO);

    AddressBookResDTO queryById(Long id);

    int updateById(AddressBookUpsertReqDTO addressBookUpsertReqDTO, Long id);

    int deleteByIds(List<Long> ids);

    int setDefault(Long id, Integer flag);

    AddressBookResDTO getUserDefaultAddress(Long userId);

    void cancelDefault(Long userId);
}
