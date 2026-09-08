package com.example.tasks.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRegisterDTO {

    private String name;
    private String email;
    private String password;

}