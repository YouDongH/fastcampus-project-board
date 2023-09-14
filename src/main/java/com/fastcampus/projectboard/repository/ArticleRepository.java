package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.QArticle;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource // rest repo 이용하기위해
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>, // 엔티티안에있는 모든 필드에 대해 기본 검색기능 추가
        QuerydslBinderCustomizer<QArticle> {

        Page<Article> findByTitle(String title, Pageable pageable);

    // Inteface에서 구현 안되나 이런식으로 가능
    @Override
    default void customize(QuerydslBindings bindings, QArticle root){
        // 검색 조건을 제한
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.content,root.hashtag, root.createdAt, root.createdBy);

        // 검색을 하나만 받음 : first((path,value)->path.eq(value));
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);  // 대소문자 구분x '%값%'
//        bindings.bind(root.title).first(StringExpression::likeIgnoreCase);  // 대소문자 구분x '값'
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);

    }
}