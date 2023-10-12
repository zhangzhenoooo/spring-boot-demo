package com.zhangz.springbootdemosql.mapper.core;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangz.springbootdemosql.entity.core.Archive;

import java.util.List;

public interface ArchiveMapper extends BaseMapper<Archive> {
    /**
     * @param invoiceCode 
     * @return
     */
    List<Archive> querybyCode (String invoiceCode);
}
