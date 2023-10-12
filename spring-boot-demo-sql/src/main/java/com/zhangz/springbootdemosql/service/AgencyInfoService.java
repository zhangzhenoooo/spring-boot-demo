package com.zhangz.springbootdemosql.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import entity.AgencyInfo;

public interface AgencyInfoService extends IService<AgencyInfo> {
    IPage<AgencyInfo> ListByAgencyInfo(AgencyInfo info);
}
