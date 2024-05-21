package com.advantal.adminRoleModuleService.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushNotificationRequestPayload {

	@NotEmpty(message = "massage body can't be empty")
	@NotNull(message = "massage body can't be null")
	private String messageBody;
 
	@NotEmpty(message = "message Tittle can't be empty")
	@NotNull(message = "message Tittle body can't be null")
	private String messageTittle;
 
	@NotEmpty(message = "notification Type  can't be empty")
	@NotNull(message = "notification Type  can't be null")
	private String notificationType;
}
