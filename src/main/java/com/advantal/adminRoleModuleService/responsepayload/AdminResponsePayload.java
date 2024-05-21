package com.advantal.adminRoleModuleService.responsepayload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminResponsePayload {

	private Long id;

	private String name;

	private String mobile;

	private String email;
	
	private String password;

	private String entryDate;

	private String updationDate;
	
	private Short status;
	//new parameter
	private String imageUrl;
	
	private String country;
	
	private String city;
	
	private String pinCode;
	
	private String address;
	
	private RoleResponsePayload roleResponsePayload;

}
