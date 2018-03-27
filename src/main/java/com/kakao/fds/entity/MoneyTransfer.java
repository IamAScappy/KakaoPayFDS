package com.kakao.fds.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

/**
 * 카카오머니 송금로그
 * @author prologue
 */
@Data
@SuppressWarnings("unused")
public class MoneyTransfer {
	private Date regdate;		// 발생일시
	private long userId;		// 사용자 아이디
	private String sndAccount;	// 송금 카카오머니 계좌번호
	private String rcvAccount;	// 수신 카카오머니 계좌번호
	private double preAmount;	// 송금 이체 전 카카오머니 계좌 잔액
	private double sndAmount;	// 송금 금액 
	private long rcvUserId;		// 수신 사용자 아이디
	
	public MoneyTransfer() { }
	
	public MoneyTransfer(String regdate, long userId, long rcvUserId, String sndAccount, String rcvAccount, float preAmount, float sndAmount) throws ParseException {
		this.userId = userId;
		this.rcvUserId = rcvUserId;
		this.sndAccount = sndAccount;
		this.rcvAccount = rcvAccount;
		this.preAmount = preAmount;
		this.sndAmount = sndAmount;
		this.regdate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(regdate);
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
	public double getPreAmount() {
		return preAmount;
	}
	public void setPreAmount(double preAmount) {
		this.preAmount = preAmount;
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
	public double getSndAmount() {
		return sndAmount;
	}
	public void setSndAmount(float sndAmount) {
		this.sndAmount = sndAmount;
	}
	public long getRcvUserId() {
		return rcvUserId;
	}
	public void setRcvUserId(long rcvUserId) {
		this.rcvUserId = rcvUserId;
	}
}
