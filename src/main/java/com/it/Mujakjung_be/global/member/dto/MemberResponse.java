package com.it.Mujakjung_be.global.member.dto;

import com.it.Mujakjung_be.global.member.entity.MemberEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String regData;
    // ✨ 이 생성자를 추가하면 MemberResponse::new 가 가능해져!
    public MemberResponse(MemberEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.role = entity.getRole().name(); // Enum이면 .name() 필수!
        this.regData = entity.getRegDate() != null ? entity.getRegDate().toString() : "";
    }
}
