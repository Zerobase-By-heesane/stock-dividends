package com.zero.stock.web;

import com.zero.stock.model.Company;
import com.zero.stock.model.constant.CacheKey;
import com.zero.stock.persist.entity.CompanyEntity;
import com.zero.stock.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@Slf4j
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final CacheManager cacheManager;

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autoComplete(@RequestParam String companyName) {
        return ResponseEntity.ok(this.companyService.getCompanyNamesByKeyword(companyName));
    }

    @GetMapping
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<?> searchCompany(final Pageable pageable) {
        Page<CompanyEntity> companies = this.companyService.getAllCompanies(pageable);
        return ResponseEntity.ok(companies);
    }

    @PostMapping
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<Company> addCompany(@RequestBody Company request) {
        String ticker = request.getTicker().trim();
        if (ObjectUtils.isEmpty(ticker)) {
            throw new RuntimeException("Ticker is empty");
        }

        Company company = this.companyService.save(ticker);

        // AutoComplete 추가
        this.companyService.addAutoCompleteKeyword(company.getName());

        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/{ticker}")
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> deleteCompany(@PathVariable String ticker) {
        String companyName = this.companyService.deleteCompany(ticker);
        this.clearFinanceCache(companyName);
        return ResponseEntity.ok(companyName);
    }

    private void clearFinanceCache(String companyName){
        this.cacheManager.getCache(CacheKey.KEY_FINANCE).evict(companyName);
    }
}
