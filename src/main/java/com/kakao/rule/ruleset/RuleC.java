package com.kakao.rule.ruleset;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.kakao.fds.dao.IUserLogDAO;
import com.kakao.fds.entity.MoneyReceive;

/**
 * RuleC : 2시간 이내, 카카오 머니 받기로 5만원 이상 금액을 3회이상 하는 경우
 * @author prologue
 *
 */
public class RuleC implements RuleDetector {
	
	@Override
	public boolean detect(long userId, IUserLogDAO dao) {
		// 카카오머니 받기로 5만원 이상 금액을 한 내역 계산
		List<MoneyReceive> receiveLog = dao.searchMoneyReceiveLog(userId);
		List<MoneyReceive> receiveRangeLog = receiveLog.stream()
				.filter(a -> a.getRcvAmount() >= 50_000)
				.collect(Collectors.toList());
		
		// 거래 내역이 전체 3건 미만이면 
		if (receiveRangeLog.size() < 3) {
			return false;
		}
		
		// 루프를 반복하면서 처리
		for (int i = 0; i < receiveRangeLog.size(); i++) {
			//System.out.print(receiveRangeLog.get(i).getRegdate());
			//System.out.println(receiveRangeLog.get(i).getRcvAmount());
			
			try {
				Date startdate = receiveRangeLog.get(i).getRegdate(); // 기준 거래 날짜 
				Date enddate = receiveRangeLog.get(i+2).getRegdate(); // n + 2 번째 거래건의 날짜
				
				long diff = Math.abs(startdate.getTime() - enddate.getTime()) / (60 * 60 * 1000); // 두 거래 사이의 시간차
				if (diff <= 2l) {
					return true;
				}
			} catch (IndexOutOfBoundsException e) {
				System.out.println(e.getMessage());
				return false;
			}
		}
		return false;
	}	
}
