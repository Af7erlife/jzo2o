package com.jzo2o.customer.controller.consumer;

import com.jzo2o.api.customer.dto.response.AddressBookResDTO;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.customer.model.dto.request.AddressBookPageQueryReqDTO;
import com.jzo2o.customer.model.dto.request.AddressBookUpsertReqDTO;
import com.jzo2o.customer.service.IAddressBookService;
import com.jzo2o.mvc.utils.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/consumer/address-book")
@Api(tags = "用户详情-地址簿")
public class AddressBookController {

    @Resource
    IAddressBookService iAddressBookService;

    @PostMapping
    @ApiOperation("新增地址簿")
    public void add(@RequestBody AddressBookUpsertReqDTO reqDTO) {
        iAddressBookService.add(UserContext.currentUserId(),reqDTO);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询地址")
    public PageResult<AddressBookResDTO> queryPage(AddressBookPageQueryReqDTO reqDTO) {
        reqDTO.setUserId(UserContext.currentUserId());
        return iAddressBookService.queryPage(reqDTO);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据地址id查询详情")
    @ApiImplicitParam(name = "id",value = "地址id", required = true, dataTypeClass = Long.class)
    public AddressBookResDTO queryById(@PathVariable Long id) {
        return iAddressBookService.queryById(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("根据地址id修改详情")
    @ApiImplicitParam(name = "id",value = "地址id", required = true, dataTypeClass = Long.class)
    public void updateById(@RequestBody AddressBookUpsertReqDTO reqDTO, @PathVariable Long id) {
        iAddressBookService.updateById(reqDTO, id);
    }

    @DeleteMapping("/batch")
    @ApiOperation("根据地址ids删除")
    public void deleteById( @RequestBody List<Long> ids) {
        iAddressBookService.deleteByIds(ids);
    }

    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "地址id", required = true, dataTypeClass = Long.class),
            @ApiImplicitParam(name = "flag",value = "是否默认地址0：否，1：是", required = true, dataTypeClass = Integer.class)
    })
    public void setDefault(@RequestParam Long id, @RequestParam Integer flag) {
        iAddressBookService.setDefault(id, flag);
    }

    @GetMapping("/defaultAddress")
    @ApiOperation("获取当前登录用户的默认地址")
    public AddressBookResDTO getUserDefaultAddress() {
        return iAddressBookService.getUserDefaultAddress(UserContext.currentUserId());
    }

}
