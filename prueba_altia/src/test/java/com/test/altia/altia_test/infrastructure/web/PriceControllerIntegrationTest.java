package com.test.altia.altia_test.infrastructure.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.test.altia.altia_test.infrastructure.persistence.JpaPriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PriceControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CacheManager cacheManager;

    @MockitoSpyBean
    private JpaPriceRepository priceRepository;

    @BeforeEach
    void clearCacheAndRepositoryInteractions() {
        cacheManager.getCache("applicable-prices").clear();
        clearInvocations(priceRepository);
    }

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

    @Test
    void returnsNotFoundWhenNoPriceApplies() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "99999")
                        .param("brandId", "1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void returnsBadRequestWhenAParameterIsInvalid() throws Exception {
        mockMvc.perform(get("/api/v1/prices")
                        .param("applicationDate", "2020-06-14T10:00:00")
                        .param("productId", "0")
                        .param("brandId", "1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void usesTheCacheForIdenticalRequests() throws Exception {
        for (int i = 0; i < 2; i++) {
            mockMvc.perform(get("/api/v1/prices")
                            .param("applicationDate", "2020-06-14T16:00:00")
                            .param("productId", "35455")
                            .param("brandId", "1"))
                    .andExpect(status().isOk());
        }

        verify(priceRepository, times(1))
                .findApplicablePrice(35455L, 1L, java.time.LocalDateTime.parse("2020-06-14T16:00:00"));
    }
}
