package com.fin.proj.auth.oauth.controller;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fin.proj.auth.oauth.service.KakaoOAuthService;
import com.fin.proj.user.model.exception.UserException;
import com.fin.proj.user.model.service.UserService;
import com.fin.proj.user.model.vo.User;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@SessionAttributes("loginUser")
@Controller
public class OAuthController {

    @Autowired
    private KakaoOAuthService kakaoOAuthService;
    
    @Autowired
    private UserService uService;

    @ResponseBody
    @GetMapping("/login/oauth2/kakao")
    public ModelAndView kakaoCalllback(@RequestParam String code, HttpSession session,
    								   RedirectAttributes redirectAttributes, Model model,
    								   HttpServletResponse response) throws IOException {
    	ModelAndView mv = new ModelAndView();
    	
        User u = kakaoOAuthService.login(code);     
        User loginUser = uService.kakaoLogin(u);
       
        if(loginUser != null) {
        	model.addAttribute("loginUser", loginUser);
        	mv.setViewName("redirect:/");        	
        } else {
        	redirectAttributes.addFlashAttribute("alertMessage", "카카오 아이디가 존재하지 않아 추가 정보를 등록해야 합니다.");
        	redirectAttributes.addFlashAttribute("newUser", u);
        	model.addAttribute("newUser", u);
        	System.out.println("newUser : " + u);
        	mv.setViewName("redirect:/kakaoEnroll.me");
        }

        return mv;
    }
    
    @GetMapping("kakaoEnroll.us")
    public String kakaoEnroll(Model model, @ModelAttribute("newUser") User newUser, HttpSession session) {
    	session.setAttribute("newUser", newUser);
    	
    	return "kakaoEnroll";
    }
    
    @PostMapping("kakaoEnrollForm.us")
    public String kakaoEnrollForm(Model model, HttpSession session, @ModelAttribute User u) {
    	
    	User newUser = (User)session.getAttribute("newUser");
    	
    	u.setKakaoId(newUser.getKakaoId());
    	u.setEmail(newUser.getEmail());

		int result = uService.kakaoEnroll(u);
		
		if(result > 0) {	
			return "redirect:/";
		} else {
			throw new UserException("회원가입 실패");
		}
    }
    
    
}
