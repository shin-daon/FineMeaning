package com.fin.proj.board.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fin.proj.board.model.exception.BoardException;
import com.fin.proj.board.model.service.BoardService;
import com.fin.proj.board.model.vo.Board;
import com.fin.proj.board.model.vo.Reply;
import com.fin.proj.common.Pagination;
import com.fin.proj.common.model.vo.PageInfo;
import com.fin.proj.member.model.vo.Member;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService bService;

	@GetMapping("faqMain.bo")
	public String faqMain(@RequestParam(value="page", required=false) Integer currentPage, Model model,
						  @RequestParam(value="category", required=false) Integer category, // '선택 없음' 시 null일 수 있음
						  @RequestParam(value="keyword", required=false) String keyword) {
		
//		System.out.println(keyword);
//		System.out.println(category);
		// 키워드 매개변수로 받아서 전체적인 흐름은 fruitMain.bo와 비슷한 맥락으로 진행!
		
		ArrayList<Board> list;
		PageInfo pageInfo;
		int listCount;
		
		HashMap<String, Object> map = new HashMap<>();
		
		if(currentPage == null) {
			currentPage = 1;
		}
		
		if(keyword != null) {
			map.put("keyword", keyword);
			map.put("i", "자주 묻는 질문");
			if(category == null) { // '선택 없음'의 경우
				map.put("category", 0);
			} else {	// 나머지 카테고리 있는 경우
				map.put("category", category);
			}
			listCount = bService.searchListCount(map);
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 2);
			list = bService.searchByTitleAndCategory(pageInfo, map);
		} else {
			listCount = bService.getListCount("자주 묻는 질문");
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 2);
			list = bService.selectBoardList(pageInfo, "자주 묻는 질문");
		}
		
		if(list != null) {
			model.addAttribute("pi", pageInfo);
			model.addAttribute("list", list);
			model.addAttribute("map", map);
			return "faq";
		} else {
			throw new BoardException("게시글 목록 조회 실패");
		}
	}
	
	@GetMapping("faqDetail.bo")
	public String faqDetail(@RequestParam("bNo") int bNo, @RequestParam("writer") String writer,
							@RequestParam("page") int page, HttpSession session, Model model,
							@RequestParam(value="keyword", required=false) String keyword, @RequestParam(value="category", required=false) Integer category) {
//		System.out.println(bNo + ", " + writer + ", "+ page);
		// 상세보기의 경우 조회수 +1, but 내 글 클릭 시 조회수 +0 (로그인 유저 정보 가져와서 비교)
		// 파라미터로 받은 page를 통해 목록으로 돌아갔을 시 원래 보던 페이지 노출
		
		Member m = (Member)session.getAttribute("loginUser");
//		System.out.println(m);
		
		String readerNickName = null;
		if(m != null) {
			readerNickName = m.getuNickName();
		}
		
		boolean countYN = false;
		if(!writer.equals(readerNickName)) {
			countYN = true;
		}
		
		Board board = bService.selectBoard(bNo, countYN);
//		System.out.println(board);
		
//		ArrayList<Reply> replyList = bService.selectReply(bNo);
//		System.out.println(replyList);
		
		if(board != null) {
			model.addAttribute("board", board);
			model.addAttribute("page", page);
			if(keyword != null) {
				model.addAttribute("keyword", keyword);
			}
			if(category != null) {
				model.addAttribute("category", category);
			}
			return "faq_Detail";
		} else {
			throw new BoardException("게시글 상세 조회 실패");
		}
	}
	
	@GetMapping("faqForm.bo")
	public String faqForm() {
		return "faq_form";
	}
	
	@PostMapping("insertFaq.bo")
	public String insertFaq(@ModelAttribute Board b, HttpSession session) {
		
		int uNo = ((Member)session.getAttribute("loginUser")).getuNo();
		b.setuNo(uNo);
		b.setBoardType("자주 묻는 질문");
		
		int result = bService.insertBoardWithCategory(b);
		
		if(result > 0) {
			return "redirect:faqMain.bo";
		} else {
			throw new BoardException("게시글 작성 실패");
		}
	}
	
	@GetMapping("faqAdmin.bo")
	public String faqAdmin(@RequestParam(value="page", required=false) Integer currentPage, Model model,
			 			   @RequestParam(value="category", required=false) Integer category, // '선택 없음' 시 null일 수 있음
			 			   @RequestParam(value="keyword", required=false) String keyword) {
		
		ArrayList<Board> list;
		int listCount;
		PageInfo pageInfo;
		
		HashMap<String, Object> params = new HashMap<>();
		
		if(currentPage == null) {
			currentPage = 1;
		}
		
		if(keyword != null) {
			params.put("keyword", keyword);
			params.put("i", "자주 묻는 질문");
			if(category == null) { // '선택 없음'의 경우
				params.put("category", 0);
			} else {	 // 나머지 카테고리 있는 경우
				params.put("category", category);
			}
			listCount = bService.searchListCount(params);
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 2);
			list = bService.searchByTitleAndCategory(pageInfo, params);
		} else { // 페이지 로드 시 메인 페이지
			listCount = bService.getListCount("자주 묻는 질문");
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 2);
			list = bService.selectBoardList(pageInfo, "자주 묻는 질문");
