package com.fin.proj.member.controller;

import java.io.PrintWriter;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.fin.proj.member.model.exception.MemberException;
import com.fin.proj.member.model.service.AuthService;
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
	
	@Autowired
	private AuthService aService;
	
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
	
	@PostMapping("insertUser.me")
	public String insertUser(@ModelAttribute Member m,
			   				 @RequestParam("emailId") String emailId,
			   				 @RequestParam("emailDomain") String emailDomain) {
		
		if(!emailId.trim().equals("")) {
			m.setEmail(emailId + "@" + emailDomain);
		}
		
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
	
	@RequestMapping("findIdResult.me")
	public String findIdResult() {		
		return "findIdResult";
	}
	
	@RequestMapping("findPwd.me")
	public String findPwd() {
		return "findPwd";
	}
	
	@RequestMapping("findPwdResult.me")
	public String findPwdResult() {
	    return "findPwdResult";
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
	
	@RequestMapping("editMyPwd.me")
	public String editMyPwd() {
		return "editMyPwd";
	}
	
	@RequestMapping("userInfoDetail.me")
	public String userInfoDetail() {
		return "userInfoDetail";
	}
	
	@RequestMapping(value="checkId.me") 
	public void checkId(@RequestParam("uId") String uId, PrintWriter out) {
		int count = mService.checkId(uId);
			
		String result = count == 0 ? "yes" : "no";
		out.print(result);
			
	}
	
	@RequestMapping(value="checkNickName.me") 
	public void checkNickName(@RequestParam("uNickName") String uNickName, PrintWriter out) {
		int count = mService.checkNickName(uNickName);
		
		String result = count == 0 ? "yes" : "no";
		out.print(result);	
	}
	
	@RequestMapping(value="checkEmail.me") 
	public void checkEmail(@RequestParam("emailAddress") String emailAddress, PrintWriter out) {
		
		System.out.println("보낼 이메일 : " + emailAddress);
		int count = aService.checkEmail(emailAddress);
		
		String result = Integer.toString(count);
		out.print(result);	
	}

	@PostMapping("updateMyInfo.me")
	public String updateMyInfo(@ModelAttribute Member m,
			   				   @RequestParam(value="emailId") String emailId,
			   				   @RequestParam("emailDomain") String emailDomain, Model model) {
		
		if(!emailId.trim().equals("")) {
			m.setEmail(emailId + "@" + emailDomain);
		} else {
			m.setEmail(null);
		}

		int result = mService.updateMyInfo(m);
		
		if(result > 0) {
			model.addAttribute("loginUser", mService.login(m));
			return "redirect:/editMyInfo.me";
		} else {
			throw new MemberException("회원정보 수정 실패");
		}	
	}
	
	@RequestMapping(value="deleteUser.me")
	public String deleteUser(Model model) {
		
		String uId = ((Member)model.getAttribute("loginUser")).getuId();
		
		int result = mService.deleteUser(uId);
		
		if(result > 0) {
			return "redirect:logout.me";
		} else {
			throw new MemberException("회원 탈퇴에 실패하였습니다.");
		}
	}
	
	@RequestMapping(value="checkLogin.me")
	public void checkLogin(Member m, @RequestParam("uId") String uId, @RequestParam("uPwd") String uPwd, PrintWriter out) {
		
		m.setuId(uId);
		Member loginUser = mService.login(m);
		
		int count = mService.checkId(uId);
		
		if(count == 0) {
			out.print(count);
		} else {
			if(!bcrypt.matches(uPwd, loginUser.getuPwd())) {
				out.print(count);
			}
		}
	}
	
	@RequestMapping(value="checkNickNameModify.me") 
	public void checkNickNameModify(Member m, Model model, @RequestParam("uNickName") String uNickName, PrintWriter out) {
		
		String uId = ((Member)model.getAttribute("loginUser")).getuId();
		m.setuId(uId);
		m.setuNickName(uNickName);
		
		int count = mService.checkNickNameModify(m);
		
		String result = count == 0 ? "yes" : "no";
		out.print(result);	
	}
	
	@RequestMapping(value="checkPwd.me")
	public void checkPwd(Member m, Model model, @RequestParam("uPwd") String uPwd, PrintWriter out) {
		
		String uId = ((Member)model.getAttribute("loginUser")).getuId();
		String password = mService.selectPwd(uId);
		
		String result = null;
		
		if(bcrypt.matches(uPwd, password)) {
			result = "yes";
		} else {
			result = "no";
		}
			
		out.print(result);			
	}
	
	@PostMapping("updateMyPwd.me")
	public String updateMyPwd(HttpSession session,
			   				  @RequestParam("uPwd") String uPwd,
			   				  @RequestParam("newPwd") String newPwd,
			   				  Model model) {
		
		Member m = (Member)model.getAttribute("loginUser");
		String uId = m.getuId();
	      
	    if(bcrypt.matches(uPwd, m.getuPwd())) {
	    	HashMap<String, String> map = new HashMap<String, String>();
	    	map.put("uId", m.getuId());
	    	map.put("newPwd", bcrypt.encode(newPwd));
	    	  
	    	int result = mService.updatePwd(map);
	    	
	    	if(result > 0) {
	    		model.addAttribute("loginUser", mService.login(m));
	    		return "redirect:/";
	    	} else {
	    		throw new MemberException("비밀번호 변경에 실패하였습니다.");
	    	}
	    	
	     } else {
	    	 throw new MemberException("비밀번호 변경에 실패하였습니다.");
	     }
	}
	
	@RequestMapping(value="findMyId.me") 
	public void findMyId(@RequestParam("uName") String uName,
					   @RequestParam("emailAddress") String emailAddress, PrintWriter out) {
		
		System.out.println("이름 : " + uName);
		System.out.println("보낼 이메일 : " + emailAddress);
		
		HashMap<String, String> map = new HashMap<String, String>();
    	map.put("uName", uName);
    	map.put("email", emailAddress);
    	  
    	int count = mService.searchEmailUser(map);
		String result = count == 0 ? "no" : "yes";
    	
    	int randomNum = 0;    	
    	if(count > 0) {
    		randomNum = aService.findId(emailAddress);
    	}
    	
    	out.print(result + "," + randomNum);
    	
	}
	
	@PostMapping("findIdForm.me")
	public String findIdForm(@ModelAttribute Member m,
							 @RequestParam("emailId") String emailId,
							 @RequestParam("emailDomain") String emailDomain, Model model) {
		
		String email = emailId + "@" + emailDomain;
		String uName = m.getuName();
		String phone = m.getPhone();
		
		m.setEmail(email);
		m.setuName(uName);
		m.setPhone(phone);
		
		Member foundUser = mService.searchUser(m);
		
		if(foundUser != null) {
			model.addAttribute("foundUser", foundUser);
			System.out.println(foundUser);
			return "findIdResult";
		} else {
			throw new MemberException("아이디 찾기에 실패하였습니다.");
		}		
	}
	
	@RequestMapping(value="findMyPwd.me") 
	public void findMyPwd(@RequestParam("uId") String uId,
					      @RequestParam("emailAddress") String emailAddress, PrintWriter out) {
		
		System.out.println("아이디 : " + uId);
		System.out.println("보낼 이메일 : " + emailAddress);
		
		HashMap<String, String> map = new HashMap<String, String>();
    	map.put("uId", uId);
    	map.put("email", emailAddress);
    	  
    	int count = mService.searchEmailUser2(map);
		String result = count == 0 ? "no" : "yes";
    	
    	int randomNum = 0;    	
    	if(count > 0) {
    		randomNum = aService.findPwd(emailAddress);
    	}
    	
    	out.print(result + "," + randomNum);  	
	}
	
	@PostMapping("findPwdForm.me")
	public String findPwdForm(@ModelAttribute Member m, HttpSession session,
							  @RequestParam("emailId") String emailId,
							  @RequestParam("emailDomain") String emailDomain,
							  @RequestParam("phone") String phone, Model model) {
		
		String email = emailId + "@" + emailDomain;
		String uId = m.getuId();
		
		m.setEmail(email);
		m.setPhone(phone);
		m.setuId(uId);
		
		Member foundUser = mService.searchUserPwd(m);
		
		if(foundUser != null) {
			session.setAttribute("foundUser", foundUser);
			return "findPwdResult";
		} else {
			throw new MemberException("비밀번호 찾기에 실패하였습니다.");
		}
	}
	
	@PostMapping("changePwd.me")
	public String changePwd(HttpSession session, @RequestParam("newPwd") String newPwd, Model model) {
		
		Member foundUser = (Member) session.getAttribute("foundUser");
		
		System.out.println("비번변경:" + foundUser);
		
		String changePwd = bcrypt.encode(newPwd);
		foundUser.setuPwd(changePwd);
	      
    	HashMap<String, String> map = new HashMap<String, String>();
    	map.put("uId", foundUser.getuId());
    	map.put("newPwd", bcrypt.encode(newPwd));
	    	  
    	int result = mService.updatePwd(map);
    	
    	if(result > 0) {
    		session.setAttribute("foundUser", foundUser);
    		return "redirect:/";
    	} else {
    		throw new MemberException("비밀번호 변경에 실패하였습니다.");    	
	    }
	}
	
	@RequestMapping(value="findMyId2.me") 
	public void findMyId2(@RequestParam("uName") String uName,
					      @RequestParam("phone") String phone, PrintWriter out) {
		
		System.out.println("이름 : " + uName);
		System.out.println("보낼 전화번호 : " + phone);
		
		HashMap<String, String> map = new HashMap<String, String>();
    	map.put("uName", uName);
    	map.put("phone", phone);
    	  
    	int count = mService.searchPhoneUser(map);
		String result = count == 0 ? "no" : "yes";
    	
    	int randomNum = 0;    	
    	if(count > 0) {
    		randomNum = aService.findBySms(phone);
    	}
    	
    	out.print(result + "," + randomNum);   	
	}
	
	@RequestMapping(value="findMyPwd2.me")
	public void findMyPwd2(@RequestParam("uId") String uId,
					       @RequestParam("phone") String phone, PrintWriter out) {
		
		System.out.println("아이디 : " + uId);
		System.out.println("보낼 전화번호 : " + phone);
		
		HashMap<String, String> map = new HashMap<String, String>();
    	map.put("uId", uId);
    	map.put("phone", phone);
    	  
    	int count = mService.searchPhoneUser2(map);
		String result = count == 0 ? "no" : "yes";
    	
    	int randomNum = 0;    	
    	if(count > 0) {
    		randomNum = aService.findBySms(phone);
    	}
    	
    	out.print(result + "," + randomNum);  	
	}
	
	@RequestMapping(value="loginFailCount.me")
	public void loginFailCount(Member m, @RequestParam("uId") String uId, PrintWriter out) {
		
		int count = mService.loginFailCount(uId);
		
		String result = count == 0 ? "yes" : "no";		
		out.print(count);	
	}	
}