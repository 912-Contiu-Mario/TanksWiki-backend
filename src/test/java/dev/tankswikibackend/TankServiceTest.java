package dev.tankswikibackend;

import dev.tankswikibackend.Entity.InvalidTankException;
import dev.tankswikibackend.Entity.RepositoryException;
import dev.tankswikibackend.Entity.Tank;
import dev.tankswikibackend.Repository.TankRepository;
import dev.tankswikibackend.Service.ModuleService;
import dev.tankswikibackend.Service.TankService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TankServiceTest {
    @Mock
    TankRepository tankRepository;

    @Mock
    ModuleService moduleService;

    @InjectMocks
    private TankService tankService;



    @Test
    public void testAddTank() throws InvalidTankException, RepositoryException {

        Tank newTank = new Tank(1L, 1L, "IS-7", "Soviet Union", "Heavy Tank", 1942, 2000, 32);

        when(tankRepository.save(any(Tank.class))).thenReturn(newTank);
        tankService.addTank(newTank);
    }

    @Test
    public void testRemoveTank() throws RepositoryException {
        Tank newTank = new Tank(1L, 1L, "IS-7", "Soviet Union", "Heavy Tank", 1942, 2000, 32);
        tankService.deleteTank(newTank.getId());
        verify(tankRepository).deleteById(newTank.getId());
    }


    @Test
    public void testGetTankById_Found() throws RepositoryException {
        Long tankId = 1L;
        Tank expectedTank = new Tank(); // Assume Tank is a valid entity class
        when(tankRepository.findById(tankId)).thenReturn(Optional.of(expectedTank));

        Tank resultTank = tankService.getTankById(tankId);

        assertThat(resultTank).isNotNull();
        assertThat(resultTank).isSameAs(expectedTank);
    }

    @Test
    public void testGetTankById_Not_Found() throws RepositoryException {
        Long tankId = 1L;
        // Assume Tank is a valid entity class
        when(tankRepository.findById(tankId)).thenReturn(Optional.empty());
        assertThatThrownBy(()->tankService.getTankById(tankId)).isInstanceOf(RepositoryException.class).hasMessage("Tank with given id not found");
    }

    @Test
    public void deleteTank_Found() throws RepositoryException {
        Long tankId = 1L;

        tankService.deleteTank(tankId);
        verify(tankRepository).deleteById(tankId);
    }

//    @Test
//    public void testGetTanksPage_Found() throws RepositoryException{
//        Tank newTank = new Tank(1L, 1L, "IS-7", "Soviet Union", "Heavy Tank", 1942, 2000, 32);
//        List<Tank> tanksList = List.of(newTank, newTank,newTank,newTank,newTank,newTank,newTank,newTank,newTank,newTank);
//
//        when(tankRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(tanksList));
//
//    }

    @Test void deleteTank_Not_Found(){
        Long tankId = 1L;


        doThrow(new RuntimeException()).when(tankRepository).deleteById(tankId);
        assertThatThrownBy(()->tankService.deleteTank(tankId)).isInstanceOf(RepositoryException.class).hasMessage("Couldn't delete tank");

    }

    @Test void updateTank_Found() throws InvalidTankException, RepositoryException {
        //Optional<Tank> tankToUpdate = tankRepository.findById(id);
        Tank newTank = new Tank(1L, 1L, "IS-7", "Soviet Union", "Heavy Tank", 1942, 2000, 32);
        when(tankRepository.findById(newTank.getId())).thenReturn(Optional.of(newTank));

        when(tankRepository.save(newTank)).thenReturn(newTank);

        tankService.updateTank(newTank.getId(), newTank);
    }


    @Test void getAll_Working() throws RepositoryException {

        Tank newTank = new Tank(1L, 1L, "IS-7", "Soviet Union", "Heavy Tank", 1942, 2000, 32);
        List<Tank> list = List.of(newTank, newTank);

        when(tankRepository.findAll()).thenReturn(list);

        assertThat(tankService.getAllTanks().get(0).getId()).isSameAs(list.get(0).getId());
        assertThat(tankService.getAllTanks().get(1).getId()).isSameAs(list.get(1).getId());
    }

    @Test void getAll_Not_Working() throws RepositoryException {

        Tank newTank = new Tank(1L, 1L, "IS-7", "Soviet Union", "Heavy Tank", 1942, 2000, 32);
        List<Tank> list = List.of(newTank, newTank);


        doThrow(new RuntimeException()).when(tankRepository).findAll();
        assertThatThrownBy(()->tankService.getAllTanks()).isInstanceOf(RepositoryException.class);
    }

    @Test void validateTank_Valid(){
        Tank newTank = new Tank(1L, 1L, "IS-7", "Soviet Union", "Heavy Tank", 1942, 2000, 32);

        assertThatCode(()->tankService.validateTank(newTank)).doesNotThrowAnyException();
    }


    @Test void validateTank_Not_Valid(){
        Tank newTank = new Tank(1L, 1L, null, "Soviet Union", "Heavy Tank", 1942, 2000, 32);


        assertThatThrownBy(()->tankService.validateTank(newTank)).isInstanceOf(InvalidTankException.class);

    }
}
