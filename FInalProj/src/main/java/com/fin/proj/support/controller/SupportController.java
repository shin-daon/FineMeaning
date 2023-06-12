package com.fin.proj.support.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fin.proj.member.model.vo.Member;
import com.fin.proj.support.model.exception.SupportException;
import com.fin.proj.support.model.service.SupportService;
import com.fin.proj.support.model.vo.Support;
import com.fin.proj.support.model.vo.SupportDetail;

import jakarta.servlet.http.HttpSession;

//import com.fin.proj.support.model.service.SupportService;

@Controller
public class SupportController {
	@Autowired
	private SupportService suService;
	
	@RequestMapping("supportMain.su")
	public String supportMain() {
		return "supportMain";
	}
	
	@RequestMapping("supportDetail.su")
	public String supportDetail() {
		return "supportDetail";
	}
	
	@RequestMapping("doSupport.su")
	public String doSupport() {
		return "doSupport";
	}
	
	@RequestMapping("doSupportEnd.su")
	public String doSupportEnd() {
		return "doSupportEnd";
	}
	
	@RequestMapping("supportListUser.su")
	public String supportListUser() {
		return "supportListUser";
	}
	@RequestMapping("supportApplicationListUser.su")
	public String supportApplicationListUser(HttpSession session, Model model) {
		int uNo = ((Member)session.getAttribute("loginUser")).getuNo();
		ArrayList<Support> sList = suService.selectApplyListUser(uNo);
		System.out.println(sList);
		System.out.println(sList.get(0).getStatus());
		model.addAttribute("sList",sList);
		return "supportApplicationListUser";
	}
	
	@RequestMapping("supportWrite.su")
	public String supportWrite() {
		return "supportWrite";
	}
	
	@RequestMapping("supportApply.su")
	public String supportApply(HttpSession session, @ModelAttribute Support s, @RequestParam("supportDetailContent") ArrayList<String> sdcList,
								@RequestParam("supportDetailAmount") ArrayList<String> sdaList) {
		
		
		int uNo = ((Member)session.getAttribute("loginUser")).getuNo();
		String registar = ((Member)session.getAttribute("loginUser")).getRegistrar();
		s.setUserNo(uNo);
		s.setRegistar(registar);

		int result1 = suService.supportApply(s);

		int result2 = 0;
		for(int i = 0; i < sdcList.size(); i++ ) {
			SupportDetail sd = new SupportDetail();
			sd.setSupportDetailContent(sdcList.get(i));
			sd.setSupportDetailAmount(Integer.parseInt(sdaList.get(i)));
			result2 = suService.insertSupportDetail(sd);
		}
		
		if(result1 + result2>0) {
			return "redirect:supportApplicationListUser.su";
		} else {
			throw new SupportException("신청에 실패하였습니다.");
		}
	}
	
	
}
