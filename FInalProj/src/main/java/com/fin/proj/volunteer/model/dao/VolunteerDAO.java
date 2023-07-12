package com.fin.proj.volunteer.model.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.fin.proj.volunteer.model.vo.Volunteer;

@Mapper
public interface VolunteerDAO {

	int insertVolunteer(Volunteer v);

	int getVolunteerCount();

	ArrayList<Volunteer> selectVolunteerList(Integer i, RowBounds rowBounds);

	Volunteer selectVolunteer(int vNo);

	int updateVolunteer(Volunteer v);

	int deleteVolunteer(String vNo);

	int applyVolunteer(Volunteer v);

	int checkVolunteerApply(HashMap<String, Integer> map);

	int searchVolunteerCount(HashMap<String, String> map);

	ArrayList<Volunteer> searchVolunteer(HashMap<String, String> map, RowBounds rowBounds);

	ArrayList<Volunteer> searchVolunteerByAjax(HashMap<String, String> ajaxMap, RowBounds rowBounds);

	int getVolunteerEnrollHistoryCount(Integer uNo);

	ArrayList<Volunteer> getSelectVolunteerEnrollHistory(Integer uNo, RowBounds rowBounds);

	int getSearchVolunteerHistoryCount(HashMap<String, Object> searchEnrollHisMap);

	ArrayList<Volunteer> selectSearchVolunteerEnrollHistory(HashMap<String, Object> searchEnrollHisMap, RowBounds rowBounds);

	int updateVolunteerStatus(HashMap<String, Object> updateStatusMap);

	int getMyVolunteerHistoryCount(int uNo);

	ArrayList<Volunteer> selectMyVolunteerHistory(int uNo, RowBounds rowBounds);

	int getSearchMyVolunteerHistoryCount(HashMap<String, Object> myHistorySearchMap);

	ArrayList<Volunteer> selectSearchMyVolunteerHistory(HashMap<String, Object> myHistorySearchMap, RowBounds rowBounds);

	ArrayList<Volunteer> selectVolunteerApplyList(Object vNo);
	
	int getAdminVolunteerApplyCount(Integer vNo);
	
	ArrayList<Volunteer> selectAdminVolunteerApplyList(Integer vNo, RowBounds rowBounds);

	int updateVolunteerHistoryStatus(HashMap<String, Object> updateStatusMap);

	ArrayList<Integer> selectVolunteerApplicantCount(Integer uNo, RowBounds rowBounds);

	ArrayList<Integer> selectSearchVolunteerApplicantCount(HashMap<String, Object> searchEnrollHisMap, RowBounds rowBounds);
}
