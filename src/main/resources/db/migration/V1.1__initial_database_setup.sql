create table customers
(
id bigserial not null
constraint pk_user
primary key,
type varchar(50) not null,
name varchar(50) not null,
registration_date date not null
);

create table licences
(
id uuid not null
constraint pk_licences
primary key,
create_date date not null,
end_date date not null,
private_key varchar(8000) not null,
customer_id bigint not null
constraint fk_40
references customers,
type varchar(255) not null,
number_of_licences bigint,
licence_key varchar(8000),
product_type varchar(255),
product_version varchar(255)
);

create table contacts
(
id bigserial not null
constraint pk_emails
primary key,
email varchar(50) not null,
customer_id bigint not null
constraint fk_43
references customers
);

insert into customers( type, name, registration_date) values
    ('company', 'FTC', '2020-12-12'),
    ('user', 'Vitya', '2020-01-20'),
    ('company', 'NSU', '2019-08-01') ;

create table expiring
(
id bigserial not null,
licence_id uuid not null,
user_id bigint not null
);

INSERT INTO licences (id, create_date, end_date, private_key, customer_id, type, number_of_licences, licence_key)
VALUES ('c2d29867-3d0b-d497-9191-18a9d8ee7830' , '2021-03-12', '2021-03-14', 'TEST', 1, 'TEST', 1, 'TEST');

create or replace procedure licence_to_expire()
language plpgsql
as $$
begin
  delete from expiring;

  insert into expiring (id, licence_id, user_id)
  select nextval('expiring_id_seq') as uuid, id as licenсe_id, customer_id
  from licences
  where current_date < end_date
  and  date_part('day', age(end_date, current_date)) < 7;

end;$$
