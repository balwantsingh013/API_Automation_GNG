package com.gng.api.db;

import com.github.javafaker.Bool;
import com.gng.api.report.ExtentReportManager;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
public class DBAction {

    private final JdbcTemplate jdbcTemplate;

    public DBAction(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getActiveCustomerDetails() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForList(query);
    }

    public List<Map<String, Object>> getAccountInformationHappyFlow() {
        String query = DBQuery.GET_ACCOUNT_INFO_API_SUCCESS_RESPONSE_PARAMETERS;
        logQueryInAllure("Get Account Information Happy Flow", query);
        return jdbcTemplate.queryForList(query);
    }

    public List<Map<String, Object>> getNoteSequenceNumber(String noteSeqNo) {
        String query = DBQuery.SELECT_NOTE_SEQUENCE_NUMBER;
        logQueryInAllure("Get Note Sequence Number", query, noteSeqNo);
        return jdbcTemplate.queryForList(query, noteSeqNo);
    }

    public Map<String, Object> custCodeParamCodeAGLCAccNoServNoTC207(String pricePlan, String sclsCode) {
        String query = DBQuery.SELECT_CUST_PREM_AGLC_SERVICE_CODES;
        logQueryInAllure("Get Customer code, premises code, AGLC Account no, service code ", query);
        return jdbcTemplate.queryForMap(query, pricePlan, sclsCode);
    }

    public Map<String, Object> custCodeParamCodeAGLCAccNoServNoTC211(String pricePlan, String sclsCode) {
        String query = DBQuery.SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_ETC_GPP;
        logQueryInAllure("Get Customer code, premises code, AGLC Account no, service code for account with ETC and GPP plan", query);
        return jdbcTemplate.queryForMap(query, pricePlan, sclsCode);
    }

    public Map<String, Object> custCodeParamCodeAGLCAccNoServNoTC217(String pricePlan, String sclsCode) {
        String query = DBQuery.SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_ETC_GREENER_LIFE;
        logQueryInAllure("Get Customer code, premises code, AGLC Account no, service code for account with ETC and greener life", query);
        return jdbcTemplate.queryForMap(query, pricePlan, sclsCode);
    }

    public Map<String, Object> custCodeParamCodeAGLCAccNoServNoTC218(String pricePlan) {
        String query = DBQuery.SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_ETC_ACTIVE_PENDING_REWARDS;
        logQueryInAllure("Get Customer code, premises code, AGLC Account no, service code for account with active/pending rewards", query);
        return jdbcTemplate.queryForMap(query, pricePlan);
    }

    public Map<String, Object> custCodeParamCodeAGLCAccNoServNoTC220(String pricePlan) {
        String query = DBQuery.SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_ETC_SONP;
        logQueryInAllure("Get Customer code, premises code, AGLC Account no, service code for account with ETC and SONP", query);
        return jdbcTemplate.queryForMap(query, pricePlan);
    }

    public Map<String, Object> custCodeParamCodeAGLCAccNoServNoTC212(String pricePlan, String sclsCode) {
        String query = DBQuery.SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_ETC_ACN;
        logQueryInAllure("Get Customer code, premises code, AGLC Account no, service code for ACN account with ETC", query);
        return jdbcTemplate.queryForMap(query, pricePlan, sclsCode);
    }

    public Map<String, Object> custCodeParamCodeAGLCAccNoServNoTC221(String pricePlan, String sclsCode) {
        String query = DBQuery.SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_UNAPPLIED_DEPOSIT;
        logQueryInAllure("Get Customer code, premises code, AGLC Account no, service code for ACN account with ETC", query);
        return jdbcTemplate.queryForMap(query, sclsCode, pricePlan);
    }

    public Map<String, Object> custCodeParamCodeAGLCAccNoServNoTC214(String pricePlan, String sclsCode) {
        String query = DBQuery.SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_PAST_DUE_BALANCE;
        logQueryInAllure("Get Customer code, premises code, AGLC Account no, service code for ACN account with past due balance", query);
        return jdbcTemplate.queryForMap(query, sclsCode, pricePlan);
    }

