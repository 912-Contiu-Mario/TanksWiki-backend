package dev.tankswikibackend.Authentication.Request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RegisterReq {
    private String email;
    private String password;
}
