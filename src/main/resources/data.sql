-- --------------------------------------------------------
-- Хост:                         127.0.0.1
-- Версия на сървъра:            5.7.12-log - MySQL Community Server (GPL)
-- ОС на сървъра:                Win64
-- HeidiSQL Версия:              9.4.0.5130
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for product_catalog
DROP DATABASE IF EXISTS `product_catalog`;
CREATE DATABASE IF NOT EXISTS `product_catalog` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `product_catalog`;

-- Дъмп структура за таблица product_catalog.brands
DROP TABLE IF EXISTS `brands`;
CREATE TABLE IF NOT EXISTS `brands` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '0',
  `country` varchar(100) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- Дъмп данни за таблица product_catalog.brands: ~1 rows (approximately)
DELETE FROM `brands`;
/*!40000 ALTER TABLE `brands` DISABLE KEYS */;
INSERT INTO `brands` (`id`, `name`, `country`) VALUES
	(1, 'Adidas', 'Germany');
/*!40000 ALTER TABLE `brands` ENABLE KEYS */;

-- Дъмп структура за таблица product_catalog.history
DROP TABLE IF EXISTS `history`;
CREATE TABLE IF NOT EXISTS `history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) DEFAULT NULL,
  `price_difference` double(10,4) DEFAULT NULL,
  `changed_on` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_history_products` (`product_id`),
  CONSTRAINT `fk_history_products` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;


-- Дъмп данни за таблица product_catalog.history: ~1 rows (approximately)
DELETE FROM `history`;
/*!40000 ALTER TABLE `history` DISABLE KEYS */;
INSERT INTO `history` (`id`, `product_id`, `price_difference`, `changed_on`) VALUES
	(1, 18, -50.0000, '2017-01-19');
/*!40000 ALTER TABLE `history` ENABLE KEYS */;

-- Дъмп структура за таблица product_catalog.products
DROP TABLE IF EXISTS `products`;
CREATE TABLE IF NOT EXISTS `products` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` longtext NOT NULL,
  `product_type_id` int(11) NOT NULL,
  `weight_type_id` int(11) NOT NULL,
  `weight_value` double(10,4) NOT NULL DEFAULT '0.0000',
  `brand_id` int(11) NOT NULL,
  `price` decimal(10,4) NOT NULL DEFAULT '0.0000',
  `expiration_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_products_product_types` (`product_type_id`),
  KEY `FK_products_weight_types` (`weight_type_id`),
  KEY `FK_products_brands` (`brand_id`),
  CONSTRAINT `FK_products_brands` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`),
  CONSTRAINT `FK_products_product_types` FOREIGN KEY (`product_type_id`) REFERENCES `product_types` (`id`),
  CONSTRAINT `FK_products_weight_types` FOREIGN KEY (`weight_type_id`) REFERENCES `weight_types` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

