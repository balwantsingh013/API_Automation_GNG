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

    public static final String GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_SONP_NON_MASTER = """
                SELECT
                    T1.UCRSCMP_CUST_CODE,
                    T1.UCRSCMP_PREM_CODE
                FROM UCRSCMP T1
                JOIN UZRPLAN T2 ON T1.UCRSCMP_SRAT_CODE = T2.UZRPLAN_SVC_SRAT_CODE
                JOIN UCBCUST T3 ON T1.UCRSCMP_CUST_CODE = T3.UCBCUST_CUST_CODE
                WHERE T1.UCRSCMP_SCTY_CODE = 'GNGSVC'
                  AND T1.UCRSCMP_START_DATE < SYSDATE
                  AND T1.UCRSCMP_END_DATE > SYSDATE
                  AND T2.UZRPLAN_EXPIRATION_DATE > SYSDATE
                  AND T1.UCRSCMP_PLAN_CODE = 'MVS' -- VARIABLE PLAN
                  AND T2.UZRPLAN_CSC_TIER = '02' -- TIER 2
                  AND T3.UCBCUST_PROSPECT_VALUE_SCORE = '101'
                  AND T3.UCBCUST_CURRENT_VALUE_SCORE = '101'
                  AND EXISTS (
                      SELECT 1
                      FROM UCBSVCO T4
                      WHERE T4.UCBSVCO_PREM_CODE = T1.UCRSCMP_PREM_CODE
                        AND T4.UCBSVCO_CUST_CODE = T1.UCRSCMP_CUST_CODE
                        AND T4.UCBSVCO_SOTP_CODE = 'SONP'
                        -- AND T4.UCBSVCO_DATE_CREATED > TO_DATE('08-NOV-2024', 'DD-MON-YYYY')
                        AND T4.UCBSVCO_STUS_CODE IN ('O', 'X', 'C')
                  )
                ORDER BY T1.UCRSCMP_CUST_CODE DESC
                FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_PENDING_REWARDS= """
               SELECT
                   T1.UCRACCT_CUST_CODE,
                   T1.UCRACCT_PREM_CODE
               FROM UCRACCT T1
               JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
               JOIN UCRSERV T3 ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                              AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
               JOIN UCRSCMP T4 ON T1.UCRACCT_CUST_CODE = T4.UCRSCMP_CUST_CODE
                              AND T1.UCRACCT_PREM_CODE = T4.UCRSCMP_PREM_CODE
               WHERE T1.UCRACCT_STATUS_IND = 'A'
                 AND T1.UCRACCT_BILL_PRES_TYPE = 'E'
                 AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO')
                 AND T3.UCRSERV_SCLS_CODE = 'RS'
                 AND T4.UCRSCMP_END_DATE > SYSDATE
                 AND T4.UCRSCMP_START_DATE < SYSDATE
                 AND T4.UCRSCMP_SCTY_CODE = 'COMM'
                 AND EXISTS (
                     SELECT 1
                     FROM GZBRWDS T5
                     WHERE T5.GZBRWDS_CUST_CODE = T1.UCRACCT_CUST_CODE
                       AND T5.GZBRWDS_PREM_CODE = T1.UCRACCT_PREM_CODE
                       AND T5.GZBRWDS_REWARD_ID = '43'
                 )
               ORDER BY T1.UCRACCT_CUST_CODE DESC
                FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_UNAPPLIED_DEPOSIT = """
            SELECT
                T1.UCRSCMP_CUST_CODE,
                T1.UCRSCMP_PREM_CODE
            FROM UCRSCMP T1
            JOIN UCBCUST T2 ON T1.UCRSCMP_CUST_CODE = T2.UCBCUST_CUST_CODE
            WHERE T1.UCRSCMP_SCTY_CODE = 'COMM'
              AND T1.UCRSCMP_START_DATE < SYSDATE
              AND T1.UCRSCMP_END_DATE > SYSDATE
              AND T1.UCRSCMP_PLAN_CODE = 'VML'
              AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('110', '120')
              AND NOT EXISTS (
                  SELECT 1
                  FROM GZBRWDS T3
                  WHERE T3.GZBRWDS_CUST_CODE = T1.UCRSCMP_CUST_CODE
                    AND T3.GZBRWDS_PREM_CODE = T1.UCRSCMP_PREM_CODE
              )
            ORDER BY T1.UCRSCMP_CUST_CODE DESC
                FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_NO_UNAPPLIED_DEPOSIT = """
            SELECT DISTINCT
                T1.UCRSCMP_CUST_CODE,
                T1.UCRSCMP_PREM_CODE
            FROM UCRSCMP T1
            JOIN UCBCUST T2 ON T1.UCRSCMP_CUST_CODE = T2.UCBCUST_CUST_CODE
            JOIN UCRACCT T3 ON T1.UCRSCMP_CUST_CODE = T3.UCRACCT_CUST_CODE
                           AND T1.UCRSCMP_PREM_CODE = T3.UCRACCT_PREM_CODE
            WHERE T1.UCRSCMP_SCTY_CODE = 'COMM'
              AND T1.UCRSCMP_START_DATE < SYSDATE
              AND T1.UCRSCMP_END_DATE > SYSDATE
              AND T1.UCRSCMP_PLAN_CODE = 'MVC'
              AND T1.UCRSCMP_RATE_CLASS = 'RS'
              AND T3.UCRACCT_STATUS_IND = 'A'
              AND NOT EXISTS (
                  SELECT 1
                  FROM GZBRWDS T4
                  WHERE T4.GZBRWDS_CUST_CODE = T1.UCRSCMP_CUST_CODE
                    AND T4.GZBRWDS_PREM_CODE = T1.UCRSCMP_PREM_CODE
              )
            ORDER BY T1.UCRSCMP_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_PGB_EXP_DATE = """
               SELECT
                   T1.UCRSCMP_CUST_CODE,
                   T1.UCRSCMP_PREM_CODE
               FROM UCRSCMP T1
               JOIN UCBCUST T2 ON T1.UCRSCMP_CUST_CODE = T2.UCBCUST_CUST_CODE
               JOIN UCRACCT T4 ON T1.UCRSCMP_CUST_CODE = T4.UCRACCT_CUST_CODE
                              AND T1.UCRSCMP_PREM_CODE = T4.UCRACCT_PREM_CODE
                 WHERE T1.UCRSCMP_START_DATE < SYSDATE
                 AND T1.UCRSCMP_END_DATE > SYSDATE
                 AND T1.UCRSCMP_PLAN_CODE = 'PGB'
                 AND T1.UCRSCMP_RATE_CLASS = 'RS'
                 AND T4.UCRACCT_STATUS_IND = 'A'
               ORDER BY T1.UCRSCMP_CUST_CODE DESC
                FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_MKT_NO_EXP_DATE= """
               SELECT
                   T1.UCRSCMP_CUST_CODE,
                   T1.UCRSCMP_PREM_CODE
               FROM UCRSCMP T1
               JOIN UCBCUST T2 ON T1.UCRSCMP_CUST_CODE = T2.UCBCUST_CUST_CODE
               JOIN UCRACCT T4 ON T1.UCRSCMP_CUST_CODE = T4.UCRACCT_CUST_CODE
                              AND T1.UCRSCMP_PREM_CODE = T4.UCRACCT_PREM_CODE
               WHERE T1.UCRSCMP_SCTY_CODE = 'COMM'
                 AND T1.UCRSCMP_START_DATE < SYSDATE
                 AND T1.UCRSCMP_END_DATE > SYSDATE
                 AND T1.UCRSCMP_PLAN_CODE = 'MKT'
                 AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('100', '101')
                 AND T1.UCRSCMP_RATE_CLASS = 'RS'
                 AND T4.UCRACCT_STATUS_IND = 'A'
               ORDER BY T1.UCRSCMP_CUST_CODE DESC
                FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_ETC= """
              SELECT
                  T1.UCRACCT_CUST_CODE,
                  T1.UCRACCT_PREM_CODE
              FROM UCRACCT T1
              JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
              JOIN UCRSERV T3 ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                             AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
              JOIN UCRSCMP T4 ON T1.UCRACCT_CUST_CODE = T4.UCRSCMP_CUST_CODE
                             AND T1.UCRACCT_PREM_CODE = T4.UCRSCMP_PREM_CODE
              WHERE T1.UCRACCT_STATUS_IND = 'A'
                AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO')
                AND T3.UCRSERV_SCLS_CODE IN ('RS')
                AND T4.UCRSCMP_PLAN_CODE = '24B'
                AND T1.UCRACCT_CYCL_CODE IN ('09', '17')
                AND T4.UCRSCMP_END_DATE > SYSDATE
                AND T4.UCRSCMP_START_DATE < SYSDATE
                AND T4.UCRSCMP_SCTY_CODE = 'COMM'
              ORDER BY T1.UCRACCT_CUST_CODE DESC
                FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_ETC_RGB= """
             SELECT
                 C.UCRACCT_CUST_CODE,
                 C.UCRACCT_PREM_CODE
             FROM UCBPREM B
             JOIN GTRACNU A ON B.UCBPREM_PREM_ID = A.GTRACNU_LDC_PREM_ID
             JOIN UCRACCT C ON B.UCBPREM_CODE = C.UCRACCT_PREM_CODE
             JOIN UCRSERV S ON C.UCRACCT_CUST_CODE = S.UCRSERV_CUST_CODE
                           AND C.UCRACCT_PREM_CODE = S.UCRSERV_PREM_CODE
             JOIN UCRSCMP D ON C.UCRACCT_CUST_CODE = D.UCRSCMP_CUST_CODE
                           AND C.UCRACCT_PREM_CODE = D.UCRSCMP_PREM_CODE
             WHERE B.UCBPREM_LANDLORD_IND IN ('L', 'T')
               AND A.GTRACNU_STATUS = 'A'
               AND C.UCRACCT_STATUS_IND = 'A'
               AND S.UCRSERV_SCLS_CODE = 'RS'
               AND D.UCRSCMP_SCTY_CODE = 'COMM'
               AND D.UCRSCMP_START_DATE < SYSDATE
               AND D.UCRSCMP_END_DATE > SYSDATE
               AND D.UCRSCMP_PLAN_CODE = 'RGB'
               AND NOT EXISTS (
                   SELECT 1
                   FROM GZBRWDS T6
                   WHERE T6.GZBRWDS_CUST_CODE = C.UCRACCT_CUST_CODE
                     AND T6.GZBRWDS_PREM_CODE = C.UCRACCT_PREM_CODE
               )
             ORDER BY C.UCRACCT_CUST_CODE DESC
                FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_GREENER_LIFE= """
            SELECT
                T1.UCRACCT_CUST_CODE,
                T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1
            JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
            JOIN UCRSERV T3 ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                           AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
            JOIN UCRSCMP T5 ON T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
                           AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
            WHERE T1.UCRACCT_STATUS_IND = 'A'
              AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO')
              AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('100')
              AND T3.UCRSERV_SCLS_CODE IN ('RS')
              AND T5.UCRSCMP_PLAN_CODE IN ('MVS')
              AND T5.UCRSCMP_END_DATE > SYSDATE
              AND T5.UCRSCMP_START_DATE < SYSDATE
              AND T5.UCRSCMP_SCTY_CODE = 'CARBAL'
              AND NOT EXISTS (
                  SELECT 1
                  FROM GZBRWDS T7
                  WHERE T7.GZBRWDS_CUST_CODE = T1.UCRACCT_CUST_CODE
                    AND T7.GZBRWDS_PREM_CODE = T1.UCRACCT_PREM_CODE
              )
            ORDER BY T1.UCRACCT_CUST_CODE DESC
                FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_CSV= """
            SELECT
                T1.UCRACCT_CUST_CODE,
                T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1
            JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
            JOIN UCRSERV T3 ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                           AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
            JOIN UCRSCMP T5 ON T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
                           AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
            WHERE T1.UCRACCT_STATUS_IND = 'A'
              AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO')
              AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('100')
              AND T3.UCRSERV_SCLS_CODE IN ('RS')
              AND T5.UCRSCMP_PLAN_CODE IN ('MVS')
              AND T5.UCRSCMP_END_DATE > SYSDATE
              AND T5.UCRSCMP_START_DATE < SYSDATE
              AND T5.UCRSCMP_SCTY_CODE = 'CARBAL'
              AND NOT EXISTS (
                  SELECT 1
                  FROM GZBRWDS T7
                  WHERE T7.GZBRWDS_CUST_CODE = T1.UCRACCT_CUST_CODE
                    AND T7.GZBRWDS_PREM_CODE = T1.UCRACCT_PREM_CODE
              )
            ORDER BY T1.UCRACCT_CUST_CODE DESC
                FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_FINAL= """
            SELECT
                T1.UCRACCT_CUST_CODE,
                T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1
            JOIN UZBENRO T2 ON T1.UCRACCT_CUST_CODE = T2.UZBENRO_CUST_CODE
                           AND T1.UCRACCT_PREM_CODE = T2.UZBENRO_PREM_CODE
            WHERE T1.UCRACCT_STATUS_IND = 'F'
              AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO')
              AND T2.UZBENRO_SCLS_CODE = 'RS'
              AND T1.UCRACCT_CYCL_CODE IN ('14', '07')
              AND NOT EXISTS (
                  SELECT 1
                  FROM UCRSERV T3
                  WHERE T3.UCRSERV_CUST_CODE = T1.UCRACCT_CUST_CODE
                    AND T3.UCRSERV_PREM_CODE = T1.UCRACCT_PREM_CODE
              )
            ORDER BY T1.UCRACCT_CUST_CODE DESC
                FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE= """
            SELECT
                T1.UCRACCT_CUST_CODE,
                T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1
            JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
            JOIN UCRSERV T3 ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                           AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
            JOIN UCRSCMP T5 ON T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
                           AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
            WHERE T1.UCRACCT_STATUS_IND = 'A'
              AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO')
              AND T3.UCRSERV_SCLS_CODE IN ('RS')
              AND T5.UCRSCMP_END_DATE > SYSDATE
              AND T5.UCRSCMP_START_DATE < SYSDATE
              AND T5.UCRSCMP_SCTY_CODE = 'COMM'
            ORDER BY T1.UCRACCT_CUST_CODE DESC
                FETCH FIRST 1 ROWS ONLY
            """;

    public static String GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_SSP_WITH_ETC= """
            SELECT
                UCRACCT.UCRACCT_CUST_CODE,
                UCRACCT.UCRACCT_PREM_CODE
            FROM UZBENRO
            JOIN UCRACCT ON UCRACCT.UCRACCT_CUST_CODE = UZBENRO.UZBENRO_CUST_CODE
                        AND UCRACCT.UCRACCT_PREM_CODE = UZBENRO.UZBENRO_PREM_CODE
            WHERE UZBENRO.UZBENRO_SSP_IND = ?
            AND UCRACCT_STATUS_IND = 'A'
              AND EXISTS (
                  SELECT 1
                  FROM UZRSSPA
                  WHERE UZRSSPA.UZRSSPA_CUST_CODE = UZBENRO.UZBENRO_CUST_CODE
                    AND UZRSSPA.UZRSSPA_PREM_CODE = UZBENRO.UZBENRO_PREM_CODE
              )
              AND F_GET_PLAN_TYPE_IND(UZBENRO.UZBENRO_PRICE_PLAN) IN ('G', 'F')
            ORDER BY UZBENRO.UZBENRO_ACTIVITY_DATE DESC
                FETCH FIRST 1 ROWS ONLY
            """;

    public static String GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_SSP_WITHOUT_ETC= """
            SELECT
                UZBENRO_CUST_CODE,
                UZBENRO_PREM_CODE
            FROM UZBENRO
            WHERE UZBENRO_SSP_IND = ?
              AND EXISTS (
                  SELECT 1
                  FROM UCRACCT
                  WHERE UCRACCT_CUST_CODE = UZBENRO.UZBENRO_CUST_CODE
                    AND UCRACCT_PREM_CODE = UZBENRO.UZBENRO_PREM_CODE
              )
              AND EXISTS (
                  SELECT 1
                  FROM UZRSSPA
                  WHERE UZRSSPA_CUST_CODE = UZBENRO.UZBENRO_CUST_CODE
                    AND UZRSSPA_PREM_CODE = UZBENRO.UZBENRO_PREM_CODE
              )
              AND F_GET_PLAN_TYPE_IND(UZBENRO_PRICE_PLAN) NOT IN ('G', 'F')
            ORDER BY UZBENRO_ACTIVITY_DATE DESC
                FETCH FIRST 1 ROWS ONLY
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

    public static final String SELECT_PASSWORD_EXPIRE_DAYS = """
            SELECT UZRPSTO_PARM_VALUE
            FROM UZRPSTO
            WHERE UZRPSTO_PARM_NAME = 'PASSWORD_EXPIRE_DAYS'
            AND UZRPSTO_OBJECT='SPK_WEB_API'
            """;


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
            UPDATE USERS SET USER_LOCKED_IND ='N', FAILED_LOGINS=0 WHERE USER_ID= ?
            """;
    public static final String EXPIRED_PASSWORD_UPDATE_QUERY = """
            UPDATE USERS
            SET user_locked_ind = 'N', failed_logins = 0,
            PASSWORD_EXPIRE = SYSDATE - 1
            WHERE USER_ID = ?
            """;
    public static final String EXPIRED_PASSWORD_CHECK_QUERY = """
            SELECT CASE WHEN PASSWORD_EXPIRE < SYSDATE THEN 'Y' ELSE 'N' END AS is_expired FROM USERS WHERE USER_ID = 'autotester'
            """;
    public static final String FAILED_LOGIN_COUNTS_FOR_EXPIRED_PASSWORD = """
            SELECT failed_logins FROM USERS WHERE USER_ID = 'autotester'
            """;


    public static final String EXPIRED_PASSWORD_ROLLBACK_QUERY = """
            UPDATE USERS SET USER_LOCKED_IND = 'N', FAILED_LOGINS = 0, PASSWORD_EXPIRE = SYSDATE +30 WHERE USER_ID = ?
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

    public static final String ACCOUNT_NUMBER_E_TYPE_NO_SSP_TC110 ="""
            SELECT C.UZBENRO_CUST_CODE,
                                C.UZBENRO_PREM_CODE
                         FROM   UZBENRO C
                         WHERE  C.UZBENRO_SSP_IND = ?
                         AND    C.UZBENRO_ENRO_STATUS ='INCL'
              FETCH FIRST 1 ROWS ONLY
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

    public static final String SELECT_VALID_USER =
            """
                        SELECT USER_ID
                        FROM USERS
                        WHERE FAILED_LOGINS = 0
                        AND USER_LOCKED_IND = 'N'
                        ORDER BY DBMS_RANDOM.VALUE
                        FETCH FIRST 1 ROWS ONLY
                    """;

    public static final String SELECT_SPECIFIC_USER_DATA =
            """
                        SELECT FAILED_LOGINS, USER_LOCKED_IND FROM USERS WHERE USER_ID = ?
                    """;

    public static final String SELECT_USER_WITH_FAILED_LOGIN_3 = """
                SELECT USER_ID
                FROM USERS
                WHERE FAILED_LOGINS = 3 AND USER_LOCKED_IND = 'N'
                FETCH FIRST 1 ROWS ONLY
            """;

    public static final String UPDATE_FAILED_ATTEMPT_TO_3 = """
                UPDATE USERS SET USER_LOCKED_IND ='N', FAILED_LOGINS=3 WHERE USER_ID= ?
            """;

    public static final String SELECT_UZBPSTO_OBJECT_Value = """
                SELECT UZBPSTO_OBJECT FROM UZBPSTO WHERE UZBPSTO_OBJECT = 'SPK_WEB_API'
            """;

    public static final String SELECT_UZRPSTO_PARM_NAME_Value = """
                SELECT UZRPSTO_OBJECT, UZRPSTO_PARM_VALUE
                FROM UZRPSTO
                WHERE UZRPSTO_PARM_NAME = 'FAILED_LOGINS_TO_LOCK'
            """;

    public static final String UPDATE_TO_LOCK_SPECIFIC_USER = """
            UPDATE USERS SET USER_LOCKED_IND ='Y', FAILED_LOGINS=4 WHERE USER_ID= ?
            """;

    public static final String UPDATE_TO_UNLOCK_SPECIFIC_USER = """
            UPDATE USERS SET USER_LOCKED_IND ='N', FAILED_LOGINS=0 WHERE USER_ID= ?
            """;

    public static final String ACTIVE_RESIDENTIAL_OR_COMMERCIAL_CUSTOMERS = """
            SELECT T1.UCRACCT_CUST_CODE, T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1, UCBCUST T2, UCRSERV T3, UCRSCMP T5
            WHERE T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
              AND T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
              AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
              AND T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
              AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
              AND T1.UCRACCT_STATUS_IND = 'A'
              AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO')
              AND T3.UCRSERV_SCLS_CODE IN (?)
              AND T5.UCRSCMP_END_DATE > SYSDATE
              AND T5.UCRSCMP_START_DATE < SYSDATE
              AND T5.UCRSCMP_SCTY_CODE = 'COMM'
            ORDER BY T1.UCRACCT_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_LASTNAME_AND_ZIPCODE = """
            SELECT T1.UZBENRO_DSM_LAST_NAME, T2.UCRADDR_ZIP
            FROM UZBENRO T1
            JOIN UCRADDR T2 ON T1.UZBENRO_CUST_CODE = T2.UCRADDR_CUST_CODE
            WHERE T1.UZBENRO_DSM_LAST_NAME IS NOT NULL
              AND T2.UCRADDR_ZIP IS NOT NULL
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_FIRSTNAME_LASTNAME_AND_ZIPCODE = """
            SELECT
                T3.UZBENRO_DSM_FIRST_NAME,
                T3.UZBENRO_DSM_LAST_NAME,
                T3.UZBENRO_CUST_CODE,
                T3.UZBENRO_PREM_CODE,
                T4.UCRADDR_ZIP
            FROM UCRSERV T1
            INNER JOIN UCRACCT T2 ON T1.UCRSERV_PREM_CODE = T2.UCRACCT_PREM_CODE
            INNER JOIN UZBENRO T3 ON T2.UCRACCT_PREM_CODE = T3.UZBENRO_PREM_CODE
            INNER JOIN UCRADDR T4 ON T3.UZBENRO_CUST_CODE = T4.UCRADDR_CUST_CODE
            WHERE T1.UCRSERV_CUST_CODE = T2.UCRACCT_CUST_CODE
              AND T1.UCRSERV_PREM_CODE = T2.UCRACCT_PREM_CODE
              AND T1.UCRSERV_SCLS_CODE = ?
              AND T1.UCRSERV_CUST_CODE IS NOT NULL
              AND T2.UCRACCT_STATUS_IND = 'A'
              AND T3.UZBENRO_DSM_FIRST_NAME IS NOT NULL
              AND T3.UZBENRO_DSM_LAST_NAME IS NOT NULL
              AND LENGTH(TRIM(T4.UCRADDR_ZIP)) = 5
            ORDER BY T1.UCRSERV_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            
            """;

    public static final String GET_CUSTOMERBUSINESSNAME_FOR_PASTDUEBALANCE_COMMERCIALACCOUNT = """
            SELECT T2.UCBCUST_LAST_NAME
            FROM UCRACCT T1, UCBCUST T2, UCRSERV T3, UCRSCMP T5
            WHERE T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
              AND T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
              AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
              AND T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
              AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
              AND T1.UCRACCT_STATUS_IND = 'A'
              AND T1.UCRACCT_CYCL_CODE NOT IN 'DEPO'
              AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('200', '220')
              AND T3.UCRSERV_SCLS_CODE = 'CM'
              AND T5.UCRSCMP_PLAN_CODE = 'CVS'
              AND T5.UCRSCMP_END_DATE > SYSDATE
              AND T5.UCRSCMP_START_DATE < SYSDATE
              AND T5.UCRSCMP_SCTY_CODE = 'COMM'
              AND EXISTS (
                SELECT 'X' FROM GZRCBHT T6
                WHERE T1.UCRACCT_CUST_CODE = T6.GZRCBHT_CUST_CODE
                  AND T1.UCRACCT_PREM_CODE = T6.GZRCBHT_PREM_CODE
              )
            ORDER BY T1.UCRACCT_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERBUSINESSNAME_FOR__SONP_COMMERCIALACCOUNT = """
            SELECT T3.UCBCUST_LAST_NAME
            FROM UCRACCT T1
            INNER JOIN UCRSERV T2 ON T1.UCRACCT_CUST_CODE = T2.UCRSERV_CUST_CODE
            INNER JOIN UCBCUST T3 ON T2.UCRSERV_CUST_CODE = T3.UCBCUST_CUST_CODE
            WHERE T1.UCRACCT_STATUS_IND = 'A'
              AND T2.UCRSERV_SCLS_CODE = 'CM'
              AND T1.UCRACCT_CYCL_CODE IN ('06', '13')
              AND EXISTS (
                SELECT 'X'
                FROM UCBSVCO
                WHERE UCBSVCO_PREM_CODE = UCRACCT_PREM_CODE
                  AND UCBSVCO_CUST_CODE = UCRACCT_CUST_CODE
                  AND UCBSVCO_SOTP_CODE = 'SONP'
                  --AND UCBSVCO_DATE_CREATED > ('08-NOV-2024')
                  AND UCBSVCO_STUS_CODE IN ('O', 'X', 'C')
              )
            ORDER BY UCRACCT_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERBUSINESSNAME_FOR_ACTIVEPENDINGREWARD_COMMERCIALACCOUNT = """
            SELECT UCBCUST_LAST_NAME
            FROM UCRACCT T1, UCBCUST T2, UCRSERV T3, UCRSCMP T5
            WHERE T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
              AND T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
              AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
              AND T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
              AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
              AND T1.UCRACCT_STATUS_IND = 'A'
              AND T1.UCRACCT_CYCL_CODE NOT IN 'DEPO'
              AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('200', '220')
              AND T3.UCRSERV_SCLS_CODE = 'CM'
              AND T5.UCRSCMP_PLAN_CODE = 'CVS'
              AND T5.UCRSCMP_END_DATE > SYSDATE
              AND T5.UCRSCMP_START_DATE < SYSDATE
              AND T5.UCRSCMP_SCTY_CODE = 'COMM'
              AND EXISTS (
                SELECT 'X'
                FROM GZBRWDS T6
                WHERE T1.UCRACCT_CUST_CODE = T6.GZBRWDS_CUST_CODE
                  AND T1.UCRACCT_PREM_CODE = T6.GZBRWDS_PREM_CODE
                  AND T6.GZBRWDS_REWARD_ID = '44'
              )
            ORDER BY T1.UCRACCT_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERBUSINESSNAME_FOR_ACTIVE_CM_ETC = """
            SELECT
                T2.UCBCUST_LAST_NAME
            FROM UCRACCT T1
            JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
            JOIN UCRSERV T3 ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                           AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
            JOIN UCRSCMP T5 ON T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
                           AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
            WHERE T1.UCRACCT_STATUS_IND = 'A'
              AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO') -- FIXED SYNTAX
              AND T2.UCBCUST_PROSPECT_VALUE_SCORE = '200'
              AND T3.UCRSERV_SCLS_CODE = 'CM' -- RS RESIDENTIAL OR CM COMMERCIAL
              AND T5.UCRSCMP_PLAN_CODE = 'CFM'
              AND T5.UCRSCMP_END_DATE > SYSDATE
              AND T5.UCRSCMP_START_DATE < SYSDATE
              AND T5.UCRSCMP_SCTY_CODE = 'COMM'
              AND NOT EXISTS (
                  SELECT 1
                  FROM GZBRWDS T6
                  WHERE T6.GZBRWDS_CUST_CODE = T1.UCRACCT_CUST_CODE
                    AND T6.GZBRWDS_PREM_CODE = T1.UCRACCT_PREM_CODE
              )
            ORDER BY T1.UCRACCT_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERBUSINESSNAME_FOR_ACTIVE_CM_No_ETC= """
            SELECT
                T2.UCBCUST_LAST_NAME
            FROM UCRACCT T1
            JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
            JOIN UCRSERV T3 ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                           AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
            JOIN UCRSCMP T4 ON T1.UCRACCT_CUST_CODE = T4.UCRSCMP_CUST_CODE
                           AND T1.UCRACCT_PREM_CODE = T4.UCRSCMP_PREM_CODE
            WHERE T1.UCRACCT_STATUS_IND = 'A'
              AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO')
              AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('200', '220')
              AND T3.UCRSERV_SCLS_CODE = 'CM' -- RS RESIDENTIAL OR CM COMMERCIAL
              AND T4.UCRSCMP_PLAN_CODE = 'CVS'
              AND T4.UCRSCMP_END_DATE > SYSDATE
              AND T4.UCRSCMP_START_DATE < SYSDATE
              AND T4.UCRSCMP_SCTY_CODE = 'COMM'
              AND EXISTS (
                  SELECT 1
                  FROM GZRCBHT T5
                  WHERE T5.GZRCBHT_CUST_CODE = T1.UCRACCT_CUST_CODE
                    AND T5.GZRCBHT_PREM_CODE = T1.UCRACCT_PREM_CODE
              )
            ORDER BY T1.UCRACCT_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERBUSINESSNAME_FOR_ACTIVE_CM_CCV= """
            SELECT
                T2.UCBCUST_LAST_NAME
            FROM UCRACCT T1
            JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
            JOIN UCRSERV T3 ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                           AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
            JOIN UCRSCMP T4 ON T1.UCRACCT_CUST_CODE = T4.UCRSCMP_CUST_CODE
                           AND T1.UCRACCT_PREM_CODE = T4.UCRSCMP_PREM_CODE
            WHERE T1.UCRACCT_STATUS_IND = 'A'
              AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO')
              -- AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('200', '220')
              AND T3.UCRSERV_SCLS_CODE = 'CM' -- RS RESIDENTIAL OR CM COMMERCIAL
              AND T4.UCRSCMP_PLAN_CODE = 'CCV'
              AND T4.UCRSCMP_END_DATE > SYSDATE
              AND T4.UCRSCMP_START_DATE < SYSDATE
              AND T4.UCRSCMP_SCTY_CODE = 'COMM'
              AND NOT EXISTS (
                  SELECT 1
                  FROM GZBRWDS T5
                  WHERE T5.GZBRWDS_CUST_CODE = T1.UCRACCT_CUST_CODE
                    AND T5.GZBRWDS_PREM_CODE = T1.UCRACCT_PREM_CODE
              )
            ORDER BY T1.UCRACCT_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERBUSINESSNAME_FOR_FINAL_CM = """
            SELECT
                T3.UCBCUST_LAST_NAME
            FROM UCRACCT T1
            JOIN UZBENRO T2 ON T1.UCRACCT_CUST_CODE = T2.UZBENRO_CUST_CODE
                           AND T1.UCRACCT_PREM_CODE = T2.UZBENRO_PREM_CODE
            JOIN UCBCUST T3 ON T1.UCRACCT_CUST_CODE = T3.UCBCUST_CUST_CODE
            WHERE T1.UCRACCT_STATUS_IND = 'F'
              AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO')
              AND T2.UZBENRO_SCLS_CODE = 'CM'
              AND NOT EXISTS (
                  SELECT 1
                  FROM UCRSERV T4
                  WHERE T4.UCRSERV_CUST_CODE = T1.UCRACCT_CUST_CODE
                    AND T4.UCRSERV_PREM_CODE = T1.UCRACCT_PREM_CODE
              )
            ORDER BY T1.UCRACCT_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_AGLC_NUMBER_FOR_ACTIVE_RS = """
            SELECT
                T2.GTBTRNH_AGLC_ACCT_NBR
            FROM UZBENRO T1
            JOIN GTBTRNH T2 ON T1.UZBENRO_CUST_CODE = T2.GTBTRNH_CUST_CODE
            JOIN GTRRNDN T3 ON T2.GTBTRNH_SEQ_NUM = T3.GTRRNDN_SEQ_NUM
            JOIN UCRSERV T4 ON T2.GTBTRNH_PREM_CODE = T4.UCRSERV_PREM_CODE
            JOIN UCRACCT T5 ON T4.UCRSERV_CUST_CODE = T5.UCRACCT_CUST_CODE
                           AND T4.UCRSERV_PREM_CODE = T5.UCRACCT_PREM_CODE
            WHERE T1.UZBENRO_SCLS_CODE = 'RS'
              AND T5.UCRACCT_STATUS_IND = 'A'
              AND T3.GTRRNDN_SERV_ORD_NUM IS NOT NULL
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ADDRESS_DETAILS_FOR_ACTIVE_RS = """
            SELECT
                T6.UCRADDR_STREET_NUMBER,
                T6.UCRADDR_STREET_NAME,
                T6.UCRADDR_SSFX_CODE,
                T6.UCRADDR_PDIR_CODE_POST,
                T6.UCRADDR_PDIR_CODE_PRE,
                T6.UCRADDR_UTYP_CODE,
                T6.UCRADDR_UNIT,
                T6.UCRADDR_CITY,
                T6.UCRADDR_STAT_CODE,
                T6.UCRADDR_ZIP
            FROM UZBENRO T1
            JOIN GTBTRNH T2 ON T1.UZBENRO_CUST_CODE = T2.GTBTRNH_CUST_CODE
            JOIN GTRRNDN T3 ON T2.GTBTRNH_SEQ_NUM = T3.GTRRNDN_SEQ_NUM
            JOIN UCRSERV T4 ON T2.GTBTRNH_PREM_CODE = T4.UCRSERV_PREM_CODE
            JOIN UCRACCT T5 ON T4.UCRSERV_CUST_CODE = T5.UCRACCT_CUST_CODE
                           AND T4.UCRSERV_PREM_CODE = T5.UCRACCT_PREM_CODE
            JOIN UCRADDR T6 ON T2.GTBTRNH_CUST_CODE = T6.UCRADDR_CUST_CODE
            JOIN UCRSCMP T7 ON T6.UCRADDR_CUST_CODE= T7.UCRSCMP_CUST_CODE
            WHERE T1.UZBENRO_SCLS_CODE = 'RS'
              AND T5.UCRACCT_STATUS_IND = 'A'
              AND T5.UCRACCT_CYCL_CODE NOT IN ('DEPO')
              AND T4.UCRSERV_SCLS_CODE = 'RS'
              AND T3.GTRRNDN_SERV_ORD_NUM IS NOT NULL
              AND T6.UCRADDR_STREET_NUMBER IS NOT NULL
              AND T6.UCRADDR_STREET_NAME IS NOT NULL
              AND T6.UCRADDR_SSFX_CODE IS NOT NULL
              AND T6.UCRADDR_PDIR_CODE_POST IS NOT NULL
              AND T6.UCRADDR_PDIR_CODE_PRE IS NOT NULL
              AND T6.UCRADDR_UTYP_CODE IS NOT NULL
              AND T6.UCRADDR_UNIT IS NOT NULL
              AND T6.UCRADDR_CITY IS NOT NULL
              AND T6.UCRADDR_STAT_CODE IS NOT NULL
              AND T6.UCRADDR_ZIP IS NOT NULL
              AND LENGTH(TRIM(T6.UCRADDR_ZIP)) = 5
              AND T7.UCRSCMP_END_DATE > SYSDATE
              AND T7.UCRSCMP_START_DATE < SYSDATE
              AND T7.UCRSCMP_SCTY_CODE = 'COMM'
            
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CITY_STATE_ZIP = """
            SELECT
                UCRADDR_STREET_NAME,
                UCRADDR_CITY,
                UCRADDR_STAT_CODE,
                UCRADDR_ZIP
            FROM
                UCRADDR
            WHERE
                UCRADDR_STREET_NAME IS NOT NULL
                AND UCRADDR_CITY IS NOT NULL
                AND UCRADDR_STAT_CODE IS NOT NULL
                AND LENGTH(UCRADDR_ZIP) = 5
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ADDRESS_DETAILS = """
            SELECT
                UCRADDR_STREET_NAME,
                UCRADDR_STREET_NUMBER,
                UCRADDR_PDIR_CODE_PRE,
                UCRADDR_PDIR_CODE_POST,
                UCRADDR_SSFX_CODE,
                UCRADDR_CITY,
                UCRADDR_STAT_CODE,
                UCRADDR_ZIP
            FROM
                UCRADDR
            WHERE
                UCRADDR_STREET_NAME IS NOT NULL
                AND UCRADDR_STREET_NUMBER IS NOT NULL
                AND UCRADDR_PDIR_CODE_PRE IS NOT NULL
                AND UCRADDR_PDIR_CODE_POST IS NOT NULL
                AND UCRADDR_SSFX_CODE IS NOT NULL
                AND UCRADDR_CITY IS NOT NULL
                AND UCRADDR_STAT_CODE IS NOT NULL
                AND LENGTH(UCRADDR_ZIP) = 5
            FETCH FIRST 1 ROWS ONLY
            """;


    public static final String GET_CUSTOMER_CODE_AND_PREMISES_CODE = """
            SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
            FROM UZBENRO
            WHERE UZBENRO_CUST_CODE=?
            AND UZBENRO_PREM_CODE=?
            """;

    public static final String SELECT_PHONE_NUMBER = """
            SELECT UCRTELE_PHONE_AREA,UCRTELE_PHONE_NUMBER FROM UCRTELE
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_CODE_NO_SSP = """
            SELECT c.uzbenro_cust_code,
                   c.uzbenro_prem_code
            FROM   uzbenro c
            WHERE  c.uzbenro_ssp_ind = ?
            AND  EXISTS (
                       SELECT 1
                       FROM   ucracct t
                       WHERE  t.ucracct_cust_code = c.uzbenro_cust_code
                       AND    t.ucracct_status_ind = 'A'
                   )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_LAST_NAME_ZIP_NO_SSP = """
            SELECT T1.UZBENRO_DSM_LAST_NAME,
                   T2.UCRADDR_ZIP
            FROM   UZBENRO T1
            JOIN   UCRADDR T2
                   ON T1.UZBENRO_CUST_CODE = T2.UCRADDR_CUST_CODE
            WHERE  T1.UZBENRO_DSM_LAST_NAME IS NOT NULL
              AND  T2.UCRADDR_ZIP IS NOT NULL
              AND  LENGTH(T2.UCRADDR_ZIP) = 5
              AND  T1.UZBENRO_SSP_IND = ?
              AND  EXISTS (
                       SELECT 1
                       FROM   UCRACCT T
                       WHERE  T.UCRACCT_CUST_CODE = T1.UZBENRO_CUST_CODE
                         AND  T.UCRACCT_STATUS_IND = 'A'
                   )
            FETCH FIRST 1 ROWS ONLY
            """;



    public static final String GET_RECORDS_MATCHING_CUSTOMER_BUSINESS_NAME = """
            SELECT COUNT(*)
            FROM   ucbcust c,
                   ucracct a
            WHERE  c.ucbcust_cust_code = a.ucracct_cust_code
              AND  c.ucbcust_last_name LIKE ?
              AND  NOT EXISTS (
                       SELECT 1
                       FROM   uabbdbt t
                       WHERE  t.uabbdbt_cust_code = c.ucbcust_cust_code
                         AND  t.uabbdbt_transfer_hold_ind = 'Y'
                   )
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES = """
            SELECT
                t2.gtbtrnh_cust_code,
                t2.gtbtrnh_prem_code,
                t2.gtbtrnh_aglc_acct_nbr,
                t3.gtrrndn_serv_ord_num
            FROM
                uzbenro t1
            JOIN
                gtbtrnh t2
                ON t1.uzbenro_cust_code = t2.gtbtrnh_cust_code
            JOIN
                gtrrndn t3
                ON t2.gtbtrnh_seq_num = t3.gtrrndn_seq_num
            JOIN
                ucrserv t4
                ON t2.gtbtrnh_prem_code = t4.ucrserv_prem_code
            JOIN
                ucracct t5
                ON t4.ucrserv_prem_code = t5.ucracct_prem_code
            WHERE
                t1.uzbenro_price_plan = ?
                AND t5.ucracct_status_ind = 'A'
                AND t4.ucrserv_scls_code = ?
                AND t3.gtrrndn_serv_ord_num IS NOT NULL
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_ETC_GPP = """
            SELECT
                T2.GTBTRNH_CUST_CODE,
                T2.GTBTRNH_PREM_CODE,
                T2.GTBTRNH_AGLC_ACCT_NBR,
                T3.GTRRNDN_SERV_ORD_NUM
            FROM
                UZBENRO T1
            JOIN
                GTBTRNH T2 ON T1.UZBENRO_CUST_CODE = T2.GTBTRNH_CUST_CODE
                          AND T1.UZBENRO_PREM_CODE = T2.GTBTRNH_PREM_CODE
            JOIN
                GTRRNDN T3 ON T2.GTBTRNH_SEQ_NUM = T3.GTRRNDN_SEQ_NUM
            JOIN
                UCRSERV T4 ON T2.GTBTRNH_PREM_CODE = T4.UCRSERV_PREM_CODE
            JOIN
                UCRACCT T5 ON T4.UCRSERV_PREM_CODE = T5.UCRACCT_PREM_CODE
                          AND T5.UCRACCT_CUST_CODE = T1.UZBENRO_CUST_CODE
            WHERE
                T1.UZBENRO_SSP_IND = 'N'
                AND T1.UZBENRO_PRICE_PLAN=?
                AND F_GET_PLAN_TYPE_IND(T1.UZBENRO_PRICE_PLAN) IN ('G', 'F')
                AND NOT EXISTS (
                    SELECT 1
                    FROM UZRSSPA R
                    WHERE R.UZRSSPA_CUST_CODE = T1.UZBENRO_CUST_CODE
                      AND R.UZRSSPA_PREM_CODE = T1.UZBENRO_PREM_CODE
                )
                AND T5.UCRACCT_STATUS_IND = 'A'
                AND T4.UCRSERV_SCLS_CODE IN (?)
            ORDER BY
                T1.UZBENRO_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_ETC_GREENER_LIFE = """
            SELECT
                T2.GTBTRNH_CUST_CODE,
                T2.GTBTRNH_PREM_CODE,
                T2.GTBTRNH_AGLC_ACCT_NBR,
                T3.GTRRNDN_SERV_ORD_NUM
            FROM
                UZBENRO T1
            JOIN
                GTBTRNH T2 ON T1.UZBENRO_CUST_CODE = T2.GTBTRNH_CUST_CODE
                         AND T1.UZBENRO_PREM_CODE = T2.GTBTRNH_PREM_CODE
            JOIN
                GTRRNDN T3 ON T2.GTBTRNH_SEQ_NUM = T3.GTRRNDN_SEQ_NUM
            JOIN
                UCRSERV T4 ON T2.GTBTRNH_PREM_CODE = T4.UCRSERV_PREM_CODE
            JOIN
                UCRACCT T5 ON T4.UCRSERV_PREM_CODE = T5.UCRACCT_PREM_CODE
                         AND T5.UCRACCT_CUST_CODE = T1.UZBENRO_CUST_CODE
            JOIN
            	GZRCBHT T6
                ON T6.GZRCBHT_CUST_CODE = T1.UZBENRO_CUST_CODE
            WHERE
                T1.UZBENRO_SSP_IND = 'N'
                AND T6.GZRCBHT_SRAT_CODE = 'CR03'
                AND T1.UZBENRO_PRICE_PLAN = ?
                AND F_GET_PLAN_TYPE_IND(T1.UZBENRO_PRICE_PLAN) IN ('G', 'F')
                AND NOT EXISTS (
                    SELECT 1
                    FROM UZRSSPA R
                    WHERE R.UZRSSPA_CUST_CODE = T1.UZBENRO_CUST_CODE
                      AND R.UZRSSPA_PREM_CODE = T1.UZBENRO_PREM_CODE
                )
                AND T5.UCRACCT_STATUS_IND = 'A'
                AND T4.UCRSERV_SCLS_CODE IN (?)
            ORDER BY
                T1.UZBENRO_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_ETC_ACTIVE_PENDING_REWARDS = """
            SELECT
                T2.GTBTRNH_CUST_CODE,
                T2.GTBTRNH_PREM_CODE,
                T2.GTBTRNH_AGLC_ACCT_NBR,
                T3.GTRRNDN_SERV_ORD_NUM
            FROM
                UZBENRO T1
            JOIN
                GTBTRNH T2 ON T1.UZBENRO_CUST_CODE = T2.GTBTRNH_CUST_CODE
                         AND T1.UZBENRO_PREM_CODE = T2.GTBTRNH_PREM_CODE
            JOIN
                GTRRNDN T3 ON T2.GTBTRNH_SEQ_NUM = T3.GTRRNDN_SEQ_NUM
            JOIN
                GZBRWDS T4 ON T1.UZBENRO_CUST_CODE = T4.GZBRWDS_CUST_CODE
                         AND T1.UZBENRO_PREM_CODE = T4.GZBRWDS_PREM_CODE
            WHERE
                T1.UZBENRO_SSP_IND = 'N'
                AND T1.UZBENRO_PRICE_PLAN = ?
                AND F_GET_PLAN_TYPE_IND(T1.UZBENRO_PRICE_PLAN) IN ('G', 'F')
                AND NOT EXISTS (
                    SELECT 1
                    FROM UZRSSPA R
                    WHERE R.UZRSSPA_CUST_CODE = T1.UZBENRO_CUST_CODE
                      AND R.UZRSSPA_PREM_CODE = T1.UZBENRO_PREM_CODE
                )
                AND EXISTS (
                    SELECT 1
                    FROM GZBRWDS BW
                    JOIN UCRSCMP CMP ON BW.GZBRWDS_CUST_CODE = CMP.UCRSCMP_CUST_CODE
                                    AND BW.GZBRWDS_PREM_CODE = CMP.UCRSCMP_PREM_CODE
                    WHERE BW.GZBRWDS_REWARD_ID = '1'
                )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERCODE_PREM_CODE_ACTIVE_RS_NON_METERED_ACCOUNT = """
            SELECT T1.UCRACCT_CUST_CODE, T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1
            WHERE T1.UCRACCT_STATUS_IND = 'N'
            AND T1.UCRACCT_CUST_CODE <> 999999999
            AND NOT EXISTS (
                SELECT 'X'
                FROM UCRSERV T3
                WHERE T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
            )
            ORDER BY T1.UCRACCT_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERCODE_PREM_CODE_ACTIVE_MSB_ACCOUNT= """
            SELECT T2.UCRACCT_CUST_CODE, T2.UCRACCT_PREM_CODE
            FROM UCRMBIL T1
            JOIN UCRACCT T2
            ON T2.UCRACCT_CUST_CODE = T1.UCRMBIL_CUST_CODE
            WHERE UCRMBIL_STATUS_IND is not null
            AND UCRMBIL_STATUS_IND <> ' '
            AND UCRMBIL_STATUS_IND <> 'T'
            AND UCRMBIL_STATUS_IND <> 'X'
            AND T2.ucracct_status_ind = 'A'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_CEILING_PLAN_ACN_ACTIVE_PENDING_REWARDS= """
            SELECT
                T2.GTBTRNH_CUST_CODE,
                T2.GTBTRNH_PREM_CODE,
                T2.GTBTRNH_AGLC_ACCT_NBR,
                T3.GTRRNDN_SERV_ORD_NUM
            FROM
                UZBENRO T1
            JOIN
                GTBTRNH T2 ON T1.UZBENRO_CUST_CODE = T2.GTBTRNH_CUST_CODE
                          AND T1.UZBENRO_PREM_CODE = T2.GTBTRNH_PREM_CODE
            JOIN
                GTRRNDN T3 ON T2.GTBTRNH_SEQ_NUM = T3.GTRRNDN_SEQ_NUM
            JOIN
                UCRSERV T4 ON T2.GTBTRNH_PREM_CODE = T4.UCRSERV_PREM_CODE
            JOIN
                UCRACCT T5 ON T4.UCRSERV_PREM_CODE = T5.UCRACCT_PREM_CODE
            JOIN
                UCBPREM B ON B.UCBPREM_CODE = T5.UCRACCT_PREM_CODE
            JOIN
                GTRACNU A ON B.UCBPREM_PREM_ID = A.GTRACNU_LDC_PREM_ID
            JOIN
                UCBCUST T6 ON T1.UZBENRO_CUST_CODE = T6.UCBCUST_CUST_CODE
            JOIN
                UCRSCMP T7 ON T7.UCRSCMP_CUST_CODE = T6.UCBCUST_CUST_CODE
            JOIN
                GZBRWDS T8 ON T8.GZBRWDS_CUST_CODE = T7.UCRSCMP_CUST_CODE
            WHERE
                B.UCBPREM_LANDLORD_IND IN ('L', 'T')
                AND A.GTRACNU_STATUS = 'A'
                AND T5.UCRACCT_STATUS_IND = 'A'
                AND T3.GTRRNDN_SERV_ORD_NUM IS NOT NULL
                AND T4.UCRSERV_SCLS_CODE = ?
                AND T5.UCRACCT_CYCL_CODE NOT IN ('DEPO')
                AND T6.UCBCUST_PROSPECT_VALUE_SCORE IN ('100', '101', '102', '103')
                AND T7.UCRSCMP_PLAN_CODE = ?
                AND T7.UCRSCMP_END_DATE > SYSDATE
                AND T7.UCRSCMP_START_DATE < SYSDATE
                AND T7.UCRSCMP_SCTY_CODE = 'COMM'
                AND EXISTS (
                    SELECT 'X'
                    FROM GZBRWDS T8
                    WHERE T7.UCRSCMP_CUST_CODE = T8.GZBRWDS_CUST_CODE
                      AND T7.UCRSCMP_PREM_CODE = T8.GZBRWDS_PREM_CODE
                      AND T8.GZBRWDS_REWARD_ID = '1'
                )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_CEILING_PLAN_PAST_DUE_BALANCE= """
            SELECT
                t2.gtbtrnh_cust_code,
                t2.gtbtrnh_prem_code,
                t2.gtbtrnh_aglc_acct_nbr,
                t3.gtrrndn_serv_ord_num
            FROM
                uzbenro t1
            JOIN
                gtbtrnh t2 ON t1.uzbenro_cust_code = t2.gtbtrnh_cust_code
            JOIN
                gtrrndn t3 ON t2.gtbtrnh_seq_num = t3.gtrrndn_seq_num
            JOIN
                ucrserv t4 ON t2.gtbtrnh_prem_code = t4.ucrserv_prem_code
            JOIN
                ucracct t5 ON t4.ucrserv_prem_code = t5.ucracct_prem_code
            JOIN
                ucbcust t6 ON t5.ucracct_cust_code = t6.ucbcust_cust_code
            JOIN
                ucrscmp t7 ON t5.ucracct_cust_code = t7.ucrscmp_cust_code
            JOIN
                uabopen t8 ON t8.uabopen_cust_code = t5.ucracct_cust_code
            WHERE
                t5.ucracct_status_ind = 'A'
                AND t4.ucrserv_scls_code = ?
                AND t3.gtrrndn_serv_ord_num IS NOT NULL
                AND t5.ucracct_cycl_code NOT IN ('DEPO')
                AND t6.ucbcust_prospect_value_score IN ('100', '101', '102', '103')
                AND t7.ucrscmp_plan_code = ?
                AND t7.ucrscmp_end_date > SYSDATE
                AND t7.ucrscmp_start_date < SYSDATE
                AND t7.ucrscmp_scty_code = 'COMM'
                AND t8.uabopen_srat_code <> 'RDEP'
                AND t8.uabopen_balance_ind = 'P'
                AND t8.uabopen_balance > 200
                AND t8.uabopen_due_date < TRUNC(SYSDATE)
                AND EXISTS (
                    SELECT 'X'
                    FROM ucrscmp t7, ucrserv t4
                    WHERE t7.ucrscmp_plan_code = 'MVS'
                      AND TRUNC(SYSDATE) BETWEEN t7.ucrscmp_start_date AND t7.ucrscmp_end_date
                      AND t7.ucrscmp_scty_code = 'COMM'
                )
            ORDER BY
                t8.uabopen_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_CEILING_PRICE_PLAN= """
            SELECT
                t2.gtbtrnh_cust_code,
                t2.gtbtrnh_prem_code,
                t2.gtbtrnh_aglc_acct_nbr,
                t3.gtrrndn_serv_ord_num
            FROM
                uzbenro t1
            JOIN
                gtbtrnh t2 ON t1.uzbenro_cust_code = t2.gtbtrnh_cust_code
            JOIN
                gtrrndn t3 ON t2.gtbtrnh_seq_num = t3.gtrrndn_seq_num
            JOIN
                ucrserv t4 ON t2.gtbtrnh_prem_code = t4.ucrserv_prem_code
            JOIN
                ucracct t5 ON t4.ucrserv_prem_code = t5.ucracct_prem_code
            JOIN
                ucbcust t6 ON t5.ucracct_cust_code = t6.ucbcust_cust_code
            JOIN
                ucrscmp t7 ON t5.ucracct_cust_code = t7.ucrscmp_cust_code
            WHERE
                t5.ucracct_status_ind = 'A'
                AND t4.ucrserv_scls_code = ?
                AND t3.gtrrndn_serv_ord_num IS NOT NULL
                AND t5.ucracct_cycl_code NOT IN ('DEPO')
                AND t6.ucbcust_prospect_value_score IN ('100', '101', '102', '103')
                AND t7.ucrscmp_plan_code = ?
                AND t7.ucrscmp_end_date > SYSDATE
                AND t7.ucrscmp_start_date < SYSDATE
                AND t7.ucrscmp_scty_code = 'COMM'
            ORDER BY
                t5.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_ACN_WITHOUT_ETC= """
            SELECT
                T2.GTBTRNH_CUST_CODE,
                T2.GTBTRNH_PREM_CODE,
                T2.GTBTRNH_AGLC_ACCT_NBR,
                T3.GTRRNDN_SERV_ORD_NUM
            FROM
                UZBENRO T1
            JOIN
                GTBTRNH T2 ON T1.UZBENRO_CUST_CODE = T2.GTBTRNH_CUST_CODE
                          AND T1.UZBENRO_PREM_CODE = T2.GTBTRNH_PREM_CODE
            JOIN
                GTRRNDN T3 ON T2.GTBTRNH_SEQ_NUM = T3.GTRRNDN_SEQ_NUM
            JOIN
                UCRSERV T4 ON T2.GTBTRNH_PREM_CODE = T4.UCRSERV_PREM_CODE
            JOIN
                UCRACCT T5 ON T4.UCRSERV_PREM_CODE = T5.UCRACCT_PREM_CODE
            JOIN
                UCBPREM B ON B.UCBPREM_CODE = T5.UCRACCT_PREM_CODE
            JOIN
                GTRACNU A ON B.UCBPREM_PREM_ID = A.GTRACNU_LDC_PREM_ID
            WHERE
                B.UCBPREM_LANDLORD_IND IN ('L', 'T')
                AND A.GTRACNU_STATUS = 'A'
                AND T5.UCRACCT_STATUS_IND = 'A'
                AND T1.UZBENRO_PRICE_PLAN = ?
                AND T4.UCRSERV_SCLS_CODE = ?
                AND T3.GTRRNDN_SERV_ORD_NUM IS NOT NULL
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_ETC_SONP = """
            SELECT
                T2.GTBTRNH_CUST_CODE,
                T2.GTBTRNH_PREM_CODE,
                T2.GTBTRNH_AGLC_ACCT_NBR,
                T3.GTRRNDN_SERV_ORD_NUM
            FROM
                UZBENRO T1
            JOIN
                GTBTRNH T2 ON T1.UZBENRO_CUST_CODE = T2.GTBTRNH_CUST_CODE
                         AND T1.UZBENRO_PREM_CODE = T2.GTBTRNH_PREM_CODE
            JOIN
                GTRRNDN T3 ON T2.GTBTRNH_SEQ_NUM = T3.GTRRNDN_SEQ_NUM
            JOIN
                GZBRWDS T4 ON T1.UZBENRO_CUST_CODE = T4.GZBRWDS_CUST_CODE
                         AND T1.UZBENRO_PREM_CODE = T4.GZBRWDS_PREM_CODE
            JOIN
                UCRACCT T5 ON T1.UZBENRO_CUST_CODE = T5.UCRACCT_CUST_CODE
            WHERE
                T1.UZBENRO_SSP_IND = 'N'
                AND T5.UCRACCT_STATUS_IND = 'A'
                AND T1.UZBENRO_PRICE_PLAN = ?
                AND F_GET_PLAN_TYPE_IND(T1.UZBENRO_PRICE_PLAN) IN ('G', 'F')
                AND NOT EXISTS (
                    SELECT 1
                    FROM UZRSSPA R
                    WHERE R.UZRSSPA_CUST_CODE = T1.UZBENRO_CUST_CODE
                      AND R.UZRSSPA_PREM_CODE = T1.UZBENRO_PREM_CODE
                )
                AND EXISTS (
                    SELECT 1
                    FROM UCBSVCO V
                    WHERE V.UCBSVCO_CUST_CODE = T1.UZBENRO_CUST_CODE
                      AND V.UCBSVCO_PREM_CODE = T1.UZBENRO_PREM_CODE
                      AND V.UCBSVCO_SOTP_CODE = 'SONP'
                      AND V.UCBSVCO_STUS_CODE IN ('O', 'X', 'C')
                )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERCODE_PREM_CODE_ACTIVE_NON_METERED_ACCOUNT= """
            SELECT T1.UCRACCT_CUST_CODE, T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1
            JOIN UZBENRO T2
            ON T2.UZBENRO_CUST_CODE = T1.UCRACCT_CUST_CODE
            WHERE T1.UCRACCT_STATUS_IND = 'A'
            AND T2.UZBENRO_SCLS_CODE = 'RS'
            AND NOT EXISTS (
                SELECT 'X'
                FROM UCRSERV T3
                WHERE T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
            )
            ORDER BY T1.UCRACCT_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_INDEXED_PRICE_PLAN= """
            SELECT
                GT.GTBTRNH_CUST_CODE,
                GT.GTBTRNH_PREM_CODE,
                GT.GTBTRNH_AGLC_ACCT_NBR,
                GR.GTRRNDN_SERV_ORD_NUM
            FROM
                UCRACCT T1
            JOIN UCRSERV T3
                ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
               AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
            JOIN UCRSCMP T5
                ON T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
               AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
               JOIN GTBTRNH GT
                ON T5.UCRSCMP_CUST_CODE = GT.GTBTRNH_CUST_CODE
            JOIN GTRRNDN GR
                ON GT.GTBTRNH_SEQ_NUM = GR.GTRRNDN_SEQ_NUM
            WHERE
                T1.UCRACCT_STATUS_IND = 'A'
                AND T1.UCRACCT_CYCL_CODE <> 'DEPO'
                AND T3.UCRSERV_SCLS_CODE = ?
                AND T5.UCRSCMP_PLAN_CODE = ?
                AND T5.UCRSCMP_END_DATE > SYSDATE
                AND T5.UCRSCMP_START_DATE < SYSDATE
                AND T5.UCRSCMP_SCTY_CODE = 'COMM'
                FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_PAST_DUE_BALANCE= """
            SELECT
                GT.GTBTRNH_CUST_CODE,
                GT.GTBTRNH_PREM_CODE,
                GT.GTBTRNH_AGLC_ACCT_NBR,
                GR.GTRRNDN_SERV_ORD_NUM
            FROM
                UZBENRO ZB
            JOIN GTBTRNH GT
                ON ZB.UZBENRO_CUST_CODE = GT.GTBTRNH_CUST_CODE
            JOIN GTRRNDN GR
                ON GT.GTBTRNH_SEQ_NUM = GR.GTRRNDN_SEQ_NUM
            JOIN UCRSERV US
                ON GT.GTBTRNH_PREM_CODE = US.UCRSERV_PREM_CODE
            JOIN UCRACCT UA
                ON US.UCRSERV_PREM_CODE = UA.UCRACCT_PREM_CODE
            JOIN UABOPEN UO
                ON UO.UABOPEN_CUST_CODE = UA.UCRACCT_CUST_CODE
               AND UO.UABOPEN_PREM_CODE = UA.UCRACCT_PREM_CODE
            WHERE
                GR.GTRRNDN_SERV_ORD_NUM IS NOT NULL
                AND US.UCRSERV_SCLS_CODE = ?
                AND UA.UCRACCT_STATUS_IND = 'A'
                AND UO.UABOPEN_SRAT_CODE <> 'RDEP'
                AND UO.UABOPEN_BALANCE_IND = 'P'
                AND UO.UABOPEN_BALANCE > 200
                AND UO.UABOPEN_DUE_DATE < TRUNC(SYSDATE)
                AND EXISTS (
                    SELECT 1
                    FROM UCRSCMP SC
                    JOIN UCRSERV SV
                        ON SV.UCRSERV_CUST_CODE = UO.UABOPEN_CUST_CODE
                       AND SV.UCRSERV_PREM_CODE = UO.UABOPEN_PREM_CODE
                    WHERE
                        SC.UCRSCMP_CUST_CODE = UA.UCRACCT_CUST_CODE
                        AND SC.UCRSCMP_PREM_CODE = UA.UCRACCT_PREM_CODE
                        AND SC.UCRSCMP_PLAN_CODE = ?
                        AND TRUNC(SYSDATE) BETWEEN SC.UCRSCMP_START_DATE AND SC.UCRSCMP_END_DATE
                        AND SC.UCRSCMP_SCTY_CODE = 'COMM'
                )
                AND NOT EXISTS (
                    SELECT 1
                    FROM UZRSSPA RS
                    WHERE RS.UZRSSPA_CUST_CODE = ZB.UZBENRO_CUST_CODE
                      AND RS.UZRSSPA_PREM_CODE = ZB.UZBENRO_PREM_CODE
                )
             FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_UNAPPLIED_DEPOSIT= """
            SELECT
                T2.GTBTRNH_CUST_CODE,
                T2.GTBTRNH_PREM_CODE,
                T2.GTBTRNH_AGLC_ACCT_NBR,
                T3.GTRRNDN_SERV_ORD_NUM
            FROM
                UZBENRO T1
            JOIN GTBTRNH T2
                ON T1.UZBENRO_CUST_CODE = T2.GTBTRNH_CUST_CODE
            JOIN GTRRNDN T3
                ON T2.GTBTRNH_SEQ_NUM = T3.GTRRNDN_SEQ_NUM
            JOIN UCRSERV T4
                ON T2.GTBTRNH_PREM_CODE = T4.UCRSERV_PREM_CODE
            JOIN UCRACCT T5
                ON T4.UCRSERV_PREM_CODE = T5.UCRACCT_PREM_CODE
            WHERE
                 T3.GTRRNDN_SERV_ORD_NUM IS NOT NULL
                AND T4.UCRSERV_SCLS_CODE = ?
                AND T5.UCRACCT_STATUS_IND = 'A'
                AND NOT EXISTS (
                    SELECT 1
                    FROM UZRSSPA R
                    WHERE R.UZRSSPA_CUST_CODE = T1.UZBENRO_CUST_CODE
                      AND R.UZRSSPA_PREM_CODE = T1.UZBENRO_PREM_CODE
                )
                AND EXISTS (
                    SELECT 1
                    FROM UCRSCMP CMP
                    JOIN UCBCUST C
                      ON CMP.UCRSCMP_CUST_CODE = C.UCBCUST_CUST_CODE
                    WHERE CMP.UCRSCMP_CUST_CODE = T1.UZBENRO_CUST_CODE
                      AND CMP.UCRSCMP_SCTY_CODE = 'COMM'
                      AND CMP.UCRSCMP_PLAN_CODE = ?
                      AND CMP.UCRSCMP_START_DATE < SYSDATE
                      AND CMP.UCRSCMP_END_DATE > SYSDATE
                      AND C.UCBCUST_PROSPECT_VALUE_SCORE IN ('110', '120')
                )
            ORDER BY
                T1.UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_ETC_ACN= """
            SELECT
                T2.GTBTRNH_CUST_CODE,
                T2.GTBTRNH_PREM_CODE,
                T2.GTBTRNH_AGLC_ACCT_NBR,
                T3.GTRRNDN_SERV_ORD_NUM
            FROM
                UZBENRO T1
            JOIN
                GTBTRNH T2 ON T1.UZBENRO_CUST_CODE = T2.GTBTRNH_CUST_CODE
                         AND T1.UZBENRO_PREM_CODE = T2.GTBTRNH_PREM_CODE
            JOIN
                GTRRNDN T3 ON T2.GTBTRNH_SEQ_NUM = T3.GTRRNDN_SEQ_NUM
            JOIN
                UCRSERV T4 ON T2.GTBTRNH_PREM_CODE = T4.UCRSERV_PREM_CODE
            JOIN
                UCRACCT T5 ON T4.UCRSERV_PREM_CODE = T5.UCRACCT_PREM_CODE
            JOIN
                UCBPREM B ON B.UCBPREM_CODE = T5.UCRACCT_PREM_CODE
            JOIN
                GTRACNU A ON B.UCBPREM_PREM_ID = A.GTRACNU_LDC_PREM_ID
            WHERE
                B.UCBPREM_LANDLORD_IND IN ('L', 'T')
                AND A.GTRACNU_STATUS = 'A'
                AND T5.UCRACCT_STATUS_IND = 'A'
                AND T1.UZBENRO_PRICE_PLAN = ?
                AND T4.UCRSERV_SCLS_CODE = ?
                AND T3.GTRRNDN_SERV_ORD_NUM IS NOT NULL
                AND T1.UZBENRO_SSP_IND = 'N'
                            AND F_GET_PLAN_TYPE_IND(T1.UZBENRO_PRICE_PLAN) IN ('G', 'F')
                            AND NOT EXISTS (
                                SELECT 1
                                FROM UZRSSPA R
                                WHERE R.UZRSSPA_CUST_CODE = T1.UZBENRO_CUST_CODE
                                  AND R.UZRSSPA_PREM_CODE = T1.UZBENRO_PREM_CODE
                            )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERCODE_PREM_CODE_INACTIVE_NON_METERED_ACCOUNT= """
            SELECT
                T1.UCRACCT_CUST_CODE,
                T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1
            JOIN UZBENRO T2 ON T1.UCRACCT_CUST_CODE = T2.UZBENRO_CUST_CODE
                           AND T1.UCRACCT_PREM_CODE = T2.UZBENRO_PREM_CODE
            WHERE T1.UCRACCT_STATUS_IND = 'I'
              AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO')
              AND T2.UZBENRO_SCLS_CODE = 'RS'
              AND NOT EXISTS (
                  SELECT 1
                  FROM UCRSERV T3
                  WHERE T3.UCRSERV_CUST_CODE = T1.UCRACCT_CUST_CODE
                    AND T3.UCRSERV_PREM_CODE = T1.UCRACCT_PREM_CODE
              )
              AND NOT EXISTS (
                  SELECT 1
                  FROM UCRSCMP T4
                  WHERE T4.UCRSCMP_CUST_CODE = T1.UCRACCT_CUST_CODE
                    AND T4.UCRSCMP_PREM_CODE = T1.UCRACCT_PREM_CODE
              )
            ORDER BY T1.UCRACCT_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERCODE_PREM_CODE_INACTIVE_METERED_ACCOUNT= """
            SELECT
                T1.UCRACCT_CUST_CODE,
                T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1
            JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
            JOIN UCRSERV T3 ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
            JOIN UCRSCMP T5 ON T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
                AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
            WHERE T1.UCRACCT_STATUS_IND = 'I'
                AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO') -- FIXED SYNTAX FOR NOT IN
                AND T3.UCRSERV_SCLS_CODE IN ('RS') -- UPDATE TO APPLICABLE SERVICE CLASS
                AND T3.UCRSERV_STATUS_IND = 'A'
                AND T5.UCRSCMP_END_DATE > SYSDATE
                AND T5.UCRSCMP_START_DATE < SYSDATE
                AND T5.UCRSCMP_SCTY_CODE = 'COMM'
            ORDER BY T1.UCRACCT_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERCODE_PREM_CODE_INACTIVE_ACCOUNT_WITH_BAD_DEBT= """
            SELECT UABOPEN_PREM_CODE, UABOPEN_CUST_CODE
            FROM UABOPEN
            WHERE (UABOPEN_BAD_DEBT_STATUS_CODE IS NULL OR UABOPEN_BAD_DEBT_STATUS_CODE NOT IN ('G', 'H', 'N'))
            GROUP BY UABOPEN_PREM_CODE, UABOPEN_CUST_CODE
            HAVING SUM(UABOPEN_BD_BALANCE) > 0
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERCODE_PREM_CODE_INACTIVE_ACCOUNT_WITH_SONP= """
            SELECT T1.UCRACCT_CUST_CODE, T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1, UCRSERV T2
            WHERE T1.UCRACCT_CUST_CODE = T2.UCRSERV_CUST_CODE
            AND T1.UCRACCT_PREM_CODE = T2.UCRSERV_PREM_CODE
            AND T1.UCRACCT_STATUS_IND = 'I'
            AND T2.UCRSERV_SCLS_CODE = 'RS' -- UPDATE TO APPLICABLE SERVICE CLASS
            AND EXISTS (
                SELECT 'X'
                FROM UCBSVCO
                WHERE UCBSVCO_PREM_CODE = UCRACCT_PREM_CODE
                AND UCBSVCO_CUST_CODE = UCRACCT_CUST_CODE
                AND UCBSVCO_SOTP_CODE = 'SONP'
                AND UCBSVCO_STUS_CODE IN ('O', 'X', 'C')
            )
            ORDER BY UCRACCT_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERCODE_PREM_CODE_RS_NEW_BANKRUPCY= """
            SELECT T1.UCRACCT_CUST_CODE, T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1
            JOIN UZBENRO T2 ON (
                T1.UCRACCT_CUST_CODE = T2.UZBENRO_CUST_CODE
            )
            WHERE T1.UCRACCT_PREM_CODE = 8888888
            AND T2.UZBENRO_SCLS_CODE = 'RS'
            AND T1.UCRACCT_STATUS_IND = 'N'
            ORDER BY T1.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERCODE_PREM_CODE_RS_INACTIVE_BANKRUPCY= """
            SELECT T1.UCRACCT_CUST_CODE, T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1
            JOIN UZBENRO T2 ON (
                T1.UCRACCT_CUST_CODE = T2.UZBENRO_CUST_CODE
            )
            WHERE T1.UCRACCT_PREM_CODE = 8888888
            AND T2.UZBENRO_SCLS_CODE = 'RS'
            AND T1.UCRACCT_STATUS_IND = 'I'
            ORDER BY T1.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERCODE_PREM_CODE_CM_NEW_NON_METERED= """
            SELECT T1.UCRACCT_CUST_CODE, T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1
            JOIN UZBENRO T3 ON T1.UCRACCT_CUST_CODE = T3.UZBENRO_CUST_CODE
                           AND T1.UCRACCT_PREM_CODE = T3.UZBENRO_PREM_CODE
            WHERE T1.UCRACCT_STATUS_IND = 'N'
              AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO')
              AND T3.UZBENRO_SCLS_CODE IN ('CM', 'IN')
              AND NOT EXISTS (
                  SELECT 1
                  FROM UCRSERV T2
                  WHERE T2.UCRSERV_CUST_CODE = T1.UCRACCT_CUST_CODE
                    AND T2.UCRSERV_PREM_CODE = T1.UCRACCT_PREM_CODE
              )
            ORDER BY T1.UCRACCT_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERCODE_PREM_CODE_CM_ACTIVE_NON_METERED= """
            SELECT
                T1.UCRACCT_CUST_CODE,
                T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1, UZBENRO T2
            WHERE T1.UCRACCT_CUST_CODE = T2.UZBENRO_CUST_CODE
            AND T1.UCRACCT_PREM_CODE = T2.UZBENRO_PREM_CODE
            AND T1.UCRACCT_STATUS_IND = 'A'
            AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO')
            AND T2.UZBENRO_SCLS_CODE = 'CM'
            AND NOT EXISTS (
                SELECT 'X'
                FROM UCRSERV T3
                WHERE T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
            )
            AND NOT EXISTS (
                SELECT 'X'
                FROM UCRSCMP T4
                WHERE T1.UCRACCT_CUST_CODE = T4.UCRSCMP_CUST_CODE
                AND T1.UCRACCT_PREM_CODE = T4.UCRSCMP_PREM_CODE
            )
            ORDER BY T1.UCRACCT_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERCODE_PREM_CODE_CM_INACTIVE_METERED= """
            SELECT
                T1.UCRACCT_CUST_CODE,
                T1.UCRACCT_PREM_CODE,
                T3.UCRSERV_SCLS_CODE,
                T1.*
            FROM UCRACCT T1, UCBCUST T2, UCRSERV T3, UCRSCMP T5
            WHERE T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
            AND T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
            AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
            AND T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
            AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
            AND T1.UCRACCT_STATUS_IND = 'I'
            AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO') -- FIXED SYNTAX FOR NOT IN
            AND T3.UCRSERV_SCLS_CODE IN ('CM') -- UPDATE TO APPLICABLE SERVICE CLASS
            AND T3.UCRSERV_STATUS_IND = 'A'
            AND T5.UCRSCMP_END_DATE > SYSDATE
            AND T5.UCRSCMP_START_DATE < SYSDATE
            AND T5.UCRSCMP_SCTY_CODE = 'COMM'
            ORDER BY T1.UCRACCT_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERCODE_PREM_CODE_CM_INACTIVE_BAD_DEBT= """
            SELECT UABBDBT_CUST_CODE, UABBDBT_PREM_CODE
            FROM UABBDBT T1
            JOIN UZBENRO T2
            ON T1.UABBDBT_CUST_CODE= T2.UZBENRO_CUST_CODE
            JOIN UCRACCT T3
            ON T3.UCRACCT_CUST_CODE=T2.UZBENRO_CUST_CODE
            WHERE T2.UZBENRO_SCLS_CODE = 'CM'
            AND T3.UCRACCT_STATUS_IND = 'I'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERCODE_PREM_CODE_CM_INACTIVE_SONP= """
            SELECT T1.UCRACCT_CUST_CODE, T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1, UCRSERV T2
            WHERE T1.UCRACCT_CUST_CODE = T2.UCRSERV_CUST_CODE
            AND T1.UCRACCT_PREM_CODE = T2.UCRSERV_PREM_CODE
            AND T1.UCRACCT_STATUS_IND = 'I'
            AND T2.UCRSERV_SCLS_CODE = 'CM' -- UPDATE TO APPLICABLE SERVICE CLASS
            AND EXISTS (
                SELECT 'X'
                FROM UCBSVCO
                WHERE UCBSVCO_PREM_CODE = UCRACCT_PREM_CODE
                AND UCBSVCO_CUST_CODE = UCRACCT_CUST_CODE
                AND UCBSVCO_SOTP_CODE = 'SONP'
                -- AND UCBSVCO_DATE_CREATED > ('08-NOV-2024') -- INSIDE 10 DAYS OF THE SONP
                AND UCBSVCO_STUS_CODE IN ('O', 'X', 'C')
            )
            ORDER BY UCRACCT_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERCODE_PREM_CODE_CM_INACTIVE_NON_METERED= """
            SELECT
                T1.UCRACCT_CUST_CODE,
                T1.UCRACCT_PREM_CODE,
                T1.*
            FROM UCRACCT T1, UZBENRO T2
            WHERE T1.UCRACCT_CUST_CODE = T2.UZBENRO_CUST_CODE
            AND T1.UCRACCT_PREM_CODE = T2.UZBENRO_PREM_CODE
            AND T1.UCRACCT_STATUS_IND = 'I'
            AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO') -- FIXED SYNTAX FOR NOT IN
            AND T2.UZBENRO_SCLS_CODE = 'CM' -- UPDATE TO APPLICABLE SERVICE CLASS
            AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO') -- REMOVED DUPLICATE CONDITION
            AND NOT EXISTS (
                SELECT 'X'
                FROM UCRSERV T3
                WHERE T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
            )
            AND NOT EXISTS (
                SELECT 'X'
                FROM UCRSCMP T4
                WHERE T1.UCRACCT_CUST_CODE = T4.UCRSCMP_CUST_CODE
                AND T1.UCRACCT_PREM_CODE = T4.UCRSCMP_PREM_CODE
            )
            ORDER BY T1.UCRACCT_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERCODE_PREM_CODE_CM_NEW_BANKRUPCY= """
            SELECT T1.UCRACCT_CUST_CODE, T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1
            JOIN UZBENRO T2 ON (
                T1.UCRACCT_CUST_CODE = T2.UZBENRO_CUST_CODE
            )
            WHERE T1.UCRACCT_PREM_CODE = 8888888
            AND T2.UZBENRO_SCLS_CODE = 'CM'
            AND T1.UCRACCT_STATUS_IND = 'N'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMERCODE_PREM_CODE_CM_INACTIVE_BANKRUPCY= """
            SELECT T1.UCRACCT_CUST_CODE, T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1
            JOIN UZBENRO T2 ON (
                T1.UCRACCT_CUST_CODE = T2.UZBENRO_CUST_CODE
            )
            WHERE T1.UCRACCT_PREM_CODE = 8888888
            AND T2.UZBENRO_SCLS_CODE = 'CM'
            AND T1.UCRACCT_STATUS_IND = 'I'
            FETCH FIRST 1 ROWS ONLY
            """;


    private DBQuery() {
    }

}
