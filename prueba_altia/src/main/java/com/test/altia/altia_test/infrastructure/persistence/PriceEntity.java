package com.test.altia.altia_test.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "prices")
public class PriceEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Column(name = "brand_id", nullable = false) private Long brandId;
    @Getter
    @Column(name = "start_date", nullable = false) private LocalDateTime startDate;
    @Getter
    @Column(name = "end_date", nullable = false) private LocalDateTime endDate;
    @Getter
    @Column(name = "price_list", nullable = false) private Long priceList;
    @Getter
    @Column(name = "product_id", nullable = false) private Long productId;
    @Getter
    @Column(nullable = false) private Integer priority;
    @Getter
    @Column(nullable = false, precision = 19, scale = 2) private BigDecimal price;
    @Getter
    @Column(nullable = false, length = 3) private String currency;

    protected PriceEntity() { }

}
