package com.electra.ElectraRegistrar.repository;

import com.electra.ElectraRegistrar.models.Company;
import com.electra.ElectraRegistrar.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByCompany(Company company);

}
