package com.fin.proj.support.controller;

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
import com.fin.proj.member.model.vo.Member;
import com.fin.proj.support.model.exception.SupportException;
import com.fin.proj.support.model.service.SupportService;
import com.fin.proj.support.model.vo.Support;
import com.fin.proj.support.model.vo.SupportDetail;
import com.fin.proj.support.model.vo.SupportHistory;

import jakarta.mail.Multipart;
import jakarta.servlet.http.HttpSession;

//import com.fin.proj.support.model.service.SupportService;

@Controller
public class SupportController {
	@Autowired
	private SupportService suService;

	@RequestMapping("supportMain.su")
	public String supportMain() {
		int result = suService.getListCount();
//		ArrayList<Support> sList = suService.selectSupportList();
		return "supportMain";
	}

	@RequestMapping("supportDetail.su")
	public String supportDetail(HttpSession session, @RequestParam("supportNo") int supportNo, Model model) {
		Support s = suService.supportDetail(supportNo);

		int uNo = ((Member) session.getAttribute("loginUser")).getuNo();
		int isAdmin = ((Member) session.getAttribute("loginUser")).getIsAdmin();

		model.addAttribute("s", s);

		if (s.getStatus() == 'Y') {
			return "supportDetail";
		} else {
			if (uNo == s.getUserNo() || isAdmin == 0) {
				return "supportApplyDetail";
			} else {
				throw new SupportException("잘못된 접근입니다.");
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
	public String doSupportEnd(HttpSession session) {
		return "doSupportEnd";
	}

	@RequestMapping("supportListUser.su")
	public String supportListUser(@RequestParam(value = "page", required = false) Integer currentPage,
			HttpSession session, Model model) {

		if (currentPage == null) {
			currentPage = 1;
		}

		int uNo = ((Member) session.getAttribute("loginUser")).getuNo();

		System.out.println(uNo);

		int listCount = suService.getMListCount(uNo);

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		ArrayList<SupportHistory> shList = suService.selectMySupportList(pi, uNo);

		model.addAttribute("shList", shList);
		model.addAttribute("pi", pi);

		return "supportListUser";
	}

	@RequestMapping("supportApplicationListUser.su")
	public String supportApplicationListUser(HttpSession session, Model model) {
		int uNo = ((Member) session.getAttribute("loginUser")).getuNo();
		ArrayList<Support> sList = suService.selectApplyListUser(uNo);
		model.addAttribute("sList", sList);
		return "supportApplicationListUser";
	}

	@RequestMapping("supportWrite.su")
	public String supportWrite() {
		return "supportWrite";
	}

	@RequestMapping("supportApply.su")
	public String supportApply(HttpSession session, @ModelAttribute Support s, @RequestParam("file") Multipart file) {

		int uNo = ((Member) session.getAttribute("loginUser")).getuNo();
		String registar = ((Member) session.getAttribute("loginUser")).getRegistrar();
		s.setUserNo(uNo);
		s.setRegistar(registar);

		int result = suService.supportApply(s);

		if (result > 0) {
			return "redirect:supportApplicationListUser.su";
		} else {
			throw new SupportException("신청에 실패하였습니다.");
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
			@RequestParam("division") String division, Model model) {
		if (currentPage == null) {
			currentPage = 1;
		}

		int listCount = suService.getDListCount(division);
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Support> sList = suService.applyDevision(pi, division);

		model.addAttribute("sList", sList);
		model.addAttribute("pi", pi);
		return "supportApplicationListAdmin";

	}

	@RequestMapping("supportDetailAdmin.su")
	public String supportDetailAdmin(@RequestParam("supportNo") int supportNo, Model model) {
		Support s = suService.supportDetail(supportNo);
		ArrayList<SupportDetail> sdList = suService.supportUsageDetail(supportNo);

		model.addAttribute("s", s);
		model.addAttribute("sdList", sdList);
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
		int listCount = suService.getSeachListCount(searchWord);

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		ArrayList<Support> sList = suService.selectSearchListAdmin(pi, searchWord);
		System.out.println(searchWord);
		System.out.println(sList);
		model.addAttribute("pi", pi);
		model.addAttribute("sList", sList);
		return "supportListAdmin";
	}

	@RequestMapping("searchApplyList.su")
	public String searchApplyList(@RequestParam(value = "page", required = false) Integer currentPage,
			HttpSession session, @RequestParam("searchWord") String searchWord, Model model) {
		int uNo = ((Member) session.getAttribute("loginUser")).getuNo();

		if (currentPage == null) {
			currentPage = 1;
		}

		Support s = new Support();
		s.setSupportTitle(searchWord);
		s.setUserNo(uNo);

		int listCount = suService.getSearchListCount(s);

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		ArrayList<Support> sList = suService.selectApplySearchList(pi, s);
		model.addAttribute("pi", pi);
		model.addAttribute("sList", sList);
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

		System.out.println("처음에 들어오는 값 : " + sh);
		Integer uNo = ((Member) session.getAttribute("loginUser")).getuNo();

		sh.setUserNo(uNo);
		System.out.println("uNO:" + uNo);
		System.out.println("sh.getUserNo:" + sh.getUserNo());

		String supporterType = null;
		if (sh.getSupporterType().equals("숨은 천사")) {
			supporterType = "익명";
		} else {
			supporterType = "본명";
		}

		sh.setSupporterType(supporterType);

		System.out.println(sh);

		int count = suService.insertSupporter(sh);
		String result = count > 0 ? "yes" : "no";
		return result;

	}

	@RequestMapping("searchMyList.su")
	public String searchMyList(@RequestParam(value = "page", required = false) Integer currentPage,
			@RequestParam("searchWord") String searchWord, Model model, HttpSession session) {

		Integer uNo = ((Member) session.getAttribute("loginUser")).getuNo();

		SupportHistory sh = new SupportHistory();

		sh.setUserNo(uNo);
		sh.setSupportTitle(searchWord);

		System.out.println(sh);

		if (currentPage == null) {
			currentPage = 1;
		}

		int listCount = suService.getSearchMyListCount(sh);
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		ArrayList<SupportHistory> shList = suService.searchMyList(pi, sh);

		model.addAttribute("shList", shList);
		model.addAttribute("pi", pi);
		return "supportListUser";
	}

	@RequestMapping("searchEndList.su")
	public String searchEndList(@RequestParam(value = "page", required = false) Integer currentPage,
			@RequestParam("searchWord") String searchWord, Model model, HttpSession session) {
		
		if (currentPage == null) {
			currentPage = 1;
		}

		int listCount = suService.getSearchEListCount(searchWord);

		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);

		ArrayList<Support> sList = suService.selectSearchEndSupportList(pi, searchWord);

		if (sList != null) {
			model.addAttribute("pi", pi);
			model.addAttribute("sList", sList);
			return "supportEndListAdmin";
		} else {
			throw new SupportException("없음");
		}
	}
}
