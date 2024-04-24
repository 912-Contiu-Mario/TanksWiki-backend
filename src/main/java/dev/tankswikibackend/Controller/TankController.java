package dev.tankswikibackend.Controller;
import dev.tankswikibackend.Entity.InvalidTankException;
import dev.tankswikibackend.Entity.RepositoryException;
import dev.tankswikibackend.Entity.Tank;
import dev.tankswikibackend.Service.TankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("api/tanks")
public class TankController {
    private TankService tankService;

    @Autowired
    public TankController(TankService tankService){
        this.tankService = tankService;
    }


    @GetMapping("/health")
    void serverHealth(){

    }

    @PostMapping("")
    ResponseEntity<String> newTank(@RequestBody Tank newTank){
        try{
            tankService.addTank(newTank);
            return ResponseEntity.ok().body("Tank added");
        }
        catch(RepositoryException exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
        catch(InvalidTankException exception){
            return ResponseEntity.badRequest().body(exception.getMessage());
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
    @DeleteMapping("{id}")
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
