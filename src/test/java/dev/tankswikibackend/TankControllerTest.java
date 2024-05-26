//package dev.tankswikibackend;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import dev.tankswikibackend.Controller.TankController;
//import dev.tankswikibackend.Entity.Tank;
//import dev.tankswikibackend.Service.TankService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.util.List;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(TankController.class)
//@AutoConfigureMockMvc(addFilters = false)
//
//public class TankControllerTest {
//
//
//    @MockBean
//    TankService mockTankService;
//
//   @Autowired
//   private MockMvc mockMvc;
//
//    @Test
//    public void testTanksEndpoint_GET_Works() throws Exception {
//        Tank newTank = new Tank(1L, "IS-7", "Soviet Union", "Heavy Tank", 1942, 2000, 32);
//
//        when(mockTankService.getAllTanks()).thenReturn(List.of(newTank, newTank));
//
//        when(mockTankService.getTankById(any(Long.class))).thenReturn(newTank);
//
//        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/tanks"))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].tankName").value("IS-7"));
//
//        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/tanks/5"))
//                .andDo(print())
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.tankName").value("IS-7"));
//    }
//
//    @Test
//        public void testTanksEndpoint_DELETE_Works() throws Exception {
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String requestBody = objectMapper.writeValueAsString(5L);
//        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/tanks/5")
//                        .contentType(MediaType.APPLICATION_JSON)) // Set content type to JSON or any appropriate type
//                        .andExpect(MockMvcResultMatchers.status().isOk())
//                        .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
//                        .andExpect(content().string("Tank deleted"));
//       verify( mockTankService).deleteTank(any(Long.class));
//    }
//
//    @Test
//    public void testTanksEndpoint_POST_Works() throws Exception {
//        Tank newTank = new Tank(1L, "IS-7", "Soviet Union", "Heavy Tank", 1942, 2000, 32);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String requestBody = objectMapper.writeValueAsString(newTank);
//
//        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/tanks")
//                        .contentType(MediaType.APPLICATION_JSON) // Set content type to JSON
//                        .content(requestBody)) // Set request body
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE+";charset=UTF-8"))
//                .andExpect(content().string("Tank added"));
//    }
//
//    @Test
//    public void testTanksEndpoint_PUT_Works() throws Exception {
//
//        Tank updatedTank = new Tank(5L, "Updated Tank", "Updated Country", "Updated Type", 2022, 1000, 50);
//
//        // Convert the updated tank object to JSON
//        ObjectMapper objectMapper = new ObjectMapper();
//        String requestBody = objectMapper.writeValueAsString(updatedTank);
//        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/tanks/5")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"))
//                .andExpect(content().string("Tank updated"));
//        verify( mockTankService).updateTank(any(Long.class), any(Tank.class));
//
//    }
//}
//
