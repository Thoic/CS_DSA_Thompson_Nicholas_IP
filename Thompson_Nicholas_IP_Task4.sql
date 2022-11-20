DROP TABLE IF EXISTS requests_repair
DROP TABLE IF EXISTS complaint_repair
DROP TABLE IF EXISTS have
DROP TABLE IF EXISTS files
DROP TABLE IF EXISTS repairs
DROP TABLE IF EXISTS tests
DROP TABLE IF EXISTS makes
DROP TABLE IF EXISTS records_cost3
DROP TABLE IF EXISTS records_cost2
DROP TABLE IF EXISTS records_cost1
DROP TABLE IF EXISTS purchases
DROP TABLE IF EXISTS accident
DROP TABLE IF EXISTS complaint
DROP TABLE IF EXISTS customer
DROP TABLE IF EXISTS product3_account
DROP TABLE IF EXISTS product2_account
DROP TABLE IF EXISTS product1_account
DROP TABLE IF EXISTS product3
DROP TABLE IF EXISTS product2
DROP TABLE IF EXISTS product1
DROP TABLE IF EXISTS product
DROP TABLE IF EXISTS graduate_technical_staff
DROP TABLE IF EXISTS technical_staff
DROP TABLE IF EXISTS worker
DROP TABLE IF EXISTS quality_controller

CREATE TABLE quality_controller(
    qname VARCHAR(64) PRIMARY KEY,
    address VARCHAR(64),
    salary DECIMAL(18,2),
    product_type VARCHAR(64)
)
CREATE INDEX quality_controller_salary_index on quality_controller(salary)

CREATE TABLE worker(
    wname VARCHAR(64) PRIMARY KEY,
    address VARCHAR(64),
    salary DECIMAL(18,2),
    max_products_per_day INT
)
CREATE INDEX worker_salary_index on worker(salary)

CREATE TABLE technical_staff(
    sname VARCHAR(64) PRIMARY KEY,
    address VARCHAR(64),
    salary DECIMAL(18,2),
    technical_position VARCHAR(64)
)
CREATE INDEX technical_staff_salary_index on technical_staff(salary)

CREATE TABLE graduate_technical_staff(
    sname VARCHAR(64),
    degree VARCHAR(64),
    CONSTRAINT PK_graduate_technical_staff PRIMARY KEY(sname, degree),
    CONSTRAINT FK_sname FOREIGN KEY (sname) REFERENCES technical_staff,
    CONSTRAINT CHK_degree CHECK (degree IN ('BS', 'MS', 'PhD')) --check taht the degree name is BS, MS or PhD
)

CREATE TABLE product(
    product_id INT PRIMARY KEY,
    size VARCHAR(8),
    CONSTRAINT CHK_size CHECK (size IN ('small', 'medium', 'large')) --product can only be small, medium or large
)

CREATE TABLE product1(
    product_id INT PRIMARY KEY,
    software_name VARCHAR(64),
    CONSTRAINT FK_product_id_product1 FOREIGN KEY (product_id) REFERENCES PRODUCT
)

CREATE TABLE product2(
    product_id INT PRIMARY KEY,
    color VARCHAR(64),
    CONSTRAINT FK_product_id_product2 FOREIGN KEY (product_id) REFERENCES PRODUCT,
)
CREATE INDEX product2_color_index ON product2(color)

CREATE TABLE product3(
    product_id INT PRIMARY KEY,
    weight DECIMAL(18,2),
    CONSTRAINT FK_product_id_product3 FOREIGN KEY (product_id) REFERENCES PRODUCT
)

CREATE TABLE product1_account(
    account_no INT PRIMARY KEY,
    date_established DATE,
    cost DECIMAL(18,2)
)

CREATE TABLE product2_account(
    account_no INT PRIMARY KEY,
    date_established DATE,
    cost DECIMAL(18,2)
)

CREATE TABLE product3_account(
    account_no INT PRIMARY KEY,
    date_established DATE,
    cost DECIMAL(18,2)
)

CREATE TABLE customer(
    cname VARCHAR(64) PRIMARY KEY,
    address VARCHAR(64)
)

CREATE TABLE complaint(
    complaint_id INT PRIMARY KEY,
    description VARCHAR(64),
    treatment_expected VARCHAR(64)
)

