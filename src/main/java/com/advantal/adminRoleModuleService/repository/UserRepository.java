package com.advantal.adminRoleModuleService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.adminRoleModuleService.models.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByPhoneNoAndStatus(String phoneNo, Short one);

	User findByIdAndStatus(Long id, Short one);

	@Query(value = "SELECT * FROM user us WHERE us.status != 0", countQuery = "SELECT count(*) from user us WHERE us.status != 0", nativeQuery = true)
	List<User> findAllUsers();


}
