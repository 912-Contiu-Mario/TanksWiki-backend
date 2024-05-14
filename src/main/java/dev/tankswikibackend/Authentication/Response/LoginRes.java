package dev.tankswikibackend.Authentication.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Setter
@Getter
public class LoginRes {
    private String email;
    private String token;
}



