package com.zero.stock.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @GetMapping("/autocomplete")
    public ResponseEntity<?> autoComplete(@RequestParam String companyName) {

        return null;
    }

    @GetMapping("")
    public ResponseEntity<?> searchCompany(@RequestParam String companyName) {

        return null;
    }

    @PostMapping("")
    public ResponseEntity<?> addCompany(@RequestBody String companyName) {

        return null;
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteCompany(@RequestParam String companyName) {

        return null;
    }
}
