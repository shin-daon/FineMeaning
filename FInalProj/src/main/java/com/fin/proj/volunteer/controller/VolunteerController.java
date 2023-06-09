package com.fin.proj.volunteer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.fin.proj.volunteer.model.service.VolunteerService;

@Controller
public class VolunteerController {
	
	@Autowired
	private VolunteerService vService;
}
