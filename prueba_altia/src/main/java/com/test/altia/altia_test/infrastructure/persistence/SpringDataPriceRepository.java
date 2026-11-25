package com.test.altia.altia_test.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface SpringDataPriceRepository extends JpaRepository<PriceEntity, Long> {
    @Query("""
            SELECT p FROM PriceEntity p
            WHERE p.productId = :productId AND p.brandId = :brandId
              AND :applicationDate BETWEEN p.startDate AND p.endDate
            ORDER BY p.priority DESC, p.startDate DESC
            """)
    List<PriceEntity> findApplicable(@Param("productId") Long productId,
                                     @Param("brandId") Long brandId,
                                     @Param("applicationDate") LocalDateTime applicationDate,
                                     Pageable pageable);
}
