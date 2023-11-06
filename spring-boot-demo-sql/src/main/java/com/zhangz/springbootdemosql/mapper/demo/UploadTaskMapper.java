package com.zhangz.springbootdemosql.mapper.demo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhangz.springbootdemosql.entity.demo.UploadTask;
import com.zhangz.springbootdemosql.entity.operation.AgencyInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UploadTaskMapper extends BaseMapper<UploadTask> {
    @Select("select * from  SYS_UPLOAD_TASK where FILE_IDENTIFIER = #{identifier}")

    UploadTask selectByIdentifier(@Param("identifier") String identifier);
    
}
