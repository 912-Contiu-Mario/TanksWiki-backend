package dev.tankswikibackend;


import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tankswikibackend.Controller.ModuleController;
import dev.tankswikibackend.Controller.TankController;
import dev.tankswikibackend.Entity.Module;
import dev.tankswikibackend.Entity.Tank;
import dev.tankswikibackend.Service.ModuleService;
import dev.tankswikibackend.Service.TankService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.print.DocFlavor;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ModuleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ModuleControllerTest {
    @MockBean
    ModuleService mockModuleService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testModuleEndpoint_POST_Works() throws Exception {
        Module newModule = new Module(60L,"Gun variation 1", "gun", 55, 30, 20, 155 );
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(newModule);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/modules")
                        .contentType(MediaType.APPLICATION_JSON) // Set content type to JSON
                        .content(requestBody)) // Set request body
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE+";charset=UTF-8"))
                .andExpect(content().string("Module added"));
    }

    @Test
    public void testModuleEndpoint_PUT_Works() throws Exception {

        Module newModule = new Module(60L,"Gun variation 1", "gun", 55, 30, 20, 155 );

        // Convert the updated tank object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(newModule);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/modules/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                    .andExpect(content().string("Module updated"));
        verify( mockModuleService).updateModule(any(Long.class), any(Module.class));

    }


    @Test
    public void testModuleEndpoint_DELETE_Works() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(5L);
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/modules/5")
                        .contentType(MediaType.APPLICATION_JSON)) // Set content type to JSON or any appropriate type
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
                .andExpect(content().string("Module deleted"));
        verify( mockModuleService).deleteModule(any(Long.class));
    }


    @Test
    public void testTanksEndpoint_GET_Works() throws Exception {
        Tank newTank = new Tank(1L, "IS-7", "Soviet Union", "Heavy Tank", 1942, 2000, 32);


        Module module1 = new Module(60L,"Gun variation 1", "gun", 55, 30, 20, 155 );
        Module module2 = new Module(60L,"Gun variation 2", "gun", 60, 40, 30, 160 );
        List<Module> list = List.of(module1, module2);
        when(mockModuleService.getTankModules(any(Long.class))).thenReturn(list);

        when(mockModuleService.getTankModulesByType(any(Long.class), any(String.class))).thenReturn(list);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/modules/5"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].moduleName").value("Gun variation 1"));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/modules/5?moduleType=gun"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].moduleName").value("Gun variation 1"));
    }




}
