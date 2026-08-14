package com.gng.api.pojo.CSIPojo.VerifyAccount;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Preferences requestbroker wrapper: {@code { "Request": "<stringified JSON>" }}. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PreferencesBrokerEnvelope {

    @JsonProperty("Request")
    private String request;
}
