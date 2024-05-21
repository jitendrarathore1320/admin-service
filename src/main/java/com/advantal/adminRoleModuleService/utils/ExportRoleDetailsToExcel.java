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

import com.advantal.adminRoleModuleService.models.Role;

public class ExportRoleDetailsToExcel {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<Role> roleList;

	public ExportRoleDetailsToExcel(List<Role> roleList) {
		this.roleList = roleList;
		workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("Role");

		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);

		createCell(row, 0, "Sr.", style);
		createCell(row, 1, "RoleName", style);
		createCell(row, 2, "RoleDescription", style);
		createCell(row, 3, "EntryDate", style);
		createCell(row, 4, "UpdationDate", style);
		createCell(row, 5, "Status", style);
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
		for (Role role : roleList) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;
			i = i + 1;
			createCell(row, columnCount++, i, style);
			createCell(row, columnCount++, role.getRoleName(), style);
			createCell(row, columnCount++, role.getRoleDescription()!=null && !role.getRoleDescription().isBlank() ? role.getRoleDescription() : "Nill",
					style);
			if (role.getEntryDate().toString() != null) {
				createCell(row, columnCount++, DateUtil.convertDateToStringDateTime(role.getEntryDate()), style);
			} else {
				createCell(row, columnCount++, "0000-00-00 00:00:00", style);
			}
			if (role.getUpdationDate().toString() != null) {
				createCell(row, columnCount++, DateUtil.convertDateToStringDateTime(role.getUpdationDate()), style);
			} else {
				createCell(row, columnCount++, "0000-00-00 00:00:00", style);
			}
			if (role.getStatus().equals(Constant.ONE)) {
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
