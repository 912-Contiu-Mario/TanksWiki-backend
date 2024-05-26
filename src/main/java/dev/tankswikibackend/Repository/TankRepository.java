package dev.tankswikibackend.Repository;

import dev.tankswikibackend.Entity.Tank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TankRepository extends JpaRepository<Tank, Long> {
    List<Tank> findByTankName(String tankName);
    List<Tank> findByUserId(Long userId);
}
