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
import com.kakao.fds.entity.AccountCreate;
import com.kakao.fds.entity.MoneyReceive;
import com.kakao.rule.ruleset.RuleB;

@RunWith(MockitoJUnitRunner.class)
public class RuleBTests {
	long userId = 32445666644334443l;
	
	@Mock
	private IUserLogDAO dao;
	
	private RuleB rule = new RuleB();
	
	/**
	 * 정상적인 거래
	 * @throws ParseException
	 */
	@Test
	public void testDetectNormal() throws ParseException {

		// 카카오머니 서비스 계좌 개설을 하고 7일 이내, 카카오 머니 받기로 10만원 이상 금액을 5회 이상 하는 경우가 있는지 체크
		when(dao.getAccountCreateLog(userId)).thenReturn(new AccountCreate(userId, "2018-03-01 01:00:00"));
		when(dao.searchMoneyReceiveLog(userId)).thenReturn(
			new ArrayList<MoneyReceive>() {
				{
					this.add(new MoneyReceive("2018-03-01 01:15:10", userId, 123456789, "1111111", "2222222", 200_000, 100_000));
					this.add(new MoneyReceive("2018-03-03 02:15:10", userId, 123456789, "1111111", "2222222", 240_000, 100_000));
					this.add(new MoneyReceive("2018-03-04 09:54:23", userId, 123456789, "1111111", "2222222", 280_000, 50_000));
					this.add(new MoneyReceive("2018-03-06 03:15:10", userId, 123456789, "1111111", "2222222", 230_000, 100_000));
					this.add(new MoneyReceive("2018-03-11 04:23:10", userId, 123456789, "1111111", "2222222", 150_000, 100_000));
					this.add(new MoneyReceive("2018-03-15 05:15:10", userId, 123456789, "1111111", "2222222", 210_000, 100_000));
					this.add(new MoneyReceive("2018-03-20 05:15:10", userId, 123456789, "1111111", "2222222", 300_000, 100_000));
				}
			}
		);
		
		boolean result = rule.detect(userId, dao);
		
		verify(dao, times(1)).getAccountCreateLog(userId);
		verify(dao, times(1)).searchMoneyReceiveLog(userId);
		
		assertFalse(result);
	}
	
	/**
	 * 비정상적인 거래
	 * @throws ParseException
	 */
	@Test
	public void testDetectNotNormal() throws ParseException {
		// 카카오머니 서비스 계좌 개설을 하고 7일 이내, 카카오 머니 받기로 10만원 이상 금액을 5회 이상 하는 경우가 있는지 체크
		when(dao.getAccountCreateLog(userId)).thenReturn(new AccountCreate(userId, "2018-03-01 01:00:00"));
		when(dao.searchMoneyReceiveLog(userId)).thenReturn(
			new ArrayList<MoneyReceive>() {
				{
					this.add(new MoneyReceive("2018-03-01 01:15:10", userId, 123456789, "1111111", "2222222", 200_000, 100_000));
					this.add(new MoneyReceive("2018-03-03 02:15:10", userId, 123456789, "1111111", "2222222", 240_000, 100_000));
					this.add(new MoneyReceive("2018-03-03 09:54:23", userId, 123456789, "1111111", "2222222", 280_000, 50_000));
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
		
		verify(dao, times(1)).getAccountCreateLog(userId);
		verify(dao, times(1)).searchMoneyReceiveLog(userId);
		
		assertTrue(result);
	}
}
