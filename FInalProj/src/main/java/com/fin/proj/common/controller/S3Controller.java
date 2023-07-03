package com.fin.proj.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fin.proj.common.model.exception.ImageException;
import com.fin.proj.common.model.service.ImageService;
import com.fin.proj.common.model.service.S3Service;
import com.fin.proj.common.model.vo.Image;

import lombok.RequiredArgsConstructor;

@RestController
//@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3Controller {
	
	@Autowired
    private S3Service S3Service;	// s3 업로드 용 서비스
//	private final S3Service S3Service;
    
    @Autowired
    ImageService iService;			// db 업로드 용 서비스
    
    @PostMapping("/file.im")
    @ResponseBody							// multipart/form-data 타입
    public String uploadFile(@RequestPart MultipartFile multipartFile) {
    	// MultipartFile : text 를 제외한 나머지 확장자에 해당 → @RequestPart로 받아와야 함!
        System.out.println("file 요청");
        try {
            String imageName = S3Service.uploadFile(multipartFile); // multipartFile : 사용자가 업로드한 파일 자체를 가리킴
            String imageUrl = S3Service.getUrl(imageName);
            
            // 여기부터 내 db 이미지 테이블에 접근!
            Image image = new Image(imageUrl, imageName);
            int result = iService.insertImage(image); // save : insert 하는 메소드
            
            if(result == 1) {
            	return imageUrl;
            } else {
            	throw new ImageException("이미지 저장 실패");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ImageException("이미지 저장 실패");
        }
    }
    
    @DeleteMapping("/file/{imageNo}")
    public int deleteImage(@PathVariable("imageNo") int imageNo) {
        try {
            Image image = iService.findByImageNo(imageNo); // db에 있는 imageNo
            S3Service.deleteFile(image.getImageName()); // UUID로 만든 이름
            return 1;
        } catch(Exception e) {
            return 0;
        }
    }
    
    @GetMapping("/file")
    @ResponseBody
    public String getFile(@RequestParam String fileName) {
        String url = S3Service.getUrl(fileName).toString();
        return url;
    }
}
