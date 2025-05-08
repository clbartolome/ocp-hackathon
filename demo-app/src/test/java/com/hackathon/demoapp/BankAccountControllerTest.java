package com.hackathon.demoapp;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.demoapp.model.BankAccount;
import com.hackathon.demoapp.repository.BankAccountRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BankAccountRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        repository.deleteAll();
        repository.saveAll(List.of(
            new BankAccount("Test1", BigDecimal.valueOf(123)),
            new BankAccount("Test2", BigDecimal.valueOf(456))
        ));
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get("/accounts"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testCreateAndGetById() throws Exception {
        BankAccount acc = new BankAccount("NewUser", BigDecimal.valueOf(789));
        String json = objectMapper.writeValueAsString(acc);

        String result = mockMvc.perform(post("/accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
               .andExpect(status().isOk())
               .andReturn().getResponse().getContentAsString();

        BankAccount created = objectMapper.readValue(result, BankAccount.class);
        assertThat(created.getId()).isNotNull();
        assertThat(created.getOwner()).isEqualTo("NewUser");
    }

    @Test
    void testUpdate() throws Exception {
        BankAccount existing = repository.findAll().get(0);
        existing.setOwner("Updated");
        existing.setBalance(BigDecimal.valueOf(999));
        String json = objectMapper.writeValueAsString(existing);

        mockMvc.perform(put("/accounts/" + existing.getId())
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(json))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.owner").value("Updated"));
    }

    @Test
    void testDelete() throws Exception {
        BankAccount existing = repository.findAll().get(0);
        mockMvc.perform(delete("/accounts/" + existing.getId()))
               .andExpect(status().isNoContent());
        assertThat(repository.existsById(existing.getId())).isFalse();
    }
}