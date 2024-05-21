package com.advantal.adminRoleModuleService.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.advantal.adminRoleModuleService.models.Admin;
import com.advantal.adminRoleModuleService.models.News;
import com.advantal.adminRoleModuleService.models.Role;
import com.advantal.adminRoleModuleService.models.Support;
import com.advantal.adminRoleModuleService.requestpayload.SearchRequestPayload;
import com.advantal.adminRoleModuleService.service.AdminService;
import com.advantal.adminRoleModuleService.service.NewsService;
import com.advantal.adminRoleModuleService.service.RoleService;
import com.advantal.adminRoleModuleService.service.SupportService;
import com.advantal.adminRoleModuleService.utils.Constant;
import com.advantal.adminRoleModuleService.utils.ExportAdminDetailsToExcel;
import com.advantal.adminRoleModuleService.utils.ExportAdminToPdf;
import com.advantal.adminRoleModuleService.utils.ExportNewsDetailsToExcel;
import com.advantal.adminRoleModuleService.utils.ExportNewsToPdf;
import com.advantal.adminRoleModuleService.utils.ExportRoleDetailsToExcel;
import com.advantal.adminRoleModuleService.utils.ExportRoleToPdf;
import com.advantal.adminRoleModuleService.utils.ExportSupportDetailsToExcel;
import com.advantal.adminRoleModuleService.utils.ExportSupportToPdf;
import com.lowagie.text.DocumentException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/admin_file")
public class FileController {

	@Autowired
	AdminService adminService;

	@Autowired
	RoleService roleService;

	@Autowired
	NewsService newsService;

	@Autowired
	SupportService supportService;

	@PostMapping("/download_admin_details_pdf")
//	@ApiOperation(value = "Download pdf file for sms details !!")
	public Map<String, String> AdminDetailsExportToPDF(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
			@RequestParam(required = false) String keyWord, HttpServletResponse response)
			throws DocumentException, IOException {
		Map<String, String> map = new HashMap<>();

//		if (searchRequestPayload.getPageSize() > Constant.ZERO) {
		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=admin_details_" + currentDateTime + ".pdf";
		List<Admin> adminList = adminService.getAllAdminDetails(keyWord);

		if (!adminList.isEmpty()) {
			log.info(" record found successfully ! status - {}", Constant.OK + adminList);
			response.setHeader(headerKey, headerValue);
			ExportAdminToPdf exportToPdf = new ExportAdminToPdf(adminList);
			exportToPdf.export(response);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
			map.put(Constant.MESSAGE, Constant.NOT_DOWNLOAD_FILE_MESSAGE);
			log.info("Not able to download the file, because no record found on the database! status - {}",
					Constant.BAD_REQUEST);
		}
//		} else {
//			map.put(Constant.RESPONSE_CODE, Constant.BAD_REQUEST);
//			map.put(Constant.MESSAGE, Constant.PAGE_SIZE_MESSAGE);
//			log.info("Page size can't be less then one! status - {}", searchRequestPayload.getPageSize());
//		}
		return map;
	}

//	@PostMapping("/download_admin_details_excel")
////	@ApiOperation(value = "Download excel file for sms details !!")
//	public Map<String, String> AdminDetailsExportToExcel(@RequestParam Integer pageIndex,
//			@RequestParam Integer pageSize, @RequestParam(required = false) String keyWord,
//			HttpServletResponse response) throws IOException {
//		Map<String, String> map = new HashMap<>();
//		response.setContentType("application/octet-stream");
//		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
//		String currentDateTime = dateFormatter.format(new Date());
//
//		String headerKey = "Content-Disposition";
//		String headerValue = "attachment; filename=admin_details_" + currentDateTime + ".xlsx";
//
//		List<Admin> adminList = adminService.getAllAdminDetails(keyWord);
//		if (!adminList.isEmpty()) {
//			log.info(" record found successfully ! status - {}", Constant.OK + adminList);
//			response.setHeader(headerKey, headerValue);
//			ExportAdminDetailsToExcel exportSmsDetails = new ExportAdminDetailsToExcel(adminList);
//			exportSmsDetails.export(response);
//		} else {
//			map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
//			map.put(Constant.MESSAGE, Constant.NOT_DOWNLOAD_FILE_MESSAGE);
//			log.info("Not able to download the file, because no record found on the database! status - {}",
//					Constant.BAD_REQUEST);
//		}
//		return map;
//	}

