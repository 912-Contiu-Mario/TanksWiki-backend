package dev.tankswikibackend.Controller;

import dev.tankswikibackend.Authentication.JwtUtil;
import dev.tankswikibackend.Authentication.Request.LoginReq;
import dev.tankswikibackend.Authentication.Request.RegisterReq;
import dev.tankswikibackend.Authentication.Response.ErrorRes;
import dev.tankswikibackend.Authentication.Response.LoginRes;
import dev.tankswikibackend.Entity.User;
import dev.tankswikibackend.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private UserService userService;
    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody LoginReq loginReq)  {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getEmail(), loginReq.getPassword()));
            String email = authentication.getName();
            User user = new User(email,"", "");

            String token = jwtUtil.createToken(user);
                LoginRes loginRes = new LoginRes(this.userService.findIdByEmail(email), email,this.userService.findUsernameByEmail(email),  token);
            return ResponseEntity.ok(loginRes);

        }catch (BadCredentialsException e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }catch (Exception e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody RegisterReq registerReq) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
            try {
                User newUser = userService.createUser(registerReq.getEmail(),registerReq.getUsername(),  registerReq.getPassword(), encoder);
                Authentication authentication =
                        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(registerReq.getEmail(), registerReq.getPassword()));
                String email = authentication.getName();
                User user = new User(email,"", "");
                String token = jwtUtil.createToken(user);
                LoginRes loginRes = new LoginRes(newUser.getId(), email, registerReq.getUsername(), token);
                return ResponseEntity.ok(loginRes);
            } catch (Exception exception) {
                return ResponseEntity.badRequest().body(exception.getMessage());
            }
    }
}
