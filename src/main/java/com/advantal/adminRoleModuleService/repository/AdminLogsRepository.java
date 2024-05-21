package com.advantal.adminRoleModuleService.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.adminRoleModuleService.models.AdminHistory;

@Repository
public interface AdminLogsRepository extends JpaRepository<AdminHistory, Long> {

	@Query(value = "SELECT * FROM admin_history ad WHERE (ad.name like concat('%',?1,'%') or ad.email like concat('%',?1,'%') or ad.mobile like concat('%',?1,'%'))", countQuery = "SELECT count(*) from admin_history ad WHERE (ad.name like concat('%',?1,'%') or ad.email like concat('%',?1,'%') or ad.mobile like concat('%',?1,'%'))", nativeQuery = true)
	Page<AdminHistory> findAllAdminWithSearching(String keyWord, Pageable pageable);

	@Query(value = "SELECT * FROM admin_history", countQuery = "SELECT * FROM admin_history", nativeQuery = true)
	Page<AdminHistory> findAllAdminWithoutSearching(Pageable pageable);

}
