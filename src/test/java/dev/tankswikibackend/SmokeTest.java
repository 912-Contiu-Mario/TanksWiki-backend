package dev.tankswikibackend;

import static org.assertj.core.api.Assertions.assertThat;

import dev.tankswikibackend.Controller.TankController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SmokeTest {

    @Autowired
    private TankController controller;

    @Test
    void contextLoads() throws Exception{
        assertThat(controller).isNotNull();
    }


}
