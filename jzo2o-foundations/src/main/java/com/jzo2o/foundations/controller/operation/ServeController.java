package com.jzo2o.foundations.controller.operation;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.naming.Name;
import java.math.BigDecimal;
import java.util.List;


@RestController("operationServeController")
@RequestMapping("/operation/serve")
@Api(tags = "运营端 - 区域服务相关接口")
public class ServeController {
    @Autowired
    IServeService serveService;

    @GetMapping("/page")
    @ApiOperation("区域服务分页查询")
    public PageResult<ServeResDTO> queryPage(ServePageQueryReqDTO reqDTO) {
        PageResult<ServeResDTO> page = serveService.page(reqDTO);
        return page;
    }

    @PostMapping("/batch")
    @ApiOperation("批量添加区域服务")
    public void add(@RequestBody List<ServeUpsertReqDTO> reqDTOList) {
        serveService.batchAdd(reqDTOList);
    }

    @PutMapping("/{id}")
    @ApiOperation("修改价格")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class),
            @ApiImplicitParam(name = "price", value = "价格", required = true, dataTypeClass = BigDecimal.class)
    })
    public void updatePrice(@PathVariable("id") Long id, @RequestParam("price") BigDecimal price) {
        serveService.update(id, price);
    }

    @PutMapping("/onSale/{id}")
    @ApiOperation("上架服务")
    @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class)
    public void onSale(@PathVariable("id") Long id) {
        serveService.onSale(id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除指定id的服务")
    @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class)
    public void delete(@PathVariable("id") Long id) {
        serveService.delete(id);
    }
    @PutMapping("/offSale/{id}")
    @ApiOperation("下架服务")
    @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class)
    public void offSale(@PathVariable("id") Long id) {
        serveService.offSale(id);
    }

    @PutMapping("/onHot/{id}")
    @ApiOperation("设置热门")
    @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class)
    public void onHot(@PathVariable("id") Long id) {
        serveService.onHot(id);
    }

    @PutMapping("/offHot/{id}")
    @ApiOperation("下架热门")
    @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class)
    public void offHot(@PathVariable("id") Long id) {
        serveService.offHot(id);
    }
}
