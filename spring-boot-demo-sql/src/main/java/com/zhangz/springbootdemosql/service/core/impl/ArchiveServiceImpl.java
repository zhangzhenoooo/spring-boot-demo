package com.zhangz.springbootdemosql.service.core.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhangz.springbootdemosql.entity.core.Archive;
import com.zhangz.springbootdemosql.mapper.core.ArchiveMapper;
import com.zhangz.springbootdemosql.service.core.ArchiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class 

ArchiveServiceImpl extends ServiceImpl<ArchiveMapper, Archive> implements ArchiveService {
    @Autowired
    private ArchiveMapper archiveMapper;

    @Override
    public List<Archive> queryByCode(String code) {
        return archiveMapper.querybyCode(code);
    }
}
