package com.fin.proj.member.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.fin.proj.member.model.vo.Member;

@Mapper
public interface MemberDAO {

	Member login(String uId, String uPwd);



}
