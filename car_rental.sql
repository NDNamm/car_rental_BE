-- MySQL dump 10.13  Distrib 8.0.38, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: car_rental
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `booking_details`
--

DROP TABLE IF EXISTS `booking_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `booking_id` bigint NOT NULL,
  `car_id` bigint NOT NULL,
  `start_date` datetime NOT NULL,
  `end_date` datetime NOT NULL,
  `rental_days` bigint NOT NULL,
  `price_per_day` decimal(19,2) NOT NULL,
  `sub_total` decimal(19,2) DEFAULT NULL,
  `deposit_amount` decimal(19,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `booking_id` (`booking_id`),
  KEY `car_id` (`car_id`),
  CONSTRAINT `booking_details_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`) ON DELETE CASCADE,
  CONSTRAINT `booking_details_ibfk_2` FOREIGN KEY (`car_id`) REFERENCES `cars` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking_details`
--

LOCK TABLES `booking_details` WRITE;
/*!40000 ALTER TABLE `booking_details` DISABLE KEYS */;
INSERT INTO `booking_details` VALUES (1,1,7,'2026-05-20 16:36:00','2026-05-23 00:00:00',2,2800000.00,5600000.00,2000.00),(2,2,6,'2026-05-24 17:39:00','2026-05-27 07:00:00',3,2000000.00,6000000.00,2000.00);
/*!40000 ALTER TABLE `booking_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `booking_status_history`
--

DROP TABLE IF EXISTS `booking_status_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `booking_status_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `booking_id` bigint NOT NULL,
  `changed_by` bigint DEFAULT NULL,
  `old_status` varchar(50) DEFAULT NULL,
  `new_status` varchar(50) NOT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `note` text,
  `changed_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_history_booking` (`booking_id`),
  KEY `fk_history_user` (`changed_by`),
  CONSTRAINT `fk_history_booking` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_history_user` FOREIGN KEY (`changed_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `booking_status_history`
--

LOCK TABLES `booking_status_history` WRITE;
/*!40000 ALTER TABLE `booking_status_history` DISABLE KEYS */;
INSERT INTO `booking_status_history` VALUES (1,1,3,NULL,'PENDING','Người dùng đã tạo đơn',NULL,'2026-05-24 02:37:07'),(2,1,2,NULL,'CONFIRMED','Thanh toán cọc thành công',NULL,'2026-05-24 02:38:03'),(3,1,3,'CONFIRMED','CANCELLED','Khách hàng hủy đơn',NULL,'2026-05-24 02:38:53'),(4,2,3,NULL,'PENDING','Người dùng đã tạo đơn',NULL,'2026-05-24 02:39:58'),(5,2,2,NULL,'CONFIRMED','Thanh toán cọc thành công',NULL,'2026-05-24 02:40:16'),(6,2,1,'CONFIRMED','RENTED',NULL,'ko co','2026-05-24 02:41:45'),(7,2,1,'RENTED','RETURNING',NULL,'Xe bi hu ...','2026-05-24 02:43:54'),(8,2,1,'RETURNING','COMPLETED','Đã thu đủ tiền','','2026-05-24 03:02:10');
/*!40000 ALTER TABLE `booking_status_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookings`
--

DROP TABLE IF EXISTS `bookings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `booking_code` varchar(20) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  `customer_name` varchar(100) DEFAULT NULL,
  `customer_phone` varchar(20) DEFAULT NULL,
  `customer_email` varchar(100) DEFAULT NULL,
  `total_price` decimal(19,2) DEFAULT NULL,
  `total_deposit_amount` decimal(19,2) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'PENDING',
  `note` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `payment_deadline` datetime DEFAULT NULL,
  `qr_code_url` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `booking_code` (`booking_code`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `bookings_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookings`
--

LOCK TABLES `bookings` WRITE;
/*!40000 ALTER TABLE `bookings` DISABLE KEYS */;
INSERT INTO `bookings` VALUES (1,'BK_15365AD2',3,'Nam Nguyễn','0123456789','nam8122k4@gmail.com',5600000.00,2000.00,'CANCELLED',NULL,'2026-05-24 02:37:07','2026-05-24 10:37:07','https://img.vietqr.io/image/MB-0936106205-compact2.png?amount=2000.00&addInfo=BK_15365AD2'),(2,'BK_B943BDDA',3,'Nam Nguyễn','0123456789','nam8122k4@gmail.com',6000000.00,2000.00,'COMPLETED',NULL,'2026-05-24 02:39:58','2026-05-24 10:39:58','https://img.vietqr.io/image/MB-0936106205-compact2.png?amount=2000.00&addInfo=BK_B943BDDA');
/*!40000 ALTER TABLE `bookings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_details`
--

DROP TABLE IF EXISTS `car_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `car_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `car_id` bigint DEFAULT NULL,
  `seat_count` int DEFAULT NULL,
  `fuel_type` varchar(50) DEFAULT NULL,
  `transmission` varchar(50) DEFAULT NULL,
  `manufacture_year` int DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `delivery_available` tinyint(1) DEFAULT '0',
  `delivery_fee` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `car_id` (`car_id`),
  KEY `idx_car_detail_car` (`car_id`),
  CONSTRAINT `car_details_ibfk_1` FOREIGN KEY (`car_id`) REFERENCES `cars` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_details`
--

LOCK TABLES `car_details` WRITE;
/*!40000 ALTER TABLE `car_details` DISABLE KEYS */;
INSERT INTO `car_details` VALUES (1,1,7,'DIESEL','AUTOMATIC',2023,'15 Phạm Hùng','Hà Nội',0,NULL),(2,2,5,'GASOLINE','MANUAL',2021,'102 Lê Lợi, Phường Bến Thành, Quận 1','TP. Hồ Chí Minh',1,100000.00),(3,3,5,'GASOLINE','AUTOMATIC',2020,'45 Nguyễn Văn Linh, Phường Nam Dương, Quận Hải Châu','Đà Nẵng',0,NULL),(4,4,4,'GASOLINE','AUTOMATIC',2019,'78 Trần Phú, Phường Lộc Thọ, TP. Nha Trang','Khánh Hòa',0,NULL),(5,5,5,'DIESEL','MANUAL',2022,'12 Hùng Vương, Phường 1, TP. Vũng Tàu, Bà Rịa','Vũng Tàu',0,NULL),(6,6,5,'GASOLINE','AUTOMATIC',2023,'56 Nguyễn Trãi, Phường Ninh Kiều','TP. Cần Thơ',0,NULL),(7,NULL,NULL,NULL,NULL,NULL,NULL,'',0,NULL),(8,7,5,'GASOLINE','AUTOMATIC',2023,'123 Nguyễn Trãi','Hà Nội',0,NULL);
/*!40000 ALTER TABLE `car_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_features`
--

DROP TABLE IF EXISTS `car_features`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `car_features` (
  `car_detail_id` bigint NOT NULL,
  `feature_id` bigint NOT NULL,
  PRIMARY KEY (`car_detail_id`,`feature_id`),
  KEY `idx_car_feature_detail` (`car_detail_id`),
  KEY `idx_car_feature_feature` (`feature_id`),
  CONSTRAINT `car_features_ibfk_1` FOREIGN KEY (`car_detail_id`) REFERENCES `car_details` (`id`),
  CONSTRAINT `car_features_ibfk_2` FOREIGN KEY (`feature_id`) REFERENCES `features` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_features`
--

LOCK TABLES `car_features` WRITE;
/*!40000 ALTER TABLE `car_features` DISABLE KEYS */;
INSERT INTO `car_features` VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(2,5),(2,7),(2,8),(2,9),(3,2),(3,3),(3,4),(3,5),(3,7),(3,11),(4,2),(4,7),(4,8),(4,9),(4,11),(5,1),(5,2),(5,3),(5,4),(5,6),(5,7),(6,1),(6,2),(6,3),(6,4),(6,5),(6,6),(6,8),(6,9),(6,11),(8,1),(8,2),(8,3),(8,4),(8,6),(8,8),(8,9),(8,11);
/*!40000 ALTER TABLE `car_features` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `car_images`
--

DROP TABLE IF EXISTS `car_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `car_images` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `car_id` bigint DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `public_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `car_id` (`car_id`),
  CONSTRAINT `car_images_ibfk_1` FOREIGN KEY (`car_id`) REFERENCES `cars` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_images`
--

LOCK TABLES `car_images` WRITE;
/*!40000 ALTER TABLE `car_images` DISABLE KEYS */;
INSERT INTO `car_images` VALUES (1,1,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777300113/car_rental/cars/toyota_fortuner_2023/kxunwh2woata4hutpysx.webp','car_rental/cars/toyota_fortuner_2023/kxunwh2woata4hutpysx'),(2,1,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777300110/car_rental/cars/toyota_fortuner_2023/askkqdzcakj49v5s3zbu.jpg','car_rental/cars/toyota_fortuner_2023/askkqdzcakj49v5s3zbu'),(3,1,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777300117/car_rental/cars/toyota_fortuner_2023/flfb49dkla9u5jdl4ca7.webp','car_rental/cars/toyota_fortuner_2023/flfb49dkla9u5jdl4ca7'),(4,1,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777300113/car_rental/cars/toyota_fortuner_2023/kxunwh2woata4hutpysx.webp','car_rental/cars/toyota_fortuner_2023/kxunwh2woata4hutpysx'),(5,1,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777300110/car_rental/cars/toyota_fortuner_2023/askkqdzcakj49v5s3zbu.jpg','car_rental/cars/toyota_fortuner_2023/askkqdzcakj49v5s3zbu'),(6,2,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302009/car_rental/cars/honda_civic_2021/mtsxzc61kdj2kdjhnenq.jpg','car_rental/cars/honda_civic_2021/mtsxzc61kdj2kdjhnenq'),(7,2,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302010/car_rental/cars/honda_civic_2021/br6yiacw2epwprhohxiw.jpg','car_rental/cars/honda_civic_2021/br6yiacw2epwprhohxiw'),(8,2,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302011/car_rental/cars/honda_civic_2021/qctooexc7sdpgw3ab0et.jpg','car_rental/cars/honda_civic_2021/qctooexc7sdpgw3ab0et'),(9,2,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302013/car_rental/cars/honda_civic_2021/gxoffusjnbkkmgtdebvk.jpg','car_rental/cars/honda_civic_2021/gxoffusjnbkkmgtdebvk'),(10,2,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302015/car_rental/cars/honda_civic_2021/nk8rsq1ojefiomf9bcwz.jpg','car_rental/cars/honda_civic_2021/nk8rsq1ojefiomf9bcwz'),(11,3,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302015/car_rental/cars/honda_civic_2021/nk8rsq1ojefiomf9bcwz.jpg','car_rental/cars/toyota_camry/gen6rm4634slln3fsteu'),(12,3,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302116/car_rental/cars/toyota_camry/wszkt2snbadguad1otfb.jpg','car_rental/cars/toyota_camry/wszkt2snbadguad1otfb'),(13,3,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302115/car_rental/cars/toyota_camry/xvhituuiqaaf6bcipiow.jpg','car_rental/cars/toyota_camry/xvhituuiqaaf6bcipiow'),(14,3,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302120/car_rental/cars/toyota_camry/h2kedox8vorp8rkqh8pg.jpg','car_rental/cars/toyota_camry/h2kedox8vorp8rkqh8pg'),(15,3,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302117/car_rental/cars/toyota_camry/st2drc8ys1klxzfue5ta.jpg','car_rental/cars/toyota_camry/st2drc8ys1klxzfue5ta'),(16,4,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302189/car_rental/cars/kia_morning/zf8skpk0zmw802jmr6wz.jpg','car_rental/cars/kia_morning/zf8skpk0zmw802jmr6wz'),(17,4,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302188/car_rental/cars/kia_morning/wdfp5fcsrhyfz8dybrsr.jpg','car_rental/cars/kia_morning/wdfp5fcsrhyfz8dybrsr'),(18,4,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302191/car_rental/cars/kia_morning/v6nmfcdw8begocok6ni8.jpg','car_rental/cars/kia_morning/v6nmfcdw8begocok6ni8'),(19,4,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302193/car_rental/cars/kia_morning/ptu8qkiswhdcjwaofylo.jpg','car_rental/cars/kia_morning/ptu8qkiswhdcjwaofylo'),(20,4,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302194/car_rental/cars/kia_morning/g73sf3l8lnimtl5wmhk2.jpg','car_rental/cars/kia_morning/g73sf3l8lnimtl5wmhk2'),(21,5,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302253/car_rental/cars/ford_ranger/j59dseeeea72pycajhse.jpg','car_rental/cars/ford_ranger/j59dseeeea72pycajhse'),(22,5,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302255/car_rental/cars/ford_ranger/zqsun8drlnraposcpbbx.jpg','car_rental/cars/ford_ranger/zqsun8drlnraposcpbbx'),(23,5,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302256/car_rental/cars/ford_ranger/iflelqp6rse4li3fyopw.jpg','car_rental/cars/ford_ranger/iflelqp6rse4li3fyopw'),(24,5,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302257/car_rental/cars/ford_ranger/vn1jb0esnk8q2cf018eb.jpg','car_rental/cars/ford_ranger/vn1jb0esnk8q2cf018eb'),(25,5,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302259/car_rental/cars/ford_ranger/buhhvxlks3owsec3fynn.jpg','car_rental/cars/ford_ranger/buhhvxlks3owsec3fynn'),(26,6,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302346/car_rental/cars/mercedes_c300/wbi1iqv0bzvo4i6i3edl.jpg','car_rental/cars/mercedes_c300/wbi1iqv0bzvo4i6i3edl'),(27,6,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302347/car_rental/cars/mercedes_c300/it7rc7gorxkzqobol1mk.jpg','car_rental/cars/mercedes_c300/it7rc7gorxkzqobol1mk'),(28,6,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302348/car_rental/cars/mercedes_c300/kqc30ugrhfxoaybbpdqm.jpg','car_rental/cars/mercedes_c300/kqc30ugrhfxoaybbpdqm'),(29,6,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302350/car_rental/cars/mercedes_c300/plpv7v6ewphsl9qjhx2g.jpg','car_rental/cars/mercedes_c300/plpv7v6ewphsl9qjhx2g'),(30,6,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1777302351/car_rental/cars/mercedes_c300/pwcqdr7pjltfgkujybhy.jpg','car_rental/cars/mercedes_c300/pwcqdr7pjltfgkujybhy'),(31,7,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1778769995/car_rental/cars/bmw_520i_m_sport/lkoosc4lhej0urlidewl.jpg','car_rental/cars/bmw_520i_m_sport/lkoosc4lhej0urlidewl'),(32,7,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1778769994/car_rental/cars/bmw_520i_m_sport/x1bkrjsexpgbnmby9et5.jpg','car_rental/cars/bmw_520i_m_sport/x1bkrjsexpgbnmby9et5'),(33,7,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1778769991/car_rental/cars/bmw_520i_m_sport/uxknl4lh5zqrwfm3zqid.jpg','car_rental/cars/bmw_520i_m_sport/uxknl4lh5zqrwfm3zqid'),(34,7,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1778769990/car_rental/cars/bmw_520i_m_sport/d19cq9omb8sdujik5vtt.jpg','car_rental/cars/bmw_520i_m_sport/d19cq9omb8sdujik5vtt'),(35,7,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1778769988/car_rental/cars/bmw_520i_m_sport/otunegzd29kbumxoxmlu.jpg','car_rental/cars/bmw_520i_m_sport/otunegzd29kbumxoxmlu');
/*!40000 ALTER TABLE `car_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cars`
--

DROP TABLE IF EXISTS `cars`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cars` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_id` bigint DEFAULT NULL,
  `name` varchar(150) NOT NULL,
  `license_plate` varchar(20) NOT NULL,
  `price_per_day` decimal(19,2) NOT NULL,
  `deposit_amount` decimal(19,2) DEFAULT NULL,
  `status` enum('AVAILABLE','RENTED','MAINTENANCE') DEFAULT 'AVAILABLE',
  `description` text,
  `trip_count` int DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `average_rating` double DEFAULT '0',
  `total_reviews` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `license_plate` (`license_plate`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `cars_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cars`
--

LOCK TABLES `cars` WRITE;
/*!40000 ALTER TABLE `cars` DISABLE KEYS */;
INSERT INTO `cars` VALUES (1,1,'Toyota Fortuner 2023','30A-12345',1200000.00,5000000.00,'AVAILABLE','? Toyota Fortuner - SUV 7 chỗ mạnh mẽ, bền bỉ\n\n?️ Thông tin cơ bản:\n- Loại xe: SUV 7 chỗ\n- Xuất xứ: Nhật Bản\n- Nhiên liệu: Diesel / Xăng (tùy phiên bản)\n- Hộp số: Số tự động 6 cấp / số sàn 6 cấp\n- Dẫn động: 1 cầu / 2 cầu (4x4)\n\n⚙️ Động cơ & vận hành:\n- Công suất: ~201 HP (bản 2.8 Diesel)\n- Mô-men xoắn: ~500 Nm\n- Tiêu hao nhiên liệu: 7–9L/100km\n- Vận hành ổn định, mạnh mẽ trên mọi địa hình\n\n? Ngoại thất:\n- Thiết kế cơ bắp, mạnh mẽ\n- Đèn LED hiện đại, tích hợp DRL\n- Mâm hợp kim 17–18 inch\n- Gầm cao ~219mm, đi đường xấu tốt\n\n?️ Nội thất:\n- 7 chỗ rộng rãi, ghế da cao cấp\n- Màn hình giải trí 8–9 inch\n- Apple CarPlay / Android Auto\n- Điều hòa 2 vùng độc lập\n\n?️ An toàn:\n- 6 túi khí\n- ABS / EBD / BA\n- Cân bằng điện tử\n- Hỗ trợ khởi hành ngang dốc\n- Camera lùi + cảm biến\n\n?‍?‍?‍? Phù hợp:\n- Gia đình đông người\n- Đi du lịch xa\n- Thuê xe tự lái hoặc có tài xế\n- Đi tỉnh, địa hình xấu\n\n? Tổng kết:\nToyota Fortuner là SUV 7 chỗ bền bỉ, mạnh mẽ, giữ giá tốt và rất phù hợp nhu cầu di chuyển đường dài.',0,'2026-04-27 13:56:11',4,1),(2,2,'Honda Civic 2021','29A-33333',900000.00,2000.00,'AVAILABLE','?️ Thông tin:\n- Năm sản xuất: 2021\n- 5 chỗ, động cơ xăng\n- Hộp số tự động CVT\n\n⚙️ Vận hành:\n- Tăng tốc tốt, cảm giác lái thể thao\n- Tiết kiệm nhiên liệu trong đô thị\n\n? Ngoại thất:\n- Thiết kế trẻ trung, năng động\n- Kiểu dáng coupe thể thao\n\n?️ Nội thất:\n- Màn hình giải trí hiện đại\n- Kết nối Bluetooth, USB\n- Điều hòa tự động\n\n?️ An toàn:\n- ABS, EBD\n- Camera lùi\n\n?‍? Phù hợp:\n- Người trẻ, nhân viên văn phòng\n- Đi lại trong thành phố\',\n7, 2021),',0,'2026-04-27 15:00:08',0,0),(3,2,'Toyota Camry ','29A-44444',1100000.00,4000000.00,'AVAILABLE','?️ Thông tin:\n- Năm sản xuất: 2020\n- 5 chỗ, số tự động\n\n⚙️ Vận hành:\n- Êm ái, ổn định\n- Cách âm tốt, phù hợp di chuyển đường dài\n\n? Ngoại thất:\n- Thiết kế thanh lịch, sang trọng\n\n?️ Nội thất:\n- Ghế da cao cấp\n- Điều hòa 2 vùng độc lập\n- Không gian rộng rãi, thoải mái\n\n?️ An toàn:\n- Hệ thống hỗ trợ phanh\n- Túi khí đầy đủ\n\n?‍? Phù hợp:\n- Doanh nhân\n- Đi công tác, tiếp khách\',\n15, 2020),\n',0,'2026-04-27 15:01:55',0,0),(4,3,'Kia Morning','30A-55555',500000.00,2000000.00,'AVAILABLE','?️ Thông tin:\n- Năm sản xuất: 2019\n- 4 chỗ, số sàn\n\n⚙️ Vận hành:\n- Tiết kiệm nhiên liệu\n- Linh hoạt trong phố đông\n\n? Ngoại thất:\n- Nhỏ gọn, dễ đỗ xe\n\n?️ Nội thất:\n- Đầy đủ tiện nghi cơ bản\n\n?‍? Phù hợp:\n- Sinh viên, người đi làm\n- Di chuyển trong nội thành\',\n20, 2019),',0,'2026-04-27 15:03:06',0,0),(5,4,'Ford Ranger','30A-66666',1400000.00,2000.00,'AVAILABLE','\'? Ford Ranger - Bán tải mạnh mẽ, đa dụng\n\n?️ Thông tin:\n- Năm sản xuất: 2022\n- 5 chỗ, máy dầu\n\n⚙️ Vận hành:\n- Khả năng tải nặng tốt\n- Đi địa hình mạnh mẽ\n\n? Ngoại thất:\n- Thiết kế hầm hố, nam tính\n\n?️ Nội thất:\n- Rộng rãi, tiện nghi\n\n?‍? Phù hợp:\n- Công trình, vận chuyển hàng hóa\n- Đi địa hình\',\n8, 2022),',4,'2026-04-27 15:04:13',5,1),(6,6,'Mercedes C300','30A-77777',2000000.00,2000.00,'AVAILABLE','? Mercedes C300 - Sedan hạng sang, đẳng cấp doanh nhân\n\n?️ Thông tin:\n- Năm sản xuất: 2023\n- 5 chỗ, số tự động\n\n⚙️ Vận hành:\n- Động cơ mạnh mẽ, tăng tốc mượt\n- Hệ thống treo êm ái\n\n? Ngoại thất:\n- Thiết kế sang trọng, tinh tế\n\n?️ Nội thất:\n- Ghế da cao cấp\n- Màn hình kép hiện đại\n- Đèn nội thất ambient\n\n?️ An toàn:\n- Công nghệ hỗ trợ lái tiên tiến\n- Cảnh báo va chạm\n\n?‍? Phù hợp:\n- Doanh nhân\n- Khách VIP, thuê xe cao cấp\',\n3, 2023)',4,'2026-04-27 15:05:44',5,3),(7,6,'BMW 520i M Sport','51H-88888',2800000.00,2000.00,'AVAILABLE','? BMW 520i M Sport - Sedan thể thao sang trọng, chuẩn phong cách châu Âu\n\n?️ Thông tin:\n- Năm sản xuất: 2023\n- 5 chỗ, số tự động\n- Phiên bản M Sport cao cấp\n\n⚙️ Vận hành:\n- Động cơ tăng áp mạnh mẽ\n- Cảm giác lái đầm chắc đặc trưng BMW\n- Tiết kiệm nhiên liệu, vận hành ổn định\n\n? Ngoại thất:\n- Thiết kế thể thao, hiện đại\n- Bộ bodykit M Sport cá tính\n- Đèn LED sắc nét sang trọng\n\n?️ Nội thất:\n- Ghế da cao cấp chỉnh điện\n- Màn hình giải trí lớn\n- Điều hòa đa vùng\n- Không gian rộng rãi, êm ái\n\n?️ An toàn:\n- Camera lùi, cảm biến hỗ trợ đỗ xe\n- Hệ thống cân bằng điện tử\n- Nhiều túi khí an toàn\n\n?‍? Phù hợp:\n- Doanh nhân\n- Đi công tác, gặp đối tác\n- Du lịch gia đình cao cấp',2,'2026-05-14 14:46:27',5,1);
/*!40000 ALTER TABLE `cars` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'SUV','Xe gầm cao, phù hợp đi đường dài'),(2,'Sedan','Xe gia đình, 4-5 chỗ'),(3,'Hatchback','Xe nhỏ gọn, tiết kiệm nhiên liệu\n'),(4,'Pickup','Xe bán tải, chở hàng'),(6,'Luxury','Xe cao cấp, sang trọng');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `features`
--

DROP TABLE IF EXISTS `features`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `features` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `features`
--

LOCK TABLES `features` WRITE;
/*!40000 ALTER TABLE `features` DISABLE KEYS */;
INSERT INTO `features` VALUES (1,'Bluetooth'),(4,'Cảm biến va chạm'),(2,'Camera lùi'),(6,'Cruise Control'),(7,'Cửa sổ trời'),(8,'Ghế da'),(3,'GPS'),(5,'Màn hình cảm ứng'),(9,'Sưởi ghế'),(11,'USB');
/*!40000 ALTER TABLE `features` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `handover_images`
--

DROP TABLE IF EXISTS `handover_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `handover_images` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `handover_id` bigint NOT NULL,
  `image_url` text,
  `public_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_handover_image_handover` (`handover_id`),
  CONSTRAINT `fk_handover_image_handover` FOREIGN KEY (`handover_id`) REFERENCES `handovers` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `handover_images`
--

LOCK TABLES `handover_images` WRITE;
/*!40000 ALTER TABLE `handover_images` DISABLE KEYS */;
INSERT INTO `handover_images` VALUES (1,1,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1779590503/car_rental/handovers/BK_B943BDDA/nh_bn_giao_xe/fixxit6gtiikhvkagdxl.jpg','car_rental/handovers/BK_B943BDDA/nh_bn_giao_xe/fixxit6gtiikhvkagdxl'),(2,1,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1779590505/car_rental/handovers/BK_B943BDDA/nh_bn_giao_xe/mhqaru7suhxci3tc1kzh.jpg','car_rental/handovers/BK_B943BDDA/nh_bn_giao_xe/mhqaru7suhxci3tc1kzh'),(3,2,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1779590632/car_rental/handovers/BK_B943BDDA/nh_thu_hi_xe/vbdh161r6tfllplf4mqk.jpg','car_rental/handovers/BK_B943BDDA/nh_thu_hi_xe/vbdh161r6tfllplf4mqk'),(4,2,'https://res.cloudinary.com/dsnp27eeq/image/upload/v1779590633/car_rental/handovers/BK_B943BDDA/nh_thu_hi_xe/jey9wpklu57hzkhkut1u.jpg','car_rental/handovers/BK_B943BDDA/nh_thu_hi_xe/jey9wpklu57hzkhkut1u');
/*!40000 ALTER TABLE `handover_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `handovers`
--

DROP TABLE IF EXISTS `handovers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `handovers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `booking_detail_id` bigint NOT NULL,
  `staff_id` bigint DEFAULT NULL,
  `type` enum('OUT','IN') NOT NULL,
  `handover_time` datetime NOT NULL,
  `mileage` bigint DEFAULT NULL,
  `fuel_level` int DEFAULT NULL,
  `note` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_handover_booking_detail` (`booking_detail_id`),
  KEY `fk_handover_staff` (`staff_id`),
  CONSTRAINT `fk_handover_booking_detail` FOREIGN KEY (`booking_detail_id`) REFERENCES `booking_details` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_handover_staff` FOREIGN KEY (`staff_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `handovers`
--

LOCK TABLES `handovers` WRITE;
/*!40000 ALTER TABLE `handovers` DISABLE KEYS */;
INSERT INTO `handovers` VALUES (1,2,1,'OUT','2026-05-24 09:41:41',15200,80,'ko bi hu hong j','2026-05-24 02:41:41'),(2,2,1,'IN','2026-05-24 09:43:51',14200,29,'Xe bi hu hai','2026-05-24 02:43:51');
/*!40000 ALTER TABLE `handovers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `incidents`
--

DROP TABLE IF EXISTS `incidents`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `incidents` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `handover_id` bigint NOT NULL,
  `description` text,
  `type` varchar(50) NOT NULL,
  `source` enum('AUTO','MANUAL') DEFAULT 'MANUAL',
  `created_by` bigint DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_incident_handover` (`handover_id`),
  KEY `fk_incident_created_by` (`created_by`),
  CONSTRAINT `fk_incident_created_by` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_incident_handover` FOREIGN KEY (`handover_id`) REFERENCES `handovers` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `incidents`
--

LOCK TABLES `incidents` WRITE;
/*!40000 ALTER TABLE `incidents` DISABLE KEYS */;
INSERT INTO `incidents` VALUES (1,2,'bi xuox son','SCRATCH','MANUAL',1,'2026-05-24 02:43:54'),(2,2,'bi hu ghe goi','INTERIOR_DAMAGE','MANUAL',1,'2026-05-24 02:43:54');
/*!40000 ALTER TABLE `incidents` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoices`
--

DROP TABLE IF EXISTS `invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoices` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `booking_id` bigint NOT NULL,
  `rental_total` decimal(12,2) DEFAULT '0.00',
  `penalty_total` decimal(12,2) DEFAULT '0.00',
  `deposit_paid` decimal(12,2) DEFAULT '0.00',
  `paid_total` decimal(12,2) DEFAULT '0.00',
  `final_amount` decimal(12,2) NOT NULL,
  `remaining_amount` decimal(12,2) DEFAULT '0.00',
  `status` enum('PENDING','PARTIAL','PAID','CANCELLED') DEFAULT 'PENDING',
  `note` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `booking_id` (`booking_id`),
  CONSTRAINT `invoices_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoices`
--

LOCK TABLES `invoices` WRITE;
/*!40000 ALTER TABLE `invoices` DISABLE KEYS */;
INSERT INTO `invoices` VALUES (1,1,5600000.00,0.00,2000.00,2000.00,5598000.00,5598000.00,'PARTIAL','Điều chỉnh hóa đơn sau kiểm tra tình trạng xe','2026-05-24 02:37:07','2026-05-24 02:38:03'),(2,2,6000000.00,2500000.00,2000.00,2000.00,8498000.00,8498000.00,'PARTIAL','Điều chỉnh hóa đơn sau kiểm tra tình trạng xe','2026-05-24 02:39:58','2026-05-24 02:43:54');
/*!40000 ALTER TABLE `invoices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `type` varchar(50) DEFAULT NULL,
  `message` text NOT NULL,
  `is_read` tinyint(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_notification_user` (`user_id`),
  CONSTRAINT `fk_notification_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (1,3,'BOOKING','Đơn hàng BK_15365AD2 của bạn đã được tạo thành công và đang chờ xác nhận.',0,'2026-05-24 02:37:07'),(2,3,'BOOKING','Đơn đặt xe BK_15365AD2 đã được xác nhận thanh toán cọc.',0,'2026-05-24 02:38:03'),(3,3,'BOOKING','Đơn hàng BK_15365AD2 của bạn đã bị hủy.',0,'2026-05-24 02:38:53'),(4,3,'BOOKING','Đơn hàng BK_B943BDDA của bạn đã được tạo thành công và đang chờ xác nhận.',0,'2026-05-24 02:39:58'),(5,3,'BOOKING','Đơn đặt xe BK_B943BDDA đã được xác nhận thanh toán cọc.',0,'2026-05-24 02:40:16'),(6,3,'BOOKING','Xe đã được giao, chúc bạn có chuyến đi vui vẻ!',1,'2026-05-24 02:41:45'),(7,3,'INCIDENT','Sự cố SCRATCH đã được ghi nhận trong quá trình bàn giao xe. Vui lòng kiểm tra chi tiết và xử lý kịp thời.',1,'2026-05-24 02:43:54'),(8,3,'INCIDENT','Sự cố INTERIOR_DAMAGE đã được ghi nhận trong quá trình bàn giao xe. Vui lòng kiểm tra chi tiết và xử lý kịp thời.',0,'2026-05-24 02:43:54'),(9,3,'BOOKING','Cảm ơn bạn đã sử dụng dịch vụ, chúng tôi đang chờ xe trả về.',0,'2026-05-24 02:43:54'),(10,3,'BOOKING','Đơn hàng BK_B943BDDA của bạn đã hoàn thành. Hãy để lại đánh giá về trải nghiệm của bạn!',1,'2026-05-24 03:02:10'),(11,7,'VERIFICATION','Xác minh tài khoản để đặt xe',1,'2026-05-24 13:52:37'),(12,1,'VERIFICATION','Có một yêu cầu xác minh mới đang chờ xử lý. Vui lòng kiểm tra và cập nhật trạng thái.',1,'2026-05-24 13:53:33');
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `booking_id` bigint NOT NULL,
  `amount` decimal(19,2) NOT NULL,
  `payment_method` varchar(50) DEFAULT 'BANK_TRANSFER',
  `payment_type` varchar(20) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'PENDING',
  `transaction_code` varchar(100) DEFAULT NULL,
  `payment_content` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `transaction_code` (`transaction_code`),
  KEY `booking_id` (`booking_id`),
  CONSTRAINT `payments_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,1,2000.00,'BANK_TRANSFER','DEPOSIT','SUCCESS','FT26145083597880','BK_15365AD2','2026-05-24 02:37:07'),(2,2,2000.00,'BANK_TRANSFER','DEPOSIT','SUCCESS','FT26145009136104','BK_B943BDDA','2026-05-24 02:39:58');
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `penalties`
--

DROP TABLE IF EXISTS `penalties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `penalties` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `incident_id` bigint NOT NULL,
  `amount` decimal(12,2) DEFAULT NULL,
  `status` enum('UNPAID','PAID') DEFAULT 'UNPAID',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_penalty_incident` (`incident_id`),
  CONSTRAINT `fk_penalty_incident` FOREIGN KEY (`incident_id`) REFERENCES `incidents` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `penalties`
--

LOCK TABLES `penalties` WRITE;
/*!40000 ALTER TABLE `penalties` DISABLE KEYS */;
INSERT INTO `penalties` VALUES (1,1,500000.00,'UNPAID','2026-05-24 02:43:54'),(2,2,2000000.00,'UNPAID','2026-05-24 02:43:54');
/*!40000 ALTER TABLE `penalties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `penalty_rules`
--

DROP TABLE IF EXISTS `penalty_rules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `penalty_rules` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` varchar(50) DEFAULT NULL,
  `unit_price` decimal(12,2) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `type` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `penalty_rules`
--

LOCK TABLES `penalty_rules` WRITE;
/*!40000 ALTER TABLE `penalty_rules` DISABLE KEYS */;
INSERT INTO `penalty_rules` VALUES (1,'QUÁ GIỜ',100000.00,'Phạt 100K mỗi giờ sau thời gian trả xe\n',1,'2026-05-16 15:28:00');
/*!40000 ALTER TABLE `penalty_rules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refunds`
--

DROP TABLE IF EXISTS `refunds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refunds` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `booking_id` bigint DEFAULT NULL,
  `hours_before` int DEFAULT NULL,
  `refund_percent` int DEFAULT NULL,
  `amount` decimal(19,2) DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `note` text,
  `qr_code` varchar(255) DEFAULT NULL,
  `public_id` varchar(255) DEFAULT NULL,
  `status` varchar(20) DEFAULT 'PENDING',
  `admin_proof_image` varchar(255) DEFAULT NULL,
  `public_id_proof` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `booking_code` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `booking_id` (`booking_id`),
  CONSTRAINT `refunds_ibfk_1` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refunds`
--

LOCK TABLES `refunds` WRITE;
/*!40000 ALTER TABLE `refunds` DISABLE KEYS */;
/*!40000 ALTER TABLE `refunds` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `car_id` bigint DEFAULT NULL,
  `booking_id` bigint DEFAULT NULL,
  `rating` int DEFAULT NULL,
  `comment` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `reason` varchar(255) DEFAULT NULL,
  `is_hidden` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `car_id` (`car_id`),
  KEY `booking_id` (`booking_id`),
  CONSTRAINT `reviews_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `reviews_ibfk_2` FOREIGN KEY (`car_id`) REFERENCES `cars` (`id`),
  CONSTRAINT `reviews_ibfk_3` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`),
  CONSTRAINT `reviews_chk_1` CHECK ((`rating` between 1 and 5))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_name` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ADMIN'),(3,'STAFF'),(4,'SYSTEM'),(2,'USER');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `avatar_url` varchar(255) DEFAULT NULL,
  `public_id_avatar` varchar(255) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `gender` enum('MALE','FEMALE','OTHER') DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `is_active` tinyint(1) DEFAULT '1',
  `reason` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,1,'nam08122004@gmail.com','$2a$10$y.BFScgZ5UML9g9/9y9hX.X62P7aEZ3Gmr.woU44G5aOrXwNAUuY.','Nguyễn Doãn Nam','http://res.cloudinary.com/dsnp27eeq/image/upload/v1777305375/car_rental/users/Nguy%E1%BB%85n%20Do%C3%A3n%20Nam/avatar.jpg','car_rental/users/Nguyễn Doãn Nam/avatar','2004-12-08','MALE','0936106205','2026-04-26 17:00:00',1,NULL),(2,4,'system@local','$2a$10$uLeXCYt.yEwYgzGvGd9AFufiwTs.OtJUF0nOM2X42t/hBkveKfWry','Hệ thống',NULL,NULL,NULL,'OTHER',NULL,'2026-04-26 17:00:00',1,NULL),(3,2,'nam8122k4@gmail.com','$2a$10$ukPHNVyICdBtiZeTAXbI5uLYVe3NJY8Z71SVQsAm1bO.gdrQ4TAfO','Nam Nguyễn','https://lh3.googleusercontent.com/a/ACg8ocKLL_fRwtgD6k9I_zz9PtJNywC9P6P5N5uSu7Jq9IoFkUomXg=s96-c',NULL,NULL,NULL,NULL,'2026-05-01 17:00:00',1,NULL),(4,2,'user1@gmail.com','$2a$10$MtrnvJDDASw1oV9TuL3ee.9NdtvSajRskCoZH3BLoA8iHROrKxwPa','userTest',NULL,NULL,'2004-01-08','OTHER','1236568931','2026-05-01 17:00:00',0,'Tài khoản bị khóa do vi phạm tiêu chuẩn'),(5,2,'nam08122k4@gmail.com','$2a$10$zjITdZU1fP7mr8QLjLJVI.BC5Ic6tDY2HkF3MtZRzVsKpJJXNE2ay','Nam Nguyễn','https://lh3.googleusercontent.com/a/ACg8ocKKB-d1z9UY3Q3puNFhfXnG31u30b9UQM9_KyBZmvYNgdmQNw=s96-c',NULL,NULL,NULL,NULL,'2026-05-02 17:00:00',1,NULL),(6,2,'user3@gmail.com','$2a$10$8aIdfLeomIF1OceyF8rRTeF9/wHImk5BU3NAKvuXJI4ZjSO7W2MXO','user3',NULL,NULL,NULL,NULL,NULL,'2026-05-06 17:00:00',0,'Tài khoản bị khóa do vi phạm tiêu chuẩn'),(7,2,'test12345@gmail.com','$2a$10$xgka5hTdYxFD0Iy3njiSquYguzK/EHZOLwRaIhhnKf8xrswsra7pq','userTest1',NULL,NULL,NULL,NULL,NULL,'2026-05-23 17:00:00',NULL,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `verifications`
--

DROP TABLE IF EXISTS `verifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `verifications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `cccd_front_url` varchar(255) DEFAULT NULL,
  `cccd_back_url` varchar(255) DEFAULT NULL,
  `license_image_url` varchar(255) DEFAULT NULL,
  `public_id_cccd_front` varchar(255) DEFAULT NULL,
  `public_id_cccd_back` varchar(255) DEFAULT NULL,
  `public_id_license` varchar(255) DEFAULT NULL,
  `license_number` varchar(50) DEFAULT NULL,
  `license_expired_date` varchar(50) DEFAULT NULL,
  `status` enum('PENDING','APPROVED','REJECTED','UNVERIFIED') DEFAULT 'UNVERIFIED',
  `note` varchar(255) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `verifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `verifications`
--

LOCK TABLES `verifications` WRITE;
/*!40000 ALTER TABLE `verifications` DISABLE KEYS */;
INSERT INTO `verifications` VALUES (1,1,'http://res.cloudinary.com/dsnp27eeq/image/upload/v1777389924/car_rental/users/Nguy%E1%BB%85n%20Do%C3%A3n%20Nam/cccd_front.jpg','http://res.cloudinary.com/dsnp27eeq/image/upload/v1777389929/car_rental/users/Nguy%E1%BB%85n%20Do%C3%A3n%20Nam/cccd_back.jpg','http://res.cloudinary.com/dsnp27eeq/image/upload/v1777386192/car_rental/users/Nguy%E1%BB%85n%20Do%C3%A3n%20Nam/driver_license.jpg','car_rental/users/Nguyễn Doãn Nam/cccd_front','car_rental/users/Nguyễn Doãn Nam/cccd_back','car_rental/users/Nguyễn Doãn Nam/driver_license','270231006182','Không giới hạn','APPROVED','','2026-04-27 17:00:00'),(3,4,'http://res.cloudinary.com/dsnp27eeq/image/upload/v1778084593/car_rental/users/user1/cccd_front.jpg','http://res.cloudinary.com/dsnp27eeq/image/upload/v1778084595/car_rental/users/user1/cccd_back.jpg','http://res.cloudinary.com/dsnp27eeq/image/upload/v1778084598/car_rental/users/user1/driver_license.jpg','car_rental/users/user1/cccd_front','car_rental/users/user1/cccd_back','car_rental/users/user1/driver_license','270231006190','Không giới hạn','APPROVED','','2026-05-05 17:00:00'),(4,3,'http://res.cloudinary.com/dsnp27eeq/image/upload/v1778858303/car_rental/users/Nam%20Nguy%E1%BB%85n/cccd_front.png','http://res.cloudinary.com/dsnp27eeq/image/upload/v1778858306/car_rental/users/Nam%20Nguy%E1%BB%85n/cccd_back.jpg','http://res.cloudinary.com/dsnp27eeq/image/upload/v1778858308/car_rental/users/Nam%20Nguy%E1%BB%85n/driver_license.avif','car_rental/users/Nam Nguyễn/cccd_front','car_rental/users/Nam Nguyễn/cccd_back','car_rental/users/Nam Nguyễn/driver_license','270231006196','Không giới hạn','APPROVED','','2026-05-14 17:00:00'),(5,5,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'UNVERIFIED',NULL,NULL),(6,6,'http://res.cloudinary.com/dsnp27eeq/image/upload/v1778118617/car_rental/users/user3/cccd_front.jpg','http://res.cloudinary.com/dsnp27eeq/image/upload/v1778118621/car_rental/users/user3/cccd_back.avif','http://res.cloudinary.com/dsnp27eeq/image/upload/v1778118627/car_rental/users/user3/driver_license.jpg','car_rental/users/user3/cccd_front','car_rental/users/user3/cccd_back','car_rental/users/user3/driver_license','270231006888','Không giới hạn','APPROVED','','2026-05-06 17:00:00'),(7,7,'http://res.cloudinary.com/dsnp27eeq/image/upload/v1779630807/car_rental/users/userTest1/cccd_front.jpg','http://res.cloudinary.com/dsnp27eeq/image/upload/v1779630810/car_rental/users/userTest1/cccd_back.jpg','http://res.cloudinary.com/dsnp27eeq/image/upload/v1779630812/car_rental/users/userTest1/driver_license.jpg','car_rental/users/userTest1/cccd_front','car_rental/users/userTest1/cccd_back','car_rental/users/userTest1/driver_license','270231006167','Không giới hạn','PENDING',NULL,'2026-05-23 17:00:00');
/*!40000 ALTER TABLE `verifications` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-24 21:06:23
