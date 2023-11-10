package com.zhangz.springbootdemoapi.controller;

import com.zhangz.springbootdemocommon.common.Result;
import com.zhangz.springbootdemofile.dto.InitTaskParam;
import com.zhangz.springbootdemofile.dto.TaskInfoDTO;
import com.zhangz.springbootdemofile.service.MinIOService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
@Slf4j
@Api(tags = "文件服务")
public class FileController {

    @Autowired
    private MinIOService minIOService;
    
    @ApiOperation("上传文件")
    @PostMapping("/upload")
    @ResponseBody
    public String upload() throws Exception {
        String path = Paths.get("BBB.pdf").toAbsolutePath().toString();
        byte[] bytes = StreamUtils.copyToByteArray(new FileInputStream(path));
        String filePath = "test/bbb.pdf";
        minIOService.upload(bytes, filePath);
        log.info("upload success!");
        return "ok";
    }

    // ************大文件上传 ***********
    // https://blog.csdn.net/weixin_44359036/article/details/126514643?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-4-126514643-blog-131507642.235^v38^pc_relevant_sort_base1&spm=1001.2101.3001.4242.3&utm_relevant_index=7
    // 安装 node.js https://blog.csdn.net/weixin_42064877/article/details/131610918
    /**
     * 创建一个上传任务
     * @return
     */
    @ApiOperation("大文件上传,创建上传任务")
    @PostMapping("/bigFile/initTask")
    public Result<TaskInfoDTO> initTask(@Valid @RequestBody InitTaskParam param, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return Result.error(bindingResult.getFieldError().getDefaultMessage());
        }
        return Result.ok(minIOService.initTask(param));
    }

    /**
     * 获取上传进度
     * @param identifier 文件md5
     * @return
     */
    @ApiOperation("大文件上传,获取上传进度")
    @GetMapping("/bigFile/taskInfo/{identifier}")
    public Result<TaskInfoDTO> taskInfo(@PathVariable("identifier") String identifier) {
        return Result.ok(minIOService.getTaskInfo(identifier));
    }

    /**
    * 获取每个分片的预签名上传地址，直接连接minio去上传，防止大文件导致服务器压力过大
    * @param identifier 
    * @param partNumber 分片
    * @return
    */
    @ApiOperation("大文件上传,获取分片上传url")
    @GetMapping("/bigFile/preSignUploadUrl/{identifier}/{partNumber}")
    public Result preSignUploadUrl(@PathVariable("identifier") String identifier, @PathVariable("partNumber") Integer partNumber) {
        TaskInfoDTO task = minIOService.getByIdentifier(identifier);
        if (task == null) {
            return Result.error("分片任务不存在");
        }
        Map<String, String> params = new HashMap<>();
        params.put("partNumber", partNumber.toString());
        params.put("uploadId", task.getUploadId());
        return Result.ok(minIOService.genPreSignUploadUrl(task.getBucketName(), task.getObjectKey(), params));
    }

    /**
     * 合并分片
     * @param identifier
     * @return
     */
    @ApiOperation("大文件上传,合并分片")
    @PostMapping("/bigFile/merge/{identifier}")
    public Result merge(@PathVariable("identifier") String identifier) {
        minIOService.merge(identifier);
        return Result.ok();
    }

}
