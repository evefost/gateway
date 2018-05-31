/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50722
Source Host           : localhost:3306
Source Database       : gateway-db

Target Server Type    : MYSQL
Target Server Version : 50722
File Encoding         : 65001

Date: 2018-05-31 18:30:10
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for gateway_app
-- ----------------------------
DROP TABLE IF EXISTS `gateway_app`;
CREATE TABLE `gateway_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` varchar(64) DEFAULT '' COMMENT '服务id应用唯一标识',
  `name` varchar(64) DEFAULT '' COMMENT '应用名称',
  `description` varchar(255) DEFAULT NULL COMMENT '应用描述',
  `version` varchar(8) DEFAULT NULL COMMENT '版本号',
  `enable` tinyint(4) DEFAULT '0' COMMENT '1,启用服务，0禁用服务',
  `context_path` varchar(48) DEFAULT NULL COMMENT '应用根路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of gateway_app
-- ----------------------------
INSERT INTO `gateway_app` VALUES ('1', 'server-a', 'servaeaaa', '666666', '22', '0', '/a-path');
INSERT INTO `gateway_app` VALUES ('3', 'server-b', 'servaeaaa', '666666', '14', '0', '');
INSERT INTO `gateway_app` VALUES ('5', 'server-c', 'servaeaaa', '666666', '14', '0', null);
INSERT INTO `gateway_app` VALUES ('8', 'xhg-boss', '小黄狗boss系统', '小黄狗boss系统', '0', '1', '');
INSERT INTO `gateway_app` VALUES ('9', 'xhg-customer', '小黄狗customer', '小黄狗customer', '0', '1', '/customer');
INSERT INTO `gateway_app` VALUES ('10', 'xhg-merchant', 'xhg-merchant', '666666', '0', '1', '/merchant');

-- ----------------------------
-- Table structure for gateway_app_noauth_uri
-- ----------------------------
DROP TABLE IF EXISTS `gateway_app_noauth_uri`;
CREATE TABLE `gateway_app_noauth_uri` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_id` int(1) DEFAULT NULL COMMENT '应用id(app表的)',
  `url` varchar(128) NOT NULL DEFAULT '' COMMENT '非受限url',
  `description` varchar(128) DEFAULT NULL COMMENT 'uri作用描述',
  `enable` tinyint(4) DEFAULT '1' COMMENT '1,启用(非受权uri生效)，0禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of gateway_app_noauth_uri
-- ----------------------------
INSERT INTO `gateway_app_noauth_uri` VALUES ('1', '1', '/99?/7777dfdfd', 'dfdfdfd', '1');
INSERT INTO `gateway_app_noauth_uri` VALUES ('2', '1', '/test2/getUser', 'dfdfdfd', '1');
INSERT INTO `gateway_app_noauth_uri` VALUES ('7', '8', '/boss/manager/user/dologin', 'dfdfdfd', '1');
INSERT INTO `gateway_app_noauth_uri` VALUES ('9', '1', '/test/login', 'server-a的登录接口', '1');
INSERT INTO `gateway_app_noauth_uri` VALUES ('10', '9', '/v1.0/user/login', '登录接口', '1');
INSERT INTO `gateway_app_noauth_uri` VALUES ('11', '10', '/v1.0/login', 'dfdfdfd', '1');

-- ----------------------------
-- Table structure for oauth2_client
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_client`;
CREATE TABLE `oauth2_client` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '客户端id',
  `client_name` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '客户端名称',
  `client_id` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '客户端ID',
  `client_secret` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '客户端安全码',
  `app_id` int(11) DEFAULT NULL COMMENT '应用id(app表的)',
  `redirect_uri` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '重定向uri',
  `client_uri` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '客户端uri',
  `client_icon_uri` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '客户端图标',
  `resource_ids` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '资源id',
  `scope` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '授权范围',
  `grant_types` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '授权类型',
  `allowed_ips` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '允许的IP',
  `status` int(11) DEFAULT NULL COMMENT '客户端状态',
  `trusted` int(11) DEFAULT NULL COMMENT '可信',
  `expire` int(11) DEFAULT '0' COMMENT '有效期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `client_id` (`client_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='客户端认证信息(只有注册的应用才能被认证)';

-- ----------------------------
-- Records of oauth2_client
-- ----------------------------
INSERT INTO `oauth2_client` VALUES ('1', 'boss系统', 'xhg-bosss', '66666', null, null, null, null, null, null, null, null, null, null, '0');
INSERT INTO `oauth2_client` VALUES ('2', 'server-a服务', 'server-a', '123456', '1', null, null, null, null, null, null, null, null, null, '0');
INSERT INTO `oauth2_client` VALUES ('3', 'xhg-customer', 'xhg-customer', '123456', '9', null, null, null, null, null, null, null, null, null, '0');
INSERT INTO `oauth2_client` VALUES ('4', 'xhg-merchant', 'xhg-merchant', '123456', '10', null, null, null, null, null, null, null, null, null, '0');
