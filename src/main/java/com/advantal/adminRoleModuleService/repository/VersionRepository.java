package com.advantal.adminRoleModuleService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.advantal.adminRoleModuleService.models.Version;

public interface VersionRepository extends JpaRepository<Version, Long> {

	

}
