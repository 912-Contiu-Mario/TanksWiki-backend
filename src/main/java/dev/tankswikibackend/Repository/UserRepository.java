package dev.tankswikibackend.Repository;

import dev.tankswikibackend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);

}
