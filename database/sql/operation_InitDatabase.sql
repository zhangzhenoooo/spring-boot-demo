DROP TABLE IF EXISTS C_OC_B_AGENCY_INFO;

CREATE TABLE C_OC_B_AGENCY_INFO
(
    ID                    varchar(32) PRIMARY KEY NOT NULL,
    AGENCY_NAME           varchar(255),
    AGENCY_TYPE           varchar(2),
    INDUSTRY              varchar(2),
    PROVINCE              varchar(32),
    ADDRESS               varchar(255),
    ORGANIZATION_CODE     varchar(32),
    CONTACT_NAME          varchar(64),
    EMAIL                 varchar(100),
    PHONE                 varchar(11),
    HOSPITAL_GRADE        varchar(3),
    PLATFORM_ADDRESS      varchar(255),
    PLATFORM_APPID        varchar(64),
    PLATFORM_APPKEY       varchar(255),
    CLOUD_ADDRESS         varchar(255),
    CLOUD_APPID           varchar(64),
    CLOUD_APPKEY          varchar(255),
    REMARK                varchar(255),
    APPLICANT             varchar(100),
    APPLY_TIME            varchar(20),
    CHECKER               varchar(32),
    CHECK_TIME            varchar(20),
    FLOW_STATUS           varchar(2),
    PARENT_CODE           varchar(32),
    CONFIG_ID             varchar(32),
    AGENCY_ID             varchar(32),
    AGENCY_CODE           varchar(32),
    APPLY_PHONE           varchar(11),
    AREA_CODE             varchar(2),
    SERVER_TYPE           varchar(2)  DEFAULT '1',
    INVOICE_STORAGE_MODEL varchar(6)  DEFAULT NULL,
    CITY_CODE             varchar(6)  DEFAULT NULL,
    DOCKING_PARTY         varchar(2)  DEFAULT '1',
    SHOW_AUTH_EXPIRATION  varchar(20) DEFAULT NULL,
    PARENT_AGENCY         varchar(10) DEFAULT NULL,
    REPLENISH_TREATY      varchar(1) DEFAULT '3' NOT NULL ,
    REPLENISH_TREATY_SIGNDATE varchar(20),
    SOURCE varchar(100),
    AGENCY_FROM varchar(100),
    AGENCY_SIGN varchar(100) DEFAULT '1 ',
    ALLOW_EXTERNAL_ACCESS varchar(1),
    PARENT_AGENCY_ID varchar(100),
    PARENT_AGENCY_NAME varchar(100),
    DELIVER_TYPE varchar(100),
    TAX_PAYER_IDENTITY varchar(100),
    PLATFORM_VERSION varchar(100),
    PLATFORM_TYPE varchar(100)
);

comment
on table C_OC_B_AGENCY_INFO is '单位表';
 
   