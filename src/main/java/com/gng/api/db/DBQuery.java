package com.gng.api.db;

public final class DBQuery {

    public static final String GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE = """
                SELECT *
                FROM (
                    SELECT a."UCRACCT_CUST_CODE", a."UCRACCT_PREM_CODE", c.UCRSERV_NUM
                    FROM ucracct a
                    JOIN ucbprem b ON a."UCRACCT_PREM_CODE" = b.ucbprem_code
                    JOIN ucrserv c ON a."UCRACCT_PREM_CODE" = c.ucrserv_prem_code
                    WHERE a.ucracct_status_ind = 'A'
                      AND a.ucracct_cycl_code BETWEEN '01' AND '21'
                      AND c.ucrserv_num = 1
                      AND c.ucrserv_status_ind = 'A'
                    ORDER BY a."UCRACCT_CUST_CODE" DESC
                )
                WHERE ROWNUM = 1
            """;
    public static final String GET_ACCOUNT_INFO_API_SUCCESS_RESPONSE_PARAMETERS = "WITH CustPremCode AS (\n" +
            "    SELECT *\n" +
            "    FROM (\n" +
            "        SELECT a.\"UCRACCT_CUST_CODE\" AS custCode, a.\"UCRACCT_PREM_CODE\" AS premCode\n" +
            "        FROM ucracct a\n" +
            "        JOIN ucbprem b ON a.\"UCRACCT_PREM_CODE\" = b.ucbprem_code\n" +
            "        JOIN ucrserv c ON a.\"UCRACCT_PREM_CODE\" = c.ucrserv_prem_code\n" +
            "        WHERE a.ucracct_status_ind = 'A'\n" +
            "          AND a.ucracct_cycl_code BETWEEN '01' AND '21'\n" +
            "          AND c.ucrserv_num = 1\n" +
            "          AND c.ucrserv_status_ind = 'A'\n" +
            "        ORDER BY a.\"UCRACCT_CUST_CODE\" DESC\n" +
            "    )\n" +
            "    WHERE ROWNUM = 1\n" +
            ")\n" +
            "--GetAccountInfo\n" +
            "SELECT \n" +
            "    ucracct_cust_code AS \"customerCode\", \n" +
            "    ucracct_prem_code AS \"premisesCode\", \n" +
            "    ucbcust_first_name AS \"custFirstName\", \n" +
            "    ucbcust_middle_name as \"custMiddleName\", \n" +
            "    ucbcust_last_name AS \"custLastNameBus\",\n" +
            "    spk_new_acct_pref_util.f_get_acct_status(ucracct_cust_code, ucracct_prem_code) AS \"accountStatus\",\n" +
            "    (SELECT ucrserv_rate_schedule \n" +
            "     FROM ucrserv v \n" +
            "     WHERE v.ucrserv_prem_code = ucbprem.ucbprem_code) AS \"rateSchedule\", \n" +
            "    ucbcust_ssn_last_four AS \"lastFourSSN\",\n" +
            "    --Premises Address\n" +
            "    ucbprem_street_number AS \"premStreetNum\", \n" +
            "    ucbprem_pdir_code_pre AS \"premStreetPreDir\",\n" +
            "    ucbprem_street_name AS \"premStreetName\", \n" +
            "    ucbprem_ssfx_code AS \"premStreetSuffix\", \n" +
            "    ucbprem_pdir_code_post AS \"premStreetPostDir\", \n" +
            "    ucbprem_utyp_code AS \"premUnitType\", \n" +
            "    ucbprem_unit AS \"premUnitNum\", \n" +
            "    ucbprem_city AS \"premCity\", \n" +
            "    ucbprem_stat_code_addr AS \"premState\",\n" +
            "    ucbprem_zipc_code AS \"premZip\", \n" +
            "    --Service Address\n" +
            "    ucraddr_street_number AS \"billingStreetNum\", \n" +
            "    ucraddr_pdir_code_pre AS \"billingStreetPreDir\", \n" +
            "    ucraddr_street_name AS \"billingStreetName\",\n" +
            "    ucraddr_ssfx_code AS \"billingStreetSuffix\", \n" +
            "    ucraddr_pdir_code_post AS \"billingStreetPostDir\", \n" +
            "    ucraddr_utyp_code AS \"billingUnitType\",\n" +
            "    ucraddr_unit AS \"billingUnitNum\", \n" +
            "    ucraddr_city AS \"billingCity\", \n" +
            "    ucraddr_stat_code AS \"billingState\", \n" +
            "    ucraddr_zip AS \"billingZip\",\n" +
            "    --Balances\n" +
            "    f_calcarbalance(ucracct_cust_code, ucracct_prem_code) AS \"billedBalance\",\n" +
            "    NVL((SELECT SUM(uabopen_balance) \n" +
            "         FROM uabopen \n" +
            "         WHERE uabopen_cust_code = ucracct_cust_code\n" +
            "         AND uabopen_prem_code = ucracct_prem_code \n" +
            "         AND uabopen_due_date < TRUNC(SYSDATE)),0) AS \"pastDueAmount\",\n" +
            "    (SELECT MAX(uabopen_due_date) \n" +
            "     FROM uabopen \n" +
            "     WHERE uabopen_cust_code = ucracct_cust_code\n" +
            "     AND uabopen_prem_code = ucracct_prem_code \n" +
            "     AND uabopen_due_date < TRUNC(SYSDATE) \n" +
            "     AND (SELECT SUM(uabopen_balance)\n" +
            "          FROM uabopen \n" +
            "          WHERE uabopen_cust_code = ucracct_cust_code \n" +
            "          AND uabopen_prem_code = ucracct_prem_code\n" +
            "          AND uabopen_due_date < TRUNC(SYSDATE)) > 0) AS \"pastDueDate\",\n" +
            "    -- Bill Info\n" +
            "    (SELECT ubbbhst_printed_date \n" +
            "     FROM ubbbhst \n" +
            "     WHERE ubbbhst_cust_code = ucracct_cust_code\n" +
            "     AND ubbbhst_prem_code = ucracct_prem_code \n" +
            "     AND ubbbhst_cancel_ind IS NULL \n" +
            "     AND ubbbhst_tran_num = (SELECT MAX(ubbbhst_tran_num)\n" +
            "                             FROM ubbbhst \n" +
            "                             WHERE ubbbhst_cust_code = ucracct_cust_code \n" +
            "                             AND ubbbhst_prem_code = ucracct_prem_code\n" +
            "                             AND ubbbhst_cancel_ind IS NULL)) AS \"billPrintDate\",\n" +
            "    NVL((SELECT ubbbhst_ending_bal \n" +
            "         FROM ubbbhst\n" +
            "         WHERE ubbbhst_cust_code = ucracct_cust_code \n" +
            "         AND ubbbhst_prem_code = ucracct_prem_code \n" +
            "         AND ubbbhst_cancel_ind IS NULL\n" +
            "         AND ubbbhst_tran_num = (SELECT MAX(ubbbhst_tran_num) \n" +
            "                                 FROM ubbbhst \n" +
            "                                 WHERE ubbbhst_cust_code = ucracct_cust_code \n" +
            "                                 AND ubbbhst_prem_code = ucracct_prem_code \n" +
            "                                 AND ubbbhst_cancel_ind IS NULL)),0) AS \"billEndAmount\",\n" +
            "    (SELECT MAX(uabopen_due_date) \n" +
            "     FROM uabopen \n" +
            "     WHERE uabopen_bhst_tran_num = (SELECT MAX(ubbbhst_tran_num)\n" +
            "                                    FROM ubbbhst \n" +
            "                                    WHERE ubbbhst_cust_code = ucracct_cust_code \n" +
            "                                    AND ubbbhst_prem_code = ucracct_prem_code \n" +
            "                                    AND ubbbhst_cancel_ind IS NULL)) AS \"billDueDate\",\n" +
            "    -- Payment info\n" +
            "    NVL((SELECT uabpymt_amount \n" +
            "         FROM uabpymt \n" +
            "         WHERE uabpymt_cust_code = ucracct_cust_code \n" +
            "         AND uabpymt_prem_code = ucracct_prem_code\n" +
            "         ORDER BY uabpymt_pymt_date DESC \n" +
            "         FETCH FIRST 1 ROWS ONLY),0) AS \"lastPaymentAmount\",\n" +
            "    (SELECT MAX(uabpymt_pymt_date) \n" +
            "     FROM uabpymt \n" +
            "     WHERE uabpymt_cust_code = ucracct_cust_code \n" +
            "     AND uabpymt_prem_code = ucracct_prem_code) AS \"lastPaymentDate\",\n" +
            "    -- Letter info\n" +
            "    (SELECT usrletd_date_1 \n" +
            "     FROM usrletd \n" +
            "     WHERE usrletd_letr_code IN ('DISCONNECT', 'PNS_PREPAID1', 'PNS_PREPAID2')\n" +
            "     AND usrletd_printed_ind = 'Y' \n" +
            "     AND usrletd_actual_cust_code = ucracct_cust_code \n" +
            "     AND usrletd_prem_code = ucracct_prem_code\n" +
            "     ORDER BY usrletd_date_1 DESC \n" +
            "     FETCH FIRST 1 ROW ONLY) AS \"discLetterDate\", \n" +
            "    NVL((SELECT usrletd_amount_1 \n" +
            "         FROM usrletd \n" +
            "         WHERE usrletd_letr_code IN ('DISCONNECT', 'PNS_PREPAID1', 'PNS_PREPAID2')\n" +
            "         AND usrletd_printed_ind = 'Y' \n" +
            "         AND usrletd_actual_cust_code = ucracct_cust_code \n" +
            "         AND usrletd_prem_code = ucracct_prem_code\n" +
            "         ORDER BY usrletd_date_1 DESC \n" +
            "         FETCH FIRST 1 ROW ONLY),0) AS \"discLetterAmount\",\n" +
            "    CASE WHEN EXISTS (SELECT DISTINCT 1 \n" +
            "                      FROM uabpyar \n" +
            "                      WHERE uabpyar_cust_code = ucracct_cust_code\n" +
            "                      AND uabpyar_prem_code = ucracct_prem_code \n" +
            "                      AND uabpyar_status = 'A') THEN 'Y' ELSE 'N' END AS \"activePAInd\",\n" +
            "    (SELECT  F_DOES_WU_CREDIT_CARD_EXIST(ucracct_cust_code, ucracct_prem_code) \n" +
            "     FROM dual) AS \"recurringCCInd\",\n" +
            "    CASE WHEN (NVL(ucracct_draft_acct_status, ' ')) = 'A' THEN 'Y' ELSE 'N' END  AS \"bankDraftInd\",\n" +
            "    CASE WHEN EXISTS (SELECT DISTINCT 1 \n" +
            "                      FROM uabbudg \n" +
            "                      WHERE uabbudg_cust_code = ucracct_cust_code\n" +
            "                      AND uabbudg_prem_code = ucracct_prem_code \n" +
            "                      AND uabbudg_status_ind = 'A')  THEN 'Y' ELSE 'N' END AS \"activeBudgetInd\",\n" +
            "    CASE WHEN EXISTS (SELECT DISTINCT 1  \n" +
            "                      FROM uabbdbt \n" +
            "                      WHERE uabbdbt_cust_code = ucracct_cust_code\n" +
            "                      AND uabbdbt_prem_code = ucracct_prem_code) THEN 'Y' ELSE 'N' END AS \"badDebtInd\",\n" +
            "    f_does_active_home_sol_exist(4623528,'4630846') AS \"activeWarrantyInd\",\n" +
            "    (SELECT MAX(ucrcrhs_occurance_date) \n" +
            "     FROM ucrcrhs \n" +
            "     WHERE ucrcrhs_cust_code = ucracct_cust_code\n" +
            "     AND ucrcrhs_prem_code = ucracct_prem_code \n" +
            "     AND ucrcrhs_ccat_code = 'MPAY' \n" +
            "     AND ucrcrhs_occurance_date <= TRUNC(SYSDATE)) AS \"lastDefaultPADate\",\n" +
            "    (SELECT MAX(ucrcrhs_occurance_date) \n" +
            "     FROM ucrcrhs \n" +
            "     WHERE ucrcrhs_cust_code = ucracct_cust_code\n" +
            "     AND ucrcrhs_prem_code = ucracct_prem_code \n" +
            "     AND ucrcrhs_ccat_code = 'SONP' \n" +
            "     AND ucrcrhs_occurance_date <= TRUNC(SYSDATE))  AS \"lastSONPDate\",\n" +
            "    (SELECT MAX(ucrcrhs_occurance_date) \n" +
            "     FROM ucrcrhs \n" +
            "     WHERE ucrcrhs_cust_code = ucracct_cust_code\n" +
            "     AND ucrcrhs_prem_code = ucracct_prem_code \n" +
            "     AND ucrcrhs_ccat_code = 'PREC' \n" +
            "     AND ucrcrhs_occurance_date <= TRUNC(SYSDATE)) AS \"lastPreCollDate\"\n" +
            "FROM \n" +
            "    ucracct, \n" +
            "    ucbcust, \n" +
            "    ucbprem, \n" +
            "    ucraddr, \n" +
            "    CustPremCode\n" +
            "WHERE \n" +
            "    ucracct.ucracct_cust_code = CustPremCode.custCode\n" +
            "    AND ucracct.ucracct_prem_code = CustPremCode.premCode\n" +
            "    AND ucbprem.ucbprem_code = ucracct.ucracct_prem_code\n" +
            "    AND ucbcust.ucbcust_cust_code = ucracct.ucracct_cust_code\n" +
            "    AND ucraddr.ucraddr_cust_code = ucbcust.ucbcust_cust_code \n" +
            "    AND ucraddr.ucraddr_status_ind = 'A'\n" +
            "ORDER BY \n" +
            "    ucracct.ucracct_status_ind ASC, \n" +
            "    ucracct.ucracct_established_date DESC, \n" +
            "    ucracct.ucracct_cust_code ASC\n";
    public static final String SELECT_NOTE_SEQUENCE_NUMBER = """
            SELECT UCBNOTE_SEQ_NUMBER, UCBNOTE_CUST_CODE, UCBNOTE_PREM_CODE
            FROM UCBNOTE WHERE UCBNOTE_SEQ_NUMBER = ?
            """;

