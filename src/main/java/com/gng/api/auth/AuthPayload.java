package com.gng.api.auth;

import lombok.Data;

@Data
public class AuthPayload {
    private String vendorId;
    private String vendorSecret;
}
