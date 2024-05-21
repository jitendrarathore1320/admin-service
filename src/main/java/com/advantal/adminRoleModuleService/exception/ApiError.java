package com.advantal.adminRoleModuleService.exception;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ApiError {

	private HttpStatus statusMessage;
	private String statusCode;
	private List<String> errorList;
	private Date date;
	private String pathUrl;
		
}
