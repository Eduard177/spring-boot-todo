package com.pichardo.SpringTodoApp.dto;

import lombok.Data;

@Data
public class LoginRes {
    private String username;
    private String token;

    public LoginRes(String username, String jwt) {
        this.username = username;
        this.token = jwt;
    }
}