//			System.out.println(list);
		}
		
		if(list != null) {
			model.addAttribute("pi", pageInfo);
			model.addAttribute("list", list);
			model.addAttribute("map", params);
			return "faqAdmin";
		} else {
			throw new BoardException("게시글 목록 조회 실패");
		}
	}
	
	@GetMapping("faqEdit.bo")
	public String faqEdit(@RequestParam("bNo") String bNo, @RequestParam("page") int page,
						  Model model) {
		
//		System.out.println(bNo);
		
		Decoder decoder = Base64.getDecoder();
		byte[] byteArr = decoder.decode(bNo);
		String decode = new String(byteArr);
		int boardNo = Integer.parseInt(decode);
		
		Board b = bService.selectBoard(boardNo, false);
		
		model.addAttribute("board", b);
		model.addAttribute("page", page);
		return "faq_edit";
	}
	
	@PostMapping("updateFaq.bo")
	public String updateFaq(@ModelAttribute Board b, @RequestParam("page") int page, HttpSession session, RedirectAttributes ra) {
		
		int result = bService.updateBoard(b);
		
		if(result > 0) {
			ra.addAttribute("writer", ((Member)session.getAttribute("loginUser")).getuNickName());
			ra.addAttribute("bNo", b.getBoardNo());
			ra.addAttribute("page", page);
			return "redirect:faqDetail.bo";
		} else {
			throw new BoardException("글 수정 실패");
		}
	}
	
	@GetMapping("deleteFaq.bo")
	@Transactional
	public String deleteFaq(@RequestParam("bNo") String bNo) {
		
		Decoder decoder = Base64.getDecoder();
		byte[] byteArr = decoder.decode(bNo);
		String decode = new String(byteArr);
		int boardNo = Integer.parseInt(decode);
		
		int result = bService.deleteBoard(boardNo);
		
		if(result > 0) {
			return "redirect:faqMain.bo";
		} else {
			throw new BoardException("게시글 삭제 실패");
		}
	}
	
	@GetMapping("finePeopleMain.bo")
	public String finePeopleMain(@RequestParam(value="page", required=false) Integer currentPage, Model model) {
		
		if(currentPage == null) {
			currentPage = 1;
		}
		
		int listCount = bService.getListCount("선뜻한 사람");
//		System.out.println(listCount);
		
		PageInfo pageInfo = Pagination.getPageInfo(currentPage, listCount, 5);
		
		ArrayList<Board> list = bService.selectBoardList(pageInfo, "선뜻한 사람");
//		System.out.println(list);
		
		if(list != null) {
			model.addAttribute("pi", pageInfo);
			model.addAttribute("list", list);
			return "finePeople";
		} else {
			throw new BoardException("게시글 목록 조회 실패");
		}
	}
	
	@GetMapping("finePeopleForm.bo")
	public String finePeopleForm() {
		return "finePeople_form";
	}
	
	@PostMapping("insertFinePeople.bo")
	public String insertFinePeople(@ModelAttribute Board b, HttpSession session) {
		
		int uNo = ((Member)session.getAttribute("loginUser")).getuNo();
		b.setuNo(uNo);
		b.setBoardType("선뜻한 사람");
		
		int result = bService.insertBoard(b);
		
		if(result > 0) {
			return "redirect:finePeopleAdmin.bo";
		} else {
			throw new BoardException("게시물 작성 실패");
		}
	}

	@GetMapping("finePeopleAdmin.bo")
	public String finePeopleAdmin(@RequestParam(value="page", required=false) Integer currentPage, Model model,
								  @RequestParam(value="keyword", required=false) String keyword) {
		
//		System.out.println(keyword);
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Board> list;
		PageInfo pageInfo;
		int listCount;
		
		if(currentPage == null) {
			currentPage = 1;
		}
		
		if(keyword != null) {
			map.put("keyword", keyword);
			map.put("i", "선뜻한 사람");
			listCount = bService.finePeopleCount(map);
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
			list = bService.searchByFpName(pageInfo, map);
		} else {
			listCount = bService.getListCount("선뜻한 사람");
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
			list = bService.selectBoardList(pageInfo, "선뜻한 사람");
		}
		
		if(list != null) {
			model.addAttribute("pi", pageInfo);
			model.addAttribute("list", list);
			model.addAttribute("map", map);
			return "finePeopleAdmin";
		} else {
			throw new BoardException("게시글 목록 조회 실패");
		}
	}
	
	@GetMapping("finePeopleEdit.bo")
	public String finePeopleEdit(@RequestParam("bNo") String bNo, @RequestParam("page") int page,
									   Model model) {
		
		Decoder decoder = Base64.getDecoder();
		byte[] byteArr = decoder.decode(bNo);
		String decode = new String(byteArr);
		int boardNo = Integer.parseInt(decode);
		
		Board b = bService.selectBoard(boardNo, false);
//		System.out.println(b);
		
		model.addAttribute("board", b);
		model.addAttribute("page", page);
		return "finePeople_edit";
	}
	
	@PostMapping("updateFinePeople.bo")
	public String updateFinePeople(@ModelAttribute Board b, @RequestParam("page") int page, RedirectAttributes ra) {
//		System.out.println(b);
//		System.out.println(page);
		
		int result = bService.updateBoard(b);
		
		if(result > 0) {
			ra.addAttribute("page", page);
			return "redirect:finePeopleAdmin.bo";
		} else {
			throw new BoardException("글 수정 실패");
		}
	}
	
	@GetMapping("deleteFinePeople.bo")
	public String deleteFinePeople(@RequestParam("bNo") String bNo) {

		Decoder decoder = Base64.getDecoder();
		byte[] byteArr = decoder.decode(bNo);
		String decode = new String(byteArr);
		int boardNo = Integer.parseInt(decode);
		
		int result = bService.deleteBoard(boardNo);
		
		if(result > 0) {
			return "redirect:finePeopleAdmin.bo";
		} else {
			throw new BoardException("게시글 삭제 실패");
		}
	}
	
	@GetMapping("fruitMain.bo")
	public String fruitMain(@RequestParam(value="page", required=false) Integer currentPage, Model model,
							@RequestParam(value="category", required=false) Integer category, // '선택 없음' 시 null일 수 있음
							@RequestParam(value="keyword", required=false) String keyword) {
		
		ArrayList<Board> list;
		int listCount;
		PageInfo pageInfo;
		
		HashMap<String, Object> params = new HashMap<>();
		
		if(currentPage == null) {
			currentPage = 1;
		}
		
		if(keyword != null) {
			params.put("keyword", keyword);
			params.put("i", "결실");
			if(category == null) { // '선택 없음'의 경우
				params.put("category", 0);
			} else {	 // '후원' 혹은 '봉사'의 경우
				params.put("category", category);
			}
			listCount = bService.searchListCount(params);
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 2);
			list = bService.searchByTitleAndCategory(pageInfo, params);
		} else { // 페이지 로드 시 메인 페이지
			listCount = bService.getListCount("결실");
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 2);
			list = bService.selectBoardList(pageInfo, "결실");
