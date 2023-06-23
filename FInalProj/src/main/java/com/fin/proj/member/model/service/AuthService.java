package com.fin.proj.member.model.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;

@Service
public class AuthService {
	 
	@Autowired
	private JavaMailSender emailSender;
	
	@Value("${coolsms.api.key}")
    private String apiKey;
	
	@Value("${coolsms.api.secret}")
    private String apiSecret;
	
	@Value("${my.phone.number}")
    private String myPhoneNumber;
		
	private int authNumber;
	 
	public void makeRandomNumber() {
		Random r = new Random();
		int checkNum = r.nextInt(888888) + 111111;
		System.out.println("인증번호 : " + checkNum);
		authNumber = checkNum;
	}

	public int checkEmail(String emailAddress) {		
		makeRandomNumber();
		
		String setFrom = "finemeaning.kh@gmail.com"; // email-config에 설정한 자신의 이메일 주소를 입력 
		String toMail = emailAddress;
		String title = "🍀[선뜻] 회원가입 인증코드 안내"; // 이메일 제목 
		String content = 
					"홈페이지를 방문해주셔서 감사합니다." + 	//html 형식으로 작성 ! 
	                "<br><br>" + 
				    "인증 번호는 " + authNumber + "입니다." + 
				    "<br>" + 
				    "해당 인증번호를 인증번호 확인란에 기입하여 주세요."; //이메일 내용 삽입
		mailSend(setFrom, toMail, title, content);
		return authNumber;
	}
	
	public int findId(String emailAddress) {
		makeRandomNumber();
		
		String setFrom = "finemeaning.kh@gmail.com";
		String toMail = emailAddress;
		String title = "🍀[선뜻] 아이디 찾기 인증코드 안내";
		String content = 
					"홈페이지를 이용해주셔서 감사합니다." +
	                "<br><br>" + 
				    "인증 번호는 " + authNumber + "입니다." + 
				    "<br>" + 
				    "해당 인증번호를 인증번호 확인란에 기입하여 주세요.";
		mailSend(setFrom, toMail, title, content);
		return authNumber;
	}
	
	public int findPwd(String emailAddress) {
		makeRandomNumber();
		
		String setFrom = "finemeaning.kh@gmail.com";
		String toMail = emailAddress;
		String title = "🍀[선뜻] 비밀번호 찾기 인증코드 안내";
		String content = 
					"홈페이지를 이용해주셔서 감사합니다." +
	                "<br><br>" + 
				    "인증 번호는 " + authNumber + "입니다." + 
				    "<br>" + 
				    "해당 인증번호를 인증번호 확인란에 기입하여 주세요.";
		mailSend(setFrom, toMail, title, content);
		return authNumber;
	}
	
	//이메일 전송 메소드
	public void mailSend(String setFrom, String toMail, String title, String content) { 
		MimeMessage message = emailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
			helper.setFrom(setFrom);
			helper.setTo(toMail);
			helper.setSubject(title);
			// true 전달 > html 형식으로 전송 , 작성하지 않으면 단순 텍스트로 전달.
			helper.setText(content, true);
			emailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public int findBySms(String phone) {
		makeRandomNumber();
		
		DefaultMessageService messageService =  NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
		// Message 패키지가 중복될 경우 net.nurigo.sdk.message.model.Message로 치환하여 주세요
		Message message = new Message();
		message.setFrom(myPhoneNumber);
		message.setTo(phone);
		message.setText("🍀[선뜻] 인증 번호는 " + authNumber + " 입니다.");

		try {
		  // send 메소드로 ArrayList<Message> 객체를 넣어도 동작합니다!
		  messageService.send(message);
		} catch (NurigoMessageNotReceivedException exception) {
		  // 발송에 실패한 메시지 목록을 확인할 수 있습니다!
		  System.out.println(exception.getFailedMessageList());
		  System.out.println(exception.getMessage());
		} catch (Exception exception) {
		  System.out.println(exception.getMessage());
		}
		
		return authNumber;
	}

}

