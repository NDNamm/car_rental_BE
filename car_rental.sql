create database car_rental;

use car_rental;

create table role(
     id bigint auto_increment primary key,
     role_name varchar(15)
);

create table users(
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_id bigint,
    -- Thông tin đăng nhập
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    -- Thông tin cá nhân
    full_name VARCHAR(100) NOT NULL,
    avatar_url VARCHAR(255),
    public_id_avatar VARCHAR(255),
    date_of_birth DATE,
    gender ENUM('MALE','FEMALE','OTHER'),
    -- Liên hệ
    phone_number VARCHAR(20),
    -- Trạng thái & phân quyền
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES role(id)
);

alter table users add column public_id_avatar VARCHAR(255);

CREATE TABLE verifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    -- Ảnh giấy tờ
    cccd_front_url VARCHAR(255) NOT NULL,
    cccd_back_url VARCHAR(255) NOT NULL,
    license_image_url VARCHAR(255) NOT NULL,
    public_id_cccd_front VARCHAR(255) DEFAULT NULL,
    public_id_cccd_back VARCHAR(255) DEFAULT NULL,
    public_id_license VARCHAR(255) DEFAULT NULL,
    -- Thông tin GPLX
    license_number VARCHAR(50) NOT NULL,
    license_expired_date VARCHAR(50) NOT NULL,
    status ENUM('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',
    note VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);



CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE cars (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category_id BIGINT,
    name VARCHAR(150) NOT NULL,
    license_plate VARCHAR(20) UNIQUE,
    price_per_day DECIMAL(19,2) NOT NULL,
    deposit_amount DECIMAL(19,2),
    status ENUM('AVAILABLE','RENTED','MAINTENANCE') DEFAULT 'AVAILABLE',
    description TEXT,
	trip_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);


CREATE TABLE car_details ( 
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    car_id BIGINT UNIQUE,
    seat_count INT,
    fuel_type VARCHAR(50),
    transmission VARCHAR(50),
    manufacture_year INT,
    address VARCHAR(255),
    city VARCHAR(100),
    delivery_available BOOLEAN DEFAULT FALSE,
    delivery_fee DECIMAL(10,2),
    FOREIGN KEY (car_id) REFERENCES cars(id)
);

CREATE TABLE features (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE car_features (
    car_detail_id BIGINT,
    feature_id BIGINT,
    PRIMARY KEY(car_detail_id, feature_id),

    FOREIGN KEY (car_detail_id) REFERENCES car_details(id),
    FOREIGN KEY (feature_id) REFERENCES features(id)
);

CREATE TABLE car_images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    car_id BIGINT,
    image_url VARCHAR(255),
    public_id VARCHAR(255),
    FOREIGN KEY (car_id) REFERENCES cars(id)
);

CREATE TABLE bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_code varchar(20),
    user_id BIGINT NOT NULL, 
    customer_name VARCHAR(100),
    customer_phone VARCHAR(20),
    customer_email VARCHAR(100),-- Khách hàng
    staff_id BIGINT,                  -- Nhân viên phụ trách duyệt đơn
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    total_price DECIMAL(19,2),        -- Tổng tiền cả đơn (sum của các detail)
    deposit_amount DECIMAL(19,2),     -- Tiền cọc tổng
    status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, CONFIRMED, RENTED, COMPLETED, CANCELLED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_booking_staff FOREIGN KEY (staff_id) REFERENCES users(id)
);


CREATE TABLE booking_details (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    car_id BIGINT NOT NULL,
    price_per_day DECIMAL(19,2) NOT NULL, -- Giá tại thời điểm đặt
    sub_total DECIMAL(19,2),              -- price * số ngày
    CONSTRAINT fk_detail_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    CONSTRAINT fk_detail_car FOREIGN KEY (car_id) REFERENCES cars(id)
);

CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    payment_method VARCHAR(20),       -- COD, VNPAY, MOMO
    payment_type VARCHAR(20),         -- DEPOSIT (Cọc), FULL_PAYMENT (Hết), PENALTY (Phạt)
    status VARCHAR(20) DEFAULT 'PENDING', -- PENDING, SUCCESS, FAILED
    transaction_code VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_payment_booking FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE
);

CREATE TABLE car_handovers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_detail_id BIGINT NOT NULL, -- Bàn giao cụ thể từng xe trong đơn
    staff_id BIGINT,                  -- Nhân viên thực hiện bàn giao
    handover_time DATETIME,           -- Lúc giao xe cho khách
    return_time DATETIME,             -- Lúc khách trả xe
    car_condition_before TEXT,        -- Tình trạng lúc giao (xước, xăng...)
    car_condition_after TEXT,         -- Tình trạng lúc nhận lại
    note TEXT,
    CONSTRAINT fk_handover_detail FOREIGN KEY (booking_detail_id) REFERENCES booking_details(id),
    CONSTRAINT fk_handover_staff FOREIGN KEY (staff_id) REFERENCES users(id)
);


-- CREATE TABLE incidents (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     booking_id BIGINT,
--     description TEXT,
--     incident_date DATETIME,
--     created_by BIGINT,
--     FOREIGN KEY (booking_id) REFERENCES bookings(id),
--     FOREIGN KEY (created_by) REFERENCES users(id)
-- );

-- CREATE TABLE penalties (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     booking_id BIGINT,
--     amount DECIMAL(10,2),
--     reason TEXT,
--     status ENUM('UNPAID','PAID') DEFAULT 'UNPAID',
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     FOREIGN KEY (booking_id) REFERENCES bookings(id)
-- );

-- CREATE TABLE reviews (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     user_id BIGINT,
--     vehicle_id BIGINT,
--     rating INT CHECK (rating BETWEEN 1 AND 5),
--     comment TEXT,
--     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
--     FOREIGN KEY (user_id) REFERENCES users(id),
--     FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
-- );

-- CREATE TABLE cancel_policies (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     hours_before INT,
--     refund_percent INT
-- );


INSERT INTO role (role_name) VALUES ('ADMIN'),('STAFF'),('USER');



