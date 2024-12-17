package rmcs.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.finra.rmcs.service.FetchCountryServiceImpl;
import org.finra.rmcs.service.GetAciTokenServiceImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@SpringJUnitConfig
public class FetchCountryServiceTest {
  @Spy @InjectMocks private FetchCountryServiceImpl fetchCountryService;

  @Mock private GetAciTokenServiceImpl getAciTokenService;

  @Mock private RestTemplate restTemplate;

  @BeforeEach
  public void init() {
    ReflectionTestUtils.setField(fetchCountryService, "countryUrl", "test");
  }

  @Test
  public void testGetCountries_whenSuccess() {
    Map<String, Object> expected = Map.of("testKey", "testValue");
    Mockito.when(getAciTokenService.getAciToken(anyString())).thenReturn("testToken");
    Mockito.when(
            restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
        .thenReturn(new ResponseEntity<>(expected, HttpStatus.OK));
    ResponseEntity<Map<String, Object>> response =
        fetchCountryService.getCountries(StringUtils.EMPTY);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(expected, response.getBody());
  }

  @Test
  public void testGetCountries_whenFail() {
    Map<String, Object> expected = Map.of("testKey", "testValue");
    Mockito.when(getAciTokenService.getAciToken(anyString())).thenReturn("testToken");
    Mockito.when(
            restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
        .thenReturn(new ResponseEntity<>(expected, HttpStatus.OK));
    ResponseEntity<Map<String, Object>> response =
        fetchCountryService.getCountries(StringUtils.EMPTY);
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(expected, response.getBody());
  }

  @Test
  public void testGetCountryDetails_whenSuccess() {
    Map<String, Object> expected = Map.of("testKey", "testValue");
    Mockito.when(getAciTokenService.getAciToken(anyString())).thenReturn("testToken");
    Mockito.when(
            restTemplate.exchange(anyString(), any(), any(), any(ParameterizedTypeReference.class)))
        .thenReturn(new ResponseEntity<>(expected, HttpStatus.OK));
    ResponseEntity<Map<String, Object>> response =
        fetchCountryService.getCountryDetails(StringUtils.EMPTY, "testCountry");
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    Assertions.assertEquals(expected, response.getBody());
  }

  @Test
  public void testDryRun_whenSuccess() {
    Mockito.doReturn(new ResponseEntity<>(HttpStatus.OK))
        .when(fetchCountryService)
        .getCountries(anyString());
    Mockito.doReturn(new ResponseEntity<>(HttpStatus.OK))
        .when(fetchCountryService)
        .getCountryDetails(anyString(), anyString());
    Assertions.assertDoesNotThrow(() -> fetchCountryService.dryRun(StringUtils.EMPTY));
  }
}
