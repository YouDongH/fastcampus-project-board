package com.fastcampus.projectboard.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("비즈니스 로직 - 페이지")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = void.class)
class PaginationServiceTest {

    private final PaginationService sut;

    public PaginationServiceTest(@Autowired PaginationService sut) {
        this.sut = sut;
    }

    @DisplayName("현재 페이지 번호와 총 페이지 수를 주면, 페이징 바 리스트를 만들어준다")
    @MethodSource               // 메소드를 이용해서 파라미터값을 여러번 입력하게
    @ParameterizedTest(name = "[{index}] {0},{1} -> {2}")          // 파라미터 테스트
    void givenCurrentPageNumberAndTotalPages_whenCalculating_thenReturnPaginationBarNumbers(
            int currentPageNumber, int totalPages, List<Integer> expected
    ){


        List<Integer> actual =sut.getPaginationBarNumbers(currentPageNumber, totalPages);

        assertThat(actual).isEqualTo(expected);

    }
    // 입력값을 넣어주는 메세지는 테스트 메세지랑 이름을 만들면 인식함
    static Stream<Arguments> givenCurrentPageNumberAndTotalPages_whenCalculating_thenReturnPaginationBarNumbers(){
        return Stream.of(
                arguments(0,13,List.of(0,1,2,3,4)),
                arguments(1,13,List.of(0,1,2,3,4)),
                arguments(2,13,List.of(0,1,2,3,4)),
                arguments(3,13,List.of(1,2,3,4,5)),
                arguments(4,13,List.of(2,3,4,5,6)),
                arguments(5,13,List.of(3,4,5,6,7)),
                arguments(9,13,List.of(7,8,9,10,11)),
                arguments(10,13,List.of(8,9,10,11,12)),
                arguments(11,13,List.of(9,10,11,12)),
                arguments(12,13,List.of(10,11,12))


        );
    }

    @DisplayName("현재 설정 되어 있는 페이지네이션 바의 길이를 알려준다")
    @Test
    void givenNothing_whenCalling_thenReturnCurrentBarLength(){


        int barLength = sut.currentBarLength();

        assertThat(barLength).isEqualTo(5);

    }


}