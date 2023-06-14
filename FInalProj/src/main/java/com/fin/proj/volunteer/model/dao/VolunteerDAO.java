package com.fin.proj.volunteer.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.fin.proj.volunteer.model.vo.Volunteer;

@Mapper
public interface VolunteerDAO {

	int insertVolunteer(Volunteer v);

	int getVolunteerCount();

//	ArrayList<Volunteer> selectVolunteerList(RowBounds rowBounds);

	ArrayList<Volunteer> selectVolunteerList(int i, RowBounds rowBounds);

}
