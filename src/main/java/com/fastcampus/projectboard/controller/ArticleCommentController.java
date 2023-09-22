package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.service.ArticleCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor    // 생성자 만들어주는 애너테이션
// 매핑수정
@RequestMapping("/comments")
@Controller
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

//    @PostMapping("/new")
//    public String postNewArticle

}
