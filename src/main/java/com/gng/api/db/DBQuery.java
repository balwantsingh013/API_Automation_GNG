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

    public static final String GET_ACTIVE_CUSTOMER_AND_PREMISES_CODE_ACTIVE= """
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
              AND T3.UCRSERV_SCLS_CODE IN ('RS')
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
            SELECT T2.UCBCUST_LAST_NAME, T1.*
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
            SELECT
                user_name
            FROM
                users
            WHERE
                active = 1
            ORDER BY
                RAND()
            LIMIT 1
            """;

    public static final String SELECT_ACTIVE_USER_NAME= """
            SELECT user_name
            FROM users u
            WHERE u.active = 1
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

    public static final String SELECT_ACCOUNT_WITH_FIRST_NAME= """
            SELECT b.ucbcust_first_name, a.ucracct_cust_code, a.ucracct_prem_code
                        FROM ucracct a
                        JOIN ucbcust b
                            ON a.ucracct_cust_code = b.ucbcust_cust_code
                        WHERE b.ucbcust_first_name IS NOT NULL
                        AND LENGTH(a.ucracct_cust_code)>=5
                        FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITHOUT_FIRST_NAME= """
            SELECT b.ucbcust_first_name, a.ucracct_cust_code, a.ucracct_prem_code
                        FROM ucracct a
                        JOIN ucbcust b
                            ON a.ucracct_cust_code = b.ucbcust_cust_code
                        WHERE b.ucbcust_first_name IS NULL
                        AND LENGTH(a.ucracct_cust_code)>=5
                        order by ucracct_cust_code desc
                        FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_RESIDENTIAL_ACCOUNT= """
            SELECT UCRACCT_PREM_CODE, UCRACCT_CUST_CODE
                FROM
                ucracct
                join
                 ucrserv
                 ON ucracct_prem_code=ucrserv_prem_code
                 WHERE ucrserv_scls_code = 'RS'
                 and length(ucracct_cust_code)>=5
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_COMMERCIAL_ACCOUNT= """
            SELECT UCRACCT_PREM_CODE, UCRACCT_CUST_CODE
                FROM
                ucracct
                join
                 ucrserv
                 ON ucracct_prem_code=ucrserv_prem_code
                 WHERE ucrserv_scls_code = 'CM'
                 and length(ucracct_cust_code)>=5
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

    public static final String SELECT_LOGIN_ID_BY_VALUE = """
            SELECT gzrapil_login_id
            FROM gcismgr.gzrapil
            WHERE gzrapil_login_id = ?
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

    public static final String SELECT_ACTIVE_ACCOUNT_WITH_SAME_NAME= """
            SELECT\s
                                        u.ucbcust_last_name,
                                        u.ucbcust_cust_code,
                                        u.ucbcust_first_name,
                                        a.ucracct_prem_code,
                                        a.ucracct_status_ind
                                    FROM\s
                                        ucbcust u
                                    JOIN\s
                                        ucracct a
                                            ON a.ucracct_cust_code = u.ucbcust_cust_code
                                    WHERE\s
                                        a.ucracct_status_ind = 'A'
                                        AND u.ucbcust_last_name IN (
                                            SELECT last_name
                                            FROM (
                                                SELECT\s
                                                    u2.ucbcust_last_name AS last_name,
                                                    COUNT(DISTINCT u2.ucbcust_first_name) AS fn_count,
                                                    COUNT(DISTINCT u2.ucbcust_cust_code) AS cust_count,
                                                    COUNT(*) AS acct_count
                                                FROM\s
                                                    ucbcust u2
                                                JOIN\s
                                                    ucracct a2
                                                        ON a2.ucracct_cust_code = u2.ucbcust_cust_code
                                                WHERE\s
                                                    a2.ucracct_status_ind = 'A'
                                                GROUP BY\s
                                                    u2.ucbcust_last_name
                                            )
                                            WHERE fn_count > 1
                                              AND cust_count > 1
                                              AND acct_count > 1
                                        )
                                    ORDER BY\s
                                        DBMS_RANDOM.VALUE
                                    FETCH FIRST 2 ROWS ONLY
            """;

    public static final String SELECT_ACTIVE_ACCOUNT_WITH_SAME_NAME2= """
            SELECT\s
                                        u.ucbcust_last_name,
                                        u.ucbcust_cust_code,
                                        u.ucbcust_first_name,
                                        a.ucracct_prem_code,
                                        a.ucracct_status_ind
                                    FROM\s
                                        ucbcust u
                                    JOIN\s
                                        ucracct a
                                            ON a.ucracct_cust_code = u.ucbcust_cust_code
                                    WHERE\s
                                        a.ucracct_status_ind = 'A'
                                        AND u.ucbcust_first_name=?
                                        AND u.ucbcust_last_name IN (
                                            SELECT last_name
                                            FROM (
                                                SELECT\s
                                                    u2.ucbcust_last_name AS last_name,
                                                    COUNT(DISTINCT u2.ucbcust_first_name) AS fn_count,
                                                    COUNT(DISTINCT u2.ucbcust_cust_code) AS cust_count,
                                                    COUNT(*) AS acct_count
                                                FROM\s
                                                    ucbcust u2
                                                JOIN\s
                                                    ucracct a2
                                                        ON a2.ucracct_cust_code = u2.ucbcust_cust_code
                                                WHERE\s
                                                    a2.ucracct_status_ind = 'A'
                                                GROUP BY\s
                                                    u2.ucbcust_last_name
                                            )
                                            WHERE fn_count > 1
                                              AND cust_count > 1
                                              AND acct_count > 1
                                        )
                                    ORDER BY\s
                                        DBMS_RANDOM.VALUE
                                    FETCH FIRST 2 ROWS ONLY
            """;

    public static final String SELECT_NEW_ACCOUNT_ONLY= """
            SELECT T1.UCRACCT_CUST_CODE,
                   T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1
            WHERE T1.UCRACCT_STATUS_IND = 'N'
              AND NOT EXISTS (
                  SELECT 1
                  FROM UCRACCT T2
                  WHERE T2.UCRACCT_CUST_CODE = T1.UCRACCT_CUST_CODE
                    AND T2.UCRACCT_PREM_CODE = T1.UCRACCT_PREM_CODE
                    AND T2.UCRACCT_STATUS_IND != 'N'
              )
            ORDER BY DBMS_RANDOM.VALUE
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_VALID_USAGE_HISTORY_ACCOUNT_NEW = """
            SELECT h.urrshis_consumption, h.*
                FROM urrshis h
                WHERE h.urrshis_consumption = 0.000
                AND h.urrshis_actn_code = 'READ'
                AND h.urrshis_action_date >= TRUNC(sysdate - 365)
                FETCH FIRST 1 ROWS ONLY

""";

    public static final String SELECT_VALID_USAGE_HISTORY_ACCOUNT = """
        SELECT ucracct_cust_code,
               ucracct_prem_code,
               ucracct_status_ind
        FROM (
            SELECT DISTINCT
                   bh.ubbbhst_cust_code AS ucracct_cust_code,
                   bh.ubbbhst_prem_code AS ucracct_prem_code,
                   ua.ucracct_status_ind
            FROM ubbbhst bh
            JOIN ucracct ua
              ON ua.ucracct_cust_code = bh.ubbbhst_cust_code
             AND ua.ucracct_prem_code = bh.ubbbhst_prem_code
            WHERE ua.ucracct_status_ind <> 'N'
              AND LENGTH(ua.ucracct_cust_code) >= 5
              AND NVL(bh.ubbbhst_cancel_ind, 0) = 0
              AND bh.ubbbhst_printed_date > ADD_MONTHS(TRUNC(SYSDATE), -12)
              AND EXISTS (
                    SELECT 1
                    FROM urrshis ur
                    WHERE ur.urrshis_cust_code   = bh.ubbbhst_cust_code
                      AND ur.urrshis_prem_code   = bh.ubbbhst_prem_code
                      AND ur.urrshis_actn_code  IN ('READ', 'OUT')
                      AND ur.urrshis_action_date IS NOT NULL
              )
              AND EXISTS (
                    SELECT 1
                    FROM uabopen op
                    WHERE op.uabopen_bhst_tran_num = bh.ubbbhst_tran_num
              )
              AND ROWNUM <= 50
        )
        WHERE ROWNUM = 1
        """;

    public static final String SELECT_VALID_BILL_HISTORY_ACCOUNT= """
            SELECT UBBBHST_cust_code, UBBBHST_prem_code  FROM UBBBHST
            JOIN ucracct
            ON UBBBHST_cust_code= ucracct_cust_code
            WHERE UBBBHST_PRINTED_DATE > ADD_MONTHS(TRUNC(SYSDATE), -24)
            AND length(UBBBHST_cust_code)>=4
            AND ucracct_status_ind='A'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_BUDGET_BILLING_ACCOUNT= """
            WITH params AS (
                SELECT ADD_MONTHS(TRUNC(SYSDATE), -24) AS win_start,
                       TRUNC(SYSDATE)                 AS win_end
                FROM dual
            ),
            valid_bills AS (
                SELECT h.ubbbhst_cust_code,
                       h.ubbbhst_prem_code,
                       h.ubbbhst_tran_num
                FROM UBBBHST h
                CROSS JOIN params p
                WHERE h.ubbbhst_printed_date >= p.win_start
                  AND h.ubbbhst_printed_date <  p.win_end + 1
                  AND NVL(h.ubbbhst_cancel_ind, 0) = 0
                  AND EXISTS (
                        SELECT 1
                        FROM UABOPEN o
                        WHERE o.uabopen_bhst_tran_num = h.ubbbhst_tran_num
                  )
            ),
            budget_accounts AS (
                SELECT DISTINCT
                       v.ubbbhst_cust_code,
                       v.ubbbhst_prem_code
                FROM valid_bills v
                JOIN UABOPEN o
                  ON o.uabopen_bhst_tran_num = v.ubbbhst_tran_num
                WHERE o.uabopen_item_type = 'B'
                  AND NVL(o.uabopen_orig_budget_amt, 0) > 0
            )
            SELECT *
            FROM (
                SELECT /*+ ALL_ROWS */
                       b.ubbbhst_cust_code,
                       b.ubbbhst_prem_code
                FROM budget_accounts b
                ORDER BY b.ubbbhst_cust_code, b.ubbbhst_prem_code
            )
            """;

    public static final String SELECT_NO_PAYMENT_HISTORY_ACCOUNT= """
            SELECT\s
                a.ucracct_cust_code AS cust_code,
                a.ucracct_prem_code AS prem_code
            FROM UCRACCT a
            WHERE a.ucracct_status_ind = 'N'
              AND NOT EXISTS (
                    SELECT 1
                    FROM UABPYMT p
                    WHERE p.uabpymt_cust_code = a.ucracct_cust_code
                      AND p.uabpymt_prem_code = a.ucracct_prem_code
                )
            FETCH FIRST 1 ROWS ONLY
""";

    public static final String SELECT_ACTIVE_PAYMENT_HISTORY_EQUAL= """
        SELECT
                        uabpymt_cust_code      AS customer_code,
                        uabpymt_prem_code      AS premises_code,
                        COUNT(DISTINCT uabpymt_pymt_date) AS total
                    FROM UABPYMT
                    WHERE uabpymt_pymt_date > ADD_MONTHS(TRUNC(SYSDATE), -12)
                      AND LENGTH(uabpymt_cust_code) >= 4
                      AND uabpymt_amount <> 0
                    GROUP BY
                        uabpymt_cust_code,
                        uabpymt_prem_code
                    HAVING COUNT(DISTINCT uabpymt_pymt_date) = 12
                    FETCH FIRST 1 ROWS ONLY
""";

    public static final String SELECT_ACTIVE_PAYMENT_HISTORY_NOT_POSTED_REVERSAL= """
            SELECT
                g.GZBRTPP_CUST_CODE             AS customer_code,
                g.GZBRTPP_PREM_CODE             AS premises_code,
                g.GZBRTPP_ORIG_DATE             AS payment_date,
                g.GZBRTPP_AMOUNT                AS amount,
                g.GZBRTPP_PYCD_CODE             AS payment_code,
                g.GZBRTPP_PAYMENT_REF           AS payment_ref,
                g.GZBRTPP_AR_TRANS              AS ar_trans,
                g.GZBRTPP_CANCEL_TRANS          AS cancel_trans,
                g.GZBRTPP_CANCEL_IND            AS cancel_ind,
                g.GZBRTPP_ERROR_IND             AS error_ind,
                'NOT POSTED'                    AS payment_status,
                'GZBRTPP'                       AS source_table
            FROM GZBRTPP g
            WHERE g.GZBRTPP_CUST_CODE           = ?                   -- ← pass customer_code from Pre-Query 1
              AND g.GZBRTPP_PREM_CODE           = ?                   -- ← pass premises_code from Pre-Query 1
              AND g.GZBRTPP_ORIG_DATE           >= ADD_MONTHS(TRUNC(SYSDATE), -24)
              AND g.GZBRTPP_AMOUNT               < 0
              AND g.GZBRTPP_AR_TRANS             IS NULL
              AND g.GZBRTPP_CANCEL_TRANS         IS NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM UABPYMT p
                    WHERE p.UABPYMT_CUST_CODE        = g.GZBRTPP_CUST_CODE
                      AND p.UABPYMT_PREM_CODE        = g.GZBRTPP_PREM_CODE
                      AND TRUNC(p.UABPYMT_PYMT_DATE) = TRUNC(g.GZBRTPP_ORIG_DATE)
                      AND p.UABPYMT_AMOUNT           < 0
              )
            FETCH FIRST 5 ROWS ONLY
            """;

    public static final String SELECT_ACTIVE_PAYMENT_HISTORY_POSTED_REVERSAL= """
   SELECT
                   p_neg.UABPYMT_CUST_CODE         AS customer_code,
                   p_neg.UABPYMT_PREM_CODE         AS premises_code,
                   p_neg.UABPYMT_AR_TRANS          AS reversal_ar_trans,
                   p_neg.UABPYMT_CANCEL_TRANS      AS cancel_trans,
                   p_neg.UABPYMT_AMOUNT            AS reversal_amount,
                   p_neg.UABPYMT_PYMT_DATE         AS reversal_date,
                   p_pos.UABPYMT_AR_TRANS          AS original_ar_trans,
                   p_pos.UABPYMT_AMOUNT            AS original_amount,
                   p_pos.UABPYMT_PYMT_DATE         AS original_payment_date,
                   o.UABOPEN_SRAT_CODE             AS reversal_srat_code,
                   o.UABOPEN_CHARGE_DATE           AS reversal_charge_date,
                   'POSTED'                        AS payment_status,    -- ← from UABPYMT = posted
                   'UABPYMT'                       AS source_table       -- ← matches API source_table
               FROM UABPYMT p_neg
               JOIN UABPYMT p_pos
                   ON  p_pos.UABPYMT_CUST_CODE     = p_neg.UABPYMT_CUST_CODE
                   AND p_pos.UABPYMT_PREM_CODE     = p_neg.UABPYMT_PREM_CODE
                   AND p_pos.UABPYMT_AR_TRANS      = p_neg.UABPYMT_CANCEL_TRANS
                   AND p_pos.UABPYMT_AMOUNT        > 0
               JOIN UABOPEN o
                   ON  o.UABOPEN_CUST_CODE         = p_neg.UABPYMT_CUST_CODE
                   AND o.UABOPEN_PREM_CODE         = p_neg.UABPYMT_PREM_CODE
                   AND o.UABOPEN_CHARGE_DATE       = p_neg.UABPYMT_PYMT_DATE
                   AND o.UABOPEN_SRAT_CODE         = 'NSF'
               WHERE p_neg.UABPYMT_AMOUNT          < 0
                 AND p_neg.UABPYMT_PYMT_DATE       >= ADD_MONTHS(TRUNC(SYSDATE), -12)
                 AND LENGTH(p_neg.UABPYMT_CUST_CODE) >= 4
               FETCH FIRST 1 ROWS ONLY
""";

    public static final String SELECT_ACTIVE_PAYMENT_HISTORY_PENDING_PAYMENTS= """
    SELECT
                    g.GZBRTPP_CUST_CODE             AS customer_code,
                    g.GZBRTPP_PREM_CODE             AS premises_code,
                    g.GZBRTPP_ORIG_DATE             AS payment_date,
                    g.GZBRTPP_AMOUNT                AS amount,
                    g.GZBRTPP_PYCD_CODE             AS payment_code,
                    g.GZBRTPP_PAYMENT_REF           AS payment_ref,
                    g.GZBRTPP_AR_TRANS              AS ar_trans,
                    g.GZBRTPP_CANCEL_TRANS          AS cancel_trans,
                    g.GZBRTPP_CANCEL_IND            AS cancel_ind,
                    g.GZBRTPP_ERROR_IND             AS error_ind,
                    'NOT POSTED'                    AS payment_status,    -- ← from GZBRTPP = not posted
                    'GZBRTPP'                       AS source_table       -- ← matches API source_table
                FROM GZBRTPP g
                JOIN UCRACCT a
                    ON  a.UCRACCT_CUST_CODE         = g.GZBRTPP_CUST_CODE
                    AND a.UCRACCT_PREM_CODE         = g.GZBRTPP_PREM_CODE
                    AND a.UCRACCT_STATUS_IND        IN ('A', 'N', 'F', 'I')
                WHERE g.GZBRTPP_AR_TRANS            IS NULL
                  AND NVL(g.GZBRTPP_CANCEL_IND,'N') = 'N'
                  AND NVL(g.GZBRTPP_ERROR_IND, 'N') = 'N'
                  AND g.GZBRTPP_ORIG_DATE           >= ADD_MONTHS(TRUNC(SYSDATE), -12)
                  AND NOT EXISTS (
                        SELECT 1 FROM UABPYMT p
                        WHERE p.UABPYMT_CUST_CODE      = g.GZBRTPP_CUST_CODE
                          AND p.UABPYMT_PREM_CODE      = g.GZBRTPP_PREM_CODE
                          AND p.UABPYMT_AMOUNT         > 0
                          AND p.UABPYMT_CANCEL_TRANS   IS NULL
                          AND p.UABPYMT_PYMT_DATE      >= ADD_MONTHS(TRUNC(SYSDATE), -12)
                  )
                FETCH FIRST 1 ROWS ONLY
""";

    public static final String SELECT_ACTIVE_PAYMENT_HISTORY_POSTED_AND_PENDING_PAYMENTS= """
    SELECT
                    p.UABPYMT_CUST_CODE             AS customer_code,
                    p.UABPYMT_PREM_CODE             AS premises_code,
                    p.UABPYMT_AR_TRANS              AS ar_trans,
                    p.UABPYMT_AMOUNT                AS amount,
                    p.UABPYMT_PYMT_DATE             AS payment_date,
                    p.UABPYMT_PYCD_CODE             AS payment_code,
                    'POSTED'                        AS payment_status,    -- ← from UABPYMT = posted
                    'UABPYMT'                       AS source_table
                FROM UABPYMT p
                JOIN UCRACCT a
                    ON  a.UCRACCT_CUST_CODE         = p.UABPYMT_CUST_CODE
                    AND a.UCRACCT_PREM_CODE         = p.UABPYMT_PREM_CODE
                    AND a.UCRACCT_STATUS_IND        IN ('A', 'N', 'F', 'I')
                WHERE p.UABPYMT_AMOUNT              > 0
                  AND p.UABPYMT_CANCEL_TRANS        IS NULL
                  AND p.UABPYMT_PYMT_DATE           >= ADD_MONTHS(TRUNC(SYSDATE), -12)
                  AND LENGTH(p.UABPYMT_CUST_CODE)   >= 4
                  AND EXISTS (
                        SELECT 1 FROM GZBRTPP g
                        WHERE g.GZBRTPP_CUST_CODE          = p.UABPYMT_CUST_CODE
                          AND g.GZBRTPP_PREM_CODE          = p.UABPYMT_PREM_CODE
                          AND g.GZBRTPP_AR_TRANS           IS NULL
                          AND NVL(g.GZBRTPP_CANCEL_IND,'N') = 'N'
                          AND NVL(g.GZBRTPP_ERROR_IND, 'N') = 'N'
                          AND g.GZBRTPP_ORIG_DATE          >= ADD_MONTHS(TRUNC(SYSDATE), -12)
                  )
                FETCH FIRST 1 ROWS ONLY
""";

    public static final String SELECT_ACTIVE_PAYMENT_HISTORY_POSTED_PAYMENTS= """
   SELECT
                   p.UABPYMT_CUST_CODE             AS customer_code,
                   p.UABPYMT_PREM_CODE             AS premises_code,
                   p.UABPYMT_AR_TRANS              AS ar_trans,
                   p.UABPYMT_AMOUNT                AS amount,
                   p.UABPYMT_PYMT_DATE             AS payment_date,
                   p.UABPYMT_PYCD_CODE             AS payment_code,
                   'POSTED'                        AS payment_status,    -- ← from UABPYMT = posted
                   'UABPYMT'                       AS source_table
               FROM UABPYMT p
               JOIN UCRACCT a
                   ON  a.UCRACCT_CUST_CODE         = p.UABPYMT_CUST_CODE
                   AND a.UCRACCT_PREM_CODE         = p.UABPYMT_PREM_CODE
                   AND a.UCRACCT_STATUS_IND        IN ('A', 'N', 'F', 'I')
               WHERE p.UABPYMT_AMOUNT              > 0
                 AND p.UABPYMT_CANCEL_TRANS        IS NULL
                 AND p.UABPYMT_PYMT_DATE           >= ADD_MONTHS(TRUNC(SYSDATE), -12)
                 AND LENGTH(p.UABPYMT_CUST_CODE)   >= 4
                 AND NOT EXISTS (
                       SELECT 1 FROM GZBRTPP g
                       WHERE g.GZBRTPP_CUST_CODE          = p.UABPYMT_CUST_CODE
                         AND g.GZBRTPP_PREM_CODE          = p.UABPYMT_PREM_CODE
                         AND g.GZBRTPP_AR_TRANS           IS NULL
                         AND NVL(g.GZBRTPP_CANCEL_IND,'N') = 'N'
                         AND NVL(g.GZBRTPP_ERROR_IND, 'N') = 'N'
                         AND g.GZBRTPP_ORIG_DATE          >= ADD_MONTHS(TRUNC(SYSDATE), -12)
                 )
               FETCH FIRST 1 ROWS ONLY
""";

    public static final String SELECT_ACTIVE_PAYMENT_HISTORY_NOT_POSTED2= """
    SELECT
                    g.GZBRTPP_CUST_CODE             AS customer_code,
                    g.GZBRTPP_PREM_CODE             AS premises_code,
                    g.GZBRTPP_ORIG_DATE             AS payment_date,
                    g.GZBRTPP_AMOUNT                AS amount,
                    g.GZBRTPP_PYCD_CODE             AS payment_code,
                    g.GZBRTPP_PAYMENT_REF           AS payment_ref,
                    g.GZBRTPP_AR_TRANS              AS ar_trans,
                    g.GZBRTPP_CANCEL_TRANS          AS cancel_trans,
                    g.GZBRTPP_CANCEL_IND            AS cancel_ind,
                    g.GZBRTPP_ERROR_IND             AS error_ind,
                    'NOT POSTED'                    AS payment_status,    -- ← from GZBRTPP = not posted
                    'GZBRTPP'                       AS source_table
                FROM GZBRTPP g
                WHERE g.GZBRTPP_CUST_CODE           = ?                   -- ← customer_code from Pre-Query 1
                  AND g.GZBRTPP_PREM_CODE           = ?                   -- ← premises_code from Pre-Query 1
                  AND g.GZBRTPP_AR_TRANS            IS NULL
                  AND NVL(g.GZBRTPP_CANCEL_IND,'N') = 'N'
                  AND NVL(g.GZBRTPP_ERROR_IND, 'N') = 'N'
                  AND g.GZBRTPP_ORIG_DATE           >= ADD_MONTHS(TRUNC(SYSDATE), -12)
                ORDER BY g.GZBRTPP_ORIG_DATE DESC
                FETCH FIRST 5 ROWS ONLY
""";
    public static final String SELECT_ACTIVE_PAYMENT_HISTORY_POSTED2= """
            SELECT
                p.UABPYMT_CUST_CODE             AS customer_code,
                p.UABPYMT_PREM_CODE             AS premises_code,
                p.UABPYMT_AR_TRANS              AS ar_trans,
                p.UABPYMT_AMOUNT                AS amount,
                p.UABPYMT_PYMT_DATE             AS payment_date,
                p.UABPYMT_PYCD_CODE             AS payment_code,
                p.UABPYMT_CANCEL_TRANS          AS cancel_trans,
                'POSTED'                        AS payment_status,    -- ← from UABPYMT = posted
                'UABPYMT'                       AS source_table       -- ← matches API source_table
            FROM UABPYMT p
            WHERE p.UABPYMT_CUST_CODE           = ?                   -- ← customer_code from Pre-Query 1
              AND p.UABPYMT_PREM_CODE           = ?                   -- ← premises_code from Pre-Query 1
              AND p.UABPYMT_AMOUNT              > 0
              AND p.UABPYMT_CANCEL_TRANS        IS NULL
              AND p.UABPYMT_PYMT_DATE           >= ADD_MONTHS(TRUNC(SYSDATE), -12)
            ORDER BY p.UABPYMT_PYMT_DATE DESC
            FETCH FIRST 5 ROWS ONLY
            """;

    public static final String SELECT_ACTIVE_PAYMENT_HISTORY_NON_POSTED_PAYMENTS= """
    SELECT
                    g.GZBRTPP_CUST_CODE             AS customer_code,
                    g.GZBRTPP_PREM_CODE             AS premises_code,
                    g.GZBRTPP_ORIG_DATE             AS payment_date,
                    g.GZBRTPP_AMOUNT                AS amount,
                    g.GZBRTPP_PYCD_CODE             AS payment_code,
                    g.GZBRTPP_PAYMENT_REF           AS payment_ref,
                    g.GZBRTPP_AR_TRANS              AS ar_trans,
                    g.GZBRTPP_CANCEL_TRANS          AS cancel_trans,
                    g.GZBRTPP_CANCEL_IND            AS cancel_ind,
                    g.GZBRTPP_ERROR_IND             AS error_ind,
                    'NOT POSTED'                    AS payment_status,    -- ← from GZBRTPP = not posted
                    'GZBRTPP'                       AS source_table
                FROM GZBRTPP g
                WHERE g.GZBRTPP_CUST_CODE           = ?                   -- ← customer_code from Pre-Query 1
                  AND g.GZBRTPP_PREM_CODE           = ?                   -- ← premises_code from Pre-Query 1
                  AND g.GZBRTPP_ORIG_DATE           >= ADD_MONTHS(TRUNC(SYSDATE), -24)
                  AND g.GZBRTPP_AR_TRANS            IS NULL               -- never posted to AR
                  AND g.GZBRTPP_CANCEL_TRANS        IS NULL               -- no cancel linkage
                  AND NVL(g.GZBRTPP_CANCEL_IND,'N') = 'N'
                  AND NVL(g.GZBRTPP_ERROR_IND, 'N') = 'N'
                FETCH FIRST 5 ROWS ONLY
""";

    public static final String SELECT_ACTIVE_PAYMENT_HISTORY_NO_REVERSAL= """
    WITH win AS (
                    SELECT
                        ADD_MONTHS(TRUNC(SYSDATE), -24) AS start_dt,
                        DATE '2026-03-01'              AS snapshot_cutoff,
                        DATE '2026-03-01' - 14         AS cutoff_minus_lag
                    FROM dual
                ),
                valid_accts AS (
                    SELECT
                        u.ucracct_cust_code  AS cust_code,
                        u.ucracct_prem_code  AS prem_code,
                        u.ucracct_status_ind AS accountstatus
                    FROM UCRACCT u
                ),
                gw_attempts AS (
                    SELECT
                        g.gzbrtpp_cust_code AS cust_code,
                        g.gzbrtpp_prem_code AS prem_code,
                        g.gzbrtpp_orig_date AS paymentdate,
                        g.gzbrtpp_amount    AS amount,
                        g.gzbrtpp_pycd_code AS paymentcode
                    FROM GZBRTPP g
                    CROSS JOIN win w
                    JOIN valid_accts va
                        ON  va.cust_code = g.gzbrtpp_cust_code
                        AND va.prem_code = g.gzbrtpp_prem_code
                    WHERE g.gzbrtpp_amount   > 0
                      AND g.gzbrtpp_orig_date BETWEEN w.start_dt AND w.cutoff_minus_lag
                ),
                posted_pos AS (
                    SELECT
                        p.uabpymt_cust_code AS cust_code,
                        p.uabpymt_prem_code AS prem_code,
                        p.uabpymt_pymt_date AS paymentdate,
                        p.uabpymt_amount    AS amount
                    FROM UABPYMT p
                    WHERE p.uabpymt_amount > 0
                )
                SELECT
                    g.cust_code,
                    g.prem_code,
                    g.paymentdate,
                    g.amount,
                    g.paymentcode,
                    va.accountstatus,
                    'NON_POSTED_GATEWAY_FAILURE' AS status
                FROM gw_attempts g
                JOIN valid_accts va
                    ON  va.cust_code = g.cust_code
                    AND va.prem_code = g.prem_code
                LEFT JOIN posted_pos p
                    ON  p.cust_code   = g.cust_code
                    AND p.prem_code   = g.prem_code
                    AND p.amount      = g.amount
                    AND p.paymentdate BETWEEN g.paymentdate - 14
                                          AND g.paymentdate + 14
                WHERE p.cust_code IS NULL
                ORDER BY g.paymentdate DESC
                FETCH FIRST 1 ROWS ONLY
""";


    public static final String SELECT_ACTIVE_PAYMENT_HISTORY_LESS= """
       SELECT
                       uabpymt_cust_code AS customer_code,
                       uabpymt_prem_code AS premises_code,
                       COUNT(DISTINCT uabpymt_pymt_date) AS months_with_payments
                   FROM UABPYMT
                   WHERE uabpymt_pymt_date >= ADD_MONTHS(TRUNC(SYSDATE), -12)
                     AND uabpymt_amount <> 0
                     AND LENGTH(uabpymt_cust_code) >= 4
                   GROUP BY
                       uabpymt_cust_code,
                       uabpymt_prem_code
                   HAVING COUNT(DISTINCT uabpymt_pymt_date) < 12
                   FETCH FIRST 1 ROWS ONLY
""";


    public static final String SELECT_ACTIVE_PAYMENT_HISTORY_TOO_OLD= """
    SELECT\s
                    a.ucracct_cust_code AS cust_code,
                    a.ucracct_prem_code AS prem_code
                FROM UCRACCT a
                WHERE a.ucracct_status_ind <> 'N'
                  AND LENGTH(a.ucracct_cust_code) >= 4
                  AND EXISTS (
                        SELECT 1
                        FROM UABPYMT p_old
                        WHERE p_old.uabpymt_cust_code = a.ucracct_cust_code
                          AND p_old.uabpymt_prem_code = a.ucracct_prem_code
                          AND p_old.uabpymt_amount <> 0
                          AND p_old.uabpymt_pymt_date <= ADD_MONTHS(TRUNC(SYSDATE), -12)
                    )
                  AND NOT EXISTS (
                        SELECT 1
                        FROM UABPYMT p_recent
                        WHERE p_recent.uabpymt_cust_code = a.ucracct_cust_code
                          AND p_recent.uabpymt_prem_code = a.ucracct_prem_code
                          AND p_recent.uabpymt_amount <> 0
                          AND p_recent.uabpymt_pymt_date > ADD_MONTHS(TRUNC(SYSDATE), -12)
                    )
                FETCH FIRST 1 ROWS ONLY
""";

    public static final String SELECT_ACTIVE_NO_PAYMENT_HISTORY_ACCOUNT= """
            SELECT\s
                a.ucracct_cust_code AS cust_code,
                a.ucracct_prem_code AS prem_code
            FROM UCRACCT a
            WHERE a.ucracct_status_ind <> 'N'
              AND NOT EXISTS (
                    SELECT 1
                    FROM UABPYMT p
                    WHERE p.uabpymt_cust_code = a.ucracct_cust_code
                      AND p.uabpymt_prem_code = a.ucracct_prem_code
                )
            FETCH FIRST 1 ROWS ONLY
""";

    public static final String SELECT_VALID_PAYMENT_HISTORY_ACCOUNT= """
            SELECT /*+ FIRST_ROWS(1) USE_NL(a) LEADING(p a) */
                p.uabpymt_cust_code AS cust_code,
                p.uabpymt_prem_code AS prem_code,
                a.ucracct_status_ind AS account_status
            FROM (
                -- Deduplicate cheaply BEFORE the JOIN
                SELECT DISTINCT
                    uabpymt_cust_code,
                    uabpymt_prem_code
                FROM UABPYMT
                WHERE uabpymt_pymt_date >= ADD_MONTHS(TRUNC(SYSDATE), -24)
                  AND uabpymt_amount    <> 0
            ) p
            LEFT JOIN UCRACCT a
                ON a.ucracct_cust_code = p.uabpymt_cust_code
               AND a.ucracct_prem_code = p.uabpymt_prem_code
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_NO_USAGE_HISTORY_ACTIVE_ACCOUNT= """
            SELECT\s
                ua.ucracct_cust_code,
                ua.ucracct_prem_code,
                ua.ucracct_status_ind
            FROM\s
                ucracct ua
            WHERE\s
                LENGTH(ua.ucracct_cust_code) >= 5
                AND ua.ucracct_status_ind = 'A'
                AND NOT EXISTS (
                    SELECT 1\s
                    FROM ubbchst ch
                    WHERE ch.ubbchst_cust_code = ua.ucracct_cust_code
                      AND ch.ubbchst_prem_code = ua.ucracct_prem_code
                )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_USAGE_HISTORY_OLD= """
            SELECT
                ubbbhst_cust_code                                              AS customer_code,
                ubbbhst_prem_code                                              AS premises_code,
                MAX(ubbbhst_printed_date)                                      AS most_recent_bill,
                COUNT(*)                                                       AS total_bills,
                MONTHS_BETWEEN(TRUNC(SYSDATE), MIN(ubbbhst_printed_date))     AS months_of_history
            FROM UBBBHST SAMPLE(10)
            WHERE ubbbhst_cancel_ind IS NULL
            AND length(ubbbhst_cust_code)>=4
              AND ubbbhst_printed_date <= ADD_MONTHS(TRUNC(SYSDATE), -12)
            GROUP BY
                ubbbhst_cust_code,
                ubbbhst_prem_code
            HAVING COUNT(*) >= 4
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_USAGE_HISTORY_EQUAL = """
            SELECT ubbbhst_cust_code, ubbbhst_prem_code, COUNT(DISTINCT ubbbhst_printed_date) total
            FROM ubbbhst
            WHERE ubbbhst_printed_date > ADD_MONTHS(TRUNC(SYSDATE),- 12)
            AND LENGTH(ubbbhst_cust_code)>=4
            AND NVL(ubbbhst_cancel_ind,0) = 0
            group by ubbbhst_cust_code, ubbbhst_prem_code
            having count(ubbbhst_printed_date) = 12
            FETCH FIRST 1 ROWS ONLY
        """;

    public static final String SELECT_USAGE_HISTORY_LESS= """
            SELECT *
            FROM (
                SELECT
                    h.ubbbhst_cust_code  AS customer_code,
                    h.ubbbhst_prem_code  AS premises_code,
                    COUNT(*)             AS total_bills
                FROM UBBBHST SAMPLE(99) h
                WHERE h.ubbbhst_cancel_ind IS NULL
            	  AND length(h.ubbbhst_cust_code)>=4
                  AND h.ubbbhst_printed_date >= ADD_MONTHS(TRUNC(SYSDATE), -12)
                  AND h.ubbbhst_printed_date <  TRUNC(SYSDATE)
                GROUP BY h.ubbbhst_cust_code, h.ubbbhst_prem_code
                HAVING COUNT(*) BETWEEN 1 AND 11
            )
            WHERE ROWNUM = 1
            """;


    public static final String SELECT_USAGE_HISTORY_GREATER= """
            SELECT\s
                ubbbhst_cust_code,\s
                ubbbhst_prem_code,\s
                COUNT(DISTINCT ubbbhst_printed_date) AS total
            FROM ubbbhst
            WHERE ubbbhst_printed_date > ADD_MONTHS(TRUNC(SYSDATE), -12)
              AND LENGTH(ubbbhst_cust_code) >= 4
              AND NVL(ubbbhst_cancel_ind, 0) = 0
            GROUP BY\s
                ubbbhst_cust_code,\s
                ubbbhst_prem_code
            HAVING COUNT(DISTINCT ubbbhst_printed_date) = 12
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_BILL_HISTORY_ONE= """
            select * from ubbbhst a,         \s
                          (select bh.ubbbhst_cust_code, bh.ubbbhst_prem_code, max(bh.ubbbhst_tran_num) tranNum
                          from ubbbhst bh
                          where bh.ubbbhst_ending_bal <> 0
                          group by bh.ubbbhst_cust_code, bh.ubbbhst_prem_code
                          having count(1) = 1) b
            where a.ubbbhst_cust_code = b.ubbbhst_cust_code
            and a.ubbbhst_prem_code = b.ubbbhst_prem_code
            and a.ubbbhst_tran_num = b.trannum
            and TRUNC(a.ubbbhst_printed_date) > ADD_MONTHS(TRUNC(SYSDATE), -12)
            AND a.ubbbhst_cust_code= '6085156'
            """;

    public static final String SELECT_BILL_HISTORY_INTERSTATE= """
            SELECT
                h.ubbbhst_cust_code,
                h.ubbbhst_prem_code,
                h.ubbbhst_printed_date,
                o.uabopen_srat_code
            FROM (
                SELECT DISTINCT utrsrat_srat_code
                FROM UTRSRAT
                WHERE UPPER(utrsrat_bill_print_desc) LIKE '%INTERSTATE%'
            ) s
            JOIN UABOPEN o
              ON o.uabopen_srat_code = s.utrsrat_srat_code
            JOIN UBBBHST h
              ON h.ubbbhst_tran_num = o.uabopen_bhst_tran_num
            WHERE NVL(h.ubbbhst_cancel_ind,0) = 0
              AND h.ubbbhst_printed_date >= ADD_MONTHS(TRUNC(SYSDATE), -12)
              FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACTIVE_PAPERLESS_ELIGIBLE= """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    g.GZBEMCP_ACCOUNT_IND,
                    g.GZBEMCP_EFFECTIVE_DATE,
                    g.GZBEMCP_EXPIRATION_DATE,
                    g.GZBEMCP_ACTIVITY_DATE,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY\s
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            ),
            latest_email AS (
                SELECT
                    GZBEMCP_CUST_CODE,
                    GZBEMCP_EMAIL_ADDR,
                    GZBEMCP_EFFECTIVE_DATE,
                    GZBEMCP_EXPIRATION_DATE,
                    GZBEMCP_ACTIVITY_DATE
                FROM active_email
                WHERE rn = 1
            ),
            latest_ocsepci AS (
                SELECT
                    p.OCSEPCI_ID,
                    p.OCSEPCI_CUST_CODE,
                    p.OCSEPCI_PREM_CODE,
                    p.OCSEPCI_EMAIL_ADDR,
                    p.OCSEPCI_BILL_PRES_TYPE,
                    p.OCSEPCI_CORR_DEL_TYPE,
                    p.OCSEPCI_EMAIL_SENT_DATE,
                    p.OCSEPCI_EMAIL_COMP_DATE,
                    p.OCSEPCI_EMAIL_EXP_DATE,
                    p.OCSEPCI_CONF_STATUS,
                    p.OCSEPCI_ACTIVITY_DATE,
                    ROW_NUMBER() OVER (
                        PARTITION BY p.OCSEPCI_CUST_CODE, p.OCSEPCI_PREM_CODE
                        ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
                    ) AS rn
                FROM OCSEPCI p
            )
            SELECT
                a.UCRACCT_CUST_CODE              AS customerCode,
                a.UCRACCT_PREM_CODE              AS premisesCode,
                a.UCRACCT_STATUS_IND             AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE         AS currentBillDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE          AS currentCorrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR             AS bannerEmail,
                o.OCSEPCI_ID                     AS latestTokenIdentifier,
                o.OCSEPCI_EMAIL_EXP_DATE         AS latestTokenExpirationDate,
                o.OCSEPCI_EMAIL_ADDR             AS latestPendingEmail,
                o.OCSEPCI_BILL_PRES_TYPE         AS latestPendingBillType,
                o.OCSEPCI_CORR_DEL_TYPE          AS latestPendingCorrType,
                o.OCSEPCI_EMAIL_SENT_DATE        AS latestEmailSentDate,
                o.OCSEPCI_EMAIL_COMP_DATE        AS latestEmailCompletedDate,
                o.OCSEPCI_CONF_STATUS            AS latestConfStatus,
                o.OCSEPCI_ACTIVITY_DATE          AS latestPendingActivityDate
            FROM UCRACCT a
            JOIN latest_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
            LEFT JOIN latest_ocsepci o
                ON o.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
               AND o.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
               AND o.rn = 1
            WHERE a.UCRACCT_STATUS_IND IN ('A')
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_TC_89_ACTIVE_NO_VALID_TOKEN = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSTIMESTAMP, 'FF9')), a.UCRACCT_ACTIVITY_DATE ASC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_TC_89_ACTIVE_NO_VALID_TOKEN_CANDIDATES = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE  AS customerCode,
                a.UCRACCT_PREM_CODE  AS premisesCode,
                a.UCRACCT_STATUS_IND AS accountStatus,
                e.GZBEMCP_EMAIL_ADDR AS bannerEmail
            FROM UCRACCT a
            JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
              )
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSTIMESTAMP, 'FF9')),
                     a.UCRACCT_ACTIVITY_DATE ASC
            FETCH FIRST 100 ROWS ONLY
            """;

    /** Same as SELECT_TC_89_ACTIVE_NO_VALID_TOKEN but skips one customer/premises pair (e.g. already used by TC_79). */
    public static final String SELECT_TC_89_ACTIVE_NO_VALID_TOKEN_EXCLUDING_ACCOUNT = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE  AS customerCode,
                a.UCRACCT_PREM_CODE  AS premisesCode,
                a.UCRACCT_STATUS_IND AS accountStatus,
                e.GZBEMCP_EMAIL_ADDR AS bannerEmail
            FROM UCRACCT a
            JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NOT (a.UCRACCT_CUST_CODE = ? AND a.UCRACCT_PREM_CODE = ?)
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
              )
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_TC_90_ACTIVE_WITH_VALID_TOKEN = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            ),
            latest_valid_token AS (
                SELECT
                    p.OCSEPCI_CUST_CODE,
                    p.OCSEPCI_PREM_CODE,
                    p.OCSEPCI_EMAIL_ADDR,
                    p.OCSEPCI_ACTIVITY_DATE,
                    ROW_NUMBER() OVER (
                        PARTITION BY p.OCSEPCI_CUST_CODE, p.OCSEPCI_PREM_CODE
                        ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
                    ) AS rn
                FROM OCSEPCI p
                WHERE p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                  AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail,
                t.OCSEPCI_ACTIVITY_DATE  AS latestTokenActivityDate
            FROM UCRACCT a
            JOIN latest_valid_token t
                ON t.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
               AND t.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
               AND t.rn = 1
            JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
               AND LOWER(TRIM(e.GZBEMCP_EMAIL_ADDR)) = LOWER(TRIM(t.OCSEPCI_EMAIL_ADDR))
            WHERE a.UCRACCT_STATUS_IND = 'A'
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSTIMESTAMP, 'FF9')),
                     t.OCSEPCI_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** TC_90/103 fallback: valid unused token on premises; Banner email used for API (no token-email match required). */
    public static final String SELECT_TC_90_ACTIVE_WITH_VALID_TOKEN_RELAXED = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            ),
            latest_valid_token AS (
                SELECT
                    p.OCSEPCI_CUST_CODE,
                    p.OCSEPCI_PREM_CODE,
                    p.OCSEPCI_ACTIVITY_DATE,
                    ROW_NUMBER() OVER (
                        PARTITION BY p.OCSEPCI_CUST_CODE, p.OCSEPCI_PREM_CODE
                        ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
                    ) AS rn
                FROM OCSEPCI p
                WHERE p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                  AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail,
                t.OCSEPCI_ACTIVITY_DATE  AS latestTokenActivityDate
            FROM UCRACCT a
            JOIN latest_valid_token t
                ON t.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
               AND t.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
               AND t.rn = 1
            JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'A'
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSTIMESTAMP, 'FF9')),
                     t.OCSEPCI_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_TC_79_ACTIVE_ACCOUNT = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND e.GZBEMCP_ACCOUNT_IND = 'Y'
              AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
              AND (e.GZBEMCP_EXPIRATION_DATE IS NULL OR e.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_TC_79_ACTIVE_ACCOUNT_CANDIDATES = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND e.GZBEMCP_ACCOUNT_IND = 'Y'
              AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
              AND (e.GZBEMCP_EXPIRATION_DATE IS NULL OR e.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 25 ROWS ONLY
            """;

    public static final String SELECT_TC_83_ACTIVE_CORR_ENROLL = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NVL(a.UCRACCT_CORR_DEL_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSTIMESTAMP, 'FF9')), a.UCRACCT_ACTIVITY_DATE ASC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** TC_86: ACTIVE, bill paper (P), Banner email, no non-expired unused OCSEPCI token (FTD). */
    public static final String SELECT_TC_86_ACTIVE_BILL_NO_VALID_TOKEN = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSTIMESTAMP, 'FF9')), a.UCRACCT_ACTIVITY_DATE ASC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** TC_81: ACTIVE, bill paper (P), Banner email, no valid unused OCSEPCI token. */
    public static final String SELECT_TC_81_ACTIVE_BILL_NO_VALID_TOKEN = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE  AS customerCode,
                a.UCRACCT_PREM_CODE  AS premisesCode,
                a.UCRACCT_STATUS_IND AS accountStatus,
                e.GZBEMCP_EMAIL_ADDR AS bannerEmail
            FROM UCRACCT a
            JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
              )
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSTIMESTAMP, 'FF9')), a.UCRACCT_ACTIVITY_DATE ASC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** TC_84: ACTIVE, both channels paper (P/P), Banner email, no valid unused OCSEPCI token. */
    public static final String SELECT_TC_84_ACTIVE_BOTH_CHANNELS_NO_VALID_TOKEN = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NVL(a.UCRACCT_CORR_DEL_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
              )
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSTIMESTAMP, 'FF9')), a.UCRACCT_ACTIVITY_DATE ASC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** TC_84: same as above but skips one customer/premises pair (e.g. already used by TC_83). */
    public static final String SELECT_TC_84_ACTIVE_BOTH_CHANNELS_NO_VALID_TOKEN_EXCLUDING = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NVL(a.UCRACCT_CORR_DEL_TYPE, 'P') = 'P'
              AND NOT (a.UCRACCT_CUST_CODE = ? AND a.UCRACCT_PREM_CODE = ?)
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
              )
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSTIMESTAMP, 'FF9')), a.UCRACCT_ACTIVITY_DATE ASC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_TC_83_ACTIVE_CORR_ENROLL_EXCLUDING_ACCOUNT = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NVL(a.UCRACCT_CORR_DEL_TYPE, 'P') = 'P'
              AND NOT (a.UCRACCT_CUST_CODE = ? AND a.UCRACCT_PREM_CODE = ?)
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_TC_97_ACTIVE_BOTH_CHANNELS_ELIGIBLE = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NVL(a.UCRACCT_CORR_DEL_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_TC_97_ACTIVE_BOTH_CHANNELS_CANDIDATES = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NVL(a.UCRACCT_CORR_DEL_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSTIMESTAMP, 'FF9')),
                     a.UCRACCT_ACTIVITY_DATE ASC
            FETCH FIRST 25 ROWS ONLY
            """;

    public static final String SELECT_TC_80_NEW_ACCOUNT = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
            WHERE a.UCRACCT_STATUS_IND = 'N'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NVL(a.UCRACCT_CORR_DEL_TYPE, 'P') = 'P'
              AND e.GZBEMCP_ACCOUNT_IND = 'Y'
              AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
              AND (e.GZBEMCP_EXPIRATION_DATE IS NULL OR e.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ENROLLED_PAPERLESS= """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    g.GZBEMCP_ACCOUNT_IND,
                    g.GZBEMCP_EFFECTIVE_DATE,
                    g.GZBEMCP_EXPIRATION_DATE,
                    g.GZBEMCP_ACTIVITY_DATE,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            ),
            latest_email AS (
                SELECT
                    GZBEMCP_CUST_CODE,
                    GZBEMCP_EMAIL_ADDR,
                    GZBEMCP_EFFECTIVE_DATE,
                    GZBEMCP_EXPIRATION_DATE,
                    GZBEMCP_ACTIVITY_DATE
                FROM active_email
                WHERE rn = 1
            ),
            latest_ocsepci AS (
                SELECT
                    p.OCSEPCI_ID,
                    p.OCSEPCI_CUST_CODE,
                    p.OCSEPCI_PREM_CODE,
                    p.OCSEPCI_EMAIL_ADDR,
                    p.OCSEPCI_BILL_PRES_TYPE,
                    p.OCSEPCI_CORR_DEL_TYPE,
                    p.OCSEPCI_EMAIL_SENT_DATE,
                    p.OCSEPCI_EMAIL_COMP_DATE,
                    p.OCSEPCI_EMAIL_EXP_DATE,
                    p.OCSEPCI_CONF_STATUS,
                    p.OCSEPCI_ACTIVITY_DATE,
                    ROW_NUMBER() OVER (
                        PARTITION BY p.OCSEPCI_CUST_CODE, p.OCSEPCI_PREM_CODE
                        ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
                    ) AS rn
                FROM OCSEPCI p
            )
            SELECT
                a.UCRACCT_CUST_CODE              AS customerCode,
                a.UCRACCT_PREM_CODE              AS premisesCode,
                a.UCRACCT_STATUS_IND             AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE         AS currentBillDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE          AS currentCorrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR             AS bannerEmail,
                o.OCSEPCI_ID                     AS latestTokenIdentifier,
                o.OCSEPCI_EMAIL_EXP_DATE         AS latestTokenExpirationDate,
                o.OCSEPCI_EMAIL_ADDR             AS latestPendingEmail,
                o.OCSEPCI_BILL_PRES_TYPE         AS latestPendingBillType,
                o.OCSEPCI_CORR_DEL_TYPE          AS latestPendingCorrType,
                o.OCSEPCI_EMAIL_SENT_DATE        AS latestEmailSentDate,
                o.OCSEPCI_EMAIL_COMP_DATE        AS latestEmailCompletedDate,
                o.OCSEPCI_CONF_STATUS            AS latestConfStatus,
                o.OCSEPCI_ACTIVITY_DATE          AS latestPendingActivityDate
            FROM UCRACCT a
            JOIN latest_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
            LEFT JOIN latest_ocsepci o
                ON o.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
               AND o.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
               AND o.rn = 1
            WHERE
                a.UCRACCT_STATUS_IND = 'A'
                AND a.UCRACCT_BILL_PRES_TYPE = 'E'
                AND NOT EXISTS (
                    SELECT 1
                    FROM OCSEPCI p
                    WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                      AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                      AND NVL(p.OCSEPCI_BILL_PRES_TYPE, 'E') = 'P'
                      AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_TC_85_ACTIVE_CORR_ENROLLED = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption
            FROM UCRACCT a
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND a.UCRACCT_CORR_DEL_TYPE = 'E'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_CORR_DEL_TYPE, 'E') = 'P'
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_TC_94_ACTIVE_BOTH_CHANNELS_ENROLLED = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption
            FROM UCRACCT a
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND a.UCRACCT_BILL_PRES_TYPE = 'E'
              AND a.UCRACCT_CORR_DEL_TYPE = 'E'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND (
                        NVL(p.OCSEPCI_BILL_PRES_TYPE, 'E') = 'P'
                        OR NVL(p.OCSEPCI_CORR_DEL_TYPE, 'E') = 'P'
                    )
              )
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSTIMESTAMP, 'FF9')),
                     a.UCRACCT_ACTIVITY_DATE ASC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_TC_94_ACTIVE_BOTH_CHANNELS_ENROLLED_SIMPLE = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption
            FROM UCRACCT a
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND a.UCRACCT_BILL_PRES_TYPE = 'E'
              AND a.UCRACCT_CORR_DEL_TYPE = 'E'
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSTIMESTAMP, 'FF9')),
                     a.UCRACCT_ACTIVITY_DATE ASC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_NEW_PAPERLESS_ELIGIBLE= """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    g.GZBEMCP_ACCOUNT_IND,
                    g.GZBEMCP_EFFECTIVE_DATE,
                    g.GZBEMCP_EXPIRATION_DATE,
                    g.GZBEMCP_ACTIVITY_DATE,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY\s
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            ),
            latest_email AS (
                SELECT
                    GZBEMCP_CUST_CODE,
                    GZBEMCP_EMAIL_ADDR,
                    GZBEMCP_EFFECTIVE_DATE,
                    GZBEMCP_EXPIRATION_DATE,
                    GZBEMCP_ACTIVITY_DATE
                FROM active_email
                WHERE rn = 1
            ),
            latest_ocsepci AS (
                SELECT
                    p.OCSEPCI_ID,
                    p.OCSEPCI_CUST_CODE,
                    p.OCSEPCI_PREM_CODE,
                    p.OCSEPCI_EMAIL_ADDR,
                    p.OCSEPCI_BILL_PRES_TYPE,
                    p.OCSEPCI_CORR_DEL_TYPE,
                    p.OCSEPCI_EMAIL_SENT_DATE,
                    p.OCSEPCI_EMAIL_COMP_DATE,
                    p.OCSEPCI_EMAIL_EXP_DATE,
                    p.OCSEPCI_CONF_STATUS,
                    p.OCSEPCI_ACTIVITY_DATE,
                    ROW_NUMBER() OVER (
                        PARTITION BY p.OCSEPCI_CUST_CODE, p.OCSEPCI_PREM_CODE
                        ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
                    ) AS rn
                FROM OCSEPCI p
            )
            SELECT
                a.UCRACCT_CUST_CODE              AS customerCode,
                a.UCRACCT_PREM_CODE              AS premisesCode,
                a.UCRACCT_STATUS_IND             AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE         AS currentBillDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE          AS currentCorrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR             AS bannerEmail,
                o.OCSEPCI_ID                     AS latestTokenIdentifier,
                o.OCSEPCI_EMAIL_EXP_DATE         AS latestTokenExpirationDate,
                o.OCSEPCI_EMAIL_ADDR             AS latestPendingEmail,
                o.OCSEPCI_BILL_PRES_TYPE         AS latestPendingBillType,
                o.OCSEPCI_CORR_DEL_TYPE          AS latestPendingCorrType,
                o.OCSEPCI_EMAIL_SENT_DATE        AS latestEmailSentDate,
                o.OCSEPCI_EMAIL_COMP_DATE        AS latestEmailCompletedDate,
                o.OCSEPCI_CONF_STATUS            AS latestConfStatus,
                o.OCSEPCI_ACTIVITY_DATE          AS latestPendingActivityDate
            FROM UCRACCT a
            JOIN latest_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
            LEFT JOIN latest_ocsepci o
                ON o.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
               AND o.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
               AND o.rn = 1
            WHERE a.UCRACCT_STATUS_IND IN ('N')
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** TC_96: NEW, bill paper (P), Banner email, never enrolled, no valid unused OCSEPCI token. */
    public static final String SELECT_TC_96_NEW_NO_VALID_TOKEN = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE  AS customerCode,
                a.UCRACCT_PREM_CODE  AS premisesCode,
                a.UCRACCT_STATUS_IND AS accountStatus,
                e.GZBEMCP_EMAIL_ADDR AS bannerEmail
            FROM UCRACCT a
            JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'N'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
              )
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSTIMESTAMP, 'FF9')),
                     a.UCRACCT_ACTIVITY_DATE ASC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** TC_98: NEW, both channels paper (P/P), Banner email, never enrolled, no valid unused OCSEPCI token. */
    public static final String SELECT_TC_98_NEW_BOTH_CHANNELS_ELIGIBLE = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'N'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NVL(a.UCRACCT_CORR_DEL_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
              )
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSTIMESTAMP, 'FF9')),
                     a.UCRACCT_ACTIVITY_DATE ASC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** TC_80 / TC_98: NEW account, both channels paper, no valid unused OCSEPCI token. */
    public static final String SELECT_TC_80_NEW_BOTH_CHANNELS_NO_VALID_TOKEN_CANDIDATES = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'N'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NVL(a.UCRACCT_CORR_DEL_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY
                CASE
                    WHEN a.UCRACCT_BILL_PRES_TYPE = 'P' AND a.UCRACCT_CORR_DEL_TYPE = 'P' THEN 0
                    ELSE 1
                END,
                ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSDATE, 'J')),
                a.UCRACCT_ACTIVITY_DATE ASC
            FETCH FIRST 200 ROWS ONLY
            """;

    /** TC_80 strict: NEW both-channels eligible and never had any OCSEPCI row (true first-time enroll). */
    public static final String SELECT_TC_80_NEW_NEVER_ENROLLED_CANDIDATES = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'N'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NVL(a.UCRACCT_CORR_DEL_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
              )
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSDATE, 'J')), a.UCRACCT_ACTIVITY_DATE ASC
            FETCH FIRST 100 ROWS ONLY
            """;

    /** TC_80 strict single account: same eligibility as SELECT_TC_98_NEW_BOTH_CHANNELS_ELIGIBLE. */
    public static final String SELECT_TC_80_NEW_NEVER_ENROLLED_STRICT = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'N'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NVL(a.UCRACCT_CORR_DEL_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
              )
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSTIMESTAMP, 'FF9')),
                     a.UCRACCT_ACTIVITY_DATE ASC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** Same as SELECT_TC_80_NEW_NEVER_ENROLLED_STRICT but skips one customer/premises pair. */
    public static final String SELECT_TC_80_NEW_NEVER_ENROLLED_EXCLUDING_ACCOUNT = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'N'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NVL(a.UCRACCT_CORR_DEL_TYPE, 'P') = 'P'
              AND NOT (a.UCRACCT_CUST_CODE = ? AND a.UCRACCT_PREM_CODE = ?)
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
              )
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSTIMESTAMP, 'FF9')),
                     a.UCRACCT_ACTIVITY_DATE ASC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ANY_OCSEPCI_FOR_ACCOUNT = """
            SELECT
                p.OCSEPCI_CUST_CODE AS customerCode,
                p.OCSEPCI_PREM_CODE AS premisesCode
            FROM OCSEPCI p
            WHERE p.OCSEPCI_CUST_CODE = ?
              AND p.OCSEPCI_PREM_CODE = ?
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_VALID_UNUSED_OCSEPCI_FOR_CUSTOMER = """
            SELECT
                p.OCSEPCI_CUST_CODE AS customerCode,
                p.OCSEPCI_PREM_CODE AS premisesCode
            FROM OCSEPCI p
            WHERE p.OCSEPCI_CUST_CODE = ?
              AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
              AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
            ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_TC_100_NEW_PENDING_BILL_ENROLLMENT = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            ),
            pending_bill_enrollment AS (
                SELECT
                    p.OCSEPCI_CUST_CODE,
                    p.OCSEPCI_PREM_CODE,
                    p.OCSEPCI_ACTIVITY_DATE,
                    ROW_NUMBER() OVER (
                        PARTITION BY p.OCSEPCI_CUST_CODE, p.OCSEPCI_PREM_CODE
                        ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
                    ) AS rn
                FROM OCSEPCI p
                WHERE NVL(p.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
                  AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                  AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN pending_bill_enrollment t
                ON t.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
               AND t.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
               AND t.rn = 1
            LEFT JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'N'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSTIMESTAMP, 'FF9')),
                     t.OCSEPCI_ACTIVITY_DATE ASC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_TC_100_NEW_PENDING_BILL_ENROLLMENT_SIMPLE = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption
            FROM UCRACCT a
            WHERE a.UCRACCT_STATUS_IND = 'N'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_TC_100_NEW_WITH_VALID_BILL_TOKEN = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            ),
            latest_valid_bill_token AS (
                SELECT
                    p.OCSEPCI_CUST_CODE,
                    p.OCSEPCI_PREM_CODE,
                    p.OCSEPCI_EMAIL_ADDR,
                    p.OCSEPCI_ACTIVITY_DATE,
                    ROW_NUMBER() OVER (
                        PARTITION BY p.OCSEPCI_CUST_CODE, p.OCSEPCI_PREM_CODE
                        ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
                    ) AS rn
                FROM OCSEPCI p
                WHERE p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                  AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
                  AND NVL(p.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                t.OCSEPCI_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            JOIN latest_valid_bill_token t
                ON t.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
               AND t.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
               AND t.rn = 1
            JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
               AND LOWER(TRIM(e.GZBEMCP_EMAIL_ADDR)) = LOWER(TRIM(t.OCSEPCI_EMAIL_ADDR))
            WHERE a.UCRACCT_STATUS_IND = 'N'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
            ORDER BY t.OCSEPCI_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** TC_104 fallback: NEW account with valid unused bill token; Banner email used for API (no token-email match). */
    public static final String SELECT_TC_104_NEW_WITH_VALID_TOKEN_RELAXED = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            ),
            latest_valid_bill_token AS (
                SELECT
                    p.OCSEPCI_CUST_CODE,
                    p.OCSEPCI_PREM_CODE,
                    p.OCSEPCI_ACTIVITY_DATE,
                    ROW_NUMBER() OVER (
                        PARTITION BY p.OCSEPCI_CUST_CODE, p.OCSEPCI_PREM_CODE
                        ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
                    ) AS rn
                FROM OCSEPCI p
                WHERE p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                  AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
                  AND NVL(p.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail,
                t.OCSEPCI_ACTIVITY_DATE  AS latestTokenActivityDate
            FROM UCRACCT a
            JOIN latest_valid_bill_token t
                ON t.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
               AND t.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
               AND t.rn = 1
            JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'N'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSTIMESTAMP, 'FF9')),
                     t.OCSEPCI_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CONFIRM_LATEST_VALID_TOKEN = """
            SELECT
                p.OCSEPCI_ID         AS tokenIdentifier,
                p.OCSEPCI_CUST_CODE  AS customerCode,
                p.OCSEPCI_PREM_CODE  AS premisesCode
            FROM OCSEPCI p
            WHERE p.OCSEPCI_CUST_CODE = ?
              AND p.OCSEPCI_PREM_CODE = ?
              AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
              AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
            ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** Latest OCSEPCI row for an account (pending PPER/token state proxy for TC_111). */
    public static final String SELECT_LATEST_OCSEPCI_FOR_ACCOUNT = """
            SELECT
                p.OCSEPCI_ID              AS tokenIdentifier,
                p.OCSEPCI_BILL_PRES_TYPE  AS pendingBillType,
                p.OCSEPCI_CORR_DEL_TYPE   AS pendingCorrType,
                p.OCSEPCI_EMAIL_ADDR      AS pendingEmail,
                p.OCSEPCI_EMAIL_EXP_DATE  AS tokenExpirationDate,
                p.OCSEPCI_EMAIL_COMP_DATE AS tokenCompletedDate,
                p.OCSEPCI_CONF_STATUS     AS confStatus
            FROM OCSEPCI p
            WHERE p.OCSEPCI_CUST_CODE = ?
              AND p.OCSEPCI_PREM_CODE = ?
            ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /**
     * Active pending bill PPER for a specific account — proves Initiated / In Progress:
     * bill type E, confirmation link not expired, and not completed.
     */
    public static final String SELECT_ACTIVE_PENDING_BILL_PPER_FOR_ACCOUNT = """
            SELECT
                p.OCSEPCI_ID              AS tokenIdentifier,
                p.OCSEPCI_CUST_CODE       AS customerCode,
                p.OCSEPCI_PREM_CODE       AS premisesCode,
                p.OCSEPCI_BILL_PRES_TYPE  AS pendingBillType,
                p.OCSEPCI_CORR_DEL_TYPE   AS pendingCorrType,
                p.OCSEPCI_EMAIL_ADDR      AS pendingEmail,
                p.OCSEPCI_EMAIL_EXP_DATE  AS tokenExpirationDate,
                p.OCSEPCI_EMAIL_COMP_DATE AS tokenCompletedDate,
                p.OCSEPCI_CONF_STATUS     AS confStatus,
                p.OCSEPCI_ACTIVITY_DATE   AS activityDate,
                CASE WHEN p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE THEN 'Y' ELSE 'N' END AS linkNotExpired,
                CASE WHEN p.OCSEPCI_EMAIL_COMP_DATE IS NULL THEN 'Y' ELSE 'N' END AS linkNotCompleted,
                'In Progress'             AS enrollmentStatus
            FROM OCSEPCI p
            WHERE p.OCSEPCI_CUST_CODE = ?
              AND p.OCSEPCI_PREM_CODE = ?
              AND NVL(p.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
              AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
              AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
            ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /**
     * Active pending correspondence PPER for a specific account — proves Initiated / In Progress:
     * corr type E, confirmation link not expired, and not completed.
     */
    public static final String SELECT_ACTIVE_PENDING_CORR_PPER_FOR_ACCOUNT = """
            SELECT
                p.OCSEPCI_ID              AS tokenIdentifier,
                p.OCSEPCI_CUST_CODE       AS customerCode,
                p.OCSEPCI_PREM_CODE       AS premisesCode,
                p.OCSEPCI_BILL_PRES_TYPE  AS pendingBillType,
                p.OCSEPCI_CORR_DEL_TYPE   AS pendingCorrType,
                p.OCSEPCI_EMAIL_ADDR      AS pendingEmail,
                p.OCSEPCI_EMAIL_EXP_DATE  AS tokenExpirationDate,
                p.OCSEPCI_EMAIL_COMP_DATE AS tokenCompletedDate,
                p.OCSEPCI_CONF_STATUS     AS confStatus,
                p.OCSEPCI_ACTIVITY_DATE   AS activityDate,
                CASE WHEN p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE THEN 'Y' ELSE 'N' END AS linkNotExpired,
                CASE WHEN p.OCSEPCI_EMAIL_COMP_DATE IS NULL THEN 'Y' ELSE 'N' END AS linkNotCompleted,
                'In Progress'             AS enrollmentStatus
            FROM OCSEPCI p
            WHERE p.OCSEPCI_CUST_CODE = ?
              AND p.OCSEPCI_PREM_CODE = ?
              AND NVL(p.OCSEPCI_CORR_DEL_TYPE, 'P') = 'E'
              AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
              AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
            ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** Recent OCSEPCI/PPER rows for an account (review evidence: old vs new pending state). */
    public static final String SELECT_RECENT_OCSEPCI_FOR_ACCOUNT = """
            SELECT
                p.OCSEPCI_ID              AS tokenIdentifier,
                p.OCSEPCI_BILL_PRES_TYPE  AS pendingBillType,
                p.OCSEPCI_CORR_DEL_TYPE   AS pendingCorrType,
                p.OCSEPCI_EMAIL_ADDR      AS pendingEmail,
                p.OCSEPCI_EMAIL_EXP_DATE  AS tokenExpirationDate,
                p.OCSEPCI_EMAIL_COMP_DATE AS tokenCompletedDate,
                p.OCSEPCI_CONF_STATUS     AS confStatus,
                p.OCSEPCI_ACTIVITY_DATE   AS activityDate
            FROM OCSEPCI p
            WHERE p.OCSEPCI_CUST_CODE = ?
              AND p.OCSEPCI_PREM_CODE = ?
            ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
            FETCH FIRST %d ROWS ONLY
            """;

    /** Unused OCSEPCI token for account without expiry filter (UAT fallback when SYSDATE comparison misses rows). */
    public static final String SELECT_LATEST_UNUSED_OCSEPCI_TOKEN_FOR_ACCOUNT = """
            SELECT
                p.OCSEPCI_ID         AS tokenIdentifier,
                p.OCSEPCI_CUST_CODE  AS customerCode,
                p.OCSEPCI_PREM_CODE  AS premisesCode
            FROM OCSEPCI p
            WHERE p.OCSEPCI_CUST_CODE = ?
              AND p.OCSEPCI_PREM_CODE = ?
              AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
            ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** Customer-level unused OCSEPCI token fallback when premises-scoped lookup returns no rows. */
    public static final String SELECT_LATEST_UNUSED_OCSEPCI_TOKEN_FOR_CUSTOMER = """
            SELECT
                p.OCSEPCI_ID         AS tokenIdentifier,
                p.OCSEPCI_CUST_CODE  AS customerCode,
                p.OCSEPCI_PREM_CODE  AS premisesCode
            FROM OCSEPCI p
            WHERE p.OCSEPCI_CUST_CODE = ?
              AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
            ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /**
     * TC_124: account whose <em>latest</em> OCSEPCI row is expired and unused (not a stale expired row
     * when a newer valid OCSEPCI exists on the same account).
     */
    public static final String SELECT_CONFIRM_EXPIRED_TOKEN = """
            WITH latest_ocsepci AS (
                SELECT
                    p.OCSEPCI_CUST_CODE,
                    p.OCSEPCI_PREM_CODE,
                    p.OCSEPCI_EMAIL_EXP_DATE,
                    p.OCSEPCI_EMAIL_COMP_DATE,
                    p.OCSEPCI_ACTIVITY_DATE,
                    ROW_NUMBER() OVER (
                        PARTITION BY p.OCSEPCI_CUST_CODE, p.OCSEPCI_PREM_CODE
                        ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
                    ) AS rn
                FROM OCSEPCI p
            )
            SELECT
                l.OCSEPCI_CUST_CODE  AS customerCode,
                l.OCSEPCI_PREM_CODE  AS premisesCode
            FROM latest_ocsepci l
            WHERE l.rn = 1
              AND l.OCSEPCI_EMAIL_COMP_DATE IS NULL
              AND l.OCSEPCI_EMAIL_EXP_DATE <= SYSDATE
            ORDER BY l.OCSEPCI_EMAIL_EXP_DATE ASC
            FETCH FIRST 1 ROWS ONLY
            """;

    /**
     * TC_124: accounts whose <em>latest</em> OCSEPCI is expired and unused (one Oracle query, multiple rows).
     */
    public static final String SELECT_CONFIRM_EXPIRED_TOKEN_ACCOUNTS = """
            WITH latest_ocsepci AS (
                SELECT
                    p.OCSEPCI_CUST_CODE,
                    p.OCSEPCI_PREM_CODE,
                    p.OCSEPCI_EMAIL_EXP_DATE,
                    p.OCSEPCI_EMAIL_COMP_DATE,
                    ROW_NUMBER() OVER (
                        PARTITION BY p.OCSEPCI_CUST_CODE, p.OCSEPCI_PREM_CODE
                        ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
                    ) AS rn
                FROM OCSEPCI p
            )
            SELECT
                l.OCSEPCI_CUST_CODE  AS customerCode,
                l.OCSEPCI_PREM_CODE  AS premisesCode
            FROM latest_ocsepci l
            WHERE l.rn = 1
              AND l.OCSEPCI_EMAIL_COMP_DATE IS NULL
              AND l.OCSEPCI_EMAIL_EXP_DATE <= SYSDATE
            ORDER BY l.OCSEPCI_EMAIL_EXP_DATE ASC
            FETCH FIRST 25 ROWS ONLY
            """;

    /** MariaDB (custadv): latest expired unused token via {@code date_time_link_expired} (UAT1). */
    public static final String SELECT_CUSTADV_EXPIRED_BY_LINK_EXPIRED = """
            SELECT *,
                   SUBSTRING_INDEX(SUBSTRING_INDEX(SUBSTRING_INDEX(email_link, '/', -1), '?', -1), 't=', -1) AS Token
            FROM custadv_email_verification_status
            WHERE email_verification_type = 0
              AND date_time_link_confirmed IS NULL
              AND date_time_link_expired IS NOT NULL
              AND date_time_link_expired < NOW()
            ORDER BY email_verification_status_id DESC
            LIMIT 1
            """;

    /** MariaDB (custadv): recent rows expired by {@code date_time_link_expired} — TC_124 probe source. */
    public static final String SELECT_CUSTADV_RECENT_EXPIRED_BY_LINK_EXPIRED = """
            SELECT *,
                   SUBSTRING_INDEX(SUBSTRING_INDEX(SUBSTRING_INDEX(email_link, '/', -1), '?', -1), 't=', -1) AS Token
            FROM custadv_email_verification_status
            WHERE email_verification_type = 0
              AND date_time_link_confirmed IS NULL
              AND date_time_link_expired IS NOT NULL
              AND date_time_link_expired < NOW()
            ORDER BY email_verification_status_id DESC
            LIMIT ?
            """;

    /** MariaDB (custadv): mark unused token expired via {@code date_time_link_expired} (UAT1 TC_124 bootstrap). */
    public static final String UPDATE_CUSTADV_SET_LINK_EXPIRED_PAST = """
            UPDATE custadv_email_verification_status
            SET date_time_link_expired = DATE_SUB(NOW(), INTERVAL 1 DAY)
            WHERE email_verification_type = 0
              AND account_number = ?
              AND date_time_link_confirmed IS NULL
            """;

    /** MariaDB (custadv): latest expired, not-yet-confirmed paperless token (single-query TC_124 when column exists). */
    public static final String SELECT_CUSTADV_EXPIRED_PAPERLESS_EMAIL_VERIFICATION_TOKEN = """
            SELECT *,
                   SUBSTRING_INDEX(SUBSTRING_INDEX(SUBSTRING_INDEX(email_link, '/', -1), '?', -1), 't=', -1) AS Token
            FROM custadv_email_verification_status
            WHERE email_verification_type = 0
              AND date_time_link_confirmed IS NULL
              AND expired IS NOT NULL
              AND expired < NOW()
            ORDER BY email_verification_status_id DESC
            LIMIT 1
            """;

    public static final String SELECT_CONFIRM_USED_TOKEN = """
            SELECT
                p.OCSEPCI_ID         AS tokenIdentifier,
                p.OCSEPCI_CUST_CODE  AS customerCode,
                p.OCSEPCI_PREM_CODE  AS premisesCode
            FROM OCSEPCI p
            WHERE p.OCSEPCI_EMAIL_COMP_DATE IS NOT NULL
            ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_CONFIRM_EMAIL_MISMATCH_TOKEN = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                p.OCSEPCI_ID         AS tokenIdentifier,
                p.OCSEPCI_CUST_CODE  AS customerCode,
                p.OCSEPCI_PREM_CODE  AS premisesCode
            FROM OCSEPCI p
            JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = p.OCSEPCI_CUST_CODE
               AND e.rn = 1
            WHERE p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
              AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              AND LOWER(TRIM(p.OCSEPCI_EMAIL_ADDR)) <> LOWER(TRIM(e.GZBEMCP_EMAIL_ADDR))
            ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** TC_125: accounts with valid pending OCSEPCI email ≠ current Banner email (passive invalidation). */
    public static final String SELECT_CONFIRM_EMAIL_MISMATCH_ACCOUNTS = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                p.OCSEPCI_CUST_CODE  AS customerCode,
                p.OCSEPCI_PREM_CODE  AS premisesCode
            FROM OCSEPCI p
            JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = p.OCSEPCI_CUST_CODE
               AND e.rn = 1
            WHERE p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
              AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              AND LOWER(TRIM(p.OCSEPCI_EMAIL_ADDR)) <> LOWER(TRIM(e.GZBEMCP_EMAIL_ADDR))
            ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
            FETCH FIRST 25 ROWS ONLY
            """;

    /**
     * TC_125 / TC_117: simulate Banner email change outside UpdatePaperlessCommunications
     * (PPER/OCSEPCI pending email unchanged → passive invalidation on confirm).
     */
    public static final String UPDATE_ACTIVE_BANNER_EMAIL_FOR_CUSTOMER = """
            UPDATE GZBEMCP g
            SET g.GZBEMCP_EMAIL_ADDR = ?
            WHERE g.GZBEMCP_CUST_CODE = ?
              AND g.GZBEMCP_ACCOUNT_IND = 'Y'
              AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            """;

    public static final String SELECT_CONFIRM_TOKEN_ACCOUNT_MISMATCH = """
            SELECT
                p.OCSEPCI_ID         AS tokenIdentifier,
                p.OCSEPCI_CUST_CODE  AS customerCode,
                p.OCSEPCI_PREM_CODE  AS premisesCode
            FROM OCSEPCI p
            WHERE p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
              AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
            ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /**
     * MariaDB (custadv): latest valid unused paperless confirmation token for ConfirmPaperlessEnrollment.
     * email_verification_type = 0 is paperless enrollment; account_number is the custadv composite key.
     * Token from {@code .../confirm?t=&lt;hex&gt;} — last path segment, query value, then strip {@code t=}.
     */
    public static final String SELECT_CUSTADV_LATEST_PAPERLESS_EMAIL_VERIFICATION_TOKEN = """
            SELECT *,
                   SUBSTRING_INDEX(SUBSTRING_INDEX(SUBSTRING_INDEX(email_link, '/', -1), '?', -1), 't=', -1) AS Token
            FROM custadv_email_verification_status
            WHERE email_verification_type = 0
              AND account_number = ?
              AND date_time_link_confirmed IS NULL
            ORDER BY email_verification_status_id DESC
            LIMIT 1
            """;

    /** MariaDB (custadv): baseline max row id before enroll (detect new token row after enroll). */
    public static final String SELECT_CUSTADV_MAX_PAPERLESS_VERIFICATION_ID = """
            SELECT COALESCE(MAX(email_verification_status_id), 0) AS maxVerificationId
            FROM custadv_email_verification_status
            WHERE email_verification_type = 0
              AND account_number = ?
            """;

    /** MariaDB (custadv): unused token row created after a specific verification id. */
    public static final String SELECT_CUSTADV_UNUSED_PAPERLESS_TOKEN_AFTER_ID = """
            SELECT *,
                   SUBSTRING_INDEX(SUBSTRING_INDEX(SUBSTRING_INDEX(email_link, '/', -1), '?', -1), 't=', -1) AS Token
            FROM custadv_email_verification_status
            WHERE email_verification_type = 0
              AND account_number = ?
              AND date_time_link_confirmed IS NULL
              AND email_verification_status_id > ?
            ORDER BY email_verification_status_id DESC
            LIMIT 1
            """;

    /** MariaDB (custadv): recent unused paperless tokens — not used for TC_124 (see per-account query). */
    public static final String SELECT_CUSTADV_RECENT_UNUSED_PAPERLESS_TOKENS = """
            SELECT *,
                   SUBSTRING_INDEX(SUBSTRING_INDEX(SUBSTRING_INDEX(email_link, '/', -1), '?', -1), 't=', -1) AS Token
            FROM custadv_email_verification_status
            WHERE email_verification_type = 0
              AND date_time_link_confirmed IS NULL
            ORDER BY email_verification_status_id DESC
            LIMIT ?
            """;

    /** MariaDB (custadv): latest unused paperless token across all accounts — TC_124 fallback. */
    public static final String SELECT_CUSTADV_LATEST_UNUSED_PAPERLESS_TOKEN_ANY_ACCOUNT = """
            SELECT *,
                   SUBSTRING_INDEX(SUBSTRING_INDEX(SUBSTRING_INDEX(email_link, '/', -1), '?', -1), 't=', -1) AS Token
            FROM custadv_email_verification_status
            WHERE email_verification_type = 0
              AND date_time_link_confirmed IS NULL
            ORDER BY email_verification_status_id DESC
            LIMIT 1
            """;

    /** MariaDB (custadv): latest already-confirmed (used) paperless token. */
    public static final String SELECT_CUSTADV_USED_PAPERLESS_EMAIL_VERIFICATION_TOKEN = """
            SELECT *,
                   SUBSTRING_INDEX(SUBSTRING_INDEX(SUBSTRING_INDEX(email_link, '/', -1), '?', -1), 't=', -1) AS Token
            FROM custadv_email_verification_status
            WHERE email_verification_type = 0
              AND date_time_link_confirmed IS NOT NULL
            ORDER BY email_verification_status_id DESC
            LIMIT 1
            """;

    /** MariaDB (custadv): latest paperless token row for an account (any confirmation state). */
    public static final String SELECT_CUSTADV_ANY_LATEST_PAPERLESS_TOKEN_FOR_ACCOUNT = """
            SELECT *,
                   SUBSTRING_INDEX(SUBSTRING_INDEX(SUBSTRING_INDEX(email_link, '/', -1), '?', -1), 't=', -1) AS Token
            FROM custadv_email_verification_status
            WHERE email_verification_type = 0
              AND account_number = ?
            ORDER BY email_verification_status_id DESC
            LIMIT 1
            """;

    /**
     * MariaDB (custadv): In Progress bill confirmation link for an account —
     * unused (not completed) and not expired. Report evidence for GetAccountInfo Initiated (I).
     */
    public static final String SELECT_CUSTADV_IN_PROGRESS_BILL_TOKEN_FOR_ACCOUNT = """
            SELECT
                email_verification_status_id,
                account_number,
                bill_delivery_type,
                correspondence_delivery_type,
                email_sent_to,
                date_time_link_confirmed,
                date_time_link_expired,
                verification_status,
                CASE WHEN date_time_link_confirmed IS NULL THEN 'Y' ELSE 'N' END AS linkNotCompleted,
                CASE WHEN date_time_link_expired IS NULL OR date_time_link_expired > NOW() THEN 'Y' ELSE 'N' END AS linkNotExpired,
                'In Progress' AS enrollmentStatus,
                SUBSTRING_INDEX(SUBSTRING_INDEX(SUBSTRING_INDEX(email_link, '/', -1), '?', -1), 't=', -1) AS Token
            FROM custadv_email_verification_status
            WHERE email_verification_type = 0
              AND account_number = ?
              AND date_time_link_confirmed IS NULL
              AND (date_time_link_expired IS NULL OR date_time_link_expired > NOW())
              AND (
                    UPPER(IFNULL(bill_delivery_type, '')) IN ('E', 'ELECTRONIC')
                 OR bill_delivery_type IS NULL
              )
            ORDER BY
                CASE WHEN UPPER(IFNULL(bill_delivery_type, '')) IN ('E', 'ELECTRONIC') THEN 0 ELSE 1 END,
                email_verification_status_id DESC
            LIMIT 1
            """;

    /**
     * MariaDB (custadv): In Progress correspondence confirmation link for an account —
     * unused (not completed) and not expired. Report evidence for GetAccountInfo Initiated (I).
     */
    public static final String SELECT_CUSTADV_IN_PROGRESS_CORR_TOKEN_FOR_ACCOUNT = """
            SELECT
                email_verification_status_id,
                account_number,
                bill_delivery_type,
                correspondence_delivery_type,
                email_sent_to,
                date_time_link_confirmed,
                date_time_link_expired,
                verification_status,
                CASE WHEN date_time_link_confirmed IS NULL THEN 'Y' ELSE 'N' END AS linkNotCompleted,
                CASE WHEN date_time_link_expired IS NULL OR date_time_link_expired > NOW() THEN 'Y' ELSE 'N' END AS linkNotExpired,
                'In Progress' AS enrollmentStatus,
                SUBSTRING_INDEX(SUBSTRING_INDEX(SUBSTRING_INDEX(email_link, '/', -1), '?', -1), 't=', -1) AS Token
            FROM custadv_email_verification_status
            WHERE email_verification_type = 0
              AND account_number = ?
              AND date_time_link_confirmed IS NULL
              AND (date_time_link_expired IS NULL OR date_time_link_expired > NOW())
              AND (
                    UPPER(IFNULL(correspondence_delivery_type, '')) IN ('E', 'ELECTRONIC')
                 OR correspondence_delivery_type IS NULL
              )
            ORDER BY
                CASE WHEN UPPER(IFNULL(correspondence_delivery_type, '')) IN ('E', 'ELECTRONIC') THEN 0 ELSE 1 END,
                email_verification_status_id DESC
            LIMIT 1
            """;

    /**
     * MariaDB (custadv): any In Progress confirmation link for an account —
     * unused (not completed) and not expired. Fallback evidence when channel type columns are unset.
     */
    public static final String SELECT_CUSTADV_IN_PROGRESS_TOKEN_FOR_ACCOUNT = """
            SELECT
                email_verification_status_id,
                account_number,
                bill_delivery_type,
                correspondence_delivery_type,
                email_sent_to,
                date_time_link_confirmed,
                date_time_link_expired,
                verification_status,
                CASE WHEN date_time_link_confirmed IS NULL THEN 'Y' ELSE 'N' END AS linkNotCompleted,
                CASE WHEN date_time_link_expired IS NULL OR date_time_link_expired > NOW() THEN 'Y' ELSE 'N' END AS linkNotExpired,
                'In Progress' AS enrollmentStatus,
                SUBSTRING_INDEX(SUBSTRING_INDEX(SUBSTRING_INDEX(email_link, '/', -1), '?', -1), 't=', -1) AS Token
            FROM custadv_email_verification_status
            WHERE email_verification_type = 0
              AND account_number = ?
              AND date_time_link_confirmed IS NULL
              AND (date_time_link_expired IS NULL OR date_time_link_expired > NOW())
            ORDER BY email_verification_status_id DESC
            LIMIT 1
            """;

    /** TC_105: ACTIVE, bill paper, Banner email, expired or used token, no valid unused token. */
    public static final String SELECT_TC_105_ACTIVE_EXPIRED_OR_USED_TOKEN = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE AS customerCode,
                a.UCRACCT_PREM_CODE AS premisesCode,
                e.GZBEMCP_EMAIL_ADDR AS bannerEmail
            FROM UCRACCT a
            JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
              AND EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND (
                        (p.OCSEPCI_EMAIL_COMP_DATE IS NULL AND p.OCSEPCI_EMAIL_EXP_DATE <= SYSDATE)
                        OR p.OCSEPCI_EMAIL_COMP_DATE IS NOT NULL
                    )
              )
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSTIMESTAMP, 'FF9')),
                     a.UCRACCT_ACTIVITY_DATE ASC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** TC_106: NEW, bill paper, Banner email, expired or used token, no valid unused token. */
    public static final String SELECT_TC_106_NEW_EXPIRED_OR_USED_TOKEN = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE AS customerCode,
                a.UCRACCT_PREM_CODE AS premisesCode,
                e.GZBEMCP_EMAIL_ADDR AS bannerEmail
            FROM UCRACCT a
            JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'N'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
              AND EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND (
                        (p.OCSEPCI_EMAIL_COMP_DATE IS NULL AND p.OCSEPCI_EMAIL_EXP_DATE <= SYSDATE)
                        OR p.OCSEPCI_EMAIL_COMP_DATE IS NOT NULL
                    )
              )
            ORDER BY ORA_HASH(a.UCRACCT_CUST_CODE || a.UCRACCT_PREM_CODE || TO_CHAR(SYSTIMESTAMP, 'FF9')),
                     a.UCRACCT_ACTIVITY_DATE ASC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** TC_116: ACTIVE account with pending token whose OCSEPCI email differs from current Banner email. */
    public static final String SELECT_TC_116_ACTIVE_EMAIL_MISMATCH_PENDING = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE AS customerCode,
                a.UCRACCT_PREM_CODE AS premisesCode,
                e.GZBEMCP_EMAIL_ADDR AS bannerEmail,
                p.OCSEPCI_ID AS tokenIdentifier
            FROM OCSEPCI p
            JOIN UCRACCT a
                ON a.UCRACCT_CUST_CODE = p.OCSEPCI_CUST_CODE
               AND a.UCRACCT_PREM_CODE = p.OCSEPCI_PREM_CODE
            JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
              AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              AND LOWER(TRIM(p.OCSEPCI_EMAIL_ADDR)) <> LOWER(TRIM(e.GZBEMCP_EMAIL_ADDR))
            ORDER BY p.OCSEPCI_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String UPDATE_OCSEPCI_EXPIRE_VALID_TOKENS_FOR_ACCOUNT = """
            UPDATE OCSEPCI p
            SET p.OCSEPCI_EMAIL_EXP_DATE = SYSDATE - 1
            WHERE p.OCSEPCI_CUST_CODE = ?
              AND p.OCSEPCI_PREM_CODE = ?
              AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
            """;

    /**
     * TC_130: remove active PendingConfirmation PPER rows so Confirm finds no pending PPER (→ 10411)
     * while the unused custadv token row remains.
     */
    public static final String DELETE_OCSEPCI_PENDING_ROWS_FOR_ACCOUNT = """
            DELETE FROM OCSEPCI p
            WHERE p.OCSEPCI_CUST_CODE = ?
              AND p.OCSEPCI_PREM_CODE = ?
              AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
            """;

    /**
     * TC_125: delete the custadv token row so the issued token identifier no longer matches any
     * stored token record (→ 10411 Token Not Found).
     */
    public static final String DELETE_CUSTADV_PAPERLESS_TOKEN_BY_ID = """
            DELETE FROM custadv_email_verification_status
            WHERE email_verification_status_id = ?
              AND email_verification_type = 0
            """;

    /**
     * TC_125: delete every unused paperless custadv row for the account (token identifier gone).
     */
    public static final String DELETE_CUSTADV_UNUSED_PAPERLESS_TOKENS_FOR_ACCOUNT = """
            DELETE FROM custadv_email_verification_status
            WHERE email_verification_type = 0
              AND account_number = ?
              AND date_time_link_confirmed IS NULL
            """;

    /**
     * TC_125: delete the unused custadv row whose email_link still contains the issued token hex.
     */
    public static final String DELETE_CUSTADV_UNUSED_PAPERLESS_TOKEN_BY_TOKEN = """
            DELETE FROM custadv_email_verification_status
            WHERE email_verification_type = 0
              AND date_time_link_confirmed IS NULL
              AND email_link LIKE CONCAT('%', ?, '%')
            """;

    /**
     * TC_130: clear pending channel prefs on the unused custadv token row while keeping the token
     * identifier (UAT1 often has no visible OCSEPCI/PPER row).
     */
    public static final String UPDATE_CUSTADV_CLEAR_PENDING_CHANNELS_FOR_ID = """
            UPDATE custadv_email_verification_status
            SET bill_delivery_type = NULL,
                correspondence_delivery_type = NULL
            WHERE email_verification_status_id = ?
              AND email_verification_type = 0
              AND date_time_link_confirmed IS NULL
            """;

    /**
     * TC_130: revive a used custadv token after Confirm archived PPER — clear confirmed timestamp so
     * the token is unused again while no active PendingConfirmation PPER remains (→ 10411).
     */
    public static final String UPDATE_CUSTADV_CLEAR_LINK_CONFIRMED_FOR_ID = """
            UPDATE custadv_email_verification_status
            SET date_time_link_confirmed = NULL
            WHERE email_verification_status_id = ?
              AND email_verification_type = 0
            """;

    /**
     * TC_130: keep the token row/email_link, but remove paperless enrollment type so Confirm no longer
     * finds an active PendingConfirmation PPER for that token (→ 10411).
     */
    public static final String UPDATE_CUSTADV_CLEAR_PAPERLESS_TYPE_FOR_ID = """
            UPDATE custadv_email_verification_status
            SET email_verification_type = 1
            WHERE email_verification_status_id = ?
              AND email_verification_type = 0
              AND date_time_link_confirmed IS NULL
            """;

    /**
     * TC_125: rewrite email_link so the previously issued token identifier no longer matches any
     * stored custadv token record (→ 10411 Token Not Found).
     */
    public static final String UPDATE_CUSTADV_SCRAMBLE_EMAIL_LINK_FOR_ID = """
            UPDATE custadv_email_verification_status
            SET email_link = CONCAT(
                    'confirm?t=',
                    REPLACE(UUID(), '-', ''),
                    REPLACE(UUID(), '-', ''),
                    REPLACE(UUID(), '-', ''),
                    REPLACE(UUID(), '-', ''))
            WHERE email_verification_status_id = ?
              AND email_verification_type = 0
            """;

    public static final String SELECT_FISERV_BILL_ACCOUNT = """
            SELECT
                a.UCRACCT_CUST_CODE              AS customerCode,
                a.UCRACCT_PREM_CODE              AS premisesCode,
                a.UCRACCT_STATUS_IND             AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE         AS currentBillDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE          AS currentCorrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR             AS bannerEmail
            FROM UCRACCT a
            INNER JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
            WHERE a.UCRACCT_BILL_PRES_TYPE = 'F'
              AND a.UCRACCT_STATUS_IND IN ('A', 'F', 'I')
              AND e.GZBEMCP_ACCOUNT_IND = 'Y'
              AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
              AND (e.GZBEMCP_EXPIRATION_DATE IS NULL OR e.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_NOT_ENROLLED_IN_PAPERLESS = """
            SELECT
                a.UCRACCT_CUST_CODE              AS customerCode,
                a.UCRACCT_PREM_CODE              AS premisesCode,
                a.UCRACCT_STATUS_IND             AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE         AS currentBillDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE          AS currentCorrDeliveryOption
            FROM UCRACCT a
            WHERE NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NVL(a.UCRACCT_CORR_DEL_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** TC_73: bill ineligible (NULL bill pref), corr already enrolled (E), active Banner email. */
    public static final String SELECT_ONE_CHANNEL_INELIGIBLE = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail,
                'BILL'                   AS ineligibleChannel
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND a.UCRACCT_BILL_PRES_TYPE IS NULL
              AND a.UCRACCT_CORR_DEL_TYPE = 'E'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** TC_73 fallback: bill channel ineligible (NULL bill pref), corr eligible, active Banner email. */
    public static final String SELECT_ONE_CHANNEL_INELIGIBLE_BILL = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail,
                'BILL'                   AS ineligibleChannel
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND a.UCRACCT_BILL_PRES_TYPE IS NULL
              AND NVL(a.UCRACCT_CORR_DEL_TYPE, 'P') IN ('P', 'E')
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** ACTIVE account with no active email on file in GZBEMCP. */
    public static final String SELECT_ACTIVE_ACCOUNT_NO_BANNER_EMAIL = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption
            FROM UCRACCT a
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NOT EXISTS (
                  SELECT 1
                  FROM GZBEMCP e
                  WHERE e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND e.GZBEMCP_ACCOUNT_IND = 'Y'
                    AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
                    AND (e.GZBEMCP_EXPIRATION_DATE IS NULL OR e.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** ACTIVE account where Banner correspondence preference is NULL. */
    public static final String SELECT_ACTIVE_ACCOUNT_NULL_CORR_PREFERENCE = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption
            FROM UCRACCT a
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND a.UCRACCT_CORR_DEL_TYPE IS NULL
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** ACTIVE account where Banner bill preference is NULL. */
    public static final String SELECT_ACTIVE_ACCOUNT_NULL_BILL_PREFERENCE = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption
            FROM UCRACCT a
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND a.UCRACCT_BILL_PRES_TYPE IS NULL
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** ACTIVE account enrolled on Fiserv eBill (bill preference = F). */
    public static final String SELECT_ACTIVE_ACCOUNT_FISERV_BILL = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND a.UCRACCT_BILL_PRES_TYPE = 'F'
              AND e.GZBEMCP_ACCOUNT_IND = 'Y'
              AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
              AND (e.GZBEMCP_EXPIRATION_DATE IS NULL OR e.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** ACTIVE account with valid unused pending correspondence enrollment (active PPER). */
    public static final String SELECT_ACTIVE_ACCOUNT_PENDING_CORR_ENROLLMENT = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption
            FROM UCRACCT a
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_CORR_DEL_TYPE, 'P') = 'E'
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** ACTIVE account with valid unused pending bill enrollment (active PPER). */
    public static final String SELECT_ACTIVE_ACCOUNT_PENDING_BILL_ENROLLMENT = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption
            FROM UCRACCT a
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** ACTIVE account enrolled in correspondence with active pending PPER override. */
    public static final String SELECT_ACTIVE_ACCOUNT_PENDING_CORR_WITH_BANNER_CORR_ENROLLED = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption
            FROM UCRACCT a
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND a.UCRACCT_CORR_DEL_TYPE = 'E'
              AND EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_CORR_DEL_TYPE, 'P') = 'E'
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** ACTIVE Fiserv bill account with active pending bill PPER override. */
    public static final String SELECT_ACTIVE_ACCOUNT_PENDING_BILL_WITH_FISERV_BILL = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND a.UCRACCT_BILL_PRES_TYPE = 'F'
              AND e.GZBEMCP_ACCOUNT_IND = 'Y'
              AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
              AND (e.GZBEMCP_EXPIRATION_DATE IS NULL OR e.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
              AND EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** ACTIVE account with no Banner email and active pending bill PPER. */
    public static final String SELECT_ACTIVE_ACCOUNT_NO_EMAIL_PENDING_BILL_ENROLLMENT = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption
            FROM UCRACCT a
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NOT EXISTS (
                  SELECT 1
                  FROM GZBEMCP e
                  WHERE e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND e.GZBEMCP_ACCOUNT_IND = 'Y'
                    AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
                    AND (e.GZBEMCP_EXPIRATION_DATE IS NULL OR e.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
              )
              AND EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** NEW account where Banner bill preference is NULL (TC_130 confirm preference update failure). */
    public static final String SELECT_NEW_ACCOUNT_NULL_BILL_PREFERENCE = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
            WHERE a.UCRACCT_STATUS_IND = 'N'
              AND a.UCRACCT_BILL_PRES_TYPE IS NULL
              AND e.GZBEMCP_ACCOUNT_IND = 'Y'
              AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
              AND (e.GZBEMCP_EXPIRATION_DATE IS NULL OR e.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** NEW account where Banner bill preference is explicitly paper ('P') on GZRPPTH (not NULL default). */
    public static final String SELECT_NEW_ACCOUNT_PAPER_BILL_PREFERENCE = """
            SELECT
                g.GZRPPTH_CUST_CODE              AS customerCode,
                g.GZRPPTH_PREM_CODE              AS premisesCode,
                g.GZRPPTH_STATUS_CODE            AS accountStatus,
                g.GZRPPTH_BILL_PRES_TYPE         AS billDeliveryOption,
                g.GZRPPTH_BILL_PRES_TYPE         AS bannerBillPreference,
                g.GZRPPTH_CORR_DEL_TYPE          AS corrDeliveryOption,
                e.GZBEMCP_EMAIL_ADDR             AS bannerEmail
            FROM GZRPPTH g
            LEFT JOIN UCRACCT a
                ON a.UCRACCT_CUST_CODE = g.GZRPPTH_CUST_CODE
               AND a.UCRACCT_PREM_CODE = g.GZRPPTH_PREM_CODE
            LEFT JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = g.GZRPPTH_CUST_CODE
               AND e.GZBEMCP_ACCOUNT_IND = 'Y'
               AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
               AND (e.GZBEMCP_EXPIRATION_DATE IS NULL OR e.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            WHERE g.GZRPPTH_STATUS_CODE = 'N'
              AND g.GZRPPTH_BILL_PRES_TYPE = 'P'
              AND a.UCRACCT_CUST_CODE IS NULL
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = g.GZRPPTH_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = g.GZRPPTH_PREM_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY g.GZRPPTH_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** ACTIVE accounts with valid unused pending bill enrollment (for TC_129 probing). */
    public static final String SELECT_LIST_ACTIVE_ACCOUNTS_PENDING_BILL_ENROLLMENT = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption
            FROM UCRACCT a
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 25 ROWS ONLY
            """;

    /** NEW accounts with valid unused pending bill enrollment (for TC_130 probing). */
    public static final String SELECT_LIST_NEW_ACCOUNTS_PENDING_BILL_ENROLLMENT = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption
            FROM UCRACCT a
            WHERE a.UCRACCT_STATUS_IND = 'N'
              AND EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 25 ROWS ONLY
            """;

    public static final String UPDATE_UCRACCT_BILL_PRES_TYPE_FOR_ACCOUNT = """
            UPDATE UCRACCT
            SET UCRACCT_BILL_PRES_TYPE = ?
            WHERE UCRACCT_CUST_CODE = ?
              AND UCRACCT_PREM_CODE = ?
            """;

    public static final String UPDATE_UCRACCT_CORR_DEL_TYPE_FOR_ACCOUNT = """
            UPDATE UCRACCT
            SET UCRACCT_CORR_DEL_TYPE = ?
            WHERE UCRACCT_CUST_CODE = ?
              AND UCRACCT_PREM_CODE = ?
            """;

    public static final String UPDATE_EXPIRE_ACTIVE_BANNER_EMAIL_FOR_CUSTOMER = """
            UPDATE GZBEMCP g
            SET g.GZBEMCP_EXPIRATION_DATE = TRUNC(SYSDATE) - 1
            WHERE g.GZBEMCP_CUST_CODE = ?
              AND g.GZBEMCP_ACCOUNT_IND = 'Y'
              AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            """;

    /**
     * TC_149 cleanup: restore Banner emails expired by
     * {@link #UPDATE_EXPIRE_ACTIVE_BANNER_EMAIL_FOR_CUSTOMER} in the same test.
     * Column is NOT NULL — use a far-future date instead of NULL.
     */
    public static final String UPDATE_UNEXPIRE_RECENTLY_EXPIRED_BANNER_EMAIL_FOR_CUSTOMER = """
            UPDATE GZBEMCP g
            SET g.GZBEMCP_EXPIRATION_DATE = DATE '2099-12-31'
            WHERE g.GZBEMCP_CUST_CODE = ?
              AND g.GZBEMCP_ACCOUNT_IND = 'Y'
              AND g.GZBEMCP_EXPIRATION_DATE = TRUNC(SYSDATE) - 1
            """;

    /**
     * TC_147: transition Banner account status (e.g. NEW {@code N} → ACTIVE {@code A})
     * after enrollment token is created and before ConfirmPaperlessEnrollment.
     */
    public static final String UPDATE_UCRACCT_STATUS_IND_FOR_ACCOUNT = """
            UPDATE UCRACCT
            SET UCRACCT_STATUS_IND = ?
            WHERE UCRACCT_CUST_CODE = ?
              AND UCRACCT_PREM_CODE = ?
            """;

    public static final String SELECT_UCRACCT_ACCOUNT_SUMMARY = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption
            FROM UCRACCT a
            WHERE a.UCRACCT_CUST_CODE = ?
              AND a.UCRACCT_PREM_CODE = ?
            """;

    /** MariaDB (custadv): remap token row account_number for account-mismatch negative setup. */
    public static final String UPDATE_CUSTADV_PAPERLESS_TOKEN_ACCOUNT_NUMBER = """
            UPDATE custadv_email_verification_status
            SET account_number = ?
            WHERE email_verification_status_id = ?
              AND email_verification_type = 0
            """;

    /** MariaDB (custadv): expire unused paperless tokens for an account (TC_105/106 bootstrap). */
    public static final String UPDATE_CUSTADV_EXPIRE_UNUSED_PAPERLESS_TOKENS_FOR_ACCOUNT = """
            UPDATE custadv_email_verification_status
            SET expired = DATE_SUB(NOW(), INTERVAL 1 DAY)
            WHERE email_verification_type = 0
              AND account_number = ?
              AND date_time_link_confirmed IS NULL
              AND (expired IS NULL OR expired >= NOW())
            """;

    /** NEW account with unconfirmed electronic bill preference (pending enrollment). */
    public static final String SELECT_NEW_ACCOUNT_UNCONFIRMED_BILL_PREFERENCE = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption
            FROM UCRACCT a
            WHERE a.UCRACCT_STATUS_IND = 'N'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /**
     * NEW account with unconfirmed electronic bill preference and no active PPER row.
     * Preference E is present on an expired/unused OCSEPCI row (confirmation date not populated).
     */
    public static final String SELECT_NEW_ACCOUNT_EXPIRED_UNCONFIRMED_BILL_PREFERENCE = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption
            FROM UCRACCT a
            WHERE a.UCRACCT_STATUS_IND = 'N'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
              AND EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
                    AND p.OCSEPCI_EMAIL_EXP_DATE <= SYSDATE
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** NEW accounts with expired unconfirmed bill preference and no active PPER (TC_201 probing). */
    public static final String SELECT_LIST_NEW_ACCOUNTS_EXPIRED_UNCONFIRMED_BILL = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption
            FROM UCRACCT a
            WHERE a.UCRACCT_STATUS_IND = 'N'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
              AND EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
                    AND p.OCSEPCI_EMAIL_EXP_DATE <= SYSDATE
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 25 ROWS ONLY
            """;

    /** NEW account with confirmed electronic bill preference. */
    public static final String SELECT_NEW_ACCOUNT_CONFIRMED_BILL_PREFERENCE = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE  AS corrDeliveryOption
            FROM UCRACCT a
            WHERE a.UCRACCT_STATUS_IND = 'N'
              AND a.UCRACCT_BILL_PRES_TYPE = 'E'
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** VerifyAccount: account with active UCRADDR street fields + Banner email for email verify. */
    public static final String SELECT_VERIFY_ACCOUNT_WITH_UCRADDR_STREET = """
            SELECT
                a.UCRACCT_CUST_CODE           AS customerCode,
                a.UCRACCT_PREM_CODE           AS premisesCode,
                a.UCRACCT_STATUS_IND          AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE      AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE       AS corrDeliveryOption,
                addr.UCRADDR_STREET_NUMBER    AS billingStreetNumber,
                addr.UCRADDR_PDIR_CODE_PRE    AS billingStreetPreDirection,
                addr.UCRADDR_STREET_NAME      AS billingStreetName,
                addr.UCRADDR_SSFX_CODE        AS billingStreetSuffix,
                addr.UCRADDR_PDIR_CODE_POST   AS billingStreetPostDirection,
                addr.UCRADDR_UTYP_CODE        AS billingUnitType,
                addr.UCRADDR_UNIT             AS billingUnitNumber,
                addr.UCRADDR_CITY             AS billingCity,
                addr.UCRADDR_STAT_CODE        AS billingStateCode,
                addr.UCRADDR_STAT_CODE        AS billingState,
                addr.UCRADDR_ZIP              AS billingZipCode,
                addr.UCRADDR_ZIP              AS billingZip,
                addr.UCRADDR_STREET_LINE2     AS billingPoBox,
                e.GZBEMCP_EMAIL_ADDR          AS bannerEmail
            FROM UCRACCT a
            INNER JOIN UCRADDR addr
                ON addr.UCRADDR_CUST_CODE = a.UCRACCT_CUST_CODE
               AND addr.UCRADDR_STATUS_IND = 'A'
            INNER JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.GZBEMCP_ACCOUNT_IND = 'Y'
               AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
               -- Match SPK_ACCT_SEARCH_UTIL.SP_VERIFY_ACCOUNT email window (avoids future EFFECTIVE_DATE → 10073)
               AND SYSDATE BETWEEN e.GZBEMCP_EFFECTIVE_DATE AND e.GZBEMCP_EXPIRATION_DATE
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND addr.UCRADDR_STREET_NUMBER IS NOT NULL
              AND addr.UCRADDR_PDIR_CODE_PRE IS NOT NULL
              AND addr.UCRADDR_STREET_NAME IS NOT NULL
              AND addr.UCRADDR_CITY IS NOT NULL
              AND addr.UCRADDR_STAT_CODE IS NOT NULL
              AND addr.UCRADDR_ZIP IS NOT NULL
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** Multiple street+email candidates for Swagger VerifyAccount UCRADDR billing TCs. */
    public static final String SELECT_VERIFY_ACCOUNT_WITH_UCRADDR_STREET_CANDIDATES = """
            SELECT
                a.UCRACCT_CUST_CODE           AS customerCode,
                a.UCRACCT_PREM_CODE           AS premisesCode,
                a.UCRACCT_STATUS_IND          AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE      AS billDeliveryOption,
                a.UCRACCT_CORR_DEL_TYPE       AS corrDeliveryOption,
                a.UCRACCT_BILL_PRES_TYPE      AS billPresType,
                a.UCRACCT_CORR_DEL_TYPE       AS correspondencePreference,
                addr.UCRADDR_STREET_NUMBER    AS billingStreetNumber,
                addr.UCRADDR_PDIR_CODE_PRE    AS billingStreetPreDirection,
                addr.UCRADDR_STREET_NAME      AS billingStreetName,
                addr.UCRADDR_SSFX_CODE        AS billingStreetSuffix,
                addr.UCRADDR_PDIR_CODE_POST   AS billingStreetPostDirection,
                addr.UCRADDR_UTYP_CODE        AS billingUnitType,
                addr.UCRADDR_UNIT             AS billingUnitNumber,
                addr.UCRADDR_CITY             AS billingCity,
                addr.UCRADDR_STAT_CODE        AS billingStateCode,
                addr.UCRADDR_STAT_CODE        AS billingState,
                addr.UCRADDR_ZIP              AS billingZipCode,
                addr.UCRADDR_ZIP              AS billingZip,
                addr.UCRADDR_STREET_LINE2     AS billingPoBox,
                e.GZBEMCP_EMAIL_ADDR          AS bannerEmail
            FROM UCRACCT a
            INNER JOIN UCRADDR addr
                ON addr.UCRADDR_CUST_CODE = a.UCRACCT_CUST_CODE
               AND addr.UCRADDR_STATUS_IND = 'A'
            INNER JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.GZBEMCP_ACCOUNT_IND = 'Y'
               AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
               AND SYSDATE BETWEEN e.GZBEMCP_EFFECTIVE_DATE AND e.GZBEMCP_EXPIRATION_DATE
            WHERE a.UCRACCT_STATUS_IND IN ('A', 'N', 'P')
              AND addr.UCRADDR_STREET_NUMBER IS NOT NULL
              AND addr.UCRADDR_STREET_NAME IS NOT NULL
              AND addr.UCRADDR_CITY IS NOT NULL
              AND addr.UCRADDR_STAT_CODE IS NOT NULL
              AND addr.UCRADDR_ZIP IS NOT NULL
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 40 ROWS ONLY
            """;

    /** VerifyAccount: account with PO BOX on active UCRADDR + Banner email. */
    public static final String SELECT_VERIFY_ACCOUNT_WITH_UCRADDR_PO_BOX = """
            SELECT
                a.UCRACCT_CUST_CODE           AS customerCode,
                a.UCRACCT_PREM_CODE           AS premisesCode,
                a.UCRACCT_STATUS_IND          AS accountStatus,
                addr.UCRADDR_STREET_LINE2     AS billingPoBox,
                addr.UCRADDR_CITY             AS billingCity,
                addr.UCRADDR_STAT_CODE        AS billingStateCode,
                addr.UCRADDR_STAT_CODE        AS billingState,
                addr.UCRADDR_ZIP              AS billingZipCode,
                addr.UCRADDR_ZIP              AS billingZip,
                e.GZBEMCP_EMAIL_ADDR          AS bannerEmail
            FROM UCRACCT a
            INNER JOIN UCRADDR addr
                ON addr.UCRADDR_CUST_CODE = a.UCRACCT_CUST_CODE
               AND addr.UCRADDR_STATUS_IND = 'A'
            INNER JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.GZBEMCP_ACCOUNT_IND = 'Y'
               AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
               AND SYSDATE BETWEEN e.GZBEMCP_EFFECTIVE_DATE AND e.GZBEMCP_EXPIRATION_DATE
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND UPPER(TRIM(addr.UCRADDR_STREET_LINE2)) LIKE 'PO BOX%'
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /**
     * TC_228/229 — NEW GTBENRL with bill address, active GZRPPTH, no UCRACCT,
     * and billing vs service address differ (Preferences VerifyAccount GTBENRL source).
     */
    public static final String SELECT_VERIFY_ACCOUNT_GTBENRL_NO_UCRADDR = """
            SELECT
                b.GTBENRL_CUST_CODE              AS customerCode,
                b.GTBENRL_PREM_CODE              AS premisesCode,
                b.GTBENRL_EMAIL                  AS bannerEmail,
                b.GTBENRL_ENRO_STATUS            AS enroStatus,
                b.GTBENRL_PROC_FLAG              AS procFlag,
                g.GZRPPTH_STATUS_CODE            AS accountStatus,
                b.GTBENRL_BILL_ADDR1             AS billAddr1,
                b.GTBENRL_BILL_CITY              AS billingCity,
                b.GTBENRL_BILL_STATE             AS billingState,
                b.GTBENRL_BILL_ZIP               AS billingZip,
                b.GTBENRL_SERV_ADDR              AS servAddr,
                b.GTBENRL_SERV_CITY              AS servCity,
                b.GTBENRL_SERV_STATE             AS servState,
                b.GTBENRL_SERV_ZIP               AS servZip
            FROM GTBENRL b
            INNER JOIN GZRPPTH g
                ON g.GZRPPTH_CUST_CODE = b.GTBENRL_CUST_CODE
               AND g.GZRPPTH_PREM_CODE = b.GTBENRL_PREM_CODE
            LEFT JOIN UCRACCT u
                ON u.UCRACCT_CUST_CODE = b.GTBENRL_CUST_CODE
               AND u.UCRACCT_PREM_CODE = b.GTBENRL_PREM_CODE
            WHERE b.GTBENRL_PROC_FLAG = 'N'
              AND b.GTBENRL_BILL_ADDR1 IS NOT NULL
              AND g.GZRPPTH_STATUS_CODE = 'N'
              AND u.UCRACCT_CUST_CODE IS NULL
              AND (
                    NVL(b.GTBENRL_BILL_CITY, ' ') <> NVL(b.GTBENRL_SERV_CITY, ' ')
                 OR NVL(b.GTBENRL_BILL_STATE, ' ') <> NVL(b.GTBENRL_SERV_STATE, ' ')
                 OR NVL(b.GTBENRL_BILL_ZIP, ' ') <> NVL(b.GTBENRL_SERV_ZIP, ' ')
              )
            FETCH FIRST 5 ROWS ONLY
            """;

    /**
     * Banner evidence snapshot for Preferences VerifyAccount review:
     * UCRACCT prefs + active UCRADDR billing address + premises street + OCSEPCI confirm dates.
     */
    public static final String SELECT_VERIFY_ACCOUNT_BANNER_EVIDENCE = """
            SELECT
                a.UCRACCT_CUST_CODE           AS customerCode,
                a.UCRACCT_PREM_CODE           AS premisesCode,
                a.UCRACCT_STATUS_IND          AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE      AS billPresType,
                a.UCRACCT_CORR_DEL_TYPE       AS correspondencePreference,
                addr.UCRADDR_STREET_NUMBER    AS billingStreetNumber,
                addr.UCRADDR_PDIR_CODE_PRE    AS billingStreetPreDirection,
                addr.UCRADDR_STREET_NAME      AS billingStreetName,
                addr.UCRADDR_SSFX_CODE        AS billingStreetSuffix,
                addr.UCRADDR_PDIR_CODE_POST   AS billingStreetPostDirection,
                addr.UCRADDR_UTYP_CODE        AS billingUnitType,
                addr.UCRADDR_UNIT             AS billingUnitNumber,
                addr.UCRADDR_CITY             AS billingCity,
                addr.UCRADDR_STAT_CODE        AS billingStateCode,
                addr.UCRADDR_STAT_CODE        AS billingState,
                addr.UCRADDR_ZIP              AS billingZipCode,
                addr.UCRADDR_ZIP              AS billingZip,
                addr.UCRADDR_STREET_LINE2     AS billingPoBox,
                p.UCBPREM_STREET_NUMBER       AS premisesStreetNumber,
                p.UCBPREM_CITY                AS premisesCity,
                p.UCBPREM_STAT_CODE_ADDR      AS premisesStateCode,
                p.UCBPREM_ZIPC_CODE           AS premisesZipCode,
                TO_CHAR(
                    (SELECT MAX(oc.OCSEPCI_EMAIL_COMP_DATE)
                     FROM OCSEPCI oc
                     WHERE oc.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                       AND oc.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                       AND NVL(oc.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
                       AND oc.OCSEPCI_EMAIL_COMP_DATE IS NOT NULL),
                    'YYYYMMDD')               AS billDeliveryConfirmDate,
                TO_CHAR(
                    (SELECT MAX(oc.OCSEPCI_EMAIL_COMP_DATE)
                     FROM OCSEPCI oc
                     WHERE oc.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                       AND oc.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                       AND NVL(oc.OCSEPCI_CORR_DEL_TYPE, 'P') = 'E'
                       AND oc.OCSEPCI_EMAIL_COMP_DATE IS NOT NULL),
                    'YYYYMMDD')               AS corrDeliveryConfirmDate
            FROM UCRACCT a
            LEFT JOIN UCRADDR addr
                ON addr.UCRADDR_CUST_CODE = a.UCRACCT_CUST_CODE
               AND addr.UCRADDR_STATUS_IND = 'A'
            LEFT JOIN UCBPREM p
                ON p.UCBPREM_CODE = a.UCRACCT_PREM_CODE
            WHERE a.UCRACCT_CUST_CODE = ?
              AND a.UCRACCT_PREM_CODE = ?
            """;

    /**
     * ACTIVE account with confirmed electronic bill (API VerifyAccount requires Active for SP_VERIFY_ACCOUNT).
     * Used as fallback when NEW confirmed-bill rows are unavailable.
     */
    public static final String SELECT_VERIFY_ACCOUNT_ACTIVE_CONFIRMED_BILL = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billPresType,
                a.UCRACCT_CORR_DEL_TYPE  AS correspondencePreference,
                TO_CHAR(
                    (SELECT MAX(p.OCSEPCI_EMAIL_COMP_DATE)
                     FROM OCSEPCI p
                     WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                       AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                       AND NVL(p.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
                       AND p.OCSEPCI_EMAIL_COMP_DATE IS NOT NULL),
                    'YYYYMMDD')          AS billDeliveryConfirmDate,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.GZBEMCP_ACCOUNT_IND = 'Y'
               AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
               AND SYSDATE BETWEEN e.GZBEMCP_EFFECTIVE_DATE AND e.GZBEMCP_EXPIRATION_DATE
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND a.UCRACCT_BILL_PRES_TYPE = 'E'
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_VERIFY_ACCOUNT_ACTIVE_CONFIRMED_CORR = """
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billPresType,
                a.UCRACCT_CORR_DEL_TYPE  AS correspondencePreference,
                TO_CHAR(
                    (SELECT MAX(p.OCSEPCI_EMAIL_COMP_DATE)
                     FROM OCSEPCI p
                     WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                       AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                       AND NVL(p.OCSEPCI_CORR_DEL_TYPE, 'P') = 'E'
                       AND p.OCSEPCI_EMAIL_COMP_DATE IS NOT NULL),
                    'YYYYMMDD')          AS corrDeliveryConfirmDate,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.GZBEMCP_ACCOUNT_IND = 'Y'
               AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
               AND SYSDATE BETWEEN e.GZBEMCP_EFFECTIVE_DATE AND e.GZBEMCP_EXPIRATION_DATE
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND a.UCRACCT_CORR_DEL_TYPE = 'E'
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_VERIFY_ACCOUNT_ACTIVE_PENDING_BILL = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billPresType,
                a.UCRACCT_CORR_DEL_TYPE  AS correspondencePreference,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            INNER JOIN UCRADDR addr
                ON addr.UCRADDR_CUST_CODE = a.UCRACCT_CUST_CODE
               AND addr.UCRADDR_STATUS_IND = 'A'
               AND addr.UCRADDR_STREET_NUMBER IS NOT NULL
               AND addr.UCRADDR_STREET_NAME IS NOT NULL
               AND addr.UCRADDR_CITY IS NOT NULL
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_VERIFY_ACCOUNT_ACTIVE_PENDING_CORR = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billPresType,
                a.UCRACCT_CORR_DEL_TYPE  AS correspondencePreference,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            INNER JOIN UCRADDR addr
                ON addr.UCRADDR_CUST_CODE = a.UCRACCT_CUST_CODE
               AND addr.UCRADDR_STATUS_IND = 'A'
               AND addr.UCRADDR_STREET_NUMBER IS NOT NULL
               AND addr.UCRADDR_STREET_NAME IS NOT NULL
               AND addr.UCRADDR_CITY IS NOT NULL
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_CORR_DEL_TYPE, 'P') = 'E'
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /**
     * ACTIVE paper bill + verifiable Banner email.
     * Requires complete UCRADDR street (same pool as TC_206) so SP_VERIFY_ACCOUNT succeeds.
     */
    public static final String SELECT_VERIFY_ACCOUNT_ACTIVE_PAPER_BILL = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billPresType,
                a.UCRACCT_CORR_DEL_TYPE  AS correspondencePreference,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            INNER JOIN UCRADDR addr
                ON addr.UCRADDR_CUST_CODE = a.UCRACCT_CUST_CODE
               AND addr.UCRADDR_STATUS_IND = 'A'
               AND addr.UCRADDR_STREET_NUMBER IS NOT NULL
               AND addr.UCRADDR_PDIR_CODE_PRE IS NOT NULL
               AND addr.UCRADDR_STREET_NAME IS NOT NULL
               AND addr.UCRADDR_CITY IS NOT NULL
               AND addr.UCRADDR_STAT_CODE IS NOT NULL
               AND addr.UCRADDR_ZIP IS NOT NULL
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_VERIFY_ACCOUNT_ACTIVE_PAPER_CORR = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billPresType,
                a.UCRACCT_CORR_DEL_TYPE  AS correspondencePreference,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            INNER JOIN UCRADDR addr
                ON addr.UCRADDR_CUST_CODE = a.UCRACCT_CUST_CODE
               AND addr.UCRADDR_STATUS_IND = 'A'
               AND addr.UCRADDR_STREET_NUMBER IS NOT NULL
               AND addr.UCRADDR_PDIR_CODE_PRE IS NOT NULL
               AND addr.UCRADDR_STREET_NAME IS NOT NULL
               AND addr.UCRADDR_CITY IS NOT NULL
               AND addr.UCRADDR_STAT_CODE IS NOT NULL
               AND addr.UCRADDR_ZIP IS NOT NULL
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NVL(a.UCRACCT_CORR_DEL_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_CORR_DEL_TYPE, 'P') = 'E'
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** Primary Banner email for a customer (active GZBEMCP account email). */
    public static final String SELECT_ACTIVE_BANNER_EMAIL_BY_CUST = """
            SELECT g.GZBEMCP_EMAIL_ADDR AS bannerEmail
            FROM GZBEMCP g
            WHERE g.GZBEMCP_CUST_CODE = ?
              AND g.GZBEMCP_ACCOUNT_IND = 'Y'
              AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
              AND SYSDATE BETWEEN g.GZBEMCP_EFFECTIVE_DATE AND g.GZBEMCP_EXPIRATION_DATE
            ORDER BY
                NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                g.GZBEMCP_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** Fallback email for GTBENRL TC_228/229 when no currently effective GZBEMCP row exists. */
    public static final String SELECT_ANY_BANNER_EMAIL_BY_CUST = """
            SELECT g.GZBEMCP_EMAIL_ADDR AS bannerEmail
            FROM GZBEMCP g
            WHERE g.GZBEMCP_CUST_CODE = ?
              AND g.GZBEMCP_ACCOUNT_IND = 'Y'
              AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
            ORDER BY
                CASE WHEN SYSDATE BETWEEN g.GZBEMCP_EFFECTIVE_DATE AND g.GZBEMCP_EXPIRATION_DATE
                     THEN 0 ELSE 1 END,
                NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                g.GZBEMCP_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /**
     * TC_230–231 / TC_234 — NEW GZRPPTH with bill PaperlessConfDate
     * (and bill_pres_type='E' for confirmed preference TC_234).
     */
    public static final String SELECT_VERIFY_ACCOUNT_NEW_CONFIRMED_BILL = """
            SELECT
                g.GZRPPTH_CUST_CODE              AS customerCode,
                g.GZRPPTH_PREM_CODE              AS premisesCode,
                g.GZRPPTH_STATUS_CODE            AS accountStatus,
                g.GZRPPTH_BILL_PRES_TYPE         AS billPresType,
                g.GZRPPTH_CORR_DEL_TYPE          AS correspondencePreference,
                TO_CHAR(g.GZRPPTH_PAPERLESS_CONF_DATE, 'YYYYMMDD') AS billDeliveryConfirmDate,
                TO_CHAR(g.GZRPPTH_PAPERLESS_CORR_CONF_DT, 'YYYYMMDD') AS corrDeliveryConfirmDate,
                e.GZBEMCP_EMAIL_ADDR             AS bannerEmail
            FROM GZRPPTH g
            LEFT JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = g.GZRPPTH_CUST_CODE
               AND e.GZBEMCP_ACCOUNT_IND = 'Y'
               AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
               AND SYSDATE BETWEEN e.GZBEMCP_EFFECTIVE_DATE AND e.GZBEMCP_EXPIRATION_DATE
            WHERE g.GZRPPTH_STATUS_CODE = 'N'
              AND g.GZRPPTH_PAPERLESS_CONF_DATE IS NOT NULL
              AND g.GZRPPTH_BILL_PRES_TYPE = 'E'
            ORDER BY g.GZRPPTH_PAPERLESS_CONF_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /**
     * TC_232–233 / TC_235 — NEW GZRPPTH with correspondence PaperlessCorrConfDate
     * (and corr_del_type='E' for confirmed preference TC_235).
     */
    public static final String SELECT_VERIFY_ACCOUNT_NEW_CONFIRMED_CORR = """
            SELECT
                g.GZRPPTH_CUST_CODE              AS customerCode,
                g.GZRPPTH_PREM_CODE              AS premisesCode,
                g.GZRPPTH_STATUS_CODE            AS accountStatus,
                g.GZRPPTH_BILL_PRES_TYPE         AS billPresType,
                g.GZRPPTH_CORR_DEL_TYPE          AS correspondencePreference,
                TO_CHAR(g.GZRPPTH_PAPERLESS_CONF_DATE, 'YYYYMMDD') AS billDeliveryConfirmDate,
                TO_CHAR(g.GZRPPTH_PAPERLESS_CORR_CONF_DT, 'YYYYMMDD') AS corrDeliveryConfirmDate,
                e.GZBEMCP_EMAIL_ADDR             AS bannerEmail
            FROM GZRPPTH g
            LEFT JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = g.GZRPPTH_CUST_CODE
               AND e.GZBEMCP_ACCOUNT_IND = 'Y'
               AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
               AND SYSDATE BETWEEN e.GZBEMCP_EFFECTIVE_DATE AND e.GZBEMCP_EXPIRATION_DATE
            WHERE g.GZRPPTH_STATUS_CODE = 'N'
              AND g.GZRPPTH_PAPERLESS_CORR_CONF_DT IS NOT NULL
              AND g.GZRPPTH_CORR_DEL_TYPE = 'E'
            ORDER BY g.GZRPPTH_PAPERLESS_CORR_CONF_DT DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /**
     * TC_236 — NEW GZRPPTH bill=E with PaperlessConfDate NULL (pre-confirmation → BillPresType I).
     */
    public static final String SELECT_VERIFY_ACCOUNT_NEW_PENDING_BILL = """
            SELECT
                g.GZRPPTH_CUST_CODE              AS customerCode,
                g.GZRPPTH_PREM_CODE              AS premisesCode,
                g.GZRPPTH_STATUS_CODE            AS accountStatus,
                'I'                              AS billPresType,
                g.GZRPPTH_CORR_DEL_TYPE          AS correspondencePreference,
                g.GZRPPTH_BILL_PRES_TYPE         AS bannerBillPresType,
                e.GZBEMCP_EMAIL_ADDR             AS bannerEmail
            FROM GZRPPTH g
            LEFT JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = g.GZRPPTH_CUST_CODE
               AND e.GZBEMCP_ACCOUNT_IND = 'Y'
               AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
               AND SYSDATE BETWEEN e.GZBEMCP_EFFECTIVE_DATE AND e.GZBEMCP_EXPIRATION_DATE
            WHERE g.GZRPPTH_STATUS_CODE = 'N'
              AND g.GZRPPTH_BILL_PRES_TYPE = 'E'
              AND g.GZRPPTH_PAPERLESS_CONF_DATE IS NULL
            ORDER BY g.GZRPPTH_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /**
     * TC_237 — NEW GZRPPTH corr=E with PaperlessCorrConfDate NULL
     * (pre-confirmation → CorrespondencePreference I).
     */
    public static final String SELECT_VERIFY_ACCOUNT_NEW_PENDING_CORR = """
            SELECT
                g.GZRPPTH_CUST_CODE              AS customerCode,
                g.GZRPPTH_PREM_CODE              AS premisesCode,
                g.GZRPPTH_STATUS_CODE            AS accountStatus,
                g.GZRPPTH_BILL_PRES_TYPE         AS billPresType,
                'I'                              AS correspondencePreference,
                g.GZRPPTH_CORR_DEL_TYPE          AS bannerCorrDelType,
                e.GZBEMCP_EMAIL_ADDR             AS bannerEmail
            FROM GZRPPTH g
            LEFT JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = g.GZRPPTH_CUST_CODE
               AND e.GZBEMCP_ACCOUNT_IND = 'Y'
               AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
               AND SYSDATE BETWEEN e.GZBEMCP_EFFECTIVE_DATE AND e.GZBEMCP_EXPIRATION_DATE
            WHERE g.GZRPPTH_STATUS_CODE = 'N'
              AND g.GZRPPTH_CORR_DEL_TYPE = 'E'
              AND g.GZRPPTH_PAPERLESS_CORR_CONF_DT IS NULL
            ORDER BY g.GZRPPTH_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /**
     * TC_238 — NEW GZRPPTH not enrolled for bill paperless ⇒ BillPresType P.
     */
    public static final String SELECT_VERIFY_ACCOUNT_NEW_PAPER_BILL = """
            SELECT
                g.GZRPPTH_CUST_CODE              AS customerCode,
                g.GZRPPTH_PREM_CODE              AS premisesCode,
                g.GZRPPTH_STATUS_CODE            AS accountStatus,
                NVL(g.GZRPPTH_BILL_PRES_TYPE, 'P') AS billPresType,
                g.GZRPPTH_CORR_DEL_TYPE          AS correspondencePreference,
                e.GZBEMCP_EMAIL_ADDR             AS bannerEmail
            FROM GZRPPTH g
            LEFT JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = g.GZRPPTH_CUST_CODE
               AND e.GZBEMCP_ACCOUNT_IND = 'Y'
               AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
               AND SYSDATE BETWEEN e.GZBEMCP_EFFECTIVE_DATE AND e.GZBEMCP_EXPIRATION_DATE
            WHERE g.GZRPPTH_STATUS_CODE = 'N'
              AND NVL(g.GZRPPTH_BILL_PRES_TYPE, 'P') = 'P'
            ORDER BY g.GZRPPTH_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /**
     * TC_239 — NEW GZRPPTH not enrolled for correspondence paperless ⇒ CorrespondencePreference P.
     */
    public static final String SELECT_VERIFY_ACCOUNT_NEW_PAPER_CORR = """
            SELECT
                g.GZRPPTH_CUST_CODE              AS customerCode,
                g.GZRPPTH_PREM_CODE              AS premisesCode,
                g.GZRPPTH_STATUS_CODE            AS accountStatus,
                g.GZRPPTH_BILL_PRES_TYPE         AS billPresType,
                NVL(g.GZRPPTH_CORR_DEL_TYPE, 'P') AS correspondencePreference,
                e.GZBEMCP_EMAIL_ADDR             AS bannerEmail
            FROM GZRPPTH g
            LEFT JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = g.GZRPPTH_CUST_CODE
               AND e.GZBEMCP_ACCOUNT_IND = 'Y'
               AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
               AND SYSDATE BETWEEN e.GZBEMCP_EFFECTIVE_DATE AND e.GZBEMCP_EXPIRATION_DATE
            WHERE g.GZRPPTH_STATUS_CODE = 'N'
              AND NVL(g.GZRPPTH_CORR_DEL_TYPE, 'P') = 'P'
            ORDER BY g.GZRPPTH_CUST_CODE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /**
     * Expired unconfirmed bill PPER; GZRPPTH bill preference paper; no UCRACCT.
     * Banner GZBEMCP email is optional for this preference state.
     */
    public static final String SELECT_VERIFY_ACCOUNT_NEW_EXPIRED_BILL = """
            SELECT
                o.OCSEPCI_CUST_CODE              AS customerCode,
                o.OCSEPCI_PREM_CODE              AS premisesCode,
                g.GZRPPTH_STATUS_CODE            AS accountStatus,
                NVL(g.GZRPPTH_BILL_PRES_TYPE, 'P') AS billPresType,
                g.GZRPPTH_CORR_DEL_TYPE          AS correspondencePreference,
                e.GZBEMCP_EMAIL_ADDR             AS bannerEmail,
                o.OCSEPCI_EMAIL_ADDR             AS ocsepciEmail,
                b.GTBENRL_EMAIL                  AS gtbenrlEmail
            FROM OCSEPCI o
            INNER JOIN GZRPPTH g
                ON g.GZRPPTH_CUST_CODE = o.OCSEPCI_CUST_CODE
               AND g.GZRPPTH_PREM_CODE = o.OCSEPCI_PREM_CODE
            INNER JOIN GTBENRL b
                ON b.GTBENRL_CUST_CODE = o.OCSEPCI_CUST_CODE
               AND b.GTBENRL_PREM_CODE = o.OCSEPCI_PREM_CODE
            LEFT JOIN UCRACCT u
                ON u.UCRACCT_CUST_CODE = o.OCSEPCI_CUST_CODE
               AND u.UCRACCT_PREM_CODE = o.OCSEPCI_PREM_CODE
            LEFT JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = o.OCSEPCI_CUST_CODE
               AND e.GZBEMCP_ACCOUNT_IND = 'Y'
               AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
               AND SYSDATE BETWEEN e.GZBEMCP_EFFECTIVE_DATE AND e.GZBEMCP_EXPIRATION_DATE
            WHERE o.OCSEPCI_BILL_PRES_TYPE = 'E'
              AND o.OCSEPCI_EMAIL_COMP_DATE IS NULL
              AND o.OCSEPCI_EMAIL_EXP_DATE < SYSDATE
              AND o.OCSEPCI_CONF_STATUS = 0
              AND g.GZRPPTH_STATUS_CODE = 'N'
              AND g.GZRPPTH_BILL_PRES_TYPE = 'P'
              AND u.UCRACCT_CUST_CODE IS NULL
            ORDER BY o.OCSEPCI_EMAIL_EXP_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /**
     * Expired unconfirmed correspondence PPER; GZRPPTH corr preference paper; no UCRACCT.
     * Banner GZBEMCP email is optional for this preference state.
     */
    public static final String SELECT_VERIFY_ACCOUNT_NEW_EXPIRED_CORR = """
            SELECT
                o.OCSEPCI_CUST_CODE              AS customerCode,
                o.OCSEPCI_PREM_CODE              AS premisesCode,
                g.GZRPPTH_STATUS_CODE            AS accountStatus,
                g.GZRPPTH_BILL_PRES_TYPE         AS billPresType,
                NVL(g.GZRPPTH_CORR_DEL_TYPE, 'P') AS correspondencePreference,
                e.GZBEMCP_EMAIL_ADDR             AS bannerEmail,
                o.OCSEPCI_EMAIL_ADDR             AS ocsepciEmail,
                b.GTBENRL_EMAIL                  AS gtbenrlEmail
            FROM OCSEPCI o
            INNER JOIN GZRPPTH g
                ON g.GZRPPTH_CUST_CODE = o.OCSEPCI_CUST_CODE
               AND g.GZRPPTH_PREM_CODE = o.OCSEPCI_PREM_CODE
            INNER JOIN GTBENRL b
                ON b.GTBENRL_CUST_CODE = o.OCSEPCI_CUST_CODE
               AND b.GTBENRL_PREM_CODE = o.OCSEPCI_PREM_CODE
            LEFT JOIN UCRACCT u
                ON u.UCRACCT_CUST_CODE = o.OCSEPCI_CUST_CODE
               AND u.UCRACCT_PREM_CODE = o.OCSEPCI_PREM_CODE
            LEFT JOIN GZBEMCP e
                ON e.GZBEMCP_CUST_CODE = o.OCSEPCI_CUST_CODE
               AND e.GZBEMCP_ACCOUNT_IND = 'Y'
               AND e.GZBEMCP_EMAIL_ADDR IS NOT NULL
               AND SYSDATE BETWEEN e.GZBEMCP_EFFECTIVE_DATE AND e.GZBEMCP_EXPIRATION_DATE
            WHERE o.OCSEPCI_CORR_DEL_TYPE = 'E'
              AND o.OCSEPCI_EMAIL_COMP_DATE IS NULL
              AND o.OCSEPCI_EMAIL_EXP_DATE < SYSDATE
              AND o.OCSEPCI_CONF_STATUS = 0
              AND g.GZRPPTH_STATUS_CODE = 'N'
              AND g.GZRPPTH_CORR_DEL_TYPE = 'P'
              AND u.UCRACCT_CUST_CODE IS NULL
            ORDER BY o.OCSEPCI_EMAIL_EXP_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** VerifyAccount TC_230 fallback: ACTIVE expired unconfirmed bill PPER → BillPresType=P. */
    public static final String SELECT_VERIFY_ACCOUNT_ACTIVE_EXPIRED_BILL = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') AS billPresType,
                a.UCRACCT_CORR_DEL_TYPE  AS correspondencePreference,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NVL(a.UCRACCT_BILL_PRES_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
              AND EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_BILL_PRES_TYPE, 'P') = 'E'
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
                    AND p.OCSEPCI_EMAIL_EXP_DATE <= SYSDATE
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_VERIFY_ACCOUNT_ACTIVE_EXPIRED_CORR = """
            WITH active_email AS (
                SELECT
                    g.GZBEMCP_CUST_CODE,
                    g.GZBEMCP_EMAIL_ADDR,
                    ROW_NUMBER() OVER (
                        PARTITION BY g.GZBEMCP_CUST_CODE
                        ORDER BY
                            NVL(g.GZBEMCP_EXPIRATION_DATE, DATE '2099-12-31') DESC,
                            g.GZBEMCP_ACTIVITY_DATE DESC
                    ) AS rn
                FROM GZBEMCP g
                WHERE g.GZBEMCP_ACCOUNT_IND = 'Y'
                  AND g.GZBEMCP_EMAIL_ADDR IS NOT NULL
                  AND (g.GZBEMCP_EXPIRATION_DATE IS NULL OR g.GZBEMCP_EXPIRATION_DATE >= SYSDATE)
            )
            SELECT
                a.UCRACCT_CUST_CODE      AS customerCode,
                a.UCRACCT_PREM_CODE      AS premisesCode,
                a.UCRACCT_STATUS_IND     AS accountStatus,
                a.UCRACCT_BILL_PRES_TYPE AS billPresType,
                NVL(a.UCRACCT_CORR_DEL_TYPE, 'P') AS correspondencePreference,
                e.GZBEMCP_EMAIL_ADDR     AS bannerEmail
            FROM UCRACCT a
            INNER JOIN active_email e
                ON e.GZBEMCP_CUST_CODE = a.UCRACCT_CUST_CODE
               AND e.rn = 1
            WHERE a.UCRACCT_STATUS_IND = 'A'
              AND NVL(a.UCRACCT_CORR_DEL_TYPE, 'P') = 'P'
              AND NOT EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND p.OCSEPCI_EMAIL_EXP_DATE > SYSDATE
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
              )
              AND EXISTS (
                  SELECT 1
                  FROM OCSEPCI p
                  WHERE p.OCSEPCI_CUST_CODE = a.UCRACCT_CUST_CODE
                    AND p.OCSEPCI_PREM_CODE = a.UCRACCT_PREM_CODE
                    AND NVL(p.OCSEPCI_CORR_DEL_TYPE, 'P') = 'E'
                    AND p.OCSEPCI_EMAIL_COMP_DATE IS NULL
                    AND p.OCSEPCI_EMAIL_EXP_DATE <= SYSDATE
              )
            ORDER BY a.UCRACCT_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_BILL_HISTORY_MORE_THAN_ONE= """
            SELECT
                b.ubbbhst_cust_code,
                b.ubbbhst_prem_code,
                COUNT(DISTINCT b.ubbbhst_printed_date) AS total_bills
            FROM ubbbhst b
            INNER JOIN ucracct a
                ON  a.ucracct_cust_code = b.ubbbhst_cust_code
                AND a.ucracct_prem_code = b.ubbbhst_prem_code
                AND a.ucracct_status_ind NOT IN ('N')
            WHERE b.ubbbhst_printed_date > ADD_MONTHS(TRUNC(SYSDATE), -4)
              AND LENGTH(b.ubbbhst_cust_code) >= 4
              AND NVL(b.ubbbhst_cancel_ind, 0) = 0
            GROUP BY
                b.ubbbhst_cust_code,
                b.ubbbhst_prem_code
            HAVING COUNT(DISTINCT b.ubbbhst_printed_date) > 1
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_BILL_INFO_WITH_REWARDS= """
            SELECT
                w.gzbrwds_cust_code,
                w.gzbrwds_prem_code
            FROM gzbrwds w
            WHERE w.gzbrwds_cncl_date IS NULL
              AND w.gzbrwds_fulfill_date IS NULL
              AND w.gzbrwds_REWARD_ID IS NOT NULL
              AND EXISTS (
                    SELECT 1
                    FROM ubbbhst b
                    WHERE b.ubbbhst_cust_code = w.gzbrwds_cust_code
                      AND b.ubbbhst_prem_code = w.gzbrwds_prem_code
                      AND b.ubbbhst_printed_date >= ADD_MONTHS(TRUNC(SYSDATE), -24)
                )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_BILL_HISTORY_PREV_BILL_0 = """
        SELECT ubbbhst_cust_code, ubbbhst_prem_code
        FROM (
            SELECT b.ubbbhst_cust_code, b.ubbbhst_prem_code
            FROM ubbbhst b
            WHERE b.ubbbhst_printed_date IS NOT NULL
              AND b.ubbbhst_printed_date > SYSDATE - 300
              AND b.ubbbhst_ending_bal > 0
              AND NVL(b.ubbbhst_prev_bal, 0) = 0
            GROUP BY b.ubbbhst_cust_code, b.ubbbhst_prem_code
            HAVING COUNT(1) = 1
            ORDER BY b.ubbbhst_cust_code
        )
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String SELECT_BILL_INFO= """
            SELECT
                b.ubbbhst_cust_code,
                b.ubbbhst_prem_code,
                COUNT(DISTINCT b.ubbbhst_printed_date) AS total_bills
            FROM ubbbhst b
            INNER JOIN ucracct a
                ON  a.ucracct_cust_code = b.ubbbhst_cust_code
                AND a.ucracct_prem_code = b.ubbbhst_prem_code
                AND a.ucracct_status_ind NOT IN ('N')
            WHERE b.ubbbhst_cust_code = ?
              AND b.ubbbhst_prem_code = ?
            GROUP BY b.ubbbhst_cust_code, b.ubbbhst_prem_code
            ORDER BY DBMS_RANDOM.VALUE
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_NO_USAGE_HISTORY_FINAL_ACCOUNT= """
            SELECT\s
                ua.ucracct_cust_code,
                ua.ucracct_prem_code,
                ua.ucracct_status_ind
            FROM\s
                ucracct ua
            WHERE\s
                LENGTH(ua.ucracct_cust_code) >= 5
                AND ua.ucracct_status_ind = 'F'
                AND NOT EXISTS (
                    SELECT 1\s
                    FROM ubbchst ch
                    WHERE ch.ubbchst_cust_code = ua.ucracct_cust_code
                      AND ch.ubbchst_prem_code = ua.ucracct_prem_code
                )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_NO_USAGE_HISTORY_INACTIVE_ACCOUNT= """
            SELECT\s
                ua.ucracct_cust_code,
                ua.ucracct_prem_code,
                ua.ucracct_status_ind
            FROM\s
                ucracct ua
            WHERE\s
                LENGTH(ua.ucracct_cust_code) >= 5
                AND ua.ucracct_status_ind = 'I'
                AND NOT EXISTS (
                    SELECT 1\s
                    FROM ubbchst ch
                    WHERE ch.ubbchst_cust_code = ua.ucracct_cust_code
                      AND ch.ubbchst_prem_code = ua.ucracct_prem_code
                )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String CHECK_ACCOUNT_REGISTERED = """
            SELECT account_number
            FROM custadv_registered_accounts
            WHERE account_number LIKE CONCAT('%', ?, '%')
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String CHECK_ACCOUNT_REGISTERED2 = """
            SELECT account_number
            FROM custadv_registered_accounts
            WHERE account_number LIKE CONCAT('%', ?, '%')
            FETCH FIRST 1 ROWS ONLY
            """;

    /**
     * MariaDB: online Preferences-capable accounts (domain_id=2) with registered account_number.
     * Used by FTD05 VerifyAccount Preferences path (Banner-only emails return ErrorCode 302).
     */
    public static final String SELECT_PREFERENCES_REGISTERED_ACCOUNTS = """
            SELECT
                u.user_name AS loginOrEmail,
                ra.account_number AS accountNumber
            FROM users u
            INNER JOIN custadv_registered_accounts ra
                ON u.user_id = ra.user_id
            WHERE u.domain_id = 2
              AND u.active = 1
              AND IFNULL(u.deleted, 0) = 0
              AND ra.account_number IS NOT NULL
              AND LENGTH(ra.account_number) >= 16
              AND u.user_name LIKE '%@%'
            ORDER BY u.user_id DESC
            LIMIT ?
            """;


    public static final String SELECT_FINAL_ACCOUNT_ONLY= """
            SELECT T1.UCRACCT_CUST_CODE,
            T1.UCRACCT_PREM_CODE
            FROM UCRACCT T1
            WHERE T1.UCRACCT_STATUS_IND = 'F'
            AND LENGTH(t1.ucracct_cust_code) >5
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

    public static final String SELECT_ACCOUNT_WITH_ACTUAL_READING= """
            SELECT cust_code,
                   prem_code
            FROM (
                SELECT DISTINCT
                       ua.ucracct_cust_code AS cust_code,
                       ua.ucracct_prem_code AS prem_code
                FROM ucracct ua
                JOIN urrshis ur
                  ON ur.urrshis_cust_code = ua.ucracct_cust_code
                 AND ur.urrshis_prem_code = ua.ucracct_prem_code
                WHERE ua.ucracct_status_ind <> 'N'
                  AND LENGTH(ua.ucracct_cust_code) >= 5
                  AND ur.urrshis_actn_code IN ('READ','OUT')
                  AND ur.urrshis_rtyp_code = 'A'
                  AND ur.urrshis_serv_num = 1
                  AND ur.urrshis_chrg_calc_num IS NOT NULL
                  AND ur.urrshis_action_date >= ADD_MONTHS(SYSDATE, -12)
                  AND ROWNUM <= 50   -- early stop for speed
            )
            WHERE ROWNUM = 1
            """;

    public static final String SELECT_ACCOUNT_WITH_ACTUAL_READING2= """
            SELECT *
            FROM urrshis x
            WHERE x.urrshis_rtyp_code = 'A'
            AND x.URRSHIS_ACTION_DATE >= ADD_MONTHS(SYSDATE, -12)
              AND EXISTS (
                    SELECT 1
                    FROM ucracct a
                    WHERE x.urrshis_cust_code = a.ucracct_cust_code
                      AND x.urrshis_prem_code = a.ucracct_prem_code
                      AND a.ucracct_status_ind <> 'N'
                      AND LENGTH(a.ucracct_cust_code) >= 5
                )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_ACTUAL_READING3= """
            SELECT *
                                    FROM urrshis x
                                    WHERE x.urrshis_rtyp_code = 'A'
                                      AND x.urrshis_action_date >= ADD_MONTHS(SYSDATE, -12)
                                      AND EXISTS (
                                            SELECT 1
                                            FROM ucracct a
                                            WHERE a.ucracct_cust_code = x.urrshis_cust_code
                                              AND a.ucracct_prem_code = x.urrshis_prem_code
                                              AND a.ucracct_status_ind <> 'N'
                                              AND LENGTH(a.ucracct_cust_code) >= 5
                                        )
                                        AND NOT EXISTS (
                                            SELECT 1
                                            FROM ucracct a
                                            WHERE a.ucracct_cust_code = x.urrshis_cust_code
                                              AND a.ucracct_prem_code = x.urrshis_prem_code
                                              AND a.ucracct_status_ind = 'A'
                                              AND LENGTH(a.ucracct_cust_code) >= 5
                                        )
                                      AND NOT EXISTS (
                                            SELECT 1
                                            FROM urrshis y
                                            WHERE y.urrshis_cust_code = x.urrshis_cust_code
                                              AND y.urrshis_prem_code = x.urrshis_prem_code
                                              AND y.urrshis_action_date < ADD_MONTHS(SYSDATE, -12)
                                        )
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_ZERO_READING= """
             SELECT cust_code,
                   prem_code
            FROM (
                SELECT DISTINCT
                       ua.ucracct_cust_code AS cust_code,
                       ua.ucracct_prem_code AS prem_code
                FROM ucracct ua
                JOIN urrshis ur
                  ON ur.urrshis_cust_code = ua.ucracct_cust_code
                 AND ur.urrshis_prem_code = ua.ucracct_prem_code
                WHERE ua.ucracct_status_ind <> 'N'
                  AND LENGTH(ua.ucracct_cust_code) >= 5
                  AND ur.urrshis_actn_code IN ('READ','OUT')
                  AND ur.urrshis_rtyp_code = 'Z'
                  AND ur.urrshis_serv_num = 1
                  AND ur.urrshis_chrg_calc_num IS NOT NULL
                  AND ur.urrshis_action_date >= ADD_MONTHS(SYSDATE, -12)
                  AND ROWNUM <= 50   -- early stop for speed
            )
            WHERE ROWNUM = 1
            """;

    public static final String SELECT_ACCOUNT_WITH_ESTIMATED_READING= """
           SELECT cust_code,
                   prem_code
            FROM (
                SELECT DISTINCT
                       ua.ucracct_cust_code AS cust_code,
                       ua.ucracct_prem_code AS prem_code
                FROM ucracct ua
                JOIN urrshis ur
                  ON ur.urrshis_cust_code = ua.ucracct_cust_code
                 AND ur.urrshis_prem_code = ua.ucracct_prem_code
                WHERE ua.ucracct_status_ind <> 'N'
                  AND LENGTH(ua.ucracct_cust_code) >= 5
                  AND ur.urrshis_actn_code IN ('READ','OUT')
                  AND ur.urrshis_rtyp_code = 'E'
                  AND ur.urrshis_serv_num = 1
                  AND ur.urrshis_chrg_calc_num IS NOT NULL
                  AND ur.urrshis_action_date >= ADD_MONTHS(SYSDATE, -24)
                  AND ROWNUM <= 50   -- early stop for speed
            )
            WHERE ROWNUM = 1
            """;


    public static final String SELECT_ACCOUNT_WITHOUT_NICKNAME= """
            SELECT
                UCRACCT_CUST_CODE,
                UCRACCT_PREM_CODE,
                UCRACCT_NICK_NAME
            FROM
                UCRACCT
            WHERE
                UCRACCT_STATUS_IND <> 'N'
                AND (UCRACCT_NICK_NAME IS NULL OR TRIM(UCRACCT_NICK_NAME) = '')
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITHOUT_NICKNAME2= """
            SELECT
                UCRACCT_CUST_CODE,
                UCRACCT_PREM_CODE,
                UCRACCT_NICK_NAME
            FROM
                UCRACCT
            WHERE
                UCRACCT_STATUS_IND <> 'N'
                AND (UCRACCT_NICK_NAME IS NULL OR TRIM(UCRACCT_NICK_NAME) = '')
                AND LENGTH(UCRACCT_CUST_CODE)>=5
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_INDUSTRIAL_ACCOUNT= """
            SELECT UCRACCT_PREM_CODE, UCRACCT_CUST_CODE
                FROM
                ucracct
                join
                 ucrserv
                 ON ucracct_prem_code=ucrserv_prem_code
                 WHERE ucrserv_scls_code = 'IN'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_AGRICULTURAL_ACCOUNT= """
            SELECT UCRACCT_PREM_CODE, UCRACCT_CUST_CODE
                FROM
                ucracct
                join
                 ucrserv
                 ON ucracct_prem_code=ucrserv_prem_code
                 WHERE ucrserv_scls_code = 'AG'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_MULTIFAMILY_ACCOUNT= """
            SELECT UCRACCT_PREM_CODE, UCRACCT_CUST_CODE
                FROM
                ucracct
                join
                 ucrserv
                 ON ucracct_prem_code=ucrserv_prem_code
                 WHERE ucrserv_scls_code = 'MF'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_SEASONAL_ACCOUNT= """
            SELECT UCRACCT_PREM_CODE, UCRACCT_CUST_CODE
                FROM
                ucracct
                join
                 ucrserv
                 ON ucracct_prem_code=ucrserv_prem_code
                 WHERE ucrserv_scls_code = 'SE'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_SENIOR_CITIZEN_ACCOUNT= """
            SELECT UCRACCT_PREM_CODE, UCRACCT_CUST_CODE
                FROM
                ucracct
                join
                 ucrserv
                 ON ucracct_prem_code=ucrserv_prem_code
                 WHERE ucrserv_scls_code = 'SR'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_STREET_NUMBER= """
            SELECT\s
                a.UCRACCT_PREM_CODE, a.UCRACCT_CUST_CODE
            FROM\s
                ucracct a
            JOIN\s
                ucbprem b
                    ON a.ucracct_prem_code = b.ucbprem_code
            WHERE\s
                b.ucbprem_street_number IS NOT NULL
                AND LENGTH(a.ucracct_cust_code) >= 5
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_STREET_NAME= """
            SELECT a.UCRACCT_PREM_CODE, a.UCRACCT_CUST_CODE
                            FROM
                            ucracct a
                            join
                             ucbprem b
                             ON a.ucracct_prem_code=b.ucbprem_code
                             WHERE
                             b.ucbprem_street_name IS NOT NULL\s
                             AND LENGTH(a.ucracct_cust_code) >= 5
                             AND a.UCRACCT_CUST_CODE <> '97557'
                        FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_STREET_SUFFIX= """
            SELECT a.UCRACCT_PREM_CODE, a.UCRACCT_CUST_CODE
                                                   FROM
                                                   ucracct a
                                                   join
                                                    ucbprem b
                                                    ON a.ucracct_prem_code=b.ucbprem_code
                                                    WHERE
                                                    b.ucbprem_ssfx_code IS NOT NULL\s
                                                    AND LENGTH(a.ucracct_cust_code) >= 5
                                                    AND a.UCRACCT_CUST_CODE <> '97557'
                                               FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITHOUT_STREET_SUFFIX= """
            SELECT a.UCRACCT_PREM_CODE, a.UCRACCT_CUST_CODE
                                                   FROM
                                                   ucracct a
                                                   join
                                                    ucbprem b
                                                    ON a.ucracct_prem_code=b.ucbprem_code
                                                    WHERE
                                                    b.ucbprem_ssfx_code IS NULL\s
                                                    AND LENGTH(a.ucracct_cust_code) >= 5
                                               FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_STREET_POST_DIR= """
            SELECT a.UCRACCT_PREM_CODE, a.UCRACCT_CUST_CODE
                            FROM
                            ucracct a
                            join
                             ucbprem b
                             ON a.ucracct_prem_code=b.ucbprem_code
                             WHERE
                             b.ucbprem_pdir_code_post IS NOT null
                             AND LENGTH(a.ucracct_cust_code) >= 5
                        FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_UNIT_TYPE= """
            SELECT a.UCRACCT_PREM_CODE, a.UCRACCT_CUST_CODE
                            FROM
                            ucracct a
                            join
                             ucbprem b
                             ON a.ucracct_prem_code=b.ucbprem_code
                             WHERE
                             b.ucbprem_utyp_code IS NOT null
                             AND LENGTH(a.ucracct_cust_code) >= 5
                        FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_UNIT_NUMBER= """
            SELECT a.UCRACCT_PREM_CODE, a.UCRACCT_CUST_CODE
                                        FROM
                                        ucracct a
                                        join
                                         ucbprem b
                                         ON a.ucracct_prem_code=b.ucbprem_code
                                         WHERE
                                         b.ucbprem_unit IS NOT null
                                         AND LENGTH(a.ucracct_cust_code) >= 5
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITHOUT_UNIT_NUMBER= """
            SELECT a.UCRACCT_PREM_CODE, a.UCRACCT_CUST_CODE
                                        FROM
                                        ucracct a
                                        join
                                         ucbprem b
                                         ON a.ucracct_prem_code=b.ucbprem_code
                                         WHERE
                                         b.ucbprem_unit IS null
                                         AND LENGTH(a.ucracct_cust_code) >= 5
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_CITY= """
            SELECT a.UCRACCT_PREM_CODE, a.UCRACCT_CUST_CODE
                                        FROM
                                        ucracct a
                                        join
                                         ucbprem b
                                         ON a.ucracct_prem_code=b.ucbprem_code
                                         WHERE
                                         b.ucbprem_city IS NOT null
                                         AND LENGTH(a.ucracct_cust_code) >= 5
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_STATE= """
            SELECT a.UCRACCT_PREM_CODE, a.UCRACCT_CUST_CODE
                                                                FROM
                                                                ucracct a
                                                                join
                                                                 ucbprem b
                                                                 ON a.ucracct_prem_code=b.ucbprem_code
                                                                 WHERE
                                                                 b.ucbprem_stat_code_addr IS NOT null
                                                                 AND LENGTH(a.ucracct_cust_code) >= 5
                                                            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITHOUT_STATE= """
            SELECT a.UCRACCT_PREM_CODE, a.UCRACCT_CUST_CODE
                                        FROM
                                        ucracct a
                                        join
                                         ucbprem b
                                         ON a.ucracct_prem_code=b.ucbprem_code
                                         WHERE
                                         b.ucbprem_stat_code_addr IS null
                                         AND LENGTH(a.ucracct_cust_code) >= 5
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_ZIP_CODE= """
            SELECT a.UCRACCT_PREM_CODE, a.UCRACCT_CUST_CODE
                                        FROM
                                        ucracct a
                                        join
                                         ucbprem b
                                         ON a.ucracct_prem_code=b.ucbprem_code
                                         WHERE
                                         b.ucbprem_zipc_code IS NOT null
                                         AND LENGTH(a.ucracct_cust_code) >= 5
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITHOUT_ZIP_CODE= """
            SELECT a.UCRACCT_PREM_CODE, a.UCRACCT_CUST_CODE
                                        FROM
                                        ucracct a
                                        join
                                         ucbprem b
                                         ON a.ucracct_prem_code=b.ucbprem_code
                                         WHERE
                                         b.ucbprem_zipc_code IS null
                                         AND LENGTH(a.ucracct_cust_code) >= 5
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITHOUT_CITY= """
            SELECT a.UCRACCT_PREM_CODE, a.UCRACCT_CUST_CODE
                                        FROM
                                        ucracct a
                                        join
                                         ucbprem b
                                         ON a.ucracct_prem_code=b.ucbprem_code
                                         WHERE
                                         b.ucbprem_city IS null
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITHOUT_UNIT_TYPE= """
           SELECT a.UCRACCT_PREM_CODE, a.UCRACCT_CUST_CODE
                            FROM
                            ucracct a
                            join
                             ucbprem b
                             ON a.ucracct_prem_code=b.ucbprem_code
                             WHERE
                             b.ucbprem_utyp_code IS null
                             AND LENGTH(a.ucracct_cust_code) >= 5
                        FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITHOUT_STREET_POST_DIR= """
            SELECT a.UCRACCT_PREM_CODE, a.UCRACCT_CUST_CODE
                            FROM
                            ucracct a
                            join
                             ucbprem b
                             ON a.ucracct_prem_code=b.ucbprem_code
                             WHERE
                             b.ucbprem_pdir_code_post IS null
                             AND LENGTH(a.ucracct_cust_code) >= 5
                        FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITHOUT_STREET_NAME= """
            SELECT a.UCRACCT_PREM_CODE, a.UCRACCT_CUST_CODE
                            FROM
                            ucracct a
                            join
                             ucbprem b
                             ON a.ucracct_prem_code=b.ucbprem_code
                             WHERE
                             b.ucbprem_street_name IS NULL
                        FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITHOUT_STREET_NUMBER= """
            SELECT a.UCRACCT_PREM_CODE, a.UCRACCT_CUST_CODE
                            FROM
                            ucracct a
                            join
                             ucbprem b
                             ON a.ucracct_prem_code=b.ucbprem_code
                             WHERE
                             b.ucbprem_street_number IS null
                             AND LENGTH(a.ucracct_cust_code) >= 5
                        FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_STREET_PREDIRECTION= """
            SELECT a.UCRACCT_PREM_CODE, a.UCRACCT_CUST_CODE
                            FROM
                            ucracct a
                            join
                             ucbprem b
                             ON a.ucracct_prem_code=b.ucbprem_code
                             WHERE
                             b.ucbprem_pdir_code_pre IS NOT NULL\s
                             AND LENGTH(a.ucracct_cust_code) >= 5
                        FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITHOUT_STREET_PREDIRECTION= """
            SELECT a.UCRACCT_PREM_CODE, a.UCRACCT_CUST_CODE
                            FROM
                            ucracct a
                            join
                             ucbprem b
                             ON a.ucracct_prem_code=b.ucbprem_code
                             WHERE
                             b.ucbprem_pdir_code_pre IS NULL
                             AND LENGTH(a.ucracct_cust_code) >= 5
                             AND a.UCRACCT_CUST_CODE <> '97557'
                        FETCH FIRST 1 ROWS ONLY
            """;


    public static final String SELECT_ACTIVE_ACCOUNT_WITHOUT_NICKNAME= """
            SELECT
                UCRACCT_CUST_CODE,
                UCRACCT_PREM_CODE,
                UCRACCT_NICK_NAME
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
                 HAVING COUNT(*) >= 1
                 )
                        FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC151= """
            SELECT a.*
                FROM
                    ucracct a
                WHERE a.ucracct_cust_code IN (
            SELECT gzbemcp_cust_code
            FROM gzbemcp g
            WHERE g.gzbemcp_partner_ind='Y'
            GROUP BY gzbemcp_cust_code
            HAVING COUNT(*) = 1
            )
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC152= """
            SELECT a.*
                FROM
                    ucracct a
                WHERE a.ucracct_cust_code IN (
            SELECT gzbemcp_cust_code
            FROM gzbemcp g
            WHERE g.gzbemcp_partner_ind='N'
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
            SELECT a.*
                FROM
                    ucracct a
                WHERE a.ucracct_cust_code IN (
            SELECT gzbemcp_cust_code
            FROM gzbemcp g
            WHERE g.GZBEMCP_MARKETING_IND='Y'
            GROUP BY gzbemcp_cust_code
            HAVING COUNT(*) >= 1
            )
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC155= """
            SELECT a.*
                FROM
                    ucracct a
                WHERE a.ucracct_cust_code IN (
            SELECT gzbemcp_cust_code
            FROM gzbemcp g
            WHERE g.GZBEMCP_MARKETING_IND='N'
            GROUP BY gzbemcp_cust_code
            HAVING COUNT(*) >= 1
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
            HAVING COUNT(*) >= 1
            )
                                    FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC157= """
            SELECT a.*
                                                                                        FROM
                                                                                            ucracct a
                                                                                        WHERE a.ucracct_cust_code IN (
                                                                                    SELECT gzbemcp_cust_code
                                                                                    FROM gzbemcp g
                                                                                    WHERE g.gzbemcp_ocs_bill_notif= 'Y'
                                                                                    GROUP BY gzbemcp_cust_code
                                                                                    HAVING COUNT(*) = 1
                                                                                    )
                                                                                                            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC158= """
            SELECT a.*
                                                                                                                                   FROM ucracct a
                                                                                                                                   WHERE a.ucracct_cust_code IN (
                                                                                                                                       SELECT g.gzbemcp_cust_code
                                                                                                                                       FROM gzbemcp g
                                                                                                                                       WHERE g.gzbemcp_ocs_bill_notif = 'N'
                                                                                                                                         AND g.gzbemcp_effective_date <= SYSDATE
                                                                                                                                       GROUP BY g.gzbemcp_cust_code
                                                                                                                                       HAVING COUNT(*) = 1
                                                                                                                                   )
                                                                                                                                   ORDER BY a.ucracct_cust_code
                                                                                                                                   FETCH FIRST 1 ROWS ONLY
            
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC159= """
                SELECT a.*
                FROM ucracct a
                WHERE LENGTH(a.ucracct_cust_code) >= 4
                  AND a.ucracct_cust_code IN (
                        SELECT g.gzbemcp_cust_code
                        FROM gzbemcp g
                        WHERE g.gzbemcp_ocs_bill_notif IS NULL
                          AND g.gzbemcp_effective_date <= SYSDATE
                        GROUP BY g.gzbemcp_cust_code
                        HAVING COUNT(*) = 1
                  )
                ORDER BY a.ucracct_cust_code
                FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC160= """
            SELECT t.ucrtele_phone_area || t.ucrtele_phone_number AS phone_number,
            t.ucrtele_cust_code,
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
            SELECT  t.ucrtele_phone_area || t.ucrtele_phone_number AS phone_number, 
            t.ucrtele_cust_code,
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
            SELECT  t.ucrtele_phone_area || t.ucrtele_phone_number AS phone_number,
                t.ucrtele_cust_code,
                a.ucracct_prem_code
            FROM\s
                ucrtele t
            JOIN\s
                ucracct a
                ON t.ucrtele_cust_code = a.ucracct_cust_code
            WHERE\s
                t.ucrtele_tele_code = 'BI'
                AND t.ucrtele_primary_ind = 'Y'
                AND t.ucrtele_cust_code IN (
                    SELECT ucrtele_cust_code
                    FROM ucrtele
                    WHERE ucrtele_tele_code = 'BU'
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

    public static final String SELECT_ACCOUNT_WITH_ACTIVE_BANK_DRAFT= """
            SELECT
                a.ucracct_cust_code AS customer_code,
                a.ucracct_prem_code AS premises_code,
                a.ucracct_bank_last_four    AS bankDraftAccountNumberLast4,
                a.ucracct_draft_acct_status AS bankDraftStatus,
                CASE
                    WHEN a.ucracct_draft_acct_status = 'A'
                         AND b.utrbank_status = 'A'
                    THEN
                        '******' ||
                        SUBSTR(
                            LPAD(b.utrbank_transit_1, 4, '0') ||
                            LPAD(b.utrbank_transit_2, 4, '0') ||
                            b.utrbank_transit_3,
                            -4
                        )
                    ELSE ''
                END AS bankDraftRoutingNumber,
                a.ucracct_check_saving_ind AS bankDraftAccountType,
                NVL(c.ucbcust_last_name, '') AS bankName
            FROM UCRACCT a
            JOIN UTRBANK b
                ON a.ucracct_bank_code = b.utrbank_code
            JOIN UCBCUST c
                ON b.utrbank_cust_code_bank = c.ucbcust_cust_code
            WHERE a.ucracct_draft_acct_status = 'A'
              AND b.utrbank_status = 'A'
              AND a.ucracct_bank_acct IS NOT NULL
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_ACTIVE_BANK_DRAFT_NO_SERVICE= """
            SELECT
                a.ucracct_cust_code AS customer_code,
                a.ucracct_prem_code AS premises_code,
                a.ucracct_draft_acct_status AS bankDraftStatus,
                CASE
                    WHEN a.ucracct_draft_acct_status = 'A'
                         AND b.utrbank_status = 'A'
                    THEN
                        '******' ||
                        SUBSTR(
                            LPAD(b.utrbank_transit_1, 4, '0') ||
                            LPAD(b.utrbank_transit_2, 4, '0') ||
                            b.utrbank_transit_3,
                            -4
                        )
                    ELSE ''
                END AS bankDraftRoutingNumber,
                a.ucracct_check_saving_ind AS bankDraftAccountType,
                NVL(c.ucbcust_last_name, '') AS bankName
            FROM UCRACCT a
            JOIN UTRBANK b
                ON a.ucracct_bank_code = b.utrbank_code
            JOIN UCBCUST c
                ON b.utrbank_cust_code_bank = c.ucbcust_cust_code
            WHERE a.ucracct_draft_acct_status = 'A'
              AND b.utrbank_status = 'A'
              AND a.ucracct_status_ind= 'A'
              AND a.ucracct_bank_acct IS NOT NULL
              AND NOT EXISTS (
                    SELECT 1
                    FROM UCRSERV s
                    WHERE s.ucrserv_cust_code = a.ucracct_cust_code
                      AND s.ucrserv_prem_code = a.ucracct_prem_code
                )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_PRENOTIFICATION_BANK_DRAFT = """
            SELECT
                       a.ucracct_cust_code AS customer_code,
                       a.ucracct_prem_code AS premises_code,
                       a.ucracct_bank_last_four    AS bankDraftAccountNumberLast4,
                       a.ucracct_draft_acct_status AS bankDraftStatus,
                       CASE
                           WHEN a.ucracct_draft_acct_status = 'P'
                                AND b.utrbank_status = 'A'
                           THEN
                               '******' ||
                               SUBSTR(
                                   LPAD(b.utrbank_transit_1, 4, '0') ||
                                   LPAD(b.utrbank_transit_2, 4, '0') ||
                                   b.utrbank_transit_3,
                                   -4
                               )
                           ELSE ''
                       END AS bankDraftRoutingNumber,
                       a.ucracct_check_saving_ind AS bankDraftAccountType,
                       NVL(c.ucbcust_last_name, '') AS bankName
                   FROM UCRACCT a
                   JOIN UTRBANK b
                       ON a.ucracct_bank_code      = b.utrbank_code
                   JOIN UCBCUST c
                       ON b.utrbank_cust_code_bank = c.ucbcust_cust_code
                   JOIN UCBCUST cust
                       ON cust.ucbcust_cust_code   = a.ucracct_cust_code
                   WHERE a.ucracct_draft_acct_status = 'P'
                     AND b.utrbank_status            = 'A'
                     AND a.ucracct_bank_acct         IS NOT NULL
                     AND a.ucracct_status_ind        = 'A'
                     AND a.ucracct_pmnt_arr          != 'Y'
                     AND NOT EXISTS (
                         SELECT 1
                         FROM UARDRFT d
                         WHERE d.uardrft_cust_code       = a.ucracct_cust_code
                           AND d.uardrft_prem_code       = a.ucracct_prem_code
                           AND d.uardrft_hold_until_date > TRUNC(SYSDATE)
                           AND d.uardrft_amount          <> 0
                           AND d.uardrft_file_date       = DATE '2099-12-31'
                     )
                   FETCH FIRST 1 ROWS ONLY
        """;


    public static final String SELECT_ACCOUNT_WITH_CANCELLED_BANK_DRAFT= """
            SELECT
                a.ucracct_cust_code AS customer_code,
                a.ucracct_prem_code AS premises_code,
                a.ucracct_bank_last_four    AS bankDraftAccountNumberLast4,
                a.ucracct_draft_acct_status AS bankDraftStatus,
                CASE
                    WHEN a.ucracct_draft_acct_status = 'C'
                         AND b.utrbank_status = 'A'
                    THEN
                        '******' ||
                        SUBSTR(
                            LPAD(b.utrbank_transit_1, 4, '0') ||
                            LPAD(b.utrbank_transit_2, 4, '0') ||
                            b.utrbank_transit_3,
                            -4
                        )
                    ELSE ''
                END AS bankDraftRoutingNumber,
                a.ucracct_check_saving_ind AS bankDraftAccountType,
                NVL(c.ucbcust_last_name, '') AS bankName
            FROM UCRACCT a
            JOIN UTRBANK b
                ON a.ucracct_bank_code = b.utrbank_code
            JOIN UCBCUST c
                ON b.utrbank_cust_code_bank = c.ucbcust_cust_code
            WHERE a.ucracct_draft_acct_status = 'C'
              AND b.utrbank_status = 'A'
              AND a.ucracct_bank_acct IS NOT NULL
            FETCH FIRST 1 ROWS ONLY
            """;


    public static final String SELECT_ACCOUNT_WITH_INACTIVE_BANK_DRAFT= """
            SELECT
                a.ucracct_cust_code AS customer_code,
                a.ucracct_prem_code AS premises_code,
                a.ucracct_bank_last_four    AS bankDraftAccountNumberLast4,
                a.ucracct_draft_acct_status AS bankDraftStatus,
                CASE
                    WHEN a.ucracct_draft_acct_status = 'I'
                         AND b.utrbank_status = 'A'
                    THEN
                        '******' ||
                        SUBSTR(
                            LPAD(b.utrbank_transit_1, 4, '0') ||
                            LPAD(b.utrbank_transit_2, 4, '0') ||
                            b.utrbank_transit_3,
                            -4
                        )
                    ELSE ''
                END AS bankDraftRoutingNumber,
                a.ucracct_check_saving_ind AS bankDraftAccountType,
                NVL(c.ucbcust_last_name, '') AS bankName
            FROM UCRACCT a
            JOIN UTRBANK b
                ON a.ucracct_bank_code = b.utrbank_code
            JOIN UCBCUST c
                ON b.utrbank_cust_code_bank = c.ucbcust_cust_code
            WHERE a.ucracct_draft_acct_status = 'I'
              AND b.utrbank_status = 'A'
              AND a.ucracct_bank_acct IS NOT NULL
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_CHECKING_ACCOUNT= """
            SELECT
                a.ucracct_cust_code         AS customer_code,
                a.ucracct_prem_code         AS premises_code,
                a.ucracct_bank_last_four    AS bankDraftAccountNumberLast4,
                a.ucracct_draft_acct_status AS draft_status,
            
                -- Masked routing number
                '******' || SUBSTR(
                    REGEXP_REPLACE(
                        LPAD(b.utrbank_transit_1, 4, '0') ||
                        LPAD(b.utrbank_transit_2, 4, '0') ||
                             b.utrbank_transit_3,
                        '[^0-9]', ''
                    ),
                    -4
                ) AS masked_routing_number,
            
                a.ucracct_check_saving_ind  AS account_type,
                c.ucbcust_last_name         AS bank_name
            FROM
                UCRACCT a
            JOIN
                UTRBANK b
                    ON a.ucracct_bank_code = b.utrbank_code
            JOIN
                UCBCUST c
                    ON b.utrbank_cust_code_bank = c.ucbcust_cust_code
            WHERE
                a.ucracct_draft_acct_status IS NOT NULL
                AND b.utrbank_status = 'A'
                AND a.ucracct_bank_acct IS NOT NULL
                AND a.ucracct_check_saving_ind = 'C'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_CHECKING_ACCOUNT2= """
            SELECT
                a.ucracct_cust_code         AS customer_code,
                a.ucracct_prem_code         AS premises_code,
                a.ucracct_draft_acct_status AS draft_status,
                '******' || SUBSTR(
                    REGEXP_REPLACE(
                        LPAD(b.utrbank_transit_1, 4, '0') ||
                        LPAD(b.utrbank_transit_2, 4, '0') ||
                             b.utrbank_transit_3,
                        '[^0-9]', ''
                    ),
                    -4
                ) AS masked_routing_number,
                a.ucracct_check_saving_ind  AS account_type,
                c.ucbcust_last_name         AS bank_name
            FROM UCRACCT a
            JOIN UTRBANK b
                ON a.ucracct_bank_code = b.utrbank_code
            JOIN UCBCUST c
                ON b.utrbank_cust_code_bank = c.ucbcust_cust_code
            WHERE a.ucracct_draft_acct_status IS NOT NULL
              AND b.utrbank_status            = 'A'
              AND a.ucracct_bank_acct         IS NOT NULL
              AND a.ucracct_check_saving_ind  = 'C'
              AND a.ucracct_status_ind        = 'A'
              AND a.ucracct_pmnt_arr          != 'Y'                -- no active payment arrangement
              AND NOT EXISTS (
                  SELECT 1
                  FROM UARDRFT d
                  WHERE d.uardrft_cust_code      = a.ucracct_cust_code
                    AND d.uardrft_prem_code      = a.ucracct_prem_code
                    AND d.uardrft_hold_until_date > TRUNC(SYSDATE)  -- no future dated payment arrangement
                    AND d.uardrft_amount         <> 0
                    AND d.uardrft_file_date      = DATE '2099-12-31'
              )
              ORDER BY a.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
        """;



    public static final String SELECT_ACCOUNT_WITH_SAVINGS_ACCOUNT= """
            SELECT
                a.ucracct_cust_code         AS customer_code,
                a.ucracct_prem_code         AS premises_code,
                a.ucracct_bank_last_four    AS bankDraftAccountNumberLast4,
                a.ucracct_draft_acct_status AS draft_status,
            
                -- Masked routing number
                '******' || SUBSTR(
                    REGEXP_REPLACE(
                        LPAD(b.utrbank_transit_1, 4, '0') ||
                        LPAD(b.utrbank_transit_2, 4, '0') ||
                             b.utrbank_transit_3,
                        '[^0-9]', ''
                    ),
                    -4
                ) AS masked_routing_number,
            
                a.ucracct_check_saving_ind  AS account_type,
                c.ucbcust_last_name         AS bank_name
            FROM
                UCRACCT a
            JOIN
                UTRBANK b
                    ON a.ucracct_bank_code = b.utrbank_code
            JOIN
                UCBCUST c
                    ON b.utrbank_cust_code_bank = c.ucbcust_cust_code
            WHERE
                a.ucracct_draft_acct_status IS NOT NULL
                AND b.utrbank_status = 'A'
                AND a.ucracct_bank_acct IS NOT NULL
                AND a.ucracct_check_saving_ind = 'S'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_BANK_DETAILS= """
    SELECT TRIM(A.UTRBANK_CODE)                        BANK_CODE,
                       UPPER(TRIM(B.UCBCUST_LAST_NAME))            BANK_NAME,
                       (LPAD(A.UTRBANK_TRANSIT_1, 4, '0')
                       || LPAD(A.UTRBANK_TRANSIT_2, 4, '0')
                       || A.UTRBANK_CHECK_DIGIT)                   ROUTING_NUM
                FROM UTRBANK A,
                     UCBCUST B
                WHERE A.UTRBANK_STATUS       = 'A'
                  AND A.UTRBANK_CUST_CODE_BANK = B.UCBCUST_CUST_CODE
                ORDER BY DBMS_RANDOM.VALUE
                FETCH FIRST 1 ROWS ONLY
""";

    public static final String UPDATE_ACCOUNT_WITH_SAVINGS_ACCOUNT2= """
    UPDATE ucracct
    SET ucracct_draft_acct_status='A'
    WHERE ucracct_cust_code = '6212561'""";

    public static final String SELECT_ACCOUNT_WITH_SAVINGS_ACCOUNT2= """
            SELECT
                a.ucracct_cust_code         AS customer_code,
                a.ucracct_prem_code         AS premises_code,
                a.ucracct_draft_acct_status AS draft_status,
                -- Masked routing number
                '******' || SUBSTR(
                    REGEXP_REPLACE(
                        LPAD(b.utrbank_transit_1, 4, '0') ||
                        LPAD(b.utrbank_transit_2, 4, '0') ||
                             b.utrbank_transit_3,
                        '[^0-9]', ''
                    ),
                    -4
                ) AS masked_routing_number,
            
                a.ucracct_check_saving_ind  AS account_type,
                c.ucbcust_last_name         AS bank_name
            FROM
                UCRACCT a
            JOIN
                UTRBANK b
                    ON a.ucracct_bank_code = b.utrbank_code
            JOIN
                UCBCUST c
                    ON b.utrbank_cust_code_bank = c.ucbcust_cust_code
            WHERE
                a.ucracct_draft_acct_status IS NOT NULL
                AND b.utrbank_status = 'A'
                AND a.ucracct_bank_acct IS NOT NULL
                AND a.ucracct_check_saving_ind = 'S'
                AND a.ucracct_status_ind='A'
                AND a.ucracct_draft_acct_status='A'
                AND a.ucracct_cust_code = '6212561'
                 AND EXISTS (
                    SELECT 1
                    FROM UCRSERV s
                    WHERE s.ucrserv_cust_code = a.ucracct_cust_code
                      AND s.ucrserv_prem_code = a.ucracct_prem_code
                )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_BANK_NAME= """
            SELECT
                a.ucracct_cust_code         AS customer_code,
                a.ucracct_prem_code         AS premises_code,
                a.ucracct_bank_last_four    AS bankDraftAccountNumberLast4,
                a.ucracct_draft_acct_status AS draft_status,
            
                -- Masked routing number
                '******' || SUBSTR(
                    REGEXP_REPLACE(
                        LPAD(b.utrbank_transit_1, 4, '0') ||
                        LPAD(b.utrbank_transit_2, 4, '0') ||
                             b.utrbank_transit_3,
                        '[^0-9]', ''
                    ),
                    -4
                ) AS masked_routing_number,
            
                a.ucracct_check_saving_ind  AS account_type,
                c.ucbcust_last_name         AS bank_name
            FROM
                UCRACCT a
            JOIN
                UTRBANK b
                    ON a.ucracct_bank_code = b.utrbank_code
            JOIN
                UCBCUST c
                    ON b.utrbank_cust_code_bank = c.ucbcust_cust_code
            WHERE
                a.ucracct_draft_acct_status IS NOT NULL
                AND b.utrbank_status = 'A'
                AND a.ucracct_bank_acct IS NOT NULL
                AND c.ucbcust_last_name IS NOT NULL
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_BANK_DRAFT_AND_ROUTING_NUMBER= """
            SELECT
                a.ucracct_cust_code AS customer_code,
                a.ucracct_prem_code AS premises_code,
                a.ucracct_bank_last_four    AS bankDraftAccountNumberLast4,
                a.ucracct_draft_acct_status AS bankDraftStatus,
                CASE
                    WHEN a.ucracct_draft_acct_status IS NOT NULL
                         AND b.utrbank_status = 'A'
                    THEN
                        '******' ||
                        SUBSTR(
                            LPAD(b.utrbank_transit_1, 4, '0') ||
                            LPAD(b.utrbank_transit_2, 4, '0') ||
                            b.utrbank_transit_3,
                            -4
                        )
                    ELSE ''
                END AS bankDraftRoutingNumber,
                a.ucracct_check_saving_ind AS bankDraftAccountType,
                NVL(c.ucbcust_last_name, '') AS bankName
            FROM UCRACCT a
            JOIN UTRBANK b
                ON a.ucracct_bank_code = b.utrbank_code
            JOIN UCBCUST c
                ON b.utrbank_cust_code_bank = c.ucbcust_cust_code
            WHERE a.ucracct_draft_acct_status IS NOT NULL
              AND b.utrbank_status = 'A'
              AND (
                    b.utrbank_transit_1 IS NOT NULL
                 OR b.utrbank_transit_2 IS NOT NULL
                 OR b.utrbank_transit_3 IS NOT NULL
                  )
              AND a.ucracct_bank_acct IS NOT NULL
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_BANK_DRAFT_AND_ACCOUNT_NUMBER= """
            SELECT
                a.ucracct_cust_code AS customer_code,
                a.ucracct_prem_code AS premises_code,
                a.ucracct_bank_last_four    AS bankDraftAccountNumberLast4,
                a.ucracct_draft_acct_status AS bankDraftStatus,
                CASE
                    WHEN a.ucracct_draft_acct_status IS NOT NULL
                         AND b.utrbank_status = 'A'
                    THEN
                        '******' ||
                        SUBSTR(
                            LPAD(b.utrbank_transit_1, 4, '0') ||
                            LPAD(b.utrbank_transit_2, 4, '0') ||
                            b.utrbank_transit_3,
                            -4
                        )
                    ELSE ''
                END AS bankDraftRoutingNumber,
                a.ucracct_check_saving_ind AS bankDraftAccountType,
                NVL(c.ucbcust_last_name, '') AS bankName
            FROM UCRACCT a
            JOIN UTRBANK b
                ON a.ucracct_bank_code = b.utrbank_code
            JOIN UCBCUST c
                ON b.utrbank_cust_code_bank = c.ucbcust_cust_code
            WHERE a.ucracct_draft_acct_status IS NOT NULL
              AND b.utrbank_status = 'A'
              AND a.ucracct_bank_acct IS NOT NULL
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITHOUT_BANK_DRAFT= """
            SELECT\s
                a.ucracct_cust_code AS customer_code,
                a.ucracct_prem_code AS premises_code,
                NULL AS bankDraftStatus,
                ''   AS bankDraftRoutingNumber,
                ''   AS bankDraftAccountNumber,
                NULL AS bankDraftAccountType,
                ''   AS bankName
            FROM UCRACCT a
            LEFT JOIN UTRBANK b\s
                ON a.ucracct_bank_code = b.utrbank_code
            WHERE a.ucracct_draft_acct_status IS NULL
              AND a.ucracct_status_ind= 'A'
              AND (b.utrbank_status IS NULL OR b.utrbank_status <> 'A')
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITHOUT_BANK_DRAFT2= """
            SELECT\s
                a.ucracct_cust_code AS customer_code,
                a.ucracct_prem_code AS premises_code,
                NULL AS bankDraftStatus,
                ''   AS bankDraftRoutingNumber,
                ''   AS bankDraftAccountNumber,
                NULL AS bankDraftAccountType,
                ''   AS bankName
            FROM UCRACCT a
            LEFT JOIN UTRBANK b\s
                ON a.ucracct_bank_code = b.utrbank_code
            WHERE a.ucracct_draft_acct_status IS NULL
              AND b.utrbank_status <> 'A'
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

    public static final String SELECT_LAST_NAME_AND_SSN= """
            SELECT
                a.ucracct_cust_code,
                a.ucracct_prem_code,
                s.ucrserv_scls_code,
                a.ucracct_status_ind,
                a.ucracct_nick_name,
                c.ucbcust_first_name,
                c.ucbcust_last_name,
                c.ucbcust_ssn_last_four,
                p.ucbprem_street_name,
                p.ucbprem_street_number,
                p.ucbprem_pdir_code_pre,
                p.ucbprem_ssfx_code,
                p.ucbprem_pdir_code_post,
                p.ucbprem_utyp_code,
                p.ucbprem_unit,
                p.ucbprem_city,
                p.ucbprem_stat_code_addr,
                p.ucbprem_zipc_code
            FROM UCRACCT a
            JOIN UCRSERV s
                ON a.ucracct_prem_code = s.ucrserv_prem_code
            JOIN UCBCUST c
                ON a.ucracct_cust_code = c.ucbcust_cust_code
            JOIN UCBPREM p
                ON a.ucracct_prem_code = p.ucbprem_code
            WHERE c.ucbcust_last_name IS NOT NULL
              AND c.ucbcust_ssn_last_four IS NOT NULL
            ORDER BY DBMS_RANDOM.VALUE
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_INFO= """
            SELECT
                a.ucracct_cust_code,
                a.ucracct_prem_code,
                s.ucrserv_scls_code,
                a.ucracct_status_ind,
                a.ucracct_nick_name,
                c.ucbcust_first_name,
                c.ucbcust_last_name,
                c.ucbcust_ssn_last_four,
                p.ucbprem_street_name,
                p.ucbprem_street_number,
                p.ucbprem_pdir_code_pre,
                p.ucbprem_ssfx_code,
                p.ucbprem_pdir_code_post,
                p.ucbprem_utyp_code,
                p.ucbprem_unit,
                p.ucbprem_city,
                p.ucbprem_stat_code_addr,
                p.ucbprem_zipc_code
            FROM UCRACCT a
            JOIN UCRSERV s
                ON a.ucracct_prem_code = s.ucrserv_prem_code
            JOIN UCBCUST c
                ON a.ucracct_cust_code = c.ucbcust_cust_code
            JOIN UCBPREM p
                ON a.ucracct_prem_code = p.ucbprem_code
                WHERE a.ucracct_cust_code = ?
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_INFO2= """
            SELECT
                a.ucracct_cust_code,
                a.ucracct_prem_code,
                a.ucracct_status_ind,
                a.ucracct_nick_name,
                c.ucbcust_first_name,
                c.ucbcust_last_name,
                c.ucbcust_ssn_last_four,
                p.ucbprem_street_name,
                p.ucbprem_street_number,
                p.ucbprem_pdir_code_pre,
                p.ucbprem_ssfx_code,
                p.ucbprem_pdir_code_post,
                p.ucbprem_utyp_code,
                p.ucbprem_unit,
                p.ucbprem_city,
                p.ucbprem_stat_code_addr,
                p.ucbprem_zipc_code
            FROM UCRACCT a
            JOIN UCBCUST c
                ON a.ucracct_cust_code = c.ucbcust_cust_code
            JOIN UCBPREM p
                ON a.ucracct_prem_code = p.ucbprem_code
                WHERE a.ucracct_cust_code = ?
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_MATCHING_ACCOUNTS= """
            SELECT
                a.ucracct_cust_code,
                a.ucracct_prem_code,
                a.ucracct_status_ind,
                a.ucracct_nick_name,
                c.ucbcust_first_name,
                c.ucbcust_last_name,
                c.ucbcust_ssn_last_four,
                p.ucbprem_street_name,
                p.ucbprem_street_number,
                p.ucbprem_pdir_code_pre,
                p.ucbprem_ssfx_code,
                p.ucbprem_pdir_code_post,
                p.ucbprem_utyp_code,
                p.ucbprem_unit,
                p.ucbprem_city,
                p.ucbprem_stat_code_addr,
                p.ucbprem_zipc_code
            FROM UCRACCT a
            JOIN UCBCUST c
                ON a.ucracct_cust_code = c.ucbcust_cust_code
            JOIN UCBPREM p
                ON a.ucracct_prem_code = p.ucbprem_code
                WHERE c.ucbcust_last_name = ?
            """;

    public static final String SELECT_EMAIL_AND_SSN= """
            SELECT
                a.ucracct_cust_code,
                a.ucracct_prem_code,
                s.ucrserv_scls_code,
                a.ucracct_status_ind,
                a.ucracct_nick_name,
                c.ucbcust_first_name,
                c.ucbcust_last_name,
                c.ucbcust_ssn_last_four,
                p.ucbprem_street_name,
                p.ucbprem_street_number,
                p.ucbprem_pdir_code_pre,
                p.ucbprem_ssfx_code,
                p.ucbprem_pdir_code_post,
                p.ucbprem_utyp_code,
                p.ucbprem_unit,
                p.ucbprem_city,
                p.ucbprem_stat_code_addr,
                p.ucbprem_zipc_code,
                d.gzbemcp_email_addr
            FROM UCRACCT a
            JOIN UCRSERV s
                ON a.ucracct_prem_code = s.ucrserv_prem_code
            JOIN UCBCUST c
                ON a.ucracct_cust_code = c.ucbcust_cust_code
            JOIN gzbemcp d
                ON d.gzbemcp_cust_code = c.ucbcust_cust_code
            JOIN UCBPREM p
                ON a.ucracct_prem_code = p.ucbprem_code
            WHERE d.gzbemcp_email_addr IS NOT NULL
              AND c.ucbcust_ssn_last_four IS NOT NULL
            ORDER BY DBMS_RANDOM.VALUE
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_PHONE_AND_SSN= """
            SELECT
                a.ucracct_cust_code,
                a.ucracct_prem_code,
                s.ucrserv_scls_code,
                a.ucracct_status_ind,
                a.ucracct_nick_name,
                c.ucbcust_first_name,
                c.ucbcust_last_name,
                c.ucbcust_ssn_last_four,
                p.ucbprem_street_name,
                p.ucbprem_street_number,
                p.ucbprem_pdir_code_pre,
                p.ucbprem_ssfx_code,
                p.ucbprem_pdir_code_post,
                p.ucbprem_utyp_code,
                p.ucbprem_unit,
                p.ucbprem_city,
                p.ucbprem_stat_code_addr,
                p.ucbprem_zipc_code,
                d.ucrtele_phone_area,
                d.ucrtele_phone_number
            FROM UCRACCT a
            JOIN UCRSERV s  ON a.ucracct_prem_code = s.ucrserv_prem_code
            JOIN UCBCUST c  ON a.ucracct_cust_code = c.ucbcust_cust_code
            JOIN ucrtele d  ON d.ucrtele_cust_code = c.ucbcust_cust_code
            JOIN UCBPREM p  ON a.ucracct_prem_code = p.ucbprem_code
            WHERE d.ucrtele_phone_number IS NOT NULL
              AND c.ucbcust_ssn_last_four IS NOT NULL
              AND a.ucracct_status_ind = 'A'
            ORDER BY DBMS_RANDOM.VALUE
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String DELETE_REGISTERED_ACCOUNT= """
            DELETE
            FROM custadv_registered_accounts
            WHERE account_number LIKE CONCAT('%', ?, '%')
            """;


    public static final String UPDATE_MAX_LIMIT= """
            update uzrpsto
            set uzrpsto_parm_value = 100
            where uzrpsto_object = 'SPK_ACCT_SEARCH_UTIL'
            and uzrpsto_parm_name = 'MAX_SEARCH_COUNT'
            """;


    public static final String UPDATE_MAX_LIMIT2= """
            update uzrpsto
            set uzrpsto_parm_value = 25000
            where uzrpsto_object = 'SPK_ACCT_SEARCH_UTIL'
            and uzrpsto_parm_name = 'MAX_SEARCH_COUNT'
            """;

    /** Banner non-prod test email override (UAT1 typically uattestemail@test.com). */
    public static final String SELECT_BANNER_NEW_TEST_EMAIL_ADDR = """
            SELECT uzrpsto_parm_value
            FROM uzrpsto
            WHERE uzrpsto_object = 'WHTCNTS_TEST_WEB_SERV_ACCESS'
              AND uzrpsto_parm_name = 'NEW_TEST_EMAIL_ADDR'
            """;

    /**
     * Oracle treats {@code ''} as NULL (ORA-01407). Use a non-empty sentinel so Banner has no
     * usable test-email override (same effect as "removing" the configured address).
     */
    public static final String CLEAR_BANNER_NEW_TEST_EMAIL_ADDR = """
            UPDATE uzrpsto
            SET uzrpsto_parm_value = ' '
            WHERE uzrpsto_object IN ( 'WHTCNTS_TEST_WEB_SERV_ACCESS','UZPSEND')
              AND uzrpsto_parm_name In ( 'NEW_TEST_EMAIL_ADDR', 'TEST_EMAIL_ADDR')
            """;

    public static final String RESTORE_BANNER_NEW_TEST_EMAIL_ADDR = """
            UPDATE uzrpsto
            SET uzrpsto_parm_value = ?
            WHERE uzrpsto_object IN ( 'WHTCNTS_TEST_WEB_SERV_ACCESS','UZPSEND')
              AND uzrpsto_parm_name In ( 'NEW_TEST_EMAIL_ADDR', 'TEST_EMAIL_ADDR')
            """;

    /**
     * TC_131/132: hold GZBEMCP email row(s) with FOR UPDATE so Banner preference/confirmation-date
     * update cannot complete (→ 40293). Release via connection rollback/close.
     */
    public static final String LOCK_UCRACCT_ROW_FOR_UPDATE = """
            SELECT GZBEMCP_CUST_CODE, GZBEMCP_EMAIL_ADDR
            FROM GZBEMCP
            WHERE GZBEMCP_CUST_CODE = ?
            FOR UPDATE
            """;

    /** Alternate/unused: snapshot GZBEMCP rows before delete (restore after Confirm). */
    public static final String SELECT_GZBEMCP_ROWS_FOR_CUSTOMER = """
            SELECT *
            FROM GZBEMCP
            WHERE GZBEMCP_CUST_CODE = ?
            """;

    /** Alternate/unused: delete GZBEMCP path (returns 10413 on Confirm, not 40293). */
    public static final String DELETE_GZBEMCP_ROWS_FOR_CUSTOMER = """
            DELETE FROM GZBEMCP
            WHERE GZBEMCP_CUST_CODE = ?
            """;

    public static final String UPDATE_PASSWORD= """
            update users
            set password='UAT2@CustomerPass'
            where user_name=?
            """;

    public static final String SELECT_ACCOUNT_DETAILS_REQUIRED= """
            SELECT
                u.user_name,
                ra.account_number
            FROM
                users u
            JOIN
                custadv_registered_accounts ra
                    ON u.user_id = ra.user_id
            WHERE  u.domain_id = 2
                AND u.user_name='zzbookie223'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_REQUIRED3= """
            SELECT
                u.user_name,
                ra.account_number
            FROM
                users u
            JOIN
                custadv_registered_accounts ra
                    ON u.user_id = ra.user_id
            WHERE  u.domain_id = 2
                AND u.user_name='TestingUsr92'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_REQUIRED2= """
            SELECT
                u.user_name,
                ra.account_number
            FROM
                users u
            JOIN
                custadv_registered_accounts ra
                    ON u.user_id = ra.user_id
            WHERE  u.domain_id = 2
                AND u.user_name='TestingUsr91'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_LAST_NAME_FOR_CUST_CODE= """
        SELECT UCBCUST_CUST_CODE, UCBCUST_LAST_NAME FROM UCBCUST
        WHERE UCBCUST_CUST_CODE= ?
""";

    public static final String UPDATE_ACCOUNT_NO= """
            UPDATE ucracct
            SET ucracct_bank_acct= ?
            WHERE ucracct_cust_code = ?
            AND ucracct_prem_code = ?
            """;

    public static final String SELECT_ACCOUNT_NUMBER= """
            SELECT u.user_name, ra.account_number
                        FROM users u
                        JOIN custadv_registered_accounts ra
                            ON u.user_id = ra.user_id
                        WHERE u.user_name ='testing1234'
                        AND u.domain_id = 2
                        AND u.user_name REGEXP '^[A-Za-z0-9]+$'
                        FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_PHONE_NUMBER_FROM_DB= """
            SELECT
                a.ucracct_cust_code,
                a.ucracct_prem_code,
                s.ucrserv_scls_code,
                a.ucracct_status_ind,
                a.ucracct_nick_name,
                c.ucbcust_first_name,
                c.ucbcust_last_name,
                c.ucbcust_ssn_last_four,
                p.ucbprem_street_name,
                p.ucbprem_street_number,
                p.ucbprem_pdir_code_pre,
                p.ucbprem_ssfx_code,
                p.ucbprem_pdir_code_post,
                p.ucbprem_utyp_code,
                p.ucbprem_unit,
                p.ucbprem_city,
                p.ucbprem_stat_code_addr,
                p.ucbprem_zipc_code,
                t.ucrtele_phone_area || t.ucrtele_phone_number AS phone_number,
                t.ucrtele_tele_code,
                t.ucrtele_primary_ind
            FROM UCRACCT a
            JOIN UCRSERV s
                ON a.ucracct_prem_code = s.ucrserv_prem_code
            JOIN UCBCUST c
                ON a.ucracct_cust_code = c.ucbcust_cust_code
            JOIN UCBPREM p
                ON a.ucracct_prem_code = p.ucbprem_code
            JOIN UCRTELE t
                ON t.ucrtele_cust_code = a.ucracct_cust_code
            WHERE t.ucrtele_primary_ind = 'Y'
              AND t.ucrtele_cust_code = '4955074'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_COUNT_OF_RECORDS= """
            SELECT COUNT(*)
            FROM ucbcust
            JOIN ucracct
              ON ucracct.ucracct_cust_code = ucbcust.ucbcust_cust_code
            WHERE ucbcust.ucbcust_last_name = 'MANE PRIORITY'
            """;

    public static final String SELECT_EMAIL_FOR_CUST_CODE= """
         SELECT
                                    a.ucracct_cust_code,
                                    a.ucracct_prem_code,
                                    s.ucrserv_scls_code,
                                    a.ucracct_status_ind,
                                    a.ucracct_nick_name,
                                    c.ucbcust_first_name,
                                    c.ucbcust_last_name,
                                    c.ucbcust_ssn_last_four,
                                    p.ucbprem_street_name,
                                    p.ucbprem_street_number,
                                    p.ucbprem_pdir_code_pre,
                                    p.ucbprem_ssfx_code,
                                    p.ucbprem_pdir_code_post,
                                    p.ucbprem_utyp_code,
                                    p.ucbprem_unit,
                                    p.ucbprem_city,
                                    p.ucbprem_stat_code_addr,
                                    p.ucbprem_zipc_code,
                                    d.gzbemcp_email_addr
                                FROM UCRACCT a
                                JOIN UCRSERV s
                                    ON a.ucracct_prem_code = s.ucrserv_prem_code
                                JOIN UCBCUST c
                                    ON a.ucracct_cust_code = c.ucbcust_cust_code
                                JOIN gzbemcp d
                                    ON d.gzbemcp_cust_code = c.ucbcust_cust_code
                                JOIN UCBPREM p
                                    ON a.ucracct_prem_code = p.ucbprem_code
                                    WHERE a.ucracct_cust_code= ?
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
            WHERE c.ucrscmp_end_date > SYSDATE
              AND NVL(p.uztppuc_rollover, 'N') = 'N'
              AND c.ucrscmp_plan_code = 'CFM'
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
            WHERE a.ucracct_cust_code = '5814879'
              AND a.ucracct_prem_code = '5789992'
              AND c.ucrscmp_end_date > SYSDATE
              AND p.uztppuc_rollover = 'Y'
            FETCH FIRST 1 ROWS ONLY
    """;

    public static final String SELECT_PLAN_ROLLOVER_INDICATOR = """
            SELECT NVL(uztppuc_rollover, 'N') AS uztppuc_rollover
            FROM uztppuc
            WHERE uztppuc_plan_code = ?
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
              AND NVL(p.uztppuc_restrict_ind, 'N') = 'N'
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
            SELECT
                t1.ucracct_cust_code,
                t1.ucracct_prem_code,
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
                        AND ucrscmp_scty_code IN ('PPTDISC', 'FLATDISC', 'CSCDISC')
                    GROUP BY
                        ucrscmp_cust_code,
                        ucrscmp_prem_code
                    HAVING COUNT(DISTINCT ucrscmp_scty_code) > 1   -- 2+ different discount types
                )
            ORDER BY
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

    public static final String SELECT_ACCOUNT_DETAILS_TC1 = """
            SELECT
                t1.ucracct_cust_code,
                t1.ucracct_prem_code,
                t3.ucrserv_scls_code,
                t4.ucrscmp_plan_code
            FROM
                ucracct t1
            JOIN
                ucbcust t2 ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN
                ucrserv t3 ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                           AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN
                ucrscmp t4 ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                           AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_scty_code = 'COMM'
                AND TRUNC(SYSDATE) BETWEEN t4.ucrscmp_start_date AND t4.ucrscmp_end_date
            ORDER BY
                t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC2 = """
            SELECT
                ucracct_cust_code,
                ucracct_prem_code,
                ucrserv_scls_code,
                ucrscmp_plan_code
            FROM (
                SELECT
                    t1.ucracct_cust_code,
                    t1.ucracct_prem_code,
                    t3.ucrserv_scls_code,
                    t4.ucrscmp_plan_code,
                    1 AS pick_priority
                FROM
                    ucracct t1
                JOIN
                    ucbcust t2 ON t1.ucracct_cust_code = t2.ucbcust_cust_code
                JOIN
                    ucrserv t3 ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                               AND t1.ucracct_prem_code = t3.ucrserv_prem_code
                JOIN
                    ucrscmp t4 ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                               AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
                WHERE
                    t1.ucracct_status_ind = 'A'
                    AND t1.ucracct_cycl_code NOT IN ('DEPO')
                    AND t3.ucrserv_scls_code = 'RS'
                    AND t4.ucrscmp_scty_code = 'COMM'
                    AND t4.ucrscmp_plan_code IN ('GPP', '12M', '18M', '24M', 'FIX', 'CFM')
                    AND TRUNC(SYSDATE) BETWEEN t4.ucrscmp_start_date AND t4.ucrscmp_end_date
                    AND t4.ucrscmp_end_date > TRUNC(SYSDATE) + 1
                    AND t4.ucrscmp_end_date <= TRUNC(SYSDATE) + 45

                UNION ALL

                SELECT
                    t1.ucracct_cust_code,
                    t1.ucracct_prem_code,
                    t3.ucrserv_scls_code,
                    t4.ucrscmp_plan_code,
                    2 AS pick_priority
                FROM
                    ucracct t1
                JOIN
                    ucbcust t2 ON t1.ucracct_cust_code = t2.ucbcust_cust_code
                JOIN
                    ucrserv t3 ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                               AND t1.ucracct_prem_code = t3.ucrserv_prem_code
                JOIN
                    ucrscmp t4 ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                               AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
                WHERE
                    t1.ucracct_cust_code = '5595532'
                    AND t1.ucracct_prem_code = '5571377'
                    AND t1.ucracct_status_ind = 'A'
                    AND t3.ucrserv_scls_code = 'RS'
                    AND t4.ucrscmp_scty_code = 'COMM'
                    AND TRUNC(SYSDATE) BETWEEN t4.ucrscmp_start_date AND t4.ucrscmp_end_date

                UNION ALL

                SELECT
                    t1.ucracct_cust_code,
                    t1.ucracct_prem_code,
                    t3.ucrserv_scls_code,
                    t4.ucrscmp_plan_code,
                    3 AS pick_priority
                FROM
                    ucracct t1
                JOIN
                    ucbcust t2 ON t1.ucracct_cust_code = t2.ucbcust_cust_code
                JOIN
                    ucrserv t3 ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                               AND t1.ucracct_prem_code = t3.ucrserv_prem_code
                JOIN
                    ucrscmp t4 ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                               AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
                WHERE
                    t1.ucracct_cust_code = '6114996'
                    AND t1.ucracct_prem_code = '6088083'
                    AND t1.ucracct_status_ind = 'A'
                    AND t3.ucrserv_scls_code = 'RS'
                    AND t4.ucrscmp_scty_code = 'COMM'
                    AND TRUNC(SYSDATE) BETWEEN t4.ucrscmp_start_date AND t4.ucrscmp_end_date
            )
            ORDER BY
                pick_priority,
                ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** TC_2 candidates for API probing planRenewalWindowIndicator = Y. */
    public static final String SELECT_ACCOUNT_DETAILS_TC2_CANDIDATES = """
            SELECT
                ucracct_cust_code,
                ucracct_prem_code,
                ucrserv_scls_code,
                ucrscmp_plan_code,
                pick_priority
            FROM (
                SELECT
                    t1.ucracct_cust_code,
                    t1.ucracct_prem_code,
                    t3.ucrserv_scls_code,
                    t4.ucrscmp_plan_code,
                    1 AS pick_priority
                FROM
                    ucracct t1
                JOIN
                    ucbcust t2 ON t1.ucracct_cust_code = t2.ucbcust_cust_code
                JOIN
                    ucrserv t3 ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                               AND t1.ucracct_prem_code = t3.ucrserv_prem_code
                JOIN
                    ucrscmp t4 ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                               AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
                WHERE
                    t1.ucracct_status_ind = 'A'
                    AND t1.ucracct_cycl_code NOT IN ('DEPO')
                    AND t3.ucrserv_scls_code = 'RS'
                    AND t4.ucrscmp_scty_code = 'COMM'
                    AND t4.ucrscmp_plan_code IN ('GPP', '12M', '18M', '24M', 'FIX', 'CFM')
                    AND TRUNC(SYSDATE) BETWEEN t4.ucrscmp_start_date AND t4.ucrscmp_end_date
                    AND t4.ucrscmp_end_date > TRUNC(SYSDATE) + 1
                    AND t4.ucrscmp_end_date <= TRUNC(SYSDATE) + 60

                UNION ALL

                SELECT
                    t1.ucracct_cust_code,
                    t1.ucracct_prem_code,
                    t3.ucrserv_scls_code,
                    t4.ucrscmp_plan_code,
                    2 AS pick_priority
                FROM
                    ucracct t1
                JOIN
                    ucrserv t3 ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                               AND t1.ucracct_prem_code = t3.ucrserv_prem_code
                JOIN
                    ucrscmp t4 ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                               AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
                WHERE
                    ((t1.ucracct_cust_code = '5595532' AND t1.ucracct_prem_code = '5571377')
                      OR (t1.ucracct_cust_code = '6114996' AND t1.ucracct_prem_code = '6088083'))
                    AND t1.ucracct_status_ind = 'A'
                    AND t3.ucrserv_scls_code = 'RS'
                    AND t4.ucrscmp_scty_code = 'COMM'
                    AND TRUNC(SYSDATE) BETWEEN t4.ucrscmp_start_date AND t4.ucrscmp_end_date
            )
            ORDER BY
                pick_priority,
                ucracct_cust_code DESC
            FETCH FIRST 40 ROWS ONLY
            """;

    /** TC_3 candidates for API probing planRenewalWindowIndicator = N. */
    public static final String SELECT_ACCOUNT_DETAILS_TC3_CANDIDATES = """
            SELECT
                t1.ucracct_cust_code,
                t1.ucracct_prem_code,
                t3.ucrserv_scls_code,
                t4.ucrscmp_plan_code
            FROM
                ucracct t1
            JOIN
                ucbcust t2 ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN
                ucrserv t3 ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                           AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN
                ucrscmp t4 ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                           AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_plan_code IN ('RGB', 'GB6', 'CGB')
                AND t4.ucrscmp_scty_code = 'COMM'
                AND TRUNC(SYSDATE) BETWEEN t4.ucrscmp_start_date AND t4.ucrscmp_end_date
            ORDER BY
                t1.ucracct_cust_code DESC
            FETCH FIRST 20 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC3 = """
            SELECT
                t1.ucracct_cust_code,
                t1.ucracct_prem_code,
                t3.ucrserv_scls_code,
                t4.ucrscmp_plan_code
            FROM
                ucracct t1
            JOIN
                ucbcust t2 ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN
                ucrserv t3 ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                           AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN
                ucrscmp t4 ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                           AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_plan_code IN ('RGB', 'GB6', 'CGB')
                AND t4.ucrscmp_scty_code = 'COMM'
                AND TRUNC(SYSDATE) BETWEEN t4.ucrscmp_start_date AND t4.ucrscmp_end_date
            ORDER BY
                t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** TC_4 candidates for API probing planRenewalWindowIndicator = -. */
    public static final String SELECT_ACCOUNT_DETAILS_TC4_CANDIDATES = """
            SELECT
                t1.ucracct_cust_code,
                t1.ucracct_prem_code,
                t3.ucrserv_scls_code,
                t4.ucrscmp_plan_code
            FROM
                ucracct t1
            JOIN
                ucbcust t2 ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN
                ucrserv t3 ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                           AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN
                ucrscmp t4 ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                           AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_plan_code IN ('MVS', '24M', '24B', 'FIX')
                AND t4.ucrscmp_scty_code = 'COMM'
                AND TRUNC(SYSDATE) BETWEEN t4.ucrscmp_start_date AND t4.ucrscmp_end_date
                AND NOT EXISTS (
                    SELECT 1
                    FROM ucrscmp other
                    WHERE other.ucrscmp_cust_code = t1.ucracct_cust_code
                      AND other.ucrscmp_prem_code = t1.ucracct_prem_code
                      AND other.ucrscmp_scty_code = 'COMM'
                      AND TRUNC(SYSDATE) BETWEEN other.ucrscmp_start_date AND other.ucrscmp_end_date
                      AND other.ucrscmp_plan_code NOT IN ('MVS', '24M', '24B', 'FIX')
                )
            ORDER BY
                t1.ucracct_cust_code DESC
            FETCH FIRST 20 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC4 = """
            SELECT
                t1.ucracct_cust_code,
                t1.ucracct_prem_code,
                t3.ucrserv_scls_code,
                t4.ucrscmp_plan_code
            FROM
                ucracct t1
            JOIN
                ucbcust t2 ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN
                ucrserv t3 ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                           AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN
                ucrscmp t4 ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                           AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_plan_code IN ('MVS', '24M', '24B', 'FIX')
                AND t4.ucrscmp_scty_code = 'COMM'
                AND TRUNC(SYSDATE) BETWEEN t4.ucrscmp_start_date AND t4.ucrscmp_end_date
                AND NOT EXISTS (
                    SELECT 1
                    FROM ucrscmp other
                    WHERE other.ucrscmp_cust_code = t1.ucracct_cust_code
                      AND other.ucrscmp_prem_code = t1.ucracct_prem_code
                      AND other.ucrscmp_scty_code = 'COMM'
                      AND TRUNC(SYSDATE) BETWEEN other.ucrscmp_start_date AND other.ucrscmp_end_date
                      AND other.ucrscmp_plan_code NOT IN ('MVS', '24M', '24B', 'FIX')
                )
            ORDER BY
                t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** TC_5 candidates: Guaranteed + discount + PRICEPRO (prefer non-transferable). */
    public static final String SELECT_ACCOUNT_DETAILS_TC5_CANDIDATES = """
            SELECT
                t1.ucracct_cust_code,
                t1.ucracct_prem_code,
                t3.ucrserv_scls_code,
                t4.ucrscmp_scty_code,
                t4.ucrscmp_plan_code
            FROM
                ucracct t1
            JOIN
                ucbcust t2 ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN
                ucrserv t3 ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                           AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN
                ucrscmp t4 ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                           AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_plan_code IN ('RGB', 'CGB', 'GB6', 'PGB')
                AND t4.ucrscmp_scty_code = 'COMM'
                AND TRUNC(SYSDATE) BETWEEN t4.ucrscmp_start_date AND t4.ucrscmp_end_date
                AND EXISTS (
                    SELECT 1
                    FROM ucrscmp disc
                    WHERE disc.ucrscmp_cust_code = t1.ucracct_cust_code
                      AND disc.ucrscmp_prem_code = t1.ucracct_prem_code
                      AND disc.ucrscmp_end_date > SYSDATE
                      AND disc.ucrscmp_scty_code IN ('PPTDISC', 'FLATDISC', 'CSCDISC')
                )
                AND EXISTS (
                    SELECT 1
                    FROM ucrscmp pp
                    WHERE pp.ucrscmp_cust_code = t1.ucracct_cust_code
                      AND pp.ucrscmp_prem_code = t1.ucracct_prem_code
                      AND pp.ucrscmp_end_date > SYSDATE
                      AND pp.ucrscmp_scty_code = 'PRICEPRO'
                )
            ORDER BY
                t1.ucracct_cust_code DESC
            FETCH FIRST 20 ROWS ONLY
            """;

    /**
     * TC_5 primary: Guaranteed Bill plan with an active discount and PRICEPRO
     * (non-transferable discount path used by TC_187) so discountTransferabilityIndicator = N
     * can be exercised with Guaranteed pricing.
     */
    public static final String SELECT_ACCOUNT_DETAILS_TC5 = """
            SELECT
                t1.ucracct_cust_code,
                t1.ucracct_prem_code,
                t3.ucrserv_scls_code,
                t4.ucrscmp_scty_code,
                t4.ucrscmp_plan_code
            FROM
                ucracct t1
            JOIN
                ucbcust t2 ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN
                ucrserv t3 ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                           AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN
                ucrscmp t4 ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                           AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_plan_code IN ('RGB', 'CGB', 'GB6', 'PGB')
                AND t4.ucrscmp_scty_code = 'COMM'
                AND TRUNC(SYSDATE) BETWEEN t4.ucrscmp_start_date AND t4.ucrscmp_end_date
                AND EXISTS (
                    SELECT 1
                    FROM ucrscmp disc
                    WHERE disc.ucrscmp_cust_code = t1.ucracct_cust_code
                      AND disc.ucrscmp_prem_code = t1.ucracct_prem_code
                      AND disc.ucrscmp_end_date > SYSDATE
                      AND disc.ucrscmp_scty_code IN ('PPTDISC', 'FLATDISC', 'CSCDISC')
                )
                AND EXISTS (
                    SELECT 1
                    FROM ucrscmp pp
                    WHERE pp.ucrscmp_cust_code = t1.ucracct_cust_code
                      AND pp.ucrscmp_prem_code = t1.ucracct_prem_code
                      AND pp.ucrscmp_end_date > SYSDATE
                      AND pp.ucrscmp_scty_code = 'PRICEPRO'
                )
            ORDER BY
                t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /** TC_5 candidates: Guaranteed + any active discount. */
    public static final String SELECT_ACCOUNT_DETAILS_TC5_WITH_ANY_DISCOUNT_CANDIDATES = """
            SELECT
                t1.ucracct_cust_code,
                t1.ucracct_prem_code,
                t3.ucrserv_scls_code,
                t4.ucrscmp_scty_code,
                t4.ucrscmp_plan_code
            FROM
                ucracct t1
            JOIN
                ucbcust t2 ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN
                ucrserv t3 ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                           AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN
                ucrscmp t4 ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                           AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_plan_code IN ('RGB', 'CGB', 'GB6', 'PGB')
                AND t4.ucrscmp_scty_code = 'COMM'
                AND TRUNC(SYSDATE) BETWEEN t4.ucrscmp_start_date AND t4.ucrscmp_end_date
                AND EXISTS (
                    SELECT 1
                    FROM ucrscmp disc
                    WHERE disc.ucrscmp_cust_code = t1.ucracct_cust_code
                      AND disc.ucrscmp_prem_code = t1.ucracct_prem_code
                      AND disc.ucrscmp_end_date > SYSDATE
                      AND disc.ucrscmp_scty_code IN ('PPTDISC', 'FLATDISC', 'CSCDISC')
                )
            ORDER BY
                t1.ucracct_cust_code DESC
            FETCH FIRST 20 ROWS ONLY
            """;

    /**
     * TC_5 secondary: Guaranteed Bill plan with any active discount
     * (used when no Guaranteed + non-transferable/PRICEPRO discount account is available).
     */
    public static final String SELECT_ACCOUNT_DETAILS_TC5_WITH_ANY_DISCOUNT = """
            SELECT
                t1.ucracct_cust_code,
                t1.ucracct_prem_code,
                t3.ucrserv_scls_code,
                t4.ucrscmp_scty_code,
                t4.ucrscmp_plan_code
            FROM
                ucracct t1
            JOIN
                ucbcust t2 ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN
                ucrserv t3 ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                           AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN
                ucrscmp t4 ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                           AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_plan_code IN ('RGB', 'CGB', 'GB6', 'PGB')
                AND t4.ucrscmp_scty_code = 'COMM'
                AND TRUNC(SYSDATE) BETWEEN t4.ucrscmp_start_date AND t4.ucrscmp_end_date
                AND EXISTS (
                    SELECT 1
                    FROM ucrscmp disc
                    WHERE disc.ucrscmp_cust_code = t1.ucracct_cust_code
                      AND disc.ucrscmp_prem_code = t1.ucracct_prem_code
                      AND disc.ucrscmp_end_date > SYSDATE
                      AND disc.ucrscmp_scty_code IN ('PPTDISC', 'FLATDISC', 'CSCDISC')
                )
            ORDER BY
                t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    /**
     * TC_5 fallback: Guaranteed Bill plan with no active discount rows
     * (used only when no Guaranteed + discount account is available).
     */
    public static final String SELECT_ACCOUNT_DETAILS_TC5_WITHOUT_DISCOUNT = """
            SELECT
                t1.ucracct_cust_code,
                t1.ucracct_prem_code,
                t3.ucrserv_scls_code,
                t4.ucrscmp_scty_code,
                t4.ucrscmp_plan_code
            FROM
                ucracct t1
            JOIN
                ucbcust t2 ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN
                ucrserv t3 ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                           AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN
                ucrscmp t4 ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                           AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_plan_code IN ('RGB', 'CGB', 'GB6', 'PGB')
                AND t4.ucrscmp_scty_code = 'COMM'
                AND TRUNC(SYSDATE) BETWEEN t4.ucrscmp_start_date AND t4.ucrscmp_end_date
                AND NOT EXISTS (
                    SELECT 1
                    FROM ucrscmp disc
                    WHERE disc.ucrscmp_cust_code = t1.ucracct_cust_code
                      AND disc.ucrscmp_prem_code = t1.ucracct_prem_code
                      AND disc.ucrscmp_end_date > SYSDATE
                      AND disc.ucrscmp_scty_code IN ('PPTDISC', 'FLATDISC', 'CSCDISC')
                )
            ORDER BY
                t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC6 = """
            SELECT
                t1.ucracct_cust_code,
                t1.ucracct_prem_code,
                t3.ucrserv_scls_code,
                t4.ucrscmp_scty_code,
                t4.ucrscmp_plan_code
            FROM
                ucracct t1
            JOIN
                ucbcust t2 ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN
                ucrserv t3 ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                           AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN
                ucrscmp t4 ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                           AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_end_date > SYSDATE
                AND NOT EXISTS (
                    SELECT 1
                    FROM ucrscmp gbp
                    WHERE gbp.ucrscmp_cust_code = t1.ucracct_cust_code
                      AND gbp.ucrscmp_prem_code = t1.ucracct_prem_code
                      AND gbp.ucrscmp_end_date > SYSDATE
                      AND gbp.ucrscmp_plan_code IN ('RGB', 'CGB', 'GB6', 'PGB')
                )
            ORDER BY
                t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC7 = """
            SELECT
                t1.ucracct_cust_code,
                t1.ucracct_prem_code,
                t3.ucrserv_scls_code,
                t4.ucrscmp_plan_code
            FROM
                ucracct t1
            JOIN
                ucbcust t2 ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN
                ucrserv t3 ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                           AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN
                ucrscmp t4 ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                           AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_scty_code = 'COMM'
                AND TRUNC(SYSDATE) BETWEEN t4.ucrscmp_start_date AND t4.ucrscmp_end_date
            ORDER BY
                t1.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_DETAILS_TC8 = """
            SELECT
                t1.ucracct_cust_code,
                t1.ucracct_prem_code,
                t3.ucrserv_scls_code,
                t4.ucrscmp_plan_code
            FROM
                ucracct t1
            JOIN
                ucbcust t2 ON t1.ucracct_cust_code = t2.ucbcust_cust_code
            JOIN
                ucrserv t3 ON t1.ucracct_cust_code = t3.ucrserv_cust_code
                           AND t1.ucracct_prem_code = t3.ucrserv_prem_code
            JOIN
                ucrscmp t4 ON t1.ucracct_cust_code = t4.ucrscmp_cust_code
                           AND t1.ucracct_prem_code = t4.ucrscmp_prem_code
            WHERE
                t1.ucracct_status_ind = 'A'
                AND t1.ucracct_cycl_code NOT IN ('DEPO')
                AND t3.ucrserv_scls_code = 'RS'
                AND t4.ucrscmp_scty_code = 'COMM'
                AND TRUNC(SYSDATE) BETWEEN t4.ucrscmp_start_date AND t4.ucrscmp_end_date
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

    public static final String SELECT_PENDING_ENROLLMENT_NEW_ACCOUNT_ONLY = """
            SELECT
                e.GTBENRL_CUST_CODE,
                e.GTBENRL_PREM_CODE
            FROM
                GTBENRL e
            WHERE
                e.GTBENRL_PROC_FLAG = 'N'
                AND e.GTBENRL_CUST_CODE IS NOT NULL
                AND e.GTBENRL_PREM_CODE IS NOT NULL
                AND NOT EXISTS (
                    SELECT
                        1
                    FROM
                        UCRACCT a
                    WHERE
                        a.UCRACCT_CUST_CODE = e.GTBENRL_CUST_CODE
                )
            ORDER BY
                DBMS_RANDOM.VALUE
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_PENDING_ENROLLMENT_MAILING_ADDRESS = """
            SELECT
                GTBENRL_CUST_CODE,
                GTBENRL_PREM_CODE,
                GTBENRL_BILL_ADDR1,
                GTBENRL_BILL_ADDR2,
                GTBENRL_BILL_ADDR3,
                GTBENRL_BILL_CITY,
                GTBENRL_BILL_STATE,
                GTBENRL_BILL_ZIP
            FROM
                GTBENRL
            WHERE
                GTBENRL_CUST_CODE = ?
                AND TO_CHAR(GTBENRL_PREM_CODE) = ?
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_MAILING_ADDRESS_ROW_COUNT_BY_CUSTOMER = """
            SELECT
                COUNT(*) AS ADDRESS_COUNT
            FROM
                UCRADDR
            WHERE
                UCRADDR_CUST_CODE = ?
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

    public static final String SELECT_ACCOUNT_TC_1= """
            SELECT
                *
            FROM
                UCRADDR
            WHERE
                    UCRADDR_CUST_CODE = ?
                AND UCRADDR_STATUS_IND = 'I'
                ORDER BY UCRADDR_ACTIVITY_DATE DESC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_TC_2= """
            SELECT
                *
            FROM
                UCRADDR
            WHERE
                    UCRADDR_CUST_CODE = ?
                AND UCRADDR_STATUS_IND = 'A'
                ORDER BY UCRADDR_ACTIVITY_DATE DESC
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
                    ORDER BY UCRADDR_ACTIVITY_DATE DESC
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
                    ORDER BY UCRADDR_ACTIVITY_DATE DESC
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
                ORDER BY DBMS_RANDOM.VALUE
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
                ORDER BY DBMS_RANDOM.VALUE
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


    public static final String SELECT_ROUTING_NO= """
            SELECT\s
                LPAD(b.utrbank_transit_1, 4, '0') ||
                LPAD(b.utrbank_transit_2, 4, '0') ||
                b.utrbank_transit_3 AS routing_number
            FROM UCRACCT a
            JOIN UTRBANK b
                ON a.ucracct_bank_code = b.utrbank_code
            WHERE a.ucracct_cust_code = ?
              AND a.ucracct_prem_code = ?
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
                UCRACCT_PREM_CODE,
                UCRACCT_NICK_NAME
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
                UCRACCT_PREM_CODE,
                UCRACCT_NICK_NAME
            FROM
                UCRACCT
            WHERE
                UCRACCT_STATUS_IND = 'I'
                AND (UCRACCT_NICK_NAME IS NULL OR TRIM(UCRACCT_NICK_NAME) = '')
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_SERVICE_NUMBER = """
        WITH seed_hist AS (
            SELECT t.ubbbhst_cust_code,
                   t.ubbbhst_prem_code,
                   t.ubbbhst_printed_date,
                   t.ubbbhst_cancel_ind,
                   t.ubbbhst_tran_num,
                   t.ubbbhst_prev_bal,
                   t.ubbbhst_ending_bal
            FROM UBBBHST t
            WHERE t.ubbbhst_cust_code = ?
              AND t.ubbbhst_prem_code = ?
            ORDER BY t.ubbbhst_printed_date DESC
            FETCH FIRST 1 ROWS ONLY
        ),
        all_hist AS (
            SELECT h2.ubbbhst_cust_code,
                   h2.ubbbhst_prem_code,
                   h2.ubbbhst_printed_date,
                   h2.ubbbhst_cancel_ind,
                   h2.ubbbhst_tran_num,
                   h2.ubbbhst_prev_bal,
                   h2.ubbbhst_ending_bal
            FROM UBBBHST h2
            JOIN seed_hist s
              ON h2.ubbbhst_cust_code = s.ubbbhst_cust_code
             AND h2.ubbbhst_prem_code = s.ubbbhst_prem_code
            WHERE h2.ubbbhst_cancel_ind IS NULL
              AND h2.ubbbhst_printed_date >= ADD_MONTHS(TRUNC(SYSDATE), -12)
        ),
        urr AS (
            SELECT r.urrshis_cust_code,
                   r.urrshis_prem_code,
                   r.urrshis_serv_num,
                   r.urrshis_reading,
                   r.urrshis_rtyp_code,
                   r.urrshis_action_date,
                   r.urrshis_charge_date,
                   r.urrshis_dos,
                   r.urrshis_consumption,
                   LAG(r.urrshis_action_date)
                       OVER (PARTITION BY r.urrshis_cust_code,
                                          r.urrshis_prem_code,
                                          r.urrshis_serv_num
                             ORDER BY r.urrshis_action_date) AS prev_action_date
            FROM URRSHIS r
            JOIN seed_hist s
              ON r.urrshis_cust_code = s.ubbbhst_cust_code
             AND r.urrshis_prem_code = s.ubbbhst_prem_code
        ),
        urr_ranges AS (
            SELECT u.*,
                   u.urrshis_action_date AS to_dt,
                   CASE
                       WHEN u.prev_action_date IS NOT NULL THEN u.prev_action_date
                       ELSE u.urrshis_action_date - u.urrshis_dos
                   END AS from_dt
            FROM urr u
        ),
        urr_with_totals AS (
            SELECT ur.*,
                   SUM(ur.urrshis_dos) OVER (
                       PARTITION BY ur.urrshis_cust_code,
                                    ur.urrshis_prem_code
                   ) AS total_dos
            FROM urr_ranges ur
        ),
        weather_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_serv_num,
                   u.urrshis_action_date,
                   NVL(ROUND(AVG(w.ocsweat_avg_temp)), 0)            AS weather_mean_avg_temp,
                   NVL(TRUNC(SUM(w.ocsweat_heating_degree_days)), 0) AS weather_sum_hdd
            FROM urr_with_totals u
            LEFT JOIN UCBPREM p
              ON p.ucbprem_code = u.urrshis_prem_code
            LEFT JOIN OCSWEAT w
              ON w.ocsweat_load_zone_code = p.ucbprem_alternate_location
             AND w.ocsweat_weather_date   > u.from_dt
             AND w.ocsweat_weather_date   < u.to_dt
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_serv_num,
                     u.urrshis_action_date
        ),
        bill_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_charge_date,
                   SUM(u.urrshis_consumption) AS actual_consump_sum,
                   SUM(u.urrshis_dos)         AS days_of_service
            FROM urr_with_totals u
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_charge_date
        )
        SELECT u.urrshis_serv_num                            AS service_number,
               TO_CHAR(h.ubbbhst_printed_date, 'YYYYMMDD')  AS bill_date
        FROM all_hist h
        LEFT JOIN urr_with_totals u
          ON  u.urrshis_cust_code   = h.ubbbhst_cust_code
          AND u.urrshis_prem_code   = h.ubbbhst_prem_code
          AND u.urrshis_charge_date = h.ubbbhst_printed_date
        LEFT JOIN weather_agg wa
          ON  wa.urrshis_cust_code   = u.urrshis_cust_code
          AND wa.urrshis_prem_code   = u.urrshis_prem_code
          AND wa.urrshis_serv_num    = u.urrshis_serv_num
          AND wa.urrshis_action_date = u.urrshis_action_date
        LEFT JOIN UBBCHST c
          ON  c.ubbchst_cust_code   = h.ubbbhst_cust_code
          AND c.ubbchst_prem_code   = h.ubbbhst_prem_code
          AND c.ubbchst_charge_date = h.ubbbhst_printed_date
        LEFT JOIN bill_agg ba
          ON  ba.urrshis_cust_code   = h.ubbbhst_cust_code
          AND ba.urrshis_prem_code   = h.ubbbhst_prem_code
          AND ba.urrshis_charge_date = h.ubbbhst_printed_date
        ORDER BY h.ubbbhst_printed_date DESC,
                 u.urrshis_serv_num,
                 u.urrshis_action_date
        """;

    public static final String GET_BILL_DATE = """
        WITH seed_hist AS (
            SELECT t.ubbbhst_cust_code,
                   t.ubbbhst_prem_code,
                   t.ubbbhst_printed_date,
                   t.ubbbhst_cancel_ind,
                   t.ubbbhst_tran_num,
                   t.ubbbhst_prev_bal,
                   t.ubbbhst_ending_bal
            FROM UBBBHST t
            WHERE t.ubbbhst_cust_code = ?
              AND t.ubbbhst_prem_code = ?
            ORDER BY t.ubbbhst_printed_date DESC
            FETCH FIRST 1 ROWS ONLY
        ),
        all_hist AS (
            SELECT h2.ubbbhst_cust_code,
                   h2.ubbbhst_prem_code,
                   h2.ubbbhst_printed_date,
                   h2.ubbbhst_cancel_ind,
                   h2.ubbbhst_tran_num,
                   h2.ubbbhst_prev_bal,
                   h2.ubbbhst_ending_bal
            FROM UBBBHST h2
            JOIN seed_hist s
              ON h2.ubbbhst_cust_code = s.ubbbhst_cust_code
             AND h2.ubbbhst_prem_code = s.ubbbhst_prem_code
            WHERE h2.ubbbhst_cancel_ind IS NULL
              AND h2.ubbbhst_printed_date >= ADD_MONTHS(TRUNC(SYSDATE), -12)
        ),
        urr AS (
            SELECT r.urrshis_cust_code,
                   r.urrshis_prem_code,
                   r.urrshis_serv_num,
                   r.urrshis_reading,
                   r.urrshis_rtyp_code,
                   r.urrshis_action_date,
                   r.urrshis_charge_date,
                   r.urrshis_dos,
                   r.urrshis_consumption,
                   LAG(r.urrshis_action_date)
                       OVER (PARTITION BY r.urrshis_cust_code,
                                          r.urrshis_prem_code,
                                          r.urrshis_serv_num
                             ORDER BY r.urrshis_action_date) AS prev_action_date
            FROM URRSHIS r
            JOIN seed_hist s
              ON r.urrshis_cust_code = s.ubbbhst_cust_code
             AND r.urrshis_prem_code = s.ubbbhst_prem_code
        ),
        urr_ranges AS (
            SELECT u.*,
                   u.urrshis_action_date AS to_dt,
                   CASE
                       WHEN u.prev_action_date IS NOT NULL THEN u.prev_action_date
                       ELSE u.urrshis_action_date - u.urrshis_dos
                   END AS from_dt
            FROM urr u
        ),
        urr_with_totals AS (
            SELECT ur.*,
                   SUM(ur.urrshis_dos) OVER (
                       PARTITION BY ur.urrshis_cust_code,
                                    ur.urrshis_prem_code
                   ) AS total_dos
            FROM urr_ranges ur
        ),
        weather_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_serv_num,
                   u.urrshis_action_date,
                   NVL(ROUND(AVG(w.ocsweat_avg_temp)), 0)            AS weather_mean_avg_temp,
                   NVL(TRUNC(SUM(w.ocsweat_heating_degree_days)), 0) AS weather_sum_hdd
            FROM urr_with_totals u
            LEFT JOIN UCBPREM p
              ON p.ucbprem_code = u.urrshis_prem_code
            LEFT JOIN OCSWEAT w
              ON w.ocsweat_load_zone_code = p.ucbprem_alternate_location
             AND w.ocsweat_weather_date   > u.from_dt
             AND w.ocsweat_weather_date   < u.to_dt
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_serv_num,
                     u.urrshis_action_date
        ),
        bill_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_charge_date,
                   SUM(u.urrshis_consumption) AS actual_consump_sum,
                   SUM(u.urrshis_dos)         AS days_of_service
            FROM urr_with_totals u
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_charge_date
        )
        SELECT h.ubbbhst_cust_code                          AS customer_code,
               TO_CHAR(h.ubbbhst_printed_date, 'YYYYMMDD')  AS bill_date
        FROM all_hist h
        LEFT JOIN urr_with_totals u
          ON  u.urrshis_cust_code   = h.ubbbhst_cust_code
          AND u.urrshis_prem_code   = h.ubbbhst_prem_code
          AND u.urrshis_charge_date = h.ubbbhst_printed_date
        LEFT JOIN weather_agg wa
          ON  wa.urrshis_cust_code   = u.urrshis_cust_code
          AND wa.urrshis_prem_code   = u.urrshis_prem_code
          AND wa.urrshis_serv_num    = u.urrshis_serv_num
          AND wa.urrshis_action_date = u.urrshis_action_date
        LEFT JOIN UBBCHST c
          ON  c.ubbchst_cust_code   = h.ubbbhst_cust_code
          AND c.ubbchst_prem_code   = h.ubbbhst_prem_code
          AND c.ubbchst_charge_date = h.ubbbhst_printed_date
        LEFT JOIN bill_agg ba
          ON  ba.urrshis_cust_code   = h.ubbbhst_cust_code
          AND ba.urrshis_prem_code   = h.ubbbhst_prem_code
          AND ba.urrshis_charge_date = h.ubbbhst_printed_date
        ORDER BY h.ubbbhst_printed_date DESC,
                 u.urrshis_serv_num,
                 u.urrshis_action_date
        """;

    public static final String GET_FROM_DATE = """
        WITH seed_hist AS (
            SELECT t.ubbbhst_cust_code,
                   t.ubbbhst_prem_code,
                   t.ubbbhst_printed_date,
                   t.ubbbhst_cancel_ind,
                   t.ubbbhst_tran_num,
                   t.ubbbhst_prev_bal,
                   t.ubbbhst_ending_bal
            FROM UBBBHST t
            WHERE t.ubbbhst_cust_code = ?
              AND t.ubbbhst_prem_code = ?
            ORDER BY t.ubbbhst_printed_date DESC
            FETCH FIRST 1 ROWS ONLY
        ),
        all_hist AS (
            SELECT h2.ubbbhst_cust_code,
                   h2.ubbbhst_prem_code,
                   h2.ubbbhst_printed_date,
                   h2.ubbbhst_cancel_ind,
                   h2.ubbbhst_tran_num,
                   h2.ubbbhst_prev_bal,
                   h2.ubbbhst_ending_bal
            FROM UBBBHST h2
            JOIN seed_hist s
              ON h2.ubbbhst_cust_code = s.ubbbhst_cust_code
             AND h2.ubbbhst_prem_code = s.ubbbhst_prem_code
            WHERE h2.ubbbhst_cancel_ind IS NULL
              AND h2.ubbbhst_printed_date >= ADD_MONTHS(TRUNC(SYSDATE), -12)
        ),
        urr AS (
            SELECT r.urrshis_cust_code,
                   r.urrshis_prem_code,
                   r.urrshis_serv_num,
                   r.urrshis_reading,
                   r.urrshis_rtyp_code,
                   r.urrshis_action_date,
                   r.urrshis_charge_date,
                   r.urrshis_dos,
                   r.urrshis_consumption,
                   LAG(r.urrshis_action_date)
                       OVER (PARTITION BY r.urrshis_cust_code,
                                          r.urrshis_prem_code,
                                          r.urrshis_serv_num
                             ORDER BY r.urrshis_action_date) AS prev_action_date
            FROM URRSHIS r
            JOIN seed_hist s
              ON r.urrshis_cust_code = s.ubbbhst_cust_code
             AND r.urrshis_prem_code = s.ubbbhst_prem_code
        ),
        urr_ranges AS (
            SELECT u.*,
                   u.urrshis_action_date AS to_dt,
                   CASE
                       WHEN u.prev_action_date IS NOT NULL THEN u.prev_action_date
                       ELSE u.urrshis_action_date - u.urrshis_dos
                   END AS from_dt
            FROM urr u
        ),
        urr_with_totals AS (
            SELECT ur.*,
                   SUM(ur.urrshis_dos) OVER (
                       PARTITION BY ur.urrshis_cust_code,
                                    ur.urrshis_prem_code
                   ) AS total_dos
            FROM urr_ranges ur
        ),
        weather_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_serv_num,
                   u.urrshis_action_date,
                   NVL(ROUND(AVG(w.ocsweat_avg_temp)), 0)            AS weather_mean_avg_temp,
                   NVL(TRUNC(SUM(w.ocsweat_heating_degree_days)), 0) AS weather_sum_hdd
            FROM urr_with_totals u
            LEFT JOIN UCBPREM p
              ON p.ucbprem_code = u.urrshis_prem_code
            LEFT JOIN OCSWEAT w
              ON w.ocsweat_load_zone_code = p.ucbprem_alternate_location
             AND w.ocsweat_weather_date   > u.from_dt
             AND w.ocsweat_weather_date   < u.to_dt
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_serv_num,
                     u.urrshis_action_date
        ),
        bill_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_charge_date,
                   SUM(u.urrshis_consumption) AS actual_consump_sum,
                   SUM(u.urrshis_dos)         AS days_of_service
            FROM urr_with_totals u
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_charge_date
        )
        SELECT TO_CHAR(u.from_dt, 'YYYYMMDD')                   AS usage_from_date,
               TO_CHAR(h.ubbbhst_printed_date, 'YYYYMMDD')      AS bill_date
        FROM all_hist h
        LEFT JOIN urr_with_totals u
          ON  u.urrshis_cust_code   = h.ubbbhst_cust_code
          AND u.urrshis_prem_code   = h.ubbbhst_prem_code
          AND u.urrshis_charge_date = h.ubbbhst_printed_date
        LEFT JOIN weather_agg wa
          ON  wa.urrshis_cust_code   = u.urrshis_cust_code
          AND wa.urrshis_prem_code   = u.urrshis_prem_code
          AND wa.urrshis_serv_num    = u.urrshis_serv_num
          AND wa.urrshis_action_date = u.urrshis_action_date
        LEFT JOIN UBBCHST c
          ON  c.ubbchst_cust_code   = h.ubbbhst_cust_code
          AND c.ubbchst_prem_code   = h.ubbbhst_prem_code
          AND c.ubbchst_charge_date = h.ubbbhst_printed_date
        LEFT JOIN bill_agg ba
          ON  ba.urrshis_cust_code   = h.ubbbhst_cust_code
          AND ba.urrshis_prem_code   = h.ubbbhst_prem_code
          AND ba.urrshis_charge_date = h.ubbbhst_printed_date
        ORDER BY h.ubbbhst_printed_date DESC,
                 u.urrshis_serv_num,
                 u.urrshis_action_date
        """;

    public static final String GET_TO_DATE = """
        WITH seed_hist AS (
            SELECT t.ubbbhst_cust_code,
                   t.ubbbhst_prem_code,
                   t.ubbbhst_printed_date,
                   t.ubbbhst_cancel_ind,
                   t.ubbbhst_tran_num,
                   t.ubbbhst_prev_bal,
                   t.ubbbhst_ending_bal
            FROM UBBBHST t
            WHERE t.ubbbhst_cust_code = ?
              AND t.ubbbhst_prem_code = ?
            ORDER BY t.ubbbhst_printed_date DESC
            FETCH FIRST 1 ROWS ONLY
        ),
        all_hist AS (
            SELECT h2.ubbbhst_cust_code,
                   h2.ubbbhst_prem_code,
                   h2.ubbbhst_printed_date,
                   h2.ubbbhst_cancel_ind,
                   h2.ubbbhst_tran_num,
                   h2.ubbbhst_prev_bal,
                   h2.ubbbhst_ending_bal
            FROM UBBBHST h2
            JOIN seed_hist s
              ON h2.ubbbhst_cust_code = s.ubbbhst_cust_code
             AND h2.ubbbhst_prem_code = s.ubbbhst_prem_code
            WHERE h2.ubbbhst_cancel_ind IS NULL
              AND h2.ubbbhst_printed_date >= ADD_MONTHS(TRUNC(SYSDATE), -12)
        ),
        urr AS (
            SELECT r.urrshis_cust_code,
                   r.urrshis_prem_code,
                   r.urrshis_serv_num,
                   r.urrshis_reading,
                   r.urrshis_rtyp_code,
                   r.urrshis_action_date,
                   r.urrshis_charge_date,
                   r.urrshis_dos,
                   r.urrshis_consumption,
                   LAG(r.urrshis_action_date)
                       OVER (PARTITION BY r.urrshis_cust_code,
                                          r.urrshis_prem_code,
                                          r.urrshis_serv_num
                             ORDER BY r.urrshis_action_date) AS prev_action_date
            FROM URRSHIS r
            JOIN seed_hist s
              ON r.urrshis_cust_code = s.ubbbhst_cust_code
             AND r.urrshis_prem_code = s.ubbbhst_prem_code
        ),
        urr_ranges AS (
            SELECT u.*,
                   u.urrshis_action_date AS to_dt,
                   CASE
                       WHEN u.prev_action_date IS NOT NULL THEN u.prev_action_date
                       ELSE u.urrshis_action_date - u.urrshis_dos
                   END AS from_dt
            FROM urr u
        ),
        urr_with_totals AS (
            SELECT ur.*,
                   SUM(ur.urrshis_dos) OVER (
                       PARTITION BY ur.urrshis_cust_code,
                                    ur.urrshis_prem_code
                   ) AS total_dos
            FROM urr_ranges ur
        ),
        weather_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_serv_num,
                   u.urrshis_action_date,
                   NVL(ROUND(AVG(w.ocsweat_avg_temp)), 0)            AS weather_mean_avg_temp,
                   NVL(TRUNC(SUM(w.ocsweat_heating_degree_days)), 0) AS weather_sum_hdd
            FROM urr_with_totals u
            LEFT JOIN UCBPREM p
              ON p.ucbprem_code = u.urrshis_prem_code
            LEFT JOIN OCSWEAT w
              ON w.ocsweat_load_zone_code = p.ucbprem_alternate_location
             AND w.ocsweat_weather_date   > u.from_dt
             AND w.ocsweat_weather_date   < u.to_dt
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_serv_num,
                     u.urrshis_action_date
        ),
        bill_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_charge_date,
                   SUM(u.urrshis_consumption) AS actual_consump_sum,
                   SUM(u.urrshis_dos)         AS days_of_service
            FROM urr_with_totals u
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_charge_date
        )
        SELECT TO_CHAR(u.to_dt,   'YYYYMMDD')                   AS usage_to_date,
               TO_CHAR(h.ubbbhst_printed_date, 'YYYYMMDD')      AS bill_date
        FROM all_hist h
        LEFT JOIN urr_with_totals u
          ON  u.urrshis_cust_code   = h.ubbbhst_cust_code
          AND u.urrshis_prem_code   = h.ubbbhst_prem_code
          AND u.urrshis_charge_date = h.ubbbhst_printed_date
        LEFT JOIN weather_agg wa
          ON  wa.urrshis_cust_code   = u.urrshis_cust_code
          AND wa.urrshis_prem_code   = u.urrshis_prem_code
          AND wa.urrshis_serv_num    = u.urrshis_serv_num
          AND wa.urrshis_action_date = u.urrshis_action_date
        LEFT JOIN UBBCHST c
          ON  c.ubbchst_cust_code   = h.ubbbhst_cust_code
          AND c.ubbchst_prem_code   = h.ubbbhst_prem_code
          AND c.ubbchst_charge_date = h.ubbbhst_printed_date
        LEFT JOIN bill_agg ba
          ON  ba.urrshis_cust_code   = h.ubbbhst_cust_code
          AND ba.urrshis_prem_code   = h.ubbbhst_prem_code
          AND ba.urrshis_charge_date = h.ubbbhst_printed_date
        ORDER BY h.ubbbhst_printed_date DESC,
                 u.urrshis_serv_num,
                 u.urrshis_action_date
        """;

    public static final String GET_ACTUAL_CONSUMPTION = """
        WITH seed_hist AS (
            SELECT t.ubbbhst_cust_code,
                   t.ubbbhst_prem_code,
                   t.ubbbhst_printed_date,
                   t.ubbbhst_cancel_ind,
                   t.ubbbhst_tran_num,
                   t.ubbbhst_prev_bal,
                   t.ubbbhst_ending_bal
            FROM UBBBHST t
            WHERE t.ubbbhst_cust_code = ?
              AND t.ubbbhst_prem_code = ?
            ORDER BY t.ubbbhst_printed_date DESC
            FETCH FIRST 1 ROWS ONLY
        ),
        all_hist AS (
            SELECT h2.ubbbhst_cust_code,
                   h2.ubbbhst_prem_code,
                   h2.ubbbhst_printed_date,
                   h2.ubbbhst_cancel_ind,
                   h2.ubbbhst_tran_num,
                   h2.ubbbhst_prev_bal,
                   h2.ubbbhst_ending_bal
            FROM UBBBHST h2
            JOIN seed_hist s
              ON h2.ubbbhst_cust_code = s.ubbbhst_cust_code
             AND h2.ubbbhst_prem_code = s.ubbbhst_prem_code
            WHERE h2.ubbbhst_cancel_ind IS NULL
              AND h2.ubbbhst_printed_date >= ADD_MONTHS(TRUNC(SYSDATE), -12)
        ),
        urr AS (
            SELECT r.urrshis_cust_code,
                   r.urrshis_prem_code,
                   r.urrshis_serv_num,
                   r.urrshis_reading,
                   r.urrshis_rtyp_code,
                   r.urrshis_action_date,
                   r.urrshis_charge_date,
                   r.urrshis_dos,
                   r.urrshis_consumption,
                   LAG(r.urrshis_action_date)
                       OVER (PARTITION BY r.urrshis_cust_code,
                                          r.urrshis_prem_code,
                                          r.urrshis_serv_num
                             ORDER BY r.urrshis_action_date) AS prev_action_date
            FROM URRSHIS r
            JOIN seed_hist s
              ON r.urrshis_cust_code = s.ubbbhst_cust_code
             AND r.urrshis_prem_code = s.ubbbhst_prem_code
        ),
        urr_ranges AS (
            SELECT u.*,
                   u.urrshis_action_date AS to_dt,
                   CASE
                       WHEN u.prev_action_date IS NOT NULL THEN u.prev_action_date
                       ELSE u.urrshis_action_date - u.urrshis_dos
                   END AS from_dt
            FROM urr u
        ),
        urr_with_totals AS (
            SELECT ur.*,
                   SUM(ur.urrshis_dos) OVER (
                       PARTITION BY ur.urrshis_cust_code,
                                    ur.urrshis_prem_code
                   ) AS total_dos
            FROM urr_ranges ur
        ),
        weather_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_serv_num,
                   u.urrshis_action_date,
                   NVL(ROUND(AVG(w.ocsweat_avg_temp)), 0)            AS weather_mean_avg_temp,
                   NVL(TRUNC(SUM(w.ocsweat_heating_degree_days)), 0) AS weather_sum_hdd
            FROM urr_with_totals u
            LEFT JOIN UCBPREM p
              ON p.ucbprem_code = u.urrshis_prem_code
            LEFT JOIN OCSWEAT w
              ON w.ocsweat_load_zone_code = p.ucbprem_alternate_location
             AND w.ocsweat_weather_date   > u.from_dt
             AND w.ocsweat_weather_date   < u.to_dt
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_serv_num,
                     u.urrshis_action_date
        ),
        bill_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_charge_date,
                   SUM(u.urrshis_consumption) AS actual_consump_sum,
                   SUM(u.urrshis_dos)         AS days_of_service
            FROM urr_with_totals u
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_charge_date
        )
        SELECT TRUNC(ROUND(ba.actual_consump_sum / NULLIF(ba.days_of_service, 0), 4), 3) AS avg_daily_actual_consumption,
               TO_CHAR(h.ubbbhst_printed_date, 'YYYYMMDD')                               AS bill_date
        FROM all_hist h
        LEFT JOIN urr_with_totals u
          ON  u.urrshis_cust_code   = h.ubbbhst_cust_code
          AND u.urrshis_prem_code   = h.ubbbhst_prem_code
          AND u.urrshis_charge_date = h.ubbbhst_printed_date
        LEFT JOIN weather_agg wa
          ON  wa.urrshis_cust_code   = u.urrshis_cust_code
          AND wa.urrshis_prem_code   = u.urrshis_prem_code
          AND wa.urrshis_serv_num    = u.urrshis_serv_num
          AND wa.urrshis_action_date = u.urrshis_action_date
        LEFT JOIN UBBCHST c
          ON  c.ubbchst_cust_code   = h.ubbbhst_cust_code
          AND c.ubbchst_prem_code   = h.ubbbhst_prem_code
          AND c.ubbchst_charge_date = h.ubbbhst_printed_date
        LEFT JOIN bill_agg ba
          ON  ba.urrshis_cust_code   = h.ubbbhst_cust_code
          AND ba.urrshis_prem_code   = h.ubbbhst_prem_code
          AND ba.urrshis_charge_date = h.ubbbhst_printed_date
        ORDER BY h.ubbbhst_printed_date DESC,
                 u.urrshis_serv_num,
                 u.urrshis_action_date
        """;


    public static final String GET_AVERAGE_DAILY_BILLED_CONSUMPTION = """
        WITH seed_hist AS (
            SELECT t.ubbbhst_cust_code,
                   t.ubbbhst_prem_code,
                   t.ubbbhst_printed_date,
                   t.ubbbhst_cancel_ind,
                   t.ubbbhst_tran_num,
                   t.ubbbhst_prev_bal,
                   t.ubbbhst_ending_bal
            FROM UBBBHST t
            WHERE t.ubbbhst_cust_code = ?
              AND t.ubbbhst_prem_code = ?
            ORDER BY t.ubbbhst_printed_date DESC
            FETCH FIRST 1 ROWS ONLY
        ),
        all_hist AS (
            SELECT h2.ubbbhst_cust_code,
                   h2.ubbbhst_prem_code,
                   h2.ubbbhst_printed_date,
                   h2.ubbbhst_cancel_ind,
                   h2.ubbbhst_tran_num,
                   h2.ubbbhst_prev_bal,
                   h2.ubbbhst_ending_bal
            FROM UBBBHST h2
            JOIN seed_hist s
              ON h2.ubbbhst_cust_code = s.ubbbhst_cust_code
             AND h2.ubbbhst_prem_code  = s.ubbbhst_prem_code
            WHERE h2.ubbbhst_cancel_ind IS NULL
              AND h2.ubbbhst_printed_date >= ADD_MONTHS(TRUNC(SYSDATE), -12)
        ),
        urr AS (
            SELECT r.urrshis_cust_code,
                   r.urrshis_prem_code,
                   r.urrshis_serv_num,
                   r.urrshis_reading,
                   r.urrshis_rtyp_code,
                   r.urrshis_action_date,
                   r.urrshis_charge_date,
                   r.urrshis_dos,
                   r.urrshis_consumption,
                   LAG(r.urrshis_action_date) OVER (
                       PARTITION BY r.urrshis_cust_code,
                                    r.urrshis_prem_code,
                                    r.urrshis_serv_num
                       ORDER BY r.urrshis_action_date
                   ) AS prev_action_date
            FROM URRSHIS r
            JOIN seed_hist s
              ON r.urrshis_cust_code = s.ubbbhst_cust_code
             AND r.urrshis_prem_code  = s.ubbbhst_prem_code
        ),
        urr_ranges AS (
            SELECT u.*,
                   u.urrshis_action_date AS to_dt,
                   CASE
                       WHEN u.prev_action_date IS NOT NULL THEN u.prev_action_date
                       ELSE u.urrshis_action_date - u.urrshis_dos
                   END AS from_dt
            FROM urr u
        ),
        urr_with_totals AS (
            SELECT ur.*,
                   SUM(ur.urrshis_dos) OVER (
                       PARTITION BY ur.urrshis_cust_code,
                                    ur.urrshis_prem_code
                   ) AS total_dos
            FROM urr_ranges ur
        ),
        weather_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_serv_num,
                   u.urrshis_action_date,
                   NVL(ROUND(AVG(w.ocsweat_avg_temp)), 0)            AS weather_mean_avg_temp,
                   NVL(TRUNC(SUM(w.ocsweat_heating_degree_days)), 0) AS weather_sum_hdd
            FROM urr_with_totals u
            LEFT JOIN UCBPREM p
              ON p.ucbprem_code = u.urrshis_prem_code
            LEFT JOIN OCSWEAT w
              ON w.ocsweat_load_zone_code = p.ucbprem_alternate_location
             AND w.ocsweat_weather_date   > u.from_dt
             AND w.ocsweat_weather_date   < u.to_dt
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_serv_num,
                     u.urrshis_action_date
        ),
        bill_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_charge_date,
                   SUM(u.urrshis_consumption) AS actual_consump_sum,
                   SUM(u.urrshis_dos)         AS days_of_service
            FROM urr_with_totals u
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_charge_date
        )
        SELECT TRUNC(ROUND(c.ubbchst_billed_consump / NULLIF(ba.days_of_service, 0), 4), 3) AS avg_daily_billed_consumption,
               TO_CHAR(h.ubbbhst_printed_date, 'YYYYMMDD')                                  AS bill_date
        FROM all_hist h
        LEFT JOIN urr_with_totals u
          ON u.urrshis_cust_code  = h.ubbbhst_cust_code
         AND u.urrshis_prem_code  = h.ubbbhst_prem_code
         AND u.urrshis_charge_date = h.ubbbhst_printed_date
        LEFT JOIN weather_agg wa
          ON wa.urrshis_cust_code   = u.urrshis_cust_code
         AND wa.urrshis_prem_code   = u.urrshis_prem_code
         AND wa.urrshis_serv_num    = u.urrshis_serv_num
         AND wa.urrshis_action_date = u.urrshis_action_date
        LEFT JOIN UBBCHST c
          ON c.ubbchst_cust_code   = h.ubbbhst_cust_code
         AND c.ubbchst_prem_code   = h.ubbbhst_prem_code
         AND c.ubbchst_charge_date = h.ubbbhst_printed_date
        LEFT JOIN bill_agg ba
          ON ba.urrshis_cust_code   = h.ubbbhst_cust_code
         AND ba.urrshis_prem_code   = h.ubbbhst_prem_code
         AND ba.urrshis_charge_date = h.ubbbhst_printed_date
        ORDER BY h.ubbbhst_printed_date DESC,
                 u.urrshis_serv_num,
                 u.urrshis_action_date
        """;


    public static final String GET_TOTAL_BILLED_CONSUMPTION = """
        WITH seed_hist AS (
            SELECT t.ubbbhst_cust_code,
                   t.ubbbhst_prem_code,
                   t.ubbbhst_printed_date,
                   t.ubbbhst_cancel_ind,
                   t.ubbbhst_tran_num,
                   t.ubbbhst_prev_bal,
                   t.ubbbhst_ending_bal
            FROM UBBBHST t
            WHERE t.ubbbhst_cust_code = ?
              AND t.ubbbhst_prem_code = ?
            ORDER BY t.ubbbhst_printed_date DESC
            FETCH FIRST 1 ROWS ONLY
        ),
        all_hist AS (
            SELECT h2.ubbbhst_cust_code,
                   h2.ubbbhst_prem_code,
                   h2.ubbbhst_printed_date,
                   h2.ubbbhst_cancel_ind,
                   h2.ubbbhst_tran_num,
                   h2.ubbbhst_prev_bal,
                   h2.ubbbhst_ending_bal
            FROM UBBBHST h2
            JOIN seed_hist s
              ON h2.ubbbhst_cust_code = s.ubbbhst_cust_code
             AND h2.ubbbhst_prem_code = s.ubbbhst_prem_code
            WHERE h2.ubbbhst_cancel_ind IS NULL
              AND h2.ubbbhst_printed_date >= ADD_MONTHS(TRUNC(SYSDATE), -12)
        ),
        urr AS (
            SELECT r.urrshis_cust_code,
                   r.urrshis_prem_code,
                   r.urrshis_serv_num,
                   r.urrshis_reading,
                   r.urrshis_rtyp_code,
                   r.urrshis_action_date,
                   r.urrshis_charge_date,
                   r.urrshis_dos,
                   r.urrshis_consumption,
                   LAG(r.urrshis_action_date) OVER (
                       PARTITION BY r.urrshis_cust_code,
                                    r.urrshis_prem_code,
                                    r.urrshis_serv_num
                       ORDER BY r.urrshis_action_date
                   ) AS prev_action_date
            FROM URRSHIS r
            JOIN seed_hist s
              ON r.urrshis_cust_code = s.ubbbhst_cust_code
             AND r.urrshis_prem_code = s.ubbbhst_prem_code
        ),
        urr_ranges AS (
            SELECT u.*,
                   u.urrshis_action_date AS to_dt,
                   CASE
                       WHEN u.prev_action_date IS NOT NULL THEN u.prev_action_date
                       ELSE u.urrshis_action_date - u.urrshis_dos
                   END AS from_dt
            FROM urr u
        ),
        urr_with_totals AS (
            SELECT ur.*,
                   SUM(ur.urrshis_dos) OVER (
                       PARTITION BY ur.urrshis_cust_code,
                                    ur.urrshis_prem_code
                   ) AS total_dos
            FROM urr_ranges ur
        ),
        weather_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_serv_num,
                   u.urrshis_action_date,
                   NVL(ROUND(AVG(w.ocsweat_avg_temp)), 0)            AS weather_mean_avg_temp,
                   NVL(TRUNC(SUM(w.ocsweat_heating_degree_days)), 0) AS weather_sum_hdd
            FROM urr_with_totals u
            LEFT JOIN UCBPREM p
              ON p.ucbprem_code = u.urrshis_prem_code
            LEFT JOIN OCSWEAT w
              ON w.ocsweat_load_zone_code = p.ucbprem_alternate_location
             AND w.ocsweat_weather_date   > u.from_dt
             AND w.ocsweat_weather_date   < u.to_dt
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_serv_num,
                     u.urrshis_action_date
        ),
        bill_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_charge_date,
                   SUM(u.urrshis_consumption) AS actual_consump_sum,
                   SUM(u.urrshis_dos)         AS days_of_service
            FROM urr_with_totals u
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_charge_date
        )
        SELECT c.ubbchst_billed_consump                    AS total_billed_consumption,
               TO_CHAR(h.ubbbhst_printed_date, 'YYYYMMDD') AS bill_date
        FROM all_hist h
        LEFT JOIN urr_with_totals u
          ON u.urrshis_cust_code   = h.ubbbhst_cust_code
         AND u.urrshis_prem_code   = h.ubbbhst_prem_code
         AND u.urrshis_charge_date = h.ubbbhst_printed_date
        LEFT JOIN weather_agg wa
          ON wa.urrshis_cust_code   = u.urrshis_cust_code
         AND wa.urrshis_prem_code   = u.urrshis_prem_code
         AND wa.urrshis_serv_num    = u.urrshis_serv_num
         AND wa.urrshis_action_date = u.urrshis_action_date
        LEFT JOIN UBBCHST c
          ON c.ubbchst_cust_code   = h.ubbbhst_cust_code
         AND c.ubbchst_prem_code   = h.ubbbhst_prem_code
         AND c.ubbchst_charge_date = h.ubbbhst_printed_date
        LEFT JOIN bill_agg ba
          ON ba.urrshis_cust_code   = h.ubbbhst_cust_code
         AND ba.urrshis_prem_code   = h.ubbbhst_prem_code
         AND ba.urrshis_charge_date = h.ubbbhst_printed_date
        ORDER BY h.ubbbhst_printed_date DESC,
                 u.urrshis_serv_num,
                 u.urrshis_action_date
        """;

    public static final String GET_DAYS_OF_SERVICE = """
        WITH seed_hist AS (
            SELECT t.ubbbhst_cust_code,
                   t.ubbbhst_prem_code,
                   t.ubbbhst_printed_date,
                   t.ubbbhst_cancel_ind,
                   t.ubbbhst_tran_num,
                   t.ubbbhst_prev_bal,
                   t.ubbbhst_ending_bal
            FROM UBBBHST t
            WHERE t.ubbbhst_cust_code = ?
              AND t.ubbbhst_prem_code = ?
            ORDER BY t.ubbbhst_printed_date DESC
            FETCH FIRST 1 ROWS ONLY
        ),
        all_hist AS (
            SELECT h2.ubbbhst_cust_code,
                   h2.ubbbhst_prem_code,
                   h2.ubbbhst_printed_date,
                   h2.ubbbhst_cancel_ind,
                   h2.ubbbhst_tran_num,
                   h2.ubbbhst_prev_bal,
                   h2.ubbbhst_ending_bal
            FROM UBBBHST h2
            JOIN seed_hist s
              ON h2.ubbbhst_cust_code = s.ubbbhst_cust_code
             AND h2.ubbbhst_prem_code = s.ubbbhst_prem_code
            WHERE h2.ubbbhst_cancel_ind IS NULL
              AND h2.ubbbhst_printed_date >= ADD_MONTHS(TRUNC(SYSDATE), -12)
        ),
        urr AS (
            SELECT r.urrshis_cust_code,
                   r.urrshis_prem_code,
                   r.urrshis_serv_num,
                   r.urrshis_reading,
                   r.urrshis_rtyp_code,
                   r.urrshis_action_date,
                   r.urrshis_charge_date,
                   r.urrshis_dos,
                   r.urrshis_consumption,
                   LAG(r.urrshis_action_date) OVER (
                       PARTITION BY r.urrshis_cust_code,
                                    r.urrshis_prem_code,
                                    r.urrshis_serv_num
                       ORDER BY r.urrshis_action_date
                   ) AS prev_action_date
            FROM URRSHIS r
            JOIN seed_hist s
              ON r.urrshis_cust_code = s.ubbbhst_cust_code
             AND r.urrshis_prem_code = s.ubbbhst_prem_code
        ),
        urr_ranges AS (
            SELECT u.*,
                   u.urrshis_action_date AS to_dt,
                   CASE
                       WHEN u.prev_action_date IS NOT NULL THEN u.prev_action_date
                       ELSE u.urrshis_action_date - u.urrshis_dos
                   END AS from_dt
            FROM urr u
        ),
        urr_with_totals AS (
            SELECT ur.*,
                   SUM(ur.urrshis_dos) OVER (
                       PARTITION BY ur.urrshis_cust_code,
                                    ur.urrshis_prem_code
                   ) AS total_dos
            FROM urr_ranges ur
        ),
        weather_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_serv_num,
                   u.urrshis_action_date,
                   NVL(ROUND(AVG(w.ocsweat_avg_temp)), 0)            AS weather_mean_avg_temp,
                   NVL(TRUNC(SUM(w.ocsweat_heating_degree_days)), 0) AS weather_sum_hdd
            FROM urr_with_totals u
            LEFT JOIN UCBPREM p
              ON p.ucbprem_code = u.urrshis_prem_code
            LEFT JOIN OCSWEAT w
              ON w.ocsweat_load_zone_code = p.ucbprem_alternate_location
             AND w.ocsweat_weather_date   > u.from_dt
             AND w.ocsweat_weather_date   < u.to_dt
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_serv_num,
                     u.urrshis_action_date
        ),
        bill_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_charge_date,
                   SUM(u.urrshis_consumption) AS actual_consump_sum,
                   SUM(u.urrshis_dos)         AS days_of_service
            FROM urr_with_totals u
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_charge_date
        )
        SELECT ba.days_of_service                          AS days_of_service,
               TO_CHAR(h.ubbbhst_printed_date, 'YYYYMMDD') AS bill_date
        FROM all_hist h
        LEFT JOIN urr_with_totals u
          ON u.urrshis_cust_code   = h.ubbbhst_cust_code
         AND u.urrshis_prem_code   = h.ubbbhst_prem_code
         AND u.urrshis_charge_date = h.ubbbhst_printed_date
        LEFT JOIN weather_agg wa
          ON wa.urrshis_cust_code   = u.urrshis_cust_code
         AND wa.urrshis_prem_code   = u.urrshis_prem_code
         AND wa.urrshis_serv_num    = u.urrshis_serv_num
         AND wa.urrshis_action_date = u.urrshis_action_date
        LEFT JOIN UBBCHST c
          ON c.ubbchst_cust_code   = h.ubbbhst_cust_code
         AND c.ubbchst_prem_code   = h.ubbbhst_prem_code
         AND c.ubbchst_charge_date = h.ubbbhst_printed_date
        LEFT JOIN bill_agg ba
          ON ba.urrshis_cust_code   = h.ubbbhst_cust_code
         AND ba.urrshis_prem_code   = h.ubbbhst_prem_code
         AND ba.urrshis_charge_date = h.ubbbhst_printed_date
        ORDER BY h.ubbbhst_printed_date DESC,
                 u.urrshis_serv_num,
                 u.urrshis_action_date
        """;

    public static final String GET_READING = """
        WITH seed_hist AS (
            SELECT t.ubbbhst_cust_code,
                   t.ubbbhst_prem_code,
                   t.ubbbhst_printed_date,
                   t.ubbbhst_cancel_ind,
                   t.ubbbhst_tran_num,
                   t.ubbbhst_prev_bal,
                   t.ubbbhst_ending_bal
            FROM UBBBHST t
            WHERE t.ubbbhst_cust_code = ?
              AND t.ubbbhst_prem_code = ?
            ORDER BY t.ubbbhst_printed_date DESC
            FETCH FIRST 1 ROWS ONLY
        ),
        all_hist AS (
            SELECT h2.ubbbhst_cust_code,
                   h2.ubbbhst_prem_code,
                   h2.ubbbhst_printed_date,
                   h2.ubbbhst_cancel_ind,
                   h2.ubbbhst_tran_num,
                   h2.ubbbhst_prev_bal,
                   h2.ubbbhst_ending_bal
            FROM UBBBHST h2
            JOIN seed_hist s
              ON h2.ubbbhst_cust_code = s.ubbbhst_cust_code
             AND h2.ubbbhst_prem_code = s.ubbbhst_prem_code
            WHERE h2.ubbbhst_cancel_ind IS NULL
              AND h2.ubbbhst_printed_date >= ADD_MONTHS(TRUNC(SYSDATE), -12)
        ),
        urr AS (
            SELECT r.urrshis_cust_code,
                   r.urrshis_prem_code,
                   r.urrshis_serv_num,
                   r.urrshis_reading,
                   r.urrshis_rtyp_code,
                   r.urrshis_action_date,
                   r.urrshis_charge_date,
                   r.urrshis_dos,
                   r.urrshis_consumption,
                   LAG(r.urrshis_action_date) OVER (
                       PARTITION BY r.urrshis_cust_code,
                                    r.urrshis_prem_code,
                                    r.urrshis_serv_num
                       ORDER BY r.urrshis_action_date
                   ) AS prev_action_date
            FROM URRSHIS r
            JOIN seed_hist s
              ON r.urrshis_cust_code = s.ubbbhst_cust_code
             AND r.urrshis_prem_code = s.ubbbhst_prem_code
        ),
        urr_ranges AS (
            SELECT u.*,
                   u.urrshis_action_date AS to_dt,
                   CASE
                       WHEN u.prev_action_date IS NOT NULL THEN u.prev_action_date
                       ELSE u.urrshis_action_date - u.urrshis_dos
                   END AS from_dt
            FROM urr u
        ),
        urr_with_totals AS (
            SELECT ur.*,
                   SUM(ur.urrshis_dos) OVER (
                       PARTITION BY ur.urrshis_cust_code,
                                    ur.urrshis_prem_code
                   ) AS total_dos
            FROM urr_ranges ur
        ),
        weather_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_serv_num,
                   u.urrshis_action_date,
                   NVL(ROUND(AVG(w.ocsweat_avg_temp)), 0)            AS weather_mean_avg_temp,
                   NVL(TRUNC(SUM(w.ocsweat_heating_degree_days)), 0) AS weather_sum_hdd
            FROM urr_with_totals u
            LEFT JOIN UCBPREM p
              ON p.ucbprem_code = u.urrshis_prem_code
            LEFT JOIN OCSWEAT w
              ON w.ocsweat_load_zone_code = p.ucbprem_alternate_location
             AND w.ocsweat_weather_date   > u.from_dt
             AND w.ocsweat_weather_date   < u.to_dt
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_serv_num,
                     u.urrshis_action_date
        ),
        bill_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_charge_date,
                   SUM(u.urrshis_consumption) AS actual_consump_sum,
                   SUM(u.urrshis_dos)         AS days_of_service
            FROM urr_with_totals u
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_charge_date
        )
        SELECT u.urrshis_reading                           AS reading,
               TO_CHAR(h.ubbbhst_printed_date, 'YYYYMMDD') AS bill_date
        FROM all_hist h
        LEFT JOIN urr_with_totals u
          ON u.urrshis_cust_code   = h.ubbbhst_cust_code
         AND u.urrshis_prem_code   = h.ubbbhst_prem_code
         AND u.urrshis_charge_date = h.ubbbhst_printed_date
        LEFT JOIN weather_agg wa
          ON wa.urrshis_cust_code   = u.urrshis_cust_code
         AND wa.urrshis_prem_code   = u.urrshis_prem_code
         AND wa.urrshis_serv_num    = u.urrshis_serv_num
         AND wa.urrshis_action_date = u.urrshis_action_date
        LEFT JOIN UBBCHST c
          ON c.ubbchst_cust_code   = h.ubbbhst_cust_code
         AND c.ubbchst_prem_code   = h.ubbbhst_prem_code
         AND c.ubbchst_charge_date = h.ubbbhst_printed_date
        LEFT JOIN bill_agg ba
          ON ba.urrshis_cust_code   = h.ubbbhst_cust_code
         AND ba.urrshis_prem_code   = h.ubbbhst_prem_code
         AND ba.urrshis_charge_date = h.ubbbhst_printed_date
        ORDER BY h.ubbbhst_printed_date DESC,
                 u.urrshis_serv_num,
                 u.urrshis_action_date
        """;

    public static final String GET_READ_DATE = """
        WITH seed_hist AS (
            SELECT t.ubbbhst_cust_code,
                   t.ubbbhst_prem_code,
                   t.ubbbhst_printed_date,
                   t.ubbbhst_cancel_ind,
                   t.ubbbhst_tran_num,
                   t.ubbbhst_prev_bal,
                   t.ubbbhst_ending_bal
            FROM UBBBHST t
            WHERE t.ubbbhst_cust_code = ?
              AND t.ubbbhst_prem_code = ?
            ORDER BY t.ubbbhst_printed_date DESC
            FETCH FIRST 1 ROWS ONLY
        ),
        all_hist AS (
            SELECT h2.ubbbhst_cust_code,
                   h2.ubbbhst_prem_code,
                   h2.ubbbhst_printed_date,
                   h2.ubbbhst_cancel_ind,
                   h2.ubbbhst_tran_num,
                   h2.ubbbhst_prev_bal,
                   h2.ubbbhst_ending_bal
            FROM UBBBHST h2
            JOIN seed_hist s
              ON h2.ubbbhst_cust_code = s.ubbbhst_cust_code
             AND h2.ubbbhst_prem_code = s.ubbbhst_prem_code
            WHERE h2.ubbbhst_cancel_ind IS NULL
              AND h2.ubbbhst_printed_date >= ADD_MONTHS(TRUNC(SYSDATE), -12)
        ),
        urr AS (
            SELECT r.urrshis_cust_code,
                   r.urrshis_prem_code,
                   r.urrshis_serv_num,
                   r.urrshis_reading,
                   r.urrshis_rtyp_code,
                   r.urrshis_action_date,
                   r.urrshis_charge_date,
                   r.urrshis_dos,
                   r.urrshis_consumption,
                   LAG(r.urrshis_action_date) OVER (
                       PARTITION BY r.urrshis_cust_code,
                                    r.urrshis_prem_code,
                                    r.urrshis_serv_num
                       ORDER BY r.urrshis_action_date
                   ) AS prev_action_date
            FROM URRSHIS r
            JOIN seed_hist s
              ON r.urrshis_cust_code = s.ubbbhst_cust_code
             AND r.urrshis_prem_code = s.ubbbhst_prem_code
        ),
        urr_ranges AS (
            SELECT u.*,
                   u.urrshis_action_date AS to_dt,
                   CASE
                       WHEN u.prev_action_date IS NOT NULL THEN u.prev_action_date
                       ELSE u.urrshis_action_date - u.urrshis_dos
                   END AS from_dt
            FROM urr u
        ),
        urr_with_totals AS (
            SELECT ur.*,
                   SUM(ur.urrshis_dos) OVER (
                       PARTITION BY ur.urrshis_cust_code,
                                    ur.urrshis_prem_code
                   ) AS total_dos
            FROM urr_ranges ur
        ),
        weather_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_serv_num,
                   u.urrshis_action_date,
                   NVL(ROUND(AVG(w.ocsweat_avg_temp)), 0)            AS weather_mean_avg_temp,
                   NVL(TRUNC(SUM(w.ocsweat_heating_degree_days)), 0) AS weather_sum_hdd
            FROM urr_with_totals u
            LEFT JOIN UCBPREM p
              ON p.ucbprem_code = u.urrshis_prem_code
            LEFT JOIN OCSWEAT w
              ON w.ocsweat_load_zone_code = p.ucbprem_alternate_location
             AND w.ocsweat_weather_date   > u.from_dt
             AND w.ocsweat_weather_date   < u.to_dt
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_serv_num,
                     u.urrshis_action_date
        ),
        bill_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_charge_date,
                   SUM(u.urrshis_consumption) AS actual_consump_sum,
                   SUM(u.urrshis_dos)         AS days_of_service
            FROM urr_with_totals u
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_charge_date
        )
        SELECT TO_CHAR(u.urrshis_action_date,  'YYYYMMDD') AS read_date,
               TO_CHAR(h.ubbbhst_printed_date, 'YYYYMMDD') AS bill_date
        FROM all_hist h
        LEFT JOIN urr_with_totals u
          ON u.urrshis_cust_code   = h.ubbbhst_cust_code
         AND u.urrshis_prem_code   = h.ubbbhst_prem_code
         AND u.urrshis_charge_date = h.ubbbhst_printed_date
        LEFT JOIN weather_agg wa
          ON wa.urrshis_cust_code   = u.urrshis_cust_code
         AND wa.urrshis_prem_code   = u.urrshis_prem_code
         AND wa.urrshis_serv_num    = u.urrshis_serv_num
         AND wa.urrshis_action_date = u.urrshis_action_date
        LEFT JOIN UBBCHST c
          ON c.ubbchst_cust_code   = h.ubbbhst_cust_code
         AND c.ubbchst_prem_code   = h.ubbbhst_prem_code
         AND c.ubbchst_charge_date = h.ubbbhst_printed_date
        LEFT JOIN bill_agg ba
          ON ba.urrshis_cust_code   = h.ubbbhst_cust_code
         AND ba.urrshis_prem_code   = h.ubbbhst_prem_code
         AND ba.urrshis_charge_date = h.ubbbhst_printed_date
        ORDER BY h.ubbbhst_printed_date DESC,
                 u.urrshis_serv_num,
                 u.urrshis_action_date
        """;

    public static final String GET_AVERAGE_TEMPERATURE = """
        WITH seed_hist AS (
            SELECT t.ubbbhst_cust_code,
                   t.ubbbhst_prem_code,
                   t.ubbbhst_printed_date,
                   t.ubbbhst_cancel_ind,
                   t.ubbbhst_tran_num,
                   t.ubbbhst_prev_bal,
                   t.ubbbhst_ending_bal
            FROM UBBBHST t
            WHERE t.ubbbhst_cust_code = ?
              AND t.ubbbhst_prem_code = ?
            ORDER BY t.ubbbhst_printed_date DESC
            FETCH FIRST 1 ROWS ONLY
        ),
        all_hist AS (
            SELECT h2.ubbbhst_cust_code,
                   h2.ubbbhst_prem_code,
                   h2.ubbbhst_printed_date,
                   h2.ubbbhst_cancel_ind,
                   h2.ubbbhst_tran_num,
                   h2.ubbbhst_prev_bal,
                   h2.ubbbhst_ending_bal
            FROM UBBBHST h2
            JOIN seed_hist s
              ON h2.ubbbhst_cust_code = s.ubbbhst_cust_code
             AND h2.ubbbhst_prem_code = s.ubbbhst_prem_code
            WHERE h2.ubbbhst_cancel_ind IS NULL
              AND h2.ubbbhst_printed_date >= ADD_MONTHS(TRUNC(SYSDATE), -12)
        ),
        urr AS (
            SELECT r.urrshis_cust_code,
                   r.urrshis_prem_code,
                   r.urrshis_serv_num,
                   r.urrshis_reading,
                   r.urrshis_rtyp_code,
                   r.urrshis_action_date,
                   r.urrshis_charge_date,
                   r.urrshis_dos,
                   r.urrshis_consumption,
                   LAG(r.urrshis_action_date) OVER (
                       PARTITION BY r.urrshis_cust_code,
                                    r.urrshis_prem_code,
                                    r.urrshis_serv_num
                       ORDER BY r.urrshis_action_date
                   ) AS prev_action_date
            FROM URRSHIS r
            JOIN seed_hist s
              ON r.urrshis_cust_code = s.ubbbhst_cust_code
             AND r.urrshis_prem_code = s.ubbbhst_prem_code
        ),
        urr_ranges AS (
            SELECT u.*,
                   u.urrshis_action_date AS to_dt,
                   CASE
                       WHEN u.prev_action_date IS NOT NULL THEN u.prev_action_date
                       ELSE u.urrshis_action_date - u.urrshis_dos
                   END AS from_dt
            FROM urr u
        ),
        urr_with_totals AS (
            SELECT ur.*,
                   SUM(ur.urrshis_dos) OVER (
                       PARTITION BY ur.urrshis_cust_code,
                                    ur.urrshis_prem_code
                   ) AS total_dos
            FROM urr_ranges ur
        ),
        weather_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_serv_num,
                   u.urrshis_action_date,
                   NVL(ROUND(AVG(w.ocsweat_avg_temp)), 0)            AS weather_mean_avg_temp,
                   NVL(TRUNC(SUM(w.ocsweat_heating_degree_days)), 0) AS weather_sum_hdd
            FROM urr_with_totals u
            LEFT JOIN UCBPREM p
              ON p.ucbprem_code = u.urrshis_prem_code
            LEFT JOIN OCSWEAT w
              ON w.ocsweat_load_zone_code = p.ucbprem_alternate_location
             AND w.ocsweat_weather_date   > u.from_dt
             AND w.ocsweat_weather_date   < u.to_dt
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_serv_num,
                     u.urrshis_action_date
        ),
        bill_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_charge_date,
                   SUM(u.urrshis_consumption) AS actual_consump_sum,
                   SUM(u.urrshis_dos)         AS days_of_service
            FROM urr_with_totals u
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_charge_date
        )
        SELECT wa.weather_mean_avg_temp                     AS average_temperature,
               TO_CHAR(h.ubbbhst_printed_date, 'YYYYMMDD')  AS bill_date
        FROM all_hist h
        LEFT JOIN urr_with_totals u
          ON u.urrshis_cust_code   = h.ubbbhst_cust_code
         AND u.urrshis_prem_code   = h.ubbbhst_prem_code
         AND u.urrshis_charge_date = h.ubbbhst_printed_date
        LEFT JOIN weather_agg wa
          ON wa.urrshis_cust_code   = u.urrshis_cust_code
         AND wa.urrshis_prem_code   = u.urrshis_prem_code
         AND wa.urrshis_serv_num    = u.urrshis_serv_num
         AND wa.urrshis_action_date = u.urrshis_action_date
        LEFT JOIN UBBCHST c
          ON c.ubbchst_cust_code   = h.ubbbhst_cust_code
         AND c.ubbchst_prem_code   = h.ubbbhst_prem_code
         AND c.ubbchst_charge_date = h.ubbbhst_printed_date
        LEFT JOIN bill_agg ba
          ON ba.urrshis_cust_code   = h.ubbbhst_cust_code
         AND ba.urrshis_prem_code   = h.ubbbhst_prem_code
         AND ba.urrshis_charge_date = h.ubbbhst_printed_date
        ORDER BY h.ubbbhst_printed_date DESC,
                 u.urrshis_serv_num,
                 u.urrshis_action_date
        """;

    public static final String GET_READ_TYPE = """
        WITH seed_hist AS (
            SELECT t.ubbbhst_cust_code,
                   t.ubbbhst_prem_code,
                   t.ubbbhst_printed_date,
                   t.ubbbhst_cancel_ind,
                   t.ubbbhst_tran_num,
                   t.ubbbhst_prev_bal,
                   t.ubbbhst_ending_bal
            FROM UBBBHST t
            WHERE t.ubbbhst_cust_code = ?
              AND t.ubbbhst_prem_code = ?
            ORDER BY t.ubbbhst_printed_date DESC
            FETCH FIRST 1 ROWS ONLY
        ),
        all_hist AS (
            SELECT h2.ubbbhst_cust_code,
                   h2.ubbbhst_prem_code,
                   h2.ubbbhst_printed_date,
                   h2.ubbbhst_cancel_ind,
                   h2.ubbbhst_tran_num,
                   h2.ubbbhst_prev_bal,
                   h2.ubbbhst_ending_bal
            FROM UBBBHST h2
            JOIN seed_hist s
              ON h2.ubbbhst_cust_code = s.ubbbhst_cust_code
             AND h2.ubbbhst_prem_code = s.ubbbhst_prem_code
            WHERE h2.ubbbhst_cancel_ind IS NULL
              AND h2.ubbbhst_printed_date >= ADD_MONTHS(TRUNC(SYSDATE), -12)
        ),
        urr AS (
            SELECT r.urrshis_cust_code,
                   r.urrshis_prem_code,
                   r.urrshis_serv_num,
                   r.urrshis_reading,
                   r.urrshis_rtyp_code,
                   r.urrshis_action_date,
                   r.urrshis_charge_date,
                   r.urrshis_dos,
                   r.urrshis_consumption,
                   LAG(r.urrshis_action_date) OVER (
                       PARTITION BY r.urrshis_cust_code,
                                    r.urrshis_prem_code,
                                    r.urrshis_serv_num
                       ORDER BY r.urrshis_action_date
                   ) AS prev_action_date
            FROM URRSHIS r
            JOIN seed_hist s
              ON r.urrshis_cust_code = s.ubbbhst_cust_code
             AND r.urrshis_prem_code = s.ubbbhst_prem_code
        ),
        urr_ranges AS (
            SELECT u.*,
                   u.urrshis_action_date AS to_dt,
                   CASE
                       WHEN u.prev_action_date IS NOT NULL THEN u.prev_action_date
                       ELSE u.urrshis_action_date - u.urrshis_dos
                   END AS from_dt
            FROM urr u
        ),
        urr_with_totals AS (
            SELECT ur.*,
                   SUM(ur.urrshis_dos) OVER (
                       PARTITION BY ur.urrshis_cust_code,
                                    ur.urrshis_prem_code
                   ) AS total_dos
            FROM urr_ranges ur
        ),
        weather_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_serv_num,
                   u.urrshis_action_date,
                   NVL(ROUND(AVG(w.ocsweat_avg_temp)), 0)            AS weather_mean_avg_temp,
                   NVL(TRUNC(SUM(w.ocsweat_heating_degree_days)), 0) AS weather_sum_hdd
            FROM urr_with_totals u
            LEFT JOIN UCBPREM p
              ON p.ucbprem_code = u.urrshis_prem_code
            LEFT JOIN OCSWEAT w
              ON w.ocsweat_load_zone_code = p.ucbprem_alternate_location
             AND w.ocsweat_weather_date   > u.from_dt
             AND w.ocsweat_weather_date   < u.to_dt
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_serv_num,
                     u.urrshis_action_date
        ),
        bill_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_charge_date,
                   SUM(u.urrshis_consumption) AS actual_consump_sum,
                   SUM(u.urrshis_dos)         AS days_of_service
            FROM urr_with_totals u
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_charge_date
        )
        SELECT u.urrshis_rtyp_code                         AS read_type_code,
               TO_CHAR(h.ubbbhst_printed_date, 'YYYYMMDD') AS bill_date
        FROM all_hist h
        LEFT JOIN urr_with_totals u
          ON u.urrshis_cust_code   = h.ubbbhst_cust_code
         AND u.urrshis_prem_code   = h.ubbbhst_prem_code
         AND u.urrshis_charge_date = h.ubbbhst_printed_date
        LEFT JOIN weather_agg wa
          ON wa.urrshis_cust_code   = u.urrshis_cust_code
         AND wa.urrshis_prem_code   = u.urrshis_prem_code
         AND wa.urrshis_serv_num    = u.urrshis_serv_num
         AND wa.urrshis_action_date = u.urrshis_action_date
        LEFT JOIN UBBCHST c
          ON c.ubbchst_cust_code   = h.ubbbhst_cust_code
         AND c.ubbchst_prem_code   = h.ubbbhst_prem_code
         AND c.ubbchst_charge_date = h.ubbbhst_printed_date
        LEFT JOIN bill_agg ba
          ON ba.urrshis_cust_code   = h.ubbbhst_cust_code
         AND ba.urrshis_prem_code   = h.ubbbhst_prem_code
         AND ba.urrshis_charge_date = h.ubbbhst_printed_date
        ORDER BY h.ubbbhst_printed_date DESC,
                 u.urrshis_serv_num,
                 u.urrshis_action_date
        """;

    public static final String GET_HEATING_DEGREE_DAYS = """
        WITH seed_hist AS (
            SELECT t.ubbbhst_cust_code, t.ubbbhst_prem_code, t.ubbbhst_printed_date,
                   t.ubbbhst_cancel_ind, t.ubbbhst_tran_num, t.ubbbhst_prev_bal, t.ubbbhst_ending_bal
            FROM UBBBHST t
            WHERE t.ubbbhst_cust_code = ?
              AND t.ubbbhst_prem_code = ?
            ORDER BY t.ubbbhst_printed_date DESC
            FETCH FIRST 1 ROWS ONLY
        ),
        all_hist AS (
            SELECT h2.ubbbhst_cust_code, h2.ubbbhst_prem_code, h2.ubbbhst_printed_date,
                   h2.ubbbhst_cancel_ind, h2.ubbbhst_tran_num, h2.ubbbhst_prev_bal, h2.ubbbhst_ending_bal
            FROM UBBBHST h2
            JOIN seed_hist s ON h2.ubbbhst_cust_code = s.ubbbhst_cust_code
                             AND h2.ubbbhst_prem_code = s.ubbbhst_prem_code
            WHERE h2.ubbbhst_cancel_ind IS NULL
              AND h2.ubbbhst_printed_date >= ADD_MONTHS(TRUNC(SYSDATE), -12)
        ),
        urr AS (
            SELECT r.urrshis_cust_code, r.urrshis_prem_code, r.urrshis_serv_num,
                   r.urrshis_reading, r.urrshis_rtyp_code, r.urrshis_action_date,
                   r.urrshis_charge_date, r.urrshis_dos, r.urrshis_consumption,
                   LAG(r.urrshis_action_date)
                       OVER (PARTITION BY r.urrshis_cust_code, r.urrshis_prem_code, r.urrshis_serv_num
                             ORDER BY r.urrshis_action_date) AS prev_action_date
            FROM URRSHIS r
            JOIN seed_hist s ON r.urrshis_cust_code = s.ubbbhst_cust_code
                             AND r.urrshis_prem_code = s.ubbbhst_prem_code
        ),
        urr_ranges AS (
            SELECT u.*,
                   u.urrshis_action_date AS to_dt,
                   CASE WHEN u.prev_action_date IS NOT NULL THEN u.prev_action_date
                        ELSE u.urrshis_action_date - u.urrshis_dos END AS from_dt
            FROM urr u
        ),
        urr_with_totals AS (
            SELECT ur.*,
                   SUM(ur.urrshis_dos) OVER (PARTITION BY ur.urrshis_cust_code, ur.urrshis_prem_code) AS total_dos
            FROM urr_ranges ur
        ),
        weather_agg AS (
            SELECT u.urrshis_cust_code, u.urrshis_prem_code, u.urrshis_serv_num, u.urrshis_action_date,
                   NVL(ROUND(AVG(w.ocsweat_avg_temp)), 0)            AS weather_mean_avg_temp,
                   NVL(TRUNC(SUM(w.ocsweat_heating_degree_days)), 0) AS weather_sum_hdd
            FROM urr_with_totals u
            LEFT JOIN UCBPREM p  ON p.ucbprem_code = u.urrshis_prem_code
            LEFT JOIN OCSWEAT w  ON w.ocsweat_load_zone_code = p.ucbprem_alternate_location
                                AND w.ocsweat_weather_date > u.from_dt
                                AND w.ocsweat_weather_date < u.to_dt
            GROUP BY u.urrshis_cust_code, u.urrshis_prem_code, u.urrshis_serv_num, u.urrshis_action_date
        ),
        bill_agg AS (
            SELECT u.urrshis_cust_code, u.urrshis_prem_code, u.urrshis_charge_date,
                   SUM(u.urrshis_consumption) AS actual_consump_sum,
                   SUM(u.urrshis_dos)         AS days_of_service
            FROM urr_with_totals u
            GROUP BY u.urrshis_cust_code, u.urrshis_prem_code, u.urrshis_charge_date
        )
        SELECT
            wa.weather_sum_hdd                          AS heating_degree_days,
            TO_CHAR(h.ubbbhst_printed_date, 'YYYYMMDD') AS bill_date
        FROM all_hist h
        LEFT JOIN urr_with_totals u
               ON u.urrshis_cust_code   = h.ubbbhst_cust_code
              AND u.urrshis_prem_code   = h.ubbbhst_prem_code
              AND u.urrshis_charge_date = h.ubbbhst_printed_date
        LEFT JOIN weather_agg wa
               ON wa.urrshis_cust_code   = u.urrshis_cust_code
              AND wa.urrshis_prem_code   = u.urrshis_prem_code
              AND wa.urrshis_serv_num    = u.urrshis_serv_num
              AND wa.urrshis_action_date = u.urrshis_action_date
        LEFT JOIN UBBCHST c
               ON c.ubbchst_cust_code   = h.ubbbhst_cust_code
              AND c.ubbchst_prem_code   = h.ubbbhst_prem_code
              AND c.ubbchst_charge_date = h.ubbbhst_printed_date
        LEFT JOIN bill_agg ba
               ON ba.urrshis_cust_code   = h.ubbbhst_cust_code
              AND ba.urrshis_prem_code   = h.ubbbhst_prem_code
              AND ba.urrshis_charge_date = h.ubbbhst_printed_date
        ORDER BY h.ubbbhst_printed_date DESC, u.urrshis_serv_num, u.urrshis_action_date
        """;

    public static final String GET_BILL_HISTORY_TRANSACTION = """
        WITH seed_hist AS (
            SELECT t.ubbbhst_cust_code, t.ubbbhst_prem_code, t.ubbbhst_printed_date,
                   t.ubbbhst_cancel_ind, t.ubbbhst_tran_num, t.ubbbhst_prev_bal, t.ubbbhst_ending_bal
            FROM UBBBHST t
            WHERE t.ubbbhst_cust_code = ?
              AND t.ubbbhst_prem_code = ?
            ORDER BY t.ubbbhst_printed_date DESC
            FETCH FIRST 1 ROWS ONLY
        ),
        all_hist AS (
            SELECT h2.ubbbhst_cust_code, h2.ubbbhst_prem_code, h2.ubbbhst_printed_date,
                   h2.ubbbhst_cancel_ind, h2.ubbbhst_tran_num, h2.ubbbhst_prev_bal, h2.ubbbhst_ending_bal
            FROM UBBBHST h2
            JOIN seed_hist s ON h2.ubbbhst_cust_code = s.ubbbhst_cust_code
                             AND h2.ubbbhst_prem_code = s.ubbbhst_prem_code
            WHERE h2.ubbbhst_cancel_ind IS NULL
              AND h2.ubbbhst_printed_date >= ADD_MONTHS(TRUNC(SYSDATE), -12)
        ),
        urr AS (
            SELECT r.urrshis_cust_code, r.urrshis_prem_code, r.urrshis_serv_num,
                   r.urrshis_reading, r.urrshis_rtyp_code, r.urrshis_action_date,
                   r.urrshis_charge_date, r.urrshis_dos, r.urrshis_consumption,
                   LAG(r.urrshis_action_date)
                       OVER (PARTITION BY r.urrshis_cust_code, r.urrshis_prem_code, r.urrshis_serv_num
                             ORDER BY r.urrshis_action_date) AS prev_action_date
            FROM URRSHIS r
            JOIN seed_hist s ON r.urrshis_cust_code = s.ubbbhst_cust_code
                             AND r.urrshis_prem_code = s.ubbbhst_prem_code
        ),
        urr_ranges AS (
            SELECT u.*,
                   u.urrshis_action_date AS to_dt,
                   CASE WHEN u.prev_action_date IS NOT NULL THEN u.prev_action_date
                        ELSE u.urrshis_action_date - u.urrshis_dos END AS from_dt
            FROM urr u
        ),
        urr_with_totals AS (
            SELECT ur.*,
                   SUM(ur.urrshis_dos) OVER (PARTITION BY ur.urrshis_cust_code, ur.urrshis_prem_code) AS total_dos
            FROM urr_ranges ur
        ),
        weather_agg AS (
            SELECT u.urrshis_cust_code, u.urrshis_prem_code, u.urrshis_serv_num, u.urrshis_action_date,
                   NVL(ROUND(AVG(w.ocsweat_avg_temp)), 0)            AS weather_mean_avg_temp,
                   NVL(TRUNC(SUM(w.ocsweat_heating_degree_days)), 0) AS weather_sum_hdd
            FROM urr_with_totals u
            LEFT JOIN UCBPREM p  ON p.ucbprem_code = u.urrshis_prem_code
            LEFT JOIN OCSWEAT w  ON w.ocsweat_load_zone_code = p.ucbprem_alternate_location
                                AND w.ocsweat_weather_date > u.from_dt
                                AND w.ocsweat_weather_date < u.to_dt
            GROUP BY u.urrshis_cust_code, u.urrshis_prem_code, u.urrshis_serv_num, u.urrshis_action_date
        ),
        bill_agg AS (
            SELECT u.urrshis_cust_code, u.urrshis_prem_code, u.urrshis_charge_date,
                   SUM(u.urrshis_consumption) AS actual_consump_sum,
                   SUM(u.urrshis_dos)         AS days_of_service
            FROM urr_with_totals u
            GROUP BY u.urrshis_cust_code, u.urrshis_prem_code, u.urrshis_charge_date
        )
        SELECT
            h.ubbbhst_tran_num                          AS bill_history_transaction_number,
            TO_CHAR(h.ubbbhst_printed_date, 'YYYYMMDD') AS bill_date
        FROM all_hist h
        LEFT JOIN urr_with_totals u
               ON u.urrshis_cust_code   = h.ubbbhst_cust_code
              AND u.urrshis_prem_code   = h.ubbbhst_prem_code
              AND u.urrshis_charge_date = h.ubbbhst_printed_date
        LEFT JOIN weather_agg wa
               ON wa.urrshis_cust_code   = u.urrshis_cust_code
              AND wa.urrshis_prem_code   = u.urrshis_prem_code
              AND wa.urrshis_serv_num    = u.urrshis_serv_num
              AND wa.urrshis_action_date = u.urrshis_action_date
        LEFT JOIN UBBCHST c
               ON c.ubbchst_cust_code   = h.ubbbhst_cust_code
              AND c.ubbchst_prem_code   = h.ubbbhst_prem_code
              AND c.ubbchst_charge_date = h.ubbbhst_printed_date
        LEFT JOIN bill_agg ba
               ON ba.urrshis_cust_code   = h.ubbbhst_cust_code
              AND ba.urrshis_prem_code   = h.ubbbhst_prem_code
              AND ba.urrshis_charge_date = h.ubbbhst_printed_date
        ORDER BY h.ubbbhst_printed_date DESC, u.urrshis_serv_num, u.urrshis_action_date
        """;

    public static final String GET_BILL_INFO = """
            WITH
            params AS (
              SELECT
                  ? AS cust_code,
                  ? AS prem_code
              FROM dual
            ),
            bill_candidates AS (
              SELECT h.*
              FROM UBBBHST h
              JOIN params p
                ON p.cust_code = h.ubbbhst_cust_code
               AND p.prem_code = h.ubbbhst_prem_code
              WHERE NVL(h.ubbbhst_cancel_ind, 0) = 0
            ),
            zero_balance_candidates AS (
              SELECT
                  h.ubbbhst_tran_num,
                  h.ubbbhst_printed_date
              FROM bill_candidates h
              WHERE NVL(h.ubbbhst_ending_bal, 0) = 0
            ),
            zero_bal_print_rows AS (
              SELECT
                  z.ubbbhst_tran_num,
                  z.ubbbhst_printed_date,
                  o.uabopen_srat_code,
                  o.uabopen_scat_code,
                  o.uabopen_billed_chg,
                  r.utrsrat_bill_print_desc
              FROM zero_balance_candidates z
              JOIN UABOPEN o
                ON o.uabopen_bhst_tran_num = z.ubbbhst_tran_num
               AND o.uabopen_printed_ind   = 'Y'
              JOIN UTRSRAT r
                ON r.utrsrat_srat_code = o.uabopen_srat_code
               AND r.utrsrat_scat_code = o.uabopen_scat_code
               AND r.utrsrat_effect_date = (
                 SELECT MAX(r2.utrsrat_effect_date)
                 FROM UTRSRAT r2
                 WHERE r2.utrsrat_srat_code = o.uabopen_srat_code
                   AND r2.utrsrat_scat_code = o.uabopen_scat_code
                   AND r2.utrsrat_effect_date <= z.ubbbhst_printed_date
               )
            ),
            gas_eligible_zero_balance_bills AS (
              SELECT DISTINCT
                  z.ubbbhst_tran_num
              FROM zero_bal_print_rows z
              WHERE LOWER(z.utrsrat_bill_print_desc) LIKE '%gas%'
                AND LOWER(z.utrsrat_bill_print_desc) LIKE '%charge%'
            ),
            eligible_bills AS (
              SELECT
                  h.*,
                  CASE
                    WHEN NVL(h.ubbbhst_ending_bal, 0) <> 0 THEN 1
                    WHEN g.ubbbhst_tran_num IS NOT NULL   THEN 1
                    ELSE 0
                  END AS is_eligible
              FROM bill_candidates h
              LEFT JOIN gas_eligible_zero_balance_bills g
                ON g.ubbbhst_tran_num = h.ubbbhst_tran_num
            ),
            current_bill AS (
              SELECT *
              FROM (
                SELECT
                    h.*,
                    ROW_NUMBER() OVER (
                      ORDER BY h.ubbbhst_printed_date DESC,
                               h.ubbbhst_tran_num DESC
                    ) AS rn
                FROM eligible_bills h
                WHERE h.is_eligible = 1
              )
              WHERE rn = 1
            ),
            billing_window AS (
              SELECT
                  MAX(r.urrshis_action_date) AS billToDate,
                  SUM(r.urrshis_dos)         AS daysOfService,
                  MAX(r.urrshis_action_date) - SUM(r.urrshis_dos) AS billFromDate
              FROM URRSHIS r
              JOIN current_bill cb
                ON cb.ubbbhst_cust_code = r.urrshis_cust_code
               AND cb.ubbbhst_prem_code = r.urrshis_prem_code
              WHERE r.urrshis_action_date = (
                SELECT MAX(r2.urrshis_action_date)
                FROM URRSHIS r2
                WHERE r2.urrshis_cust_code = cb.ubbbhst_cust_code
                  AND r2.urrshis_prem_code = cb.ubbbhst_prem_code
                  AND r2.urrshis_action_date <= cb.ubbbhst_printed_date
              )
            ),
            payments_since_bill AS (
              SELECT
                  NVL(SUM(p.uabpymt_amount), 0) AS paymentsSinceLastBill
              FROM UABPYMT p
              JOIN current_bill cb
                ON cb.ubbbhst_cust_code = p.uabpymt_cust_code
               AND cb.ubbbhst_prem_code = p.uabpymt_prem_code
              WHERE p.uabpymt_pymt_date > cb.ubbbhst_printed_date
            ),
            bill_financials AS (
              SELECT
                  NVL(SUM(o.uabopen_billed_chg), 0) AS currentCharges
              FROM UABOPEN o
              JOIN current_bill cb
                ON cb.ubbbhst_tran_num = o.uabopen_bhst_tran_num
            ),
            bill_due_info AS (
              SELECT
                  MAX(o.uabopen_due_date) AS billDueDate
              FROM UABOPEN o
              JOIN current_bill cb
                ON cb.ubbbhst_tran_num = o.uabopen_bhst_tran_num
              WHERE o.uabopen_printed_ind = 'Y'
            ),
            past_due_info AS (
              SELECT
                  pd.pastDueAmount,
                  CASE
                    WHEN pd.pastDueAmount > 0 THEN bdi.billDueDate
                    ELSE NULL
                  END AS pastDueDate
              FROM (
                SELECT
                  GCISMGR.F_GET_PAST_DUE_AMT(cb.ubbbhst_cust_code, cb.ubbbhst_prem_code) AS pastDueAmount
                FROM current_bill cb
              ) pd
              LEFT JOIN bill_due_info bdi
                ON 1 = 1
            ),
            budget_info AS (
              SELECT
                (
                  SELECT b.uabbudg_bdgt_bill_amt
                  FROM UABBUDG b
                  WHERE b.uabbudg_cust_code = cb.ubbbhst_cust_code
                    AND b.uabbudg_prem_code = cb.ubbbhst_prem_code
                    AND b.uabbudg_bdgt_start_date <= cb.ubbbhst_printed_date
                  ORDER BY b.uabbudg_bdgt_start_date DESC
                  FETCH FIRST 1 ROW ONLY
                ) AS budgetBillingAmount,
                (
                  SELECT SUM(o.uabopen_budget_variance)
                  FROM UABOPEN o
                  WHERE o.uabopen_bhst_tran_num = cb.ubbbhst_tran_num
                    AND o.uabopen_item_type = 'B'
                ) AS budgetBillingTotalVariance,
                (
                  SELECT b.uabbudg_bdgt_start_date
                  FROM UABBUDG b
                  WHERE b.uabbudg_cust_code = cb.ubbbhst_cust_code
                    AND b.uabbudg_prem_code = cb.ubbbhst_prem_code
                    AND b.uabbudg_bdgt_start_date <= cb.ubbbhst_printed_date
                  ORDER BY b.uabbudg_bdgt_start_date DESC
                  FETCH FIRST 1 ROW ONLY
                ) AS budgetBillingStartDate
              FROM current_bill cb
            ),
            current_bill_print_rows AS (
              SELECT
                  o.uabopen_bhst_tran_num,
                  o.uabopen_srat_code        AS code,
                  o.uabopen_scat_code,
                  o.uabopen_billed_chg       AS amount,
                  r.utrsrat_bill_print_desc  AS description
              FROM UABOPEN o
              JOIN current_bill cb
                ON cb.ubbbhst_tran_num = o.uabopen_bhst_tran_num
              JOIN UTRSRAT r
                ON r.utrsrat_srat_code = o.uabopen_srat_code
               AND r.utrsrat_scat_code = o.uabopen_scat_code
               AND r.utrsrat_effect_date = (
                 SELECT MAX(r2.utrsrat_effect_date)
                 FROM UTRSRAT r2
                 WHERE r2.utrsrat_srat_code = o.uabopen_srat_code
                   AND r2.utrsrat_scat_code = o.uabopen_scat_code
                   AND r2.utrsrat_effect_date <= cb.ubbbhst_printed_date
               )
              WHERE o.uabopen_printed_ind = 'Y'
            ),
            bill_codes AS (
              SELECT DISTINCT
                  x.code
              FROM current_bill_print_rows x
            ),
            effective_reward_codes AS (
              SELECT DISTINCT
                  bc.code AS reward_code
              FROM bill_codes bc
              JOIN current_bill cb
                ON 1 = 1
              JOIN GZRRWDR g
                ON g.gzrrwdr_srat_code = bc.code
               AND cb.ubbbhst_printed_date >= g.gzrrwdr_start_date
               AND (
                     g.gzrrwdr_end_date IS NULL
                  OR cb.ubbbhst_printed_date <= g.gzrrwdr_end_date
                  OR NVL(g.gzrrwdr_allow_expired_ind, 'N') = 'Y'
               )
            ),
            classified_lines AS (
              SELECT
                  x.code,
                  x.description,
                  x.amount,
                  CASE
                    WHEN erc.reward_code IS NOT NULL
                      THEN 'REWARD'
                    WHEN LOWER(x.description) LIKE '%base%'
                     AND LOWER(x.description) LIKE '%charge%'
                      THEN 'BASE'
                    WHEN LOWER(x.description) LIKE '%customer%'
                     AND LOWER(x.description) LIKE '%service%'
                     AND LOWER(x.description) LIKE '%charge%'
                      THEN 'SERVICE'
                    WHEN LOWER(x.description) LIKE '%gas%'
                     AND LOWER(x.description) LIKE '%charge%'
                      THEN 'GAS'
                    WHEN LOWER(x.description) LIKE '%guaranteed%'
                     AND LOWER(x.description) LIKE '%amount%'
                      THEN 'GUARANTEE'
                    WHEN LOWER(x.description) LIKE '%interstate%'
                     AND LOWER(x.description) LIKE '%capacity%'
                      THEN 'INTERSTATE'
                    WHEN LOWER(x.description) LIKE '%discount%'
                      OR LOWER(x.description) LIKE '%promo%'
                      THEN 'DISCOUNT'
                    WHEN LOWER(x.description) LIKE '%tax%'
                      THEN 'TAX'
                    WHEN NVL(x.amount, 0) < 0
                      THEN 'MISCCREDIT'
                    ELSE 'MISCCHARGE'
                  END AS category
              FROM current_bill_print_rows x
              LEFT JOIN effective_reward_codes erc
                ON erc.reward_code = x.code
            ),
            non_tax_lines AS (
              SELECT
                  c.code,
                  c.description,
                  SUM(c.amount) AS amount,
                  c.category
              FROM classified_lines c
              WHERE c.category <> 'TAX'
              GROUP BY
                  c.code,
                  c.description,
                  c.category
            ),
            tax_line AS (
              SELECT
                  MIN(c.code)     AS code,
                  'Sales Tax'     AS description,
                  SUM(c.amount)   AS amount,
                  'TAX'           AS category
              FROM classified_lines c
              WHERE c.category = 'TAX'
            ),
            bill_lines AS (
              SELECT
                  n.code,
                  n.description,
                  n.amount,
                  n.category,
                  CASE n.category
                    WHEN 'BASE'       THEN 1
                    WHEN 'SERVICE'    THEN 2
                    WHEN 'GAS'        THEN 3
                    WHEN 'GUARANTEE'  THEN 4
                    WHEN 'INTERSTATE' THEN 5
                    WHEN 'DISCOUNT'   THEN 6
                    WHEN 'REWARD'     THEN 7
                    WHEN 'MISCCHARGE' THEN 8
                    WHEN 'MISCCREDIT' THEN 9
                    WHEN 'TAX'        THEN 10
                  END AS category_sort_order
              FROM non_tax_lines n
              UNION ALL
              SELECT
                  t.code,
                  t.description,
                  t.amount,
                  t.category,
                  10 AS category_sort_order
              FROM tax_line t
              WHERE t.amount IS NOT NULL
            )
            SELECT
                cb.ubbbhst_cust_code AS customerCode,
                cb.ubbbhst_prem_code AS premisesCode,
                TO_CHAR(cb.ubbbhst_printed_date, 'YYYYMMDD') AS billDate,
                TO_CHAR(bw.billFromDate, 'YYYYMMDD')         AS billFromDate,
                TO_CHAR(bw.billToDate,   'YYYYMMDD')         AS billToDate,
                pdi.pastDueAmount AS pastDueAmount,
                TO_CHAR(pdi.pastDueDate, 'YYYYMMDD') AS pastDueDate,
                cb.ubbbhst_prev_bal AS previousBillAmount,
                cb.ubbbhst_payments AS paymentsApplied,
                (cb.ubbbhst_prev_bal - cb.ubbbhst_payments) AS balanceBroughtForward,
                NVL(bf.currentCharges, 0) AS currentCharges,
                NVL(psb.paymentsSinceLastBill, 0) AS paymentsSinceLastBill,
                GCISMGR.F_CALCARBALANCE(cb.ubbbhst_cust_code, cb.ubbbhst_prem_code) AS currentBalance,
                cb.ubbbhst_ending_bal AS totalAmountDue,
                TO_CHAR(bdi.billDueDate, 'YYYYMMDD') AS billDueDate,
                bi.budgetBillingAmount,
                bi.budgetBillingTotalVariance,
                TO_CHAR(bi.budgetBillingStartDate, 'YYYYMMDD') AS budgetBillingStartDate,
                bl.code,
                bl.description,
                bl.amount,
                bl.category,
                bw.daysOfService
            FROM current_bill cb
            LEFT JOIN billing_window bw ON 1 = 1
            LEFT JOIN payments_since_bill psb ON 1 = 1
            LEFT JOIN bill_financials bf ON 1 = 1
            LEFT JOIN bill_due_info bdi ON 1 = 1
            LEFT JOIN past_due_info pdi ON 1 = 1
            LEFT JOIN budget_info bi ON 1 = 1
            LEFT JOIN bill_lines bl ON 1 = 1
            ORDER BY
                bl.category_sort_order,
                bl.description ASC,
                bl.amount DESC
    """;


    public static final String GET_USAGE_HISTORY = """
            WITH seed_hist AS (
                SELECT t.ubbbhst_cust_code,
                       t.ubbbhst_prem_code,
                       t.ubbbhst_printed_date,
                       t.ubbbhst_cancel_ind,
                       t.ubbbhst_tran_num,
                       t.ubbbhst_prev_bal,
                       t.ubbbhst_ending_bal
                FROM UBBBHST t
                WHERE t.ubbbhst_cust_code = ?
                  AND t.ubbbhst_prem_code = ?
                ORDER BY t.ubbbhst_printed_date DESC
                FETCH FIRST 1 ROWS ONLY
            ),
            all_hist AS (
                SELECT h2.ubbbhst_cust_code,
                       h2.ubbbhst_prem_code,
                       h2.ubbbhst_printed_date,
                       h2.ubbbhst_cancel_ind,
                       h2.ubbbhst_tran_num,
                       h2.ubbbhst_prev_bal,
                       h2.ubbbhst_ending_bal
                FROM UBBBHST h2
                JOIN seed_hist s
                  ON h2.ubbbhst_cust_code = s.ubbbhst_cust_code
                 AND h2.ubbbhst_prem_code = s.ubbbhst_prem_code
                WHERE h2.ubbbhst_cancel_ind IS NULL
                  AND h2.ubbbhst_printed_date >= ADD_MONTHS(TRUNC(SYSDATE), -12)
            ),
            urr AS (
                SELECT r.urrshis_cust_code,
                       r.urrshis_prem_code,
                       r.urrshis_serv_num,
                       r.urrshis_reading,
                       r.urrshis_rtyp_code,
                       r.urrshis_action_date,
                       r.urrshis_charge_date,
                       r.urrshis_dos,
                       r.urrshis_consumption,
                       LAG(r.urrshis_action_date)
                           OVER (PARTITION BY r.urrshis_cust_code,
                                              r.urrshis_prem_code,
                                              r.urrshis_serv_num
                                 ORDER BY r.urrshis_action_date) AS prev_action_date
                FROM URRSHIS r
                JOIN seed_hist s
                  ON r.urrshis_cust_code = s.ubbbhst_cust_code
                 AND r.urrshis_prem_code = s.ubbbhst_prem_code
            ),
            urr_ranges AS (
                SELECT\s
                    u.*,
                    u.urrshis_action_date AS to_dt,
                    CASE
                        WHEN u.prev_action_date IS NOT NULL THEN u.prev_action_date
                        ELSE u.urrshis_action_date - u.urrshis_dos
                    END AS from_dt
                FROM urr u
            ),
            urr_with_totals AS (
                SELECT\s
                    ur.*,
                    SUM(ur.urrshis_dos) OVER (
                        PARTITION BY ur.urrshis_cust_code,
                                     ur.urrshis_prem_code
                    ) AS total_dos
                FROM urr_ranges ur
            ),
            weather_agg AS (
                SELECT u.urrshis_cust_code,
                       u.urrshis_prem_code,
                       u.urrshis_serv_num,
                       u.urrshis_action_date,
                       ROUND(SUM(ocsweat_avg_temp)/COUNT(1),1)              AS weather_mean_avg_temp,
                       ROUND(SUM(NVL(ocsweat_heating_degree_days,0)),1)     AS weather_sum_hdd
                FROM urr_with_totals u
                LEFT JOIN UCBPREM p
                  ON p.ucbprem_code = u.urrshis_prem_code
                LEFT JOIN OCSWEAT w
                  ON w.ocsweat_load_zone_code = p.ucbprem_alternate_location
                 AND w.ocsweat_weather_date   > u.from_dt
                 AND w.ocsweat_weather_date   < u.to_dt
                GROUP BY u.urrshis_cust_code,
                         u.urrshis_prem_code,
                         u.urrshis_serv_num,
                         u.urrshis_action_date
            ),
            bill_agg AS (
                SELECT u.urrshis_cust_code,
                       u.urrshis_prem_code,
                       u.urrshis_charge_date,
                       SUM(u.urrshis_consumption) AS actual_consump_sum,
                       SUM(u.urrshis_dos)         AS days_of_service
                FROM urr_with_totals u
                GROUP BY u.urrshis_cust_code,
                         u.urrshis_prem_code,
                         u.urrshis_charge_date
            )
            SELECT u.urrshis_serv_num                                                             AS service_number,
                   TO_CHAR(h.ubbbhst_printed_date, 'YYYYMMDD')                                   AS bill_date,
                   TO_CHAR(u.from_dt, 'YYYYMMDD')                                                AS usage_from_date,
                   TO_CHAR(u.to_dt,   'YYYYMMDD')                                                AS usage_to_date,
                   TRUNC(ba.actual_consump_sum / NULLIF(ba.days_of_service, 0), 3)               AS avg_daily_actual_consumption,
                   TRUNC(ROUND(c.ubbchst_billed_consump / NULLIF(ba.days_of_service, 0), 4), 3) AS avg_daily_billed_consumption,
                   c.ubbchst_billed_consump                                                      AS total_billed_consumption,
                   ba.days_of_service                                                            AS days_of_service,
                   u.urrshis_reading                                                             AS reading,
                   u.urrshis_rtyp_code                                                           AS read_type_code,
                   TO_CHAR(u.urrshis_action_date, 'YYYYMMDD')                                    AS read_date,
                   wa.weather_mean_avg_temp                                                      AS average_temperature,
                   wa.weather_sum_hdd                                                            AS heating_degree_days,
                   h.ubbbhst_tran_num                                                            AS bill_history_transaction_number,
                   COUNT(*) OVER ()                                                              AS number_of_matches
            FROM all_hist h
            LEFT JOIN urr_with_totals u
              ON  u.urrshis_cust_code   = h.ubbbhst_cust_code
              AND u.urrshis_prem_code   = h.ubbbhst_prem_code
              AND u.urrshis_charge_date = h.ubbbhst_printed_date
            LEFT JOIN weather_agg wa
              ON  wa.urrshis_cust_code   = u.urrshis_cust_code
              AND wa.urrshis_prem_code   = u.urrshis_prem_code
              AND wa.urrshis_serv_num    = u.urrshis_serv_num
              AND wa.urrshis_action_date = u.urrshis_action_date
            LEFT JOIN UBBCHST c
              ON  c.ubbchst_cust_code   = h.ubbbhst_cust_code
              AND c.ubbchst_prem_code   = h.ubbbhst_prem_code
              AND c.ubbchst_charge_date = h.ubbbhst_printed_date
            LEFT JOIN bill_agg ba
              ON  ba.urrshis_cust_code   = h.ubbbhst_cust_code
              AND ba.urrshis_prem_code   = h.ubbbhst_prem_code
              AND ba.urrshis_charge_date = h.ubbbhst_printed_date
            ORDER BY h.ubbbhst_printed_date DESC,
                     u.urrshis_serv_num,
                     u.urrshis_action_date
          """;

    public static final String GET_BANK_DRAFT_INFO = """
        SELECT
            a.ucracct_cust_code         AS customer_code,
            a.ucracct_prem_code         AS premises_code,
            a.ucracct_draft_acct_status AS draft_status,
            a.ucracct_check_saving_ind  AS account_type,
            c.ucbcust_last_name         AS bank_name
        FROM UCRACCT a
        JOIN UTRBANK b
            ON a.ucracct_bank_code      = b.utrbank_code
        JOIN UCBCUST c
            ON b.utrbank_cust_code_bank = c.ucbcust_cust_code
        WHERE a.ucracct_cust_code       = ?
          AND a.ucracct_prem_code       = ?
          AND a.ucracct_draft_acct_status IS NOT NULL
          AND b.utrbank_status          = 'A'
          AND a.ucracct_bank_acct       IS NOT NULL
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_BANK_DRAFT_INFO2 = """
        SELECT
            a.ucracct_cust_code         AS customer_code,
            a.ucracct_prem_code         AS premises_code,
            a.ucracct_draft_acct_status AS draft_status,
            a.ucracct_check_saving_ind  AS account_type,
            c.ucbcust_last_name         AS bank_name
        FROM UCRACCT a
        JOIN UTRBANK b
            ON a.ucracct_bank_code      = b.utrbank_code
        JOIN UCBCUST c
            ON b.utrbank_cust_code_bank = c.ucbcust_cust_code
        WHERE a.ucracct_cust_code       = ?
          AND a.ucracct_prem_code       = ?
          AND a.ucracct_draft_acct_status IS NOT NULL
          AND b.utrbank_status          = 'A'
          AND a.ucracct_draft_acct_status = 'P'
          AND a.ucracct_bank_acct       IS NOT NULL
        FETCH FIRST 1 ROWS ONLY
        """;

    public static final String GET_PAYMENT_ARRANGEMENT_INFO= """
            SELECT
                h.UABPYAR_CUST_CODE                              AS custCode,
                h.UABPYAR_PREM_CODE                              AS premCode,
                h.UABPYAR_ARRNG_NUM                              AS paNumber,
                h.UABPYAR_PYAR_CODE                              AS paTypeCode,
                h.UABPYAR_TOTAL_AMT                              AS paTotalAmount,
                TO_CHAR(h.UABPYAR_DATE_CREATED, 'YYYYMMDD')      AS paDateCreated,
                COUNT(*) OVER (
                    PARTITION BY h.UABPYAR_CUST_CODE,
                                 h.UABPYAR_PREM_CODE,
                                 h.UABPYAR_ARRNG_NUM
                )                                                AS numberOfInstallments,
                h.UABPYAR_STATUS                                 AS status,
                d.UARPYAR_AMT_DUE                                AS amountDue,
                d.UARPYAR_BALANCE                                AS balance,
                TO_CHAR(d.UARPYAR_DATE_DUE, 'YYYYMMDD')          AS dateDue,
                TO_CHAR(d.UARPYAR_DATE_PAID_IN_FULL, 'YYYYMMDD') AS datePaid
            FROM UABPYAR h
            JOIN UARPYAR d
                ON  d.UARPYAR_CUST_CODE = h.UABPYAR_CUST_CODE
                AND d.UARPYAR_PREM_CODE = h.UABPYAR_PREM_CODE
                AND d.UARPYAR_ARRNG_NUM = h.UABPYAR_ARRNG_NUM
            WHERE h.UABPYAR_CUST_CODE  = ?
              AND h.UABPYAR_PREM_CODE  = ?
              AND h.UABPYAR_STATUS     = 'A'
            ORDER BY
                h.UABPYAR_CUST_CODE,
                h.UABPYAR_PREM_CODE,
                h.UABPYAR_ARRNG_NUM,
                d.UARPYAR_DATE_DUE NULLS LAST
            """;

    public static final String GET_PAYMENT_HISTORY = """
            WITH
            cfg AS (
              SELECT
                ? AS cust_code,
                ? AS prem_code,
                NVL(P_PARM_GET('SPK_ACCOUNT_UTIL','PYMT_REV_CODE'), 'NSF') AS rev_code,
                LEAST(24, NVL(P_PARM_GET('SPK_ACCOUNT_UTIL','PYMT_HIST_MAX_MONTHS'), 99)) AS max_months
              FROM dual
            ),
            win AS (
              SELECT
                ADD_MONTHS(TRUNC(SYSDATE), -max_months) AS start_dt,
                SYSDATE AS end_dt,
                cust_code, prem_code, rev_code
              FROM cfg
            ),
            neg_strict AS (
              SELECT
                p.uabpymt_cust_code    AS cust_code,
                p.uabpymt_prem_code    AS prem_code,
                p.uabpymt_ar_trans     AS ar_trans,
                p.uabpymt_cancel_trans AS cancel_trans,
                p.uabpymt_pymt_date    AS pymt_date
              FROM UABPYMT p
              CROSS JOIN win w
              WHERE p.uabpymt_cust_code = w.cust_code
                AND p.uabpymt_prem_code = w.prem_code
                AND p.uabpymt_pymt_date BETWEEN w.start_dt AND w.end_dt
                AND p.uabpymt_amount < 0
                AND EXISTS (
                      SELECT 1
                      FROM UABOPEN o
                      WHERE o.uabopen_cust_code          = p.uabpymt_cust_code
                        AND o.uabopen_prem_code          = p.uabpymt_prem_code
                        AND TRUNC(o.uabopen_charge_date) = TRUNC(p.uabpymt_pymt_date)
                        AND o.uabopen_srat_code          = w.rev_code
                    )
            ),
            posted AS (
              SELECT
                p.uabpymt_cust_code    AS cust_code,
                p.uabpymt_prem_code    AS prem_code,
                p.uabpymt_pymt_date    AS paymentdate,
                p.uabpymt_amount       AS paymentamount,
                p.uabpymt_pycd_code    AS paymentcode,
                p.uabpymt_ar_trans     AS ar_trans,
                p.uabpymt_cancel_trans AS cancel_trans,
                CASE
                  WHEN p.uabpymt_amount > 0 AND p.uabpymt_cancel_trans IS NULL
                       AND NOT EXISTS (
                             SELECT 1
                             FROM UABPYMT n, win w
                             WHERE n.uabpymt_cust_code = p.uabpymt_cust_code
                               AND n.uabpymt_prem_code = p.uabpymt_prem_code
                               AND n.uabpymt_amount    < 0
                               AND n.uabpymt_pymt_date BETWEEN w.start_dt AND w.end_dt
                               AND (
                                     n.uabpymt_ar_trans     = p.uabpymt_ar_trans
                                  OR n.uabpymt_ar_trans     = p.uabpymt_cancel_trans
                                  OR n.uabpymt_cancel_trans = p.uabpymt_ar_trans
                                   )
                           )
                    THEN 'REG_POS'
                  WHEN p.uabpymt_amount > 0 AND p.uabpymt_cancel_trans IS NOT NULL
                       AND EXISTS (
                             SELECT 1
                             FROM UABPYMT n, win w
                             WHERE n.uabpymt_cust_code = p.uabpymt_cust_code
                               AND n.uabpymt_prem_code = p.uabpymt_prem_code
                               AND n.uabpymt_amount    < 0
                               AND n.uabpymt_pymt_date BETWEEN w.start_dt AND w.end_dt
                               AND (
                                     n.uabpymt_ar_trans     = p.uabpymt_ar_trans
                                  OR n.uabpymt_ar_trans     = p.uabpymt_cancel_trans
                                  OR n.uabpymt_cancel_trans = p.uabpymt_ar_trans
                                   )
                           )
                       AND EXISTS (
                             SELECT 1
                             FROM UABOPEN o
                             WHERE o.uabopen_cust_code          = p.uabpymt_cust_code
                               AND o.uabopen_prem_code          = p.uabpymt_prem_code
                               AND TRUNC(o.uabopen_charge_date) = TRUNC(p.uabpymt_pymt_date)
                               AND o.uabopen_srat_code          = (SELECT rev_code FROM win)
                           )
                    THEN 'CXL_POS_POS_UO'
                  WHEN p.uabpymt_amount > 0 AND p.uabpymt_cancel_trans IS NOT NULL
                       AND EXISTS (
                             SELECT 1
                             FROM UABPYMT n, win w
                             WHERE n.uabpymt_cust_code = p.uabpymt_cust_code
                               AND n.uabpymt_prem_code = p.uabpymt_prem_code
                               AND n.uabpymt_amount    < 0
                               AND n.uabpymt_pymt_date BETWEEN w.start_dt AND w.end_dt
                               AND (
                                     n.uabpymt_ar_trans     = p.uabpymt_ar_trans
                                  OR n.uabpymt_ar_trans     = p.uabpymt_cancel_trans
                                  OR n.uabpymt_cancel_trans = p.uabpymt_ar_trans
                                   )
                           )
                       AND EXISTS (
                             SELECT 1
                             FROM neg_strict nx
                             WHERE nx.cust_code = p.uabpymt_cust_code
                               AND nx.prem_code = p.uabpymt_prem_code
                               AND (
                                     nx.ar_trans     = p.uabpymt_ar_trans
                                  OR nx.ar_trans     = p.uabpymt_cancel_trans
                                  OR nx.cancel_trans = p.uabpymt_ar_trans
                                   )
                           )
                    THEN 'CXL_POS_COATTL'
                  WHEN p.uabpymt_amount < 0
                       AND EXISTS (
                             SELECT 1
                             FROM UABPYMT pos, win w
                             WHERE pos.uabpymt_cust_code = p.uabpymt_cust_code
                               AND pos.uabpymt_prem_code = p.uabpymt_prem_code
                               AND pos.uabpymt_amount    > 0
                               AND pos.uabpymt_pymt_date BETWEEN w.start_dt AND w.end_dt
                               AND (
                                     pos.uabpymt_ar_trans     = p.uabpymt_ar_trans
                                  OR pos.uabpymt_ar_trans     = p.uabpymt_cancel_trans
                                  OR pos.uabpymt_cancel_trans = p.uabpymt_ar_trans
                                   )
                           )
                       AND EXISTS (
                             SELECT 1
                             FROM UABOPEN o
                             WHERE o.uabopen_cust_code          = p.uabpymt_cust_code
                               AND o.uabopen_prem_code          = p.uabpymt_prem_code
                               AND TRUNC(o.uabopen_charge_date) = TRUNC(p.uabpymt_pymt_date)
                               AND o.uabopen_srat_code          = (SELECT rev_code FROM win)
                           )
                    THEN 'NEG_REV_STRICT'
                  ELSE 'NOT_QUALIFIED'
                END AS qualify_rule
              FROM UABPYMT p
              JOIN win w
                ON p.uabpymt_cust_code = w.cust_code
               AND p.uabpymt_prem_code = w.prem_code
               AND p.uabpymt_pymt_date BETWEEN w.start_dt AND w.end_dt
            ),
            posted_qualified AS (
              SELECT *
              FROM posted
              WHERE qualify_rule IN ('REG_POS','CXL_POS_POS_UO','CXL_POS_COATTL','NEG_REV_STRICT')
            ),
            posted_lkp AS (
              SELECT
                p.cust_code,
                p.prem_code,
                p.paymentdate,
                p.paymentamount,
                p.paymentcode,
                CASE WHEN UPPER(pc.utrpycd_desc) = 'REMITTANCE' THEN 'CHECK' ELSE pc.utrpycd_desc END AS paymentdescription,
                CASE WHEN p.paymentamount < 0 THEN 'REVERSAL' ELSE 'PAYMENT' END AS paymenttype,
                'UABPYMT' AS source_table,
                (SELECT rev_code FROM win) AS rev_code,
                p.qualify_rule
              FROM posted_qualified p
              LEFT JOIN UTRPYCD pc ON pc.utrpycd_code = p.paymentcode
            ),
            gw AS (
              SELECT
                g.gzbrtpp_cust_code AS cust_code,
                g.gzbrtpp_prem_code AS prem_code,
                g.gzbrtpp_orig_date AS paymentdate,
                g.gzbrtpp_amount    AS paymentamount,
                g.gzbrtpp_pycd_code AS paymentcode
              FROM GZBRTPP g
              JOIN win w
                ON g.gzbrtpp_cust_code = w.cust_code
               AND g.gzbrtpp_prem_code = w.prem_code
               AND g.gzbrtpp_orig_date BETWEEN w.start_dt AND w.end_dt
              WHERE NVL(g.gzbrtpp_cancel_ind, 'N') = 'N'
                AND NVL(g.gzbrtpp_error_ind , 'N') = 'N'
                AND g.gzbrtpp_ar_trans     IS NULL
                AND g.gzbrtpp_cancel_trans IS NULL
            ),
            gw_lkp AS (
              SELECT
                g.cust_code,
                g.prem_code,
                TRUNC(g.paymentdate) AS paymentdate,
                g.paymentamount,
                g.paymentcode,
                CASE WHEN UPPER(pc.utrpycd_desc) = 'REMITTANCE' THEN 'CHECK' ELSE pc.utrpycd_desc END AS paymentdescription,
                CASE WHEN g.paymentamount < 0 THEN 'REVERSAL' ELSE 'PAYMENT' END AS paymenttype,
                'GZBRTPP' AS source_table,
                (SELECT rev_code FROM win) AS rev_code,
                'GATEWAY_PENDING' AS qualify_rule
              FROM gw g
              LEFT JOIN UTRPYCD pc ON pc.utrpycd_code = g.paymentcode
            ),
            acct AS (
              SELECT
                u.ucracct_cust_code  AS cust_code,
                u.ucracct_prem_code  AS prem_code,
                u.ucracct_status_ind AS accountstatus
              FROM UCRACCT u
            )
            SELECT
              h.paymentdate,
              h.paymentamount,
              h.paymentcode,
              h.paymentdescription,
              h.paymenttype,
              h.source_table,
              s.accountstatus
            FROM (
              SELECT cust_code, prem_code, paymentdate, paymentamount, paymentcode, paymentdescription, paymenttype, source_table, rev_code, qualify_rule FROM posted_lkp
              UNION ALL
              SELECT cust_code, prem_code, paymentdate, paymentamount, paymentcode, paymentdescription, paymenttype, source_table, rev_code, qualify_rule FROM gw_lkp
            ) h
            LEFT JOIN acct s
              ON s.cust_code = h.cust_code
             AND s.prem_code = h.prem_code
            ORDER BY h.paymentdate DESC
    """;

    public static final String GET_BILL_HISTORY = """
        WITH params AS (
            SELECT ADD_MONTHS(TRUNC(SYSDATE), -24) AS win_start,
                   TRUNC(SYSDATE)                 AS win_end
            FROM dual
        ),
        bhst AS (
            SELECT h.*
            FROM UBBBHST h
            CROSS JOIN params p
            WHERE h.ubbbhst_cust_code    = ?
              AND h.ubbbhst_prem_code    = ?
              AND h.ubbbhst_printed_date >= p.win_start
              AND h.ubbbhst_printed_date <  p.win_end + 1
              AND NVL(h.ubbbhst_cancel_ind, 0) = 0
              AND EXISTS (
                    SELECT /*+ NO_UNNEST */ 1
                    FROM UABOPEN o
                    WHERE o.uabopen_bhst_tran_num = h.ubbbhst_tran_num
                    AND ROWNUM = 1
              )
        ),
        bhst_cnt AS (
            SELECT COUNT(*) AS no_of_matches FROM bhst
        ),
        urr_cycle_date AS (
            SELECT h.ubbbhst_cust_code,
                   h.ubbbhst_prem_code,
                   h.ubbbhst_printed_date     AS bill_date,
                   MAX(r.urrshis_charge_date) AS cycle_charge_date
            FROM bhst h
            JOIN URRSHIS r
              ON r.urrshis_cust_code   = h.ubbbhst_cust_code
             AND r.urrshis_prem_code   = h.ubbbhst_prem_code
             AND r.urrshis_charge_date <= h.ubbbhst_printed_date
             AND NVL(r.urrshis_dos, 0) > 0
             AND r.urrshis_action_date IS NOT NULL
            GROUP BY h.ubbbhst_cust_code, h.ubbbhst_prem_code, h.ubbbhst_printed_date
        ),
        urr_rows AS (
            SELECT r.*, c.bill_date
            FROM urr_cycle_date c
            JOIN URRSHIS r
              ON r.urrshis_cust_code   = c.ubbbhst_cust_code
             AND r.urrshis_prem_code   = c.ubbbhst_prem_code
             AND r.urrshis_charge_date = c.cycle_charge_date
             AND NVL(r.urrshis_dos, 0) > 0
             AND r.urrshis_action_date IS NOT NULL
        ),
        urr_bill AS (
            SELECT r.urrshis_cust_code,
                   r.urrshis_prem_code,
                   r.bill_date,
                   TRUNC(MAX(r.urrshis_action_date)) AS bill_to_date,
                   SUM(r.urrshis_dos)                AS days_of_service
            FROM urr_rows r
            GROUP BY r.urrshis_cust_code, r.urrshis_prem_code, r.bill_date
        ),
        usage_window AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.bill_date,
                   (u.bill_to_date - u.days_of_service) AS bill_from_date,
                   u.bill_to_date,
                   u.days_of_service
            FROM urr_bill u
        ),
        ubbchst_bill AS (
            SELECT h.ubbbhst_cust_code,
                   h.ubbbhst_prem_code,
                   h.ubbbhst_printed_date                 AS bill_date,
                   SUM(NVL(c.ubbchst_billed_consump, 0)) AS total_billed_consumption
            FROM bhst h
            LEFT JOIN usage_window u
              ON u.urrshis_cust_code = h.ubbbhst_cust_code
             AND u.urrshis_prem_code = h.ubbbhst_prem_code
             AND u.bill_date         = h.ubbbhst_printed_date
            LEFT JOIN urr_cycle_date cd
              ON cd.ubbbhst_cust_code = h.ubbbhst_cust_code
             AND cd.ubbbhst_prem_code = h.ubbbhst_prem_code
             AND cd.bill_date         = h.ubbbhst_printed_date
            LEFT JOIN UBBCHST c
              ON c.ubbchst_cust_code   = h.ubbbhst_cust_code
             AND c.ubbchst_prem_code   = h.ubbbhst_prem_code
             AND c.ubbchst_charge_date IN ( h.ubbbhst_printed_date,
                                            u.bill_to_date,
                                            cd.cycle_charge_date )
            GROUP BY h.ubbbhst_cust_code, h.ubbbhst_prem_code, h.ubbbhst_printed_date
        ),
        rt_match AS (
            SELECT t.utrsrat_srat_code,
                   t.utrsrat_scat_code,
                   t.utrsrat_bill_print_desc,
                   h.ubbbhst_tran_num,
                   ROW_NUMBER() OVER (
                       PARTITION BY t.utrsrat_srat_code, t.utrsrat_scat_code, h.ubbbhst_tran_num
                       ORDER BY t.utrsrat_effect_date DESC
                   ) AS rn
            FROM UTRSRAT t
            JOIN bhst h ON t.utrsrat_effect_date <= h.ubbbhst_printed_date
        ),
        uabopen_enriched AS (
            SELECT h.ubbbhst_cust_code,
                   h.ubbbhst_prem_code,
                   h.ubbbhst_printed_date                        AS bill_date,
                   o.uabopen_item_type,
                   NVL(o.uabopen_discount,       0)              AS discount_raw,
                   NVL(o.uabopen_balance,        0)              AS balance_raw,
                   UPPER(rt.utrsrat_bill_print_desc)             AS bill_print_desc,
                   TRUNC(NVL(o.uabopen_billed_chg,      0), 2)  AS billed_chg_2dp,
                   TRUNC(NVL(o.uabopen_orig_budget_amt, 0), 2)  AS orig_budget_amt_2dp,
                   CASE
                       WHEN NVL(o.uabopen_discount, 0) <> 0
                           THEN TRUNC(-ABS(o.uabopen_discount), 2)
                       ELSE 0
                   END                                           AS promo_disc_2dp,
                   CASE
                       WHEN NVL(o.uabopen_discount, 0) <> 0 THEN 0
                       WHEN NVL(o.uabopen_balance,  0) <> 0 THEN 0
                       ELSE TRUNC(NVL(o.uabopen_billed_chg, 0), 2)
                   END                                           AS amt_for_buckets_2dp
            FROM UABOPEN o
            JOIN bhst h
              ON h.ubbbhst_tran_num = o.uabopen_bhst_tran_num
            LEFT JOIN rt_match rt
              ON rt.utrsrat_srat_code = o.uabopen_srat_code
             AND rt.utrsrat_scat_code = o.uabopen_scat_code
             AND rt.ubbbhst_tran_num  = h.ubbbhst_tran_num
             AND rt.rn = 1
        ),
        uabopen_agg AS (
            SELECT e.ubbbhst_cust_code,
                   e.ubbbhst_prem_code,
                   e.bill_date,
                   SUM(CASE WHEN e.uabopen_item_type = 'B'
                            THEN e.orig_budget_amt_2dp ELSE 0 END)  AS budget_billing_amount_raw,
                   SUM(CASE WHEN e.bill_print_desc LIKE '%TAX%'
                            THEN e.billed_chg_2dp ELSE 0 END)       AS taxes,
                   SUM(CASE WHEN e.bill_print_desc IN (
                                  'BASE CHARGE',
                                  'CUSTOMER SERVICE CHARGE',
                                  'GAS CHARGE',
                                  'INTERSTATE PIPELINE CAPACITY CHARGE',
                                  'INTERSTATE PIPELINE CAPACITY',
                                  'MCF METER CHARGE'
                              )
                            THEN e.billed_chg_2dp ELSE 0 END)       AS gas_service_charges,
                   SUM(CASE
                           WHEN e.bill_print_desc LIKE '%TAX%'      THEN 0
                           WHEN e.bill_print_desc IN (
                                  'BASE CHARGE',
                                  'CUSTOMER SERVICE CHARGE',
                                  'GAS CHARGE',
                                  'INTERSTATE PIPELINE CAPACITY CHARGE',
                                  'INTERSTATE PIPELINE CAPACITY',
                                  'MCF METER CHARGE'
                              )                                     THEN 0
                           WHEN e.bill_print_desc LIKE '%DISCOUNT%' THEN 0
                           WHEN e.uabopen_item_type = 'B'           THEN 0
                           ELSE e.amt_for_buckets_2dp
                       END)                                         AS other_charges_base,
                   SUM(e.promo_disc_2dp)
                   + SUM(CASE
                             WHEN e.bill_print_desc LIKE '%DISCOUNT%'
                              AND NVL(e.discount_raw, 0) = 0
                                 THEN e.billed_chg_2dp
                             ELSE 0
                         END)                                       AS promo_discounts
            FROM uabopen_enriched e
            GROUP BY e.ubbbhst_cust_code, e.ubbbhst_prem_code, e.bill_date
        ),
        num_seq AS (
            SELECT LEVEL AS n FROM dual CONNECT BY LEVEL <= 400
        ),
        hdd_window AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.bill_date,
                   (u.bill_to_date - u.days_of_service) AS hdd_from_date,
                   u.bill_to_date                       AS hdd_to_date
            FROM usage_window u
        ),
        bill_window_days AS (
            SELECT hw.urrshis_cust_code,
                   hw.urrshis_prem_code,
                   hw.bill_date,
                   (TRUNC(hw.hdd_from_date) + s.n - 1) AS weather_date
            FROM hdd_window hw
            JOIN num_seq s
              ON s.n <= (TRUNC(hw.hdd_to_date) - TRUNC(hw.hdd_from_date) + 1)
        ),
        sweat_day AS (
            SELECT w.ocsweat_load_zone_code,
                   TRUNC(w.ocsweat_weather_date)              AS weather_date,
                   SUM(NVL(w.ocsweat_heating_degree_days, 0)) AS hdd_day
            FROM OCSWEAT w
            GROUP BY w.ocsweat_load_zone_code, TRUNC(w.ocsweat_weather_date)
        ),
        hdd_agg AS (
            SELECT bwd.urrshis_cust_code,
                   bwd.urrshis_prem_code,
                   bwd.bill_date,
                   TRUNC(SUM(sd.hdd_day)) AS heating_degree_days
            FROM bill_window_days bwd
            LEFT JOIN UCBPREM p
              ON p.ucbprem_code = bwd.urrshis_prem_code
            LEFT JOIN sweat_day sd
              ON sd.ocsweat_load_zone_code = p.ucbprem_alternate_location
             AND sd.weather_date           = bwd.weather_date
            GROUP BY bwd.urrshis_cust_code, bwd.urrshis_prem_code, bwd.bill_date
        ),
        bill_result AS (
            SELECT TO_CHAR(h.ubbbhst_printed_date, 'YYYYMMDD')                         AS bill_date,
                   TO_CHAR(u.bill_from_date,        'YYYYMMDD')                         AS bill_from_date,
                   TO_CHAR(u.bill_to_date,           'YYYYMMDD')                        AS bill_to_date,
                   u.days_of_service                                                    AS days_of_service,
                   w.heating_degree_days                                                AS heating_degree_days,
                   c.total_billed_consumption                                           AS total_billed_consumption,
                   TRUNC((NVL(h.ubbbhst_prev_bal, 0) - NVL(h.ubbbhst_payments, 0)), 2) AS balance_brought_forward,
                   TRUNC(NVL(ua.gas_service_charges,  0), 2)                           AS gas_service_charges,
                   TRUNC(NVL(ua.other_charges_base,   0), 2)                           AS other_charges,
                   TRUNC(NVL(ua.promo_discounts,       0), 2)                          AS promotional_discounts,
                   TRUNC(NVL(ua.taxes,                 0), 2)                          AS taxes,
                   NULLIF(TRUNC(NVL(ua.budget_billing_amount_raw, 0), 2), 0)           AS budget_billing_amount,
                   TRUNC(
                       CASE
                           WHEN NVL(ua.budget_billing_amount_raw, 0) > 0
                               THEN TRUNC(NVL(ua.budget_billing_amount_raw, 0), 2)
                           ELSE
                               TRUNC(NVL(ua.gas_service_charges,  0), 2)
                             + TRUNC(NVL(ua.other_charges_base,   0), 2)
                             + TRUNC(NVL(ua.taxes,                0), 2)
                             + TRUNC(NVL(ua.promo_discounts,       0), 2)
                             + TRUNC((NVL(h.ubbbhst_prev_bal, 0) - NVL(h.ubbbhst_payments, 0)), 2)
                       END
                   , 2)                                                                 AS total_bill_amount,
                   h.ubbbhst_tran_num                                                   AS bill_history_transaction_number,
                   (SELECT no_of_matches FROM bhst_cnt)                                 AS number_of_matches
            FROM bhst h
            LEFT JOIN usage_window u
              ON u.urrshis_cust_code = h.ubbbhst_cust_code
             AND u.urrshis_prem_code = h.ubbbhst_prem_code
             AND u.bill_date         = h.ubbbhst_printed_date
            LEFT JOIN ubbchst_bill c
              ON c.ubbbhst_cust_code = h.ubbbhst_cust_code
             AND c.ubbbhst_prem_code = h.ubbbhst_prem_code
             AND c.bill_date         = h.ubbbhst_printed_date
            LEFT JOIN uabopen_agg ua
              ON ua.ubbbhst_cust_code = h.ubbbhst_cust_code
             AND ua.ubbbhst_prem_code = h.ubbbhst_prem_code
             AND ua.bill_date         = h.ubbbhst_printed_date
            LEFT JOIN hdd_agg w
              ON w.urrshis_cust_code = h.ubbbhst_cust_code
             AND w.urrshis_prem_code = h.ubbbhst_prem_code
             AND w.bill_date         = h.ubbbhst_printed_date
        )
        SELECT *
        FROM bill_result
        WHERE total_bill_amount <> 0
        ORDER BY bill_date DESC
        """;




    public static final String GET_USAGE_HISTORY2 = """
        WITH seed_hist AS (
            SELECT t.ubbbhst_cust_code,
                   t.ubbbhst_prem_code,
                   t.ubbbhst_printed_date,
                   t.ubbbhst_cancel_ind,
                   t.ubbbhst_tran_num,
                   t.ubbbhst_prev_bal,
                   t.ubbbhst_ending_bal
            FROM UBBBHST t
            WHERE t.ubbbhst_cust_code = ?
              AND t.ubbbhst_prem_code = ?
            ORDER BY t.ubbbhst_printed_date DESC
            FETCH FIRST 1 ROWS ONLY
        ),
        all_hist AS (
            SELECT h2.ubbbhst_cust_code,
                   h2.ubbbhst_prem_code,
                   h2.ubbbhst_printed_date,
                   h2.ubbbhst_cancel_ind,
                   h2.ubbbhst_tran_num,
                   h2.ubbbhst_prev_bal,
                   h2.ubbbhst_ending_bal
            FROM UBBBHST h2
            JOIN seed_hist s
              ON h2.ubbbhst_cust_code = s.ubbbhst_cust_code
             AND h2.ubbbhst_prem_code = s.ubbbhst_prem_code
            WHERE h2.ubbbhst_cancel_ind IS NULL
              AND h2.ubbbhst_printed_date >= ADD_MONTHS(TRUNC(SYSDATE), -24)
        ),
        urr AS (
            SELECT r.urrshis_cust_code,
                   r.urrshis_prem_code,
                   r.urrshis_serv_num,
                   r.urrshis_reading,
                   r.urrshis_rtyp_code,
                   r.urrshis_action_date,
                   r.urrshis_charge_date,
                   r.urrshis_dos,
                   r.urrshis_consumption,
                   LAG(r.urrshis_action_date)
                       OVER (PARTITION BY r.urrshis_cust_code,
                                          r.urrshis_prem_code,
                                          r.urrshis_serv_num
                             ORDER BY r.urrshis_action_date) AS prev_action_date
            FROM URRSHIS r
            JOIN seed_hist s
              ON r.urrshis_cust_code = s.ubbbhst_cust_code
             AND r.urrshis_prem_code = s.ubbbhst_prem_code
        ),
        urr_ranges AS (
            SELECT u.*,
                   u.urrshis_action_date AS to_dt,
                   CASE
                       WHEN u.prev_action_date IS NOT NULL THEN u.prev_action_date
                       ELSE u.urrshis_action_date - u.urrshis_dos
                   END AS from_dt
            FROM urr u
        ),
        urr_with_totals AS (
            SELECT ur.*,
                   SUM(ur.urrshis_dos) OVER (
                       PARTITION BY ur.urrshis_cust_code,
                                    ur.urrshis_prem_code
                   ) AS total_dos
            FROM urr_ranges ur
        ),
        weather_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_serv_num,
                   u.urrshis_action_date,
                   NVL(ROUND(AVG(w.ocsweat_avg_temp)), 0)            AS weather_mean_avg_temp,
                   NVL(TRUNC(SUM(w.ocsweat_heating_degree_days)), 0) AS weather_sum_hdd
            FROM urr_with_totals u
            LEFT JOIN UCBPREM p
              ON p.ucbprem_code = u.urrshis_prem_code
            LEFT JOIN OCSWEAT w
              ON w.ocsweat_load_zone_code = p.ucbprem_alternate_location
             AND w.ocsweat_weather_date   > u.from_dt
             AND w.ocsweat_weather_date   < u.to_dt
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_serv_num,
                     u.urrshis_action_date
        ),
        bill_agg AS (
            SELECT u.urrshis_cust_code,
                   u.urrshis_prem_code,
                   u.urrshis_charge_date,
                   SUM(u.urrshis_consumption) AS actual_consump_sum,
                   SUM(u.urrshis_dos)         AS days_of_service
            FROM urr_with_totals u
            GROUP BY u.urrshis_cust_code,
                     u.urrshis_prem_code,
                     u.urrshis_charge_date
        )
        SELECT u.urrshis_serv_num                                                            AS service_number,
               TO_CHAR(h.ubbbhst_printed_date, 'YYYYMMDD')                                  AS bill_date,
               TO_CHAR(u.from_dt, 'YYYYMMDD')                                               AS usage_from_date,
               TO_CHAR(u.to_dt,   'YYYYMMDD')                                               AS usage_to_date,
               TRUNC(ROUND(ba.actual_consump_sum / NULLIF(ba.days_of_service, 0), 4), 3)    AS avg_daily_actual_consumption,
               TRUNC(ROUND(c.ubbchst_billed_consump / NULLIF(ba.days_of_service, 0), 4), 3) AS avg_daily_billed_consumption,
               c.ubbchst_billed_consump                                                     AS total_billed_consumption,
               ba.days_of_service                                                           AS days_of_service,
               u.urrshis_reading                                                            AS reading,
               u.urrshis_rtyp_code                                                          AS read_type_code,
               TO_CHAR(u.urrshis_action_date, 'YYYYMMDD')                                   AS read_date,
               wa.weather_mean_avg_temp                                                     AS average_temperature,
               wa.weather_sum_hdd                                                           AS heating_degree_days,
               h.ubbbhst_tran_num                                                           AS bill_history_transaction_number,
               COUNT(*) OVER ()                                                             AS number_of_matches
        FROM all_hist h
        LEFT JOIN urr_with_totals u
          ON  u.urrshis_cust_code   = h.ubbbhst_cust_code
          AND u.urrshis_prem_code   = h.ubbbhst_prem_code
          AND u.urrshis_charge_date = h.ubbbhst_printed_date
        LEFT JOIN weather_agg wa
          ON  wa.urrshis_cust_code   = u.urrshis_cust_code
          AND wa.urrshis_prem_code   = u.urrshis_prem_code
          AND wa.urrshis_serv_num    = u.urrshis_serv_num
          AND wa.urrshis_action_date = u.urrshis_action_date
        LEFT JOIN UBBCHST c
          ON  c.ubbchst_cust_code   = h.ubbbhst_cust_code
          AND c.ubbchst_prem_code   = h.ubbbhst_prem_code
          AND c.ubbchst_charge_date = h.ubbbhst_printed_date
        LEFT JOIN bill_agg ba
          ON  ba.urrshis_cust_code   = h.ubbbhst_cust_code
          AND ba.urrshis_prem_code   = h.ubbbhst_prem_code
          AND ba.urrshis_charge_date = h.ubbbhst_printed_date
        ORDER BY h.ubbbhst_printed_date DESC,
                 u.urrshis_serv_num,
                 u.urrshis_action_date
        """;

    public static final String GET_PHONE_NUMBERS= """
            SELECT  t.ucrtele_phone_area || t.ucrtele_phone_number AS phone_number,
                 t.ucrtele_tele_code, t.ucrtele_primary_ind,
                --t.*,
                a.ucracct_prem_code
            FROM
                ucrtele t
            JOIN
                ucracct a
                ON t.ucrtele_cust_code = a.ucracct_cust_code
            WHERE a.ucracct_prem_code = ?
            AND t.ucrtele_primary_ind = 'Y'
            """;

    public static final String GET_USER_ACCOUNT_INFO = """
            SELECT * FROM UCRACCT
            WHERE UCRACCT_CUST_CODE= ?
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_USER_ACCOUNT_INFO_NEW = """
            SELECT u.user_name, ra.account_number
            FROM users u
            JOIN custadv_registered_accounts ra
                ON u.user_id = ra.user_id
            WHERE ra.account_number LIKE CONCAT('%', ?, '%')
            AND u.domain_id = 2
            AND u.user_name REGEXP '^[A-Za-z0-9]+$'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_USER_ACCOUNT_INFO_NEW2 = """
            SELECT u.user_name, ra.account_number
            FROM users u
            JOIN custadv_registered_accounts ra
                ON u.user_id = ra.user_id
            WHERE ra.account_number LIKE CONCAT('%', ?, '%', ? , '%')
            AND u.domain_id = 2
            AND u.user_name REGEXP '^[A-Za-z0-9]+$'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_USER_ACCOUNT_INFO_NEW_INACTIVE = """
            SELECT
                u.user_name,
                ra.account_number
            FROM
                users u
            JOIN
                custadv_registered_accounts ra
                    ON u.user_id = ra.user_id
            WHERE
                ra.account_number LIKE CONCAT('%', ?, '%')
                AND u.domain_id = 2
                AND u.active= 0
                AND u.user_name REGEXP '^[A-Za-z0-9]+$'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ACCOUNT_ROUTING_NO = """
            SELECT *
            FROM UTRBANK
            WHERE LPAD(utrbank_transit_1, 4, '0')
               || LPAD(utrbank_transit_2, 4, '0')
               || utrbank_transit_3 = ?
            """;

    public static final String GET_PRE_DIRECTION = """
            SELECT UTVPDIR_CODE FROM UTVPDIR
            """;

    public static final String GET_UNIT_TYPE = """
            SELECT UTVUTYP_CODE FROM UTVUTYP
            """;

    public static final String SELECT_ACCOUNT_WITH_ACTIVE_PAYMENT_ARRANGEMENT = """
            SELECT
                a.ucracct_cust_code AS customer_code,
                a.ucracct_prem_code AS premises_code,
                a.ucracct_draft_acct_status AS bankDraftStatus,
                LPAD(b.utrbank_transit_1, 4, '0') ||
                LPAD(b.utrbank_transit_2, 4, '0') ||
                b.utrbank_transit_3 AS bankDraftRoutingNumber,
                a.ucracct_check_saving_ind AS bankDraftAccountType,
                NVL(c.ucbcust_last_name, '') AS bankName
            FROM UCRACCT a
            JOIN UTRBANK b
                ON a.ucracct_bank_code = b.utrbank_code
            JOIN UCBCUST c
                ON b.utrbank_cust_code_bank = c.ucbcust_cust_code
            WHERE a.ucracct_draft_acct_status = 'A'
              AND b.utrbank_status = 'A'
              AND a.ucracct_bank_acct IS NOT NULL
              AND a.ucracct_pmnt_arr = 'Y'
              AND EXISTS (
                    SELECT 1
                    FROM UCRSERV s
                    WHERE s.ucrserv_cust_code = a.ucracct_cust_code
                      AND s.ucrserv_prem_code = a.ucracct_prem_code
                )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_INACTIVE_PAYMENT_ARRANGEMENT= """
    SELECT
                    h.UABPYAR_CUST_CODE                              AS custCode,
                    h.UABPYAR_PREM_CODE                              AS premCode,
                    h.UABPYAR_ARRNG_NUM                              AS paNumber,
                    h.UABPYAR_PYAR_CODE                              AS paTypeCode,
                    h.UABPYAR_TOTAL_AMT                              AS paTotalAmount,
                    TO_CHAR(h.UABPYAR_DATE_CREATED, 'YYYYMMDD')      AS paDateCreated,
                    COUNT(*) OVER (
                        PARTITION BY h.UABPYAR_CUST_CODE,
                                     h.UABPYAR_PREM_CODE,
                                     h.UABPYAR_ARRNG_NUM
                    )                                                AS numberOfInstallments,
                    h.UABPYAR_STATUS                                 AS status,
                    'INACTIVE ARRANGEMENT'                           AS arrangementStatus,
                    d.UARPYAR_AMT_DUE                                AS amountDue,
                    d.UARPYAR_BALANCE                                AS balance,
                    TO_CHAR(d.UARPYAR_DATE_DUE, 'YYYYMMDD')          AS dateDue,
                    TO_CHAR(d.UARPYAR_DATE_PAID_IN_FULL, 'YYYYMMDD') AS datePaid
                FROM UABPYAR h
                JOIN UARPYAR d
                    ON  d.UARPYAR_CUST_CODE = h.UABPYAR_CUST_CODE
                    AND d.UARPYAR_PREM_CODE = h.UABPYAR_PREM_CODE
                    AND d.UARPYAR_ARRNG_NUM = h.UABPYAR_ARRNG_NUM
                WHERE h.UABPYAR_STATUS     <> 'A'
                  AND h.UABPYAR_DATE_CREATED >= ADD_MONTHS(TRUNC(SYSDATE), -12)
                  AND EXISTS (
                        SELECT 1
                        FROM UCRACCT u
                        WHERE u.UCRACCT_CUST_CODE  = h.UABPYAR_CUST_CODE
                          AND u.UCRACCT_PREM_CODE  = h.UABPYAR_PREM_CODE
                          AND u.UCRACCT_STATUS_IND = 'A'
                  )
                  AND NOT EXISTS (
                        SELECT 1
                        FROM UABPYAR active
                        WHERE active.UABPYAR_CUST_CODE = h.UABPYAR_CUST_CODE
                          AND active.UABPYAR_PREM_CODE = h.UABPYAR_PREM_CODE
                          AND active.UABPYAR_STATUS    = 'A'
                  )
                ORDER BY
                    h.UABPYAR_CUST_CODE,
                    h.UABPYAR_PREM_CODE,
                    h.UABPYAR_ARRNG_NUM,
                    d.UARPYAR_DATE_DUE NULLS LAST
                FETCH FIRST 1 ROWS ONLY
""";


    public static final String SELECT_ACCOUNT_ACTIVE_PAYMENT_ARRANGEMENT1= """
    SELECT
                    h.UABPYAR_CUST_CODE                              AS custCode,
                    h.UABPYAR_PREM_CODE                              AS premCode,
                    h.UABPYAR_ARRNG_NUM                              AS paNumber,
                    h.UABPYAR_PYAR_CODE                              AS paTypeCode,
                    h.UABPYAR_TOTAL_AMT                              AS paTotalAmount,
                    TO_CHAR(h.UABPYAR_DATE_CREATED, 'YYYYMMDD')      AS paDateCreated,
                    COUNT(*) OVER (
                        PARTITION BY h.UABPYAR_CUST_CODE,
                                     h.UABPYAR_PREM_CODE,
                                     h.UABPYAR_ARRNG_NUM
                    )                                                AS numberOfInstallments,
                    h.UABPYAR_STATUS                                 AS status,
                    'ACTIVE ARRANGEMENT'                             AS arrangementStatus,
                    d.UARPYAR_AMT_DUE                                AS amountDue,
                    d.UARPYAR_BALANCE                                AS balance,
                    TO_CHAR(d.UARPYAR_DATE_DUE, 'YYYYMMDD')          AS dateDue,
                    TO_CHAR(d.UARPYAR_DATE_PAID_IN_FULL, 'YYYYMMDD') AS datePaid
                FROM UABPYAR h
                JOIN UARPYAR d
                    ON  d.UARPYAR_CUST_CODE = h.UABPYAR_CUST_CODE
                    AND d.UARPYAR_PREM_CODE = h.UABPYAR_PREM_CODE
                    AND d.UARPYAR_ARRNG_NUM = h.UABPYAR_ARRNG_NUM
                WHERE h.UABPYAR_STATUS      = 'A'
                  AND h.UABPYAR_DATE_CREATED >= ADD_MONTHS(TRUNC(SYSDATE), -12)
                  AND EXISTS (
                        SELECT 1
                        FROM UCRACCT u
                        WHERE u.UCRACCT_CUST_CODE  = h.UABPYAR_CUST_CODE
                          AND u.UCRACCT_PREM_CODE  = h.UABPYAR_PREM_CODE
                          AND u.UCRACCT_STATUS_IND = 'A'
                  )
                  AND (
                        SELECT COUNT(*)
                        FROM UARPYAR d2
                        WHERE d2.UARPYAR_CUST_CODE = h.UABPYAR_CUST_CODE
                          AND d2.UARPYAR_PREM_CODE = h.UABPYAR_PREM_CODE
                          AND d2.UARPYAR_ARRNG_NUM = h.UABPYAR_ARRNG_NUM
                  ) = 1
                ORDER BY
                    h.UABPYAR_CUST_CODE,
                    h.UABPYAR_PREM_CODE,
                    h.UABPYAR_ARRNG_NUM,
                    d.UARPYAR_DATE_DUE NULLS LAST
                FETCH FIRST 1 ROWS ONLY
""";

    public static final String SELECT_ACCOUNT_ACTIVE_PAYMENT_ARRANGEMENT_MORE= """
    SELECT
                    h.UABPYAR_CUST_CODE                              AS custCode,
                    h.UABPYAR_PREM_CODE                              AS premCode,
                    h.UABPYAR_ARRNG_NUM                              AS paNumber,
                    h.UABPYAR_PYAR_CODE                              AS paTypeCode,
                    h.UABPYAR_TOTAL_AMT                              AS paTotalAmount,
                    TO_CHAR(h.UABPYAR_DATE_CREATED, 'YYYYMMDD')      AS paDateCreated,
                    COUNT(*) OVER (
                        PARTITION BY h.UABPYAR_CUST_CODE,
                                     h.UABPYAR_PREM_CODE,
                                     h.UABPYAR_ARRNG_NUM
                    )                                                AS numberOfInstallments,
                    h.UABPYAR_STATUS                                 AS status,
                    'ACTIVE ARRANGEMENT'                             AS arrangementStatus,
                    d.UARPYAR_AMT_DUE                                AS amountDue,
                    d.UARPYAR_BALANCE                                AS balance,
                    TO_CHAR(d.UARPYAR_DATE_DUE, 'YYYYMMDD')          AS dateDue,
                    TO_CHAR(d.UARPYAR_DATE_PAID_IN_FULL, 'YYYYMMDD') AS datePaid
                FROM UABPYAR h
                JOIN UARPYAR d
                    ON  d.UARPYAR_CUST_CODE = h.UABPYAR_CUST_CODE
                    AND d.UARPYAR_PREM_CODE = h.UABPYAR_PREM_CODE
                    AND d.UARPYAR_ARRNG_NUM = h.UABPYAR_ARRNG_NUM
                WHERE h.UABPYAR_STATUS      = 'A'
                  AND h.UABPYAR_DATE_CREATED >= ADD_MONTHS(TRUNC(SYSDATE), -12)
                  AND EXISTS (
                        SELECT 1
                        FROM UCRACCT u
                        WHERE u.UCRACCT_CUST_CODE  = h.UABPYAR_CUST_CODE
                          AND u.UCRACCT_PREM_CODE  = h.UABPYAR_PREM_CODE
                          AND u.UCRACCT_STATUS_IND = 'A'
                  )
                  AND (
                        SELECT COUNT(*)
                        FROM UARPYAR d2
                        WHERE d2.UARPYAR_CUST_CODE = h.UABPYAR_CUST_CODE
                          AND d2.UARPYAR_PREM_CODE = h.UABPYAR_PREM_CODE
                          AND d2.UARPYAR_ARRNG_NUM = h.UABPYAR_ARRNG_NUM
                  ) > 1
                ORDER BY
                    h.UABPYAR_CUST_CODE,
                    h.UABPYAR_PREM_CODE,
                    h.UABPYAR_ARRNG_NUM,
                    d.UARPYAR_DATE_DUE NULLS LAST
                FETCH FIRST 1 ROWS ONLY
""";

    public static final String SELECT_ACCOUNT_ACTIVE_PAYMENT_ARRANGEMENT_MORE2= """
    SELECT
                    h.UABPYAR_CUST_CODE                              AS custCode,
                    h.UABPYAR_PREM_CODE                              AS premCode,
                    h.UABPYAR_ARRNG_NUM                              AS paNumber,
                    h.UABPYAR_PYAR_CODE                              AS paTypeCode,
                    h.UABPYAR_TOTAL_AMT                              AS paTotalAmount,
                    TO_CHAR(h.UABPYAR_DATE_CREATED, 'YYYYMMDD')      AS paDateCreated,
                    COUNT(*) OVER (
                        PARTITION BY h.UABPYAR_CUST_CODE,
                                     h.UABPYAR_PREM_CODE,
                                     h.UABPYAR_ARRNG_NUM
                    )                                                AS numberOfInstallments,
                    h.UABPYAR_STATUS                                 AS status,
                    'ACTIVE ARRANGEMENT'                             AS arrangementStatus,
                    d.UARPYAR_AMT_DUE                                AS amountDue,
                    d.UARPYAR_BALANCE                                AS balance,
                    TO_CHAR(d.UARPYAR_DATE_DUE, 'YYYYMMDD')          AS dateDue,
                    TO_CHAR(d.UARPYAR_DATE_PAID_IN_FULL, 'YYYYMMDD') AS datePaid
                FROM UABPYAR h
                JOIN UARPYAR d
                    ON  d.UARPYAR_CUST_CODE = h.UABPYAR_CUST_CODE
                    AND d.UARPYAR_PREM_CODE = h.UABPYAR_PREM_CODE
                    AND d.UARPYAR_ARRNG_NUM = h.UABPYAR_ARRNG_NUM
                WHERE h.UABPYAR_STATUS      = 'A'
                  AND h.UABPYAR_DATE_CREATED >= ADD_MONTHS(TRUNC(SYSDATE), -12)
                  AND d.UARPYAR_DATE_PAID_IN_FULL IS NOT NULL
                  AND EXISTS (
                        SELECT 1
                        FROM UCRACCT u
                        WHERE u.UCRACCT_CUST_CODE  = h.UABPYAR_CUST_CODE
                          AND u.UCRACCT_PREM_CODE  = h.UABPYAR_PREM_CODE
                          AND u.UCRACCT_STATUS_IND = 'A'
                  )
                  AND (
                        SELECT COUNT(*)
                        FROM UARPYAR d2
                        WHERE d2.UARPYAR_CUST_CODE = h.UABPYAR_CUST_CODE
                          AND d2.UARPYAR_PREM_CODE = h.UABPYAR_PREM_CODE
                          AND d2.UARPYAR_ARRNG_NUM = h.UABPYAR_ARRNG_NUM
                  ) > 1
                ORDER BY DBMS_RANDOM.VALUE
                FETCH FIRST 1 ROWS ONLY
""";

    public static final String SELECT_ACCOUNT_NO_PAYMENT_ARRANGEMENT= """
            SELECT
                u.UCRACCT_CUST_CODE             AS customer_code,
                u.UCRACCT_PREM_CODE             AS premises_code,
                u.UCRACCT_STATUS_IND            AS account_status,
                'NO ARRANGEMENT'                AS arrangement_status  -- ← confirms no active arrangement
            FROM UCRACCT u
            WHERE u.UCRACCT_STATUS_IND          IN ('A', 'N', 'F', 'I')
              AND NOT EXISTS (
                    -- no active payment arrangement in UABPYAR
                    SELECT 1
                    FROM UABPYAR ar
                    WHERE ar.UABPYAR_CUST_CODE  = u.UCRACCT_CUST_CODE
                      AND ar.UABPYAR_PREM_CODE  = u.UCRACCT_PREM_CODE
                      AND ar.UABPYAR_STATUS     = 'A'             -- no active arrangement
              )
              AND NOT EXISTS (
                    -- no arrangement detail rows in UARPYAR either
                    SELECT 1
                    FROM UARPYAR d
                    WHERE d.UARPYAR_CUST_CODE   = u.UCRACCT_CUST_CODE
                      AND d.UARPYAR_PREM_CODE   = u.UCRACCT_PREM_CODE
              )
              AND EXISTS (
                    -- account must have at least one posted payment to be meaningful
                    SELECT 1
                    FROM UABPYMT p
                    WHERE p.UABPYMT_CUST_CODE   = u.UCRACCT_CUST_CODE
                      AND p.UABPYMT_PREM_CODE   = u.UCRACCT_PREM_CODE
                      AND p.UABPYMT_PYMT_DATE   >= ADD_MONTHS(TRUNC(SYSDATE), -24)
              )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_NO_PAYMENT_ARRANGEMENT = """
            SELECT
                a.ucracct_cust_code AS customer_code,
                a.ucracct_prem_code AS premises_code,
                a.ucracct_draft_acct_status AS bankDraftStatus,
                LPAD(b.utrbank_transit_1, 4, '0') ||
                LPAD(b.utrbank_transit_2, 4, '0') ||
                b.utrbank_transit_3 AS bankDraftRoutingNumber,
                a.ucracct_check_saving_ind AS bankDraftAccountType,
                NVL(c.ucbcust_last_name, '') AS bankName
            FROM UCRACCT a
            JOIN UTRBANK b
                ON a.ucracct_bank_code = b.utrbank_code
            JOIN UCBCUST c
                ON b.utrbank_cust_code_bank = c.ucbcust_cust_code
            WHERE a.ucracct_draft_acct_status = 'A'
              AND b.utrbank_status = 'A'
              AND a.ucracct_bank_acct IS NOT NULL
              AND EXISTS (
                    SELECT 1
                    FROM UCRSERV s
                    WHERE s.ucrserv_cust_code = a.ucracct_cust_code
                      AND s.ucrserv_prem_code = a.ucracct_prem_code
                )
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_FUTURE_PAYMENT_ARRANGEMENT = """
            SELECT
                d.UARDRFT_CUST_CODE,
                d.UARDRFT_PREM_CODE
            FROM UARDRFT d
            WHERE d.UARDRFT_HOLD_UNTIL_DATE > TRUNC(SYSDATE)
              AND d.UARDRFT_AMOUNT          <> 0
              AND d.UARDRFT_FILE_DATE        = DATE '2099-12-31'
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_WITH_NO_UPDATE = """
            SELECT
                a.ucracct_cust_code AS customer_code,
                a.ucracct_prem_code AS premises_code,
                a.ucracct_draft_acct_status AS bankDraftStatus,
                CASE
                    WHEN a.ucracct_draft_acct_status IS NOT NULL
                         AND b.utrbank_status = 'A'
                    THEN
                        LPAD(b.utrbank_transit_1, 4, '0') ||
                        LPAD(b.utrbank_transit_2, 4, '0') ||
                        b.utrbank_transit_3
                    ELSE ''
                END AS bankDraftRoutingNumber,
                a.ucracct_check_saving_ind AS bankDraftAccountType,
                SPK_SED.D_STRING(a.ucracct_bank_acct, 'UCRACCT_BANK_ACCT') AS bankDraftAccountNumber,
                NVL(c.ucbcust_last_name, '') AS bankName
            FROM UCRACCT a
            JOIN UTRBANK b
                ON a.ucracct_bank_code = b.utrbank_code
            JOIN UCBCUST c
                ON b.utrbank_cust_code_bank = c.ucbcust_cust_code
            WHERE a.ucracct_draft_acct_status IS NOT NULL
              AND b.utrbank_status = 'A'
              AND a.ucracct_bank_acct IS NOT NULL
              ORDER BY a.ucracct_cust_code DESC
            FETCH FIRST 1 ROWS ONLY
            """;



    public static final String GET_ZIP = """
            SELECT * FROM GTVZIPC
            WHERE GTVZIPC_CODE= ?
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_ZIP_AND_CITY = """
            SELECT * FROM GTVZIPC
            WHERE GTVZIPC_CODE= ?
            AND GTVZIPC_CITY= ?
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String GET_STREET_SUFFIX = """
            SELECT UTVSSFX_CODE FROM UTVSSFX
            """;

    public static final String SELECT_ACTIVE_USER_NAME_2= """
            SELECT user_name
            FROM users
            WHERE active = 1
            AND domain_id = 2
            AND LENGTH(user_name) > 5
            AND user_name REGEXP '^[a-zA-Z0-9]+$'
            AND user_name <> '0000000000'
            ORDER BY user_name ASC
            FETCH FIRST 1 ROWS ONLY
            """;

    public static final String SELECT_ACCOUNT_INFORMATION= """
            Select * from ucbcust
            where ucbcust_cust_code='3935333'
            """;

    public static final String SELECT_ACCOUNT_INFORMATION2= """
            Select * from ucbcust
            where ucbcust_cust_code='1314486'
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
