package dev.tankswikibackend.Service;

import dev.tankswikibackend.Entity.RepositoryException;
import dev.tankswikibackend.Entity.User;
import dev.tankswikibackend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
   public User createUser(String email, String unencryptedPassword, PasswordEncoder encoder) throws RepositoryException {
        try{
            this.validateEmail(email);
            this.validatePassword(unencryptedPassword);
            User userToCreate = new User(email, encoder.encode(unencryptedPassword));
            return userRepository.save(userToCreate);

        }

        catch (Exception exception){
            System.out.println(exception.getMessage());
            throw new RepositoryException("Couldn't create User");
        }
    }

    void validateEmail(String email){

    }
    void validatePassword(String password){

    }



}
