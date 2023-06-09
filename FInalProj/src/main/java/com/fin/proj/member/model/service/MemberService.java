package com.fin.proj.member.model.service;

import com.fin.proj.member.model.vo.Member;

public interface MemberService {

	Member login(String uId, String uPwd);

}
