package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import com.fastcampus.projectboard.domain.constant.SearchType;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

// TODO: 코드 다시 보면서 보기 + CommentsServiceTest도 함께
@DisplayName("service - articles")
@ExtendWith(MockitoExtension.class) // 간단하게 시작하기위해서 Mockito이용
class ArticleServiceTest {

    @InjectMocks    // mock을 주입하는 대상    // 생성자 주입 불가
    private ArticleService sut;
    @Mock   // 나머지대상
    private ArticleRepository articleRepository;    // 생성자주입가능
    @Mock private UserAccountRepository userAccountRepository;

    @DisplayName("검색어 없이 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    // 페이지로 반환
    void givenNoSearchParameters_whenSearchingArticles_thenReturnsArticlePage(){

        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findAll(pageable)).willReturn(Page.empty());

//        SearchParameters param = SearchParameters.of(SearchType.TITLE,"search keyword");
        // 페이지안에는 페이징관련정보가 담겨있음
        Page<ArticleDto> articles = sut.searchArticles(null, null, pageable);

        assertThat(articles).isEmpty();
        then(articleRepository).should().findAll(pageable);
    }

    @DisplayName("검색어 없이 게시글을 해시태그 검색하면, 빈 페이지를 반환한다.")
    @Test
    void givenNoSearchParameters_whenSearchingArticlesViaHashtag_thenReturnsEmptyPage() {
        // Given
        Pageable pageable = Pageable.ofSize(20);


        // When
        Page<ArticleDto> articles = sut.searchArticlesViaHashtag(null, pageable);

        // Then
        assertThat(articles).isEqualTo(Page.empty(pageable));
        then(articleRepository).shouldHaveNoInteractions();
    }
    @DisplayName("게시글을 해시태그 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenHashtag_whenSearchingArticlesViaHashtag_thenReturnsArticlesPage() {
        // Given
        String hashtag = "#java";
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findByHashtag(hashtag,pageable)).willReturn(Page.empty(pageable));

        // When
        Page<ArticleDto> articles = sut.searchArticlesViaHashtag(hashtag, pageable);

        // Then
        assertThat(articles).isEqualTo(Page.empty(pageable));
        then(articleRepository).should().findByHashtag(hashtag,pageable);
    }

    @DisplayName("해시태그를 조회하면, 유니크 해시태그 리스트를 반환한다.")
    @Test
    void givenNothing_whenCalling_thenReturnsHashtags() {
        // Given
        List<String> expectedHashtags = List.of("#java","#spring","#boot");
        given(articleRepository.findAllDistinctHashtags()).willReturn(expectedHashtags);

        // When
        List<String> actualHashtags = sut.getHashtags();

        // Then
        assertThat(actualHashtags).isEqualTo(expectedHashtags);
        then(articleRepository).should().findAllDistinctHashtags();
    }

    @DisplayName("검색어와 함께 게시글을 검색하면, 게시글 페이지를 반환한다.")
    @Test
    void givenSearchParameters_whenSearchingArticles_thenReturnsArticlePage() {
        // Given
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title";
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findByTitleContaining(searchKeyword, pageable)).willReturn(Page.empty());

        // When
        Page<ArticleDto> articles = sut.searchArticles(searchType, searchKeyword, pageable);

        // Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findByTitleContaining(searchKeyword, pageable);
    }

    @DisplayName("게시글을 조회하면, 게시글을 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle(){
        Long articleId = 1L;
        Article article = createArticle();
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        // When
        ArticleWithCommentsDto dto = sut.getArticleWithComments(articleId);

        // Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent())
                .hasFieldOrPropertyWithValue("hashtag", article.getHashtag());
        then(articleRepository).should().findById(articleId);
    }
    @DisplayName("없는 게시글을 조회하면, 예외를 던진다.")
    @Test
    void givenNonexistentArticleId_whenSearchingArticle_thenThrowsException() {
        // Given
        Long articleId = 0L;
        given(articleRepository.findById(articleId)).willReturn(Optional.empty());

        // When
        Throwable t = catchThrowable(() -> sut.getArticleWithComments(articleId));

        // Then
        assertThat(t)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다 - articleId: " + articleId);
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("게시글 정보를 입력하면, 게시글을 생성한다.")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSavesArticle() {
        // Given
        ArticleDto dto = createArticleDto();
        given(articleRepository.save(any(Article.class))).willReturn(createArticle());

        // When
        sut.saveArticle(dto);

        // Then
        then(articleRepository).should().save(any(Article.class));
    }
    @DisplayName("게시글의 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenModifiedArticleInfo_whenUpdatingArticle_thenUpdatesArticle() {
        // Given
        Article article = createArticle();
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
        given(articleRepository.getReferenceById(dto.id())).willReturn(article);
        given(userAccountRepository.getReferenceById(dto.userAccountDto().userId())).willReturn(dto.userAccountDto().toEntity());

        // When
        sut.updateArticle(dto.id(),dto);

        // Then
        assertThat(article)
                .hasFieldOrPropertyWithValue("title", dto.title())
                .hasFieldOrPropertyWithValue("content", dto.content())
                .hasFieldOrPropertyWithValue("hashtag", dto.hashtag());
        then(articleRepository).should().getReferenceById(dto.id());
        then(userAccountRepository).should().getReferenceById(dto.userAccountDto().userId());
    }

    @DisplayName("없는 게시글의 수정 정보를 입력하면, 경고 로그를 찍고 아무 것도 하지 않는다.")
    @Test
    void givenNonexistentArticleInfo_whenUpdatingArticle_thenLogsWarningAndDoesNothing() {
        // Given
        ArticleDto dto = createArticleDto("새 타이틀", "새 내용", "#springboot");
        given(articleRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

        // When
        sut.updateArticle(dto.id(),dto);

        // Then
        then(articleRepository).should().getReferenceById(dto.id());
    }
    @DisplayName("게시글의 ID를 입력하면, 게시글을 삭제한다")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle() {
        // Given
        Long articleId = 1L;
        String userId="test";
        willDoNothing().given(articleRepository).deleteByIdAndUserAccount_UserId(articleId,userId);

        // When
        sut.deleteArticle(1L,userId);

        // Then
        then(articleRepository).should().deleteByIdAndUserAccount_UserId(articleId,userId);
    }


    private UserAccount createUserAccount() {
        return UserAccount.of(
                "uno",
                "password",
                "uno@email.com",
                "Uno",
                null
        );
    }

    private Article createArticle() {
        return Article.of(
                createUserAccount(),
                "title",
                "content",
                "#java"
        );
    }

    private ArticleDto createArticleDto() {
        return createArticleDto("title", "content", "#java");
    }

    private ArticleDto createArticleDto(String title, String content, String hashtag) {
        return ArticleDto.of(1L,
                createUserAccountDto(),
                title,
                content,
                hashtag,
                LocalDateTime.now(),
                "Uno",
                LocalDateTime.now(),
                "Uno");
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "uno",
                "password",
                "uno@mail.com",
                "Uno",
                "This is memo",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }

}

