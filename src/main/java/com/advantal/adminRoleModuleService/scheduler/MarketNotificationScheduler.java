package com.advantal.adminRoleModuleService.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.advantal.adminRoleModuleService.requestpayload.PushNotificationRequestPayload;
import com.advantal.adminRoleModuleService.service.DataProcessUnit;
import com.advantal.adminRoleModuleService.service.PushNotificationService;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.annotation.PostConstruct;

@Component
public class MarketNotificationScheduler {

	@Autowired
	private PushNotificationService pushNotificationService;

	@Autowired
	private DataProcessUnit dataProcessUnit;

	// US market opening time (9:30 AM EST)
	@Scheduled(cron = "0 30 9 * * MON-FRI", zone = "America/New_York")
	public void sendUSMarketOpeningNotification() {
		if (isWeekday()) {
			dataProcessUnit.USMarketOpen();
		}
	}

	// US market closing time (4:00 PM EST)
	@Scheduled(cron = "0 0 16 * * MON-FRI", zone = "America/New_York")
	public void sendUSMarketClosingNotification() {
		if (isWeekday()) {
			dataProcessUnit.USMarketClose();
		}

	}

	// Saudi market opening time (10:00 AM AST)
	@Scheduled(cron = "0 0 10 * * SUN-THU", zone = "Asia/Riyadh")
	public void sendSaudiMarketOpeningNotification() {
		if (isWeekday()) {
			dataProcessUnit.SaudiMarketOpen();
		}
	}

	// Saudi market closing time (3:00 PM AST)
	@Scheduled(cron = "0 0 15 * * SUN-THU", zone = "Asia/Riyadh")
	public void sendSaudiMarketClosingNotification() {
		if (isWeekday()) {
			dataProcessUnit.SaudiMarketClose();
		}
	}

	private boolean isWeekday() {
		DayOfWeek dayOfWeek = ZonedDateTime.now(ZoneId.of("America/New_York")).getDayOfWeek();
		return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
	}
}
