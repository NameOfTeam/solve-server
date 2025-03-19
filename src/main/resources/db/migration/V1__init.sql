CREATE TABLE users
(
    id             BINARY(16) PRIMARY KEY,
    username       VARCHAR(255) NOT NULL UNIQUE,
    email          VARCHAR(255) NOT NULL UNIQUE,
    password       VARCHAR(255) NOT NULL,
    introduction   TEXT,
    gender         VARCHAR(50)  NOT NULL DEFAULT '',
    birth          DATE,
    is_verified    BOOLEAN      NOT NULL DEFAULT FALSE,
    role           VARCHAR(50)  NOT NULL,
    tier           VARCHAR(50)  NOT NULL,
    rating         INT          NOT NULL DEFAULT 0,
    solved_count   INT          NOT NULL DEFAULT 0,
    streak         INT          NOT NULL DEFAULT 0,
    max_streak     INT          NOT NULL DEFAULT 0,
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_username (username),
    INDEX idx_user_email (email),
    INDEX idx_user_tier (tier),
    INDEX idx_user_role (role)
);

CREATE TABLE user_connections
(
    id      BINARY(16) PRIMARY KEY,
    user_id BINARY(16)   NOT NULL,
    type    VARCHAR(50)  NOT NULL,
    value   VARCHAR(255) NOT NULL,
    INDEX idx_user_connection_user (user_id),
    INDEX idx_user_connection_type (type),
    INDEX idx_user_connection_value (value),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE email_verifications
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    verification_token VARCHAR(255) NOT NULL UNIQUE,
    email              VARCHAR(255) NOT NULL UNIQUE,
    expired_at         TIMESTAMP    NOT NULL,
    is_verified        BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE badges
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    image_url   VARCHAR(255) NOT NULL,
    `condition`   TEXT         NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE user_badges
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BINARY(16) NOT NULL,
    badge_id   BIGINT     NOT NULL,
    created_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_badge_user (user_id),
    INDEX idx_user_badge_badge (badge_id),
    UNIQUE INDEX idx_user_badge_unique (user_id, badge_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (badge_id) REFERENCES badges (id) ON DELETE CASCADE
);

CREATE TABLE user_links
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BINARY(16)   NOT NULL,
    link       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_link_user (user_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE problems
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    content      TEXT         NOT NULL,
    input        TEXT         NOT NULL,
    output       TEXT         NOT NULL,
    memory_limit BIGINT       NOT NULL,
    time_limit   DOUBLE       NOT NULL,
    tier         VARCHAR(50)  NOT NULL,
    author_id    BINARY(16)   NOT NULL,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_problem_author (author_id),
    INDEX idx_problem_tier (tier),
    FOREIGN KEY (author_id) REFERENCES users (id)
);

CREATE TABLE problem_tags
(
    problem_id BIGINT      NOT NULL,
    tag        VARCHAR(50) NOT NULL,
    PRIMARY KEY (problem_id, tag),
    FOREIGN KEY (problem_id) REFERENCES problems (id) ON DELETE CASCADE
);

CREATE TABLE problem_test_cases
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    problem_id BIGINT    NOT NULL,
    input      TEXT      NOT NULL,
    output     TEXT      NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_problem_test_case_problem (problem_id),
    FOREIGN KEY (problem_id) REFERENCES problems (id) ON DELETE CASCADE
);

CREATE TABLE problem_examples
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    problem_id  BIGINT    NOT NULL,
    input       TEXT      NOT NULL,
    output      TEXT      NOT NULL,
    description TEXT,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_problem_example_problem (problem_id),
    FOREIGN KEY (problem_id) REFERENCES problems (id) ON DELETE CASCADE
);

