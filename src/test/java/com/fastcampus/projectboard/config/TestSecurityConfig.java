package com.fastcampus.projectboard.config;

import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
public class TestSecurityConfig {
    @MockBean private UserAccountRepository userAccountRepository;

    @BeforeTestMethod   // 각 테스트가 수행되기전에 아래 매소드가 실행되서 인증정도 들어갈수 있게
    public void securitySetUp(){
        given(userAccountRepository.findById(anyString())).willReturn(Optional.of(UserAccount.of(
            "test",
            "pw",
            "test@email.com",
            "test",
            "test-memo"
        )));
    }
}
