package dev.tankswikibackend.Controller;

import dev.tankswikibackend.Entity.*;
import dev.tankswikibackend.Service.UserService;
import jakarta.annotation.security.RolesAllowed;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/users")

@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }


    @PostMapping("")
    @RolesAllowed({"ROLE_ADMIN"})
    public ResponseEntity<?> newUser(@RequestBody User newUser){
        try{
           return ResponseEntity.ok().body(userService.addUser(newUser));
        }
        catch (RepositoryException repositoryException){
            return ResponseEntity.internalServerError().body(repositoryException.getMessage());
        }
        catch(InvalidUserException invalidUserException){
            return ResponseEntity.badRequest().body(invalidUserException.getMessage());
        }
    }

    @GetMapping("")
    @RolesAllowed({"ROLE_ADMIN"})
    ResponseEntity<?> allUsers(){
        try{
            return ResponseEntity.ok().body(userService.getAllUsers());
        }
        catch (Exception exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

    @RolesAllowed({"ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteUser(@PathVariable Long id){
        try{
            userService.deleteUser(id);
            return ResponseEntity.ok().body("User deleted");
        }
        catch(RepositoryException exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

    @RolesAllowed("ROLE_ADMIN")
    @PutMapping("/{id}")
    ResponseEntity<String> updateTank(@PathVariable Long id, @RequestBody User updatedUser){
        try{
            userService.updateUser(id, updatedUser);
            return ResponseEntity.ok().body("User updated");
        }
        catch(RepositoryException exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
        catch (InvalidUserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }







}
