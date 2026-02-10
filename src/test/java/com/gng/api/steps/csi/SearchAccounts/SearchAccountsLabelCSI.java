package com.gng.api.steps.csi.SearchAccounts;

public enum SearchAccountsLabelCSI {

    search_accounts_csi,

    // NEGATIVE CASES
    TC_188__Negative__Missing_Request_ID,
    TC_189__Negative__Invalid_Request_ID_Length,
    TC_190__Negative__Duplicate_Request_ID,

    TC_191__Negative__Invalid_customerCode_Length,
    TC_192__Negative__Invalid_customerCode_Format,

    TC_193__Negative__Invalid_premisesCode_Length,
    TC_194__Negative__Invalid_premisesCode_Format,

    TC_195__Negative__Invalid_Last_or_Business_Name_Length,
    TC_196__Negative__Invalid_First_Name_Length,

    TC_197__Negative__Invalid_Last_Four_SSN_Length_Too_Long,
    TC_198__Negative__Invalid_Last_Four_SSN_Length_Too_Short,

    TC_199__Negative__Invalid_Federal_Tax_ID_Length_Too_Long,
    TC_200__Negative__Invalid_Federal_Tax_ID_Length_Too_Short,

    TC_201__Negative__Invalid_Email_Address_Format,

    TC_202__Negative__Invalid_Phone_Number_Length_Too_Long,
    TC_203__Negative__Invalid_Phone_Number_Length_Too_Short,

    TC_204__Negative__Invalid_Username_Format,

    // NEW NEGATIVE CASES (TC_206 - TC_223)
    TC_205__Negative__Inactive_Username,
    TC_206__Negative__Invalid_Password_Format_Length_Too_Short,
    TC_207__Negative__Invalid_Password_Format_Length_Too_Long,

    TC_208__Negative__Last4SSN_____Password,
    TC_209__Negative__Last4SSN_____Username,
    TC_210__Negative__Last4SSN_____CustomerCode_____PremisesCode,
    TC_211__Negative__Last4SSN_____FederalTaxID_Number,

    TC_212__Negative__Password_____FederalTaxID_Number,

    TC_213__Negative__CustomerLastName_____EmailAddress,
    TC_214__Negative__CustomerLastName_____PhoneNumber,
    TC_215__Negative__CustomerLastName_____Username,
    TC_216__Negative__CustomerLastName_____CustomerCode_____PremisesCode,

    TC_217__Negative__EmailAddress_____PhoneNumber,
    TC_218__Negative__EmailAddress_____Username,
    TC_219__Negative__EmailAddress_____CustomerCode_____PremisesCode,
    TC_220__Negative__EmailAddress_____FederalTaxID_Number,

    TC_221__Negative__PhoneNumber_____Username,
    TC_222__Negative__PhoneNumber_____CustomerCode_____PremisesCode,
    TC_223__Negative__PhoneNumber_____FederalTaxID_Number,
    TC_224__Negative__Username_____CustomerCode_____PremisesCode,
    TC_225__Negative__Username_____FederalTaxID_Number,
    TC_226__Negative__CustomerCode_____PremisesCode_____FederalTaxID_Number,
    TC_227__Negative__Too_Many_Matches,

    // NEW POSITIVE CASES (TC_233 - TC_235)
    TC_228__Positive__Last4SSN_____CustomerLastName,
    TC_229__Positive__Last4SSN_____EmailAddress,
    TC_230__Positive__Last4SSN_____PhoneNumber,

    // NEW POSITIVE CASES (TC_236 - TC_241)
    TC_231__Positive__Password_____Username,
    TC_232__Positive__Password_____CustomerLastNameBusiness,
    TC_233__Positive__Password_____CustomerCode_____PremisesCode,
    TC_234__Positive__Password_____EmailAddress,
    TC_235__Positive__Password_____PhoneNumber,
    TC_236__Positive__BusinessName_____FederalTaxID,

    // NEW POSITIVE CASES (TC_242 - TC_245)
    TC_237__Positive__AccountStatus_Active,
    TC_238__Positive__AccountStatus_Final,
    TC_239__Positive__AccountStatus_Inactive,
    TC_240__Positive__AccountStatus_New,

    // NEW POSITIVE CASES (TC_246 - TC_256)
    TC_241__Positive__Username,
    TC_242__Positive__No_Username,
    TC_243__Positive__Nickname,
    TC_244__Positive__No_Nickname,
    TC_245__Positive__FirstName,
    TC_246__Positive__No_FirstName,
    TC_247__Positive__Residential,
    TC_248__Positive__Commercial,
    TC_249__Positive__Industrial,
    TC_250__Positive__Agriculture,
    TC_251__Positive__MultiFamily,

    // NEW POSITIVE CASES (TC_257 - TC_267)
    TC_252__Positive__Seasonal,
    TC_253__Positive__SeniorCitizen,
    TC_254__Positive__PremisesAddress_StreetNumber,
    TC_255__Positive__PremisesAddress_No_StreetNumber,
    TC_256__Positive__PremisesAddress_StreetPreDirection,
    TC_257__Positive__PremisesAddress_No_StreetPreDirection,
    TC_258__Positive__PremisesAddress_StreetName,
    TC_259__Positive__PremisesAddress_No_StreetName,
    TC_260__Positive__PremisesAddress_StreetSuffix,
    TC_261__Positive__PremisesAddress_No_StreetSuffix,
    TC_262__Positive__PremisesAddress_StreetPostDirection,

    // NEW POSITIVE CASES (TC_268 - TC_278)
    TC_263__Positive__PremisesAddress_No_StreetPostDirection,
    TC_264__Positive__PremisesAddress_UnitType,
    TC_265__Positive__PremisesAddress_No_UnitType,
    TC_266__Positive__PremisesAddress_UnitNumber,
    TC_267__Positive__PremisesAddress_No_UnitNumber,
    TC_268__Positive__PremisesAddress_City,
    TC_269__Positive__PremisesAddress_No_City,
    TC_270__Positive__PremisesAddress_State,
    TC_271__Positive__PremisesAddress_No_State,
    TC_272__Positive__PremisesAddress_ZipCode,
    TC_273__Positive__PremisesAddress_No_ZipCode,
    TC_274__Positive__Search_Order__Active_______Final_______New_______Inactive,
    TC_275__Positive__Search_Order__Active_______Final_______New,
    TC_276__Positive__Search_Order__Active_______Final_______Inactive
}
