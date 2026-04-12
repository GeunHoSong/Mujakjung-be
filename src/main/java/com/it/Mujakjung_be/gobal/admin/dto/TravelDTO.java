package com.it.Mujakjung_be.gobal.admin.dto;

import lombok.*;

@Data
@Getter
@Setter
public class TravelDTO {
    private Long id;
    private String category;
    private String title;
    private String location;
    private String content;

    // 기본 생성자 (jackson 라이브러리 json를 객체를 바꿀때 필요)
    public TravelDTO (){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getContent() {
        return content;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
