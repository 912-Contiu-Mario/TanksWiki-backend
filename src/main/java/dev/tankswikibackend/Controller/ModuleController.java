package dev.tankswikibackend.Controller;


import dev.tankswikibackend.Entity.Module;
import dev.tankswikibackend.Entity.RepositoryException;
import dev.tankswikibackend.Service.ModuleService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController


@RequestMapping("api/modules")
public class ModuleController {
    private ModuleService moduleService;
    @Autowired
    public ModuleController(ModuleService moduleService){
        this.moduleService= moduleService;
    }



    @RolesAllowed({"ROLE_ADMIN", "ROLE_MANAGER"})
    @PostMapping("")
    ResponseEntity<?> newModule(@RequestBody Module newModule){
        try{
            return ResponseEntity.ok().body(moduleService.addModule(newModule));
        }
        catch(RepositoryException exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }


    @RolesAllowed({"ROLE_ADMIN", "ROLE_MANAGER"})
    @PostMapping("/bulk")
    public ResponseEntity<String> newModules(@RequestBody List<Module> newModules) {
        try {
            moduleService.addModules(newModules);
            return ResponseEntity.ok().body("Modules added");
        } catch (RepositoryException exception) {
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }



    @RolesAllowed({"ROLE_ADMIN", "ROLE_MANAGER"})
    @PutMapping("/{moduleId}")
    ResponseEntity<String> updateModule(@PathVariable Long moduleId, @RequestBody Module updatedModule){
        try{
            moduleService.updateModule(moduleId, updatedModule);
            return ResponseEntity.ok().body("Module updated");
        }
        catch (RepositoryException exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());

        }
    }



    @RolesAllowed({"ROLE_ADMIN", "ROLE_MANAGER"})
    @DeleteMapping("/{moduleId}")
    ResponseEntity<String> deleteModule(@PathVariable Long moduleId){
        try{
            moduleService.deleteModule(moduleId);
            return ResponseEntity.ok().body("Module deleted");
        }
        catch(RepositoryException exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

    @GetMapping("/{tankId}")
    ResponseEntity<?> getTankModules(@PathVariable Long tankId){

        try{
            return ResponseEntity.ok().body(moduleService.getTankModules(tankId));
        }
        catch(RepositoryException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping(value = "/{tankId}", params = "moduleType")
    ResponseEntity<?> getTankModulesByModuleType(@PathVariable Long tankId, @RequestParam(value = "moduleType")String moduleType ){

        try{
            return ResponseEntity.ok().body(moduleService.getTankModulesByType(tankId, moduleType));
        }
        catch(RepositoryException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }
    @GetMapping(value = "modulesPage/{tankId}", params = "moduleType")
    ResponseEntity<?> getTankModulesByModuleTypePaginated(@PathVariable Long tankId, @RequestParam(value = "moduleType")String moduleType, @PageableDefault(size = 50) Pageable pageable){
        try{
            return ResponseEntity.ok().body(moduleService.getModulesPage(tankId, moduleType, pageable).getContent());
        }
        catch(RepositoryException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

}
