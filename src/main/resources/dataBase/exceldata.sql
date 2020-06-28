/*
Navicat MySQL Data Transfer

Source Server         : reinsurance
Source Server Version : 80016
Source Host           : 10.8.40.107:3306
Source Database       : reinsurance

Target Server Type    : MYSQL
Target Server Version : 80016
File Encoding         : 65001

Date: 2019-07-11 15:10:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_exceldata
-- ----------------------------
DROP TABLE IF EXISTS `sys_exceldata`;
CREATE TABLE `sys_exceldata` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `office_id` int(11) NOT NULL,
  `login_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=80 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of sys_exceldata
-- ----------------------------
INSERT INTO `sys_exceldata` VALUES ('2', '1', 'panwei1', '潘伟');
INSERT INTO `sys_exceldata` VALUES ('5', '11', 'zhangsan', '张三');
INSERT INTO `sys_exceldata` VALUES ('6', '11', 'lisi', '李四');
INSERT INTO `sys_exceldata` VALUES ('9', '1', 'panwei', '潘伟');
INSERT INTO `sys_exceldata` VALUES ('65', '1', 'Aaaa', 'ewreew');
INSERT INTO `sys_exceldata` VALUES ('66', '1', 'rtr', 'ser');
INSERT INTO `sys_exceldata` VALUES ('67', '15', 'uyiui', 'rtr');
INSERT INTO `sys_exceldata` VALUES ('76', '1', 'yiu', '就覅偶是降低哦分');
INSERT INTO `sys_exceldata` VALUES ('79', '1', 'ad', 'dsada');
