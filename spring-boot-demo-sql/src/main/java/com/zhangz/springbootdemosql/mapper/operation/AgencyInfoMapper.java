package com.zhangz.springbootdemosql.mapper.operation;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangz.springbootdemosql.entity.operation.AgencyInfo;

public interface AgencyInfoMapper extends BaseMapper<AgencyInfo> {
    AgencyInfo querybyAgencyName(String agencyName);
}
