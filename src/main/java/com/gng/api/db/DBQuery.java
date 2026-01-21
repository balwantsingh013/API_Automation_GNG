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

    public static final String GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_WITH_PAYMENT_ARRANGEMENT_PAST_DUE_BALANCE = """            
              SELECT *
                   FROM (
                       SELECT a."UCRACCT_CUST_CODE", a."UCRACCT_PREM_CODE", c.UCRSERV_NUM, u."UABOPEN_BALANCE"
                       FROM ucracct a
                       JOIN ucbprem b ON a."UCRACCT_PREM_CODE" = b.ucbprem_code
                       JOIN ucrserv c ON a."UCRACCT_PREM_CODE" = c.ucrserv_prem_code
                       JOIN UABOPEN u ON a."UCRACCT_PREM_CODE" = u.UABOPEN_PREM_CODE
                       WHERE a.ucracct_status_ind = 'A'
                         AND a.ucracct_cycl_code BETWEEN '01' AND '21'
                         AND c.ucrserv_num = 1
                         AND c.ucrserv_status_ind = 'A'
                         AND u.uabopen_balance > 0
                       ORDER BY a."UCRACCT_CUST_CODE" DESC
                   )
                   WHERE ROWNUM = 1
            """;
public static final String GET_CUSTOMER_AND_PREMISES_WITH_DEFAULTED_PA_ACTIVE_BUDGET = """        
        WITH flags AS (
          SELECT
            a.ucracct_cust_code AS cust_code,
            a.ucracct_prem_code AS prem_code,
            CASE WHEN EXISTS (
                   SELECT 1
                     FROM uabpyar
                    WHERE uabpyar_cust_code = a.ucracct_cust_code
                      AND uabpyar_prem_code = a.ucracct_prem_code
                      AND uabpyar_status    = 'A'
                 ) THEN 'Y' ELSE 'N' END AS activePAInd,
            CASE WHEN NVL(a.ucracct_draft_acct_status,' ') = 'A'
                 THEN 'Y' ELSE 'N' END AS bankDraftInd,
            CASE WHEN EXISTS (
                   SELECT 1
                     FROM uabbudg
                    WHERE uabbudg_cust_code  = a.ucracct_cust_code
                      AND uabbudg_prem_code  = a.ucracct_prem_code
                      AND uabbudg_status_ind = 'A'
                 ) THEN 'Y' ELSE 'N' END AS activeBudgetInd
          FROM ucracct a
        
        )
        SELECT
          a.ucracct_cust_code,
          a.ucracct_prem_code,
          a.ucracct_status_ind AS "accountStatus",
          f.activePAInd        AS "activePAInd",
          f.bankDraftInd       AS "bankDraftInd",
          f.activeBudgetInd    AS "activeBudgetInd",
          (SELECT MAX(ucrcrhs_occurance_date)
             FROM ucrcrhs
            WHERE ucrcrhs_cust_code = a.ucracct_cust_code
              AND ucrcrhs_prem_code = a.ucracct_prem_code
              AND ucrcrhs_ccat_code = 'MPAY') AS "lastDefaultPADate"
        FROM ucracct a
        JOIN flags f
          ON f.cust_code = a.ucracct_cust_code
         AND f.prem_code = a.ucracct_prem_code
        WHERE a.ucracct_status_ind = 'A'
          AND f.activeBudgetInd    = 'Y'
          AND f.activePAInd        = 'N'
          AND f.bankDraftInd       = 'N'
          AND EXISTS (
                SELECT 1
                  FROM ucrcrhs h
                 WHERE h.ucrcrhs_cust_code = a.ucracct_cust_code
                   AND h.ucrcrhs_prem_code = a.ucracct_prem_code
                   AND h.ucrcrhs_ccat_code = 'MPAY'
              )
         ORDER BY a.ucracct_cust_code DESC
              FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_CUSTOMER_AND_PREMISES_CODE_WITH_SONP_PAST_DUE_BALANCE_BAD_DEBT = """    
              WITH params AS (
                    SELECT
                      ?        AS st,
                      ?        AS rate_sched,
                      ?        AS plan_ind,
                      ?        AS plan_code
                    FROM dual
                  )
                  SELECT
                    a.ucracct_cust_code,
                    a.ucracct_prem_code,
                    a.ucracct_status_ind,
                    s.ucrserv_num,
                    s.ucrserv_rate_schedule,
                    c.ucbcust_first_name,
                    c.ucbcust_middle_name,
                    c.ucbcust_last_name,
                    c.ucbcust_ssn_last_four
                  FROM ucracct a
                  JOIN params p       ON 1=1
                  JOIN ucrserv s      ON s.ucrserv_prem_code = a.ucracct_prem_code AND s.ucrserv_num = 1
                  JOIN ucbcust c      ON c.ucbcust_cust_code = a.ucracct_cust_code
                  JOIN ucbprem pr     ON pr.ucbprem_code      = a.ucracct_prem_code
                  LEFT JOIN uzbenro z ON z.uzbenro_prem_code  = a.ucracct_prem_code
                  LEFT JOIN uzvplan v ON v.uzvplan_code       = z.uzbenro_price_plan
                  WHERE a.ucracct_status_ind = p.st
                    AND (p.rate_sched IS NULL OR s.ucrserv_rate_schedule = p.rate_sched)
                    AND (p.plan_ind   IS NULL OR v.uzvplan_pltp_ind      = p.plan_ind)
                    AND (p.plan_code   IS NULL OR v.uzvplan_code      = p.plan_code)
                    AND (
                          EXISTS (
                            SELECT 1
                            FROM uabopen bo
                            WHERE bo.uabopen_cust_code = a.ucracct_cust_code
                              AND bo.uabopen_prem_code = a.ucracct_prem_code
                              AND NVL(bo.uabopen_bd_balance,0) > 0
                              AND (bo.uabopen_bad_debt_status_code IS NULL
                                   OR bo.uabopen_bad_debt_status_code NOT IN ('G','H','N'))
                          )
                          OR EXISTS (
                            SELECT 1
                            FROM uabbdbt bd
                            WHERE bd.uabbdbt_cust_code = a.ucracct_cust_code
                              AND bd.uabbdbt_prem_code = a.ucracct_prem_code
                          )
                        )
                    AND EXISTS (
                          SELECT 1
                          FROM ucrcrhs h
                          WHERE h.ucrcrhs_cust_code = a.ucracct_cust_code
                            AND h.ucrcrhs_prem_code = a.ucracct_prem_code
                            AND h.ucrcrhs_ccat_code = 'SONP'
                            AND h.ucrcrhs_occurance_date <= TRUNC(SYSDATE)
                        )
                    AND EXISTS (
                          SELECT 1
                          FROM usrletd l
                          WHERE l.usrletd_actual_cust_code = a.ucracct_cust_code
                            AND l.usrletd_prem_code        = a.ucracct_prem_code
                            AND l.usrletd_letr_code        = 'DISCONNECT'
                            AND l.usrletd_printed_ind      = 'Y'
                        )
                  FETCH FIRST 1 ROWS ONLY
            """;


    public static final String GET_CUSTOMER_AND_PREMISES_CODE_NO_BILLS_YET = """    
            WITH params AS (
              SELECT
                ? AS st,
                ? AS rate_sched,
                ? AS plan_ind,
                ? AS plan_code
              FROM dual
            )
            SELECT
              a.ucracct_cust_code,
              a.ucracct_prem_code,
              a.ucracct_status_ind,
              s.ucrserv_num,
              s.ucrserv_rate_schedule,
              c.ucbcust_first_name,
              c.ucbcust_middle_name,
              c.ucbcust_last_name,
              c.ucbcust_ssn_last_four
            FROM ucracct a
            JOIN params p         ON 1 = 1
            JOIN ucbcust c        ON c.ucbcust_cust_code = a.ucracct_cust_code
            JOIN ucbprem pr       ON pr.ucbprem_code      = a.ucracct_prem_code
            LEFT JOIN ucrserv s   ON s.ucrserv_cust_code  = a.ucracct_cust_code
                                 AND s.ucrserv_prem_code  = a.ucracct_prem_code
                                 AND s.ucrserv_num        = 1
            LEFT JOIN uzbenro z   ON z.uzbenro_cust_code  = a.ucracct_cust_code
                                 AND z.uzbenro_prem_code  = a.ucracct_prem_code
            LEFT JOIN uzvplan v   ON v.uzvplan_code       = z.uzbenro_price_plan
            WHERE (p.st IS NULL OR a.ucracct_status_ind = p.st)
              AND (NULLIF(p.rate_sched,'') IS NULL OR s.ucrserv_rate_schedule = p.rate_sched)
              AND (p.plan_ind   IS NULL OR v.uzvplan_pltp_ind = p.plan_ind)
              AND (p.plan_code  IS NULL OR v.uzvplan_code     = p.plan_code)
              AND NOT EXISTS (
                    SELECT 1
                    FROM ubbbhst b
                    WHERE b.ubbbhst_cust_code = a.ucracct_cust_code
                      AND b.ubbbhst_prem_code = a.ucracct_prem_code
                  )
            ORDER BY DBMS_RANDOM.VALUE
            FETCH FIRST 1 ROWS ONLY
            
            """;
    public static final String GET_CUSTOMER_INFORMATION_INACTIVE_ABD_ACCOUNT = """            
            SELECT a.ucracct_cust_code, a.ucracct_prem_code
            FROM ucracct a
            WHERE a.ucracct_status_ind = 'I'
              AND F_DOES_WU_CREDIT_CARD_EXIST(a.ucracct_cust_code, a.ucracct_prem_code) = 'Y'
              FETCH FIRST 1 ROWS only
            """;

    public static final String GET_CUSTOMER_INFORMATION_BASED_ON_ACCOUNT_STATUS_AND_PLAN_TYPE = """            
             SELECT
                 a.ucracct_cust_code,
                 a.ucracct_prem_code,
                 a.ucracct_status_ind,
                 s.ucrserv_num,
                 s.ucrserv_rate_schedule,
                 c.ucbcust_first_name,
                 c.ucbcust_middle_name,
                 c.ucbcust_last_name,
                 c.ucbcust_ssn_last_four
             FROM ucracct a
             JOIN ucrserv s   ON s.ucrserv_prem_code = a.ucracct_prem_code AND s.ucrserv_num = 1
             JOIN ucbcust c   ON c.ucbcust_cust_code = a.ucracct_cust_code
             JOIN ucbprem p   ON p.ucbprem_code      = a.ucracct_prem_code
             JOIN uzbenro z   ON z.uzbenro_prem_code = a.ucracct_prem_code
             WHERE a.ucracct_status_ind = ?
               AND z.UZBENRO_PRICE_PLAN  = ?
               AND c.ucbcust_first_name IS NOT NULL
             ORDER BY z.uzbenro_activity_date DESC, DBMS_RANDOM.VALUE
             FETCH FIRST 1 ROW ONLY
            """;
    public static final String GET_TENANT_CUSTOMER_INFORMATION_BASED_ON_ACCOUNT_STATUS_AND_PLAN_TYPE = """            
             SELECT
                 a.ucracct_cust_code,
                 a.ucracct_prem_code,
                 a.ucracct_status_ind,
                 s.ucrserv_num,
                 s.ucrserv_rate_schedule,
                 c.ucbcust_first_name,
                 c.ucbcust_middle_name,
                 c.ucbcust_last_name,
                 c.ucbcust_ssn_last_four
             FROM ucracct a
             JOIN ucrserv s   ON s.ucrserv_prem_code = a.ucracct_prem_code AND s.ucrserv_num = 1
             JOIN ucbcust c   ON c.ucbcust_cust_code = a.ucracct_cust_code
             JOIN ucbprem p   ON p.ucbprem_code      = a.ucracct_prem_code
             JOIN uzbenro z   ON z.uzbenro_prem_code = a.ucracct_prem_code
             WHERE a.ucracct_status_ind = ?
               AND z.UZBENRO_PRICE_PLAN  = ?
               AND p.ucbprem_landlord_ind NOT IN ('L')
            AND  z.uzbenro_enro_status IN ('INCL','ENRO')
            ORDER BY z.uzbenro_activity_date Desc
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMER_INFORMATION_BASED_ON_ACCOUNT_STATUS_AND_PLAN_TYPE_WITH_MIDDLE_NAME = """            
             SELECT
                 a.ucracct_cust_code,
                 a.ucracct_prem_code,
                 a.ucracct_status_ind,
                 s.ucrserv_num,
                 s.ucrserv_rate_schedule,
                 c.ucbcust_first_name,
                 c.ucbcust_middle_name,
                 c.ucbcust_last_name,
                 c.ucbcust_ssn_last_four
             FROM ucracct a
             JOIN ucrserv s   ON s.ucrserv_prem_code = a.ucracct_prem_code AND s.ucrserv_num = 1
             JOIN ucbcust c   ON c.ucbcust_cust_code = a.ucracct_cust_code
             JOIN ucbprem p   ON p.ucbprem_code      = a.ucracct_prem_code
             JOIN uzbenro z   ON z.uzbenro_prem_code = a.ucracct_prem_code
             WHERE a.ucracct_status_ind = ?
             AND z.UZBENRO_PRICE_PLAN   = ?
             <ABD>
             <middleNameNotNull>
             ORDER BY DBMS_RANDOM.VALUE
             FETCH FIRST 1 ROWS ONLY
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

    public static final String GET_ACTIVE_CUSTOMER_WITH_SERVICE_TRANSFER_ENROLLMENT = """
            SELECT
              a.ucracct_cust_code,
                 a.ucracct_prem_code,
                 a.ucracct_status_ind,
                 c.ucbcust_first_name,
                 c.ucbcust_last_name,
                 b.UCBPREM_ZIPC_CODE,
                 z.UZBENRO_TYPE_CODE  AS typeCode,
                 z.UZBENRO_ENRO_STATUS  AS status,
                 z.UZBENRO_ACTIVITY_DATE
             FROM ucracct a
             JOIN uzbenro z  ON z.uzbenro_prem_code = a.ucracct_prem_code
             JOIN ucbcust c  ON c.ucbcust_cust_code = a.ucracct_cust_code
             JOIN ucbprem b ON a.UCRACCT_PREM_CODE = b.UCBPREM_CODE
            WHERE z.UZBENRO_TYPE_CODE = 'SETM'
            AND z.UZBENRO_ENRO_STATUS = 'INCL'
            
            ORDER BY z.UZBENRO_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROW ONLY
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

    public static final String GET_FINAL_CUSTOMER_AND_PREMISES_CODE_RS_ACTIVE_PENDING_REWARDS= """
            SELECT
                T1.UCRACCT_CUST_CODE,
                T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1
            JOIN UCBCUST T2
                ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
            JOIN UCRSCMP T5
                ON T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
                AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
            JOIN UABOPEN T7
                ON T1.UCRACCT_CUST_CODE = T7.UABOPEN_CUST_CODE
            WHERE T1.UCRACCT_STATUS_IND = 'F'
              AND T5.UCRSCMP_END_DATE < SYSDATE
              AND T5.UCRSCMP_START_DATE < SYSDATE
              AND T5.UCRSCMP_SCTY_CODE = 'COMM'
              AND T7.UABOPEN_BALANCE > 0
              AND EXISTS (
                                SELECT 'X'
                                FROM GZBRWDS T8
                                WHERE T5.UCRSCMP_CUST_CODE = T8.GZBRWDS_CUST_CODE
                                  AND T5.UCRSCMP_PREM_CODE = T8.GZBRWDS_PREM_CODE
                            )
                            AND NOT EXISTS (
                              SELECT 1
                              FROM UCBSVCO T4
                              WHERE T4.UCBSVCO_PREM_CODE = T5.UCRSCMP_PREM_CODE
                                AND T4.UCBSVCO_CUST_CODE = T5.UCRSCMP_CUST_CODE
                                AND T4.UCBSVCO_SOTP_CODE = 'SONP'
                                AND T4.UCBSVCO_STUS_CODE IN ('O', 'X', 'C')
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

    public static final String GET_CUSTOMER_AND_PREMISES_CODE_RS_CSV_UNAPPLIED_DEPOSIT = """
            SELECT T1.UCRACCT_CUST_CODE,
                                                                                               T1.UCRACCT_PREM_CODE
                                                                                          FROM UCRACCT T1
                                                                                          JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
                                                                                          JOIN UCRSERV T3 ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                                                                                                         AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
                                                                                          JOIN UCRSCMP T5 ON T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
                                                                                                         AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
                                                                                        WHERE T1.UCRACCT_STATUS_IND = 'A'
                                                                                          AND T1.UCRACCT_CYCL_CODE IN ('04')
                                                                                          AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('10')
                                                                                          AND T5.UCRSCMP_PLAN_CODE = 'RGB'
                                                                                          AND T5.UCRSCMP_START_DATE < SYSDATE
                                                                                          AND T5.UCRSCMP_SCTY_CODE = 'COMM'
                                                                                          AND NOT EXISTS (
                                                                                              SELECT 1
                                                                                                FROM GZBRWDS T8
                                                                                               WHERE T8.GZBRWDS_CUST_CODE = T1.UCRACCT_CUST_CODE
                                                                                                 AND T8.GZBRWDS_PREM_CODE = T1.UCRACCT_PREM_CODE
                                                                                          )
                                                                                        ORDER BY T1.UCRACCT_CUST_CODE DESC
                                                                                        FETCH FIRST 1 ROWS ONLY
          """;

    public static final String GET_CUSTOMER_AND_PREMISES_CODE_RS_CSV_ACTIVE_DISCOUNTS = """
            SELECT
                T1.UCRSCMP_CUST_CODE,
                T1.UCRSCMP_PREM_CODE
            FROM UCRSCMP T1
            JOIN UCBCUST T2 ON T1.UCRSCMP_CUST_CODE = T2.UCBCUST_CUST_CODE
            JOIN UCRACCT T3 ON T1.UCRSCMP_CUST_CODE = T3.UCRACCT_CUST_CODE
            JOIN UCBSVCO T5 ON T5.UCBSVCO_CUST_CODE = T1.UCRSCMP_CUST_CODE
            WHERE T1.UCRSCMP_START_DATE < SYSDATE
              AND T1.UCRSCMP_END_DATE > SYSDATE
              AND T1.UCRSCMP_PLAN_CODE = 'CSV'
              AND T3.UCRACCT_STATUS_IND = 'A'
              AND NOT EXISTS (
                  SELECT 1
                  FROM GZBRWDS T6
                  WHERE T6.GZBRWDS_CUST_CODE = T1.UCRSCMP_CUST_CODE
                    AND T6.GZBRWDS_PREM_CODE = T1.UCRSCMP_PREM_CODE
              )
              ORDER BY T1.UCRSCMP_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_RS_INACTIVE_UNAPPLIED_DEPOSIT = """
            SELECT
                T1.UCRSCMP_CUST_CODE,
                T1.UCRSCMP_PREM_CODE
            FROM UCRSCMP T1
            JOIN UCBCUST T2 ON T1.UCRSCMP_CUST_CODE = T2.UCBCUST_CUST_CODE
            JOIN UCRACCT T3 ON T1.UCRSCMP_CUST_CODE = T3.UCRACCT_CUST_CODE
            WHERE T1.UCRSCMP_SCTY_CODE = 'COMM'
              AND T1.UCRSCMP_START_DATE < SYSDATE
              AND T1.UCRSCMP_END_DATE > SYSDATE
              AND T1.UCRSCMP_PLAN_CODE = 'VML'
              AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('110', '120')
              AND T3.UCRACCT_STATUS_IND = 'I'
              AND NOT EXISTS (
                  SELECT 1
                  FROM GZBRWDS T3
                  WHERE T3.GZBRWDS_CUST_CODE = T1.UCRSCMP_CUST_CODE
                    AND T3.GZBRWDS_PREM_CODE = T1.UCRSCMP_PREM_CODE
              )
            ORDER BY T1.UCRSCMP_CUST_CODE DESC
                FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMER_AND_PREMISES_CODE_RS_NEW_UNAPPLIED_DEPOSIT = """
            SELECT
                T1.UCRSCMP_CUST_CODE,
                T1.UCRSCMP_PREM_CODE
            FROM UCRSCMP T1
            JOIN UCBCUST T2 ON T1.UCRSCMP_CUST_CODE = T2.UCBCUST_CUST_CODE
            JOIN UCRACCT T3 ON T1.UCRSCMP_CUST_CODE = T3.UCRACCT_CUST_CODE
            WHERE T1.UCRSCMP_SCTY_CODE = 'COMM'
              AND T1.UCRSCMP_START_DATE < SYSDATE
              AND T1.UCRSCMP_END_DATE > SYSDATE
              AND T1.UCRSCMP_PLAN_CODE = 'VML'
              AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('110', '120')
              AND T3.UCRACCT_STATUS_IND = 'N'
              AND NOT EXISTS (
                  SELECT 1
                  FROM GZBRWDS T3
                  WHERE T3.GZBRWDS_CUST_CODE = T1.UCRSCMP_CUST_CODE
                    AND T3.GZBRWDS_PREM_CODE = T1.UCRSCMP_PREM_CODE
              )
            ORDER BY T1.UCRSCMP_CUST_CODE DESC
                FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMER_AND_PREMISES_CODE_RS_FINAL_UNAPPLIED_DEPOSIT = """
            SELECT
                                                       T1.UCRSCMP_CUST_CODE,
                                                       T1.UCRSCMP_PREM_CODE
                                                   FROM UCRSCMP T1
                                                   JOIN UCBCUST T2 ON T1.UCRSCMP_CUST_CODE = T2.UCBCUST_CUST_CODE
                                                   JOIN UCRACCT T3 ON T1.UCRSCMP_CUST_CODE = T3.UCRACCT_CUST_CODE
                                                   WHERE T1.UCRSCMP_SCTY_CODE = 'COMM'
                                                     AND T1.UCRSCMP_START_DATE < SYSDATE
                                                     AND T1.UCRSCMP_END_DATE < SYSDATE
                                                     AND T1.UCRSCMP_PLAN_CODE = 'CF6'
                                                     AND T3.UCRACCT_STATUS_IND = 'F'
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

    public static final String GET_INACTIVE_ACCOUNT_WITH_PAST_DUE_NO_SONP= """
            SELECT T1.UCRACCT_CUST_CODE,
                                                      T1.UCRACCT_PREM_CODE
                                                 FROM UCRACCT T1
                                                 JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
                                                 JOIN UCRSCMP T5 ON T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
                                                                AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
                                                 JOIN UABOPEN T7 ON T1.UCRACCT_CUST_CODE = T7.UABOPEN_CUST_CODE
                                               WHERE T1.UCRACCT_STATUS_IND = 'I'
                                                 AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO')
                                                 AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('120')
                                                 AND T5.UCRSCMP_PLAN_CODE = 'RGB'
                                                 AND T5.UCRSCMP_END_DATE < SYSDATE
                                                 AND T5.UCRSCMP_START_DATE < SYSDATE
                                                 AND T5.UCRSCMP_SCTY_CODE = 'COMM'
                                                 AND T7.UABOPEN_BALANCE > 0
                                                 AND EXISTS (
                                                     SELECT 1
                                                       FROM GZBRWDS T8
                                                      WHERE T8.GZBRWDS_CUST_CODE = T1.UCRACCT_CUST_CODE
                                                        AND T8.GZBRWDS_PREM_CODE = T1.UCRACCT_PREM_CODE
                                                 )
                                               ORDER BY T1.UCRACCT_CUST_CODE DESC
                                               FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACTIVE_CUST_PREM_CODE_ACTIVEPAST_DUE_FIXED_PRICE= """
    SELECT T1.UCRACCT_CUST_CODE,
                                   T1.UCRACCT_PREM_CODE
                              FROM UCRACCT T1
                              JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
                              JOIN UCRSERV T3 ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                                             AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
                              JOIN UCRSCMP T5 ON T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
                                             AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
                              JOIN UABOPEN T7 ON T1.UCRACCT_CUST_CODE = T7.UABOPEN_CUST_CODE
                            WHERE T1.UCRACCT_STATUS_IND = 'A'
                              AND T1.UCRACCT_CYCL_CODE IN ('19')
                              AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('230')
                              AND T5.UCRSCMP_PLAN_CODE = 'CCV'
                              AND T5.UCRSCMP_START_DATE < SYSDATE
                              AND T5.UCRSCMP_SCTY_CODE = 'PIPELINE'
                              AND T7.UABOPEN_BALANCE > 0
                              AND NOT EXISTS (
                                  SELECT 1
                                    FROM GZBRWDS T8
                                   WHERE T8.GZBRWDS_CUST_CODE = T1.UCRACCT_CUST_CODE
                                     AND T8.GZBRWDS_PREM_CODE = T1.UCRACCT_PREM_CODE
                              )
                            ORDER BY T1.UCRACCT_CUST_CODE DESC
                            FETCH FIRST 1 ROWS ONLY
""";

    public static final String GET_ACTIVE_CUST_PREM_CODE_ACTIVE_GREENER_PENDING_REWARDS_PAST_DUE= """
            SELECT T1.UCRACCT_CUST_CODE,
                   T1.UCRACCT_PREM_CODE
              FROM UCRACCT T1
              JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
              JOIN UCRSERV T3 ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                             AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
              JOIN UCRSCMP T5 ON T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
                             AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
              JOIN UABOPEN T7 ON T1.UCRACCT_CUST_CODE = T7.UABOPEN_CUST_CODE
            WHERE T1.UCRACCT_STATUS_IND = 'A'
              AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO')
              AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('100')
              AND T3.UCRSERV_SCLS_CODE = 'RS'
              AND T5.UCRSCMP_PLAN_CODE = 'MVS'
              AND T5.UCRSCMP_END_DATE > SYSDATE
              AND T5.UCRSCMP_START_DATE < SYSDATE
              AND T5.UCRSCMP_SCTY_CODE = 'CARBAL'
              AND T7.UABOPEN_BALANCE > 0
              AND NOT EXISTS (
                  SELECT 1
                    FROM GZBRWDS T8
                   WHERE T8.GZBRWDS_CUST_CODE = T1.UCRACCT_CUST_CODE
                     AND T8.GZBRWDS_PREM_CODE = T1.UCRACCT_PREM_CODE
              )
            ORDER BY T1.UCRACCT_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;


    public static final String GET_ACTIVE_CUST_PREM_CODE_ACTIVE_GREENER_PENDING_REWARDS_SONP = """
   SELECT
                                                       T1.UCRACCT_CUST_CODE,
                                                       T1.UCRACCT_PREM_CODE
                                                   FROM UCRACCT T1
                                                   JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
                                                   JOIN UCRSERV T3 ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                                                                  AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
                                                   JOIN UCRSCMP T5 ON T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
                                                                  AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
                                                   JOIN UABOPEN T7 ON T1.UCRACCT_CUST_CODE = T7.UABOPEN_CUST_CODE
                                                   WHERE T1.UCRACCT_STATUS_IND = 'A'
                                                     AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO')
                                                     AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('100')
                                                     AND T3.UCRSERV_SCLS_CODE = 'RS'
                                                     AND T5.UCRSCMP_PLAN_CODE = 'MVS'
                                                     AND T5.UCRSCMP_END_DATE > SYSDATE
                                                     AND T5.UCRSCMP_START_DATE < SYSDATE
                                                     AND T5.UCRSCMP_SCTY_CODE = 'CARBAL'
                                                     AND T7.UABOPEN_BALANCE > 0
                                                     AND EXISTS (
                                                         SELECT 1
                                                         FROM GZBRWDS BW
                                                         JOIN UCRSCMP CMP ON BW.GZBRWDS_CUST_CODE = CMP.UCRSCMP_CUST_CODE
                                                                         AND BW.GZBRWDS_PREM_CODE = CMP.UCRSCMP_PREM_CODE
                                                         WHERE BW.GZBRWDS_REWARD_ID = '1'
                                                           AND CMP.UCRSCMP_CUST_CODE = T1.UCRACCT_CUST_CODE
                                                           AND CMP.UCRSCMP_PREM_CODE = T1.UCRACCT_PREM_CODE
                                                     )
                                                     AND EXISTS (
                                                         SELECT 1
                                                         FROM UCBSVCO SV
                                                         JOIN UZBENRO UZ ON SV.UCBSVCO_PREM_CODE = UZ.UZBENRO_PREM_CODE
                                                                        AND SV.UCBSVCO_CUST_CODE = UZ.UZBENRO_CUST_CODE
                                                         WHERE SV.UCBSVCO_SOTP_CODE = 'SONP'
                                                           AND SV.UCBSVCO_STUS_CODE IN ('O', 'X', 'C')
                                                           AND UZ.UZBENRO_CUST_CODE = T1.UCRACCT_CUST_CODE
                                                           AND UZ.UZBENRO_PREM_CODE = T1.UCRACCT_PREM_CODE
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

    public static String GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_SSP_WITHOUT_ETC_TC105B= """
            SELECT
                UZ.UZBENRO_CUST_CODE,
                UZ.UZBENRO_PREM_CODE
            FROM UZBENRO UZ
            JOIN UCRSERV US ON UZ.UZBENRO_CUST_CODE = US.UCRSERV_CUST_CODE
            WHERE UZ.UZBENRO_SSP_IND = 'N'
              AND US.UCRSERV_SCLS_CODE = 'CM'
              AND NOT EXISTS (
                  SELECT 1
                  FROM UCBSVCO SV
                  WHERE SV.UCBSVCO_PREM_CODE = UZ.UZBENRO_PREM_CODE
                    AND SV.UCBSVCO_CUST_CODE = UZ.UZBENRO_CUST_CODE
                    AND SV.UCBSVCO_SOTP_CODE = 'SONP'
                    AND SV.UCBSVCO_STUS_CODE IN ('O', 'X', 'C')
              )
              AND EXISTS (
                  SELECT 1
                  FROM UCRACCT UA
                  WHERE UA.UCRACCT_CUST_CODE = UZ.UZBENRO_CUST_CODE
                    AND UA.UCRACCT_PREM_CODE = UZ.UZBENRO_PREM_CODE
              )
              AND EXISTS (
                  SELECT 1
                  FROM UZRSSPA RS
                  WHERE RS.UZRSSPA_CUST_CODE = UZ.UZBENRO_CUST_CODE
                    AND RS.UZRSSPA_PREM_CODE = UZ.UZBENRO_PREM_CODE
              )
              AND F_GET_PLAN_TYPE_IND(UZ.UZBENRO_PRICE_PLAN) NOT IN ('G', 'F')
            ORDER BY UZ.UZBENRO_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static String GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_SSP_WITH_ETC= """
            SELECT
                UCRACCT.UCRACCT_CUST_CODE,
                UCRACCT.UCRACCT_PREM_CODE
            FROM UZBENRO
            JOIN UCRACCT ON UCRACCT.UCRACCT_CUST_CODE = UZBENRO.UZBENRO_CUST_CODE
                        AND UCRACCT.UCRACCT_PREM_CODE = UZBENRO.UZBENRO_PREM_CODE
            JOIN UCRSERV ON UZBENRO_CUST_CODE = UCRSERV_CUST_CODE
            WHERE UZBENRO.UZBENRO_SSP_IND = ?
            AND UCRACCT_STATUS_IND = 'A'
            AND UCRSERV_SCLS_CODE IN (?)
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
                UZ.UZBENRO_CUST_CODE,
                UZ.UZBENRO_PREM_CODE
            FROM UZBENRO UZ
            JOIN UCRSERV US ON UZ.UZBENRO_CUST_CODE = US.UCRSERV_CUST_CODE
            WHERE UZ.UZBENRO_SSP_IND = 'N'
              AND US.UCRSERV_SCLS_CODE = 'RS'
              AND NOT EXISTS (
                  SELECT 1
                  FROM UCBSVCO SV
                  WHERE SV.UCBSVCO_PREM_CODE = UZ.UZBENRO_PREM_CODE
                    AND SV.UCBSVCO_CUST_CODE = UZ.UZBENRO_CUST_CODE
                    AND SV.UCBSVCO_SOTP_CODE = 'SONP'
                    AND SV.UCBSVCO_STUS_CODE IN ('O', 'X', 'C')
              )
              AND EXISTS (
                  SELECT 1
                  FROM UCRACCT UA
                  WHERE UA.UCRACCT_CUST_CODE = UZ.UZBENRO_CUST_CODE
                    AND UA.UCRACCT_PREM_CODE = UZ.UZBENRO_PREM_CODE
              )
              AND EXISTS (
                  SELECT 1
                  FROM UZRSSPA RS
                  WHERE RS.UZRSSPA_CUST_CODE = UZ.UZBENRO_CUST_CODE
                    AND RS.UZRSSPA_PREM_CODE = UZ.UZBENRO_PREM_CODE
              )
              AND F_GET_PLAN_TYPE_IND(UZ.UZBENRO_PRICE_PLAN) NOT IN ('G', 'F')
            ORDER BY UZ.UZBENRO_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACCOUNT_INFO_RESPONSE_BY_CUSTOMER_CODE_AND_STATUS = """
            WITH params AS (
              SELECT
                CAST(? AS VARCHAR2(20))  AS cust_code,
                CAST(? AS CHAR(1))       AS st,
                CAST(? AS VARCHAR2(30))  AS rate_sched
              FROM dual
            ),
              cust_prem AS (
                SELECT a.ucracct_cust_code AS cust_code,
                       a.ucracct_prem_code AS prem_code
                FROM   ucracct a
                JOIN   params p ON p.cust_code = a.ucracct_cust_code
                WHERE  a.ucracct_status_ind = p.st
              ),
              bill_last AS (
                SELECT ubbbhst_cust_code AS cust_code,
                       ubbbhst_prem_code AS prem_code,
                       ubbbhst_printed_date AS bill_print_date,
                       ubbbhst_ending_bal   AS bill_end_amount,
                       ubbbhst_tran_num,
                       ROW_NUMBER() OVER (
                         PARTITION BY ubbbhst_cust_code, ubbbhst_prem_code
                         ORDER BY ubbbhst_tran_num DESC
                       ) AS rn
                FROM   ubbbhst
                WHERE  ubbbhst_cancel_ind IS NULL
              ),
              pay_last AS (
                SELECT uabpymt_cust_code AS cust_code,
                       uabpymt_prem_code AS prem_code,
                       uabpymt_pymt_date AS last_payment_date,
                       uabpymt_amount    AS last_payment_amount,
                       ROW_NUMBER() OVER (
                         PARTITION BY uabpymt_cust_code, uabpymt_prem_code
                         ORDER BY uabpymt_pymt_date DESC
                       ) AS rn
                FROM   uabpymt
              ),
              letter_last AS (
                SELECT usrletd_actual_cust_code AS cust_code,
                       usrletd_prem_code        AS prem_code,
                       usrletd_date_1           AS disc_letter_date,
                       usrletd_amount_1         AS disc_letter_amount,
                       ROW_NUMBER() OVER (
                         PARTITION BY usrletd_actual_cust_code, usrletd_prem_code
                         ORDER BY usrletd_date_1 DESC
                       ) AS rn
                FROM   usrletd
                WHERE  usrletd_letr_code IN ('DISCONNECT','PNS_PREPAID1','PNS_PREPAID2')
                  AND  usrletd_printed_ind = 'Y'
              ),
              past_due AS (
                SELECT uabopen_cust_code AS cust_code,
                       uabopen_prem_code AS prem_code,
                       SUM(uabopen_balance) AS past_due_amount,
                       MAX(uabopen_due_date) AS raw_due_date
                FROM   uabopen
                WHERE  uabopen_due_date < TRUNC(SYSDATE)
                GROUP  BY uabopen_cust_code, uabopen_prem_code
              ),
              flags AS (
                   SELECT a.ucracct_cust_code AS cust_code,
                          a.ucracct_prem_code AS prem_code,
                          CASE WHEN EXISTS (
                                 SELECT 1 FROM uabpyar
                                  WHERE uabpyar_cust_code = a.ucracct_cust_code
                                    AND uabpyar_prem_code = a.ucracct_prem_code
                                    AND uabpyar_status    = (SELECT st FROM params)
                               ) THEN 'Y' ELSE 'N' END AS active_pa_ind,
                          CASE WHEN NVL(a.ucracct_draft_acct_status,' ') = (SELECT st FROM params)
                               THEN 'Y' ELSE 'N' END AS bank_draft_ind,
                          CASE WHEN EXISTS (
                                 SELECT 1 FROM uabbudg
                                  WHERE uabbudg_cust_code  = a.ucracct_cust_code
                                    AND uabbudg_prem_code  = a.ucracct_prem_code
                                    AND uabbudg_status_ind = 'A'
                               ) THEN 'Y' ELSE 'N' END AS active_budget_ind,
                          CASE WHEN EXISTS (
                                 SELECT 1 FROM uabbdbt
                                  WHERE uabbdbt_cust_code = a.ucracct_cust_code
                                    AND uabbdbt_prem_code = a.ucracct_prem_code
                               ) THEN 'Y' ELSE 'N' END AS bad_debt_ind,
                          (SELECT F_DOES_WU_CREDIT_CARD_EXIST(a.ucracct_cust_code, a.ucracct_prem_code)
                             FROM dual) AS recurring_cc_ind
                   FROM   ucracct a
                 )            
              SELECT
                a.ucracct_cust_code                                           AS "customerCode",
                a.ucracct_prem_code                                           AS "premisesCode",
                c.ucbcust_first_name                                          AS "custFirstName",
                c.ucbcust_middle_name                                         AS "custMiddleName",
                c.ucbcust_last_name                                           AS "custLastNameBus",
                spk_new_acct_pref_util.f_get_acct_status(a.ucracct_cust_code, a.ucracct_prem_code) AS "accountStatus",
                vs.ucrserv_rate_schedule                                      AS "rateSchedule",
                c.ucbcust_ssn_last_four                                       AS "lastFourSSN",
                p.ucbprem_street_number                                       AS "premStreetNum",
                p.ucbprem_pdir_code_pre                                       AS "premStreetPreDir",
                p.ucbprem_street_name                                         AS "premStreetName",
                p.ucbprem_ssfx_code                                           AS "premStreetSuffix",
                p.ucbprem_pdir_code_post                                      AS "premStreetPostDir",
                p.ucbprem_utyp_code                                           AS "premUnitType",
                p.ucbprem_unit                                                AS "premUnitNum",
                p.ucbprem_city                                                AS "premCity",
                p.ucbprem_stat_code_addr                                      AS "premState",
                p.ucbprem_zipc_code                                           AS "premZip",
                adr.ucraddr_street_number                                     AS "billingStreetNum",
                adr.ucraddr_pdir_code_pre                                     AS "billingStreetPreDir",
                adr.ucraddr_street_name                                       AS "billingStreetName",
                adr.ucraddr_ssfx_code                                         AS "billingStreetSuffix",
                adr.ucraddr_pdir_code_post                                    AS "billingStreetPostDir",
                adr.ucraddr_utyp_code                                         AS "billingUnitType",
                adr.ucraddr_unit                                              AS "billingUnitNum",
                adr.ucraddr_city                                              AS "billingCity",
                adr.ucraddr_stat_code                                         AS "billingState",
                adr.ucraddr_zip                                               AS "billingZip",
                CAST(f_calcarbalance(a.ucracct_cust_code, a.ucracct_prem_code) AS NUMBER(18,2)) AS "billedBalance",
                CAST(NVL(pd.past_due_amount, 0) AS NUMBER(18,2))                                  AS "pastDueAmount",
                CASE WHEN NVL(pd.past_due_amount,0) > 0 THEN pd.raw_due_date ELSE NULL END        AS "pastDueDate",
                bl.bill_print_date                                                                AS "billPrintDate",
                CAST(NVL(bl.bill_end_amount, 0) AS NUMBER(18,2))                                   AS "billEndAmount",
                (SELECT MAX(uabopen_due_date)
                   FROM uabopen
                  WHERE uabopen_bhst_tran_num = bl.ubbbhst_tran_num)                               AS "billDueDate",
                CAST(NVL(py.last_payment_amount, 0) AS NUMBER(18,2))                               AS "lastPaymentAmount",
                py.last_payment_date                                                               AS "lastPaymentDate",
                lt.disc_letter_date                                                                AS "discLetterDate",
                CAST(NVL(lt.disc_letter_amount, 0) AS NUMBER(18,2))                                AS "discLetterAmount",
                fl.active_pa_ind                                                                   AS "activePAInd",
                fl.recurring_cc_ind                                                                AS "recurringCCInd",
                fl.bank_draft_ind                                                                  AS "bankDraftInd",
                fl.active_budget_ind                                                               AS "activeBudgetInd",
                fl.bad_debt_ind                                                                    AS "badDebtInd",
                f_does_active_home_sol_exist(a.ucracct_cust_code, a.ucracct_prem_code)             AS "activeWarrantyInd",
                (SELECT MAX(ucrcrhs_occurance_date) FROM ucrcrhs
                  WHERE ucrcrhs_cust_code = a.ucracct_cust_code AND ucrcrhs_prem_code = a.ucracct_prem_code
                    AND ucrcrhs_ccat_code = 'MPAY' AND ucrcrhs_occurance_date <= TRUNC(SYSDATE))   AS "lastDefaultPADate",
                (SELECT MAX(ucrcrhs_occurance_date) FROM ucrcrhs
                  WHERE ucrcrhs_cust_code = a.ucracct_cust_code AND ucrcrhs_prem_code = a.ucracct_prem_code
                    AND ucrcrhs_ccat_code = 'SONP' AND ucrcrhs_occurance_date <= TRUNC(SYSDATE))   AS "lastSONPDate",
                (SELECT MAX(ucrcrhs_occurance_date) FROM ucrcrhs
                  WHERE ucrcrhs_cust_code = a.ucracct_cust_code AND ucrcrhs_prem_code = a.ucracct_prem_code
                    AND ucrcrhs_ccat_code = 'PREC' AND ucrcrhs_occurance_date <= TRUNC(SYSDATE))   AS "lastPreCollDate"
              FROM cust_prem cp
              JOIN ucracct a   ON a.ucracct_cust_code = cp.cust_code
                                AND a.ucracct_prem_code = cp.prem_code
              JOIN ucbcust c   ON c.ucbcust_cust_code = a.ucracct_cust_code
              JOIN ucbprem p   ON p.ucbprem_code      = a.ucracct_prem_code
              JOIN ucraddr adr ON adr.ucraddr_cust_code = c.ucbcust_cust_code
                                AND adr.ucraddr_status_ind = 'A'
              LEFT JOIN ucrserv vs  ON vs.ucrserv_prem_code = a.ucracct_prem_code
                                     AND vs.ucrserv_num       = 1
              LEFT JOIN uzbenro z   ON z.uzbenro_prem_code = a.ucracct_prem_code
              LEFT JOIN uzvplan vp  ON vp.uzvplan_code     = z.uzbenro_price_plan
              LEFT JOIN bill_last  bl ON bl.cust_code = a.ucracct_cust_code
                                       AND bl.prem_code = a.ucracct_prem_code
                                       AND bl.rn = 1
              LEFT JOIN pay_last   py ON py.cust_code = a.ucracct_cust_code
                                       AND py.prem_code = a.ucracct_prem_code
                                       AND py.rn = 1
              LEFT JOIN letter_last lt ON lt.cust_code = a.ucracct_cust_code
                                        AND lt.prem_code = a.ucracct_prem_code
                                        AND lt.rn = 1
              LEFT JOIN past_due   pd ON pd.cust_code = a.ucracct_cust_code
                                       AND pd.prem_code = a.ucracct_prem_code
              LEFT JOIN flags      fl ON fl.cust_code = a.ucracct_cust_code
                                       AND fl.prem_code = a.ucracct_prem_code
              WHERE
                ( NULLIF((SELECT rate_sched FROM params),'') IS NULL
                  OR vs.ucrserv_rate_schedule = (SELECT rate_sched FROM params) )             
              ORDER BY a.ucracct_status_ind, a.ucracct_established_date DESC, a.ucracct_cust_code
            """;

    public static final String SELECT_NOTE_SEQUENCE_NUMBER = """
            SELECT UCBNOTE_SEQ_NUMBER, UCBNOTE_CUST_CODE, UCBNOTE_PREM_CODE
            FROM UCBNOTE WHERE UCBNOTE_SEQ_NUMBER = ?
            """;

    public static final String SELECT_NOTE_BY_CUSTOMER_CODE = """
            select * from ucbnote  where ucbnote_cust_code  = ?
            """;

    public static final String SELECT_NOTE_BY_SEQUENCE_NUMBER = """
            select * from ucrnote where ucrnote_note_seq_num = ?
            FETCH FIRST 1 ROWS ONLY
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
            SELECT failed_logins FROM USERS WHERE USER_ID=?
            """;

    public static final String PASSWORD_EXPIRATION_SYSDATE_PLUS_45 = """
        SELECT CASE
        WHEN TRUNC(password_expire) = TRUNC(SYSDATE + 45) THEN 'TRUE'
        ELSE 'FALSE'
        END AS password_expiry_status
        FROM users
        WHERE user_id = ?
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
            SELECT CASE WHEN PASSWORD_EXPIRE < SYSDATE THEN 'Y' ELSE 'N' END AS is_expired FROM USERS WHERE USER_ID = ?
            """;

    public static final String FAILED_LOGIN_COUNTS_FOR_EXPIRED_PASSWORD = """
            SELECT failed_logins FROM USERS WHERE USER_ID = 'autotester'
            """;

    public static final String EXPIRED_PASSWORD_ROLLBACK_QUERY = """
            UPDATE USERS SET USER_LOCKED_IND = 'N', FAILED_LOGINS = 0, PASSWORD_EXPIRE = SYSDATE +30 WHERE USER_ID = ?
            """;


    public static final String UPDATE_USER_LOCK_STATUS_QUERY = """
            UPDATE USERS SET user_locked_ind =? , failed_logins=? WHERE USER_ID=?
            """;

    public static final String CHECK_USER_LOCK_STATUS_QUERY = """
            SELECT user_locked_ind FROM users WHERE USER_ID = 'autotester1'
            """;
    public static final String FAILED_LOGIN_COUNTS_FOR_USER_LOCK_STATUS_QUERY = """
            UPDATE users SET user_locked_ind = 'N', failed_logins = 0 WHERE USER_ID = 'autotester'
            """;

    public static final String ROLL_BACK_QUERY_FOR_USER_LOCK_STATUS_QUERY = """
            UPDATE USERS SET user_locked_ind ='N', failed_logins=? WHERE USER_ID=?
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
            SELECT\s
                'SSP FALL TURN ON RECORD' AS recordType,
                ucbcust_cust_code AS customerCode,
                ucbprem_code AS premisesCode,
                ucbcust_first_name AS customerFirstName,
                ucbcust_middle_name AS customerMiddleName,
                ucbcust_last_name AS customerLastNameBusiness,
                uzbenro_credit_check_name AS creditCheckBusinessName,
                uzbenro_scls_code AS customerType,
                ucbcust_ssn_last_four AS lastFourSocialSecurityNumber,
                ucbprem_street_number AS premisesStreetNumber,
                ucbprem_pdir_code_pre AS premisesStreetPreDirection,
                ucbprem_street_name AS premisesStreetName,
                ucbprem_ssfx_code AS premisesStreetSuffix,
                ucbprem_pdir_code_post AS premisesStreetPostDirection,
                ucbprem_utyp_code AS premisesUnitType,
                ucbprem_unit AS premisesUnitNumber,
                ucbprem_city AS premisesCity,
                ucbprem_stat_code_addr AS premisesStateCode,
                ucbprem_zipc_code AS premisesZipCode,
                ucbprem.ucbprem_tjur_code AS premisesCountyCode,
                spk_new_acct_pref_util.f_get_acct_status(ucbcust_cust_code, ucbprem_code) AS accountStatus,
                uzbenro_old_acct_num AS aglcAccountNumber,
                uzbenro_enro_status AS enrollmentStatus,
                TO_CHAR(uzbenro_enro_status_date,'YYYYMMDD') AS enrollmentStatusDate,
                uzbenro_type_code AS enrollmentType,
                TO_NUMBER(NULL) AS pastDueAmount,
                TO_NUMBER(NULL) AS badDebtAmount,
                DECODE(uzbenro_price_plan,'PRP','true','false') AS prepayPlanIndicator,
                DECODE(uzbenro_price_plan,'PGB','true','false') AS payInAdvanceIndicator,
                uzbenro_price_plan AS pricePlan
            FROM\s
                ucbcust,
                ucbprem,
                uzbenro
            WHERE\s
                ucbcust_cust_code = uzbenro_cust_code
                AND ucbprem_code = uzbenro_prem_code
                AND uzbenro_enro_status IN ('INCL')
                AND uzbenro_ssp_ind = 'Y'
                AND uzbenro_cira_ind = 'N'
                AND MONTHS_BETWEEN(SYSDATE, uzbenro_activity_date) <= 3
                AND NOT EXISTS (
                    SELECT 'X'\s
                    FROM uabbdbt\s
                    WHERE\s
                        uabbdbt_transfer_hold_ind = 'Y'
                        AND uabbdbt.uabbdbt_prem_code = uzbenro_prem_code
                        AND uabbdbt.uabbdbt_cust_code = uzbenro_cust_code
                )
                AND NOT EXISTS (
                    SELECT 1\s
                    FROM ucracct\s
                    WHERE\s
                        ucracct_cust_code = uzbenro_cust_code\s
                        AND ucracct_prem_code = uzbenro_prem_code
                )
                AND EXISTS (
                    SELECT 1\s
                    FROM uzbsspp\s
                    WHERE uzbsspp_participant_code = uzbenro_cust_code
                )
                AND NOT EXISTS (
                    SELECT 1\s
                    FROM uzrsspa\s
                    WHERE\s
                        uzrsspa_cust_code = uzbenro_cust_code\s
                        AND uzrsspa_prem_code = uzbenro_prem_code
                )
                FETCH FIRST 1 ROWS ONLY
            """;

    public static final String ACCOUNT_NUMBER_E_TYPE_NO_SSP_TC110 ="""
            SELECT C.UZBENRO_CUST_CODE,
                   C.UZBENRO_PREM_CODE
            FROM   UZBENRO C
            WHERE  C.UZBENRO_SSP_IND = ?
            AND    C.UZBENRO_ENRO_STATUS ='INCL'
            AND C.UZBENRO_SOURCE='PHONE'
            AND C.UZBENRO_PREM_TYPE='NACN'
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
           """;

    public static final String AGLC_ACCOUNT_NUMBER_TC114 = """
            SELECT\s
                uzbenro_old_acct_num AS aglcAccountNumber
            FROM\s
                ucbcust,
                ucbprem,
                uzbenro
            WHERE\s
                ucbcust_cust_code = uzbenro_cust_code
                AND ucbprem_code = uzbenro_prem_code
                AND uzbenro_enro_status IN ('CRDS')
                AND uzbenro_ssp_ind = 'N'
                AND uzbenro_cira_ind = 'N'
                AND MONTHS_BETWEEN(SYSDATE, uzbenro_activity_date) <= 3
                AND NOT EXISTS (
                    SELECT 'X'
                    FROM uabbdbt
                    WHERE\s
                        uabbdbt_transfer_hold_ind = 'Y'
                        AND uabbdbt.uabbdbt_prem_code = uzbenro_prem_code
                        AND uabbdbt.uabbdbt_cust_code = uzbenro_cust_code
                )
                AND uzbenro_old_acct_num IS NOT NULL
                AND uzbenro_old_acct_num <> 0
            FETCH FIRST 1 ROWS ONLY
            """;
    public static final String CUSTOMER_DATA_WITH_TYPE_TC115 = """
            SELECT
                'SSP ACCOUNT INCOMPLETE ENROLLMENT RECORD' AS recordType,
                ucbcust_cust_code AS customerCode,
                ucbprem_code AS premisesCode,
                ucbcust_first_name AS customerFirstName,
                ucbcust_middle_name AS customerMiddleName,
                ucbcust_last_name AS customerLastNameBusiness,
                uzbenro_credit_check_name AS creditCheckBusinessName,
                uzbenro_scls_code AS customerType,
                ucbcust_ssn_last_four AS lastFourSocialSecurityNumber,
                ucbprem_street_number AS premisesStreetNumber,
                ucbprem_pdir_code_pre AS premisesStreetPreDirection,
                ucbprem_street_name AS premisesStreetName,
                ucbprem_ssfx_code AS premisesStreetSuffix,
                ucbprem_pdir_code_post AS premisesStreetPostDirection,
                ucbprem_utyp_code AS premisesUnitType,
                ucbprem_unit AS premisesUnitNumber,
                ucbprem_city AS premisesCity,
                ucbprem_stat_code_addr AS premisesStateCode,
                ucbprem_zipc_code AS premisesZipCode,
                ucbprem.ucbprem_tjur_code AS premisesCountyCode,
                spk_new_acct_pref_util.f_get_acct_status(ucbcust_cust_code, ucbprem_code) AS accountStatus,
                uzbenro_old_acct_num AS aglcAccountNumber,
                uzbenro_enro_status AS enrollmentStatus,
                TO_CHAR(uzbenro_enro_status_date, 'YYYYMMDD') AS enrollmentStatusDate,
                uzbenro_type_code AS enrollmentType,
                TO_NUMBER(NULL) AS pastDueAmount,
                TO_NUMBER(NULL) AS badDebtAmount,
                DECODE(uzbenro_price_plan, 'PRP', 'true', 'false') AS prepayPlanIndicator,
                DECODE(uzbenro_price_plan, 'PGB', 'true', 'false') AS payInAdvanceIndicator,
                uzbenro_price_plan AS pricePlan
            FROM
                ucbcust,
                ucbprem,
                uzbenro
            WHERE
                ucbcust_cust_code = uzbenro_cust_code
                AND ucbprem_code = uzbenro_prem_code
                AND uzbenro_enro_status IN ('INCL')
                AND uzbenro_ssp_ind = 'Y'
                AND uzbenro_cira_ind = 'N'
                AND MONTHS_BETWEEN(SYSDATE, uzbenro_activity_date) <= 3
                AND NOT EXISTS (
                    SELECT 'X'
                    FROM uabbdbt
                    WHERE
                        uabbdbt_transfer_hold_ind = 'Y'
                        AND uabbdbt.uabbdbt_prem_code = uzbenro_prem_code
                        AND uabbdbt.uabbdbt_cust_code = uzbenro_cust_code
                )
                AND NOT EXISTS (
                    SELECT 1
                    FROM ucracct
                    WHERE
                        ucracct_cust_code = uzbenro_cust_code
                        AND ucracct_prem_code = uzbenro_prem_code
                )
                AND EXISTS (
                    SELECT 1
                    FROM uzbsspp
                    WHERE uzbsspp_participant_code <> uzbenro_cust_code
                )
                AND EXISTS (
                    SELECT 1
                    FROM uzrsspa
                    WHERE
                        uzrsspa_cust_code = uzbenro_cust_code
                        AND uzrsspa_prem_code = uzbenro_prem_code
                )
                FETCH FIRST 1 ROWS ONLY
            """;


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
                AND USER_ID NOT LIKE '%,%'
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

    public static final String PAST_DUE_BALANCE_AND_PARTIAL_PAYMENT = """
            SELECT   T2.GZBRTPP_CUST_CODE,
                     T2.GZBRTPP_PREM_CODE,
                     T2.GZBRTPP_PAYMENT_REF,
                     T2.GZBRTPP_AMOUNT,
                     T2.GZBRTPP_AR_TRANS
            FROM     GZBRTPP T2
            JOIN     UABOPEN T1
                ON   T2.GZBRTPP_CUST_CODE = T1.UABOPEN_CUST_CODE
            JOIN     UCRACCT T5
                ON   T5.UCRACCT_CUST_CODE = T2.GZBRTPP_CUST_CODE
            WHERE    T2.GZBRTPP_PAYMENT_REF IS NOT NULL
            AND      T2.GZBRTPP_AMOUNT > 0
            AND      T5.UCRACCT_STATUS_IND = 'A'
            AND      T1.UABOPEN_SRAT_CODE <> 'RDEP'
            AND      T1.UABOPEN_BALANCE_IND = 'P'
            AND      T1.UABOPEN_BALANCE > 200
            AND      T1.UABOPEN_DUE_DATE < TRUNC(SYSDATE)
            AND T2.GZBRTPP_AR_TRANS IS NULL
            AND      EXISTS (
                         SELECT  1
                         FROM    UCRSCMP T3
                         JOIN    UCRSERV T4
                             ON  T4.UCRSERV_CUST_CODE = T1.UABOPEN_CUST_CODE
                             AND T4.UCRSERV_PREM_CODE = T1.UABOPEN_PREM_CODE
                     )
            ORDER BY T1.UABOPEN_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String PAST_DUE_BALANCE_AND_NO_PAYMENT = """
            SELECT   T2.GZBRTPP_CUST_CODE,
                     T2.GZBRTPP_PREM_CODE
            FROM     GZBRTPP T2
            JOIN     UABOPEN T1
                ON   T2.GZBRTPP_CUST_CODE = T1.UABOPEN_CUST_CODE
            JOIN     UCRACCT T5
                ON   T5.UCRACCT_CUST_CODE = T2.GZBRTPP_CUST_CODE
            WHERE    T2.GZBRTPP_PAYMENT_REF IS NOT NULL
            AND      T5.UCRACCT_STATUS_IND = 'A'
            AND GZBRTPP_AR_TRANS IS NOT NULL
            FETCH FIRST 1 ROWS ONLY
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

    public static final String GET_CUST_PREM_CODE_TIER_1= """
            SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
            FROM uzbenro 
            WHERE UZBENRO_CUST_CODE= 5999651
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUST_PREM_CODE_TIER_1_NACN= """
            SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
            FROM uzbenro 
            WHERE UZBENRO_CUST_CODE= 6095374
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACN_RS_TC_253= """
            SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
            FROM uzbenro 
            WHERE UZBENRO_CUST_CODE= 5999651
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_NACN_RS_TC_254= """
            SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
            FROM uzbenro 
            WHERE UZBENRO_CUST_CODE= 6095775
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_NACN_RS_TC_255= """
            SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
            FROM uzbenro 
            WHERE UZBENRO_CUST_CODE= 5562880
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_NACN_RS_TC_256= """
            SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
            FROM uzbenro 
            WHERE UZBENRO_CUST_CODE= 3127775
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_NACN_SR_TC_258= """
            SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
            FROM uzbenro 
            WHERE UZBENRO_CUST_CODE= 3889665
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACN_RS_TC_259= """
            SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
            FROM uzbenro 
            WHERE UZBENRO_CUST_CODE= 6065754
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_NACN_RS_TC_260= """
            SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
            FROM uzbenro 
            WHERE UZBENRO_CUST_CODE= 6084817
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_NACN_SR_TC_262= """
            SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
            FROM uzbenro 
            WHERE UZBENRO_CUST_CODE= 6092310
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACN_RS_TC_263= """
            SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
            FROM uzbenro 
            WHERE UZBENRO_CUST_CODE= 6092310
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_NACN_RS_TC_264= """
            SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
            FROM uzbenro 
            WHERE UZBENRO_CUST_CODE= 6068419
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACN_RS_TC_265= """
            SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
            FROM uzbenro 
            WHERE UZBENRO_CUST_CODE= 6079304
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_NACN_RS_TC_266= """
            SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
            FROM uzbenro 
            WHERE UZBENRO_CUST_CODE= 6081193
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_NACN_RS_TC_267= """
            SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
            FROM uzbenro 
            WHERE UZBENRO_CUST_CODE= 5917691
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_NACN_RS_TC_268= """
            SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
            FROM uzbenro 
            WHERE UZBENRO_CUST_CODE= 4077700
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACN_RS_TC_269= """
            SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
            FROM uzbenro 
            WHERE UZBENRO_CUST_CODE= 5756868
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_NACN_RS_TC_270= """
            SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
            FROM uzbenro 
            WHERE UZBENRO_CUST_CODE= 3024099
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACN_RS_TC_271= """
            SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
            FROM uzbenro 
            WHERE UZBENRO_CUST_CODE= 5530505
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_NACN_RS_TC_273 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 5846423
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_ACN_RS_TC_274 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 5489493
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_ACN_RS_TC_275 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 5683025
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_NACN_RS_TC_276 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 5446398
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_NACN_RS_TC_277 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 5440396
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_ACN_RS_TC_278 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 5604443
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_NACN_RS_TC_279 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 5457237
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_NACN_RS_TC_280 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 5926346
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_ACN_CM_TC_281 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 5320867
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_NACN_CM_TC_282 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 6095968
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_NACN_CM_TC_283 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 6063100
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_ACN_CM_TC_284 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 5496469
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_NACN_RS_TC_285 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 4619972
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_NACN_RS_TC_286 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 5645530
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_NACN_CM_TC_287 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 5897231
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_NACN_CM_TC_288 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 4726305
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_NACN_RS_TC_289 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 5624822
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_NACN_RS_TC_290 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 1790794
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_NACN_RS_TC_291 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 6004998
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_NACN_RS_TC_292 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 4542311
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_NACN_RS_TC_293 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 6012004
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_NACN_RS_TC_294 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 5966978
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_NACN_RS_TC_295 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 5930052
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_NACN_RS_TC_296 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 574944
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_NACN_RS_TC_297 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 3480662
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_ACN_RS_TC_298 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 6095812
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;


    public static final String GET_NACN_RS_TC_300 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 4736615
        AND UZBENRO_PREM_CODE=4743029
        ORDER BY UZBENRO_CUST_CODE DESC
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_NACN_CM_TC_301 = """
        SELECT UZBENRO_CUST_CODE, UZBENRO_PREM_CODE
        FROM uzbenro 
        WHERE UZBENRO_CUST_CODE= 4952313
        ORDER BY UZBENRO_CUST_CODE DESC
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
            FROM UCRACCT T1, UCBCUST T2, UCRSERV T3, UCRSCMP T5,UABOPEN T7
            WHERE T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
              AND T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
              AND T1.UCRACCT_CUST_CODE = T7.UABOPEN_CUST_CODE
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
              AND T7.UABOPEN_BALANCE > 200
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

    public static final String GET_CUSTOMERBUSINESSNAME_FOR_ACTIVE_CM_NO_ETC = """
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
            ORDER BY UCBCUST_LAST_NAME DESC
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
            JOIN UCBSVCO T5 ON T5.UCBSVCO_CUST_CODE= T1.UCRACCT_CUST_CODE
            WHERE T1.UCRACCT_STATUS_IND = 'F'
              AND T1.UCRACCT_CYCL_CODE NOT IN ('DEPO')
              AND T2.UZBENRO_SCLS_CODE = 'CM'
              AND T5.ucbsvco_sotp_code <> 'SONP'
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
                UCRADDR_STREET_NAME='YORKTOWN'
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
            WHERE UCRTELE_TELE_CODE=?
            AND UCRTELE_STATUS_IND=?
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_CODE_NOT_IN_SSP_PARTICIPANT_PARENT_TABLE= """
            SELECT T1.UZRSSPA_CUST_CODE, T1.UZRSSPA_PREM_CODE
            FROM UZRSSPA T1
             WHERE NOT EXISTS (
                   SELECT 1
                   FROM   uzbsspp T
                   WHERE  T1.UZRSSPA_CUST_CODE= T.UZBSSPP_PARTICIPANT_CODE
                                            )
            AND LENGTH(T1.UZRSSPA_PREM_CODE)=7
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_CODE_SSP_PARTICIPANT_CODE= """
            SELECT T1.UZRSSPA_CUST_CODE, T1.UZRSSPA_PREM_CODE
            FROM UZRSSPA T1
            JOIN UZBSSPP T2
            ON T1.UZRSSPA_CUST_CODE= T2.UZBSSPP_PARTICIPANT_CODE
            WHERE T1.UZRSSPA_CUST_CODE= T2.UZBSSPP_PARTICIPANT_CODE
            AND T2.UZBSSPP_STATUS=?
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

    public static final String SELECT_ENROLLMENT_RECORD_DATA_FOR_GETELIGIBLE_PLANS= """
            SELECT
                BEN.UZBENRO_CUST_CODE
            FROM UZBENRO BEN
                LEFT JOIN GZRGBPH GZR
                    ON BEN.UZBENRO_CUST_CODE = GZR.GZRGBPH_CUST_CODE
                LEFT JOIN UCBCUST CUS
                    ON BEN.UZBENRO_CUST_CODE = CUS.UCBCUST_CUST_CODE
                LEFT JOIN UCBPREM PRE
                    ON BEN.UZBENRO_PREM_CODE = PRE.UCBPREM_CODE
                LEFT JOIN UCRADDR ADR
                    ON BEN.UZBENRO_CUST_CODE = ADR.UCRADDR_CUST_CODE
                    LEFT JOIN UCRCHST UCR
                    ON BEN.UZBENRO_CUST_CODE = UCR.UCRCHST_CUST_CODE
            WHERE BEN.UZBENRO_CUST_CODE= ?
            AND BEN.UZBENRO_PREM_CODE= ?
            AND BEN.UZBENRO_ENRO_STATUS='INCL'
            AND BEN.UZBENRO_SCLS_CODE='RS'
            AND CUS.UCBCUST_FIRST_NAME= ?
            AND PRE.UCBPREM_ZIPC_CODE= ?
            AND UCR.UCRCHST_LAST_NAME= ?
            AND UCR.UCRCHST_FIRST_NAME= ?
            AND GZR.GZRGBPH_APP_REQUEST_CODE='OMSENRL'
            AND GZR.GZRGBPH_RESP_MESSAGE='Successful Quote'
            AND BEN.UZBENRO_CUST_NAME= ?
            AND BEN.UZBENRO_PREM_TYPE='NACN'
            AND BEN.UZBENRO_SOURCE='MAIL'
            AND BEN.UZBENRO_AGLC_PREM_ID= ?
            AND BEN.UZBENRO_LANDLORD_TENANT='T'
            AND CUS.UCBCUST_STATUS_IND='A'
            AND CUS.UCBCUST_LAST_NAME= ?
            AND PRE.UCBPREM_CITY= ?
            AND PRE.UCBPREM_STREET_NUMBER= ?
            AND PRE.UCBPREM_STAT_CODE_ADDR= ?
            AND ADR.UCRADDR_CITY= ?
            AND ADR.UCRADDR_STREET_NUMBER= ?
            AND ADR.UCRADDR_STAT_CODE= ?
            AND ADR.UCRADDR_ZIP= ?
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_LAST_NAME_ZIP_NO_SSP = """
            SELECT   T3.UZBENRO_DSM_LAST_NAME,
                     T4.UCRADDR_ZIP
            FROM     UZRSSPA T1
            JOIN     UZBSSPP T2
                ON   T1.UZRSSPA_CUST_CODE = T2.UZBSSPP_PARTICIPANT_CODE
            JOIN     UZBENRO T3
                ON   T3.UZBENRO_CUST_CODE = T1.UZRSSPA_CUST_CODE
            JOIN     UCRADDR T4
                ON   T4.UCRADDR_CUST_CODE = T3.UZBENRO_CUST_CODE
            WHERE    T2.UZBSSPP_STATUS = 'A'
            AND      T3.UZBENRO_DSM_LAST_NAME IS NOT NULL
            AND      LENGTH(T4.UCRADDR_ZIP) = 5
            AND      T3.UZBENRO_SSP_IND = ?
            AND      EXISTS (
                         SELECT 1
                         FROM   UCRACCT
                         WHERE  UCRACCT_CUST_CODE = T3.UZBENRO_CUST_CODE
                         AND    UCRACCT_PREM_CODE = T3.UZBENRO_PREM_CODE
                     )
            AND      EXISTS (
                         SELECT 1
                         FROM   UZRSSPA
                         WHERE  UZRSSPA_CUST_CODE = T3.UZBENRO_CUST_CODE
                         AND    UZRSSPA_PREM_CODE = T3.UZBENRO_PREM_CODE
                     )
            AND      F_GET_PLAN_TYPE_IND(T3.UZBENRO_PRICE_PLAN) NOT IN ('G', 'F')
            ORDER BY T3.UZBENRO_DSM_LAST_NAME
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

    public static final String SELECT_USER_NAME= """
            select user_name from users
            where user_name=?
            """;

    public static final String SELECT_ACTIVE_USER_NAME_FOR_UPDATE_PASSWORD= """
            SELECT user_name
                        FROM users
                        WHERE user_name='ZZZ999'
            """;

    public static final String SELECT_ACTIVE_USER_NAME= """
            SELECT user_name
            FROM users u
            WHERE u.active = 1
              AND u.deleted = 0
              AND u.domain_id <> 2
              AND LENGTH(u.user_name) > 5
              AND u.user_name REGEXP '^[a-zA-Z0-9]+$'
              AND NOT EXISTS (
                    SELECT 1
                    FROM users x
                    WHERE x.user_name = u.user_name
                      AND x.domain_id = 2
                )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACTIVE_USER_NAME3= """
            SELECT user_name
            FROM users u
            WHERE NOT EXISTS (
                    SELECT 1
                    FROM users x
                    WHERE x.user_name = u.user_name
                      AND x.domain_id = 2
                )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACTIVE_USER_NAME2= """
            SELECT user_name
            FROM users
            WHERE active = 1
            AND deleted = 0
            AND domain_id = 2
            AND LENGTH(user_name) > 5
            AND user_name REGEXP '^[a-zA-Z0-9]+$'
            ORDER BY user_name DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_PREM_CODE= """
            SELECT ucbprem_code
            FROM ucbprem
            WHERE ucbprem_code=?
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_PREMISES_CODE= """
            SELECT ucbprem_code
            FROM ucbprem
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_CODE = """
        SELECT ucbcust_cust_code
        FROM ucbcust
        WHERE LENGTH(ucbcust_cust_code) > 4
        FETCH FIRST 1 ROWS ONLY
        """;


    public static final String SELECT_CUSTOMER_CODE= """
            SELECT ucbcust_cust_code
            FROM ucbcust
            WHERE ucbcust_cust_code=?
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_PREMISE_CODE= """
            SELECT ucbprem_code
            FROM ucbprem
            WHERE ucbprem_code=?
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_NEW_ACCOUNT= """
            SELECT ucracct_cust_code, ucracct_prem_code
            FROM ucracct
            WHERE ucracct_status_ind= 'N'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_NICKNAME= """
            SELECT
                                           UCRACCT_CUST_CODE,
                                           UCRACCT_PREM_CODE,
                                           UCRACCT_NICK_NAME
                                       FROM
                                           UCRACCT
                                       WHERE
                                           UCRACCT_STATUS_IND = 'A'
                                           AND UCRACCT_NICK_NAME IS NOT NULL
                                       FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_FINAL_ACCOUNT_WITH_NICKNAME= """
            SELECT
                                           UCRACCT_CUST_CODE,
                                           UCRACCT_PREM_CODE,
                                           UCRACCT_NICK_NAME
                                       FROM
                                           UCRACCT
                                       WHERE
                                           UCRACCT_STATUS_IND = 'F'
                                           AND UCRACCT_NICK_NAME IS NOT NULL
                                       FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_INACTIVE_ACCOUNT_WITH_NICKNAME= """
            SELECT
                                           UCRACCT_CUST_CODE,
                                           UCRACCT_PREM_CODE,
                                           UCRACCT_NICK_NAME
                                       FROM
                                           UCRACCT
                                       WHERE
                                           UCRACCT_STATUS_IND = 'I'
                                           AND UCRACCT_NICK_NAME IS NOT NULL
                                       FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_LATEST_LOGIN_ID= """
            SELECT gzrapil_login_id
            FROM gcismgr.gzrapil
            ORDER BY gzrapil_activity_date DESC
            FETCH FIRST 1 ROW ONLY
            """;

    public static final String SELECT_ACTIVE_ACCOUNT_ONLY= """
            SELECT T1.UCRACCT_CUST_CODE,
                   T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1
            WHERE T1.UCRACCT_STATUS_IND = 'A'
            ORDER BY DBMS_RANDOM.VALUE
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_NEW_ACCOUNT_ONLY= """
            SELECT T1.UCRACCT_CUST_CODE,
                   T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1
            WHERE T1.UCRACCT_STATUS_IND = 'N'
            ORDER BY DBMS_RANDOM.VALUE
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String CHECK_ACCOUNT_REGISTERED = """
            SELECT account_number
            FROM custadv_registered_accounts
            WHERE account_number LIKE CONCAT('%', ?, '%')
            FETCH FIRST 1 ROWS ONLY
            """;


    public static final String SELECT_FINAL_ACCOUNT_ONLY= """
            SELECT T1.UCRACCT_CUST_CODE,
                                          T1.UCRACCT_PREM_CODE
                                   FROM UCRACCT T1
                                   WHERE T1.UCRACCT_STATUS_IND = 'F'
                                   ORDER BY DBMS_RANDOM.VALUE
                                   FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_INACTIVE_ACCOUNT_ONLY= """
            SELECT T1.UCRACCT_CUST_CODE,
                                          T1.UCRACCT_PREM_CODE
                                   FROM UCRACCT T1
                                   WHERE T1.UCRACCT_STATUS_IND = 'I'
                                   ORDER BY DBMS_RANDOM.VALUE
                                   FETCH FIRST 1 ROWS ONLY
            """;


    public static final String SELECT_ACCOUNT_WITHOUT_NICKNAME= """
            SELECT
                UCRACCT_CUST_CODE,
                UCRACCT_PREM_CODE
            FROM
                UCRACCT
            WHERE
                UCRACCT_STATUS_IND <> 'N'
                AND (UCRACCT_NICK_NAME IS NULL OR TRIM(UCRACCT_NICK_NAME) = '')
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACTIVE_ACCOUNT_WITHOUT_NICKNAME= """
            SELECT
                UCRACCT_CUST_CODE,
                UCRACCT_PREM_CODE
            FROM
                UCRACCT
            WHERE
                UCRACCT_STATUS_IND = 'A'
                AND (UCRACCT_NICK_NAME IS NULL OR TRIM(UCRACCT_NICK_NAME) = '')
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS= """
            SELECT\s
                e.*,
                a.ucracct_prem_code
            FROM\s
                gzbemcp e
            INNER JOIN\s
                ucracct a
                ON e.gzbemcp_cust_code = a.ucracct_cust_code
                WHERE e.GZBEMCP_EMAIL_ADDR IS NOT NULL 
                 AND e.gzbemcp_cust_code IN (
                    SELECT gzbemcp_cust_code
                    FROM gzbemcp
                    GROUP BY gzbemcp_cust_code
                    HAVING COUNT(*) = 1
                )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC150= """
            SELECT a.*
                     FROM
                         ucracct a
                     WHERE a.ucracct_cust_code NOT IN (
                 SELECT gzbemcp_cust_code
                 FROM gzbemcp
                 GROUP BY gzbemcp_cust_code
                 HAVING COUNT(*) = 1
                 )
                        FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC151= """
            SELECT
                                        e.*,
                                        a.ucracct_prem_code
                                    FROM
                                        gzbemcp e
                                    INNER JOIN
                                        ucracct a
                                        ON e.gzbemcp_cust_code = a.ucracct_cust_code
                                        WHERE e.GZBEMCP_PARTNER_IND='Y'
                                         AND e.gzbemcp_cust_code IN (
                    SELECT gzbemcp_cust_code
                    FROM gzbemcp
                    GROUP BY gzbemcp_cust_code
                    HAVING COUNT(*) = 1
                )
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC152= """
            SELECT
                                        e.*,
                                        a.ucracct_prem_code
                                    FROM
                                        gzbemcp e
                                    INNER JOIN
                                        ucracct a
                                        ON e.gzbemcp_cust_code = a.ucracct_cust_code
                                        WHERE e.GZBEMCP_PARTNER_IND='N'
                                         AND e.gzbemcp_cust_code IN (
                    SELECT gzbemcp_cust_code
                    FROM gzbemcp
                    GROUP BY gzbemcp_cust_code
                    HAVING COUNT(*) = 1
                )
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC153= """
            SELECT a.*
                FROM
                    ucracct a
                WHERE a.ucracct_cust_code IN (
            SELECT gzbemcp_cust_code
            FROM gzbemcp g
            WHERE g.gzbemcp_partner_ind IS NULL
            GROUP BY gzbemcp_cust_code
            HAVING COUNT(*) = 1
            )
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC154= """
            SELECT
                                        e.*,
                                        a.ucracct_prem_code
                                    FROM
                                        gzbemcp e
                                    INNER JOIN
                                        ucracct a
                                        ON e.gzbemcp_cust_code = a.ucracct_cust_code
                                        WHERE e.GZBEMCP_MARKETING_IND='Y'
                                         AND e.gzbemcp_cust_code IN (
                    SELECT gzbemcp_cust_code
                    FROM gzbemcp
                    GROUP BY gzbemcp_cust_code
                    HAVING COUNT(*) = 1
                )
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC155= """
            SELECT\s
                e.*,
                a.ucracct_prem_code AS UCRACCT_PREM_CODE
            FROM\s
                gzbemcp e
            INNER JOIN\s
                ucracct a
                    ON e.gzbemcp_cust_code = a.ucracct_cust_code
            WHERE\s
                e.gzbemcp_marketing_ind = 'N'
                AND e.gzbemcp_cust_code IN (
                    SELECT gzbemcp_cust_code
                    FROM gzbemcp
                    GROUP BY gzbemcp_cust_code
                    HAVING COUNT(*) = 1
                )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC156= """
            SELECT a.*
                FROM
                    ucracct a
                WHERE a.ucracct_cust_code IN (
            SELECT gzbemcp_cust_code
            FROM gzbemcp g
            WHERE g.GZBEMCP_MARKETING_IND IS NULL
            GROUP BY gzbemcp_cust_code
            HAVING COUNT(*) = 1
            )
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC157= """
            SELECT
                                                     e.*,
                                                     a.*
                                                 FROM
                                                     gzbemcp e
                                                 INNER JOIN
                                                     ucracct a
                                                     ON e.gzbemcp_cust_code = a.ucracct_cust_code
                                                 WHERE
                                                     e.GZBEMCP_ACCOUNT_IND = 'Y'
                                                     AND LENGTH(e.gzbemcp_cust_code) >= 4
                                                     AND e.gzbemcp_cust_code IN (
                                                         SELECT gzbemcp_cust_code
                                                         FROM gzbemcp g
                                                         WHERE g.gzbemcp_account_ind = 'Y'
                                                         AND g.gzbemcp_ocs_pymt_remind = 'Y'
                                                           AND TRUNC(g.gzbemcp_expiration_date) >= TRUNC(SYSDATE)
                                                     )
                                                 FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC158= """
            SELECT
                                        e.*,
                                        a.ucracct_prem_code
                                    FROM
                                        gzbemcp e
                                    INNER JOIN
                                        ucracct a
                                        ON e.gzbemcp_cust_code = a.ucracct_cust_code
                                        WHERE e.GZBEMCP_ACCOUNT_IND='N'
                                         AND e.gzbemcp_cust_code IN (
                    SELECT gzbemcp_cust_code
                    FROM gzbemcp
                    GROUP BY gzbemcp_cust_code
                    HAVING COUNT(*) = 1
                )
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC159= """
            SELECT
                                        e.*,
                                        a.ucracct_prem_code
                                    FROM
                                        gzbemcp e
                                    INNER JOIN
                                        ucracct a
                                        ON e.gzbemcp_cust_code = a.ucracct_cust_code
                                        WHERE e.GZBEMCP_ACCOUNT_IND IS NULL
                                         AND e.gzbemcp_cust_code IN (
                    SELECT gzbemcp_cust_code
                    FROM gzbemcp
                    GROUP BY gzbemcp_cust_code
                    HAVING COUNT(*) = 1
                )
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC160= """
            SELECT t.ucrtele_cust_code,
            a.ucracct_prem_code\s
            FROM
            ucrtele t
            JOIN ucracct a
            ON t.ucrtele_cust_code= a.ucracct_cust_code
            WHERE t.ucrtele_tele_code='BI'
            AND t.ucrtele_primary_ind='Y'
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC161= """
            SELECT t.ucrtele_cust_code,
            a.ucracct_prem_code\s
            FROM
            ucrtele t
            JOIN ucracct a
            ON t.ucrtele_cust_code= a.ucracct_cust_code
            WHERE t.ucrtele_tele_code='BU'
            AND t.ucrtele_primary_ind='Y'
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC162= """
            SELECT\s
                t.*,
                a.ucracct_prem_code
            FROM\s
                ucrtele t
            JOIN\s
                ucracct a
                ON t.ucrtele_cust_code = a.ucracct_cust_code
            WHERE\s
                t.ucrtele_tele_code = 'BU'
                AND t.ucrtele_primary_ind = 'Y'
                AND t.ucrtele_cust_code IN (
                    SELECT ucrtele_cust_code
                    FROM ucrtele
                    WHERE ucrtele_tele_code = 'BI'
                      AND ucrtele_primary_ind = 'Y'
                )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC163= """
            SELECT\s
                a.ucracct_cust_code,
                a.ucracct_prem_code
            FROM\s
                ucracct a
            WHERE NOT EXISTS (
                SELECT 1
                FROM ucrtele t
                WHERE t.ucrtele_cust_code = a.ucracct_cust_code
            )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC164= """
            select \s
            t1.ucracct_cust_code, \s
            t1.ucracct_prem_code, \s
            t3.ucrserv_scls_code,\s
            t4.ucrscmp_scty_code,\s
            t4.ucrscmp_plan_code,
            t1.ucracct_bill_pres_type,
            t1.ucracct_corr_del_type,
            t4.ucrscmp_acr_ind
            from ucracct t1, ucbcust t2, ucrserv t3, ucrscmp t4
            where t1.ucracct_cust_code = t2.ucbcust_cust_code \s
            and t1.ucracct_cust_code = t3.ucrserv_cust_code\s
            and t1.ucracct_prem_code = t3.ucrserv_prem_code\s
            and t1.ucracct_cust_code = t4.ucrscmp_cust_code\s
            and t1.ucracct_prem_code = t4.ucrscmp_prem_code
            and t1.ucracct_status_ind = 'A'
            and t1.ucracct_cycl_code NOT IN 'DEPO'\s
            and t3.ucrserv_scls_code = 'RS'---RS residential and/or CM commercial\s
            and t4.ucrscmp_end_date > SYSDATE\s
            and t4.ucrscmp_scty_code in ('CARBAL') -- filter to the Greener Life rows
            order by t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC165= """
            SELECT \s
                                                  t1.ucracct_cust_code, \s
                                                  t1.ucracct_prem_code, \s
                                                  t3.ucrserv_scls_code,\s
                                                  t1.ucracct_bill_pres_type,
                                                  t1.ucracct_corr_del_type
                                              FROM\s
                                                  ucracct t1
                                              JOIN\s
                                                  ucbcust t2\s
                                                      ON t1.ucracct_cust_code = t2.ucbcust_cust_code
                                              JOIN\s
                                                  ucrserv t3\s
                                                      ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                                                     AND t1.ucracct_prem_code = t3.ucrserv_prem_code
                                              WHERE\s
                                                  t1.ucracct_status_ind = 'A'
                                                  AND t1.ucracct_cycl_code NOT IN ('DEPO')
                                                  AND t3.ucrserv_scls_code = 'RS'
                                                  AND NOT EXISTS (
                                                      SELECT 1
                                                      FROM ucrscmp t4
                                                      WHERE t4.ucrscmp_cust_code = t1.ucracct_cust_code
                                                        AND t4.ucrscmp_prem_code = t1.ucracct_prem_code
                                                  )
                                              ORDER BY\s
                                                  t1.ucracct_cust_code DESC
                                              FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC166= """
            SELECT \s
                t1.ucracct_cust_code, \s
                t1.ucracct_prem_code, \s
                t3.ucrserv_scls_code,\s
                t1.ucracct_bill_pres_type,
                t1.ucracct_corr_del_type
            FROM\s
                ucracct t1
            JOIN\s
                ucbcust t2\s
                    ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN\s
                ucrserv t3\s
                    ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                   AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            WHERE\s
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t1.ucracct_bill_pres_type='P'
            ORDER BY\s
                t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC167= """
            SELECT \s
                t1.ucracct_cust_code, \s
                t1.ucracct_prem_code, \s
                t3.ucrserv_scls_code,\s
                t1.ucracct_bill_pres_type,
                t1.ucracct_corr_del_type
            FROM\s
                ucracct t1
            JOIN\s
                ucbcust t2\s
                    ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN\s
                ucrserv t3\s
                    ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                   AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            WHERE\s
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t1.ucracct_bill_pres_type='E'
            ORDER BY\s
                t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC168= """
            SELECT \s
                t1.ucracct_cust_code, \s
                t1.ucracct_prem_code, \s
                t3.ucrserv_scls_code,\s
                t1.ucracct_bill_pres_type,
                t1.ucracct_corr_del_type
            FROM\s
                ucracct t1
            JOIN\s
                ucbcust t2\s
                    ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN\s
                ucrserv t3\s
                    ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                   AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            WHERE\s
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t1.ucracct_bill_pres_type='F'
            ORDER BY\s
                t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC169= """
            SELECT \s
                t1.ucracct_cust_code, \s
                t1.ucracct_prem_code, \s
                t3.ucrserv_scls_code,\s
                t1.ucracct_bill_pres_type,
                t1.ucracct_corr_del_type
            FROM\s
                ucracct t1
            JOIN\s
                ucbcust t2\s
                    ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN\s
                ucrserv t3\s
                    ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                   AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            WHERE\s
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t1.ucracct_bill_pres_type IS NULL
            ORDER BY\s
                t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC170= """
            SELECT \s
                t1.ucracct_cust_code, \s
                t1.ucracct_prem_code, \s
                t3.ucrserv_scls_code,\s
                t1.ucracct_bill_pres_type,
                t1.ucracct_corr_del_type
            FROM\s
                ucracct t1
            JOIN\s
                ucbcust t2\s
                    ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN\s
                ucrserv t3\s
                    ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                   AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            WHERE\s
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t1.ucracct_corr_del_type='P'
            ORDER BY\s
                t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC171= """
             SELECT \s
                t1.ucracct_cust_code, \s
                t1.ucracct_prem_code, \s
                t3.ucrserv_scls_code,\s
                t1.ucracct_bill_pres_type,
                t1.ucracct_corr_del_type
            FROM\s
                ucracct t1
            JOIN\s
                ucbcust t2\s
                    ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN\s
                ucrserv t3\s
                    ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                   AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            WHERE\s
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t1.ucracct_corr_del_type='E'
            ORDER BY\s
                t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC172= """
             SELECT \s
                t1.ucracct_cust_code, \s
                t1.ucracct_prem_code, \s
                t3.ucrserv_scls_code,\s
                t1.ucracct_bill_pres_type,
                t1.ucracct_corr_del_type
            FROM\s
                ucracct t1
            JOIN\s
                ucbcust t2\s
                    ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN\s
                ucrserv t3\s
                    ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                   AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            WHERE\s
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t1.ucracct_corr_del_type IS NULL
            ORDER BY\s
                t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC173= """
            SELECT \s
                t1.ucracct_cust_code, \s
                t1.ucracct_prem_code, \s
                t3.ucrserv_scls_code,\s
                t1.ucracct_bill_pres_type,
                t1.ucracct_corr_del_type,
                t4.ucrscmp_plan_code
            FROM\s
                ucracct t1
            JOIN\s
                ucbcust t2\s
                    ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN\s
                ucrserv t3\s
                    ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                   AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN
                ucrscmp t4
                    ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                   AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE\s
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_end_date > SYSDATE
                AND (
                        t4.ucrscmp_plan_code IN ('RGB','CGB','GB6','PGB')\s
                     OR t4.ucrscmp_plan_code IN ('CCV','CSV')
                    )
                AND (t1.ucracct_cust_code, t1.ucracct_prem_code) IN (
                    SELECT\s
                        ucrscmp_cust_code,
                        ucrscmp_prem_code
                    FROM\s
                        ucrscmp
                    WHERE\s
                        ucrscmp_end_date > SYSDATE
                    GROUP BY\s
                        ucrscmp_cust_code,
                        ucrscmp_prem_code
                    HAVING COUNT(*) = 1
                )
            ORDER BY\s
                t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC174= """
            SELECT \s
                t1.ucracct_cust_code, \s
                t1.ucracct_prem_code, \s
                t3.ucrserv_scls_code,\s
                t1.ucracct_bill_pres_type,
                t1.ucracct_corr_del_type,
                t4.ucrscmp_plan_code
            FROM\s
                ucracct t1
            JOIN\s
                ucbcust t2\s
                    ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN\s
                ucrserv t3\s
                    ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                   AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN
                ucrscmp t4
                    ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                   AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE\s
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_end_date > SYSDATE
                AND (t1.ucracct_cust_code, t1.ucracct_prem_code) IN (
                    SELECT\s
                        ucrscmp_cust_code,
                        ucrscmp_prem_code
                    FROM\s
                        ucrscmp
                    WHERE\s
                        ucrscmp_end_date > SYSDATE
                    GROUP BY\s
                        ucrscmp_cust_code,
                        ucrscmp_prem_code
                    HAVING COUNT(*) > 1
                )
            ORDER BY\s
                t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC175= """
            SELECT \s
                t1.ucracct_cust_code, \s
                t1.ucracct_prem_code, \s
                t3.ucrserv_scls_code,\s
                t4.ucrscmp_scty_code,\s
                t4.ucrscmp_plan_code,
                t1.ucracct_bill_pres_type,
                t1.ucracct_corr_del_type,
                t4.ucrscmp_acr_ind
            FROM\s
                ucracct t1
            JOIN\s
                ucbcust t2\s
                    ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN\s
                ucrserv t3\s
                    ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                   AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN\s
                ucrscmp t4
                    ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                   AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE\s
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_end_date > SYSDATE
                AND NOT EXISTS (
                    SELECT 1
                    FROM ucrscmp x
                    WHERE x.ucrscmp_cust_code = t1.ucracct_cust_code
                      AND x.ucrscmp_prem_code = t1.ucracct_prem_code
                      AND x.ucrscmp_end_date > SYSDATE
                      AND x.ucrscmp_plan_code IN ('RGB','CGB','GB6','PGB')   -- Guaranteed Bill Plans
                )
            ORDER BY\s
                t1.ucracct_cust_code DESC
    FETCH FIRST 1 ROWS ONLY
    """;

    public static final String SELECT_ACCOUNT_DETAILS_TC176= """
            SELECT \s
                t1.ucracct_cust_code, \s
                t1.ucracct_prem_code, \s
                t3.ucrserv_scls_code,\s
                t4.ucrscmp_scty_code,\s
                t4.ucrscmp_plan_code,
                t1.ucracct_bill_pres_type,
                t1.ucracct_corr_del_type,
                t4.ucrscmp_acr_ind
            FROM\s
                ucracct t1
            JOIN\s
                ucbcust t2\s
                    ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN\s
                ucrserv t3\s
                    ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                   AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN\s
                ucrscmp t4
                    ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                   AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE\s
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_end_date > SYSDATE
                AND t4.ucrscmp_plan_code IN ('RGB','CGB','GB6','PGB')   -- Guaranteed Bill Plans
            ORDER BY\s
                t1.ucracct_cust_code DESC
        FETCH FIRST 1 ROWS ONLY
    """;

    public static final String SELECT_ACCOUNT_DETAILS_TC177= """
            SELECT \s
                t1.ucracct_cust_code, \s
                t1.ucracct_prem_code, \s
                t3.ucrserv_scls_code,\s
                t4.ucrscmp_scty_code,\s
                t4.ucrscmp_plan_code,
                t1.ucracct_bill_pres_type,
                t1.ucracct_corr_del_type,
                t4.ucrscmp_acr_ind
            FROM\s
                ucracct t1
            JOIN\s
                ucbcust t2\s
                    ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN\s
                ucrserv t3\s
                    ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                   AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN\s
                ucrscmp t4
                    ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                   AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE\s
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_end_date > SYSDATE
                AND NOT EXISTS (
                    SELECT 1
                    FROM ucrscmp x
                    WHERE x.ucrscmp_cust_code = t1.ucracct_cust_code
                      AND x.ucrscmp_prem_code = t1.ucracct_prem_code
                      AND x.ucrscmp_end_date > SYSDATE
                      AND x.ucrscmp_plan_code IN ('CCV','CSV')   -- Price Protection Plans
                )
            ORDER BY\s
                t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
    """;

    public static final String SELECT_ACCOUNT_DETAILS_TC178= """
            SELECT \s
                t1.ucracct_cust_code, \s
                t1.ucracct_prem_code, \s
                t3.ucrserv_scls_code,\s
                t4.ucrscmp_scty_code,\s
                t4.ucrscmp_plan_code,
                t1.ucracct_bill_pres_type,
                t1.ucracct_corr_del_type,
                t4.ucrscmp_acr_ind
            FROM\s
                ucracct t1
            JOIN\s
                ucbcust t2\s
                    ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN\s
                ucrserv t3\s
                    ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                   AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN\s
                ucrscmp t4
                    ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                   AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE\s
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_end_date > SYSDATE
                AND t4.ucrscmp_plan_code IN ('CCV','CSV')   -- Price Protection Plans
            ORDER BY\s
                t1.ucracct_cust_code DESC
    FETCH FIRST 1 ROWS ONLY
    """;

    public static final String SELECT_ACCOUNT_DETAILS_TC179= """
            SELECT
                a.ucracct_cust_code,
                a.ucracct_prem_code,
                s.ucrserv_scls_code,
                c.ucrscmp_scty_code,
                c.ucrscmp_plan_code,
                a.ucracct_bill_pres_type,
                a.ucracct_corr_del_type,
                c.ucrscmp_acr_ind,
                p.uztppuc_rollover,
                p.uztppuc_restrict_ind
            FROM ucracct a
            JOIN ucrserv s
                ON s.ucrserv_cust_code = a.ucracct_cust_code
               AND s.ucrserv_prem_code = a.ucracct_prem_code
            JOIN ucrscmp c
                ON c.ucrscmp_cust_code = a.ucracct_cust_code
               AND c.ucrscmp_prem_code = a.ucracct_prem_code
            JOIN uztppuc p
                ON p.uztppuc_plan_code = c.ucrscmp_plan_code
            WHERE a.ucracct_status_ind = 'A'
              AND a.ucracct_cycl_code <> 'DEPO'
              AND s.ucrserv_scls_code = 'RS'
              AND c.ucrscmp_end_date > SYSDATE
              AND p.uztppuc_rollover = 'N'
              AND c.ucrscmp_plan_code<>'MVS'
            FETCH FIRST 1 ROWS ONLY
    """;

    public static final String SELECT_ACCOUNT_DETAILS_TC180= """
            SELECT
                a.ucracct_cust_code,
                a.ucracct_prem_code,
                s.ucrserv_scls_code,
                c.ucrscmp_scty_code,
                c.ucrscmp_plan_code,
                a.ucracct_bill_pres_type,
                a.ucracct_corr_del_type,
                c.ucrscmp_acr_ind,
                p.uztppuc_rollover,
                p.uztppuc_restrict_ind
            FROM ucracct a
            JOIN ucrserv s
                ON s.ucrserv_cust_code = a.ucracct_cust_code
               AND s.ucrserv_prem_code = a.ucracct_prem_code
            JOIN ucrscmp c
                ON c.ucrscmp_cust_code = a.ucracct_cust_code
               AND c.ucrscmp_prem_code = a.ucracct_prem_code
            JOIN uztppuc p
                ON p.uztppuc_plan_code = c.ucrscmp_plan_code
            WHERE a.ucracct_status_ind = 'A'
              AND a.ucracct_cycl_code <> 'DEPO'
              AND s.ucrserv_scls_code = 'RS'
              AND c.ucrscmp_end_date > SYSDATE
              AND p.uztppuc_rollover = 'Y'
            FETCH FIRST 1 ROWS ONLY
    """;

    public static final String SELECT_ACCOUNT_DETAILS_TC181= """
     SELECT
                a.ucracct_cust_code,
                a.ucracct_prem_code,
                s.ucrserv_scls_code,
                c.ucrscmp_scty_code,
                c.ucrscmp_plan_code,
                a.ucracct_bill_pres_type,
                a.ucracct_corr_del_type,
                c.ucrscmp_acr_ind,
                p.uztppuc_rollover,
                p.uztppuc_restrict_ind
            FROM ucracct a
            JOIN ucrserv s
                ON s.ucrserv_cust_code = a.ucracct_cust_code
               AND s.ucrserv_prem_code = a.ucracct_prem_code
            JOIN ucrscmp c
                ON c.ucrscmp_cust_code = a.ucracct_cust_code
               AND c.ucrscmp_prem_code = a.ucracct_prem_code
            JOIN uztppuc p
                ON p.uztppuc_plan_code = c.ucrscmp_plan_code
            WHERE a.ucracct_status_ind = 'A'
              AND a.ucracct_cycl_code <> 'DEPO'
              AND s.ucrserv_scls_code = 'RS'
              AND c.ucrscmp_end_date > SYSDATE
              AND p.uztppuc_restrict_ind = 'N'
            FETCH FIRST 1 ROWS ONLY
    """;

    public static final String SELECT_ACCOUNT_DETAILS_TC182= """
    SELECT
                a.ucracct_cust_code,
                a.ucracct_prem_code,
                s.ucrserv_scls_code,
                c.ucrscmp_scty_code,
                c.ucrscmp_plan_code,
                a.ucracct_bill_pres_type,
                a.ucracct_corr_del_type,
                c.ucrscmp_acr_ind,
                p.uztppuc_rollover,
                p.uztppuc_restrict_ind
            FROM ucracct a
            JOIN ucrserv s
                ON s.ucrserv_cust_code = a.ucracct_cust_code
               AND s.ucrserv_prem_code = a.ucracct_prem_code
            JOIN ucrscmp c
                ON c.ucrscmp_cust_code = a.ucracct_cust_code
               AND c.ucrscmp_prem_code = a.ucracct_prem_code
            JOIN uztppuc p
                ON p.uztppuc_plan_code = c.ucrscmp_plan_code
            WHERE c.ucrscmp_end_date > SYSDATE
              AND p.uztppuc_restrict_ind = 'Y'
            FETCH FIRST 1 ROWS ONLY
    """;

    public static final String SELECT_ACCOUNT_DETAILS_TC183= """
            SELECT \s
                t1.ucracct_cust_code, \s
                t1.ucracct_prem_code, \s
                t3.ucrserv_scls_code,\s
                t4.ucrscmp_scty_code,\s
                t4.ucrscmp_plan_code,
                t1.ucracct_bill_pres_type,
                t1.ucracct_corr_del_type,
                t4.ucrscmp_acr_ind
            FROM\s
                ucracct t1
            JOIN\s
                ucbcust t2\s
                    ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN\s
                ucrserv t3\s
                    ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                   AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN\s
                ucrscmp t4
                    ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                   AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE\s
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_end_date > SYSDATE
                AND t4.ucrscmp_scty_code IN ('PPTDISC', 'FLATDISC', 'CSCDISC')   -- discount rows
                AND (t1.ucracct_cust_code, t1.ucracct_prem_code) IN (
                    SELECT\s
                        ucrscmp_cust_code,
                        ucrscmp_prem_code
                    FROM\s
                        ucrscmp
                    WHERE\s
                        ucrscmp_end_date > SYSDATE
                        AND ucrscmp_scty_code IN ('PPTDISC', 'FLATDISC', 'CSCDISC')
                    GROUP BY\s
                        ucrscmp_cust_code,
                        ucrscmp_prem_code
                    HAVING COUNT(*) = 1   -- exactly one discount
                )
            ORDER BY\s
                t1.ucracct_cust_code DESC
        FETCH FIRST 1 ROWS ONLY
    """;

    public static final String SELECT_ACCOUNT_DETAILS_TC184= """
            SELECT \s
                t1.ucracct_cust_code, \s
                t1.ucracct_prem_code, \s
                t3.ucrserv_scls_code,\s
                t4.ucrscmp_scty_code,\s
                t4.ucrscmp_plan_code,
                t1.ucracct_bill_pres_type,
                t1.ucracct_corr_del_type,
                t4.ucrscmp_acr_ind
            FROM\s
                ucracct t1
            JOIN\s
                ucbcust t2\s
                    ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN\s
                ucrserv t3\s
                    ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                   AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN\s
                ucrscmp t4
                    ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                   AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE\s
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_end_date > SYSDATE
                AND t4.ucrscmp_scty_code IN ('PPTDISC', 'FLATDISC', 'CSCDISC')
                AND (t1.ucracct_cust_code, t1.ucracct_prem_code) IN (
                    SELECT\s
                        ucrscmp_cust_code,
                        ucrscmp_prem_code
                    FROM\s
                        ucrscmp
                    WHERE\s
                        ucrscmp_end_date > SYSDATE
                        AND ucrscmp_scty_code IN ('PPTDISC', 'FLATDISC', 'CSCDISC')
                    GROUP BY\s
                        ucrscmp_cust_code,
                        ucrscmp_prem_code
                    HAVING COUNT(*) > 1   -- multiple discounts
                )
            ORDER BY\s
                t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
    """;

    public static final String SELECT_ACCOUNT_DETAILS_TC185= """
            SELECT \s
                t1.ucracct_cust_code, \s
                t1.ucracct_prem_code, \s
                t3.ucrserv_scls_code,\s
                t4.ucrscmp_scty_code,\s
                t4.ucrscmp_plan_code,
                t1.ucracct_bill_pres_type,
                t1.ucracct_corr_del_type,
                t4.ucrscmp_acr_ind
            FROM\s
                ucracct t1
            JOIN\s
                ucbcust t2\s
                    ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN\s
                ucrserv t3\s
                    ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                   AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN\s
                ucrscmp t4
                    ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                   AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE\s
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_end_date > SYSDATE
                AND NOT EXISTS (
                    SELECT 1
                    FROM ucrscmp x
                    WHERE x.ucrscmp_cust_code = t1.ucracct_cust_code
                      AND x.ucrscmp_prem_code = t1.ucracct_prem_code
                      AND x.ucrscmp_end_date > SYSDATE
                      AND x.ucrscmp_scty_code IN ('PPTDISC','FLATDISC','CSCDISC')  -- discount rows
                )
            ORDER BY\s
                t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
    """;

    public static final String SELECT_ACCOUNT_DETAILS_TC186= """
             SELECT \s
                        t1.ucracct_cust_code, \s
                        t1.ucracct_prem_code, \s
                        t3.ucrserv_scls_code,\s
                        t4.ucrscmp_scty_code,\s
                        t4.ucrscmp_plan_code,
                        t1.ucracct_bill_pres_type,
                        t1.ucracct_corr_del_type,
                        t4.ucrscmp_acr_ind
                    FROM\s
                        ucracct t1
                    JOIN\s
                        ucbcust t2\s
                            ON t1.ucracct_cust_code = t2.ucbcust_cust_code
                    JOIN\s
                        ucrserv t3\s
                            ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                           AND t1.ucracct_prem_code = t3.ucrserv_prem_code
                    JOIN\s
                        ucrscmp t4
                            ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                           AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
                    WHERE\s
                        t1.ucracct_status_ind = 'A'
                        AND t1.ucracct_cycl_code NOT IN ('DEPO')
                        AND t3.ucrserv_scls_code = 'RS'
                        AND t4.ucrscmp_end_date > SYSDATE
                        AND t4.ucrscmp_scty_code IN ('PPTDISC', 'FLATDISC', 'CSCDISC')   -- discount rows
                        AND (t1.ucracct_cust_code, t1.ucracct_prem_code) IN (
                            SELECT\s
                                ucrscmp_cust_code,
                                ucrscmp_prem_code
                            FROM\s
                                ucrscmp
                            WHERE\s
                                ucrscmp_end_date > SYSDATE
                                AND ucrscmp_scty_code IN ('PPTDISC', 'FLATDISC', 'CSCDISC')
                            GROUP BY\s
                                ucrscmp_cust_code,
                                ucrscmp_prem_code
                            HAVING COUNT(*) = 1   -- exactly one discount
                        )
                    ORDER BY\s
                        t1.ucracct_cust_code DESC
                FETCH FIRST 1 ROWS ONLY
    """;

    public static final String SELECT_ACCOUNT_DETAILS_TC187= """
            SELECT\s
                                                                   t1.ucracct_cust_code,\s
                                                                   t1.ucracct_prem_code,\s
                                                                   t3.ucrserv_scls_code,
                                                                   t4.ucrscmp_scty_code,
                                                                   t4.ucrscmp_plan_code,
                                                                   t1.ucracct_bill_pres_type,
                                                                   t1.ucracct_corr_del_type,
                                                                   t4.ucrscmp_acr_ind
                                                               FROM
                                                                   ucracct t1
                                                               JOIN
                                                                   ucbcust t2
                                                                       ON t1.ucracct_cust_code = t2.ucbcust_cust_code
                                                               JOIN
                                                                   ucrserv t3
                                                                       ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                                                                      AND t1.ucracct_prem_code = t3.ucrserv_prem_code
                                                               JOIN
                                                                   ucrscmp t4
                                                                       ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                                                                      AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
                                                               WHERE
                                                                   t1.ucracct_status_ind = 'A'
                                                                   AND t1.ucracct_cycl_code NOT IN ('DEPO')
                                                                   AND t3.ucrserv_scls_code = 'RS'
                                                                   AND t4.ucrscmp_end_date > SYSDATE
                                                                   AND t4.ucrscmp_scty_code IN ('PPTDISC', 'FLATDISC', 'CSCDISC')
                                                                   AND (t1.ucracct_cust_code, t1.ucracct_prem_code) IN (
                                                                       SELECT
                                                                           ucrscmp_cust_code,
                                                                           ucrscmp_prem_code
                                                                       FROM
                                                                           ucrscmp
                                                                       WHERE
                                                                           ucrscmp_end_date > SYSDATE
                                                                           AND ucrscmp_scty_code IN ('PRICEPRO')
                                                                   )
                                                               ORDER BY
                                                                   t1.ucracct_cust_code DESC
                                                               FETCH FIRST 1 ROWS ONLY
    """;

    public static final String SELECT_ACCOUNT_WITHOUT_ADDRESS= """
            SELECT
                A.UCRACCT_CUST_CODE,
                A.UCRACCT_PREM_CODE
            FROM
                UCRACCT A
            WHERE
                NOT EXISTS (
                    SELECT
                        1
                    FROM
                        UCRADDR B
                    WHERE
                        A.UCRACCT_CUST_CODE = B.UCRADDR_CUST_CODE
                )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_TC_113= """
            SELECT
                *
            FROM
                UCRADDR
            WHERE
                    UCRADDR_CUST_CODE = ?
                AND UCRADDR_STATUS_IND = 'A'
                AND TRUNC(UCRADDR_FROM_DATE) = TRUNC(SYSDATE)
                AND TRUNC(UCRADDR_TO_DATE) IS NULL
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_TC_116= """
            SELECT
                *
            FROM
                UCRADDR
            WHERE
                    UCRADDR_CUST_CODE = ?
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_TC_119= """
            SELECT
                *
            FROM
                UCRADDR
            WHERE
                    UCRADDR_CUST_CODE = ?
                    AND UCRADDR_STATUS_IND = 'A'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_TC_119_2= """
            SELECT
                *
            FROM
                UCRADDR
            WHERE
                    UCRADDR_CUST_CODE = ?
                    AND UCRADDR_STATUS_IND = 'I'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_ADDRESS_DIFFERENT_DAY= """
            SELECT
                A.UCRACCT_CUST_CODE,
                A.UCRACCT_PREM_CODE
            FROM
                UCRACCT A
            WHERE
                EXISTS (
                    SELECT
                        1
                    FROM
                        UCRADDR B
                    WHERE
                        A.UCRACCT_CUST_CODE = B.UCRADDR_CUST_CODE
                        AND TRUNC(B.UCRADDR_FROM_DATE) > TRUNC(SYSDATE)
                )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_ADDRESS_SAME_DAY= """
            SELECT
                A.UCRACCT_CUST_CODE,
                A.UCRACCT_PREM_CODE
            FROM
                UCRACCT A
            WHERE
                EXISTS (
                    SELECT
                        1
                    FROM
                        UCRADDR B
                    WHERE
                        A.UCRACCT_CUST_CODE = B.UCRADDR_CUST_CODE
                        AND B.UCRADDR_STATUS_IND = 'A'
                        AND TRUNC(B.UCRADDR_FROM_DATE) = TRUNC(SYSDATE)
                )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_UPDATED_NICKNAME_RECORD= """
            SELECT UCRACCT_NICK_NAME
            FROM
                UCRACCT
            WHERE
                UCRACCT_NICK_NAME=?
                AND UCRACCT_CUST_CODE= ?
                AND UCRACCT_PREM_CODE=?
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_UPDATED_NICKNAME_RECORD2= """
            SELECT UCRACCT_NICK_NAME
            FROM
                UCRACCT
            WHERE
                UCRACCT_NICK_NAME IS NULL
                AND UCRACCT_CUST_CODE= ?
                AND UCRACCT_PREM_CODE=?
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACTIVE_REWARDS= """
            SELECT *
                 FROM GZBRWDS
                 WHERE GZBRWDS_CNCL_DATE IS NULL
                 AND GZBRWDS_FULFILL_DATE IS NULL\s
                FETCH FIRST 1 ROWS ONLY
            """;


    public static final String SELECT_PENDING_REWARDS= """
            SELECT *
                             FROM GZBPRWD
                             WHERE GZBPRWD_CNCL_DATE IS NULL
                             AND GZBPRWD_ESTAB_DATE IS NULL\s
                            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACTIVE_REFER_A_FRIEND_REWARDS= """
            SELECT *
                 FROM GZBRWDS
                 WHERE GZBRWDS_CNCL_DATE IS NULL
                 AND GZBRWDS_FULFILL_DATE IS NULL\s
                 AND GZBRWDS_REWARD_ID=2
                FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_PENDING_REFER_A_FRIEND_REWARDS= """
            SELECT *
                             FROM GZBPRWD
                             WHERE GZBPRWD_CNCL_DATE IS NULL
                             AND GZBPRWD_ESTAB_DATE IS NULL\s
                             AND GZBPRWD_REWARD_ID= 2
                        FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_REWARD_DETAILS= """
            SELECT
                *
            FROM
                GZRRWDR
                WHERE GZRRWDR_ID= ?
                FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_NO_REWARDS= """
            SELECT *
            FROM UCRACCT a
            WHERE LENGTH(a.UCRACCT_CUST_CODE) >= 5
              AND NOT EXISTS (
                    SELECT 1
                    FROM GZBRWDS b
                    WHERE b.GZBRWDS_CUST_CODE = a.UCRACCT_CUST_CODE
                      AND b.GZBRWDS_PREM_CODE = a.UCRACCT_PREM_CODE
              )
              AND NOT EXISTS (
                    SELECT 1
                    FROM GZBPRWD p
                    WHERE p.GZBPRWD_CUST_CODE = a.UCRACCT_CUST_CODE
                      AND p.GZBPRWD_PREM_CODE = a.UCRACCT_PREM_CODE
              )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_MIXED_REWARDS= """
            SELECT\s
                                            A.*,
                                            P.*
                                        FROM GZBRWDS A
                                        INNER JOIN GZBPRWD P
                                            ON A.GZBRWDS_CUST_CODE = P.GZBPRWD_CUST_CODE
                                           AND A.GZBRWDS_PREM_CODE = P.GZBPRWD_PREM_CODE
                                        WHERE A.GZBRWDS_CNCL_DATE IS NULL
                                          AND A.GZBRWDS_FULFILL_DATE IS NULL
                                          AND P.GZBPRWD_CNCL_DATE IS NULL
                                          AND P.GZBPRWD_ESTAB_DATE IS NULL
                                        FETCH FIRST 1 ROWS ONLY
            """;


    public static final String SELECT_FINAL_ACCOUNT_WITHOUT_NICKNAME= """
            SELECT
                UCRACCT_CUST_CODE,
                UCRACCT_PREM_CODE
            FROM
                UCRACCT
            WHERE
                UCRACCT_STATUS_IND = 'F'
                AND (UCRACCT_NICK_NAME IS NULL OR TRIM(UCRACCT_NICK_NAME) = '')
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_INACTIVE_ACCOUNT_WITHOUT_NICKNAME= """
            SELECT
                UCRACCT_CUST_CODE,
                UCRACCT_PREM_CODE
            FROM
                UCRACCT
            WHERE
                UCRACCT_STATUS_IND = 'I'
                AND (UCRACCT_NICK_NAME IS NULL OR TRIM(UCRACCT_NICK_NAME) = '')
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_USER_ACCOUNT_INFO = """
            SELECT * FROM UCRACCT
            WHERE UCRACCT_CUST_CODE= ?
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACTIVE_USER_NAME_2= """
            SELECT user_name
            FROM users
            WHERE active = 1
            AND deleted = 0
            AND domain_id = 2
            AND LENGTH(user_name) > 5
            AND user_name REGEXP '^[a-zA-Z0-9]+$'
            ORDER BY user_name ASC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_INACTIVE_USER= """
            SELECT user_name, password
            FROM users
            WHERE active = 0
            AND domain_id = 2
            AND user_name REGEXP '^[a-zA-Z0-9]+$'
            ORDER BY user_name DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_DESIRED_USERNAME= """
            SELECT user_name
            FROM users
            WHERE active = 1
            AND domain_id = 2
            AND user_name=?
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_INACTIVE_USER2= """
            SELECT user_name
            FROM users
            WHERE deleted = 1
            AND domain_id = 2
              AND LENGTH(user_name) >= 5
              AND user_name REGEXP '^[a-zA-Z0-9]+$'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACTIVE_USER_NAME_3= """
            SELECT user_name
            FROM custadv_pending_registrations
            WHERE LENGTH(user_name) < 15
              AND user_name NOT IN (
                  SELECT user_name FROM users
              )
            ORDER BY user_name DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_PASSWORD_FOR_USER= """
            SELECT password
            FROM users
            WHERE user_name=?
            """;

    public static final String SELECT_INACTIVE_USER_NAME= """
            SELECT user_name
            FROM users
            WHERE active = 0
            AND user_name REGEXP '^[a-zA-Z0-9]+$'
            ORDER BY user_name DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES = """
            WITH eligible_customers AS (
                SELECT gtbtrnh_cust_code
                FROM gtbtrnh
                GROUP BY gtbtrnh_cust_code
                HAVING COUNT(*) < 30
            ),
            randomized_results AS (
                SELECT
                    t2.gtbtrnh_cust_code,
                    t2.gtbtrnh_prem_code,
                    t2.gtbtrnh_aglc_acct_nbr,
                    t3.gtrrndn_serv_ord_num,
                    DBMS_RANDOM.VALUE AS rand_val
                FROM gtbtrnh t2
                JOIN uzbenro t1 ON t1.uzbenro_cust_code = t2.gtbtrnh_cust_code
                JOIN gtrrndn t3 ON t2.gtbtrnh_seq_num = t3.gtrrndn_seq_num
                JOIN ucrserv t4 ON t2.gtbtrnh_prem_code = t4.ucrserv_prem_code
                JOIN ucracct t5 ON t5.ucracct_prem_code = t4.ucrserv_prem_code
                               AND t5.ucracct_cust_code = t2.gtbtrnh_cust_code
                WHERE t1.uzbenro_price_plan = ?
                  AND t5.ucracct_status_ind = 'A'
                  AND t4.ucrserv_scls_code = ?
                  AND t3.gtrrndn_serv_ord_num IS NOT NULL
                  AND t2.gtbtrnh_cust_code IN (
                      SELECT gtbtrnh_cust_code FROM eligible_customers
                  )
            )
            SELECT
                gtbtrnh_cust_code,
                gtbtrnh_prem_code,
                gtbtrnh_aglc_acct_nbr,
                gtrrndn_serv_ord_num
            FROM randomized_results
            ORDER BY rand_val
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ENROLLMENT_RECORD_DATA= """
            SELECT\s
            benro.UZBENRO_CUST_CODE
            FROM UZBENRO benro
            LEFT JOIN GZRSCFP gzrs
              ON benro.UZBENRO_CUST_CODE = gzrs.GZRSCFP_CUST_CODE AND benro.UZBENRO_PREM_CODE = gzrs.GZRSCFP_PREM_CODE
            LEFT JOIN UCRACCT acct
              ON benro.UZBENRO_CUST_CODE = acct.UCRACCT_CUST_CODE AND benro.UZBENRO_PREM_CODE = acct.UCRACCT_PREM_CODE
            LEFT JOIN OCBCONT cont
              ON benro.UZBENRO_CUST_CODE = cont.cust_ucbcust_code AND benro.UZBENRO_PREM_CODE = cont.ocbcont_premises_code
            LEFT JOIN OCRCDET crc
              ON benro.UZBENRO_CUST_CODE = crc.OCRCDET_IMPACTED_CUST_CODE AND benro.UZBENRO_PREM_CODE = crc.OCRCDET_IMPACTED_PREM_CODE
            LEFT JOIN OCRCTIM tim
              ON cont.ocbcont_contact_code = tim.cont_ocbcont_contact_code
            WHERE benro.UZBENRO_CUST_CODE = ?
              AND benro.UZBENRO_PREM_CODE = ?
            AND benro.UZBENRO_TRANS_CHNL='OMSTNON'
            AND benro.UZBENRO_ENRO_STATUS=?
            AND acct.UCRACCT_STATUS_IND=?
            AND acct.UCRACCT_CYCL_CODE=?
            AND acct.UCRACCT_PMNT_ARR=?
            AND acct.UCRACCT_BAD_DEBT_EXEMPT_IND=?
            AND acct.UCRACCT_NCOA_PROTECT_IND=?
            AND cont.OCBCONT_FEEDBACK_IND=?
            AND cont.OCBCONT_CONTACT_DIRECTION=?
            AND crc.OCRCDET_CATEGORY_CODE='ENROLL'
            AND crc.OCRCDET_REASON_CODE=?
            AND crc.OCRCDET_REFERRED_INDICATOR=?
            AND crc.CTYP_OTVCTYP_CONTACT_TYPE= ?
            AND crc.OCRCDET_STATUS=?
            AND tim.OCRCTIM_AUTOMATIC_INDICATOR=?
            AND tim.CDET_OCRCDET_REASON_CODE=?
            AND tim.CDET_OCRCDET_CATEGORY_CODE='ENROLL'
            """;

    public static final String SELECT_ENROLLMENT_RECORD_DATA_FOR_INCOMPLETE_ENROLLMENT= """
            SELECT
            benro.UZBENRO_CUST_CODE
            FROM UZBENRO benro
            LEFT JOIN GZRSCFP gzrs
              ON benro.UZBENRO_CUST_CODE = gzrs.GZRSCFP_CUST_CODE AND benro.UZBENRO_PREM_CODE = gzrs.GZRSCFP_PREM_CODE
            LEFT JOIN UCRACCT acct
              ON benro.UZBENRO_CUST_CODE = acct.UCRACCT_CUST_CODE AND benro.UZBENRO_PREM_CODE = acct.UCRACCT_PREM_CODE
            LEFT JOIN OCBCONT cont
              ON benro.UZBENRO_CUST_CODE = cont.cust_ucbcust_code AND benro.UZBENRO_PREM_CODE = cont.ocbcont_premises_code
            LEFT JOIN OCRCDET crc
              ON benro.UZBENRO_CUST_CODE = crc.OCRCDET_IMPACTED_CUST_CODE AND benro.UZBENRO_PREM_CODE = crc.OCRCDET_IMPACTED_PREM_CODE
            LEFT JOIN OCRCTIM tim
              ON cont.ocbcont_contact_code = tim.cont_ocbcont_contact_code
            WHERE benro.UZBENRO_CUST_CODE = ?
              AND benro.UZBENRO_PREM_CODE = ?
            AND benro.UZBENRO_TRANS_CHNL='OMSTNON'
            AND benro.UZBENRO_ENRO_STATUS=?
            AND acct.UCRACCT_STATUS_IND IS NULL
            AND acct.UCRACCT_CYCL_CODE IS NULL
            AND acct.UCRACCT_PMNT_ARR IS NULL
            AND acct.UCRACCT_BAD_DEBT_EXEMPT_IND IS NULL
            AND acct.UCRACCT_NCOA_PROTECT_IND IS NULL
            AND cont.OCBCONT_FEEDBACK_IND=?
            AND cont.OCBCONT_CONTACT_DIRECTION=?
            AND crc.OCRCDET_CATEGORY_CODE='ENROLL'
            AND crc.OCRCDET_REASON_CODE=?
            AND crc.OCRCDET_REFERRED_INDICATOR=?
            AND crc.CTYP_OTVCTYP_CONTACT_TYPE= ?
            AND crc.OCRCDET_STATUS=?
            AND tim.OCRCTIM_AUTOMATIC_INDICATOR=?
            AND tim.CDET_OCRCDET_REASON_CODE=?
            AND tim.CDET_OCRCDET_CATEGORY_CODE='ENROLL'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_UNENROLLMENT_ACCOUNT_DETAILS = """
            SELECT T1.UZBENRO_CUST_CODE
            FROM UZBENRO T1
            JOIN GTBTRNH T2 ON T2.GTBTRNH_CUST_CODE = T1.UZBENRO_CUST_CODE
            JOIN OCBCONT T3 ON T3.CUST_UCBCUST_CODE = T1.UZBENRO_CUST_CODE
            JOIN OCRCDET T4 ON T4.OCRCDET_IMPACTED_CUST_CODE = T1.UZBENRO_CUST_CODE
            JOIN UCBNOTE T5 ON T5.UCBNOTE_CUST_CODE = T1.UZBENRO_CUST_CODE
            JOIN UCRADDR T6 ON T6.UCRADDR_CUST_CODE = T1.UZBENRO_CUST_CODE
            WHERE T1.UZBENRO_CUST_CODE = ?
              AND T1.UZBENRO_ENRO_STATUS = 'TOFS'
              AND T1.UZBENRO_USER_ID = 'AUTOTESTER'
              AND T1.UZBENRO_TYPE_CODE = 'TOFF'
              AND T2.GTBTRNH_TRAN_TYPE = 'IOTF'
              AND T4.OCRCDET_REASON_CODE = 'TOFF'
              AND T4.OCRCDET_CATEGORY_CODE = 'ENROLL'
              AND T6.UCRADDR_ZIP IS NOT NULL
              FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_ETC_GPP = """
            WITH eligible_customers AS (
                SELECT GTBTRNH_CUST_CODE
                FROM GTBTRNH
                GROUP BY GTBTRNH_CUST_CODE
                HAVING COUNT(*) < 30
            ),
            randomized_results AS (
                SELECT
                    T2.GTBTRNH_CUST_CODE,
                    T2.GTBTRNH_PREM_CODE,
                    T2.GTBTRNH_AGLC_ACCT_NBR,
                    T3.GTRRNDN_SERV_ORD_NUM,
                    DBMS_RANDOM.VALUE AS rand_val
                FROM UZBENRO T1
                JOIN GTBTRNH T2 ON T1.UZBENRO_CUST_CODE = T2.GTBTRNH_CUST_CODE
                               AND T1.UZBENRO_PREM_CODE = T2.GTBTRNH_PREM_CODE
                JOIN GTRRNDN T3 ON T2.GTBTRNH_SEQ_NUM = T3.GTRRNDN_SEQ_NUM
                JOIN UCRSERV T4 ON T2.GTBTRNH_PREM_CODE = T4.UCRSERV_PREM_CODE
                JOIN UCRACCT T5 ON T4.UCRSERV_PREM_CODE = T5.UCRACCT_PREM_CODE
                               AND T5.UCRACCT_CUST_CODE = T1.UZBENRO_CUST_CODE
                WHERE T1.UZBENRO_SSP_IND = 'N'
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
                  AND T2.GTBTRNH_CUST_CODE IN (
                      SELECT GTBTRNH_CUST_CODE FROM eligible_customers
                  )
            )
            SELECT
                GTBTRNH_CUST_CODE,
                GTBTRNH_PREM_CODE,
                GTBTRNH_AGLC_ACCT_NBR,
                GTRRNDN_SERV_ORD_NUM
            FROM randomized_results
            ORDER BY rand_val
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CONTROL_NUMBER = """
            SELECT UZTCOTT_CONTROL_NUM FROM uztcott\s
            ORDER  BY uztcott_control_num DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_USER_ROLE_IDS = """
            SELECT role_id FROM user_role WHERE user_id = ?
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_ETC_GREENER_LIFE = """
            WITH eligible_customers AS (
                SELECT GTBTRNH_CUST_CODE
                FROM GTBTRNH
                GROUP BY GTBTRNH_CUST_CODE
                HAVING COUNT(*) < 30
            ),
            randomized_results AS (
                SELECT
                    T2.GTBTRNH_CUST_CODE,
                    T2.GTBTRNH_PREM_CODE,
                    T2.GTBTRNH_AGLC_ACCT_NBR,
                    T3.GTRRNDN_SERV_ORD_NUM,
                    DBMS_RANDOM.VALUE AS rand_val
                FROM UZBENRO T1
                JOIN GTBTRNH T2 ON T1.UZBENRO_CUST_CODE = T2.GTBTRNH_CUST_CODE
                              AND T1.UZBENRO_PREM_CODE = T2.GTBTRNH_PREM_CODE
                JOIN GTRRNDN T3 ON T2.GTBTRNH_SEQ_NUM = T3.GTRRNDN_SEQ_NUM
                JOIN UCRSERV T4 ON T2.GTBTRNH_PREM_CODE = T4.UCRSERV_PREM_CODE
                JOIN UCRACCT T5 ON T4.UCRSERV_PREM_CODE = T5.UCRACCT_PREM_CODE
                              AND T5.UCRACCT_CUST_CODE = T1.UZBENRO_CUST_CODE
                JOIN GZRCBHT T6 ON T6.GZRCBHT_CUST_CODE = T1.UZBENRO_CUST_CODE
                WHERE T1.UZBENRO_SSP_IND = 'N'
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
                  AND T2.GTBTRNH_CUST_CODE IN (
                      SELECT GTBTRNH_CUST_CODE FROM eligible_customers
                  )
            )
            SELECT
                GTBTRNH_CUST_CODE,
                GTBTRNH_PREM_CODE,
                GTBTRNH_AGLC_ACCT_NBR,
                GTRRNDN_SERV_ORD_NUM
            FROM randomized_results
            ORDER BY rand_val
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_ETC_ACTIVE_PENDING_REWARDS = """
            WITH eligible_customers AS (
                SELECT GTBTRNH_CUST_CODE
                FROM GTBTRNH
                GROUP BY GTBTRNH_CUST_CODE
                HAVING COUNT(*) BETWEEN 5 AND 30
            ),
            randomized_results AS (
                SELECT
                    T2.GTBTRNH_CUST_CODE,
                    T2.GTBTRNH_PREM_CODE,
                    T2.GTBTRNH_AGLC_ACCT_NBR,
                    T3.GTRRNDN_SERV_ORD_NUM,
                    DBMS_RANDOM.VALUE AS rand_val
                FROM UZBENRO T1
                JOIN GTBTRNH T2 ON T1.UZBENRO_CUST_CODE = T2.GTBTRNH_CUST_CODE
                               AND T1.UZBENRO_PREM_CODE = T2.GTBTRNH_PREM_CODE
                JOIN GTRRNDN T3 ON T2.GTBTRNH_SEQ_NUM = T3.GTRRNDN_SEQ_NUM
                JOIN GZBRWDS T4 ON T1.UZBENRO_CUST_CODE = T4.GZBRWDS_CUST_CODE
                               AND T1.UZBENRO_PREM_CODE = T4.GZBRWDS_PREM_CODE
                WHERE T1.UZBENRO_SSP_IND = 'N'
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
                  AND T2.GTBTRNH_CUST_CODE IN (
                      SELECT GTBTRNH_CUST_CODE FROM eligible_customers
                  )
            )
            SELECT
                GTBTRNH_CUST_CODE,
                GTBTRNH_PREM_CODE,
                GTBTRNH_AGLC_ACCT_NBR,
                GTRRNDN_SERV_ORD_NUM
            FROM randomized_results
            ORDER BY rand_val
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
                                           WHERE t7.ucrscmp_plan_code = ?
                                             AND TRUNC(SYSDATE) BETWEEN t7.ucrscmp_start_date AND t7.ucrscmp_end_date
                                             AND t7.ucrscmp_scty_code = 'COMM'
                                       )
                                   ORDER BY
                                       t8.uabopen_cust_code DESC
                                   FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_CEILING_PRICE_PLAN= """
            WITH eligible_customers AS (
                SELECT gtbtrnh_cust_code
                FROM gtbtrnh
                GROUP BY gtbtrnh_cust_code
                HAVING COUNT(*) < 90
            ),
            randomized_results AS (
                SELECT
                    t2.gtbtrnh_cust_code,
                    t2.gtbtrnh_prem_code,
                    t2.gtbtrnh_aglc_acct_nbr,
                    t3.gtrrndn_serv_ord_num,
                    DBMS_RANDOM.VALUE AS rand_val
                FROM uzbenro t1
                JOIN gtbtrnh t2 ON t1.uzbenro_cust_code = t2.gtbtrnh_cust_code
                JOIN gtrrndn t3 ON t2.gtbtrnh_seq_num = t3.gtrrndn_seq_num
                JOIN ucrserv t4 ON t2.gtbtrnh_prem_code = t4.ucrserv_prem_code
                JOIN ucracct t5 ON t4.ucrserv_prem_code = t5.ucracct_prem_code
                JOIN ucbcust t6 ON t5.ucracct_cust_code = t6.ucbcust_cust_code
                JOIN ucrscmp t7 ON t5.ucracct_cust_code = t7.ucrscmp_cust_code
                WHERE t5.ucracct_status_ind = 'A'
                  AND t4.ucrserv_scls_code = ?
                  AND t3.gtrrndn_serv_ord_num IS NOT NULL
                  AND t5.ucracct_cycl_code <> 'DEPO'
                  AND t6.ucbcust_prospect_value_score IN ('100', '101', '102', '103')
                  AND t7.ucrscmp_plan_code = ?
                  AND t7.ucrscmp_end_date > SYSDATE
                  AND t7.ucrscmp_start_date < SYSDATE
                  AND t7.ucrscmp_scty_code = 'COMM'
                  AND t2.gtbtrnh_cust_code IN (
                      SELECT gtbtrnh_cust_code FROM eligible_customers
                  )
            )
            SELECT
                gtbtrnh_cust_code,
                gtbtrnh_prem_code,
                gtbtrnh_aglc_acct_nbr,
                gtrrndn_serv_ord_num
            FROM randomized_results
            ORDER BY rand_val
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_CEILING_PRICE_PLAN_TC_230= """
            WITH filtered_data AS (
                SELECT
                    t2.gtbtrnh_cust_code,
                    t2.gtbtrnh_prem_code,
                    t2.gtbtrnh_aglc_acct_nbr,
                    t3.gtrrndn_serv_ord_num
                FROM
                    uzbenro t1
                JOIN gtbtrnh t2 ON t1.uzbenro_cust_code = t2.gtbtrnh_cust_code
                JOIN gtrrndn t3 ON t2.gtbtrnh_seq_num = t3.gtrrndn_seq_num
                JOIN ucrserv t4 ON t2.gtbtrnh_prem_code = t4.ucrserv_prem_code
                JOIN ucracct t5 ON t4.ucrserv_prem_code = t5.ucracct_prem_code
                JOIN ucbcust t6 ON t5.ucracct_cust_code = t6.ucbcust_cust_code
                JOIN ucrscmp t7 ON t5.ucracct_cust_code = t7.ucrscmp_cust_code
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
            )
            SELECT *
            FROM filtered_data
            ORDER BY DBMS_RANDOM.VALUE
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_ACN_WITHOUT_ETC= """
            WITH eligible_customers AS (
                SELECT GTBTRNH_CUST_CODE
                FROM GTBTRNH
                GROUP BY GTBTRNH_CUST_CODE
                HAVING COUNT(*) < 30
            )
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
                eligible_customers EC ON T2.GTBTRNH_CUST_CODE = EC.GTBTRNH_CUST_CODE
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
            WITH eligible_customers AS (
                SELECT GTBTRNH_CUST_CODE
                FROM GTBTRNH
                GROUP BY GTBTRNH_CUST_CODE
                HAVING COUNT(*) < 100
            ),
            randomized_results AS (
                SELECT
                    GT.GTBTRNH_CUST_CODE,
                    GT.GTBTRNH_PREM_CODE,
                    GT.GTBTRNH_AGLC_ACCT_NBR,
                    GR.GTRRNDN_SERV_ORD_NUM,
                    DBMS_RANDOM.VALUE AS rand_val
                FROM UCRACCT T1
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
                WHERE T1.UCRACCT_STATUS_IND = 'A'
                  AND T1.UCRACCT_CYCL_CODE <> 'DEPO'
                  AND T3.UCRSERV_SCLS_CODE = ?
                  AND T5.UCRSCMP_PLAN_CODE = ?
                  AND T5.UCRSCMP_END_DATE > SYSDATE
                  AND T5.UCRSCMP_START_DATE < SYSDATE
                  AND T5.UCRSCMP_SCTY_CODE = 'COMM'
                  AND GT.GTBTRNH_CUST_CODE IN (
                      SELECT GTBTRNH_CUST_CODE FROM eligible_customers
                  )
            )
            SELECT
                GTBTRNH_CUST_CODE,
                GTBTRNH_PREM_CODE,
                GTBTRNH_AGLC_ACCT_NBR,
                GTRRNDN_SERV_ORD_NUM
            FROM randomized_results
            ORDER BY rand_val
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_AGLC_SERVICE_CODES_ACC_WITH_PAST_DUE_BALANCE= """
            WITH eligible_customers AS (
                SELECT GTBTRNH_CUST_CODE
                FROM GTBTRNH
                GROUP BY GTBTRNH_CUST_CODE
                HAVING COUNT(*) < 100
            )
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
                AND GT.GTBTRNH_CUST_CODE IN (SELECT GTBTRNH_CUST_CODE FROM eligible_customers)
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

    public static final String SELECT_CUST_PREM_CODE_WITH_UNAPPLIED_DEPOSIT= """
            SELECT
                T2.GTBTRNH_CUST_CODE,
                T2.GTBTRNH_PREM_CODE
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
                AND T4.UCRSERV_SCLS_CODE = 'RS'
                AND T5.UCRACCT_STATUS_IND = 'I'
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
                      AND CMP.UCRSCMP_START_DATE < SYSDATE
                      AND CMP.UCRSCMP_END_DATE > SYSDATE
                      AND C.UCBCUST_PROSPECT_VALUE_SCORE IN ('110', '120')
                )
            ORDER BY
                T1.UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_CODE_DISCOUNTS_WITH_RESTRICTIONS= """
            SELECT T1.UCRACCT_CUST_CODE,
                               T1.UCRACCT_PREM_CODE
                          FROM UCRACCT T1
                          JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
                          JOIN UCRSERV T3 ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                                         AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
                          JOIN UCRSCMP T5 ON T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
                                         AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
                        WHERE T1.UCRACCT_STATUS_IND = 'A'
                          AND T1.UCRACCT_CYCL_CODE IN ('KF')
                          AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('104')
                          AND T5.UCRSCMP_PLAN_CODE = 'MVS'
                          AND T5.UCRSCMP_START_DATE < SYSDATE
                          AND T5.UCRSCMP_SCTY_CODE = 'COMM'
                          AND NOT EXISTS (
                              SELECT 1
                                FROM GZBRWDS T8
                               WHERE T8.GZBRWDS_CUST_CODE = T1.UCRACCT_CUST_CODE
                                 AND T8.GZBRWDS_PREM_CODE = T1.UCRACCT_PREM_CODE
                          )
                        ORDER BY T1.UCRACCT_CUST_CODE DESC
                        FETCH FIRST 1 ROWS ONLY
           """;

    public static final String SELECT_CUST_PREM_CODE_MULTIPLE_DISCOUNTS = """
            SELECT T1.UCRACCT_CUST_CODE,
                                                                  T1.UCRACCT_PREM_CODE
                                                             FROM UCRACCT T1
                                                             JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
                                                             JOIN UCRSERV T3 ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                                                                            AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
                                                             JOIN UCRSCMP T5 ON T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
                                                                            AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
                                                           WHERE T1.UCRACCT_STATUS_IND = 'A'
                                                             AND T1.UCRACCT_CYCL_CODE IN ('03')
                                                             AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('103')
                                                             AND T5.UCRSCMP_PLAN_CODE = 'MVS'
                                                             AND T5.UCRSCMP_START_DATE < SYSDATE
                                                             AND T5.UCRSCMP_SCTY_CODE = 'FLATDISC'
                                                           ORDER BY T1.UCRACCT_CUST_CODE DESC
                                                           FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_CODE_TRANSFERABLE_DISCOUNTS = """
            SELECT T1.UCRACCT_CUST_CODE,
                               T1.UCRACCT_PREM_CODE
                          FROM UCRACCT T1
                          JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
                          JOIN UCRSERV T3 ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                                         AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
                          JOIN UCRSCMP T5 ON T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
                                         AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
                        WHERE T1.UCRACCT_STATUS_IND = 'A'
                          AND T1.UCRACCT_CYCL_CODE IN ('12')
                          AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('100')
                          AND T5.UCRSCMP_PLAN_CODE = '18M'
                          AND T5.UCRSCMP_START_DATE < SYSDATE
                          AND T5.UCRSCMP_SCTY_CODE = 'COMM'
                          AND NOT EXISTS (
                              SELECT 1
                                FROM GZBRWDS T8
                               WHERE T8.GZBRWDS_CUST_CODE = T1.UCRACCT_CUST_CODE
                                 AND T8.GZBRWDS_PREM_CODE = T1.UCRACCT_PREM_CODE
                          )
                        ORDER BY T1.UCRACCT_CUST_CODE DESC
                        FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_CODE_ACTIVE_UNAPPLIED_DEPOSIT = """
   SELECT
                                           T2.GTBTRNH_CUST_CODE,
                                           T2.GTBTRNH_PREM_CODE
                                       FROM
                                           UZBENRO T1
                                       JOIN GTBTRNH T2
                                           ON T1.UZBENRO_CUST_CODE = T2.GTBTRNH_CUST_CODE
                                       JOIN GTRRNDN T3
                                           ON T2.GTBTRNH_SEQ_NUM = T3.GTRRNDN_SEQ_NUM
                                       JOIN UCRACCT T5
                                           ON T2.GTBTRNH_PREM_CODE = T5.UCRACCT_PREM_CODE
                                       JOIN UABOPEN T7
                                           ON T2.GTBTRNH_PREM_CODE = T7.UABOPEN_PREM_CODE
                                       JOIN UCRDPST T9
                                           ON T9.UCRDPST_CUST_CODE = T1.UZBENRO_CUST_CODE
                                       WHERE
                                           T3.GTRRNDN_SERV_ORD_NUM IS NOT NULL
                                           AND T5.UCRACCT_STATUS_IND = 'A'
                                           AND T7.UABOPEN_BALANCE > 0
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
                                                 AND CMP.UCRSCMP_START_DATE < SYSDATE
                                                 AND CMP.UCRSCMP_END_DATE > SYSDATE
                                                 AND C.UCBCUST_PROSPECT_VALUE_SCORE IN ('102')
                                                 AND T5.UCRACCT_CYCL_CODE = '15'
                                                 AND T9.UCRDPST_DEPOSIT_AMT > 0
                                                 AND EXISTS (
                                                     SELECT 1
                                                     FROM UCBSVCO T4
                                                     WHERE T4.UCBSVCO_PREM_CODE = CMP.UCRSCMP_PREM_CODE
                                                       AND T4.UCBSVCO_CUST_CODE = CMP.UCRSCMP_CUST_CODE
                                                       AND T4.UCBSVCO_SOTP_CODE = 'SONP'
                                                       -- AND T4.UCBSVCO_DATE_CREATED > TO_DATE('08-NOV-2024', 'DD-MON-YYYY')
                                                       AND T4.UCBSVCO_STUS_CODE IN ('O', 'X', 'C')
                                                 )
                                           )
                                           AND EXISTS (
                                               SELECT 1
                                               FROM UCRDPST D
                                               WHERE D.UCRDPST_CUST_CODE = T1.UZBENRO_CUST_CODE
                                               GROUP BY D.UCRDPST_CUST_CODE
                                               HAVING SUM(NVL(D.UCRDPST_DEPOSIT_AMT, 0)) - SUM(NVL(D.UCRDPST_APPLIED_DEP_AMT, 0)) > 0
                                           )
                                       ORDER BY
                                           T1.UZBENRO_CUST_CODE DESC
                                       FETCH FIRST 1 ROWS ONLY
