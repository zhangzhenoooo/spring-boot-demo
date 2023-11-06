package com.zhangz.springbootdemofile.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.excel.util.DateUtils;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.zhangz.springbootdemocommon.exception.BussinessException;
import com.zhangz.springbootdemofile.config.FileConfig;
import com.zhangz.springbootdemofile.config.MimeTypeEnum;
import com.zhangz.springbootdemofile.dto.InitTaskParam;
import com.zhangz.springbootdemofile.dto.TaskInfoDTO;
import com.zhangz.springbootdemofile.dto.TaskRecordDTO;
import com.zhangz.springbootdemofile.properties.MinioProperties;
import com.zhangz.springbootdemofile.service.MinIOService;
import com.zhangz.springbootdemosql.entity.demo.UploadTask;
import com.zhangz.springbootdemosql.service.demo.UploadTaskService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.vfs2.FileObject;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileSystemException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@Service(interfaceClass = MinIOService.class, timeout = 1000)
public class MinIOServiceImpl implements MinIOService {
    @Resource
    private FileObject baseFolder;

    @Resource
    private MinioClient minioClient;

    @Resource
    private FileConfig fileConfig;

    @Resource
    private UploadTaskService uploadTaskService;

    @Resource
    private MinioProperties minioProperties;
    @Resource
    private AmazonS3 amazonS3;

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

    @Override
    public TaskInfoDTO initTask(InitTaskParam param) {
        Date currentDate = new Date();
        String bucketName = minioProperties.getBucket();
        String fileName = param.getFileName();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String key = StrUtil.format("{}/{}.{}", DateUtils.format(currentDate, "YYYY-MM-dd"), UUID.randomUUID(), suffix);
        String contentType = MediaTypeFactory.getMediaType(key).orElse(MediaType.APPLICATION_OCTET_STREAM).toString();
        // 可在 ObjectMetadata 中设置加密方式等
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);

        // 大对象分块上传
        InitiateMultipartUploadResult initiateMultipartUploadResult =
            amazonS3.initiateMultipartUpload(new InitiateMultipartUploadRequest(bucketName, key).withObjectMetadata(objectMetadata));
        String uploadId = initiateMultipartUploadResult.getUploadId();

        UploadTask task = new UploadTask();
        int chunkNum = (int)Math.ceil(param.getTotalSize() * 1.0 / param.getChunkSize());
        task.setBucketName(minioProperties.getBucket()).setChunkNum(chunkNum).setChunkSize(param.getChunkSize()).setTotalSize(param.getTotalSize())
            .setFileIdentifier(param.getIdentifier()).setFileName(fileName).setObjectKey(key).setUploadId(uploadId);
        uploadTaskService.save(task);
        return new TaskInfoDTO().setFinished(false).setTaskRecord(TaskRecordDTO.convertFromEntity(task)).setPath(getPath(bucketName, key));
    }

    @Override
    public TaskInfoDTO getTaskInfo(String identifier) {
        UploadTask task = uploadTaskService.selectByIdentifier(identifier);

        if (task == null) {
            return null;
        }

        TaskInfoDTO result = new TaskInfoDTO().setFinished(true).setTaskRecord(TaskRecordDTO.convertFromEntity(task)).setPath(getPath(task.getBucketName(), task.getObjectKey()));

        boolean doesObjectExist = amazonS3.doesObjectExist(task.getBucketName(), task.getObjectKey());
        if (!doesObjectExist) {
            // 未上传完，返回已上传的分片
            ListPartsRequest listPartsRequest = new ListPartsRequest(task.getBucketName(), task.getObjectKey(), task.getUploadId());
            PartListing partListing = amazonS3.listParts(listPartsRequest);
            result.setFinished(false).getTaskRecord().setExitPartList(partListing.getParts());
        }
        return result;
    }

    @Override
    public TaskInfoDTO getByIdentifier(String identifier) {
        UploadTask uploadTask = uploadTaskService.selectByIdentifier(identifier);
        return BeanUtil.copyProperties(uploadTask, TaskInfoDTO.class);
    }

    @Override
    public String genPreSignUploadUrl(String bucketName, String objectKey, Map<String, String> params) {
        Date currentDate = new Date();
        Date expireDate = DateUtil.offsetMillisecond(currentDate, (int)(60 * 10 * 1000L)); // 过期时间

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(minioProperties.getBucket(), objectKey).withExpiration(expireDate).withMethod(HttpMethod.PUT);
        if (params != null) {
            params.forEach((key, val) -> request.addRequestParameter(key, val));
        }
        URL preSignedUrl = amazonS3.generatePresignedUrl(request);
        return preSignedUrl.toString();
    }

    @Override
    public void merge(String identifier) {
        UploadTask task = uploadTaskService.selectByIdentifier(identifier);
        if (task == null) {
            throw new RuntimeException("分片任务不存");
        }

        ListPartsRequest listPartsRequest = new ListPartsRequest(task.getBucketName(), task.getObjectKey(), task.getUploadId());
        PartListing partListing = amazonS3.listParts(listPartsRequest);
        List<PartSummary> parts = partListing.getParts();
        if (task.getChunkNum() != parts.size()) {
            // 已上传分块数量与记录中的数量不对应，不能合并分块
            throw new RuntimeException("分片缺失，请重新上传");
        }

        CompleteMultipartUploadRequest completeMultipartUploadRequest =
            new CompleteMultipartUploadRequest().withUploadId(task.getUploadId()).withKey(task.getObjectKey()).withBucketName(task.getBucketName())
                .withPartETags(parts.stream().map(partSummary -> new PartETag(partSummary.getPartNumber(), partSummary.getETag())).collect(Collectors.toList()));
        CompleteMultipartUploadResult result = amazonS3.completeMultipartUpload(completeMultipartUploadRequest);

    }

    public String getPath(String bucket, String objectKey) {
        return StrUtil.format("{}/{}/{}", minioProperties.getEndpoint(), bucket, objectKey);
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
