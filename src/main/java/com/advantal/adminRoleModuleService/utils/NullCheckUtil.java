package com.advantal.adminRoleModuleService.utils;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.advantal.adminRoleModuleService.requestpayload.AdminRequestPayload;
import com.advantal.adminRoleModuleService.requestpayload.BrokerRequestPayload;
import com.advantal.adminRoleModuleService.requestpayload.NewsRequestPayload;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Component
public class NullCheckUtil {

	public Map<String, Object> checkValidation(AdminRequestPayload adminRequest) {
		Map<String, Object> map = new HashMap<String, Object>();
		/* check mobile number */
		if ((adminRequest.getMobile() == null || adminRequest.getMobile().isBlank()) || adminRequest.getId() == null) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.MOBILE_NOT_EMPTY);
			map.put(Constant.DATA, adminRequest);
			log.info("Mobile number or admin id can't be null or empty !!" + adminRequest.getMobile(),
					adminRequest.getId());
			return map;
		}

		/* check Address number */
		if (adminRequest.getAddress() == null) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.ADDRESS_NOT_EMPTY);
			map.put(Constant.DATA, adminRequest);
			log.info("Address can't be null !!" + adminRequest.getAddress());
			return map;
		}

		/* check City number */
		if (adminRequest.getCity() == null) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.CITY_NOT_EMPTY);
			map.put(Constant.DATA, adminRequest);
			log.info("City can't be null !!" + adminRequest.getCity());
			return map;
		}

		/* check Country number */
		if (adminRequest.getCountry() == null) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.COUNTRY_NOT_EMPTY);
			map.put(Constant.DATA, adminRequest);
			log.info("Conutry can't be null !!" + adminRequest.getCountry());
			return map;
		}

		/* check PinCode number */
		if (adminRequest.getPinCode() == null) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.PINCODE_NOT_EMPTY);
			map.put(Constant.DATA, adminRequest);
			log.info("Pincode can't be null !!" + adminRequest.getPinCode());
			return map;
		}

		/* check Email number */
		if ((adminRequest.getEmail() == null || adminRequest.getEmail().isBlank())) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.EMAIL_NOT_EMPTY);
			map.put(Constant.DATA, adminRequest);
			log.info("Email can't be null or empty !!" + adminRequest.getEmail());
			return map;
		}

		/* check Name number */
		if ((adminRequest.getName() == null || adminRequest.getName().isBlank())) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.NAME_NOT_EMPTY);
			map.put(Constant.DATA, adminRequest);
			log.info("Name can't be null or empty !!!" + adminRequest.getName());
			return map;
		}
		map.put(Constant.RESPONSE_CODE, Constant.OK);
		map.put(Constant.DATA, adminRequest);
		return map;
	}

	public static Map<String, Object> checkNewsRequestPayloadIsNull(NewsRequestPayload requestPayload) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (requestPayload.getId() == null) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, "Id Can't be null !!");
			log.info("Id Can't be null ! status - {}" + requestPayload.getId());
			return map;
		}
		if (requestPayload.getTitle() == null || requestPayload.getTitle().isBlank()) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, "Title Can't be null or empty !!");
			log.info("Title can't be null or empty ! status - {}" + requestPayload.getTitle());
			return map;
		}
//		if (requestPayload.getSubTitle() == null || requestPayload.getSubTitle().isBlank()) {
//			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
//			map.put(Constant.MESSAGE, "SubTitle Can't be null or empty !!");
//			log.info("SubTitle can't be null or empty ! status - {}" + requestPayload.getSubTitle());
//			return map;
//		}
		if (requestPayload.getDescription() == null || requestPayload.getDescription().isBlank()) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, "Description Can't be null or empty !!");
			log.info("Description can't be null or empty ! status - {}" + requestPayload.getDescription());
			return map;
		}
		return map;
	}

	public Map<String, Object> checkBrokerValidation(BrokerRequestPayload brokerRequestPayload2) {
		Map<String, Object> map = new HashMap<String, Object>();
		/* check id */
		if (brokerRequestPayload2.getId() == null) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.ID_CAN_NOT_NULL_MESSAGE);
			map.put(Constant.DATA, brokerRequestPayload2);
			log.info("Brocker id can't be null !!" + brokerRequestPayload2.getId());
			return map;
		}
		
		/* check broker */
		if(brokerRequestPayload2.getBroker() == null  || brokerRequestPayload2.getBroker().isBlank()) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.BROKER_NOT_EMPTY);
			map.put(Constant.DATA, brokerRequestPayload2);
			log.info("Broker can't be null !!" + brokerRequestPayload2.getBroker());
			return map;
		}
		
		/* check country */
		if(brokerRequestPayload2.getType() == null || brokerRequestPayload2.getType().isBlank()) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.TYPE_CANT_BE_NULL);
			map.put(Constant.DATA, brokerRequestPayload2);
			log.info("Broker can't be null !!" + brokerRequestPayload2.getType());
			return map;
		}
		map.put(Constant.RESPONSE_CODE, Constant.OK);
		map.put(Constant.DATA, brokerRequestPayload2);
		return map;
	}

}
