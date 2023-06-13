package com.fin.proj.member.controller;

import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

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
			
			if(loginUser.getIsAdmin() == 0) {
				return "editUserInfo";
			} else {
				return "redirect:/";
			}
			
		} else {
			throw new MemberException("로그인 실패");
		}
	}
	
	@RequestMapping("enroll.me")
	public String enroll() {
		return "enroll";
	}
	
	@GetMapping("insertUser.me")
	public String insertUser(@ModelAttribute Member m,
			   				 @RequestParam("emailId") String emailId,
			   				 @RequestParam("emailDomain") String emailDomain,
			   				 @RequestParam("first-ssn") String firstSsn,
			   				 @RequestParam("two-ssn") String twoSsn) {
		
		if(!emailId.trim().equals("")) {
			m.setEmail(emailId + "@" + emailDomain);
		}
		
		m.setResidentNo(firstSsn + "-" + twoSsn);
		
		String encPwd = bcrypt.encode(m.getuPwd());
		m.setuPwd(encPwd);

		int result = mService.insertUser(m);
		
		if(result > 0) {
			return "redirect:/";
		} else {
			throw new MemberException("회원가입 실패");
		}	
	}
	
	@RequestMapping("findId.me")
	public String findId() {
		return "findId";
	}
	
	@RequestMapping("findPwd.me")
	public String findPwd() {
		return "findPwd";
	}
	
	@RequestMapping("logout.me")
	public String logout(SessionStatus status) {
		status.setComplete();
		return "redirect:/";
	}
	
	@RequestMapping("editMyInfo.me")
	public String editMyInfo() {
		return "editMyInfo";
	}
	
	@GetMapping("checkId.me")
	public void checkId(@RequestParam("uId") String uId, PrintWriter out) {
		int count = mService.checkId(uId);
			
		String result = count == 0 ? "yes" : "no";
		out.print(result);
			
	}
	
	@GetMapping("checkNickName.me")
	public void checkNickName(@RequestParam("uNickName") String uNickName, PrintWriter out) {
		int count = mService.checkNickName(uNickName);
		
		String result = count == 0 ? "yes" : "no";
		out.print(result);	
	}
}
