package org.finra.rmcs.service;

import java.io.IOException;

public interface S3Service {
  String retrieveS3Object(String s3Bucket, String key) throws IOException;
}
