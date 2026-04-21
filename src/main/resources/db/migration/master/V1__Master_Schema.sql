-- 1. TABLAS INDEPENDIENTES Y PADRES PRINCIPALES
create table status (id_status bigint AUTO_INCREMENT not null, uuid varchar(36) not null, name varchar(255) not null, primary key (id_status));
CREATE TABLE http_status_codes (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   code INT NOT NULL UNIQUE,
                                   name VARCHAR(50) NOT NULL,
                                   description VARCHAR(255)
);
create table system_tenants (
                                id_tenant BIGINT AUTO_INCREMENT PRIMARY KEY,
                                tenant_key VARCHAR(100) NOT NULL UNIQUE,
                                display_name VARCHAR(255) NOT NULL,
                                contact_email VARCHAR(255),
                                status VARCHAR(50) DEFAULT 'ACTIVE',
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. TABLAS HIJAS INTERMEDIAS
create table system_roles (id_role bigint AUTO_INCREMENT not null, status_id bigint not null, uuid varchar(36) not null, role_name varchar(50) not null, primary key (id_role));

create table audit_logs (id_log bigint AUTO_INCREMENT not null, id_status bigint not null, concept_audit varchar(255) not null, date_ocurrence varchar(255) not null, user_blamed varchar(255) not null, primary key (id_log));

-- 3. TABLA DEPENDIENTE FINAL (Users)
-- NOTA CRÍTICA: tenant_id ajustado a varchar(100) para homologar con system_tenants.tenant_key
create table system_users (id_user bigint AUTO_INCREMENT not null, role_id bigint not null, status_id bigint not null, uuid varchar(36) not null, tenant_id varchar(100) default 'his_master' not null, password varchar(255) not null, user_name varchar(255) not null, primary key (id_user));


-- 4. RESTRICCIONES DE INTEGRIDAD Y LLAVES FORÁNEAS (Originales)
alter table status add constraint UKjay9oq3tlp3u1t3ly2rryl7aw unique (uuid);
alter table status add constraint UKreccgx9nr0a8dwv201t44l6pd unique (name);
alter table system_roles add constraint UKpa1sa9t1m9jd7s6koflptyymv unique (uuid);
alter table system_roles add constraint UKcgd9737yoqb95gh1wnjkkp9ox unique (role_name);
alter table system_users add constraint UKdrxd81r9o9lehtqu5ndox1e4t unique (uuid);
alter table system_users add constraint UKhqt9yudybkbihodsyx9as5lm3 unique (user_name);
alter table audit_logs add constraint FKi4e1fjluwuf3wejrquwixkq0w foreign key (id_status) references status (id_status) ;
alter table system_roles add constraint FKggrltabclsfudftyvl4sy3m19 foreign key (status_id) references status (id_status);
alter table system_users add constraint FKuh6yk6r9lnqn6am45w2df5gv foreign key (role_id) references system_roles (id_role);
alter table system_users add constraint FKt9tcycdhhmxcvdq2l7c03s0gn foreign key (status_id) references status (id_status);

-- 5. NUEVA LLAVE FORÁNEA (Enlace de Usuarios al Directorio de Inquilinos)
alter table system_users add constraint FK_user_tenant foreign key (tenant_id) references system_tenants (tenant_key);


-- 6. SEMBRADO DE DATOS: DICCIONARIO GLOBAL DE ESTATUS
INSERT INTO status (uuid, name) VALUES ('C2A1B3A0-3E12-4C10-8A51-1A2B3C4D5E60', 'Active');
INSERT INTO status (uuid, name) VALUES ('D4B2C4B1-4F23-5D21-9B62-2B3C4D5E6F71', 'Inactive');
INSERT INTO status (uuid, name) VALUES ('E5C3D5C2-5034-6E32-AC73-3C4D5E6F7082', 'Edited');
INSERT INTO status (uuid, name) VALUES ('F6D4E6D3-6145-7F43-BD84-4D5E6F708193', 'Deleted');
INSERT INTO status (uuid, name) VALUES ('07E5F7E4-7256-8054-CE95-5E6F708192A4', 'Added');
-- 6.5. SEMBRADO DE DATOS: DICCIONARIO DE HTTP_STATUS_CODE
INSERT INTO http_status_codes (code, name, description) VALUES (200, 'OK', 'Petición procesada correctamente');
INSERT INTO http_status_codes (code, name, description) VALUES (201, 'Created', 'Recurso aprovisionado o creado con éxito');
INSERT INTO http_status_codes (code, name, description) VALUES (400, 'Bad Request', 'Estructura de la petición inválida o datos faltantes');
INSERT INTO http_status_codes (code, name, description) VALUES (403, 'Forbidden', 'Acceso denegado o módulo inhabilitado');
INSERT INTO http_status_codes (code, name, description) VALUES (500, 'Internal Server Error', 'Fallo crítico en el procesamiento del servidor');
-- 7. SEMBRADO DE DATOS: REGISTRO DEL INQUILINO MAESTRO
INSERT INTO system_tenants (tenant_key, display_name, status) VALUES ('his_master', 'Sistema Central Nexus HIS', 'SYSTEM');