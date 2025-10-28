package com.cs79_1.interactive_dashboard.DTO;

import lombok.Data;

@Data
public class UserInfoResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String appearance;

    private long userId;
    private int ageYear;
    private int sex;
  
    public UserInfoResponse(String username, String firstName, String lastName, int ageYear, int sex, long userId, String appearance) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.ageYear = ageYear;
        this.sex = sex;
        this.appearance = appearance;
    }
}
