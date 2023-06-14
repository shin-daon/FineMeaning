package com.fin.proj.volunteer.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.fin.proj.volunteer.model.vo.Volunteer;

@Mapper
public interface VolunteerDAO {

	int insertVolunteer(Volunteer v);

}
