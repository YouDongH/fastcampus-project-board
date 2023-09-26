package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.config.JpaConfig;
import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

// test용 db를 설정
// but @DataJpaTest 가 설정 무시하고 내장db로 테스트
// 그래서 @AutoConfigureTestDatabase 써야함
//@ActiveProfiles("testdb")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)    // test.database.replace: none
@DisplayName("JPA 연결 테스트")
@Import(JpaRepositoryTest.TestJpaConfig.class)    // Auditing을 위해서
@DataJpaTest
// JpaTest는 각 메소드마다 메소드단위로 트랜잭션이 걸려있음 , 테스트 트랜잭션은 디폴트가 디폴트
//@AutoConfigureTestDatabase
class JpaRepositoryTest {

    // 생성자 주입패턴으로  객체생성
    private  ArticleRepository articleRepository;
    private  ArticleCommentRepository articleCommentRepository;

    private final UserAccountRepository userAccountRepository;

    JpaRepositoryTest(
            // paramiter에 오토와이드 가능
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository,
            @Autowired UserAccountRepository userAccountRepository
    ) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
        this.userAccountRepository = userAccountRepository;
    }

    /**
     *
     */
    @DisplayName("select테스트")
    @Test
    void givenTestData_whenSelecting_theWorksFine(){
        // Given
        
        // When
        List<Article> articles = articleRepository.findAll();
        // Then
        assertThat(articles)
                .isNotNull()
                .hasSize(123);
    }

    @DisplayName("insert테스트")
    @Test
    void givenTestData_whenInserting_theWorksFine(){
        // Given
        Long previousCount = articleRepository.count();
        UserAccount userAccount = userAccountRepository.save(UserAccount.of("uno", "pw", null, null, null));
        Article article = Article.of(userAccount, "new article", "new content", "#spring");

        // When
        articleRepository.save(article);
        // Then
        assertThat(articleRepository.count())
                .isEqualTo(previousCount+1);
    }
    @DisplayName("update테스트")
    @Test
    void givenTestData_whenUpdating_theWorksFine(){
        // Given
        Article article = articleRepository.findById(1L).orElseThrow(); // orElseThrow : 값가져오고 아니면 예외던져서 테스트종료
        String updatedHashtag = "#springboot";
        article.setHashtag(updatedHashtag);
        // When
        Article savedArticle = articleRepository.saveAndFlush(article); // Flush를 해줘야 롤백안됨

        // Then
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag",updatedHashtag);
    }
    @DisplayName("delete테스트")
    @Test
    void givenTestData_whenDeleting_theWorksFine(){
        // Given
        Article article = articleRepository.findById(1L).orElseThrow(); // orElseThrow : 이건 한번보기 아니면 돌려버림
        String updatedHashtag = "#springboot";
        article.setHashtag(updatedHashtag);
        // When
        Article savedArticle = articleRepository.saveAndFlush(article); // Flush를 해줘야 롤백안됨

        // Then
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag",updatedHashtag);
    }

    // Auditing 할때 시큐리티 무시하게 동작하게만듬
    @EnableJpaAuditing
    @TestConfiguration
    public static class TestJpaConfig{
        @Bean
        public AuditorAware<String> auditorAware(){
            return ()-> Optional.of("test");
        }
    }
//    @DisplayName("delete 테스트")
//    @Test
//    void givenTestData_whenDeleting_thenWorksFine() {
//        // Given
//        // 삭제테스트는 글을 지움에 따라 댓글도 삭제되므로 댓글 숫자까지 테스트해야함
//        Article article = articleRepository.findById(1L).orElseThrow();
//        long previousArticleCount = articleRepository.count();
//        long previousArticleCommentCount = articleCommentRepository.count();
//        int deletedCommentsSize = article.getArticleCommnets().size();
//
//        // When
//        articleRepository.delete(article);
//
//        // Then
//        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
//        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentsSize);
//    }

}