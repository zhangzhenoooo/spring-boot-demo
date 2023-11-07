CREATE TABLE sys_upload_task
(
    id              varchar(32)    PRIMARY KEY NOT NULL COMMENT '主键ID',
    upload_id       varchar(255)       NOT NULL COMMENT '分片上传的uploadId',
    file_identifier varchar(500)       NOT NULL COMMENT '文件唯一标识（md5）',
    file_name       varchar(500)       NOT NULL COMMENT '文件名',
    bucket_name     varchar(255)       NOT NULL COMMENT '所属桶名',
    object_key      varchar(500)       NOT NULL COMMENT '文件的key',
    total_size      bigint             NOT NULL COMMENT '文件大小（byte）',
    chunk_size      bigint             NOT NULL COMMENT '每个分片大小（byte）',
    chunk_num       int                NOT NULL COMMENT '分片数量'
);
comment on table SYS_UPLOAD_TASK is '分片上传-分片任务记录';
create unique index SYS_UPLOAD_TASK_UPLOAD_ID_UINDEX on SYS_UPLOAD_TASK (UPLOAD_ID);
create unique index SYS_UPLOAD_TASK_file_identifier_UINDEX on SYS_UPLOAD_TASK (file_identifier);