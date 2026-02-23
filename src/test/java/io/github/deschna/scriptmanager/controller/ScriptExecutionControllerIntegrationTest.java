package io.github.deschna.scriptmanager.controller;

import com.jayway.jsonpath.JsonPath;
import io.github.deschna.scriptmanager.repository.ScriptExecutionRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ScriptExecutionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScriptExecutionRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void createExecution_returns201AndLocation() throws Exception {
        String json = """
            {
              "sourceCode": "print('hello')"
            }
            """;

        var result = mockMvc.perform(post("/executions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String location = result.getResponse().getHeader("Location");

        String id = JsonPath.read(responseBody, "$.id");

        assertThat(location)
                .isNotNull()
                .endsWith("/executions/" + id);
    }

    @Test
    void createExecution_withBlankSourceCode_returns400() throws Exception {
        String json = """
            {
              "sourceCode": ""
            }
            """;

        mockMvc.perform(post("/executions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createExecution_withoutSourceCode_returns400() throws Exception {
        String json = """
            {
            }
            """;

        mockMvc.perform(post("/executions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getExistingExecution_returns200() throws Exception {
        String json = """
            {
              "sourceCode": "print('hello')"
            }
            """;

        var postResult = mockMvc.perform(post("/executions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andReturn();

        String location = postResult.getResponse().getHeader("Location");
        assertThat(location).isNotNull();

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.sourceCode").value("print('hello')"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void getUnknownExecution_returns404() throws Exception {
        mockMvc.perform(get("/executions/" + UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getInvalidUuid_returns400() throws Exception {
        mockMvc.perform(get("/executions/invalid-uuid"))
                .andExpect(status().isBadRequest());
    }
}
