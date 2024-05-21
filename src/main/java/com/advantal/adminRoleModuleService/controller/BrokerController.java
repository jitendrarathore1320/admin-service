package com.advantal.adminRoleModuleService.controller;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.advantal.adminRoleModuleService.requestpayload.BrokerRequestPayload;
import com.advantal.adminRoleModuleService.service.BrokerService;

@RestController
@RequestMapping("/api/broker")
public class BrokerController {

	@Autowired
	private BrokerService brokerService;

	@PostMapping(value = "/broker",consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Map<String, Object>> connectWallet(@RequestPart(required = true) String brokerRequestPayload,
			@RequestPart(value = "file", required = false) MultipartFile file) {
		return new ResponseEntity<Map<String, Object>>(brokerService.addBroker(brokerRequestPayload,file), HttpStatus.OK);
	}

	@GetMapping(value = "/brokers")
	public ResponseEntity<Map<String, Object>> getBrokers(@RequestParam String type) {
		return new ResponseEntity<Map<String, Object>>(brokerService.getBrokers(type), HttpStatus.OK);
	}

}
