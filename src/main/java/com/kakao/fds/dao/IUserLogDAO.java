package com.kakao.fds.dao;

import java.util.List;

import com.kakao.fds.entity.AccountCreate;
import com.kakao.fds.entity.MoneyReceive;
import com.kakao.fds.entity.MoneyTopup;
import com.kakao.fds.entity.MoneyTransfer;

public interface IUserLogDAO {

	public List<MoneyTopup> searchMoneyTopupLog(long userId);
	
	public List<MoneyTransfer> searchMoneyTransferLog(long userId);
	
	public List<MoneyReceive> searchMoneyReceiveLog(long userId);
	
	public AccountCreate getAccountCreateLog(long userId);
}
