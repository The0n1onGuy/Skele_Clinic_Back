-- ==================================================================================================
-- FASE 1: TABLAS INDEPENDIENTES (Nivel 0 - No tienen Llaves Foráneas)
-- ==================================================================================================
CREATE TABLE status (id_status bigint AUTO_INCREMENT NOT NULL, uuid varchar(36) not null, name varchar(255) not null, primary key (id_status));
CREATE TABLE categories (id bigint AUTO_INCREMENT NOT NULL, name varchar(50) not null, description TEXT, primary key (id));
CREATE TABLE citas (fecha date, hora time, id bigint AUTO_INCREMENT NOT NULL, especialista varchar(255), estado varchar(255), motivo varchar(255), paciente_nombre varchar(255), primary key (id));
CREATE TABLE doctor_model (id bigint not null, apellido varchar(255), nombre varchar(255), primary key (id));
CREATE TABLE patient (enfermedades_cronicas varchar(255) ,contacto_emergencia varchar(255),alergias varchar(255), fecha_nacimiento date, id VARCHAR(36) not null, apellidos varchar(255) not null, curp varchar(255) not null, direccion varchar(255), email varchar(255), genero varchar(255), nombre varchar(255) not null, telefono varchar(255), tipo_sangre varchar(255), primary key (id));


-- ==================================================================================================
-- FASE 2: TABLAS DEPENDIENTES (Nivel 1 - Solo dependen de la tabla status u otra Nivel 0)
-- ==================================================================================================
CREATE TABLE audit_logs (id_log bigint AUTO_INCREMENT not null, id_status bigint not null, concept_audit varchar(255) not null, date_ocurrence varchar(255) not null, user_blamed varchar(255) not null, primary key (id_log));
CREATE TABLE appointments (fecha_hora_fin DATETIME(6), fecha_hora_inicio DATETIME(6), id VARCHAR(36) not null, patient_id VARCHAR(36), consultorio varchar(255), estado varchar(255) check ((estado in ('PENDIENTE','CONFIRMADA','CANCELADA','COMPLETADA'))), primary key (id));
CREATE TABLE cleaning_supplies_model (current_stock int, stock_min int, id_supplies bigint AUTO_INCREMENT not null, status_id bigint not null, name varchar(100) not null, expiration_date varchar(255), unit_measurement varchar(255), primary key (id_supplies));
CREATE TABLE clinic_history (fecha_registro DATETIME(6) not null, id VARCHAR(36) not null, patient_id VARCHAR(36) not null, motivo_consulta varchar(2000), padecimiento_actual varchar(5000), diagnostico_preliminar varchar(255), primary key (id));
CREATE TABLE lyr_employees_model (telefono int, id_empleado bigint not null, status_id bigint not null, apellido varchar(255), area varchar(255), nombre varchar(255), turno varchar(255), primary key (id_empleado));
CREATE TABLE morgue (numero_gaveta int, id bigint not null, id_paciente bigint, id_paciente_local bigint, id_status bigint not null, causa_defuncion varchar(255), fecha_ingreso varchar(255), nombre_paciente_simulado varchar(255), primary key (id));
CREATE TABLE rpbi_cat_clasification (id bigint AUTO_INCREMENT NOT NULL, status_id bigint not null, color_code varchar(20), uuid varchar(36) not null, name varchar(50) not null, description varchar(255), primary key (id));
CREATE TABLE rpbi_cat_containers (id bigint AUTO_INCREMENT NOT NULL, id_status bigint not null, uuid varchar(36) not null, name varchar(100) not null, description varchar(255), primary key (id));
CREATE TABLE rpbi_cat_phyisical_state (id bigint AUTO_INCREMENT NOT NULL, status_id bigint not null, measure_unit varchar(10) not null, uuid varchar(36) not null, name varchar(50) not null, primary key (id));
CREATE TABLE rrhh_departments (id_department bigint AUTO_INCREMENT NOT NULL, id_status bigint not null, uuid VARCHAR(36) not null, name varchar(100) not null, primary key (id_department));
CREATE TABLE rrhh_positions (id_position bigint AUTO_INCREMENT NOT NULL, id_status bigint not null, uuid VARCHAR(36) not null, name varchar(100) not null, description TEXT, primary key (id_position));
CREATE TABLE textile_articles_model (id_articles bigint not null, status_id bigint not null, descripcion varchar(255), name varchar(255), primary key (id_articles));
CREATE TABLE triages (frecuencia_cardiaca int, saturacion_oxigeno int, temperatura float(53), fecha_hora DATETIME(6), id VARCHAR(36) not null, patient_id VARCHAR(36) not null, nivel varchar(255) check ((nivel in ('RESUSCITATION','EMERGENCY','URGENCY','LESS_URGENCY','NON_URGENCY'))), presion_arterial varchar(255), primary key (id));