	@PostMapping("/download_role_details_pdf")
//	@ApiOperation(value = "Download pdf file for sms details !!")
	public Map<String, String> roleDetailsExportToPDF(@RequestParam(required = false) Integer pageIndex,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String searchText,
			@RequestParam(required = false) String status, HttpServletResponse response)
			throws DocumentException, IOException {
		Map<String, String> map = new HashMap<>();

		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=role_details_" + currentDateTime + ".pdf";
		List<Role> roleList = roleService.getAllRoleDetails(searchText);

		if (!roleList.isEmpty()) {
			log.info(" record found successfully ! status - {}", Constant.OK + roleList);
			response.setHeader(headerKey, headerValue);
			ExportRoleToPdf exportToPdf = new ExportRoleToPdf(roleList);
			exportToPdf.export(response);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
			map.put(Constant.MESSAGE, Constant.NOT_DOWNLOAD_FILE_MESSAGE);
			log.info("Not able to download the file, because no record found on the database! status - {}",
					Constant.BAD_REQUEST);
		}
		return map;
	}

//	@PostMapping("/download_role_details_excel")
////	@ApiOperation(value = "Download excel file for sms details !!")
//	public Map<String, String> roleDetailsExportToExcel(@RequestParam(required = false) Integer pageIndex,
//			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String searchText,
//			@RequestParam(required = false) String status, HttpServletResponse response) throws IOException {
//		Map<String, String> map = new HashMap<>();
//		response.setContentType("application/octet-stream");
//		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
//		String currentDateTime = dateFormatter.format(new Date());
//
//		String headerKey = "Content-Disposition";
//		String headerValue = "attachment; filename=role_details_" + currentDateTime + ".xlsx";
//
//		List<Role> roleList = roleService.getAllRoleDetails(searchText);
//		if (!roleList.isEmpty()) {
//			log.info(" record found successfully ! status - {}", Constant.OK + roleList);
//			response.setHeader(headerKey, headerValue);
//			ExportRoleDetailsToExcel exportSmsDetails = new ExportRoleDetailsToExcel(roleList);
//			exportSmsDetails.export(response);
//		} else {
//			map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
//			map.put(Constant.MESSAGE, Constant.NOT_DOWNLOAD_FILE_MESSAGE);
//			log.info("Not able to download the file, because no record found on the database! status - {}",
//					Constant.BAD_REQUEST);
//		}
//		return map;
//	}

//	@PostMapping("/download_news_details_pdf")
////	@ApiOperation(value = "Download pdf file for sms details !!")
//	public Map<String, String> newsDetailsExportToPDF(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
//			@RequestParam(required = false) String keyWord, HttpServletResponse response)
//			throws DocumentException, IOException {
//		Map<String, String> map = new HashMap<>();
//
//		response.setContentType("application/pdf");
//		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
//		String currentDateTime = dateFormatter.format(new Date());
//
//		String headerKey = "Content-Disposition";
//		String headerValue = "attachment; filename=news_details_" + currentDateTime + ".pdf";
//		List<News> newsList = newsService.getAllNewsDetails(keyWord);
//
//		if (!newsList.isEmpty()) {
//			log.info(" record found successfully ! status - {}", Constant.OK + newsList);
//			response.setHeader(headerKey, headerValue);
//			ExportNewsToPdf exportToPdf = new ExportNewsToPdf(newsList);
//			exportToPdf.export(response);
//		} else {
//			map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
//			map.put(Constant.MESSAGE, Constant.NOT_DOWNLOAD_FILE_MESSAGE);
//			log.info("Not able to download the file, because no record found on the database! status - {}",
//					Constant.BAD_REQUEST);
//		}
//		return map;
//	}
//
//	@PostMapping("/download_news_details_excel")
////	@ApiOperation(value = "Download excel file for sms details !!")
//	public Map<String, String> newsDetailsExportToExcel(@RequestParam Integer pageIndex, @RequestParam Integer pageSize,
//			@RequestParam(required = false) String keyWord, HttpServletResponse response) throws IOException {
//		Map<String, String> map = new HashMap<>();
//		response.setContentType("application/octet-stream");
//		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
//		String currentDateTime = dateFormatter.format(new Date());
//
//		String headerKey = "Content-Disposition";
//		String headerValue = "attachment; filename=news_details_" + currentDateTime + ".xlsx";
//
//		List<News> newsList = newsService.getAllNewsDetails(keyWord);
//		if (!newsList.isEmpty()) {
//			log.info(" record found successfully ! status - {}", Constant.OK + newsList);
//			response.setHeader(headerKey, headerValue);
//			ExportNewsDetailsToExcel exportSmsDetails = new ExportNewsDetailsToExcel(newsList);
//			exportSmsDetails.export(response);
//		} else {
//			map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
//			map.put(Constant.MESSAGE, Constant.NOT_DOWNLOAD_FILE_MESSAGE);
//			log.info("Not able to download the file, because no record found on the database! status - {}",
//					Constant.BAD_REQUEST);
//		}
//		return map;
//	}

