package org.finra.rmcs.constants;

public class Constants {
  public static final String CLASS = "class: ";
  public static final String METHOD = "method: ";
  public static final String CORRELATION_ID = "correlationId: ";
  public static final String NASDCORP = "nasdcorp";
  public static final String ORG_FINRA_RMCS = "org.finra.rmcs";
  public static final String ORG_FINRA = "org.finra";
  public static final String ORG_FINRA_RMCS_REPO = "org.finra.rmcs.repo";
  public static final String RMCS_ENTITY_MANAGER_FACTORY = "rmcsEntityManagerFactory";
  public static final String RMCS_TRANSACTION_MANAGER = "rmcsTransactionManager";
  public static final String SPRING_PROFILES_ACTIVE = "SPRING_PROFILES_ACTIVE";
  public static final String ORG_POSTGRESQL_DRIVER = "org.postgresql.Driver";
  public static final String RMCS = "RMCS";
  public static final String ORG_FINRA_RMCS_ENTITY = "org.finra.rmcs.entity";
  public static final String BODY_JSON = "body-json";
  public static final String HEALTHCHECK = "healthcheck";
  public static final String DRY_RUN_PROCESSED_SUCCESSFULLY = "dryRun is success";

  public static final String REF_EXP_N = "\\\\n";
  public static final String REF_EXP_T = "\\\\t";
  public static final String REF_EXP_B = "\\\\b";
  public static final String REF_EXP_R = "\\\\r";
  public static final String REF_EXP_F = "\\\\f";
  public static final String REF_EXP_Q = "\\\\'";
  public static final String REF_EXP_D = "^\"|\"$";

  public static final String REF_EXP_S = "\\\\";
  public static final String CONTEXT = "context";
  public static final String ID_TOKEN = "id_token";


  public static final String REQ_BODY = "body";
  public static final String REQ_RECORDS = "Records";

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
  public static final String FIP_JWKS_ENDPOINT = "FIP_JWKS_ENDPOINT";
  public static final String EXPIRED_TOKEN_MSG = "Expired token!";
  public static final String MESSAGE_SEND_NOTIFICATION_SUCCESSFUL =
      "Payment notification has been processed successfully";
  public static final String MESSAGE_SEND_NOTIFICATION_FAILED =
      "Payment notification processing failed";
  public static final String AGS = "RMCS";
  public static final String FALSE = "false";
  public static final String TRUE = "true";
  public static final String DRY_RUN = "dryRun";
  public static final String EMAIL_SUBJECT = "subject";
  public static final String EMAIL_SUBJECT_PAID = "Paid";
  public static final String EMAIL_SUBJECT_FAILED_PAYMENT_FAILURE = "Failed, Payment Failure";
  public static final String EMAIL_TEMPLATE_PAID_FILING_ID = "filingId";
  public static final String EMAIL_TEMPLATE_FAILED_PAYMENT_FAILURE_FAILURE_REASON = "failureReason";

  public static final String JSON_NODE_FILING_ID = "filing_id";

  public static final String APPLICATION_JSON = "application/json";

  // Used before we get createBy, updated by from JWT token
  public static final String TEST_USER = "TEST_USER";
  public static final String X_AUTH_KEY = "X-Auth-Key";

  public static final String ACI_PAYMENT_ENCODING_JSON_VALUE = "application/json";
  public static final String ACI_PAYMENT_BEARER = "Bearer ";

  public static final String CERT_PW = "changeit";
  public static final String BUYER_EMAIL = "BUYER_EMAIL";
  public static final String BCC_EMAIL = "candidateservices@finra.org";
  public static final String EMAIL = "email";
  public static final String ACTION_LINK = "https://finra.org";
  public static final String FINRA_HELP_DESK = "FINRA Help Desk";
  public static final String SUPPORT_CONTACT = "555-555-5555";

  public static final String SUPP_START_DAY = "Monday";
  public static final String SUPP_END_DAY = "Friday";


  public static final String SUPP_START_TIME = "9AM";
  public static final String SUPP_END_TIME = "5PM";
  public static final String U4_REQ_GRP = "DEF Group";

  public static final String PAID_EMAIL_SUBJ =
      "FINRA Payment Confirmation for Maintaining Qualifications Program";
  public static final String SUBSCRIPTION_GRP_NAME = "RMCS Notifications";
  public static final String SUBSCRIPTION_TYPE_NAME = "OPAY";
  public static final String SOURCE_APP_NAME = "Payments Service";
  public static final String NOW = "now";
  public static final String ANNOUNCEMENT = "ANNOUNCEMENT";
  public static final String PUBLISED = "PUBLISHED";
  public static final String AUDIENCE_TYPE = "EMAILS";

  public static final String TEMPLATE_NAME = "standardNotification";

  public static final String GRANT_TYPE = "?grant_type=";

  public static final String BREAK = "<br/>";
  public static final String CONST_OPAY = "<//opay:FilingId>";
  public static final String CONST_ACI_CONF = "<Bill/BillStatusTracking/ACIConfirmation >";

  public static final String CONST_TRNSCTION_AMT =
      "<Bill/BillStatusTracking/Payment/TransactionAmount>";
  public static final String CONST_FAILURE_RSN = "<failureReason>";
  public static final String EMPTY_STR = "";
  public static final String CONST_BILL_TS = "<Bill/SubmitTimestamp><";

  public static final String CONST_CONFIRM_CD =
      "<Bill/BillStatusTracking/Reversal/OriginalConfirmation>";
  public static final String RET_CD_MSG =
      "<Bill/BillStatusTracking/Reversal/ReturnCode - ReturnCodeMessage>";

  /*
   * public static final String CONST_SEMI_COLON = ";"; public static final String REMI_DATE =
   * "<Bill/BillStatusTracking/Reversal/RemitDate>"; public static final int
   * REVENUE_STREAM_APP_CONFIG_SEND_REMIDER_HRS_NOT_SEND = 0;
   * 
   * public static final String PURCHAE_ITEMS_JSON_AMOUNT = "amount"; public static final String
   * PURCHAE_ITEMS_JSON_CONVENIENCE_FEE = "convenience_fee"; public static final String
   * PURCHAE_ITEMS_JSON_CONVENIENCE_FEE_WAIVER_REASON = "convenience_fee_waiver_reason";
   */
  public static final String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";


  private Constants() {
    throw new IllegalStateException(" all are static methods ");
  }
}
