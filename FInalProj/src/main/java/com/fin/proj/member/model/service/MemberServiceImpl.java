package com.fin.proj.member.model.service;

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

}
