package com.kakao.rule.engine;

import java.util.ArrayList;
import java.util.List;

import com.kakao.fds.dao.IUserLogDAO;
import com.kakao.rule.ruleset.RuleA;
import com.kakao.rule.ruleset.RuleB;
import com.kakao.rule.ruleset.RuleC;
import com.kakao.rule.ruleset.RuleSet;

/**
 * 룰 엔진. 
 * @author prologue
 *
 */
public class RuleEngine {
	
	// 적용할 룰셋을 수집하는 멤버변수
	private List<RuleSet> ruleCollector; 
	
	private IUserLogDAO dao;
	
	public RuleEngine() { 
		// 룰셋 콜렉터 실행
		this.roleCollect();
	}
	
	public RuleEngine(IUserLogDAO dao) {
		// DAO를 등록한다.
		this.dao = dao;
		
		// 룰셋 콜렉터 실행
		this.roleCollect();
	}
	
	/**
	 * 룰셋을 수집하여 등록한다.
	 * 전체 프레임워크 기반에서는 XML을 이용한 동적 룰셋 수집을 적용하는 것이 좋다.
	 * 이를 위해, RuleSet 객체는 문자열을 기반으로 하는 동적 클래스 로딩을 지원한다. 
	 */
	public void roleCollect() {
		// 룰 디텍터를 등록한다.
		this.ruleCollector = new ArrayList<>();
		
		this.ruleCollector.add(new RuleSet("RuleA", new RuleA()));
		this.ruleCollector.add(new RuleSet("RuleB", new RuleB()));
		this.ruleCollector.add(new RuleSet("RuleC", new RuleC()));
	}
	
	/**
	 * UserID를 기반으로 실제 FDS 탐지 여부를 결정한다. 
	 * @param userId
	 * @return
	 */
	public List<String> judge(long userId) {
		List<String> result = new ArrayList<String>();
		
		for (RuleSet rule : this.ruleCollector) {
			if (rule.detect(userId, this.dao) == true) {
				result.add(rule.getRuleName());
			}
		}
		return result;
	}	
	
	/**
	 * DAO는 어떤 것이라도 상관없다. 
	 * 실제 서비스 환경에서는 MyISAM 기반의 MySQL이나 Couchbase, ElasticSearch 등이 적절할 것이다.  
	 * @param dao
	 */
	public void setDataSource(IUserLogDAO dao) {
		this.dao = dao;
	}
}