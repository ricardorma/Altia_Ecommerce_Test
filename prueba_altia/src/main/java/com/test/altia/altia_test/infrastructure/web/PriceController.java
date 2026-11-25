package com.test.altia.altia_test.infrastructure.web;

import com.test.altia.altia_test.application.port.in.GetApplicablePriceUseCase;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/prices")
public class PriceController {
    private final GetApplicablePriceUseCase getApplicablePrice;
    public PriceController(GetApplicablePriceUseCase getApplicablePrice) { this.getApplicablePrice = getApplicablePrice; }

    @GetMapping
    public ResponseEntity<PriceResponse> getApplicablePrice(
            @RequestParam @Positive Long productId,
            @RequestParam @Positive Long brandId,
            @RequestParam @NotNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate) {
        return ResponseEntity.ok(PriceResponse.from(getApplicablePrice.execute(productId, brandId, applicationDate)));
    }
}
