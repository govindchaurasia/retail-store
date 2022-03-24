create table if not exists user_master
(
	id bigint primary key auto_increment,
    username varchar(200),
    user_type varchar(200),
    created_on timestamp default current_timestamp,
    updated_on timestamp
);

create table if not exists product
(
	id bigint primary key auto_increment,
    product_name varchar(200),
    category varchar(200),
    unit int,
    price int,
    created_on timestamp default current_timestamp,
    updated_on timestamp
);

create table if not exists invoice
(
	id bigint primary key auto_increment,
    user_id_fk bigint,
    total_before_discount int,
    final_amount int,
    discount_type varchar(200),
    discount int,
    invoice_date timestamp,
    created_on timestamp default current_timestamp,
	updated_on timestamp,
    
    CONSTRAINT fk_user_id FOREIGN KEY (user_id_fk) REFERENCES user_master (id)
);

create table if not exists invoice_line_items
(
	id bigint primary key auto_increment,
    invoice_id_fk bigint,
    product_id_fk bigint,
    product_qty int,
    product_price int,
    created_on timestamp default current_timestamp,
	updated_on timestamp,
    
    CONSTRAINT fk_invoice_id FOREIGN KEY (invoice_id_fk) REFERENCES invoice (id),
    CONSTRAINT fk_product_id FOREIGN KEY (product_id_fk) REFERENCES product (id)
);
