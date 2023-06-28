package com.fin.proj.auth.oauth.info;

public interface OAuthUserInfo {
    Long getId();
    String getEmail();
    String getName();
    String getNickname();
    String getImageUrl();
}