    public Map<String, Object> custCodeParamCodeAGLCAccNoServNoTC226(String pricePlan, String sclsCode) {
        String query = DBQuery.SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_INDEXED_PRICE_PLAN;
        logQueryInAllure("Get Customer code, premises code, AGLC Account no, service code for ACN account with past due balance", query);
        return jdbcTemplate.queryForMap(query, sclsCode, pricePlan);
    }

    public Map<String, Object> custCodeParamCodeAGLCAccNoServNoTC209(String pricePlan, String sclsCode) {
        String query = DBQuery.SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_ACN_WITHOUT_ETC;
        logQueryInAllure("Get Customer code, premises code, AGLC Account no, service code for ACN account without ETC", query);
        return jdbcTemplate.queryForMap(query, pricePlan, sclsCode);
    }

    public Map<String, Object> custCodeParamCodeAGLCAccNoServNoTC210(String pricePlan, String sclsCode) {
        String query = DBQuery.SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_CEILING_PRICE_PLAN;
        logQueryInAllure("Get Customer code, premises code, AGLC Account no, service code for ACN account without ETC", query);
        return jdbcTemplate.queryForMap(query, sclsCode, pricePlan);
    }

    public Map<String, Object> custCodeParamCodeAGLCAccNoServNoTC216(String pricePlan, String sclsCode) {
        String query = DBQuery.SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_CEILING_PLAN_PAST_DUE_BALANCE;
        logQueryInAllure("Get Customer code, premises code, AGLC Account no, service code for account with past due balance", query);
        return jdbcTemplate.queryForMap(query,sclsCode, pricePlan);
    }

    public Map<String, Object> custCodeParamCodeAGLCAccNoServNoTC219(String pricePlan, String sclsCode) {
        String query = DBQuery.SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_CEILING_PLAN_ACN_ACTIVE_PENDING_REWARDS;
        logQueryInAllure("Get Customer code, premises code, AGLC Account no, service code for ACN account ceiling plan active/pending rewards", query);
        return jdbcTemplate.queryForMap(query, sclsCode, pricePlan);
    }

    public Map<String, Object> cityStateZip() {
        String query = DBQuery.SELECT_CITY_STATE_ZIP;
        logQueryInAllure("Get city, state and zip", query);
        return jdbcTemplate.queryForMap(query);
    }

    public List<Map<String, Object>> getInvalidCustomerCode(String customerCode) {
        String query = (DBQuery.SEARCH_ACC_SELECT_INVALID_CUSTOMER_CODE).replace("CustomerCode", customerCode);
        logQueryInAllure("Get Invalid Customer Code", query);
        return jdbcTemplate.queryForList(query);
    }

    public Map<String, Object> getPasswordExpireDaysValue() {
        String query = DBQuery.SELECT_PASSWORD_EXPIRE_DAYS;
        logQueryInAllure("Get param password expire day", query);
        return jdbcTemplate.queryForMap(query);

    }

    public int theInvalidPasswordMatchUpdateQuery() {
        String query = DBQuery.THE_PASSWORD_UPDATE_QUERY;
        logQueryInAllure("password doesn't match the login ID", query);
        return jdbcTemplate.update(query);
    }

    public List<Map<String, Object>> togetthefailedcountsandvalidateshouldbe4() {
        String query = DBQuery.FAILED_LOGIN_COUNTS;
        logQueryInAllure("Failed login counts ", query);
        return jdbcTemplate.queryForList(query);
    }

