package dev.tankswikibackend.Repository;

import dev.tankswikibackend.Entity.Tank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TankRepository extends JpaRepository<Tank, Long> {


}
