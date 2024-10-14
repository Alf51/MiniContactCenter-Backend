CREATE TABLE PERSONS
(
    id       int primary key auto_increment not null,
    login    VARCHAR(255)                   NOT NULL,
    password VARCHAR(255)                   NOT NULL,
    role     VARCHAR(255)                   NOT NULL
);

INSERT INTO PERSONS (login, password, role)
VALUES ('dok', '$2a$10$cJT47aEfyHHIvNYGu8tLruUrY6lhgPqibZ8EgAlIbxh48gH4jlv5a', 'ADMIN'),
('grad', '$2a$10$cJT47aEfyHHIvNYGu8tLruUrY6lhgPqibZ8EgAlIbxh48gH4jlv5a', 'ADMIN');




