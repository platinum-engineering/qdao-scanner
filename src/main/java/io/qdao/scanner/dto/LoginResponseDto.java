package io.qdao.scanner.dto;

import java.io.Serializable;

public class LoginResponseDto implements Serializable {

    private String token;

    public LoginResponseDto() { }

    public LoginResponseDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
