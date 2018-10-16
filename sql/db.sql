CREATE DATABASE contracts;
CREATE TABLE contracts.contract
(
    id bigint(20) PRIMARY KEY NOT NULL,
    begin_date date,
    contract_date date,
    contractor varchar(255),
    end_date date,
    total_cost bigint(20)
);
CREATE TABLE contracts.stage
(
    id bigint(20) PRIMARY KEY NOT NULL,
    begin_date date,
    cost bigint(20),
    end_date date,
    name varchar(255),
    payment_date date,
    contract_id bigint(20) NOT NULL,
    CONSTRAINT FKp6difyxpp0w5ju1wk2wgfck4q FOREIGN KEY (contract_id) REFERENCES contracts.contract (id)
);
CREATE INDEX FKp6difyxpp0w5ju1wk2wgfck4q ON contracts.stage (contract_id);