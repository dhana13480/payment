package org.finra.rmcs.function;

import com.amazonaws.services.securitytoken.model.ExpiredTokenException;
import com.auth0.jwk.GuavaCachedJwkProvider;
import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.finra.rmcs.constants.Constants;
import org.finra.rmcs.dto.EsmpNotificationRequest;
import org.finra.rmcs.dto.PaymentClaimCheckRequest;
import org.finra.rmcs.dto.PaymentClaimCheckResponse;
import org.finra.rmcs.service.S3Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentClaimCheck implements Function<Map<String, Object>, ResponseEntity<Object>> {
  private final S3Service s3Service;
  private final ObjectMapper objectMapper;

  @Value("${esmp.notification.s3.bucket}")
  private String s3Bucket;

  @Value("${esmp.notification.s3.keyPrefix}")
  private String keyPrefix;

  @Value("${fip.token.enable}")
  private boolean enableDecodeToken;

  @Override
  public ResponseEntity<Object> apply(Map<String, Object> reqMap) {
    EsmpNotificationRequest esmpNtfyReqObj = null;
    String jwtToken = null;
    String correlationId = UUID.randomUUID().toString();
    String methodName =
        Constants.CLASS
            + this.getClass().getSimpleName()
            + " "
            + Constants.METHOD
            + Thread.currentThread().getStackTrace()[1].getMethodName()
            + " "
            + Constants.CORRELATION_ID
            + correlationId;
    log.info("{} message: method entry", methodName);
    try {

    // Parse request
    PaymentClaimCheckRequest pmtClaimCkReq = objectMapper
        .readValue(objectMapper.writeValueAsString(reqMap), PaymentClaimCheckRequest.class);
    if(Constants.HEALTHCHECK.equalsIgnoreCase(pmtClaimCkReq.getParams().getPath().getApplicationName())){
      log.info("{} message, dryRun mode", methodName);
      return new ResponseEntity<>(PaymentClaimCheckResponse.builder()
            .message(Constants.DRY_RUN_PROCESSED_SUCCESSFULLY).build(), HttpStatus.OK);
    }

      // Verfify JWT token, if enabled
      if (reqMap.get(Constants.CONTEXT) != null) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString =
            gson.toJson(reqMap.get(Constants.CONTEXT))
                .replaceAll(Constants.REF_EXP_N, "\n")
                .replaceAll(Constants.REF_EXP_T, "\t")
                .replaceAll(Constants.REF_EXP_B, "\b")
                .replaceAll(Constants.REF_EXP_R, "\r")
                .replaceAll(Constants.REF_EXP_F, "\f")
                .replaceAll(Constants.REF_EXP_Q, "'")
                .replaceAll(Constants.REF_EXP_D, "");
        jsonString = jsonString.replaceAll(Constants.REF_EXP_S, "");
        HashMap context = gson.fromJson(jsonString, HashMap.class);
        jwtToken = (context).get(Constants.ID_TOKEN).toString();
      }
      // Decoding and validating id token
      if (enableDecodeToken) {
        decodeAndValidateToken(jwtToken, correlationId);
      }

      // Validate request
      PaymentClaimCheckResponse validationResp = validateRequest(pmtClaimCkReq);
      if (validationResp != null) {
        return new ResponseEntity<>(validationResp, HttpStatus.BAD_REQUEST);
      }


      StringBuffer s3KeyBuf = new StringBuffer();
      s3KeyBuf.append(keyPrefix).append(pmtClaimCkReq.getParams().getPath().getApplicationName())
          .append("/").append(pmtClaimCkReq.getParams().getPath().getClaimCheckId());

      // Read payment claim from S3
      String pmtClaimFileJson = s3Service.retrieveS3Object(s3Bucket, s3KeyBuf.toString());
      esmpNtfyReqObj = objectMapper.readValue(pmtClaimFileJson, EsmpNotificationRequest.class);
    } catch (NoSuchKeyException ne) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND.name(), HttpStatus.NOT_FOUND);
    } catch (ExpiredTokenException | SecurityException e) {
      String errorMsg = e instanceof SecurityException?"{} message: Unauthorized {} ":"{} message: JWT token expired {} ";
      String responseMsg = e instanceof SecurityException? "Unauthorized": "JWT token expired ";
      log.error(errorMsg, methodName, e.getMessage());
      PaymentClaimCheckResponse paymentReqResponse =
          PaymentClaimCheckResponse.builder().errors(Collections.emptyList())
              .message(responseMsg).build();
      return new ResponseEntity<>(paymentReqResponse, HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
      e.printStackTrace();
      log.error("Payment claim check failed, error: {}", e);
      PaymentClaimCheckResponse paymentReqResponse =
          PaymentClaimCheckResponse.builder().errors(Collections.singletonList(e.getMessage()))
              .message("Payment claim check request failed").build();
      return new ResponseEntity<>(paymentReqResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>(esmpNtfyReqObj, HttpStatus.OK);
  }

  private PaymentClaimCheckResponse validateRequest(PaymentClaimCheckRequest pmtClaimCkReq) {
    List<String> errMsgs = new ArrayList<>();
    if (pmtClaimCkReq.getParams() == null || pmtClaimCkReq.getParams().getPath() == null
        || StringUtils.isBlank(pmtClaimCkReq.getParams().getPath().getApplicationName())) {
      errMsgs.add("Application name is required");
    }

    if (pmtClaimCkReq.getParams() == null || pmtClaimCkReq.getParams().getPath() == null
        || StringUtils.isBlank(pmtClaimCkReq.getParams().getPath().getClaimCheckId())) {
      errMsgs.add("Claim check id is required");
    }

    // Validate id_token if JWT token enabled

    if (!CollectionUtils.isEmpty(errMsgs)) {
      return PaymentClaimCheckResponse.builder().errors(errMsgs)
          .message("Payment claim check request validation failed").build();
    } else
      return null;
  }

  @SneakyThrows
  private void decodeAndValidateToken(
      String jwtToken, String correlationId) {
    String methodName =
        Constants.CLASS
            + this.getClass().getSimpleName()
            + " "
            + Constants.METHOD
            + Thread.currentThread().getStackTrace()[1].getMethodName()
            + " "
            + Constants.CORRELATION_ID
            + correlationId;
    log.info("{} message: method entry", methodName);
    String tokenJwkProviderUrl = System.getenv(Constants.FIP_JWKS_ENDPOINT);
    log.info("The the token string is {}", jwtToken);

    URL url = new URL(tokenJwkProviderUrl);
    DecodedJWT decodedJwt = JWT.decode(jwtToken);
    JwkProvider provider =
        new GuavaCachedJwkProvider(new UrlJwkProvider(url), 10, 10, TimeUnit.MINUTES);
    String tokenKey = decodedJwt.getKeyId();
    log.info("{} message: KeyId from JWT  {}", methodName, tokenKey);
    Jwk jwk = provider.get(tokenKey);
    Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
    Verification verifier = JWT.require(algorithm);
    verifier.build().verify(decodedJwt);
    String tokenPayload = decodedJwt.getPayload();
    String decodedPayload = new String(Base64.getUrlDecoder().decode(tokenPayload));
    log.info("{} message: The the decodedPayload string is {}", methodName, decodedPayload);
    String env = System.getenv(Constants.SPRING_PROFILES_ACTIVE);
    log.info("{} message: The env is {}", methodName, env);
    // Check expiration
    if (decodedJwt.getExpiresAt().before(Calendar.getInstance().getTime())) {
      log.info("{} message: Token is expired", methodName);
      throw new ExpiredTokenException(Constants.EXPIRED_TOKEN_MSG);
    }
    // on DEV we bypass checking of current user belonging to APP_APP_RMCS_MESSAGING_READ_D
    if (!isAuthorized(env, decodedPayload)) {
      throw new SecurityException("Principal is not a member of group APP_APP_RMCS_MESSAGING_READ_<ENV>");
    }
  }

  public boolean isAuthorized(String env, String payload) {
    return env != null
        && (env.contains("dev")
        || payload.contains(
        "CN=APP_RMCS_MESSAGING_READ" + (env.equalsIgnoreCase("prod") ? "_P" : "_Q")));
  }

}
