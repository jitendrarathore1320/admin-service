package com.advantal.adminRoleModuleService.responsepayload;

import com.advantal.adminRoleModuleService.models.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminHistoryResponse {

	private Long id;

	private String name;

	private String mobile;

	private String email;

	private String password;

	private String entryDate;

	private String updationDate;

	private String lastUpdationDate;

	private String passwordUpdationDate;

	private Short status;
	
	private boolean login;

	private Long roleId;

	private String imageUrl;

	private String country;

	private String city;

	private String pinCode;

	private String address;

	private RoleResponse role;

}
