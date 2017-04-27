# --- !Ups

create table "ACCOUNTS" ("POSTING_CODE" VARCHAR NOT NULL,"RECORD_CODE" VARCHAR NOT NULL,"CHART_CODE" VARCHAR NOT NULL,"NAME" VARCHAR NOT NULL,"ID_MAIN" INTEGER NOT NULL,"ID_SUBSIDIARY" INTEGER NOT NULL,"IS_NOMINAL" BOOLEAN NOT NULL,"IS_DEBIT" BOOLEAN NOT NULL,"NOTES" VARCHAR NOT NULL,"REMARKS" VARCHAR NOT NULL,"ID_PARENT" INTEGER,"ID_FIRM" INTEGER,"ID" SERIAL NOT NULL PRIMARY KEY);

create table "ACCOUNT_CLASSIFICATIONS" ("NAME" VARCHAR NOT NULL,"IS_MAIN" BOOLEAN NOT NULL,"CODE" SERIAL NOT NULL PRIMARY KEY);

create table "ACCOUNTING_METHODS" ("CODE" VARCHAR NOT NULL PRIMARY KEY,"NAME" VARCHAR NOT NULL);

create table "ACCOUNTING_PERIODS" ("ID_FIRM" INTEGER NOT NULL PRIMARY KEY,"START_DATE" timestamp NOT NULL,"END_DATE" timestamp NOT NULL);

create index "IDX_END_DATE_ACCOUNTING_PERIODS" on "ACCOUNTING_PERIODS" ("END_DATE");

create index "IDX_START_DATE_ACCOUNTING_PERIODS" on "ACCOUNTING_PERIODS" ("START_DATE");

create table "BUSINESSTYPES" ("NAME" VARCHAR NOT NULL,"ID" SERIAL NOT NULL PRIMARY KEY);

create table "CONTACTINFOS" ("VALUE" VARCHAR NOT NULL,"ID_FIRM" INTEGER NOT NULL,"ID" SERIAL NOT NULL PRIMARY KEY);

create table "COUNTRIES" ("NAME" VARCHAR NOT NULL,"ID" SERIAL NOT NULL PRIMARY KEY);

create table "FIRMS" ("NAME" VARCHAR NOT NULL,"ADDRESS" VARCHAR NOT NULL,"POSTING_PERIOD" SMALLINT NOT NULL,"ID_BUSINESS_TYPE" INTEGER NOT NULL,"ID_VAT" INTEGER NOT NULL,"AREA_CODE" INTEGER NOT NULL,"CODE_ACCOUNTING_METHOD" VARCHAR NOT NULL,"CODE_POSTING_METHOD" VARCHAR NOT NULL,"ID_PARENT" INTEGER,"ID_FIRMTYPE" INTEGER NOT NULL,"IS_ENABLE" BOOLEAN NOT NULL,"ID" SERIAL NOT NULL PRIMARY KEY);

create table "FIRM_TYPES" ("NAME" VARCHAR NOT NULL,"ID" SERIAL NOT NULL PRIMARY KEY);

create table "MUNICIPALITIES" ("NAME" VARCHAR NOT NULL,"AREACODE" INTEGER NOT NULL,"ID_PROVINCE" INTEGER NOT NULL,"ID" SERIAL NOT NULL PRIMARY KEY);

create unique index "IDX_AREA_CODE_MUNICIPALITIES" on "MUNICIPALITIES" ("AREACODE");

create table "POSTING_METHODS" ("CODE" VARCHAR NOT NULL PRIMARY KEY);

create table "PROPRIETORS" ("NAME" VARCHAR NOT NULL,"SHARE" DOUBLE PRECISION NOT NULL,"ID_FIRM" INTEGER NOT NULL,"ID" SERIAL NOT NULL PRIMARY KEY);

create table "PROVINCES" ("NAME" VARCHAR NOT NULL,"ID_REGION" INTEGER NOT NULL,"ID" SERIAL NOT NULL PRIMARY KEY);

create table "REGIONS" ("NAME" VARCHAR NOT NULL,"ID_COUNTRY" INTEGER NOT NULL,"ID" SERIAL NOT NULL PRIMARY KEY);

create table "VATS" ("NAME" VARCHAR NOT NULL,"VALUE" DOUBLE PRECISION NOT NULL,"ID" SERIAL NOT NULL PRIMARY KEY);

alter table "ACCOUNTS" add constraint "FK_ACCOUNT_FIRM_ACCOUNTS" foreign key("ID_FIRM") references "FIRMS"("ID") on update CASCADE on delete RESTRICT;