-- ==================================================================================================
-- FASE 3: TABLAS ALTAMENTE DEPENDIENTES (Nivel 2 y 3)
-- ==================================================================================================
CREATE TABLE rpbi_compliance_matrix_nom087 (classification_id bigint not null, container_id bigint not null, id bigint AUTO_INCREMENT NOT NULL, physical_state_id bigint not null, status_id bigint not null, primary key (id));
CREATE TABLE rpbi_generation_record (quantity float(53) not null, classification_id bigint not null, container_id bigint not null, generation_date DATETIME(6) not null, id bigint AUTO_INCREMENT NOT NULL, physical_state_id bigint not null, status_id bigint not null, uuid varchar(36) not null, generation_area varchar(100) not null, responsible_user varchar(100) not null, primary key (id));
CREATE TABLE rrhh_employees (id_department bigint not null, id_employee bigint AUTO_INCREMENT NOT NULL, id_position bigint not null, id_status bigint not null, uuid VARCHAR(36) not null, curp varchar(255) not null, datebirth varchar(255) not null, datereg varchar(255) not null, gender varchar(255) not null, matname varchar(255) not null, name varchar(255) not null, patname varchar(255) not null, rfc varchar(255) not null, primary key (id_employee));
CREATE TABLE rrhh_clinic_details (id_clinic_details bigint AUTO_INCREMENT NOT NULL, id_employee bigint not null, id_status bigint not null, professional_license varchar(20), uuid varchar(36) not null, specialty varchar(100), graduation_institution varchar(150), primary key (id_clinic_details));
CREATE TABLE rrhh_contracts (base_salary numeric(18,2) not null, id_contract bigint AUTO_INCREMENT NOT NULL, id_employee bigint not null, id_status bigint not null, uuid varchar(36) not null, contract_type varchar(50) not null, hiring_date varchar(255) not null, termination_date varchar(255), primary key (id_contract));
CREATE TABLE rrhh_schedules (end_time time not null, start_time time not null, id_employee bigint not null, id_schedule bigint AUTO_INCREMENT NOT NULL, id_status bigint not null, day_week varchar(15) not null, uuid VARCHAR(36) not null, primary key (id_schedule));
CREATE TABLE supplies_movements_model (cantidad int, employees_id bigint not null, id_movimiento bigint not null, supplies_id bigint not null, fecha_movimiento varchar(255), observaciones TEXT, tipo_movimiento varchar(255), primary key (id_movimiento));
CREATE TABLE textile_movements_model (amount int, articulo_id bigint not null, empleado_id bigint not null, id_motion bigint not null, motion_date varchar(255), observations varchar(255), type_motion varchar(255), primary key (id_motion));


-- ==================================================================================================
-- FASE 4: ÍNDICES ÚNICOS Y RESTRICCIONES (CONSTRAINTS)
-- ==================================================================================================
create unique index UKew5wyevkvulec1m6uqqjyx9hf on morgue (id_paciente);
create unique index UKjqmwxxxugx1rpdw7dl1y35kbc on rrhh_clinic_details (professional_license);

alter table status add constraint UKjay9oq3tlp3u1t3ly2rryl7aw unique (uuid);
alter table status add constraint UKreccgx9nr0a8dwv201t44l6pd unique (name);
alter table patient add constraint UKmx9n36weavbf3f9rudp1d4kxq unique (curp);
alter table rpbi_cat_clasification add constraint UKkokft60e2mt5nnljb1f03fny1 unique (uuid);
alter table rpbi_cat_containers add constraint UKtaeq2xafjm9o7mp6n4srf10km unique (uuid);
alter table rpbi_cat_phyisical_state add constraint UK1ju7tdjpal3am5ir3bamrop unique (uuid);
alter table rpbi_cat_phyisical_state add constraint UKfelyglt4nqhjgeevp6hxdrv8q unique (name);
alter table rpbi_generation_record add constraint UKhpe43jib1m8mbpjekdia09cv unique (uuid);
alter table rrhh_clinic_details add constraint UKeyvtte4daqer820t1pj6s1h20 unique (id_employee);
alter table rrhh_clinic_details add constraint UKl8uw7tr1qtjji7vjoyu9pdx1b unique (uuid);
alter table rrhh_contracts add constraint UK7vjpnyclxwrqot91pbxwu73dc unique (uuid);

