package rmcs.service;

import static org.mockito.ArgumentMatchers.anyString;

import org.apache.commons.lang3.StringUtils;
import org.finra.rmcs.constant.Constants;
import org.finra.rmcs.dto.AciTokenResponse;
import org.finra.rmcs.exception.InternalValidationException;
import org.finra.rmcs.service.GetAciTokenServiceImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@SpringJUnitConfig
public class GetAciTokenServiceTest {

  @InjectMocks @Spy private GetAciTokenServiceImpl getAciTokenService;

  @Mock private RestTemplate restTemplate;

  @BeforeEach
  public void init() {
    ReflectionTestUtils.setField(getAciTokenService, "aciTokenUrl", "testUrl");
    ReflectionTestUtils.setField(getAciTokenService, "authKey", "testAuthKey");
    ReflectionTestUtils.setField(getAciTokenService, "clientId", "client_id");
    ReflectionTestUtils.setField(getAciTokenService, "clientSecret", "client_secret");
    ReflectionTestUtils.setField(getAciTokenService, "grantType", "grant_type");
  }

  @Test
  public void testGetAciToken_success() {
    String token = "testToken";
    AciTokenResponse aciTokenResponse = new AciTokenResponse();
    aciTokenResponse.setAccessToken(token);
    String tokenBody =
        String.format(
            "client_id=%s&client_secret=%s&grant_type=%s",
            "client_id", "client_secret", "grant_type");
    HttpHeaders headers = new HttpHeaders();
    headers.add(Constants.X_AUTH_KEY, "testAuthKey");
    headers.add(HttpHeaders.CONTENT_TYPE, Constants.ACI_TOKEN_CONTENT_TYPE);
    HttpEntity<String> httpEntity = new HttpEntity<>(tokenBody, headers);
    Mockito.when(
            restTemplate.exchange("testUrl", HttpMethod.POST, httpEntity, AciTokenResponse.class))
        .thenReturn(new ResponseEntity<>(aciTokenResponse, HttpStatus.OK));
    Assertions.assertEquals(token, getAciTokenService.getAciToken(StringUtils.EMPTY));
  }

  @Test
  public void testGetAciToken_fail() {
    String token = "";
    AciTokenResponse aciTokenResponse = new AciTokenResponse();
    aciTokenResponse.setAccessToken(token);
    String tokenBody =
        String.format(
            "client_id=%s&client_secret=%s&grant_type=%s",
            "client_id", "client_secret", "grant_type");
    HttpHeaders headers = new HttpHeaders();
    headers.add(Constants.X_AUTH_KEY, "testAuthKey");
    headers.add(HttpHeaders.CONTENT_TYPE, Constants.ACI_TOKEN_CONTENT_TYPE);
    HttpEntity<String> httpEntity = new HttpEntity<>(tokenBody, headers);
    Mockito.when(
            restTemplate.exchange("testUrl", HttpMethod.POST, httpEntity, AciTokenResponse.class))
        .thenReturn(new ResponseEntity<>(aciTokenResponse, HttpStatus.OK));

    Assertions.assertThrows(
        InternalValidationException.class,
        () -> getAciTokenService.getAciToken(StringUtils.EMPTY),
        "Failed to get OAuth2 token from ACI testUrl");
  }

  @Test
  public void testDryRun_success() {
    String token = "testToken";
    AciTokenResponse aciTokenResponse = new AciTokenResponse();
    aciTokenResponse.setAccessToken(token);
    String tokenBody =
        String.format(
            "client_id=%s&client_secret=%s&grant_type=%s",
            "client_id", "client_secret", "grant_type");
    HttpHeaders headers = new HttpHeaders();
    headers.add(Constants.X_AUTH_KEY, "testAuthKey");
    headers.add(HttpHeaders.CONTENT_TYPE, Constants.ACI_TOKEN_CONTENT_TYPE);
    HttpEntity<String> httpEntity = new HttpEntity<>(tokenBody, headers);
    Mockito.when(
            restTemplate.exchange("testUrl", HttpMethod.POST, httpEntity, AciTokenResponse.class))
        .thenReturn(new ResponseEntity<>(aciTokenResponse, HttpStatus.OK));
    Assertions.assertDoesNotThrow(() -> getAciTokenService.dryRun(StringUtils.EMPTY));
  }

  @Test
  public void testDryRun_fail() {
    String exception = "testException";
    Mockito.doThrow(new InternalValidationException(exception))
        .when(getAciTokenService)
        .getAciToken(anyString());
    Assertions.assertThrows(
        InternalValidationException.class,
        () -> getAciTokenService.dryRun(StringUtils.EMPTY),
        exception);
  }
}
