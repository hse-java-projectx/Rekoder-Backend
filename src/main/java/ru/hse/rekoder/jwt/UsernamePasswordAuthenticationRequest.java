package ru.hse.rekoder.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UsernamePasswordAuthenticationRequest {
    @JsonProperty("id")
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
