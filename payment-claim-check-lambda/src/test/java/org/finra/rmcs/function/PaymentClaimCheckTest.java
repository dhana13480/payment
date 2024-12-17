package org.finra.rmcs.function;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.finra.rmcs.TestUtils;
import org.finra.rmcs.service.S3Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
public class PaymentClaimCheckTest {
  static Map<String, Object> request;

  @Mock
  S3Service s3Service;

  @Spy
  ObjectMapper objectMapper = TestUtils.getObjectMapper();

  @InjectMocks
  PaymentClaimCheck pmtClaimCkFunction;

  static Map<String, Object> lambdaRequestMap = null;
  static String pmtClaimCkRespJsonString = null;

  @BeforeEach
  public void initReceivables() throws Exception {
    request = TestUtils.readJsonFileToMap("PaymentClaimCheckRequest.json");
    pmtClaimCkRespJsonString = TestUtils.readFileAsString("PaymentClaimCheckResponse.json");
  }

  @BeforeEach
  public void init(){
    ReflectionTestUtils.setField(pmtClaimCkFunction, "enableDecodeToken", false);
  }

  @Test
  public void apply_success() throws Exception {
    when(s3Service.retrieveS3Object(any(), any())).thenReturn(pmtClaimCkRespJsonString);
    ResponseEntity<Object> response = pmtClaimCkFunction.apply(request);
    Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
  }

  @Test
  public void apply_nosuchkey_exception_failed() throws Exception {
    when(s3Service.retrieveS3Object(any(), any())).thenThrow(NoSuchKeyException.class);
    ResponseEntity<Object> response = pmtClaimCkFunction.apply(request);
    Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
  }

  @Test
  public void apply_other_exception_failed() throws Exception {
    when(s3Service.retrieveS3Object(any(), any())).thenThrow(NullPointerException.class);
    ResponseEntity<Object> response = pmtClaimCkFunction.apply(request);
    Assertions.assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  public void apply_validation_failed_no_applicationName() throws Exception {
    Map<String, Object> request =
        TestUtils.readJsonFileToMap("PaymentClaimCheckRequest_no_application_name.json");
    ResponseEntity<Object> response = pmtClaimCkFunction.apply(request);
    Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
  }

  @Test
  public void apply_validation_failed_no_claimCheckId() throws Exception {
    Map<String, Object> request =
        TestUtils.readJsonFileToMap("PaymentClaimCheckRequest_no_claim_check_id.json");
    ResponseEntity<Object> response = pmtClaimCkFunction.apply(request);
    Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
  }

  // token_id check failed if JWT enabled
}
