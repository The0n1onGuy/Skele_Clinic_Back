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
