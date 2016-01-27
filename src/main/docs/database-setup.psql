CREATE TABLE adminusers
(
  username character varying NOT NULL,
  name character varying,
  email character varying,
  CONSTRAINT adminusers_pkey PRIMARY KEY (username)
);


CREATE TABLE users
(
  username text NOT NULL,
  password text NOT NULL,
  enabled boolean NOT NULL,
  email text,
  PRIMARY KEY (username)
);


CREATE TABLE authorities
(
  username text NOT NULL,
  authority text NOT NULL,
  CONSTRAINT fk_authorities_users FOREIGN KEY (username)
      REFERENCES users (username) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT,
  UNIQUE(username)
);


CREATE TABLE bookmarks
(
  username text NOT NULL,
  itemid text NOT NULL,
  page bigint NOT NULL,
  thumbnailurl text NOT NULL,
  dateadded timestamp with time zone,
  PRIMARY KEY (username, itemid, page),
  FOREIGN KEY (username)
      REFERENCES users (username) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT
);
CREATE INDEX ON bookmarks (itemid);


CREATE TABLE collections
(
  collectionid text NOT NULL,
  title text NOT NULL,
  summaryurl text NOT NULL,
  sponsorsurl text NOT NULL,
  type text NOT NULL,
  collectionorder bigint,
  parentcollectionid text,
  PRIMARY KEY (collectionid)
);


CREATE TABLE items
(
  itemid text NOT NULL,
  taggingstatus boolean NOT NULL DEFAULT false,
  PRIMARY KEY (itemid)
);


CREATE TABLE itemsincollection
(
  itemid text NOT NULL,
  collectionid text NOT NULL,
  visible boolean NOT NULL,
  itemorder bigint NOT NULL,
  PRIMARY KEY (itemid, collectionid),
  FOREIGN KEY (collectionid)
      REFERENCES collections (collectionid) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT,
  FOREIGN KEY (itemid)
      REFERENCES items (itemid) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT
);
CREATE INDEX ON itemsincollection (collectionid);


CREATE TABLE persistent_logins
(
  username text NOT NULL,
  series text NOT NULL,
  token text NOT NULL,
  last_used timestamp with time zone NOT NULL DEFAULT now(),
  PRIMARY KEY (series)
);



CREATE TABLE "DocumentTags"
(
  id serial NOT NULL,
  "docId" character varying(64),
  tags json,
  CONSTRAINT "DocumentTags_pkey" PRIMARY KEY (id),
  CONSTRAINT "DocumentTags_unique" UNIQUE ("docId")
);

CREATE TABLE "DocumentRemovedTags"
(
  id serial NOT NULL,
  oid character varying(128),
  "docId" character varying(64),
  removedtags json,
  CONSTRAINT "DocumentRemovedTags_pkey" PRIMARY KEY (id),
  CONSTRAINT "DocumentRemovedTags_unique" UNIQUE (oid, "docId")
);

CREATE TABLE "DocumentAnnotations"
(
  id serial NOT NULL,
  oid character varying(128),
  "docId" character varying(64),
  annos json,
  CONSTRAINT "DocumentAnnotations_pkey" PRIMARY KEY (id),
  CONSTRAINT "DocumentAnnotations_unique" UNIQUE (oid, "docId")
);
