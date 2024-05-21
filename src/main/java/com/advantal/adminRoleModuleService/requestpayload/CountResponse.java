package com.advantal.adminRoleModuleService.requestpayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountResponse {

	private Long total;
	
	private Long active;
    
	private Long inactive;
	
	private Long deleted;
	
	private Long not_verified;
}
