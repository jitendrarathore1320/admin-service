package com.advantal.adminRoleModuleService.utils;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.advantal.adminRoleModuleService.models.Admin;

public class ExportAdminDetailsToExcel {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<Admin> adminList;

	public ExportAdminDetailsToExcel(List<Admin> adminList) {
		this.adminList = adminList;
		workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("Admin");

		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);

		createCell(row, 0, "Sr.", style);
		createCell(row, 1, "Name", style);
		createCell(row, 2, "Mobile", style);
		createCell(row, 3, "Email", style);
		createCell(row, 4, "RoleName", style);
		createCell(row, 5, "Country", style);
		createCell(row, 6, "City", style);
		createCell(row, 7, "PinCode", style);
		createCell(row, 8, "Address", style);
		createCell(row, 9, "EntryDate", style);
		createCell(row, 10, "UpdationDate", style);
		createCell(row, 11, "PasswordUpdationDate", style);
		createCell(row, 12, "Status", style);
	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Integer) {
			cell.setCellValue((Integer) value);
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void writeDataLines() {
		int rowCount = 1;

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);
		int i = 0;
		for (Admin admin : adminList) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			i = i + 1;
			createCell(row, columnCount++, i, style);
			createCell(row, columnCount++, admin.getName(), style);
			createCell(row, columnCount++, admin.getMobile(), style);
			createCell(row, columnCount++, admin.getEmail(), style);
			createCell(row, columnCount++, admin.getRole().getRoleName(), style);
			createCell(row, columnCount++,
					admin.getCountry() != null && !admin.getCountry().isBlank() ? admin.getCountry() : "Nill", style);
			createCell(row, columnCount++,
					admin.getCity() != null && !admin.getCity().isBlank() ? admin.getCity() : "Nill", style);
			createCell(row, columnCount++,
					admin.getPinCode() != null && !admin.getPinCode().isBlank() ? admin.getPinCode() : "Nill", style);
			createCell(row, columnCount++,
					admin.getAddress() != null && !admin.getAddress().isBlank() ? admin.getAddress() : "Nill", style);
			createCell(row, columnCount++,
					admin.getEntryDate() != null ? DateUtil.convertDateToStringDateTime(admin.getEntryDate())
							: "00000-00-00 00:00:00",
					style);

			createCell(row, columnCount++,
					admin.getUpdationDate() != null ? DateUtil.convertDateToStringDateTime(admin.getUpdationDate())
							: "00000-00-00 00:00:00",
					style);
			createCell(row, columnCount++,
					admin.getPasswordUpdationDate() != null
							? DateUtil.convertDateToStringDateTime(admin.getPasswordUpdationDate())
							: "00000-00-00 00:00:00",
					style);

			if (admin.getStatus().equals(Constant.ONE)) {
				createCell(row, columnCount++, "Active", style);
			} else {
				createCell(row, columnCount++, "Inactive", style);
			}
		}
	}

	public void export(HttpServletResponse response) throws IOException {
		writeHeaderLine();
		writeDataLines();

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

}