    public void rollBackQuery(String user) {
        String query = DBQuery.ROLLBACK_QUERIES;
        String formattedQuery = query.replace("?", "'" + user + "'");
        ExtentReportManager.logInfoToReport("Executing Rollback Query: {}" + formattedQuery);
        log.info("Executing Rollback Query: {}", formattedQuery);
        logQueryInAllure("Rollback Query", formattedQuery);
        int rowsUpdated = jdbcTemplate.update(query, user);
        log.info("Rollback executed for user: {} | Rows affected: {}", user, rowsUpdated);
        ExtentReportManager.logInfoToReport("Rollback executed for user: {} | Rows affected: {}" +" " +user +" "+ rowsUpdated);
    }


    public int passwordExpiredUpdateQuery(String user) {
        String query = DBQuery.EXPIRED_PASSWORD_UPDATE_QUERY;
        logQueryInAllure("Query to update expire date to current date -1 ", query);
        return jdbcTemplate.update(query, user);
    }

    public List<Map<String, Object>> PasswordExpiredCheckQuery() {
        String query = DBQuery.EXPIRED_PASSWORD_CHECK_QUERY;
        logQueryInAllure("password doesn't match the login ID", query);
        return jdbcTemplate.queryForList(query);
    }

    public List<Map<String, Object>> failedLoginCountQuery() {
        String query = DBQuery.FAILED_LOGIN_COUNTS_FOR_EXPIRED_PASSWORD;
        logQueryInAllure("password doesn't match the login ID", query);
        return jdbcTemplate.queryForList(query);
    }

    public int rollBackQueryForPasswordExpired(String user) {
        String query = DBQuery.EXPIRED_PASSWORD_ROLLBACK_QUERY;
        logQueryInAllure("Query to update expiry date to currentDate + 30 - ", query);
        return jdbcTemplate.update(query, user);
    }

    public int updateUserLockStatusQuery(String user) {
        String query = DBQuery.UPDATE_TO_LOCK_SPECIFIC_USER;
        logQueryInAllure("Update query to lock the user", query);
        return jdbcTemplate.update(query, user);
    }

    public List<Map<String, Object>> checkUserLockStatusQuery() {
        String query = DBQuery.CHECK_USER_LOCK_STATUS_QUERY;
        logQueryInAllure("password doesn't match the login ID", query);
        return jdbcTemplate.queryForList(query);
    }

    public int failedUserLockCountQuery() {
        String query = DBQuery.FAILED_LOGIN_COUNTS_FOR_USER_LOCK_STATUS_QUERY;
        logQueryInAllure("password doesn't match the login ID", query);
        return jdbcTemplate.update(query);
    }

    public int updateQueryToUnlockUser(String user) {
        String query = DBQuery.UPDATE_TO_UNLOCK_SPECIFIC_USER;
        logQueryInAllure("Update query to unlock the user", query);
        return jdbcTemplate.update(query, user);
    }

    public int updateTheFailedLoginQuery() {
        String query = DBQuery.UPDATE_FAILED_LOGIN_QUERY;
        logQueryInAllure("password doesn't match the login ID", query);
        return jdbcTemplate.update(query);
    }

    public List<Map<String, Object>> RoleCountQuery() {
        String query = DBQuery.ROLE_COUNT_QUERY;
        logQueryInAllure("password doesn't match the login ID", query);
        return jdbcTemplate.queryForList(query);
    }

    public List<Map<String, Object>> FailedCountUserRoleQuery() {
        String query = DBQuery.FAILED_COUNT_ON_USER_ROLE_QUERY;
        logQueryInAllure("password doesn't match the login ID", query);
        return jdbcTemplate.queryForList(query);
    }

    public int rollbackCountUserRoleQuery() {
        String query = DBQuery.ROLLBACK_COUNT_ON_USER_ROLE_QUERY;
        logQueryInAllure("password doesn't match the login ID", query);
        return jdbcTemplate.update(query);
    }

    public List<Map<String, Object>> lastNameFirstNameTC112Query() {
        String query = DBQuery.LAST_NAME_FIRST_NAME_QUERY_TC112;
        logQueryInAllure("Last name and First Name ", query);
        return jdbcTemplate.queryForList(query);
    }

