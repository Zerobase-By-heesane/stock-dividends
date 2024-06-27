package com.zero.stock.web;

import com.zero.stock.model.ScrapedResult;
import com.zero.stock.service.FinanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/finance")
@AllArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/dividends/{companyName}")
    public ResponseEntity<ScrapedResult> searchDividends(@PathVariable String companyName) {
        return ResponseEntity.ok(this.financeService.getDividendsByCompanyName(companyName));
    }
}
