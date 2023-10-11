package com.zhangz.springbootdemoapi.controller;

import com.zhangz.springbootdemofile.service.MinIOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.nio.file.Paths;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {
    
    @Autowired
    private MinIOService minIOService;
    
    @PostMapping("/upload")
    @ResponseBody
    public String upload() throws Exception{
        String path = Paths.get("BBB.pdf").toAbsolutePath().toString();
        byte[] bytes = StreamUtils.copyToByteArray(new FileInputStream(path));
        String filePath = "test/bbb.pdf";
        minIOService.upload(bytes, filePath);
        log.info("upload success!");
        return "ok";
    }
}
