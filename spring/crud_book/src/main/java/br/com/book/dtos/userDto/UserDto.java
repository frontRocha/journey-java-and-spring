package br.com.book.dtos.userDto;

import jakarta.validation.constraints.NotBlank;

public class UserDto {
    @NotBlank
    private String login;
    @NotBlank
    private String password;
    @NotBlank
    private Integer id;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
