package com.fin.proj.user.model.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fin.proj.common.model.vo.PageInfo;
import com.fin.proj.user.model.dao.UserDAO;
import com.fin.proj.user.model.vo.User;
import com.fin.proj.user.security.UserRepository;

@Service
public class UserServiceImpl implements UserService, UserRepository {
	
	@Autowired
	private UserDAO uDAO;
	
	@Override
	public User login(User u) {
		return uDAO.login(u);
	}
	
	@Override
	public int insertUser(User u) {
		return uDAO.insertUser(u);
	}
	
	@Override
	public int checkId(String uId) {
		return uDAO.checkId(uId);
	}
	
	@Override
	public int checkNickName(String uNickName) {
		return uDAO.checkNickName(uNickName);
	}
	
	@Override
	public int updateMyInfo(User u) {
		return uDAO.updateMyInfo(u);
	}
	
	@Override
	public int deleteUser(String uId) {
		return uDAO.deleteUser(uId);
	}
	
	@Override
	public int checkNickNameModify(User u) {
		return uDAO.checkNickNameModify(u);
	}
	
	@Override
	public int updatePwd(HashMap<String, String> map) {
		return uDAO.updatePwd(map);
	}
	
	@Override
	public User selectPwd(String uId) {
		return uDAO.selectPwd(uId);
	}
	
	@Override
	public int searchEmailUser(HashMap<String, String> map) {
		return uDAO.searchEmailUser(map);
	}
	
	@Override
	public ArrayList<User> searchUser(User u) {
		return uDAO.searchUser(u);
	}
	
	@Override
	public int searchEmailUser2(HashMap<String, String> map) {
		return uDAO.searchEmailUser2(map);
	}
	
	@Override
	public User searchUserPwd(User u) {
		return uDAO.searchUserPwd(u);
	}
	
	@Override
	public int searchPhoneUser(HashMap<String, String> map) {
		return uDAO.searchPhoneUser(map);
	}
	
	@Override
	public int searchPhoneUser2(HashMap<String, String> map) {
		return uDAO.searchPhoneUser2(map);
	}
	
	@Override
	public int loginFailCount(String uId) {
		return uDAO.loginFailCount(uId);
	}
	
	@Override
	public User loginFailDate(Timestamp timestamp) {
		return uDAO.loginFailDate(timestamp);
	}
	
	@Override
	public int getListCount() {
		return uDAO.getListCount();
	}
	
	@Override
	public ArrayList<User> selectUserList(PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());

		return uDAO.selectUserList(rowBounds);
	}
	
	@Override
	public int getUserListCount(int uNo) {
		return uDAO.getUserListCount(uNo);
	}
	
	@Override
	public User selectUserListEach(PageInfo pi, int uNo) {
		return uDAO.selectUserListEach(uNo);
	}
	
	@Override
	public ArrayList<User> statusUserList() {
		return uDAO.statusUserList();
	}
	
	@Override
	public int getCategoryCount(HashMap<String, String> map) {
		return uDAO.getCategoryCount(map);
	}
	
	@Override
	public ArrayList<User> selectCategoryListAdmin(PageInfo pi, HashMap<String, String> map) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		return uDAO.selectCategoryListAdmin(rowBounds, map);
	}
	
	@Override
	public User kakaoLogin(User u) {
		return uDAO.kakaoLogin(u);
	}
	
	@Override
	public int kakaoEnroll(User u) {
		return uDAO.kakaoEnroll(u);
	}

	@Override
	public int checkEmail(String emailAddress) {
		return uDAO.checkEmail(emailAddress);
	}
	
	@Override
	public int getSearchListCount(String searchWord) {
		return uDAO.getSearchListCount(searchWord);
	}
	
	@Override
	public ArrayList<User> selectSearchListAdmin(PageInfo pi, String searchWord) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		return uDAO.selectSearchListAdmin(rowBounds, searchWord);
	}
	
	@Override
	public int updateUserInfo(Properties prop) {
		return uDAO.updateUserInfo(prop);
	}
	
	@Override
	public int updateState(Properties prop) {
		return uDAO.updateState(prop);
	}
	
	@Override
	public int checkLogin(String uId) {
		return uDAO.checkLogin(uId);
	}

	@Override
	public List<User> findUser(String username) {
		return uDAO.findUser(username);
	}
}