-- ==================================================================================================
-- FASE 5: LLAVES FORÁNEAS (FOREIGN KEYS)
-- ==================================================================================================
-- Auditoría Local (La tabla nueva)
alter table audit_logs add constraint FK_audit_status foreign key (id_status) references status (id_status);

alter table appointments add constraint FKcl9b1a19a01yhjcdibna1gjl foreign key (patient_id) references patient (id);
alter table cleaning_supplies_model add constraint FKmdq084nnceaw7lcryg4fv4mwf foreign key (status_id) references status (id_status);
alter table clinic_history add constraint FK4ndp40tr0671esw3ainxj6sy8 foreign key (patient_id) references patient (id);
alter table lyr_employees_model add constraint FKfp03rf61nclx11mrbxb19jn31 foreign key (status_id) references status (id_status);
alter table morgue add constraint FKnt6w6felfnxfjhm0m4vohohx6 foreign key (id_status) references status (id_status);
alter table morgue add constraint FK86son91dymdclqmr2qykmsee foreign key (id_paciente) references doctor_model (id);
alter table rpbi_cat_clasification add constraint FKoxoj6tb7jk3cofmhc03xjslty foreign key (status_id) references status (id_status);
alter table rpbi_cat_containers add constraint FKajw19eji43mce5vw3sq9ge0ih foreign key (id_status) references status (id_status);
alter table rpbi_cat_phyisical_state add constraint FK40ks7qd81ygobtbksrwuqhbts foreign key (status_id) references status (id_status);
alter table rpbi_compliance_matrix_nom087 add constraint FK8ql38b0isbiqfhhfbif17qsp6 foreign key (classification_id) references rpbi_cat_clasification (id);
alter table rpbi_compliance_matrix_nom087 add constraint FKtmwhl5393c6xs4qqjfnboy478 foreign key (container_id) references rpbi_cat_containers (id);
alter table rpbi_compliance_matrix_nom087 add constraint FK1i621edrwam2lqfvlcsrp0dpl foreign key (physical_state_id) references rpbi_cat_phyisical_state (id);
alter table rpbi_compliance_matrix_nom087 add constraint FKjgwm0swodfiggxkqw463ja8s1 foreign key (status_id) references status (id_status);
alter table rpbi_generation_record add constraint FKlry259i1dqr7xhe8mifnna8ie foreign key (classification_id) references rpbi_cat_clasification (id);
alter table rpbi_generation_record add constraint FKkivyoralqlaisioom0mqwgtn4 foreign key (container_id) references rpbi_cat_containers (id);
alter table rpbi_generation_record add constraint FK45ujx6bfm8eaalmr9yr97fbsn foreign key (physical_state_id) references rpbi_cat_phyisical_state (id);
alter table rpbi_generation_record add constraint FK6ea89sxse64l1nl9yijsi62uu foreign key (status_id) references status (id_status);
alter table rrhh_clinic_details add constraint FKr10a4by8qijrlbrhi0opwh54v foreign key (id_employee) references rrhh_employees (id_employee);
alter table rrhh_clinic_details add constraint FKqihnk5t599war6hyw0dhkigrs foreign key (id_status) references status (id_status);
alter table rrhh_contracts add constraint FKc5f9w36b5x2kl507iamt7cahd foreign key (id_employee) references rrhh_employees (id_employee);
alter table rrhh_contracts add constraint FKo831ys17g2edamgvd3s5oecrk foreign key (id_status) references status (id_status);
alter table rrhh_departments add constraint FKh2avpfx781enwaruupih4plnx foreign key (id_status) references status (id_status);
alter table rrhh_employees add constraint FKajp5fjxjhxk50a6kyc2f6pwu0 foreign key (id_department) references rrhh_departments (id_department);
alter table rrhh_employees add constraint FKaks2xpunpcw1cact0lwrsgs5m foreign key (id_position) references rrhh_positions (id_position);
alter table rrhh_employees add constraint FKi2275x6d17r0ept8n5yvkh1wd foreign key (id_status) references status (id_status);
alter table rrhh_positions add constraint FK13dxlbf5ik6swv54jmcb65val foreign key (id_status) references status (id_status);
alter table rrhh_schedules add constraint FK2deacoa7jgaepv77goof68kxn foreign key (id_employee) references rrhh_employees (id_employee);
alter table rrhh_schedules add constraint FKivave5g2wewbw8lg5b38pa0nh foreign key (id_status) references status (id_status);
alter table supplies_movements_model add constraint FK47h1y77iql9em1b0paf5tgey3 foreign key (employees_id) references lyr_employees_model (id_empleado);
alter table supplies_movements_model add constraint FK6mtujppgykd6g0rp8ahjve061 foreign key (supplies_id) references cleaning_supplies_model (id_supplies);
alter table textile_articles_model add constraint FKfa1ewycoree4s3h1i2jfh8tms foreign key (status_id) references status (id_status);
alter table textile_movements_model add constraint FK8q7u5608r8pqnsk24c94kqa90 foreign key (empleado_id) references lyr_employees_model (id_empleado);
alter table textile_movements_model add constraint FKmqwkcu4cc10lhpog79ctb08b3 foreign key (articulo_id) references textile_articles_model (id_articles);
alter table triages add constraint FKpmr4nldkp9t4aetyrbls8jvs3 foreign key (patient_id) references patient (id);
-- ==================================================================================================
-- FASE 6: INYECCIÓN DE CATÁLOGOS BASE (Orden Topológico Estricto)
-- ==================================================================================================

