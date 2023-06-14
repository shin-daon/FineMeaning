package com.fin.proj.volunteer.model.service;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fin.proj.common.model.vo.PageInfo;
import com.fin.proj.volunteer.model.dao.VolunteerDAO;
import com.fin.proj.volunteer.model.vo.Volunteer;

@Service
public class VolunteerServiceImpl implements VolunteerService {
	
	@Autowired
	private VolunteerDAO vDAO;

	@Override
	public int insertVolunteer(Volunteer v) {
		return vDAO.insertVolunteer(v);
	}
	
	@Override
	public int getVolunteerCount() {
		return vDAO.getVolunteerCount();
	}

	@Override
	public ArrayList<Volunteer> selectVolunteerList(PageInfo pi) {
		int offset = (pi.getListCount() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return vDAO.selectVolunteerList(rowBounds);
	}
}
