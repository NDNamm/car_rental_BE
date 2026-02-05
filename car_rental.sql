create database car_rental;

use car_rental;

create table role(
     id bigint auto_increment primary key,
     role_name varchar(15)
);

create table users(
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    -- Thông tin đăng nhập
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    -- Thông tin cá nhân
    full_name VARCHAR(100) NOT NULL,
    avatar_url VARCHAR(255),
    date_of_birth DATE,
    gender ENUM('MALE','FEMALE','OTHER'),
    -- Liên hệ
    email VARCHAR(100),
    phone_number VARCHAR(20),
    -- Trạng thái & phân quyền
    role_id VARCHAR(20) DEFAULT 'USER',
    verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE verifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    -- Ảnh giấy tờ
    cccd_image_url VARCHAR(255) NOT NULL,
    license_image_url VARCHAR(255) NOT NULL,
    -- Thông tin GPLX
    license_number VARCHAR(50) NOT NULL,
    license_type ENUM('A1','A2','B1','B2') NOT NULL,
    license_expired_date DATE NOT NULL,
    status ENUM('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',
    note VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);