    public static final String SELECT_PASSWORD_EXPIRE_DAY = """
            SELECT *
               FROM UZRPSTO
               WHERE UZRPSTO_PARM_NAME = 'PASSWORD_EXPIRE_DAYS'
               AND UZRPSTO_PARM_VALUE = '45'""";


    public static final String EXTERNAL_PARAM_OBJECT_ADDED = """
              SELECT *
              FROM UZBPSTO
            WHERE  UZBPSTO_OBJECT = 'SPK_WEB_API';
            """;


    public static final String SEARCH_ACC_SELECT_INVALID_CUSTOMER_CODE = """
            SELECT *
                                         FROM UCRACCT\s
                                         WHERE UCRACCT_CUST_CODE = 'CustomerCode'
            """;


    public static final String THE_PASSWORD_UPDATE_QUERY = """
                UPDATE USERS SET user_locked_ind ='N', failed_logins=3 WHERE USER_ID='autotester'
            """;
    public static final String FAILED_LOGIN_COUNTS = """
            SELECT failed_logins FROM USERS WHERE USER_ID='autotester'
            """;

    public static final String ROLLBACK_QUERIES = """
            UPDATE USERS SET user_locked_ind ='N', failed_logins=1 WHERE USER_ID='autotester'
            """;
    public static final String EXPIRED_PASSWORD_UPDATE_QUERY = """
            UPDATE USERS
            SET user_locked_ind = 'N', failed_logins = 0,
            PASSWORD_EXPIRE = SYSDATE - 1
            WHERE USER_ID = 'autotester'
            """;
    public static final String EXPIRED_PASSWORD_CHECK_QUERY = """
            SELECT CASE WHEN PASSWORD_EXPIRE < SYSDATE THEN 'Y' ELSE 'N' END AS is_expired FROM USERS WHERE USER_ID = 'autotester'
            """;
    public static final String FAILED_LOGIN_COUNTS_FOR_EXPIRED_PASSWORD = """
            SELECT failed_logins FROM USERS WHERE USER_ID = 'autotester'
            """;


