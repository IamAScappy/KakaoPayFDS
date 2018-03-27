package com.kakao.fds;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.kakao.fds.dao.IUserLogDAO;
import com.kakao.fds.entity.AccountCreate;
import com.kakao.fds.entity.MoneyReceive;
import com.kakao.fds.entity.MoneyTopup;
import com.kakao.fds.entity.MoneyTransfer;
import com.kakao.fds.service.FraudService;

/**
 * 서비스 레벨에서의 테스트 
 * DAO는 정의되지 않았으므로 Mockup Data로 대신한다.
 * @author prologue
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class FraudServiceTests {
	long userId = 32445666644334443l;
	
	@Mock
	private IUserLogDAO dao;
	
	@InjectMocks
	private FraudService fraudService;
	
	/**
	 * 아무것도 탐지되지 않는지 테스트
	 * @throws ParseException
	 */
	@Test
	public void testSearch0() throws ParseException {
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
						this.add(new MoneyTransfer("2018-03-01 01:15:08", userId, 123456798, "1111111", "2222222", 150_000, 80_000));
						this.add(new MoneyTransfer("2018-03-01 01:58:29", userId, 123456798, "1111111", "2222222", 70_000, 68_000));
					}
				}
			);
		when(dao.searchMoneyReceiveLog(userId)).thenReturn(
				Arrays.asList(
						new MoneyReceive("2018-03-01 01:15:10", userId, 123456789, "1111111", "2222222", 200_000, 100_000),
						new MoneyReceive("2018-03-03 02:15:10", userId, 123456789, "1111111", "2222222", 240_000, 100_000),
						new MoneyReceive("2018-03-03 09:54:23", userId, 123456789, "1111111", "2222222", 280_000, 50_000),
						new MoneyReceive("2018-03-04 03:15:10", userId, 123456789, "1111111", "2222222", 230_000, 100_000),
						new MoneyReceive("2018-03-04 23:08:23", userId, 123456789, "1111111", "2222222", 280_000, 50_000),
						new MoneyReceive("2018-03-05 04:23:10", userId, 123456789, "1111111", "2222222", 150_000, 100_000),
						new MoneyReceive("2018-03-05 04:53:23", userId, 123456789, "1111111", "2222222", 150_000, 30_000),
						new MoneyReceive("2018-03-05 05:45:08", userId, 123456789, "1111111", "2222222", 150_000, 70_000),
						new MoneyReceive("2018-03-06 05:15:15", userId, 123456789, "1111111", "2222222", 210_000, 30_000),
						new MoneyReceive("2018-03-07 05:16:18", userId, 123456789, "1111111", "2222222", 210_000, 40_000),
						new MoneyReceive("2018-03-20 05:15:11", userId, 123456789, "1111111", "2222222", 300_000, 10_000)
				)
			);
		
		List<String> result = fraudService.search(userId);
		String joinResult = result.stream().collect(Collectors.joining(", "));
		System.out.println(joinResult);

		verify(dao, atLeast(2)).getAccountCreateLog(userId);
		assertEquals("", joinResult);
	}
	
	/**
	 * RuleA, RuleB, RuleC에 탐지되는지 테스트
	 * @throws ParseException
	 */
	@Test
	public void testSearch1() throws ParseException {
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
						this.add(new MoneyTransfer("2018-03-01 01:15:08", userId, 123456798, "1111111", "2222222", 150_000, 80_000));
						this.add(new MoneyTransfer("2018-03-01 01:58:29", userId, 123456798, "1111111", "2222222", 70_000, 69_000));
					}
				}
			);
		when(dao.searchMoneyReceiveLog(userId)).thenReturn(
				Arrays.asList(
						new MoneyReceive("2018-03-01 01:15:10", userId, 123456789, "1111111", "2222222", 200_000, 100_000),
						new MoneyReceive("2018-03-03 02:15:10", userId, 123456789, "1111111", "2222222", 240_000, 100_000),
						new MoneyReceive("2018-03-03 09:54:23", userId, 123456789, "1111111", "2222222", 280_000, 50_000),
						new MoneyReceive("2018-03-04 03:15:10", userId, 123456789, "1111111", "2222222", 230_000, 100_000),
						new MoneyReceive("2018-03-04 23:08:23", userId, 123456789, "1111111", "2222222", 280_000, 50_000),
						new MoneyReceive("2018-03-05 04:23:10", userId, 123456789, "1111111", "2222222", 150_000, 100_000),
						new MoneyReceive("2018-03-05 04:53:23", userId, 123456789, "1111111", "2222222", 150_000, 50_000),
						new MoneyReceive("2018-03-05 05:45:08", userId, 123456789, "1111111", "2222222", 150_000, 70_000),
						new MoneyReceive("2018-03-06 05:15:15", userId, 123456789, "1111111", "2222222", 210_000, 30_000),
						new MoneyReceive("2018-03-07 05:16:18", userId, 123456789, "1111111", "2222222", 210_000, 100_000),
						new MoneyReceive("2018-03-20 05:15:11", userId, 123456789, "1111111", "2222222", 300_000, 10_000)
				)
			);
		
		List<String> result = fraudService.search(userId);
		String joinResult = result.stream().collect(Collectors.joining(", "));
		System.out.println(joinResult);

		verify(dao, atLeast(2)).getAccountCreateLog(userId);
		assertEquals("RuleA, RuleB, RuleC", joinResult);
	}
	
	/**
	 * RuleB, RuleC에 탐지되는지 테스트
	 * @throws ParseException
	 */
	@Test
	public void testSearch2() throws ParseException {
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
						this.add(new MoneyTransfer("2018-03-01 01:15:08", userId, 123456798, "1111111", "2222222", 150_000, 80_000));
						this.add(new MoneyTransfer("2018-03-01 01:58:29", userId, 123456798, "1111111", "2222222", 70_000, 68_000));
					}
				}
			);
		when(dao.searchMoneyReceiveLog(userId)).thenReturn(
				Arrays.asList(
						new MoneyReceive("2018-03-01 01:15:10", userId, 123456789, "1111111", "2222222", 200_000, 100_000),
						new MoneyReceive("2018-03-03 02:15:10", userId, 123456789, "1111111", "2222222", 240_000, 100_000),
						new MoneyReceive("2018-03-03 09:54:23", userId, 123456789, "1111111", "2222222", 280_000, 50_000),
						new MoneyReceive("2018-03-04 03:15:10", userId, 123456789, "1111111", "2222222", 230_000, 100_000),
						new MoneyReceive("2018-03-04 23:08:23", userId, 123456789, "1111111", "2222222", 280_000, 50_000),
						new MoneyReceive("2018-03-05 04:23:10", userId, 123456789, "1111111", "2222222", 150_000, 100_000),
						new MoneyReceive("2018-03-05 04:53:23", userId, 123456789, "1111111", "2222222", 150_000, 50_000),
						new MoneyReceive("2018-03-05 05:45:08", userId, 123456789, "1111111", "2222222", 150_000, 70_000),
						new MoneyReceive("2018-03-06 05:15:15", userId, 123456789, "1111111", "2222222", 210_000, 30_000),
						new MoneyReceive("2018-03-07 05:16:18", userId, 123456789, "1111111", "2222222", 210_000, 100_000),
						new MoneyReceive("2018-03-20 05:15:11", userId, 123456789, "1111111", "2222222", 300_000, 10_000)
				)
			);
		
		List<String> result = fraudService.search(userId);
		String joinResult = result.stream().collect(Collectors.joining(", "));
		System.out.println(joinResult);

		verify(dao, atLeast(2)).getAccountCreateLog(userId);
		assertEquals("RuleB, RuleC", joinResult);
	}
}
