package com.fin.proj.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fin.proj.member.model.exception.MemberException;
import com.fin.proj.member.model.service.MemberService;
import com.fin.proj.member.model.vo.Member;

import jakarta.servlet.http.HttpSession;

@SessionAttributes("loginUser")
@Controller
public class MemberController {
	
	@Autowired
	private MemberService mService;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@RequestMapping("loginView.me")
	public String loginView() {
		return "login";
	}
	
	@PostMapping("login.me")
	public String login(Member m, Model model, HttpSession session) {
		
		Member loginUser = mService.login(m);
		
		if(bcrypt.matches(m.getuPwd(), loginUser.getuPwd())) {
			model.addAttribute("loginUser", loginUser);
			System.out.println("로그인 성공");
			return "redirect:/";
		} else {
			throw new MemberException("로그인 실패");
		}
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
