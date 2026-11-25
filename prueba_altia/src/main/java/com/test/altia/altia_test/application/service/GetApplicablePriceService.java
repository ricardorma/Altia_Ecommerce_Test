package com.test.altia.altia_test.application.service;

import com.test.altia.altia_test.application.port.in.GetApplicablePriceUseCase;
import com.test.altia.altia_test.domain.model.Price;
import com.test.altia.altia_test.domain.port.out.PriceRepository;
import java.time.LocalDateTime;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GetApplicablePriceService implements GetApplicablePriceUseCase {
    private final PriceRepository priceRepository;

    public GetApplicablePriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    @Cacheable(cacheNames = "applicable-prices", key = "#productId + ':' + #brandId + ':' + #applicationDate")
    public Price execute(Long productId, Long brandId, LocalDateTime applicationDate) {
        log.info("Looking up price: productId={}, brandId={}, applicationDate={}", productId, brandId, applicationDate);
        return priceRepository.findApplicablePrice(productId, brandId, applicationDate)
                .orElseThrow(() -> new PriceNotFoundException(productId, brandId));
    }
}
