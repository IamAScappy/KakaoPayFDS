package com.kakao.fds;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
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
import com.kakao.fds.entity.MoneyTopup;
import com.kakao.fds.entity.MoneyTransfer;
import com.kakao.rule.ruleset.RuleA;

/**
 * RuleA : 카카오머니 서비스 계좌 개설을 하고 1시간 이내, 20만원 충전 후 잔액이 1000원 이하가 되는지 체크한다.
 * @author prologue
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class RuleATests {
	long userId = 32445666644334443l;
	
	@Mock
	private IUserLogDAO dao;
	private RuleA rule = new RuleA();
	
	/**
	 * CASE 1 : 충전 로그가 없을 때.
	 * @throws ParseException
	 */
	@Test
	public void testDetectNoTopup() throws ParseException {

		when(dao.getAccountCreateLog(userId)).thenReturn(new AccountCreate(userId, "2018-03-01 01:00:00"));
		
		boolean result = rule.detect(userId, dao);
		
		// 충전 내역이 없다면 송금 로그용 DAO 메소드는 실행되지 않아야 한다.
		verify(dao, never()).searchMoneyTransferLog(userId);		
		assertFalse(result);
	}

	/**
	 * CASE 2 : 충전 로그가 있지만, 20만원이 아닐 때
	 * @throws ParseException
	 */
	@Test
	public void testDetectNoEnoughTopup() throws ParseException {

		when(dao.getAccountCreateLog(userId)).thenReturn(new AccountCreate(userId, "2018-03-01 01:00:00"));
		when(dao.searchMoneyTopupLog(userId)).thenReturn(
			new ArrayList<MoneyTopup>() {
				{ this.add(new MoneyTopup("2018-03-01 01:03:20", userId, "1111111", "2222222", 100_000)); }
			}
		);
		
		boolean result = rule.detect(userId, dao);
		
		// 충전 내역은 있지만, 20만원이 아니므로 송금 로그용 DAO 메소드는 실행되지 않아야 한다.
		verify(dao, never()).searchMoneyTransferLog(userId);	
		assertFalse(result);
	}
	
	/**
	 * Case 3 : 충전 기준은 충족했지만, 잔액이 1000원 이상일 때
	 * @throws ParseException
	 */
	@Test
	public void testDetectEnoughBalance() throws ParseException {

		when(dao.getAccountCreateLog(userId)).thenReturn(new AccountCreate(userId, "2018-03-01 01:00:00"));
		when(dao.searchMoneyTopupLog(userId)).thenReturn(
			new ArrayList<MoneyTopup>() {
				{ this.add(new MoneyTopup("2018-03-01 01:03:20", userId, "1111111", "2222222", 200_000)); }
			}
		);
		when(dao.searchMoneyTransferLog(userId)).thenReturn(
				new ArrayList<MoneyTransfer>() {
					{
						this.add(new MoneyTransfer("2018-03-01 01:05:25", userId, 123456798, "1111111", "2222222", 200_000, 50_000));
						this.add(new MoneyTransfer("2018-03-01 01:05:25", userId, 123456798, "1111111", "2222222", 150_000, 80_000));
						this.add(new MoneyTransfer("2018-03-01 01:05:25", userId, 123456798, "1111111", "2222222", 70_000, 68_000));
					}
				}
			);
		
		boolean result = rule.detect(userId, dao);
		
		verify(dao).searchMoneyTransferLog(userId);	
		assertFalse(result);
	}
	
	/**
	 * Case 3 : 충전 기준도 충족하고, 잔액이 1000원 이하일 때
	 * @throws ParseException
	 */
	@Test
	public void testDetectNotNormal() throws ParseException {
		when(dao.getAccountCreateLog(userId)).thenReturn(new AccountCreate(userId, "2018-03-01 01:00:00"));
		when(dao.searchMoneyTopupLog(userId)).thenReturn(
				new ArrayList<MoneyTopup>() {
					{ this.add(new MoneyTopup("2018-03-01 01:03:20", userId, "1111111", "2222222", 200_000)); }
				}
			);
		when(dao.searchMoneyTransferLog(userId)).thenReturn(
				new ArrayList<MoneyTransfer>() {
					{
						this.add(new MoneyTransfer("2018-03-01 01:05:25", userId, 123456798, "1111111", "2222222", 200_000, 50_000));
						this.add(new MoneyTransfer("2018-03-01 01:05:25", userId, 123456798, "1111111", "2222222", 150_000, 80_000));
						this.add(new MoneyTransfer("2018-03-01 01:05:25", userId, 123456798, "1111111", "2222222", 70_000, 69_000));
					}
				}
			);
		boolean result = rule.detect(userId, dao);
		assertTrue(result);
	}
}
