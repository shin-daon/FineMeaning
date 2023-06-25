package com.fin.proj.volunteer.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fin.proj.common.Pagination;
import com.fin.proj.common.model.vo.PageInfo;
import com.fin.proj.member.model.vo.Member;
import com.fin.proj.volunteer.Map;
import com.fin.proj.volunteer.model.exception.VolunteerException;
import com.fin.proj.volunteer.model.service.VolunteerService;
import com.fin.proj.volunteer.model.vo.Volunteer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class VolunteerController {
	
	@Autowired
	private VolunteerService vService;
	
	@RequestMapping("volunteer.vo")
	public String volunteer(@RequestParam(value="page", required=false) Integer currentPage, Model model) {
		if(currentPage == null) {
			currentPage = 1;
		}
		int volunteerCount = vService.getVolunteerCount();
		PageInfo pi = Pagination.getPageInfo(currentPage, volunteerCount, 5);
		ArrayList<Volunteer> list = vService.selectVolunteerList(pi);
		
//			System.out.println(volunteerCount);
//			System.out.println(list);
		
		if(list != null) {
			model.addAttribute("pi", pi);
			model.addAttribute("list", list);
			
			return "volunteer";
		}
		throw new VolunteerException("봉사 조회에 실패하였습니다.");
	}
	
	@GetMapping("volunteerDetail.vo")
	public String volunteerDetail(@RequestParam("vNo") int vNo, @RequestParam("page") int page, @RequestParam(value="vStartDate", required=false) String vStartDate,
								  @RequestParam(value="vEndDate", required=false) String vEndDate, @RequestParam(value="vName", required=false) String vName,
								  @RequestParam(value="registrar", required=false) String registrar, Model model) {
		Volunteer v = vService.selectVolunteer(vNo);
//		System.out.println(v);
		if(v != null) {
			HashMap<String, Double> map = Map.getLongitudeAndLatitude(v.getAddress());
//			System.out.println(map);
			model.addAttribute("v", v);
			model.addAttribute("page", page);
			model.addAttribute("map", map);
			
			if(vStartDate != null) {
				model.addAttribute("vStartDate", vStartDate);
				model.addAttribute("vEndDate", vEndDate);
				model.addAttribute("vName", vName);
				model.addAttribute("registrar", registrar);
			}
			
			return "volunteerDetail";
		} 
		throw new VolunteerException("봉사 상세 조회에 실패하였습니다.");
	}
	
	@GetMapping("volunteerApply.vo")
	public String volunteerApply(@RequestParam("vNo") int vNo, HttpSession session, Model model) {
		Volunteer v = vService.selectVolunteer(vNo);
		Member m = (Member)session.getAttribute("loginUser");
		
		model.addAttribute("v", v);
		model.addAttribute("m", m);
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
	
	@PostMapping("volunteerEdit.vo")
	public String volunteerEdit(@RequestParam("vNo") int vNo, @RequestParam("page") int page, Model model) {
		Volunteer v = vService.selectVolunteer(vNo);
		model.addAttribute("v", v);
		model.addAttribute("page", page);
		return "volunteerEdit";
	}
	
	@PostMapping("insertVolunteer.vo")
	public String insertVolunteer(@ModelAttribute Volunteer v, HttpSession session) {
//		System.out.println((Member)session.getAttribute("loginUser"));
		v.setuNo(((Member)session.getAttribute("loginUser")).getuNo());
//		System.out.println(v);
		int result = vService.insertVolunteer(v);
		if(result > 0) {
			return "redirect:volunteerEnrollHistory.vo";
		}
		throw new VolunteerException("봉사 등록에 실패하였습니다.");
	}
	
	@PostMapping("updateVolunteer.vo")
	public String updateVolunteer(@ModelAttribute Volunteer v, @RequestParam("page") int page, HttpSession session, RedirectAttributes ra) {
		v.setuNo(((Member)session.getAttribute("loginUser")).getuNo());
//		System.out.println(v);
		int result = vService.updateVolunteer(v);
		if(result > 0) {
			ra.addAttribute("vNo", v.getvNo());
			ra.addAttribute("page", page);
			return "redirect:volunteerDetail.vo";
		}
		throw new VolunteerException("봉사 수정에 실패하였습니다.");
	}
	
	@GetMapping("deleteVolunteer.vo")
	public String deleteVolunteer(@RequestParam("vNo") String encodedVNo) {
		Decoder decoder = Base64.getDecoder();
		byte[] byteArr = decoder.decode(encodedVNo);
		String vNo = new String(byteArr);
		
		int result = vService.deleteVolunteer(vNo);
		if(result > 0) {
			return "redirect:volunteer.vo";
		}
		throw new VolunteerException("봉사 삭제에 실패하였습니다.");
	}
	
	@PostMapping("applyVolunteer.vo")
	public String applyVolunteer(@ModelAttribute Volunteer v, HttpSession session) {
		v.setuNo(((Member)session.getAttribute("loginUser")).getuNo());
//		System.out.println(v);
		int result = vService.applyVolunteer(v);
		if(result > 0) {
			return "redirect:volunteerHistory.vo";
		}
		throw new VolunteerException("봉사 신청에 실패하였습니다.");
	}
	
	@ResponseBody
	@RequestMapping("checkVolunteerApply.vo")
	public void checkVolunteerApply(@RequestParam("vNo") int vNo, @RequestParam("uNo") Integer uNo, PrintWriter out) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("vNo", vNo);
		map.put("uNo", uNo);
		int count = vService.checkVolunteerApply(map);
		String result = count == 0 ? "yes" : "no";
		out.print(result);
	}
	
	@PostMapping("volunteerAjax.vo")
	public void volunteerAjax(@RequestParam(value="page", required=false) Integer currentPage, @RequestParam("vStartDate") String vStartDate, @RequestParam("vEndDate") String vEndDate, 
							  @RequestParam("vName") String vName, @RequestParam("registrar") String registrar, @RequestParam("vArea") String vArea, 
							  @RequestParam("vMainCategoryName") String vMainCategoryName, @RequestParam("vActivityType") String vActivityType,
							  @RequestParam("vTargetCategoryName") String vTargetCategoryName, @RequestParam("status") String status,HttpServletResponse response) {
		HashMap<String, String> ajaxMap = new HashMap<String, String>();
		if(vArea.equals("전체")) {
			vArea = "";
		} 
		
		if(vMainCategoryName.equals("전체")) {
			vMainCategoryName = "";
		}
		
		if(vActivityType.equals("전체")) {
			vActivityType = "";
		}
		
		if(vTargetCategoryName.equals("전체")) {
			vTargetCategoryName = "";
		}
		
		if(status.equals("전체")) {
			status = "";
		}
		
		ajaxMap.put("vStartDate", vStartDate);
		ajaxMap.put("vEndDate", vEndDate);
		ajaxMap.put("vName", vName);
		ajaxMap.put("registrar", registrar);
		ajaxMap.put("vArea", vArea);
		ajaxMap.put("vMainCategoryName", vMainCategoryName);
		ajaxMap.put("vActivityType", vActivityType);
		ajaxMap.put("vTargetCategoryName", vTargetCategoryName);
		ajaxMap.put("status", status);
		
		int searchVolunteerAjaxCount = vService.getSearchVolunteerCount(ajaxMap);
		PageInfo pi = Pagination.getPageInfo(1, searchVolunteerAjaxCount, 5);
		ArrayList<Volunteer> volunteerList = vService.searchVolunteerByAjax(pi, ajaxMap);
		response.setContentType("application/json; charset=UTF-8");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		try {
			gson.toJson(volunteerList, response.getWriter());
		} catch (JsonIOException | IOException e) {
			e.printStackTrace();
		}
	}
}