    public static final String EXPIRED_PASSWORD_ROLLBACK_QUERY = """
            UPDATE USERS SET user_locked_ind = 'N', failed_logins = 0, PASSWORD_EXPIRE = SYSDATE +30 WHERE USER_ID = 'autotester'
            """;


    public static final String UPDATE_USER_LOCK_STATUS_QUERY = """
            UPDATE USERS SET user_locked_ind ='Y', failed_logins=4 WHERE USER_ID='autotester'
            """;

    public static final String CHECK_USER_LOCK_STATUS_QUERY = """
            SELECT user_locked_ind FROM users WHERE USER_ID = 'autotester'
            """;
    public static final String FAILED_LOGIN_COUNTS_FOR_USER_LOCK_STATUS_QUERY = """
            UPDATE users SET user_locked_ind = 'N', failed_logins = 0 WHERE USER_ID = 'autotester'
            """;

    public static final String ROLL_BACK_QUERY_FOR_USER_LOCK_STATUS_QUERY = """
            UPDATE USERS SET user_locked_ind ='N', failed_logins=1 WHERE USER_ID='autotester'
            """;
    public static final String UPDATE_FAILED_LOGIN_QUERY = """
            UPDATE USERS SET user_locked_ind ='N', failed_logins=2 WHERE USER_ID='autotester'
            """;
    public static final String ROLE_COUNT_QUERY = """
            SELECT count(*) FROM USER_ROLE WHERE USER_ID = 'autotester'
            """;
    public static final String FAILED_COUNT_ON_USER_ROLE_QUERY = """
                            SELECT failed_logins FROM USERS WHERE USER_ID='autotester'
            """;
    public static final String ROLLBACK_COUNT_ON_USER_ROLE_QUERY = """
                               UPDATE USERS SET user_locked_ind ='N', failed_logins=2 WHERE USER_ID='autotester'
            
            """;
    public static final String LAST_NAME_FIRST_NAME_QUERY_TC112 = """
            SELECT 'SSP FALL TURN ON RECORD' as recordType, ucbcust_cust_code as customerCode, ucbprem_code as premisesCode, ucbcust_first_name as customerFirstName, ucbcust_middle_name as customerMiddleName, ucbcust_last_name as customerLastNameBusiness, uzbenro_credit_check_name as creditCheckBusinessName, uzbenro_scls_code as customerType, ucbcust_ssn_last_four as lastFourSocialSecurityNumber, ucbprem_street_number as premisesStreetNumber, ucbprem_pdir_code_pre as premisesStreetPreDirection, ucbprem_street_name as premisesStreetName, ucbprem_ssfx_code as premisesStreetSuffix, ucbprem_pdir_code_post as premisesStreetPostDirection, ucbprem_utyp_code as premisesUnitType, ucbprem_unit as premisesUnitNumber, ucbprem_city as premisesCity, ucbprem_stat_code_addr as premisesStateCode, ucbprem_zipc_code as premisesZipCode, ucbprem.ucbprem_tjur_code as  premisesCountyCode, spk_new_acct_pref_util.f_get_acct_status(ucbcust_cust_code,ucbprem_code) as accountStatus, uzbenro_old_acct_num as aglcAccountNumber, uzbenro_enro_status as enrollmentSatus, TO_CHAR(uzbenro_enro_status_date,'YYYYMMDD') as enrollmentStatusDate, UZBENRO_TYPE_CODE as enrollmentType, TO_NUMBER(NULL) as pastDueAmount, TO_NUMBER(NULL) as badDebtAmount, DECODE(uzbenro_price_plan,'PRP','true','false') as prepayPlanIndicator, DECODE(uzbenro_price_plan,'PGB','true','false') as payInAdvanceIndicator, uzbenro_price_plan as pricePlan FROM ucbcust, ucbprem, uzbenro  WHERE  ucbcust_cust_code = uzbenro_cust_code  AND ucbprem_code = uzbenro_prem_code  AND uzbenro_enro_status in ('INCL') AND UZBENRO_SSP_IND ='Y' AND uzbenro_cira_ind = 'N' AND MONTHS_BETWEEN(SYSDATE,uzbenro_activity_date) <= 3  AND NOT EXISTS (SELECT 'X'  FROM uabbdbt  WHERE uabbdbt_transfer_hold_ind='Y'  AND  uabbdbt.uabbdbt_prem_code = uzbenro_prem_code  AND  uabbdbt.uabbdbt_cust_code = uzbenro_cust_code )
            AND NOT EXISTS (  SELECT 1 FROM ucracct  WHERE ucracct_cust_code = uzbenro_cust_code AND ucracct_prem_code = uzbenro_prem_code)
            AND EXISTS (  SELECT 1 FROM uzbsspp WHERE uzbsspp_participant_code = uzbenro_cust_code) AND NOT EXISTS (SELECT 1 FROM uzrsspa  WHERE uzrsspa_cust_code = uzbenro_cust_code  AND uzrsspa_prem_code = uzbenro_prem_code)""";

