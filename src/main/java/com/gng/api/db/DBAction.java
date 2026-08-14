package com.gng.api.db;

import com.gng.api.context.ApplicationContext;
import com.gng.api.report.DualReportManager;
import com.gng.api.report.SimplifiedExtentReportManager;
import com.gng.api.util.PaperlessConfirmationTokenUtil;
import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
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

    public Map<String, Object> getAccountInformation() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_INFORMATION;

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

    public Map<String, Object> getAccountInformation2() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_INFORMATION2;

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

    public Map<String, Object> getStoredLoginId(String loginId) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_LOGIN_ID_BY_VALUE;
        String loggedQuery = query.replace("?", "'" + loginId + "'");

        logQueryInAllure("get stored login id", loggedQuery);

        Map<String, Object> result = jdbcTemplate.queryForMap(query, loginId);

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,
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


    public Map<String, Object> getValidUsageHistoryAccountNew() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_VALID_USAGE_HISTORY_ACCOUNT_NEW;

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

    public Map<String, Object> getAccountWithBillHistory() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_VALID_BILL_HISTORY_ACCOUNT;

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

    public List<Map<String, Object>> getBudgetBillingAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_BUDGET_BILLING_ACCOUNT;

        logQueryInAllure("get customer code", query);

        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }


    public Map<String, Object> getAccountWithPaymentHistory() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_VALID_PAYMENT_HISTORY_ACCOUNT;

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

    public Map<String, Object> getAccountWithNoPaymentArrangement() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_NO_PAYMENT_ARRANGEMENT;

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

    public Map<String, Object> getAccountWithInactivePaymentArrangement() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_INACTIVE_PAYMENT_ARRANGEMENT;

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

    public Map<String, Object> getAccountWithActivePaymentArrangement1() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_ACTIVE_PAYMENT_ARRANGEMENT1;

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

    public Map<String, Object> getAccountWithActivePaymentArrangementMore() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_ACTIVE_PAYMENT_ARRANGEMENT_MORE;

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

    public Map<String, Object> getAccountWithActivePaymentArrangementMore2() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_ACTIVE_PAYMENT_ARRANGEMENT_MORE2;

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

    public Map<String, Object> getNewAccountNoPaymentHistory() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_NO_PAYMENT_HISTORY_ACCOUNT;

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

    public Map<String, Object> getActiveAccountNoPaymentHistory() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_NO_PAYMENT_HISTORY_ACCOUNT;

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

    public Map<String, Object> getPaymentHistoryTooOld() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_PAYMENT_HISTORY_TOO_OLD;

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

    public Map<String, Object> getPaymentHistoryEqual() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_PAYMENT_HISTORY_EQUAL;

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

    public Map<String, Object> getPaymentHistoryPostedReversal() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_PAYMENT_HISTORY_POSTED_REVERSAL;

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

    public Map<String, Object> getPaymentHistoryNoReversal() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_PAYMENT_HISTORY_NO_REVERSAL;

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

    public Map<String, Object> getPaymentHistoryPostedPayments() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_PAYMENT_HISTORY_POSTED_PAYMENTS;

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

    public Map<String, Object> getPaymentHistoryPendingPayments() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_PAYMENT_HISTORY_PENDING_PAYMENTS;

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

    public Map<String, Object> getPaymentHistoryPostedAndPendingPayments() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_PAYMENT_HISTORY_POSTED_AND_PENDING_PAYMENTS;

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

    public Map<String, Object> getPaymentHistoryLess() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_PAYMENT_HISTORY_LESS;

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

    public Map<String, Object> getUsageHistoryOld() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_USAGE_HISTORY_OLD;

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



    public Map<String, Object> getUsageHitstoryEqualToMonthsRequested() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_USAGE_HISTORY_EQUAL;

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

    public Map<String, Object> getUsageHistoryLess() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_USAGE_HISTORY_LESS;

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

    public Map<String, Object> getUsageHistoryGreater() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_USAGE_HISTORY_GREATER;

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


    public Map<String, Object> getUsageHistoryOne() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_BILL_HISTORY_ONE;

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

    public Map<String, Object> getUsageHistoryMoreThanOne() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_BILL_HISTORY_MORE_THAN_ONE;

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

    public Map<String, Object> getActivePaperlessEligibleAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_PAPERLESS_ELIGIBLE;

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

    public Map<String, Object> getActivePaperlessEligibleAccountWithNoToken() {
        return getActivePaperlessEligibleAccountWithNoTokenExcluding(null, null);
    }

    /**
     * Strict account selection for first-time bill enrollment (TC_79, TC_81, TC_89).
     * Does not fall back to queries that may return accounts with pending OCSEPCI tokens.
     */
    public List<Map<String, Object>> listFreshBillEnrollmentCandidates() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_TC_89_ACTIVE_NO_VALID_TOKEN_CANDIDATES;
        logQueryInAllure("list fresh bill enrollment candidates", query);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        logPaperlessDbResult(query, Map.of("candidateCount", result.size()), startTime);
        return result;
    }

    public Map<String, Object> getActiveAccountForFreshBillEnrollmentStrict() {
        return getActiveAccountForFreshBillEnrollmentStrictExcluding(null, null);
    }

    public Map<String, Object> getActiveAccountForFreshBillEnrollmentStrictExcluding(
            String excludeCustomerCode, String excludePremisesCode) {
        long startTime = System.currentTimeMillis();
        boolean hasExclusion = excludeCustomerCode != null && excludePremisesCode != null;

        if (hasExclusion) {
            String excludeQuery = DBQuery.SELECT_TC_89_ACTIVE_NO_VALID_TOKEN_EXCLUDING_ACCOUNT;
            logQueryInAllure("get fresh bill enrollment account excluding "
                    + excludeCustomerCode + "/" + excludePremisesCode, excludeQuery);
            try {
                Map<String, Object> result = jdbcTemplate.queryForMap(
                        excludeQuery, excludeCustomerCode, excludePremisesCode);
                logPaperlessDbResult(excludeQuery, result, startTime);
                return result;
            } catch (EmptyResultDataAccessException ex) {
                log.warn("No alternate fresh enrollment account after excluding {}/{}",
                        excludeCustomerCode, excludePremisesCode);
            }
        }

        String query = DBQuery.SELECT_TC_89_ACTIVE_NO_VALID_TOKEN;
        logQueryInAllure("get fresh bill enrollment account (strict, no OCSEPCI token)", query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalStateException(
                    "No ACTIVE account without a valid unused OCSEPCI token. "
                            + "UCRACCT may still show bill=P while OCSEPCI holds a pending enrollment "
                            + "(billDeliveryOptionStatus will be NO_CHANGE, not INITIATED). "
                            + "Confirm or expire pending OCSEPCI rows, or use another customer/premises.");
        }
    }

    public Map<String, Object> getActivePaperlessEligibleAccountWithNoTokenExcluding(
            String excludeCustomerCode, String excludePremisesCode) {
        long startTime = System.currentTimeMillis();
        boolean hasExclusion = excludeCustomerCode != null && excludePremisesCode != null;

        if (hasExclusion) {
            String excludeQuery = DBQuery.SELECT_TC_89_ACTIVE_NO_VALID_TOKEN_EXCLUDING_ACCOUNT;
            logQueryInAllure("get active account with no valid token excluding "
                    + excludeCustomerCode + "/" + excludePremisesCode, excludeQuery);
            try {
                Map<String, Object> result = jdbcTemplate.queryForMap(
                        excludeQuery, excludeCustomerCode, excludePremisesCode);
                logPaperlessDbResult(excludeQuery, result, startTime);
                return result;
            } catch (EmptyResultDataAccessException ex) {
                log.warn("No alternate account after excluding {}/{}; trying default no-token query",
                        excludeCustomerCode, excludePremisesCode);
            }
        }

        String query = DBQuery.SELECT_TC_89_ACTIVE_NO_VALID_TOKEN;
        logQueryInAllure("get active account with no valid unused confirmation token", query);

        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            log.warn("No account for strict no-token query; falling back to paperless-eligible query");
            query = DBQuery.SELECT_ACTIVE_PAPERLESS_ELIGIBLE;
            logQueryInAllure("get active paperless eligible account (fallback)", query);
            try {
                Map<String, Object> result = jdbcTemplate.queryForMap(query);
                logPaperlessDbResult(query, result, startTime);
                return result;
            } catch (EmptyResultDataAccessException ex2) {
                log.warn("Paperless-eligible query returned no rows; falling back to TC_79 account query");
                query = DBQuery.SELECT_TC_79_ACTIVE_ACCOUNT;
                logQueryInAllure("get active account for TC_79 (fallback)", query);
                Map<String, Object> result = jdbcTemplate.queryForMap(query);
                logPaperlessDbResult(query, result, startTime);
                return result;
            }
        }
    }

    private void logPaperlessDbResult(String query, Map<String, Object> result, long startTime) {
        long elapsed = System.currentTimeMillis() - startTime;
        SimplifiedExtentReportManager.logDatabaseQuery(query, result.toString(), elapsed);
    }

    public Map<String, Object> getActiveAccountForTc90() {
        long startTime = System.currentTimeMillis();
        String strictQuery = DBQuery.SELECT_TC_90_ACTIVE_WITH_VALID_TOKEN;
        logQueryInAllure("get ACTIVE account for TC_90 (valid unused confirmation token)", strictQuery);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(strictQuery);
            logPaperlessDbResult(strictQuery, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            String relaxedQuery = DBQuery.SELECT_TC_90_ACTIVE_WITH_VALID_TOKEN_RELAXED;
            logQueryInAllure("get ACTIVE account for TC_90/103 (valid token, relaxed email match)", relaxedQuery);
            try {
                Map<String, Object> result = jdbcTemplate.queryForMap(relaxedQuery);
                logPaperlessDbResult(relaxedQuery, result, startTime);
                return result;
            } catch (EmptyResultDataAccessException relaxedEx) {
                throw new IllegalStateException(
                        "No ACTIVE account for TC_90/103 with a non-expired unused confirmation token. "
                                + "Run TC_89 first in the same suite to create a token, or prior enroll will run.");
            }
        }
    }

    public Map<String, Object> getActivePaperlessEligibleAccountWithValidToken() {
        return getActiveAccountForTc90();
    }

    public String getLatestValidTokenIdentifier(String customerCode, String premisesCode) {
        String query = DBQuery.SELECT_CONFIRM_LATEST_VALID_TOKEN;
        logQueryInAllure("get latest valid unused token for " + customerCode + "/" + premisesCode, query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query, customerCode, premisesCode);
            Object tokenId = result.get("tokenIdentifier");
            if (tokenId == null) {
                throw new IllegalStateException(
                        "Latest valid token query returned no tokenIdentifier for "
                                + customerCode + "/" + premisesCode);
            }
            return tokenId.toString();
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalStateException(
                    "No valid unused confirmation token in OCSEPCI for "
                            + customerCode + "/" + premisesCode);
        }
    }

    public String tryGetLatestValidTokenIdentifier(String customerCode, String premisesCode) {
        return com.gng.api.util.PaperlessConfirmationTokenUtil.tryGetLatestTokenIdentifier(customerCode, premisesCode);
    }

    /**
     * Single-shot OCSEPCI probe without logging failed lookups (keeps UAT client reports clean).
     * Logs to the report only when a token row is found.
     */
    public String tryGetLatestValidTokenIdentifierQuiet(String customerCode, String premisesCode) {
        String custAdvToken = com.gng.api.util.PaperlessConfirmationTokenUtil
                .tryGetLatestTokenIdentifierFromCustAdvQuiet(customerCode, premisesCode);
        if (custAdvToken != null) {
            return custAdvToken;
        }
        return tryGetLatestOcsepciTokenIdentifierQuiet(customerCode, premisesCode);
    }

    /**
     * OCSEPCI-only token lookup (no custadv); used as fallback after MariaDB custadv query.
     */
    public String tryGetLatestOcsepciTokenIdentifierQuiet(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();
        String strictQuery = DBQuery.SELECT_CONFIRM_LATEST_VALID_TOKEN;
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(strictQuery, customerCode, premisesCode);
            Object tokenId = result.get("tokenIdentifier");
            if (tokenId != null) {
                logPaperlessDbResult(strictQuery, result, startTime);
                return tokenId.toString();
            }
        } catch (EmptyResultDataAccessException ignored) {
            // UAT often has no visible OCSEPCI row; skip noisy failure logging
        }

        String accountQuery = DBQuery.SELECT_LATEST_UNUSED_OCSEPCI_TOKEN_FOR_ACCOUNT;
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(accountQuery, customerCode, premisesCode);
            Object tokenId = result.get("tokenIdentifier");
            if (tokenId != null) {
                logPaperlessDbResult(accountQuery, result, startTime);
                return tokenId.toString();
            }
        } catch (EmptyResultDataAccessException ignored) {
            // fall through
        }

        String customerQuery = DBQuery.SELECT_LATEST_UNUSED_OCSEPCI_TOKEN_FOR_CUSTOMER;
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(customerQuery, customerCode);
            Object tokenId = result.get("tokenIdentifier");
            if (tokenId != null) {
                logPaperlessDbResult(customerQuery, result, startTime);
                return tokenId.toString();
            }
        } catch (EmptyResultDataAccessException ignored) {
            return null;
        }
        return null;
    }

    private String tryGetLatestUnusedOcsepciTokenIdentifier(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();
        String accountQuery = DBQuery.SELECT_LATEST_UNUSED_OCSEPCI_TOKEN_FOR_ACCOUNT;
        logQueryInAllure("try latest unused OCSEPCI token for " + customerCode + "/" + premisesCode, accountQuery);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(accountQuery, customerCode, premisesCode);
            logPaperlessDbResult(accountQuery, result, startTime);
            Object tokenId = result.get("tokenIdentifier");
            return tokenId != null ? tokenId.toString() : null;
        } catch (EmptyResultDataAccessException ex) {
            String customerQuery = DBQuery.SELECT_LATEST_UNUSED_OCSEPCI_TOKEN_FOR_CUSTOMER;
            logQueryInAllure("try latest unused OCSEPCI token for customer " + customerCode, customerQuery);
            try {
                Map<String, Object> result = jdbcTemplate.queryForMap(customerQuery, customerCode);
                logPaperlessDbResult(customerQuery, result, startTime);
                Object tokenId = result.get("tokenIdentifier");
                return tokenId != null ? tokenId.toString() : null;
            } catch (EmptyResultDataAccessException customerEx) {
                logPaperlessDbResult(customerQuery, Map.of("hasUnusedToken", false), startTime);
                return null;
            }
        }
    }

    public String waitForLatestValidTokenIdentifier(String customerCode,
                                                    String premisesCode,
                                                    int maxAttempts,
                                                    long delayMs) {
        return com.gng.api.util.PaperlessConfirmationTokenUtil.waitForLatestTokenIdentifier(
                customerCode, premisesCode, maxAttempts, delayMs);
    }

    public List<Map<String, Object>> listActiveAccountsForTc79() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_TC_79_ACTIVE_ACCOUNT_CANDIDATES;
        logQueryInAllure("list active accounts for TC_79", query);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        logPaperlessDbResult(query, Map.of("candidateCount", result.size()), startTime);
        return result;
    }

    public Map<String, Object> getActiveAccountForTc79() {
        return getActiveAccountForFreshBillEnrollmentStrict();
    }

    public Map<String, Object> getActiveAccountForTc83() {
        return getActiveAccountForFreshCorrEnrollmentStrict();
    }

    public Map<String, Object> getActiveAccountForFreshCorrEnrollmentStrict() {
        return getActiveAccountForFreshCorrEnrollmentStrictExcluding(null, null);
    }

    public Map<String, Object> getActiveAccountForFreshCorrEnrollmentStrictExcluding(
            String excludeCustomerCode, String excludePremisesCode) {
        long startTime = System.currentTimeMillis();
        boolean hasExclusion = excludeCustomerCode != null && excludePremisesCode != null;

        if (hasExclusion) {
            String excludeQuery = DBQuery.SELECT_TC_83_ACTIVE_CORR_ENROLL_EXCLUDING_ACCOUNT;
            logQueryInAllure("get fresh corr enrollment account excluding "
                    + excludeCustomerCode + "/" + excludePremisesCode, excludeQuery);
            try {
                Map<String, Object> result = jdbcTemplate.queryForMap(
                        excludeQuery, excludeCustomerCode, excludePremisesCode);
                logPaperlessDbResult(excludeQuery, result, startTime);
                return result;
            } catch (EmptyResultDataAccessException ex) {
                log.warn("No alternate fresh corr enrollment account after excluding {}/{}",
                        excludeCustomerCode, excludePremisesCode);
            }
        }

        String query = DBQuery.SELECT_TC_83_ACTIVE_CORR_ENROLL;
        logQueryInAllure("get ACTIVE corr enrollment account for TC_83 (no valid unused token)", query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalStateException(
                    "No ACTIVE account with bill=P, corr=P, and no valid unused OCSEPCI token. "
                            + "Prior corr enrollment runs may have left pending OCSEPCI rows "
                            + "(corrDeliveryOptionStatus will be NO_CHANGE, not INITIATED).");
        }
    }

    public Map<String, Object> getActiveAccountForTc89() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_TC_89_ACTIVE_NO_VALID_TOKEN;
        logQueryInAllure("get ACTIVE bill enrollment account for TC_89 (no valid unused token)", query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalStateException(
                    "No ACTIVE account for TC_89 with bill=P, matching Banner email, "
                            + "and no non-expired unused confirmation token. "
                            + "Run SELECT_TC_89_ACTIVE_NO_VALID_TOKEN in GNGBBAN (UAT1).");
        }
    }

    public Map<String, Object> getActiveAccountForTc87() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_TC_86_ACTIVE_BILL_NO_VALID_TOKEN;
        logQueryInAllure("get ACTIVE bill enrollment account for TC_87 (no valid unused token)", query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalStateException(
                    "No ACTIVE account for TC_87 with bill=P, matching Banner email, "
                            + "and no non-expired unused confirmation token. "
                            + "Run SELECT_TC_86_ACTIVE_BILL_NO_VALID_TOKEN in GNGBBAN (UAT1).");
        }
    }

    public Map<String, Object> getActiveAccountForTc86() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_TC_86_ACTIVE_BILL_NO_VALID_TOKEN;
        logQueryInAllure("get ACTIVE bill enrollment account for TC_86 (no valid unused token)", query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalStateException(
                    "No ACTIVE account for TC_86 with bill=P, matching Banner email, "
                            + "and no non-expired unused confirmation token. "
                            + "Run SELECT_TC_86_ACTIVE_BILL_NO_VALID_TOKEN in GNGBBAN (UAT1).");
        }
    }

    public Map<String, Object> getActiveAccountForTc81() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_TC_81_ACTIVE_BILL_NO_VALID_TOKEN;
        logQueryInAllure("get ACTIVE bill enrollment account for TC_81 (never enrolled, no valid unused token)", query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalStateException(
                    "No ACTIVE account for TC_81 with bill=P, matching Banner email, "
                            + "no OCSEPCI history at account, and no non-expired unused confirmation token. "
                            + "Run SELECT_TC_81_ACTIVE_BILL_NO_VALID_TOKEN in GNGBBAN (UAT1).");
        }
    }

    public Map<String, Object> getActiveAccountForTc84() {
        return getActiveAccountForTc84Excluding(null, null);
    }

    public Map<String, Object> getActiveAccountForTc84Excluding(
            String excludeCustomerCode, String excludePremisesCode) {
        long startTime = System.currentTimeMillis();
        boolean hasExclusion = excludeCustomerCode != null && excludePremisesCode != null;

        if (hasExclusion) {
            String excludeQuery = DBQuery.SELECT_TC_84_ACTIVE_BOTH_CHANNELS_NO_VALID_TOKEN_EXCLUDING;
            logQueryInAllure("get ACTIVE both-channels account for TC_84 excluding "
                    + excludeCustomerCode + "/" + excludePremisesCode, excludeQuery);
            try {
                Map<String, Object> result = jdbcTemplate.queryForMap(
                        excludeQuery, excludeCustomerCode, excludePremisesCode);
                logPaperlessDbResult(excludeQuery, result, startTime);
                return result;
            } catch (EmptyResultDataAccessException ex) {
                log.warn("No alternate TC_84 account after excluding {}/{}",
                        excludeCustomerCode, excludePremisesCode);
            }
        }

        String query = DBQuery.SELECT_TC_84_ACTIVE_BOTH_CHANNELS_NO_VALID_TOKEN;
        logQueryInAllure("get ACTIVE both-channels account for TC_84 (never enrolled, no valid unused token)", query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalStateException(
                    "No ACTIVE account for TC_84 with bill=P, corr=P, matching Banner email, "
                            + "no OCSEPCI history at account, and no non-expired unused confirmation token. "
                            + "Run SELECT_TC_84_ACTIVE_BOTH_CHANNELS_NO_VALID_TOKEN in GNGBBAN (UAT1).");
        }
    }

    public Map<String, Object> getActiveAccountForTc97() {
        long startTime = System.currentTimeMillis();

        String[][] queryChain = {
                {
                        "get active account for TC_97 (both P/P, email, no unused token)",
                        DBQuery.SELECT_TC_97_ACTIVE_BOTH_CHANNELS_ELIGIBLE
                },
                {
                        "get active account for TC_97 fallback (both P/P, email — same as TC_84)",
                        DBQuery.SELECT_TC_83_ACTIVE_CORR_ENROLL
                }
        };

        for (String[] step : queryChain) {
            String label = step[0];
            String query = step[1];
            logQueryInAllure(label, query);
            try {
                Map<String, Object> result = jdbcTemplate.queryForMap(query);
                logPaperlessDbResult(query, result, startTime);
                return result;
            } catch (EmptyResultDataAccessException ex) {
                log.warn("{} returned no rows; trying next TC_97 fallback", label);
            }
        }

        throw new IllegalStateException(
                "No ACTIVE account found for TC_97. Run SELECT_TC_97_ACTIVE_BOTH_CHANNELS_ELIGIBLE in GNGBBAN (UAT1)."
        );
    }

    public List<Map<String, Object>> listActiveBothChannelsEnrollmentCandidates() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_TC_97_ACTIVE_BOTH_CHANNELS_CANDIDATES;
        logQueryInAllure("list active both-channels enrollment candidates", query);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        logPaperlessDbResult(query, Map.of("candidateCount", result.size()), startTime);
        return result;
    }

    public Map<String, Object> getNewAccountForTc98() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_TC_98_NEW_BOTH_CHANNELS_ELIGIBLE;
        logQueryInAllure(
                "get NEW both-channels enrollment account for TC_98 (never enrolled, no valid unused token)",
                query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalStateException(
                    "No NEW account for TC_98 with bill=P, corr=P, matching Banner email, "
                            + "no OCSEPCI history at account, and no non-expired unused confirmation token. "
                            + "Run SELECT_TC_98_NEW_BOTH_CHANNELS_ELIGIBLE in GNGBBAN (UAT1).");
        }
    }

    public Map<String, Object> getNewAccountWithEnrolledPendingBillPref() {
        long startTime = System.currentTimeMillis();

        String[][] queryChain = {
                {
                        "get NEW account for TC_100 (pending bill enrollment, banner still paper)",
                        DBQuery.SELECT_TC_100_NEW_PENDING_BILL_ENROLLMENT
                },
                {
                        "get NEW account for TC_100 (valid bill enrollment token, same as TC_90 for NEW)",
                        DBQuery.SELECT_TC_100_NEW_WITH_VALID_BILL_TOKEN
                },
                {
                        "get NEW account for TC_100 fallback (pending bill enrollment, simple)",
                        DBQuery.SELECT_TC_100_NEW_PENDING_BILL_ENROLLMENT_SIMPLE
                }
        };

        for (String[] step : queryChain) {
            String label = step[0];
            String query = step[1];
            logQueryInAllure(label, query);
            try {
                Map<String, Object> result = jdbcTemplate.queryForMap(query);
                logPaperlessDbResult(query, result, startTime);
                return result;
            } catch (EmptyResultDataAccessException ex) {
                log.warn("{} returned no rows; trying next TC_100 fallback", label);
            }
        }

        throw new IllegalStateException(
                "No NEW account found for TC_100. Run SELECT_TC_100_NEW_PENDING_BILL_ENROLLMENT in GNGBBAN (UAT1)."
        );
    }

    public Map<String, Object> getNewAccountForTc104() {
        long startTime = System.currentTimeMillis();
        String strictQuery = DBQuery.SELECT_TC_100_NEW_WITH_VALID_BILL_TOKEN;
        logQueryInAllure("get NEW account for TC_104 (valid unused bill confirmation token)", strictQuery);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(strictQuery);
            logPaperlessDbResult(strictQuery, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            String relaxedQuery = DBQuery.SELECT_TC_104_NEW_WITH_VALID_TOKEN_RELAXED;
            logQueryInAllure("get NEW account for TC_104 (valid bill token, relaxed email match)", relaxedQuery);
            try {
                Map<String, Object> result = jdbcTemplate.queryForMap(relaxedQuery);
                logPaperlessDbResult(relaxedQuery, result, startTime);
                return result;
            } catch (EmptyResultDataAccessException relaxedEx) {
                throw new IllegalStateException(
                        "No NEW account for TC_104 with a non-expired unused confirmation token. "
                                + "Run TC_96 first in the same suite to create a token, or prior enroll will run.");
            }
        }
    }

    public Map<String, Object> getNewPaperlessEligibleAccountWithValidToken() {
        return getNewAccountForTc104();
    }

    public List<Map<String, Object>> listNewBothChannelsEnrollmentCandidates() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_TC_80_NEW_NEVER_ENROLLED_CANDIDATES;
        logQueryInAllure("list NEW never-enrolled both-channels candidates for TC_80", query);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        logPaperlessDbResult(query, Map.of("candidateCount", result.size()), startTime);
        return result;
    }

    /**
     * Strict account selection for first-time NEW both-channels enrollment (TC_80, TC_98).
     */
    public Map<String, Object> getNewAccountForBothChannelsEnrollmentStrict() {
        return getNewAccountForBothChannelsEnrollmentStrictExcluding(null, null);
    }

    public Map<String, Object> getNewAccountForBothChannelsEnrollmentStrictExcluding(
            String excludeCustomerCode, String excludePremisesCode) {
        long startTime = System.currentTimeMillis();
        boolean hasExclusion = excludeCustomerCode != null && excludePremisesCode != null;

        if (hasExclusion) {
            String excludeQuery = DBQuery.SELECT_TC_80_NEW_NEVER_ENROLLED_EXCLUDING_ACCOUNT;
            logQueryInAllure("get NEW both-channels enrollment account excluding "
                    + excludeCustomerCode + "/" + excludePremisesCode, excludeQuery);
            try {
                Map<String, Object> result = jdbcTemplate.queryForMap(
                        excludeQuery, excludeCustomerCode, excludePremisesCode);
                logPaperlessDbResult(excludeQuery, result, startTime);
                return result;
            } catch (EmptyResultDataAccessException ex) {
                log.warn("No alternate NEW both-channels enrollment account after excluding {}/{}",
                        excludeCustomerCode, excludePremisesCode);
            }
        }

        String query = DBQuery.SELECT_TC_80_NEW_NEVER_ENROLLED_STRICT;
        logQueryInAllure("get NEW both-channels enrollment account (strict, never enrolled)", query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalStateException(
                    "No NEW account for both-channels first-time enrollment. "
                            + "UCRACCT may still show bill/corr=P while OCSEPCI holds a pending enrollment "
                            + "(billDeliveryOptionStatus will be NO_CHANGE, not INITIATED). "
                            + "Confirm or expire pending OCSEPCI rows, or use another customer/premises.");
        }
    }

    public boolean hasAnyOcsepciEnrollmentRecord(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ANY_OCSEPCI_FOR_ACCOUNT;
        logQueryInAllure("check any OCSEPCI enrollment history for " + customerCode + "/" + premisesCode, query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query, customerCode, premisesCode);
            logPaperlessDbResult(query, result, startTime);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            logPaperlessDbResult(query, Map.of("hasOcsepciHistory", false), startTime);
            return false;
        }
    }

    public boolean hasValidUnusedConfirmationToken(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_CONFIRM_LATEST_VALID_TOKEN;
        logQueryInAllure("check valid unused confirmation token for " + customerCode + "/" + premisesCode, query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query, customerCode, premisesCode);
            logPaperlessDbResult(query, result, startTime);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            logPaperlessDbResult(query, Map.of("hasValidToken", false), startTime);
            return false;
        }
    }

    public boolean hasValidUnusedConfirmationTokenForCustomer(String customerCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_VALID_UNUSED_OCSEPCI_FOR_CUSTOMER;
        logQueryInAllure("check valid unused confirmation token for customer " + customerCode, query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query, customerCode);
            logPaperlessDbResult(query, result, startTime);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            logPaperlessDbResult(query, Map.of("hasValidToken", false), startTime);
            return false;
        }
    }

    public Map<String, Object> getNewAccountForTc80() {
        return getNewAccountForBothChannelsEnrollmentStrict();
    }

    public Map<String, Object> getActiveAccountEnrolledInPaperlessBill() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ENROLLED_PAPERLESS;

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

    public Map<String, Object> getActiveAccountEnrolledInPaperlessCorr() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_TC_85_ACTIVE_CORR_ENROLLED;

        logQueryInAllure("get active account enrolled in paperless correspondence for TC_85", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getActiveAccountForTc94() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_TC_94_ACTIVE_BOTH_CHANNELS_ENROLLED;
        logQueryInAllure("get ACTIVE E/E enrolled account for TC_94 (both channels paperless)", query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            log.warn("TC_94 strict query returned no rows; falling back to simple both-channels-enrolled query");
            query = DBQuery.SELECT_TC_94_ACTIVE_BOTH_CHANNELS_ENROLLED_SIMPLE;
            logQueryInAllure("get ACTIVE E/E enrolled account for TC_94 (fallback)", query);
            try {
                Map<String, Object> result = jdbcTemplate.queryForMap(query);
                logPaperlessDbResult(query, result, startTime);
                return result;
            } catch (EmptyResultDataAccessException ex2) {
                throw new IllegalStateException(
                        "No ACTIVE account with bill=E and corr=E for TC_94. "
                                + "Run SELECT_TC_94_ACTIVE_BOTH_CHANNELS_ENROLLED in GNGBBAN (UAT1).");
            }
        }
    }

    public Map<String, Object> getActiveAccountEnrolledInPaperlessBillAndCorr() {
        return getActiveAccountForTc94();
    }

    public Map<String, Object> getNewPaperlessEligibleAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_NEW_PAPERLESS_ELIGIBLE;

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

    public Map<String, Object> getActiveAccountForTc96() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_TC_96_NEW_NO_VALID_TOKEN;
        logQueryInAllure("get NEW bill enrollment account for TC_96 (never enrolled, no valid unused token)", query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalStateException(
                    "No NEW account for TC_96 with bill=P, matching Banner email, "
                            + "no OCSEPCI history at account, and no non-expired unused confirmation token. "
                            + "Run SELECT_TC_96_NEW_NO_VALID_TOKEN in GNGBBAN (UAT1).");
        }
    }

    public Map<String, Object> getNewPaperlessEligibleAccountWithNoToken() {
        long startTime = System.currentTimeMillis();
        try {
            return getActiveAccountForTc96();
        } catch (IllegalStateException ex) {
            log.warn("TC_96 strict query returned no rows; falling back to new paperless-eligible query");
            String query = DBQuery.SELECT_NEW_PAPERLESS_ELIGIBLE;
            logQueryInAllure("get new paperless eligible account (fallback)", query);
            try {
                Map<String, Object> result = jdbcTemplate.queryForMap(query);
                logPaperlessDbResult(query, result, startTime);
                return result;
            } catch (EmptyResultDataAccessException ex2) {
                log.warn("New paperless-eligible query returned no rows; falling back to TC_80 NEW account query");
                query = DBQuery.SELECT_TC_80_NEW_ACCOUNT;
                logQueryInAllure("get NEW account for TC_80 (fallback)", query);
                Map<String, Object> result = jdbcTemplate.queryForMap(query);
                logPaperlessDbResult(query, result, startTime);
                return result;
            }
        }
    }

    /** TC_73: one channel ineligible for enrollment, other eligible, with Banner email on file. */
    public Map<String, Object> getOneChannelIneligibleAccount() {
        try {
            return querySinglePaperlessAccount(
                    DBQuery.SELECT_ONE_CHANNEL_INELIGIBLE,
                    "get one channel ineligible account (corr ineligible, bill eligible)");
        } catch (IllegalStateException ex) {
            return querySinglePaperlessAccount(
                    DBQuery.SELECT_ONE_CHANNEL_INELIGIBLE_BILL,
                    "get one channel ineligible account (bill ineligible, corr eligible)");
        }
    }

    public Map<String, Object> getFiservBillAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_FISERV_BILL_ACCOUNT;

        logQueryInAllure("get Fiserv bill account", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    /**
     * TC_69: bill enrollment ineligible for a non-Fiserv reason.
     * UAT treats NULL bill preference as paper (P) and allows enrollment — use already-enrolled
     * paperless bill (E) so Update returns 40287 without creating a confirmation token.
     */
    public Map<String, Object> getBillEnrollmentIneligibleAccount() {
        return getActiveAccountEnrolledInPaperlessBill();
    }

    /**
     * TC_71: correspondence enrollment ineligible.
     * UAT treats NULL corr preference as paper (P) — use already-enrolled paperless corr (E)
     * so Update returns 40289 without side effects.
     */
    public Map<String, Object> getCorrEnrollmentIneligibleAccount() {
        return getActiveAccountEnrolledInPaperlessCorr();
    }

    /** TC_72: both channels ineligible (no active Banner email on file). */
    public Map<String, Object> getBothChannelsIneligibleAccount() {
        return getActiveAccountWithNoBannerEmail();
    }

    /** TC_76: bill channel only in Initiated pending state (not fully enrolled). */
    public Map<String, Object> getAccountInInitiatedPaperlessState() {
        return getActiveAccountWithPendingBillEnrollment();
    }

    /** TC_76 bootstrap: returns null when no pending bill enrollment exists in UAT1. */
    public Map<String, Object> tryGetAccountInInitiatedPaperlessState() {
        try {
            return getActiveAccountWithPendingBillEnrollment();
        } catch (IllegalStateException ex) {
            return null;
        }
    }

    /** TC_77: ACTIVE account eligible except confirmation email must fail at SMTP. */
    public Map<String, Object> getActivePaperlessEligibleAccountWithEmailFailure() {
        return getActiveAccountForFreshBillEnrollmentStrict();
    }

    /** TC_78: NEW account eligible except confirmation email must fail at SMTP. */
    public Map<String, Object> getNewPaperlessEligibleAccountWithEmailFailure() {
        return getNewPaperlessEligibleAccountWithNoToken();
    }

    public Map<String, Object> getActiveAccountWithNoBannerEmail() {
        return querySinglePaperlessAccount(
                DBQuery.SELECT_ACTIVE_ACCOUNT_NO_BANNER_EMAIL,
                "get ACTIVE account with no active Banner email");
    }

    public Map<String, Object> getActiveAccountWithNullCorrPreference() {
        return querySinglePaperlessAccount(
                DBQuery.SELECT_ACTIVE_ACCOUNT_NULL_CORR_PREFERENCE,
                "get ACTIVE account with NULL correspondence preference");
    }

    public Map<String, Object> getActiveAccountWithNullBillPreference() {
        return querySinglePaperlessAccount(
                DBQuery.SELECT_ACTIVE_ACCOUNT_NULL_BILL_PREFERENCE,
                "get ACTIVE account with NULL bill preference");
    }

    public Map<String, Object> getActiveAccountWithFiservBillDelivery() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_ACCOUNT_FISERV_BILL;
        logQueryInAllure("get ACTIVE account with Fiserv eBill delivery", query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            log.warn("No ACTIVE Fiserv account; falling back to SELECT_FISERV_BILL_ACCOUNT");
            return getFiservBillAccount();
        }
    }

    public Map<String, Object> getActiveAccountWithPaperBillDeliveryAndBannerEmail() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_TC_86_ACTIVE_BILL_NO_VALID_TOKEN;
        logQueryInAllure("get ACTIVE account with paper bill delivery and Banner email", query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalStateException(
                    "No ACTIVE account with bill=P, matching Banner email, "
                            + "and no non-expired unused confirmation token.");
        }
    }

    public Map<String, Object> getActiveAccountWithPendingCorrEnrollment() {
        return querySinglePaperlessAccount(
                DBQuery.SELECT_ACTIVE_ACCOUNT_PENDING_CORR_ENROLLMENT,
                "get ACTIVE account with pending correspondence enrollment");
    }

    public Map<String, Object> tryGetActiveAccountWithPendingCorrEnrollment() {
        return tryQuerySinglePaperlessAccount(
                DBQuery.SELECT_ACTIVE_ACCOUNT_PENDING_CORR_ENROLLMENT,
                "get ACTIVE account with pending correspondence enrollment", false);
    }

    public Map<String, Object> getActiveAccountWithPendingBillEnrollment() {
        return querySinglePaperlessAccount(
                DBQuery.SELECT_ACTIVE_ACCOUNT_PENDING_BILL_ENROLLMENT,
                "get ACTIVE account with pending bill enrollment");
    }

    public Map<String, Object> tryGetActiveAccountWithPendingBillEnrollment() {
        return tryQuerySinglePaperlessAccount(
                DBQuery.SELECT_ACTIVE_ACCOUNT_PENDING_BILL_ENROLLMENT,
                "get ACTIVE account with pending bill enrollment", false);
    }

    public Map<String, Object> getActiveAccountWithPendingCorrAndBannerCorrEnrolled() {
        return querySinglePaperlessAccount(
                DBQuery.SELECT_ACTIVE_ACCOUNT_PENDING_CORR_WITH_BANNER_CORR_ENROLLED,
                "get ACTIVE account with pending correspondence enrollment and Banner corr=E");
    }

    public Map<String, Object> tryGetActiveAccountWithPendingCorrAndBannerCorrEnrolled() {
        return tryQuerySinglePaperlessAccount(
                DBQuery.SELECT_ACTIVE_ACCOUNT_PENDING_CORR_WITH_BANNER_CORR_ENROLLED,
                "get ACTIVE account with pending correspondence enrollment and Banner corr=E", false);
    }

    public Map<String, Object> getActiveAccountWithPendingBillAndFiservBill() {
        return querySinglePaperlessAccount(
                DBQuery.SELECT_ACTIVE_ACCOUNT_PENDING_BILL_WITH_FISERV_BILL,
                "get ACTIVE account with pending bill enrollment and Fiserv bill delivery");
    }

    public Map<String, Object> tryGetActiveAccountWithPendingBillAndFiservBill() {
        return tryQuerySinglePaperlessAccount(
                DBQuery.SELECT_ACTIVE_ACCOUNT_PENDING_BILL_WITH_FISERV_BILL,
                "get ACTIVE account with pending bill enrollment and Fiserv bill delivery", false);
    }

    public Map<String, Object> getActiveAccountWithNoEmailAndPendingBillEnrollment() {
        return querySinglePaperlessAccount(
                DBQuery.SELECT_ACTIVE_ACCOUNT_NO_EMAIL_PENDING_BILL_ENROLLMENT,
                "get ACTIVE account with no Banner email and pending bill enrollment");
    }

    public Map<String, Object> tryGetActiveAccountWithNoEmailAndPendingBillEnrollment() {
        return tryQuerySinglePaperlessAccount(
                DBQuery.SELECT_ACTIVE_ACCOUNT_NO_EMAIL_PENDING_BILL_ENROLLMENT,
                "get ACTIVE account with no Banner email and pending bill enrollment", false);
    }

    public Map<String, Object> tryGetNewAccountWithUnconfirmedBillPreference() {
        String query = DBQuery.SELECT_NEW_ACCOUNT_UNCONFIRMED_BILL_PREFERENCE;
        try {
            return jdbcTemplate.queryForMap(query);
        } catch (EmptyResultDataAccessException ex) {
            return tryGetNewAccountWithEnrolledPendingBillPrefQuiet();
        }
    }

    private Map<String, Object> tryGetNewAccountWithEnrolledPendingBillPrefQuiet() {
        String[][] queryChain = {
                {DBQuery.SELECT_TC_100_NEW_PENDING_BILL_ENROLLMENT},
                {DBQuery.SELECT_TC_100_NEW_PENDING_BILL_ENROLLMENT_SIMPLE}
        };
        for (String[] step : queryChain) {
            try {
                return jdbcTemplate.queryForMap(step[0]);
            } catch (EmptyResultDataAccessException ex) {
                // try next fallback
            }
        }
        return null;
    }

    private Map<String, Object> tryGetNewAccountWithEnrolledPendingBillPref() {
        String[][] queryChain = {
                {
                        "try get NEW account with pending bill enrollment (TC_100 strict)",
                        DBQuery.SELECT_TC_100_NEW_PENDING_BILL_ENROLLMENT
                },
                {
                        "try get NEW account with pending bill enrollment (TC_100 simple)",
                        DBQuery.SELECT_TC_100_NEW_PENDING_BILL_ENROLLMENT_SIMPLE
                }
        };
        for (String[] step : queryChain) {
            String label = step[0];
            String query = step[1];
            logQueryInAllure(label, query);
            try {
                long startTime = System.currentTimeMillis();
                Map<String, Object> result = jdbcTemplate.queryForMap(query);
                logPaperlessDbResult(query, result, startTime);
                return result;
            } catch (EmptyResultDataAccessException ex) {
                log.warn("{} returned no rows", label);
            }
        }
        return null;
    }

    public Map<String, Object> getNewAccountWithUnconfirmedBillPreference() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_NEW_ACCOUNT_UNCONFIRMED_BILL_PREFERENCE;
        logQueryInAllure("get NEW account with unconfirmed electronic bill preference", query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            log.warn("Strict NEW unconfirmed bill query returned no rows; falling back to pending bill enrollment lookup");
            return getNewAccountWithEnrolledPendingBillPref();
        }
    }

    public Map<String, Object> getNewAccountWithNullBillPreference() {
        return querySinglePaperlessAccount(
                DBQuery.SELECT_NEW_ACCOUNT_NULL_BILL_PREFERENCE,
                "get NEW account with NULL bill preference");
    }

    public Map<String, Object> getNewAccountWithPaperBillPreference() {
        return tryQuerySinglePaperlessAccount(
                DBQuery.SELECT_NEW_ACCOUNT_PAPER_BILL_PREFERENCE,
                "get NEW account with paper bill preference P",
                false);
    }

    /**
     * Quiet custadv update for TC_127 account-mismatch bootstrap (not logged to Extent report).
     */
    public void updateCustAdvPaperlessTokenAccountNumberQuiet(long verificationStatusId, String accountNumber) {
        int updated = jdbcTemplate.update(
                DBQuery.UPDATE_CUSTADV_PAPERLESS_TOKEN_ACCOUNT_NUMBER, accountNumber, verificationStatusId);
        if (updated == 0) {
            throw new IllegalStateException(
                    "No custadv paperless token row updated for verification id " + verificationStatusId);
        }
        log.debug("Remapped custadv token row {} to account_number {}", verificationStatusId, accountNumber);
    }

    /**
     * TC_125: delete custadv token row so the issued token identifier no longer matches any stored
     * record (Confirm → 10411 Token Not Found).
     */
    public void deleteCustAdvPaperlessTokenByIdQuiet(long verificationStatusId) {
        int deleted = jdbcTemplate.update(
                DBQuery.DELETE_CUSTADV_PAPERLESS_TOKEN_BY_ID, verificationStatusId);
        if (deleted == 0) {
            throw new IllegalStateException(
                    "No custadv paperless token row deleted for verification id " + verificationStatusId);
        }
        log.debug("Deleted custadv paperless token row {}", verificationStatusId);
    }

    /**
     * TC_125: delete unused custadv row(s) that still contain the issued token hex in email_link.
     */
    public int deleteCustAdvUnusedPaperlessTokenByTokenQuiet(String tokenHex) {
        String normalized = PaperlessConfirmationTokenUtil.normalizeTokenPlaintext(tokenHex);
        if (normalized == null || normalized.isBlank()) {
            throw new IllegalArgumentException("tokenHex required to delete custadv token row");
        }
        int deleted = jdbcTemplate.update(
                DBQuery.DELETE_CUSTADV_UNUSED_PAPERLESS_TOKEN_BY_TOKEN, normalized);
        log.debug("Deleted {} custadv unused row(s) matching token length {}", deleted, normalized.length());
        return deleted;
    }

    /**
     * TC_125: delete all unused paperless custadv rows for the account.
     */
    public int deleteCustAdvUnusedPaperlessTokensForAccountQuiet(String customerCode, String premisesCode) {
        String accountNumber = buildCustAdvAccountNumber(customerCode, premisesCode);
        int deleted = jdbcTemplate.update(
                DBQuery.DELETE_CUSTADV_UNUSED_PAPERLESS_TOKENS_FOR_ACCOUNT, accountNumber);
        log.debug("Deleted {} unused custadv paperless rows for {}", deleted, accountNumber);
        return deleted;
    }

    /**
     * TC_130: clear pending bill/corr channels on an unused custadv token row (token kept).
     */
    public void clearCustAdvPendingChannelsQuiet(long verificationStatusId) {
        int updated = jdbcTemplate.update(
                DBQuery.UPDATE_CUSTADV_CLEAR_PENDING_CHANNELS_FOR_ID, verificationStatusId);
        if (updated == 0) {
            throw new IllegalStateException(
                    "No custadv pending channels cleared for verification id " + verificationStatusId);
        }
        log.debug("Cleared custadv pending channels for verification id {}", verificationStatusId);
    }

    /**
     * TC_130: clear {@code date_time_link_confirmed} so a previously used token is unused again.
     */
    public void clearCustAdvLinkConfirmedQuiet(long verificationStatusId) {
        int updated = jdbcTemplate.update(
                DBQuery.UPDATE_CUSTADV_CLEAR_LINK_CONFIRMED_FOR_ID, verificationStatusId);
        if (updated == 0) {
            throw new IllegalStateException(
                    "No custadv link-confirmed cleared for verification id " + verificationStatusId);
        }
        log.debug("Cleared custadv date_time_link_confirmed for verification id {}", verificationStatusId);
    }

    /**
     * TC_130: demote paperless verification type so the token row remains but is not an active
     * PendingConfirmation PPER (Confirm → 10411).
     */
    public void clearCustAdvPaperlessTypeQuiet(long verificationStatusId) {
        int updated = jdbcTemplate.update(
                DBQuery.UPDATE_CUSTADV_CLEAR_PAPERLESS_TYPE_FOR_ID, verificationStatusId);
        if (updated == 0) {
            throw new IllegalStateException(
                    "No custadv paperless type cleared for verification id " + verificationStatusId);
        }
        log.debug("Cleared custadv paperless type for verification id {}", verificationStatusId);
    }

    /**
     * TC_130: delete incomplete OCSEPCI/PPER rows for the account when present (0 rows is OK on UAT1).
     */
    public int deletePendingOcsepciRowsForAccountQuiet(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.DELETE_OCSEPCI_PENDING_ROWS_FOR_ACCOUNT;
        logQueryInAllure("delete pending OCSEPCI/PPER rows for " + customerCode + "/" + premisesCode, query);
        int deleted = jdbcTemplate.update(query, customerCode, premisesCode);
        logPaperlessDbResult(query, Map.of("rowsDeleted", deleted), startTime);
        return deleted;
    }

    /**
     * TC_125: scramble custadv {@code email_link} so the issued token identifier no longer matches
     * any stored row (Confirm → 10411 Token Not Found).
     */
    public void scrambleCustAdvPaperlessTokenLinkQuiet(long verificationStatusId) {
        int updated = jdbcTemplate.update(
                DBQuery.UPDATE_CUSTADV_SCRAMBLE_EMAIL_LINK_FOR_ID, verificationStatusId);
        if (updated == 0) {
            throw new IllegalStateException(
                    "No custadv paperless token email_link scrambled for verification id " + verificationStatusId);
        }
        log.debug("Scrambled custadv email_link for verification id {}", verificationStatusId);
    }

    /**
     * TC_130: delete incomplete OCSEPCI/PPER rows for the account so Confirm has no active pending PPER
     * (→ 10411) while the unused custadv token remains.
     */
    public void deletePendingOcsepciRowsForAccount(String customerCode, String premisesCode) {
        int deleted = deletePendingOcsepciRowsForAccountQuiet(customerCode, premisesCode);
        if (deleted == 0) {
            throw new IllegalStateException(
                    "No pending OCSEPCI rows deleted for " + customerCode + "/" + premisesCode);
        }
    }

    public List<Map<String, Object>> listActiveAccountsWithPendingBillEnrollment(int maxRows, boolean logToReport) {
        return listPaperlessAccounts(DBQuery.SELECT_LIST_ACTIVE_ACCOUNTS_PENDING_BILL_ENROLLMENT,
                maxRows, logToReport, "list ACTIVE accounts with pending bill enrollment");
    }

    public List<Map<String, Object>> listNewAccountsWithPendingBillEnrollment(int maxRows, boolean logToReport) {
        return listPaperlessAccounts(DBQuery.SELECT_LIST_NEW_ACCOUNTS_PENDING_BILL_ENROLLMENT,
                maxRows, logToReport, "list NEW accounts with pending bill enrollment");
    }

    public List<Map<String, Object>> listNewAccountsWithExpiredUnconfirmedBillEnrollment(int maxRows,
                                                                                         boolean logToReport) {
        return listPaperlessAccounts(DBQuery.SELECT_LIST_NEW_ACCOUNTS_EXPIRED_UNCONFIRMED_BILL,
                maxRows, logToReport, "list NEW accounts with expired unconfirmed bill preference");
    }

    public Map<String, Object> tryGetNewAccountWithExpiredUnconfirmedBillPreference() {
        return tryQuerySinglePaperlessAccount(
                DBQuery.SELECT_NEW_ACCOUNT_EXPIRED_UNCONFIRMED_BILL_PREFERENCE,
                "get NEW account with expired unconfirmed bill preference");
    }

    private List<Map<String, Object>> listPaperlessAccounts(String query,
                                                            int maxRows,
                                                            boolean logToReport,
                                                            String label) {
        long startTime = System.currentTimeMillis();
        if (logToReport) {
            logQueryInAllure(label, query);
        }
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        if (result.size() > maxRows) {
            result = result.subList(0, maxRows);
        }
        if (logToReport) {
            logPaperlessDbResult(query, Map.of("accountCount", result.size()), startTime);
        }
        return result;
    }

    /** Quiet Banner update for TC_129/130 — block paperless bill preference apply on confirm. */
    public void updateAccountBillPresentationTypeQuiet(String customerCode, String premisesCode, String billType) {
        int updated = jdbcTemplate.update(
                DBQuery.UPDATE_UCRACCT_BILL_PRES_TYPE_FOR_ACCOUNT, billType, customerCode, premisesCode);
        if (updated == 0) {
            throw new IllegalStateException(
                    "No UCRACCT bill preference updated for " + customerCode + "/" + premisesCode);
        }
        log.debug("Updated bill presentation type to {} for {}/{}", billType, customerCode, premisesCode);
    }

    /** Quiet Banner update — set correspondence delivery type without API side effects. */
    public void updateAccountCorrDeliveryTypeQuiet(String customerCode, String premisesCode, String corrType) {
        int updated = jdbcTemplate.update(
                DBQuery.UPDATE_UCRACCT_CORR_DEL_TYPE_FOR_ACCOUNT, corrType, customerCode, premisesCode);
        if (updated == 0) {
            throw new IllegalStateException(
                    "No UCRACCT correspondence preference updated for " + customerCode + "/" + premisesCode);
        }
        log.debug("Updated correspondence delivery type to {} for {}/{}", corrType, customerCode, premisesCode);
    }

    /** Quiet Banner update — expire active account emails (simulate no email on file). */
    public void expireActiveBannerEmailsQuiet(String customerCode) {
        int updated = jdbcTemplate.update(DBQuery.UPDATE_EXPIRE_ACTIVE_BANNER_EMAIL_FOR_CUSTOMER, customerCode);
        if (updated == 0) {
            throw new IllegalStateException("No active GZBEMCP email rows expired for customer " + customerCode);
        }
        log.debug("Expired {} active Banner email row(s) for customer {}", updated, customerCode);
    }

    /**
     * TC_149 cleanup: clear expiration on Banner emails expired earlier in the same test
     * ({@link #expireActiveBannerEmailsQuiet(String)}).
     */
    public int unexpireRecentlyExpiredBannerEmailsQuiet(String customerCode) {
        int updated = jdbcTemplate.update(
                DBQuery.UPDATE_UNEXPIRE_RECENTLY_EXPIRED_BANNER_EMAIL_FOR_CUSTOMER, customerCode);
        log.debug("Unexpired {} recently-expired Banner email row(s) for customer {}", updated, customerCode);
        return updated;
    }

    /**
     * TC_147: set Banner {@code UCRACCT_STATUS_IND} (e.g. {@code N} → {@code A}) for an account.
     */
    public void updateUcracctStatusInd(String customerCode, String premisesCode, String statusInd) {
        int updated = jdbcTemplate.update(
                DBQuery.UPDATE_UCRACCT_STATUS_IND_FOR_ACCOUNT, statusInd, customerCode, premisesCode);
        if (updated == 0) {
            throw new IllegalStateException(
                    "No UCRACCT row updated for status " + statusInd + " on " + customerCode + "/" + premisesCode);
        }
        log.debug("Updated UCRACCT_STATUS_IND to {} for {}/{} (rows={})",
                statusInd, customerCode, premisesCode, updated);
    }

    public Map<String, Object> tryLookupUcracctAccount(String customerCode, String premisesCode) {
        try {
            return jdbcTemplate.queryForMap(
                    DBQuery.SELECT_UCRACCT_ACCOUNT_SUMMARY, customerCode, premisesCode);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Banner UCRACCT/UCRADDR/UCBPREM + OCSEPCI confirm-date evidence for VerifyAccount review logs.
     * Tries common cust/prem code formats (as-is, zero-stripped, 7-digit padded) because
     * Preferences MariaDB account_number parsing may not match Banner VARCHAR storage.
     * Returns null when no UCRACCT row exists for the Preferences account.
     */
    public Map<String, Object> tryGetVerifyAccountBannerEvidence(String customerCode, String premisesCode) {
        for (String[] pair : verifyAccountCodeVariants(customerCode, premisesCode)) {
            try {
                Map<String, Object> row = jdbcTemplate.queryForMap(
                        DBQuery.SELECT_VERIFY_ACCOUNT_BANNER_EVIDENCE, pair[0], pair[1]);
                if (row != null) {
                    return row;
                }
            } catch (EmptyResultDataAccessException ignored) {
                // try next code format
            }
        }
        return null;
    }

    /** Cust/prem variants used when joining Preferences account numbers to Banner UCRACCT. */
    public static List<String[]> verifyAccountCodeVariants(String customerCode, String premisesCode) {
        LinkedHashSet<String> custs = new LinkedHashSet<>();
        LinkedHashSet<String> prems = new LinkedHashSet<>();
        addCodeVariants(custs, customerCode);
        addCodeVariants(prems, premisesCode);
        List<String[]> pairs = new ArrayList<>();
        for (String cust : custs) {
            for (String prem : prems) {
                pairs.add(new String[]{cust, prem});
            }
        }
        return pairs;
    }

    private static void addCodeVariants(LinkedHashSet<String> target, String raw) {
        if (raw == null || raw.isBlank()) {
            return;
        }
        String trimmed = raw.trim();
        target.add(trimmed);
        String digits = trimmed.replaceAll("\\D", "");
        if (!digits.isBlank()) {
            target.add(digits);
            String stripped = digits.replaceFirst("^0+(?!$)", "");
            target.add(stripped);
            if (stripped.length() <= 7) {
                target.add(String.format("%7s", stripped).replace(' ', '0'));
            }
        }
    }

    public boolean hasActiveBannerEmail(String customerCode) {
        Integer count = jdbcTemplate.queryForObject(
                """
                SELECT COUNT(1)
                FROM GZBEMCP e
                WHERE e.GZBEMCP_CUST_CODE = ?
                  AND e.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (e.GZBEMCP_EXPIRATION_DATE IS NULL OR e.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
                """,
                Integer.class,
                customerCode);
        return count != null && count > 0;
    }

    public boolean hasUnusedCustAdvPaperlessToken(String customerCode, String premisesCode) {
        return hasUnusedCustAdvPaperlessToken(customerCode, premisesCode, false);
    }

    public boolean hasUnusedCustAdvPaperlessToken(String customerCode, String premisesCode, boolean logToReport) {
        return ApplicationContext.get().getDbAction("mariadb")
                .tryGetLatestConfirmPaperlessTokenFromCustAdv(customerCode, premisesCode, logToReport) != null;
    }

    public Map<String, Object> getNewAccountWithConfirmedBillPreference() {
        return querySinglePaperlessAccount(
                DBQuery.SELECT_NEW_ACCOUNT_CONFIRMED_BILL_PREFERENCE,
                "get NEW account with confirmed electronic bill preference");
    }

    public Map<String, Object> getVerifyAccountWithUcraddrStreet() {
        return querySinglePaperlessAccount(
                DBQuery.SELECT_VERIFY_ACCOUNT_WITH_UCRADDR_STREET,
                "get VerifyAccount account with UCRADDR street fields");
    }

    /**
     * TC_228/229: GTBENRL + GZRPPTH NEW, no UCRACCT, bill vs service address differ.
     */
    public List<Map<String, Object>> listVerifyAccountGtbenrlNoUcraddr(int limit) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_VERIFY_ACCOUNT_GTBENRL_NO_UCRADDR;
        logQueryInAllure("list VerifyAccount GTBENRL candidates (TC_228/229)", query);
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
            if (rows.size() > Math.max(1, limit)) {
                rows = rows.subList(0, limit);
            }
            if (!rows.isEmpty()) {
                Map<String, Object> first = rows.get(0);
                log.info("GTBENRL candidate[0] entries={}", first);
                DualReportManager.logInfo("GTBENRL candidate[0]=" + first);
            }
            long elapsed = System.currentTimeMillis() - startTime;
            SimplifiedExtentReportManager.logDatabaseQuery(
                    query, "gtbenrlCandidates=" + rows.size(), elapsed);
            return rows;
        } catch (EmptyResultDataAccessException ex) {
            return List.of();
        }
    }

    public Map<String, Object> getVerifyAccountGtbenrlNoUcraddr() {
        List<Map<String, Object>> rows = listVerifyAccountGtbenrlNoUcraddr(1);
        if (rows == null || rows.isEmpty()) {
            throw new IllegalStateException(
                    "No GTBENRL account for TC_228/229 "
                            + "(PROC_FLAG='N', BILL_ADDR1, GZRPPTH status='N', no UCRACCT, bill vs service differ)");
        }
        return rows.get(0);
    }

    public List<Map<String, Object>> listVerifyAccountStreetCandidates(int limit) {
        return listVerifyAccountStreetCandidates(limit, true);
    }

    /**
     * @param logToReport when false, probe-only (avoids leaking first-candidate cust/prem into the active TC report)
     */
    public List<Map<String, Object>> listVerifyAccountStreetCandidates(int limit, boolean logToReport) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_VERIFY_ACCOUNT_WITH_UCRADDR_STREET_CANDIDATES;
        if (logToReport) {
            logQueryInAllure("list VerifyAccount street candidates (limit " + limit + ")", query);
        }
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
            if (rows.size() > limit) {
                rows = rows.subList(0, limit);
            }
            if (logToReport) {
                long elapsed = System.currentTimeMillis() - startTime;
                SimplifiedExtentReportManager.logDatabaseQuery(
                        query,
                        "candidateRows=" + rows.size() + " (probe pool only; not the executed VerifyAccount account)",
                        elapsed);
            } else {
                log.info("VerifyAccount Banner street candidates (probe-only, not logged to Extent): {}",
                        rows.size());
            }
            return rows;
        } catch (EmptyResultDataAccessException ex) {
            return List.of();
        }
    }

    /**
     * MariaDB online-registered accounts for Preferences VerifyAccount probing.
     * Call via {@code ApplicationContext.get().getDbAction("mariadb")}.
     */
    public List<Map<String, Object>> listPreferencesRegisteredAccounts(int limit) {
        return listPreferencesRegisteredAccounts(limit, true);
    }

    /**
     * @param logToReport when false, probe-only (keeps MariaDB candidate SQL out of the active TC report)
     */
    public List<Map<String, Object>> listPreferencesRegisteredAccounts(int limit, boolean logToReport) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_PREFERENCES_REGISTERED_ACCOUNTS;
        if (logToReport) {
            logQueryInAllure("list Preferences registered accounts (limit " + limit + ")", query);
        }
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, Math.max(1, limit));
            log.info("Found {} Preferences registered account candidates", rows.size());
            if (logToReport) {
                long elapsed = System.currentTimeMillis() - startTime;
                SimplifiedExtentReportManager.logDatabaseQuery(
                        query, "rows=" + rows.size(), elapsed);
            }
            return rows;
        } catch (EmptyResultDataAccessException ex) {
            return List.of();
        } catch (Exception ex) {
            log.warn("listPreferencesRegisteredAccounts failed: {}", ex.getMessage());
            return List.of();
        }
    }

    /**
     * Parses custadv {@code 00}+7+7 account_number into customerCode / premisesCode.
     */
    public static String[] parseCustAdvAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("accountNumber is blank");
        }
        String digits = accountNumber.replaceAll("\\D", "");
        if (digits.length() < 14) {
            throw new IllegalArgumentException("accountNumber too short: " + accountNumber);
        }
        String cust = String.valueOf(Long.parseLong(digits.substring(digits.length() - 14, digits.length() - 7)));
        String prem = String.valueOf(Long.parseLong(digits.substring(digits.length() - 7)));
        return new String[]{cust, prem};
    }

    public Map<String, Object> getVerifyAccountWithUcraddrPoBox() {
        return querySinglePaperlessAccount(
                DBQuery.SELECT_VERIFY_ACCOUNT_WITH_UCRADDR_PO_BOX,
                "get VerifyAccount account with UCRADDR PO Box");
    }

    public Map<String, Object> getVerifyAccountNewConfirmedBill() {
        // TC_230-231 / TC_234: GZRPPTH NEW + paperless_conf_date
        return querySinglePaperlessAccount(
                DBQuery.SELECT_VERIFY_ACCOUNT_NEW_CONFIRMED_BILL,
                "get VerifyAccount GZRPPTH NEW confirmed bill (TC_230-234)");
    }

    public Map<String, Object> getVerifyAccountNewConfirmedCorr() {
        return querySinglePaperlessAccount(
                DBQuery.SELECT_VERIFY_ACCOUNT_NEW_CONFIRMED_CORR,
                "get VerifyAccount GZRPPTH NEW confirmed corr (TC_232-235)");
    }

    public Map<String, Object> getVerifyAccountNewPendingBill() {
        return querySinglePaperlessAccount(
                DBQuery.SELECT_VERIFY_ACCOUNT_NEW_PENDING_BILL,
                "get VerifyAccount GZRPPTH NEW pending bill / Initiated I (TC_236)");
    }

    public Map<String, Object> getVerifyAccountNewPendingCorr() {
        return querySinglePaperlessAccount(
                DBQuery.SELECT_VERIFY_ACCOUNT_NEW_PENDING_CORR,
                "get VerifyAccount GZRPPTH NEW pending corr / Initiated I (TC_237)");
    }

    public Map<String, Object> getVerifyAccountNewPaperBill() {
        return querySinglePaperlessAccount(
                DBQuery.SELECT_VERIFY_ACCOUNT_NEW_PAPER_BILL,
                "get VerifyAccount GZRPPTH NEW paper bill P (TC_238)");
    }

    public Map<String, Object> getVerifyAccountNewPaperCorr() {
        return querySinglePaperlessAccount(
                DBQuery.SELECT_VERIFY_ACCOUNT_NEW_PAPER_CORR,
                "get VerifyAccount GZRPPTH NEW paper corr P (TC_239)");
    }

    /** TC_240: expired bill (OCSEPCI + GZRPPTH P + GTBENRL, no UCRACCT). */
    public Map<String, Object> getVerifyAccountNewExpiredBill() {
        return querySinglePaperlessAccount(
                DBQuery.SELECT_VERIFY_ACCOUNT_NEW_EXPIRED_BILL,
                "get VerifyAccount GZRPPTH expired bill → P (TC_240)");
    }

    /** TC_241: expired correspondence. */
    public Map<String, Object> getVerifyAccountNewExpiredCorr() {
        return querySinglePaperlessAccount(
                DBQuery.SELECT_VERIFY_ACCOUNT_NEW_EXPIRED_CORR,
                "get VerifyAccount GZRPPTH expired corr → P (TC_241)");
    }

    /** Active Banner GZBEMCP email for customer, or null if none. */
    public String getActiveBannerEmailForCustomer(String customerCode) {
        if (customerCode == null || customerCode.isBlank()) {
            return null;
        }
        try {
            return jdbcTemplate.queryForObject(
                    DBQuery.SELECT_ACTIVE_BANNER_EMAIL_BY_CUST, String.class, customerCode.trim());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /** Any Banner GZBEMCP email for customer (prefers currently effective). */
    public String getAnyBannerEmailForCustomer(String customerCode) {
        if (customerCode == null || customerCode.isBlank()) {
            return null;
        }
        try {
            return jdbcTemplate.queryForObject(
                    DBQuery.SELECT_ANY_BANNER_EMAIL_BY_CUST, String.class, customerCode.trim());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public void enrichBannerEmail(Map<String, Object> account) {
        if (account == null) {
            return;
        }
        Object existing = account.get("bannerEmail");
        if (existing != null && !existing.toString().isBlank()) {
            return;
        }
        Object customerCode = account.get("customerCode");
        if (customerCode == null) {
            for (Map.Entry<String, Object> entry : account.entrySet()) {
                if ("customerCode".equalsIgnoreCase(entry.getKey()) && entry.getValue() != null) {
                    customerCode = entry.getValue();
                    break;
                }
            }
        }
        if (customerCode == null) {
            return;
        }
        String email = getActiveBannerEmailForCustomer(customerCode.toString());
        if (email == null) {
            email = getAnyBannerEmailForCustomer(customerCode.toString());
        }
        if (email != null) {
            account.put("bannerEmail", email);
        }
    }

    private Map<String, Object> querySinglePaperlessAccount(String query, String allureLabel) {
        long startTime = System.currentTimeMillis();
        logQueryInAllure(allureLabel, query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalStateException("No account found for: " + allureLabel);
        }
    }

    private Map<String, Object> tryQuerySinglePaperlessAccount(String query, String allureLabel) {
        return tryQuerySinglePaperlessAccount(query, allureLabel, true);
    }

    private Map<String, Object> tryQuerySinglePaperlessAccount(String query, String allureLabel, boolean logToReport) {
        long startTime = System.currentTimeMillis();
        if (logToReport) {
            logQueryInAllure(allureLabel, query);
        }
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            if (logToReport) {
                logPaperlessDbResult(query, result, startTime);
            }
            return result;
        } catch (EmptyResultDataAccessException ex) {
            if (logToReport) {
                log.warn("No account found for: {}", allureLabel);
            }
            return null;
        }
    }

    public Map<String, Object> getAccountNotEnrolledInPaperless() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_NOT_ENROLLED_IN_PAPERLESS;

        logQueryInAllure("get account not enrolled in paperless", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getLatestValidConfirmationToken(String customerCode, String premisesCode) {
        return com.gng.api.util.PaperlessConfirmationTokenUtil.getLatestTokenData(customerCode, premisesCode);
    }

    /**
     * Builds custadv account_number: {@code 00} + 7-digit customerCode + 7-digit premisesCode
     * (e.g. customer 6003650, premises 5977402 → {@code 0060036505977402}).
     */
    public static String buildCustAdvAccountNumber(String customerCode, String premisesCode) {
        return String.format(
                "00%07d%07d",
                Long.parseLong(customerCode.trim()),
                Long.parseLong(premisesCode.trim()));
    }

    /**
     * Latest paperless confirmation token from MariaDB custadv_email_verification_status.
     * Use via {@code ApplicationContext.get().getDbAction("mariadb")}.
     */
    public Map<String, Object> getLatestConfirmPaperlessTokenFromCustAdv(String customerCode, String premisesCode) {
        return getLatestConfirmPaperlessTokenFromCustAdv(customerCode, premisesCode, true);
    }

    public Map<String, Object> getLatestConfirmPaperlessTokenFromCustAdv(String customerCode,
                                                                  String premisesCode,
                                                                  boolean logToReport) {
        long startTime = System.currentTimeMillis();
        String accountNumber = buildCustAdvAccountNumber(customerCode, premisesCode);
        String query = DBQuery.SELECT_CUSTADV_LATEST_PAPERLESS_EMAIL_VERIFICATION_TOKEN;

        if (logToReport) {
            logQueryInAllure("get latest paperless confirm token from custadv for account " + accountNumber, query);
        }

        Map<String, Object> result = jdbcTemplate.queryForMap(query, accountNumber);
        normalizeCustAdvTokenRow(result, customerCode, premisesCode, accountNumber);

        if (logToReport) {
            long elapsed = System.currentTimeMillis() - startTime;
            SimplifiedExtentReportManager.logDatabaseQuery(query, result.toString(), elapsed);
        }
        return result;
    }

    public String tryGetLatestConfirmPaperlessTokenFromCustAdv(String customerCode, String premisesCode) {
        return tryGetLatestConfirmPaperlessTokenFromCustAdv(customerCode, premisesCode, true);
    }

    public String tryGetLatestConfirmPaperlessTokenFromCustAdv(String customerCode,
                                                               String premisesCode,
                                                               boolean logToReport) {
        try {
            Map<String, Object> row = getLatestConfirmPaperlessTokenFromCustAdv(
                    customerCode, premisesCode, logToReport);
            return resolveTokenIdentifierFromRow(row);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public long getMaxCustAdvPaperlessVerificationId(String customerCode, String premisesCode) {
        String accountNumber = buildCustAdvAccountNumber(customerCode, premisesCode);
        String query = DBQuery.SELECT_CUSTADV_MAX_PAPERLESS_VERIFICATION_ID;
        Number maxId = jdbcTemplate.queryForObject(query, Number.class, accountNumber);
        return maxId == null ? 0L : maxId.longValue();
    }

    public String tryGetUnusedConfirmPaperlessTokenAfterId(String customerCode,
                                                           String premisesCode,
                                                           long minVerificationId) {
        try {
            Map<String, Object> row = getUnusedConfirmPaperlessTokenAfterId(
                    customerCode, premisesCode, minVerificationId, false);
            return resolveTokenIdentifierFromRow(row);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public Map<String, Object> getUnusedConfirmPaperlessTokenAfterId(String customerCode,
                                                                     String premisesCode,
                                                                     long minVerificationId) {
        return getUnusedConfirmPaperlessTokenAfterId(customerCode, premisesCode, minVerificationId, true);
    }

    public Map<String, Object> getUnusedConfirmPaperlessTokenAfterId(String customerCode,
                                                                     String premisesCode,
                                                                     long minVerificationId,
                                                                     boolean logToReport) {
        long startTime = System.currentTimeMillis();
        String accountNumber = buildCustAdvAccountNumber(customerCode, premisesCode);
        String query = DBQuery.SELECT_CUSTADV_UNUSED_PAPERLESS_TOKEN_AFTER_ID;
        if (logToReport) {
            logQueryInAllure("get unused custadv paperless token after id " + minVerificationId
                    + " for account " + accountNumber, query);
        }
        Map<String, Object> result = jdbcTemplate.queryForMap(query, accountNumber, minVerificationId);
        normalizeCustAdvTokenRow(result, customerCode, premisesCode, accountNumber);
        if (logToReport) {
            logPaperlessDbResult(query, result, startTime);
        }
        return result;
    }

    /**
     * Custadv In Progress bill token (not confirmed, not expired, bill type E).
     * Use via {@code ApplicationContext.get().getDbAction("mariadb")}.
     */
    public Map<String, Object> tryGetCustAdvInProgressBillTokenForAccount(String customerCode, String premisesCode) {
        return tryGetCustAdvInProgressTokenForAccount(
                customerCode,
                premisesCode,
                DBQuery.SELECT_CUSTADV_IN_PROGRESS_BILL_TOKEN_FOR_ACCOUNT,
                "get custadv In Progress bill confirmation link");
    }

    /**
     * Custadv In Progress correspondence token (not confirmed, not expired, corr type E).
     * Use via {@code ApplicationContext.get().getDbAction("mariadb")}.
     */
    public Map<String, Object> tryGetCustAdvInProgressCorrTokenForAccount(String customerCode, String premisesCode) {
        return tryGetCustAdvInProgressTokenForAccount(
                customerCode,
                premisesCode,
                DBQuery.SELECT_CUSTADV_IN_PROGRESS_CORR_TOKEN_FOR_ACCOUNT,
                "get custadv In Progress correspondence confirmation link");
    }

    /** Any unused, not-expired custadv confirmation link for the account (channel-agnostic fallback). */
    public Map<String, Object> tryGetCustAdvInProgressTokenForAccount(String customerCode, String premisesCode) {
        return tryGetCustAdvInProgressTokenForAccount(
                customerCode,
                premisesCode,
                DBQuery.SELECT_CUSTADV_IN_PROGRESS_TOKEN_FOR_ACCOUNT,
                "get custadv In Progress confirmation link (any channel)");
    }

    private Map<String, Object> tryGetCustAdvInProgressTokenForAccount(String customerCode,
                                                                       String premisesCode,
                                                                       String query,
                                                                       String allureLabel) {
        long startTime = System.currentTimeMillis();
        String accountNumber = buildCustAdvAccountNumber(customerCode, premisesCode);
        logQueryInAllure(allureLabel + " for account " + accountNumber, query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query, accountNumber);
            normalizeCustAdvTokenRow(result, customerCode, premisesCode, accountNumber);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            logPaperlessDbResult(query, Map.of("rowCount", 0, "accountNumber", accountNumber), startTime);
            return null;
        }
    }

    /**
     * MariaDB (custadv): latest paperless token for account regardless of used/expired state.
     */
    public Map<String, Object> getAnyLatestConfirmPaperlessTokenFromCustAdv(String customerCode, String premisesCode) {
        return getAnyLatestConfirmPaperlessTokenFromCustAdv(customerCode, premisesCode, true);
    }

    public Map<String, Object> getAnyLatestConfirmPaperlessTokenFromCustAdv(String customerCode,
                                                                           String premisesCode,
                                                                           boolean logToReport) {
        long startTime = System.currentTimeMillis();
        String accountNumber = buildCustAdvAccountNumber(customerCode, premisesCode);
        String query = DBQuery.SELECT_CUSTADV_ANY_LATEST_PAPERLESS_TOKEN_FOR_ACCOUNT;

        if (logToReport) {
            logQueryInAllure("get any latest paperless token from custadv for account " + accountNumber, query);
        }

        Map<String, Object> result = jdbcTemplate.queryForMap(query, accountNumber);
        normalizeCustAdvTokenRow(result, customerCode, premisesCode, accountNumber);

        if (logToReport) {
            long elapsed = System.currentTimeMillis() - startTime;
            SimplifiedExtentReportManager.logDatabaseQuery(query, result.toString(), elapsed);
        }
        return result;
    }

    public String tryGetAnyConfirmPaperlessTokenFromCustAdv(String customerCode, String premisesCode) {
        try {
            Map<String, Object> row = getAnyLatestConfirmPaperlessTokenFromCustAdv(customerCode, premisesCode);
            return resolveTokenIdentifierFromRow(row);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * MariaDB (custadv): latest used paperless confirmation token ({@code date_time_link_confirmed} populated).
     */
    public Map<String, Object> getCustAdvUsedConfirmationToken() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_CUSTADV_USED_PAPERLESS_EMAIL_VERIFICATION_TOKEN;

        logQueryInAllure("get used paperless confirm token from custadv", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        enrichCustAdvTokenRow(result);

        long elapsed = System.currentTimeMillis() - startTime;
        SimplifiedExtentReportManager.logDatabaseQuery(query, result.toString(), elapsed);
        return result;
    }

    public Map<String, Object> tryGetCustAdvExpiredByLinkExpiredToken() {
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(
                    DBQuery.SELECT_CUSTADV_EXPIRED_BY_LINK_EXPIRED);
            enrichCustAdvTokenRow(result);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            return null;
        } catch (RuntimeException ex) {
            log.debug("custadv date_time_link_expired query unavailable: {}", ex.getMessage());
            return null;
        }
    }

    public List<Map<String, Object>> listCustAdvExpiredByLinkExpiredTokens(int limit, boolean logToReport) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_CUSTADV_RECENT_EXPIRED_BY_LINK_EXPIRED;
        if (logToReport) {
            logQueryInAllure("list custadv tokens expired by date_time_link_expired (limit " + limit + ")", query);
        }
        try {
            List<Map<String, Object>> result = jdbcTemplate.queryForList(query, limit);
            for (Map<String, Object> row : result) {
                enrichCustAdvTokenRow(row);
            }
            if (logToReport) {
                logPaperlessDbResult(query, Map.of("rowCount", result.size()), startTime);
            }
            return result;
        } catch (RuntimeException ex) {
            log.debug("custadv date_time_link_expired list query unavailable: {}", ex.getMessage());
            return List.of();
        }
    }

    /**
     * MariaDB (custadv): latest expired, unused paperless confirmation token.
     */
    public Map<String, Object> getCustAdvExpiredConfirmationToken() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_CUSTADV_EXPIRED_PAPERLESS_EMAIL_VERIFICATION_TOKEN;

        logQueryInAllure("get expired paperless confirm token from custadv", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        enrichCustAdvTokenRow(result);

        long elapsed = System.currentTimeMillis() - startTime;
        SimplifiedExtentReportManager.logDatabaseQuery(query, result.toString(), elapsed);
        return result;
    }

    /**
     * MariaDB (custadv): latest unused paperless token across all accounts (no account filter).
     * TC_124: row may be expired while {@code date_time_link_confirmed IS NULL} — Confirm returns 10413.
     */
    public Map<String, Object> getCustAdvLatestUnusedPaperlessTokenAnyAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_CUSTADV_LATEST_UNUSED_PAPERLESS_TOKEN_ANY_ACCOUNT;
        logQueryInAllure("get latest unused paperless token from custadv (any account)", query);
        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        enrichCustAdvTokenRow(result);
        long elapsed = System.currentTimeMillis() - startTime;
        SimplifiedExtentReportManager.logDatabaseQuery(query, result.toString(), elapsed);
        return result;
    }

    static void enrichCustAdvTokenRow(Map<String, Object> row) {
        if (row == null) {
            return;
        }
        normalizeCustAdvTokenFields(row);
        Object accountNumberObj = row.get("account_number");
        if (accountNumberObj == null) {
            accountNumberObj = row.get("accountNumber");
        }
        if (accountNumberObj == null) {
            return;
        }
        String accountNumber = accountNumberObj.toString();
        if (accountNumber.length() < 16) {
            return;
        }
        row.putIfAbsent("customerCode", String.valueOf(Long.parseLong(accountNumber.substring(2, 9))));
        row.putIfAbsent("premisesCode", String.valueOf(Long.parseLong(accountNumber.substring(9, 16))));
        row.putIfAbsent("accountNumber", accountNumber);
    }

    static void normalizeCustAdvTokenRow(Map<String, Object> row,
                                         String customerCode,
                                         String premisesCode,
                                         String accountNumber) {
        normalizeCustAdvTokenFields(row);
        row.putIfAbsent("customerCode", customerCode);
        row.putIfAbsent("premisesCode", premisesCode);
        row.put("accountNumber", accountNumber);
    }

    /** Normalizes Token / tokenIdentifier to hex (strips {@code t=} from email_link query). */
    private static void normalizeCustAdvTokenFields(Map<String, Object> row) {
        if (row == null) {
            return;
        }
        for (String key : new String[]{"Token", "token", "tokenIdentifier"}) {
            Object value = row.get(key);
            if (value != null && !value.toString().isBlank()) {
                row.put(key, PaperlessConfirmationTokenUtil.normalizeTokenPlaintext(value.toString()));
            }
        }
        if (row.get("tokenIdentifier") == null && row.get("Token") != null) {
            row.put("tokenIdentifier", row.get("Token"));
        }
    }

    public static String resolveTokenIdentifierFromRow(Map<String, Object> row) {
        if (row == null) {
            return null;
        }
        for (String key : new String[]{"tokenIdentifier", "Token", "token"}) {
            Object value = row.get(key);
            if (value != null && !value.toString().isBlank()) {
                // Strip confirm?t=<hex> / t=<hex> so Confirm API receives the hex token only.
                return PaperlessConfirmationTokenUtil.normalizeTokenPlaintext(value.toString());
            }
        }
        return null;
    }

    public Map<String, Object> getLatestValidConfirmationTokenFromOcsepci(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_CONFIRM_LATEST_VALID_TOKEN;

        logQueryInAllure("get latest valid confirmation token from OCSEPCI (fallback)", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query, customerCode, premisesCode);

        long elapsed = System.currentTimeMillis() - startTime;
        SimplifiedExtentReportManager.logDatabaseQuery(query, result.toString(), elapsed);
        return result;
    }

    public Map<String, Object> getLatestOcsepciForAccount(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_LATEST_OCSEPCI_FOR_ACCOUNT;
        logQueryInAllure("get latest OCSEPCI pending state for " + customerCode + "/" + premisesCode, query);
        Map<String, Object> result = jdbcTemplate.queryForMap(query, customerCode, premisesCode);
        logPaperlessDbResult(query, result, startTime);
        return result;
    }

    public Map<String, Object> tryGetLatestOcsepciForAccount(String customerCode, String premisesCode) {
        try {
            return getLatestOcsepciForAccount(customerCode, premisesCode);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Active pending bill OCSEPCI row for cust/prem (type E, link not expired, not completed).
     * Report evidence that GetAccountInfo Initiated (I) is backed by In Progress PPER.
     */
    public Map<String, Object> tryGetActivePendingBillPperForAccount(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_PENDING_BILL_PPER_FOR_ACCOUNT;
        logQueryInAllure("get active pending bill PPER (In Progress) for " + customerCode + "/" + premisesCode, query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query, customerCode, premisesCode);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            logPaperlessDbResult(query, Map.of("rowCount", 0), startTime);
            return null;
        }
    }

    /**
     * Active pending correspondence OCSEPCI row for cust/prem (type E, link not expired, not completed).
     * Report evidence that GetAccountInfo Initiated (I) is backed by In Progress PPER.
     */
    public Map<String, Object> tryGetActivePendingCorrPperForAccount(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACTIVE_PENDING_CORR_PPER_FOR_ACCOUNT;
        logQueryInAllure("get active pending corr PPER (In Progress) for " + customerCode + "/" + premisesCode, query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query, customerCode, premisesCode);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            logPaperlessDbResult(query, Map.of("rowCount", 0), startTime);
            return null;
        }
    }

    /** Recent OCSEPCI/PPER rows for review evidence (does not change enrollment behavior). */
    public List<Map<String, Object>> listRecentOcsepciForAccount(String customerCode,
                                                                 String premisesCode,
                                                                 int maxRows) {
        long startTime = System.currentTimeMillis();
        int limit = Math.max(1, Math.min(maxRows, 10));
        String query = String.format(DBQuery.SELECT_RECENT_OCSEPCI_FOR_ACCOUNT, limit);
        logQueryInAllure("list recent OCSEPCI/PPER rows for " + customerCode + "/" + premisesCode, query);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query, customerCode, premisesCode);
        logPaperlessDbResult(query, Map.of("rowCount", result.size()), startTime);
        return result;
    }

    public Map<String, Object> getExpiredConfirmationToken() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_CONFIRM_EXPIRED_TOKEN;

        logQueryInAllure("get expired confirmation token account (latest OCSEPCI expired+unused)", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;
        SimplifiedExtentReportManager.logDatabaseQuery(query, result.toString(), elapsed);
        return result;
    }

    public Map<String, Object> getExpiredConfirmationTokenQuiet() {
        return jdbcTemplate.queryForMap(DBQuery.SELECT_CONFIRM_EXPIRED_TOKEN);
    }

    /**
     * Accounts whose latest OCSEPCI is expired and unused.
     */
    public List<Map<String, Object>> listExpiredConfirmationTokenAccounts(int maxRows) {
        return listExpiredConfirmationTokenAccounts(maxRows, true);
    }

    public List<Map<String, Object>> listExpiredConfirmationTokenAccounts(int maxRows, boolean logToReport) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_CONFIRM_EXPIRED_TOKEN_ACCOUNTS;
        if (logToReport) {
            logQueryInAllure("list expired OCSEPCI confirmation token accounts (latest row expired+unused)", query);
        }
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        if (result.size() > maxRows) {
            result = result.subList(0, maxRows);
        }
        if (logToReport) {
            logPaperlessDbResult(query, Map.of("accountCount", result.size()), startTime);
        }
        return result;
    }

    /**
     * Logs custadv unused-token lookup using the same format as {@link #getLatestConfirmPaperlessTokenFromCustAdv}.
     */
    public void logConfirmPaperlessTokenLookupResult(String customerCode,
                                                     String premisesCode,
                                                     Map<String, Object> result) {
        long startTime = System.currentTimeMillis();
        String accountNumber = buildCustAdvAccountNumber(customerCode, premisesCode);
        String query = DBQuery.SELECT_CUSTADV_LATEST_PAPERLESS_EMAIL_VERIFICATION_TOKEN;
        logQueryInAllure("get latest paperless confirm token from custadv for account " + accountNumber, query);
        logPaperlessDbResult(query, result, startTime);
    }

    /**
     * Latest unused paperless tokens across accounts (no account filter).
     */
    public List<Map<String, Object>> listRecentUnusedPaperlessTokensFromCustAdv(int limit) {
        return listRecentUnusedPaperlessTokensFromCustAdv(limit, true);
    }

    public List<Map<String, Object>> listRecentUnusedPaperlessTokensFromCustAdv(int limit, boolean logToReport) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_CUSTADV_RECENT_UNUSED_PAPERLESS_TOKENS;
        if (logToReport) {
            logQueryInAllure("list recent unused paperless tokens from custadv (limit " + limit + ")", query);
        }
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query, limit);
        for (Map<String, Object> row : result) {
            enrichCustAdvTokenRow(row);
        }
        if (logToReport) {
            logPaperlessDbResult(query, Map.of("rowCount", result.size()), startTime);
        }
        return result;
    }

    public Map<String, Object> getUsedConfirmationToken() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_CONFIRM_USED_TOKEN;

        logQueryInAllure("get used confirmation token", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;
        SimplifiedExtentReportManager.logDatabaseQuery(query, result.toString(), elapsed);
        return result;
    }

    public Map<String, Object> getEmailMismatchConfirmationToken() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_CONFIRM_EMAIL_MISMATCH_TOKEN;

        logQueryInAllure("get email mismatch confirmation token", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;
        SimplifiedExtentReportManager.logDatabaseQuery(query, result.toString(), elapsed);
        return result;
    }

    public List<Map<String, Object>> listEmailMismatchConfirmationTokenAccounts(int maxRows) {
        return listEmailMismatchConfirmationTokenAccounts(maxRows, true);
    }

    public List<Map<String, Object>> listEmailMismatchConfirmationTokenAccounts(int maxRows, boolean logToReport) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_CONFIRM_EMAIL_MISMATCH_ACCOUNTS;
        if (logToReport) {
            logQueryInAllure("list email-mismatch confirmation token accounts (PPER email != Banner email)", query);
        }
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        if (result.size() > maxRows) {
            result = result.subList(0, maxRows);
        }
        if (logToReport) {
            logPaperlessDbResult(query, Map.of("accountCount", result.size()), startTime);
        }
        return result;
    }

    public void updateActiveBannerEmailForCustomer(String customerCode, String newEmailAddress) {
        String query = DBQuery.UPDATE_ACTIVE_BANNER_EMAIL_FOR_CUSTOMER;
        int updated = jdbcTemplate.update(query, newEmailAddress, customerCode);
        if (updated == 0) {
            throw new IllegalStateException(
                    "No active Banner email row updated for customer " + customerCode);
        }
        log.debug("Updated active Banner email for customer {} (rows={})", customerCode, updated);
    }

    public Map<String, Object> getActiveAccountForTc105() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_TC_105_ACTIVE_EXPIRED_OR_USED_TOKEN;
        logQueryInAllure("get ACTIVE account for TC_105 (expired or used token, no valid unused token)", query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalStateException(
                    "No ACTIVE account for TC_105 with an expired or used confirmation token and no valid unused token.");
        }
    }

    public Map<String, Object> getNewAccountForTc106() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_TC_106_NEW_EXPIRED_OR_USED_TOKEN;
        logQueryInAllure("get NEW account for TC_106 (expired or used token, no valid unused token)", query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalStateException(
                    "No NEW account for TC_106 with an expired or used confirmation token and no valid unused token.");
        }
    }

    public Map<String, Object> getActiveAccountForTc116() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_TC_116_ACTIVE_EMAIL_MISMATCH_PENDING;
        logQueryInAllure("get ACTIVE account for TC_116 (pending token email differs from Banner email)", query);
        try {
            Map<String, Object> result = jdbcTemplate.queryForMap(query);
            logPaperlessDbResult(query, result, startTime);
            return result;
        } catch (EmptyResultDataAccessException ex) {
            throw new IllegalStateException(
                    "No ACTIVE account for TC_116 with a pending token whose email differs from Banner email.");
        }
    }

    public void expireValidConfirmationTokensForAccount(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.UPDATE_OCSEPCI_EXPIRE_VALID_TOKENS_FOR_ACCOUNT;
        logQueryInAllure("expire valid unused confirmation tokens for " + customerCode + "/" + premisesCode, query);
        int updated = jdbcTemplate.update(query, customerCode, premisesCode);
        logPaperlessDbResult(query, Map.of("rowsUpdated", updated), startTime);

        try {
            ApplicationContext.get().getDbAction("mariadb")
                    .expireUnusedCustAdvPaperlessTokensForAccount(customerCode, premisesCode);
        } catch (RuntimeException ex) {
            log.warn("Could not expire custadv tokens for {}/{}: {}",
                    customerCode, premisesCode, ex.getMessage());
        }
    }

    /** MariaDB (custadv): mark unused paperless tokens expired for account (TC_105/106, TC_124 bootstrap). */
    public void expireUnusedCustAdvPaperlessTokensForAccount(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();
        String accountNumber = buildCustAdvAccountNumber(customerCode, premisesCode);
        int updated = expireUnusedCustAdvPaperlessTokensQuiet(accountNumber);
        logQueryInAllure("expire unused custadv paperless tokens for account " + accountNumber,
                DBQuery.UPDATE_CUSTADV_SET_LINK_EXPIRED_PAST + " / "
                        + DBQuery.UPDATE_CUSTADV_EXPIRE_UNUSED_PAPERLESS_TOKENS_FOR_ACCOUNT);
        logPaperlessDbResult(
                "expire unused custadv paperless tokens for " + accountNumber,
                Map.of("rowsUpdated", updated),
                startTime);
    }

    /** Quiet custadv expiry for TC_124 bootstrap — tries UAT1 link-expired column then legacy {@code expired} column. */
    public int expireUnusedCustAdvPaperlessTokensQuiet(String accountNumber) {
        int updated = 0;
        try {
            updated += jdbcTemplate.update(DBQuery.UPDATE_CUSTADV_SET_LINK_EXPIRED_PAST, accountNumber);
        } catch (RuntimeException ex) {
            log.debug("date_time_link_expired update unavailable for {}: {}", accountNumber, ex.getMessage());
        }
        try {
            updated += jdbcTemplate.update(
                    DBQuery.UPDATE_CUSTADV_EXPIRE_UNUSED_PAPERLESS_TOKENS_FOR_ACCOUNT, accountNumber);
        } catch (RuntimeException ex) {
            log.debug("expired column update unavailable for {}: {}", accountNumber, ex.getMessage());
        }
        return updated;
    }

    public Map<String, Object> getConfirmationTokenForAccountMismatch() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_CONFIRM_TOKEN_ACCOUNT_MISMATCH;

        logQueryInAllure("get confirmation token for account mismatch scenario", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;
        SimplifiedExtentReportManager.logDatabaseQuery(query, result.toString(), elapsed);
        return result;
    }

    public Map<String, Object> getUsageHistoryINTERSTATE() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_BILL_HISTORY_INTERSTATE;

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

    public Map<String, Object> getBillInfoWithRewards() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_BILL_INFO_WITH_REWARDS;

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

    public Map<String, Object> getUsageHistoryPreviousBill0() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_BILL_HISTORY_PREV_BILL_0;

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

    public Map<String, Object> getAccountWithoutNickname2() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITHOUT_NICKNAME2;

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

    public Map<String, Object> getAccountForBillInfo( String custCode, String premCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.SELECT_BILL_INFO;

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


    public Map<String, Object> getRoutingNo( String custCode, String premCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.SELECT_ROUTING_NO;

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

    public Map<String, Object> getPendingEnrollmentNewAccountOnly() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_PENDING_ENROLLMENT_NEW_ACCOUNT_ONLY;

        logQueryInAllure("get pending enrollment NEW account only", query);

        Map<String, Object> result = jdbcTemplate.queryForMap(query);

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                query,
                result.toString(),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getPendingEnrollmentMailingAddress(String custCode, String premCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_PENDING_ENROLLMENT_MAILING_ADDRESS;
        String loggedQuery = query.replaceFirst("\\?", "'" + custCode + "'")
                .replaceFirst("\\?", "'" + premCode + "'");

        logQueryInAllure("get pending enrollment mailing address", loggedQuery);

        Map<String, Object> result;
        try {
            result = jdbcTemplate.queryForMap(query, custCode, premCode);
        } catch (EmptyResultDataAccessException e) {
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

    public int getMailingAddressRowCountByCustomer(String custCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_MAILING_ADDRESS_ROW_COUNT_BY_CUSTOMER;
        String loggedQuery = query.replace("?", "'" + custCode + "'");

        logQueryInAllure("get mailing address row count by customer", loggedQuery);

        Integer count = jdbcTemplate.queryForObject(query, Integer.class, custCode);

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,
                String.valueOf(count),
                elapsed
        );

        return count == null ? 0 : count;
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


    public Map<String, Object> getAccountWithActivePaymentArrangement() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_ACTIVE_PAYMENT_ARRANGEMENT;

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

    public Map<String, Object> getAccountWithActiveNoPaymentArrangement() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_NO_PAYMENT_ARRANGEMENT;

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

    public Map<String, Object> getAccountWithFuturePaymentArrangement() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_FUTURE_PAYMENT_ARRANGEMENT;

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

    public Map<String, Object> getAccountInfoForNoUpdate() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_NO_UPDATE;

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

    public Map<String, Object> getActiveBankDraftAccountNoService() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_ACTIVE_BANK_DRAFT_NO_SERVICE;

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


    public Map<String, Object> getActiveBankDraftAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_ACTIVE_BANK_DRAFT;

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

    public Map<String, Object> getPreNotificationBankDraftAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_PRENOTIFICATION_BANK_DRAFT;

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

    public Map<String, Object> getCanceledBankDraftAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_CANCELLED_BANK_DRAFT;

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


    public Map<String, Object> getInactiveBankDraftAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_INACTIVE_BANK_DRAFT;

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


    public Map<String, Object> getBankDraftWithRoutingNumber() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_BANK_DRAFT_AND_ROUTING_NUMBER;

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


    public Map<String, Object> getBankDraftCheckingAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_CHECKING_ACCOUNT;

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

    public Map<String, Object> getBankDraftCheckingAccount2() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_CHECKING_ACCOUNT2;

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

    public Map<String, Object> getBankDraftWithBankName() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_BANK_NAME;

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


    public Map<String, Object> getBankDraftSavingsAccount() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_SAVINGS_ACCOUNT;

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

    public Map<String, Object> getBankDraftSavingsAccount2() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_SAVINGS_ACCOUNT2;

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

    public int updateDraftStaus() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.UPDATE_ACCOUNT_WITH_SAVINGS_ACCOUNT2;
        logQueryInAllure("delete registered account", query);

        int rowsAffected = jdbcTemplate.update(query);

        long elapsed = System.currentTimeMillis() - startTime;

        return rowsAffected;
    }

    public Map<String, Object> getBankDetails() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_BANK_DETAILS;

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

    public Map<String, Object> getBankDraftWithAccountNumber() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITH_BANK_DRAFT_AND_ACCOUNT_NUMBER;

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


    public Map<String, Object> getAccountWithoutBankDraft() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITHOUT_BANK_DRAFT;

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

    public Map<String, Object> getAccountWithoutBankDraft2() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_WITHOUT_BANK_DRAFT2;

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


    public Map<String, Object> getCustomerWithEmailAndSSN() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_EMAIL_AND_SSN;

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

    public Map<String, Object> getCustomerWithPhoneAndSSN() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_PHONE_AND_SSN;

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

    public int updateTheMaxLimit() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.UPDATE_MAX_LIMIT;
        logQueryInAllure("delete registered account", query);

        int rowsAffected = jdbcTemplate.update(query);

        long elapsed = System.currentTimeMillis() - startTime;

        return rowsAffected;
    }

    public int updateTheMaxLimit2() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.UPDATE_MAX_LIMIT2;
        logQueryInAllure("delete registered account", query);

        int rowsAffected = jdbcTemplate.update(query);

        long elapsed = System.currentTimeMillis() - startTime;

        return rowsAffected;
    }

    /**
     * Current Banner non-prod {@code NEW_TEST_EMAIL_ADDR} override (may be null/blank if already cleared).
     */
    public String getBannerNewTestEmailAddr() {
        String query = DBQuery.SELECT_BANNER_NEW_TEST_EMAIL_ADDR;
        logQueryInAllure("Get Banner NEW_TEST_EMAIL_ADDR", query);
        try {
            String value = jdbcTemplate.queryForObject(query, String.class);
            return value == null ? null : value.trim();
        } catch (EmptyResultDataAccessException ex) {
            log.warn("Banner NEW_TEST_EMAIL_ADDR row not found on uzrpsto");
            return null;
        }
    }

    /** Clears Banner test-email override so paperless notification create/send can fail (40281). */
    public int clearBannerNewTestEmailAddr() {
        String query = DBQuery.CLEAR_BANNER_NEW_TEST_EMAIL_ADDR;
        logQueryInAllure("Clear Banner NEW_TEST_EMAIL_ADDR", query);
        return jdbcTemplate.update(query);
    }

    /** Restores Banner test-email override after 40281 failure scenarios. */
    public int restoreBannerNewTestEmailAddr(String emailAddress) {
        String query = DBQuery.RESTORE_BANNER_NEW_TEST_EMAIL_ADDR;
        logQueryInAllure("Restore Banner NEW_TEST_EMAIL_ADDR to " + emailAddress, query);
        return jdbcTemplate.update(query, emailAddress);
    }

    public int rollbackPasswordToPrev(String username) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.UPDATE_PASSWORD;
        logQueryInAllure("delete registered account", query);

        int rowsAffected = jdbcTemplate.update(query, username);

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

    public Map<String, Object> getRequiredAccountDetails3() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_REQUIRED3;

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

    public Map<String, Object> getRequiredAccountDetails2() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_REQUIRED2;

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

    public Map<String, Object> updateAccountNo(String customerCode, String premCode, String accountNo) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.UPDATE_ACCOUNT_NO;

        // Escape single quotes for logging
        String safeCustomer = customerCode.replace("'", "''");
        String safePrem = premCode.replace("'", "''");

        // Masked value for logging only
        String masked = "****" + accountNo.substring(accountNo.length() - 4);

        // Build logged SQL
        String loggedQuery = query
                .replaceFirst("\\?", masked)
                .replaceFirst("\\?", "'" + safeCustomer + "'")
                .replaceFirst("\\?", "'" + safePrem + "'");

        logQueryInAllure("update account no", loggedQuery);

        // Execute actual update (raw value)
        int rows = jdbcTemplate.update(
                query,
                Integer.parseInt(accountNo),
                customerCode,
                premCode
        );

        long elapsed = System.currentTimeMillis() - startTime;

        Map<String, Object> result = new HashMap<>();
        result.put("rowsUpdated", rows);

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,
                result.toString(),
                elapsed
        );

        return result;
    }



    public Map<String, Object> getTheAccountInfo(String customerCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_INFO;

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

    public Map<String, Object> getTheAccountInfo2(String customerCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_INFO2;

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

    public String getPlanRolloverIndicator(String planCode) {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_PLAN_ROLLOVER_INDICATOR;

        logQueryInAllure("get plan rollover indicator", query);

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, planCode);
        String value = rows.isEmpty() || rows.get(0).get("uztppuc_rollover") == null
                ? "N"
                : rows.get(0).get("uztppuc_rollover").toString().trim();

        long elapsed = System.currentTimeMillis() - startTime;
        SimplifiedExtentReportManager.logDatabaseQuery(query, value, elapsed);
        return value;
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

    public Map<String, Object> getAccountDetailsTC1() {
        return queryAccountDetails(DBQuery.SELECT_ACCOUNT_DETAILS_TC1);
    }

    public Map<String, Object> getAccountDetailsTC2() {
        return queryAccountDetails(DBQuery.SELECT_ACCOUNT_DETAILS_TC2);
    }

    /** Candidate accounts for TC_2 probing (planRenewalWindowIndicator = Y). */
    public List<Map<String, Object>> listAccountDetailsTC2Candidates() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC2_CANDIDATES;
        logQueryInAllure("list TC_2 renewal-indicator Y candidates", query);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        long elapsed = System.currentTimeMillis() - startTime;
        SimplifiedExtentReportManager.logDatabaseQuery(
                query, "candidateCount=" + result.size(), elapsed);
        return result;
    }

    public Map<String, Object> getAccountDetailsTC3() {
        return queryAccountDetails(DBQuery.SELECT_ACCOUNT_DETAILS_TC3);
    }

    /** Candidate accounts for TC_3 probing (planRenewalWindowIndicator = N). */
    public List<Map<String, Object>> listAccountDetailsTC3Candidates() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC3_CANDIDATES;
        logQueryInAllure("list TC_3 renewal-indicator N candidates", query);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        long elapsed = System.currentTimeMillis() - startTime;
        SimplifiedExtentReportManager.logDatabaseQuery(
                query, "candidateCount=" + result.size(), elapsed);
        return result;
    }

    public Map<String, Object> getAccountDetailsTC4() {
        return queryAccountDetails(DBQuery.SELECT_ACCOUNT_DETAILS_TC4);
    }

    /** Candidate accounts for TC_4 probing (planRenewalWindowIndicator = -). */
    public List<Map<String, Object>> listAccountDetailsTC4Candidates() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC4_CANDIDATES;
        logQueryInAllure("list TC_4 renewal-indicator dash candidates", query);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        long elapsed = System.currentTimeMillis() - startTime;
        SimplifiedExtentReportManager.logDatabaseQuery(
                query, "candidateCount=" + result.size(), elapsed);
        return result;
    }

    public Map<String, Object> getAccountDetailsTC5() {
        // Prefer Guaranteed Bill + discount (non-transferable/PRICEPRO first) per FTD.
        try {
            Map<String, Object> withNonTransferableDiscount =
                    queryAccountDetails(DBQuery.SELECT_ACCOUNT_DETAILS_TC5);
            DualReportManager.logInfo(
                    "TC_5: selected Guaranteed Bill account WITH discount + PRICEPRO (non-transferable path) "
                            + withNonTransferableDiscount.get("ucracct_cust_code") + "/"
                            + withNonTransferableDiscount.get("ucracct_prem_code"));
            return withNonTransferableDiscount;
        } catch (EmptyResultDataAccessException ignored) {
            DualReportManager.logInfo(
                    "TC_5: no Guaranteed + PRICEPRO/discount account; trying Guaranteed + any discount");
        }

        try {
            Map<String, Object> withAnyDiscount =
                    queryAccountDetails(DBQuery.SELECT_ACCOUNT_DETAILS_TC5_WITH_ANY_DISCOUNT);
            DualReportManager.logInfo(
                    "TC_5: selected Guaranteed Bill account WITH active discount "
                            + withAnyDiscount.get("ucracct_cust_code") + "/"
                            + withAnyDiscount.get("ucracct_prem_code"));
            return withAnyDiscount;
        } catch (EmptyResultDataAccessException ignored) {
            DualReportManager.logInfo(
                    "TC_5: no Guaranteed + discount account found; falling back to Guaranteed without discount");
        }

        Map<String, Object> withoutDiscount =
                queryAccountDetails(DBQuery.SELECT_ACCOUNT_DETAILS_TC5_WITHOUT_DISCOUNT);
        DualReportManager.logInfo("TC_5: selected Guaranteed Bill account WITHOUT discount "
                + withoutDiscount.get("ucracct_cust_code") + "/"
                + withoutDiscount.get("ucracct_prem_code"));
        return withoutDiscount;
    }

    public List<Map<String, Object>> listAccountDetailsTC5PriceProCandidates() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC5_CANDIDATES;
        logQueryInAllure("list TC_5 Guaranteed+PRICEPRO discount candidates", query);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        long elapsed = System.currentTimeMillis() - startTime;
        SimplifiedExtentReportManager.logDatabaseQuery(
                query, "candidateCount=" + result.size(), elapsed);
        return result;
    }

    public List<Map<String, Object>> listAccountDetailsTC5AnyDiscountCandidates() {
        long startTime = System.currentTimeMillis();
        String query = DBQuery.SELECT_ACCOUNT_DETAILS_TC5_WITH_ANY_DISCOUNT_CANDIDATES;
        logQueryInAllure("list TC_5 Guaranteed+any-discount candidates", query);
        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        long elapsed = System.currentTimeMillis() - startTime;
        SimplifiedExtentReportManager.logDatabaseQuery(
                query, "candidateCount=" + result.size(), elapsed);
        return result;
    }

    public Map<String, Object> getAccountDetailsTC5WithoutDiscount() {
        return queryAccountDetails(DBQuery.SELECT_ACCOUNT_DETAILS_TC5_WITHOUT_DISCOUNT);
    }

    public Map<String, Object> getAccountDetailsTC6() {
        return queryAccountDetails(DBQuery.SELECT_ACCOUNT_DETAILS_TC6);
    }

    public Map<String, Object> getAccountDetailsTC7() {
        return queryAccountDetails(DBQuery.SELECT_ACCOUNT_DETAILS_TC7);
    }

    public Map<String, Object> getAccountDetailsTC8() {
        return queryAccountDetails(DBQuery.SELECT_ACCOUNT_DETAILS_TC8);
    }

    private Map<String, Object> queryAccountDetails(String query) {
        long startTime = System.currentTimeMillis();
        logQueryInAllure("get customer code", query);
        Map<String, Object> result = jdbcTemplate.queryForMap(query);
        long elapsed = System.currentTimeMillis() - startTime;
        SimplifiedExtentReportManager.logDatabaseQuery(query, result.toString(), elapsed);
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

    public List<Map<String, Object>> getUsageHistory(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_USAGE_HISTORY;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
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

    public List<Map<String, Object>> getBillInfo(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_BILL_INFO;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
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

    public List<Map<String, Object>> getBillHistory(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_BILL_HISTORY;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
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

    public List<Map<String, Object>> getPaymentHistory(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_PAYMENT_HISTORY;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
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


    public List<Map<String, Object>> getPaymentArrangementInfo(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_PAYMENT_ARRANGEMENT_INFO;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
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

    public Map<String, Object> getPaymentHistoryNotPostedReversal(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.SELECT_ACTIVE_PAYMENT_HISTORY_NOT_POSTED_REVERSAL;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        Map<String, Object> result;

        try {
            result = jdbcTemplate.queryForMap(queryTemplate, customerCode, premisesCode);
        } catch (EmptyResultDataAccessException e) {
            result = Collections.emptyMap();
        }

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,
                String.valueOf(result),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getPaymentHistoryNonPosted(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.SELECT_ACTIVE_PAYMENT_HISTORY_NON_POSTED_PAYMENTS;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        Map<String, Object> result;

        try {
            result = jdbcTemplate.queryForMap(queryTemplate, customerCode, premisesCode);
        } catch (EmptyResultDataAccessException e) {
            result = Collections.emptyMap();
        }

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,
                String.valueOf(result),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getPaymentHistoryPosted2(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.SELECT_ACTIVE_PAYMENT_HISTORY_POSTED2;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        Map<String, Object> result;

        try {
            result = jdbcTemplate.queryForMap(queryTemplate, customerCode, premisesCode);
        } catch (EmptyResultDataAccessException e) {
            result = Collections.emptyMap();
        }

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,
                String.valueOf(result),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getPaymentHistoryNotPosted2(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.SELECT_ACTIVE_PAYMENT_HISTORY_NOT_POSTED2;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        Map<String, Object> result;

        try {
            result = jdbcTemplate.queryForMap(queryTemplate, customerCode, premisesCode);
        } catch (EmptyResultDataAccessException e) {
            result = Collections.emptyMap();
        }

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,
                String.valueOf(result),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getBankDraftInfo(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_BANK_DRAFT_INFO;

        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get bank draft info", loggedQuery);

        Map<String, Object> result;

        try {
            result = jdbcTemplate.queryForMap(queryTemplate, customerCode, premisesCode);
        } catch (EmptyResultDataAccessException e) {
            result = Collections.emptyMap();
        }

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,
                String.valueOf(result),
                elapsed
        );

        return result;
    }

    public Map<String, Object> getBankDraftInfo2(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_BANK_DRAFT_INFO2;

        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get bank draft info", loggedQuery);

        Map<String, Object> result;

        try {
            result = jdbcTemplate.queryForMap(queryTemplate, customerCode, premisesCode);
        } catch (EmptyResultDataAccessException e) {
            result = Collections.emptyMap();
        }

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,
                String.valueOf(result),
                elapsed
        );

        return result;
    }


    public List<Map<String, Object>> getPaymentHistory2(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_PAYMENT_HISTORY;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
        } catch (EmptyResultDataAccessException e) {
            result = Collections.emptyList();
        }

        long elapsed = System.currentTimeMillis() - startTime;

//        SimplifiedExtentReportManager.logDatabaseQuery(
//                loggedQuery,
//                String.valueOf(result),
//                elapsed
//        );

        return result;
    }

    public List<Map<String, Object>> getBillHistory2(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_BILL_HISTORY;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
        } catch (EmptyResultDataAccessException e) {
            result = Collections.emptyList();
        }

        long elapsed = System.currentTimeMillis() - startTime;

//        SimplifiedExtentReportManager.logDatabaseQuery(
//                loggedQuery,
//                String.valueOf(result),
//                elapsed
//        );

        return result;
    }

    public List<Map<String, Object>> getUsageHistory2(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_USAGE_HISTORY2;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
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

    public List<Map<String, Object>> getServiceNumber(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_SERVICE_NUMBER;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
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

    public List<Map<String, Object>> getBillDate(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_BILL_DATE;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
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


    public List<Map<String, Object>> getFromDate(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_FROM_DATE;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
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

    public List<Map<String, Object>> getToDate(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_TO_DATE;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
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

    public List<Map<String, Object>> getActualConsumption(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_ACTUAL_CONSUMPTION;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
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


    public List<Map<String, Object>> getAverageDailyBilledConsumption(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_AVERAGE_DAILY_BILLED_CONSUMPTION;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
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


    public List<Map<String, Object>> getTotalBilledConsumption(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_TOTAL_BILLED_CONSUMPTION;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
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

    public List<Map<String, Object>> getDaysOfService(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_DAYS_OF_SERVICE;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
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

    public List<Map<String, Object>> getReading(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_READING;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
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

    public List<Map<String, Object>> getReadDate(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_READ_DATE;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
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


    public List<Map<String, Object>> getHeatingDegreeDays(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_HEATING_DEGREE_DAYS;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
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

    public List<Map<String, Object>> getBillHistoryTransaction(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_BILL_HISTORY_TRANSACTION;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
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


    public List<Map<String, Object>> getAverageTemperatureFormat(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_AVERAGE_TEMPERATURE;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
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

    public List<Map<String, Object>> getReadType(String customerCode, String premisesCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_READ_TYPE;

        // Replace ? placeholders with quoted values for logging
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + customerCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premisesCode.replace("'", "''") + "'");

        logQueryInAllure("get usage history", loggedQuery);

        List<Map<String, Object>> result;

        try {
            result = jdbcTemplate.queryForList(queryTemplate, customerCode, premisesCode);
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


    public List<Map<String, Object>> getTheMatchingAccounts(String lastName) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_MATCHING_ACCOUNTS;

        // Replace ? with quoted and escaped custCode for logging
        String loggedQuery = queryTemplate.replaceFirst(
                "\\?",
                "'" + lastName.replace("'", "''") + "'"
        );

        // Log expanded SQL
        logQueryInAllure("check account number registered", loggedQuery);

        List<Map<String, Object>> result = null;

        try {
            // Execute query and return list of maps
            result = jdbcTemplate.queryForList(queryTemplate, lastName);
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

    public Map<String, Object> getUserAccountInfoNew2(String custCode, String premCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_USER_ACCOUNT_INFO_NEW2;

        // Replace the two ? placeholders (custCode, premCode) with quoted+escaped
        // values, in order, for logging only
        String loggedQuery = queryTemplate
                .replaceFirst("\\?", "'" + custCode.replace("'", "''") + "'")
                .replaceFirst("\\?", "'" + premCode.replace("'", "''") + "'");

        // Log expanded SQL
        logQueryInAllure("check account number registered", loggedQuery);

        Map<String, Object> result = null;

        try {
            // Execute parameterized query (safe)
            result = jdbcTemplate.queryForMap(queryTemplate, custCode, premCode);
        } catch (EmptyResultDataAccessException e) {
            result = null;
        }

        long elapsed = System.currentTimeMillis() - startTime;

        SimplifiedExtentReportManager.logDatabaseQuery(
                loggedQuery,          // expanded query
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


    public Map<String, Object> getUserAccountWithRoutingNo(String custCode) {
        long startTime = System.currentTimeMillis();

        String queryTemplate = DBQuery.GET_ACCOUNT_ROUTING_NO;

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

    public Map<String, Object> getCustPremCodeActive() {
        String query = DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_ACTIVE;
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
