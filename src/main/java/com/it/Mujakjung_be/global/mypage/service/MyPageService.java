package com.it.Mujakjung_be.global.mypage.service;

import com.it.Mujakjung_be.global.mypage.dto.MyPageDto;
import com.it.Mujakjung_be.global.mypage.entity.MyPageEntity;
import com.it.Mujakjung_be.global.mypage.repository.MyPageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final MyPageRepository myPageRepository;

    // 파일 업로드를 저장할 경로 (로컬 경로 또는 프로젝트 설정 경로에 맞게 조절)
    private final String uploadDir = "C:/mujajung/upload/";

    /**
     * 1. 이메일로 마이페이지 정보 조회
     */
    public MyPageDto getMyPageByEmail(String email) {
        MyPageEntity entity = myPageRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저를 찾을 수 없습니다: " + email));

        // Entity를 Dto로 변환해서 반환
        MyPageDto dto = new MyPageDto();
        dto.setId(entity.getId());
        dto.setNickname(entity.getNickname());
        dto.setEmail(entity.getEmail());
        dto.setBio(entity.getBio());
        dto.setProfileImg(entity.getProfileImg());

        return dto;
    }

    /**
     * 2. 프로필 수정 (닉네임, 자기소개, 프로필 이미지 파일)
     */
    @Transactional
    public void updateMypage(String email, MyPageDto dto, MultipartFile file) {
        // 기존 회원 엔티티 조회
        MyPageEntity entity = myPageRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저를 찾을 수 없습니다: " + email));

        // 텍스트 정보 업데이트 (닉네임, 자기소개 등)
        if (dto.getNickname() != null) {
            entity.setNickname(dto.getNickname());
        }
        if (dto.getBio() != null) {
            entity.setBio(dto.getBio());
        }

        // 프로필 이미지 파일이 새로 업로드된 경우 처리
        if (file != null && !file.isEmpty()) {
            try {
                // 원본 파일 이름
                String originalFilename = file.getOriginalFilename();
                // 겹치지 않도록 UUID 난수 생성
                String uuid = UUID.randomUUID().toString();
                String savedFileName = uuid + "_" + originalFilename;

                // 저장 폴더가 없으면 생성
                File folder = new File(uploadDir);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                // 지정된 경로에 파일 저장
                File destination = new File(uploadDir + savedFileName);
                file.transferTo(destination);

                // 엔티티에 저장된 파일 이름 반영
                entity.setProfileImg(savedFileName);

            } catch (IOException e) {
                throw new RuntimeException("프로필 이미지 업로드 중 오류가 발생했습니다.", e);
            }
        }

        // 변경사항 저장 (@Transactional에 의해 자동 반영되지만 명시적으로 호출할 수도 있음)
        myPageRepository.save(entity);
    }
}