package com.example.project.model;

/**
 * @author u7580335 Shiying Cai
 */
public class User {
    private String userName;
    private String password;

    public User() {}

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
