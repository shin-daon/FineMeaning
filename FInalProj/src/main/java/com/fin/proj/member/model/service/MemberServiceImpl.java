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
	public Member login(String uId, String uPwd) {
		return mDAO.login(uId, uPwd);
	}

}
