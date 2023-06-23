package com.fin.proj.member.model.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fin.proj.member.model.dao.MemberDAO;
import com.fin.proj.member.model.vo.Member;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberDAO mDAO;
	
	@Override
	public Member login(Member m) {
		return mDAO.login(m);
	}
	
	@Override
	public int insertUser(Member m) {
		return mDAO.insertUser(m);
	}
	
	@Override
	public int checkId(String uId) {
		return mDAO.checkId(uId);
	}
	
	@Override
	public int checkNickName(String uNickName) {
		return mDAO.checkNickName(uNickName);
	}
	
	@Override
	public int updateMyInfo(Member m) {
		return mDAO.updateMyInfo(m);
	}
	
	@Override
	public int deleteUser(String uId) {
		return mDAO.deleteUser(uId);
	}
	
	@Override
	public int checkNickNameModify(Member m) {
		return mDAO.checkNickNameModify(m);
	}
	
	@Override
	public int updatePwd(HashMap<String, String> map) {
		return mDAO.updatePwd(map);
	}
	
	@Override
	public String selectPwd(String uId) {
		return mDAO.selectPwd(uId);
	}
	
	@Override
	public int searchEmailUser(HashMap<String, String> map) {
		return mDAO.searchEmailUser(map);
	}
	
	@Override
	public Member searchUser(Member m) {
		return mDAO.searchUser(m);
	}
	
	@Override
	public int searchEmailUser2(HashMap<String, String> map) {
		return mDAO.searchEmailUser2(map);
	}
	
	@Override
	public Member searchUserPwd(Member m) {
		return mDAO.searchUserPwd(m);
	}
	
	@Override
	public int searchPhoneUser(HashMap<String, String> map) {
		return mDAO.searchPhoneUser(map);
	}
	
	@Override
	public int searchPhoneUser2(HashMap<String, String> map) {
		return mDAO.searchPhoneUser2(map);
	}


}
