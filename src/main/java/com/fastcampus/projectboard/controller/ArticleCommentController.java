package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.dto.request.ArticleCommentRequest;
import com.fastcampus.projectboard.dto.request.ArticleRequest;
import com.fastcampus.projectboard.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor    // 생성자 만들어주는 애너테이션
// 매핑수정
@RequestMapping("/comments")
@Controller
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

    @PostMapping("/new")
    public String postNewArticleComment(ArticleCommentRequest articleCommentRequest){
        //TODO: 인증정보 넣어줘야함
        articleCommentService.saveArticleComment(articleCommentRequest.toDto(UserAccountDto.of(
                "dong","1234","dong@emainl.com",null,null,null,null,null,null
        )));



        return "redirect:/articles" + articleCommentRequest.articleId();
    }


    @PostMapping("/{commentId}/delete")
    public String deleteArticleComment(@PathVariable Long commentId, Long articleId){

        articleCommentService.deleteArticleComment(commentId);

        return "redirect:/articles" + articleId;
    }

}
