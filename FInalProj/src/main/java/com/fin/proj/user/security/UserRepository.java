package com.fin.proj.user.security;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.fin.proj.user.model.vo.User;

@Repository
public interface UserRepository {

	 List<User> findUser(String username);

}
