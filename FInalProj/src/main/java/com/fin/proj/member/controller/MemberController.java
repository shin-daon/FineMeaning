package com.fin.proj.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MemberController {
	
	@RequestMapping("login.me")
	public String login() {
		return "login";
	}
	
	@RequestMapping("enroll.me")
	public String enroll() {
		return "enroll";
	}
	
	@RequestMapping("findId.me")
	public String findId() {
		return "findId";
	}
	
	@RequestMapping("findPwd.me")
	public String findPwd() {
		return "findPwd";
	}

}