""";

    public static final String SELECT_CUST_PREM_CODE_ACTIVE_SONP_DISCOUNTS= """
            SELECT T1.UCRACCT_CUST_CODE,
                                                                                                           T1.UCRACCT_PREM_CODE
                                                                                                      FROM UCRACCT T1
                                                                                                      JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
                                                                                                      JOIN UCRSERV T3 ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                                                                                                                     AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
                                                                                                      JOIN UCRSCMP T5 ON T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
                                                                                                                     AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
                                                                                                    WHERE T1.UCRACCT_STATUS_IND = 'A'
                                                                                                      AND T1.UCRACCT_CYCL_CODE IN ('19')
                                                                                                      AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('150')
                                                                                                      AND T5.UCRSCMP_PLAN_CODE = 'RGB'
                                                                                                      AND T5.UCRSCMP_START_DATE < SYSDATE
                                                                                                      AND T5.UCRSCMP_SCTY_CODE = 'COMM'
                                                                                                      AND NOT EXISTS (
                                                                                                          SELECT 1
                                                                                                            FROM GZBRWDS T8
                                                                                                           WHERE T8.GZBRWDS_CUST_CODE = T1.UCRACCT_CUST_CODE
                                                                                                             AND T8.GZBRWDS_PREM_CODE = T1.UCRACCT_PREM_CODE
                                                                                                      )
                                                                                                       AND EXISTS (
                                                                             SELECT 1
                                                                             FROM UCBSVCO T4
                                                                             WHERE T4.UCBSVCO_PREM_CODE = T5.UCRSCMP_PREM_CODE
                                                                               AND T4.UCBSVCO_CUST_CODE = T5.UCRSCMP_CUST_CODE
                                                                               AND T4.UCBSVCO_SOTP_CODE = 'SONP'
                                                                               -- AND T4.UCBSVCO_DATE_CREATED > TO_DATE('08-NOV-2024', 'DD-MON-YYYY')
                                                                               AND T4.UCBSVCO_STUS_CODE IN ('O', 'X', 'C')
                                                                         )
                                                                                                    ORDER BY T1.UCRACCT_CUST_CODE DESC
                                                                                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_CODE_ACTIVE_SONP_DISCOUNTS_TRANSFERABLE= """
            SELECT T1.UCRACCT_CUST_CODE,
                                           T1.UCRACCT_PREM_CODE
                                      FROM UCRACCT T1
                                      JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
                                      JOIN UCRSERV T3 ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                                                     AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
                                      JOIN UCRSCMP T5 ON T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
                                                     AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
                                    WHERE T1.UCRACCT_STATUS_IND = 'A'
                                      AND T1.UCRACCT_CYCL_CODE IN ('17')
                                      AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('102')
                                      AND T5.UCRSCMP_PLAN_CODE = 'MVS'
                                      AND T5.UCRSCMP_START_DATE < SYSDATE
                                      AND EXISTS (
                                                                 SELECT 1
                                                                 FROM UCBSVCO T4
                                                                 WHERE T4.UCBSVCO_PREM_CODE = T5.UCRSCMP_PREM_CODE
                                                                   AND T4.UCBSVCO_CUST_CODE = T5.UCRSCMP_CUST_CODE
                                                                   AND T4.UCBSVCO_SOTP_CODE = 'SONP'
                                                                   -- AND T4.UCBSVCO_DATE_CREATED > TO_DATE('08-NOV-2024', 'DD-MON-YYYY')
                                                                   AND T4.UCBSVCO_STUS_CODE IN ('O', 'X', 'C')
                                                             )
                                                             and t5.ucrscmp_scty_code in ('PPTDISC', 'FLATDISC', 'CSCDISC')
                                                             and t1.ucracct_cust_code < 5992157
                                    ORDER BY T1.UCRACCT_CUST_CODE DESC
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_CODE_ACTIVE_SONP_DISCOUNTS_MULTIPLE= """
            SELECT T1.UCRACCT_CUST_CODE,
                                                                              T1.UCRACCT_PREM_CODE
                                                                         FROM UCRACCT T1
                                                                         JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
                                                                         JOIN UCRSERV T3 ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                                                                                        AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
                                                                         JOIN UCRSCMP T5 ON T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
                                                                                        AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
                                                                       WHERE T1.UCRACCT_STATUS_IND = 'A'
                                                                         AND T1.UCRACCT_CYCL_CODE IN ('04')
                                                                         AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('100')
                                                                         AND T5.UCRSCMP_PLAN_CODE = 'MVS'
                                                                         AND T5.UCRSCMP_START_DATE < SYSDATE
                                                                         AND T5.UCRSCMP_SCTY_CODE = 'PPTDISC'
                                                                         AND EXISTS (
                                                                             SELECT 1
                                                                             FROM UCBSVCO T4
                                                                             WHERE T4.UCBSVCO_PREM_CODE = T5.UCRSCMP_PREM_CODE
                                                                               AND T4.UCBSVCO_CUST_CODE = T5.UCRSCMP_CUST_CODE
                                                                               AND T4.UCBSVCO_SOTP_CODE = 'SONP'
                                                                               -- AND T4.UCBSVCO_DATE_CREATED > TO_DATE('08-NOV-2024', 'DD-MON-YYYY')
                                                                               AND T4.UCBSVCO_STUS_CODE IN ('O', 'X', 'C')
                                                                         )
                                                                         AND T1.UCRACCT_CUST_CODE < 5158000
                                                                       ORDER BY T1.UCRACCT_CUST_CODE DESC
                                                                       FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_CODE_ACTIVE_GREENER_LIFE_NO_SONP= """
            SELECT
                                                                   T1.UCRACCT_CUST_CODE,
                                                                   T1.UCRACCT_PREM_CODE
                                                               FROM UCRACCT T1
                                                               JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
                                                               JOIN UCRSCMP T5 ON T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
                                                                              AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
                                                               JOIN UABOPEN T7 ON T1.UCRACCT_CUST_CODE = T7.UABOPEN_CUST_CODE
                                                               WHERE T1.UCRACCT_STATUS_IND = 'A'
                                                                 AND T1.UCRACCT_CYCL_CODE IN ('16')
                                                                 AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('100')
                                                                 AND T5.UCRSCMP_PLAN_CODE = 'GPP'
                                                                 AND T5.UCRSCMP_END_DATE > SYSDATE
                                                                 AND T5.UCRSCMP_START_DATE < SYSDATE
                                                                 AND T5.UCRSCMP_SCTY_CODE = 'PPTDISC'
                                                                 AND EXISTS (
                                                                     SELECT 1
                                                                     FROM GZBRWDS BW
                                                                     JOIN UCRSCMP CMP ON BW.GZBRWDS_CUST_CODE = CMP.UCRSCMP_CUST_CODE
                                                                                     AND BW.GZBRWDS_PREM_CODE = CMP.UCRSCMP_PREM_CODE
                                                                     WHERE CMP.UCRSCMP_CUST_CODE = T1.UCRACCT_CUST_CODE
                                                                       AND CMP.UCRSCMP_PREM_CODE = T1.UCRACCT_PREM_CODE
                                                                 )
                                                                 AND NOT EXISTS (
                                                                     SELECT 1
                                                                     FROM UCBSVCO SV
                                                                     JOIN UZBENRO UZ ON SV.UCBSVCO_PREM_CODE = UZ.UZBENRO_PREM_CODE
                                                                                    AND SV.UCBSVCO_CUST_CODE = UZ.UZBENRO_CUST_CODE
                                                                     WHERE SV.UCBSVCO_SOTP_CODE = 'SONP'
                                                                       AND SV.UCBSVCO_STUS_CODE IN ('O', 'X', 'C')
                                                                       AND UZ.UZBENRO_CUST_CODE = T1.UCRACCT_CUST_CODE
                                                                       AND UZ.UZBENRO_PREM_CODE = T1.UCRACCT_PREM_CODE
                                                                 )
                                                               ORDER BY T1.UCRACCT_CUST_CODE DESC
                                                               FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_CODE_ACTIVE_SONP_DISCOUNTS_RESTRICTIONS= """
            SELECT T1.UCRACCT_CUST_CODE,
                                                                                                           T1.UCRACCT_PREM_CODE
                                                                                                      FROM UCRACCT T1
                                                                                                      JOIN UCBCUST T2 ON T1.UCRACCT_CUST_CODE = T2.UCBCUST_CUST_CODE
                                                                                                      JOIN UCRSERV T3 ON T1.UCRACCT_CUST_CODE = T3.UCRSERV_CUST_CODE
                                                                                                                     AND T1.UCRACCT_PREM_CODE = T3.UCRSERV_PREM_CODE
                                                                                                      JOIN UCRSCMP T5 ON T1.UCRACCT_CUST_CODE = T5.UCRSCMP_CUST_CODE
                                                                                                                     AND T1.UCRACCT_PREM_CODE = T5.UCRSCMP_PREM_CODE
                                                                                                    WHERE T1.UCRACCT_STATUS_IND = 'A'
                                                                                                      AND T1.UCRACCT_CYCL_CODE IN ('07')
                                                                                                      AND T2.UCBCUST_PROSPECT_VALUE_SCORE IN ('110')
                                                                                                      AND T5.UCRSCMP_PLAN_CODE = 'PRP'
                                                                                                      AND T5.UCRSCMP_START_DATE < SYSDATE
                                                                                                      AND T5.UCRSCMP_SCTY_CODE = 'COMM'
                                                                                                      AND NOT EXISTS (
                                                                                                          SELECT 1
                                                                                                            FROM GZBRWDS T8
                                                                                                           WHERE T8.GZBRWDS_CUST_CODE = T1.UCRACCT_CUST_CODE
                                                                                                             AND T8.GZBRWDS_PREM_CODE = T1.UCRACCT_PREM_CODE
                                                                                                      )
                                                                                                       AND EXISTS (
                                                                             SELECT 1
                                                                             FROM UCBSVCO T4
                                                                             WHERE T4.UCBSVCO_PREM_CODE = T5.UCRSCMP_PREM_CODE
                                                                               AND T4.UCBSVCO_CUST_CODE = T5.UCRSCMP_CUST_CODE
                                                                               AND T4.UCBSVCO_SOTP_CODE = 'SONP'
                                                                               -- AND T4.UCBSVCO_DATE_CREATED > TO_DATE('08-NOV-2024', 'DD-MON-YYYY')
                                                                               AND T4.UCBSVCO_STUS_CODE IN ('O', 'X', 'C')
                                                                         )
                                                                                                    ORDER BY T1.UCRACCT_CUST_CODE DESC
                                                                                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CUST_PREM_CODE_ACTIVE_SONP_REWARDS= """
            SELECT
                                                       T2.GTBTRNH_CUST_CODE,
                                                       T2.GTBTRNH_PREM_CODE
                                                   FROM
                                                       UZBENRO T1
                                                   JOIN GTBTRNH T2
                                                       ON T1.UZBENRO_CUST_CODE = T2.GTBTRNH_CUST_CODE
                                                   JOIN GTRRNDN T3
                                                       ON T2.GTBTRNH_SEQ_NUM = T3.GTRRNDN_SEQ_NUM
                                                   JOIN UCRACCT T5
                                                       ON T2.GTBTRNH_PREM_CODE = T5.UCRACCT_PREM_CODE
                                                   JOIN UABOPEN T7
                                                       ON T2.GTBTRNH_PREM_CODE = T7.UABOPEN_PREM_CODE
                                                   JOIN UCRDPST T9
                                                       ON T9.UCRDPST_CUST_CODE = T1.UZBENRO_CUST_CODE
                                                   WHERE
                                                       T3.GTRRNDN_SERV_ORD_NUM IS NOT NULL
                                                       AND T5.UCRACCT_STATUS_IND = 'A'
                                                       AND T7.UABOPEN_BALANCE > 0
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
                                                             AND CMP.UCRSCMP_START_DATE < SYSDATE
                                                             AND CMP.UCRSCMP_END_DATE > SYSDATE
                                                             AND C.UCBCUST_PROSPECT_VALUE_SCORE IN ('230')
                                                             AND T5.UCRACCT_CYCL_CODE = '12'
                                                             AND EXISTS (
                                                                 SELECT 1
                                                                 FROM UCBSVCO T4
                                                                 WHERE T4.UCBSVCO_PREM_CODE = CMP.UCRSCMP_PREM_CODE
                                                                   AND T4.UCBSVCO_CUST_CODE = CMP.UCRSCMP_CUST_CODE
                                                                   AND T4.UCBSVCO_SOTP_CODE = 'SONP'
                                                                   -- AND T4.UCBSVCO_DATE_CREATED > TO_DATE('08-NOV-2024', 'DD-MON-YYYY')
                                                                   AND T4.UCBSVCO_STUS_CODE IN ('O', 'X', 'C')
                                                             )
                                                       )
                                                       AND EXISTS (
                                                           SELECT 1
                                                           FROM UCRDPST D
                                                           WHERE D.UCRDPST_CUST_CODE = T1.UZBENRO_CUST_CODE
                                                           GROUP BY D.UCRDPST_CUST_CODE
                                                           HAVING SUM(NVL(D.UCRDPST_DEPOSIT_AMT, 0)) - SUM(NVL(D.UCRDPST_APPLIED_DEP_AMT, 0)) > 0
                                                       )
                                                        AND EXISTS (
                                 SELECT 1
                                 FROM GZBRWDS T5
                                 WHERE T5.GZBRWDS_CUST_CODE = T5.UCRACCT_CUST_CODE
                                   AND T5.GZBRWDS_PREM_CODE = T5.UCRACCT_PREM_CODE
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

    public static final String GET_VALIDATION_PLANS_AND_OFFERS_RESULT = """      
            SELECT
              t1.uztcott_app_request_code   AS "appRequestCode",
              t1.uztcott_plan_code          AS "planCode",
              t1.uztcott_plan_desc          AS "planDescription",
              t1.uztcott_sort_order         AS "sortOrder",
              t1.uztcott_therm_price        AS "thermPrice",
              t1.uztcott_duration           AS "planDuration",
              t1.uztcott_svc_charge         AS "serviceCharge",
              t1.uztcott_sign_up_charge     AS "signUpCharge",
              t1.uztcott_cap_amount         AS "priceCeiling",
              t1.uztcott_protect_fee        AS "priceProtectionFee",
              t1.uztcott_cancel_fee         AS "cancelFee",
              t1.uztcott_high_csc           AS "highCustomerServiceCharge",
              t1.uztcott_low_csc            AS "lowCustomerServiceCharge",
              t1.uztcott_mktg_terms         AS "marketingTerms",
              t1.uztcott_terms              AS "offerTerms",
              t1.uztcott_ext_terms          AS "externalTerms",
              t1.uztcott_restricted_ind     AS "restrictedIndicator",
              t1.uztcott_pre_pay_ind        AS "prepayPlanIndicator",
              t1.uztcott_estimated_chg      AS "prepayEstimateAmountDue",
              t1.uztcott_estimated_cons     AS "prepayEstimatedConsumption",
              t1.uztcott_due_date_cust      AS "prepayCustomerPayByDate",
              t1.uztcott_due_date_sys       AS "prepaySystemPayByDate",
              t1.uztcott_prepay_reduction_amt AS "prepayOneTimeWelcomeCredit",
              t1.uztcott_prepay_original_amt  AS "prepayEstimateOriginalAmount",
              t1.uztcott_pia_ind            AS "payInAdvanceIndicator",
                  MAX(CASE WHEN p.rn = 1 THEN p.uztprmo_prmo_code END) AS "promotion1Code",
                          MAX(CASE WHEN p.rn = 1 THEN p.uztprmo_prmo_desc END) AS "promotion1Description",
                          MAX(CASE WHEN p.rn = 1 THEN p.uztprmo_terms END) AS "promotion1Terms",
                          MAX(CASE WHEN p.rn = 1 THEN p.uztprmo_mktg_msg END) AS "promotion1MarketingMessage",
                          MAX(CASE WHEN p.rn = 1 THEN p.uztprmo_trans_ind END) AS "promotion1TransferIndicator",
                          MAX(CASE WHEN p.rn = 1 THEN p.uztprmo_visa_ind END) AS "promotion1VisaIndicator",
                          MAX(CASE WHEN p.rn = 2 THEN p.uztprmo_prmo_code END) AS "promotion2Code",
                          MAX(CASE WHEN p.rn = 2 THEN p.uztprmo_prmo_desc END) AS "promotion2Description",
                          MAX(CASE WHEN p.rn = 2 THEN p.uztprmo_terms END) AS "promotion2Terms",
                          MAX(CASE WHEN p.rn = 2 THEN p.uztprmo_mktg_msg END) AS "promotion2MarketingMessage",
                          MAX(CASE WHEN p.rn = 2 THEN p.uztprmo_trans_ind END) AS "promotion2TransferIndicator",
                          MAX(CASE WHEN p.rn = 2 THEN p.uztprmo_visa_ind END) AS "promotion2VisaIndicator",
              t1.uztcott_control_num        AS "controlNum"
            FROM uztcott t1
            LEFT JOIN (
              SELECT
                t2.uztprmo_control_num,
                t2.uztprmo_plan_code,
                t2.uztprmo_bucket_id,
                t2.uztprmo_prmo_code,
                t2.uztprmo_prmo_desc,
                t2.uztprmo_terms,
                t2.uztprmo_mktg_msg,
                t2.uztprmo_trans_ind,
                t2.uztprmo_visa_ind,
                ROW_NUMBER() OVER (
                  PARTITION BY t2.uztprmo_plan_code, t2.uztprmo_control_num
                  ORDER BY t2.uztprmo_prmo_sort_order
                ) AS rn
              FROM uztprmo t2
            ) p ON t1.uztcott_plan_code = p.uztprmo_plan_code
                 AND t1.uztcott_control_num = p.uztprmo_control_num
                 AND t1.uztcott_bucket_id = p.uztprmo_bucket_id
            LEFT JOIN odmbckt t3 ON t1.uztcott_bucket_id = t3.bucket_id
            LEFT JOIN gtbenrl t4 ON t1.uztcott_control_num = t4.gtbenrl_control_num
            WHERE t1.uztcott_app_request_code = 'OMSENRL'
              AND t1.uztcott_control_num IN (<controlNumber>)
            GROUP BY
              t1.uztcott_app_request_code,
              t1.uztcott_plan_code,
              t1.uztcott_plan_desc,
              t1.uztcott_sort_order,
              t1.uztcott_therm_price,
              t1.uztcott_duration,
              t1.uztcott_svc_charge,
              t1.uztcott_sign_up_charge,
              t1.uztcott_cap_amount,
              t1.uztcott_protect_fee,
              t1.uztcott_cancel_fee,
              t1.uztcott_high_csc,
              t1.uztcott_low_csc,
              t1.uztcott_mktg_terms,
              t1.uztcott_terms,
              t1.uztcott_ext_terms,
              t1.uztcott_restricted_ind,
              t1.uztcott_pre_pay_ind,
              t1.uztcott_estimated_chg,
              t1.uztcott_estimated_cons,
              t1.uztcott_due_date_cust,
              t1.uztcott_due_date_sys,
              t1.uztcott_prepay_reduction_amt,
              t1.uztcott_prepay_original_amt,
              t1.uztcott_pia_ind,
              t1.uztcott_control_num
            ORDER BY t1.uztcott_sort_order
        """;

    public static final String GET_VALIDATION_PREPAY_PLANS_RESULT = """ 
           SELECT
                   t1.UZTCOTT_PLAN_CODE              AS "planCode",
                   t1.UZTCOTT_PLAN_DESC              AS "planDescription",
                   t1.UZTCOTT_SORT_ORDER             AS "sortOrder",
                   t1.UZTCOTT_THERM_PRICE            AS "thermPrice",
                   t1.UZTCOTT_DURATION               AS "planDuration",
                   t1.UZTCOTT_SVC_CHARGE             AS "serviceCharge",
                   t1.UZTCOTT_SIGN_UP_CHARGE         AS "signUpCharge",
                   t1.UZTCOTT_CAP_AMOUNT             AS "priceCeiling",
                   t1.UZTCOTT_PROTECT_FEE            AS "priceProtectionFee",
                   t1.UZTCOTT_CANCEL_FEE             AS "cancelFee",
                   t1.UZTCOTT_HIGH_CSC               AS "highCustomerServiceCharge",
                   t1.UZTCOTT_LOW_CSC                AS "lowCustomerServiceCharge",
                   t1.UZTCOTT_MKTG_TERMS             AS "marketingTerms",
                   t1.UZTCOTT_TERMS                  AS "offerTerms",
                   t1.UZTCOTT_EXT_TERMS              AS "externalTerms",
                   t1.UZTCOTT_RESTRICTED_IND         AS "restrictedIndicator",
                   t1.UZTCOTT_PRE_PAY_IND            AS "prepayPlanIndicator",
                   t1.UZTCOTT_ESTIMATED_CHG          AS "prepayEstimateAmountDue",
                   t1.UZTCOTT_ESTIMATED_CONS         AS "prepayEstimatedConsumption",
                   t1.UZTCOTT_DUE_DATE_CUST          AS "prepayCustomerPayByDate",
                   t1.UZTCOTT_DUE_DATE_SYS           AS "prepaySystemPayByDate",
                   t1.UZTCOTT_PREPAY_REDUCTION_AMT   AS "prepayOneTimeWelcomeCredit",
                   t1.UZTCOTT_PREPAY_ORIGINAL_AMT    AS "prepayEstimateOriginalAmount",
                   t1.UZTCOTT_PIA_IND                AS "payInAdvanceIndicator",
                   MAX(CASE WHEN p.rn = 1 THEN p.UZTPRMO_PRMO_CODE END)        AS "promotion1Code",
                   MAX(CASE WHEN p.rn = 1 THEN p.UZTPRMO_PRMO_DESC END)        AS "promotion1Description",
                   MAX(CASE WHEN p.rn = 1 THEN p.UZTPRMO_TERMS END)            AS "promotion1Terms",
                   MAX(CASE WHEN p.rn = 1 THEN p.UZTPRMO_MKTG_MSG END)         AS "promotion1MarketingMessage",
                   MAX(CASE WHEN p.rn = 1 THEN p.UZTPRMO_TRANS_IND END)        AS "promotion1TransferIndicator",
                   MAX(CASE WHEN p.rn = 1 THEN p.UZTPRMO_VISA_IND END)         AS "promotion1VisaIndicator",
                   MAX(CASE WHEN p.rn = 2 THEN p.UZTPRMO_PRMO_CODE END)        AS "promotion2Code",
                   MAX(CASE WHEN p.rn = 2 THEN p.UZTPRMO_PRMO_DESC END)        AS "promotion2Description",
                   MAX(CASE WHEN p.rn = 2 THEN p.UZTPRMO_TERMS END)            AS "promotion2Terms",
                   MAX(CASE WHEN p.rn = 2 THEN p.UZTPRMO_MKTG_MSG END)         AS "promotion2MarketingMessage",
                   MAX(CASE WHEN p.rn = 2 THEN p.UZTPRMO_TRANS_IND END)        AS "promotion2TransferIndicator",
                   MAX(CASE WHEN p.rn = 2 THEN p.UZTPRMO_VISA_IND END)         AS "promotion2VisaIndicator"
               FROM UZRRCOT rr
               JOIN UZTCOTT t1 ON rr.UZRRCOT_CONTROL_NUM = t1.UZTCOTT_CONTROL_NUM
               LEFT JOIN (
                   SELECT
                       t2.UZTPRMO_CONTROL_NUM,
                       t2.UZTPRMO_PLAN_CODE,
                       t2.UZTPRMO_BUCKET_ID,
                       t2.UZTPRMO_PRMO_CODE,
                       t2.UZTPRMO_PRMO_DESC,
                       t2.UZTPRMO_TERMS,
                       t2.UZTPRMO_MKTG_MSG,
                       t2.UZTPRMO_TRANS_IND,
                       t2.UZTPRMO_VISA_IND,
                       ROW_NUMBER() OVER (
                           PARTITION BY t2.UZTPRMO_PLAN_CODE, t2.UZTPRMO_CONTROL_NUM
                           ORDER BY t2.UZTPRMO_PRMO_SORT_ORDER
                       ) AS rn
                   FROM UZTPRMO t2
               ) p ON t1.UZTCOTT_PLAN_CODE = p.UZTPRMO_PLAN_CODE
                   AND t1.UZTCOTT_CONTROL_NUM = p.UZTPRMO_CONTROL_NUM
                   AND t1.UZTCOTT_BUCKET_ID = p.UZTPRMO_BUCKET_ID
               WHERE rr.UZRRCOT_TRANSACTION_ID = '<transactionId>'
               	AND t1.UZTCOTT_DUE_DATE_CUST IS NOT null
               GROUP BY
                   t1.UZTCOTT_PLAN_CODE,
                   t1.UZTCOTT_PLAN_DESC,
                   t1.UZTCOTT_SORT_ORDER,
                   t1.UZTCOTT_THERM_PRICE,
                   t1.UZTCOTT_DURATION,
                   t1.UZTCOTT_SVC_CHARGE,
                   t1.UZTCOTT_SIGN_UP_CHARGE,
                   t1.UZTCOTT_CAP_AMOUNT,
                   t1.UZTCOTT_PROTECT_FEE,
                   t1.UZTCOTT_CANCEL_FEE,
                   t1.UZTCOTT_HIGH_CSC,
                   t1.UZTCOTT_LOW_CSC,
                   t1.UZTCOTT_MKTG_TERMS,
                   t1.UZTCOTT_TERMS,
                   t1.UZTCOTT_EXT_TERMS,
                   t1.UZTCOTT_RESTRICTED_IND,
                   t1.UZTCOTT_PRE_PAY_IND,
                   t1.UZTCOTT_ESTIMATED_CHG,
                   t1.UZTCOTT_ESTIMATED_CONS,
                   t1.UZTCOTT_DUE_DATE_CUST,
                   t1.UZTCOTT_DUE_DATE_SYS,
                   t1.UZTCOTT_PREPAY_REDUCTION_AMT,
                   t1.UZTCOTT_PREPAY_ORIGINAL_AMT,
                   t1.UZTCOTT_PIA_IND
        """;

    public static final String GET_PRE_PAY_QUOTE = """
            SELECT * FROM UABOPEN a
              JOIN uzbenro e ON a."UABOPEN_PREM_CODE" = e."UZBENRO_PREM_CODE"
            WHERE a."UABOPEN_CUST_CODE" = '<customerCode>'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String UPDATE_PRE_PAY_QUOTE = """
            UPDATE UABOPEN a
            SET a.UABOPEN_DUE_DATE = SYSDATE - 10
            WHERE a.UABOPEN_CUST_CODE = '<customerCode>'
            """;

    public static final String DELETE_URBLEX_BY_CUSTOMER_CODE = """
            DELETE
            FROM UBRBLEX a
            WHERE a.UBRBLEX_CUST_CODE = '<customerCode>'
            """;

    public static final String GET_ENROLLMENT_RECORD_BY_CUSTOMER_CODE = """
            SELECT *
            FROM UZBENRO a
            WHERE a.UZBENRO_CUST_CODE = ?
            ORDER BY UZBENRO_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_MARKETER_SWITCH_ENROLLMENT_RECORD_BY_CUSTOMER_LAST_NAME = """
            SELECT *
            FROM uzbenro
            WHERE uzbenro_enro_status_date >= SYSDATE - NUMTODSINTERVAL(1, 'MINUTE')
              AND uzbenro_cust_name = ?
              AND uzbenro_type_code = 'MKSW'
            ORDER BY uzbenro_enro_status_date DESC, uzbenro_enro_num DESC
            """;

    public static final String GET_LATEST_UZRRCOT = """
             SELECT * FROM UZRRCOT
             ORDER BY UZRRCOT_ACTIVITY_DATE DESC
             FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_CUSTOMER_INFORMATION_WITH_TEXT_NO_RECORD = """    
     WITH cand AS (
         SELECT
           b.UZBENRO_CUST_CODE,
           b.UZBENRO_PREM_CODE,
           c.UCBCUST_FIRST_NAME,
           c.UCBCUST_LAST_NAME,
           b.UZBENRO_ACTIVITY_DATE   AS activity_dt,
           b.UZBENRO_TYPE_CODE       AS tran_type,
           s.UCRSERV_SCLS_CODE       AS scls_code,
           b.UZBENRO_CRED_SCORE      AS credit_score
         FROM UZBENRO b
         JOIN UCBCUST c
           ON c.UCBCUST_CUST_CODE = b.UZBENRO_CUST_CODE
         JOIN UCRSERV s
           ON s.UCRSERV_CUST_CODE = b.UZBENRO_CUST_CODE
          AND s.UCRSERV_PREM_CODE = b.UZBENRO_PREM_CODE
         WHERE s.UCRSERV_SCLS_CODE = 'RS'
           AND b.UZBENRO_CRED_SCORE_STATUS = 'TEXT'
           AND b.UZBENRO_CRED_SCORE_TEXT = 'NO RECORD FOUND'
           AND c.UCBCUST_FIRST_NAME IS NOT NULL
       ),
           top_50 AS (
             SELECT *
             FROM cand
             ORDER BY credit_score DESC, activity_dt DESC
             FETCH FIRST 50 ROWS ONLY
           )
       SELECT *
       FROM top_50
       ORDER BY  DBMS_RANDOM.VALUE
       FETCH FIRST 1 ROWS ONLY
 """;

    public static final String GET_CUSTOMER_INFORMATION_WITH_CREDIT_FREEZE = """    
            WITH cand AS (
                                 SELECT
                                   b.UZBENRO_CUST_CODE,
                                   b.UZBENRO_PREM_CODE,
                                   c.UCBCUST_FIRST_NAME,
                                   c.UCBCUST_LAST_NAME,
                                   b.UZBENRO_ACTIVITY_DATE AS activity_dt,
                                   s.UCRSERV_SCLS_CODE AS scls_code,
                                   b.UZBENRO_CRED_SCORE_STATUS,
                                   b.UZBENRO_CRED_SCORE_TEXT
                                 FROM UZBENRO b
                                 JOIN UCBCUST c
                                   ON c.UCBCUST_CUST_CODE = b.UZBENRO_CUST_CODE
                                 JOIN UCRSERV s
                                   ON s.UCRSERV_CUST_CODE = b.UZBENRO_CUST_CODE
                                  AND s.UCRSERV_PREM_CODE = b.UZBENRO_PREM_CODE
                                 WHERE s.UCRSERV_SCLS_CODE = 'RS'
                                     AND  b.uzbenro_enro_status = 'BADC'
                               )
                               SELECT *
                               FROM (
                                 SELECT *
                                 FROM cand
                                 ORDER BY activity_dt DESC
                               )
                               WHERE ROWNUM = 1

 """;

    private DBQuery() {
    }

}
