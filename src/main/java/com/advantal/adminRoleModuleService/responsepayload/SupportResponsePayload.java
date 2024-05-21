package com.advantal.adminRoleModuleService.responsepayload;

import lombok.Data;
@Data
public class SupportResponsePayload {
	
	private Long id;
	
	private Long userId;
	
	private String ticketType;
	
	private String ticketDescription;

	private String creationDate;

	private String updationDate;

	private Short status;
	
	private String phoneNo;
	
//	private String email;

}
