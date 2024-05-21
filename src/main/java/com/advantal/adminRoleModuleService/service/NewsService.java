package com.advantal.adminRoleModuleService.service;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.web.multipart.MultipartFile;

import com.advantal.adminRoleModuleService.models.News;
import com.advantal.adminRoleModuleService.requestpayload.SearchRequestPayload;



public interface NewsService {

//	Map<String, Object> addNews(NewsRequestPayload newsRequestPayload, MultipartFile file);

	Map<String, Object> addNews(String newsRequestPayload, MultipartFile file);

	//Map<String, Object> getAllNews(Integer pageIndex, Integer pageSize, String keyWord);

	Map<String, Object> getAllNews(Integer pageIndex, Integer pageSize, String keyWord);

	Map<String, Object> deleteNews(Long id);

	List<News> getAllNewsDetails(String keyWord);
	



}
