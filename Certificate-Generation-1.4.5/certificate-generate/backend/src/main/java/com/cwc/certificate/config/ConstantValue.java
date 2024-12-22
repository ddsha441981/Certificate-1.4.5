package com.cwc.certificate.config;


/**
 * @author Deendayal KUmawat
 * @version 1.4.3
 * @since 2024/02/14
 */
public class ConstantValue {
    public static final String DEFAULT_GOOGLE_APP_SCRIPT_URL_TYPE = "https://script.google.com/macros/s/";

    public static final String DEFAULT_GOOGLE_APP_SCRIPT_URL = "exec";
    public static final String DEFAULT_GOOGLE_APP_SCRIPT_URL_SEPARATE = "/";
    public static final String DEFAULT_GOOGLE_APP_SCRIPT_CACHE = "document";
    public static final String DEFAULT_NAME_SALARY = "salary";
    public static final String REMOVED_DATA_DOCS = "documentData";
    public static final String CERTIFICATE_CARD_NAME_DOCS = "certificate";
    public static final String IDENTIFICATION_DETAILS = "identificationDetails";
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY_COMPANY = "companyId";
    public static final String DEFAULT_SORT_BY_CERTIFICATE = "certificateId";
    public static final String DEFAULT_SORT_DIR = "asc";
    public static final String DEFAULT_SEARCH_KEYWORD = "%";
    public static final String DEFAULT_APP_SCRIPT_GOOGLE_VALUE_DOCUMENT = "Documents constant data";
    public static final String DEFAULT_APP_SCRIPT_GOOGLE_VALUE_SALARY = "Salary constant data";
    public static final String DEFAULT_APP_SCRIPT_GOOGLE_ID_DOCUMENT = "document";
    public static final String DEFAULT_APP_SCRIPT_GOOGLE_ID_SALARY = "salary";

    public static final String DEFAULT_APP_SCRIPT_GOOGLE_ID_DRIVE = "drive";
    public static final String DEFAULT_APP_SCRIPT_GOOGLE_VALUE_DRIVE = "Drive constant data";

    public static final String DEFAULT_APP_SCRIPT_GOOGLE_ID_INTERVIEW = "interview";
    public static final String DEFAULT_APP_SCRIPT_GOOGLE_VALUE_INTERVIEW = "Schedule Interview constant data";

    public static final String DEFAULT_DOCS_NAME = "document";
    public static final String DEFAULT_SALARY_NAME = "salary";
    public static final String DEFAULT_DRIVE_NAME = "drive";
    public static final String DEFAULT_INTERVIEW_NAME = "interview";

    public static final String DEFAULT_IMAGE_FORMAT = "image/png";

    public static final String DEFAULT_VERSION_FILE_NAME = "version_info.md";


    //   Default bank details
    public static final String DEFAULT_BANK_NAME = "HDFC Bank";
    public static final String DEFAULT_ACCOUNT_NUMBER = "1234567890";
    public static final String DEFAULT_IFSC_CODE = "HDFC0001234";
    public static final String DEFAULT_ACCOUNT_HOLDER_NAME = "CWC";
    public static final String DEFAULT_UAN = "1234567890";
    public static final String DEFAULT_ESI = "1234567890";
    public static final String DEFAULT_CUSTOMERID = "1234567890";

    //   Default Identification Details
    public static final String DEFAULT_AADAR_NUMBER = "122345678";
    public static final String DEFAULT_PAN_NUMBER = "CERT675AT";
    public static final String DEFAULT_DOC_NAME = "dummay.png";
    public static final String DEFAULT_DOCUMENT_TYPE = "deaultType";
    public static final String DEFAULT_DOCUMENT_DATA = "defaultData";


    public static final String FORGOT_PASSWORD_EMAIL_SUBJECT = "Forgot Password Recovery";


