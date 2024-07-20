-- create schema if not exists catalogue;
--
-- create table catalogue.t_product
-- (
--     id        serial primary key,
--     c_title   varchar(50) not null check (length(trim(c_title)) >= 3),
--     c_details varchar(1000)
-- );

insert into catalogue.t_product (id, c_title, c_details)
VALUES (1, 'Product 1', 'Detail 1'),
       (2, 'Prod 2', 'Detail 1'),
       (3, 'Product 3', 'Detail 3'),
       (4, 'Duct 4', 'Detail 4');