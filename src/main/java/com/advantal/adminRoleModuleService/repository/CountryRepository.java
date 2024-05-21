package com.advantal.adminRoleModuleService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advantal.adminRoleModuleService.models.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

	Country findByCountry(String country);

}
