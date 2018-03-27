package com.kakao.rule.ruleset;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.kakao.fds.dao.IUserLogDAO;
import com.kakao.fds.entity.AccountCreate;
import com.kakao.fds.entity.MoneyTopup;
import com.kakao.fds.entity.MoneyTransfer;
import com.kakao.rule.utils.DateUtils;

/**
 * RuleA : 카카오머니 서비스 계좌 개설을 하고 1시간 이내, 20만원 충전 후 잔액이 1000원 이하가 되는지 체크한다.
 * @author prologue
 *
 */
public class RuleA implements RuleDetector {	
	
	@Override
	public boolean detect(long userId, IUserLogDAO dao) {
		
		// STEP 1. 유효한 시간 범위 계산 (카카오 머니 서비스 계좌 개설 후 1시간 이내)  
		AccountCreate createLog = dao.getAccountCreateLog(userId);
		
		Date startdate = createLog.getRegdate();
		Date enddate = DateUtils.calcTime(startdate, 1, Calendar.HOUR_OF_DAY); 		
				
		// SETP 2. 1시간 이내에 발생한 충전 거래 정보를 필터링하여 20만원 충전 내역이 있는지 확인한다. 
		List<MoneyTopup> topupLog = dao.searchMoneyTopupLog(userId);
		List<MoneyTopup> topupRangeLog = this.getTopupRangeLog(topupLog, startdate, enddate);
		
		if (topupRangeLog.size() == 0) { // 탐지된 내역이 없다면 
			return false;
		}
		
		// STEP 3. 송금으로 지불된 잔액 계산 
		List<MoneyTransfer> transferLog = dao.searchMoneyTransferLog(userId);
		double tranferAmount = this.getTransferAmount(transferLog, startdate, enddate);
		
		// STEP 4. 잔액이 1,000원 이하일 경우 룰에 의해 감지
		if (200_000 - tranferAmount  <= 1_000) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 시간 범위 내에 200_000원을 충전한 내역이 있는지 확인한다.
	 * @param topupLog
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	private List<MoneyTopup> getTopupRangeLog(List<MoneyTopup> topupLog, Date startdate, Date enddate) {
		return topupLog.stream()
			.filter(c -> c.getRegdate().compareTo(startdate) > 0)
			.filter(c -> c.getRegdate().compareTo(enddate) < 0)
			.filter(c -> c.getAmount() == 200_000)
			.collect(Collectors.toList());		
	}
	
	/**
	 * 송금으로 지불된 총 금액 계산
	 * @param log
	 * @param startdate
	 * @param enddate
	 * @return
	 */
	private double getTransferAmount(List<MoneyTransfer> log, Date startdate, Date enddate) {
		List<MoneyTransfer> transferLog = log.stream()
				.filter(c -> c.getRegdate().compareTo(startdate) > 0)
				.filter(c -> c.getRegdate().compareTo(enddate) < 0)
				.collect(Collectors.toList());
			
		double totalAmount = 0;
		for (MoneyTransfer m : transferLog) {
			totalAmount += m.getSndAmount(); 
		}
		return totalAmount;
	}
}