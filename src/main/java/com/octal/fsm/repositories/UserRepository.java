package com.octal.fsm.repositories;

import com.octal.fsm.entities.Role;
import com.octal.fsm.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String username);

    Optional<User> findByEmailAndRole(@NotBlank String email, Role role);

    Optional<User> findByEmailAndRoleAndTenantId(@NotBlank String email, Role role, String tenantId);
}
