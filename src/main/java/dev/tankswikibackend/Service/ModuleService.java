package dev.tankswikibackend.Service;


import dev.tankswikibackend.Entity.Module;
import dev.tankswikibackend.Entity.RepositoryException;
import dev.tankswikibackend.Entity.Tank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import dev.tankswikibackend.Repository.ModuleRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ModuleService {


    private ModuleRepository moduleRepository;


    private static final String[] MODULE_TYPES = {
            "engine", "suspension", "gun", "radio"
    };

    private static final String[] ENGINE_TERMS = {"Turbocharged", "V6", "V8", "Inline 4", "Electric", "Hybrid", "Diesel", "Petrol", "High Performance", "Eco-Friendly", "Fuel-efficient", "Powerplant", "Thrust", "Propulsion", "Cylinder", "Exhaust", "Motorized", "Overdrive", "Hyperdrive", "Boosted"};
    private static final String[] SUSPENSION_TERMS = {"Adaptive", "Coilover", "Air Suspension", "MacPherson Strut", "Multi-link", "Leaf Spring", "Torsion Bar", "Independent", "Active Damping", "Passive Damping", "Off-road", "All-terrain", "Rugged", "Trail-rated", "Bounce-reducing", "Shock-absorbing", "Terrain Response", "Stability Control", "Ride Height", "Comfort-oriented"};
    private static final String[] GUN_TERMS = {"Cannon", "Howitzer", "Artillery", "Rifle", "Machine Gun", "Gatling", "Missile Launcher", "Turret", "Cannonade", "Ballista", "Projectile", "Ammunition", "Caliber", "Gunpowder", "Barrel", "Firearm", "Armament", "Firing Mechanism", "Anti-tank", "Heavy Artillery"};
    private static final String[] RADIO_TERMS = {"Communication", "Transceiver", "Receiver", "Transmitter", "Frequency", "Antenna", "Wireless", "Satellite", "Intercom", "Walkie-Talkie", "Broadcast", "Encryption", "Decryption", "Voice Transmission", "Data Transmission", "Long-range", "Short-wave", "Emergency", "Military-grade", "Electronic Warfare"};

    private static final String[] MODULE_SUFFIXES = {"Module", "Unit", "System", "Component", "Assembly", "Apparatus", "Part", "Kit", "Device", "Element", "Mechanism", "Contraption", "Instrument", "Machinery", "Implement", "Fixture", "Tool", "Instrumentation", "Gear", "Widget"};

    private static final String[] moduleTypes = {"engine", "suspension", "gun", "radio"};


    private String generateModuleName(String moduleType) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        String prefix;
        switch (moduleType) {
            case "engine":
                prefix = ENGINE_TERMS[random.nextInt(ENGINE_TERMS.length)];
                break;
            case "suspension":
                prefix = SUSPENSION_TERMS[random.nextInt(SUSPENSION_TERMS.length)];
                break;
            case "gun":
                prefix = GUN_TERMS[random.nextInt(GUN_TERMS.length)];
                break;
            case "radio":
                prefix = RADIO_TERMS[random.nextInt(RADIO_TERMS.length)];
                break;
            default:
                throw new IllegalArgumentException("Invalid module type: " + moduleType);
        }
        String suffix = MODULE_SUFFIXES[random.nextInt(MODULE_SUFFIXES.length)];
        return prefix + " " + suffix;
    }


    @Transactional
    public List<Module> generateFakeModules(List<Tank> tanksToGenerateFor, int numberOfModules) {
        if (tanksToGenerateFor.isEmpty()) {
            throw new IllegalArgumentException("Tank list must not be empty");
        }

        List<Module> generatedModules = IntStream.range(0, numberOfModules)
                .parallel() // Enable parallel processing
                .mapToObj(i -> {
                    Tank randomTank = tanksToGenerateFor.get(ThreadLocalRandom.current().nextInt(tanksToGenerateFor.size()));
                    String moduleType = MODULE_TYPES[ThreadLocalRandom.current().nextInt(MODULE_TYPES.length)];
                    return generateFakeModule(randomTank.getId(), moduleType);
                })
                .collect(Collectors.toList());

        // Save modules in batches
        List<List<Module>> batches = partition(generatedModules, 100); // Split modules into batches of 100
        for (List<Module> batch : batches) {
            moduleRepository.saveAll(batch);
        }

        return generatedModules;
    }

    // Helper method to partition list into batches
    private <T> List<List<T>> partition(List<T> list, int batchSize) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += batchSize) {
            partitions.add(list.subList(i, Math.min(i + batchSize, list.size())));
        }
        return partitions;
    }

    public Module generateFakeModule(Long tankId, String moduleType) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        Module module = new Module();
        module.setModuleType(moduleType);
        module.setTankId(tankId);
        module.setModuleName(generateModuleName(moduleType));
        module.setModuleWeight(random.nextInt(0, 100000)); // All modules have weight
        switch (moduleType) {
            case "engine":
                module.setModuleHorsepower(random.nextInt(0, 10000));
                break;
            case "suspension":
                module.setModuleLoadLimit(random.nextInt(0, 1000));
                break;
            case "gun":
                module.setModuleDamage(random.nextInt(0, 5000));
                module.setModuleRateOfFire(random.nextInt(0, 100));
                module.setModulePenetration(random.nextInt(0, 1000));
                break;
            case "radio":
                module.setModuleSignalRange(random.nextInt(0, 5000));
                break;
        }
        return module;
    }

    @Autowired
    ModuleService(ModuleRepository moduleRepository){
        this.moduleRepository = moduleRepository;
    }

    public Long addModule(Module moduleToAdd) throws RepositoryException {
        try{
            return moduleRepository.save(moduleToAdd).getId();
        }
        catch (Exception exception){
            throw new RepositoryException("Couldn't save module");
        }
    }


    public Page<Module> getModulesPage(Long tankId, String moduleType, Pageable pageable) throws RepositoryException {
        try {
            return moduleRepository.findByTankIdAndModuleType(tankId, moduleType, pageable);
        }
        catch (Exception exception){
            throw new RepositoryException("Couldn't fetch module page");

        }
    }


    public void addModules(List<Module> modulesToAdd) throws RepositoryException {
        try{
            moduleRepository.saveAll(modulesToAdd);
        }
        catch (Exception exception){
            throw new RepositoryException("Couldn't save module");
        }
    }


    public void deleteModules(List<Module> modules) throws RepositoryException {
        try{
        moduleRepository.deleteAll(modules);}
        catch(Exception exception){
            throw new RepositoryException("Couldn't delete modules");
        }
    }


    public void deleteTankModules(Long tankId) throws RepositoryException {
        try{
            List<Module> modulesToDelete =  moduleRepository.findByTankId(tankId);
            moduleRepository.deleteAll(modulesToDelete);
        }
        catch (Exception exception){
            throw new RepositoryException("Couldn't delete tank modules");
        }
    }

    public void updateModule(Long moduleId, Module updatedModule) throws RepositoryException{
        try{
            Optional<Module> moduleToUpdate = moduleRepository.findById(updatedModule.getId());
            if(moduleToUpdate.isEmpty()){
                throw new RepositoryException("Module you want to update doesn't exist");
            }
            moduleRepository.save(updatedModule);
        }
        catch(Exception exception){
            throw new RepositoryException("Couldn't update tank");
        }
    }

    public void deleteModule(Long moduleId)throws RepositoryException{
        try{
            moduleRepository.deleteById(moduleId);
        }
        catch(Exception exception){
            throw new RepositoryException("Couldn't delete module");
        }
    }

    public List<Module> getTankModules(Long tankId) throws RepositoryException {
        try{

        return moduleRepository.findByTankId(tankId);
        }
        catch(Exception exception){
            throw new RepositoryException("Couldn't fetch modules");
        }
    }

    public List<Module> getTankModulesByType(Long tankId, String moduleType) throws RepositoryException {
        try{
            return moduleRepository.findByTankIdAndModuleType(tankId, moduleType);
        }
        catch (Exception exception){
            throw new RepositoryException("Couldn't fetch modules by tank and type");
        }
    }

}
