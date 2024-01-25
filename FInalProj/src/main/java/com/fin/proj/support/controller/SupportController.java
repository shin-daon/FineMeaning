package com.fin.proj.support.controller;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fin.proj.common.Pagination;
import com.fin.proj.common.model.vo.PageInfo;
import com.fin.proj.support.model.exception.SupportException;
import com.fin.proj.support.model.service.SupportService;
import com.fin.proj.support.model.vo.Support;
import com.fin.proj.support.model.vo.SupportHistory;
import com.fin.proj.user.model.vo.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class SupportController {
	@Autowired
	private SupportService suService;

	@RequestMapping("supportMain.su")
	public String supportMain(@RequestParam(value = "page", required = false) Integer currentPage, Model model) {
		int maintotalCount = suService.maintotalCount();
		int maintotalAmount = suService.maintotalAmount();
				
		if (currentPage == null) {
			currentPage = 1;
		}

		int listCount = suService.getListCount();
		
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 11);

		ArrayList<Support> sList = suService.selectSupportList(pi);
		
		model.addAttribute("pi", pi);
		model.addAttribute("sList", sList);
		model.addAttribute("totalCount", maintotalCount);
		model.addAttribute("totalAmount", maintotalAmount);
		return "supportMain";
	}

	@RequestMapping("supportDetail.su")
	public String supportDetail(HttpSession session, @RequestParam("supportNo") int supportNo, Model model) {
		
		Support s = suService.supportDetail(supportNo);
		int dDay = suService.getDday(supportNo);
		ArrayList<SupportHistory> shList = suService.currentSupporter(supportNo);

		
		if((User)session.getAttribute("loginUser")!= null) {			
			System.out.println("지금 여기로 들어오는거니?");
			int uNo = ((User) session.getAttribute("loginUser")).getuNo();
			int isAdmin = ((User) session.getAttribute("loginUser")).getIsAdmin();
			model.addAttribute("s", s);
			model.addAttribute("dDay", dDay);
			model.addAttribute("shList", shList);
			if (s.getStatus() == 'Y') {
				return "supportDetail";
			} else {
				if (uNo == s.getUserNo() || isAdmin == 0) {
					return "supportApplyDetail";
				} else {
					throw new SupportException("잘못된 접근입니다.");
				}
			}
		} else {
			if(s.getStatus() == 'Y') {
				model.addAttribute("s", s);
				model.addAttribute("dDay", dDay);
				model.addAttribute("shList", shList);
				return "supportDetail";
			} else {
				throw new SupportException("열람할 수 없습니다. ");
			}
		}

	}

	@RequestMapping("doSupport.su")
	public String doSupport(@RequestParam("supportNo") int supportNo, Model model) {
		Support s = suService.supportDetail(supportNo);
		model.addAttribute("s", s);
		return "doSupport";
	}

	@RequestMapping("doSupportEnd.su")
	public String doSupportEnd(@RequestParam("registar") String registar, @RequestParam("supportTitle") String supportTitle, Model model) {
		System.out.println(supportTitle);
		System.out.println(registar);
		model.addAttribute("supportTitle", supportTitle);
		model.addAttribute("registar", registar);
		return "doSupportEnd";
	}

	@RequestMapping("supportListUser.su")
	public String supportListUser(@RequestParam(value = "page", required = false) Integer currentPage,
			HttpSession session, Model model) {

		if (currentPage == null) {
			currentPage = 1;
		}

		int uNo = ((User) session.getAttribute("loginUser")).getuNo();

		System.out.println(uNo);

		int listCount = suService.getMListCount(uNo);

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		ArrayList<SupportHistory> shList = suService.selectMySupportList(pi, uNo);

		model.addAttribute("shList", shList);
		model.addAttribute("pi", pi);

		return "supportListUser";
	}

	@RequestMapping("supportApplicationListUser.su")
	public String supportApplicationListUser(HttpSession session, Model model,@RequestParam(value = "page", required = false) Integer currentPage) {
		int uNo = ((User) session.getAttribute("loginUser")).getuNo();

		if (currentPage == null) {
			currentPage = 1;
		}
		
		int listCount = suService.getApplyListUser(uNo);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		
		ArrayList<Support> sList = suService.selectApplyListUser(pi,uNo);
		model.addAttribute("sList", sList);
		model.addAttribute("pi", pi);
		
		return "supportApplicationListUser";
	}

	@RequestMapping("supportWrite.su")
	public String supportWrite() {
		return "supportWrite";
	}

	@RequestMapping("supportApply.su")
	public String supportApply(HttpSession session, @ModelAttribute Support s,@RequestParam("imageUrl") String imageUrl) {

		int uNo = ((User) session.getAttribute("loginUser")).getuNo();
		String registar = ((User) session.getAttribute("loginUser")).getRegistrar();
		s.setUserNo(uNo);
		s.setRegistar(registar);
		s.setImageUrl(imageUrl);
		
		System.out.println(s);
		
		int result = suService.supportApply(s);

		if (result > 0) {
			return "redirect:supportApplicationListUser.su";
		} else {
			throw new SupportException("신청에  실패하였습니다.");
		}
	}

	@RequestMapping("supportListAdmin.su")
	public String supportListAdmin(@RequestParam(value = "page", required = false) Integer currentPage, Model model) {

		if (currentPage == null) {
			currentPage = 1;
		}

		int listCount = suService.getListCount();

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		ArrayList<Support> sList = suService.selectSupportList(pi);

		if (sList != null) {
			model.addAttribute("pi", pi);
			model.addAttribute("sList", sList);
			return "supportListAdmin";
		} else {
			throw new SupportException("없음");
		}
	}

	@RequestMapping("supportApplyListAdmin.su")
	public String supportApplyListAdmin(@RequestParam(value = "page", required = false) Integer currentPage,
			Model model) {
		if (currentPage == null) {
			currentPage = 1;
		}

		int listCount = suService.getWListCount();

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Support> sList = suService.selectApplyList(pi);

		if (sList != null) {
			model.addAttribute("pi", pi);
			model.addAttribute("sList", sList);
			return "supportApplicationListAdmin";
		} else {
			throw new SupportException("신청 내역이 없음");
		}
	}

	@RequestMapping("applyDevision.su")
	public String applyDevision(@RequestParam(value = "page", required = false) Integer currentPage,
			@RequestParam(value="status", required=false) String status,@RequestParam(value="category", required=false) String category, @RequestParam(value="searchWord", required=false) String searchWord, Model model) {
		if (currentPage == null) {
			currentPage = 1;
		}
		
		Support s = new Support();
		s.setSupportCategory(category);
		s.setSupportTitle(searchWord);
		s.setStatus(status.charAt(0));

		int listCount = suService.getApplyListCount(s);
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Support> sList = suService.applyListAdmin(pi, s);

		model.addAttribute("sList", sList);
		model.addAttribute("pi", pi);
		model.addAttribute("category", category);
		model.addAttribute("status", status);
		model.addAttribute("searchWord", searchWord);
		
		return "supportApplicationListAdmin";

	}

	@RequestMapping("supportDetailAdmin.su")
	public String supportDetailAdmin(@RequestParam("supportNo") int supportNo, Model model) {
		Support s = suService.supportDetail(supportNo);

		model.addAttribute("s", s);
		if (s.getStatus() != 'Y') {
			return "supportApplyDetailAdmin";
		} else {
			return "supportDetail";
		}

	}

	@RequestMapping("updateApplyStatus.su")
	public ModelAndView updateApplyStatus(@RequestParam("supportNo") int supportNo,
			@RequestParam("status") String status, ModelMap model) {
		Support s = new Support();

		s.setStatus(status.charAt(0));
		s.setSupportNo(supportNo);
		
		int result = suService.updateApplyStatus(s);
		if(status.equals("Y")) {			
			int result2 = suService.updateStartDate(supportNo);
		}
		
		if (result > 0) {
			if (status.equals('Y')) {
				model.addAttribute("supportNo", supportNo);
				return new ModelAndView("redirect:supportDetail.su", model);
			} else {
				return new ModelAndView("redirect:supportApplyListAdmin.su");
			}
		} else {
			throw new SupportException("후원 상태 수정에 실패하였습니다.");
		}
	}

	@RequestMapping("supportEndListAdmin.su")
	public String supportEndListAdmin(@RequestParam(value = "page", required = false) Integer currentPage,
			Model model) {

		if (currentPage == null) {
			currentPage = 1;
		}

		int listCount = suService.getEListCount();

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		ArrayList<Support> sList = suService.selectEndSupportList(pi);

		if (sList != null) {
			model.addAttribute("pi", pi);
			model.addAttribute("sList", sList);
			return "supportEndListAdmin";
		} else {
			throw new SupportException("없음");
		}
	}

	@RequestMapping("searchSupportListAdmin.su")
	public String searchSupportListAdmin(@RequestParam(value = "page", required = false) Integer currentPage,
			@RequestParam("searchWord") String searchWord, Model model) {

		if (currentPage == null) {
			currentPage = 1;
		}
		
		int listCount = suService.getSeachListCount(searchWord.trim());

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		ArrayList<Support> sList = suService.selectSearchListAdmin(pi, searchWord.trim());
		model.addAttribute("pi", pi);
		model.addAttribute("sList", sList);
		model.addAttribute("searchWord", searchWord.trim());
		return "supportListAdmin";
	}

	@RequestMapping("searchApplyList.su")
	public String searchApplyList(@RequestParam(value = "page", required = false) Integer currentPage,
								 HttpSession session, @RequestParam(value="searchWord", required=false) String searchWord,
								 @RequestParam(value="category", required=false) String category, @RequestParam(value="status", required=false) String status,
								 Model model) {
		
		
		Support s = new Support();
		s.setSupportCategory(category);
		s.setSupportTitle(searchWord);
		s.setStatus(status.charAt(0));
		
		int uNo = ((User) session.getAttribute("loginUser")).getuNo();

		if (currentPage == null) {
			currentPage = 1;
		}
		s.setUserNo(uNo);
		s.setSupportTitle(s.getSupportTitle().trim());
		
		int listCount = suService.getSearchListCount(s);

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		ArrayList<Support> sList = suService.selectApplySearchList(pi, s);
		model.addAttribute("pi", pi);
		model.addAttribute("sList", sList);
		model.addAttribute("searchWord", s.getSupportTitle().trim());
		model.addAttribute("category", s.getSupportCategory());
		model.addAttribute("status", s.getStatus());
		return "supportApplicationListUser";

	}

	@RequestMapping("supporterListEach.su")
	public String supporterListEach(@RequestParam(value = "page", required = false) Integer currentPage,
			@RequestParam("supportNo") int supportNo, Model model) {

		if (currentPage == null) {
			currentPage = 1;
		}

		int listCount = suService.getSupporterListCount(supportNo);
		System.out.println("supportNo 넘어옴?" + supportNo);
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		ArrayList<SupportHistory> shList = suService.selectSupporterListEach(pi, supportNo);
		System.out.println("이게 널널ㄴ러널잉라고?" + shList);
		model.addAttribute("shList", shList);
		model.addAttribute("pi", pi);
		model.addAttribute("supportNo", supportNo);
		return "supporterList";

	}

	@RequestMapping("supporterListAdmin.su")
	public String supporterListAdmin(@RequestParam(value = "page", required = false) Integer currentPage, Model model) {
		if (currentPage == null) {
			currentPage = 1;
		}

		int listCount = suService.getSupporterListAllCount();
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		ArrayList<SupportHistory> shList = suService.selectSupporterList(pi);
		model.addAttribute("shList", shList);
		model.addAttribute("pi", pi);
		return "supporterList";

	}

	@RequestMapping("insertSupporter.su")
	@ResponseBody
	public String insertSupporter(HttpSession session, @ModelAttribute SupportHistory sh) {

		Integer uNo = ((User) session.getAttribute("loginUser")).getuNo();

		sh.setUserNo(uNo);

		String supporterType = null;
		if (sh.getSupporterType().equals("숨은 천사")) {
			supporterType = "익명";
		} else {
			supporterType = "본명";
		}

		sh.setSupporterType(supporterType);


		int count = suService.insertSupporter(sh);
		int upFundAmount = suService.updateFundAmount(sh);
		
		String result = count > 0 ? "yes" : "no";
		return result;

	}

	@RequestMapping("searchMyList.su")
	public String searchMyList(@RequestParam(value = "page", required = false) Integer currentPage,
			@RequestParam("searchWord") String searchWord, Model model, HttpSession session) {

		Integer uNo = ((User) session.getAttribute("loginUser")).getuNo();

		SupportHistory sh = new SupportHistory();

		sh.setUserNo(uNo);
		sh.setSupportTitle(searchWord.trim());

		System.out.println(sh);

		if (currentPage == null) {
			currentPage = 1;
		}

		int listCount = suService.getSearchMyListCount(sh);
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		ArrayList<SupportHistory> shList = suService.searchMyList(pi, sh);

		model.addAttribute("shList", shList);
		model.addAttribute("pi", pi);
		model.addAttribute("searchWord", searchWord.trim());
		return "supportListUser";
	}

	@RequestMapping("searchEndList.su")
	public String searchEndList(@RequestParam(value = "page", required = false) Integer currentPage,
			@RequestParam("searchWord") String searchWord, Model model, HttpSession session) {
		
		if (currentPage == null) {
			currentPage = 1;
		}

		int listCount = suService.getSearchEListCount(searchWord.trim());

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		ArrayList<Support> sList = suService.selectSearchEndSupportList(pi, searchWord.trim());

		if (sList != null) {
			model.addAttribute("pi", pi);
			model.addAttribute("sList", sList);
			model.addAttribute("searchWord", searchWord.trim());
			return "supportEndListAdmin";
		} else {
			throw new SupportException("없음");
		}
	}
	
	@RequestMapping("categoryListAdmin.su")
	public String categoryListAdmin(@RequestParam("category") String category,
									@RequestParam(value = "page", required = false) Integer currentPage,
									@RequestParam(value = "searchWord", required=false) String searchWord,
									Model model) {
		
		System.out.println(category);
		System.out.println(searchWord);
		
		if (currentPage == null) {
			currentPage = 1;
		}
		
		Support s = new Support();
		s.setSupportCategory(category);
		
		System.out.println("set한 category" + category);
		if(searchWord == null || searchWord.trim().equals("")) {
			s.setSupportTitle(null);
		} else {
			s.setSupportTitle(searchWord.trim());
		}
		
		int listCount = suService.getCategoryCount(s);

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		
		ArrayList<Support> sList = suService.selectCategoryListAdmin(pi, s);

		
		if(searchWord == null || searchWord.trim().equals("")) {
			model.addAttribute("sList", sList);
			model.addAttribute("pi", pi);
			model.addAttribute("category", category);
			return "supportListAdmin";
		} else {
			model.addAttribute("sList", sList);
			model.addAttribute("pi", pi);
			model.addAttribute("category", category);
			model.addAttribute("searchWord", searchWord);
			return "supportListAdmin";
		}	

	}
	
	@RequestMapping("mainCategory.su")
	public String mainCategory(@RequestParam("category") String category, 
							   @RequestParam(value = "page", required = false) Integer currentPage,
							   @RequestParam(value="searchWord", required = false) String searchWord,
								Model model) {
		
		
		
		if(category.equals("전체")) {
			return "redirect:supportMain.su";
		}
		
		int maintotalCount = suService.maintotalCount();
		int maintotalAmount = suService.maintotalAmount();
		
		if (currentPage == null) {
			currentPage = 1;
		}
		
		Support s = new Support();
		s.setSupportCategory(category);
		
		if(searchWord == null || searchWord.trim().equals("")) {
			s.setSupportTitle(null);
		} else {
			s.setSupportTitle(searchWord.trim());
		}
		
		int listCount = suService.getCategoryCount(s);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 11);
		ArrayList<Support> sList = suService.selectCategoryListAdmin(pi, s);
		
		if(searchWord == null || searchWord.trim().equals("")) {
			model.addAttribute("sList", sList);
			model.addAttribute("pi", pi);
			model.addAttribute("category", category);
			model.addAttribute("totalCount", maintotalCount);
			model.addAttribute("totalAmount", maintotalAmount);
			return "supportMain";
		} else {
			model.addAttribute("sList", sList);
			model.addAttribute("pi", pi);
			model.addAttribute("category", category);
			model.addAttribute("searchWord", searchWord);
			model.addAttribute("totalCount", maintotalCount);
			model.addAttribute("totalAmount", maintotalAmount);
			return "supportMain";
		}
		
	}
	
	@RequestMapping("mainSearch.su")
	public String mainSearch(@RequestParam("searchWord") String searchWord,
							@RequestParam(value = "page", required = false) Integer currentPage,
								Model model ) {
		
		int maintotalCount = suService.maintotalCount();
		int maintotalAmount = suService.maintotalAmount();
		
		if(searchWord.trim().equals("")) {
			return "redirect:supportMain.su";
		}
		
		if (currentPage == null) {
			currentPage = 1;
		}
		
		int listCount = suService.getSeachListCount(searchWord.trim());

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 11);

		ArrayList<Support> sList = suService.selectSearchListAdmin(pi, searchWord.trim());
		
		model.addAttribute("pi", pi);
		model.addAttribute("sList", sList);
		model.addAttribute("searchWord", searchWord.trim());
		model.addAttribute("totalCount", maintotalCount);
		model.addAttribute("totalAmount", maintotalAmount);
		return "supportMain";
		
		
	}
	
	@RequestMapping("categorySupportListUser.su")
	public String categorySupportListUser(@RequestParam(value = "page", required = false) Integer currentPage,
											Model model, @RequestParam("category") String category, 
											@RequestParam(value="searchWord", required=false) String searchWord, HttpSession session) {
		
		if (currentPage == null) {
			currentPage = 1;
		}

		int uNo = ((User) session.getAttribute("loginUser")).getuNo();
		SupportHistory sh = new SupportHistory();
		sh.setUserNo(uNo);
		sh.setCategory(category);
		if(searchWord == null || searchWord.trim().equals("")) {
			sh.setSupportTitle(null);
		} else {
			sh.setSupportTitle(searchWord.trim());
		}
		
		System.out.println(sh);
		
		int listCount = suService.getMyListCount(sh);

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		ArrayList<SupportHistory> shList = suService.cateMySupportList(pi, sh);
		System.out.println("category: " + category);
		System.out.println("searchWord: " + searchWord);
		System.out.println(shList);
		
		if(searchWord == null || searchWord.trim().equals("")) {
			model.addAttribute("shList", shList);
			model.addAttribute("pi", pi);
			model.addAttribute("category", category);
			return "supportListUser";
		} else {
			model.addAttribute("shList", shList);
			model.addAttribute("pi", pi);
			model.addAttribute("category", category);
			model.addAttribute("searchWord", searchWord);
			return "supportListUser";
		}

	}
	
	@RequestMapping("reloadDetail.su")
	public void relodaDetail(@RequestParam("supportNo") int supportNo, HttpServletResponse response) {
		Support s = suService.supportDetail(supportNo);
		
		response.setContentType("application/json; charset=UTF-8");
		Gson gson = new Gson();
		try {
			gson.toJson(s, response.getWriter());
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping("categoryEndListAdmin.su")
	public String categoryEndListAdmin(@RequestParam("category") String category,
										@RequestParam(value = "page", required = false) Integer currentPage,
										@RequestParam(value = "searchWord", required=false) String searchWord, ModelMap model) {
		if (currentPage == null) {
			currentPage = 1;
		}
		
		Support s = new Support();
		s.setSupportCategory(category);
		s.setSupportTitle(searchWord);
		
		int listCount = suService.getCateEndListCount(s);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Support> sList = suService.cateEndSupportList(pi, s);
		
		model.addAttribute("searchWord", searchWord);
		model.addAttribute("category", category);
		model.addAttribute("sList", sList);
		model.addAttribute("pi", pi);
		return "supportEndListAdmin";
	}
	
	@RequestMapping("deleteSupport.su")
	public String deleteSupport(@RequestParam("supportNo") int supportNo) {
		Support s = new Support();
		s.setStatus('N');
		s.setSupportNo(supportNo);
		int result = suService.updateApplyStatus(s);
		if (result > 0) {
			return "redirect:supportMain.su";
		} else {
			throw new SupportException("후원 삭제에 실패하였습니다.");
		}
	}
	
	@RequestMapping("supportEdit.su")
	public String EditView(@RequestParam("supportNo") int supportNo, Model model) {
		Support s = suService.supportDetail(supportNo);
		
		model.addAttribute("s", s);
		return "supportEdit";
	}
	
	@RequestMapping("updateSupport.su")
	public ModelAndView updateSupport(@ModelAttribute Support s, ModelMap model) {
		System.out.println(s);
		
		int result = suService.updateSupport(s);
		if(result>0) {			
			model.addAttribute("supportNo", s.getSupportNo());
			return new ModelAndView("redirect:supportDetail.su", model);
		} else {
			throw new SupportException("후원 수정에 실패했습니다.");
		}
	}
}
