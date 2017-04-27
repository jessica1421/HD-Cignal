# --- !Ups

INSERT INTO "BUSINESSTYPES" ("NAME") VALUES ('Corporation');
INSERT INTO "BUSINESSTYPES" ("NAME") VALUES ('Partnership');
INSERT INTO "BUSINESSTYPES" ("NAME") VALUES ('Sole Proprietorship');

INSERT INTO "FIRM_TYPES" ("NAME") VALUES ('Company/Franchisee');
INSERT INTO "FIRM_TYPES" ("NAME") VALUES ('Branch');
INSERT INTO "FIRM_TYPES" ("NAME") VALUES ('Logistics');
INSERT INTO "FIRM_TYPES" ("NAME") VALUES ('Commissary');
INSERT INTO "FIRM_TYPES" ("NAME") VALUES ('Others');

INSERT INTO "ACCOUNTING_METHODS" ("CODE", "NAME") VALUES ('AC', 'ACCRUAL');
INSERT INTO "ACCOUNTING_METHODS" ("CODE", "NAME") VALUES ('CA', 'CASH');
INSERT INTO "ACCOUNTING_METHODS" ("CODE", "NAME") VALUES ('HY', 'HYBRID');

INSERT INTO "VATS" ("NAME", "VALUE") VALUES ('VAT', 0.12);
INSERT INTO "VATS" ("NAME", "VALUE") VALUES ('NON-VAT', 1.25);

INSERT INTO "COUNTRIES" ("NAME") VALUES ('PH');
INSERT INTO "COUNTRIES" ("NAME") VALUES ('US');
INSERT INTO "COUNTRIES" ("NAME") VALUES ('JP');

INSERT INTO "REGIONS"  ("NAME", "ID_COUNTRY") VALUES ('Region VII', 1);
INSERT INTO "REGIONS"  ("NAME", "ID_COUNTRY") VALUES ('REGION 4-A', 1);

INSERT INTO "PROVINCES"  ("NAME", "ID_REGION") VALUES ('CEBU', 1);
INSERT INTO "PROVINCES"  ("NAME", "ID_REGION") VALUES ('RIZAL', 2);

INSERT INTO "MUNICIPALITIES"  ("NAME", "AREACODE", "ID_PROVINCE") VALUES ('Cebu City', '6000', 1);
INSERT INTO "MUNICIPALITIES"  ("NAME", "AREACODE", "ID_PROVINCE") VALUES ('Compostela', '6003', 1);
INSERT INTO "MUNICIPALITIES"  ("NAME", "AREACODE", "ID_PROVINCE") VALUES ('Consolacion', '6001', 1);
INSERT INTO "MUNICIPALITIES"  ("NAME", "AREACODE", "ID_PROVINCE") VALUES ('Cordova', '6017', 1);
INSERT INTO "MUNICIPALITIES"  ("NAME", "AREACODE", "ID_PROVINCE") VALUES ('Daanbantayan', '6013', 1);
INSERT INTO "MUNICIPALITIES"  ("NAME", "AREACODE", "ID_PROVINCE") VALUES ('Dalaguete', '6022', 1);
INSERT INTO "MUNICIPALITIES"  ("NAME", "AREACODE", "ID_PROVINCE") VALUES ('Antipolo', '1870', 2);
INSERT INTO "MUNICIPALITIES"  ("NAME", "AREACODE", "ID_PROVINCE") VALUES ('Baras', '1970', 2);
INSERT INTO "MUNICIPALITIES"  ("NAME", "AREACODE", "ID_PROVINCE") VALUES ('Binangonan', '1940', 2);
INSERT INTO "MUNICIPALITIES"  ("NAME", "AREACODE", "ID_PROVINCE") VALUES ('Cainta', '1900', 2);
INSERT INTO "MUNICIPALITIES"  ("NAME", "AREACODE", "ID_PROVINCE") VALUES ('Cardona', '1950', 2);
INSERT INTO "MUNICIPALITIES"  ("NAME", "AREACODE", "ID_PROVINCE") VALUES ('Morong', '1960', 2);

# --- !Downs
