package com.zhangz.springbootdemofile.service;

import com.zhangz.springbootdemocommon.exception.BussinessException;

import java.io.IOException;

public interface MinIOService {

    void upload(byte[] bytes, String filePath) throws IOException;
    
    byte[] downByPath(String filePath) throws Exception;

    boolean isExist(String path) throws BussinessException;

    boolean remove(String path) throws BussinessException;
    
}
