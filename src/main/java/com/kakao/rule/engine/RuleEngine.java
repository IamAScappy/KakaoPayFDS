package com.kakao.rule.engine;

import java.util.ArrayList;
import java.util.List;

import com.kakao.fds.dao.IUserLogDAO;
import com.kakao.fds.entity.AccountCreate;
import com.kakao.fds.entity.MoneyReceive;
import com.kakao.fds.entity.MoneyTopup;
import com.kakao.fds.entity.MoneyTransfer;
import com.kakao.fds.vo.ResultVO;
import com.kakao.rule.ruleset.RuleA;
import com.kakao.rule.ruleset.RuleB;
import com.kakao.rule.ruleset.RuleC;
import com.kakao.rule.ruleset.RuleSet;

public class RuleEngine {
	
	private List<RuleSet> detectorCollect; 
	
	protected List<AccountCreate> accountCreateLog;	
	protected List<MoneyReceive> moneyReceiveLog;	
	protected List<MoneyTopup> moneyTopupLog;
	protected List<MoneyTransfer> moneyTransferLog;
	
	private IUserLogDAO dao;
	
	public RuleEngine(IUserLogDAO dao) {
		// DAO를 등록한다.
		this.dao = dao;
		
		// 룰 디텍터를 등록한다.
		this.detectorCollect = new ArrayList<>();
		
		this.detectorCollect.add(new RuleSet("RuleA", new RuleA()));
		this.detectorCollect.add(new RuleSet("RuleB", new RuleB()));
		this.detectorCollect.add(new RuleSet("RuleC", new RuleC()));
	}
	
	public List<String> judge(long userId) {
		List<String> result = new ArrayList<String>();
		
		for (RuleSet rule : this.detectorCollect) {
			if (rule.detect(userId, this.dao) == true) {
				result.add(rule.getRuleName());
			}
		}
		return result;
	}	
}