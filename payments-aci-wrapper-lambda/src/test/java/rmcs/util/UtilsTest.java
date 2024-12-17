package rmcs.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.finra.rmcs.constant.Constants;
import org.finra.rmcs.exception.InternalValidationException;
import org.finra.rmcs.util.Utils;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import rmcs.TestFileReaderUtil;

public class UtilsTest {

  @Test
  public void testGenerateUrl() throws IOException {
    String json = TestFileReaderUtil.getResourceContent("AchPaymentReqRequest.json");
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> request = objectMapper.readValue(json, HashMap.class);
    String expect =
        "testDomain/billeraccounts/079593e7-acf3-4a63-9a1b-cdcdc35a10d6/rmcsgatewaydev/fundingaccounts";
    String actual = Utils.generateUrl("testDomain", request);
    assertEquals(expect, actual);
  }

  @Test
  public void testValidateRequest_whenAchSuccess() throws IOException {
    String json = TestFileReaderUtil.getResourceContent("AchPaymentReqRequest.json");
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> request = objectMapper.readValue(json, HashMap.class);
    assertDoesNotThrow(() -> Utils.validateRequest(request));
  }

  @Test
  public void testValidateRequest_whenAchFail() throws IOException {
    String json = TestFileReaderUtil.getResourceContent("AchPaymentReqRequest_error.json");
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> request = objectMapper.readValue(json, HashMap.class);

    try {
      Utils.validateRequest(request);
    } catch (InternalValidationException e) {
      List<String> violations = e.getViolations();
      assertEquals(14, violations.size());
      for (String violation : violations) {
        assertTrue(violation.contains("is required."));
      }
    }
  }

  @Test
  public void testValidateRequest_whenCardSuccess() throws IOException {
    String json = TestFileReaderUtil.getResourceContent("CardPaymentReqRequest.json");
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> request = objectMapper.readValue(json, HashMap.class);
    assertDoesNotThrow(() -> Utils.validateRequest(request));
  }

  @Test
  public void testValidateRequest_whenCardFail() throws IOException {
    String json = TestFileReaderUtil.getResourceContent("CardPaymentReqRequest_error.json");
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> request = objectMapper.readValue(json, HashMap.class);

    try {
      Utils.validateRequest(request);
    } catch (InternalValidationException e) {
      List<String> violations = e.getViolations();
      assertEquals(12, violations.size());
      for (String violation : violations) {
        assertTrue(violation.contains("is required."));
      }
    }
  }

  @Test
  public void testValidateRequest_whenUnknownKind() {
    try {
      Utils.validateRequest(new HashMap<>());
    } catch (InternalValidationException e) {
      List<String> violations = e.getViolations();
      System.out.println(violations);
      assertEquals(7, violations.size());
      for (String violation : violations) {
        assertTrue(violation.contains("kind is missing or unknown."));
      }
    }
  }

  @Test
  public void testCovertAciErrorResponse_when500error() {
    HttpServerErrorException httpServerErrorException =
        new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
    Map<String, Object> actual = Utils.covertAciErrorResponse(httpServerErrorException);
    List<String> errors = (List<String>) actual.get("errors");
    assertEquals(1, errors.size());
    assertEquals(Constants.GENERAL_ERROR_MESSAGE, errors.get(0));
  }

  @Test
  public void testCovertAciErrorResponse_when422error_aciError1() throws IOException {
    String response = TestFileReaderUtil.getResourceContent("Aci422Error1.json");

    HttpClientErrorException httpClientErrorException =
        HttpClientErrorException.create(
            HttpStatus.UNPROCESSABLE_ENTITY,
            null,
            null,
            response.getBytes(StandardCharsets.UTF_8),
            null);
    Map<String, Object> actual = Utils.covertAciErrorResponse(httpClientErrorException);
    List<String> errors = (List<String>) actual.get("errors");
    assertEquals(1, errors.size());
    assertEquals("Full Name has invalid format.", errors.get(0));
  }

  @Test
  public void testCovertAciErrorResponse_when422error_aciError2() throws IOException {
    String response = TestFileReaderUtil.getResourceContent("Aci422Error2.json");

    HttpClientErrorException httpClientErrorException =
        HttpClientErrorException.create(
            HttpStatus.UNPROCESSABLE_ENTITY,
            null,
            null,
            response.getBytes(StandardCharsets.UTF_8),
            null);
    Map<String, Object> actual = Utils.covertAciErrorResponse(httpClientErrorException);
    List<String> errors = (List<String>) actual.get("errors");
    assertEquals(1, errors.size());
    assertEquals("The Routing Number you entered was not found.", errors.get(0));
  }

  @Test
  public void testCovertAciErrorResponse_when422error_aciError3() throws IOException {
    String response = TestFileReaderUtil.getResourceContent("Aci422Error3.json");

    HttpClientErrorException httpClientErrorException =
        HttpClientErrorException.create(
            HttpStatus.UNPROCESSABLE_ENTITY,
            null,
            null,
            response.getBytes(StandardCharsets.UTF_8),
            null);
    Map<String, Object> actual = Utils.covertAciErrorResponse(httpClientErrorException);
    List<String> errors = (List<String>) actual.get("errors");
    assertEquals(1, errors.size());
    assertEquals(
        "Bank account authorization declined. This routing or bank account number is invalid. Please enter different account numbers or pick a different payment method.",
        errors.get(0));
  }

  @Test
  public void testCovertAciErrorResponse_whenUnexpectedError() throws IOException {
    String response = TestFileReaderUtil.getResourceContent("AciUnexpectedError.json");

    HttpClientErrorException httpClientErrorException =
        HttpClientErrorException.create(
            HttpStatus.NOT_FOUND, null, null, response.getBytes(StandardCharsets.UTF_8), null);
    Map<String, Object> actual = Utils.covertAciErrorResponse(httpClientErrorException);
    List<String> errors = (List<String>) actual.get("errors");
    assertEquals(1, errors.size());
    assertEquals(response, errors.get(0));
  }

  @Test
  public void testCovertAciErrorResponse_when400Error() throws IOException {
    String response = TestFileReaderUtil.getResourceContent("Aci400Error.json");

    HttpClientErrorException httpClientErrorException =
        HttpClientErrorException.create(
            HttpStatus.NOT_FOUND, null, null, response.getBytes(StandardCharsets.UTF_8), null);
    Map<String, Object> actual = Utils.covertAciErrorResponse(httpClientErrorException);
    List<String> errors = (List<String>) actual.get("errors");
    assertEquals(1, errors.size());
    assertEquals("The request is malformed.", errors.get(0));
  }

  @Test
  public void testCovertAciErrorResponse_whenUnknownError() throws IOException {
    String response = TestFileReaderUtil.getResourceContent("AciUnknownError.json");

    HttpClientErrorException httpClientErrorException =
        HttpClientErrorException.create(
            HttpStatus.NOT_FOUND, null, null, response.getBytes(StandardCharsets.UTF_8), null);
    Map<String, Object> actual = Utils.covertAciErrorResponse(httpClientErrorException);
    List<String> errors = (List<String>) actual.get("errors");
    assertEquals(1, errors.size());
    assertEquals(Constants.GENERAL_ERROR_MESSAGE, errors.get(0));
  }

  @Test
  public void testGetErrorReturnMap() {
    List<String> errors = List.of("error");
    Map<String, Object> map = Utils.getErrorReturnMap(errors);
    assertEquals(1, map.size());
    assertTrue(map.containsKey("errors"));
  }
}
