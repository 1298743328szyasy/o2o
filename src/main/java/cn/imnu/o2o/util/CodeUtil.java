package cn.imnu.o2o.util;

import javax.servlet.http.HttpServletRequest;

public class CodeUtil {
	public static boolean checkVerifyCode(HttpServletRequest request) {
		//前台显示验证码
		String verifyCodeExpected=(String)request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_CONFIG_KEY);
		//输入验证码
		String verifyCodeActual = HttpServletRequestUtil.getString(request,"verifyCodeActual");
		if(verifyCodeActual==null||!verifyCodeActual.equals(verifyCodeExpected)) {
			return false;
		}
		return true;
	}
}
