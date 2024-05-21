package com.advantal.adminRoleModuleService.requestpayload;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsRequestPayload {
	@NotNull(message = "Id can't be null !!")
	private Long id;

	@NotEmpty(message = "Title  can't be empty !!")
	@NotNull(message = "Title can't be null !!")
	private String title;

//	@NotEmpty(message = "SubTitle can't be empty !!")
	@NotNull(message = "subTitle can't be null !!")
	private String subTitle;

	@NotEmpty(message = "Description  can't be empty !!")
	@NotNull(message = "Description can't be null !!")
	private String description;

	@NotEmpty(message = "Content  can't be empty !!")
	@NotNull(message = "Content can't be null !!")
	private String content;

}
