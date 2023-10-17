package com.zhangz.springbootdemofile.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.zhangz.springbootdemofile.config.FileConfig;
import com.zhangz.springbootdemofile.config.MimeTypeEnum;
import com.zhangz.springbootdemofile.service.MinIOService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.vfs2.FileObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystemException;
import com.zhangz.springbootdemocommon.exception.BussinessException;

@Slf4j
@Component
@Service(interfaceClass = MinIOService.class,timeout = 1000)
public class MinIOServiceImpl implements MinIOService {
    @Autowired
    private FileObject baseFolder;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private FileConfig fileConfig;

    @Override
    public void upload(byte[] bytes, String filePath) throws IOException {
        String fileType = filePath.substring(filePath.lastIndexOf(".") + 1);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        PutObjectArgs putObjectArgs = PutObjectArgs.builder().contentType(MimeTypeEnum.getContentType(fileType)).stream(inputStream, inputStream.available(), -1)
            .bucket(fileConfig.getMinioBucketName()).object(filePath).build();
        try {
            minioClient.putObject(putObjectArgs);
        } catch (Exception e) {
            log.error("minio上传失败", e);
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public byte[] downByPath(String filePath) throws Exception {
        try {

            if (StringUtils.isEmpty(filePath)) {
                log.error("文件路径为空");
                return new byte[0];
            }
            return download(filePath);
        } catch (FileSystemException | org.apache.commons.vfs2.FileSystemException e) {
            log.error("文件【{}】下载失败", filePath, e);
            throw new Exception("文件下载异常");
        }
    }

    @Override
    public boolean isExist(String path) throws BussinessException {
        try {
            FileObject fileObject = baseFolder.resolveFile(path);
            return fileObject.exists();
        } catch (org.apache.commons.vfs2.FileSystemException e) {
            throw new BussinessException("查询异常，连接cos异常");
        }
    }

    @Override
    public boolean remove(String path) throws BussinessException {
        try {
            FileObject fileObject = baseFolder.resolveFile(path);
            fileObject.delete();
            return true;
        } catch (org.apache.commons.vfs2.FileSystemException e) {
            throw new BussinessException("100", "删除版式文件失败");
        }
    }

    private byte[] download(String path) throws FileSystemException, org.apache.commons.vfs2.FileSystemException {
        FileObject fileObject = baseFolder.resolveFile(path);
        byte[] bytes = new byte[0];
        if (fileObject.exists()) {
            // 获取文件的类型
            String extension = fileObject.getName().getExtension();
            // 读取文本内容
            bytes = inputStreamToByteArray(fileObject.getContent().getInputStream());
        }
        return bytes;
    }

    /**
     * inputStream转byte数组
     *
     * @param inputStream 输入流对象
     * @return byte数组
     */
    public static byte[] inputStreamToByteArray(InputStream inputStream) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int num;
            while ((num = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, num);
            }
            byteArrayOutputStream.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            log.error("inputStream to byte[] 转换异常", e);
        }
        return new byte[] {};
    }

    /**
     * 获取minio文件的下载或者预览地址
     * 取决于调用本方法的方法中的PutObjectOptions对象有没有设置contentType
     *
     * @param bucketName: 桶名
     * @param fileName:   文件名
     */
    @SneakyThrows(Exception.class)
    private String getPreviewFileUrl(String bucketName, String fileName) {
        return minioClient.presignedGetObject(bucketName, fileName);
    }

}
