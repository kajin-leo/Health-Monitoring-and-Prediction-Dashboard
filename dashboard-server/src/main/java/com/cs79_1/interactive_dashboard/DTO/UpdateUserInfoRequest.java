package com.cs79_1.interactive_dashboard.DTO;

public class UpdateUserInfoRequest {
    private String username;
    private String password;    
    private String firstName;   
    private String lastName;    
    private Integer ageYear;    
    private Integer sex;        

    public UpdateUserInfoRequest() {}

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Integer getAgeYear() { return ageYear; }
    public void setAgeYear(Integer ageYear) { this.ageYear = ageYear; }

    public Integer getSex() { return sex; }
    public void setSex(Integer sex) { this.sex = sex; }
}