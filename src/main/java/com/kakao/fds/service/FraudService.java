package com.kakao.fds.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kakao.fds.dao.IUserLogDAO;
import com.kakao.rule.engine.RuleEngine;

/**
 * 서비스 레이어
 * @author prologue
 */
@Service
public class FraudService implements IFraudService {

	@Autowired(required = false)
	private IUserLogDAO dao;
	
	/**
	 * 룰 엔진 객체를 생성하여, DAO를 전달한다.
	 */
	@Override
	public List<String> search(long userId) {
		RuleEngine engine = new RuleEngine(dao);
		return engine.judge(userId);
	}
	
}
