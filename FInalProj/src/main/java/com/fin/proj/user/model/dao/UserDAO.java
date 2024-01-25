package com.fin.proj.user.model.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.fin.proj.user.model.vo.User;

@Mapper
public interface UserDAO {

	User login(User u);

	int insertUser(User u);

	int checkId(String uId);

	int checkNickName(String uNickName);

	int updateMyInfo(User u);

	int deleteUser(String uId);

	int checkNickNameModify(User u);

	int updatePwd(HashMap<String, String> map);

	User selectPwd(String uId);

	int searchEmailUser(HashMap<String, String> map);

	ArrayList<User> searchUser(User u);

	int searchEmailUser2(HashMap<String, String> map);

	User searchUserPwd(User u);

	int searchPhoneUser(HashMap<String, String> map);

	int searchPhoneUser2(HashMap<String, String> map);

	int loginFailCount(String uId);

	User loginFailDate(Timestamp timestamp);

	int getListCount();

	ArrayList<User> selectUserList(RowBounds rowBounds);

	int getUserListCount(int uNo);

	User selectUserListEach(int uNo);

	ArrayList<User> statusUserList();

	int getCategoryCount(HashMap<String, String> map);

	ArrayList<User> selectCategoryListAdmin(RowBounds rowBounds, HashMap<String, String> map);

	User kakaoLogin(User u);

	int kakaoEnroll(User u);

	int checkEmail(String emailAddress);

	int getSearchListCount(String searchWord);

	ArrayList<User> selectSearchListAdmin(RowBounds rowBounds, String searchWord);

	int updateUserInfo(Properties prop);

	int updateState(Properties prop);

	int checkLogin(String uId);
}
