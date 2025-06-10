package com.gng.api.db;

import com.gng.api.report.ExtentReportManager;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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
        String query = DBQuery.select_UZBPSTO_OBJECT_Value;
        logQueryInAllure("Get UZBPSTO_OBJECT Value", query);
        return jdbcTemplate.queryForObject(query, String.class);
    }

    public Map<String, Object> select_UZRPSTO_PARM_NAME_Value() {
        String query = DBQuery.select_UZRPSTO_PARM_NAME_Value;
        logQueryInAllure("Get UZRPSTO_PARM_NAME Value", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getAccountDetails_ForResidentialOrCommercialAccount(String account_type) {
        String query = DBQuery.ACTIVE_RESIDENTIAL_OR_COMMERCIAL_CUSTOMERS;
        logQueryInAllure("Get Customer Code And Premise Code", query);
        return jdbcTemplate.queryForMap(query, account_type);
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

    public Map<String, Object> getAccountDetails_ForPastDueBalanceCommercialAccount(String account_type) {
        String query = DBQuery.GET_CUSTOMERBUSINESSNAME_FOR_PASTDUEBALANCE_COMMERCIALACCOUNT;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getAccountDetails_ForSONPCommercialAccount(String account_type) {
        String query = DBQuery.GET_CUSTOMERBUSINESSNAME_FOR_PASTDUEBALANCE_COMMERCIALACCOUNT;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustomBusnsNm_ForActPenRewardCommercialAccount() {
        String query = DBQuery.GET_CUSTOMERBUSINESSNAME_FOR_ACTIVEPENDINGREWARD_COMMERCIALACCOUNT;
        logQueryInAllure("Get CustomerBusinessName", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustomBusnsNm_ForCommercialAccountTC85_87(String account_type) {
        String query = DBQuery.GET_CUSTOMERBUSINESSNAME_TC85_87;
        String fromattedQuery1=null;
        String formattedQuery2=null;
        if (account_type.equalsIgnoreCase("EarlyTerminationCharge")) {
            fromattedQuery1=query.replace("{value}", "'200', '220'");
            formattedQuery2=query.replace("?","CCV");
        } else if (account_type.equalsIgnoreCase("SeniorResidential")) {
            fromattedQuery1=query.replace("{value}", "'200'");
            formattedQuery2=query.replace("?","CFM");
        } else {
            throw new IllegalArgumentException("Invalid account type: " + account_type);
        }


        logQueryInAllure("Get CustomerBusinessName", formattedQuery2);
        return jdbcTemplate.queryForMap(formattedQuery2);
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
