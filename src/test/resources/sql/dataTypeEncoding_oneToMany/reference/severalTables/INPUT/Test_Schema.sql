CREATE TABLE codelist (

   name text NOT NULL PRIMARY KEY,
   documentation text
);

CREATE TABLE featuretype1 (

   _id bigserial NOT NULL PRIMARY KEY,
   attribute3 text NOT NULL
);

CREATE TABLE featuretype1_attribute1 (

   _id bigserial NOT NULL PRIMARY KEY,
   attribute4 text NOT NULL,
   attribute5 integer NOT NULL,
   attribute15 text NOT NULL,
   attribute17 text NOT NULL,
   featuretype1_id bigserial NOT NULL
);

CREATE TABLE featuretype1_attribute1_attribute11 (

   _id bigserial NOT NULL PRIMARY KEY,
   attribute9 integer NOT NULL,
   rolemodttoft4 bigserial NOT NULL,
   mydatatype_id bigserial NOT NULL
);

CREATE TABLE featuretype1_attribute1_attribute12 (

   _id bigserial NOT NULL PRIMARY KEY,
   attribute9 integer NOT NULL,
   rolemodttoft4 bigserial NOT NULL,
   mydatatype_id bigserial NOT NULL
);

CREATE TABLE featuretype1_attribute1_attribute14 (

   mydatatype_id bigserial NOT NULL,
   attribute14 text NOT NULL,
   PRIMARY KEY (mydatatype_id, attribute14)
);

CREATE TABLE featuretype1_attribute1_attribute16 (

   mydatatype_id bigserial NOT NULL,
   codelist_id text NOT NULL,
   PRIMARY KEY (mydatatype_id, codelist_id)
);

CREATE TABLE featuretype1_attribute2 (

   featuretype1_id bigserial NOT NULL,
   attribute2 text NOT NULL,
   PRIMARY KEY (featuretype1_id, attribute2)
);

CREATE TABLE featuretype2 (

   _id bigserial NOT NULL PRIMARY KEY
);

CREATE TABLE featuretype2_attribute13 (

   _id bigserial NOT NULL PRIMARY KEY,
   attribute9 integer NOT NULL,
   rolemodttoft4 bigserial NOT NULL,
   featuretype2_id bigserial NOT NULL
);

CREATE TABLE featuretype2_attribute6 (

   _id bigserial NOT NULL PRIMARY KEY,
   attribute4 text NOT NULL,
   attribute5 integer NOT NULL,
   attribute15 text NOT NULL,
   attribute17 text NOT NULL,
   featuretype2_id bigserial NOT NULL
);

CREATE TABLE featuretype2_attribute6_attribute11 (

   _id bigserial NOT NULL PRIMARY KEY,
   attribute9 integer NOT NULL,
   rolemodttoft4 bigserial NOT NULL,
   mydatatype_id bigserial NOT NULL
);

CREATE TABLE featuretype2_attribute6_attribute12 (

   _id bigserial NOT NULL PRIMARY KEY,
   attribute9 integer NOT NULL,
   rolemodttoft4 bigserial NOT NULL,
   mydatatype_id bigserial NOT NULL
);

CREATE TABLE featuretype2_attribute6_attribute14 (

   mydatatype_id bigserial NOT NULL,
   attribute14 text NOT NULL,
   PRIMARY KEY (mydatatype_id, attribute14)
);

CREATE TABLE featuretype2_attribute6_attribute16 (

   mydatatype_id bigserial NOT NULL,
   codelist_id text NOT NULL,
   PRIMARY KEY (mydatatype_id, codelist_id)
);

CREATE TABLE featuretype3 (

   _id bigserial NOT NULL PRIMARY KEY,
   attribute10 text
);

CREATE TABLE featuretype3_attribute7 (

   _id bigserial NOT NULL PRIMARY KEY,
   attribute4 text NOT NULL,
   attribute5 integer NOT NULL,
   attribute15 text NOT NULL,
   attribute17 text NOT NULL,
   featuretype3_id bigserial NOT NULL
);

CREATE TABLE featuretype3_attribute7_attribute11 (

   _id bigserial NOT NULL PRIMARY KEY,
   attribute9 integer NOT NULL,
   rolemodttoft4 bigserial NOT NULL,
   mydatatype_id bigserial NOT NULL
);

CREATE TABLE featuretype3_attribute7_attribute12 (

   _id bigserial NOT NULL PRIMARY KEY,
   attribute9 integer NOT NULL,
   rolemodttoft4 bigserial NOT NULL,
   mydatatype_id bigserial NOT NULL
);

CREATE TABLE featuretype3_attribute7_attribute14 (

   mydatatype_id bigserial NOT NULL,
   attribute14 text NOT NULL,
   PRIMARY KEY (mydatatype_id, attribute14)
);

