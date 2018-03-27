package com.kakao.fds.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

/**
 * 카카오 머니 받기 로그
 * @author prologue
 */
@SuppressWarnings("unused")
public class MoneyReceive {
	private Date regdate;		// 발생일시 
	
	private long userId;		// 사용자 ID
	private long sndUserId;		// 보낸 사용자 아이디
	
	private String sndAccount;	// 보낸 카카오머니 계좌번호
	private String rcvAccount;	// 받는 카카오머니 계좌번호 
	
	private double preBalance;	// 받기 전 카카오머니 계좌 잔액
	private double rcvAmount;	// 받은 금액 
	
	public MoneyReceive(String regdate, long userId, long sndUserId, String sndAccount, String rcvAccount, double preBalance, double rcvAmount) throws ParseException {
		this.regdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(regdate);
		this.userId = userId;
		this.sndUserId = sndUserId;
		this.sndAccount = sndAccount;
		this.rcvAccount = rcvAccount;
		this.preBalance = preBalance;
		this.rcvAmount = rcvAmount;
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
	public long getSndUserId() {
		return sndUserId;
	}
	public void setSndUserId(long sndUserId) {
		this.sndUserId = sndUserId;
	}	
	public String getSndAccount() {
		return sndAccount;
	}
	public void setSndAccount(String sndAccount) {
		this.sndAccount = sndAccount;
	}
	public String getRcvAccount() {
		return rcvAccount;
	}
	public void setRcvAccount(String rcvAccount) {
		this.rcvAccount = rcvAccount;
	}
	public double getPreBalance() {
		return preBalance;
	}
	public void setPreBalance(double preBalance) {
		this.preBalance = preBalance;
	}
	public double getRcvAmount() {
		return rcvAmount;
	}
	public void setRcvAmount(double rcvAmount) {
		this.rcvAmount = rcvAmount;
	}	
}
