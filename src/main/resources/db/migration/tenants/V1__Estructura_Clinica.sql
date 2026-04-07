create sequence ${tenant_Schema}.cleaning_supplies_model_seq start with 1 increment by 50;
create sequence ${tenant_Schema}.doctor_model_seq start with 1 increment by 50;
create sequence ${tenant_Schema}.lyr_employees_model_seq start with 1 increment by 50;
create sequence ${tenant_Schema}.morgue_seq start with 1 increment by 50;
create sequence ${tenant_Schema}.supplies_movements_model_seq start with 1 increment by 50;
create sequence ${tenant_Schema}.textile_articles_model_seq start with 1 increment by 50;
create sequence ${tenant_Schema}.textile_movements_model_seq start with 1 increment by 50;
create table ${tenant_Schema}.appointments (fecha_hora_fin datetime2(7), fecha_hora_inicio datetime2(7), id uniqueidentifier not null, patient_id uniqueidentifier, consultorio varchar(255), estado varchar(255) check ((estado in ('PENDIENTE','CONFIRMADA','CANCELADA','COMPLETADA'))), primary key (id));
create table ${tenant_Schema}.categories (id bigint identity not null, name varchar(50) not null, description TEXT, primary key (id));
create table ${tenant_Schema}.citas (fecha date, hora time, id bigint identity not null, especialista varchar(255), estado varchar(255), motivo varchar(255), paciente_nombre varchar(255), primary key (id));
create table ${tenant_Schema}.cleaning_supplies_model (current_stock int, stock_min int, id_supplies bigint not null, status_id bigint not null, name varchar(100) not null, expiration_date varchar(255), unit_measurement varchar(255), primary key (id_supplies));
create table ${tenant_Schema}.clinic_history (fecha_registro datetime2(7) not null, id uniqueidentifier not null, patient_id uniqueidentifier not null, motivo_consulta varchar(2000), padecimiento_actual varchar(5000), diagnostico_preliminar varchar(255), primary key (id));
create table ${tenant_Schema}.doctor_model (id bigint not null, apellido varchar(255), nombre varchar(255), primary key (id));
create table ${tenant_Schema}.lyr_employees_model (telefono int, id_empleado bigint not null, status_id bigint not null, apellido varchar(255), area varchar(255), nombre varchar(255), turno varchar(255), primary key (id_empleado));
create table ${tenant_Schema}.morgue (numero_gaveta int, id bigint not null, id_paciente bigint, id_paciente_local bigint, id_status bigint not null, causa_defuncion varchar(255), fecha_ingreso varchar(255), nombre_paciente_simulado varchar(255), primary key (id));
create table ${tenant_Schema}.patient (fecha_nacimiento date, id uniqueidentifier not null, apellidos varchar(255) not null, curp varchar(255) not null, direccion varchar(255), email varchar(255), genero varchar(255), nombre varchar(255) not null, telefono varchar(255), tipo_sangre varchar(255), primary key (id));
create table ${tenant_Schema}.rpbi_cat_clasification (id bigint identity not null, status_id bigint not null, color_code varchar(20), uuid varchar(36) not null, name varchar(50) not null, description varchar(255), primary key (id));
create table ${tenant_Schema}.rpbi_cat_containers (id bigint identity not null, id_status bigint not null, uuid varchar(36) not null, name varchar(100) not null, description varchar(255), primary key (id));
create table ${tenant_Schema}.rpbi_cat_phyisical_state (id bigint identity not null, status_id bigint not null, measure_unit varchar(10) not null, uuid varchar(36) not null, name varchar(50) not null, primary key (id));
create table ${tenant_Schema}.rpbi_compliance_matrix_nom087 (classification_id bigint not null, container_id bigint not null, id bigint identity not null, physical_state_id bigint not null, status_id bigint not null, primary key (id));
create table ${tenant_Schema}.rpbi_generation_record (quantity float(53) not null, classification_id bigint not null, container_id bigint not null, generation_date datetime2(7) not null, id bigint identity not null, physical_state_id bigint not null, status_id bigint not null, uuid varchar(36) not null, generation_area varchar(100) not null, responsible_user varchar(100) not null, primary key (id));
create table ${tenant_Schema}.rrhh_clinic_details (id_clinic_details bigint identity not null, id_employee bigint not null, id_status bigint not null, professional_license varchar(20), uuid varchar(36) not null, specialty varchar(100), graduation_institution varchar(150), primary key (id_clinic_details));
create table ${tenant_Schema}.rrhh_contracts (base_salary numeric(18,2) not null, id_contract bigint identity not null, id_employee bigint not null, id_status bigint not null, uuid varchar(36) not null, contract_type varchar(50) not null, hiring_date varchar(255) not null, termination_date varchar(255), primary key (id_contract));
create table ${tenant_Schema}.rrhh_departments (id_department bigint identity not null, id_status bigint not null, uuid UNIQUEIDENTIFIER not null, name varchar(100) not null, primary key (id_department));
create table ${tenant_Schema}.rrhh_employees (id_department bigint not null, id_employee bigint identity not null, id_position bigint not null, id_status bigint not null, uuid UNIQUEIDENTIFIER not null, curp varchar(255) not null, datebirth varchar(255) not null, datereg varchar(255) not null, gender varchar(255) not null, matname varchar(255) not null, name varchar(255) not null, patname varchar(255) not null, rfc varchar(255) not null, primary key (id_employee));
create table ${tenant_Schema}.rrhh_positions (id_position bigint identity not null, id_status bigint not null, uuid UNIQUEIDENTIFIER not null, name varchar(100) not null, description VARCHAR(MAX), primary key (id_position));
create table ${tenant_Schema}.rrhh_schedules (end_time time not null, start_time time not null, id_employee bigint not null, id_schedule bigint identity not null, id_status bigint not null, day_week varchar(15) not null, uuid UNIQUEIDENTIFIER not null, primary key (id_schedule));
create table ${tenant_Schema}.supplies_movements_model (cantidad int, employees_id bigint not null, id_movimiento bigint not null, supplies_id bigint not null, fecha_movimiento varchar(255), observaciones TEXT, tipo_movimiento varchar(255), primary key (id_movimiento));
create table ${tenant_Schema}.textile_articles_model (id_articles bigint not null, status_id bigint not null, descripcion varchar(255), name varchar(255), primary key (id_articles));
create table ${tenant_Schema}.textile_movements_model (amount int, articulo_id bigint not null, empleado_id bigint not null, id_motion bigint not null, motion_date varchar(255), observations varchar(255), type_motion varchar(255), primary key (id_motion));
create table ${tenant_Schema}.triages (frecuencia_cardiaca int, saturacion_oxigeno int, temperatura float(53), fecha_hora datetime2(7), id uniqueidentifier not null, patient_id uniqueidentifier not null, nivel varchar(255) check ((nivel in ('RESUSCITATION','EMERGENCY','URGENCY','LESS_URGENCY','NON_URGENCY'))), presion_arterial varchar(255), primary key (id));
create unique nonclustered index UKew5wyevkvulec1m6uqqjyx9hf on ${tenant_Schema}.morgue (id_paciente) where id_paciente is not null;
create table ${tenant_Schema}.status (id_status bigint identity not null, uuid varchar(36) not null, name varchar(255) not null, primary key (id_status));
alter table ${tenant_Schema}.status add constraint UKjay9oq3tlp3u1t3ly2rryl7aw unique (uuid);
alter table ${tenant_Schema}.status add constraint UKreccgx9nr0a8dwv201t44l6pd unique (name);
alter table ${tenant_Schema}.patient add constraint UKmx9n36weavbf3f9rudp1d4kxq unique (curp);
alter table ${tenant_Schema}.rpbi_cat_clasification add constraint UKkokft60e2mt5nnljb1f03fny1 unique (uuid);
alter table ${tenant_Schema}.rpbi_cat_containers add constraint UKtaeq2xafjm9o7mp6n4srf10km unique (uuid);
alter table ${tenant_Schema}.rpbi_cat_phyisical_state add constraint UK1ju7tdjpal3am5ir3bamrop unique (uuid);
alter table ${tenant_Schema}.rpbi_cat_phyisical_state add constraint UKfelyglt4nqhjgeevp6hxdrv8q unique (name);
alter table ${tenant_Schema}.rpbi_generation_record add constraint UKhpe43jib1m8mbpjekdia09cv unique (uuid);
alter table ${tenant_Schema}.rrhh_clinic_details add constraint UKeyvtte4daqer820t1pj6s1h20 unique (id_employee);
create unique nonclustered index UKjqmwxxxugx1rpdw7dl1y35kbc on ${tenant_Schema}.rrhh_clinic_details (professional_license) where professional_license is not null;
alter table ${tenant_Schema}.rrhh_clinic_details add constraint UKl8uw7tr1qtjji7vjoyu9pdx1b unique (uuid);
alter table ${tenant_Schema}.rrhh_contracts add constraint UK7vjpnyclxwrqot91pbxwu73dc unique (uuid);
alter table ${tenant_Schema}.appointments add constraint FKcl9b1a19a01yhjcdibna1gjl foreign key (patient_id) references ${tenant_Schema}.patient;
alter table ${tenant_Schema}.cleaning_supplies_model add constraint FKmdq084nnceaw7lcryg4fv4mwf foreign key (status_id) references ${tenant_Schema}.status;
alter table ${tenant_Schema}.clinic_history add constraint FK4ndp40tr0671esw3ainxj6sy8 foreign key (patient_id) references ${tenant_Schema}.patient;
alter table ${tenant_Schema}.lyr_employees_model add constraint FKfp03rf61nclx11mrbxb19jn31 foreign key (status_id) references ${tenant_Schema}.status;
alter table ${tenant_Schema}.morgue add constraint FKnt6w6felfnxfjhm0m4vohohx6 foreign key (id_status) references ${tenant_Schema}.status;
alter table ${tenant_Schema}.morgue add constraint FK86son91dymdclqmr2qykmsee foreign key (id_paciente) references ${tenant_Schema}.doctor_model;
alter table ${tenant_Schema}.rpbi_cat_clasification add constraint FKoxoj6tb7jk3cofmhc03xjslty foreign key (status_id) references ${tenant_Schema}.status;
alter table ${tenant_Schema}.rpbi_cat_containers add constraint FKajw19eji43mce5vw3sq9ge0ih foreign key (id_status) references ${tenant_Schema}.status;
alter table ${tenant_Schema}.rpbi_cat_phyisical_state add constraint FK40ks7qd81ygobtbksrwuqhbts foreign key (status_id) references ${tenant_Schema}.status;
alter table ${tenant_Schema}.rpbi_compliance_matrix_nom087 add constraint FK8ql38b0isbiqfhhfbif17qsp6 foreign key (classification_id) references ${tenant_Schema}.rpbi_cat_clasification;
alter table ${tenant_Schema}.rpbi_compliance_matrix_nom087 add constraint FKtmwhl5393c6xs4qqjfnboy478 foreign key (container_id) references ${tenant_Schema}.rpbi_cat_containers;
alter table ${tenant_Schema}.rpbi_compliance_matrix_nom087 add constraint FK1i621edrwam2lqfvlcsrp0dpl foreign key (physical_state_id) references ${tenant_Schema}.rpbi_cat_phyisical_state;
alter table ${tenant_Schema}.rpbi_compliance_matrix_nom087 add constraint FKjgwm0swodfiggxkqw463ja8s1 foreign key (status_id) references ${tenant_Schema}.status;
alter table ${tenant_Schema}.rpbi_generation_record add constraint FKlry259i1dqr7xhe8mifnna8ie foreign key (classification_id) references ${tenant_Schema}.rpbi_cat_clasification;
alter table ${tenant_Schema}.rpbi_generation_record add constraint FKkivyoralqlaisioom0mqwgtn4 foreign key (container_id) references ${tenant_Schema}.rpbi_cat_containers;
alter table ${tenant_Schema}.rpbi_generation_record add constraint FK45ujx6bfm8eaalmr9yr97fbsn foreign key (physical_state_id) references ${tenant_Schema}.rpbi_cat_phyisical_state;
alter table ${tenant_Schema}.rpbi_generation_record add constraint FK6ea89sxse64l1nl9yijsi62uu foreign key (status_id) references ${tenant_Schema}.status;
alter table ${tenant_Schema}.rrhh_clinic_details add constraint FKr10a4by8qijrlbrhi0opwh54v foreign key (id_employee) references ${tenant_Schema}.rrhh_employees;
alter table ${tenant_Schema}.rrhh_clinic_details add constraint FKqihnk5t599war6hyw0dhkigrs foreign key (id_status) references ${tenant_Schema}.status;
alter table ${tenant_Schema}.rrhh_contracts add constraint FKc5f9w36b5x2kl507iamt7cahd foreign key (id_employee) references ${tenant_Schema}.rrhh_employees;
alter table ${tenant_Schema}.rrhh_contracts add constraint FKo831ys17g2edamgvd3s5oecrk foreign key (id_status) references ${tenant_Schema}.status;
alter table ${tenant_Schema}.rrhh_departments add constraint FKh2avpfx781enwaruupih4plnx foreign key (id_status) references ${tenant_Schema}.status;
alter table ${tenant_Schema}.rrhh_employees add constraint FKajp5fjxjhxk50a6kyc2f6pwu0 foreign key (id_department) references ${tenant_Schema}.rrhh_departments;
alter table ${tenant_Schema}.rrhh_employees add constraint FKaks2xpunpcw1cact0lwrsgs5m foreign key (id_position) references ${tenant_Schema}.rrhh_positions;
alter table ${tenant_Schema}.rrhh_employees add constraint FKi2275x6d17r0ept8n5yvkh1wd foreign key (id_status) references ${tenant_Schema}.status;
alter table ${tenant_Schema}.rrhh_positions add constraint FK13dxlbf5ik6swv54jmcb65val foreign key (id_status) references ${tenant_Schema}.status;
alter table ${tenant_Schema}.rrhh_schedules add constraint FK2deacoa7jgaepv77goof68kxn foreign key (id_employee) references ${tenant_Schema}.rrhh_employees;
alter table ${tenant_Schema}.rrhh_schedules add constraint FKivave5g2wewbw8lg5b38pa0nh foreign key (id_status) references ${tenant_Schema}.status;
alter table ${tenant_Schema}.supplies_movements_model add constraint FK47h1y77iql9em1b0paf5tgey3 foreign key (employees_id) references ${tenant_Schema}.lyr_employees_model;
alter table ${tenant_Schema}.supplies_movements_model add constraint FK6mtujppgykd6g0rp8ahjve061 foreign key (supplies_id) references ${tenant_Schema}.cleaning_supplies_model;
alter table ${tenant_Schema}.textile_articles_model add constraint FKfa1ewycoree4s3h1i2jfh8tms foreign key (status_id) references ${tenant_Schema}.status;
alter table ${tenant_Schema}.textile_movements_model add constraint FK8q7u5608r8pqnsk24c94kqa90 foreign key (empleado_id) references ${tenant_Schema}.lyr_employees_model;
alter table ${tenant_Schema}.textile_movements_model add constraint FKmqwkcu4cc10lhpog79ctb08b3 foreign key (articulo_id) references ${tenant_Schema}.textile_articles_model;
alter table ${tenant_Schema}.triages add constraint FKpmr4nldkp9t4aetyrbls8jvs3 foreign key (patient_id) references ${tenant_Schema}.patient;
-- REPLICA DEL DICCIONARIO DE ESTATUS (Para cada hospital)
INSERT INTO ${tenant_schema}.status (uuid, name) VALUES ('C2A1B3A0-3E12-4C10-8A51-1A2B3C4D5E60', 'Active');
INSERT INTO ${tenant_schema}.status (uuid, name) VALUES ('D4B2C4B1-4F23-5D21-9B62-2B3C4D5E6F71', 'Inactive');

