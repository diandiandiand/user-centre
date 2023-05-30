-- auto-generated definition
create table user
(
    id           bigint auto_increment
        primary key,
    userName     varchar(56)                        null,
    userAccount  varchar(56)                        null,
    avatarUrl    varchar(1024)                      null comment '头像地址',
    gender       tinyint                            null,
    userPassword varchar(56)                        not null,
    phone        varchar(128)                       null,
    email        varchar(512)                       null,
    userStatus   int      default 0                 null comment '0:正常',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     int      default 0                 not null,
    userRole     int      default 0                 not null comment '用户角色   0-普通用户  1-管理员'
);

