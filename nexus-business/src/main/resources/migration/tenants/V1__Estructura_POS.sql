-- ==================================================================================================
-- FASE 1: TABLAS INDEPENDIENTES (Nivel 0 - No tienen Llaves Foráneas)
-- ==================================================================================================
CREATE TABLE status (id_status bigint AUTO_INCREMENT NOT NULL, uuid varchar(36) not null, name varchar(255) not null, primary key (id_status));
alter table status add constraint UKjay9oq3tlp3u1t3ly2rryl7aw unique (uuid);
alter table status add constraint UKreccgx9nr0a8dwv201t44l6pd unique (name);
INSERT INTO status (id_status, uuid, name) VALUES (1, 'C2A1B3A0-3E12-4C10-8A51-1A2B3C4D5E60', 'Active');
INSERT INTO status (id_status, uuid, name) VALUES (2, 'D4B2C4B1-4F23-5D21-9B62-2B3C4D5E6F71', 'Inactive');
INSERT INTO status (id_status, uuid, name) VALUES (3, 'E5C3D5C2-5034-6E32-AC73-3C4D5E6F7082', 'Edited');
INSERT INTO status (id_status, uuid, name) VALUES (4, 'F6D4E6D3-6145-7F43-BD84-4D5E6F708193', 'Deleted');
INSERT INTO status (id_status, uuid, name) VALUES (5, '07E5F7E4-7256-8054-CE95-5E6F708192A4', 'Added');
