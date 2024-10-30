Feature: Verify CreateAccountNote Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @CreateAccountNoteApiWithValidData @Phase1 @HappyFlow
  Scenario: Verify CreateAccountNote Api request with valid data
    When a request is made to the CreateAccountNote Api with valid data
      | noteTypeCode | ACCT            |
      | noteText     | This is a test2 |
      | origin       | IVR             |
    Then verify response code of "CreateAccountNote" Api is <200>
    And verify response contains a valid NoteSequenceNumber
    And verify NoteSequenceNumber match the value in the database

  @CreateAccountNoteApiMissingRequestID @Phase1
  Scenario: Verify CreateAccountNote Api request with missing RequestID
    When a request is made to the CreateAccountNote Api with missing RequestID
      | serviceNumber | 1    |
      | noteTypeCode  | ACCT |
      | noteText      | Test |
      | origin        | IVR  |
    Then verify response code of "CreateAccountNote" Api is <200>
    And response should have ErrorCode 10001 and ErrorMessage 'Missing Request ID'

  @CreateAccountNoteApiDuplicateRequestID @Phase1
  Scenario: Verify CreateAccountNote Api request with Duplicate requestID
    When a request is made to the CreateAccountNote Api with Duplicate requestID
      | serviceNumber | 1    |
      | noteTypeCode  | ACCT |
      | noteText      | Test |
      | origin        | IVR  |
    Then verify response code of "CreateAccountNote" Api is <200>
    And response should have ErrorCode 10003 and ErrorMessage 'Duplicate Request ID'

  @CreateAccountNoteApiNullCustomerCode @Phase1
  Scenario: Verify CreateAccountNote Api request with Null CustomerCode
    When a request is made to the CreateAccountNote Api with Null CustomerCode
      | serviceNumber | 1    |
      | noteTypeCode  | ACCT |
      | noteText      | Test |
      | origin        | IVR  |
    Then verify response code of "CreateAccountNote" Api is <200>
    And response should have ErrorCode 10011 and ErrorMessage 'Missing Customer Code'

  @CreateAccountNoteApiInvalidParamFormat @Phase1
  Scenario Outline: Verify CreateAccountNote Api request with "<param>" value is "<paramValue>"
    When a request is made to the CreateAccountNote Api with "<param>" value is "<paramValue>"
      | serviceNumber | 1    |
      | noteTypeCode  | ACCT |
      | noteText      | Test |
      | origin        | IVR  |
    Then verify response code of "CreateAccountNote" Api is <200>
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
    Then verify response code of "CreateAccountNote" Api is <200>
    And response should have ErrorCode 40015 and ErrorMessage 'Invalid Account Number'

  @CreateAccountNoteApiNullNoteTypeCode @Phase1
  Scenario: Verify CreateAccountNote Api request with Null NoteTypeCode
    When a request is made to the CreateAccountNote Api with Null NoteTypeCode
      | serviceNumber | 1    |
      | noteText      | Test |
      | origin        | IVR  |
    Then verify response code of "CreateAccountNote" Api is <200>
    And response should have ErrorCode 10027 and ErrorMessage 'Missing Note Type Code'

  @CreateAccountNoteApiNullNoteText @Phase1
  Scenario: Verify CreateAccountNote Api request with Null NoteText
    When a request is made to the CreateAccountNote Api with Null NoteText
      | serviceNumber | 1    |
      | noteTypeCode  | ACCT |
      | origin        | IVR  |
    Then verify response code of "CreateAccountNote" Api is <200>
    And response should have ErrorCode 10025 and ErrorMessage 'Missing Note Text'

  @CreateAccountNoteApiNullOrigin @Phase1
  Scenario: Verify CreateAccountNote Api request with Null Origin
    When a request is made to the CreateAccountNote Api with Null Origin
      | serviceNumber | 1    |
      | noteTypeCode  | ACCT |
      | noteText      | Test |
    Then verify response code of "CreateAccountNote" Api is <200>
    And response should have ErrorCode 10029 and ErrorMessage 'Missing Origin'

  @CreateAccountNoteApiInvalidExpirationDateFormat @Phase1
  Scenario: Verify CreateAccountNote Api request with Invalid Expiration Date Format
    When a request is made to the CreateAccountNote Api with Invalid Expiration Date Format
      | serviceNumber  | 1            |
      | noteTypeCode   | ACCT         |
      | noteText       | Test         |
      | origin         | IVR          |
      | expirationDate | invalid-date |
    Then verify response code of "CreateAccountNote" Api is <200>
    And response should have ErrorCode 10031 and ErrorMessage 'Invalid Expiration Date Format'

  @CreateAccountNoteApiNonExistentServiceNoPremCode @Phase1
  Scenario: Verify CreateAccountNote Api request with non-existent combination of ServiceNumber and PremisesCode
    When a request is made to the CreateAccountNote Api with non-existent combination of ServiceNumber and PremisesCode
      | noteTypeCode | ACCT |
      | noteText     | Test |
      | origin       | IVR  |
    Then verify response code of "CreateAccountNote" Api is <200>
    And response should have ErrorCode 40043 and ErrorMessage 'Invalid Service Number'

  @CreateAccountNoteApiInvalidServiceNumberFormat @Phase1
  Scenario: Verify CreateAccountNote Api request with Invalid ServiceNumber Format that is more than 4-digit number
    When a request is made to the CreateAccountNote Api with Invalid ServiceNumber Format
      | noteTypeCode | ACCT |
      | noteText     | Test |
      | origin       | IVR  |
    Then verify response code of "CreateAccountNote" Api is <200>
    And response should have ErrorCode 10045 and ErrorMessage 'Invalid Service Number Format'

  @CreateAccountNoteApiNonExistentNoteType @Phase1
  Scenario: Verify CreateAccountNote Api request with non-existent NoteType
    When a request is made to the CreateAccountNote Api with non-existent NoteType
      | noteText | Test |
      | origin   | IVR  |
    Then verify response code of "CreateAccountNote" Api is <200>
    And response should have ErrorCode 40045 and ErrorMessage 'Invalid Note Type'