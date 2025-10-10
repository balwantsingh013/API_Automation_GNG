Feature: Verify CreateAccountNote Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @CreateBannerNotesWithInvalidParameters @NegativeFlow @Phase1
  Scenario Outline: Verify CreateBannerNotes with invalid parameters "<testCondition>"
    When a request is made to the CreateBannerNotes Api with invalid parameters for "<testCondition>" condition
    Then verify response code of "CreateBannerNotes" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | testCondition                                            | errorCode | errorMessage                          |
      # Request ID
      | REQUEST_ID_MISSING_NEGATIVE_TC65                         | 10001     | Missing Request ID                     |
      | REQUEST_ID_DUPLICATE_NEGATIVE_TC66                       | 10003     | Duplicate Request ID                   |
     # # Customer Code
      | CUSTOMER_CODE_NULL_NEGATIVE_TC67                         | 10011     | Missing Customer Code                  |
      | CUSTOMER_CODE_LENGTH_GT9_NEGATIVE_TC69                   | 10015     | Invalid Customer Code Format           |
     # # Premises Code
      | PREMISES_CODE_NULL_NEGATIVE_TC68                         | 10013     | Missing Premises Code                  |
      | PREMISES_CODE_LENGTH_GT7_NEGATIVE_TC70                   | 10005     | Invalid Premises Code Format           |
      # Account Combination
      | ACCOUNT_COMBINATION_INVALID_NEGATIVE_TC71                | 40015     | Invalid Account Number                 |
      # Note Type / Text / Origin
      | NOTE_TYPE_CODE_NULL_NEGATIVE_TC72                        | 10027     | Missing Note Type Code                 |
      | NOTE_TEXT_NULL_NEGATIVE_TC73                             | 10025     | Missing Note Text                      |
      | ORIGIN_NULL_NEGATIVE_TC74                                | 10029     | Missing Origin                         |
      # Dates
      | EXPIRATION_DATE_INVALID_FORMAT_NEGATIVE_TC75             | 10031     |  Invalid Suspense Date Format      |
      #TC75 should be-> 10031 'Invalid Expiration Date Format'
      | SUSPENSE_DATE_INVALID_FORMAT_NEGATIVE_TC76               | 10033     | Invalid Suspense Date Format           |
      # Service Number
      | SERVICE_NUMBER_NOT_FOUND_FOR_PREMISES_NEGATIVE_TC77      | 40043     | Invalid Service Number                 |
      | SERVICE_NUMBER_INVALID_FORMAT_NEGATIVE_TC78              | 10045     | Invalid Service Number Format          |
      # Note Type validation
      | NOTE_TYPE_INVALID_NEGATIVE_TC79                          | 40045     | Invalid Note Type                      |

  @CreateBannerNotesPositiveFlows @HappyFlow @Phase1
  Scenario Outline: Verify CreateBannerNotes positive flows for "<testCondition>"
    When a request is made to the CreateBannerNotes Api with valid parameters "<noteText>" noteText for "<testCondition>" condition
    Then verify response code of "CreateBannerNotes" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
   # And verify response contains a valid NoteSequenceNumber
    And verify NoteSequenceNumber match the value in the database
    And verify banner note is created successfully with "<noteText>" noteText for "<testCondition>" condition
    #And verify note formatting and content rules for "<testCondition>"

    Examples:
      | testCondition                         | noteText                                        |
      | CREATE_NOTE_ACCT_POSITIVE_TC80        | Phone number 1234567890 listed\|~ as Caller ID. |
      | CREATE_NOTE_PMT_RPT_POSITIVE_TC81     |                                |
      | CREATE_NOTE_IVR_NPA_POSITIVE_TC82     |                                |
      | CREATE_NOTE_IVR_NPA_ALT_POSITIVE_TC83 |                                |


  @CreateAccountNoteApiWithValidData @Phase1 @HappyFlow
  Scenario: Verify CreateAccountNote Api request with valid data
    When a request is made to the CreateAccountNote Api with valid data
      | noteTypeCode | ACCT            |
      | noteText     | This is a test2 |
      | origin       | IVR             |
    Then verify response code of "CreateAccountNote" Api is 200
    And verify response contains a valid NoteSequenceNumber
    And verify NoteSequenceNumber match the value in the database

  @CreateAccountNoteApiMissingRequestID @Phase1
  Scenario: Verify CreateAccountNote Api request with missing RequestID
    When a request is made to the CreateAccountNote Api with missing RequestID
      | serviceNumber | 1    |
      | noteTypeCode  | ACCT |
      | noteText      | Test |
      | origin        | IVR  |
    Then verify response code of "CreateAccountNote" Api is 200
    And response should have ErrorCode 10001 and ErrorMessage 'Missing Request ID'

  @CreateAccountNoteApiDuplicateRequestID @Phase1
  Scenario: Verify CreateAccountNote Api request with Duplicate requestID
    When a request is made to the CreateAccountNote Api with Duplicate requestID
      | serviceNumber | 1    |
      | noteTypeCode  | ACCT |
      | noteText      | Test |
      | origin        | IVR  |
    Then verify response code of "CreateAccountNote" Api is 200
    And response should have ErrorCode 10003 and ErrorMessage 'Duplicate Request ID'

  @CreateAccountNoteApiNullCustomerCode @Phase1
  Scenario: Verify CreateAccountNote Api request with Null CustomerCode
    When a request is made to the CreateAccountNote Api with Null CustomerCode
      | serviceNumber | 1    |
      | noteTypeCode  | ACCT |
      | noteText      | Test |
      | origin        | IVR  |
    Then verify response code of "CreateAccountNote" Api is 200
    And response should have ErrorCode 10011 and ErrorMessage 'Missing Customer Code'

  @CreateAccountNoteApiInvalidParamFormat @Phase1
  Scenario Outline: Verify CreateAccountNote Api request with "<param>" value is "<paramValue>"
    When a request is made to the CreateAccountNote Api with "<param>" value is "<paramValue>"
      | serviceNumber | 1    |
      | noteTypeCode  | ACCT |
      | noteText      | Test |
      | origin        | IVR  |
    Then verify response code of "CreateAccountNote" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"
    Examples:
      | param        | paramValue               | errorCode | errorMessage                 |
      | CustomerCode | longer than 9 characters | 10015     | Invalid Customer Code Format |
      | PremisesCode | longer than 7 characters | 10005     | Invalid Premises Code Format |

  @CreateAccountNoteNonExistentCustPremCode @Phase1
  Scenario: Verify CreateAccountNote Api request with non-existent combination of CustomerCode and PremisesCode
    When a request is made to the CreateAccountNote Api with non-existent combination of CustomerCode and PremisesCode
      | serviceNumber | 1    |
      | noteTypeCode  | ACCT |
      | noteText      | Test |
      | origin        | IVR  |
    Then verify response code of "CreateAccountNote" Api is 200
    And response should have ErrorCode 40015 and ErrorMessage 'Invalid Account Number'

  @CreateAccountNoteApiNullNoteTypeCode @Phase1
  Scenario: Verify CreateAccountNote Api request with Null NoteTypeCode
    When a request is made to the CreateAccountNote Api with Null NoteTypeCode
      | serviceNumber | 1    |
      | noteText      | Test |
      | origin        | IVR  |
    Then verify response code of "CreateAccountNote" Api is 200
    And response should have ErrorCode 10027 and ErrorMessage 'Missing Note Type Code'

  @CreateAccountNoteApiNullNoteText @Phase1
  Scenario: Verify CreateAccountNote Api request with Null NoteText
    When a request is made to the CreateAccountNote Api with Null NoteText
      | serviceNumber | 1    |
      | noteTypeCode  | ACCT |
      | origin        | IVR  |
    Then verify response code of "CreateAccountNote" Api is 200
    And response should have ErrorCode 10025 and ErrorMessage 'Missing Note Text'

  @CreateAccountNoteApiNullOrigin @Phase1
  Scenario: Verify CreateAccountNote Api request with Null Origin
    When a request is made to the CreateAccountNote Api with Null Origin
      | serviceNumber | 1    |
      | noteTypeCode  | ACCT |
      | noteText      | Test |
    Then verify response code of "CreateAccountNote" Api is 200
    And response should have ErrorCode 10029 and ErrorMessage 'Missing Origin'

  @CreateAccountNoteApiInvalidExpirationDateFormat @Phase1
  Scenario: Verify CreateAccountNote Api request with Invalid Expiration Date Format
    When a request is made to the CreateAccountNote Api with Invalid Expiration Date Format
      | serviceNumber  | 1            |
      | noteTypeCode   | ACCT         |
      | noteText       | Test         |
      | origin         | IVR          |
      | expirationDate | invalid-date |
    Then verify response code of "CreateAccountNote" Api is 200
    And response should have ErrorCode 10031 and ErrorMessage 'Invalid Expiration Date Format'

  @CreateAccountNoteApiNonExistentServiceNoPremCode @Phase1
  Scenario: Verify CreateAccountNote Api request with non-existent combination of ServiceNumber and PremisesCode
    When a request is made to the CreateAccountNote Api with non-existent combination of ServiceNumber and PremisesCode
      | noteTypeCode | ACCT |
      | noteText     | Test |
      | origin       | IVR  |
    Then verify response code of "CreateAccountNote" Api is 200
    And response should have ErrorCode 40043 and ErrorMessage 'Invalid Service Number'

  @CreateAccountNoteApiInvalidServiceNumberFormat @Phase1
  Scenario: Verify CreateAccountNote Api request with Invalid ServiceNumber Format that is more than 4-digit number
    When a request is made to the CreateAccountNote Api with Invalid ServiceNumber Format
      | noteTypeCode | ACCT |
      | noteText     | Test |
      | origin       | IVR  |
    Then verify response code of "CreateAccountNote" Api is 200
    And response should have ErrorCode 10045 and ErrorMessage 'Invalid Service Number Format'

  @CreateAccountNoteApiNonExistentNoteType @Phase1
  Scenario: Verify CreateAccountNote Api request with non-existent NoteType
    When a request is made to the CreateAccountNote Api with non-existent NoteType
      | noteText | Test |
      | origin   | IVR  |
    Then verify response code of "CreateAccountNote" Api is 200
    And response should have ErrorCode 40045 and ErrorMessage 'Invalid Note Type'