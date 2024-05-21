package com.advantal.adminRoleModuleService.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUploader {
	public static String uploadLanguageFile(MultipartFile mulfile, String path) {
		String filename = "";
		if (!mulfile.isEmpty()) {
			Long timestamp = System.currentTimeMillis();
			filename = mulfile.getOriginalFilename();
			String extension = FilenameUtils.getExtension(filename);
//			filename = timestamp.toString()+"_"+ filename.concat("."+extension);
//			filename = filename.concat("."+extension);
			try {
				byte[] bytes = mulfile.getBytes();
				BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(path + filename, true));
				buffStream.write(bytes);
				buffStream.close();
//				return path + filename;
				return filename;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return filename;
	}

	public static StringBuilder uploadImage(MultipartFile mulfile, String path, Long id) {
		StringBuilder fileName = new StringBuilder();
		try {
			if (!mulfile.isEmpty()) {
				File fileDir = new File(path);
				String name = "amwal_news_" + id.toString() + "."
						+ FilenameUtils.getExtension(mulfile.getOriginalFilename());
				
				File file = new File(fileDir + "\\" + name);
				if (file.exists()) {
					log.info(name + " : named image already available into the server! status - {}", Constant.OK);
					file.delete();
					log.info(name + " : named image deleted from the server! status - {}", Constant.OK);
				}
				fileName.append("amwal_news_" + id);
				fileName.append("." + FilenameUtils.getExtension(mulfile.getOriginalFilename()));
				try {
					byte[] bytes = mulfile.getBytes();
					BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(path + name, true));
					buffStream.write(bytes);
					buffStream.close();
					return fileName;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
		
	}
	
	public static String uploadBrokerImage(MultipartFile mulfile, String path, Long id) {
		

		String filename = "";

		if (!mulfile.isEmpty()) {
			filename = mulfile.getOriginalFilename();
			filename = id + "_" + filename;

			try {
				// Delete all existing files associated with the userid
				File userDir = new File(path);
				File[] existingFiles = userDir.listFiles((dir, name) -> name.startsWith(id + "_"));

				if (existingFiles != null) {
					for (File existingFile : existingFiles) {
						existingFile.delete();
					}
				}

				byte[] bytes = mulfile.getBytes();
				BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(path + filename, true));
				buffStream.write(bytes);
				buffStream.close();

				return filename;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return filename;
	
		
//		StringBuilder fileName = new StringBuilder();
//		try {
//			if (!mulfile.isEmpty()) {
//				File fileDir = new File(path);
//				String name = "amwal_broker_" + id.toString() + "."
//						+ FilenameUtils.getExtension(mulfile.getOriginalFilename());
//				
//				File file = new File(fileDir + "\\" + name);
//				if (file.exists()) {
//					log.info(name + " : named image already available into the server! status - {}", Constant.OK);
//					file.delete();
//					log.info(name + " : named image deleted from the server! status - {}", Constant.OK);
//				}
//				fileName.append("amwal_broker_" + id);
//				fileName.append("." + FilenameUtils.getExtension(mulfile.getOriginalFilename()));
//				try {
//					byte[] bytes = mulfile.getBytes();
//					BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(path + name, true));
//					buffStream.write(bytes);
//					buffStream.close();
//					return fileName;
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return fileName;
		
	}

}
