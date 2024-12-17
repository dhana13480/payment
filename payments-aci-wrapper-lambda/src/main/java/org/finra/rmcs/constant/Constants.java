package org.finra.rmcs.constant;

import java.util.List;

public class Constants {

  public static final String ORG_FINRA_RMCS = "org.finra.rmcs";
  public static final String ORG_FINRA = "org.finra";
  public static final String ORG_FINRA_RMCS_REPO = "org.finra.rmcs.repo";
  public static final String RMCS_ENTITY_MANAGER_FACTORY = "rmcsEntityManagerFactory";
  public static final String RMCS_TRANSACTION_MANAGER = "rmcsTransactionManager";
  public static final String SPRING_PROFILES_ACTIVE = "SPRING_PROFILES_ACTIVE";
  public static final String ORG_POSTGRESQL_DRIVER = "org.postgresql.Driver";
  public static final String ORG_FINRA_RMCS_ENTITY = "org.finra.rmcs.entity";
  // Hibernate Properties Start
  public static final String HIBERNATE_DIALECT = "hibernate.dialect";
  public static final String ORG_HIBERNATE_DIALECT_POSTGRESQL_DIALECT =
      "org.hibernate.dialect.PostgreSQLDialect";
  public static final String HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE =
      "hibernate.cache.use_second_level_cache";
  public static final String HIBERNATE_SHOW_SQL = "hibernate.show_sql";
  public static final String HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
  public static final String HIBERNATE_JDBC_BATCH_SIZE = "hibernate.jdbc.batch_size";
  public static final String HIBERNATE_ORDER_INSERTS = "hibernate.order_inserts";
  public static final String HIBERNATE_ENABLE_LAZY_LOAD_NO_TRANS =
      "hibernate.enable_lazy_load_no_trans";
  public static final String HIBERNATE_JDBC_LOB_NON_CONTEXTUAL_CREATION =
      "hibernate.jdbc.lob.non_contextual_creation";
  public static final String PAYMENT_PROCESS_FALSE_FLAG = "false";
  public static final String PAYMENT_PROCESS_TRUE_FLAG = "true";
  public static final String RMCS = "RMCS";
  public static final String BODY_JSON = "body-json";
  public static final String CERT_PW = "changeit";
  public static final String REGEX_REPLACE_CLASS = "(?<=class: ).*(?= method)";
  public static final String REGEX_REPLACE_METHOD = "(?<=method: ).*(?= correlationId)";
  public static final String APPLICATION_JSON = "application/json";
  public static final String CLASS = "class: ";
  public static final String METHOD = "method: ";
  public static final String CORRELATION_ID_LOG = "correlationId: ";
  public static final String PAYMENT_REQ_ID_LOG = "payment_req_id: ";
  public static final String DRY_RUN = "dryRun";
  public static final String DRY_RUN_PROCESSED_SUCCESSFULLY = "dryRun is success";
  public static final String FALSE = "false";
  public static final String TRUE = "true";
  public static final String TRANSMISSION_ID = "transmission_id";
  public static final String PAYMENT_REQ_ID = "payment_req_id";
  public static final String MESSAGE = "message";
  public static final String OWNER = "owner";
  public static final String KIND = "kind";
  public static final String BILLING_ADDRESS = "billingAddress";
  public static final String SINGLE_USE = "singleUse";
  public static final String REQUEST_PATH = "request-path";
  public static final String X_AUTH_KEY = "X-Auth-Key";
  public static final String ACI_TOKEN_CONTENT_TYPE = "application/x-www-form-urlencoded";
  public static final String HTTP_STATUS_CODE_EXCEPTION_LOG_FORMAT =
      "{} message: HttpStatusCodeException occurred, state code: {}, response: {}";
  public static final String GENERAL_ERROR_MESSAGE =
      "An error has occurred when attempting to process your request. Please try again after some time. For assistance, contact the FINRA Call Center at (301) 590-6500.";
  public static final List<String> ccRequiredFields =
      List.of(
          TRANSMISSION_ID,
          PAYMENT_REQ_ID,
          OWNER,
          KIND,
          "cardHolderName",
          "cardNumber",
          "expirationDate",
          BILLING_ADDRESS,
          SINGLE_USE);
  public static final List<String> achRequiredFields =
      List.of(
          TRANSMISSION_ID,
          PAYMENT_REQ_ID,
          OWNER,
          KIND,
          "paymentMethodKind",
          "accountHolderName",
          "brandKind",
          "aba",
          "dda",
          BILLING_ADDRESS,
          SINGLE_USE);
  public static final List<String> ownerRequiredFields =
      List.of(KIND, "billerId", "billerAccountId");
  public static final List<String> billingAddressRequiredFields =
      List.of("countryCode");

  private Constants() {
    throw new IllegalStateException(" all are static methods ");
  }
}
