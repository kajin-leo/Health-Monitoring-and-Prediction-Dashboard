package com.cs79_1.interactive_dashboard.DTO;

import lombok.Data;

@Data
public class UpdateUserInfoRequest {
    private String username;
    private String password;    
    private String firstName;   
    private String lastName;    
    private Integer ageYear;    
    private Integer sex;        

    public UpdateUserInfoRequest() {}
}