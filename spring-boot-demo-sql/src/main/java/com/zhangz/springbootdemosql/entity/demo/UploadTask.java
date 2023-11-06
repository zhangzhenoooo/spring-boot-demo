package com.zhangz.springbootdemosql.entity.demo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("SYS_UPLOAD_TASK")

public class UploadTask implements Serializable {
    @TableId("ID")
    private int id;

    @TableField("UPLOAD_ID")
    private String uploadId;

    @TableField("FILE_IDENTIFIER")
    private String fileIdentifier;

    @TableField("FILE_NAME")
    private String fileName;

    @TableField("BUCKET_NAME")
    private String bucketName;

    @TableField("OBJECT_KEY")
    private String objectKey;

    @TableField("TOTAL_SIZE")
    private Long totalSize;

    @TableField("CHUNK_SIZE")
    private Long chunkSize;

    @TableField("CHUNK_NUM")
    private int chunkNum;

}
