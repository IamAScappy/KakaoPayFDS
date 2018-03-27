package com.kakao.rule.ruleset;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.kakao.fds.dao.IUserLogDAO;
import com.kakao.fds.entity.AccountCreate;
import com.kakao.fds.entity.MoneyReceive;
import com.kakao.rule.utils.DateUtils;

/**
 * RuleB : 카카오머니 서비스 계좌 개설을 하고 7일 이내, 카카오 머니 받기로 10만원 이상 금액을 5회 이상 하는 경우가 있는지 체크
 * @author prologue
 *
 */
public class RuleB implements RuleDetector {
	
	@Override
	public boolean detect(long userId, IUserLogDAO dao) {
		// 1. 유효한 시간 범위 계산 (카카오 머니 서비스 계좌 개설 후 7일 이내)  
		AccountCreate createLog = dao.getAccountCreateLog(userId);
		
		Date startdate = createLog.getRegdate();
		Date enddate = DateUtils.calcTime(startdate, 7, Calendar.DATE); 
		
		// 2. 해당 기간 범위 내, 10만원 이상 금액을 카카오머니로 받은 경우를 필터링한다.
		List<MoneyReceive> receiveLog = dao.searchMoneyReceiveLog(userId);
		long size = receiveLog.parallelStream()				
				.filter(c -> c.getRegdate().compareTo(startdate) > 0)
				.filter(c -> c.getRegdate().compareTo(enddate) < 0)
				.filter(c -> c.getRcvAmount() >= 100_000) 
				.count();
		
		// 3. 해당 경우가 5건 이상 있는지 체크하여 true / false 반환
		return size >= 5;
	}	
}