//			System.out.println(list);
		}
		
		if(list != null) {
			model.addAttribute("pi", pageInfo);
			model.addAttribute("list", list);
			model.addAttribute("params", params);
			return "fruit";
		} else {
			throw new BoardException("게시글 목록 조회 실패");
		}
	}
	
	@GetMapping("fruitDetail.bo")
	public String fruitDetail(@RequestParam("bNo") int bNo, @RequestParam("page") int page,
							  HttpSession session, Model model, @RequestParam(value="replyPage", required=false) Integer replyPage,
							  @RequestParam(value="keyword", required=false) String keyword, @RequestParam(value="category", required=false) Integer category) {
		
		
		Member m = (Member)session.getAttribute("loginUser");
//		System.out.println(m);
		
		boolean countYN = false;
		if(m == null || m.getIsAdmin() == 1) {
			countYN = true;
		}
		
		Board board = bService.selectBoard(bNo, countYN);
//		System.out.println(board);
		
		if(replyPage == null) {
			replyPage = 1;
		}
		
		int replyListCount = bService.replyCount(bNo);
		PageInfo pageInfo = Pagination.getPageInfo(replyPage, replyListCount, 5);
		ArrayList<Reply> replyList = bService.selectReplyList(pageInfo, bNo);
//		System.out.println(pageInfo);
//		ArrayList<Reply> replyList = bService.selectReply(bNo);
		
		if(board != null) {
			model.addAttribute("board", board);
			model.addAttribute("page", page);
			model.addAttribute("replyList", replyList);
			model.addAttribute("pi", pageInfo);
			model.addAttribute("bNo", bNo);
			if(keyword != null) {
				model.addAttribute("keyword", keyword);
			}
			if(category!= null) {
				model.addAttribute("category", category);
			}
			return "fruit_detail";
		} else {
			throw new BoardException("게시글 상세 조회 실패");
		}
	}
	
	@GetMapping("fruitForm.bo")
	public String fruitForm() {
		return "fruit_form";
	}
	
	@PostMapping("insertFruit.bo")
	public String insertFruit(@ModelAttribute Board b, HttpSession session) {
		
//		System.out.println(b);
		int uNo = ((Member)session.getAttribute("loginUser")).getuNo();
		b.setuNo(uNo);
		b.setBoardType("결실");
		
		int result = bService.insertBoardWithCategory(b);
		
		if(result > 0) {
			return "redirect:fruitAdmin.bo";
		} else {
			throw new BoardException("게시글 작성 실패");
		}
	}
	
	@GetMapping("fruitAdmin.bo")
	public String fruitAdmin(@RequestParam(value="page", required=false) Integer currentPage, Model model,
							 @RequestParam(value="category", required=false) Integer category, // '선택 없음' 시 null일 수 있음
							 @RequestParam(value="keyword", required=false) String keyword) {
		
		ArrayList<Board> list;
		int listCount;
		PageInfo pageInfo;
		
		HashMap<String, Object> params = new HashMap<>();
		
		if(currentPage == null) {
			currentPage = 1;
		}
		
		if(keyword != null) {
			params.put("keyword", keyword);
			params.put("i", "결실");
			if(category == null) { // '선택 없음'의 경우
				params.put("category", 0);
			} else {	 // '후원' 혹은 '봉사'의 경우
				params.put("category", category);
			}
			listCount = bService.searchListCount(params);
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 2);
			list = bService.searchByTitleAndCategory(pageInfo, params);
		} else { // 페이지 로드 시 메인 페이지
			listCount = bService.getListCount("결실");
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 2);
			list = bService.selectBoardList(pageInfo, "결실");