-- Дъмп данни за таблица product_catalog.products: ~30 rows (approximately)
DELETE FROM `products`;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` (`id`, `name`, `description`, `product_type_id`, `weight_type_id`, `weight_value`, `brand_id`, `price`, `expiration_date`) VALUES
	(1, 'Stamat', 'Some product description 1', 1, 2, 0.5000, 1, 36.0000, '2017-01-16 00:00:00'),
	(2, 'Stamat 1', 'Some product description 1', 1, 2, 0.5000, 1, 36.0000, '2017-01-16 00:00:00'),
	(3, 'Stamat 2', 'Some product description 2', 1, 3, 1.5000, 1, 37.0000, '2018-01-16 00:00:00'),
	(4, 'Stamat 3', 'Some product description 3', 1, 2, 2.5000, 1, 38.0000, '2019-01-16 00:00:00'),
	(5, 'Stamat 4', 'Some product description 4', 1, 2, 3.5000, 1, 39.0000, '2020-01-16 00:00:00'),
	(6, 'Stamat 5', 'Some product description 5', 1, 2, 4.5000, 1, 40.0000, '2021-01-16 00:00:00'),
	(7, 'Stamat 6', 'Some product description 6', 1, 2, 5.5000, 1, 41.0000, '2022-01-16 00:00:00'),
	(8, 'Stamat 7', 'Some product description 7', 1, 1, 6.5000, 1, 42.0000, '2023-01-16 00:00:00'),
	(9, 'Stamat 8', 'Some product description 8', 1, 2, 7.5000, 1, 43.0000, '2024-01-16 00:00:00'),
	(10, 'Stamat 9', 'Some product description 9', 1, 2, 8.5000, 1, 44.0000, '2025-01-16 00:00:00'),
	(11, 'Stamat 10', 'Some product description 10', 1, 2, 9.5000, 1, 45.0000, '2026-01-16 00:00:00'),
	(12, 'Stamat 11', 'Some product description 11', 1, 2, 10.5000, 1, 46.0000, '2027-01-16 00:00:00'),
	(14, 'Stamat 13', 'Some product description 13', 1, 2, 12.5000, 1, 48.0000, '2029-01-16 00:00:00'),
	(15, 'Stamat 14', 'Some product description 14', 1, 2, 13.5000, 1, 49.0000, '2030-01-16 00:00:00'),
	(16, 'Stamat 15', 'Some product description 15', 1, 2, 14.5000, 1, 50.0000, '2031-01-16 00:00:00'),
	(17, 'Pesho', 'Import from Add Product', 2, 3, 12.5000, 1, 46.0000, '2017-01-01 00:00:00'),
	(18, 'Lokumcho', 'Clear test', 3, 1, 20.0000, 1, 1212.0000, '2016-09-14 00:00:00'),
	(19, 'Stamat 1', 'Some product description 1', 1, 2, 0.5000, 1, 36.0000, '2017-01-16 00:00:00'),
	(20, 'Stamat 2', 'Some product description 2', 1, 3, 1.5000, 1, 37.0000, '2018-01-16 00:00:00'),
	(21, 'Stamat 3', 'Some product description 3', 1, 2, 2.5000, 1, 38.0000, '2019-01-16 00:00:00'),
	(22, 'Stamat 4', 'Some product description 4', 1, 2, 3.5000, 1, 39.0000, '2020-01-16 00:00:00'),
	(23, 'Stamat 5', 'Some product description 5', 1, 2, 4.5000, 1, 40.0000, '2021-01-16 00:00:00'),
	(24, 'Stamat 6', 'Some product description 6', 1, 2, 5.5000, 1, 41.0000, '2022-01-16 00:00:00'),
	(25, 'Stamat 7', 'Some product description 7', 1, 1, 6.5000, 1, 42.0000, '2023-01-16 00:00:00'),
	(26, 'Stamat 8', 'Some product description 8', 1, 2, 7.5000, 1, 43.0000, '2024-01-16 00:00:00'),
	(27, 'Stamat 9', 'Some product description 9', 1, 2, 8.5000, 1, 44.0000, '2025-01-16 00:00:00'),
	(28, 'Stamat 10', 'Some product description 10', 1, 2, 9.5000, 1, 45.0000, '2026-01-16 00:00:00'),
	(29, 'Stamat 11', 'Some product description 11', 1, 2, 10.5000, 1, 46.0000, '2027-01-16 00:00:00'),
	(30, 'Stamat 12', 'Some product description 12', 1, 2, 11.5000, 1, 47.0000, '2028-01-16 00:00:00'),
	(31, 'Stamat 13', 'Some product description Edited Some product description Edited Some product description Edited\nSome product description Edited Some product description Edited and again', 1, 2, 12.5000, 1, 48.0000, '2017-01-13 00:00:00');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;

-- Дъмп структура за таблица product_catalog.product_types
DROP TABLE IF EXISTS `product_types`;
CREATE TABLE IF NOT EXISTS `product_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Дъмп данни за таблица product_catalog.product_types: ~3 rows (approximately)
DELETE FROM `product_types`;
/*!40000 ALTER TABLE `product_types` DISABLE KEYS */;
INSERT INTO `product_types` (`id`, `name`) VALUES
	(1, 'food'),
	(2, 'clothes'),
	(3, 'appliances');
/*!40000 ALTER TABLE `product_types` ENABLE KEYS */;

-- Дъмп структура за таблица product_catalog.weight_types
DROP TABLE IF EXISTS `weight_types`;
CREATE TABLE IF NOT EXISTS `weight_types` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET latin1 NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Дъмп данни за таблица product_catalog.weight_types: ~3 rows (approximately)
DELETE FROM `weight_types`;
/*!40000 ALTER TABLE `weight_types` DISABLE KEYS */;
INSERT INTO `weight_types` (`id`, `name`) VALUES
	(1, 'litres'),
	(2, 'kg'),
	(3, 'cm');
/*!40000 ALTER TABLE `weight_types` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