CREATE TABLE problem_contributors
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    problem_id BIGINT     NOT NULL,
    user_id    BINARY(16) NOT NULL,
    UNIQUE INDEX idx_problem_contributor_unique (problem_id, user_id),
    INDEX idx_problem_contributor_user (user_id),
    INDEX idx_problem_contributor_problem (problem_id),
    FOREIGN KEY (problem_id) REFERENCES problems (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE problem_codes
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    problem_id BIGINT      NOT NULL,
    user_id    BINARY(16)  NOT NULL,
    code       TEXT        NOT NULL,
    language   VARCHAR(50) NOT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_problem_code_user (user_id),
    INDEX idx_problem_code_problem (problem_id),
    INDEX idx_problem_code_language (language),
    UNIQUE KEY uk_problem_code_user_language (problem_id, user_id, language),
    FOREIGN KEY (problem_id) REFERENCES problems (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE user_solved
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BINARY(16) NOT NULL,
    problem_id BIGINT     NOT NULL,
    date       DATE       NOT NULL,
    created_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_solved_user (user_id),
    INDEX idx_user_solved_problem (problem_id),
    INDEX idx_user_solved_date (date),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (problem_id) REFERENCES problems (id) ON DELETE CASCADE
);

CREATE TABLE user_frozen
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BINARY(16) NOT NULL,
    date    DATE       NOT NULL,
    INDEX idx_user_frozen_user (user_id),
    INDEX idx_user_frozen_date (date),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE submits
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    problem_id   BIGINT      NOT NULL,
    author_id    BINARY(16)  NOT NULL,
    code         TEXT        NOT NULL,
    language     VARCHAR(50) NOT NULL,
    state        VARCHAR(50) NOT NULL,
    memory_usage BIGINT,
    time_usage   BIGINT,
    visibility   VARCHAR(50) NOT NULL,
    created_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_submit_problem (problem_id),
    INDEX idx_submit_author (author_id),
    INDEX idx_submit_state (state),
    INDEX idx_submit_language (language),
    FOREIGN KEY (problem_id) REFERENCES problems (id),
    FOREIGN KEY (author_id) REFERENCES users (id)
);

CREATE TABLE runs
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    author_id  BINARY(16)  NOT NULL,
    code       TEXT        NOT NULL,
    language   VARCHAR(50) NOT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES users (id)
);

CREATE TABLE posts
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    content    LONGTEXT     NOT NULL,
    language   VARCHAR(50),
    category   VARCHAR(50)  NOT NULL,
    problem_id BIGINT,
    author_id  BINARY(16)   NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_post_author (author_id),
    INDEX idx_post_problem (problem_id),
    FOREIGN KEY (problem_id) REFERENCES problems (id),
    FOREIGN KEY (author_id) REFERENCES users (id)
);

CREATE TABLE post_likes
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id    BIGINT     NOT NULL,
    user_id    BINARY(16) NOT NULL,
    created_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE INDEX idx_post_like_unique (post_id, user_id),
    INDEX idx_post_like_user (user_id),
    INDEX idx_post_like_post (post_id),
    FOREIGN KEY (post_id) REFERENCES posts (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE post_comments
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id    BIGINT     NOT NULL,
    content    LONGTEXT   NOT NULL,
    author_id  BINARY(16) NOT NULL,
    created_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_post_comment_post (post_id),
    INDEX idx_post_comment_author (author_id),
    FOREIGN KEY (post_id) REFERENCES posts (id),
    FOREIGN KEY (author_id) REFERENCES users (id)
);

CREATE TABLE post_comment_likes
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_comment_id BIGINT     NOT NULL,
    user_id         BINARY(16) NOT NULL,
    UNIQUE INDEX idx_post_comment_like_unique (post_comment_id, user_id),
    INDEX idx_post_comment_like_user (user_id),
    INDEX idx_post_comment_like_comment (post_comment_id),
    FOREIGN KEY (post_comment_id) REFERENCES post_comments (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE post_comment_replies
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    content    LONGTEXT   NOT NULL,
    post_id    BIGINT     NOT NULL,
    author_id  BINARY(16) NOT NULL,
    comment_id BIGINT,
    reply_id   BIGINT,
    created_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_post_comment_reply_post (post_id),
    INDEX idx_post_comment_reply_author (author_id),
    FOREIGN KEY (post_id) REFERENCES posts (id),
    FOREIGN KEY (author_id) REFERENCES users (id),
    FOREIGN KEY (comment_id) REFERENCES post_comments (id),
    FOREIGN KEY (reply_id) REFERENCES post_comment_replies (id)
);

