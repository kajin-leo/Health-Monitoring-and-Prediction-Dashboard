package com.cs79_1.interactive_dashboard.DTO.Authenticate;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private long userId;
    private String userName;
    private String role;

    public LoginResponse() {}

    public LoginResponse(String accessToken, String refreshToken, String tokenType, long userId, String userName, String role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.userId = userId;
        this.userName = userName;
        this.role = role;
    }
}