alter table "ACCOUNTS" add constraint "FK_ACCOUNT_MAIN_ACCOUNTS" foreign key("ID_MAIN") references "ACCOUNT_CLASSIFICATIONS"("CODE") on update CASCADE on delete RESTRICT;

alter table "ACCOUNTS" add constraint "FK_ACCOUNT_SUBSIDIARY_ACCOUNTS" foreign key("ID_SUBSIDIARY") references "ACCOUNT_CLASSIFICATIONS"("CODE") on update CASCADE on delete RESTRICT;

alter table "ACCOUNTING_PERIODS" add constraint "FK_FIRMS_ACCOUNTING_PERIODS" foreign key("ID_FIRM") references "FIRMS"("ID") on update CASCADE on delete RESTRICT;

alter table "FIRMS" add constraint "FK_ACCOUNTING_METHOD_FIRMS" foreign key("CODE_ACCOUNTING_METHOD") references "ACCOUNTING_METHODS"("CODE") on update CASCADE on delete RESTRICT;

alter table "FIRMS" add constraint "FK_BUSINESSTYPES_FIRMS" foreign key("ID_BUSINESS_TYPE") references "BUSINESSTYPES"("ID") on update CASCADE on delete RESTRICT;

alter table "FIRMS" add constraint "FK_FIRM_TYPE_FIRMS" foreign key("ID_FIRMTYPE") references "FIRM_TYPES"("ID") on update CASCADE on delete RESTRICT;

alter table "FIRMS" add constraint "FK_POSTING_METHOD_FIRMS" foreign key("CODE_POSTING_METHOD") references "POSTING_METHODS"("CODE") on update CASCADE on delete RESTRICT;

alter table "FIRMS" add constraint "FK_VAT_FIRMS" foreign key("ID_VAT") references "VATS"("ID") on update CASCADE on delete RESTRICT;

alter table "MUNICIPALITIES" add constraint "FK_PROVINCES_MUNICIPALITIES" foreign key("ID_PROVINCE") references "PROVINCES"("ID") on update CASCADE on delete RESTRICT;

alter table "PROPRIETORS" add constraint "FK_COUNTRIES_PROPRIETORS" foreign key("ID_FIRM") references "FIRMS"("ID") on update CASCADE on delete RESTRICT;

alter table "PROVINCES" add constraint "FK_REGIONS_PROVINCES" foreign key("ID_REGION") references "REGIONS"("ID") on update CASCADE on delete RESTRICT;

alter table "REGIONS" add constraint "FK_COUNTRIES_REGIONS" foreign key("ID_COUNTRY") references "COUNTRIES"("ID") on update CASCADE on delete RESTRICT;



# --- !Downs

alter table "REGIONS" drop constraint "FK_COUNTRIES_REGIONS";
alter table "PROVINCES" drop constraint "FK_REGIONS_PROVINCES";
alter table "PROPRIETORS" drop constraint "FK_COUNTRIES_PROPRIETORS";
alter table "MUNICIPALITIES" drop constraint "FK_PROVINCES_MUNICIPALITIES";
alter table "FIRMS" drop constraint "FK_ACCOUNTING_METHOD_FIRMS";
alter table "FIRMS" drop constraint "FK_BUSINESSTYPES_FIRMS";
alter table "FIRMS" drop constraint "FK_FIRM_TYPE_FIRMS";
alter table "FIRMS" drop constraint "FK_POSTING_METHOD_FIRMS";
alter table "FIRMS" drop constraint "FK_VAT_FIRMS";
alter table "ACCOUNTING_PERIODS" drop constraint "FK_FIRMS_ACCOUNTING_PERIODS";
alter table "ACCOUNTS" drop constraint "FK_ACCOUNT_FIRM_ACCOUNTS";
alter table "ACCOUNTS" drop constraint "FK_ACCOUNT_MAIN_ACCOUNTS";
alter table "ACCOUNTS" drop constraint "FK_ACCOUNT_SUBSIDIARY_ACCOUNTS";
drop table "VATS";
drop table "REGIONS";
drop table "PROVINCES";
drop table "PROPRIETORS";
drop table "POSTING_METHODS";
drop table "MUNICIPALITIES";
drop table "FIRM_TYPES";
drop table "FIRMS";
drop table "COUNTRIES";
drop table "CONTACTINFOS";
drop table "BUSINESSTYPES";
drop table "ACCOUNTING_PERIODS";
drop table "ACCOUNTING_METHODS";
drop table "ACCOUNT_CLASSIFICATIONS";
drop table "ACCOUNTS";
