package com.fin.proj.member.model.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.fin.proj.member.model.vo.Member;

@Mapper
public interface MemberDAO {

	Member login(Member m);

	int insertUser(Member m);

	int checkId(String uId);

	int checkNickName(String uNickName);

	int updateMyInfo(Member m);

	int deleteUser(String uId);

	int checkNickNameModify(Member m);

	int updatePwd(HashMap<String, String> map);

}
