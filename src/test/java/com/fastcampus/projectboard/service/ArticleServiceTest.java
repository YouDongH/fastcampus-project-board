package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleUpdateDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import com.fastcampus.projectboard.type.SearchType;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("service - articles")
@ExtendWith(MockitoExtension.class) // 간단하게 시작하기위해서 Mockito이용
class ArticleServiceTest {

    @InjectMocks    // mock을 주입하는 대상    // 생성자 주입 불가
    private ArticleService sut;
    @Mock   // 나머지대상
    private ArticleRepository articleRepository;    // 생성자주입가능

    @DisplayName("search - list")
    @Test
    void givenSearchParameters_whenSearchingArticles_thenReturnsArticleList(){

//        SearchParameters param = SearchParameters.of(SearchType.TITLE,"search keyword");
        // 페이지안에는 페이징관련정보가 담겨있음
        Page<ArticleDto> articles = sut.searchArticles(SearchType.TITLE,"search keyword");

        assertThat(articles).isNotNull();

    }
    @DisplayName("click - article")
    @Test
    void givenArticleId_whenSearchingArticle_thenReturnsArticle(){


        ArticleDto articles = sut.searchArticle(1L);

        assertThat(articles).isNotNull();

    }
    @DisplayName("article정보를 입력 -> make article")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSavesArticle(){

        ArticleDto articles = ArticleDto.of(LocalDateTime.now(), "Dong","title","content",
                "hashtag");
        given(articleRepository.save(any(Article.class))).willReturn(null);
        // 아무 Article을 넘겨주면 아무 Article을 반환

        sut.saveArticle(articles);

        then(articleRepository).should().save(any(Article.class));  // save를 한번 호출 했는지 확인


    }

    @DisplayName("update article")
    @Test
    void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle(){

        ArticleUpdateDto articles = ArticleUpdateDto.of("title","content","hashtag");
        given(articleRepository.save(any(Article.class))).willReturn(null);
        // 아무 Article을 넘겨주면 아무 Article을 반환

        sut.updateArticle(1L,articles);

        then(articleRepository).should().save(any(Article.class));  // save를 한번 호출 했는지 확인


    }
    @DisplayName("delete article")
    @Test
    void givenArticleIdDeleteInfo_whenDeletingArticle_thenDeletesArticle(){

        willDoNothing().given(articleRepository).delete(any(Article.class));  // 반환형이 없을때 willDoNothing()사용
        // 아무 Article을 넘겨주면 아무 Article을 반환

        sut.deleteArticle(1L);

        then(articleRepository).should().delete(any(Article.class));  // save를 한번 호출 했는지 확인


    }

}