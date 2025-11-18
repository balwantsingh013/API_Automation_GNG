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
      | testCondition                                            | errorCode | errorMessage                           |
      | REQUEST_ID_MISSING_NEGATIVE_TC65                         | 10001     | Missing Request ID                     |
      | REQUEST_ID_DUPLICATE_NEGATIVE_TC66                       | 10003     | Duplicate Request ID                   |
      | CUSTOMER_CODE_NULL_NEGATIVE_TC67                         | 10011     | Missing Customer Code                  |
      | PREMISES_CODE_NULL_NEGATIVE_TC68                         | 10013     | Missing Premises Code                  |
      | CUSTOMER_CODE_LENGTH_GT9_NEGATIVE_TC69                   | 10015     | Invalid Customer Code Format           |
      | PREMISES_CODE_LENGTH_GT7_NEGATIVE_TC70                   | 10005     | Invalid Premises Code Format           |
      | ACCOUNT_COMBINATION_INVALID_NEGATIVE_TC71                | 40015     | Invalid Account Number                 |
      | NOTE_TYPE_CODE_NULL_NEGATIVE_TC72                        | 10027     | Missing Note Type Code                 |
      | NOTE_TEXT_NULL_NEGATIVE_TC73                             | 10025     | Missing Note Text                      |
      | ORIGIN_NULL_NEGATIVE_TC74                                | 10029     | Missing Origin                         |
      | EXPIRATION_DATE_INVALID_FORMAT_NEGATIVE_TC75             | 10031     | Invalid Suspense Date Format           |
      #TC75 should be-> 10031 'Invalid Expiration Date Format'
      | SUSPENSE_DATE_INVALID_FORMAT_NEGATIVE_TC76               | 10033     | Invalid Suspense Date Format           |
      | SERVICE_NUMBER_NOT_FOUND_FOR_PREMISES_NEGATIVE_TC77      | 40043     | Invalid Service Number                 |
      | SERVICE_NUMBER_INVALID_FORMAT_NEGATIVE_TC78              | 10045     | Invalid Service Number Format          |
      | NOTE_TYPE_INVALID_NEGATIVE_TC79                          | 40045     | Invalid Note Type                      |

  @CreateBannerNotesPositiveFlows @HappyFlow @Phase1
  Scenario Outline: Verify CreateBannerNotes positive flows for "<testCondition>"
    When a request is made to the CreateBannerNotes Api with valid parameters "<noteText>" noteText "<noteTypeCode>" noteTypeCode "<userIDRemind>" userIDRemind for "<testCondition>" condition
    Then verify response code of "CreateBannerNotes" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""
    And verify NoteSequenceNumber match the value in the database
    And verify banner note is created successfully with "<noteText>" noteText for "<testCondition>" condition

    Examples:
      | testCondition                         | noteText                                         | noteTypeCode | userIDRemind |
      | CREATE_NOTE_ACCT_POSITIVE_TC80        | Phone number 1234567890 listed \|~ as Caller ID. |   ACCT       |              |
      | CREATE_NOTE_PMT_RPT_POSITIVE_TC81     | Payment received 10/01 via IVR\|~ Ref: 555001    |   PMTRPT     | SYSTEM       |
      | CREATE_NOTE_IVR_NPA_POSITIVE_TC82     | IVR NPA/NXX captured 404-555\|~ 1212             |   IVRNPA     |              |
      | CREATE_NOTE_IVR_NPA_ALT_POSITIVE_TC83 | IVR NPA/NXX captured 770-555\|~ 8989 ext\|~ 42   |   IVRNPA     |              |

