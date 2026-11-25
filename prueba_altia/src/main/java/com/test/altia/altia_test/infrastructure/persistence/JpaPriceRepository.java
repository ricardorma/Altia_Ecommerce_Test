package com.test.altia.altia_test.infrastructure.persistence;

import com.test.altia.altia_test.domain.model.Price;
import com.test.altia.altia_test.domain.port.out.PriceRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
public class JpaPriceRepository implements PriceRepository {
    private final SpringDataPriceRepository repository;
    public JpaPriceRepository(SpringDataPriceRepository repository) { this.repository = repository; }
    public Optional<Price> findApplicablePrice(Long productId, Long brandId, LocalDateTime applicationDate) {
        return repository.findApplicable(productId, brandId, applicationDate, PageRequest.of(0, 1))
                .stream().findFirst().map(this::toDomain);
    }
    private Price toDomain(PriceEntity entity) {
        return new Price(entity.getBrandId(), entity.getStartDate(), entity.getEndDate(), entity.getPriceList(),
                entity.getProductId(), entity.getPriority(), entity.getPrice(), entity.getCurrency());
    }
}
