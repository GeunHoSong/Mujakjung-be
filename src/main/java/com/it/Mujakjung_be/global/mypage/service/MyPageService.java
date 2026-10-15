package com.it.Mujakjung_be.global.mypage.service;

import com.it.Mujakjung_be.global.member.dto.MyPageResponse;
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

    // 파일 업로드 저장 경로 (로컬 경로)
    private final String uploadDir = "C:/mujajung/upload/";

    /**
     * 1. 이메일로 마이페이지 정보 조회
     */
    @Transactional
    public MyPageDto getMyPage(String email) {
        // 1. mypage 테이블에서 이메일로 조회, 없으면 새로 생성해서 저장
        MyPageEntity myPage = myPageRepository.findByEmail(email)
                .orElseGet(() -> {
                    MyPageEntity newMyPage = MyPageEntity.builder()
                            .email(email)
                            .nickname("사용자") // 기본 닉네임 설정
                            .bio("안녕하세요!") // 기본 자기소개 설정
                            .build();
                    return myPageRepository.save(newMyPage);
                });

        // MyPageDto로 변환하여 반환 (MyPageDto의 구조에 맞춰 매핑)
        return MyPageDto.builder()
                .email(myPage.getEmail())
                .nickname(myPage.getNickname())
                .bio(myPage.getBio())
                .profileImg(myPage.getProfileImg())
                .build();
    }
    /**
     * 2. 프로필 수정 (닉네임, 자기소개, 프로필 이미지 파일)
     */
    @Transactional
    public void updateMypage(String email, MyPageDto dto, MultipartFile file) {
        MyPageEntity entity = myPageRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저를 찾을 수 없습니다: " + email));

        String updatedNickname = dto.getNickname() != null ? dto.getNickname() : entity.getNickname();
        String updatedBio = dto.getBio() != null ? dto.getBio() : entity.getBio();
        String savedFileName = entity.getProfileImg();

        // 프로필 이미지 파일이 새로 업로드된 경우 처리
        if (file != null && !file.isEmpty()) {
            try {
                String originalFilename = file.getOriginalFilename();
                String uuid = UUID.randomUUID().toString();
                savedFileName = uuid + "_" + originalFilename;

                File folder = new File(uploadDir);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                File destination = new File(uploadDir + savedFileName);
                file.transferTo(destination);

            } catch (IOException e) {
                throw new RuntimeException("프로필 이미지 업로드 중 오류가 발생했습니다.", e);
            }
        }

        // 엔티티 내부 update 메서드를 통해 정보 갱신
        entity.update(updatedNickname, updatedBio, savedFileName);

        myPageRepository.save(entity);
    }
}