-- 1. Primero nacen los Estados (Para satisfacer las llaves foráneas de todo el sistema)
INSERT INTO status (id_status, uuid, name) VALUES (1, 'C2A1B3A0-3E12-4C10-8A51-1A2B3C4D5E60', 'Active');
INSERT INTO status (id_status, uuid, name) VALUES (2, 'D4B2C4B1-4F23-5D21-9B62-2B3C4D5E6F71', 'Inactive');
INSERT INTO status (id_status, uuid, name) VALUES (3, 'E5C3D5C2-5034-6E32-AC73-3C4D5E6F7082', 'Edited');
INSERT INTO status (id_status, uuid, name) VALUES (4, 'F6D4E6D3-6145-7F43-BD84-4D5E6F708193', 'Deleted');
INSERT INTO status (id_status, uuid, name) VALUES (5, '07E5F7E4-7256-8054-CE95-5E6F708192A4', 'Added');

-- 2. DICCIONARIOS CLÍNICOS (RPBI) - Ya pueden referenciar al status 1
INSERT INTO rpbi_cat_clasification (status_id, uuid, name, description) VALUES (1, '11111111-1111-1111-1111-111111111111', 'Punzocortantes', 'Agujas de jeringas, hojas de bisturí...');
INSERT INTO rpbi_cat_phyisical_state (status_id, measure_unit, uuid, name) VALUES (1, 'Kilogramos', '22222222-2222-2222-2222-222222222222', 'Sólido');
INSERT INTO rpbi_cat_containers (id_status, uuid, name, description) VALUES (1, '33333333-3333-3333-3333-333333333333', 'Recipiente Rígido Rojo', 'Contenedor de polipropileno');
INSERT INTO rpbi_compliance_matrix_nom087 (classification_id, container_id, physical_state_id, status_id) VALUES (1, 1, 1, 1);

-- 3. DICCIONARIOS CLÍNICOS (RRHH)
INSERT INTO rrhh_departments (id_status, uuid, name) VALUES (1, UUID(), 'Medicina General');
INSERT INTO rrhh_positions (id_status, uuid, name, description) VALUES (1, UUID(), 'Médico Titular', 'Médico responsable de área');

-- 4. INVENTARIO / LIMPIEZA
INSERT INTO cleaning_supplies_model (status_id, name, expiration_date, unit_measurement, stock_min, current_stock) VALUES (1, 'Cloro', '10/12/2028', 'ml', 20, 50);