package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.config.SecurityConfig;
import com.fastcampus.projectboard.config.TestSecurityConfig;
import com.fastcampus.projectboard.domain.constant.FormStatus;
import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDto;
import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.dto.request.ArticleRequest;
import com.fastcampus.projectboard.dto.response.ArticleResponse;
import com.fastcampus.projectboard.dto.security.BoardPrincipal;
import com.fastcampus.projectboard.service.ArticleService;
import com.fastcampus.projectboard.service.PaginationService;
import com.fastcampus.projectboard.domain.constant.SearchType;
import com.fastcampus.projectboard.util.FormDataEncoder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("view controller - articles")
@Import({TestSecurityConfig.class, FormDataEncoder.class})   // SecurityConfig파일을 읽어와야 접근가능하므로
@WebMvcTest(ArticleController.class)    // 테스트를 할 컨트롤러만 선택할수있음
class ArticleControllerTest {

    private final MockMvc mvc;

    @MockBean // mokito의 mock같음
    private ArticleService articleService;
    @MockBean // mokito의 mock같음
    private PaginationService paginationService;

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

//    정렬테스트
    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 페이징, 정렬 기능")
    @Test
    void givenPagingAndSortingParams_whenSearchingArticlesPage_thenReturnsArticlesPage() throws Exception {
        // Given
        String sortName = "title";
        String direction = "desc";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = List.of(1, 2, 3, 4, 5);
        given(articleService.searchArticles(null, null, pageable)).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages())).willReturn(barNumbers);

        // When & Then
        mvc.perform(
                        get("/articles")
                                .queryParam("page", String.valueOf(pageNumber))
                                .queryParam("size", String.valueOf(pageSize))
                                .queryParam("sort", sortName + "," + direction)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attribute("paginationBarNumbers", barNumbers));
        then(articleService).should().searchArticles(null, null, pageable);
        then(paginationService).should().getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages());
    }

    @DisplayName("게시글 리스트 페이지 - 검색어와 함께 호출")
    @Test
    public void givenSearchKeyword_whenSearchingArticlesView_thenReturnsArticlesView() throws Exception {
        //given
        SearchType searchType = SearchType.TITLE;
        String searchValue = "title";
        given(articleService.searchArticles(eq(searchType),eq(searchValue),any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(0,1,2,3,4));

        //When&Then
        mvc.perform(
                get("/articles")
                        .queryParam("searchType",searchType.name())
                        .queryParam("searchValue",searchValue)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))    // 호환되는 타입까지
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("searchTypes"));

        then(articleService).should().searchArticles(eq(searchType),eq(searchValue),any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());
    }

    @DisplayName("게시글 리스트 페이지 - 정상호출")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        //given
        given(articleService.searchArticles(eq(null),eq(null),any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(0,1,2,3,4));
        //When&Then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))    // 호환되는 타입까지
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationBarNumbers"));
        
        then(articleService).should().searchArticles(eq(null),eq(null),any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());
    }
    @DisplayName("게시글 상세페이지 - 인증 없을 때 로그인페이지로 이동")
    @Test
    public void givenNothing_whenRequestingArticleView_thenRedirectsToLoginPage() throws Exception {
        //given
        Long articleId=1L;

        //When&Then
        mvc.perform(get("/articles/"+articleId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        then(articleService).shouldHaveNoInteractions();
    }

    @WithMockUser   // 유저 정보를 목킹해서 넣어줌
    @DisplayName("게시글 상세페이지 - 정상호출, 인증된 사용자")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        //given
        Long articleId=1L;
        given(articleService.getArticleWithComments(articleId)).willReturn(createArticleWithCommentsDto());
        //When&Then
        mvc.perform(get("/articles/"+articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"));

        then(articleService).should().getArticleWithComments(articleId);
    }

    @WithMockUser
    @DisplayName("[view][GET] 새 게시글 작성 페이지")
    @Test
    void givenNothing_whenRequesting_thenReturnsNewArticlePage() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/articles/form"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/form"))
                .andExpect(model().attribute("formStatus", FormStatus.CREATE));
    }

    // Bean을 하나 밖에 안 만든 경우에는 userDetailsServiceBeanName 생략해도 찾을 수 있음
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsService",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 새 게시글 등록 - 정상 호출")
    @Test
    void givenNewArticleInfo_whenRequesting_thenSavesNewArticle() throws Exception {
        // Given
        ArticleRequest articleRequest = ArticleRequest.of("new title", "new content", "#new");
        willDoNothing().given(articleService).saveArticle(any(ArticleDto.class));

        // When & Then
        mvc.perform(
                        post("/articles/form")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                                .content(formDataEncoder.encode(articleRequest))
//                                .with(csrf())
//                                .with(user(BoardPrincipal.of()))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"))
                .andExpect(redirectedUrl("/login"));
        then(articleService).should().saveArticle(any(ArticleDto.class));
    }

    @WithMockUser
    @DisplayName("[view][GET] 게시글 수정 페이지")
    @Test
    void givenNothing_whenRequesting_thenReturnsUpdatedArticlePage() throws Exception {
        // Given
        long articleId = 1L;
        ArticleDto dto = createArticleDto();
        given(articleService.getArticle(articleId)).willReturn(dto);

        // When & Then
        mvc.perform(get("/articles/" + articleId + "/form"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/form"))
                .andExpect(model().attribute("article", ArticleResponse.from(dto)))
                .andExpect(model().attribute("formStatus", FormStatus.UPDATE));
        then(articleService).should().getArticle(articleId);
    }
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsService",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 게시글 수정 - 정상 호출")
    @Test
    void givenUpdatedArticleInfo_whenRequesting_thenUpdatesNewArticle() throws Exception {
        // Given
        long articleId = 1L;
        ArticleRequest articleRequest = ArticleRequest.of("new title", "new content", "#new");
        willDoNothing().given(articleService).updateArticle(eq(articleId), any(ArticleDto.class));

        // When & Then
        mvc.perform(
                        post("/articles/" + articleId + "/form")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                                .content(formDataEncoder.encode(articleRequest))
//                                .with(csrf())
                )
//                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles/" + articleId))
                .andExpect(redirectedUrl("/articles/" + articleId));
        then(articleService).should().updateArticle(eq(articleId), any(ArticleDto.class));
    }
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsService",setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("[view][POST] 게시글 삭제 - 정상 호출")
    @Test
    void givenArticleIdToDelete_whenRequesting_thenDeletesArticle() throws Exception {
        // Given
        long articleId = 1L;
        String userId="test";
        willDoNothing().given(articleService).deleteArticle(articleId,userId);

        // When & Then
        mvc.perform(
                        post("/articles/" + articleId + "/delete")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                                .with(csrf())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/articles"))
                .andExpect(redirectedUrl("/articles"));
        then(articleService).should().deleteArticle(articleId,userId);
    }

    @Disabled
    @DisplayName("게시글 검색 전용페이지-정상호출")
    @Test
    public void givenNothing_whenRequestingArticleSearchView_thenReturnsArticleSearchView() throws Exception {
        //given

        //When&Then
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search"));
    }


    @DisplayName("해시태그 검색페이지-정상호출")
    @Test
    public void givenNothing_whenRequestingArticleHashtagSearchView_thenReturnsArticleHashtagSearchView() throws Exception {
        //given
        given(articleService.searchArticlesViaHashtag(eq(null),any(Pageable.class))).willReturn(Page.empty());

        //When&Then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search-hashtag"))
                .andExpect(model().attribute("articles",Page.empty()))
                .andExpect(model().attributeExists("hashtags"))
                .andExpect(model().attributeExists("paginationBarNumbers"));

        then(articleService).should().searchArticlesViaHashtag(eq(null),any(Pageable.class));
    }


    @DisplayName("해시태그 검색페이지-정상호출,해시태그 입력")
    @Test
    public void givenHashtag_whenRequestingArticleHashtagSearchView_thenReturnsArticleHashtagSearchView() throws Exception {
        //given
        String hashtag ="#java";
        given(articleService.searchArticlesViaHashtag(eq(hashtag),any(Pageable.class))).willReturn(Page.empty());

        //When&Then
        mvc.perform(
                get("/articles/search-hashtag")
                        .queryParam("searchValue",hashtag)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search-hashtag"))
                .andExpect(model().attribute("articles",Page.empty()))
                .andExpect(model().attributeExists("hashtags"))
                .andExpect(model().attributeExists("paginationBarNumbers"));

        then(articleService).should().searchArticlesViaHashtag(eq(hashtag),any(Pageable.class));
    }

    private ArticleDto createArticleDto() {
        return ArticleDto.of(
                createUserAccountDto(),
                "title",
                "content",
                "#java"
        );
    }

    private ArticleWithCommentsDto createArticleWithCommentsDto(){
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#hashtag",
                LocalDateTime.now(),
                "title",
                LocalDateTime.now(),
                "title"

        );
    }
    private UserAccountDto createUserAccountDto(){
        return UserAccountDto.of(
                "dong",
                "pw",
                "email",
                "dong",
                "memo",
                LocalDateTime.now(),
                "dong",
                LocalDateTime.now(),
                "dong"

        );
    }
}