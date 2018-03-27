package com.kakao.fds.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kakao.fds.dao.IUserLogDAO;
import com.kakao.rule.engine.RuleEngine;

@Service
public class FraudService implements IFraudService {

	@Autowired(required = false)
	private IUserLogDAO dao;
	
	@Override
	public List<String> search(long userId) {
		RuleEngine engine = new RuleEngine(dao);
		return engine.judge(userId);
	}
	
}
