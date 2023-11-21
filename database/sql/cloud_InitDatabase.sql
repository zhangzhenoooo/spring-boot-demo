-- 192.168.2.20  zz_test 中的新脚本。
-- mysql 
-- 日期：2023年11月7日
-- 
-- 时间：上午10:44:08
drop table if exists cloud_bs_product ;

CREATE TABLE  cloud_bs_product
(
    pid    varchar(32) PRIMARY KEY NOT NULL COMMENT '主键ID',
    pname  varchar(255)            NOT NULL COMMENT '商品名称',
    pprice decimal                 NOT NULL COMMENT '价格',
    stock  int                     NOT NULL COMMENT '库存',
    remark varchar(500)            NOT NULL COMMENT '备注'
);
ALTER TABLE cloud_bs_product COMMENT='商品表';


drop table if exists cloud_bs_order;

CREATE TABLE cloud_bs_order
(
    oid      varchar(32) PRIMARY KEY NOT NULL COMMENT '订单ID',
    uid      varchar(255)            NOT NULL COMMENT '用户ID',
    username decimal                 NOT NULL COMMENT '用户名',
    pid      varchar(100)            NOT NULL COMMENT '商品ID',
    pname    varchar(500)            NOT NULL COMMENT '商品名称',
    pprice   decimal                 NOT NULL COMMENT '商品价格',
    number   int                     NOT NULL COMMENT '数量'
);
ALTER TABLE cloud_bs_order COMMENT='订单表';

drop table if exists cloud_bs_user;

CREATE TABLE cloud_bs_user
(
    uid      varchar(32) PRIMARY KEY NOT NULL COMMENT '用户ID',
    username decimal                 NOT NULL COMMENT '用户名',
    password varchar(100)            NOT NULL COMMENT '密码',
    status   tinyint                 NOT NULL default 1 COMMENT '状态'
);
ALTER TABLE cloud_bs_user COMMENT='用户表';
