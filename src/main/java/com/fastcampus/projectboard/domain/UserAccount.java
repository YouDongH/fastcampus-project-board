package com.fastcampus.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "userId"),
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class UserAccount extends AuditingFields {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//        private Long id;
    // 애초에 유저에 숫자로 들어갈 이유가 없었음
    @Id
    @Column(length = 50)
    private String userId;

//    @Setter @Column(nullable = false, length = 50) private String userId;
    @Setter @Column(nullable = false) private String userPassword;

    @Setter @Column(length = 100) private String email;
    @Setter @Column(length = 100) private String nickname;
    @Setter private String memo;


    protected UserAccount() {}

    private UserAccount(String userId, String userPassword, String email, String nickname, String memo) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
    }

    public static UserAccount of(String userId, String userPassword, String email, String nickname, String memo) {
        return new UserAccount(userId, userPassword, email, nickname, memo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount userAccount)) return false;
//        return id != null && id.equals(userAccount.id);
        // 기본키 변경으로 인해 영향 
        return userId != null && userId.equals(userAccount.userId);
    }

    @Override
    public int hashCode() {
//        return Objects.hash(id);
        return Objects.hash(userId);
    }

}