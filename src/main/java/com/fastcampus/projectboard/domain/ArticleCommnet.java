package com.fastcampus.projectboard.domain;

import java.time.LocalDateTime;

public class ArticleCommnet {

    private Long id;
    private Article article;    //게시글 (id)
    private String content; // 본문

    // 메타데이터랑 구분을 줘서
    private LocalDateTime createdAt;    //생성일
    private String createdBy;   // 생성자
    private LocalDateTime modifiedAt;   // 수정일
    private String modifiedBy;  // 수정자

}
