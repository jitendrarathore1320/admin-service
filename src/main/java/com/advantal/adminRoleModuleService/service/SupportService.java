package com.advantal.adminRoleModuleService.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.advantal.adminRoleModuleService.models.Support;
import com.advantal.adminRoleModuleService.requestpayload.SearchRequestPayload;
import com.advantal.adminRoleModuleService.requestpayload.SupportRequestPayload;

public interface SupportService {

	Map<String, Object> raiseTicket(SupportRequestPayload supportRequestPayload);

	Map<String, Object> getAllTickets(SearchRequestPayload searchRequestPayload);

	Map<String, Object> ticketAction(SupportRequestPayload supportRequestPayload);

	List<Support> getAllSupportDetails(@Valid SearchRequestPayload searchRequestPayload);

}
