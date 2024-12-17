package org.finra.rmcs.service.impl;

import org.finra.rmcs.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

@Slf4j
@Service
public class S3ServiceImpl implements S3Service {
  private final S3Client s3Client;

  @Autowired
  public S3ServiceImpl(S3Client s3Client) {
    this.s3Client = s3Client;
  }

  // Read whole file as one string
  public String retrieveS3Object(String bucket, String key) {
    log.info("Getting S3 Object with bucket {} and key: {}", bucket, key);
    GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucket).key(key).build();
    return s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes()).asUtf8String();
  }

}
