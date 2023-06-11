package com.fin.proj.volunteer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.fin.proj.volunteer.model.service.VolunteerService;

@Controller
public class VolunteerController {
	
	@Autowired
	private VolunteerService vService;
	
	@GetMapping("volunteer.vo")
	public String volunteer() {
		return "volunteer";
	}
	
	@GetMapping("volunteerDetail.vo")
	public String volunteerDetail() {
		return "volunteerDetail";
	}
	
	@GetMapping("volunteerApply.vo")
	public String volunteerApply() {
		return "volunteerApply";
	}
	
	@GetMapping("volunteerHistory.vo")
	public String volunteerHistory() {
		return "volunteerHistory";
	}
	
	// 관리자
	@GetMapping("volunteerEnroll.vo")
	public String volunteerEnroll() {
		return "volunteerEnroll";
	}
	
	@GetMapping("volunteerEnrollHistory.vo")
	public String volunteerEnrollHistory() {
		return "volunteerEnrollHistory";
	}
	
	@GetMapping("volunteerEdit.vo")
	public String volunteerEdit() {
		return "volunteerEdit";
	}
}
