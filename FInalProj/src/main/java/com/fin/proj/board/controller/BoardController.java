package com.fin.proj.board.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fin.proj.board.exception.BoardException;
import com.fin.proj.board.model.service.BoardService;
import com.fin.proj.board.model.vo.Board;
import com.fin.proj.board.model.vo.Reply;
import com.fin.proj.common.Pagination;
import com.fin.proj.common.model.vo.PageInfo;
import com.fin.proj.member.model.vo.Member;

import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService bService;

	@GetMapping("faqMain.bo")
	public String faqMain(@RequestParam(value="page", required=false) Integer currentPage, Model model) {
		
		if(currentPage == null) {
			currentPage = 1;
		}
		
		int listCount = bService.getListCount(6);
//		System.out.println(listCount);
		
		PageInfo pageInfo= Pagination.getPageInfo(currentPage, listCount, 10);
		
		ArrayList<Board> list = bService.selectBoardList(pageInfo, 6);
//		System.out.println(list);
		
		if(list != null) {
			model.addAttribute("pi", pageInfo);
			model.addAttribute("list", list);
			return "faq";
		} else {
			throw new BoardException("게시글 목록 조회 실패");
		}
	}
	
	@GetMapping("faq_detail.bo")
	public String faqDetail(@RequestParam("bNo") int bNo, @RequestParam("writer") String writer,
							@RequestParam("page") int page, HttpSession session, Model model) {
//		System.out.println(bNo + ", " + writer + ", "+ page);
		// 상세보기의 경우 조회수 +1, but 내 글 클릭 시 조회수 +0 (로그인 유저 정보 가져와서 비교)
		// 파라미터로 받은 page를 통해 목록으로 돌아갔을 시 원래 보던 페이지 노출
		
		Member m = (Member)session.getAttribute("loginUser");
		System.out.println(m);
		
		String readerNickName = null;
		if(m != null) {
			readerNickName = m.getuNickName();
		}
		
		boolean countYN = false;
		if(!writer.equals(readerNickName)) {
			countYN = true;
		}
		
		Board board = bService.selectBoard(bNo, countYN);
		System.out.println(board);
		
		ArrayList<Reply> replyList = bService.selectReply(bNo);
		System.out.println(replyList);
		
		if(board != null) {
			model.addAttribute("board", board);
			model.addAttribute("page", page);
//			model.addAttribute("replyList", replyList);
			return "faq_Detail";
		} else {
			throw new BoardException("게시글 상세 조회 실패");
		}
	}
	
	@GetMapping("faq_form.bo")
	public String faqForm() {
		return "faq_form";
	}
	
	
	@GetMapping("finePeopleMain.bo")
	public String finePeopleMain() {
		return "finePeople";
	}
	
	@GetMapping("finePeople_form.bo")
	public String finePeopleForm() {
		return "finePeople_form";
	}
	
	@GetMapping("fruitMain.bo")
	public String fruitMain() {
		return "fruit";
	}
	
	@GetMapping("fruit_form.bo")
	public String fruitForm() {
		return "fruit_form";
	}
	
	@GetMapping("fruit_detail.bo")
	public String fruitDetail() {
		return "fruit_detail";
	}
	
	@GetMapping("fineNewsMain.bo")
	public String fineNewsMain() {
		return "fineNews";
	}
	
	@GetMapping("fineNews_form.bo")
	public String fineNewsForm() {
		return "fineNews_form";
	}
	
	@GetMapping("commList.bo")
		public String CommMain(@RequestParam(value="page", required=false) Integer currentPage, Model model) {
		
		if(currentPage == null) {
			currentPage = 1;
		}
		
		int listCount = bService.getListCount(1);
		
		PageInfo pageInfo= Pagination.getPageInfo(currentPage, listCount, 10);
		
		ArrayList<Board> list = bService.selectBoardList(pageInfo, 1);
		
		if(list != null) {
			model.addAttribute("pi", pageInfo);
			model.addAttribute("list", list);
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
	public String CommDetail(@RequestParam("bNo") int bNo, @RequestParam("writer") String writer,
							@RequestParam("page") int page, HttpSession session, Model model) {
		Member m = (Member)session.getAttribute("loginUser");
		
		String readerNickName = null;
		if(m != null) {
			readerNickName = m.getuNickName();
		}
		
		boolean countYN = false;
		if(!writer.equals(readerNickName)) {
			countYN = true;
		}
		
		Board board = bService.selectBoard(bNo, countYN);
		
		ArrayList<Reply> replyList = bService.selectReply(bNo);
		System.out.println(replyList);
		
		if(board != null) {
			model.addAttribute("board", board);
			model.addAttribute("page", page);
//			model.addAttribute("replyList", replyList);
			return "commDetail";
		} else {
			throw new BoardException("게시글 상세 조회 실패");
		}
	}
	
	@PostMapping("insertBoard.bo")
	public String insertCommBoard(@ModelAttribute Board b, HttpSession session) {
		String id = ((Member)session.getAttribute("loginUser")).getuId();
		b.setuId(id);
		b.setBoardType(1);
		
		int result = bService.insertBoard(b);
		if(result > 0) {
			return "redirect:commList.bo";
			
		} else {
			throw new BoardException("게시글 작성 실패 ㅠ");
		}
	}
	
	@GetMapping("noticeList.bo")
	public String noticeList() {
		return "noticeList";
	}
	
	@GetMapping("writeNotice.bo")
	public String writeNotice() {
		return "writeNotice";
	}
	
	@GetMapping("qaList.bo")
	public String qaList() {
		return "qaList";
	}
	
	@GetMapping("writeQa.bo")
	public String writeQa() {
		return "writeQa";
	}
	
	@GetMapping("commDetail.bo")
	public String commDetail() {
		return "commDetail";
	}
	
	@GetMapping("qaDetail.bo")
	public String qaDetail() {
		return "qaDetail";
	}
	
	@GetMapping("replyQa.bo")
	public String replyQa() {
		return "replyQa";
	}
	
	@GetMapping("editComm.bo")
	public String editComm() {
		return "editComm";
	}
	
	@GetMapping("editQa.bo")
	public String editQa() {
		return "editQa";
	}
	
	@GetMapping("editNotice.bo")
	public String editNotice() {
		return "editNotice";
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
	
}
