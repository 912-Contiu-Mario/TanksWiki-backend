package dev.tankswikibackend.Controller;
import dev.tankswikibackend.Entity.InvalidTankException;
import dev.tankswikibackend.Entity.RepositoryException;
import dev.tankswikibackend.Entity.Tank;
import dev.tankswikibackend.Service.TankService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("api/tanks")
public class TankController {
    private TankService tankService;

    @Autowired
    public TankController(TankService tankService){
        this.tankService = tankService;
    }




    @PostMapping("")
    @RolesAllowed("ROLE_ADMIN")
    ResponseEntity<?> newTank(@RequestBody Tank newTank){
        try{

            return ResponseEntity.ok().body(tankService.addTank(newTank));
        }
        catch(RepositoryException exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
        catch(InvalidTankException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }



    @GetMapping("/tanksPage")
    ResponseEntity<?> getTanks(@PageableDefault(size = 50)Pageable pageable){
        try{
            Page<Tank> tanksPage = tankService.getTanksPage(pageable);
            return ResponseEntity.ok(tanksPage.getContent());
        }
        catch (RepositoryException exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }


    @RolesAllowed({"ROLE_ADMIN", "ROLE_MANAGER"})
    @PostMapping("/bulk")
    public ResponseEntity<String> newTanks(@RequestBody List<Tank> newTanks) {
        try {
            tankService.addTanks(newTanks);
            return ResponseEntity.ok().body("Tanks added");
        } catch (RepositoryException exception) {
            return ResponseEntity.internalServerError().body(exception.getMessage());
        } catch (InvalidTankException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }


    @GetMapping("/findIdByName")
    ResponseEntity<?> getIdByName(@RequestParam String tankName){
        try{
            return ResponseEntity.ok().body(tankService.getTankByName(tankName).getId());
        }
        catch(RepositoryException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }


    @GetMapping("/{id}")
    ResponseEntity<?> getTankById(@PathVariable Long id){
        try{
            return ResponseEntity.ok().body(tankService.getTankById(id));
        }
        catch(RepositoryException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @GetMapping("")
    ResponseEntity<?> allTanks(){
        try{
            return ResponseEntity.ok().body(tankService.getAllTanksSorted(""));
        }
        catch (Exception exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

    @GetMapping("/userTanks")
    ResponseEntity<?> allUserTanks(@RequestParam Long userId){
        try{
            return ResponseEntity.ok(tankService.getAllTanksByUserId(userId));
        }
        catch (RepositoryException exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }


    @RolesAllowed({"ROLE_ADMIN", "ROLE_MANAGER"})
    @PutMapping("/{id}")
    ResponseEntity<String> updateTank(@PathVariable Long id, @RequestBody Tank updatedTank){
        try{
            tankService.updateTank(id, updatedTank);
            return ResponseEntity.ok().body("Tank updated");
        }
        catch(RepositoryException exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
        catch(InvalidTankException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }


    @RolesAllowed({"ROLE_ADMIN", "ROLE_MANAGER"})
    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteTank(@PathVariable Long id){
        try{
            tankService.deleteTank(id);
            return ResponseEntity.ok().body("Tank deleted");
        }
        catch(RepositoryException exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }
}
