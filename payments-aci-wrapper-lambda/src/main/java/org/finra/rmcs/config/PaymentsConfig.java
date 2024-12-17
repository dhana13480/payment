package org.finra.rmcs.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.finra.rmcs.constant.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
@ComponentScan(basePackages = Constants.ORG_FINRA)
public class PaymentsConfig {

  @SneakyThrows
  public static void createCustomTrustStore() {
    // locate the default truststore
    log.info("createCustomTrustStore");
    String filename =
        System.getProperty("java.home") + "/lib/security/cacerts".replace('/', File.separatorChar);
    log.info("filename:{}", filename);
    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

    try (FileInputStream fis = new FileInputStream(filename)) {
      keyStore.load(fis, Constants.CERT_PW.toCharArray());
    }

    URL res = PaymentsConfig.class.getClassLoader().getResource("FINRACorpRootCA_cert.crt");
    // add Certificate to Key store
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    Certificate caCert = cf.generateCertificate(new FileInputStream(res.toURI().getPath()));
    keyStore.setCertificateEntry("ca-cert", caCert);

    // can only save to /tmp from a lambda
    String certPath =
        System.getProperty("java.io.tmpdir") + File.separatorChar + "CustomTruststore";
    log.info("certPath: {}", certPath);
    // write Key Store
    try (FileOutputStream out = new FileOutputStream(certPath)) {
      keyStore.store(out, Constants.CERT_PW.toCharArray());
    }

    // Set Certificates to System properties
    System.setProperty("javax.net.ssl.trustStore", certPath);
    System.setProperty("javax.net.ssl.trustStorePassword", Constants.CERT_PW);
  }

  @Bean
  public RestTemplate restTemplate() {
    createCustomTrustStore();
    RestTemplate restTemplate = new RestTemplate();
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(new ObjectMapper());
    restTemplate.getMessageConverters().add(converter);
    return restTemplate;
  }
}
