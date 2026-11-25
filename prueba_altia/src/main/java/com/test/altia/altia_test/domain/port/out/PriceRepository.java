package com.test.altia.altia_test.domain.port.out;

import com.test.altia.altia_test.domain.model.Price;
import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceRepository {
    Optional<Price> findApplicablePrice(Long productId, Long brandId, LocalDateTime applicationDate);
}
