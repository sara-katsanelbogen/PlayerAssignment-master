import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PlayerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired ObjectMapper objectMapper;

    @Test
    void testGetPlayers_NoContent() throws Exception {
        mockMvc.perform(get("/api/players"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetPlayers_Success() throws Exception {
        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPlayers_InternalServerError() throws Exception {
        mockMvc.perform(get("/api/players"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getAllPlayersTest() throws Exception {
        // Perform GET request to /api/players endpoint
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/players")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Check HTTP status code
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());

        // Deserialize response body into a list of players
        List<Player> players = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<Player>>() {});

        // Assert the number of players returned
        assertEquals(19370, players.size());
    }
}
