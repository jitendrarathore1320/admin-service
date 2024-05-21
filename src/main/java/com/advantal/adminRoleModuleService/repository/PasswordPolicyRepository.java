package com.advantal.adminRoleModuleService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advantal.adminRoleModuleService.models.PasswordPolicy;

@Repository
public interface PasswordPolicyRepository extends JpaRepository<PasswordPolicy, Integer>{

	PasswordPolicy findByTitle(String title);

}