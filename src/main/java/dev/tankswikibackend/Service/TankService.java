package dev.tankswikibackend.Service;


import com.github.javafaker.Faker;
import dev.tankswikibackend.Entity.InvalidTankException;
import dev.tankswikibackend.Entity.RepositoryException;
import dev.tankswikibackend.Entity.Tank;
import dev.tankswikibackend.Repository.TankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TankService {
    private ModuleService moduleService;
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
    public TankService(TankRepository tankRepository, ModuleService moduleService){
        this.tankRepository = tankRepository;
        this.moduleService = moduleService;
        this.generateFakeTanks(500);
        moduleService.generateFakeModules(tankRepository.findAll(), 100  );
    }



    public Long addTank(Tank tankToAdd) throws InvalidTankException, RepositoryException{
        validateTank(tankToAdd);

        try{
            Tank addedTank = this.tankRepository.save(tankToAdd);
            return addedTank.getId();
        }
        catch (Exception exception){
            throw new RepositoryException("Couldn't save tank");
        }
    }

    public void addTanks(List<Tank> tanksToAdd) throws InvalidTankException, RepositoryException{
//        validateTank(tankToAdd);


        try{

            this.tankRepository.saveAll(tanksToAdd);}
        catch (Exception exception){
            throw new RepositoryException("Couldn't save tanks");
        }
    }

    public Tank getTankByName(String name) throws RepositoryException {

        try{
            return this.tankRepository.findByTankName(name).get(0);
        }
        catch (Exception exception){
            throw new RepositoryException("Couldn't find tank");
        }



    }

    public Page<Tank> getTanksPage(Pageable pageable) throws RepositoryException {
        try {
            return tankRepository.findAll(pageable);

        }
        catch (Exception exception){
            throw new RepositoryException("Couldn't fetch tank page");

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
            moduleService.deleteTankModules(id);
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



    private String generateTankName() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String militaryTerm = MILITARY_TERMS[random.nextInt(MILITARY_TERMS.length)];
        String technologyTerm = TECHNOLOGY_TERMS[random.nextInt(TECHNOLOGY_TERMS.length)];
        String suffix = TANK_SUFFIXES[random.nextInt(TANK_SUFFIXES.length)];
        return militaryTerm + " " + technologyTerm + " " + suffix;
    }

    @Transactional
    public List<Tank> generateFakeTanks(int numberOfTanks) {
        List<Tank> tanks = IntStream.range(0, numberOfTanks)
                .parallel() // Enable parallel processing
                .mapToObj(i -> {
                    Tank tank = new Tank();
                    tank.setTankName(generateTankName());
                    tank.setTankCountry(TANK_COUNTRIES[ThreadLocalRandom.current().nextInt(TANK_COUNTRIES.length)]);
                    tank.setTankType(TANK_TYPES[ThreadLocalRandom.current().nextInt(TANK_TYPES.length)]);
                    tank.setTankYear(ThreadLocalRandom.current().nextInt(1917, 2025)); // Adjust year range as needed
                    tank.setTankFirepower(ThreadLocalRandom.current().nextInt(20, 5000)); // Adjust firepower range as needed
                    tank.setTankSpeed(ThreadLocalRandom.current().nextInt(10, 100)); // Adjust speed range as needed
                    return tank;
                })
                .collect(Collectors.toList());

        // Save tanks in batches
        List<List<Tank>> batches = partition(tanks, 100); // Split tanks into batches of 100
        for (List<Tank> batch : batches) {
            tankRepository.saveAll(batch);
        }

        return tanks;
    }

    // Helper method to partition list into batches
    private <T> List<List<T>> partition(List<T> list, int batchSize) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += batchSize) {
            partitions.add(list.subList(i, Math.min(i + batchSize, list.size())));
        }
        return partitions;
    }

}
