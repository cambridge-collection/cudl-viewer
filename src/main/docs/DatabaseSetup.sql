CREATE TABLE collections
(
collectionid varchar(255) NOT NULL,
title varchar(255) NOT NULL,
summaryurl varchar(255) NOT NULL,
sponsorsurl varchar(255) NOT NULL,
type varchar(255) NOT NULL,
collectionorder int UNIQUE NOT NULL,
parentcollectionid varchar (255),
PRIMARY KEY (collectionid)
);

CREATE TABLE items
(
itemid varchar(255) NOT NULL
PRIMARY KEY (itemid)
);


CREATE TABLE itemsincollection
(
itemid varchar(255) NOT NULL,
collectionid varchar(255) NOT NULL,
visible boolean NOT NULL,
itemorder int NOT NULL,
PRIMARY KEY (itemid,collectionid),
FOREIGN KEY (itemid) REFERENCES items(itemid),
FOREIGN KEY (collectionid) REFERENCES collections(collectionid)
);


CREATE TABLE bookmarks
(
userid varchar(255) NOT NULL,
itemid varchar(255) NOT NULL,
page int NOT NULL,
PRIMARY KEY (userid,itemid,page),
FOREIGN KEY (itemid) REFERENCES items(itemid)
);

create table users(
      username varchar(250) not null primary key,
      password varchar(250) not null,
      enabled boolean not null);

create table authorities (
      username varchar(250) not null,
      authority varchar(250) not null,
      constraint fk_authorities_users foreign key(username) references users(username));
      create unique index ix_auth_username on authorities (username,authority);
      
create table persistent_logins (
  username varchar(250) not null,
  series varchar(250) primary key,
  token varchar(250) not null,
  last_used timestamp not null);