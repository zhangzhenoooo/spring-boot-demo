package com.zhangz.springbootdemofile.service;

import com.zhangz.springbootdemocommon.exception.BussinessException;
import com.zhangz.springbootdemofile.dto.InitTaskParam;
import com.zhangz.springbootdemofile.dto.TaskInfoDTO;

import java.io.IOException;
import java.util.Map;

public interface MinIOService {

    void upload(byte[] bytes, String filePath) throws IOException;

    byte[] downByPath(String filePath) throws Exception;

    boolean isExist(String path) throws BussinessException;

    boolean remove(String path) throws BussinessException;

    TaskInfoDTO initTask(InitTaskParam param);

    TaskInfoDTO getTaskInfo(String identifier);

    TaskInfoDTO getByIdentifier(String identifier);

    String genPreSignUploadUrl(String bucketName, String objectKey, Map<String, String> params);

    void merge(String identifier);
}
