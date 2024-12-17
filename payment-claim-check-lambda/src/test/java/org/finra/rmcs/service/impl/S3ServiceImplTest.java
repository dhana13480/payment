package org.finra.rmcs.service.impl;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.utils.StringInputStream;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
public class S3ServiceImplTest {

  @Mock
  private S3Client s3Client;

  @InjectMocks
  private S3ServiceImpl s3Service;

  @SuppressWarnings("unchecked")
  @Test
  public void testReadStringFromS3() throws IOException {
    String expectedContents = "{\"test\" : \"val\"}";
    InputStream testInputStream = new StringInputStream(expectedContents);
    ResponseBytes<GetObjectResponse> getObjectResponseResponseBytes =
        ResponseBytes.fromInputStream(GetObjectResponse.builder().build(), testInputStream);
    when(s3Client.getObject(any(GetObjectRequest.class), any(ResponseTransformer.class)))
        .thenReturn(getObjectResponseResponseBytes);
    assertEquals(expectedContents,
        s3Service.retrieveS3Object("mockBucket", "MQPBI/420ff8b0-b29a-4f10-8d54-dc0c76a9c53e"));
  }

}
