package com.fin.proj.auth.oauth.service;

import com.fin.proj.auth.oauth.info.OAuthUserInfo;
import com.fin.proj.user.model.vo.User;

public interface OAuthService {
    User login(String code);
    String getAccessToken(String code);
    OAuthUserInfo getOAuthUserInfo(String accessToken);
}
