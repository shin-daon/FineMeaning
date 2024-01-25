package com.fin.proj.user.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.fin.proj.common.Pagination;
import com.fin.proj.common.model.vo.PageInfo;
import com.fin.proj.user.model.exception.UserException;
import com.fin.proj.user.model.service.AuthService;
import com.fin.proj.user.model.service.UserService;
import com.fin.proj.user.model.vo.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@SessionAttributes("loginUser")
@Controller
public class UserController {
	
	@Autowired
	private UserService uService;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Autowired
	private AuthService aService;
	
	/*
	 * 로그인 & 로그아웃
	 */
	@RequestMapping("loginView.us")
	public String loginView() {
		return "login";
	}
	
	@PostMapping("login.us")
	public String login(User u, Model model, HttpSession session, 
						@RequestParam("beforeURL") String beforeURL, HttpServletResponse response) {
		
		User loginUser = uService.login(u);
		if(bcrypt.matches(u.getuPwd(), loginUser.getuPwd())) {
			model.addAttribute("loginUser", loginUser);
			
			if(loginUser.getIsAdmin() == 0) {
				return "redirect:/editUserInfo.us";
			} else if(beforeURL.contains("enroll.us")) {
				return "redirect:/";
			} else if(beforeURL.toLowerCase().contains("insert") || beforeURL.toLowerCase().contains("update")){
				try {
					response.setContentType("text/html; charset=UTF-8");
					response.getWriter().write("<script>location.href=history.go(-2);</script>");
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			} else {
				return "redirect:" + beforeURL;
			}
			
		} else {
			throw new UserException("로그인 실패");
		}
	}
	
	@RequestMapping(value="checkLogin.us") //특정 회원 조회, 로그인 시도시 해당 회원이 없을 경우 로그인 불가
	public void checkLogin(User u, @RequestParam("uId") String uId, @RequestParam("uPwd") String uPwd, PrintWriter out) {
		
		u.setuId(uId);
		User loginUser = uService.login(u);
		
		int count = uService.checkLogin(uId);
		
		if(count == 0) {
			out.print(count);
		} else {
			if(!bcrypt.matches(uPwd, loginUser.getuPwd())) {
				out.print(count);
			}
		}
	}
	
	@RequestMapping("logout.us")
	public String logout(SessionStatus status) {
		status.setComplete();
		return "redirect:/";
	}
	
	/*
	 * 회원가입
	 */
	@RequestMapping("enroll.us")
	public String enroll() {
		return "enroll";
	}
	
	@RequestMapping(value="checkId.us") // 아이디 유효성 검사
	public void checkId(@RequestParam("uId") String uId, PrintWriter out) {
		int count = uService.checkId(uId);
			
		String result = count == 0 ? "yes" : "no";
		out.print(result);
			
	}
	
	@RequestMapping(value="checkNickName.us") // 닉네임 유효성 검사 (카카오 가입때도 사용)
	public void checkNickName(@RequestParam("uNickName") String uNickName, PrintWriter out) {
		int count = uService.checkNickName(uNickName);
		
		String result = count == 0 ? "yes" : "no";
		out.print(result);	
	}
	
	@RequestMapping(value="checkEmail.us") // 이메일 전송
	public void checkEmail(@RequestParam("emailAddress") String emailAddress, PrintWriter out) {
		int count = aService.sendEnrollMail(emailAddress);
		
		String result = Integer.toString(count);
		out.print(result);	
			
	}
	
	@PostMapping("insertUser.us")
	public String insertUser(@ModelAttribute User u,
			   				 @RequestParam("emailId") String emailId,
			   				 @RequestParam("emailDomain") String emailDomain) {
		
		if(!emailId.trim().equals("")) {
			u.setEmail(emailId + "@" + emailDomain);
		}
		
		String encPwd = bcrypt.encode(u.getuPwd());
		u.setuPwd(encPwd);

		int result = uService.insertUser(u);
		
		if(result > 0) {
			return "redirect:/";
		} else {
			throw new UserException("회원가입 실패");
		}	
	}
	
	/*
	 * 아이디 찾기
	 */
	@RequestMapping("findId.us")
	public String findId() {
		return "findId";
	}
	
	@RequestMapping("findIdResult.us")
	public String findIdResult() {		
		return "findIdResult";
	}
	
	@RequestMapping(value="findMyIdByEmail.us") // 해당 이메일을 사용하는 유저 조회
	public void findMyIdByEmail(@RequestParam("uName") String uName,
					     @RequestParam("emailAddress") String emailAddress, PrintWriter out) {
		
		HashMap<String, String> map = new HashMap<String, String>();
    	map.put("uName", uName);
    	map.put("email", emailAddress);
    	  
    	int count = uService.searchEmailUser(map);
		String result = count == 0 ? "no" : "yes";
    	
    	int randomNum = 0;    	
    	if(count > 0) {
    		randomNum = aService.sendFindUserMail(emailAddress);
    	}
    	
    	out.print(result + "," + randomNum);
    	
	}
	
	@RequestMapping(value="findMyIdByPhone.me") // 해당 폰 번호를 사용하는 유저 조회
	public void findMyIdByPhone(@RequestParam("uName") String uName,
					      @RequestParam("phone") String phone, PrintWriter out) {
	
		HashMap<String, String> map = new HashMap<String, String>();
    	map.put("uName", uName);
    	map.put("phone", phone);
    	  
    	int count = uService.searchPhoneUser(map);
		String result = count == 0 ? "no" : "yes";
    	
    	int randomNum = 0;    	
    	if(count > 0) {
    		randomNum = aService.sendFindUserSms(phone);
    	}
    	
    	out.print(result + "," + randomNum);   	
	}
	
	@PostMapping("findUserId.me")
	public String findUserId(@ModelAttribute User u,
							 @RequestParam("emailId") String emailId,
							 @RequestParam("emailDomain") String emailDomain, Model model) {
		
		String email = emailId + "@" + emailDomain;
		
		email = email.replace(",", "");
	
		String uName = u.getuName();
		String phone = u.getPhone();
		
		u.setEmail(email);
		u.setuName(uName);
		u.setPhone(phone);

		ArrayList<User> foundUser = uService.searchUser(u);
	
		if(foundUser != null) {
			model.addAttribute("foundUser", foundUser);
			return "findIdResult";
		} else {
			throw new UserException("아이디 찾기에 실패하였습니다.");
		}		
	}
	
	/*
	 * 비밀번호 찾기
	 */
	@RequestMapping("findPwd.us")
	public String findPwd() {
		return "findPwd";
	}
	
	@RequestMapping("findPwdResult.us")
	public String findPwdResult() {
	    return "findPwdResult";
	}
	
	@RequestMapping(value="findMyPwdByEmail.me")
	public void findMyPwdByEmail(@RequestParam("uId") String uId,
					      		 @RequestParam("emailAddress") String emailAddress, PrintWriter out) {
		
		HashMap<String, String> map = new HashMap<String, String>();
    	map.put("uId", uId);
    	map.put("email", emailAddress);
    	  
    	int count = uService.searchEmailUser2(map);
		String result = count == 0 ? "no" : "yes";
    	
    	int randomNum = 0;    	
    	if(count > 0) {
    		randomNum = aService.sendFindUserMail(emailAddress);
    	}
    	
    	out.print(result + "," + randomNum);  	
	}
	
	@RequestMapping(value="findMyPwdByPhone.me")
	public void findMyPwdByPhone(@RequestParam("uId") String uId,
					       		 @RequestParam("phone") String phone, PrintWriter out) {
		
		HashMap<String, String> map = new HashMap<String, String>();
    	map.put("uId", uId);
    	map.put("phone", phone);
    	  
    	int count = uService.searchPhoneUser2(map);
		String result = count == 0 ? "no" : "yes";
    	
    	int randomNum = 0;    	
    	if(count > 0) {
    		randomNum = aService.sendFindUserSms(phone);
    	}
    	
    	out.print(result + "," + randomNum);
	}
	
	@PostMapping("findUserPwd.us")
	public String findUserPwd(@ModelAttribute User u, HttpSession session,
							  @RequestParam("emailId") String emailId,
							  @RequestParam("emailDomain") String emailDomain,
							  @RequestParam("phone") String phone, Model model) {
		
		String email = emailId + "@" + emailDomain;
		email = email.replace(",", "");
		String uId = u.getuId();
		
		u.setEmail(email);
		u.setPhone(phone);
		u.setuId(uId);
		
		User foundUser = uService.searchUserPwd(u);
		
		if(foundUser != null) {
			session.setAttribute("foundUser", foundUser);
			return "findPwdResult";
		} else {
			throw new UserException("비밀번호 찾기에 실패하였습니다.");
		}
	}
	
	@PostMapping("changePwd.me") // 해당 유저의 비밀번호 재설정
	public String changePwd(HttpSession session, @RequestParam("newPwd") String newPwd, Model model) {
		
		User foundUser = (User) session.getAttribute("foundUser");
		
		System.out.println("비번변경:" + foundUser);
		
		String changePwd = bcrypt.encode(newPwd);
		foundUser.setuPwd(changePwd);
	      
    	HashMap<String, String> map = new HashMap<String, String>();
    	map.put("uId", foundUser.getuId());
    	map.put("newPwd", bcrypt.encode(newPwd));
	    	  
    	int result = uService.updatePwd(map);
    	
    	if(result > 0) {
    		session.setAttribute("foundUser", foundUser);
    		return "redirect:/";
    	} else {
    		throw new UserException("비밀번호 변경에 실패하였습니다.");    	
	    }
	}
	
	/*
	 * 내 정보 수정 & 회원 탈퇴
	 */
	@RequestMapping("editMyInfo.us")
	public String editMyInfo() {
		return "editMyInfo";
	}
	
	@RequestMapping(value="checkNickNameModify.us") // 닉네임 유효성 검사
	public void checkNickNameModify(User u, Model model, @RequestParam("uNickName") String uNickName, PrintWriter out) {
		
		Integer uNo = ((User)model.getAttribute("loginUser")).getuNo();
		u.setuNo(uNo);
		u.setuNickName(uNickName.trim());
		
		int count = uService.checkNickNameModify(u);
		
		String result = count == 0 ? "yes" : "no";
		out.print(result);	
	}
	
	@PostMapping("updateMyInfo.us")
	public String updateMyInfo(@ModelAttribute User u, HttpSession session,
			   				   @RequestParam(value="emailId") String emailId,
			   				   @RequestParam("emailDomain") String emailDomain, Model model) {
		
		User loginUser = (User)session.getAttribute("loginUser");
		u.setuNo(loginUser.getuNo());
		u.setuId(loginUser.getuId());
		u.setLoginType(loginUser.getLoginType());
		u.setKakaoId(loginUser.getKakaoId());
		u.setuStatus(loginUser.getuStatus());
		
		if(!emailId.trim().equals("")) {
			u.setEmail(emailId + "@" + emailDomain);
		} else {
			u.setEmail(null);
		}
		
		u.setuNickName(u.getuNickName().trim());
		
		int result = uService.updateMyInfo(u);
		
		if(result > 0) {
			if(u.getLoginType() == "일반" || u.getKakaoId() == null) {
				model.addAttribute("loginUser", uService.login(u));
				return "redirect:/editMyInfo.us";
			} else {
				model.addAttribute("loginUser", uService.kakaoLogin(u));
				return "redirect:/editMyInfo.us";
			}
		} else {
			throw new UserException("회원정보 수정 실패");
		}	
	}
	
	@RequestMapping(value="deleteUser.us")
	public String deleteUser(Model model) {
		
		String uNo = ((User)model.getAttribute("loginUser")).getuNo()+"";
		
		int result = uService.deleteUser(uNo);
		
		if(result > 0) {
			return "redirect:/logout.us";
		} else {
			throw new UserException("회원 탈퇴에 실패하였습니다.");
		}
	}
	
	/*
	 * 비밀번호 변경
	 */	
	@RequestMapping("editMyPwd.us")
	public String editMyPwd() {
		return "editMyPwd";
	}
	
	@RequestMapping(value="checkPwd.us") // 비밀번호 유효성 검사
	public void checkPwd(User u, Model model, @RequestParam("uPwd") String uPwd, PrintWriter out) {
		
		String uId = ((User)model.getAttribute("loginUser")).getuId();
		String password = ((User)model.getAttribute("loginUser")).getuPwd();
		
		int result = 0;
		
		if(bcrypt.matches(uPwd, password)) {
			result = 0;
		} else {
			result = 1;
		}
		
		out.print(result);			
	}
	
	@PostMapping("updateMyPwd.us")
	public String updateMyPwd(HttpSession session,
			   				  @RequestParam("uPwd") String uPwd,
			   				  @RequestParam("newPwd") String newPwd,
			   				  Model model) {
		
		User u = (User)model.getAttribute("loginUser");
		String uId = u.getuId();
	      
	    if(bcrypt.matches(uPwd, u.getuPwd())) {
	    	HashMap<String, String> map = new HashMap<String, String>();
	    	map.put("uId", u.getuId());
	    	map.put("newPwd", bcrypt.encode(newPwd));
	    	  
	    	int result = uService.updatePwd(map);
	    	
	    	if(result > 0) {
	    		model.addAttribute("loginUser", uService.login(u));
	    		return "redirect:/";
	    	} else {
	    		throw new UserException("비밀번호 변경에 실패하였습니다.");
	    	}
	    	
	     } else {
	    	 throw new UserException("비밀번호 변경에 실패하였습니다.");
	     }
	}
	
	/*
	 * 관리자 페이지 - 회원 목록 조회
	 */
	
	@RequestMapping("editUserInfo.us") // 페이징
	public String editUserInfo(@RequestParam(value = "page", required = false) Integer currentPage, Model model) {

		if (currentPage == null) {
			currentPage = 1;
		}

		int listCount = uService.getListCount();

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		ArrayList<User> uList = uService.selectUserList(pi);

		if (uList != null) {
			model.addAttribute("pi", pi);
			model.addAttribute("mList", uList);
			return "editUserInfo";
		} else {
			throw new UserException("회원 목록 조회에 실패했습니다.");
		}
	}
	
	@RequestMapping("categoryListAdmin.us") // 카테고리 검색
	public String categoryListAdmin(@RequestParam("category") String category,
									@RequestParam(value = "searchWord", required=false) String searchWord,
									@RequestParam(value = "page", required = false) Integer currentPage,
									Model model) {
		
		if (currentPage == null) {
			currentPage = 1;
		}
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("category", category);
		map.put("searchWord", searchWord);
		
		int listCount = uService.getCategoryCount(map);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		
		User u = new User();
		ArrayList<User> uList = uService.selectCategoryListAdmin(pi, map);
		
		
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
	
	@RequestMapping("searchUserListAdmin.us") // 단독 검색
	public String searchUserListAdmin(@RequestParam(value = "searchWord", required=false) String searchWord,
									  @RequestParam(value = "page", required = false) Integer currentPage, Model model) {
	
		if (currentPage == null) {
			currentPage = 1;
		}
		
		int listCount = uService.getSearchListCount(searchWord.trim());

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		ArrayList<User> uList = uService.selectSearchListAdmin(pi, searchWord.trim());
		model.addAttribute("pi", pi);
		model.addAttribute("uList", uList);
		model.addAttribute("searchWord", searchWord.trim());
		return "editUserInfo";
		
	}
	
	@GetMapping("updateState.us") // 회원 상태 변경
	@ResponseBody
	public String updateState(@RequestParam("status") String status, @RequestParam("uNo") Integer uNo) {
		
		Properties prop = new Properties();
		prop.setProperty("status", status);
		prop.setProperty("uNo", uNo+"");
		int result = uService.updateState(prop);
		
		return result == 1 ? "success" : "fail";
		
	}
	
	/*
	 * 관리자 페이지 - 특정 회원 조회
	 */	
	@RequestMapping("userInfoDetail.us")
	public String userInfoDetail(@RequestParam(value = "page", required = false) Integer currentPage,
								 @RequestParam("uNo") int uNo, Model model) {
		
		if (currentPage == null) {
			currentPage = 1;
		}

		int listCount = uService.getUserListCount(uNo);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		User uList = uService.selectUserListEach(pi, uNo);
		model.addAttribute("uList", uList);
		model.addAttribute("pi", pi);
		model.addAttribute("uNo", uNo);
		
		return "userInfoDetail";
	}
	
	@GetMapping("updateUserInfo.us") // 회원 정보 수정
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
		
		int result = uService.updateUserInfo(prop);
		
		return result == 1 ? "success" : "fail";
		
	}

}