package dev.tankswikibackend.Controller;


import dev.tankswikibackend.Entity.RepositoryException;
import dev.tankswikibackend.Service.TankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@EnableScheduling
@CrossOrigin(origins = "http://localhost:3000")
public class SocketTankController {

    TankService tankService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    SocketTankController(TankService tankService){
        this.tankService = tankService;
    }

    @MessageMapping("/getTanks")
    @Scheduled(fixedDelay = 5000)
    public void sendTankUpdate(){
        try {
            messagingTemplate.convertAndSend("/topic/tanks", tankService.getAllTanksSorted(""));
        }
        catch(RepositoryException exception){

        }
        catch (Exception exception)
        {

        }
    }
}
