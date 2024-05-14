package dev.tankswikibackend.Repository;

import dev.tankswikibackend.Entity.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByTankId(Long tankId);
    List<Module> findByTankIdAndModuleType(Long tankId, String moduleType);
    Page<Module> findByTankIdAndModuleType(Long tankId, String moduleType, Pageable pageable);


}
