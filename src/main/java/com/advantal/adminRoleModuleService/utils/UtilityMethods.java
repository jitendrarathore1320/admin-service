package com.advantal.adminRoleModuleService.utils;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.advantal.adminRoleModuleService.models.Admin;
import com.advantal.adminRoleModuleService.models.Otp;
import com.advantal.adminRoleModuleService.repository.OtpRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UtilityMethods {
	
	@Autowired
	private OtpRepository otpRepository;

	public static Integer randomNumber() {
		Random random = new Random();
		return random.nextInt(1000000);
	}

	public static File createFileDirectory() {
		File file = null;
		try {
			File fileDir = new File("C:\\Users\\hp\\Downloads");
			String fileName = "language_file";
			file = new File(fileDir + "\\" + fileName + ".xlsx");
			if (!fileDir.exists()) {
				if (fileDir.mkdir()) {
					log.info("New directory created into your local system! status - {}", fileDir);
				}
				if (!file.exists()) {
					file.createNewFile();
					log.info("New file created into your local system! status - {}", file);
				} else
					log.info("File already exist into your local system! status - {}", file);
			} else {
				log.info("Directory found into your local system! status - {}", fileDir);
				if (!file.exists()) {
					file.createNewFile();
					log.info("New file created into your local system! status - {}", file);
				} else
					log.info("File already exist into your local system! status - {}", file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}

	public static Integer sendOtp(Integer oldOtp) {
		Integer otp = oldOtp;
		return otp;
	}

//	public static void main(String args[]) {
//		WebClient webClient = WebClient.builder()
//				.clientConnector(new ReactorClientHttpConnector(HttpClient.create(ConnectionProvider.newConnection())))
//				.build();
//
//		// String apiResponse="Statuscode=200";
//		String apiResponse = webClient.get()
//				.uri("https://api.twelvedata.com/profile?symbol=AAPL&apikey=44969d2137e84e798fd38caa661b20d4")
////				.header("Authorization",
////						"Basic STYzU2U2TzVNZ2FaeFcwYWgycnogQU5kOkt4bGlqTUZZcnFoQUkwVHlucnJvbElwczZFRUFxN2lrcFFRaktTTEc=")
//				.exchangeToMono(response -> {
//					return response.bodyToMono(String.class);
//				}).block();
//		System.out.println(apiResponse.toString());
//		Gson gson=new Gson();
//		CompanayProfile companayProfile=new CompanayProfile();
//		
//		companayProfile=gson.fromJson(apiResponse, CompanayProfile.class);
//		System.out.println(companayProfile.toString());
//		System.out.println();
//		System.out.println("About companay: "+companayProfile.getDescription());
//	}

	public String sendOtp(Admin admin) {
		String str = "";
		Otp oldotp = new Otp();
		Otp newotp = new Otp();
		Map<String, Object> map = new HashMap<>();
		try {
			String otpstr = RandomStringGenerator.getRandomNumberString(6);
			// encrypt OTP first
//				otpstr = EncryptDecryptUtil.encrypt(otpstr);
			// save otp
			oldotp = otpRepository.findByAdminIdFk(admin.getId());
			if (oldotp != null) {
				oldotp.setOtp(otpstr);
				oldotp.setCreationDate(new Date());
				oldotp.setStatus(Constant.ZERO);
				oldotp = otpRepository.save(oldotp);
				str = oldotp.getOtp();
			} else {
				newotp.setAdminIdFk(admin.getId());
				newotp.setOtp(otpstr);
				newotp.setCreationDate(new Date());
				newotp.setStatus(Constant.ZERO);
				newotp = otpRepository.save(newotp);
				str = newotp.getOtp();
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put(Constant.HTTP_STATUS, Constant.SERVER_ERROR);
			map.put(Constant.MESSAGE, Constant.PLEASE_TRY_AGAIN);
		}
		return str;
	}
}
