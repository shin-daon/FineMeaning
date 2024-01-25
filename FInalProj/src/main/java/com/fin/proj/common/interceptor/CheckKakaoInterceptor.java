package com.fin.proj.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import com.fin.proj.user.model.vo.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CheckKakaoInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
		User loginUser = (User)session.getAttribute("loginUser");
		
		if( loginUser.getuId() == null ) {
						
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write("<script>alert('카카오 회원은 비밀번호를 변경 할 수 없습니다.'); location.href='editMyInfo.us';</script>");
			
			return false;
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}