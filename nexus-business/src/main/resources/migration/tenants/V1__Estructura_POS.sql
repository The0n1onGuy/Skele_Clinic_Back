CREATE TABLE status (id_status bigint AUTO_INCREMENT NOT NULL, uuid varchar(36) not null, name varchar(255) not null, primary key (id_status));

-- Start creating the first important models (Status is from core)
CREATE TABLE pos_category (
                              id bigint AUTO_INCREMENT NOT NULL,
                              name varchar(50) NOT NULL,
                              description TEXT, -- MySQL compliant for @Lob
                              PRIMARY KEY (id)
);

-- Master Product Table
CREATE TABLE pos_products (
                              id bigint AUTO_INCREMENT NOT NULL,
                              uuid varchar(36) NOT NULL, -- UUID stored as VARCHAR(36)
                              sku varchar(50) NOT NULL, -- Barcode index for scanners
                              name varchar(150) NOT NULL,
                              base_price decimal(10, 2) NOT NULL, -- Precision for financial safety
                              base_description TEXT,
                              category_id bigint NOT NULL,
                              status_id bigint NOT NULL,
                              PRIMARY KEY (id),
                              UNIQUE (uuid),
                              UNIQUE (sku)
);

-- Physical stock management
CREATE TABLE pos_inventory (
                               id bigint AUTO_INCREMENT NOT NULL,
                               product_id bigint NOT NULL,
                               current_stock int NOT NULL DEFAULT 0,
                               min_stock int NOT NULL DEFAULT 5,
                               last_restock_date DATETIME,
                               PRIMARY KEY (id),
                               UNIQUE (product_id)
);

-- Sales Header (The Voucher/Ticket)
CREATE TABLE pos_sales (
                           id bigint AUTO_INCREMENT NOT NULL,
                           uuid varchar(36) NOT NULL,
                           ticket_number varchar(20) NOT NULL,
                           total_amount decimal(12, 2) NOT NULL,
                           tax_amount decimal(12, 2),
                           sale_date DATETIME NOT NULL,
                           payment_method varchar(50),
                           status_id bigint NOT NULL,
                           PRIMARY KEY (id),
                           UNIQUE (uuid),
                           UNIQUE (ticket_number)
);

-- Sale Body (Items per ticket)
CREATE TABLE pos_sale_details (
                                  id bigint AUTO_INCREMENT NOT NULL,
                                  sale_id bigint NOT NULL,
                                  product_id bigint NOT NULL,
                                  sold_price decimal(10, 2) NOT NULL, -- Historical price protection
                                  quantity int NOT NULL,
                                  subtotal decimal(12, 2) NOT NULL,
                                  PRIMARY KEY (id)
);

-- Audit trail for stock changes
CREATE TABLE pos_inventory_movements (
                                         id bigint AUTO_INCREMENT NOT NULL,
                                         product_id bigint NOT NULL,
                                         type varchar(20) NOT NULL, -- SALE, RESTOCK, DAMAGE, RETURN
                                         quantity int NOT NULL,
                                         reason TEXT,
                                         movement_date DATETIME NOT NULL,
                                         PRIMARY KEY (id)
);
--
--
--
--
--
--
CREATE TABLE pos_offers (
                            id bigint AUTO_INCREMENT NOT NULL,
                            name varchar(255) NOT NULL,
                            discount_type varchar(20) NOT NULL, -- 'PERCENTAGE' or 'FIXED'
                            discount_value decimal(10, 2) NOT NULL,
                            active tinyint(1) NOT NULL DEFAULT 1, -- Boolean representation in MySQL
                            start_date DATETIME,
                            end_date DATETIME,
                            PRIMARY KEY (id)
);

-- Many-to-Many Bridge Table for Offers <-> Categories
CREATE TABLE pos_offer_categories (
                                      offer_id bigint NOT NULL,
                                      category_id bigint NOT NULL,
                                      PRIMARY KEY (offer_id, category_id)
);

-- Many-to-Many Bridge Table for Offers <-> Products
CREATE TABLE pos_offer_products (
                                    offer_id bigint NOT NULL,
                                    product_id bigint NOT NULL,
                                    PRIMARY KEY (offer_id, product_id)
);

