package com.zhangz.springbootdemostarter.file;

import com.zhangz.springbootdemofile.service.MinIOService;
import com.zhangz.springbootdemostarter.SpringBootDemoStarterApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StreamUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Slf4j
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class MinioTest {
    @Autowired
    private MinIOService minIOService;

    @Test
    public void upload() throws IOException {
        String path = Paths.get("BBB.pdf").toAbsolutePath().toString();
        byte[] bytes = StreamUtils.copyToByteArray(new FileInputStream(path));
        String filePath = "test/bbb.pdf";
        minIOService.upload(bytes, filePath);
        log.info("upload success!"); 
    }

    @Test
    public void download() {

    }

    @Test
    public void exist() {

    }

    @Test
    public void delete() {

    }
}
