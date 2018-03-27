package com.kakao.fds.vo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

public class ResultVO {
	
	@JsonProperty("user_id")
	private long userId = 0l; // 요청 변수의 사용자 아이디
	
	@JsonProperty("is_fraud")
	private boolean fraud = false; // 룰에 의한 이상 거래 가능성 여부 
	
	@JsonProperty("rule")
	private String rule = ""; // 해당되는 룰 이름 (여러 개일 경우 콤마로 구분)
	
	@JsonIgnore
	private List<String> ruleItem = new ArrayList<String>(); // 룰이 여러개일 경우 각각의 룰 아이템 정보

	public ResultVO() {}
	
	public ResultVO(long userId) {
		this.userId = userId;
	}
	
	public ResultVO(long userId, boolean fraud, String... ruleItem) {
		this.userId = userId;
		this.fraud = fraud;
		
		// ruleItem이 있을 경우, 이들을 조합하여 rule 값을 만들어 준다.
		for (String r : ruleItem) {
			this.ruleItem.add(r);
		}
	}
	
	public String getRule() {
		if (this.ruleItem.size() > 0) {
			return String.join(",",  this.ruleItem);
		} else {
			return rule;
		}
	}

	public List<String> getRuleItem() {
		return ruleItem;
	}

	public void setRuleItem(List<String> ruleItem) {
		this.ruleItem = ruleItem;
	}
	
	public void addRuleItem(String ruleName) {
		this.ruleItem.add(ruleName);
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public boolean getFraud() {
		return fraud;
	}

	public void setFraud(boolean fraud) {
		this.fraud = fraud;
	}	
}
