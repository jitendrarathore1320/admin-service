package com.advantal.adminRoleModuleService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advantal.adminRoleModuleService.models.Otp;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {

	Otp findByAdminIdFk(Long id);

	void deleteByAdminIdFk(Long id);

}