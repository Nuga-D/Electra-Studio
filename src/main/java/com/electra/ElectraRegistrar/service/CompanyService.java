package com.electra.ElectraRegistrar.service;

import com.electra.ElectraRegistrar.models.Company;
import com.electra.ElectraRegistrar.models.User;
import com.electra.ElectraRegistrar.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public Company addCompany(Company company) {
        Optional<Company> existingCompany = companyRepository.findByName(company.getName());
        if (existingCompany.isPresent()) {
            return existingCompany.get();
        } else {
            return companyRepository.save(company);
        }
    }

    public boolean companyExists(String companyName) {
        return companyRepository.findByName(companyName).isPresent();
    }

    public Optional<Company> findByName(String name) {
        return companyRepository.findByName(name);
    }

    public void saveCompany(Company company) {
        companyRepository.save(company);
    }
}
