package com.kakao.fds.dao;

import java.util.List;

import com.kakao.fds.entity.AccountCreate;
import com.kakao.fds.entity.MoneyReceive;
import com.kakao.fds.entity.MoneyTopup;
import com.kakao.fds.entity.MoneyTransfer;

/**
 * 데이터 소스를 위한 DAO 레이어. 
 * 서비스 개발 완료 단계에서 해당 DAO는 MyISAM 기반의 MySQL 또는 Couchbase 또는 ElasticSearch가 적절할 것으로 생각
 * @author prologue
 *
 */
public interface IUserLogDAO {

	public List<MoneyTopup> searchMoneyTopupLog(long userId);
	
	public List<MoneyTransfer> searchMoneyTransferLog(long userId);
	
	public List<MoneyReceive> searchMoneyReceiveLog(long userId);
	
	public AccountCreate getAccountCreateLog(long userId);
}
