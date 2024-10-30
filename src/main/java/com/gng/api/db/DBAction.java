package com.gng.api.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
public class DBAction {

    private final JdbcTemplate jdbcTemplate;

    public DBAction(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getActiveCustomerDetails() {
        return jdbcTemplate.queryForList(DBQuery.GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE);
    }

    public List<Map<String, Object>> getAccountInformationHappyFlow() {
        return jdbcTemplate.queryForList(DBQuery.GET_ACCOUNT_INFO_API_SUCCESS_RESPONSE_PARAMETERS);
    }

    public List<Map<String, Object>> getNoteSequenceNumber(String noteSeqNo) {
        return jdbcTemplate.queryForList(DBQuery.SELECT_NOTE_SEQUENCE_NUMBER, noteSeqNo);
    }

}