CREATE TABLE accident(
    accident_no INT PRIMARY KEY,
    days_lost INT
)

CREATE TABLE purchases(
    product_id INT PRIMARY KEY,
    cname VARCHAR(64),
    CONSTRAINT FK_product_id_purchases FOREIGN KEY (product_id) REFERENCES product
)
CREATE INDEX purchases_cname_index ON purchases(product_id, cname)

CREATE TABLE records_cost1(
    product_id INT PRIMARY KEY,
    account_no INT UNIQUE,
    CONSTRAINT FK_product_id_records_cost1 FOREIGN KEY (product_id) REFERENCES product1,
    CONSTRAINT FK_account_no_records_cost1 FOREIGN KEY (account_no) REFERENCES product1_account
)
--no need to create index for account_no, it is automatically created with UNIQUE specifier

CREATE TABLE records_cost2(
    product_id INT PRIMARY KEY,
    account_no INT UNIQUE,
    CONSTRAINT FK_product_id_records_cost2 FOREIGN KEY (product_id) REFERENCES product2,
    CONSTRAINT FK_account_no_records_cost2 FOREIGN KEY (account_no) REFERENCES product2_account
)

CREATE TABLE records_cost3(
    product_id INT PRIMARY KEY,
    account_no INT UNIQUE,
    CONSTRAINT FK_product_id_records_cost3 FOREIGN KEY (product_id) REFERENCES product3,
    CONSTRAINT FK_account_no_records_cost3 FOREIGN KEY (account_no) REFERENCES product3_account
)

CREATE TABLE makes(
    product_id INT PRIMARY KEY,
    wname VARCHAR(64),
    prod_date DATE,
    prod_dur DECIMAL(18,2),
    CONSTRAINT FK_product_id_makes FOREIGN KEY (product_id) REFERENCES product,
    CONSTRAINT FK_wname_makes FOREIGN KEY (wname) REFERENCES worker
)
CREATE INDEX makes_wname_index ON makes(wname)

CREATE TABLE tests(
    product_id INT PRIMARY KEY,
    qname VARCHAR(64),
    CONSTRAINT FK_product_id_tests FOREIGN KEY (product_id) REFERENCES product,
    CONSTRAINT FK_qname_tests FOREIGN KEY (qname) REFERENCES quality_controller
)
CREATE INDEX tests_qname_index ON tests(qname)

CREATE TABLE repairs(
    product_id INT PRIMARY KEY,
    sname VARCHAR(64),
    date DATE,
    CONSTRAINT FK_product_id_repairs FOREIGN KEY (product_id) REFERENCES product,
    CONSTRAINT FK_sname_repairs FOREIGN KEY (sname) REFERENCES technical_staff
)

CREATE TABLE files(
    complaint_id INT PRIMARY KEY,
    product_id INT,
    date DATE,
    CONSTRAINT FK_complaint_id_files FOREIGN KEY (complaint_id) REFERENCES complaint,
    CONSTRAINT FK_product_id_files FOREIGN KEY (product_id) REFERENCES product
)
CREATE INDEX files_product_id_index ON files(product_id)

CREATE TABLE have(
    product_id INT,
    accident_no INT PRIMARY KEY,
    date DATE,
    CONSTRAINT FK_product_id_have FOREIGN KEY (product_id) REFERENCES product,
    CONSTRAINT FK_accident_no_have FOREIGN KEY (accident_no) REFERENCES accident
)
CREATE INDEX have_accident_date_index ON have(accident_no, date)

CREATE TABLE complaint_repair(
    product_id INT PRIMARY KEY,
    complaint_id INT UNIQUE,
    CONSTRAINT FK_product_id_complaint_repair FOREIGN KEY (product_id) REFERENCES product,
    CONSTRAINT FK_complaint_id_complaint_repair FOREIGN KEY (complaint_id) REFERENCES complaint
)

CREATE TABLE requests_repair(
    product_id INT PRIMARY KEY,
    qname VARCHAR(64),
    CONSTRAINT FK_product_id_requests_repair FOREIGN KEY (product_id) REFERENCES product,
    CONSTRAINT FK_qname_requests_repair FOREIGN KEY (qname) REFERENCES quality_controller
)