package com.advantal.adminRoleModuleService.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.functors.ExceptionClosure;

public class DateUtil {

	public static String convertDateToStringDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = formatter.format(date);
		System.out.println("Date Format with yyyy-MM-dd : " + strDate);
		return strDate;
	}

	public static String convertDateToStringDateTime(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String strDateTime = "null";
		if (date != null) {
			strDateTime = formatter.format(date);
			System.out.println("Date Format with yyyy-MM-dd hh:mm:ss: " + strDateTime);
		}
		return strDateTime;
	}

	public static String convertDOBToAge(String DOB) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate date = LocalDate.parse(DOB, formatter);
		Period period = Period.between(date, LocalDate.now());
		String age = "0";
//		String age = period.getYears() + " Years " + period.getMonths() + " Months";
		if (period.getYears() != 0) {
			age = period.getYears() + " Years ";
		} else if (period.getMonths() != 0) {
			age = period.getMonths() + " Months ";
		} else {
			age = period.getDays() + " Days ";
		}
		return age;
	}

	public static String dateToString(Date date) {
		SimpleDateFormat fromDateFormat = new SimpleDateFormat("ddMMMYY");
		String strDate = fromDateFormat.format(date);
		return strDate;
	}

//	public static long differenceBetweenDate(Date oldDate, LocalDateTime currentDate) throws ParseException {
//		try {
//			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////			Date date = formatter.parse(currentDate);
////			long differenceInTime = date.getTime() - oldDate.getTime();
//			long differenceInMinute = TimeUnit.MILLISECONDS.toMinutes(differenceInTime);
////	long differenceInMinute = differenceInTime/ (1000 * 60);
//			return differenceInMinute;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return 0L;
//		
//	}

	public static long differenceBetweenDate(Date oldDate, LocalDateTime currentDate) throws ParseException {
		try {
			if(oldDate!=null) {
				DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				String cDate = currentDate.format(dateTimeFormatter);

				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = formatter.parse(cDate);
				long differenceInTime = date.getTime() - oldDate.getTime();
				long differenceInMinute = TimeUnit.MILLISECONDS.toMinutes(differenceInTime);
				return differenceInMinute;
			}else 
				return 0L;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0L;
	}

	public static String convertTimestampToString(Timestamp timeStampDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		String date = formatter.format(timeStampDate);
		return date;
	}

	public static Timestamp convertStringToTimeStamp(String stringDate) throws ParseException {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = formatter.parse(stringDate);
		Timestamp timeStapDate = new Timestamp(date.getTime());
		return timeStapDate;
	}

	public static String convertDateToStringDDMMYYYY(Date date) {
		if (date != null) {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String strDate = dateFormat.format(date);
			return strDate;
		} else {
			return "";
		}
	}

	public static String convertDateToString(Date date) {
		if (date != null) {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			String strDate = dateFormat.format(date);
			return strDate;
		} else {
			return "";
		}
	}

	public static String convertDateToStringYYYYMMDD(Date date) {
		if (date != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String strDate = formatter.format(date);
			return strDate;
		} else {
			return "";
		}
	}

	public static String convertDateToStringWithTime(Date date) {
		if (date != null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
			String strDate = dateFormat.format(date);
			return strDate;
		} else {
			return "";
		}
	}

	public static String dateToStringWithTime(Date date) {
		if (date != null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			String strDate = dateFormat.format(date);
			return strDate;
		} else {
			return "";
		}
	}

	public static String dateToStringWithTimeslashFormat(Date date) {
		if (date != null) {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
			String strDate = dateFormat.format(date);
			return strDate;
		} else {
			return "";
		}
	}

	public static String convertDateToStringExpiry(Integer duration) {
		if (duration != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy hh:mm:ss");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, duration - 1);
			String newDate = sdf.format(c.getTime());
			return newDate;
		} else {
			return "";
		}
	}

	public static String convertTimeStampToDate(long timeStamp) {
		if (timeStamp != 0L) {
			Date currentDate = new Date(timeStamp);
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
			String newDate = df.format(currentDate);
			return newDate;
		} else {
			return "";
		}
	}

	public static long getMintue(Date expriyDate) throws ParseException {
		Date date = new Date();

		long timeDiff;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String strDate = dateFormat.format(date);
		String expirydate = dateFormat.format(expriyDate);
		Date startDateObj = dateFormat.parse(strDate);
		Date endDateObj = dateFormat.parse(expirydate);
		if (strDate.compareTo(expirydate) > 0) {
			timeDiff = startDateObj.getTime() - endDateObj.getTime();
		} else if (strDate.compareTo(expirydate) < 0) {
			timeDiff = startDateObj.getTime() - endDateObj.getTime();
		} else {
			timeDiff = endDateObj.getTime() - startDateObj.getTime();
		}
		long minDiff = timeDiff / (1000 * 60);
		return minDiff;
	}

	public static Date convertStringToDateTime(String date) {
		try {
			if (date != null) {
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				Date dateTime = dateFormat.parse(date);
				return dateTime;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date convertStringToDateTimeddMMYY(String date) {
		try {
			if (date != null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date dateTime = dateFormat.parse(date);
				return dateTime;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date slashFormatDateTime(String date) {
		try {
			if (date != null) {
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
				Date dateTime = dateFormat.parse(date);
				return dateTime;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date slashFormatDateTimeddMMYYY(String date) {
		try {
			if (date != null) {
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
				Date dateTime = dateFormat.parse(date);
				return dateTime;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date convertStringToDateTimeYYYY(String date) {
		try {
			if (date != null) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date dateTime = dateFormat.parse(date);
				return dateTime;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static Date StringToDate(String date) {
		try {
			if (date != null) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date dateTime = dateFormat.parse(date);
				return dateTime;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String formatDate(Date date) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy HH:mm");
			String strDate = dateFormat.format(date);
			return strDate;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date convertLocalDateToGMTDateTimeZone(Date date) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		String strDateTime = formatter.format(date);
		System.out.println("Date Format with yyyy-MM-dd hh:mm:ss: " + strDateTime);

		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		Date gmtDate = formatter1.parse(strDateTime);
		System.out.println("converted gmtDate==>" + gmtDate);
		return gmtDate;
	}

	public static Long StringDateToLongDate00(String date) {
		try {
			if (date != null) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date dateTime = dateFormat.parse(date);
//				System.out.println("converted from string to date : " + dateTime);

				DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
				String newDate = dateFormat2.format(dateTime);
//				System.out.println("converted again into string date : " + newDate);

				DateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dt = dateFormat3.parse(newDate);
//				System.out.println("converted again into  date formate : " + dt);

				Long longDateTime = dt.getTime();
//				System.out.println("Timestamp date : " + longDateTime);
				return longDateTime;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Long StringDateToLongDate23(String date) {
		try {
			if (date != null) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date dateTime = dateFormat.parse(date);
//				System.out.println("converted from string to date : " + dateTime);

				DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
				String newDate = dateFormat2.format(dateTime);
//				System.out.println("converted again into string date : " + newDate);

				DateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dt = dateFormat3.parse(newDate);
//				System.out.println("converted again into  date formate : " + dt);

				Long longDateTime = dt.getTime();
//				System.out.println("Timestamp date : " + longDateTime);
				return longDateTime;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Long StringDateToLongDate(String date) {
		try {
			if (date != null) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dt = dateFormat.parse(date);
				System.out.println("converted again into  date formate : " + dt);

				Long longDateTime = dt.getTime();
				System.out.println("Timestamp date : " + longDateTime);
				return longDateTime;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Long StringDateToLongDateTime(String date) {
		try {
			if (date != null) {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dt = dateFormat.parse(date);
				System.out.println("converted again into  date formate : " + dt);

				Long longDateTime = dt.getTime();
				System.out.println("Timestamp date : " + longDateTime);
				return longDateTime;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String convertTimeStampToStringDate(long timeStamp) {
		if (timeStamp != 0L) {
			Date currentDate = new Date(timeStamp);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd 'T' HH:mm:ss");
			String newDate = df.format(currentDate);
			return newDate;
		} else {
			return "";
		}
	}

	// Interval Type: daily/weekly/monthly/yearly
	public static String convertTimeStampToStringDate1(long timeStamp) {
		if (timeStamp != 0L) {
			Date currentDate = new Date(timeStamp);
			DateFormat df = new SimpleDateFormat("d/MM/yyyy");
			String newDate = df.format(currentDate);
			return newDate;
		} else {
			return "";
		}
	}

	public static String convertTimeStampToStringDate2(long timeStamp) {
		if (timeStamp != 0L) {
			Date currentDate = new Date(timeStamp);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String newDate = df.format(currentDate);
			return newDate;
		} else {
			return "";
		}
	}

	public static String getPreviousDate(String intervalType) {
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		String startrDate = localDate.format(dateTimeFormatter);
		String endDate = "";
		if (intervalType.equals("daily")) {
			LocalDate previousDate = localDate.minusDays(1);
			endDate = previousDate.format(dateTimeFormatter);
			System.out.println("Previous date: " + endDate);
			return endDate;
		} else if (intervalType.equals("weekly")) {
			LocalDate previousDate = localDate.minusDays(7);
			endDate = previousDate.format(dateTimeFormatter);
			System.out.println("Previous date: " + endDate);
			System.out.println("Current date: " + startrDate);
			return endDate;
		} else if (intervalType.equals("monthly")) {
			LocalDate previousDate = localDate.minusDays(30);
			endDate = previousDate.format(dateTimeFormatter);
			System.out.println("Previous month String date: " + endDate);
			System.out.println("Current String date: " + startrDate);
			return endDate;
		} else if (intervalType.equals("yearly")) {
			LocalDate previousDate = localDate.minusDays(365);
			endDate = previousDate.format(dateTimeFormatter);
			System.out.println("Previous year String date: " + endDate);
			System.out.println("Current String date: " + startrDate);
			return endDate;
		}
		return "";
	}

	public static LocalDateTime stringToLocalDateTime(String date) {
		if (date != null) {
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime localDateTime;
			localDateTime = LocalDateTime.parse(date, dateTimeFormatter);
			System.out.println("Local date time : " + localDateTime);
			return localDateTime;
		} else {
			return null;
		}

	}

	public static LocalDateTime stringToLocalDate(String date) {
		if (date != null) {
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDateTime localDateTime;
			localDateTime = LocalDateTime.parse(date, dateTimeFormatter);
			System.out.println("Local date time : " + localDateTime);
			return localDateTime;
		} else {
			return null;
		}
	}

	public static LocalDateTime dateToLocalDate(Date date) {
		if (date != null) {
			ZoneId defaultZoneId = ZoneId.systemDefault();
			Instant instant = date.toInstant();
			LocalDateTime localDate = instant.atZone(defaultZoneId).toLocalDateTime();
			System.out.println("Local date time : " + localDate);
			return localDate;
		} else {
			return null;
		}
	}

//	public static void main(String args[]) throws ParseException {
////		StringDateToLongDate("2022-10-20 10:11:20");
////		convertTimeStampToStringDate(StringDateToLongDate("2022-10-20 10:11:20"));
//
////		StringDateToLongDate("2022-10-21 10:38:36");
//
//	}

}
