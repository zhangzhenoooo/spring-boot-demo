package com.zhangz.springbootdemosql.service.demo.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.zhangz.springbootdemosql.entity.demo.UploadTask;
import com.zhangz.springbootdemosql.mapper.demo.UploadTaskMapper;
import com.zhangz.springbootdemosql.mapper.operation.AgencyInfoMapper;
import com.zhangz.springbootdemosql.service.demo.UploadTaskService;
import com.zhangz.springbootdemosql.service.operation.AgencyInfoService;
import com.zhangz.springbootdemosql.entity.operation.AgencyInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Slf4j
@Service
public class UploadTaskServiceImpl extends ServiceImpl<UploadTaskMapper, UploadTask> implements UploadTaskService {
    @Resource
    private UploadTaskMapper uploadTaskMapper;

    @Override
    public UploadTask selectByIdentifier(String identifier) {
        return uploadTaskMapper.selectByIdentifier(identifier);
    }
}
