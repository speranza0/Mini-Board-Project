# SpringBoot-Project-MiniBoard
스프링 부트 + 타임리프 게시판

## 🖥️ 프로젝트 소개
게시글 등록 수정 삭제 (CRUD)를 구현한 MVC 구조의 게시판 프로젝트입니다.

## 🕰️ 개발 기간
22.10.12일 - 22.11.08일

### ⚙️ 개발 환경
- `Java 11`
- `JDK 11.0.15`
- **IDE** : IntelliJ
- **Framework** : Springboot(2.x)
- **Database** : MariaDB
- **ORM** : Mybatis

## 📌 주요 기능
#### 로그인
- 로그인 시 세션(Session) 생성
- 관리자 계정(테스트용) 로그인
- 일반회원 계정(테스트용) 로그인
#### 회원가입
- 필수 입력값 체크
- 이용약관 및 개인정보처리방침에 동의 체크박스 체크 여부
#### 공지사항
- 관리자 계정만 등록, 수정, 삭제 가능
- 일반 회원 계정은 공지사항 게시판의 게시글 검색, 조회 가능
#### 커뮤니티
- 일반 회원 계정 등록 가능
- 등록된 게시글의 수정, 삭제는 글 작성한 회원 가능
- 관리자 계정은 커뮤니티 모든 게시글 등록, 수정, 삭제 가능
- 모든 사용자 커뮤니티 게시판의 게시글 검색, 조회 가능
