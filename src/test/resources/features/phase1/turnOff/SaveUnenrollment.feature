Feature: Verify SaveUnenrollment Api

  Background: Generate Authentication Token
    When a request is made to generate authentication token
    Then verify Authentication Token Api response status code is 200
    And a valid token is received in response

  @PositiveResidentialTurnOff_UC28 @HappyFlow @TC207 @SaveUnenrollment
  Scenario: Verify SaveUnenrollment Api for Active Residential account with MVS Plan TC207
    When a request is made to the SaveUnenrollment Api for account with "MVS" plan "RS" type with forwardingAddressIs "CA" with type "not present" and turnoffreason "Seasonal or Heat Only" and setEmail false with etcExists false
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @PositiveResidentialTurnOff_UC29 @HappyFlow @TC208 @SaveUnenrollment
  Scenario: Verify SaveUnenrollment Api for Active Residential account with MI Plan TC208
    When a request is made to the SaveUnenrollment Api for account with "MI" plan "RS" type with forwardingAddressIs "NA" with type "S" and turnoffreason "Moving - Outside AGLC" and setEmail true with etcExists false
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @PositiveResidentialTurnOff_UC30 @HappyFlow @TC209 @SaveUnenrollment
  Scenario: Verify SaveUnenrollment Api for Active Residential account with MAP Plan TC209
    When a request is made to the SaveUnenrollment Api for account with "MAP" plan "RS" type with forwardingAddressIs "NA" with type "R" and turnoffreason "Other - Military" and setEmail true with etcExists false
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @PositiveResidentialTurnOff_UC31 @HappyFlow @TC210 @SaveUnenrollment
  Scenario: Verify SaveUnenrollment Api for Active Residential account with CSV Plan TC210
    When a request is made to the SaveUnenrollment Api for account with "CSV" plan "RS" type with forwardingAddressIs "NA" with type "P" and turnoffreason "REAP/Realtor Inspection" and setEmail false with etcExists false
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @PositiveResidentialTurnOff_UC35 @HappyFlow @TC214 @SaveUnenrollment
  Scenario: Verify SaveUnenrollment Api for Active Residential account with PRP Plan TC214
    When a request is made to the SaveUnenrollment Api for account with "PRP" plan "RS" type with forwardingAddressIs "NA" with type "S" and turnoffreason "Household Account Change" and setEmail true with etcExists false
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @PositiveResidentialTurnOff_UC36 @HappyFlow @TC215 @SaveUnenrollment
  Scenario: Verify SaveUnenrollment Api for Active Residential account with TRD Plan TC215
    When a request is made to the SaveUnenrollment Api for account with "TRD" plan "RS" type with forwardingAddressIs "CA" with type "not present" and turnoffreason "Other - financial situation" and setEmail true with etcExists false
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @PositiveResidentialTurnOff_UC37 @HappyFlow @TC216 @SaveUnenrollment
  Scenario: Verify SaveUnenrollment Api for Active Residential account with MVS Plan TC216
    When a request is made to the SaveUnenrollment Api for account with "MVS" plan "RS" type with forwardingAddressIs "CA" with type "not present" and turnoffreason "Moving - Not staying with GNG" and setEmail false with etcExists false
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @PositiveResidentialTurnOff_UC40 @HappyFlow @TC219 @SaveUnenrollment
  Scenario: Verify SaveUnenrollment Api for Active Residential account with MVS Plan TC219
    When a request is made to the SaveUnenrollment Api for account with "MVS" plan "RS" type with forwardingAddressIs "CA" with type "not present" and turnoffreason "Other - Regulated Provider" and setEmail false with etcExists false
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @PositiveCommercialTurnOff_UC44 @HappyFlow @TC223 @SaveUnenrollment
  Scenario: Verify SaveUnenrollment Api for Active Commercial account with CCV Plan TC223
    When a request is made to the SaveUnenrollment Api for account with "CCV" plan "CM" type with forwardingAddressIs "CA" with type "not present" and turnoffreason "Seasonal or Heat Only" and setEmail false with etcExists false
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @PositiveResidentialTurnOff_UC43 @HappyFlow @TC222 @SaveUnenrollment
  Scenario: Verify SaveUnenrollment Api for Active Residential account with VML Plan TC222
    When a request is made to the SaveUnenrollment Api for account with "VML" plan "RS" type with forwardingAddressIs "CA" with type "not present" and turnoffreason "Other - Deceased" and setEmail false with etcExists false
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode 0 and ErrorMessage ""

  @SaveUnenrollmentWithInvalidRequestID @NegativeFlow @SaveUnenrollment
  Scenario Outline: Verify SaveUnenrollment Api with invalid requestID "<requestID>"
    When a request is made to the SaveUnenrollment Api with "<requestID>"
    Then verify response code of "SaveUnenrollment" Api is 200
    And response should have ErrorCode <errorCode> and ErrorMessage "<errorMessage>"

    Examples:
      | requestID            | errorCode | errorMessage         |
      | EMPTY_REQUEST_ID     | 10001     | Missing Request ID   |
      | DUPLICATE_REQUEST_ID | 10003     | Duplicate Request ID |
      | LONG_REQUEST_ID      | 10002     | Invalid Request ID   |