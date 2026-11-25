package com.test.altia.altia_test.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.test.altia.altia_test.domain.model.Price;
import com.test.altia.altia_test.domain.port.out.PriceRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class GetApplicablePriceServiceTest {
    @Test
    void returnsThePriceProvidedByTheRepository() {
        Price expected = new Price(1L, LocalDateTime.parse("2020-06-14T15:00:00"),
                LocalDateTime.parse("2020-06-14T18:30:00"), 2L, 35455L, 1,
                new BigDecimal("25.45"), "EUR");
        PriceRepository repository = (productId, brandId, date) -> Optional.of(expected);

        Price result = new GetApplicablePriceService(repository)
                .execute(35455L, 1L, LocalDateTime.parse("2020-06-14T16:00:00"));

        assertEquals(expected, result);
    }
}
