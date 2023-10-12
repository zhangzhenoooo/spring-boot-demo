package com.zhangz.springbootdemosql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import entity.AgencyInfo;

public interface AgencyInfoMapper extends BaseMapper<AgencyInfo> {
    AgencyInfo querybyAgencyName(String agencyName);
}
