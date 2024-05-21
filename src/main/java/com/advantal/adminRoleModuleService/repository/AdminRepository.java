package com.advantal.adminRoleModuleService.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.adminRoleModuleService.models.Admin;
import com.advantal.adminRoleModuleService.responsepayload.AdminHistoryResponse;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

	Admin findByEmailAndStatus(String email, Short one);

	Admin findByMobileAndStatus(String mobile, Short one);

	Admin findByEmailAndPassword(String email, String password);

	Admin findByIdAndStatus(Long id, Short one);

	Admin findByEmail(String email);

	Admin findByMobileAndIdAndStatus(String mobile, Long id, Short one);

//	Page<Admin> findAllByStatus(Short one, Pageable pageable);

//	@Query(value = "SELECT * FROM admin adm WHERE adm.status != 0", countQuery = "SELECT count(*) from admin adm WHERE adm.status != 0", nativeQuery = true)
//	Page<Admin> findByNameContainingOrEmailContainingOrMobileContaining(String searchText, String searchText2,
//			String searchText3, Pageable pageable);

	List<Admin> findAllByStatus(Short one);

	List<Admin> findByRoleId(Long id);

	@Query(value = "SELECT * FROM admin ad WHERE ad.status != 0", countQuery = "SELECT count(*) from admin ad WHERE ad.status != 0", nativeQuery = true)
	Page<Admin> findAllAdmin(Pageable pageable);

	@Query(value = "SELECT * FROM admin ad WHERE ad.status != 0 and (ad.name like concat('%',?1,'%') or ad.email like concat('%',?1,'%') or ad.mobile like concat('%',?1,'%'))", countQuery = "SELECT count(*) from admin ad WHERE ad.status != 0 and (ad.name like concat('%',?1,'%') or ad.email like concat('%',?1,'%') or ad.mobile like concat('%',?1,'%'))", nativeQuery = true)
	Page<Admin> findAllAdmin(String keyWord, Pageable pageable);

	@Query(value = "SELECT COUNT(*) AS admin_count FROM admin;", nativeQuery = true)
	Long findAllAdminsCount();

	@Query(value = "SELECT COUNT(*) AS admin_count FROM admin ad WHERE ad.status = 1;", nativeQuery = true)
	Long findAllActiveAdminsCount();

	@Query(value = "SELECT COUNT(*) AS admin_count FROM admin ad WHERE ad.status = 2;", nativeQuery = true)
	Long findAllInactiveAdminsCount();

	@Query(value = "SELECT COUNT(*) AS admin_count FROM admin ad WHERE ad.status = 0;", nativeQuery = true)
	Long findAllDeletedAdminsCount();

	@Query(value = "SELECT COUNT(*) AS record_count	FROM admin ad WHERE (ad.entry_date >= ?1 AND ad.entry_date <= ?2);", nativeQuery = true)
	Long findAllAdminCurrentMonth(String startingDate, String endingDate);

	@Query(value = "SELECT COUNT(*) AS record_count	FROM admin ad WHERE (ad.entry_date >= ?1 AND ad.entry_date <= ?2) AND ad.status=1;", nativeQuery = true)
	Long findAllCurrentMonthActiveAdminsCount(String string, String string2);

//	@Query(value = "SELECT * FROM admin adm WHERE adm.status != 0", countQuery = "SELECT count(*) from admin adm WHERE adm.status != 0", nativeQuery = true)
//	List<Admin> findByAllAdmins();

//	@Query(value = "SELECT * FROM admin ad WHERE ad.status != 0 and (ad.name like concat('%',?1,'%') or ad.email like concat('%',?1,'%') or ad.mobile like concat('%',?1,'%'))", countQuery = "SELECT count(*) from admin ad WHERE ad.status != 0 and (ad.name like concat('%',?1,'%') or ad.email like concat('%',?1,'%') or ad.mobile like concat('%',?1,'%'))", nativeQuery = true)
//	Page<Admin> findAllSearchingAdmin(String keyWord, Pageable pageable);

	@Query(value = "SELECT * FROM admin ad WHERE ad.status != 0 and (ad.name like concat('%',?1,'%') or ad.email like concat('%',?1,'%') or ad.mobile like concat('%',?1,'%'))", countQuery = "SELECT count(*) from admin ad WHERE ad.status != 0 and (ad.name like concat('%',?1,'%') or ad.email like concat('%',?1,'%') or ad.mobile like concat('%',?1,'%'))", nativeQuery = true)
	List<Admin> findAllSearchingAdmin(String keyword);

	@Query(value = "SELECT * FROM admin ad WHERE ad.status != 0", countQuery = "SELECT count(*) from admin ad WHERE ad.status != 0", nativeQuery = true)
	List<Admin> findAllAdmins();


}
