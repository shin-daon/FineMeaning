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

	String selectPwd(String uId);

	int searchEmailUser(HashMap<String, String> map);

	Member searchUser(Member m);

	int searchEmailUser2(HashMap<String, String> map);

	Member searchUserPwd(Member m);

	int searchPhoneUser(HashMap<String, String> map);

	int searchPhoneUser2(HashMap<String, String> map);

}
