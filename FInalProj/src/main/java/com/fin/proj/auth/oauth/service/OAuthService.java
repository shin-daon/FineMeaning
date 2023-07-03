package com.fin.proj.auth.oauth.service;

import com.fin.proj.auth.oauth.info.OAuthUserInfo;
import com.fin.proj.member.model.vo.Member;

public interface OAuthService {
    Member login(String code);
    String getAccessToken(String code);
    OAuthUserInfo getOAuthUserInfo(String accessToken);
}