-- DICCIONARIOS CLÍNICOS (RPBI) - Nombres exactos de la exportación
INSERT INTO ${tenant_schema}.rpbi_cat_clasification (status_id, uuid, name, description)
VALUES (1, '11111111-1111-1111-1111-111111111111', 'Punzocortantes', 'Agujas de jeringas, hojas de bisturí...');

INSERT INTO ${tenant_schema}.rpbi_cat_phyisical_state (status_id, measure_unit, uuid, name)
VALUES (1, 'Kilogramos', '22222222-2222-2222-2222-222222222222', 'Sólido');

INSERT INTO ${tenant_schema}.rpbi_cat_containers (id_status, uuid, name, description)
VALUES (1, '33333333-3333-3333-3333-333333333333', 'Recipiente Rígido Rojo', 'Contenedor de polipropileno');

INSERT INTO ${tenant_schema}.rpbi_compliance_matrix_nom087 (classification_id, container_id, physical_state_id, status_id)
VALUES (1, 1, 1, 1);

-- DICCIONARIOS CLÍNICOS (RRHH) - Uso de NEWID() para UNIQUEIDENTIFIER
INSERT INTO ${tenant_schema}.rrhh_departments (id_status, uuid, name)
VALUES (1, NEWID(), 'Medicina General');

INSERT INTO ${tenant_schema}.rrhh_positions (id_status, uuid, name, description)
VALUES (1, NEWID(), 'Médico Titular', 'Médico responsable de área');

-- INVENTARIO / LIMPIEZA - Uso estricto de id_supplies
INSERT INTO ${tenant_schema}.cleaning_supplies_model (id_supplies, status_id, name, expiration_date, unit_measurement, stock_min, current_stock)
VALUES (NEXT VALUE FOR ${tenant_schema}.cleaning_supplies_model_seq, 1, 'Cloro', '10/12/2028', 'ml', 20, 50);