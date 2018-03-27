package com.kakao.fds.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccountCreate {
	private Date regdate;
	private long userId;
	
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
