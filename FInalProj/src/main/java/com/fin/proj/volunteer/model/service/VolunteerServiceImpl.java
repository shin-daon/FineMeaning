package com.fin.proj.volunteer.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fin.proj.volunteer.model.dao.VolunteerDAO;
import com.fin.proj.volunteer.model.vo.Volunteer;

@Service
public class VolunteerServiceImpl implements VolunteerService {
	
	@Autowired
	private VolunteerDAO vDAO;

	@Override
	public int insertVolunteer(Volunteer v) {
		return vDAO.insertVolunteer();
	}
}
