package com.fin.proj.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fin.proj.common.interceptor.CheckAdminInterceptor;
import com.fin.proj.common.interceptor.CheckKakaoInterceptor;
import com.fin.proj.common.interceptor.CheckLoginInterceptor;
import com.fin.proj.common.interceptor.CheckVolunteerAdminInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new CheckLoginInterceptor())
				.addPathPatterns("/*.su", "/*.bo", "/*.vo", "/editMyInfo.me", "/editMyPwd.me")
				.excludePathPatterns("/supportMain.su", "/mainCategory.su", "/mainSearch.su", "/supportDetail.su",
									 "/faqMain.bo", "/faqDetail.bo", "/fineNewsMain.bo", "/finePeopleMain.bo", "/fruitMain.bo", "/fruitDetail.bo",
									 "/noticeList.bo", "/commList.bo",
									 "/volunteer.vo", "/volunteerDetail.vo", "/volunteerAjax.vo");
		
		registry.addInterceptor(new CheckAdminInterceptor())
				.addPathPatterns("/supportListAdmin.su",
								 "/supportApplyListAdmin.su",
								 "/applyDevision.su",
								 "/supportDetailAdmin.su",
								 "/updateApplyStatus.su",
								 "/supportEndListAdmin.su",
								 "/searchSupportListAdmin.su",
								 "/supporterListEach.su",
								 "/supporterListAdmin.su",
								 "/searchEndList.su",
								 "/finePeopleAdmin.bo", "/finePeopleForm.bo", "/finePeopleEdit.bo",
								 "/fineNewsAdmin.bo", "/fineNewsForm.bo", "/fineNewsEdit.bo",
								 "/fruitAdmin.bo", "/fruitForm.bo", "/fruitEdit.bo",
								 "/faqAdmin.bo", "/faqForm.bo", "/faqEdit.bo",
								 "/adminVolunteerList.vo", "/searchAdminVolunteerList.vo", "/adminAllVolunteerApplyList.vo", "/adminVolunteerApplyList.vo",
								 "/noticeListAdmin.bo", "/qaAdminList.bo", "/commAdminList.bo",
								 "/editUserInfo.me", "/userInfoDetail.me"
								 
								 );
		
		registry.addInterceptor(new CheckKakaoInterceptor())
				.addPathPatterns("/editMyPwd.me");
		
		registry.addInterceptor(new CheckVolunteerAdminInterceptor())
				.addPathPatterns("/volunteerEnroll.vo", "/volunteerEnrollHistory.vo", "/searchVolunteerEnrollHistory.vo",
								"/volunteerEdit.vo", "/insertVolunteer.vo", "/updateVolunteer.vo", "/deleteVolunteer.vo",
								"/updateVolunteerStatus.vo", "/volunteerApplyList.vo");
	}
}
