package com.electra.ElectraRegistrar.controller;

import com.electra.ElectraRegistrar.models.Company;
import com.electra.ElectraRegistrar.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @PostMapping("/addCompany")
    public ResponseEntity<?> addCompany(@RequestBody Company company) {
        Company savedCompany = companyService.addCompany(company);
        return ResponseEntity.ok(savedCompany);
    }
}