    public static final String ACCOUNT_NUMBER_E_TYPE_NO_SSP_TC110 = """
            SELECT 'E' as recordType, ucbcust_cust_code as customerCode, ucbprem_code as premisesCode, ucbcust_first_name as customerFirstName, ucbcust_middle_name as customerMiddleName, ucbcust_last_name as customerLastNameBusiness, uzbenro_credit_check_name as creditCheckBusinessName, uzbenro_scls_code as customerType, ucbcust_ssn_last_four as lastFourSocialSecurityNumber, ucbprem_street_number as premisesStreetNumber, ucbprem_pdir_code_pre as premisesStreetPreDirection, ucbprem_street_name as premisesStreetName, ucbprem_ssfx_code as premisesStreetSuffix, ucbprem_pdir_code_post as premisesStreetPostDirection, ucbprem_utyp_code as premisesUnitType, ucbprem_unit as premisesUnitNumber, ucbprem_city as premisesCity, ucbprem_stat_code_addr as premisesStateCode, ucbprem_zipc_code as premisesZipCode, ucbprem.ucbprem_tjur_code as  premisesCountyCode, spk_new_acct_pref_util.f_get_acct_status(ucbcust_cust_code,ucbprem_code) as accountStatus, uzbenro_old_acct_num as aglcAccountNumber, uzbenro_enro_status as enrollmentSatus, TO_CHAR(uzbenro_enro_status_date,'YYYYMMDD') as enrollmentStatusDate, UZBENRO_TYPE_CODE as enrollmentType, TO_NUMBER(NULL) as pastDueAmount, TO_NUMBER(NULL) as badDebtAmount, DECODE(uzbenro_price_plan,'PRP','true','false') as prepayPlanIndicator, DECODE(uzbenro_price_plan,'PGB','true','false') as payInAdvanceIndicator, uzbenro_price_plan as pricePlan FROM ucbcust, ucbprem, uzbenro  WHERE  ucbcust_cust_code = uzbenro_cust_code  AND ucbprem_code = uzbenro_prem_code  AND uzbenro_enro_status in ('INCL','CRDS','PRPY') AND UZBENRO_SSP_IND ='N' AND uzbenro_cira_ind = 'N' AND MONTHS_BETWEEN(SYSDATE,uzbenro_activity_date) <= 3  AND NOT EXISTS (SELECT 'X'  FROM uabbdbt  WHERE uabbdbt_transfer_hold_ind='Y' 
             AND  uabbdbt.uabbdbt_prem_code = uzbenro_prem_code  
             AND  uabbdbt.uabbdbt_cust_code = uzbenro_cust_code )  
     AND ROWNUM < 31 ORDER BY 1,6,4,2
          """;

    public static final String AGLC_ACCOUNT_NUMBER_TC114 = """
            
            select uzbenro_old_acct_num as aglcAccountNumber FROM  uzbenro 
            WHERE  uzbenro_old_acct_num is not NULL and uzbenro_old_acct_num <> 0  ;
            
            """;
    public static final String CUSTOMER_DATA_WITH_TYPE_TC115 = """
            
            SELECT * FROM UCRADDR WHERE UCRADDR_PDIR_CODE_POST IS NOT NULL
                                                     AND UCRADDR_PDIR_CODE_PRE IS NOT NULL AND UCRADDR_PHONE_EXT IS NOT NULL\s""";


    public static final String SSP_INDICATOR_VALUE = """
                SELECT UCRACCT
               FROM UCRACCT
            WHERE UCRACCT_CUST_CODE =?
              \s""";

    private DBQuery() {
    }

}
