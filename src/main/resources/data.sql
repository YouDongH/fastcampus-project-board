-- 테스트 계정
-- TODO: 테스트용이지만 비밀번호가 노출된 데이터 세팅. 개선하는 것이 좋을 지 고민해 보자.
 insert into user_account (user_id, user_password, nickname, email, memo, created_at, created_by, modified_at, modified_by) values
     ('dong', '1234', 'dong', 'dong@mail.com', 'I am Dong.', now(), 'dong', now(), 'dong')
 ;
 insert into user_account (user_id, user_password, nickname, email, memo, created_at, created_by, modified_at, modified_by) values
     ('dong2', '1234', 'Dong2', 'dong2@mail.com', 'I am Dong2.', now(), 'dong2', now(), 'dong2')
 ;
 insert into user_account (user_id, user_password, nickname, email, memo, created_at, created_by, modified_at, modified_by) values
     ('don3', '1234', 'Dong2', 'dong3@mail.com', 'I am Dong3.', now(), 'dong3', now(), 'dong3')
;

-- delete from article_comment;
-- delete from article;
