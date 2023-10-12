package com.zhangz.springbootdemosql.service.operation;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangz.springbootdemosql.entity.operation.AgencyInfo;

public interface AgencyInfoService extends IService<AgencyInfo> {
    IPage<AgencyInfo> ListByAgencyInfo(AgencyInfo info);
}
