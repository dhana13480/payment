package rmcs.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.finra.rmcs.exception.InternalValidationException;
import org.finra.rmcs.service.FundingTokenServiceImpl;
import org.finra.rmcs.service.GetAciTokenServiceImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import rmcs.TestFileReaderUtil;

@SpringJUnitConfig
public class FundingTokenServiceTest {
  @Spy @InjectMocks private FundingTokenServiceImpl fundingTokenService;

  @Mock private GetAciTokenServiceImpl getAciTokenService;

  @Mock private RestTemplate restTemplate;

  @Test
  public void testGenerateUrl_whenSuccess() throws IOException {
    String json = TestFileReaderUtil.getResourceContent("AchPaymentReqRequest.json");
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> request = objectMapper.readValue(json, HashMap.class);
    Map<String, Object> expected = Map.of("testKey", "testValue");
    Mockito.when(getAciTokenService.getAciToken(anyString())).thenReturn("testToken");
    Mockito.when(
            restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
        .thenReturn(new ResponseEntity<>(expected, HttpStatus.OK));
    ResponseEntity<Map<String, Object>> response =
        fundingTokenService.getFundingToken(StringUtils.EMPTY, request);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(expected, response.getBody());
  }

  @Test
  public void testGenerateUrl_whenNoToken() throws IOException {
    String json = TestFileReaderUtil.getResourceContent("AchPaymentReqRequest.json");
    String exception =
        "There is no token in payments.aci_nonsdk_token table, it should not happen!";
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> request = objectMapper.readValue(json, HashMap.class);
    Mockito.when(getAciTokenService.getAciToken(anyString()))
        .thenThrow(new InternalValidationException(List.of(exception)));
    Assertions.assertThrows(
        InternalValidationException.class,
        () -> fundingTokenService.getFundingToken(StringUtils.EMPTY, request),
        exception);
  }

  @Test
  public void testDryRun_success() {
    Mockito.doReturn(new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY))
        .when(fundingTokenService)
        .getFundingToken(anyString(), anyMap());
    Assertions.assertDoesNotThrow(() -> fundingTokenService.dryRun(StringUtils.EMPTY));
  }

  @Test
  public void testDryRun_other400error_fail() {
    Mockito.doThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN))
        .when(fundingTokenService)
        .getFundingToken(anyString(), anyMap());
    Assertions.assertThrows(
        RuntimeException.class, () -> fundingTokenService.dryRun(StringUtils.EMPTY));
  }

  @Test
  public void testDryRun_500error_fail() {
    Mockito.doThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR))
        .when(fundingTokenService)
        .getFundingToken(anyString(), anyMap());
    Assertions.assertThrows(
        RuntimeException.class, () -> fundingTokenService.dryRun(StringUtils.EMPTY));
  }
}
