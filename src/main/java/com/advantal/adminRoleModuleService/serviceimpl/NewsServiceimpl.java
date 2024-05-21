package com.advantal.adminRoleModuleService.serviceimpl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.advantal.adminRoleModuleService.models.News;
import com.advantal.adminRoleModuleService.repository.NewsRepository;
import com.advantal.adminRoleModuleService.requestpayload.NewsRequestPayload;
import com.advantal.adminRoleModuleService.responsepayload.NewsResponsePayload;
import com.advantal.adminRoleModuleService.responsepayload.NewsResponsePage;
import com.advantal.adminRoleModuleService.service.NewsService;
import com.advantal.adminRoleModuleService.utils.Constant;
import com.advantal.adminRoleModuleService.utils.DateUtil;
import com.advantal.adminRoleModuleService.utils.FileUploader;
import com.advantal.adminRoleModuleService.utils.NullCheckUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NewsServiceimpl implements NewsService {

	@Autowired
	private NewsRepository newsRepository;

	@Value("${spring.imagedir}")
	private String imageDir;

	@Value("${spring.baseurl}")
	private String baseUrl;

	@Override
	public Map<String, Object> addNews(String newsRequestPayload, MultipartFile file) {
		Map<String, Object> map = new HashMap<>();
		News oldNews = new News();
		String imagePath = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			NewsRequestPayload requestPayload = objectMapper.readValue(newsRequestPayload, NewsRequestPayload.class);

			map = NullCheckUtil.checkNewsRequestPayloadIsNull(requestPayload);
			if (!map.isEmpty()) {
				return map;
			} else {
				if (requestPayload.getId() != 0) {
					/* here news is updating */
					oldNews = newsRepository.findByIdAndStatus(requestPayload.getId(), Constant.ONE);
					if (oldNews != null) {
						log.info("Record found! status - {}", oldNews);
						oldNews.setTitle(requestPayload.getTitle());
						oldNews.setSubTitle(requestPayload.getSubTitle());
						oldNews.setDescription(requestPayload.getDescription());
						oldNews.setUpdationDate(new Date());
						oldNews.setStatus(Constant.ONE);
						newsRepository.save(oldNews);

						if (file != null) {
							imagePath = FileUploader.uploadImage(file, imageDir, oldNews.getId()).toString();
							if (imagePath != null) {
								imagePath = baseUrl + imagePath;
								log.info("Image uploaded successfully! status - {}", baseUrl + imagePath);
								oldNews.setImageUrl(imagePath);
								newsRepository.save(oldNews);
							}
						}

						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.NEWS_UPDATED_MESSAGE);
						map.put(Constant.DATA, oldNews);
						log.info("News updated successfully! status - {}", oldNews);
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
						map.put(Constant.MESSAGE, Constant.NEWS_NOT_UPDATED_MESSAGE);
						log.info("News not updated because, given id not found into the database! status - {}",
								Constant.NOT_FOUND);
					}
				} else {
					/* creating news here */
					News news = new News();
					news.setTitle(requestPayload.getTitle());
					news.setSubTitle(requestPayload.getSubTitle());
					news.setDescription(requestPayload.getDescription());
					news.setCreationDate(new Date());
					news.setStatus(Constant.ONE);
					newsRepository.save(news);

					if (file != null) {
						imagePath = FileUploader.uploadImage(file, imageDir, news.getId()).toString();
						if (imagePath != null) {
							imagePath = baseUrl + imagePath;
							log.info("Image uploaded successfully! status - {}", baseUrl + imagePath);
							news.setImageUrl(imagePath);
							newsRepository.save(news);
						}
					}

					map.put(Constant.RESPONSE_CODE, Constant.OK);
					map.put(Constant.MESSAGE, Constant.NEWS_CREATED_MESSAGE);
					map.put(Constant.DATA, news);
					log.info("News created successfully! status - {}", Constant.OK);
				}
			}

		} catch (JsonParseException e) {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, "Source JSON not parsable");
			log.info("Source JSON not parsable ! status - {}", Constant.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Map<String, Object> getAllNews(Integer pageIndex, Integer pageSize, String keyWord) {
		Map<String, Object> map = new HashMap<>();
		try {
			// List<News> newsList = new ArrayList<>();
			List<NewsResponsePayload> newsReponcePayloadList = new ArrayList<>();
			NewsResponsePage newsResponsePage = new NewsResponsePage();
			List<News> newsList = new ArrayList<>();
			Page<News> page = null;
			if (pageSize >= 1) {
				Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.by("id").descending());
				if (keyWord != "" && keyWord != null) {
					page = newsRepository.findAllNews(keyWord, pageable);
				} else {
					page = newsRepository.findAllNews(pageable);
				}
				if (page != null && page.getContent().size() > Constant.ZERO) {
					newsList = page.getContent();
//					newsList = newsRepository.findAllByStatusOrderByCreationDateDesc(Constant.ONE);
					if (!newsList.isEmpty()) {
						for (News news : newsList) {
							NewsResponsePayload newsResponcePayload = new NewsResponsePayload();
							BeanUtils.copyProperties(news, newsResponcePayload);
							newsResponcePayload
									.setCreationDate(DateUtil.convertDateToStringDateTime(news.getCreationDate()));
							newsResponcePayload
									.setUpdationDate(DateUtil.convertDateToStringDateTime(news.getUpdationDate()));
							newsReponcePayloadList.add(newsResponcePayload);
						}
						System.out.println("List" + newsReponcePayloadList);
						newsResponsePage.setNewsResponsePayloadList(newsReponcePayloadList);
						newsResponsePage.setPageIndex(page.getNumber());
						newsResponsePage.setPageSize(page.getSize());
						newsResponsePage.setTotalElement(page.getTotalElements());
						newsResponsePage.setTotalPages(page.getTotalPages());
						newsResponsePage.setIsLastPage(page.isLast());
						newsResponsePage.setIsFirstPage(page.isFirst());
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.NEWS_LIST_FOUND_MESSAGE);
						map.put(Constant.DATA, newsResponsePage);
						log.info("News list found! status - {}", newsResponsePage);
					} else {
						map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
						map.put(Constant.MESSAGE, Constant.NEWS_LIST_EMPTY_MESSAGE);
						log.info("News list empty! status - {}", Constant.NOT_FOUND);
					}

				}
			}
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
	public Map<String, Object> deleteNews(Long id) {
		Map<String, Object> map = new HashMap<>();
		Optional<News> optionalNews = Optional.empty();
		try {
			if (id != null) {
				optionalNews = newsRepository.findById(id);
				if (optionalNews != null) {
					News news = optionalNews.get();
					if (news.getStatus().equals(Constant.ZERO)) {
						map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
						map.put(Constant.MESSAGE, Constant.ALREADY_DELETED_MESSAGE);
						log.info("Already deleted! status - {}", Constant.CONFLICT);
					} else {
						/* ----- Perform delete operation ----- */
						news.setStatus(Constant.ZERO);
						news.setUpdationDate(new Date());
						news = newsRepository.save(news);
						map.put(Constant.RESPONSE_CODE, Constant.OK);
						map.put(Constant.MESSAGE, Constant.DELETED_MESSAGE);
						log.info("Deleted successfully! status - {}", Constant.OK);
					}
				} else {
					map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
					map.put(Constant.MESSAGE, Constant.ID_NOT_FOUND_MESSAGE);
					log.info("Given id not found into the database! status - {}", Constant.NOT_FOUND);
				}
			} else {
				map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
				map.put(Constant.MESSAGE, Constant.ID_CAN_NOT_NULL_MESSAGE);
				log.info("Id can not null, it should be valid! status - {}", Constant.BAD_REQUEST);
			}

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
	public List<News> getAllNewsDetails(String keyWord) {
		List<News> newslist = new ArrayList<>();
		if (keyWord != "" && keyWord != null) {
			newslist = newsRepository.findAllNewsWithSeraching(keyWord);
		} else {
			newslist = newsRepository.findAllNews();
		}
		return newslist;
	}
}