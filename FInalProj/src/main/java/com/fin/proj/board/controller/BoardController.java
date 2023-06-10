package com.fin.proj.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {

	@GetMapping("faqMain.bo")
	public String faqMain() {
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
}
