package com.advantal.adminRoleModuleService.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.advantal.adminRoleModuleService.externalService.UserService;
import com.advantal.adminRoleModuleService.models.Support;
import com.advantal.adminRoleModuleService.models.User;
import com.advantal.adminRoleModuleService.repository.SupportRepository;
import com.advantal.adminRoleModuleService.repository.UserRepository;
import com.advantal.adminRoleModuleService.requestpayload.SearchRequestPayload;
import com.advantal.adminRoleModuleService.requestpayload.SupportRequestPayload;
import com.advantal.adminRoleModuleService.responsepayload.SupportResponsePage;
import com.advantal.adminRoleModuleService.responsepayload.SupportResponsePayload;
import com.advantal.adminRoleModuleService.service.SupportService;
import com.advantal.adminRoleModuleService.utils.Constant;
import com.advantal.adminRoleModuleService.utils.DateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SupportServiceimpl implements SupportService {

	@Autowired
	private SupportRepository supportRepository;

	@Autowired
	private UserRepository userRepository;

//	@Autowired
//	private RestTemplate restTemplate;

	@Autowired
	private UserService userService;

	@Override
	public Map<String, Object> ticketAction(SupportRequestPayload supportRequestPayload) {
		Map<String, Object> map = new HashMap<>();
		try {
			Support support = new Support();
			Optional<Support> optionalSupport = Optional.empty();
			User user = new User();
//			user = userRepository.findByIdAndStatus(supportRequestPayload.getUserId(), Constant.ONE);

			/* Calling USER-SERVICE */
			try {
				Map<String, Object> res = userService.getProfileById(supportRequestPayload.getUserId());
				Object obj = res.get("data");
				ObjectMapper mapper = new ObjectMapper();
				user = mapper.convertValue(obj, User.class);
			} catch (Exception e) {
				map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
				map.put(Constant.MESSAGE, Constant.INTERNAL_SERVER_ERROR_MESSAGE);
				log.info(e.getMessage() + " status - {}", Constant.SERVER_ERROR);
				return map;
			}
			if (user != null) {
				/* Close the ticket, value should be like status = 2 */
				if (supportRequestPayload.getId() != 0 && supportRequestPayload.getStatus() == 2) {
					optionalSupport = supportRepository.findById(supportRequestPayload.getId());
					if (!optionalSupport.isEmpty()) {
						support = optionalSupport.get();
						if (support.getStatus() != 0) {
							if (support.getStatus() != 2) {
								support.setTicketType(supportRequestPayload.getTicketType());
								support.setTicketDescription(supportRequestPayload.getTicketDescription());
								support.setStatus(supportRequestPayload.getStatus());
								support.setUpdationDate(new Date());
								supportRepository.save(support);
								map.put(Constant.RESPONSE_CODE, Constant.OK);
								map.put(Constant.MESSAGE, Constant.TICKET_CLOSED_SUCCESS_MESSAGE);
								log.info("Ticket closed successfully! status - {}", Constant.OK);
							} else {
								map.put(Constant.RESPONSE_CODE, Constant.CONFLICT);
								map.put(Constant.MESSAGE, Constant.TICKET_ALREADY_CLOSED_MESSAGE);
								log.info("This ticket already closed! status - {}", Constant.CONFLICT);
							}
						} else {
							map.put(Constant.RESPONSE_CODE, Constant.CONFLICT);
							map.put(Constant.MESSAGE, Constant.TICKET_ALREADY_DELETED_MESSAGE);
							log.info("This ticket already deleted! status - {}", Constant.CONFLICT);
						}
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
						map.put(Constant.MESSAGE, Constant.FAILED_TO_CLOSE_TICKET_MESSAGE);
						log.info(
								"Failed to close the ticket because, given id not found into the database! status - {}",
								Constant.NOT_FOUND);
					}
				}

				/* For replay value should be like status = 1 */
				else if (supportRequestPayload.getId() != 0 && supportRequestPayload.getStatus() == 1) {
					optionalSupport = supportRepository.findById(supportRequestPayload.getId());
					if (!optionalSupport.isEmpty()) {
						support = optionalSupport.get();
						if (support.getStatus() != 0) {
							if (support.getStatus() != 2) {
//								supportRequestPayload.setStatus(support.getStatus());
//								String message = HtmlTemplate.replayOnTicketTemplate(user.getEmail(),
//										supportRequestPayload);
//								SendMail.sendMailTemplate(user.getEmail(), "Ticket Status!!", message);
//								log.info("------------- Mail sent To : " + user.getEmail());

								map.put(Constant.RESPONSE_CODE, Constant.OK);
								map.put(Constant.MESSAGE, Constant.REPLIED_SUCCESS_MESSAGE);
								log.info("Replied to the user successfully! status - {}", Constant.OK);
							} else {
								map.put(Constant.RESPONSE_CODE, Constant.CONFLICT);
								map.put(Constant.MESSAGE, Constant.TICKET_ALREADY_CLOSED_MESSAGE);
								log.info("This ticket already closed! status - {}", Constant.CONFLICT);
							}
						} else {
							map.put(Constant.RESPONSE_CODE, Constant.CONFLICT);
							map.put(Constant.MESSAGE, Constant.TICKET_ALREADY_DELETED_MESSAGE);
							log.info("This ticket already deleted! status - {}", Constant.CONFLICT);
						}
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
						map.put(Constant.MESSAGE, Constant.FAILED_TO_REPLAY_MESSAGE);
						log.info(
								"Failed to replay to the user because, given id not found into the database! status - {}",
								Constant.NOT_FOUND);
					}
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
					map.put(Constant.MESSAGE, Constant.FAILED_TO_PERFORM_ACTION_ID_STATUS_MESSAGE);
					log.info(
							"Failed to perform an action because, given id can't be zero or null and status should be 1 or 2! status - {}",
							Constant.BAD_REQUEST);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
				map.put(Constant.MESSAGE, Constant.FAILED_TO_PERFORM_ACTION_MESSAGE);
				log.info("Failed to perform an action because, given userId not found into the database! status - {}",
						Constant.NOT_FOUND);
			}
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
		}
		return map;
	}

	@Override
	public Map<String, Object> getAllTickets(SearchRequestPayload searchRequestPayload) {
		Map<String, Object> map = new HashMap<>();
		try {
			List<SupportResponsePayload> supportResponsePayloadList = new ArrayList<>();
			SupportResponsePage supportResponsePage = new SupportResponsePage();
			List<Support> supportList = new ArrayList<>();
			Page<Support> page = null;
//			if (searchRequestPayload.getPageSize() >= 1) {

			if (searchRequestPayload.getPageSize() != 0) {
				Pageable pageable = PageRequest.of(searchRequestPayload.getPageIndex(),
						searchRequestPayload.getPageSize(), Sort.by("id").descending());
				if (searchRequestPayload.getKeyWord() != "" && !searchRequestPayload.getKeyWord().isBlank()) {
					page = supportRepository.getAllTickets(searchRequestPayload.getKeyWord(), pageable);
				} else {
					page = supportRepository.getAllTickets(pageable);
				}
			} else {
				if (searchRequestPayload.getKeyWord() != null && !searchRequestPayload.getKeyWord().isBlank()) {
					supportList = supportRepository.getAllTickets(searchRequestPayload.getKeyWord());
				} else {
					supportList = supportRepository.getAllTickets();
				}
			}

			if (page != null && page.getContent().size() > Constant.ZERO) {
				supportList = page.getContent();

			}

			for (Support support : supportList) {
				SupportResponsePayload supportResponsePayload = new SupportResponsePayload();
				BeanUtils.copyProperties(support, supportResponsePayload);
				supportResponsePayload.setUserId(support.getUser().getId());
				supportResponsePayload.setPhoneNo(support.getUser().getPhoneNo());
				supportResponsePayload.setCreationDate(DateUtil.convertDateToStringDateTime(support.getCreationDate()));
				supportResponsePayload.setUpdationDate(DateUtil.convertDateToStringDateTime(support.getUpdationDate()));
				supportResponsePayloadList.add(supportResponsePayload);
			}
			supportResponsePage.setSupportResponsePayloadList(supportResponsePayloadList);
			supportResponsePage.setPageIndex(page != null ? page.getNumber() : null);
			supportResponsePage.setPageSize(page != null ? page.getSize() : null);
			supportResponsePage
					.setTotalElement(page != null ? page.getTotalElements() : supportResponsePayloadList.size());
			supportResponsePage.setTotalPages(page != null ? page.getTotalPages() : null);
			supportResponsePage.setIsLastPage(page != null ? page.isLast() : null);
			supportResponsePage.setIsFirstPage(page != null ? page.isFirst() : null);

			map.put(Constant.RESPONSE_CODE, Constant.OK);
			map.put(Constant.MESSAGE, Constant.RECORD_FOUND_MESSAGE);
			map.put(Constant.DATA, supportResponsePage);
			log.info("Record found! status - {}", supportResponsePage);
//				else {
//					map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
//					map.put(Constant.MESSAGE, Constant.RECORD_NOT_FOUND_MESSAGE);
//					map.put(Constant.DATA, supportResponsePage);
//					log.info("Record not found! status - {}", supportResponsePage);
//				}
//			} else {
//				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
//				map.put(Constant.MESSAGE, Constant.PAGE_SIZE_MESSAGE);
//				log.info("Page size can't be less then one! status - {}", searchRequestPayload.getPageSize());
//			}
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		} catch (Exception e) {
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
			log.error("Exception : " + e.getMessage());
		}
		return map;
	}

	@Override
	public Map<String, Object> raiseTicket(SupportRequestPayload supportRequestPayload) {
		Map<String, Object> map = new HashMap<>();
		try {
			Support support = new Support();
			SupportResponsePayload supportResponsePayload = new SupportResponsePayload();
			User user = new User();
			user = userRepository.findByIdAndStatus(supportRequestPayload.getUserId(), Constant.ONE);

			/* Create the ticket || values should be like id=0, status=1 */
			if (user != null) {
				log.info("User found! status - {}", Constant.OK);
				BeanUtils.copyProperties(supportRequestPayload, support);
				support.setCreationDate(new Date());
				support.setStatus(supportRequestPayload.getStatus());
				support.setUser(user);
				support = supportRepository.save(support);

				BeanUtils.copyProperties(support, supportResponsePayload);
				supportResponsePayload.setPhoneNo(support.getUser().getPhoneNo());
				supportResponsePayload.setUserId(support.getUser().getId());
				supportResponsePayload.setCreationDate(DateUtil.convertDateToStringDateTime(support.getCreationDate()));
				supportResponsePayload.setUpdationDate(DateUtil.convertDateToStringDateTime(support.getUpdationDate()));

				map.put(Constant.RESPONSE_CODE, Constant.OK);
				map.put(Constant.MESSAGE, Constant.TICKET_GENERATED_SUCCESS_MESSAGE);
				map.put(Constant.DATA, supportResponsePayload);
				log.info("Ticket generated successfully! status - {}", supportResponsePayload);
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
				map.put(Constant.MESSAGE, Constant.TICKET_NOT_GENERATED_MESSAGE);
				log.info("You can not raise ticket because, given userId not found into the database! status - {}",
						Constant.NOT_FOUND);
			}
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			map.put(Constant.RESPONSE_CODE, Constant.DB_CONNECTION_ERROR);
			map.put(Constant.MESSAGE, Constant.NO_SERVER_CONNECTION);
			log.error("Exception : " + e.getMessage());
		} catch (Exception e) {
			log.error("Exception : " + e.getMessage());
			map.put(Constant.RESPONSE_CODE, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.SERVER_MESSAGE);
		}
		return map;
	}

	@Override
	public List<Support> getAllSupportDetails(@Valid SearchRequestPayload searchRequestPayload) {
		List<Support> supportlist = new ArrayList<>();
		if (searchRequestPayload.getKeyWord() != "" && searchRequestPayload.getKeyWord() != null) {
			supportlist = supportRepository.getAllTickets(searchRequestPayload.getKeyWord());
		} else {
			supportlist = supportRepository.getAllTickets();
		}
		return supportlist;

	}

}