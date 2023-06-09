package com.fin.proj.support.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

//import com.fin.proj.support.model.service.SupportService;

@Controller
public class SupportController {
	@Autowired
//	private SupportService suService;
	
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
	public String supportApplicationListUser() {
		return "supportApplicationListUser";
	}
	@RequestMapping("supportWrite.su")
	public String supportWrite() {
		return "supportWrite";
	}
	
}
