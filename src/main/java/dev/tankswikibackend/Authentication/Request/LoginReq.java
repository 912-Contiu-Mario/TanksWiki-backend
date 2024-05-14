package dev.tankswikibackend.Authentication.Request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class LoginReq {
    private String email;
    private String password;
}
