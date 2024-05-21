package com.advantal.adminRoleModuleService.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class BrokerRequestPayload {
	
	@NotNull(message = "Id can't be null !!")
	private Long id;
	
	@NotEmpty(message = "Broker can't be empty !!")
	@NotNull(message = "Broker can't be null !!")
	private String broker;
	
	@NotEmpty(message = "Type can't be empty !!")
	@NotNull(message = "Type can't be null !!")
	private String type;
	
}
