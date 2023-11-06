package com.zhangz.springbootdemosql.service.demo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhangz.springbootdemosql.entity.demo.UploadTask;
import com.zhangz.springbootdemosql.entity.operation.AgencyInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UploadTaskService extends IService<UploadTask> {
    
    UploadTask selectByIdentifier(String identifier);
    
}
