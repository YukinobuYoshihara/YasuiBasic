DROP TABLE yasui_user CASCADE CONSTRAINTS;
DROP TABLE item CASCADE CONSTRAINTS;
DROP TABLE stock CASCADE CONSTRAINTS;
DROP TABLE contents CASCADE CONSTRAINTS;
DROP TABLE orders CASCADE CONSTRAINTS;

create table yasui_user(
	user_id nchar(5) ,
	name nvarchar2(20),
	passwd nvarchar2(42),
	descript nvarchar2(42),
	role nvarchar2(30) not null,
	is_delete number(1),
	constraint pk_user primary key( user_id ),
	constraint uq_data1 unique( name ),
	constraint ck_userflag CHECK ( is_delete IN ('1', '0'))
);

create table item(
	item_id nchar(5) ,
	item_name nvarchar2(50) not null,
	imgurl nvarchar2(50),
	item_size nvarchar2(50),
	price number(8) not null,
	is_delete number(1) default '0',
	constraint pk_item primary key( item_id ),
	constraint ck_flag CHECK ( is_delete IN ('1', '0'))
);

create table stock(
	item_id nchar(5) ,
	stock_num number(8) not null ,
	is_delete number(1) default '0',
	constraint pk_stock primary key( item_id ),
	constraint ck_stocknum CHECK ( stock_num >= '0'),
	constraint ck_stockflag CHECK ( is_delete IN ('1', '0'))
);

create table contents (
  mid nvarchar2(20) NOT NULL,
  title nvarchar2(100) default NULL,
  keywd nvarchar2(50) default NULL,
  descript nvarchar2(100) default NULL,
  role nvarchar2(100) default NULL,
  skip number(1) default NULL,
  constraint pk_contents primary key(mid)
);

create table orders  (
  oid nvarchar2(25) NOT NULL,
  user_id nchar(5) NOT NULL,
  item_id nchar(5) NOT NULL,
  quantity number(8,0) default NULL,
  is_delivery number(1),
  order_date DATE DEFAULT SYSDATE,
  delivery_date DATE default NULL,
  constraint pk_orders primary key(oid,item_id),
  constraint ck_delivery CHECK ( is_delivery IN ('1', '0'))
);

commit;

INSERT INTO yasui_user VALUES ('A0001','admin','password',n'管理者','administrator',0);
INSERT INTO yasui_user VALUES ('C0001','customer1','password',n'香川真司','user',0);
INSERT INTO yasui_user VALUES ('C0002','customer2','password',n'本田圭佑','user',0);

INSERT INTO item VALUES ('00001',n'キッチンテーブル（茶）','http://localhost:8080/YasuiRLS/img/00001.jpg','100x60x70',19800,0);
INSERT INTO item VALUES ('00002',n'デスク（ブラウン）','http://localhost:8080/YasuiRLS/img/00002.jpg','100x60x70',123500,0);
INSERT INTO item VALUES ('00003',n'いす（青）','http://localhost:8080/YasuiRLS/img/00003.jpg','100x60x70',9800,0);
INSERT INTO item VALUES ('00004',n'ベッド','http://localhost:8080/YasuiRLS/img/00004.jpg','100x60x70',354800,0);
INSERT INTO item VALUES ('00005',n'ソファー','http://localhost:8080/YasuiRLS/img/00005.jpg','100x60x70',99999,0);

INSERT INTO stock VALUES ('00001',25,0);
INSERT INTO stock VALUES ('00002',25,0);
INSERT INTO stock VALUES ('00003',25,0);
INSERT INTO stock VALUES ('00004',25,0);
INSERT INTO stock VALUES ('00005',0,0);

INSERT INTO contents VALUES ('Login','P0001:ログイン','ログイン','ログイン処理を行います。','user',1);
INSERT INTO contents VALUES ('LoginError','ログインエラー','ログイン, エラー','ログインエラー画面です。','user',1);
INSERT INTO contents VALUES ('ListItem','P0002:商品一覧','商品一覧, メイン, トップ','通信販売システムのメインメニューです。','user',0);
INSERT INTO contents VALUES ('PurchaseConfirm','P0003:注文の確認','家具,注文,確認','注文の確認を行います','user',0);
INSERT INTO contents VALUES ('PurchaseStoreDb','P0004:注文の完了','家具,注文','注文を完了します','user',0);
INSERT INTO contents VALUES ('PurchaseComplete','P0004:注文の完了','家具,注文','注文を完了します','user',0);
INSERT INTO contents VALUES ('Logout','P0005:ログアウト','ログアウト','通信販売システムからのログアウト処理を行います。','user',0);
INSERT INTO contents VALUES ('AddItem','A0001:新規商品登録','新規, 商品, 登録','新規商品登録を行います。','administrator',0);
INSERT INTO contents VALUES ('AddItemConfirm','A0002:新規商品登録の確認','新規, 商品, 登録','新規商品登録の確認を行います','administrator',0);
INSERT INTO contents VALUES ('AddItemStoreDb','A0003:新規商品登録の完了','新規, 商品, 登録','新規商品登録を完了します','administrator',0);
INSERT INTO contents VALUES ('AddItemComplete','A0003:新規商品登録の完了','新規, 商品, 登録','新規商品登録を完了します','administrator',0);
INSERT INTO contents VALUES ('ChangeStock','A0004:在庫数量変更','在庫,数量,変更 商品, 登録','在庫数量の変更を行います。','administrator',0);
INSERT INTO contents VALUES ('ChangeStockConfirm','A0005:在庫数量変更の確認','在庫,数量,変更 商品, 登録','在庫数量変更の確認を行います','administrator',0);
INSERT INTO contents VALUES ('ChangeStockStoreDb','A0006:在庫数量変更の完了','在庫,数量,変更 商品, 登録','在庫数量変更を完了します','administrator',0);
INSERT INTO contents VALUES ('ChangeStockComplete','A0006:在庫数量変更の完了','在庫,数量,変更 商品, 登録','在庫数量変更を完了します','administrator',0);
INSERT INTO contents VALUES ('RemoveItem','A0007:商品削除','商品, 削除','商品の削除を行います。','administrator',0);
INSERT INTO contents VALUES ('RemoveItemConfirm','A0008:商品削除の確認','商品, 削除','商品削除の確認を行います','administrator',0);
INSERT INTO contents VALUES ('RemoveItemComplete','A0009:商品削除の完了','商品, 削除','商品削除を完了します','administrator',0);
INSERT INTO contents VALUES ('RemoveItemStoreDb','A0009:商品削除の完了','商品, 削除','商品削除を完了します','administrator',0);

commit;

CREATE SEQUENCE seq_item_id
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE SEQUENCE seq_oid
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

commit;