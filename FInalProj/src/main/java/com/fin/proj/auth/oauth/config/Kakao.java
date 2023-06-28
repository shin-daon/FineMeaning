package com.fin.proj.auth.oauth.config;

public class Kakao {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String tokenUri;
    private String authorizationUri;
    private String userInfoUri;
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	public String getTokenUri() {
		return tokenUri;
	}
	public void setTokenUri(String tokenUri) {
		this.tokenUri = tokenUri;
	}
	public String getAuthorizationUri() {
		return authorizationUri;
	}
	public void setAuthorizationUri(String authorizationUri) {
		this.authorizationUri = authorizationUri;
	}
	public String getUserInfoUri() {
		return userInfoUri;
	}
	public void setUserInfoUri(String userInfoUri) {
		this.userInfoUri = userInfoUri;
	}
    
    
}
