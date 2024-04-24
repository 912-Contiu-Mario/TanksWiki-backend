package dev.tankswikibackend.Service;


import dev.tankswikibackend.Entity.Module;
import dev.tankswikibackend.Entity.RepositoryException;
import dev.tankswikibackend.Entity.Tank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.tankswikibackend.Repository.ModuleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ModuleService {


    private ModuleRepository moduleRepository;

    @Autowired
    ModuleService(ModuleRepository moduleRepository){
        this.moduleRepository = moduleRepository;
    }


//    public void validateModule(Module moduleToValidate, String moduleType){
//        if(moduleType == "gun"){
//            assert(moduleToValidate.getModuleDamage()!=null);
//            assert(moduleToValidate.getModulePenetration()!=null);
//            assert(moduleToValidate.get)
//        }
//        if(moduleToValidate)
//    }

    public void addModule(Module moduleToAdd) throws RepositoryException {
        try{


            moduleRepository.save(moduleToAdd);
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
