package com.fin.proj.board.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
						  @RequestParam(value="keyword", required=false) String keyword) {
		
//		System.out.println(keyword);
		// 키워드 매개변수로 받아서 전체적인 흐름은 fruitMain.bo와 비슷한 맥락으로 진행!
		
		HashMap<String, Object> map = new HashMap<>();
		ArrayList<Board> list;
		PageInfo pageInfo;
		int listCount;
		
		if(currentPage == null) {
			currentPage = 1;
		}
		
		if(keyword != null) {
			map.put("keyword", keyword);
			map.put("i", "자주 묻는 질문");
			listCount = bService.searchListCount(map);
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
			list = bService.searchByTitle(pageInfo, map);
			System.out.println(list);
		} else {
			listCount = bService.getListCount("자주 묻는 질문");
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
			list = bService.selectBoardList(pageInfo, "자주 묻는 질문");
//			System.out.println(list);
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
							@RequestParam("page") int page, HttpSession session, Model model) {
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
//			model.addAttribute("replyList", replyList);
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
		b.setImageUrl(null);
		b.setNewsURL(null);
		b.setFpName(null);
		
		int result = bService.insertBoard(b);
		
		if(result > 0) {
			return "redirect:faqMain.bo";
		} else {
			throw new BoardException("게시글 작성 실패");
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
			return "redirect:faq_detail.bo";
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
		
		int boardResult = bService.deleteBoard(boardNo);
		
		if(boardResult > 0) {
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
			return "redirect:finePeopleMain.bo";
		} else {
			throw new BoardException("게시물 작성 실패");
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
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
			list = bService.searchByTitleAndCategory(pageInfo, params);
		} else { // 페이지 로드 시 메인 페이지
			listCount = bService.getListCount("결실");
			pageInfo = Pagination.getPageInfo(currentPage, listCount, 10);
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
							  HttpSession session, Model model) {
		
		Member m = (Member)session.getAttribute("loginUser");
//		System.out.println(m);
		
		boolean countYN = false;
		if(m == null || m.getIsAdmin() == 1) {
			countYN = true;
		}
		
		Board board = bService.selectBoard(bNo, countYN);
//		System.out.println(board);
		
		ArrayList<Reply> replyList = bService.selectReply(bNo);
//		System.out.println(replyList);
		
		if(board != null) {
			model.addAttribute("board", board);
			model.addAttribute("page", page);
			model.addAttribute("replyList", replyList);
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
		
		int result = bService.insertFruit(b);
		
		if(result > 0) {
			return "redirect:fruitMain.bo";
		} else {
			throw new BoardException("게시글 작성 실패");
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
			
			return "redirect:fruit_detail.bo";
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
		
		int boardResult = bService.deleteBoard(boardNo);
		if(boardResult > 0) {
			return "redirect:fruitMain.bo";
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
	
	// 댓글
	@RequestMapping("insertReply.bo")
	public void insertReply(@ModelAttribute Reply r, HttpServletResponse response) {
		
		System.out.println(r);
		bService.insertReply(r);
		
		ArrayList<Reply> list = bService.selectReply(r.getBoardNo());
		response.setContentType("application/json; charset=UTF-8");
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd / HH:mm:ss").create();
		try {
			gson.toJson(list, response.getWriter());
		} catch (JsonIOException | IOException e) {
			e.printStackTrace();
		} 
	}
	
	@RequestMapping("deleteReply.bo")
	public String deleteReply(@RequestParam("rNo") String replyNo,
							  @RequestParam("bNo") int boardNo,
							  @RequestParam("page") int page,
							  RedirectAttributes ra) {
		
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
			return "redirect:fruit_detail.bo";
		} else {
			throw new BoardException("댓글 삭제에 실패하였습니다.");
		}
		
		// replyNo 이용해 해당 댓글 삭제한 후,
		// boardNo 받아와서 selectReply 한 후 해당 디테일 페이지 띄워주기
		// RedirectAttribute 이용!
	}
	
	// my page
	@GetMapping("myBoard.bo")
	public String myBoard() {
		return "myBoard";
	}
	
	@GetMapping("myReply.bo")
	public String myReply() {
		return "myReply";
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
							  HttpSession session, Model model) {
		
		Member m = (Member)session.getAttribute("loginUser");
//		System.out.println(m);
		
		boolean countYN = false;
		if(m == null || m.getIsAdmin() == 1) {
			countYN = true;
		}
		
		Board board = bService.selectBoard(bNo, countYN);
//		System.out.println(board);
		
		ArrayList<Reply> replyList = bService.selectReply(bNo);
//		System.out.println(replyList);
		
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
	
	@RequestMapping("deleteCommReply.bo")
	public String deleteCommReply(@RequestParam("rNo") String replyNo,
							  @RequestParam("bNo") int boardNo,
							  @RequestParam("page") int page,
							  RedirectAttributes ra) {
		
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
			return "redirect:commDetailPage.bo";
		} else {
			throw new BoardException("댓글 삭제에 실패하였습니다.");
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
			return "redirect:noticeList.bo";
			
		} else {
			throw new BoardException("공지 작성 에러");
		}
	}
	
	@PostMapping("updateNotice.bo")
	public String updateNotice(@ModelAttribute Board b, @RequestParam("page") int page, RedirectAttributes ra, HttpSession session) {
		
//		b.setBoardType(1);
		int result = bService.updateBoard(b);
//		System.out.println(result);
		
		if(result > 0) {
			ra.addAttribute("bNo", b.getBoardNo());
			ra.addAttribute("page", page);
			return "redirect:noticeList.bo";
			
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
			return "redirect:noticeList.bo";
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
//		System.out.println(m);
		
		boolean countYN = false;
		if(m == null || m.getIsAdmin() == 1) {
			countYN = true;
		}
		
		Board board = bService.selectBoard(bNo, countYN);
//		System.out.println(board);
		
		ArrayList<Reply> replyList = bService.selectReply(bNo);
//		System.out.println(replyList);
		
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
//		b.setBoardType(1);
		
		int result = bService.insertBoard(b);
		if(result > 0) {
			return "redirect:qaList.bo";
			
		} else {
			throw new BoardException("1:1 질문 작성 실패 ㅠ");
		}
	}
	
	@PostMapping("editQa.bo")
	public String EditQa(@ModelAttribute Board b, @RequestParam("page") int page, RedirectAttributes ra, HttpSession session) {
		
//		b.setBoardType(1);
		int result = bService.updateBoard(b);
//		System.out.println(result);
		
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
	
	@GetMapping("replyQa.bo")
	public String replyQa() {
		return "replyQa";
	}
	
	@GetMapping("editQa.bo")
	public String editQa() {
		return "editQa";
	}
	
}
