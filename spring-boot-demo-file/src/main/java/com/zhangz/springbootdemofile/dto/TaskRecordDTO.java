package com.zhangz.springbootdemofile.dto;

import com.amazonaws.services.s3.model.PartSummary;
import com.zhangz.springbootdemosql.entity.demo.UploadTask;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@ToString
@Accessors(chain = true)
public class TaskRecordDTO extends UploadTask {

    /**
     * 已上传完的分片
     */
    private List<PartSummary> exitPartList;

    public static TaskRecordDTO convertFromEntity (UploadTask task) {
        TaskRecordDTO dto = new TaskRecordDTO();
        BeanUtils.copyProperties(task, dto);
        return dto;
    }
}
