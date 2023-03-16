package com.electra.ElectraRegistrar.repository;

import com.electra.ElectraRegistrar.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query(value = "SELECT u FROM User u where u.role like %:role%")
    List<User> findByRole(String role);

    @Query(value = "SELECT u FROM User u where u.email like %:email%")
    List<User> findByMail(String email);

    boolean existsByEmail(String email);
}
