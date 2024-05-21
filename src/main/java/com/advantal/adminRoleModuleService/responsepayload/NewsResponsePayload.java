package com.advantal.adminRoleModuleService.responsepayload;

import java.util.Date;

import lombok.Data;
@Data
public class NewsResponsePayload {
	
	private Long id;
	
	private String title;

	private String subTitle;
	
	private String description;

//	private String content;

	private String imageUrl;

	private String creationDate;

	private String updationDate;

	private Short status;

}
