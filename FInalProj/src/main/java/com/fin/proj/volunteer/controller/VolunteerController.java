package com.fin.proj.volunteer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.fin.proj.member.model.vo.Member;
import com.fin.proj.volunteer.model.service.VolunteerService;
import com.fin.proj.volunteer.model.vo.Volunteer;

import jakarta.servlet.http.HttpSession;

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
	
	@PostMapping("insertVolunteer.vo")
	public String insertVolunteer(@ModelAttribute Volunteer v, HttpSession session) {
//		System.out.println((Member)session.getAttribute("loginUser"));
		v.setuNo(((Member)session.getAttribute("loginUser")).getuNo());
		System.out.println(v);
		int result = vService.insertVolunteer(v);
		if(result > 0) {
			return "redirect:volunteerEnrollHistory.vo";
		}
		return null;
	}
}
