package com.cocus.codechallenge.repositories.impl.s3.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "aws")
public class S3Properties {

    private String endpoint;

    private String region;

    private String accessKeyId;

    private String secretAccessKey;

    private String bucketName;

}
