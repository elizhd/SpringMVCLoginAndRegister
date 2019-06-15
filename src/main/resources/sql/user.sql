CREATE TABLE if not exists `user`
(
    `id`       int(11) NOT NULL AUTO_INCREMENT,
    `name`     varchar(64) DEFAULT NULL,
    `password` varchar(64) DEFAULT NULL,
    `email`    varchar(64) DEFAULT NULL,
    `role`     tinyint(1)  DEFAULT NULL,
    PRIMARY KEY (`id`)
);

insert into user(name, password, email, role) values('admin','admin','admin',true);
insert into user(name, password, email, role) values('test','test','test',false);
insert into user(name, password, email, role) values('test2','test2','test2',false);
insert into user(name, password, email, role) values('test3','test3','test3',false);


