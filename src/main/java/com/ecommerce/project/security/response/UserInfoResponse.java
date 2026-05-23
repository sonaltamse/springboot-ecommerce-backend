package com.ecommerce.project.security.response;
import java.util.List;
public class UserInfoResponse {
    private Long id;
    private String token;
    private String userName;
    private List<String> roles;
    public UserInfoResponse(Long id,String token, String userName, List<String> roles) {
        this.id = id;
        this.token = token;
        this.userName = userName;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public List<String> getRoles() {
        return roles;
    }
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
