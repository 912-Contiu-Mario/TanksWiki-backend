package dev.tankswikibackend.Service;

import dev.tankswikibackend.Entity.*;
import dev.tankswikibackend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class UserService {
    private UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
   public User addUser(User newUser) throws RepositoryException, InvalidUserException {
       String email = newUser.getEmail();
       String unencryptedPassword = newUser.getPassword();
       String username = newUser.getUsername();
       String role = newUser.getRole();
       this.validateEmail(email);
       this.validateUsername(username);
       this.validatePassword(unencryptedPassword);
       this.validateRole(role);
        try{
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
            User userToCreate = new User(email, username, role,  encoder.encode(unencryptedPassword));
            return userRepository.save(userToCreate);
        }


        catch (Exception exception){
            throw new RepositoryException("Couldn't save User");
        }
    }




    void validateEmail(String email){

    }
    void validatePassword(String password){

    }

    void validateRole(String role) throws InvalidUserException {
        if(!role.equals("USER") && !role.equals("MANAGER") && !role.equals("ADMIN")){
            throw new InvalidUserException("Invalid role");
        }
    }

    void validateUsername(String username){

    }

    public String findUsernameByEmail(String email){
        return this.userRepository.findByEmail(email).getUsername();
    }

    public Long findIdByEmail(String email){
        return this.userRepository.findByEmail(email).getId();
    }


    public String getUserRole(User user){
        return this.userRepository.findByEmail(user.getEmail()).getRole();
    }

    public User findUserByEmail(String email){
        return this.userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() throws RepositoryException {
        try{
            return this.userRepository.findAll();
        }
        catch(Exception exception){
            throw(new RepositoryException("Couldn't fetch users"));
        }
    }

    public void deleteUser(Long id)throws RepositoryException{
        Optional<User> userToDelete = userRepository.findById(id);
        if(userToDelete.isEmpty()){
            throw new RepositoryException("User you want to delete doesn't exist");
        }
        if(Objects.equals(userToDelete.get().getRole(), "ADMIN")){
            throw new RepositoryException("Cannot delete ADMIN ROLES");
        }
        try{
            userRepository.deleteById(id);
        }
        catch(Exception exception){
            throw  new RepositoryException("Couldn't delete user");
        }
    }

    public void updateUser(Long id, User updatedUser) throws RepositoryException, InvalidUserException {

        this.validateEmail(updatedUser.getEmail());
        this.validateUsername(updatedUser.getUsername());
        this.validatePassword(updatedUser.getPassword());
        this.validateRole(updatedUser.getRole());
        Optional<User> userToUpdate = userRepository.findById(id);
        if(userToUpdate.isEmpty()){
            throw new RepositoryException("User you want to update does not exist");
        }

        if(Objects.equals(userToUpdate.get().getRole(), "ADMIN")){
            throw new RepositoryException("Cannot update ADMIN ROLES");
        }
        try{
                updatedUser.setId(id);
                userRepository.save(updatedUser);

        }
        catch (Exception exception){
            throw new RepositoryException("Couldn't update user");
        }
    }






}