//			System.out.println(list);
		}
		
		if(list != null) {
			model.addAttribute("pi", pageInfo);
			model.addAttribute("list", list);
			model.addAttribute("params", params);
			return "fruitAdmin";
		} else {
			throw new BoardException("게시글 목록 조회 실패");
		}
	}
	
	@GetMapping("fruitEdit.bo")
	public String fruitEdit(@RequestParam("bNo") String bNo, @RequestParam("page") int page,
							Model model) {
		
//		System.out.println(bNo);
		
		Decoder decoder = Base64.getDecoder();
		byte[] byteArr = decoder.decode(bNo);
		String decode = new String(byteArr);
		int boardNo = Integer.parseInt(decode);
		
		Board b = bService.selectBoard(boardNo, false);
		
		model.addAttribute("board", b);
		model.addAttribute("page", page);
		
		return "fruit_edit"; 
	}
	
	@PostMapping("updateFruit.bo")
	public String updateFruit(@ModelAttribute Board b, @RequestParam("page") int page,
								HttpSession session, RedirectAttributes ra) {

//		System.out.println(b);
		
		int result = bService.updateBoard(b);
		
		if(result > 0) {
			ra.addAttribute("writer", ((Member)session.getAttribute("loginUser")).getuNickName());
			ra.addAttribute("bNo", b.getBoardNo());
			ra.addAttribute("page", page);
			
			return "redirect:fruitDetail.bo";
		} else {
			throw new BoardException("게시글 수정 실패");
		}
	}
	
	@GetMapping("deleteFruit.bo")
	public String deleteFruit(@RequestParam("bNo") String bNo) {

		Decoder decoder = Base64.getDecoder();
		byte[] byteArr = decoder.decode(bNo);
		String decode = new String(byteArr);
		int boardNo = Integer.parseInt(decode);
		
		int replyCount = bService.replyCount(boardNo);
		
		if(replyCount > 0) {
			int replyResult = bService.deleteReplyAll(boardNo);
			if(replyResult == 0) {
				throw new BoardException("게시글 내 댓글 삭제 실패");
			}
		}
		
		int result = bService.deleteBoard(boardNo);
		
		if(result > 0) {
			return "redirect:fruitAdmin.bo";
		} else {
			throw new BoardException("게시글 삭제 실패");
		}
	}
	
	@GetMapping("fineNewsMain.bo")
	public String fineNewsMain(@RequestParam(value="page", required=false) Integer currentPage, Model model){
		
		if(currentPage == null) {
			currentPage = 1;
		}
		
		int listCount = bService.getListCount("선한 뉴스");
		
		PageInfo pageInfo = Pagination.getPageInfo(currentPage, listCount, 9);
		
		ArrayList<Board> list = bService.selectBoardList(pageInfo, "선한 뉴스");
		
		if(list != null) {
			model.addAttribute("pi", pageInfo);
			model.addAttribute("list", list);
			return "fineNews";
		} else {
			throw new BoardException("게시글 목록 조회 실패");
		}
	}
	
	@GetMapping("fineNewsForm.bo")
	public String fineNewsForm() {
		return "fineNews_form";
	}
	
	@PostMapping("insertFineNews.bo")
	public String insertFineNews(@ModelAttribute Board b, HttpSession session, Model model) {
		
		int uNo = ((Member)session.getAttribute("loginUser")).getuNo();
		b.setuNo(uNo);
		b.setBoardType("선한 뉴스");
		
		int result = bService.insertBoard(b);
		
		if(result > 0) {
			return "redirect:fineNewsMain.bo";
		} else {
			throw new BoardException("게시물 작성 실패");
		}
	}
	
	@GetMapping("fineNewsAdmin.bo")
	public String fineNewsAdmin(@RequestParam(value="page", required=false) Integer currentPage, Model model,
			  					@RequestParam(value="keyword", required=false) String keyword) {
		
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Board> list;
		PageInfo pageInfo;
		int listCount;
		
		if(currentPage == null) {
			currentPage = 1;
		}
		
		if(keyword != null) {
			map.put("keyword", keyword);
			map.put("i", "선한 뉴스");
			listCount = bService.searchListCount(map);
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
			list = bService.searchByTitle(pageInfo, map);
		} else {
			listCount = bService.getListCount("선한 뉴스");
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
			list = bService.selectBoardList(pageInfo, "선한 뉴스");
		}
		
		if(list != null) {
			model.addAttribute("pi", pageInfo);
			model.addAttribute("list", list);
			model.addAttribute("map", map);
		}
		return "fineNewsAdmin";
	}
	
	@GetMapping("fineNewsEdit.bo")
	public String fineNewsEdit(@RequestParam("bNo") String bNo, @RequestParam("page") int page,
									Model model) {
		
		Decoder decoder = Base64.getDecoder();
		byte[] byteArr = decoder.decode(bNo);
		String decode = new String(byteArr);
		int boardNo = Integer.parseInt(decode);
		
		Board b = bService.selectBoard(boardNo, false);
		
		model.addAttribute("board", b);
		model.addAttribute("page", page);
		return "fineNews_edit";
	}
	
	@PostMapping("updateFineNews.bo")
	public String updateFineNews(@ModelAttribute Board b, @RequestParam("page") int page, RedirectAttributes ra) {
		
		int result = bService.updateBoard(b);
		
		if(result > 0) {
			ra.addAttribute("page", page);
			return "redirect:fineNewsAdmin.bo";
		} else {
			throw new BoardException("글 수정 실패");
		}
	}
	
	@GetMapping("deleteFineNews.bo")
	public String deleteFineNews(@RequestParam("bNo") String bNo) {
		
		Decoder decoder = Base64.getDecoder();
		byte[] byteArr = decoder.decode(bNo);
		String decode = new String(byteArr);
		int boardNo = Integer.parseInt(decode);
		
		int result = bService.deleteBoard(boardNo);
		if(result > 0) {
			return "redirect:fineNewsAdmin.bo";
		} else {
			throw new BoardException("게시글 삭제 실패");
		}
	}
	
	// 댓글
	@RequestMapping("insertReply.bo")
	public void insertReply(@ModelAttribute Reply r, HttpServletResponse response, @RequestParam("page") int page,
							Model model) {
		//@RequestParam(value="replyPage", required=false) Integer replyPage, 
//		System.out.println(r);
		bService.insertReply(r);
		
//		if(replyPage == null) {
//			replyPage = 1;
//		}
		
		int replyListCount = bService.replyCount(r.getBoardNo());
		PageInfo pageInfo = Pagination.getPageInfo(1, replyListCount, 5);
//		System.out.println(pageInfo);
		ArrayList<Reply> list = bService.selectReplyList(pageInfo, r.getBoardNo());
		
		response.setContentType("application/json; charset=UTF-8");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd / HH:mm:ss").create();
		try {
			Map<String, Object> data = new HashMap<>();
			data.put("list", list);
			data.put("pi", pageInfo);
			gson.toJson(data, response.getWriter());
		} catch (JsonIOException | IOException e) {
			e.printStackTrace();
		} 
	}
	
	@RequestMapping("deleteReply.bo")
	public String deleteReply(@RequestParam("rNo") String replyNo,
							  @RequestParam("bNo") int boardNo,
							  @RequestParam("page") int page,
							  RedirectAttributes ra) {
		
		System.out.println(page);
		System.out.println(replyNo);
		System.out.println(boardNo);
		
		Decoder decoder = Base64.getDecoder();
		byte[] byteArr = decoder.decode(replyNo);
		String decode = new String(byteArr);
		int rNo = Integer.parseInt(decode);
		
		int result = bService.deleteReply(rNo);

		if(result > 0) {
			ra.addAttribute("bNo", boardNo);
			ra.addAttribute("page", page);
			return "redirect:fruitDetail.bo";
		} else {
			throw new BoardException("댓글 삭제에 실패하였습니다.");
		}
		
		// replyNo 이용해 해당 댓글 삭제한 후,
		// boardNo 받아와서 selectReply 한 후 해당 디테일 페이지 띄워주기
		// RedirectAttribute 이용!
	}
	
	@RequestMapping("deleteMyReply.bo")
	@ResponseBody
	public String deleteMyReply(@RequestParam("replies") String replies) {
		
		for(String reply : replies.split(",")) {
			int rNo = Integer.parseInt(reply);
			int result = bService.deleteReply(rNo);
			if(result < 0) {
				throw new BoardException("댓글 삭제에 실패하였습니다.");
			}
		}
		return "";
	}
	
	// my page
	@GetMapping("myReply.bo")
	public String myReply(@RequestParam(value="page", required=false) Integer currentPage, 
						  HttpSession session, Model model) {	// string으로 (1, 4, 5)로 보내던지 split(",")로 구분
		
		PageInfo pageInfo;
		int listCount;
		
		if(currentPage == null) {
			currentPage = 1;
		}
		
		int uNo = ((Member)session.getAttribute("loginUser")).getuNo();
		
		listCount = bService.myReplyCount(uNo);
		pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Reply> list = bService.selectMyReply(pageInfo, uNo);
		System.out.println(list);
		
		if(list != null) {
			model.addAttribute("pi", pageInfo);
			model.addAttribute("list", list);
			return "myReply";
		} else {
			throw new BoardException("내 댓글 목록 조회 실패");
		}
	}
	
	@GetMapping("myBoard.bo")
	public String myBoard(@RequestParam(value="page", required=false) Integer currentPage, Model model,
			 				HttpSession session) {
		
		PageInfo pageInfo;
		int listCount;
		
		if(currentPage == null) {
			currentPage = 1;
		}
		
		int uNo = ((Member)session.getAttribute("loginUser")).getuNo();
		
		listCount = bService.myBoardCount(uNo);
		pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Board> list = bService.selectMyBoard(pageInfo, uNo);
		
		if(list != null) {
			model.addAttribute("pi", pageInfo);
			model.addAttribute("list", list);
			return "myBoard";
		} else {
			throw new BoardException("내가 작성한 글 목록 조회 실패");
		}
	}
	
	@GetMapping("commList.bo")
		public String CommMain(@RequestParam(value="page", required=false) Integer currentPage, Model model,
								@RequestParam(value="keyword", required=false) String keyword) {
		
		if(currentPage == null) {
			currentPage = 1;
		}
		
		int listCount = bService.getListCount("일반");
		
		PageInfo pageInfo= Pagination.getPageInfo(currentPage, listCount, 10);
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Board> list = bService.selectBoardList(pageInfo, "일반");
		
		if(keyword != null) {
			map.put("keyword", keyword);
			map.put("i", "일반");
			listCount = bService.searchListCount(map);
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
			list = bService.searchByTitle(pageInfo, map);
		} else {
			listCount = bService.getListCount("일반");
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
			list = bService.selectBoardList(pageInfo, "일반");
		}
		if(list != null) {
			model.addAttribute("pi", pageInfo);
			model.addAttribute("list", list);
			model.addAttribute("map", map);
			return "commList";
		} else {
			throw new BoardException("게시글 목록 조회 실패");
		}
	}
	
	@GetMapping("writeComm.bo")
	public String writeComm() {
		return "writeComm";
	}
	
	
	@GetMapping("commDetailPage.bo")
	public String CommDetail(@RequestParam("bNo") int bNo, @RequestParam("page") int page,
								@RequestParam("writer") String writer,
							  HttpSession session, Model model) {
		
		Member m = (Member)session.getAttribute("loginUser");
		
		String me = null;
		if(m != null) {
			me = m.getuNickName();
		}
		
		boolean countYN = false;
		if(!writer.equals(me)) {
			countYN = true;
		}
		
		Board board = bService.selectBoard(bNo, countYN);
		
		ArrayList<Reply> replyList = bService.selectReply(bNo);
		
		if(board != null) {
			model.addAttribute("board", board);
			model.addAttribute("page", page);
			model.addAttribute("replyList", replyList);
			return "commDetail";
		} else {
			throw new BoardException("게시글 상세 조회 실패");
		}
	}
	
	
	@PostMapping("insertCommBoard.bo")
	public String insertCommBoard(@ModelAttribute Board b, HttpSession session) {
		int id = ((Member)session.getAttribute("loginUser")).getuNo();
		System.out.println("id=" + id);
		b.setuNo(id);
		b.setBoardType("일반");
		b.setImageUrl(null);
		System.out.println("들어간 id" + id);
//		b.setBoardType(1);
		int result = bService.insertBoard(b);
		if(result > 0) {
			return "redirect:commList.bo";
		} else {
			throw new BoardException("게시글 작성 실패 ㅠ");
		}
	}
	
	@RequestMapping("updateForm.bo")
	public String updateForm(@RequestParam("bNo") int boardNo, @RequestParam("page") int page, Model model) {
		Board board = bService.selectBoard(boardNo, false);
		model.addAttribute("board", board);
		model.addAttribute("page", page);
		return "editComm";
	}
	
	@PostMapping("updateBoard.bo")
	public String updateCommBoard(@ModelAttribute Board b, @RequestParam("page") int page, RedirectAttributes ra, HttpSession session) {
		
//		b.setBoardType(1);
		int result = bService.updateBoard(b);
//		System.out.println(result);
		
		if(result > 0) {
			ra.addAttribute("bNo", b.getBoardNo());
			ra.addAttribute("writer", ((Member)session.getAttribute("loginUser")).getuId());
			ra.addAttribute("page", page);
			return "redirect:commDetailPage.bo";
			
		} else {
			throw new BoardException("게시글 수정을 실패하였습니다.");
		}
	}
	
	@RequestMapping("Commdelete.bo")
	public String deleteCommBoard(@RequestParam("bId") String encode) {
		
		Decoder decoder = Base64.getDecoder();
		byte[] byteArr = decoder.decode(encode);
		String decode = new String(byteArr);
		int bId = Integer.parseInt(decode);
		
		int result = bService.deleteBoard(bId);
		if(result > 0) {
			return "redirect:commList.bo";
		} else {
			throw new BoardException("게시글 삭제 실패했습니다유ㅠ");
		}
	}
	
	@GetMapping("noticeList.bo")
		public String CommNotice(@RequestParam(value="page", required=false) Integer currentPage, Model model,
								@RequestParam(value="keyword", required=false) String keyword) {
		
		if(currentPage == null) {
			currentPage = 1;
		}
		
		int listCount = bService.getListCount("공지");
		
		PageInfo pageInfo= Pagination.getPageInfo(currentPage, listCount, 10);
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Board> list = bService.selectBoardList(pageInfo, "공지");
		
		
		if(keyword != null) {
			map.put("keyword", keyword);
			map.put("i", "공지");
			listCount = bService.searchListCount(map);
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
			list = bService.searchByTitle(pageInfo, map);
		} else {
			listCount = bService.getListCount("공지");
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
			list = bService.selectBoardList(pageInfo, "공지");
		}
		if(list != null) {
			model.addAttribute("pi", pageInfo);
			model.addAttribute("list", list);
			model.addAttribute("map", map);
			model.addAttribute("page", currentPage);
			return "noticeList";
		} else {
			throw new BoardException("공지 목록 조회 실패");
		}
	}
	
	@PostMapping("insertNoticeBoard.bo")
	public String insertNoticeBoard(@ModelAttribute Board b, HttpSession session) {
		int id = ((Member)session.getAttribute("loginUser")).getuNo();
		System.out.println("id=" + id);
		b.setuNo(id);
		b.setBoardType("공지");
		b.setImageUrl(null);
		System.out.println("들어간 id" + id);
		
		int result = bService.insertBoard(b);
		if(result > 0) {
			return "redirect:noticeListAdmin.bo";
			
		} else {
			throw new BoardException("공지 작성 에러");
		}
	}
	
	@PostMapping("updateNotice.bo")
	public String updateNotice(@ModelAttribute Board b, @RequestParam("page") int page, RedirectAttributes ra, HttpSession session) {
		
		int result = bService.updateBoard(b);
		
		if(result > 0) {
			ra.addAttribute("bNo", b.getBoardNo());
			ra.addAttribute("page", page);
			return "redirect:noticeListAdmin.bo";
			
		} else {
			throw new BoardException("공지 수정 실패");
		}
	}
	
	@GetMapping("writeNotice.bo")
	public String writeNotice() {
		return "writeNotice";
	}
	
	@RequestMapping("updateNoticeForm.bo")
	public String updateNoticeForm(@RequestParam("bNo") int boardNo, @RequestParam("page") int page, Model model) {
		Board board = bService.selectBoard(boardNo, false);
		model.addAttribute("b", board);
		model.addAttribute("page", page);
		return "editNotice";
	}
	
	@RequestMapping("deleteNotice.bo")
	public String deleteNotice(@RequestParam("bId") String encode) {
		
		Decoder decoder = Base64.getDecoder();
		byte[] byteArr = decoder.decode(encode);
		String decode = new String(byteArr);
		int bId = Integer.parseInt(decode);
		
		int result = bService.deleteBoard(bId);
		if(result > 0) {
			return "redirect:noticeListAdmin.bo";
		} else {
			throw new BoardException("공지 삭제 실패");
		}
	}
	
	
	
	@GetMapping("qaList.bo")
	public String qaMain(@RequestParam(value = "page", required = false) Integer currentPage, Model model,
						@RequestParam(value = "keyword", required = false) String keyword) {

		if (currentPage == null) {
			currentPage = 1;
		}

		int listCount = bService.getListCount("QA");

		PageInfo pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Board> list = bService.selectBoardList(pageInfo, "QA");

		if (keyword != null) {
			map.put("keyword", keyword);
			map.put("i", "QA");
			listCount = bService.searchListCount(map);
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
			list = bService.searchByTitle(pageInfo, map);
		} else {
			listCount = bService.getListCount("QA");
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
			list = bService.selectBoardList(pageInfo, "QA");
		}
		if (list != null) {
			model.addAttribute("pi", pageInfo);
			model.addAttribute("list", list);
			model.addAttribute("map", map);
			return "qaList";
		} else {
			throw new BoardException("게시글 목록 조회 실패");
		}
	}
	
	@GetMapping("writeQa.bo")
	public String writeQa() {
		return "writeQa";
	}
	
	@GetMapping("qaDetailPage.bo")
	public String QaDetail(@RequestParam("bNo") int bNo, @RequestParam("page") int page,
							  HttpSession session, Model model) {
		
		Member m = (Member)session.getAttribute("loginUser");
		
		boolean countYN = false;
		if(m == null || m.getIsAdmin() == 1) {
			countYN = true;
		}
		
		Board board = bService.selectBoard(bNo, countYN);
		
		ArrayList<Reply> replyList = bService.selectReply(bNo);
		
		if(board != null) {
			model.addAttribute("board", board);
			model.addAttribute("page", page);
			model.addAttribute("replyList", replyList);
			return "qaDetail";
		} else {
			throw new BoardException("1:1질문 상세 조회 실패");
		}
	}
	
	@PostMapping("insertQaBoard.bo")
	public String insertQaBoard(@ModelAttribute Board b, HttpSession session) {
		int id = ((Member)session.getAttribute("loginUser")).getuNo();
		System.out.println("id=" + id);
		b.setuNo(id);
		b.setBoardType("QA");
		b.setImageUrl(null);
		System.out.println("들어간 id" + id);
		
		int result = bService.insertBoard(b);
		if(result > 0) {
			return "redirect:qaList.bo";
			
		} else {
			throw new BoardException("1:1 질문 작성 실패 ㅠ");
		}
	}
	
	@PostMapping("editQa.bo")
	public String EditQa(@ModelAttribute Board b, @RequestParam("page") int page, RedirectAttributes ra, HttpSession session) {
		
		int result = bService.updateBoard(b);
		
		if(result > 0) {
			ra.addAttribute("bNo", b.getBoardNo());
			ra.addAttribute("writer", ((Member)session.getAttribute("loginUser")).getuId());
			ra.addAttribute("page", page);
			return "redirect:qaDetailPage.bo";
			
		} else {
			throw new BoardException("1:1질문 수정을 실패하였습니다.");
		}
	}
	
	@RequestMapping("updateQaForm.bo")
	public String updateQaForm(@RequestParam("bNo") int boardNo, @RequestParam("page") int page, Model model) {
		Board board = bService.selectBoard(boardNo, false);
		model.addAttribute("b", board);
		model.addAttribute("page", page);
		return "editQa";
	}
	
	//댓글조회
	@ResponseBody
	@GetMapping("/board/{boardNo}/comments")
    public List<Reply> findAllComment(@PathVariable int boardNo) {
		System.out.println(boardNo);
        return bService.findAllComment(boardNo);
    }
	
	//댓글 등록
	@ResponseBody
	@PostMapping("/board/{boardNo}/comments")
    public List<Reply> saveComment(@PathVariable int boardNo, @RequestBody Reply params) {
        int id = bService.saveComment(params);
        System.out.println("머가 넘어오는거니?" + params);
        return bService.findAllComment(boardNo);
    }
	
	// 댓글 삭제
	@ResponseBody
    @DeleteMapping("/board/{boardNo}/comments/{replyNo}")
    public int deleteComment(@PathVariable int replyNo, @PathVariable int boardNo) {
        return bService.deleteComment(replyNo);
    }
	
	
	@RequestMapping("qaDelete.bo")
	public String deleteQaBoard(@RequestParam("bId") String encode) {
		
		Decoder decoder = Base64.getDecoder();
		byte[] byteArr = decoder.decode(encode);
		String decode = new String(byteArr);
		int bId = Integer.parseInt(decode);
		
		int result = bService.deleteBoard(bId);
		if(result > 0) {
			return "redirect:qaList.bo";
		} else {
			throw new BoardException("게시글 삭제 실패했습니다.");
		}
	}
	
	@GetMapping("noticeListAdmin.bo")
	public String CommAdminNotice(@RequestParam(value="page", required=false) Integer currentPage, Model model,
							@RequestParam(value="keyword", required=false) String keyword) {
	
	if(currentPage == null) {
		currentPage = 1;
	}
	
	int listCount = bService.getListCount("공지");
	
	PageInfo pageInfo= Pagination.getPageInfo(currentPage, listCount, 10);
	HashMap<String, Object> map = new HashMap<>();
	ArrayList<Board> list = bService.selectBoardList(pageInfo, "공지");
	
	
	if(keyword != null) {
		map.put("keyword", keyword);
		map.put("i", "공지");
		listCount = bService.searchListCount(map);
		pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
		list = bService.searchByTitle(pageInfo, map);
	} else {
		listCount = bService.getListCount("공지");
		pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
		list = bService.selectBoardList(pageInfo, "공지");
	}
	if(list != null) {
		model.addAttribute("pi", pageInfo);
		model.addAttribute("list", list);
		model.addAttribute("map", map);
		model.addAttribute("page", currentPage);
		return "noticeAdminList";
	} else {
		throw new BoardException("공지 목록 조회 실패");
	}
}
	
	@GetMapping("qaAdminList.bo")
	public String qaAdminMain(@RequestParam(value = "page", required = false) Integer currentPage, Model model,
						@RequestParam(value = "keyword", required = false) String keyword, HttpSession session) {

		if (currentPage == null) {
			currentPage = 1;
		}

		int listCount = bService.getListCount("QA");

		PageInfo pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Board> list = bService.selectBoardList(pageInfo, "QA");
		

		if (keyword != null) {
			map.put("keyword", keyword);
			map.put("i", "QA");
			listCount = bService.searchListCount(map);
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
			list = bService.searchByTitle(pageInfo, map);
		} else {
			listCount = bService.getListCount("QA");
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
			list = bService.selectBoardList(pageInfo, "QA");
		}
		if (list != null) {
			model.addAttribute("pi", pageInfo);
			model.addAttribute("list", list);
			model.addAttribute("map", map);
			return "qaAdmin";
		} else {
			throw new BoardException("게시글 목록 조회 실패");
		}
	}
	
	@GetMapping("commAdminList.bo")
	public String CommAdminMain(@RequestParam(value="page", required=false) Integer currentPage, Model model,
							@RequestParam(value="keyword", required=false) String keyword) {
	
	if(currentPage == null) {
		currentPage = 1;
	}
	
	int listCount = bService.getListCount("일반");
	
	PageInfo pageInfo= Pagination.getPageInfo(currentPage, listCount, 10);
	HashMap<String, Object> map = new HashMap<>();
	ArrayList<Board> list = bService.selectBoardList(pageInfo, "일반");
	
	if(keyword != null) {
		map.put("keyword", keyword);
		map.put("i", "일반");
		listCount = bService.searchListCount(map);
		pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
		list = bService.searchByTitle(pageInfo, map);
	} else {
		listCount = bService.getListCount("일반");
		pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
		list = bService.selectBoardList(pageInfo, "일반");
	}
	if(list != null) {
		model.addAttribute("pi", pageInfo);
		model.addAttribute("list", list);
		model.addAttribute("map", map);
		return "commAdmin";
	} else {
		throw new BoardException("게시글 목록 조회 실패");
	}
}
	
}
