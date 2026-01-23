package com.gng.api.steps.csi.SearchAccounts;

public enum SearchAccountsLabelCSI {

    search_accounts_csi,

    // NEGATIVE CASES
    TC_189__Negative__Missing_Request_ID,
    TC_190__Negative__Invalid_Request_ID_Length,
    TC_191__Negative__Duplicate_Request_ID,

    TC_192__Negative__Invalid_customerCode_Length,
    TC_193__Negative__Invalid_customerCode_Format__Not_String,

    TC_194__Negative__Invalid_premisesCode_Length,
    TC_195__Negative__Invalid_premisesCode_Format__Not_String,

    TC_196__Negative__Invalid_Last_or_Business_Name_Length,
    TC_197__Negative__Invalid_First_Name_Length,

    TC_198__Negative__Invalid_Last_Four_SSN_Length_Too_Long,
    TC_199__Negative__Invalid_Last_Four_SSN_Length_Too_Short,

    TC_200__Negative__Invalid_Federal_Tax_ID_Length_Too_Long,
    TC_201__Negative__Invalid_Federal_Tax_ID_Length_Too_Short,

    TC_202__Negative__Invalid_Email_Address_Format,

    TC_203__Negative__Invalid_Phone_Number_Length_Too_Long,
    TC_204__Negative__Invalid_Phone_Number_Length_Too_Short,

    TC_205__Negative__Invalid_Username_Format,

    // NEW NEGATIVE CASES (TC_206 - TC_223)
    TC_206__Negative__Inactive_Username,
    TC_207__Negative__Invalid_Password_Format_Length_Too_Short,
    TC_208__Negative__Invalid_Password_Format_Length_Too_Long,

    TC_209__Negative__Last4SSN_Password,
    TC_210__Negative__Last4SSN_Username,
    TC_211__Negative__Last4SSN_CustomerCode_PremisesCode,
    TC_212__Negative__Last4SSN_FederalTaxID,

    TC_213__Negative__Password_FederalTaxID,

    TC_214__Negative__CustomerLastName_EmailAddress,
    TC_215__Negative__CustomerLastName_PhoneNumber,
    TC_216__Negative__CustomerLastName_Username,
    TC_217__Negative__CustomerLastName_CustomerCode_PremisesCode,

    TC_218__Negative__BusinessName_EmailAddress,
    TC_219__Negative__BusinessName_PhoneNumber,
    TC_220__Negative__BusinessName_Username,
    TC_221__Negative__BusinessName_CustomerCode_PremisesCode,

    TC_222__Negative__EmailAddress_PhoneNumber,
    TC_223__Negative__EmailAddress_Username,

    // NEW NEGATIVE CASES (TC_224 - TC_232)
    TC_224__Negative__EmailAddress_CustomerCode_PremisesCode,
    TC_225__Negative__EmailAddress_FederalTaxID,
    TC_226__Negative__PhoneNumber_Username,
    TC_227__Negative__PhoneNumber_CustomerCode_PremisesCode,
    TC_228__Negative__PhoneNumber_FederalTaxID,
    TC_229__Negative__Username_CustomerCode_PremisesCode,
    TC_230__Negative__Username_FederalTaxID,
    TC_231__Negative__CustomerCode_PremisesCode_FederalTaxID,
    TC_232__Negative__Too_Many_Matches,

    // NEW POSITIVE CASES (TC_233 - TC_235)
    TC_233__Positive__Last4SSN_CustomerLastName,
    TC_234__Positive__Last4SSN_EmailAddress,
    TC_235__Positive__Last4SSN_PhoneNumber,

    // NEW POSITIVE CASES (TC_236 - TC_241)
    TC_236__Positive__Password_Username,
    TC_237__Positive__Password_CustomerLastNameBusiness,
    TC_238__Positive__Password_CustomerCode_PremisesCode,
    TC_239__Positive__Password_EmailAddress,
    TC_240__Positive__Password_PhoneNumber,
    TC_241__Positive__BusinessName_FederalTaxID,

    // NEW POSITIVE CASES (TC_242 - TC_245)
    TC_242__Positive__AccountStatus_Active,
    TC_243__Positive__AccountStatus_Final,
    TC_244__Positive__AccountStatus_Inactive,
    TC_245__Positive__AccountStatus_New,

    // NEW POSITIVE CASES (TC_246 - TC_256)
    TC_246__Positive__Username,
    TC_247__Positive__No_Username,
    TC_248__Positive__Nickname,
    TC_249__Positive__No_Nickname,
    TC_250__Positive__FirstName,
    TC_251__Positive__No_FirstName,
    TC_252__Positive__Residential,
    TC_253__Positive__Commercial,
    TC_254__Positive__Industrial,
    TC_255__Positive__Agriculture,
    TC_256__Positive__MultiFamily,

    // NEW POSITIVE CASES (TC_257 - TC_267)
    TC_257__Positive__Seasonal,
    TC_258__Positive__SeniorCitizen,
    TC_259__Positive__PremisesAddress_StreetNumber,
    TC_260__Positive__PremisesAddress_No_StreetNumber,
    TC_261__Positive__PremisesAddress_StreetPreDirection,
    TC_262__Positive__PremisesAddress_No_StreetPreDirection,
    TC_263__Positive__PremisesAddress_StreetName,
    TC_264__Positive__PremisesAddress_No_StreetName,
    TC_265__Positive__PremisesAddress_StreetSuffix,
    TC_266__Positive__PremisesAddress_No_StreetSuffix,
    TC_267__Positive__PremisesAddress_StreetPostDirection,

    // NEW POSITIVE CASES (TC_268 - TC_278)
    TC_268__Positive__PremisesAddress_No_StreetPostDirection,
    TC_269__Positive__PremisesAddress_UnitType,
    TC_270__Positive__PremisesAddress_No_UnitType,
    TC_271__Positive__PremisesAddress_UnitNumber,
    TC_272__Positive__PremisesAddress_No_UnitNumber,
    TC_273__Positive__PremisesAddress_City,
    TC_274__Positive__PremisesAddress_No_City,
    TC_275__Positive__PremisesAddress_State,
    TC_276__Positive__PremisesAddress_No_State,
    TC_277__Positive__PremisesAddress_ZipCode,
    TC_278__Positive__PremisesAddress_No_ZipCode
}