    public List<Map<String, Object>> accountNumberSearchETypeNoSSPDBTC110Query() {
        String query = DBQuery.ACCOUNT_NUMBER_E_TYPE_NO_SSP_TC110;
        logQueryInAllure("Last name and First Name ", query);
        return jdbcTemplate.queryForList(query);
    }

    public List<Map<String, Object>> aglcAccountNumberETypeNoSSPTC114Query() {
        String query = DBQuery.AGLC_ACCOUNT_NUMBER_TC114;
        logQueryInAllure("Last name and First Name ", query);
        return jdbcTemplate.queryForList(query);
    }


    public List<Map<String, Object>> customerDataWithETypeTC115Query() {
        String query = DBQuery.CUSTOMER_DATA_WITH_TYPE_TC115;
        logQueryInAllure("Customer Data Not Found With E type ", query);
        return jdbcTemplate.queryForList(query);
    }


    public String getActiveUserID() {
        String query = DBQuery.SELECT_VALID_USER;
        logQueryInAllure("Get active user ID", query);
        return jdbcTemplate.queryForObject(query, String.class);
    }

    public Map<String, Object> validateFailedLoginForSpecificUser(String userID) {
        String query = DBQuery.SELECT_SPECIFIC_USER_DATA;
        logQueryInAllure("Get active user ID", query);
        return jdbcTemplate.queryForMap(query, userID);
    }

    public String extractUserFromDBWithFailedLogin3() {
        String query = DBQuery.SELECT_USER_WITH_FAILED_LOGIN_3;
        logQueryInAllure("Get active user with 3 failed login attempts", query);
        try {
            return jdbcTemplate.queryForObject(query, String.class);
        } catch (EmptyResultDataAccessException e) {
            log.warn("No user found with 3 failed login attempts.");
            return null;
        }
    }


    public int updateUserToFailedAttempt3(String user) {
        String query = DBQuery.UPDATE_FAILED_ATTEMPT_TO_3;
        logQueryInAllure("Get active user ID", query);
        return jdbcTemplate.update(query, user);
    }

    public String select_UZBPSTO_OBJECT_Value() {
        String query = DBQuery.SELECT_UZBPSTO_OBJECT_Value;
        logQueryInAllure("Get UZBPSTO_OBJECT Value", query);
        return jdbcTemplate.queryForObject(query, String.class);
    }

    public Map<String, Object> select_UZRPSTO_PARM_NAME_Value() {
        String query = DBQuery.SELECT_UZRPSTO_PARM_NAME_Value;
        logQueryInAllure("Get UZRPSTO_PARM_NAME Value", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getAccountDetails_ForResidentialOrCommercialAccount(String account_type) {
        String query = DBQuery.ACTIVE_RESIDENTIAL_OR_COMMERCIAL_CUSTOMERS;
        logQueryInAllure("Get Customer Code And Premise Code", query);
        return jdbcTemplate.queryForMap(query, account_type);
    }

    public Map<String, Object> getCustPremCodeRSSONPNonMaster() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_SONP_NON_MASTER;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActivePendingRewards() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_PENDING_REWARDS;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActiveUnappliedDeposit() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_UNAPPLIED_DEPOSIT;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActiveNoUnappliedDeposit() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_NO_UNAPPLIED_DEPOSIT;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActivePGBExpirationDate() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_PGB_EXP_DATE;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActiveMKTNoExpirationDate() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_MKT_NO_EXP_DATE;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActiveETC() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_ETC;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActiveETCRGB() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_ETC_RGB;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActiveGreenerLife() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_GREENER_LIFE;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActiveCSV() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_CSV;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSFinal() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_FINAL;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActive() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeSSPAccountwithETC(String SSpIndicator) {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_SSP_WITH_ETC;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query,SSpIndicator);
    }

    public Map<String, Object> getCustPremCodeSSPAccountiWithoutETC(String SSpIndicator) {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_SSP_WITHOUT_ETC;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query,SSpIndicator);
    }


