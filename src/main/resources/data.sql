-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        10.9.1-MariaDB - mariadb.org binary distribution
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- mini_board 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `mini_board` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `mini_board`;

-- 테이블 mini_board.tb_board 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_board` (
  `idx` int(11) NOT NULL COMMENT 'PK',
  `name` varchar(255) DEFAULT NULL COMMENT '게시판 명',
  PRIMARY KEY (`idx`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 테이블 데이터 mini_board.tb_board:~2 rows (대략적) 내보내기
DELETE FROM `tb_board`;
/*!40000 ALTER TABLE `tb_board` DISABLE KEYS */;
INSERT INTO `tb_board` (`idx`, `name`) VALUES
	(1, 'notice'),
	(10, 'community');
/*!40000 ALTER TABLE `tb_board` ENABLE KEYS */;

-- 테이블 mini_board.tb_post 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_post` (
  `idx` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `board_idx` int(11) NOT NULL COMMENT '게시판 분류 PK',
  `user_idx` int(11) NOT NULL COMMENT '작성자 PK',
  `title` varchar(255) DEFAULT NULL COMMENT '게시글 제목',
  `content` mediumtext DEFAULT NULL COMMENT '게시글 내용',
  `regdate` datetime DEFAULT NULL COMMENT '게시글 등록시간',
  `updatetime` datetime DEFAULT NULL COMMENT '게시글 수정시간',
  `hit` int(11) DEFAULT NULL COMMENT '게시글 조회수',
  PRIMARY KEY (`idx`) USING BTREE,
  KEY `FK_tb_post_tb_board` (`board_idx`),
  KEY `FK_tb_post_tb_user` (`user_idx`),
  CONSTRAINT `FK_tb_post_tb_board` FOREIGN KEY (`board_idx`) REFERENCES `tb_board` (`idx`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_tb_post_tb_user` FOREIGN KEY (`user_idx`) REFERENCES `tb_user` (`idx`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- 테이블 데이터 mini_board.tb_post:~0 rows (대략적) 내보내기
DELETE FROM `tb_post`;
/*!40000 ALTER TABLE `tb_post` DISABLE KEYS */;
INSERT INTO `tb_post` (`idx`, `board_idx`, `user_idx`, `title`, `content`, `regdate`, `updatetime`, `hit`) VALUES
	(1, 1, 1, '글쓰기 테스트', '테스트123', '2022-10-13 19:05:33', NULL, 0);
/*!40000 ALTER TABLE `tb_post` ENABLE KEYS */;

-- 테이블 mini_board.tb_post_file 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_post_file` (
  `idx` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `post_idx` int(11) NOT NULL COMMENT '게시판 PK',
  `board_idx` int(11) NOT NULL COMMENT '게시판 분류 PK',
  `originname` varchar(255) DEFAULT NULL COMMENT '업로드시 원래 파일명',
  `uuid` varchar(255) DEFAULT NULL COMMENT '업로드시 저장된 파일명',
  `size` int(11) DEFAULT NULL COMMENT '파일 크기',
  `type` varchar(10) DEFAULT NULL COMMENT '파일 확장자',
  `path` varchar(255) DEFAULT NULL COMMENT '업로드시 파일경로',
  PRIMARY KEY (`idx`) USING BTREE,
  KEY `FK_tb_post_file_tb_board_group` (`board_idx`) USING BTREE,
  KEY `FK_tb_post_file_tb_post` (`post_idx`),
  CONSTRAINT `FK_tb_post_file_tb_board` FOREIGN KEY (`board_idx`) REFERENCES `tb_board` (`idx`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_tb_post_file_tb_post` FOREIGN KEY (`post_idx`) REFERENCES `tb_post` (`idx`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 테이블 데이터 mini_board.tb_post_file:~0 rows (대략적) 내보내기
DELETE FROM `tb_post_file`;
/*!40000 ALTER TABLE `tb_post_file` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_post_file` ENABLE KEYS */;

-- 테이블 mini_board.tb_user 구조 내보내기
CREATE TABLE IF NOT EXISTS `tb_user` (
  `idx` int(11) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `id` varchar(255) NOT NULL COMMENT '회원 아이디',
  `level` int(11) DEFAULT NULL COMMENT '회원 권한레벨',
  `password` varchar(255) DEFAULT NULL COMMENT '회원 비밀번호',
  `email` varchar(255) DEFAULT NULL COMMENT '회원 이메일',
  `name` varchar(255) DEFAULT NULL COMMENT '회원 이름',
  `nickname` varchar(255) DEFAULT NULL COMMENT '회원 닉네임',
  `phone` varchar(255) DEFAULT NULL COMMENT '회원 연락처',
  `latest_login` datetime DEFAULT NULL COMMENT '최종 로그인 시간',
  `regdate` datetime DEFAULT NULL COMMENT '회원등록일',
  PRIMARY KEY (`idx`) USING BTREE,
  UNIQUE KEY `user_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;

-- 테이블 데이터 mini_board.tb_user:~7 rows (대략적) 내보내기
DELETE FROM `tb_user`;
/*!40000 ALTER TABLE `tb_user` DISABLE KEYS */;
INSERT INTO `tb_user` (`idx`, `id`, `level`, `password`, `email`, `name`, `nickname`, `phone`, `latest_login`, `regdate`) VALUES
	(1, 'admin', 1, '$2a$10$PJzLGO0c7L/AmB86hwmWUOLBE4nsAkApOntnuGJ3s6l4CRO.sxX1.', 'admin@develop.com', '관리자', '관리자', '042-123-4567', '2022-10-11 18:19:50', '2022-10-11 18:19:50'),
	(2, 'test123', 10, '$2a$10$PJzLGO0c7L/AmB86hwmWUOLBE4nsAkApOntnuGJ3s6l4CRO.sxX1.', 'test@gmail.com', '테스터', '테스터', '010-1234-5678', NULL, '2022-10-12 18:07:22'),
	(3, 'tester', 10, '$2a$10$PJzLGO0c7L/AmB86hwmWUOLBE4nsAkApOntnuGJ3s6l4CRO.sxX1.', 'test@gmail.com', '테스터', '테스터', '010-1234-5678', NULL, '2022-10-12 18:08:46'),
	(4, 'test111', 10, '$2a$10$PJzLGO0c7L/AmB86hwmWUOLBE4nsAkApOntnuGJ3s6l4CRO.sxX1.', 'test@gmail.com', '테스터', '테스터', '010-1234-5678', NULL, '2022-10-12 18:12:39'),
	(5, 'test1111', 10, '$2a$10$PJzLGO0c7L/AmB86hwmWUOLBE4nsAkApOntnuGJ3s6l4CRO.sxX1.', 'test@gmail.com', '테스터', '테스터', '010-1234-5678', NULL, '2022-10-12 18:21:11'),
	(7, 'test1234', 10, '$2a$10$PJzLGO0c7L/AmB86hwmWUOLBE4nsAkApOntnuGJ3s6l4CRO.sxX1.', 'test@gmail.com', '테스터', '테스터', '010-1234-5678', NULL, '2022-10-12 18:24:37'),
	(8, 'pwtester', 10, '$2a$10$PJzLGO0c7L/AmB86hwmWUOLBE4nsAkApOntnuGJ3s6l4CRO.sxX1.', 'test@gmail.com', '테스터', '테스터', '010-1234-5678', NULL, '2022-10-13 14:56:55'),
	(9, 'tdqwd1212', 10, '$2a$10$lfarQnSqps/cEZKdk7VMoOxtYLVnzNF6lwkHtiotN2xESYIYCyJ4.', 'test@gmail.com', '테스터', '테스터', '010-1234-5678', NULL, '2022-10-13 19:49:01');
/*!40000 ALTER TABLE `tb_user` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
