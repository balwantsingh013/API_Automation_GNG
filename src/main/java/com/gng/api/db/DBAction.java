package com.gng.api.db;

import io.qameta.allure.Allure;
import lombok.extern.slf4j.Slf4j;
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
    public List<Map<String, Object>> getInvalidCustomerCode() {
        String query = DBQuery.SEARCH_ACC_SELECT_INVALID_CUSTOMER_CODE;
        logQueryInAllure("Get Invalid Customer Code", query);
        return jdbcTemplate.queryForList(query);
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