    public Map<String, Object> getAccountDetails_LastNameZipCode() {
        String query = DBQuery.GET_LASTNAME_AND_ZIPCODE;
        logQueryInAllure("Get Last Name And Zip Code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getAccountDetails_ForResidentialOrSeniorResAccount(String account_type) {
        String query = DBQuery.GET_FIRSTNAME_LASTNAME_AND_ZIPCODE;
        logQueryInAllure("Get First Name, Last Name And Zip Code", query);
        return jdbcTemplate.queryForMap(query, account_type);
    }

    public Map<String, Object> getAccountDetails_ForPastDueBalanceCommercialAccount() {
        String query = DBQuery.GET_CUSTOMERBUSINESSNAME_FOR_PASTDUEBALANCE_COMMERCIALACCOUNT;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getAccountDetails_ForSONPCommercialAccount() {
        String query = DBQuery.GET_CUSTOMERBUSINESSNAME_FOR__SONP_COMMERCIALACCOUNT;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustomBusnsNm_ForActPenRewardCommercialAccount() {
        String query = DBQuery.GET_CUSTOMERBUSINESSNAME_FOR_ACTIVEPENDINGREWARD_COMMERCIALACCOUNT;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustomerBusinessNameCMActiveETC() {
        String query = DBQuery.GET_CUSTOMERBUSINESSNAME_FOR_ACTIVE_CM_ETC;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustomerBusinessNameCMActiveNoETC() {
        String query = DBQuery.GET_CUSTOMERBUSINESSNAME_FOR_ACTIVE_CM_No_ETC;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustomerBusinessNameCMActiveCCV() {
        String query = DBQuery.GET_CUSTOMERBUSINESSNAME_FOR_ACTIVE_CM_CCV;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustomerBusinessNameCMFinalAccount() {
        String query = DBQuery.GET_CUSTOMERBUSINESSNAME_FOR_FINAL_CM;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getAGLCNumberRSActiveAccount() {
        String query = DBQuery.GET_AGLC_NUMBER_FOR_ACTIVE_RS;
        logQueryInAllure("Get AGLC account number", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getAddressDetailsRSActiveAccount() {
        String query = DBQuery.GET_ADDRESS_DETAILS_FOR_ACTIVE_RS;
        logQueryInAllure("Get Address Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActiveNonMeteredAccount() {
        String query = DBQuery.GET_CUSTOMERCODE_PREM_CODE_ACTIVE_RS_NON_METERED_ACCOUNT;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeMSBAccount() {
        String query = DBQuery.GET_CUSTOMERCODE_PREM_CODE_ACTIVE_MSB_ACCOUNT;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeActiveNonMeteredAccount() {
        String query = DBQuery.GET_CUSTOMERCODE_PREM_CODE_ACTIVE_NON_METERED_ACCOUNT;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeInactiveNonMeteredAccount() {
        String query = DBQuery.GET_CUSTOMERCODE_PREM_CODE_INACTIVE_NON_METERED_ACCOUNT;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeInactiveMeteredAccount() {
        String query = DBQuery.GET_CUSTOMERCODE_PREM_CODE_INACTIVE_METERED_ACCOUNT;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeInactiveAccWithBadDebt() {
        String query = DBQuery.GET_CUSTOMERCODE_PREM_CODE_INACTIVE_ACCOUNT_WITH_BAD_DEBT;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeInactiveAccWithSONP() {
        String query = DBQuery.GET_CUSTOMERCODE_PREM_CODE_INACTIVE_ACCOUNT_WITH_SONP;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeResidentialNewBankrupcy() {
        String query = DBQuery.GET_CUSTOMERCODE_PREM_CODE_RS_NEW_BANKRUPCY;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeResidentialInactiveBankrupcy() {
        String query = DBQuery.GET_CUSTOMERCODE_PREM_CODE_RS_INACTIVE_BANKRUPCY;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeCMNewNonMetered() {
        String query = DBQuery.GET_CUSTOMERCODE_PREM_CODE_CM_NEW_NON_METERED;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeCMActiveNonMetered() {
        String query = DBQuery.GET_CUSTOMERCODE_PREM_CODE_CM_ACTIVE_NON_METERED;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }



    public Map<String, Object> getCustPremCodeCMInactiveMetered() {
        String query = DBQuery.GET_CUSTOMERCODE_PREM_CODE_CM_INACTIVE_METERED;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeCMInactiveBadDebt() {
        String query = DBQuery.GET_CUSTOMERCODE_PREM_CODE_CM_INACTIVE_BAD_DEBT;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeCMInactiveSONP() {
        String query = DBQuery.GET_CUSTOMERCODE_PREM_CODE_CM_INACTIVE_SONP;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeCMInactiveNonMetered() {
        String query = DBQuery.GET_CUSTOMERCODE_PREM_CODE_CM_INACTIVE_NON_METERED;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }


    public Map<String, Object> getCustPremCodeCMNewBakrupcy() {
        String query = DBQuery.GET_CUSTOMERCODE_PREM_CODE_CM_NEW_BANKRUPCY;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeCMInactiveBakrupcy() {
        String query = DBQuery.GET_CUSTOMERCODE_PREM_CODE_CM_INACTIVE_BANKRUPCY;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getEligiblePlansAndOffersRequestParams(String premiseType,
                                                                      String accountType,
                                                                      String creditCheck,
                                                                      Boolean promotionCode,
                                                                      String valueScore,
                                                                      String creditMin,
                                                                      String creditMax,
                                                                      String custCode) {
        String query = DBQuery.GET_ELIGIBLE_PLANS_AND_OFFERS_REQUEST_PARAMS
                .replace("<premiseType>", premiseType)
                .replace("<accountType>", accountType)
                .replace("<creditCheck>", creditCheck)
               // .replace("<promotionCode>", promotionCode)
                .replace("<valueScore>", valueScore)
                .replace("<creditMin>", creditMin)
                .replace("<creditMax>", creditMax)
                .replace("<custCode>", custCode);

        query = promotionCode ? query.replace("AND e.\"UZBENRO_MKT_PROG_CODE\" IS NULL",
                "AND e.\"UZBENRO_MKT_PROG_CODE\" IS NOT NULL")
                : query;

        logQueryInAllure("Get EligiblePlansAndOffersRequestParams", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getEligiblePlansAndOffersCreditSkipRequestParams(String premiseType,
                                                                      String accountType,
                                                                      String creditCheck,
                                                                      Boolean promotionCode,
                                                                      String custCode) {
        String query = DBQuery.GET_ELIGIBLE_PLANS_AND_OFFERS_REQUEST_PARAMS
                .replace("<premiseType>", premiseType)
                .replace("<accountType>", accountType)
                .replace("<creditCheck>", creditCheck)
                .replace("<custCode>", custCode);

        query = promotionCode ? query.replace("AND e.\"UZBENRO_MKT_PROG_CODE\" IS NULL",
                "AND e.\"UZBENRO_MKT_PROG_CODE\" IS NOT NULL")
                : query;

        logQueryInAllure("Get EligiblePlansAndOffersRequestParams", query);
        return jdbcTemplate.queryForMap(query);
    }

    private void logQueryInAllure(String title, String query, Object... params) {
        // Convert parameters to a string
        String paramsString = params != null ? java.util.Arrays.toString(params) : "None";
        // Log query with parameters in the console
        log.debug("Executing SQL: {} with parameters: {}", query, paramsString);
        // Log query with parameters in Allure
        String logContent = "Query: " + query + "\nParameters: " + paramsString;
        Allure.addAttachment(title, new ByteArrayInputStream(logContent.getBytes(StandardCharsets.UTF_8)));
    }
}
