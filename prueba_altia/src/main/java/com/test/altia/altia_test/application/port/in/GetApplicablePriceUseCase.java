package com.test.altia.altia_test.application.port.in;

import com.test.altia.altia_test.domain.model.Price;
import java.time.LocalDateTime;

public interface GetApplicablePriceUseCase {
    Price execute(Long productId, Long brandId, LocalDateTime applicationDate);
}
