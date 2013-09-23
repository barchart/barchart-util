package com.barchart.util.hocon;

import com.google.common.base.CaseFormat;

public class DefaultNameMorpher implements NameMorpher {

	@Override
	public String getConfigPath(String methodName) {
		CaseFormat startingCase;
		if (methodName.startsWith("get")) {
			methodName = methodName.substring(3, methodName.length());
			startingCase = CaseFormat.UPPER_CAMEL;
		} else if (methodName.startsWith("is")) {
			methodName = methodName.substring(2, methodName.length());
			startingCase = CaseFormat.UPPER_CAMEL;
		} else {
			startingCase = CaseFormat.LOWER_CAMEL;
		}

		return startingCase.to(CaseFormat.LOWER_UNDERSCORE, methodName);
	}
}
