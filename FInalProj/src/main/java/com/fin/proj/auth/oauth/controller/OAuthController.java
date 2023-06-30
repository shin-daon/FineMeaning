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
import com.fin.proj.member.model.exception.MemberException;
import com.fin.proj.member.model.service.MemberService;
import com.fin.proj.member.model.vo.Member;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@SessionAttributes("loginUser")
@Controller
public class OAuthController {

    @Autowired
    private KakaoOAuthService kakaoOAuthService;
    
    @Autowired
    private MemberService mService;

    @ResponseBody
    @GetMapping("/login/oauth2/kakao")
    public ModelAndView kakaoCalllback(@RequestParam String code, HttpSession session,
    								   RedirectAttributes redirectAttributes, Model model,
    								   HttpServletResponse response) throws IOException {
    	ModelAndView mv = new ModelAndView();
    	
        Member m = kakaoOAuthService.login(code);     
        Member loginUser = mService.kakaoLogin(m);
       
        if(loginUser != null) {
        	//mv.addObject("loginUser", loginUser);
        	//session.setAttribute("loginUser", loginUser);
        	model.addAttribute("loginUser", loginUser);
        	mv.setViewName("redirect:/");        	
        } else {
        	redirectAttributes.addFlashAttribute("alertMessage", "카카오 아이디가 존재하지 않아 추가 정보를 등록해야 합니다.");
        	redirectAttributes.addFlashAttribute("newUser", m);
        	model.addAttribute("newUser", m);
        	System.out.println("newUser : " + m);
        	mv.setViewName("redirect:/kakaoEnroll.me");
        }

        return mv;
    }
    
    @GetMapping("kakaoEnroll.me")
    public String kakaoEnroll(Model model, @ModelAttribute("newUser") Member newUser, HttpSession session) {
    	session.setAttribute("newUser", newUser);
    	System.out.println("newUser : " + newUser);
    	return "kakaoEnroll";
    }
    
    @PostMapping("kakaoEnrollForm.me")
    public String kakaoEnrollForm(Model model, HttpSession session, @ModelAttribute Member m) {
    	
    	Member newUser = (Member)session.getAttribute("newUser");
    	
    	System.out.println("newUser : " + newUser);
    	m.setKakaoId(newUser.getKakaoId());
    	m.setEmail(newUser.getEmail());
    	
    	System.out.println(m);

		int result = mService.kakaoEnroll(m);
		
		if(result > 0) {	
			return "redirect:/";
		} else {
			throw new MemberException("회원가입 실패");
		}
    }
    
    
}
