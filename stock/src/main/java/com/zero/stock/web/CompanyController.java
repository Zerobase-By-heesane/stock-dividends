package com.zero.stock.web;

import com.zero.stock.model.Company;
import com.zero.stock.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@Slf4j
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autoComplete(@RequestParam String companyName) {

        return null;
    }

    @GetMapping("")
    public ResponseEntity<?> searchCompany(@RequestParam String companyName) {

        return null;
    }

    @PostMapping("")
    public ResponseEntity<?> addCompany(@RequestBody Company request) {
        String ticker  = request.getTicker().trim();
        if(ObjectUtils.isEmpty(ticker)){
            throw new RuntimeException("Ticker is empty");
        }
        
        Company company = this.companyService.save(ticker);
        
        
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteCompany(@RequestParam String companyName) {

        return null;
    }
}
