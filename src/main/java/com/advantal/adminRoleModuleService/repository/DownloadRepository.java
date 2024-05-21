package com.advantal.adminRoleModuleService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.advantal.adminRoleModuleService.models.Download;

@Repository

public interface DownloadRepository extends JpaRepository<Download, Long> {

	//Download findByDeviceType(String deviceType);

//	Download findByDeviceType(String deviceType);
//
//	@Query("SELECT SUM(d.iosCount) FROM Download d")
//	int getIosDownloadCount();
//
//	@Query("SELECT SUM(d.androidCount) FROM Download d")
//	int getAndroidDownloadCount();
//
//	List<Download> findAll();

//	Integer getTotalIosCount();
//
//	Integer getTotalAndroidCount();
//	

//	@Query("SELECT SUM(d.iosCount) FROM Download d")
//	Integer getTotalIosCount();
//
//	@Query("SELECT SUM(d.androidCount) FROM Download d")
//	Integer getTotalAndroidCount();

	// Download findByDeviceType(String deviceType);
	 Integer getDownloadCountByDeviceType(String deviceType);

     Download findByDeviceType(String deviceType);
}
