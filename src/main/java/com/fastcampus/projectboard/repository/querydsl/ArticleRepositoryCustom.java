package com.fastcampus.projectboard.repository.querydsl;

import java.util.List;

public interface ArticleRepositoryCustom {
    // 도메인아니므로 queryDsl이용
    List<String> findAllDistinctHashtags();

}
