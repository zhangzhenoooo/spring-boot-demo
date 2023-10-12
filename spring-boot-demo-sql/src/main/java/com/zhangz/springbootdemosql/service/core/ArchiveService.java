package com.zhangz.springbootdemosql.service.core;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangz.springbootdemosql.entity.core.Archive;

import java.util.List;

public interface ArchiveService extends IService<Archive> {
    List<Archive> queryByCode(String code);
}
