package com.zhangz.springbootdemostarter.sql;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zhangz.springbootdemosql.entity.core.Archive;
import com.zhangz.springbootdemosql.mapper.operation.AgencyInfoMapper;
import com.zhangz.springbootdemosql.entity.operation.AgencyInfo;
import com.zhangz.springbootdemosql.service.core.ArchiveService;
import com.zhangz.springbootdemosql.service.operation.AgencyInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@Slf4j
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SqlTest {
    @Autowired
    private AgencyInfoMapper agencyInfoMapper;
    @Autowired
    private AgencyInfoService agencyInfoService;
    @Autowired
    private ArchiveService archiveService;

    @Test
    public void queryByAgencyName() {
        AgencyInfo agencyInfo = agencyInfoMapper.querybyAgencyName("二连浩特市医疗通用测试单位");
        log.info("agencyInfo = {}", agencyInfo.toString());
    }

    @Test
    public void ListByAgencyInfo() {
        IPage<AgencyInfo> agencyInfoIPage = agencyInfoService.ListByAgencyInfo(new AgencyInfo());
        log.info("agencyInfoIPage = {}", JSON.toJSONString(agencyInfoIPage));
    }

    @Test
    public void queryArchiveByCode() {
        List<Archive> archive = archiveService.queryByCode("15060123");
        log.info("archive = {}", archive.toString());
    }

}
