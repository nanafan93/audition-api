package com.audition;

import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@Getter
class AuditionApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Value("${spring.application.api.baseUrl}")
    private String baseUrl;

    @Test
    void testSimpleHealth() {
        Assertions.assertDoesNotThrow(() ->
            mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "/actuator/health"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()));
    }

}
