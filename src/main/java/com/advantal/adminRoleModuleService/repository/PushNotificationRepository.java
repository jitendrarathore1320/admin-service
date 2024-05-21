package com.advantal.adminRoleModuleService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.advantal.adminRoleModuleService.models.PushNotification;

@Repository
public interface PushNotificationRepository extends JpaRepository<PushNotification, Long>{

	PushNotification findByNotificationType(String type);

}
