package com.electra.ElectraRegistrar.controller;

import com.electra.ElectraRegistrar.models.Company;
import com.electra.ElectraRegistrar.models.SuccessResponse;
import com.electra.ElectraRegistrar.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/allCompanies")
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Optional<Company> companyOptional = companyService.getCompanyById(id);
        return companyOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompany(@PathVariable Long id, @RequestBody Company company) {
        Optional<Company> existingCompany = companyService.getCompanyById(id);
        if (!existingCompany.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        company.setId(id);
        companyService.saveCompany(company);
        return ResponseEntity.ok(new SuccessResponse("Company updated successfully!"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        Optional<Company> existingCompany = companyService.getCompanyById(id);
        if (!existingCompany.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        companyService.deleteCompanyById(id);
        return ResponseEntity.noContent().build();
    }
}
