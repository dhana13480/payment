package rmcs.function;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.finra.rmcs.constant.Constants;
import org.finra.rmcs.function.PaymentsAciWrapperFunction;
import org.finra.rmcs.service.FetchCountryService;
import org.finra.rmcs.service.FundingTokenService;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import rmcs.TestFileReaderUtil;

@SpringJUnitConfig
public class PaymentsAciWrapperFunctionTest {

  @InjectMocks private PaymentsAciWrapperFunction paymentsAciWrapperFunction;

  @Mock private FundingTokenService fundingTokenService;

  @Mock private FetchCountryService fetchCountryService;

  @Test
  public void testPaymentsAciWrapperFunction_dryRun() {
    Map<String, Object> request = new HashMap<>();
    request.put(Constants.DRY_RUN, Constants.TRUE);
    Mockito.doNothing().when(fundingTokenService).dryRun(anyString());
    ResponseEntity<Map<String, Object>> response = paymentsAciWrapperFunction.apply(request);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(
        Constants.DRY_RUN_PROCESSED_SUCCESSFULLY,
        response.getBody().get(Constants.MESSAGE).toString());
  }

  @Test
  public void testPaymentsAciWrapperFunction_dryRun_dbError_fail() {
    Map<String, Object> request = new HashMap<>();
    request.put(Constants.DRY_RUN, Constants.TRUE);
    Mockito.doThrow(new RuntimeException()).when(fundingTokenService).dryRun(anyString());
    ResponseEntity<Map<String, Object>> response = paymentsAciWrapperFunction.apply(request);
    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void testPaymentsAciWrapperFunction_dryRun_aciError_fail() {
    Map<String, Object> request = new HashMap<>();
    request.put(Constants.DRY_RUN, Constants.TRUE);
    Mockito.doThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR))
        .when(fundingTokenService)
        .dryRun(anyString());
    ResponseEntity<Map<String, Object>> response = paymentsAciWrapperFunction.apply(request);
    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void testPaymentsAciWrapperFunction_success() throws IOException {
    String json = TestFileReaderUtil.getResourceContent("ApiGatewayAchPaymentReqRequest.json");
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> request = objectMapper.readValue(json, HashMap.class);
    Mockito.when(fundingTokenService.getFundingToken(anyString(), anyMap()))
        .thenReturn(new ResponseEntity<>(HttpStatus.OK));
    ResponseEntity<Map<String, Object>> response = paymentsAciWrapperFunction.apply(request);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testPaymentsAciWrapperFunction_internalValidationFail() throws IOException {
    String json = TestFileReaderUtil.getResourceContent("AchPaymentReqRequest_error.json");
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> request = objectMapper.readValue(json, HashMap.class);
    ResponseEntity<Map<String, Object>> response = paymentsAciWrapperFunction.apply(request);
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void testPaymentsAciWrapperFunction_aciValidationFail() throws IOException {
    String json = TestFileReaderUtil.getResourceContent("AchPaymentReqRequest.json");
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> request = objectMapper.readValue(json, HashMap.class);
    Mockito.doThrow(new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY))
        .when(fundingTokenService)
        .getFundingToken(anyString(), anyMap());
    ResponseEntity<Map<String, Object>> response = paymentsAciWrapperFunction.apply(request);
    Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
  }

  @Test
  public void testPaymentsAciWrapperFunction_getListOfCountry_success() throws IOException {
    String json = TestFileReaderUtil.getResourceContent("GetListOfCountryRequest.json");
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> request = objectMapper.readValue(json, HashMap.class);
    Mockito.when(fetchCountryService.getCountries(anyString()))
        .thenReturn(new ResponseEntity<>(HttpStatus.OK));
    ResponseEntity<Map<String, Object>> response = paymentsAciWrapperFunction.apply(request);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testPaymentsAciWrapperFunction_getListOfCountry_fail() throws IOException {
    String json = TestFileReaderUtil.getResourceContent("GetListOfCountryRequest.json");
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> request = objectMapper.readValue(json, HashMap.class);
    Mockito.doThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR))
        .when(fetchCountryService)
        .getCountries(anyString());
    ResponseEntity<Map<String, Object>> response = paymentsAciWrapperFunction.apply(request);
    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void testPaymentsAciWrapperFunction_getCountryDetails_success() throws IOException {
    String json = TestFileReaderUtil.getResourceContent("GetCountryDetailsRequest.json");
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> request = objectMapper.readValue(json, HashMap.class);
    Mockito.when(fetchCountryService.getCountryDetails(anyString(), anyString()))
        .thenReturn(new ResponseEntity<>(HttpStatus.OK));
    ResponseEntity<Map<String, Object>> response = paymentsAciWrapperFunction.apply(request);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testPaymentsAciWrapperFunction_getCountryDetails_fail() throws IOException {
    String json = TestFileReaderUtil.getResourceContent("GetCountryDetailsRequest.json");
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> request = objectMapper.readValue(json, HashMap.class);
    Mockito.when(fetchCountryService.getCountryDetails(anyString(), anyString()))
        .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    ResponseEntity<Map<String, Object>> response = paymentsAciWrapperFunction.apply(request);
    Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void testPaymentsAciWrapperFunction_getCountryDetails_dryRun() throws IOException {
    String json = TestFileReaderUtil.getResourceContent("GetCountryDetailsRequestDryRun.json");
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> request = objectMapper.readValue(json, HashMap.class);
    Mockito.doThrow(new RuntimeException("exception"))
        .when(fetchCountryService)
        .dryRun(anyString());
    ResponseEntity<Map<String, Object>> response = paymentsAciWrapperFunction.apply(request);
    System.out.println(response.getBody());
    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    Assertions.assertTrue(
        response
            .getBody()
            .get(Constants.MESSAGE)
            .toString()
            .contains("java.lang.RuntimeException: exception"));
  }
}
