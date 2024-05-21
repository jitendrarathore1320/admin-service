package com.advantal.adminRoleModuleService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advantal.adminRoleModuleService.models.Broker;
import com.advantal.adminRoleModuleService.models.Country;

@Repository
public interface BrokerRepository extends JpaRepository<Broker, Long> {

	Broker findByIdAndStatus(Long id, Short one);

	List<Broker> findByType(String country);

}
