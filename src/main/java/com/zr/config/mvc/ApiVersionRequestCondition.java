package com.zr.config.mvc;

import lombok.EqualsAndHashCode;
import org.springframework.web.servlet.mvc.condition.AbstractRequestCondition;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EqualsAndHashCode
public class ApiVersionRequestCondition extends AbstractRequestCondition<ApiVersionRequestCondition> {

	private final static Pattern VERSION_PATTERN = Pattern.compile("v(\\d+)"); // 版本号正则表达式

	private final int apiVersion; // 接口版本号

	public ApiVersionRequestCondition(int apiVersion) {
		this.apiVersion = apiVersion;
	}

	@Override
	protected Collection<?> getContent() {
		return Collections.emptyList();
	}

	@Override
	protected String getToStringInfix() {
		return "";
	}

	@Override
	public ApiVersionRequestCondition combine(ApiVersionRequestCondition other) {
		return new ApiVersionRequestCondition(other.apiVersion);
	}

	@Override
	public ApiVersionRequestCondition getMatchingCondition(HttpServletRequest request) {
		Matcher matcher = VERSION_PATTERN.matcher(request.getHeader("X-Api-Version"));
		if (matcher.find()) {
			int version = Integer.parseInt(matcher.group(1));
			if (version == this.apiVersion) { // 当前版本大于等于请求的版本，则进行匹配
				return this;
			}
		}
		return null;
	}

	/**
	 * 排优逻辑
	 * @param other
	 * @param request
	 * @return
	 */
	@Override
	public int compareTo(ApiVersionRequestCondition other, HttpServletRequest request) {
		return other.apiVersion - this.apiVersion;
	}
}
