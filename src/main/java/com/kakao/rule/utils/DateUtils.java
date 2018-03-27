package com.kakao.rule.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	/**
	 * 시간 계산용 메소드
	 * @param srcDate
	 * @param interval
	 * @param unit
	 * @return
	 */
	public static Date calcTime(Date srcDate, int interval, int unit) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(srcDate);
		cal.add(unit, interval);
		return cal.getTime();
	}
}