CREATE TABLE featuretype3_attribute7_attribute16 (

   mydatatype_id bigserial NOT NULL,
   codelist_id text NOT NULL,
   PRIMARY KEY (mydatatype_id, codelist_id)
);

CREATE TABLE featuretype3_attribute8 (

   _id bigserial NOT NULL PRIMARY KEY,
   attribute9 integer NOT NULL,
   rolemodttoft4 bigserial NOT NULL,
   featuretype3_id bigserial NOT NULL
);

CREATE TABLE featuretype3_attribute9 (

   featuretype3_id bigserial NOT NULL,
   attribute9 text NOT NULL,
   PRIMARY KEY (featuretype3_id, attribute9)
);

CREATE TABLE featuretype4 (

   _id bigserial NOT NULL PRIMARY KEY
);


ALTER TABLE featuretype1_attribute1 ADD CONSTRAINT featuretype1_attribute1_attribute15_chk CHECK (attribute15 IN ('enum1', 'enum2'));
ALTER TABLE featuretype1_attribute1 ADD CONSTRAINT fk_featuretype1_attribute1_attribute17 FOREIGN KEY (attribute17) REFERENCES codelist;
ALTER TABLE featuretype1_attribute1 ADD CONSTRAINT fk_featuretype1_attribute1_featuretype1_id FOREIGN KEY (featuretype1_id) REFERENCES featuretype1;
ALTER TABLE featuretype1_attribute1_attribute11 ADD CONSTRAINT fk_featuretype1_attribute1_attribute11_mydatatype_id FOREIGN KEY (mydatatype_id) REFERENCES featuretype1_attribute1;
ALTER TABLE featuretype1_attribute1_attribute11 ADD CONSTRAINT fk_featuretype1_attribute1_attribute11_rolemodttoft4 FOREIGN KEY (rolemodttoft4) REFERENCES featuretype4;
ALTER TABLE featuretype1_attribute1_attribute12 ADD CONSTRAINT fk_featuretype1_attribute1_attribute12_mydatatype_id FOREIGN KEY (mydatatype_id) REFERENCES featuretype1_attribute1;
ALTER TABLE featuretype1_attribute1_attribute12 ADD CONSTRAINT fk_featuretype1_attribute1_attribute12_rolemodttoft4 FOREIGN KEY (rolemodttoft4) REFERENCES featuretype4;
ALTER TABLE featuretype1_attribute1_attribute14 ADD CONSTRAINT featuretype1_attribute1_attribute14_attribute14_chk CHECK (attribute14 IN ('enum1', 'enum2'));
ALTER TABLE featuretype1_attribute1_attribute14 ADD CONSTRAINT fk_featuretype1_attribute1_attribute14_mydatatype_id FOREIGN KEY (mydatatype_id) REFERENCES featuretype1_attribute1;
ALTER TABLE featuretype1_attribute1_attribute16 ADD CONSTRAINT fk_featuretype1_attribute1_attribute16_codelist_id FOREIGN KEY (codelist_id) REFERENCES codelist;
ALTER TABLE featuretype1_attribute1_attribute16 ADD CONSTRAINT fk_featuretype1_attribute1_attribute16_mydatatype_id FOREIGN KEY (mydatatype_id) REFERENCES featuretype1_attribute1;
ALTER TABLE featuretype1_attribute2 ADD CONSTRAINT fk_featuretype1_attribute2_featuretype1_id FOREIGN KEY (featuretype1_id) REFERENCES featuretype1;
ALTER TABLE featuretype2_attribute13 ADD CONSTRAINT fk_featuretype2_attribute13_featuretype2_id FOREIGN KEY (featuretype2_id) REFERENCES featuretype2;
ALTER TABLE featuretype2_attribute13 ADD CONSTRAINT fk_featuretype2_attribute13_rolemodttoft4 FOREIGN KEY (rolemodttoft4) REFERENCES featuretype4;
ALTER TABLE featuretype2_attribute6 ADD CONSTRAINT featuretype2_attribute6_attribute15_chk CHECK (attribute15 IN ('enum1', 'enum2'));
ALTER TABLE featuretype2_attribute6 ADD CONSTRAINT fk_featuretype2_attribute6_attribute17 FOREIGN KEY (attribute17) REFERENCES codelist;
ALTER TABLE featuretype2_attribute6 ADD CONSTRAINT fk_featuretype2_attribute6_featuretype2_id FOREIGN KEY (featuretype2_id) REFERENCES featuretype2;
ALTER TABLE featuretype2_attribute6_attribute11 ADD CONSTRAINT fk_featuretype2_attribute6_attribute11_mydatatype_id FOREIGN KEY (mydatatype_id) REFERENCES featuretype2_attribute6;
ALTER TABLE featuretype2_attribute6_attribute11 ADD CONSTRAINT fk_featuretype2_attribute6_attribute11_rolemodttoft4 FOREIGN KEY (rolemodttoft4) REFERENCES featuretype4;
ALTER TABLE featuretype2_attribute6_attribute12 ADD CONSTRAINT fk_featuretype2_attribute6_attribute12_mydatatype_id FOREIGN KEY (mydatatype_id) REFERENCES featuretype2_attribute6;
ALTER TABLE featuretype2_attribute6_attribute12 ADD CONSTRAINT fk_featuretype2_attribute6_attribute12_rolemodttoft4 FOREIGN KEY (rolemodttoft4) REFERENCES featuretype4;
ALTER TABLE featuretype2_attribute6_attribute14 ADD CONSTRAINT featuretype2_attribute6_attribute14_attribute14_chk CHECK (attribute14 IN ('enum1', 'enum2'));
ALTER TABLE featuretype2_attribute6_attribute14 ADD CONSTRAINT fk_featuretype2_attribute6_attribute14_mydatatype_id FOREIGN KEY (mydatatype_id) REFERENCES featuretype2_attribute6;
ALTER TABLE featuretype2_attribute6_attribute16 ADD CONSTRAINT fk_featuretype2_attribute6_attribute16_codelist_id FOREIGN KEY (codelist_id) REFERENCES codelist;
ALTER TABLE featuretype2_attribute6_attribute16 ADD CONSTRAINT fk_featuretype2_attribute6_attribute16_mydatatype_id FOREIGN KEY (mydatatype_id) REFERENCES featuretype2_attribute6;
ALTER TABLE featuretype3_attribute7 ADD CONSTRAINT featuretype3_attribute7_attribute15_chk CHECK (attribute15 IN ('enum1', 'enum2'));
ALTER TABLE featuretype3_attribute7 ADD CONSTRAINT fk_featuretype3_attribute7_attribute17 FOREIGN KEY (attribute17) REFERENCES codelist;
ALTER TABLE featuretype3_attribute7 ADD CONSTRAINT fk_featuretype3_attribute7_featuretype3_id FOREIGN KEY (featuretype3_id) REFERENCES featuretype3;
ALTER TABLE featuretype3_attribute7_attribute11 ADD CONSTRAINT fk_featuretype3_attribute7_attribute11_mydatatype_id FOREIGN KEY (mydatatype_id) REFERENCES featuretype3_attribute7;
ALTER TABLE featuretype3_attribute7_attribute11 ADD CONSTRAINT fk_featuretype3_attribute7_attribute11_rolemodttoft4 FOREIGN KEY (rolemodttoft4) REFERENCES featuretype4;
ALTER TABLE featuretype3_attribute7_attribute12 ADD CONSTRAINT fk_featuretype3_attribute7_attribute12_mydatatype_id FOREIGN KEY (mydatatype_id) REFERENCES featuretype3_attribute7;
ALTER TABLE featuretype3_attribute7_attribute12 ADD CONSTRAINT fk_featuretype3_attribute7_attribute12_rolemodttoft4 FOREIGN KEY (rolemodttoft4) REFERENCES featuretype4;
ALTER TABLE featuretype3_attribute7_attribute14 ADD CONSTRAINT featuretype3_attribute7_attribute14_attribute14_chk CHECK (attribute14 IN ('enum1', 'enum2'));
ALTER TABLE featuretype3_attribute7_attribute14 ADD CONSTRAINT fk_featuretype3_attribute7_attribute14_mydatatype_id FOREIGN KEY (mydatatype_id) REFERENCES featuretype3_attribute7;
ALTER TABLE featuretype3_attribute7_attribute16 ADD CONSTRAINT fk_featuretype3_attribute7_attribute16_codelist_id FOREIGN KEY (codelist_id) REFERENCES codelist;
ALTER TABLE featuretype3_attribute7_attribute16 ADD CONSTRAINT fk_featuretype3_attribute7_attribute16_mydatatype_id FOREIGN KEY (mydatatype_id) REFERENCES featuretype3_attribute7;
ALTER TABLE featuretype3_attribute8 ADD CONSTRAINT fk_featuretype3_attribute8_featuretype3_id FOREIGN KEY (featuretype3_id) REFERENCES featuretype3;
ALTER TABLE featuretype3_attribute8 ADD CONSTRAINT fk_featuretype3_attribute8_rolemodttoft4 FOREIGN KEY (rolemodttoft4) REFERENCES featuretype4;
ALTER TABLE featuretype3_attribute9 ADD CONSTRAINT fk_featuretype3_attribute9_featuretype3_id FOREIGN KEY (featuretype3_id) REFERENCES featuretype3;

INSERT INTO codelist (name, documentation) VALUES ('code1', '');
INSERT INTO codelist (name, documentation) VALUES ('code2', '');
