package com.gng.api.db;

import com.gng.api.report.SimplifiedExtentReportManager;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
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

    public Map<String, Object> getActiveCustomerWithServiceTransferEnrollment() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_WITH_SERVICE_TRANSFER_ENROLLMENT;
        logQueryInAllure("Get Active Customer with Service Transfer Enrollment Details", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustomerInformationByStatusAndPlanType(String accountStatus, String planType) {
        String query = DBQuery.GET_CUSTOMER_INFORMATION_BASED_ON_ACCOUNT_STATUS_AND_PLAN_TYPE;
        logQueryInAllure("Get Customer info based on account status and plan type", query);
        return jdbcTemplate.queryForMap(query, accountStatus, planType);
    }
    public Map<String, Object> getCustomerInformationInactiveABDAccount() {
        String query = DBQuery.GET_CUSTOMER_INFORMATION_INACTIVE_ABD_ACCOUNT;
        logQueryInAllure("Get Customer info for final ABD account", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustomerInformationCreditScoreTextNoRecord() {
        String query = DBQuery.GET_CUSTOMER_INFORMATION_WITH_TEXT_NO_RECORD;
        logQueryInAllure("Get Customer info based on text no record found", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustomerInformationCreditFreeze() {
        String query = DBQuery.GET_CUSTOMER_INFORMATION_WITH_CREDIT_FREEZE;
        logQueryInAllure("Get Customer info based on text no record found", query);
        return jdbcTemplate.queryForMap(query);
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

    public List<Map<String, Object>> getAccountInformationResponseHappy(String custCode, String accountStatus, String rateSchedule) {
        final String query = DBQuery.GET_ACCOUNT_INFO_RESPONSE_BY_CUSTOMER_CODE_AND_STATUS;
        final String rs = (rateSchedule == null || rateSchedule.trim().isEmpty())
                ? null
                : rateSchedule.trim();

        logQueryInAllure("Get Account Information Happy Flow", query);

        return jdbcTemplate.queryForList(
                query,
                new SqlParameterValue(Types.VARCHAR, custCode),
                new SqlParameterValue(Types.CHAR,    accountStatus),
                new SqlParameterValue(Types.VARCHAR, rs)
        );
    }

    public List<Map<String, Object>> getNoteSequenceNumber(String noteSeqNo) {
        String query = DBQuery.SELECT_NOTE_SEQUENCE_NUMBER;
        logQueryInAllure("Get Note Sequence Number", query, noteSeqNo);
        return jdbcTemplate.queryForList(query, noteSeqNo);
    }

    public Map<String, Object> getNoteBySequenceNumber(String noteSeqNo) {
        String query = DBQuery.SELECT_NOTE_BY_SEQUENCE_NUMBER;
        logQueryInAllure("Get Note By Sequence Number", query, noteSeqNo);
        return jdbcTemplate.queryForMap(query, noteSeqNo);
    }

    public List<Map<String, Object>> getNoteByCustomerCode(String customerCode) {
        String query = DBQuery.SELECT_NOTE_BY_CUSTOMER_CODE;
        logQueryInAllure("Get Note by customerCode", query, customerCode);
        return jdbcTemplate.queryForList(query, customerCode);
    }

    public Map<String, Object> custCodeParamCodeAGLCAccNoServNoTC207(String pricePlan, String sclsCode) {
        String query = DBQuery.SELECT_CUST_PREM_AGLC_SERVICE_CODES;
        logQueryInAllure("Get Customer code, premises code, AGLC Account no, service code ", query);
        return jdbcTemplate.queryForMap(query, pricePlan, sclsCode);
    }

    public Map<String, Object> getUsername(String userName) {
        long startTime = System.currentTimeMillis();
        String queryTemplate = DBQuery.SELECT_USER_NAME;

        // Replace ? with quoted and escaped userName for logging
        String loggedQuery = queryTemplate.replaceFirst("\\?", "'" + userName.replace("'", "''") + "'");

        logQueryInAllure("get username", loggedQuery);

        Map<String, Object> result;
        try {
            result = jdbcTemplate.queryForMap(queryTemplate, userName);

            long elapsed = System.currentTimeMillis() - startTime;

            SimplifiedExtentReportManager.logDatabaseQuery(
                    loggedQuery,
                    result.toString(),
                    elapsed
            );
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            long elapsed = System.currentTimeMillis() - startTime;

            SimplifiedExtentReportManager.logDatabaseQuery(
                    loggedQuery,
                    null,
                    elapsed
            );
            return Collections.emptyMap();
        }

        return result;
    }

    public Map<String, Object> getActiveUsername2() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_USER_NAME2;

        logQueryInAllure("get active username", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getActiveUsername3() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_USER_NAME3;

        logQueryInAllure("get active username", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getActiveUsername() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_USER_NAME;

        logQueryInAllure("get active username", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getActiveUsernameForUpdatePassword() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_USER_NAME_FOR_UPDATE_PASSWORD;
        logQueryInAllure("get active username", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query );

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getActiveUsernameFromOtherTable2() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_USER_NAME_3;

        logQueryInAllure("get active username", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getPasswordForUser(String userName) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_PASSWORD_FOR_USER;

        // Build a safe, readable version of the query for logging
        String loggedQuery = query.replaceFirst("\\?", "'" + userName.replace("'", "''") + "'");

        // Log the actual SQL being executed
        logQueryInAllure("get password for user", loggedQuery);

        // Execute the query with parameter binding (prevents SQL injection)
        Map<String, Object> result = jdbcTemplate.queryForMap(query, userName);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,
                result.toString(),
                elapsed
        );

        return result;
    }



    public Map<String, Object> getActiveUsernameFromOtherTable() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_USER_NAME_2;

        logQueryInAllure("get active username", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getActiveUsernameFromOtherTable5() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_USER_NAME_2;

        logQueryInAllure("get active username", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getActiveUsernameFromOtherTable4() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_USER_NAME_2;

        logQueryInAllure("get active username", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }



    public Map<String, Object> getActiveUsernameFromOtherTable3() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_USER_NAME_2;

        logQueryInAllure("get active username", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithNickname() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_NICKNAME;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithFirstName() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_FIRST_NAME;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithoutFirstName() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITHOUT_FIRST_NAME;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getResidentialAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_RESIDENTIAL_ACCOUNT;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getCommercialAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_COMMERCIAL_ACCOUNT;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getFinalAccountWithNickname() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_FINAL_ACCOUNT_WITH_NICKNAME;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getLatestLoginIdStored() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_LATEST_LOGIN_ID;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getInactiveAccountWithNickname() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_INACTIVE_ACCOUNT_WITH_NICKNAME;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getActiveAccountOnly() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_ACCOUNT_ONLY;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getActiveAccountOnly2() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_ACCOUNT_ONLY;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

//        // Log SQL, result, and execution time
//        SimplifiedExtentReportManager.logDatabaseQuery(
//                query,
//                result.toString(),
//                elapsed
//        );

        return result;
    }

    public List<Map<String, Object>> getActiveAccountWithSameName() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_ACCOUNT_WITH_SAME_NAME;

        logQueryInAllure("get customer code", query);

        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

        long elapsed = System.currentTimeMillis() - startTime;

//        // Log SQL, result, and execution time
//        SimplifiedExtentReportManager.logDatabaseQuery(
//                query,
//                result.toString(),
//                elapsed
//        );

        return result;
    }

    public List<Map<String, Object>> getActiveAccountWithSameName2(String firstname) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_ACCOUNT_WITH_SAME_NAME2;

        logQueryInAllure("get customer code", query);

        List<Map<String, Object>> result = jdbcTemplate.queryForList(query, firstname);

        long elapsed = System.currentTimeMillis() - startTime;

//        // Log SQL, result, and execution time
//        SimplifiedExtentReportManager.logDatabaseQuery(
//                query,
//                result.toString(),
//                elapsed
//        );

        return result;
    }

    public Map<String, Object> getInactiveAccountOnly() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_INACTIVE_ACCOUNT_ONLY;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }



    public Map<String, Object> getInactiveAccountOnly2() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_INACTIVE_ACCOUNT_ONLY;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
//        SimplifiedExtentReportManager.logDatabaseQuery(
//                query,
//                result.toString(),
//                elapsed
//        );

        return result;
    }

    public boolean isAccountNumberRegistered(String custCode) {
        long startTime = System.currentTimeMillis();

        String query = "SELECT 1 FROM custadv_registered_accounts WHERE account_number LIKE CONCAT('%', ?, '%') LIMIT 1";

        logQueryInAllure("check account number registered", query);

        boolean exists = Boolean.TRUE.equals(jdbcTemplate.query(
                query,
                ps -> ps.setString(1, custCode),
                ResultSet::next
        ));

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                "exists=" + exists,
                elapsed
        );

        return exists;
    }


    public Map<String, Object> getAccountWithActualReading() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_ACTUAL_READING;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithActualReading3() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_ACTUAL_READING3;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getAccountWithActualReading2() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_ACTUAL_READING2;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithZeroReading() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_ZERO_READING;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getAccountWithEstimatedReading() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_ESTIMATED_READING;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }



    public Map<String, Object> getValidUsageHistoryAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_VALID_USAGE_HISTORY_ACCOUNT;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getNoUsageHistoryActiveAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_NO_USAGE_HISTORY_ACTIVE_ACCOUNT;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getNoUsageHistoryFinalAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_NO_USAGE_HISTORY_FINAL_ACCOUNT;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getNoUsageHistoryInactiveAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_NO_USAGE_HISTORY_INACTIVE_ACCOUNT;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getNewAccountOnly() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_NEW_ACCOUNT_ONLY;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getNewAccountOnly2() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_NEW_ACCOUNT_ONLY;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
//        SimplifiedExtentReportManager.logDatabaseQuery(
//                query,
//                result.toString(),
//                elapsed
//        );

        return result;
    }


    public Map<String, Object> getFinalAccountOnly() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_FINAL_ACCOUNT_ONLY;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getFinalAccountOnly2() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_FINAL_ACCOUNT_ONLY;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

//         Log SQL, result, and execution time
//        SimplifiedExtentReportManager.logDatabaseQuery(
//                query,
//                result.toString(),
//                elapsed
//        );

        return result;
    }

    public Map<String, Object> getAccountWithoutNickname() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITHOUT_NICKNAME;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getIndustrialAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_INDUSTRIAL_ACCOUNT;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAgricultureAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_AGRICULTURAL_ACCOUNT;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getMultiFamilyAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_MULTIFAMILY_ACCOUNT;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getSeasonalAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_SEASONAL_ACCOUNT;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getSeniorCitizenAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_SENIOR_CITIZEN_ACCOUNT;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getAccountWithStreetName() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_STREET_NAME;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithStreetSuffix() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_STREET_SUFFIX;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithoutStreetSuffix() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITHOUT_STREET_SUFFIX;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithStreetPostDirection() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_STREET_POST_DIR;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithoutStreetPostDirection() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITHOUT_STREET_POST_DIR;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithUnitType() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_UNIT_TYPE;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithoutUnitType() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITHOUT_UNIT_TYPE;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithUnitNumber() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_UNIT_NUMBER;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithoutUnitNumber() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITHOUT_UNIT_NUMBER;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithCity() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_CITY;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithoutCity() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITHOUT_CITY;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getAccountWithState() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_STATE;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithoutState() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITHOUT_STATE;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getAccountWithZipCode() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_ZIP_CODE;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithoutZipCode() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITHOUT_ZIP_CODE;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getAccountWithoutStreetName() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITHOUT_STREET_NAME;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }



    public Map<String, Object> getAccountWithStreetNumber() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_STREET_NUMBER;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithoutStreetNumber() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITHOUT_STREET_NUMBER;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithStreetPreDirection() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_STREET_PREDIRECTION;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithoutStreetPreDirection() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITHOUT_STREET_PREDIRECTION;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getActiveRewards() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_REWARDS;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getPendingRewards() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_PENDING_REWARDS;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getMixedRewards() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_MIXED_REWARDS;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getNoRewards() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_NO_REWARDS;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getActiveReferAFriendRewards() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_REFER_A_FRIEND_REWARDS;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getPendingReferAFriendRewards() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_PENDING_REFER_A_FRIEND_REWARDS;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getRewardDetails(String rewardId) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.SELECT_REWARD_DETAILS;

        // Replace ? placeholders with actual escaped values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + rewardId.replace("'", "''") + "'");

        logQueryInAllure("get nickname for account", loggedQuery);

        // Execute parameterized query safely
        Map<String, Object> result = jdbcTemplate.queryForMap(queryTemplate, rewardId);

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,          // log expanded SQL
                String.valueOf(result),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getNicknameForAccount(String nickName, String custCode, String premCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.SELECT_UPDATED_NICKNAME_RECORD;

        // Replace ? placeholders with actual escaped values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + nickName.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + custCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premCode.replace("'", "''") + "'");

        logQueryInAllure("get nickname for account", loggedQuery);

        // Execute parameterized query safely
        Map<String, Object> result = jdbcTemplate.queryForMap(queryTemplate, nickName, custCode, premCode);

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,          // log expanded SQL
                String.valueOf(result),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getNicknameForAccount2( String custCode, String premCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.SELECT_UPDATED_NICKNAME_RECORD2;

        // Replace ? placeholders with actual escaped values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + custCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premCode.replace("'", "''") + "'");

        logQueryInAllure("get nickname for account", loggedQuery);

        // Execute parameterized query safely
        Map<String, Object> result = jdbcTemplate.queryForMap(queryTemplate, custCode, premCode);

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,          // log expanded SQL
                String.valueOf(result),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getActiveAccountWithoutNickname() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_ACCOUNT_WITHOUT_NICKNAME;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> performDatabaseValidationsTC113(String custCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_TC_113;

        // Replace ? with actual value for logging only
        String loggedQuery = query.replace("?", "'" + custCode + "'");

        logQueryInAllure("get customer code", loggedQuery);

        Map<String, Object> result = jdbcTemplate.queryForMap(query, custCode);

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> performDatabaseValidationsTC116(String custCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_TC_116;

        // Replace ? with actual value for logging only
        String loggedQuery = query.replace("?", "'" + custCode + "'");

        logQueryInAllure("get customer code", loggedQuery);

        Map<String, Object> result = jdbcTemplate.queryForMap(query, custCode);

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> performDatabaseValidations1(String custCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_TC_1;

        // Replace ? with actual value for logging only
        String loggedQuery = query.replace("?", "'" + custCode + "'");

        logQueryInAllure("get customer code", loggedQuery);

        Map<String, Object> result = jdbcTemplate.queryForMap(query, custCode);

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> performDatabaseValidations2(String custCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_TC_2;

        // Replace ? with actual value for logging only
        String loggedQuery = query.replace("?", "'" + custCode + "'");

        logQueryInAllure("get customer code", loggedQuery);

        Map<String, Object> result = jdbcTemplate.queryForMap(query, custCode);

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> performDatabaseValidationsTC119(String custCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_TC_119;

        // Replace ? with actual value for logging only
        String loggedQuery = query.replace("?", "'" + custCode + "'");

        logQueryInAllure("get customer code", loggedQuery);

        Map<String, Object> result;

        try {
            result = jdbcTemplate.queryForMap(query, custCode);
        } catch (EmptyResultDataAccessException e) {
            // Do not fail — return empty map instead
            result = Collections.emptyMap();
        }

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,
                result.toString(),
                elapsed
        );

        return result;
    }



    public Map<String, Object> performDatabaseValidationsTC119_2(String custCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_TC_119_2;

        // Replace ? with actual value for logging only
        String loggedQuery = query.replace("?", "'" + custCode + "'");

        logQueryInAllure("get customer code", loggedQuery);

        Map<String, Object> result = jdbcTemplate.queryForMap(query, custCode);

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getAccountWithoutAddress() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITHOUT_ADDRESS;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetails() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC150() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC150;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC151() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC151;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC152() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC152;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC153() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC153;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC154() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC154;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC155() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC155;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC156() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC156;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC157() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC157;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC160() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC160;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC161() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC161;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC162() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC162;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC163() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC163;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC164() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC164;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC165() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC165;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC166() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC166;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getAccountDetailsTC167() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC167;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getAccountDetailsTC168() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC168;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC169() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC169;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC170() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC170;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC171() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC171;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC172() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC172;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC173() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC173;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC174() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC174;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC175() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC175;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC176() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC176;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getCustomerWithLastNameAndSSN() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_LAST_NAME_AND_SSN;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public int performRollbackForUpdateUsername(String accountNumber) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.DELETE_REGISTERED_ACCOUNT;
        logQueryInAllure("delete registered account", query);

        int rowsAffected = jdbcTemplate.update(query, accountNumber);

        long elapsed = System.currentTimeMillis() - startTime;

        return rowsAffected;
    }


    public Map<String, Object> getRequiredAccountDetails() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_REQUIRED;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getLastnameForCustomerCode(String customerCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_LAST_NAME_FOR_CUST_CODE;

        String loggedQuery = query.replaceFirst(
                "\\?",
                "'" + customerCode.replace("'", "''") + "'"
        );

        logQueryInAllure("get customer code", loggedQuery);

        Map<String, Object> result = jdbcTemplate.queryForMap(query, customerCode);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getPhoneNumberFromDB() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_PHONE_NUMBER_FROM_DB;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> countOfRecords() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_COUNT_OF_RECORDS;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountNumber() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_NUMBER;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getEmailAddressFromCustCode(String customerCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_EMAIL_FOR_CUST_CODE;

        String loggedQuery = query.replaceFirst(
                "\\?",
                "'" + customerCode.replace("'", "''") + "'"
        );

        logQueryInAllure("get customer code", loggedQuery);

        Map<String, Object> result = jdbcTemplate.queryForMap(query, customerCode);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC177() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC177;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC178() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC178;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC179() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC179;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC180() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC180;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC181() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC181;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC182() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC182;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC183() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC183;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC184() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC184;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC185() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC185;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC186() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC186;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC187() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC187;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getAccountDetailsTC158() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC158;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountDetailsTC159() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC159;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithAddressSameDay() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_ADDRESS_SAME_DAY;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getAccountWithAddressDifferentDay() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_ADDRESS_DIFFERENT_DAY;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getFinalAccountWithoutNickname() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_FINAL_ACCOUNT_WITHOUT_NICKNAME;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getInactiveAccountWithoutNickname() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_INACTIVE_ACCOUNT_WITHOUT_NICKNAME;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public List<Map<String, Object>> getPhoneNumbers(String premCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_PHONE_NUMBERS;

        // Replace ? with quoted and escaped custCode for logging
        String loggedQuery = queryTemplate.replaceFirst(
                "\\?",
                "'" + premCode.replace("'", "''") + "'"
        );

        // Log expanded SQL
        logQueryInAllure("check account number registered", loggedQuery);

        List<Map<String, Object>> result = null;

        try {
            // Execute query and return list of maps
            result = jdbcTemplate.queryForList(queryTemplate, premCode);
        } catch (EmptyResultDataAccessException e) {
            result = Collections.emptyList();
        }

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,
                String.valueOf(result),
                elapsed
        );

        return result;
    }



    public List<Map<String, Object>> verifyThePredirection() {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_PRE_DIRECTION;

        // Log expanded SQL
        logQueryInAllure("check account number registered", queryTemplate);

        List<Map<String, Object>> result = null;

        try {
            // Execute query and return list of maps
            result = jdbcTemplate.queryForList(queryTemplate);
        } catch (EmptyResultDataAccessException e) {
            result = Collections.emptyList();
        }

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                queryTemplate,
                String.valueOf(result),
                elapsed
        );

        return result;
    }


    public List<Map<String, Object>> verifyTheUnitType() {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_UNIT_TYPE;


        // Log expanded SQL
        logQueryInAllure("check account number registered", queryTemplate);

        List<Map<String, Object>> result = null;

        try {
            // Execute parameterized query (safe)
            result = jdbcTemplate.queryForList(queryTemplate);
        } catch (EmptyResultDataAccessException e) {
            result = null;
        }

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                queryTemplate,          // <-- use expanded query here
                String.valueOf(result),
                elapsed
        );

        return result;
    }

    public Map<String, Object> verifyTheZipCode(String zip) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_ZIP;

        // Replace ? with quoted and escaped custCode for logging
        String loggedQuery = queryTemplate.replaceFirst(
                "\\?",
                "'" + zip.replace("'", "''") + "'"
        );

        // Log expanded SQL
        logQueryInAllure("check account number registered", loggedQuery);

        Map<String, Object> result = null;

        try {
            // Execute parameterized query (safe)
            result = jdbcTemplate.queryForMap(queryTemplate, zip);
        } catch (EmptyResultDataAccessException e) {
            result = null;
        }

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,          // <-- use expanded query here
                String.valueOf(result),
                elapsed
        );

        return result;
    }

    public Map<String, Object> verifyTheZipCodeAncCity(String zip, String city) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_ZIP_AND_CITY;

        // Escape single quotes
        String safeZip = zip.replace("'", "''");
        String safeCity = city.replace("'", "''");

        // Replace first ? with zip
        String loggedQuery = queryTemplate.replaceFirst("\\?", "'" + safeZip + "'");

        // Replace second ? with city
        loggedQuery = loggedQuery.replaceFirst("\\?", "'" + safeCity + "'");

        // Log expanded SQL
        logQueryInAllure("check account number registered", loggedQuery);

        Map<String, Object> result = null;

        try {
            // Execute parameterized query (safe)
            result = jdbcTemplate.queryForMap(queryTemplate, zip, city);
        } catch (EmptyResultDataAccessException e) {
            result = null;
        }

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,          // expanded SQL with both parameters
                String.valueOf(result),
                elapsed
        );

        return result;
    }



    public List<Map<String, Object>> verifyTheStreetSuffix() {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_STREET_SUFFIX;



        // Log expanded SQL
        logQueryInAllure("check account number registered", queryTemplate);

        List<Map<String, Object>> result = null;

        try {
            // Execute parameterized query (safe)
            result = jdbcTemplate.queryForList(queryTemplate);
        } catch (EmptyResultDataAccessException e) {
            result = null;
        }

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                queryTemplate,          // <-- use expanded query here
                String.valueOf(result),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getUserAccountInfoNew(String accountNo) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_USER_ACCOUNT_INFO_NEW;

        // Replace ? with quoted and escaped custCode for logging
        String loggedQuery = queryTemplate.replaceFirst(
                "\\?",
                "'" + accountNo.replace("'", "''") + "'"
        );

        // Log expanded SQL
        logQueryInAllure("check account number registered", loggedQuery);

        Map<String, Object> result = null;

        try {
            // Execute parameterized query (safe)
            result = jdbcTemplate.queryForMap(queryTemplate, accountNo);
        } catch (EmptyResultDataAccessException e) {
            result = null;
        }

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,          // <-- use expanded query here
                String.valueOf(result),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getUserAccountInfoNewInactive(String accountNo) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_USER_ACCOUNT_INFO_NEW_INACTIVE;

        // Replace ? with quoted and escaped custCode for logging
        String loggedQuery = queryTemplate.replaceFirst(
                "\\?",
                "'" + accountNo.replace("'", "''") + "'"
        );

        // Log expanded SQL
        logQueryInAllure("check account number registered", loggedQuery);

        Map<String, Object> result = null;

        try {
            // Execute parameterized query (safe)
            result = jdbcTemplate.queryForMap(queryTemplate, accountNo);
        } catch (EmptyResultDataAccessException e) {
            result = null;
        }

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,          // <-- use expanded query here
                String.valueOf(result),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getUserAccountInfo(String custCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_USER_ACCOUNT_INFO;

        // Replace ? with quoted and escaped custCode for logging
        String loggedQuery = queryTemplate.replaceFirst(
                "\\?",
                "'" + custCode.replace("'", "''") + "'"
        );

        // Log expanded SQL
        logQueryInAllure("check account number registered", loggedQuery);

        Map<String, Object> result = null;

        try {
            // Execute parameterized query (safe)
            result = jdbcTemplate.queryForMap(queryTemplate, custCode);
        } catch (EmptyResultDataAccessException e) {
            result = null;
        }

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,          // <-- use expanded query here
                String.valueOf(result),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getRegisteredAccount2(String custCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.CHECK_ACCOUNT_REGISTERED2;

        // Replace ? with quoted and escaped custCode for logging
        String loggedQuery = queryTemplate.replaceFirst(
                "\\?",
                "'" + custCode.replace("'", "''") + "'"
        );


        // Log expanded SQL
        logQueryInAllure("check account number registered", loggedQuery);

        Map<String, Object> result = null;

        try {
            // Execute parameterized query (safe)
            result = jdbcTemplate.queryForMap(queryTemplate, custCode);
        } catch (EmptyResultDataAccessException e) {
            result = null;
        }

        long elapsed = System.currentTimeMillis() - startTime;

//         Log expanded SQL, result, and execution time
//        SimplifiedExtentReportManager.logDatabaseQuery(
//                loggedQuery,          // <-- use expanded query here
//                String.valueOf(result),
//                elapsed
//        );

        return result;
    }

    public Map<String, Object> getRegisteredAccount(String custCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.CHECK_ACCOUNT_REGISTERED;

        // Replace ? with quoted and escaped custCode for logging
        String loggedQuery = queryTemplate.replaceFirst(
                "\\?",
                "'" + custCode.replace("'", "''") + "'"
        );

        // Log expanded SQL
        logQueryInAllure("check account number registered", loggedQuery);

        Map<String, Object> result = null;

        try {
            // Execute parameterized query (safe)
            result = jdbcTemplate.queryForMap(queryTemplate, custCode);
        } catch (EmptyResultDataAccessException e) {
            result = null;
        }

        long elapsed = System.currentTimeMillis() - startTime;

        // Log expanded SQL, result, and execution time
//        SimplifiedExtentReportManager.logDatabaseQuery(
//                loggedQuery,          // <-- use expanded query here
//                String.valueOf(result),
//                elapsed
//        );

        return result;
    }




    public Map<String, Object> getNewStatusAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_NEW_ACCOUNT;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> selectPremisesCode() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_PREMISES_CODE;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> selectCustCode() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_CUST_CODE;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getPremisesCode(String premCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_PREM_CODE;

        logQueryInAllure("get premises code", query);

        Map<String, Object> result;
        try {
            // safer: use queryForList to avoid exception
            List<Map<String, Object>> results = jdbcTemplate.queryForList(query, premCode);
            result = results.isEmpty() ? Collections.emptyMap() : results.get(0);
        } catch (EmptyResultDataAccessException e) {
            // fallback if queryForMap is used internally
            result = Collections.emptyMap();
        }

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }



    public Map<String, Object> getCustomerCode(String custCode) {
        long startTime = System.currentTimeMillis();
        String queryTemplate = DBQuery.SELECT_CUSTOMER_CODE;

        // Escape single quotes inside the value
        String safeValue = custCode.replace("'", "''");

        // Replace the first ? with the actual value for logging
        String loggedQuery = queryTemplate.replaceFirst("\\?", "'" + safeValue + "'");

        // Log expanded SQL in Allure
        logQueryInAllure("get customer code", loggedQuery);

        Map<String, Object> result;
        try {
            // safer: use queryForList to avoid exception
            List<Map<String, Object>> results = jdbcTemplate.queryForList(queryTemplate, custCode);
            result = results.isEmpty() ? Collections.emptyMap() : results.get(0);
        } catch (EmptyResultDataAccessException e) {
            result = Collections.emptyMap();
        }

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,          // expanded SQL
                result.toString(),    // DB result
                elapsed
        );

        return result;
    }


    public Map<String, Object> getPremCode(String premCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_PREMISE_CODE;

        logQueryInAllure("get customer code", query);

        Map<String, Object> result;
        try {
            // safer: use queryForList to avoid exception
            List<Map<String, Object>> results = jdbcTemplate.queryForList(query, premCode);
            result = results.isEmpty() ? Collections.emptyMap() : results.get(0);
        } catch (EmptyResultDataAccessException e) {
            // fallback if queryForMap is used
            result = Collections.emptyMap();
        }

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getDesiredUsername(String username) {
        long startTime = System.currentTimeMillis();
        String queryTemplate = DBQuery.SELECT_DESIRED_USERNAME;

        // Replace ? with quoted and escaped username for logging
        String loggedQuery = queryTemplate.replaceFirst("\\?", "'" + username.replace("'", "''") + "'");

        // Log the fully expanded SQL
        logQueryInAllure("get desired username", loggedQuery);

        // Execute the actual parameterized query
        Map<String, Object> result = jdbcTemplate.queryForMap(queryTemplate, username);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,          // <-- use expanded query here
                result.toString(),
                elapsed
        );

        return result;
    }



    public Map<String, Object> getInactiveUser() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_INACTIVE_USER;

        logQueryInAllure("get active username", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getInactiveUser2() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_INACTIVE_USER2;

        logQueryInAllure("get active username", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getInactiveUsername() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_INACTIVE_USER_NAME;

        logQueryInAllure("get inactive username", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        // Log SQL, result, and execution time
        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
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
        //ExtentReportManager.logInfoToReport("Executing Rollback Query: {}" + formattedQuery);
        log.info("Executing Rollback Query: {}", formattedQuery);
        logQueryInAllure("Rollback Query", formattedQuery);
        int rowsUpdated = jdbcTemplate.update(query, user);
        log.info("Rollback executed for user: {} | Rows affected: {}", user, rowsUpdated);
        //ExtentReportManager.logInfoToReport("Rollback executed for user: {} | Rows affected: {}" +" " +user +" "+ rowsUpdated);
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

    public Map<String, Object> getCustPremCodeResidentialTier1() {
        String query = DBQuery.GET_CUST_PREM_CODE_TIER_1;
        logQueryInAllure("Get Customer code and Prem code for Tier 1", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeResidentialTier1NACN() {
        String query = DBQuery.GET_CUST_PREM_CODE_TIER_1_NACN;
        logQueryInAllure("Get Customer code and Prem code for Tier 1", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeResidentialACNRS() {
        String query = DBQuery.GET_ACN_RS_TC_253;
        logQueryInAllure("Get Customer code and Prem code for Tier 1", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC254() {
        String query = DBQuery.GET_NACN_RS_TC_254;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC255() {
        String query = DBQuery.GET_NACN_RS_TC_255;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC256() {
        String query = DBQuery.GET_NACN_RS_TC_256;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC258() {
        String query = DBQuery.GET_NACN_SR_TC_258;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC259() {
        String query = DBQuery.GET_ACN_RS_TC_259;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC260() {
        String query = DBQuery.GET_NACN_RS_TC_260;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC262() {
        String query = DBQuery.GET_NACN_SR_TC_262;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC263() {
        String query = DBQuery.GET_ACN_RS_TC_263;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC264() {
        String query = DBQuery.GET_NACN_RS_TC_264;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC265() {
        String query = DBQuery.GET_ACN_RS_TC_265;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC266() {
        String query = DBQuery.GET_NACN_RS_TC_266;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC267() {
        String query = DBQuery.GET_NACN_RS_TC_267;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC268() {
        String query = DBQuery.GET_NACN_RS_TC_268;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC269() {
        String query = DBQuery.GET_ACN_RS_TC_269;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC270() {
        String query = DBQuery.GET_NACN_RS_TC_270;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC271() {
        String query = DBQuery.GET_ACN_RS_TC_271;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC273() {
        String query = DBQuery.GET_NACN_RS_TC_273;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC274() {
        String query = DBQuery.GET_ACN_RS_TC_274;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC275() {
        String query = DBQuery.GET_ACN_RS_TC_275;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC276() {
        String query = DBQuery.GET_NACN_RS_TC_276;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC277() {
        String query = DBQuery.GET_NACN_RS_TC_277;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC278() {
        String query = DBQuery.GET_ACN_RS_TC_278;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC279() {
        String query = DBQuery.GET_NACN_RS_TC_279;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC280() {
        String query = DBQuery.GET_NACN_RS_TC_280;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC281() {
        String query = DBQuery.GET_ACN_CM_TC_281;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC282() {
        String query = DBQuery.GET_NACN_CM_TC_282;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC283() {
        String query = DBQuery.GET_NACN_CM_TC_283;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC284() {
        String query = DBQuery.GET_ACN_CM_TC_284;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC285() {
        String query = DBQuery.GET_NACN_RS_TC_285;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC286() {
        String query = DBQuery.GET_NACN_RS_TC_286;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC287() {
        String query = DBQuery.GET_NACN_CM_TC_287;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC288() {
        String query = DBQuery.GET_NACN_CM_TC_288;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC289() {
        String query = DBQuery.GET_NACN_RS_TC_289;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC290() {
        String query = DBQuery.GET_NACN_RS_TC_290;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC291() {
        String query = DBQuery.GET_NACN_RS_TC_291;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC292() {
        String query = DBQuery.GET_NACN_RS_TC_292;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC293() {
        String query = DBQuery.GET_NACN_RS_TC_293;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC294() {
        String query = DBQuery.GET_NACN_RS_TC_294;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC295() {
        String query = DBQuery.GET_NACN_RS_TC_295;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC296() {
        String query = DBQuery.GET_NACN_RS_TC_296;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC297() {
        String query = DBQuery.GET_NACN_RS_TC_297;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC298() {
        String query = DBQuery.GET_ACN_RS_TC_298;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC300() {
        String query = DBQuery.GET_NACN_RS_TC_300;
        logQueryInAllure("Get Customer code and Prem code", query);
        return jdbcTemplate.queryForMap(query);
    }

    public Map<String, Object> getCustPremCodeTC301() {
        String query = DBQuery.GET_NACN_CM_TC_301;
        logQueryInAllure("Get Customer code and Prem code", query);
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
        long startTime = System.currentTimeMillis();
        String template = DBQuery.GET_VALIDATION_PLANS_AND_OFFERS_RESULT;

        // Build the final SQL with the actual controlNum value, properly quoted or NULL
        String expandedSql = template.replace(
                "<controlNumber>",
                controlNum == null
                        ? "NULL"
                        : "'" + controlNum.replace("'", "''") + "'"
        );

        List<Map<String, Object>> result;
        try {
            // Execute the query
            result = jdbcTemplate.queryForList(expandedSql);

            // Calculate execution time
            long elapsed = System.currentTimeMillis() - startTime;

            // Log SQL, results, and timing in ExtentReports
            SimplifiedExtentReportManager.logDatabaseQuery(
                    expandedSql,
                    result.toString(),
                    elapsed
            );
        } catch (Exception e) {
            long elapsed = System.currentTimeMillis() - startTime;

            // Log SQL, error, and timing in ExtentReports
            SimplifiedExtentReportManager.logDatabaseQuery(
                    expandedSql,
                    null,
                    elapsed,
                    false,
                    e.getMessage()
            );
            throw e;
        }

        return result;
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

    public Map<String, Object> getEnrollmentRecordByCustomerCode(String customerCode) {
        String query = DBQuery.GET_ENROLLMENT_RECORD_BY_CUSTOMER_CODE;
        logQueryInAllure("Get enrollment record by customer code", query);
        return jdbcTemplate.queryForMap(query, customerCode);
    }

    public List<Map<String, Object>> getEnrollmentRecordsForMarketerSwitch(String customerLastName) {
        String query = DBQuery.GET_MARKETER_SWITCH_ENROLLMENT_RECORD_BY_CUSTOMER_LAST_NAME;
        logQueryInAllure("Get enrollment record by customer last name", query);
        return jdbcTemplate.queryForList(query, customerLastName);
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
