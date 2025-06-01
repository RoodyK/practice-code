create table board (
	board_id BIGINT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(50) NOT NULL,
	created_at TIMESTAMP,
	modified_at TIMESTAMP
);

create table post (
	post_id BIGINT PRIMARY KEY AUTO_INCREMENT,
	title VARCHAR(50) NOT NULL,
	content VARCHAR(1000),
	views BIGINT,
	post_type VARCHAR(20),
	board_id BIGINT,
	created_at TIMESTAMP,
	modified_at TIMESTAMP,
);

-- board 데이터 셋업
INSERT INTO board (name, created_at, modified_at)
VALUES ('일상 게시판', NOW(), NOW());


-- post 데이터 셋업
DELIMITER //
CREATE PROCEDURE insert_bulk_posts()
BEGIN
  DECLARE i BIGINT DEFAULT 1;
  DECLARE boardId INT;
  DECLARE types TEXT DEFAULT 'NORMAL,QUESTION,SHARE,INFORMATION,ETC';
  DECLARE postType VARCHAR(12);

  START TRANSACTION;

  WHILE i <= 12000000 DO
	SET boardId = 1;
    SET postType = ELT(1 + FLOOR(RAND() * 5), 'NORMAL', 'QUESTION', 'SHARE', 'INFORMATION', 'ETC');

    INSERT INTO post (
      title, content, post_type, views, board_id, created_at, modified_at
    ) VALUES (
      CONCAT('제목 ', i),
      CONCAT('내용 ', i),
      postType,
      FLOOR(RAND() * 10000),
      boardId,
      NOW(),
      NOW()
    );

    IF MOD(i, 1000000) = 0 THEN
      COMMIT;
      START TRANSACTION;
    END IF;

    SET i = i + 1;
  END WHILE;

  COMMIT;
END //

DELIMITER ;

drop procedure insert_bulk_posts;

-- 실행
CALL insert_bulk_posts();