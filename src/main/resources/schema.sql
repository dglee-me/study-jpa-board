CREATE TABLE IF NOT EXISTS `USERS`
(
    `id`         BIGINT,
    `email`      VARCHAR(255),
    `user_name`  VARCHAR(255),
    `password`   VARCHAR(255),
    `created_at` DATETIME(6),

    PRIMARY KEY (`id`)
);