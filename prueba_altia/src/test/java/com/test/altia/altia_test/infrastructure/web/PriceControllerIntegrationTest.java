package com.test.altia.altia_test.infrastructure.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PriceControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest(name = "request at {0} returns price list {1}")
    @CsvSource({
            "2020-06-14T10:00:00, 1, 35.50",
            "2020-06-14T16:00:00, 2, 25.45",
            "2020-06-14T21:00:00, 1, 35.50",
            "2020-06-15T10:00:00, 3, 30.50",
            "2020-06-16T21:00:00, 4, 38.95"
    })
    void returnsExpectedApplicablePrice(String date, long expectedPriceList, String expectedPrice) throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", date)
                        .param("productId", "35455")
                        .param("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.priceList").value(expectedPriceList))
                .andExpect(jsonPath("$.price").value(Double.parseDouble(expectedPrice)))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }
}
