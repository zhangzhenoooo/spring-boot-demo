package com.zhangz.springbootdemosql.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.zhangz.springbootdemosql.mapper.AgencyInfoMapper;
import com.zhangz.springbootdemosql.service.AgencyInfoService;
import entity.AgencyInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class AgencyInfoServiceImpl extends ServiceImpl<AgencyInfoMapper, AgencyInfo> implements AgencyInfoService {

    @Autowired
    private AgencyInfoMapper agencyInfoMapper;

    /**
     *  mybatis plus query wrapper 使用案例 不方便排查SQL，本人不推荐  
     *   
     * @param info 单位信息
     * @return list
     */
    @Override
    public IPage<AgencyInfo> ListByAgencyInfo(AgencyInfo info) {

        Page<AgencyInfo> page = new Page();
        page.setCurrent(0);
        page.setSize(15);

        QueryWrapper<AgencyInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(StringUtils.isEmpty(info.getAgencyId()), AgencyInfo::getAgencyId, info.getAgencyId())
                .in(AgencyInfo::getIndustry, Lists.newArrayList("1","5"))
            .orderByAsc(AgencyInfo::getApplyTime);
        // IPage<AgencyInfo> agencyInfoIPage1 = agencyInfoMapper.selectPage(page, queryWrapper);
        IPage<AgencyInfo> agencyInfoIPage = this.page(page, queryWrapper);

        return agencyInfoIPage;
    }
}
