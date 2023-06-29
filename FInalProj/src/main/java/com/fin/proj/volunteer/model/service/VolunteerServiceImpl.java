package com.fin.proj.volunteer.model.service;

import java.util.ArrayList;
import java.util.HashMap;

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
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return vDAO.selectVolunteerList(null, rowBounds);
	}

	@Override
	public Volunteer selectVolunteer(int vNo) {
		return vDAO.selectVolunteer(vNo);
	}

	@Override
	public int updateVolunteer(Volunteer v) {
		return vDAO.updateVolunteer(v);
	}

	@Override
	public int deleteVolunteer(String vNo) {
		return vDAO.deleteVolunteer(vNo);
	}

	@Override
	public int applyVolunteer(Volunteer v) {
		return vDAO.applyVolunteer(v);
	}

	@Override
	public int checkVolunteerApply(HashMap<String, Integer> map) {
		return vDAO.checkVolunteerApply(map);
	}

	@Override
	public int getSearchVolunteerCount(HashMap<String, String> map) {
		return vDAO.searchVolunteerCount(map);
	}

	@Override
	public ArrayList<Volunteer> searchVolunteer(PageInfo pi, HashMap<String, String> map) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return vDAO.searchVolunteer(map, rowBounds);
	}

	@Override
	public ArrayList<Volunteer> searchVolunteerByAjax(PageInfo pi, HashMap<String, String> ajaxMap) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return vDAO.searchVolunteer(ajaxMap, rowBounds);
	}

	@Override
	public int getVolunteerEnrollHistoryCount(Integer uNo) {
		return vDAO.getVolunteerEnrollHistoryCount(uNo);
	}

	@Override
	public ArrayList<Volunteer> selectVolunteerEnrollHistory(PageInfo pi, Integer uNo) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return vDAO.getSelectVolunteerEnrollHistory(uNo, rowBounds);
	}

	@Override
	public int getSearchVolunteerHistoryCount(HashMap<String, Object> searchEnrollHisMap) {
		return vDAO.getSearchVolunteerHistoryCount(searchEnrollHisMap);
	}

	@Override
	public ArrayList<Volunteer> selectSearchVolunteerEnrollHistory(PageInfo pi, HashMap<String, Object> searchEnrollHisMap) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return vDAO.selectSearchVolunteerEnrollHistory(searchEnrollHisMap, rowBounds);
	}

	@Override
	public int updateVolunteerStatus(HashMap<String, Object> updateStatusMap) {
		return vDAO.updateVolunteerStatus(updateStatusMap);
	}

	@Override
	public int getMyVolunteerHistoryCount(int uNo) {
		return vDAO.getMyVolunteerHistoryCount(uNo);
	}

	@Override
	public ArrayList<Volunteer> selectMyVolunteerHistory(PageInfo pi, int uNo) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return vDAO.selectMyVolunteerHistory(uNo, rowBounds);
	}

}
