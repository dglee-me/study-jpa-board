CREATE TABLE IF NOT EXISTS `USERS`
(
    `id`         BIGINT,
    `email`      VARCHAR(255),
    `user_name`  VARCHAR(255),
    `role`  VARCHAR(50) CHECK (`role` IN ('ADMIN_ROLE', 'USER_ROLE')) ,
    `password`   VARCHAR(255),
    `created_at` DATETIME(6),

    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_email` (`email`)
);