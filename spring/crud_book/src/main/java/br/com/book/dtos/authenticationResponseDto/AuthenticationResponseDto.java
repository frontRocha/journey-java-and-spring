package br.com.book.dtos.authenticationResponseDto;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthenticationResponseDto {
    private String token;
    private Map<String, Object> data;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
