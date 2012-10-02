CREATE TABLE collections
(
collectionid varchar(255) NOT NULL,
title varchar(255) NOT NULL,
summaryurl varchar(255) NOT NULL,
sponsorsurl varchar(255) NOT NULL,
type varchar(255) NOT NULL,
collectionorder int NOT NULL UNIQUE AUTO_INCREMENT,
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