package org.finra.rmcs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

  @Bean(name = "s3Client")
  public S3Client getS3Client() {
    return S3Client.builder().region(Region.US_EAST_1)
        .credentialsProvider(EnvironmentVariableCredentialsProvider.create()).build();
  }
}
