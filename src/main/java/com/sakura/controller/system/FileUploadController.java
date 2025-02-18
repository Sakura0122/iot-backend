package com.sakura.controller.system;

import com.sakura.common.Result;
import com.sakura.minio.MinioUploader;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: sakura
 * @date: 2024/10/12 13:31
 * @description:
 */
@RestController
@RequestMapping("/system/upload")
@Tag(name = "文件上传")
public class FileUploadController {

    @Resource
    private MinioUploader minioUploader;

    @PostMapping()
    @Schema(description = "文件上传")
    public Result<String> upload(@RequestParam("file") MultipartFile file) throws Exception {
        return Result.success(minioUploader.uploadFile(file));
    }
}
