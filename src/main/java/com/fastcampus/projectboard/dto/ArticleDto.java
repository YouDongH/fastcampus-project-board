package com.fastcampus.projectboard.dto;

import com.fastcampus.projectboard.domain.Article;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.fastcampus.projectboard.domain.Article}
 */
public record ArticleDto(
        Long id,
        UserAccountDto userAccountDto,
        String title,
        String content,
        String hashtag,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {
    public static ArticleDto of(Long id, UserAccountDto userAccountDto, String title, String content, String hashtag, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
    return new ArticleDto(id, userAccountDto, title, content, hashtag, createdAt, createdBy, modifiedAt, modifiedBy);
}

    // 이렇게 작성을하면 Domain코드는 dto의 존재를 몰라도 됨 -> Domain코드가 영향안받음 OSIV

    // entity로부터 dto생성
//    public static ArticleDto from(Article entity) {
//        return new ArticleDto(
//                entity.getId(),
//                UserAccountDto.from(entity.getUserAccount()),
//                entity.getTitle(),
//                entity.getContent(),
//                entity.getHashtag(),
//                entity.getCreatedAt(),
//                entity.getCreatedBy(),
//                entity.getModifiedAt(),
//                entity.getModifiedBy()
//        );
//    }

    // dto로부터 엔티티 생성
    public Article toEntity() {
        return Article.of(
                userAccountDto.toEntity(),
                title,
                content,
                hashtag
        );
    }
}