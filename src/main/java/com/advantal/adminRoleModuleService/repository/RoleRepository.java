package com.advantal.adminRoleModuleService.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.adminRoleModuleService.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByRoleNameAndStatus(String roleName, Short one);

	Role findByIdAndStatus(Long id, Short one);

	Role findByRoleNameAndIdAndStatus(String roleName, Long id, Short one);

//	@Query(value = "SELECT * FROM role r WHERE r.status != 0 and r.role_name=?1", nativeQuery = true)
//	Page<Role> findByRoleNameContaining(String searchText, Pageable pageable);

	Page<Role> findAllByStatus(Pageable pageable, Short status);

	Page<Role> findByStatus(Pageable pageable, Short status);

	List<Role> findAllByStatus(Short st);

//	@Query(value = "SELECT * FROM role r WHERE r.status != 0", countQuery = "SELECT count(*) from role r WHERE r.status != 0", nativeQuery = true)
//	Page<Role> findAllByStatus(Pageable pageable);

	@Query(value = "SELECT * FROM role r WHERE r.status != 0 and r.role_name like concat('%',?1,'%')", countQuery = "SELECT count(*) from role r WHERE r.status != 0 and r.role_name like concat('%',?1,'%')", nativeQuery = true)
	Page<Role> findBySearchTextWithPagination(String searchText, Pageable pageable);

	@Query(value = "SELECT * FROM role r WHERE r.status != 0", countQuery = "SELECT count(*) from role r WHERE r.status != 0", nativeQuery = true)
	Page<Role> findAllRole(Pageable pageable);

	@Query(value = "SELECT * FROM role r WHERE r.status != 0 and r.role_name like concat('%',?1,'%')", countQuery = "SELECT count(*) from role r WHERE r.status != 0 and r.role_name like concat('%',?1,'%')", nativeQuery = true)
	List<Role> findBySearchText(String searchText);

	@Query(value = "SELECT * FROM role r WHERE r.status != 0", countQuery = "SELECT count(*) from role r WHERE r.status != 0", nativeQuery = true)
	List<Role> findAllRole();

}
