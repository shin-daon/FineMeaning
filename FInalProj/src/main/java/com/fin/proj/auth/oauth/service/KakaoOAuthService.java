package com.fin.proj.auth.oauth.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fin.proj.auth.oauth.info.KakaoUserInfo;
import com.fin.proj.auth.oauth.info.OAuthUserInfo;
import com.fin.proj.member.model.vo.Member;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@Component
public class KakaoOAuthService implements OAuthService {

    @Value("${oauth.kakao.client-id}")
    private String kakaoClientId;

    @Value("${oauth.kakao.client-secret}")
    private String kakaoClientSecret;

    @Value("${oauth.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    @Value("${oauth.kakao.token-uri}")
    private String kakaoTokenUri;

    @Value("${oauth.kakao.authorization-uri}")
    private String kakaoAuthorizationUri;

    @Value(("${oauth.kakao.user-info-uri}"))
    private String kakaoUserInfoUri;

    @Override
    public Member login(String code) {
        String accessToken = this.getAccessToken(code);
        OAuthUserInfo kakaoUserInfo = this.getOAuthUserInfo(accessToken);

        // TODO: kakaoUserInfo 를 이용해서 사용자 정보를 가져오고 서비스 DB 에 저장
        // ex) 회원가입 안 한 유저라면 회원가입 페이지로 넘기기 (여기서 if 문으로 처리하고 controller에서 회원가입 되어 있는지 로그인인지 확인해서 페이지 보내기)
        // ex) 회원가입 했다면 로그인 시켜주기 등등
        
        Member m = new Member();
        m.setuNickName(kakaoUserInfo.getNickname());
        m.setKakaoId(kakaoUserInfo.getId());
        m.setEmail(kakaoUserInfo.getEmail());
        
        System.out.println("m = " + m);
        
        // 이런 식으로 정보를 가져오면 됩니다.
        // 정보는 카카오 developers 콘솔에서 동의 항목 설정한 것들만 가져올 수 있습니다.
        System.out.println("nickname = " + kakaoUserInfo.getNickname());
        System.out.println("id = " + kakaoUserInfo.getId());
        System.out.println("Email() = " + kakaoUserInfo.getEmail());
        
        return m;
    }

    @Override
    public String getAccessToken(String code) {
        String accessToken = "";
        String reqURL = kakaoTokenUri;
        String grantType = "authorization_code";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // POST 요청을 위해 기본값이 false인 setDoOutput을 true로 설정
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // POST 요청에 필요로 요구하는 파라미터를 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter((new OutputStreamWriter(conn.getOutputStream())));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type="+grantType);
            sb.append("&client_id="+kakaoClientId);
            sb.append("&redirect_uri="+kakaoRedirectUri);
            sb.append("&code=" + code);
            sb.append("&client_secret="+kakaoClientSecret);
            bw.write(sb.toString());
            bw.flush();

            // 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();

            // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            // Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            accessToken = element.getAsJsonObject().get("access_token").getAsString();

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    @Override
    public OAuthUserInfo getOAuthUserInfo(String accessToken) {
        String reqURL = kakaoUserInfoUri;
        String reqMethod = "GET";
        //access_token을 이용하여 사용자 정보 조회
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod(reqMethod);
            conn.setRequestProperty("Authorization", "Bearer " + accessToken); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            String responseBody = conn.getResponseMessage();

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            
            System.out.println("response body : " + result);
            System.out.println("responseCode : " + responseCode);

            ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {};
            Map<String, Object> map = objectMapper.readValue(result, typeReference);
            System.out.println("map = " + map);
            return new KakaoUserInfo(map);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
