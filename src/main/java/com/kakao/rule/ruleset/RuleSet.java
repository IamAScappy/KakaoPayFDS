package com.kakao.rule.ruleset;

import com.kakao.fds.dao.IUserLogDAO;

/**
 * Strategy 패턴 활용을 위한 기본 룰셋 뼈대 
 * @author prologue
 *
 */
public class RuleSet {
	
	// FDS 룰 규칙을 체크하기 위한 탐지 인터페이스	
	private RuleDetector ruleDetector;
	
	// 룰 이름
	private String ruleName;
	
	/**
	 * 생성자 : 룰 이름과 룰 알고리즘 객체를 인자값으로 받아 룰셋을 생성한다.
	 * @param ruleName
	 * @param behavior
	 */
	public RuleSet(String ruleName, RuleDetector detector) {
		this.ruleName = ruleName;
		this.ruleDetector = detector; // 탐지용 룰 객체를 주입
	}
	
	/**
	 * 생성자 : 룰 이름과 룰 알고리즘 객체용 클래스명을 인자값으로 받아, 동적으로 룰 알고리즘 객체를 생성하고 이를 이용하여 룰셋을 생성한다. 
	 * 이후 고도화 과정에서 룰셋을 XML 기반으로 관리하게 될 경우를 대비해 만든 생성자. 디텍터 클래스를 동적으로 로딩하여 룰셋을 구성한다.
	 * @param ruleName
	 * @param detector
	 * @throws ClassNotFoundException 
	 */
	public RuleSet(String ruleName, String detectorName) {
		this.ruleName = ruleName;
		
		try {
			ClassLoader cl = ClassLoader.getSystemClassLoader();
			Object obj = cl.loadClass(detectorName).newInstance();
			
			if (obj instanceof RuleDetector) {
				this.ruleDetector = (RuleDetector) obj;
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 룰 탐지를 실행하는 메소드
	 * @param userId
	 * @return
	 */
	public boolean detect(Long userId, IUserLogDAO dao) {
		return this.ruleDetector.detect(userId, dao);
	}
	
	/**
	 * 룰 이름을 리턴하는 메소드
	 * @return
	 */
	public String getRuleName() {
		return this.ruleName;
	}
}
