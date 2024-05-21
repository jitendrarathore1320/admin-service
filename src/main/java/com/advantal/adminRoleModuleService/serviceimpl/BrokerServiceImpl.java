package com.advantal.adminRoleModuleService.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.advantal.adminRoleModuleService.models.Broker;
import com.advantal.adminRoleModuleService.models.Country;
import com.advantal.adminRoleModuleService.repository.BrokerRepository;
import com.advantal.adminRoleModuleService.repository.CountryRepository;
import com.advantal.adminRoleModuleService.requestpayload.BrokerRequestPayload;
import com.advantal.adminRoleModuleService.responsepayload.BrokerResponsePayload;
import com.advantal.adminRoleModuleService.service.BrokerService;
import com.advantal.adminRoleModuleService.utils.Constant;
import com.advantal.adminRoleModuleService.utils.FileUploader;
import com.advantal.adminRoleModuleService.utils.NullCheckUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BrokerServiceImpl implements BrokerService {

	@Autowired
	private BrokerRepository brokerRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private NullCheckUtil nullCheckUtil;

	@Value("${spring.filedir}")
	private String uploadDir;

	@Value("${spring.serverfilepath}")
	private String serverFilePath;

	@Value("${spring.imagedir}")
	private String imageDir;

	@Value("${spring.baseurl}")
	private String baseurl;

	@Value("${spring.logodir}")
	private String logoDir;

	@Value("${spring.logourl}")
	private String logoUrl;

	Boolean isExist(BrokerRequestPayload brokerRequestPayload, String country) {
		Boolean flag = false;
		List<Broker> brokerList = brokerRepository.findByType(country);
		if (!brokerList.isEmpty()) {
			for (Broker broker : brokerList) {
				if (broker.getBroker().equals(brokerRequestPayload.getBroker())) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	Boolean isExistBroker(BrokerRequestPayload brokerRequestPayload, String country) {
		Boolean flag = false;
		List<Broker> brokerList = brokerRepository.findByType(country);
		if (!brokerList.isEmpty()) {
			for (Broker broker : brokerList) {
				if (broker.getBroker().equals(brokerRequestPayload.getBroker())
						&& broker.getId().equals(brokerRequestPayload.getId())) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

//	@Override
//	public Map<String, Object> addBroker(BrokerRequestPayload brokerRequestPayload) {
//		Map<String, Object> map = new HashMap<>();
//		try {
//			Country country = countryRepository.findByCountry(brokerRequestPayload.getCountry());
//			if (country != null) {
//				BrokerResponsePayload brokerResponsePayload = new BrokerResponsePayload();
//				if (brokerRequestPayload.getId() == 0) {
//					if (!isExist(brokerRequestPayload, country)) {
//						Broker broker = new Broker();
//						BeanUtils.copyProperties(brokerRequestPayload, broker);
//						broker.setStatus(Constant.ONE);
//						broker.setCreationDate(new Date());
//						broker.setCountryIdFk(country.getId());
//						broker = brokerRepository.save(broker);
//
//						BeanUtils.copyProperties(broker, brokerResponsePayload);
//						map.put(Constant.RESPONSE_CODE, Constant.OK);
//						map.put(Constant.MESSAGE, Constant.BROKER_SAVED_MESSAGE);
//						map.put(Constant.DATA, brokerResponsePayload);
//						log.info(Constant.BROKER_SAVED_MESSAGE + " status - {} " + Constant.OK);
//					} else {
//						map.put(Constant.RESPONSE_CODE, Constant.OK);
//						map.put(Constant.MESSAGE, Constant.BROKER_ALREADY_EXIST_MESSAGE);
//						log.info(Constant.BROKER_ALREADY_EXIST_MESSAGE + " status - {} " + Constant.OK);
//						return map;
//					}
//				} else {
//					Broker broker = brokerRepository.findByIdAndStatus(brokerRequestPayload.getId(), Constant.ONE);
//					if (broker != null) {
//						if (isExistBroker(brokerRequestPayload, country)) {
//							BeanUtils.copyProperties(brokerRequestPayload, broker);
//							broker.setUpdationDate(new Date());
//							broker = brokerRepository.save(broker);
//							BeanUtils.copyProperties(broker, brokerResponsePayload);
//							map.put(Constant.RESPONSE_CODE, Constant.OK);
//							map.put(Constant.MESSAGE, Constant.BROKER_UPDATED_MESSAGE);
//							map.put(Constant.DATA, brokerResponsePayload);
//							log.info(Constant.BROKER_UPDATED_MESSAGE + " status - {} " + Constant.OK);
//						} else if (!isExist(brokerRequestPayload, country)) {
//							BeanUtils.copyProperties(brokerRequestPayload, broker);
//							broker.setUpdationDate(new Date());
//							broker = brokerRepository.save(broker);
//							BeanUtils.copyProperties(broker, brokerResponsePayload);
//							map.put(Constant.RESPONSE_CODE, Constant.OK);
//							map.put(Constant.MESSAGE, Constant.BROKER_UPDATED_MESSAGE);
//							map.put(Constant.DATA, brokerResponsePayload);
//							log.info(Constant.BROKER_UPDATED_MESSAGE + " status - {} " + Constant.OK);
//						} else {
//							map.put(Constant.RESPONSE_CODE, Constant.OK);
//							map.put(Constant.MESSAGE, Constant.BROKER_ALREADY_EXIST_MESSAGE);
//							log.info(Constant.BROKER_ALREADY_EXIST_MESSAGE + " status - {} " + Constant.OK);
//						}
//					} else {
//						map.put(Constant.RESPONSE_CODE, Constant.OK);
//						map.put(Constant.MESSAGE, Constant.ID_NOT_FOUND_MESSAGE);
//						log.info(Constant.ID_NOT_FOUND_MESSAGE + " status - {} " + Constant.OK);
//					}
//				}
//			} else {
//				map.put(Constant.RESPONSE_CODE, Constant.OK);
//				map.put(Constant.MESSAGE, Constant.COUNTRY_NOT_FOUND_MESSAGE);
//				log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " status - {} " + Constant.OK);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return map;
//	}

	@Override
	public Map<String, Object> getBrokers(String type) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<BrokerResponsePayload> brokerResponseList = new ArrayList<>();
			if (type != null && !type.isBlank()) {
				List<Broker> brokerList = brokerRepository.findByType(type);
				if (!brokerList.isEmpty()) {
					for (Broker broker : brokerList) {
						BrokerResponsePayload brokerResponsePayload = new BrokerResponsePayload();
						BeanUtils.copyProperties(broker, brokerResponsePayload);
						brokerResponseList.add(brokerResponsePayload);
					}
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
					map.put(Constant.DATA, brokerResponseList);
					log.info(Constant.RECORD_FOUND_MESSAGE + " status - {} " + Constant.OK);
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
					map.put(Constant.DATA, brokerResponseList);
					log.info(Constant.RECORD_NOT_FOUND_MESSAGE + " status - {} " + Constant.OK);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.COUNTRY_CAN_NOT_NULL_MESSAGE);
				log.info(Constant.COUNTRY_CAN_NOT_NULL_MESSAGE + " status - {} " + Constant.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Map<String, Object> addBroker(String brokerRequestPayload, MultipartFile file) {
		Map<String, Object> map = new HashMap<>();
		try {
			String imagePath = null;
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			BrokerRequestPayload brokerRequestPayload2 = objectMapper.readValue(brokerRequestPayload,
					BrokerRequestPayload.class);
			Map<String, Object> map2 = nullCheckUtil.checkBrokerValidation(brokerRequestPayload2);
			BrokerRequestPayload brokerRequestPayload3 = objectMapper.convertValue(map2.get("data"),
					BrokerRequestPayload.class);
			if (map2.get("responseCode").equals("200")) {
				if (brokerRequestPayload3 != null && brokerRequestPayload3.getId() != null) {
					Country country = countryRepository.findByCountry(brokerRequestPayload3.getType());
					if (country != null) {
						BrokerResponsePayload brokerResponsePayload = new BrokerResponsePayload();
						if (brokerRequestPayload3.getId() == 0) {
							if (!isExist(brokerRequestPayload3, country.getCountry())) {
								Broker broker = new Broker();
								BeanUtils.copyProperties(brokerRequestPayload3, broker);
								if (file != null) {
									imagePath = logoUrl + FileUploader.uploadBrokerImage(file, logoDir,
											brokerRequestPayload3.getId());
									log.info("Image update successfully! status - {}", imagePath);
								}
								if (file != null) {
									broker.setLogo(imagePath);
								} else {
									broker.setLogo(broker.getLogo());
								}
								broker.setStatus(Constant.ONE);
								broker.setCreationDate(new Date());
								broker = brokerRepository.save(broker);

								BeanUtils.copyProperties(broker, brokerResponsePayload);
								map.put(Constant.RESPONSE_CODE, Constant.OK);
								map.put(Constant.MESSAGE, Constant.BROKER_SAVED_MESSAGE);
								map.put(Constant.DATA, brokerResponsePayload);
								log.info(Constant.BROKER_SAVED_MESSAGE + " status - {} " + Constant.OK);
							} else {
								map.put(Constant.RESPONSE_CODE, Constant.OK);
								map.put(Constant.MESSAGE, Constant.BROKER_ALREADY_EXIST_MESSAGE);
								log.info(Constant.BROKER_ALREADY_EXIST_MESSAGE + " status - {} " + Constant.OK);
								return map;
							}
						} else {
							Broker broker = brokerRepository.findByIdAndStatus(brokerRequestPayload3.getId(),
									Constant.ONE);
							if (broker != null) {
								if (isExistBroker(brokerRequestPayload3, country.getCountry())) {
									BeanUtils.copyProperties(brokerRequestPayload3, broker);
									if (file != null) {
										imagePath = logoUrl + FileUploader.uploadBrokerImage(file, logoDir,
												brokerRequestPayload3.getId());
										log.info("Image update successfully! status - {}", imagePath);
									}
									if (file != null) {
										broker.setLogo(imagePath);
									} else {
										broker.setLogo(broker.getLogo());
									}
									broker.setUpdationDate(new Date());
									broker = brokerRepository.save(broker);
									BeanUtils.copyProperties(broker, brokerResponsePayload);
									map.put(Constant.RESPONSE_CODE, Constant.OK);
									map.put(Constant.MESSAGE, Constant.BROKER_UPDATED_MESSAGE);
									map.put(Constant.DATA, brokerResponsePayload);
									log.info(Constant.BROKER_UPDATED_MESSAGE + " status - {} " + Constant.OK);
								} else if (!isExist(brokerRequestPayload3, country.getCountry())) {
									BeanUtils.copyProperties(brokerRequestPayload3, broker);
									if (file != null) {
										imagePath = logoUrl + FileUploader.uploadBrokerImage(file, logoDir,
												brokerRequestPayload3.getId());
										log.info("Image update successfully! status - {}", imagePath);
									}
									if (file != null) {
										broker.setLogo(imagePath);
									} else {
										broker.setLogo(broker.getLogo());
									}
									broker.setUpdationDate(new Date());
									broker = brokerRepository.save(broker);
									BeanUtils.copyProperties(broker, brokerResponsePayload);
									map.put(Constant.RESPONSE_CODE, Constant.OK);
									map.put(Constant.MESSAGE, Constant.BROKER_UPDATED_MESSAGE);
									map.put(Constant.DATA, brokerResponsePayload);
									log.info(Constant.BROKER_UPDATED_MESSAGE + " status - {} " + Constant.OK);
								} else {
									map.put(Constant.RESPONSE_CODE, Constant.OK);
									map.put(Constant.MESSAGE, Constant.BROKER_ALREADY_EXIST_MESSAGE);
									log.info(Constant.BROKER_ALREADY_EXIST_MESSAGE + " status - {} " + Constant.OK);
								}
							} else {
								map.put(Constant.RESPONSE_CODE, Constant.OK);
								map.put(Constant.MESSAGE, Constant.ID_NOT_FOUND_MESSAGE);
								log.info(Constant.ID_NOT_FOUND_MESSAGE + " status - {} " + Constant.OK);
							}
						}
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.COUNTRY_NOT_FOUND_MESSAGE);
						log.info(Constant.COUNTRY_NOT_FOUND_MESSAGE + " status - {} " + Constant.OK);
					}
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
					map.put(Constant.MESSAGE, Constant.BROKER_ID_NOT_EMPTY);
					log.info("Admin id can't be null or empty !!" + brokerRequestPayload3.getId());
				}
			} else {
				map.put(Constant.RESPONSE_CODE, map2.get("responseCode"));
				map.put(Constant.MESSAGE, map2.get("message"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}
