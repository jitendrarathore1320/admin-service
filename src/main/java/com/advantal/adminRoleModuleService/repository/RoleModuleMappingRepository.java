package com.advantal.adminRoleModuleService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advantal.adminRoleModuleService.models.RoleModuleMapping;


@Repository
public interface RoleModuleMappingRepository extends JpaRepository<RoleModuleMapping, Long>{

	RoleModuleMapping findByIdAndStatus(Long id, Short one);

	RoleModuleMapping findByIdAndModuleNameAndStatus(Long id, String moduleName, Short one);

//	List<RoleModuleMapping> findByRoleIdFk(Long id);


}
