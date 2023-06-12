package com.fin.proj.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class CommunityController {
	
	@GetMapping("/commList.co")
	public String commList() {
		return "commList";
	}
	
	@GetMapping("/writeComm.co")
	public String writeComm() {
		return "writeComm";
	}
	
	@GetMapping("/noticeList.co")
	public String noticeList() {
		return "noticeList";
	}
	
	@GetMapping("/writeNotice.co")
	public String writeNotice() {
		return "writeNotice";
	}
	
	@GetMapping("/qaList.co")
	public String qaList() {
		return "qaList";
	}
	
	@GetMapping("/writeQa.co")
	public String writeQa() {
		return "writeQa";
	}
	
	@GetMapping("/commDetail.co")
	public String commDetail() {
		return "commDetail";
	}
	
	@GetMapping("/qaDetail.co")
	public String qaDetail() {
		return "qaDetail";
	}
	
	@GetMapping("/replyQa.co")
	public String replyQa() {
		return "replyQa";
	}
	
	@GetMapping("/editComm.co")
	public String editComm() {
		return "editComm";
	}
	
	@GetMapping("/editQa.co")
	public String editQa() {
		return "editQa";
	}
	
	@GetMapping("/editNotice.co")
	public String editNotice() {
		return "editNotice";
	}
	
}
