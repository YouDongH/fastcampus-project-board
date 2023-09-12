package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.dto.ArticleDto;
import com.fastcampus.projectboard.dto.ArticleUpdateDto;
import com.fastcampus.projectboard.repository.ArticleRepository;
import com.fastcampus.projectboard.type.SearchType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor    // 필수 필드에 대한 생성자를 자동으로 만들어주는 애너테이션(Lombok)
@Transactional  // 클래스단위로 트랜잭션걸고
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true) // 읽기만하므로 읽기 단위로 트랜잭션검
    // Page클래스안에 페이징정보와 정렬기능이 있음 - 따로 메소드x
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword) {
        return Page.empty();
    }

    @Transactional(readOnly = true)
    public ArticleDto searchArticle(long l) {
        return null;
    }

    public void saveArticle(ArticleDto dto){

    }

    public void updateArticle(long articleId,ArticleUpdateDto articles) {
    }

    public void deleteArticle(long l) {
    }
}
