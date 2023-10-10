# BoardPj
## DDL
- 회원
```
CREATE TABLE user_tb (
  id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
  username varchar(20) not null,
  password varchar(60) not null,
  email varchar(20) not null,
  nickName varchar(20) not null,
  roles varchar(255),
  created_at timestamp not null default now(),
  primary key (id)
 );
```
- 게시판
```
-- 새싹 GENERAL, 우수 VIP
-- hide_flag: 0 보임, 1 숨김

CREATE TABLE board (
	id bigint primary key auto_increment,
	title varchar(100) not null,                     "게시글 제목"
	content varchar(1000) not null,                  "게시글 내용"
	thumbnail varchar(300),                          "썸네일 여부에 따라 null 가능"
	catagory varchar(50) default "GENERAL" not null, "새싹 회원 : GENERAL, 우수 회원 : VIP"
	created_at timestamp not null default  now(),    "게시글 생성일"
  updated_at timestamp default  now(),             "게시글 수정일"
  hide_flag tinyint not null default 0,            "게시글 숨김 여부"
	user_id bigint not null,
	foreign key(user_id) references user(id),
);
```
- 댓글
```
-- 깊이(depth)은 댓글과 대댓글 들을 구성하기 위한 계층입니다. depth  0은 부모(댓글) depth 1은 자식(대댓글) 형태
-- 그룹 id는(group_id)는 **부모** **댓글의 인덱스가 들어갑니다**.
CREATE TABLE comment (
	id bigint primary key auto_increment,
	content varchar(50) not null,                   "댓글 내용"
  depth tinyint not null default 0,               "depth 0은 부모(댓글), depth  1은 자식(대댓글) 형태"
  group_id bigint,                      "대댓글일 경우 부모의 댓글 인덱스를 가짐"
  created_at timestamp not null default now(),
	board_id bigint not null,
	user_id bigint not null,
	foreign key(user_id) references user(id),
	foreign key(board_id) references board(id)
);

```
- 게시글신고
```
-- report_type : 0 욕설, 1 음란, 2 비방
CREATE TABLE report_board(
	id bigint primary key auto_increment,
	board_id bigint not null,
  report_type tinyint not null,    "신고 분류"
  reporter_id bigint not null,     "신고자 ID"
  image_url varchar(300) not null, "신고 증거 사진"
  created_at timestamp not null default now(),
  foreign key(board_id) references board(id),
  foreign key(reporter_id) references user(id)
);
```


## 게시판
1. 게시판 쓰기/상세보기/삭제하기/수정하기
2. 게시판 카테고리/목록보기/페이징/검색/신고
3. 스케줄러 등록
4. 관리자
5. 댓글 쓰기/삭제

## 회원
1. 회원가입
   - (id, username, password, email, nickName, role, createdAt, updatedAt)
   - role은 새싹 회원과 우수회원으로 구분 (디폴트 : 새싹회원, 게시글 수 10 개)
   - 동일 username 중복체크하기
2. 로그인
   - (username, password)
3. 회원 정보/수정/비밀번호 수정
   - 회원정보 username, email, role, createdAt 확인
   - 회원수정 email, nickName 변경가능

## 게시글 카테고리
- 새싹회원 게시판, 우수회원 게시판
- (게시판은 2개이지만 하나의 화면을 공유해서 사용하고 카테고리로 구분)
1. 게시글 쓰기
   - 권한(새싹,우수)에 따라 다른 게시판에 글이 적어짐
2. 게시글 목록보기
   - id, title, content, thumbnail, user의 nickName 화면에 보여야 함,
   - content내용을 화면에 2줄이 넘어가면 Ellipsis(...)으로 스타일 변경,
   - 정렬은 id순 Desc
3. 게시글 페이징
   - 페이지당 6개 게시글 보여야 함
   - 게시글은 Grid 형식으로 3개씩 카드(Card) 배치
4. 게시글검색
   - 작성자(nickName), 제목(title), 내용(content)로 검색가능해야 함
5. 게시글 상세보기
   -  id, title, content, nickName, 댓글의 comment 리스트(id, comment, 댓글의 작성자 nickName) 이 화면에 보여야 함
   -  게시글 삭제버튼과 수정버 튼 보여야 함(본인이 적은 글에 대해서만)
   -  댓글 삭제버튼이 보여야함(본인이 적은 댓글에 대해서만)
6. 게시글 삭제하기
   - 본인이 적은 게시글만 삭제가능
7. 게시글 수정하기
   - title, content 수정 가능
8. 게시글 신고하기
   - 게시글 신고가능 (형태 : 욕설, 음란, 비방)
9. 댓글쓰기
   - 댓글 쓰기 50자이내!
   - 댓글에 댓글을 작성할 수 있음
   - 대댓글 기능 구현 (depth 1까지)
10. 댓글삭제
   - 댓글 삭제가능 (댓글은 수정은 없음)

## 스케줄러 등록
@Schedule 을 사용하여, 1분에 한번씩 게시글 수가 10개인데, 우수회원이 아닌 새싹회원 등급 자동 변경

## 관리자 회원 권한 관리
1. 회원의 role 변경 가능해야 함
2. 관리자 회원 Email 전송 관리
3. 회원에게 email 전송 가능해야 함
4. 관리자 게시글 CRUD 관리
5. 게시글 목록보기, 삭제하기, 숨기기/보이기, 블랙리스트(욕설) 등록 가능해야 함 (수정, 상세보기는 구현할 필요 없음)
6. 관리자 게시글 통계 관리
7. 유저의 게시글 수, 댓글 수를 볼 수 있고, 댓글수가 많은 유저 순, 게시글수가 많은 유저 순으로 정렬가능해야 함
8. 관리자 블랙리스트 고객 등록/해제
9. 게시글 신고목록 페이지 구현, 해당 페이지에서 블랙리스트 고객 등록 및 해제 가능
