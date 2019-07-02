/*
Navicat MySQL Data Transfer

Source Server         : localhost-push
Source Server Version : 50721
Source Host           : localhost:3306
Source Database       : push

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2019-07-03 07:26:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for app_account
-- ----------------------------
DROP TABLE IF EXISTS `app_account`;
CREATE TABLE `app_account` (
  `app_id` bigint(20) NOT NULL,
  `app_key` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `app_secret_key` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT '0',
  `client_token` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `app_token` varchar(60) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `client_token_expire` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `app_token_expire` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `mobile_phone` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of app_account
-- ----------------------------
INSERT INTO `app_account` VALUES ('517723931931574272', '9f5d74bb0f68', 'cb2eb85b362941f1b3e1', 'a8e0cb7d5c45b6045e13bc09002c7374', 'd41d49c16013850b522189b34d31658e', '2018-11-29 15:30:04', '2018-11-29 15:30:04', '2021-11-29 15:30:04', '2021-11-29 15:30:04', '15600000000');
