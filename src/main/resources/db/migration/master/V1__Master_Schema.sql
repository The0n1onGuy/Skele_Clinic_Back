create table audit_logs (id_log bigint identity not null, id_status bigint not null, concept_audit varchar(255) not null, date_ocurrence varchar(255) not null, user_blamed varchar(255) not null, primary key (id_log));


create table status (id_status bigint identity not null, uuid varchar(36) not null, name varchar(255) not null, primary key (id_status));
create table system_roles (id_role bigint identity not null, status_id bigint not null, uuid varchar(36) not null, role_name varchar(50) not null, primary key (id_role));
create table system_users (id_user bigint identity not null, role_id bigint not null, status_id bigint not null, uuid varchar(36) not null, tenant_id varchar(50) default 'dbo' not null, password varchar(255) not null, user_name varchar(255) not null, primary key (id_user));

alter table status add constraint UKjay9oq3tlp3u1t3ly2rryl7aw unique (uuid);
alter table status add constraint UKreccgx9nr0a8dwv201t44l6pd unique (name);
alter table system_roles add constraint UKpa1sa9t1m9jd7s6koflptyymv unique (uuid);
alter table system_roles add constraint UKcgd9737yoqb95gh1wnjkkp9ox unique (role_name);
alter table system_users add constraint UKdrxd81r9o9lehtqu5ndox1e4t unique (uuid);
alter table system_users add constraint UKhqt9yudybkbihodsyx9as5lm3 unique (user_name);
alter table audit_logs add constraint FKi4e1fjluwuf3wejrquwixkq0w foreign key (id_status) references status;
alter table system_roles add constraint FKggrltabclsfudftyvl4sy3m19 foreign key (status_id) references status;
alter table system_users add constraint FKuh6yk6r9lnqn6am45w2df5gv foreign key (role_id) references system_roles;
alter table system_users add constraint FKt9tcycdhhmxcvdq2l7c03s0gn foreign key (status_id) references status;
-- DICCIONARIO GLOBAL DE ESTATUS (dbo)
INSERT INTO status (uuid, name) VALUES ('C2A1B3A0-3E12-4C10-8A51-1A2B3C4D5E60', 'Active');
INSERT INTO status (uuid, name) VALUES ('D4B2C4B1-4F23-5D21-9B62-2B3C4D5E6F71', 'Inactive');
INSERT INTO status (uuid, name) VALUES ('E5C3D5C2-5034-6E32-AC73-3C4D5E6F7082', 'Edited');
INSERT INTO status (uuid, name) VALUES ('F6D4E6D3-6145-7F43-BD84-4D5E6F708193', 'Deleted');
INSERT INTO status (uuid, name) VALUES ('07E5F7E4-7256-8054-CE95-5E6F708192A4', 'Added');