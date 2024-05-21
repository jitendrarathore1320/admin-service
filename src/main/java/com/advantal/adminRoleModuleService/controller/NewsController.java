package com.advantal.adminRoleModuleService.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.advantal.adminRoleModuleService.service.NewsService;
import com.advantal.adminRoleModuleService.utils.Constant;

//import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/news")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NewsController {

	@Autowired
	private NewsService newsService;

	@PostMapping(value = "/addNews", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
//	@ApiOperation(value = "we can add and update news!!")
	public Map<String, Object> addNews(@RequestPart String newsRequestPayload,
			@RequestPart(value = "file", required = false) MultipartFile file) {
		Map<String, Object> map = new HashMap<>();
		if (!newsRequestPayload.isBlank()) {
			return newsService.addNews(newsRequestPayload, file);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
			log.info("Bad request! status - {}", Constant.BAD_REQUEST);
			return map;
		}
	}

	@GetMapping(value = "/get_all_news")
	public Map<String, Object> getAllNews(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
			@RequestParam(required = false) String keyWord) {
		Map<String, Object> map = new HashMap<>();
		if (pageIndex != null && pageSize != null) {
			return newsService.getAllNews(pageIndex, pageSize, keyWord);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
			map.put(Constant.MESSAGE, Constant.BAD_REQUEST_MESSAGE);
			log.info("Bad request! status - {}", Constant.BAD_REQUEST);
			return map;
		}
	}

	@GetMapping(value = "/deleteNews")
	public Map<String, Object> deleteNews(@RequestParam(value = "id", required = true) Long id) {
		return newsService.deleteNews(id);
	}

}
