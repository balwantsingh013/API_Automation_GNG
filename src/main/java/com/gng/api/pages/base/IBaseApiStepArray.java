package com.gng.api.pages.base;

import io.restassured.response.Response;

import java.util.Map;

/**
 * Interface with T1 and T2 as Array Object Parameter
 *
 * @param <T1>
 * @param <T2>
 */
public interface IBaseApiStepArray<T1, T2> {

    void setRequestSpecification(String apiName, T1[] t1);

    T1[] getApiPayload(String apiName, T1[] t1);

    T1[] deserializeJsonToPojo(String apiName);

    T2 deserializeResponseToPojo(String apiName, Response response);

    Map<String, String> getApiHeaders(String apiName);

    String getApiQueryParams(String apiName);

}
