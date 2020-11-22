CREATE TABLE if not exists `user`
(
    `id`       int(11) NOT NULL AUTO_INCREMENT,
    `name`     varchar(64) DEFAULT NULL,
    `password` varchar(64) DEFAULT NULL,
    `email`    varchar(64) DEFAULT NULL,
    `role`     tinyint(1)  DEFAULT NULL,
    PRIMARY KEY (`id`)
);




