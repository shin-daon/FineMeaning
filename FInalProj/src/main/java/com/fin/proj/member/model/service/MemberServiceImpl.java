package com.fin.proj.member.model.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fin.proj.common.model.vo.PageInfo;
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
	
	@Override
	public int loginFailCount(String uId) {
		return mDAO.loginFailCount(uId);
	}
	
	@Override
	public Member loginFailDate(Timestamp timestamp) {
		return mDAO.loginFailDate(timestamp);
	}
	
	@Override
	public int getListCount() {
		return mDAO.getListCount();
	}
	
	@Override
	public ArrayList<Member> selectUserList(PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());

		return mDAO.selectUserList(rowBounds);
	}
	
	@Override
	public int getUserListCount(int uNo) {
		return mDAO.getUserListCount(uNo);
	}
	
	@Override
	public Member selectUserListEach(PageInfo pi, int uNo) {
		return mDAO.selectUserListEach(uNo);
	}
	
	@Override
	public ArrayList<Member> statusUserList() {
		return mDAO.statusUserList();
	}
	
	@Override
	public int getCategoryCount(HashMap<String, String> map) {
		return mDAO.getCategoryCount(map);
	}
	
	@Override
	public ArrayList<Member> selectCategoryListAdmin(PageInfo pi, HashMap<String, String> map) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		return mDAO.selectCategoryListAdmin(rowBounds, map);
	}
	
	@Override
	public Member kakaoLogin(Member m) {
		return mDAO.kakaoLogin(m);
	}
	
	@Override
	public int kakaoEnroll(Member m) {
		return mDAO.kakaoEnroll(m);
	}

	@Override
	public int checkEmail(String emailAddress) {
		return mDAO.checkEmail(emailAddress);
	}
	
	@Override
	public int getSearchListCount(String searchWord) {
		return mDAO.getSearchListCount(searchWord);
	}
	
	@Override
	public ArrayList<Member> selectSearchListAdmin(PageInfo pi, String searchWord) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		return mDAO.selectSearchListAdmin(rowBounds, searchWord);
	}
}