	@PostMapping("/download_support_details_pdf")
//	@ApiOperation(value = "Download pdf file for sms details !!")
	public Map<String, String> supportDetailsExportToPDF(@RequestBody @Valid SearchRequestPayload searchRequestPayload,
			HttpServletResponse response) throws DocumentException, IOException {
		Map<String, String> map = new HashMap<>();

		response.setContentType("application/pdf");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=support_details_" + currentDateTime + ".pdf";
		List<Support> supportList = supportService.getAllSupportDetails(searchRequestPayload);

		if (!supportList.isEmpty()) {
			log.info(" record found successfully ! status - {}", Constant.OK + supportList);
			response.setHeader(headerKey, headerValue);
			ExportSupportToPdf exportToPdf = new ExportSupportToPdf(supportList);
			exportToPdf.export(response);
		} else {
			map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
			map.put(Constant.MESSAGE, Constant.NOT_DOWNLOAD_FILE_MESSAGE);
			log.info("Not able to download the file, because no record found on the database! status - {}",
					Constant.BAD_REQUEST);
		}
		return map;
	}

//	@PostMapping("/download_support_details_excel")
////	@ApiOperation(value = "Download excel file for sms details !!")
//	public Map<String, String> supportDetailsExportToExcel(
//			@RequestBody @Valid SearchRequestPayload searchRequestPayload, HttpServletResponse response)
//			throws IOException {
//		Map<String, String> map = new HashMap<>();
//		response.setContentType("application/octet-stream");
//		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
//		String currentDateTime = dateFormatter.format(new Date());
//
//		String headerKey = "Content-Disposition";
//		String headerValue = "attachment; filename=support_details_" + currentDateTime + ".xlsx";
//
//		List<Support> supportList = supportService.getAllSupportDetails(searchRequestPayload);
//		if (!supportList.isEmpty()) {
//			log.info(" record found successfully ! status - {}", Constant.OK + supportList);
//			response.setHeader(headerKey, headerValue);
//			ExportSupportDetailsToExcel exportSmsDetails = new ExportSupportDetailsToExcel(supportList);
//			exportSmsDetails.export(response);
//		} else {
//			map.put(Constant.RESPONSE_CODE, Constant.NOT_FOUND);
//			map.put(Constant.MESSAGE, Constant.NOT_DOWNLOAD_FILE_MESSAGE);
//			log.info("Not able to download the file, because no record found on the database! status - {}",
//					Constant.BAD_REQUEST);
//		}
//		return map;
//	}
}
