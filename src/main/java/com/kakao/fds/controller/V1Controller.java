package com.kakao.fds.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.fds.service.FraudService;
import com.kakao.fds.service.IFraudService;
import com.kakao.fds.vo.ResultVO;
import com.kakao.rule.engine.RuleEngine;

@RestController
@RequestMapping(value="/v1")
public class V1Controller {

	private final static Logger log = LoggerFactory.getLogger(V1Controller.class);
	
	@Autowired 
	private IFraudService fraudService;
	
	@GetMapping("fraud/{user_id}")
	private ResultVO fraud(@PathVariable("user_id") long userId) {
		log.info("userId={}", userId);
		
		// 반환할 객체와 서비스 처리 결과 객체를 생성
		ResultVO resultvo = new ResultVO(userId);
		List<String> rules = fraudService.search(userId);
		
		for (String s : rules) {
			resultvo.addRuleItem(s);
		}
		
		// 해당하는 룰이 하나라도 있다면 isFraud의 값을 true로
		resultvo.setFraud(rules.size() > 0);
		
		return resultvo;
	}
}
