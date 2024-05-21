package com.advantal.adminRoleModuleService.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String phoneNo;
		
	private String deviceId;

	private String deviceToken;

	private String deviceType;
	
	private String creationDate;

	private String updationDate;
	
	private Short status;
	
	private String countryCode;
	
	//add new param

	private String name;
	
	private String imageUrl;
	
	private String email;

}
