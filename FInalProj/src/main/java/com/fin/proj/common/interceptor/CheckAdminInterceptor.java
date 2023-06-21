package com.fin.proj.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import com.fin.proj.member.model.vo.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CheckAdminInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		HttpSession session = request.getSession();
		Member loginUser = (Member)session.getAttribute("loginUser");
		
		if( loginUser== null || loginUser.getIsAdmin().equals("N") ) {
						
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write("<script>alert('잘못된 접근입니다.'); location.href='loginView.me';</script>");
			
			return false;
		}
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
	
}
