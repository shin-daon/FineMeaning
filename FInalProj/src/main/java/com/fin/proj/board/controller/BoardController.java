package com.fin.proj.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fin.proj.board.model.service.BoardService;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService bService;

	@GetMapping("faqMain.bo")
	public String faqMain(@RequestParam(value="page", required=false) Integer currentPage, Model model) {
		
		return "faq";
	}
	
	@GetMapping("faq_form.bo")
	public String faqForm() {
		return "faq_form";
	}
	
	@GetMapping("faq_detail.bo")
	public String faqDetail() {
		return "faq_detail";
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
	public String commList() {
		return "commList";
	}
	
	@GetMapping("writeComm.bo")
	public String writeComm() {
		return "writeComm";
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
