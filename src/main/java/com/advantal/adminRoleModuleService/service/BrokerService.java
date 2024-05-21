package com.advantal.adminRoleModuleService.service;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.advantal.adminRoleModuleService.requestpayload.BrokerRequestPayload;

public interface BrokerService {

//	Map<String, Object> addBroker(BrokerRequestPayload brokerRequestPayload);

	Map<String, Object> getBrokers(String type);

	Map<String, Object> addBroker(String brokerRequestPayload, MultipartFile file);

}