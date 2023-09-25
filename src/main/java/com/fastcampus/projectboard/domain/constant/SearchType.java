package com.fastcampus.projectboard.domain.constant;

import lombok.Getter;

public enum SearchType {
//    TITLE,CONTENT,ID,NICKNAME,HASHTAG
    TITLE("제목"),
    CONTENT("본문"),
    ID("유저ID"),
    NICKNAME("닉네임"),
    HASHTAG("해시태그");

    @Getter private final String description;

    SearchType(String description){
        this.description = description;
    }
}