-- ----------------------------------------------------------
-- NEW: BUNDLES AND PACKAGES
-- ----------------------------------------------------------
CREATE TABLE pos_bundles (
                             id bigint AUTO_INCREMENT NOT NULL,
                             name varchar(255) NOT NULL,
                             reference_sku varchar(100) UNIQUE,
                             description TEXT,
                             is_seasonal_offer tinyint(1) DEFAULT 0,
                             total_normal_value decimal(12, 2),
                             bundle_price decimal(12, 2),
                             PRIMARY KEY (id)
);

-- Bridge Table mapping Products inside a Bundle
CREATE TABLE pos_bundle_lines (
                                  id bigint AUTO_INCREMENT NOT NULL,
                                  bundle_id_fk bigint NOT NULL,
                                  product_id_fk bigint NOT NULL,
                                  quantity int NOT NULL DEFAULT 1,
                                  PRIMARY KEY (id)
);
alter table status add constraint UKjay9oq3tlp3u1t3ly2rryl7aw unique (uuid);
alter table status add constraint UKreccgx9nr0a8dwv201t44l6pd unique (name);

-- Foreign Keys for Offers (Categories & Products)
ALTER TABLE pos_offer_categories ADD CONSTRAINT FK_offercat_offer FOREIGN KEY (offer_id) REFERENCES pos_offers(id);
ALTER TABLE pos_offer_categories ADD CONSTRAINT FK_offercat_category FOREIGN KEY (category_id) REFERENCES pos_category(id);

ALTER TABLE pos_offer_products ADD CONSTRAINT FK_offerprod_offer FOREIGN KEY (offer_id) REFERENCES pos_offers(id);
ALTER TABLE pos_offer_products ADD CONSTRAINT FK_offerprod_product FOREIGN KEY (product_id) REFERENCES pos_products(id);

-- Foreign Keys for Bundle Lines
ALTER TABLE pos_bundle_lines ADD CONSTRAINT FK_bundleline_bundle FOREIGN KEY (bundle_id_fk) REFERENCES pos_bundles(id);
ALTER TABLE pos_bundle_lines ADD CONSTRAINT FK_bundleline_product FOREIGN KEY (product_id_fk) REFERENCES pos_products(id);
--
--
--
--
--
-- Foreign Keys for Product
ALTER TABLE pos_products ADD CONSTRAINT FK_product_category FOREIGN KEY (category_id) REFERENCES pos_category(id);
ALTER TABLE pos_products ADD CONSTRAINT FK_product_status FOREIGN KEY (status_id) REFERENCES status(id_status);

-- Foreign Keys for Inventory and Sales
ALTER TABLE pos_inventory ADD CONSTRAINT FK_inventory_product FOREIGN KEY (product_id) REFERENCES pos_products(id);
ALTER TABLE pos_sales ADD CONSTRAINT FK_sale_status FOREIGN KEY (status_id) REFERENCES status(id_status);

-- Foreign Keys for Details
ALTER TABLE pos_sale_details ADD CONSTRAINT FK_detail_sale FOREIGN KEY (sale_id) REFERENCES pos_sales(id);
ALTER TABLE pos_sale_details ADD CONSTRAINT FK_detail_product FOREIGN KEY (product_id) REFERENCES pos_products(id);

-- Foreign Keys for Audit
ALTER TABLE pos_inventory_movements ADD CONSTRAINT FK_movement_product FOREIGN KEY (product_id) REFERENCES pos_products(id);

-- Populate the status tables (try to keep them the same as others)
INSERT INTO status (id_status, uuid, name) VALUES (1, 'C2A1B3A0-3E12-4C10-8A51-1A2B3C4D5E60', 'Active');
INSERT INTO status (id_status, uuid, name) VALUES (2, 'D4B2C4B1-4F23-5D21-9B62-2B3C4D5E6F71', 'Inactive');
INSERT INTO status (id_status, uuid, name) VALUES (3, 'E5C3D5C2-5034-6E32-AC73-3C4D5E6F7082', 'Edited');
INSERT INTO status (id_status, uuid, name) VALUES (4, 'F6D4E6D3-6145-7F43-BD84-4D5E6F708193', 'Deleted');
INSERT INTO status (id_status, uuid, name) VALUES (5, '07E5F7E4-7256-8054-CE95-5E6F708192A4', 'Added');