CREATE TABLE post_comment_reply_likes
(
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_comment_reply_id BIGINT     NOT NULL,
    user_id               BINARY(16) NOT NULL,
    UNIQUE INDEX idx_post_comment_reply_like_unique (post_comment_reply_id, user_id),
    INDEX idx_post_comment_reply_like_user (user_id),
    INDEX idx_post_comment_reply_like_comment (post_comment_reply_id),
    FOREIGN KEY (post_comment_reply_id) REFERENCES post_comment_replies (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE contests
(
    id                   BIGINT AUTO_INCREMENT PRIMARY KEY,
    title                VARCHAR(255) NOT NULL,
    description          TEXT         NOT NULL,
    start_time           TIMESTAMP    NOT NULL,
    end_time             TIMESTAMP    NOT NULL,
    owner_id             BINARY(16)   NOT NULL,
    winner_id            BINARY(16),
    is_public            BOOLEAN      NOT NULL DEFAULT FALSE,
    is_deleted           BOOLEAN      NOT NULL DEFAULT FALSE,
    is_registration_open BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users (id),
    FOREIGN KEY (winner_id) REFERENCES users (id)
);

CREATE TABLE contest_operators
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    contest_id BIGINT     NOT NULL,
    user_id    BINARY(16) NOT NULL,
    UNIQUE INDEX idx_contest_operator_unique (contest_id, user_id),
    FOREIGN KEY (contest_id) REFERENCES contests (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE contest_participants
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    contest_id        BIGINT     NOT NULL,
    user_id           BINARY(16) NOT NULL,
    registration_time TIMESTAMP  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE INDEX idx_contest_participant_unique (contest_id, user_id),
    FOREIGN KEY (contest_id) REFERENCES contests (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE contest_problems
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    contest_id BIGINT NOT NULL,
    problem_id BIGINT NOT NULL,
    `order`    INT    NOT NULL,
    score      INT    NOT NULL,
    UNIQUE INDEX idx_contest_problem_unique (contest_id, problem_id),
    FOREIGN KEY (contest_id) REFERENCES contests (id),
    FOREIGN KEY (problem_id) REFERENCES problems (id)
);

CREATE TABLE contest_announcements (
                                       id           BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       contest_id   BIGINT       NOT NULL,
                                       user_id      BINARY(16)   NOT NULL,
                                       title        VARCHAR(255) NOT NULL,
                                       content      TEXT         NOT NULL,
                                       important    BOOLEAN      NOT NULL,
                                       published_at DATETIME     NOT NULL,
                                       created_at   DATETIME     NOT NULL,
                                       updated_at   DATETIME     NOT NULL,
                                       CONSTRAINT fk_contest_announcements_contest FOREIGN KEY (contest_id) REFERENCES contests (id),
                                       CONSTRAINT fk_contest_announcements_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE contest_submissions
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    participant_id BIGINT      NOT NULL,
    problem_id     BIGINT      NOT NULL,
    code           TEXT        NOT NULL,
    language       VARCHAR(50) NOT NULL,
    result         VARCHAR(50),
    score          INT,
    FOREIGN KEY (participant_id) REFERENCES contest_participants (id),
    FOREIGN KEY (problem_id) REFERENCES contest_problems (id)
);

CREATE TABLE workbooks
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    author_id   BINARY(16)   NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_workbook_author (author_id),
    FOREIGN KEY (author_id) REFERENCES users (id)
);

CREATE TABLE workbook_problems
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    workbook_id BIGINT NOT NULL,
    problem_id  BIGINT NOT NULL,
    UNIQUE INDEX idx_workbook_problem_unique (workbook_id, problem_id),
    INDEX idx_workbook_problem_workbook (workbook_id),
    INDEX idx_workbook_problem_problem (problem_id),
    FOREIGN KEY (workbook_id) REFERENCES workbooks (id),
    FOREIGN KEY (problem_id) REFERENCES problems (id)
);

CREATE TABLE workbook_likes
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    workbook_id BIGINT     NOT NULL,
    user_id     BINARY(16) NOT NULL,
    UNIQUE INDEX idx_workbook_like_unique (workbook_id, user_id),
    INDEX idx_workbook_like_user (user_id),
    INDEX idx_workbook_like_workbook (workbook_id),
    FOREIGN KEY (workbook_id) REFERENCES workbooks (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE workbook_bookmarks
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    workbook_id BIGINT     NOT NULL,
    user_id     BINARY(16) NOT NULL,
    UNIQUE INDEX idx_workbook_bookmark_unique (workbook_id, user_id),
    INDEX idx_workbook_bookmark_user (user_id),
    INDEX idx_workbook_bookmark_workbook (workbook_id),
    FOREIGN KEY (workbook_id) REFERENCES workbooks (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE themes
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    name              VARCHAR(255) NOT NULL,
    description       TEXT         NOT NULL,
    thumbnail         VARCHAR(255) NOT NULL,
    background        VARCHAR(255) NOT NULL,
    background_border VARCHAR(255) NOT NULL,
    container         VARCHAR(255) NOT NULL,
    container_border  VARCHAR(255) NOT NULL,
    main              VARCHAR(255) NOT NULL,
    price             INT          NOT NULL,
    created_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);