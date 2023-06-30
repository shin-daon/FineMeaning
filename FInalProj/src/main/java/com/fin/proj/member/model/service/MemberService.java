package com.fin.proj.member.model.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import com.fin.proj.common.model.vo.PageInfo;
import com.fin.proj.member.model.vo.Member;

public interface MemberService {

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

	int loginFailCount(String uId);

	Member loginFailDate(Timestamp timestamp);

	int getListCount();

	ArrayList<Member> selectUserList(PageInfo pi);

	int getUserListCount(int uNo);

	Member selectUserListEach(PageInfo pi, int uNo);

	ArrayList<Member> statusUserList();

	int getCategoryCount(HashMap<String, String> map);

	ArrayList<Member> selectCategoryListAdmin(PageInfo pi, HashMap<String, String> map);

	Member kakaoLogin(Member m);

	int kakaoEnroll(Member m);

	int checkEmail(String emailAddress);
	

}
