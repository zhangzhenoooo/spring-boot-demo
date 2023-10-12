package entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author jcc
 * @since 2020-04-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("C_OC_B_AGENCY_INFO")
public class AgencyInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("ID")
    private String id;

    @TableField("AGENCY_NAME")
    private String agencyName;

    @TableField("AGENCY_CODE")
    private String agencyCode;

    @TableField("AGENCY_ID")
    private String agencyId;

    @TableField("AGENCY_TYPE")
    private String agencyType;

    @TableField("INDUSTRY")
    private String industry;

    @TableField("PROVINCE")
    private String province;

    @TableField("ADDRESS")
    private String address;

    @TableField("ORGANIZATION_CODE")
    private String organizationCode;

    @TableField("CONTACT_NAME")
    private String contactName;

    @TableField("EMAIL")
    private String email;

    @TableField("PHONE")
    private String phone;

    @TableField("HOSPITAL_GRADE")
    private String hospitalGrade;
    // 1：对接财政，2：对接单位 3：对接saas公共服务，4：对接本地公共服务
    @TableField("DOCKING_PARTY")
    private String dockingParty;

    @TableField("PLATFORM_ADDRESS")
    private String platformAddress;

    @TableField("PLATFORM_APPID")
    private String platformAppid;

    @TableField("PLATFORM_APPKEY")
    private String platformAppkey;

    @TableField("PLATFORM_VERSION")
    private String platformVersion;

    @TableField("PLATFORM_TYPE")
    private String platformType;

    @TableField("CLOUD_ADDRESS")
    private String cloudAddress;

    @TableField("CLOUD_APPID")
    private String cloudAppid;

    @TableField("CLOUD_APPKEY")
    private String cloudAppkey;

    @TableField("REMARK")
    private String remark;

    @TableField("APPLICANT")
    private String applicant;

    @TableField("APPLY_PHONE")
    private String applyPhone;

    @TableField("APPLY_TIME")
    private String applyTime;

    @TableField("CHECKER")
    private String checker;

    @TableField("CHECK_TIME")
    private String checkTime;

    @TableField("FLOW_STATUS")
    private String flowStatus;

    @TableField("PARENT_CODE")
    private String parentCode;

    @TableField("CONFIG_ID")
    private String configId;

    @TableField("AREA_CODE")
    private String areaCode;

    @TableField("SERVER_TYPE")
    private String serverType;

    @TableField("CITY_CODE")
    private String cityCode;

    @TableField("INVOICE_STORAGE_MODEL")
    private String invoiceStorageModel;

    @TableField("SHOW_AUTH_EXPIRATION")
    private String showAuthExpiration;

    @TableField("PARENT_AGENCY")
    private String parentAgency;

    @TableField("REPLENISH_TREATY")
    private String replenishTreaty;

    /**
     * 补充协议签订日期
     */
    @TableField("REPLENISH_TREATY_SIGNDATE")
    private String replenishTreatySigndate;

    /**
     * 交付类型 1、财票 2、预交金 3、金3税票 4、金4税票 5、其他凭证
     */
    @TableField("DELIVER_TYPE")
    private String deliverType;

    @TableField("AGENCY_FROM")
    private String agencyFrom;

    private static final String AGENCY_SIGN_DEFAULT_VALUE = "1";
    @TableField("AGENCY_SIGN")
    private String agencySign = AGENCY_SIGN_DEFAULT_VALUE;

    /**
     * 在线单位:父单位id
     */
    @TableField("PARENT_AGENCY_ID")
    private String parentAgencyId;
    /**
     * 在线单位:父单位名称
     */
    @TableField("PARENT_AGENCY_NAME")
    private String parentAgencyName;

    /**
     * 纳税人识别号
     */
    @TableField("TAX_PAYER_IDENTITY")
    private String taxPayerIdentify;



}
