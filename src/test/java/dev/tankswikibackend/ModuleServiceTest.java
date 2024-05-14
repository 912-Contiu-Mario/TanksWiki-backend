package dev.tankswikibackend;


import dev.tankswikibackend.Entity.Module;
import dev.tankswikibackend.Entity.RepositoryException;
import dev.tankswikibackend.Repository.ModuleRepository;
import dev.tankswikibackend.Service.ModuleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ModuleServiceTest {
    @Mock
    ModuleRepository moduleRepository;

    @InjectMocks
    ModuleService moduleService;


    @Test
    public void testAddModule() throws RepositoryException {
        Module newModule = new Module(1L, 60L,"Gun variation 1", "gun", 55, 30, 20, 155 );

        when(moduleRepository.save(any(Module.class))).thenReturn(newModule);
        assert(moduleService.addModule(newModule) == 1L);
    }

    @Test
    public void testUpdateModule_Found() throws RepositoryException {
        Module newModule = new Module(60L,"Gun variation 1", "gun", 55, 30, 20, 155 );
        when(moduleRepository.findById(newModule.getId())).thenReturn(Optional.of(newModule));

        when(moduleRepository.save(newModule)).thenReturn(newModule);

        moduleService.updateModule(newModule.getId(), newModule);
    }


    @Test
    public void testDeleteModule_Found() throws RepositoryException{
        Long moduleId = 1L;

        moduleService.deleteModule(moduleId);
        verify(moduleRepository).deleteById(moduleId);
    }


    @Test
    public void testGetTankModules_Works() throws RepositoryException {
        Long tankId = 60L;
        Module module1 = new Module(60L,"Gun variation 1", "gun", 55, 30, 20, 155 );
        Module module2 = new Module(60L,"Gun variation 2", "gun", 60, 40, 30, 160 );
        List<Module> list = List.of(module1, module2);

        when(moduleRepository.findByTankId(tankId)).thenReturn(list);
        moduleService.getTankModules(tankId);
    }


    @Test
    public void testGetTankModuleByType_Works() throws RepositoryException {

        Long tankId = 60L;
        String type = "gun";
        Module module1 = new Module(60L,"Gun variation 1", "gun", 55, 30, 20, 155 );
        Module module2 = new Module(60L,"Gun variation 2", "gun", 60, 40, 30, 160 );
        List<Module> list = List.of(module1, module2);

        when(moduleRepository.findByTankIdAndModuleType(tankId, type)).thenReturn(list);
        moduleService.getTankModulesByType(tankId, type);

    }
    @Test
    public void testGenerateFakeModules() {
        // Assume moduleService is instantiated

        // Module types to test
        String[] moduleTypes = {"engine", "suspension", "gun", "radio"};

        for (String type : moduleTypes) {
            Module module = moduleService.generateFakeModule(1L, type);

            // Check weight for all - always relevant
            assertTrue(module.getModuleWeight() >= 0 && module.getModuleWeight() <= 100000, "Weight should be within 0-100000 for " + type);

            // Check properties based on module type
            switch (type) {
                case "engine":
                    assertTrue(module.getModuleHorsepower() >= 0 && module.getModuleHorsepower() <= 10000, "Horsepower should be within 0-10000 for engine");
                    assertNull(module.getModuleLoadLimit() , "Load limit should be within 0-1000 for engine");
                    assertNull(module.getModuleDamage());
                    assertNull( module.getModuleRateOfFire(), "Rate of fire should be zero for engine");
                    assertNull(module.getModulePenetration(), "Penetration should be zero for engine");
                    assertNull(module.getModuleSignalRange(), "Signal range should be zero for engine");
                    break;
                case "suspension":
                    assertTrue(module.getModuleLoadLimit() >= 0 && module.getModuleLoadLimit() <= 1000, "Load limit should be within 0-1000 for suspension");
                    assertNull(module.getModuleHorsepower(), "Horsepower should be zero for suspension");
                    assertNull(module.getModuleDamage(), "Damage should be zero for suspension");
                    assertNull(module.getModuleRateOfFire(), "Rate of fire should be zero for suspension");
                    assertNull(module.getModulePenetration(), "Penetration should be zero for suspension");
                    assertNull(module.getModuleSignalRange(), "Signal range should be zero for suspension");
                    break;
                case "gun":
                    assertTrue(module.getModuleDamage() >= 0 && module.getModuleDamage() <= 5000, "Damage should be within 0-5000 for gun");
                    assertTrue(module.getModuleRateOfFire() >= 0 && module.getModuleRateOfFire() <= 100, "Rate of fire should be within 0-100 for gun");
                    assertTrue(module.getModulePenetration() >= 0 && module.getModulePenetration() <= 1000, "Penetration should be within 0-1000 for gun");
                    assertNull( module.getModuleHorsepower(), "Horsepower should be zero for gun");
                    assertNull( module.getModuleLoadLimit(), "Load limit should be zero for gun");
                    assertNull( module.getModuleSignalRange(), "Signal range should be zero for gun");
                    break;
                case "radio":
                    assertTrue(module.getModuleSignalRange() >= 0 && module.getModuleSignalRange() <= 5000, "Signal range should be within 0-5000 for radio");
                    assertNull( module.getModuleHorsepower(), "Horsepower should be zero for radio");
                    assertNull( module.getModuleLoadLimit(), "Load limit should be zero for radio");
                    assertNull( module.getModuleDamage(), "Damage should be zero for radio");
                    assertNull( module.getModuleRateOfFire(), "Rate of fire should be zero for radio");
                    assertNull( module.getModulePenetration(), "Penetration should be zero for radio");
                    break;
                default:
                    fail("Unexpected module type: " + type);
            }
        }
    }




}
