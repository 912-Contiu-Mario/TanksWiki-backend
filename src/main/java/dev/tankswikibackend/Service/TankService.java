package dev.tankswikibackend.Service;


import dev.tankswikibackend.Entity.InvalidTankException;
import dev.tankswikibackend.Entity.RepositoryException;
import dev.tankswikibackend.Entity.Tank;
import dev.tankswikibackend.Repository.TankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TankService {
    TankRepository tankRepository;

    @Autowired
    public TankService(TankRepository tankRepository){
        this.tankRepository = tankRepository;
    }

    public void addTank(Tank tankToAdd) throws InvalidTankException, RepositoryException{
        validateTank(tankToAdd);
        System.out.println(tankToAdd.getTankYear());
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

}
