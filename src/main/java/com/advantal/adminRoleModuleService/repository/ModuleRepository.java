package com.advantal.adminRoleModuleService.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.advantal.adminRoleModuleService.models.Module;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long>{

	Module findByIdAndStatus(Long id, Short one);

	Module findByIdAndModuleNameAndStatus(Long id, String moduleName, Short one);

	List<Module> findAllByStatus(Short one);
}
