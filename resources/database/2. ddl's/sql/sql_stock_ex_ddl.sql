-- Database: sql_stock_exchange

-- DROP DATABASE sql_stock_exchange;

drop table if exists based_in cascade;
drop table if exists company cascade;
drop table if exists country cascade;
drop table if exists listed_at cascade;
drop table if exists stock_exchange cascade;




create table company(
	id integer primary key,
	name varchar(64),
	year_established integer
);

create table country(
	code varchar(3) primary key,
	name varchar(64)
);

create table based_in(
	company_id integer,
	country_code varchar(3),
	primary key(company_id, country_code),
	
	foreign key(country_code) references country(code),
	foreign key(company_id) references company(id)
);

create table stock_exchange(
	code varchar(10) primary key,
	name varchar(64),
	country_code varchar(3),
	
	foreign key (country_code) references country(code)
);

create table listed_at(
	company_id integer,
	stock_exchange_code varchar(10),
	date_valid date,
	share_price numeric(10,2),
	share_cnt integer,
	
	foreign key (stock_exchange_code) references stock_exchange(code),
	foreign key (company_id) references company(id)
);


copy country
from 'C:\Users\Public\stock_exchange\country_data.csv'
delimiter ',';

copy company
from 'C:\Users\Public\stock_exchange\company_data.csv'
delimiter ',';

select * from country;
copy based_in
from 'C:\Users\Public\stock_exchange\based_in_data.csv'
delimiter ',';

copy stock_exchange
from 'C:\Users\Public\stock_exchange\stock_exchange_data.csv'
delimiter ',';

copy listed_at
from 'C:\Users\Public\stock_exchange\listed_at_data.csv'
delimiter ';';

