package com.fastcampus.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter //lombok이용해서 게터
@ToString
//@EqualsAndHashCode // 이방법은 lombok을 이용한 방법 모든 필드를 비교함
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
//@EntityListeners(AuditingEntityListener.class)  //Auditing사용하기위해서 붙여야함
@Entity
public class Article extends AuditingFields {
    @Id
    // 자동증가를 해주기위한 애너테이션
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Setter
    @Column(nullable = false)
    private String title;   // 제목
    @Setter
    @Column(nullable = false, length = 10000)
    private String content; //  본문

    @Setter private String hashtag; // 해시태그

    @OrderBy("id")
    @OneToMany(mappedBy = "article",cascade = CascadeType.ALL)
    @ToString.Exclude
    // 이 변수 때문에 무한루프 걸릴수 있으니 toString에서 제외해줌
    // 중복을 허용x , 모아서 보여주겟다
    private final Set<ArticleComment> articleCommnets = new LinkedHashSet<>();

    // 공통부분을 상속받는걸로 처리
    // 메타데이터랑 구분을 줘서
    // jpa auditing이용
    // 자동으로 Auditing됨
//    @CreatedDate
//    @Column(nullable = false)
//    private LocalDateTime createdAt;    //생성일
//    @CreatedBy
//    @Column(nullable = false, length = 100)
//    private String createdBy;   // 생성자
//    @LastModifiedDate
//    @Column(nullable = false)
//    private LocalDateTime modifiedAt;   // 수정일
//    @LastModifiedBy
//    @Column(nullable = false, length = 100)
//    private String modifiedBy;  // 수정자

    protected Article(){}

    // 생성할때 직접적으로 생성하는 것을 맡고 팩토리 메소드를 이용함
    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(String title, String content, String hashtag){
        return new Article(title,content,hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Java14 Patten matching
        // 넘어온 Object값이 Article인지 확인하고 바로 값을 적용
        if (!(o instanceof Article article)) return false;
        // id가 널값인지 까지 확인
        // id가 부여되지않았다 = 영속화가 되지 않았다 그럼 검사할 필요가 없으니까
        return id != null && Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
