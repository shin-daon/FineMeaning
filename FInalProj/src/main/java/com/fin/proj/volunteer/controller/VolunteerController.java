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
import com.fin.proj.user.model.vo.User;
import com.fin.proj.volunteer.Map;
import com.fin.proj.volunteer.model.exception.VolunteerException;
import com.fin.proj.volunteer.model.service.VolunteerService;
import com.fin.proj.volunteer.model.vo.Volunteer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class VolunteerController {
	
	@Autowired
	private VolunteerService vService;
	
	@RequestMapping("volunteer.vo")
	public String volunteer(@RequestParam(value="page", required=false) Integer currentPage, @RequestParam(value="searchObject", required=false) String searchObject, Model model) {
		if(currentPage == null) {
			currentPage = 1;
		}
		
		PageInfo pi = null;
		ArrayList<Volunteer> list = null;
		if(searchObject == null) {
			int volunteerCount = vService.getVolunteerCount();
			pi = Pagination.getPageInfo(currentPage, volunteerCount, 5);
			list = vService.selectVolunteerList(pi);
		} else {
			HashMap<String, String> searchMap = new Gson().fromJson(String.valueOf(searchObject), new TypeToken<HashMap<String, Object>>(){}.getType());
			
			if(searchMap.get("vArea").equals("전체")) {
				searchMap.put("vArea", "");
			} 
			if(searchMap.get("vMainCategoryName").equals("전체")) {
				searchMap.put("vMainCategoryName", "");
			} 
			if(searchMap.get("vActivityType").equals("전체")) {
				searchMap.put("vActivityType", "");
			} 
			if(searchMap.get("vTargetCategoryName").equals("전체")) {
				searchMap.put("vTargetCategoryName", "");
			} 
			if(searchMap.get("status").equals("전체")) {
				searchMap.put("status", "");
			} 
			
			int searchVolunteerCount = vService.getSearchVolunteerCount(searchMap);
			pi = Pagination.getPageInfo(currentPage, searchVolunteerCount, 5);
			list = vService.searchVolunteer(pi, searchMap);
			
			model.addAttribute("searchMap", searchMap);
		}
		
		if(list != null) {
			model.addAttribute("pi", pi);
			model.addAttribute("list", list);
			
			return "volunteer";
		}
		throw new VolunteerException("봉사 조회에 실패하였습니다.");
	}
	
	@RequestMapping("volunteerDetail.vo")
	public String volunteerDetail(@RequestParam("vNo") int vNo, @RequestParam(value="page", required=false) Integer page, @RequestParam(value="searchObject", required=false) String searchObject, Model model) {
		Volunteer v = vService.selectVolunteer(vNo);
		if(v != null) {
			if(page == null) {
				page = 1;
			}
			
			HashMap<String, Double> map = Map.getLongitudeAndLatitude(v.getvLocation());
			model.addAttribute("v", v);
			model.addAttribute("page", page);
			model.addAttribute("map", map);
			
			if(searchObject != null) {
				HashMap<String, String> searchMap = new Gson().fromJson(String.valueOf(searchObject), new TypeToken<HashMap<String, Object>>(){}.getType());
				
				if(searchMap.get("vArea").equals("전체")) {
					searchMap.put("vArea", "");
				} 
				if(searchMap.get("vMainCategoryName").equals("전체")) {
					searchMap.put("vMainCategoryName", "");
				} 
				if(searchMap.get("vActivityType").equals("전체")) {
					searchMap.put("vActivityType", "");
				} 
				if(searchMap.get("vTargetCategoryName").equals("전체")) {
					searchMap.put("vTargetCategoryName", "");
				} 
				if(searchMap.get("status").equals("전체")) {
					searchMap.put("status", "");
				}
				
				model.addAttribute("searchMap", searchMap);
			}
			
			return "volunteerDetail";
		} 
		throw new VolunteerException("봉사 상세 조회에 실패하였습니다.");
	}
	
	@RequestMapping("volunteerApply.vo")
	public String volunteerApply(@RequestParam("vNo") int vNo, @RequestParam(value="page", required=false) Integer page, @RequestParam(value="searchObject", required=false) String searchObject, HttpSession session, Model model) {
		Volunteer v = vService.selectVolunteer(vNo);
		if(page == null) {
			page = 1;
		}
		model.addAttribute("v", v);
		model.addAttribute("page", page);
		model.addAttribute("u", ((User)session.getAttribute("loginUser")));
		
		if(searchObject != null) {
			HashMap<String, String> searchMap = new Gson().fromJson(String.valueOf(searchObject), new TypeToken<HashMap<String, Object>>(){}.getType());
			
			if(searchMap.get("vArea").equals("전체")) {
				searchMap.put("vArea", "");
			} 
			if(searchMap.get("vMainCategoryName").equals("전체")) {
				searchMap.put("vMainCategoryName", "");
			} 
			if(searchMap.get("vActivityType").equals("전체")) {
				searchMap.put("vActivityType", "");
			} 
			if(searchMap.get("vTargetCategoryName").equals("전체")) {
				searchMap.put("vTargetCategoryName", "");
			} 
			if(searchMap.get("status").equals("전체")) {
				searchMap.put("status", "");
			}
			
			model.addAttribute("searchMap", searchMap);
		}
		
		return "volunteerApply";
	}
	
	@RequestMapping("applyVolunteer.vo")
	public String applyVolunteer(@ModelAttribute Volunteer v, HttpSession session) {
		v.setuNo(((User)session.getAttribute("loginUser")).getuNo());
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
	public void volunteerAjax(@RequestParam("searchObject") String searchObject, HttpServletResponse response) {
		HashMap<String, String> ajaxMap = new Gson().fromJson(String.valueOf(searchObject), new TypeToken<HashMap<String, Object>>(){}.getType());
		
		if(ajaxMap.get("vArea").equals("전체")) {
			ajaxMap.put("vArea", "");
		} 
		if(ajaxMap.get("vMainCategoryName").equals("전체")) {
			ajaxMap.put("vMainCategoryName", "");
		} 
		if(ajaxMap.get("vActivityType").equals("전체")) {
			ajaxMap.put("vActivityType", "");
		} 
		if(ajaxMap.get("vTargetCategoryName").equals("전체")) {
			ajaxMap.put("vTargetCategoryName", "");
		} 
		if(ajaxMap.get("status").equals("전체")) {
			ajaxMap.put("status", "");
		} 
		
		int searchVolunteerAjaxCount = vService.getSearchVolunteerCount(ajaxMap);
		PageInfo pi = Pagination.getPageInfo(1, searchVolunteerAjaxCount, 5);
		ArrayList<Volunteer> volunteerList = vService.searchVolunteer(pi, ajaxMap);
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put("pi", pi);
		data.put("volunteerList", volunteerList);
		response.setContentType("application/json; charset=UTF-8");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
		try {
			gson.toJson(data, response.getWriter());
		} catch (JsonIOException | IOException e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("volunteerHistory.vo")
	public String volunteerHistory(@RequestParam(value="page", required=false) Integer currentPage, HttpSession session, Model model) {
		if(currentPage == null) {
			currentPage = 1;
		}
		
		int uNo = ((User)session.getAttribute("loginUser")).getuNo();
		
		int vHistoryCount = vService.getMyVolunteerHistoryCount(uNo);
		PageInfo pi = Pagination.getPageInfo(currentPage, vHistoryCount, 10);
			
		ArrayList<Volunteer> vHistories = vService.selectMyVolunteerHistory(pi, uNo);
		if(vHistories != null) {
			model.addAttribute("pi", pi);
			model.addAttribute("vHistories", vHistories);
			
			return "volunteerHistory";
		}
		throw new VolunteerException("봉사 내역 조회에 실패하였습니다.");
	}
	
	@GetMapping("searchMyVolunteerHistory.vo")
	public String searchMyVolunteerHistory(@RequestParam(value="page", required=false) Integer currentPage, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, 
										   @RequestParam("vName") String vName, @RequestParam("vArea") String vArea, @RequestParam("registrar") String registrar,
										   @RequestParam("status") String status, HttpSession session, Model model) {
		if(currentPage == null) {
			currentPage = 1;
		}
		
		if(vArea.equals("전체")) {
			vArea = "";
		} 
		
		if(status.equals("전체")) {
			status = "";
		}
		
		int uNo = ((User)session.getAttribute("loginUser")).getuNo();
		HashMap<String, Object> myHistorySearchMap = new HashMap<String, Object>();
		myHistorySearchMap.put("uNo", uNo);
		myHistorySearchMap.put("startDate", startDate);
		myHistorySearchMap.put("endDate", endDate);
		myHistorySearchMap.put("vName", vName);
		myHistorySearchMap.put("vArea", vArea);
		myHistorySearchMap.put("registrar", registrar);
		myHistorySearchMap.put("status", status);
		
		int vHistoryCount = vService.getSearchMyVolunteerHistoryCount(myHistorySearchMap);
		PageInfo pi = Pagination.getPageInfo(currentPage, vHistoryCount, 10);
		
		ArrayList<Volunteer> vHistories = vService.selectSearchMyVolunteerHistory(pi, myHistorySearchMap);
		
		if(vHistories != null) {
			model.addAttribute("pi", pi);
			model.addAttribute("vHistories", vHistories);
			model.addAttribute("searchMap", myHistorySearchMap);
			return "volunteerHistory";
		}
		throw new VolunteerException("봉사 내역 검색에 실패하였습니다.");
	}
	
	// 봉사 관리자
	@GetMapping("volunteerEnroll.vo")
	public String volunteerEnroll() {
		return "volunteerEnroll";
	}
	
	@GetMapping("volunteerEnrollHistory.vo")
	public String volunteerEnrollHistory(@RequestParam(value="page", required=false) Integer currentPage, HttpSession session, Model model) {
		if(currentPage == null) {
			currentPage = 1;
		}
		
		int uNo = ((User)session.getAttribute("loginUser")).getuNo();
		
		int vEnrollHistoryCount = vService.getVolunteerEnrollHistoryCount(uNo);
		PageInfo pi = Pagination.getPageInfo(currentPage, vEnrollHistoryCount, 10);
			
		ArrayList<Volunteer> vHistories = vService.selectVolunteerEnrollHistory(pi, uNo);
		ArrayList<Integer> peopleCount = vService.selectVolunteerApplicantCount(pi, uNo);
		if(vHistories != null) {
			model.addAttribute("pi", pi);
			model.addAttribute("vHistories", vHistories);
			model.addAttribute("peopleCount", peopleCount);
			
			return "volunteerEnrollHistory";
		}
		throw new VolunteerException("봉사 등록 내역 조회에 실패하였습니다.");
	}
	
	@GetMapping("searchVolunteerEnrollHistory.vo")
	public String searchVolunteerEnrollHistory(@RequestParam(value="page", required=false) Integer currentPage, @RequestParam("startDate") String startDate, 
											   @RequestParam("endDate") String endDate, @RequestParam("vMainCategoryName") String vMainCategoryName, 
											   @RequestParam("status") String status, @RequestParam("vName") String vName, @RequestParam(value="column", required=false) String column, 
											   @RequestParam("vTargetCategoryName") String vTargetCategoryName, HttpSession session, Model model) {
		if(currentPage == null) {
			currentPage = 1;
		}
		
		if(vMainCategoryName.equals("전체")) {
			vMainCategoryName = "";
		} 
		if(vTargetCategoryName.equals("전체")) {
			vTargetCategoryName = "";
		}
		if(status.equals("전체")) {
			status = "";
		}
		
		HashMap<String, Object> searchEnrollHisMap = new HashMap<String, Object>();
		searchEnrollHisMap.put("column", column);
		searchEnrollHisMap.put("startDate", startDate);
		searchEnrollHisMap.put("endDate", endDate);
		searchEnrollHisMap.put("vMainCategoryName", vMainCategoryName);
		searchEnrollHisMap.put("status", status);
		searchEnrollHisMap.put("vName", vName);
		searchEnrollHisMap.put("vTargetCategoryName", vTargetCategoryName);
		searchEnrollHisMap.put("uNo", ((User)session.getAttribute("loginUser")).getuNo());
		
		int searchVolunteerHistoryCount = vService.getSearchVolunteerHistoryCount(searchEnrollHisMap);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, searchVolunteerHistoryCount, 10);
		
		ArrayList<Volunteer> searchVEnrollHistories = vService.selectSearchVolunteerEnrollHistory(pi, searchEnrollHisMap);
		ArrayList<Integer> peopleCount = vService.selectSearchVolunteerApplicantCount(pi, searchEnrollHisMap);
		
		if(searchVEnrollHistories != null) {
			model.addAttribute("pi", pi);
			model.addAttribute("vHistories", searchVEnrollHistories);
			model.addAttribute("searchMap", searchEnrollHisMap);
			model.addAttribute("peopleCount", peopleCount);
			return "volunteerEnrollHistory";
		}
		throw new VolunteerException("봉사 등록 내역 검색에 실패하였습니다.");
	}
	
	@PostMapping("volunteerEdit.vo")
	public String volunteerEdit(@RequestParam("vNo") int vNo, @RequestParam("page") int page, @RequestParam(value="searchObject", required=false) String searchObject, Model model) {
		Volunteer v = vService.selectVolunteer(vNo);
		model.addAttribute("v", v);
		model.addAttribute("page", page);
		
		if(searchObject != null) {
			HashMap<String, String> searchMap = new Gson().fromJson(String.valueOf(searchObject), new TypeToken<HashMap<String, Object>>(){}.getType());
			
			if(searchMap.get("vArea").equals("전체")) {
				searchMap.put("vArea", "");
			} 
			if(searchMap.get("vMainCategoryName").equals("전체")) {
				searchMap.put("vMainCategoryName", "");
			} 
			if(searchMap.get("vActivityType").equals("전체")) {
				searchMap.put("vActivityType", "");
			} 
			if(searchMap.get("vTargetCategoryName").equals("전체")) {
				searchMap.put("vTargetCategoryName", "");
			} 
			if(searchMap.get("status").equals("전체")) {
				searchMap.put("status", "");
			}
			
			model.addAttribute("searchMap", searchMap);
		}
		
		return "volunteerEdit";
	}
	
	@PostMapping("insertVolunteer.vo")
	public String insertVolunteer(@ModelAttribute Volunteer v, HttpSession session) {
		v.setuNo(((User)session.getAttribute("loginUser")).getuNo());
		int result = vService.insertVolunteer(v);
		if(result > 0) {
			return "redirect:volunteerEnrollHistory.vo";
		}
		throw new VolunteerException("봉사 등록에 실패하였습니다.");
	}
	
	@PostMapping("updateVolunteer.vo")
	public String updateVolunteer(@ModelAttribute Volunteer v, @RequestParam("page") int page, @RequestParam(value="searchObject", required=false) String searchObject, HttpSession session, RedirectAttributes ra) {
		v.setuNo(((User)session.getAttribute("loginUser")).getuNo());
		
		int result = vService.updateVolunteer(v);
		if(result > 0) {
			ra.addAttribute("vNo", v.getvNo());
			ra.addAttribute("page", page);
			if(searchObject != null) {
				ra.addAttribute("searchObject", searchObject);
			}
			
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
	
	@ResponseBody
	@GetMapping("updateVolunteerStatus.vo")
	public String updateVolunteerStatus(@RequestParam("vNo") int vNo, @RequestParam("status") String status) {
		HashMap<String, Object> updateStatusMap = new HashMap<String, Object>();
		updateStatusMap.put("vNo", vNo);
		updateStatusMap.put("status", status);
		
		int result = vService.updateVolunteerStatus(updateStatusMap);
		return result == 1 ? "success" : "fail";
	}
	
	@ResponseBody
	@GetMapping("updateVolunteerHistoryStatus.vo")
	public String updateVolunteerHistoryStatus(@RequestParam("vHisNo") int vHisNo, @RequestParam("status") String status) {
		HashMap<String, Object> updateStatusMap = new HashMap<String, Object>();
		updateStatusMap.put("vHisNo", vHisNo);
		updateStatusMap.put("status", status);
		
		int result = vService.updateVolunteerHistoryStatus(updateStatusMap);
		return result == 1? "success" : "fail";
	}
	
	@GetMapping("volunteerApplyList.vo")
	public String volunteerApplyList(@RequestParam(value="vNo", required=false) Integer vNo, 
									 @RequestParam(value="vHisStatus", required=false) String vHisStatus, @RequestParam("applicant") int applicant, Model model) {
		HashMap<String, Object> applyMap = new HashMap<String, Object>();
		applyMap.put("vNo", vNo);
		applyMap.put("vHisStatus", vHisStatus);
		
		ArrayList<Volunteer> vHistories = vService.selectVolunteerApplyList(applyMap);
		
		if(vHistories != null) {
			model.addAttribute("vHistories", vHistories);
			model.addAttribute("vNo", vNo);
			model.addAttribute("vHisStatus", vHisStatus);
			model.addAttribute("applicant", applicant);
			return "volunteerApplyList";
		}
		throw new VolunteerException("봉사 신청자 목록 조회에 실패하였습니다.");
	}
	
	// 관리자
	@GetMapping("adminVolunteerList.vo")
	public String adminVolunteerList(@RequestParam(value="page", required=false) Integer currentPage, Model model) {
		if(currentPage == null) {
			currentPage = 1;
		}
		
		int vEnrollHistoryCount = vService.getVolunteerEnrollHistoryCount(null);
		PageInfo pi = Pagination.getPageInfo(currentPage, vEnrollHistoryCount, 10);
			
		ArrayList<Volunteer> vHistories = vService.selectSearchVolunteerEnrollHistory(pi, null);
		ArrayList<Integer> peopleCount = vService.selectVolunteerApplicantCount(pi, null);
		
		if(vHistories != null) {
			model.addAttribute("pi", pi);
			model.addAttribute("vHistories", vHistories);
			model.addAttribute("peopleCount", peopleCount);
			
			return "adminVolunteerList";
		}
		
		throw new VolunteerException("봉사 목록 조회에 실패하였습니다.");
	}
	
	@GetMapping("searchAdminVolunteerList.vo")
	public String searchAdminVolunteerList(@RequestParam(value="page", required=false) Integer currentPage, @RequestParam("startDate") String startDate, 
										   @RequestParam("endDate") String endDate, @RequestParam("vMainCategoryName") String vMainCategoryName, 
										   @RequestParam("status") String status, @RequestParam("vName") String vName, @RequestParam(value="column", required=false) String column, 
										   @RequestParam("vTargetCategoryName") String vTargetCategoryName, @RequestParam(value="registrar", required=false) String registrar,
										   HttpSession session, Model model) {
		if(currentPage == null) {
			currentPage = 1;
		}
		
		if(vMainCategoryName.equals("전체")) {
			vMainCategoryName = "";
		} 
		if(vTargetCategoryName.equals("전체")) {
			vTargetCategoryName = "";
		}
		if(status.equals("전체")) {
			status = "";
		}
		
		HashMap<String, Object> searchEnrollHisMap = new HashMap<String, Object>();
		searchEnrollHisMap.put("column", column);
		searchEnrollHisMap.put("startDate", startDate);
		searchEnrollHisMap.put("endDate", endDate);
		searchEnrollHisMap.put("vMainCategoryName", vMainCategoryName);
		searchEnrollHisMap.put("status", status);
		searchEnrollHisMap.put("vName", vName);
		searchEnrollHisMap.put("vTargetCategoryName", vTargetCategoryName);
		if(registrar != null) {
			searchEnrollHisMap.put("registrar", registrar);
		}
		
		int searchVolunteerHistoryCount = vService.getSearchVolunteerHistoryCount(searchEnrollHisMap);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, searchVolunteerHistoryCount, 10);
		
		ArrayList<Volunteer> searchVEnrollHistories = vService.selectSearchVolunteerEnrollHistory(pi, searchEnrollHisMap);
		ArrayList<Integer> peopleCount = vService.selectSearchVolunteerApplicantCount(pi, searchEnrollHisMap);
		
		if(searchVEnrollHistories != null) {
			model.addAttribute("pi", pi);
			model.addAttribute("vHistories", searchVEnrollHistories);
			model.addAttribute("searchMap", searchEnrollHisMap);
			model.addAttribute("peopleCount", peopleCount);
			return "adminVolunteerList";
		}
		throw new VolunteerException("봉사 목록 검색에 실패하였습니다.");
	}
	
	@GetMapping("adminAllVolunteerApplyList.vo")
	public String adminAllVolunteerApplyList(@RequestParam(value="page", required=false) Integer currentPage, @RequestParam(value="vNo", required=false) Integer vNo, Model model) {
		if(currentPage == null) {
			currentPage = 1;
		}
		
		int volunteerApplyCount = vService.getAdminVolunteerApplyCount(vNo);
		PageInfo pi = Pagination.getPageInfo(currentPage, volunteerApplyCount, 10);
		ArrayList<Volunteer> vHistories = vService.selectAdminVolunteerApplyList(pi, vNo);
		
		if(vHistories != null) {
			model.addAttribute("pi", pi);
			model.addAttribute("vHistories", vHistories);
			model.addAttribute("vNo", vNo);
			return "adminAllVolunteerApplyList";
		}
		throw new VolunteerException("봉사 신청자 목록 조회에 실패하였습니다.");
	}
	
	@GetMapping("adminVolunteerApplyList.vo")
	public String adminVolunteerApplyList(@RequestParam(value="vNo", required=false) Integer vNo, 
										  @RequestParam(value="vHisStatus", required=false) String vHisStatus, @RequestParam("applicant") int applicant, Model model) {
		HashMap<String, Object> applyMap = new HashMap<String, Object>();
		applyMap.put("vNo", vNo);
		applyMap.put("vHisStatus", vHisStatus);
		
		ArrayList<Volunteer> vHistories = vService.selectVolunteerApplyList(applyMap);
		
		if(vHistories != null) {
			model.addAttribute("vHistories", vHistories);
			model.addAttribute("vHisStatus", vHisStatus);
			model.addAttribute("vNo", vNo);
			model.addAttribute("applicant", applicant);
			return "adminVolunteerApplyList";
		}
		
		throw new VolunteerException("봉사 신청자 목록 조회에 실패하였습니다.");
	}
}
