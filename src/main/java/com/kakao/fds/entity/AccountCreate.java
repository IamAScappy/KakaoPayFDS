package com.kakao.fds.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 사용자 계좌 개설 로그
 * @author prologue
 *
 */
public class AccountCreate {
	private Date regdate;	// 발생일자 
	private long userId;	// 유저 아이디
	private String account;	// 카카오머니 계좌번호
	
	public AccountCreate() {} 
	
	public AccountCreate(long userId, String regdate) throws ParseException {
		this.userId = userId;
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
}
