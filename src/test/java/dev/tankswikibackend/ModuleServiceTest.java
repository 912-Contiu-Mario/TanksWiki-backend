package dev.tankswikibackend;


import dev.tankswikibackend.Entity.Module;
import dev.tankswikibackend.Entity.RepositoryException;
import dev.tankswikibackend.Entity.Tank;
import dev.tankswikibackend.Repository.ModuleRepository;
import dev.tankswikibackend.Service.ModuleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
public class ModuleServiceTest {
    @Mock
    ModuleRepository moduleRepository;

    @InjectMocks
    ModuleService moduleService;


    @Test
    public void testAddModule() throws RepositoryException {
        Module newModule = new Module(60L,"Gun variation 1", "gun", 55, 30, 20, 155 );

        when(moduleRepository.save(any(Module.class))).thenReturn(newModule);
        moduleService.addModule(newModule);

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




}
