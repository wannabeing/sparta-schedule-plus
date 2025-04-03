USE schedulePlus;

-- 유저 테이블
CREATE TABLE user (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      email VARCHAR(255) NOT NULL UNIQUE,
                      password VARCHAR(200) NOT NULL,
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 일정 테이블
CREATE TABLE schedule (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          contents LONGTEXT NOT NULL,
                          user_id BIGINT NOT NULL,  -- 외래 키
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE  -- user 삭제 시 schedule도 삭제
);

-- 댓글 테이블
CREATE TABLE comment (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         comment TEXT NOT NULL,
                         user_id BIGINT NOT NULL,  -- 외래 키
                         schedule_id BIGINT NOT NULL,  -- 외래 키
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                         FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,  -- user 삭제 시 comment도 삭제
                         FOREIGN KEY (schedule_id) REFERENCES schedule(id) ON DELETE CASCADE  -- schedule 삭제 시 comment도 삭제
);
