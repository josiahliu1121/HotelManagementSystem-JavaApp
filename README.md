Mysql Database is used for this app, please create the following table to use.
Please change the properties in config.properties to your own database.

CREATE TABLE `employee` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `gender` tinyint DEFAULT NULL COMMENT '0 is female, 1 is male',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `room_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `room_name` varchar(50) NOT NULL,
  `suggested_price` decimal(10,2) NOT NULL,
  `count` int NOT NULL,
  `description` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `daily_room_status` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `available_count` int NOT NULL,
  `booked_count` int NOT NULL,
  `room_type_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `daily_room_status_room_type_id_fk` (`room_type_id`),
  CONSTRAINT `daily_room_status_room_type_id_fk` FOREIGN KEY (`room_type_id`) REFERENCES `room_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE `booking` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `customer_name` varchar(50) NOT NULL,
  `customer_phone` varchar(15) NOT NULL,
  `cost` decimal(10,2) NOT NULL,
  `booking_date` date NOT NULL,
  `adult` int NOT NULL,
  `child` int NOT NULL,
  `remarks` varchar(300) DEFAULT NULL,
  `daily_room_status_id` bigint NOT NULL,
  `room_type_id` bigint NOT NULL,
  `employee_id` bigint NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `booking_room_type_id_fk` (`room_type_id`),
  CONSTRAINT `booking_room_type_id_fk` FOREIGN KEY (`room_type_id`) REFERENCES `room_type` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
