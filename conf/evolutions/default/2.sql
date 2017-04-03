# --- !Ups

alter table "MUNICIPALITIES" drop constraint "FK_PROVINCES_MUNICIPALITIES";
alter table "PROVINCES" drop constraint "FK_REGIONS_PROVINCES";
alter table "REGIONS" drop constraint "FK_COUNTRIES_REGIONS";

# --- !Downs
