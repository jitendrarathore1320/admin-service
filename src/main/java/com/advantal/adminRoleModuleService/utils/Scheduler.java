package com.advantal.adminRoleModuleService.utils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.advantal.adminRoleModuleService.models.Admin;
import com.advantal.adminRoleModuleService.repository.AdminRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Scheduler {

	@Autowired
	private AdminRepository adminRepository;

//	@Scheduled(fixedRate = 86400000) // 300000=5min//600000=10min // 1week=604800000 mil.sec.
	@Scheduled(cron = " 00 00 09 * * ? ") // 1week=604800000 mil.sec.
//	@PostConstruct
	public void ResetPasswordNotificationMailSender() throws ParseException {
		log.info("------------- Scheduler started------------");
		List<Admin> adminList = new ArrayList<>();
		adminList = adminRepository.findAllByStatus(Constant.ONE);
		if (!adminList.isEmpty()) {
			for (Admin admin : adminList) {
				long time=0L, time2 = 0L;
				if (admin.getPasswordUpdationDate() != null) {
					time=DateUtil.getMintue(admin.getPasswordUpdationDate());
				}else if(admin.getUpdationDate()!=null){
					time=DateUtil.getMintue(admin.getUpdationDate());
				}else {
					time=DateUtil.getMintue(admin.getEntryDate());
				}
				time2 = TimeUnit.MILLISECONDS.toMinutes(90*24*60*60*1000);// 90Days;
				if (time >= TimeUnit.MILLISECONDS.toMinutes(90*24*60*60*1000) && !admin.getRole().getRoleName().equalsIgnoreCase("super admin")) {
					admin.setStatus(Constant.TWO);
					admin.setPasswordUpdationDate(new Date());
					adminRepository.save(admin);
					log.info(
							"You have not updated your password, so your account has been blocked without creating new password you can not access your account!! status - {}",
							Constant.OK);

					String link = Constant.CHANGE_PASSWORD_PAGE_LINK;
					String message = HtmlTemplate.accountBlockedReminderTemplate(admin.getName(), link);
					SendMail.sendMailTemplate(admin.getEmail(), "Reminder! your account has been blocked!!", message);
					log.info("------------- Mail sent To : " + admin.getEmail());
				} else if (time >= time2) {
					String link = Constant.CHANGE_PASSWORD_PAGE_LINK;
					String message = HtmlTemplate.forgetPasswordTemplate(admin.getName(), link);
					SendMail.sendMailTemplate(admin.getEmail(),
							"Reminder! You must have to reset your Password before password expired!!", message);
					log.info("------------- Mail sent To : " + admin.getEmail());
				}
			}
		}
	}
	
}
