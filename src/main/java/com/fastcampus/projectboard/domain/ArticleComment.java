package com.fastcampus.projectboard.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter //lombok이용해서 게터
@ToString
//@EqualsAndHashCode // 이방법은 lombok을 이용한 방법 모든 필드를 비교함
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@EntityListeners(AuditingEntityListener.class)  //Auditing사용하기위해서 붙여야함
//@NoArgsConstructor(access = AccessLevel.PROTECTED) // 아래 기본생성자와 동일
@Entity
public class ArticleComment {

    @Id
    // 자동증가를 해주기위한 애너테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @ManyToOne(optional = false)    // cascade는 디폴트값이 논
    private Article article;    //게시글 (id)
    @Setter
    @Column(nullable = false, length = 500)
    private String content; // 본문

    // 메타데이터랑 구분을 줘서
    // jpa auditing이용
    // 자동으로 Auditing됨
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;    //생성일
    @CreatedBy
    @Column(nullable = false, length = 100)
    private String createdBy;   // 생성자
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;   // 수정일
    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private String modifiedBy;  // 수정자

    protected ArticleComment(){}

    private ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }

    private ArticleComment of(Article article, String content) {
        return new ArticleComment(article,content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }



}
