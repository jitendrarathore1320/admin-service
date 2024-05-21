package com.advantal.adminRoleModuleService.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.advantal.adminRoleModuleService.requestpayload.SearchRequestPayload;
import com.advantal.adminRoleModuleService.requestpayload.SupportRequestPayload;
import com.advantal.adminRoleModuleService.service.SupportService;
import com.advantal.adminRoleModuleService.utils.Constant;

//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
//import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/support")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SupportController {

	@Autowired
	private SupportService supportService;

	@PostMapping(value = "/ticket_action")
//	@CircuitBreaker(name="getUserCircuiteBreaker", fallbackMethod = "getUserFallBack")
//	@ApiOperation(value = "Replay to user status=1 should be or for close the ticket status=2 should be !!")
	public Map<String, Object> ticketAction(@RequestBody @Valid SupportRequestPayload supportRequestPayload) {
		Map<String, Object> map = new HashMap<>();
		if (supportRequestPayload != null) {
			return supportService.ticketAction(supportRequestPayload);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
			log.info("Bad request! status - {}", Constant.BAD_REQUEST);
			return map;
		}
	}

	/* FallBack method for getUserFallBack() */
//	public Map<String, Object> getUserFallBack(SupportRequestPayload supportRequestPayload, Exception e){
//		Map<String, Object> map = new HashMap<>();
//		
//		map.put(Constant.DATA, supportRequestPayload);
//		map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
//		map.put(Constant.MESSAGE, "User service is unavailable!!");
//		log.info("User service is unavailable ! status - {}", Constant.SERVER_ERROR);
//		return map;
//	}

	@PostMapping("/tickets")
//	@ApiOperation(value = "Get tickets list by search or default list !!")
	public Map<String, Object> getAllTickets(@RequestBody SearchRequestPayload searchRequestPayload) {
		Map<String, Object> map = new HashMap<>();
		if (searchRequestPayload != null) {
			return supportService.getAllTickets(searchRequestPayload);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
			log.info("Bad request! status - {}", Constant.BAD_REQUEST);
			return map;
		}
	}

	@PostMapping(value = "/raise_ticket")
//	@ApiOperation(value = "This api can be used to generate  ticket!!")
	public Map<String, Object> raiseTicket(@RequestBody @Valid SupportRequestPayload supportRequestPayload) {
		Map<String, Object> map = new HashMap<>();
		if (supportRequestPayload != null) {
			return supportService.raiseTicket(supportRequestPayload);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
			log.info("Bad request! status - {}", Constant.BAD_REQUEST);
			return map;
		}
	}

}
