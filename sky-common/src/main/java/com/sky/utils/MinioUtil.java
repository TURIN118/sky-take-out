package com.sky.utils;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component
@Slf4j
public class MinioUtil {

    @Value("${sky.alioss.endpoint}")
    private String endpoint;
    @Value("${sky.alioss.access-key-id}")
    private String accessKey;
    @Value("${sky.alioss.access-key-secret}")
    private String secretKey;
    @Value("${sky.alioss.bucket-name}")
    private String bucketName;

    private MinioClient minioClient;

    @PostConstruct
    public void init() {
        try {
            minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();

            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("创建桶: {}", bucketName);
            }
        } catch (Exception e) {
            log.error("MinIO 初始化失败", e);
            throw new RuntimeException("MinIO 初始化异常");
        }
    }

    public String uploadFile(MultipartFile file, String dir) throws Exception {
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID() + suffix;
        String objectName = dir + fileName;

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        return endpoint + "/" + bucketName + "/" + objectName;
    }
}

