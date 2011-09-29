CREATE TABLE tbl_uniq_id (
    id INTEGER NOT NULL PRIMARY KEY
) ENGINE=InnoDB;

############################################

CREATE TABLE tbl_user_orders (
    order_id BIGINT UNSIGNED NOT NULL PRIMARY KEY,
    surname VARCHAR(2) NOT NULL,
    gender tinyint DEFAULT 0,  #  0 - unknown, 1 - male, 2 - female
    phone_number VARCHAR(20) NOT NULL,
    start_address VARCHAR(60) NOT NULL,
    end_address VARCHAR(60) NOT NULL,
    expected_time CHAR(12) default NULL,   # NULL means "now"
    passenger_count tinyint DEFAULT 1,
    is_vip tinyint DEFAULT 0,
    notes VARCHAR(60) NOT NULL,    
    creation_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    taxi_plate VARCHAR(12),
    result SMALLINT UNSIGNED NOT NULL DEFAULT 0  #  0 - record just created, 1 - seeking for taxi, 2 - taxi assigned but still waiting, 4 - got taxi, 8 - no taxi available, 16 - taxi driver failed to come, ...
) ENGINE=InnoDB;


####### 出租车位置 ##########
CREATE TABLE tbl_taxi_company_orders (
    order_id BIGINT UNSIGNED NOT NULL PRIMARY KEY,
    latest_lat INT UNSIGNED,
    latest_lng INT UNSIGNED,
    is_empty tinyint UNSIGNED DEFAULT 0
) ENGINE=InnoDB;


############################################
CREATE TABLE tbl_user_taxi_company_order_mapping (
    user_order_id BIGINT UNSIGNED NOT NULL PRIMARY KEY,
    taxi_company_order_id BIGINT UNSIGNED NOT NULL
) ENGINE=InnoDB;

ALTER TABLE `tbl_user_taxi_company_order_mapping` ADD CONSTRAINT `FK_user_order_id` FOREIGN KEY (`user_order_id`) REFERENCES `tbl_user_orders` (`order_id`) ON DELETE CASCADE ON UPDATE RESTRICT; 
ALTER TABLE `tbl_user_taxi_company_order_mapping` ADD CONSTRAINT `FK_taxi_company_order_id` FOREIGN KEY (`taxi_company_order_id`) REFERENCES `tbl_taxi_company_orders` (`order_id`) ON DELETE CASCADE ON UPDATE RESTRICT; 


