package com.wetube.user.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Configuration
@ConditionalOnProperty(name = "storage.type", havingValue = "minio")
public class MinioConfig {

    @Autowired
    private Environment env;

    @Value("${minio.url}")
    private String url;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    @Value("${minio.bucket-users}")
    private String bucketName;

    @Bean
    public MinioClient minioClient() throws Exception {
        MinioClient client=MinioClient.builder()
                .endpoint(url)
                .credentials(accessKey, secretKey)
                .build();

        boolean isTest= Arrays.asList(env.getActiveProfiles()).contains("test");

        if (!isTest) {
            try {
                boolean found = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
                if (!found) {
                    client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                    System.out.println("bucket creado exitosamente: " + bucketName);
                }
                setPublicReadOnlyPolicy(client, "profiles/*");
            } catch (Exception e) {
                throw new RuntimeException("no se pudo inicialisar el bucket de minio: " + e);
            }
        }
return client;
    }

    private void setPublicReadOnlyPolicy(MinioClient client, String prefix) throws Exception{
        String policy = "{\n" +
                "  \"Version\": \"2012-10-17\",\n" +
                "  \"Statement\": [\n" +
                "    {\n" +
                "      \"Effect\": \"Allow\",\n" +
                "      \"Principal\": \"*\",\n" +
                "      \"Action\": \"s3:GetObject\",\n" +
                "      \"Resource\": \"arn:aws:s3:::" + bucketName + "/" + prefix + "\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        client.setBucketPolicy(SetBucketPolicyArgs.builder().bucket(bucketName).config(policy).build());
    }

}
