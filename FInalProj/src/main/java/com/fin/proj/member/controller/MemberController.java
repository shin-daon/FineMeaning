package com.fin.proj.member.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.fin.proj.common.Pagination;
import com.fin.proj.common.model.vo.PageInfo;
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
		
		int statusEmail = mService.checkEmail(emailAddress);
		
		if(statusEmail != 0) {
			String result = "registered email";
			out.print(result);
		} else {
			int count = aService.checkEmail(emailAddress);
		
			String result = Integer.toString(count);
			out.print(result);
		}	
			
	}

	@PostMapping("updateMyInfo.me")
	public String updateMyInfo(@ModelAttribute Member m, HttpSession session,
			   				   @RequestParam(value="emailId") String emailId,
			   				   @RequestParam("emailDomain") String emailDomain, Model model) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		m.setuNo(loginUser.getuNo());
		m.setuId(loginUser.getuId());
		m.setLoginType(loginUser.getLoginType());
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
			if(m.getLoginType() == "일반" || m.getKakaoId() == null) {
				model.addAttribute("loginUser", mService.login(m));
				return "redirect:/editMyInfo.me";
			} else {
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
		
		System.out.println("findId 폼에서 넘어온 정보 : " + m);
		
		String email = emailId + "@" + emailDomain;
		String uName = m.getuName();
		String phone = m.getPhone();
		
		m.setEmail(email);
		m.setuName(uName);
		m.setPhone(phone);
		
		System.out.println("m에 값을 넣음 (이름, 이메일, 폰) : " + m);
		
		Member foundUser = mService.searchUser(m);
	
		if(foundUser != null) {
			model.addAttribute("foundUser", foundUser);
			System.out.println("foundUser : " + foundUser);
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
		System.out.println(uList);
		model.addAttribute("uList", uList);
		model.addAttribute("pi", pi);
		model.addAttribute("uNo", uNo);
		
		return "userInfoDetail";
	}
	
	@RequestMapping("categoryListAdmin.me")
	public String categoryListAdmin(@RequestParam("category") String category,
									@RequestParam(value = "searchWord", required=false) String searchWord,
									@RequestParam(value = "page", required = false) Integer currentPage,
									Model model) {
		
		System.out.println(category);
		
		if (currentPage == null) {
			currentPage = 1;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("category", category);
		map.put("searchWord", searchWord);
		
		int listCount = mService.getCategoryCount(map);
		System.out.println(searchWord);
		System.out.println(listCount);
		System.out.println(map);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		
		Member m = new Member();
		ArrayList<Member> mList = mService.selectCategoryListAdmin(pi, map);
		System.out.println(mList);
		
		
		if(searchWord == null || searchWord.trim().equals("")) {
			model.addAttribute("mList", mList);
			model.addAttribute("pi", pi);
			model.addAttribute("category", category);
			return "editUserInfo";
		} else {
			model.addAttribute("mList", mList);
			model.addAttribute("pi", pi);
			model.addAttribute("category", category);
			model.addAttribute("searchWord", searchWord);
			return "editUserInfo";
		}

	}
	
	@RequestMapping("searchUserListAdmin.me")
	public String searchUserListAdmin(@RequestParam(value = "searchWord", required=false) String searchWord,
									  @RequestParam(value = "page", required = false) Integer currentPage, Model model) {
	
		if (currentPage == null) {
			currentPage = 1;
		}
		
		int listCount = mService.getSearchListCount(searchWord.trim());

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		ArrayList<Member> mList = mService.selectSearchListAdmin(pi, searchWord.trim());
		model.addAttribute("pi", pi);
		model.addAttribute("mList", mList);
		model.addAttribute("searchWord", searchWord.trim());
		return "editUserInfo";
		
	}
	
	@GetMapping("updateUserInfo.me")
	@ResponseBody
	public String updateUserInfo(@RequestParam("column") String column,
								 @RequestParam("data") String data, @RequestParam("uNo") Integer uNo) {
		
		if (column.equals("이름")) {
		    column = "u_name";
		} else if (column.equals("생년월일")) {
		    column = "resident_no";
		} else if (column.equals("주소")) {
		    column = "address";
		} else if (column.equals("전화번호")) {
		    column = "phone";
		} else if (column.equals("이메일")) {
			column = "email";
		} else if (column.equals("닉네임")) {
			column = "u_nickname"; 
		} else if (column.equals("회원 유형")) {
			column = "u_type";
		} else if (column.equals("등록 기관")) {
			column = "registrar";
		} else if (column.equals("관리자 여부")) {
			column = "is_admin";
			
			if (data.equals("일반")) {
				data = "1";
			} else if (data.equals("관리자")) {
				data = "0";
			} else if (data.equals("봉사 관리자")) {
				data = "2";
			}
		}

		Properties prop = new Properties();
		prop.setProperty("column", column);
		prop.setProperty("data", data);
		prop.setProperty("uNo", uNo+"");
		
		System.out.println(prop);
		
		int result = mService.updateUserInfo(prop);
		
		return result == 1 ? "success" : "fail";
		
	}
	
	@GetMapping("updateState.me")
	@ResponseBody
	public String updateState(@RequestParam("status") String status, @RequestParam("uNo") Integer uNo) {
		
		Properties prop = new Properties();
		prop.setProperty("status", status);
		prop.setProperty("uNo", uNo+"");
		System.out.println(prop);
		int result = mService.updateState(prop);
		
		return result == 1 ? "success" : "fail";
		
	}
}