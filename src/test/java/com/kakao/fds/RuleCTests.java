package com.kakao.fds;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kakao.fds.dao.IUserLogDAO;
import com.kakao.fds.entity.MoneyReceive;
import com.kakao.rule.ruleset.RuleC;

@RunWith(MockitoJUnitRunner.class)
public class RuleCTests {
	long userId = 32445666644334443l;
	
	@Mock
	private IUserLogDAO dao;
	
	private RuleC rule = new RuleC();
	
	/**
	 * 2시간 이내, 카카오 머니 받기로 5만원 이상 금액을 3회이상 하는 경우가 없는 경우 테스트
	 * 정상적인 거래
	 * @throws ParseException
	 */
	@Test
	public void testDetectNormal() throws ParseException {

		when(dao.searchMoneyReceiveLog(userId)).thenReturn(
			new ArrayList<MoneyReceive>() {
				{
					this.add(new MoneyReceive("2018-03-01 01:15:10", userId, 123456789, "1111111", "2222222", 200_000, 30_000));
					this.add(new MoneyReceive("2018-03-01 01:20:10", userId, 123456789, "1111111", "2222222", 240_000, 70_000));
					this.add(new MoneyReceive("2018-03-01 01:21:23", userId, 123456789, "1111111", "2222222", 280_000, 50_000));
					this.add(new MoneyReceive("2018-03-01 02:55:10", userId, 123456789, "1111111", "2222222", 230_000, 20_000));
					this.add(new MoneyReceive("2018-03-02 03:10:10", userId, 123456789, "1111111", "2222222", 150_000, 30_000));
					this.add(new MoneyReceive("2018-03-02 05:15:10", userId, 123456789, "1111111", "2222222", 210_000, 10_000));
					this.add(new MoneyReceive("2018-03-03 05:15:10", userId, 123456789, "1111111", "2222222", 300_000, 50_000));
				}
			}
		);
		
		boolean result = rule.detect(userId, dao);
		
		verify(dao, times(1)).searchMoneyReceiveLog(userId);		
		assertFalse(result);
	}
	
	/**
	 * 2시간 이내, 카카오 머니 받기로 5만원 이상 금액을 3회이상 하는 경우가 있는 경우 테스트
	 * 비정상적인 거래
	 * @throws ParseException
	 */
	@Test
	public void testDetectNotNormal() throws ParseException {
		when(dao.searchMoneyReceiveLog(userId)).thenReturn(
			new ArrayList<MoneyReceive>() {
				{
					this.add(new MoneyReceive("2018-03-01 01:15:10", userId, 123456789, "1111111", "2222222", 200_000, 60_000));
					this.add(new MoneyReceive("2018-03-01 01:20:10", userId, 123456789, "1111111", "2222222", 240_000, 30_000));
					this.add(new MoneyReceive("2018-03-01 01:21:23", userId, 123456789, "1111111", "2222222", 280_000, 40_000));
					this.add(new MoneyReceive("2018-03-01 02:55:10", userId, 123456789, "1111111", "2222222", 230_000, 50_000));
					this.add(new MoneyReceive("2018-03-01 03:10:10", userId, 123456789, "1111111", "2222222", 150_000, 70_000));
					this.add(new MoneyReceive("2018-03-02 05:15:10", userId, 123456789, "1111111", "2222222", 210_000, 10_000));
					this.add(new MoneyReceive("2018-03-03 08:15:12", userId, 123456789, "1111111", "2222222", 300_000, 50_000));
					this.add(new MoneyReceive("2018-03-03 09:54:23", userId, 123456789, "1111111", "2222222", 280_000, 50_000));
					this.add(new MoneyReceive("2018-03-04 03:15:10", userId, 123456789, "1111111", "2222222", 230_000, 100_000));
					this.add(new MoneyReceive("2018-03-04 03:20:10", userId, 123456789, "1111111", "2222222", 230_000, 70_000));
					this.add(new MoneyReceive("2018-03-04 04:15:10", userId, 123456789, "1111111", "2222222", 230_000, 50_000));
					this.add(new MoneyReceive("2018-03-04 03:15:10", userId, 123456789, "1111111", "2222222", 230_000, 100_000));
					this.add(new MoneyReceive("2018-03-04 23:08:23", userId, 123456789, "1111111", "2222222", 280_000, 50_000));
					this.add(new MoneyReceive("2018-03-05 04:23:10", userId, 123456789, "1111111", "2222222", 150_000, 100_000));
					this.add(new MoneyReceive("2018-03-06 05:15:15", userId, 123456789, "1111111", "2222222", 210_000, 30_000));
					this.add(new MoneyReceive("2018-03-07 05:16:18", userId, 123456789, "1111111", "2222222", 210_000, 100_000));
					this.add(new MoneyReceive("2018-03-20 05:15:11", userId, 123456789, "1111111", "2222222", 300_000, 10_000));
				}
			}
		);
		
		boolean result = rule.detect(userId, dao);
		
		verify(dao, times(1)).searchMoneyReceiveLog(userId);
		assertTrue(result);
	}
}
