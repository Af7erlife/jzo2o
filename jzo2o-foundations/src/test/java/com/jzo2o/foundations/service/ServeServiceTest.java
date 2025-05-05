package com.jzo2o.foundations.service;

import cn.hutool.core.lang.Assert;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
@Slf4j
public class ServeServiceTest {
    @Resource
    ServeMapper serveMapper;
    @Test
    public void test() {
        List<ServeResDTO> resDTOS = serveMapper.queryServeListByRegionId(1692472339767234562L);
        Assert.notEmpty(resDTOS);
    }
}
