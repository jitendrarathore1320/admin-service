package com.advantal.adminRoleModuleService.utils;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.advantal.adminRoleModuleService.models.Admin;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExportAdminToPdf {
	private List<Admin> adminList;

//	private RestTemplate restTemplate;

	public ExportAdminToPdf(List<Admin> adminList) {
		this.adminList = adminList;
	}

	private void writeTableHeader(PdfPTable table) {

		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.lightGray);
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setColor(Color.black);
		font.setSize(9);

		cell.setPhrase(new Phrase("Sr.", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Name", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Mobile", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Email", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("RoleName", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Country", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("City", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("PinCode", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Address", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("EntryDate", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("UpdationDate", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("PasswordUpdationDate", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Status", font));
		table.addCell(cell);

	}

	private void writeTableData(PdfPTable table) {
		int i = 0;
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.black);
		font.setSize(9);

		PdfPCell cell = new PdfPCell();
		for (Admin admin : adminList) {
			i = i + 1;
			cell.setPhrase(new Phrase(String.valueOf(i), font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(admin.getName(), font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(admin.getMobile(), font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(admin.getEmail(), font));
			table.addCell(cell);

			cell.setPhrase(new Phrase(admin.getRole().getRoleName(), font));
			table.addCell(cell);

			if(admin.getCountry()!=null && !admin.getCountry().isBlank()) {
				cell.setPhrase(new Phrase(admin.getCountry(), font));
				table.addCell(cell);
			}else {
				cell.setPhrase(new Phrase("Nill", font));
				table.addCell(cell);
			}
			
			if(admin.getCity()!=null && !admin.getCity().isBlank()) {
				cell.setPhrase(new Phrase(admin.getCity(), font));
				table.addCell(cell);
			}else {
				cell.setPhrase(new Phrase("Nill", font));
				table.addCell(cell);
			}
			
			if(admin.getPinCode()!=null && !admin.getPinCode().isBlank()) {
				cell.setPhrase(new Phrase(admin.getPinCode(), font));
				table.addCell(cell);
			}else {
				cell.setPhrase(new Phrase("Nill", font));
				table.addCell(cell);
			}
			
			if(admin.getAddress()!=null && !admin.getAddress().isBlank()) {
				cell.setPhrase(new Phrase(admin.getAddress(), font));
				table.addCell(cell);
			}else {
				cell.setPhrase(new Phrase("Nill", font));
				table.addCell(cell);
			}

			cell.setPhrase(new Phrase(DateUtil.convertDateToStringDateTime(admin.getEntryDate()), font));
			table.addCell(cell);

			if (admin.getUpdationDate() != null) {
				cell.setPhrase(new Phrase(DateUtil.convertDateToStringDateTime(admin.getUpdationDate()), font));
				table.addCell(cell);
			} else {
				cell.setPhrase(new Phrase("0000-00-00 00:00:00", font));
				table.addCell(cell);
			}

			if (admin.getPasswordUpdationDate() != null) {
				cell.setPhrase(new Phrase(DateUtil.convertDateToStringDateTime(admin.getPasswordUpdationDate()), font));
				table.addCell(cell);
			} else {
				cell.setPhrase(new Phrase("0000-00-00 00:00:00", font));
				table.addCell(cell);
			}

			if (admin.getStatus().equals(Constant.ONE)) {
				cell.setPhrase(new Phrase("Active", font));
				table.addCell(cell);
			} else {
				cell.setPhrase(new Phrase("Inactive", font));
				table.addCell(cell);
			}
		}
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
//
//		String filename;
//		Image img = null;
//		if (DiskPathStoreServiceImpl.isUnix()) {
////			filename = "/home/shieldcrypt/packages/tomcat/webapps/smsc-api-gui/WEB-INF/classes/logo/iaf-logo.png";
//			img = Image.getInstance(filename);
//			log.info("<<<<<<<<<< Image >>>> is set on linux os >>>>>> : " + filename);
//		} else if (DiskPathStoreServiceImpl.isWindows()) {
//			filename = ResourceUtils.getFile("src\\main\\resources\\logo\\amwalLogo.PNG").getPath();
//			img = Image.getInstance(filename);
//			log.info("<<<<<<<<<< Image >>>> is set on windows os >>>>>> : " + filename);
//		}
//		img.scaleToFit(100, 100);
//		img.setAlignment(Image.MIDDLE);
//		img.setAlignment(Image.TOP);
//		document.add(img);
//		log.info("<<<<<<<<<< Added >>>> image in document  >>>>>> : " + document);

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(12);
		font.setColor(Color.BLACK);

		Paragraph p = new Paragraph("List of Admin Details", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(p);

		PdfPTable table = new PdfPTable(13);
		table.setWidthPercentage(100f);
//		table.setWidths(new float[] { 2.5f, 4.1f, 4.1f, 4.1f, 3.5f, 4.5f, 3.7f, 5.1f, 5.2f, 4.9f,4.9f,4.9f,4.1f });
		table.setSpacingBefore(10);
		writeTableHeader(table);
		writeTableData(table);
		document.add(table);
		document.close();
	}
}