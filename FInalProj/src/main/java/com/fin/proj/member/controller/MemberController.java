package com.fin.proj.member.controller;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.fin.proj.common.Pagination;
import com.fin.proj.common.model.vo.PageInfo;
import com.fin.proj.member.model.exception.MemberException;
import com.fin.proj.member.model.service.AuthService;
import com.fin.proj.member.model.service.MemberService;
import com.fin.proj.member.model.vo.Member;
import com.fin.proj.support.model.vo.Support;

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
				return "redirect:/editUserInfo.me";
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
	public String updateMyInfo(@ModelAttribute Member m, HttpSession session,
			   				   @RequestParam(value="emailId") String emailId,
			   				   @RequestParam("emailDomain") String emailDomain, Model model) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		m.setuNo(loginUser.getuNo());
		m.setuId(loginUser.getuId());
		m.setKakaoId(loginUser.getKakaoId());
		m.setuStatus(loginUser.getuStatus());
		
		if(!emailId.trim().equals("")) {
			m.setEmail(emailId + "@" + emailDomain);
		} else {
			m.setEmail(null);
		}
		
		int result = mService.updateMyInfo(m);
		System.out.println(loginUser);
		System.out.println(m);
		
		if(result > 0) {
			if(m.getKakaoId() == null) {
				System.out.println("일반네요");
				model.addAttribute("loginUser", mService.login(m));
				return "redirect:/editMyInfo.me";
			} else {
				System.out.println("카카오네요");
				model.addAttribute("loginUser", mService.kakaoLogin(m));
				return "redirect:/editMyInfo.me";
			}
		} else {
			throw new MemberException("회원정보 수정 실패");
		}	
	}
	
	@RequestMapping(value="deleteUser.me")
	public String deleteUser(Model model) {
		
		String uNo = ((Member)model.getAttribute("loginUser")).getuNo()+"";
		
		int result = mService.deleteUser(uNo);
		
		if(result > 0) {
			return "redirect:/logout.me";
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
		
		Integer uNo = ((Member)model.getAttribute("loginUser")).getuNo();
		m.setuNo(uNo);
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
	public void loginFailCount(Member m, @RequestParam("uId") String uId, Model model, PrintWriter out) {
		
		Date now = new Date();
		Timestamp timestamp = new Timestamp(now.getTime());
		
		int result = mService.loginFailCount(uId);
			
		if(result >= 5) {
			Member failUser = mService.loginFailDate(timestamp);
			System.out.println(failUser);
		}
		out.print(result);	
	}
	
	@RequestMapping("editUserInfo.me")
	public String editUserInfo(@RequestParam(value = "page", required = false) Integer currentPage, Model model) {

		if (currentPage == null) {
			currentPage = 1;
		}

		int listCount = mService.getListCount();

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		ArrayList<Member> mList = mService.selectUserList(pi);

		if (mList != null) {
			model.addAttribute("pi", pi);
			model.addAttribute("mList", mList);
			return "editUserInfo";
		} else {
			throw new MemberException("회원 목록 조회에 실패했습니다.");
		}
	}
	
	@RequestMapping("userInfoDetail.me")
	public String userInfoDetail(@RequestParam(value = "page", required = false) Integer currentPage,
								 @RequestParam("uNo") int uNo, Model model) {
		
		if (currentPage == null) {
			currentPage = 1;
		}

		int listCount = mService.getUserListCount(uNo);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		Member uList = mService.selectUserListEach(pi, uNo);
		model.addAttribute("uList", uList);
		model.addAttribute("pi", pi);
		model.addAttribute("uNo", uNo);
		
		return "userInfoDetail";
	}
	
	@RequestMapping("categoryListAdmin.me")
	public String categoryListAdmin(@RequestParam("category") String category,
									@RequestParam(value = "page", required = false) Integer currentPage,
									@RequestParam(value = "searchWord", required=false) String searchWord,
									Model model) {
		
		System.out.println(category);
		System.out.println(searchWord);
		
		if (currentPage == null) {
			currentPage = 1;
		}
		
		Member m = new Member();
		
		if(searchWord == null || searchWord.trim().equals("")) {
			switch (category) {
	        case "활동중인 회원":
	            ArrayList<Member> suList = mService.statusUserList();
	            model.addAttribute("suList", suList);
	            break;

//	        case "관리자인 회원":
//	            ArrayList<Member> auList = mService.adminUserList();
//	            model.addAttribute("auList", auList);
//	            break;
//
//	        case "기관 승인":
//	            ArrayList<Member> vuList = mService.volUserList();
//	            model.addAttribute("vuList", vuList);
//	            break;

	        default: break;
			}
	        
		} else {
			switch (category) {
//	        case "활동중인 회원":
//	            ArrayList<Member> suList = mService.statusUserListWithSearch(searchWord.trim());
//	            model.addAttribute("suList", suList);
//	            break;
//
//	        case "관리자인 회원":
//	            ArrayList<Member> auList = mService.adminUserListWithSearch(searchWord.trim());
//	            model.addAttribute("auList", auList);
//	            break;
//
//	        case "기관 승인":
//	            ArrayList<Member> vuList = mService.volUserListWithSearch(searchWord.trim());
//	            model.addAttribute("vuList", vuList);
//	            break;

	        default: break;
			}
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
    	map.put("category", category);
    	map.put("searchWord", searchWord);
    	map.put("uNo", m.getuNo()+"");
		
		int listCount = mService.getCategoryCount(map);

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		
		ArrayList<Member> uList = mService.selectCategoryListAdmin(pi, m);

		if(searchWord == null || searchWord.trim().equals("")) {			
			model.addAttribute("uList", uList);
			model.addAttribute("pi", pi);
			model.addAttribute("category", category);
			
			return "editUserInfo";
		} else {
			model.addAttribute("uList", uList);
			model.addAttribute("pi", pi);
			model.addAttribute("category", category);
			model.addAttribute("searchWord", searchWord);
			
			return "editUserInfo";
		}	

	}
		
}