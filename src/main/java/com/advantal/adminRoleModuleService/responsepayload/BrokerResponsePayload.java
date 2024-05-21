package com.advantal.adminRoleModuleService.responsepayload;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class BrokerResponsePayload {
	
	private Long id;
	
	private String broker;
	
	private String type;

	private String logo;
}
