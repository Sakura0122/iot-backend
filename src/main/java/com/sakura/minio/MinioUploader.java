package com.sakura.minio;

import com.sakura.common.ResultCodeEnum;
import com.sakura.exception.SakuraException;
import io.minio.*;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author: sakura
 * @date: 2024/10/23 16:39
 * @description:
 */
@Configuration
public class MinioUploader {

    @Resource
    private MinioProperties minioProperties;
    @Resource
    private MinioClient minioClient;

    public String uploadFile(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new SakuraException(ResultCodeEnum.NONE_FILE);
        }
        String prefix = UUID.randomUUID().toString().replaceAll("-", "");
        String fileName = prefix + file.getOriginalFilename();

        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minioProperties.getBucketName())
                .object(fileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build());

        return minioProperties.getEndpointUrl() + "/" + minioProperties.getBucketName() + "/" + fileName;
    }
}
