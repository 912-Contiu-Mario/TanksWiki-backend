package dev.tankswikibackend.Controller;


import dev.tankswikibackend.Entity.Module;
import dev.tankswikibackend.Entity.RepositoryException;
import dev.tankswikibackend.Service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController


@RequestMapping("api/modules")
public class ModuleController {
    private ModuleService moduleService;


    @Autowired
    public ModuleController(ModuleService moduleService){
        this.moduleService= moduleService;
    }


    @PostMapping("")
    ResponseEntity<String> newModule(@RequestBody Module newModule){
        try{
            moduleService.addModule(newModule);
            return ResponseEntity.ok().body("Module added");
        }
        catch(RepositoryException exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

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
    ResponseEntity<?> getTankModules(@PathVariable Long tankId, @RequestParam(value = "moduleType")String moduleType ){

        try{
            return ResponseEntity.ok().body(moduleService.getTankModulesByType(tankId, moduleType));
        }
        catch(RepositoryException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

}
