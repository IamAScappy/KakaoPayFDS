package com.kakao.fds.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

/**
 * 카카오머니 충전 로그 (은행 계좌에서 머니 충전)
 * @author prologue
 */
@SuppressWarnings("unused")
public class MoneyTopup {	
	private Date regdate; 			// 발생일시
	private long userId;			// 사용자 아이디 
	private String accountKakao;	// 카카오머니 계좌번호 
	private String accountBank;		// 은행계좌
	private double amount;			// 충전금액 
	
	public MoneyTopup() {}
	
	public MoneyTopup(String regdate, long userId, String accountKakao, String accountBank, float amount) throws ParseException {
		this.regdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(regdate);
		this.userId = userId;
		this.accountKakao = accountKakao;
		this.accountBank = accountBank;
		this.amount = amount;
	}
	
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getAccountKakao() {
		return accountKakao;
	}
	public void setAccountKakao(String accountKakao) {
		this.accountKakao = accountKakao;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getAccountBank() {
		return accountBank;
	}
	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}
}
