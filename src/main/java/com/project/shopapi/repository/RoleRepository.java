package com.project.shopapi.repository;


import com.project.shopapi.entity.enums.ERole;
import com.project.shopapi.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
    boolean existsByName(ERole eRole);
}
