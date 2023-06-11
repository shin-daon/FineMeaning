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
}