    public static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-resources"
    };

    public static final String[] ACTUATOR_WHITELIST = {
            "/actuator",
            "actuator/prometheus",
            "/actuator/**",
    };


    public static String FORGET_PASSWORD_LINK_MESSAGE = """
            Dear, %s!
            please click on the following link to reset your password: %s 
            If you are unable to click on the link, you can copy and paste it into your web browser's address bar.
            """;

    public static final String BIRTHDAY_SUBJECT = "Birthday Notification";
    public static final String DEFAUL_BIRTHDAY_LIST = "Today's Birthday List: ";

    // Status Update
    public static final String UPDATE_ACTIVE_STATUS = "active";
    public static final String UPDATE_INACTIVE_STATUS = "inactive";

    // Status Update
    public static final String GENERATED_ACTIVE_STATUS = "GENERATED";
    public static final String UPDATE_PENDING_INACTIVE_STATUS = "PENDING";


    // Authorization
    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5 * 60 * 60 * 1000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";


    //Documents Rest API
    public static final String API_BASE_URL_VERSION_1 = "/api/v1/**";

    //Excel and PDF Integration
    public static final String API_BASE_URL_VERSION_2 = "/api/v2/**";

    //Security Integration
    public static final String API_BASE_URL_VERSION_3 = "/api/v3/security/**";

    //Video Calling Integration
    public static final String API_BASE_URL_VERSION_4 = "/api/v4/**";

    public static final String API_BASE_URL_VERSION_5 = "/api/v5/**";
    public static final String API_BASE_URL_VERSION_6 = "/api/v6/**";

    //   Swagger Constants Value For Local Server
    public static final String EMAIL_ADDRESS = "codwithcup.developer@gmail.com";
    public static final String DEVELOPER_NAME = "Deendayal Kumawat";
    public static final String SET_URL = "https://www.github.com/ddsha441981";
    public static final String SET_LOCALHOST_URL = "https://www.github.com/ddsha441981";
    public static final String SET_LOCAL_DESCRIPTION = "This is the local development environment for the Certificate Management API. It is used for testing and debugging during the development phase.";

    //   Swagger Constants Value For Production Server
    public static final String SET_PRODUCTION_URL = "https://www.github.com/ddsha441981";
    public static final String SET_PRODUCTION_DESCRIPTION = "This is the production environment for the Certificate Management API. It is used for live services and available to external clients.";

    //MIT Licence Constant Value
    public static final String SET_MIT_LICENSE = "MIT License";
    public static final String SET_MIT_LICENSE_URL = "https://choosealicense.com/licenses/mit/";

    //Information of Swagger Constant Value
    public static final String SET_INFO_TITLE = "CERTIFICATE MANAGEMENT API";
    public static final String SET_APP_VERSION = "1.4.3";
    public static final String SET_TERMS_OF_SERVICE = "https://my-awesome-api.com/terms";
    public static final String SET_INFO_SUMMERY = "A comprehensive API for managing product certificates in various environments.";
    public static final String SET_INFO_DESCRIPTION = "The Certificate Management API provides a suite of services for managing certificates. These services include the creation, retrieval, updating, and deletion of certificates. The API is designed for ease of integration and aims to provide secure access to certificate data.";

    //Security Configuration Constant Value
    public static final String TOKEN_TYPE_HEADER_NAME = "bearerAuth";
    public static final String TOKEN_TYPE_SCHEME_NAME = "bearerAuth";
    public static final String TOKEN_TYPE_BEARER_AUTH = "JWT";

    //RateLimit MESSAGE

    public static final String RATE_LIMIT_ERROR_MESSAGE = "To many request at endpoint %s from IP %s! Please try again after %d milliseconds!";


    //Default value of Activity Log
    public static final int DEFAULT_PAGE_NUMBER_ACTIVITY_LOG = 0;
    public static final int DEFAULT_PAGE_SIZE_ACTIVITY_LOG = 4;
    public static final String DEFAULT_SORT_BY_ACTIVITY_LOG = "actionTimestamp";


    //Excel & PDF Data
    public static final String DEFAULT_ID_COLUMN = "ID";
    public static final String DEFAULT_NAME_COLUMN = "Name";
    public static final String DEFAULT_COMPANY_NAME_COLUMN = "Company Name";
    public static final String DEFAULT_EMAIL_COLUMN = "Email";
    public static final String DEFAULT_JOB_COLUMN = "Job";
    public static final String DEFAULT_STATUS_COLUMN = "Status";
    public static final String DEFAULT_COMPANY_NAME = "Company";
    public static final String DEFAULT_CERTIFICATE_NAME = "Certificate";
    public static final String DEFAULT_EXCEL_FORMAT_NAME = ".xlsx";
    public static final String DEFAULT_PDF_FORMAT_NAME = ".pdf";
    public static final String DEFAULT_ATTACHMENT_NAME = "attachment";
    public static final String DEFAULT_PDF_REPORT_NAME = "-Report";
    public static final String DEFAULT_EXCEL_REPORT_NAME = "Data Sheet";
    public static final String DEFAULT_PDF_ADAPTER_NAME = "PdfAdapter";
    public static final String DEFAULT_EXCEL_ADAPTER_NAME = "ExcelAdapter";

    //Spring Batch

    public static String[] DEFAULT_BATCH_SQL_TABLE_QUERY = {
        "CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ",
        "CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ",
        "CREATE SEQUENCE BATCH_JOB_SEQ",
        "CREATE TABLE BATCH_JOB_INSTANCE ( JOB_INSTANCE_ID BIGSERIAL PRIMARY KEY, VERSION BIGINT, JOB_NAME VARCHAR(100) NOT NULL, JOB_KEY VARCHAR(32) NOT NULL, UNIQUE (JOB_NAME, JOB_KEY) )",
        "CREATE TABLE BATCH_JOB_EXECUTION ( JOB_EXECUTION_ID BIGSERIAL PRIMARY KEY, VERSION BIGINT, JOB_INSTANCE_ID BIGINT NOT NULL, CREATE_TIME TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, START_TIME TIMESTAMP DEFAULT NULL, END_TIME TIMESTAMP DEFAULT NULL, STATUS VARCHAR(10), EXIT_CODE VARCHAR(20), EXIT_MESSAGE VARCHAR(2500), LAST_UPDATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (JOB_INSTANCE_ID) REFERENCES BATCH_JOB_INSTANCE(JOB_INSTANCE_ID) )",
        "CREATE TABLE BATCH_JOB_EXECUTION_PARAMS ( JOB_EXECUTION_ID BIGINT NOT NULL, PARAMETER_NAME VARCHAR(100) NOT NULL, PARAMETER_TYPE VARCHAR(100) NOT NULL, PARAMETER_VALUE VARCHAR(2500), IDENTIFYING CHAR(1) NOT NULL, PRIMARY KEY (JOB_EXECUTION_ID, PARAMETER_NAME), FOREIGN KEY (JOB_EXECUTION_ID) REFERENCES BATCH_JOB_EXECUTION(JOB_EXECUTION_ID) )",
        "CREATE TABLE BATCH_STEP_EXECUTION ( STEP_EXECUTION_ID BIGSERIAL PRIMARY KEY, VERSION BIGINT NOT NULL, STEP_NAME VARCHAR(100) NOT NULL, JOB_EXECUTION_ID BIGINT NOT NULL, CREATE_TIME TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, START_TIME TIMESTAMP DEFAULT NULL, END_TIME TIMESTAMP DEFAULT NULL, STATUS VARCHAR(10), COMMIT_COUNT BIGINT, READ_COUNT BIGINT, FILTER_COUNT BIGINT, WRITE_COUNT BIGINT, READ_SKIP_COUNT BIGINT, WRITE_SKIP_COUNT BIGINT, PROCESS_SKIP_COUNT BIGINT, ROLLBACK_COUNT BIGINT, EXIT_CODE VARCHAR(20), EXIT_MESSAGE VARCHAR(2500), LAST_UPDATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (JOB_EXECUTION_ID) REFERENCES BATCH_JOB_EXECUTION(JOB_EXECUTION_ID) )",
        "CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT ( JOB_EXECUTION_ID BIGINT PRIMARY KEY, SHORT_CONTEXT VARCHAR(2500) NOT NULL, SERIALIZED_CONTEXT TEXT, FOREIGN KEY (JOB_EXECUTION_ID) REFERENCES BATCH_JOB_EXECUTION(JOB_EXECUTION_ID) )",
        "CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT ( STEP_EXECUTION_ID BIGINT PRIMARY KEY, SHORT_CONTEXT VARCHAR(2500) NOT NULL, SERIALIZED_CONTEXT TEXT, FOREIGN KEY (STEP_EXECUTION_ID) REFERENCES BATCH_STEP_EXECUTION(STEP_EXECUTION_ID) )"
    };


}
