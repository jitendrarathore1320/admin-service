package com.advantal.adminRoleModuleService.requestpayload;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupportRequestPayload {
	@NotNull(message = "Id can't be null !!")
	private Long id;

	@NotEmpty(message = "TicketType can't be empty !!")
	private String ticketType;

	@NotEmpty(message = "Ticket description can't be empty !!")
	private String ticketDescription;
	
	@NotNull(message = "UserId can't be null !!")
	private Long userId;

	@NotNull(message = "Status can't be null !!")
	private Short status;
}
