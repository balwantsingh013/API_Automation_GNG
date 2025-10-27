package com.gng.api.db;

import com.gng.api.report.ExtentReportManager;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Types;
import java.util.Collections;
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

    public List<Map<String, Object>> getActiveCustomerOnPaymentArrangementWithBalanceDetails() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_WITH_PAYMENT_ARRANGEMENT_PAST_DUE_BALANCE;
        logQueryInAllure("Get Active Customer on Payment Arrangement with Balance Details", query);
        return jdbcTemplate.queryForList(query);
    }

    public List<Map<String, Object>> getActiveCustomerWithServiceTransferEnrollment() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_WITH_SERVICE_TRANSFER_ENROLLMENT;
        logQueryInAllure("Get Active Customer with Service Transfer Enrollment Details", query);
        return jdbcTemplate.queryForList(query);
    }

    public Map<String, Object> getCustomerInformationByStatusAndPlanType(String accountStatus, String planType) {
        String query = DBQuery.GET_CUSTOMER_INFORMATION_BASED_ON_ACCOUNT_STATUS_AND_PLAN_TYPE;
        logQueryInAllure("Get Customer info based on account status and plan type", query);
        return jdbcTemplate.queryForMap(query, accountStatus, planType);
    }

    public Map<String, Object> getTenantCustomerInformationByStatusAndPlanType(String accountStatus, String planType) {
        String query = DBQuery.GET_TENANT_CUSTOMER_INFORMATION_BASED_ON_ACCOUNT_STATUS_AND_PLAN_TYPE;
        logQueryInAllure("Get Tenant Customer info based on account status and plan type", query);
        return jdbcTemplate.queryForMap(query, accountStatus, planType);
    }

    public List<Map<String, Object>> getCustomerInformationByStatusAndPlanTypeWithMiddleName(String accountStatus, String planType, boolean onAbd, boolean middleNameNotNull) {
        String query = DBQuery.GET_CUSTOMER_INFORMATION_BASED_ON_ACCOUNT_STATUS_AND_PLAN_TYPE_WITH_MIDDLE_NAME;
        query =   query.replace("<ABD>", "");
        if (onAbd){
            query = query.replace("<ABD>", "AND NVL(a.ucracct_draft_acct_status,'N') = 'A'");
        }
        else{
            query =   query.replace("<ABD>", "");
        }
        if (middleNameNotNull){
            query = query.replace("<middleNameNotNull>", "AND c.ucbcust_middle_name IS NOT NULL");
        }
        else{
            query = query.replace("<middleNameNotNull>", "");
        }
        logQueryInAllure("Get Customer info based on account status and plan type", query);
        return jdbcTemplate.queryForList(query, accountStatus, planType);
    }

    public List<Map<String, Object>> getCustomerInformationByStatusWithBadDebt(String accountStatus, String rateSchedule, String planType, String planCode) {
        String query = DBQuery.GET_CUSTOMER_AND_PREMISES_CODE_WITH_SONP_PAST_DUE_BALANCE_BAD_DEBT;
        logQueryInAllure("Get  Customer info based on account status with bad debt and disconnect", query);
        return jdbcTemplate.queryForList(query, accountStatus, rateSchedule, planType, planCode);
    }


    public List<Map<String, Object>> getCustomerInformationDefaultedPaBudget() {
        String query = DBQuery.GET_CUSTOMER_AND_PREMISES_WITH_DEFAULTED_PA_ACTIVE_BUDGET;
        logQueryInAllure("Get  Customer info based on account status with defaulted pa active budget plan", query);
        return jdbcTemplate.queryForList(query);
    }


    public List<Map<String, Object>> getCustomerInformationByStatusNoBills(String accountStatus, String rateSchedule, String planType, String planCode) {
        String query = DBQuery.GET_CUSTOMER_AND_PREMISES_CODE_NO_BILLS_YET;
        logQueryInAllure("Get  Customer info based on account status with bad debt and disconnect", query);
        return jdbcTemplate.queryForList(query, accountStatus, rateSchedule, planType, planCode);
    }

    public List<Map<String, Object>> getAccountInformationResponseHappy(String custCode, String accountStatus, String planTypeInd, String rateSchedule) {
        final String query = DBQuery.GET_ACCOUNT_INFO_RESPONSE_BY_CUSTOMER_CODE_AND_STATUS;
        final String rs = (rateSchedule == null || rateSchedule.trim().isEmpty())
                ? null
                : rateSchedule.trim();

        logQueryInAllure("Get Account Information Happy Flow", query);

        return jdbcTemplate.queryForList(
                query,
                new SqlParameterValue(Types.VARCHAR, custCode),
                new SqlParameterValue(Types.CHAR,    accountStatus),
                new SqlParameterValue(Types.VARCHAR, rs),
                new SqlParameterValue(Types.CHAR,    planTypeInd)
        );
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
    public Map<String, Object> custCodeParamCodeAGLCAccNo_WitEtcGPP(String pricePlan, String sclsCode) {
        String query = DBQuery.SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_ETC_GPP;
        logQueryInAllure("Get Customer code, premises code, AGLC Account no, service code for account with ETC and GPP plan", query);
        return jdbcTemplate.queryForMap(query, pricePlan, sclsCode);
    }

    public Map<String, Object> validateAllTheTablesAfterEnrollment(String customerCode,
                                                                   String premisesCode,
                                                                   String cycleCode,
                                                                   String reasonCode,
                                                                   String enrollmentStatus,
                                                                   String accountStatusIdicator,
                                                                   String paymentArrear,
                                                                   String badDebtExemptIndicator,
                                                                   String NCOAProtectIndicator,
                                                                   String feedbackIndicator,
                                                                   String contactDirection,
                                                                   String referredIndicator,
                                                                   String OCRCDETStatus,
                                                                   String OCRCTIMAutomaticIndicator,
                                                                   String contactType
    ){
        String query = DBQuery.SELECT_ENROLLMENT_RECORD_DATA;
        logQueryInAllure("Get Customer code for newly enrolled account", query);
        return jdbcTemplate.queryForMap(query, customerCode, premisesCode,enrollmentStatus, accountStatusIdicator, cycleCode, paymentArrear, badDebtExemptIndicator,NCOAProtectIndicator, feedbackIndicator,contactDirection, reasonCode,referredIndicator,contactType, OCRCDETStatus, OCRCTIMAutomaticIndicator, reasonCode);
    }

    public Map<String, Object> validateAllTheTablesAfterEnrollmentForIncompleteEnrollment(String customerCode,
                                                                   String premisesCode,
                                                                   String enrollmentStatus,
                                                                   String feedbackIndicator,
                                                                   String contactDirection,
                                                                   String reasonCode,
                                                                   String referredIndicator,
                                                                   String OCRCDETStatus,
                                                                   String OCRCTIMAutomaticIndicator,
                                                                   String contactType
    ){
        String query = DBQuery.SELECT_ENROLLMENT_RECORD_DATA_FOR_INCOMPLETE_ENROLLMENT;
        logQueryInAllure("Get Customer code for newly enrolled account", query);
        return jdbcTemplate.queryForMap(query, customerCode, premisesCode,enrollmentStatus, feedbackIndicator,contactDirection, reasonCode,referredIndicator,contactType, OCRCDETStatus, OCRCTIMAutomaticIndicator, reasonCode);
    }

    public Map<String, Object> validateAllTheTablesAfterUnenrollment(String customerCode){
        String query = DBQuery.SELECT_UNENROLLMENT_ACCOUNT_DETAILS;
        logQueryInAllure("Get Customer code for newly enrolled account", query);
        return jdbcTemplate.queryForMap(query, customerCode);

    }

    public Map<String, Object> validateAllTheTablesAfterGetEligiblePlansRequest(String customerCode,
                                                                                String premisesCode,
                                                                                String firstName,
                                                                                String lastName,
                                                                                String zipCode,
                                                                                String aglcServiceLocationID,
                                                                                String streetNumber,
                                                                                String city,
                                                                                String state){

        String query = DBQuery.SELECT_ENROLLMENT_RECORD_DATA_FOR_GETELIGIBLE_PLANS;
        logQueryInAllure("Get Customer code for newly enrolled account", query);
        return jdbcTemplate.queryForMap(
                query,
                customerCode,
                premisesCode,
                firstName,
                zipCode,
                lastName,
                firstName,
                lastName,
                aglcServiceLocationID,
                lastName,
                city,
                streetNumber,
                state,
                city,
                streetNumber,
                state,
                zipCode
        );
    }

    public Map<String, Object> custCodePremCodeNoSSPAccount(String sspIndicator) {
        String query = DBQuery.SELECT_CUST_PREM_CODE_NO_SSP;
        logQueryInAllure("Get Customer code, premises code for account without SSP", query);
        return jdbcTemplate.queryForMap(query, sspIndicator);
    }

    public Map<String, Object> custCodePremCodeSSPParticipantCodeAccount(String status) {
        String query = DBQuery.SELECT_CUST_PREM_CODE_SSP_PARTICIPANT_CODE;
        logQueryInAllure("Get Customer code, premises code for account without SSP", query);
        return jdbcTemplate.queryForMap(query, status);
    }

    public Map<String, Object> getCustCodePremCodeNotInSSPParticipantParentTable() {
        String query = DBQuery.SELECT_CUST_PREM_CODE_NOT_IN_SSP_PARTICIPANT_PARENT_TABLE;
        logQueryInAllure("Get Customer code, premises code for account no in SSP Participant parent table", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> lastNameZipNoSSPAccount(String sspIndicator) {
        String query = DBQuery.SELECT_LAST_NAME_ZIP_NO_SSP;
        logQueryInAllure("Get Customer code, premises code for account without SSP", query);
        return jdbcTemplate.queryForMap(query, sspIndicator);
    }

    public Map<String, Object> getStreetCityStateZipDetails() {
        String query = DBQuery.SELECT_CITY_STATE_ZIP;
        logQueryInAllure("Get address details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getPhoneNumber(String telecode, String status) {
        String query = DBQuery.SELECT_PHONE_NUMBER;
        logQueryInAllure("Get phone number", query);
        return jdbcTemplate.queryForMap(query, telecode, status);
    }

    public Map<String, Object> getAddressDetails() {
        String query = DBQuery.SELECT_ADDRESS_DETAILS;
        logQueryInAllure("Get address details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> searchForCustomerCodeAndPremisesCodeInDatabase(String customerCode, String premisesCode) {
        String query = DBQuery.GET_CUSTOMER_CODE_AND_PREMISES_CODE;
        logQueryInAllure("Get Customer code and premises code", query);
        List<Map<String, Object>> results = jdbcTemplate.queryForList(query, customerCode, premisesCode);
        if (results.isEmpty()) {
            return Collections.emptyMap();
        }
        return results.getFirst();
    }

    public Long searchForCustomerBusinessNameInDatabase(String customerBusinessName) {
        String query = DBQuery.GET_RECORDS_MATCHING_CUSTOMER_BUSINESS_NAME;
        logQueryInAllure("Get Records matching customer business name", query);
        Long count = jdbcTemplate.queryForObject(query, Long.class, customerBusinessName+"%");
        return count;
    }

    public Map<String, Object> custCodeParamCodeAGLCAccNoServNoTC217(String pricePlan, String sclsCode) {
        String query = DBQuery.SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_ETC_GREENER_LIFE;
        logQueryInAllure("Get Customer code, premises code, AGLC Account no, service code for account with ETC and greener life", query);
        return jdbcTemplate.queryForMap(query, pricePlan, sclsCode);
    }

    public Map<String, Object> getControlNumber() {
        String query = DBQuery.GET_CONTROL_NUMBER;
        logQueryInAllure("Get control number", query);
        return jdbcTemplate.queryForMap(query);
    }

    public List<Map<String, Object>> getUserRoleIDs(String userId) {
        String query = DBQuery.GET_USER_ROLE_IDS;
        logQueryInAllure("Get user role IDs", query);
        return jdbcTemplate.queryForList(query, userId);   }


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

    public Map<String, Object> custCodePremCode() {
        String query = DBQuery.SELECT_CUST_PREM_CODE_WITH_UNAPPLIED_DEPOSIT;
        logQueryInAllure("Get Customer code, premises code", query);
        return jdbcTemplate.queryForMap(query);
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

    public Map<String, Object> custCodeParamCodeAGLCAccNoServNoTC230(String pricePlan, String sclsCode) {
        String query = DBQuery.SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_CEILING_PRICE_PLAN_TC_230;
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

    public Map<String, Object> custCodeParamCodeAGLCAccNoServByPlanCodeCustomerType(String pricePlan, String sclsCode) {
        String query = DBQuery.SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_ACN_WITHOUT_ETC;
        logQueryInAllure("Get Customer code, premises code, AGLC Account no, service code for ACN account without ETC", query);
        return jdbcTemplate.queryForMap(query, pricePlan, sclsCode);
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

    public int failedLoginsCount(String user) {
        String query = DBQuery.FAILED_LOGIN_COUNTS;
        logQueryInAllure("Failed login counts", query);
        return jdbcTemplate.queryForObject(query, Integer.class, user);
    }

    public boolean isPasswordExpirationUpdatedToSysdatePlus45(String user) {
        String query = DBQuery.PASSWORD_EXPIRATION_SYSDATE_PLUS_45;
        logQueryInAllure("Password expiration status", query);
        String result = jdbcTemplate.queryForObject(query, String.class, user);
        return "TRUE".equalsIgnoreCase(result);
    }

    public int updateFailedLoginsCount(int count, String user) {
        String query = DBQuery.ROLL_BACK_QUERY_FOR_USER_LOCK_STATUS_QUERY;
        logQueryInAllure("Failed login counts ", query);
        return jdbcTemplate.update(query, count, user);
    }

    public int updateUserLockedStatus(String lockedOutIndicato, int count, String loginId) {
        String query = DBQuery.UPDATE_USER_LOCK_STATUS_QUERY;
        logQueryInAllure("Failed login counts ", query);
        return jdbcTemplate.update(query, lockedOutIndicato, count, loginId);
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

    public Map<String, Object> PasswordExpiredCheckQuery(String user) {
        String query = DBQuery.EXPIRED_PASSWORD_CHECK_QUERY;
        logQueryInAllure("password doesn't match the login ID", query);
        return jdbcTemplate.queryForMap(query, user);
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

    public Map<String, Object> checkUserLockStatusQuery() {
        String query = DBQuery.CHECK_USER_LOCK_STATUS_QUERY;
        logQueryInAllure("check lock status of an account", query);
        return jdbcTemplate.queryForMap(query);
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

    public Map<String, Object> lastNameFirstNameTC112Query() {
        String query = DBQuery.LAST_NAME_FIRST_NAME_QUERY_TC112;
        logQueryInAllure("Last name and First Name ", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> accountNumberSearchETypeNoSSPDBTC110Query(String sspIndicator) {
        String query = DBQuery.ACCOUNT_NUMBER_E_TYPE_NO_SSP_TC110;
        logQueryInAllure("customer code and premises code for E type No SSP ", query);
        return jdbcTemplate.queryForMap(query, sspIndicator);
    }

    public Map<String, Object> aglcAccountNumberETypeNoSSPTC114Query() {
        String query = DBQuery.AGLC_ACCOUNT_NUMBER_TC114;
        logQueryInAllure("Last name and First Name ", query);
        return jdbcTemplate.queryForMap(query);
    }


    public Map<String, Object> customerDataWithETypeTC115Query() {
        String query = DBQuery.CUSTOMER_DATA_WITH_TYPE_TC115;
        logQueryInAllure("Customer Data Not Found With E type ", query);
        return jdbcTemplate.queryForMap(query);
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

    public Map<String, Object> getCustPremCodeRSFinalPendingRewards() {
        String query = DBQuery.GET_FINAL_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_PENDING_REWARDS;
        logQueryInAllure("Get final Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActiveUnappliedDeposit() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_UNAPPLIED_DEPOSIT;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSInactiveUnappliedDeposit() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_INACTIVE_UNAPPLIED_DEPOSIT;
        logQueryInAllure("Get Inactive Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSNewUnappliedDeposit() {
        String query = DBQuery.GET_CUSTOMER_AND_PREMISES_CODE_RS_NEW_UNAPPLIED_DEPOSIT;
        logQueryInAllure("Get Inactive Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActiveUnappliedDepositCSV() {
        String query = DBQuery.GET_CUSTOMER_AND_PREMISES_CODE_RS_CSV_UNAPPLIED_DEPOSIT;
        logQueryInAllure("Get active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActiveDiscount() {
        String query = DBQuery.GET_CUSTOMER_AND_PREMISES_CODE_RS_CSV_ACTIVE_DISCOUNTS;
        logQueryInAllure("Get Inactive Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSFinalUnappliedDeposit() {
        String query = DBQuery.GET_CUSTOMER_AND_PREMISES_CODE_RS_FINAL_UNAPPLIED_DEPOSIT;
        logQueryInAllure("Get Inactive Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSInactivePastDue() {
        String query = DBQuery.GET_INACTIVE_ACCOUNT_WITH_PAST_DUE_NO_SONP;
        logQueryInAllure("Get Inactive Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActivePastDue() {
        String query = DBQuery.GET_ACTIVE_CUST_PREM_CODE_ACTIVE_GREENER_PENDING_REWARDS_PAST_DUE;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActivePastDueRewards() {
        String query = DBQuery.GET_ACTIVE_CUST_PREM_CODE_ACTIVEPAST_DUE_FIXED_PRICE;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActiveSONP() {
        String query = DBQuery.GET_ACTIVE_CUST_PREM_CODE_ACTIVE_GREENER_PENDING_REWARDS_SONP;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActiveSONPUnappliedDeposit() {
        String query = DBQuery.SELECT_CUST_PREM_CODE_ACTIVE_UNAPPLIED_DEPOSIT;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActiveSONPRewards() {
        String query = DBQuery.SELECT_CUST_PREM_CODE_ACTIVE_SONP_REWARDS;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActiveSONPDiscounts() {
        String query = DBQuery.SELECT_CUST_PREM_CODE_ACTIVE_SONP_DISCOUNTS;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActiveSONPDiscountsRestrictions() {
        String query = DBQuery.SELECT_CUST_PREM_CODE_ACTIVE_SONP_DISCOUNTS_RESTRICTIONS;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActiveSONPDiscountsMultiple() {
        String query = DBQuery.SELECT_CUST_PREM_CODE_ACTIVE_SONP_DISCOUNTS_MULTIPLE;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActiveSONPDiscountsTransfersble() {
        String query = DBQuery.SELECT_CUST_PREM_CODE_ACTIVE_SONP_DISCOUNTS_TRANSFERABLE;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeRSActiveGreenerLifeNoSONP() {
        String query = DBQuery.SELECT_CUST_PREM_CODE_ACTIVE_GREENER_LIFE_NO_SONP;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeDiscountWithRestrictions() {
        String query = DBQuery.SELECT_CUST_PREM_CODE_DISCOUNTS_WITH_RESTRICTIONS;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTransferableDiscount() {
        String query = DBQuery.SELECT_CUST_PREM_CODE_TRANSFERABLE_DISCOUNTS;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeMultipleDiscount() {
        String query = DBQuery.SELECT_CUST_PREM_CODE_MULTIPLE_DISCOUNTS;
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

    public Map<String, Object> getCustPremCodeSSPAccountwithETC(String SSpIndicator, String customerType) {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_SSP_WITH_ETC;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query,SSpIndicator, customerType);
    }

    public Map<String, Object> getCustPremCodeSSPAccountiWithoutETC() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_SSP_WITHOUT_ETC;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeSSPAccountiWithoutETCTC105B() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_SSP_WITHOUT_ETC_TC105B;
        logQueryInAllure("Get Active Customer Details", query);
        return jdbcTemplate.queryForMap(query);
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

    public Map<String, Object> getAccountDetails_ForPastDueBalanceAndPartialPayment() {
        String query = DBQuery.PAST_DUE_BALANCE_AND_PARTIAL_PAYMENT;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getAccountDetails_ForPastDueBalanceAndNoPayment() {
        String query = DBQuery.PAST_DUE_BALANCE_AND_NO_PAYMENT;
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
        String query = DBQuery.GET_CUSTOMERBUSINESSNAME_FOR_ACTIVE_CM_NO_ETC;
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

    public List<Map<String, Object>> getValidationPlansAndOffers(String controlNum) {
        String query = DBQuery.GET_VALIDATION_PLANS_AND_OFFERS_RESULT
                .replace("<controlNumber>", controlNum);
        logQueryInAllure("Get ValidationPlansAndOffersResult", query);
        return jdbcTemplate.queryForList(query);
    }

    public List<Map<String, Object>> getValidationPrepayPlans(String transactionId) {
        String query = DBQuery.GET_VALIDATION_PREPAY_PLANS_RESULT
                .replace("<transactionId>", transactionId);
        logQueryInAllure("Get ValidationPlansAndOffersResult", query);
        return jdbcTemplate.queryForList(query);
    }
    public Map<String, Object> getPrepayQuote(String customerCode) {
        String query = DBQuery.GET_PRE_PAY_QUOTE
                .replace("<customerCode>", customerCode);
        logQueryInAllure("Get prepay quote", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getLatestUZRRCOTRecord() {
        String query = DBQuery.GET_LATEST_UZRRCOT;
        logQueryInAllure("Get latest UZRRCOT record", query);
        return jdbcTemplate.queryForMap(query);
    }

    public int expirePrepayQuote(String customerCode) {
        String query = DBQuery.UPDATE_PRE_PAY_QUOTE
                .replace("<customerCode>", customerCode);
        logQueryInAllure("expire prepay quote", query);
        return jdbcTemplate.update(query);
    }

    public int deleteUrblerxByCustomerCode(String customerCode) {
        String query = DBQuery.DELETE_URBLEX_BY_CUSTOMER_CODE
                .replace("<customerCode>", customerCode);
        logQueryInAllure("delete urblerx by customer code", query);
        return jdbcTemplate.update(query);
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
