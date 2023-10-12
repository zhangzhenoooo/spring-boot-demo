package com.zhangz.springbootdemostarter.sql;

import com.zhangz.springbootdemofile.service.MinIOService;
import com.zhangz.springbootdemosql.mapper.AgencyInfoMapper;
import entity.AgencyInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StreamUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Slf4j
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SqlTest {
    @Autowired
    private AgencyInfoMapper agencyInfoMapper;

    @Test
    public void queryByAgencyName() throws IOException {
        AgencyInfo agencyInfo = agencyInfoMapper.querybyAgencyName("二连浩特市医疗通用测试单位");
        log.info("agencyInfo = {}", agencyInfo.toString());
    }

}