-- ==============================================================================
-- FASE 2: RESILIENCIA E IDEMPOTENCIA
-- ==============================================================================
-- Tabla de Idempotencia para registrar los eventos de mensajería ya procesados (Idempotent Consumer).
-- Evita ejecuciones duplicadas de scripts de Flyway o transacciones redundantes ante re-entregas de RabbitMQ.
CREATE TABLE processed_events (
    event_id VARCHAR(36) PRIMARY KEY,
    event_type VARCHAR(255) NOT NULL,
    processed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- ==============================================================================
-- FASE 3: ROLES Y PERMISOS DEL PUNTO DE VENTA (POS)
-- ==============================================================================

-- Tabla de Roles del POS
CREATE TABLE pos_roles (
    id_role BIGINT AUTO_INCREMENT NOT NULL,
    uuid VARCHAR(36) NOT NULL,
    role_name VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL,
    status_id BIGINT NOT NULL,
    PRIMARY KEY (id_role),
    CONSTRAINT UK_pos_roles_uuid UNIQUE (uuid),
    CONSTRAINT UK_pos_roles_name UNIQUE (role_name),
    CONSTRAINT FK_pos_roles_status FOREIGN KEY (status_id) REFERENCES status (id_status)
);

-- Tabla de Permisos del POS
CREATE TABLE pos_permissions (
     id_permission BIGINT AUTO_INCREMENT NOT NULL,
     uuid VARCHAR(36) NOT NULL,
     permission_name VARCHAR(100) NOT NULL,
     description VARCHAR(255) NOT NULL,
     PRIMARY KEY (id_permission),
     CONSTRAINT UK_pos_permissions_uuid UNIQUE (uuid),
     CONSTRAINT UK_pos_permissions_name UNIQUE (permission_name)
);

-- Tabla de Asociación Roles y Permisos (Muchos a Muchos)
CREATE TABLE pos_role_permissions (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT FK_pos_rp_role FOREIGN KEY (role_id) REFERENCES pos_roles (id_role) ON DELETE CASCADE,
    CONSTRAINT FK_pos_rp_permission FOREIGN KEY (permission_id) REFERENCES pos_permissions (id_permission) ON DELETE CASCADE
);

-- ==============================================================================
-- INSERCIÓN DE CATÁLOGOS BASE (SEMILLADO ESTÁTICO DE SEGURIDAD)
-- ==============================================================================

-- 1. Insertar Permisos Base de POS (Tienda, Restaurante, Supermercado)
INSERT INTO pos_permissions (id_permission, uuid, permission_name, description) VALUES
    (1, 'P1111111-1111-1111-1111-111111111111', 'CATALOG_VIEW', 'Visualizar catálogo de productos, categorías y precios'),
    (2, 'P2222222-2222-2222-2222-222222222222', 'CATALOG_MANAGE', 'Crear, editar y eliminar productos del catálogo'),
    (3, 'P3333333-3333-3333-3333-333333333333', 'SALES_CREATE', 'Registrar ventas y procesar cobros básicos'),
    (4, 'P4444444-4444-4444-4444-444444444444', 'SALES_VOID', 'Anular transacciones de venta completadas y tickets emitidos'),
    (5, 'P5555555-5555-5555-5555-555555555555', 'SALES_DISCOUNT', 'Aplicar descuentos discrecionales y cortesías'),
    (6, 'P6666666-6666-6666-6666-666666666666', 'CASH_DRAWER_OPEN', 'Abrir cajón de efectivo fuera de venta y realizar arqueos'),
    (7, 'P7777777-7777-7777-7777-777777777777', 'CASH_DRAWER_CLOSE', 'Realizar cortes de caja finales de turno y cierres'),
    (8, 'P8888888-8888-8888-8888-888888888888', 'INVENTORY_VIEW', 'Visualizar niveles de stock e inventario actual'),
    (9, 'P9999999-9999-9999-9999-999999999999', 'INVENTORY_MANAGE', 'Ajustar stock, registrar órdenes de compra y recepciones'),
    (10, 'PAAAAAAA-AAAA-AAAA-AAAA-AAAAAAAAAAAA', 'TABLES_MANAGE', 'Gestionar layout de mesas, zonas y reservas (Restaurante)'),
    (11, 'PBBBBBBB-BBBB-BBBB-BBBB-BBBBBBBBBBBB', 'ORDERS_TAKE', 'Tomar comandas a mesas o pedidos para llevar (Restaurante)'),
    (12, 'PCCCCCCC-CCCC-CCCC-CCCC-CCCCCCCCCCCC', 'KITCHEN_VIEW', 'Visualizar órdenes y tiempos de preparación en pantalla de cocina'),
    (13, 'PDDDDDDD-DDDD-DDDD-DDDD-DDDDDDDDDDDD', 'REPORTS_VIEW', 'Visualizar reportes financieros, cortes generales y auditorías'),
    (14, 'PEEEEEEE-EEEE-EEEE-EEEE-EEEEEEEEEEEE', 'USERS_MANAGE', 'Gestionar personal del tenant, asignando roles y permisos locales');

-- 2. Insertar Roles Base del POS (Asociados al id_status = 1 'Active' del V1)
INSERT INTO pos_roles (id_role, uuid, role_name, description, status_id) VALUES
    (1, 'R1111111-1111-1111-1111-111111111111', 'ROLE_POS_ADMIN', 'Administrador General del Punto de Venta (Control total)', 1),
    (2, 'R2222222-2222-2222-2222-222222222222', 'ROLE_POS_GERENTE', 'Gerente de Sucursal (Operaciones y catálogo local)', 1),
    (3, 'R3333333-3333-3333-3333-333333333333', 'ROLE_POS_CAJERO', 'Cajero de Punto de Venta (Registro y cobro)', 1),
    (4, 'R4444444-4444-4444-4444-444444444444', 'ROLE_POS_SUPERVISOR', 'Supervisor de Cajas (Autorizaciones y reembolsos)', 1),
    (5, 'R5555555-5555-5555-5555-555555555555', 'ROLE_POS_INVENTARIOS', 'Encargado de Almacén e Inventarios', 1),
    (6, 'R6666666-6666-6666-6666-666666666666', 'ROLE_POS_MESERO', 'Personal de servicio de mesas (Comandas)', 1),
    (7, 'R7777777-7777-7777-7777-777777777777', 'ROLE_POS_COCINERO', 'Personal de producción de alimentos y bebidas', 1),
    (8, 'R8888888-8888-8888-8888-888888888888', 'ROLE_POS_AUDITOR', 'Auditor de Ventas y Finanzas (Acceso de lectura)', 1);

-- 3. Mapear Permisos a Roles (Asociaciones ACID y Seguras)

-- ROLE_POS_ADMIN (Acceso total)
INSERT INTO pos_role_permissions (role_id, permission_id) VALUES
    (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), (1, 11), (1, 12), (1, 13), (1, 14);

-- ROLE_POS_GERENTE (Acceso total excepto gestión crítica de usuarios/permisos)
INSERT INTO pos_role_permissions (role_id, permission_id) VALUES
    (2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6), (2, 7), (2, 8), (2, 9), (2, 10), (2, 11), (2, 12), (2, 13);

-- ROLE_POS_CAJERO (Cobros básicos, visualiza catálogo y stock)
INSERT INTO pos_role_permissions (role_id, permission_id) VALUES
    (3, 1), (3, 3), (3, 8);

-- ROLE_POS_SUPERVISOR (Control operacional de cajas y autorizaciones especiales)
INSERT INTO pos_role_permissions (role_id, permission_id) VALUES
    (4, 1), (4, 3), (4, 4), (4, 5), (4, 6), (4, 8);

-- ROLE_POS_INVENTARIOS (Gestión de stock, almacén y catálogo)
INSERT INTO pos_role_permissions (role_id, permission_id) VALUES
    (5, 1), (5, 2), (5, 8), (5, 9);

-- ROLE_POS_MESERO (Mesas, órdenes y comanda)
INSERT INTO pos_role_permissions (role_id, permission_id) VALUES
    (6, 1), (6, 10), (6, 11);

-- ROLE_POS_COCINERO (Pantalla de cocina únicamente)
INSERT INTO pos_role_permissions (role_id, permission_id) VALUES
    (7, 12);

-- ROLE_POS_AUDITOR (Reportes y lecturas únicamente)
INSERT INTO pos_role_permissions (role_id, permission_id) VALUES
    (8, 1), (8, 8), (8, 13);

