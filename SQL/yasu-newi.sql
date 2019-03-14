DROP TABLE yasui_user CASCADE CONSTRAINTS;
DROP TABLE item CASCADE CONSTRAINTS;
DROP TABLE stock CASCADE CONSTRAINTS;
DROP TABLE contents CASCADE CONSTRAINTS;
DROP TABLE orders CASCADE CONSTRAINTS;

DROP SEQUENCE seq_item_id;
DROP SEQUENCE seq_oid;

commit;

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

INSERT INTO yasui_user VALUES ('A0001','admin','password',n'�Ǘ���','administrator',0);
INSERT INTO yasui_user VALUES ('C0001','customer1','password',n'����^�i','user',0);
INSERT INTO yasui_user VALUES ('C0002','customer2','password',n'�{�c�\�C','user',0);

INSERT INTO item VALUES ('00001',n'�L�b�`���e�[�u���i���j','http://localhost:8080/YasuiRLS/img/00001.jpg','100x60x70',19800,0);
INSERT INTO item VALUES ('00002',n'�f�X�N�i�u���E���j','http://localhost:8080/YasuiRLS/img/00002.jpg','100x60x70',123500,0);
INSERT INTO item VALUES ('00003',n'�����i�j','http://localhost:8080/YasuiRLS/img/00003.jpg','100x60x70',9800,0);
INSERT INTO item VALUES ('00004',n'�x�b�h','http://localhost:8080/YasuiRLS/img/00004.jpg','100x60x70',354800,0);
INSERT INTO item VALUES ('00005',n'�\�t�@�[','http://localhost:8080/YasuiRLS/img/00005.jpg','100x60x70',99999,0);

INSERT INTO stock VALUES ('00001',25,0);
INSERT INTO stock VALUES ('00002',25,0);
INSERT INTO stock VALUES ('00003',25,0);
INSERT INTO stock VALUES ('00004',25,0);
INSERT INTO stock VALUES ('00005',0,0);

INSERT INTO contents VALUES ('Login','P0001:���O�C��','���O�C��','���O�C���������s���܂��B','user',1);
INSERT INTO contents VALUES ('LoginError','���O�C���G���[','���O�C��, �G���[','���O�C���G���[��ʂł��B','user',1);
INSERT INTO contents VALUES ('ListItem','P0002:���i�ꗗ','���i�ꗗ, ���C��, �g�b�v','�ʐM�̔��V�X�e���̃��C�����j���[�ł��B','user',0);
INSERT INTO contents VALUES ('PurchaseConfirm','P0003:�����̊m�F','�Ƌ�,����,�m�F','�����̊m�F���s���܂�','user',0);
INSERT INTO contents VALUES ('PurchaseStoreDb','P0004:�����̊���','�Ƌ�,����','�������������܂�','user',0);
INSERT INTO contents VALUES ('PurchaseComplete','P0004:�����̊���','�Ƌ�,����','�������������܂�','user',0);
INSERT INTO contents VALUES ('Logout','P0005:���O�A�E�g','���O�A�E�g','�ʐM�̔��V�X�e������̃��O�A�E�g�������s���܂��B','user',0);
INSERT INTO contents VALUES ('AddItem','A0001:�V�K���i�o�^','�V�K, ���i, �o�^','�V�K���i�o�^���s���܂��B','administrator',0);
INSERT INTO contents VALUES ('AddItemConfirm','A0002:�V�K���i�o�^�̊m�F','�V�K, ���i, �o�^','�V�K���i�o�^�̊m�F���s���܂�','administrator',0);
INSERT INTO contents VALUES ('AddItemStoreDb','A0003:�V�K���i�o�^�̊���','�V�K, ���i, �o�^','�V�K���i�o�^���������܂�','administrator',0);
INSERT INTO contents VALUES ('AddItemComplete','A0003:�V�K���i�o�^�̊���','�V�K, ���i, �o�^','�V�K���i�o�^���������܂�','administrator',0);
INSERT INTO contents VALUES ('ChangeStock','A0004:�݌ɐ��ʕύX','�݌�,����,�ύX ���i, �o�^','�݌ɐ��ʂ̕ύX���s���܂��B','administrator',0);
INSERT INTO contents VALUES ('ChangeStockConfirm','A0005:�݌ɐ��ʕύX�̊m�F','�݌�,����,�ύX ���i, �o�^','�݌ɐ��ʕύX�̊m�F���s���܂�','administrator',0);
INSERT INTO contents VALUES ('ChangeStockStoreDb','A0006:�݌ɐ��ʕύX�̊���','�݌�,����,�ύX ���i, �o�^','�݌ɐ��ʕύX���������܂�','administrator',0);
INSERT INTO contents VALUES ('ChangeStockComplete','A0006:�݌ɐ��ʕύX�̊���','�݌�,����,�ύX ���i, �o�^','�݌ɐ��ʕύX���������܂�','administrator',0);
INSERT INTO contents VALUES ('RemoveItem','A0007:���i�폜','���i, �폜','���i�̍폜���s���܂��B','administrator',0);
INSERT INTO contents VALUES ('RemoveItemConfirm','A0008:���i�폜�̊m�F','���i, �폜','���i�폜�̊m�F���s���܂�','administrator',0);
INSERT INTO contents VALUES ('RemoveItemComplete','A0009:���i�폜�̊���','���i, �폜','���i�폜���������܂�','administrator',0);
INSERT INTO contents VALUES ('RemoveItemStoreDb','A0009:���i�폜�̊���','���i, �폜','���i�폜���������܂�','administrator',0);

commit;

CREATE SEQUENCE seq_item_id
START WITH 6
INCREMENT BY 1
MINVALUE 6
MAXVALUE 99999
NOCYCLE
CACHE 10;

CREATE SEQUENCE seq_oid
START WITH 1
INCREMENT BY 1
MINVALUE 1
MAXVALUE 99999999
CYCLE
CACHE 10;

commit;