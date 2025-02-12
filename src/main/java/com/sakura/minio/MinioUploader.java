package com.sakura.minio;

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

    @Bean
    public MinioClient minioClient() throws Exception {
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioProperties.getEndpointUrl())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioProperties.getBucketName()).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioProperties.getBucketName()).build());
        } else {
            System.out.println("桶" + minioProperties.getBucketName() + "已存在");
        }
        return minioClient;
    }

    public String uploadFile(MultipartFile file) throws Exception {
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
