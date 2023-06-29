package com.fin.proj.common.model.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fin.proj.common.model.exception.ImageException;

@Service
//@RequiredArgsConstructor
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    
    @Autowired
    private AmazonS3 amazonS3;
//    private final AmazonS3 amazonS3;

    public String uploadFile(MultipartFile multipartFile) {
    	
        String fileName = createFileName(multipartFile.getOriginalFilename());	// 원래 사용자가 갖고 있던 파일의 이름
        
        // Metadata : 데이터의 정보가 담긴 데이터 (키 : 값의 데이터)
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());		// 1. 객체 크기 / 파일 크기 (바이트의 크기) 지정
        objectMetadata.setContentType(multipartFile.getContentType());	// 2. 객체 유형 / multipartFile contentType이라고 지정하여 보냄
        																// 컨트롤러로 요청을 보낼 때 multipart/form-data 타입이고,
        																// getContentType을 할 때는 image/png, image/jpeg 등의 타입으로 나옴.
        // ObjectMetadata : 메타 데이터를 객체 형식으로 보내는 것
        
        System.out.println(multipartFile.getContentType());
        
        try(InputStream inputStream = multipartFile.getInputStream()) {
        	// amazonS3에 정해진 메소드 → putObject()
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)); // publicRead 권한으로 업로드
            // 1. bucket : 상단에 있는 내 bucket의 정보 (aws에 만든 버킷)
            // 2. fileName : 난수화 된 파일 이름 (내가 가진 파일 명에서는 확장자만 가져와 랜덤 값에 붙인 것)
            // 3. inputStream : 읽어올 파일 내용 (바이트 단위의 데이터)
            // 4. objectMetadata : 위에서 만든 메타 데이터
        } catch (IOException e) {
            throw new ImageException("이미지 업로드 실패");
        }

        return fileName; // 오류 없으면 만들어진 파일 명 리턴 (UUID + 확장자)
    }

    public void deleteFile(String fileName) {
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    private String createFileName(String fileName) { // 먼저 파일 업로드 시, UUID.randomUUID()로 파일명을 난수화(random으로 돌림)
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
        	// 랜덤 값에 concat으로 파일의 확장자를 붙여줌. (ex. .png / .jpg ...)
        	// UUID : 네트워크 상에서 중복되지 않는 ID를 만들기 위한 표준 규약 (자바에 내장)
    }

    private String getFileExtension(String fileName) {
    	// file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }
    					// fileName : UUID + 확장자
    public String getUrl(String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
     // amazonS3에 정해진 메소드 → getUrl() : 버킷 정보와 fileName을 토대로 알아서 url을 가져옴
    }
}