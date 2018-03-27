package com.kakao.rule.ruleset;

import com.kakao.fds.dao.IUserLogDAO;

public interface RuleDetector {

	public boolean detect(long userId, IUserLogDAO dao);
}
