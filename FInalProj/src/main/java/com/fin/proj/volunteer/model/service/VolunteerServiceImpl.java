package com.fin.proj.volunteer.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fin.proj.volunteer.model.dao.VolunteerDAO;

@Service
public class VolunteerServiceImpl implements VolunteerService {
	
	@Autowired
	private VolunteerDAO vDAO;
}
