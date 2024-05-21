package com.advantal.adminRoleModuleService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.adminRoleModuleService.models.Support;


@Repository
public interface SupportRepository extends JpaRepository<Support, Long> {

	Optional<Support> findByIdAndStatus(Long id, Short one);

	@Query(value = "SELECT * FROM support su WHERE su.status != 0 and (su.ticket_type like concat('%',?1,'%') or su.ticket_description like concat('%',?1,'%'))", countQuery = "SELECT count(*) from support su WHERE su.status != 0 and (su.ticket_type like concat('%',?1,'%') or su.ticket_description like concat('%',?1,'%'))", nativeQuery = true)
	Page<Support> getAllTickets(String keyWord, Pageable pageable);

	@Query(value = "SELECT * FROM support su WHERE su.status != 0 ORDER BY id DESC", countQuery = "SELECT count(*) from support su WHERE su.status != 0", nativeQuery = true)
	Page<Support> getAllTickets(Pageable pageable);

	@Query(value = "SELECT * FROM support su WHERE su.status != 0 and (su.ticket_type like concat('%',?1,'%') or su.ticket_description like concat('%',?1,'%'))", countQuery = "SELECT count(*) from support su WHERE su.status != 0 and (su.ticket_type like concat('%',?1,'%') or su.ticket_description like concat('%',?1,'%'))", nativeQuery = true)
	List<Support> getAllTickets(String keyWord);

	@Query(value = "SELECT * FROM support su WHERE su.status != 0 ORDER BY id DESC", countQuery = "SELECT count(*) from support su WHERE su.status != 0", nativeQuery = true)
	List<Support> getAllTickets();


}	