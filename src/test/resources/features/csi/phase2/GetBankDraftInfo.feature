Feature: Verify GetBankDraftInfo Api

  @GetBankDraftInfo @NegativeFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetBankDraftInfo Api for "<testCondition>"
    Then verify response code of "GetBankDraftInfo" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                   | errorCode | errorMessage                |
      | TC_40__Negative__Missing_Request_ID              | 10001     | Missing Request ID          |
      | TC_41__Negative__Invalid_Request_ID_Length       | 10002     | Invalid Request ID          |
      | TC_42__Negative__Duplicate_Request_ID            | 10003     | Duplicate Request ID        |
      | TC_43__Negative__Missing_CustomerCode            | 10011     | Missing Customer Code       |
      | TC_44__Negative__Invalid_CustomerCode_Length     | 10015     | Invalid Customer Code Format|
      | TC_45__Negative__Invalid_CustomerCode_Format     | 10015     | Invalid Customer Code Format|
      | TC_46__Negative__Missing_PremisesCode            | 10013     | Missing Premises Code       |
      | TC_47__Negative__Invalid_PremisesCode_Length     | 10005     | Invalid Premises Code Format|
      | TC_48__Negative__Invalid_PremisesCode_Format     | 10005     | Invalid Premises Code Format|
      | TC_49__Negative__Invalid_Account_Number         | 40015     | Invalid Account Number      |


  @GetBankDraftInfoPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetBankDraftInfo Api for "<testCondition>"
    Then verify response code of "GetBankDraftInfo" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And the response should have data as "<data>"
    And perform the validation for "<testCondition>"


    Examples:
      | testCondition                                           | errorCode | errorMessage | data    | bankDraftStatus   |
      | TC_50__Positive__No_BankDraft_Info                      | 0         |              | notNull | null              |

  @GetBankDraftInfoPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetBankDraftInfo Api for "<testCondition>"
    Then verify response code of "GetBankDraftInfo" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And the response should have data as "<data>"
    And the response should have bankDraftStatus as "<bankDraftStatus>"

    Examples:
      | testCondition                                           | errorCode | errorMessage | data    | bankDraftStatus   |
      | TC_51__Positive__BankDraftStatus_Active                 | 0         |              | notNull | ACTIVE            |
      | TC_52__Positive__BankDraftStatus_PreNotification        | 0         |              | notNull | PRENOTIFICATION   |
      | TC_53__Positive__BankDraftStatus_Canceled               | 0         |              | notNull | CANCELED          |
      | TC_54__Positive__BankDraftStatus_Inactive               | 0         |              | notNull | INACTIVE          |


  @GetBankDraftInfoPositive @HappyFlow @CSI
  Scenario Outline: "<testCondition>"
    When a request is made to GetBankDraftInfo Api for "<testCondition>"
    Then verify response code of "GetBankDraftInfo" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    And the response should have data as "<data>"
    And perform the validation for "<testCondition>"

    Examples:
      | testCondition                                           | errorCode | errorMessage | data    |
      | TC_55__Positive__BankRoutingNumber                      | 0         |              | notNull |
      | TC_56__Positive__BankAccountNumber                      | 0         |              | notNull |
      | TC_57__Positive__BankAccountType_Checking               | 0         |              | notNull |
      | TC_58__Positive__BankAccountType_Savings                | 0         |              | notNull |
      | TC_59__Positive__BankName                               | 0         |              | notNull |
