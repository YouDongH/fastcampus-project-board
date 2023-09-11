package com.fastcampus.projectboard.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled("불필요하므로 제거")
// 직접 만들어서 테스트한게 아니라 주어진 기능을 쓴거기 때문에 불필요
@DisplayName("Data REST Api Test")
//인테그래션 테스트 이므로 DB에 영향을 줘서 트랜잭션 애너테이션 넣음
@Transactional  // 테스트에서 동작하는 모든 행위가 rollback이 디폴트값
@AutoConfigureMockMvc   // MockMvc의 존재를 알기위해서
@SpringBootTest
//@WebMvcTest // 이 테스트는 controller와 관련된 최소한의 빈만 읽음 , dataRest 오토 컨피그레이션을 안읽어서 테스트 실패함
public class DataRestTest {
    //WebMvcTest는 내부적으로 MockMvc 준비해줌
    private MockMvc mvc;

    public DataRestTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("api article list all")
    @Test
    void givenNothing_whenRequestingArticles_thenReturnsArticlesJsonResponse() throws Exception {
        //Given

        // When & Then
        // api/acticles이 잘실행되는지. 상태가 ok인지 등
        mvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
                .andDo(print());    // 내용을 보기위해서
        
    }
    @DisplayName("api article list one")
    @Test
    void givenNothing_whenRequestingArticle_thenReturnsArticlesJsonResponse() throws Exception {
        //Given

        // When & Then
        // api/acticles이 잘실행되는지. 상태가 ok인지 등
        mvc.perform(get("/api/articles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
                .andDo(print());    // 내용을 보기위해서

    }
    @DisplayName("api articleComment list one")
    @Test
    void givenNothing_whenRequestingArticleCommentsFromArticle_thenReturnsArticlesJsonResponse() throws Exception {
        //Given

        // When & Then
        // api/acticles이 잘실행되는지. 상태가 ok인지 등
        mvc.perform(get("/api/articles/1/ArticleComments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
                .andDo(print());    // 내용을 보기위해서

    }
    @DisplayName("api articleComment list all")
    @Test
    void givenNothing_whenRequestingArticleComments_thenReturnsArticlesJsonResponse() throws Exception {
        //Given

        // When & Then
        // api/acticles이 잘실행되는지. 상태가 ok인지 등
        mvc.perform(get("/api/ArticleComments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
                .andDo(print());    // 내용을 보기위해서

    }
    @DisplayName("api articleComment list one")
    @Test
    void givenNothing_whenRequestingArticleComment_thenReturnsArticlesJsonResponse() throws Exception {
        //Given

        // When & Then
        // api/acticles이 잘실행되는지. 상태가 ok인지 등
        mvc.perform(get("/api/ArticleComments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")))
                .andDo(print());    // 내용을 보기위해서

    }
}
