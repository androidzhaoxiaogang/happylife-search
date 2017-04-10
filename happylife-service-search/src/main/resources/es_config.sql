/*
Navicat MySQL Data Transfer

Source Server         : test
Source Server Version : 50711
Source Host           : 119.23.113.78:3306
Source Database       : wgj_service

Target Server Type    : MYSQL
Target Server Version : 50711
File Encoding         : 65001

Date: 2017-04-10 15:19:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for es_config
-- ----------------------------
DROP TABLE IF EXISTS `es_config`;
CREATE TABLE `es_config` (
  `id` varchar(64) NOT NULL,
  `type` int(4) NOT NULL COMMENT '类型(0 商品  1 region)',
  `last_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '上次同步时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of es_config
-- ----------------------------
INSERT INTO `es_config` VALUES ('a81994b3-4553-4c3e-ade8-ce8a24989aa4', '0', '2017-04-08 14:16:17');
INSERT INTO `es_config` VALUES ('a81994b3-4553-4c3e-ade8-ce8a24989aa5', '1', '2017-04-07 15:19:19');