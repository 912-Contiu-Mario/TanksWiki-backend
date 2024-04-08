package dev.tankswikibackend.Service;


import com.github.javafaker.Faker;
import dev.tankswikibackend.Entity.InvalidTankException;
import dev.tankswikibackend.Entity.RepositoryException;
import dev.tankswikibackend.Entity.Tank;
import dev.tankswikibackend.Repository.TankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TankService {

    private static final String[] MILITARY_TERMS = {
            "Steel", "Iron", "Armored", "Thunder", "Warrior", "Shield", "Battle", "Victory", "Elite", "Spartan",
            "Delta", "Bravo", "Ranger", "Commando", "Valor", "Guardian", "Assault", "Recon", "Dominion", "Sentinel"
    };    private static final String[] TECHNOLOGY_TERMS = {
            "Alpha", "Omega", "Titan", "Phoenix", "Cyber", "Nova", "Vanguard", "Inferno", "Apex", "Nemesis",
            "Quantum", "Neon", "Galactic", "Hyper", "Vortex", "Infinity", "Spectre", "Zenith", "Eclipse", "Pulse"
    };

    private static final String[] TANK_SUFFIXES = {"Mark", "II", "III", "IV", "V", "X", "Alpha", "Beta", "Delta", "Gamma"};

    private static final String[] TANK_TYPES = {"Heavy Tank", "Light Tank", "Medium Tank", "Main Battle Tank"};

    private static final String[] TANK_COUNTRIES = {"Russia", "Germany", "UK", "France", "Japan", "USA"};
    TankRepository tankRepository;

    @Autowired
    public TankService(TankRepository tankRepository){
        this.tankRepository = tankRepository;
        this.generateFakeTanks(50);
    }

    public void addTank(Tank tankToAdd) throws InvalidTankException, RepositoryException{
        validateTank(tankToAdd);
        try{
            this.tankRepository.save(tankToAdd);}
        catch (Exception exception){
            throw new RepositoryException("Couldn't save tank");
        }
    }

    public void validateTank(Tank tankToValidate) throws InvalidTankException {

        if(tankToValidate.getTankName() == null || tankToValidate.getTankType() == null
                || tankToValidate.getTankCountry() == null  || tankToValidate.getTankSpeed() == null
                || tankToValidate.getTankFirepower()==null || tankToValidate.getTankYear() == null)
            throw new InvalidTankException("No fields can be null");
    }

    public Tank getTankById(Long id) throws RepositoryException {
        try{
            return this.tankRepository.findById(id).get();}
        catch(Exception exception){
            throw(new RepositoryException("Tank with given id not found"));
        }
    }

    public List<Tank> getAllTanks() throws RepositoryException {
        try{
            return this.tankRepository.findAll();
        }
        catch(Exception exception){
            throw(new RepositoryException("Couldn't fetch tanks"));
        }
    }

    public void updateTank(Long id, Tank updatedTank) throws RepositoryException, InvalidTankException {

        validateTank(updatedTank);
        try{
            Optional<Tank> tankToUpdate = tankRepository.findById(id);
            if(tankToUpdate.isEmpty()){
                throw new RepositoryException("Tank you want to update does not exist");
            }
            tankRepository.save(updatedTank);
        }
        catch (Exception exception){
            throw new RepositoryException("Couldn't update tank");
        }
    }

    public void deleteTank(Long id)throws RepositoryException{
        try{
            tankRepository.deleteById(id);
        }
        catch(Exception exception){
            throw  new RepositoryException("Couldn't delete tank");
        }
    }


    public List<Tank> getAllTanksSorted(String sortCriteria) throws RepositoryException {
        try{
            List<Tank> unsorted = this.tankRepository.findAll();


            unsorted.sort(Comparator.comparing(Tank::getTankName));


            return unsorted;
        }
        catch(Exception exception){
            throw(new RepositoryException("Couldn't fetch tanks"));
        }
    }



    private String generateTankName(){
        Faker faker = new Faker();
        String militaryTerm = MILITARY_TERMS[faker.random().nextInt(MILITARY_TERMS.length)];
        String technologyTerm = TECHNOLOGY_TERMS[faker.random().nextInt(TECHNOLOGY_TERMS.length)];
        String suffix = TANK_SUFFIXES[faker.random().nextInt(TANK_SUFFIXES.length)];
        return militaryTerm + " " + technologyTerm + " " + suffix;
    }


//    @Scheduled(fixedDelay = 5000) // Generate new tank every 5 seconds
//    @Async
    public void generateNewTank(){
        generateFakeTanks(1);
    }


    public void generateFakeTanks(int numberOfTanks) {
        Faker faker = new Faker();

        List<Tank> tanks = new ArrayList<>();
        for (int i = 0; i < numberOfTanks; i++) {
            Tank tank = new Tank();
            tank.setTankName(generateTankName());
            tank.setTankCountry(TANK_COUNTRIES[faker.random().nextInt(TANK_COUNTRIES.length)]);
            tank.setTankType(TANK_TYPES[faker.random().nextInt(TANK_TYPES.length)]); // Randomly select tank type
            tank.setTankYear(faker.number().numberBetween(1917, 2025)); // Adjust year range as needed
            tank.setTankFirepower(faker.number().numberBetween(20, 5000)); // Adjust firepower range as needed
            tank.setTankSpeed(faker.number().numberBetween(10, 100)); // Adjust speed range as needed

            tanks.add(tank);
        }

        tankRepository.saveAll(tanks);
    }

}
