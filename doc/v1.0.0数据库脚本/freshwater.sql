/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : freshwater

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 03/08/2022 16:48:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for freshwater_button
-- ----------------------------
DROP TABLE IF EXISTS `freshwater_button`;
CREATE TABLE `freshwater_button`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `state` int NOT NULL COMMENT '状态,0:禁用,1:启用',
  `type` int NOT NULL COMMENT '类型:0:平台,1:APP',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '按钮编码',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '按钮名称',
  `menu_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单ID',
  `urls` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '可访问的url',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK7mowpqugji1ud3qwm4t1l5gqj`(`type`, `code`) USING BTREE,
  INDEX `IDX8b5h0sclg6bcif6lv58ch55ki`(`state`) USING BTREE,
  INDEX `FK5qmm1ni66u36rhyvfuyd4udjc`(`menu_id`) USING BTREE,
  CONSTRAINT `FK5qmm1ni66u36rhyvfuyd4udjc` FOREIGN KEY (`menu_id`) REFERENCES `freshwater_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '按钮' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of freshwater_button
-- ----------------------------
INSERT INTO `freshwater_button` VALUES ('e3ea94e46549747d6d37095f3cb4fea2', 1, 1, 'user-updateById', '更新', 'c2c01dc2880affad1a4524f7426bd33d', 'PUT:/v1/users', '2022-08-03 10:42:48', '2022-08-03 10:42:48');

-- ----------------------------
-- Table structure for freshwater_file
-- ----------------------------
DROP TABLE IF EXISTS `freshwater_file`;
CREATE TABLE `freshwater_file`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件相对路径',
  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件路径',
  `original_filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '源文件名',
  `effective_date` date NULL DEFAULT NULL COMMENT '有效期',
  `suffix` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件扩展名',
  `creator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件上传者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UKkb4n05fqspwegxav1uq1bvuny`(`path`, `filename`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of freshwater_file
-- ----------------------------
INSERT INTO `freshwater_file` VALUES ('2edab1d5d69707e208d76ab933eb535b', '/freshwater/20220803/2', '2c45a84e-de15-4079-9195-3734255c040e.jpg', 'avatar-6.jpg', '3999-01-01', 'jpg', '', '2022-08-03 15:05:28');
INSERT INTO `freshwater_file` VALUES ('389a910897d45134b3f81d67b12c80dc', '/freshwater/20220803/6', 'af3845a6-4a8c-47b9-97d7-fc33fab738f0.jpg', 'avatar-6.jpg', '3999-01-01', 'jpg', '', '2022-08-03 14:53:42');

-- ----------------------------
-- Table structure for freshwater_menu
-- ----------------------------
DROP TABLE IF EXISTS `freshwater_menu`;
CREATE TABLE `freshwater_menu`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `state` int NOT NULL COMMENT '状态,0:禁用,1:启用',
  `type` int NOT NULL COMMENT '类型:0平台,1APP',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单编码',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '菜单地址',
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `parent_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上级ID',
  `sort_index` int NOT NULL COMMENT '排序,数字越小,权重越大',
  `creator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人账号',
  `creator_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人姓名',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modifier` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '最后修改人账号',
  `modifier_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '最后修改人姓名',
  `modify_time` datetime NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK6r8u50xfdrwtd83hs0w4etbnr`(`type`, `code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of freshwater_menu
-- ----------------------------
INSERT INTO `freshwater_menu` VALUES ('922662ee6e440e14e7d947b5b2f6ed80', 1, 0, '0003', '角色管理', '', '0003', 'b6f3e9e9a17f2236a311e7883f85a4c2', 2, 'admin', '超级管理员', '2022-08-03 10:30:31', 'admin', '超级管理员', '2022-08-03 10:30:31');
INSERT INTO `freshwater_menu` VALUES ('b6f3e9e9a17f2236a311e7883f85a4c2', 1, 0, '0001', '系统设置', '', '0001', '', 0, 'admin', '超级管理员', '2022-08-03 10:29:40', 'admin', '超级管理员', '2022-08-03 10:29:40');
INSERT INTO `freshwater_menu` VALUES ('c2c01dc2880affad1a4524f7426bd33d', 1, 0, '0002', '用户管理', '', '0002', 'b6f3e9e9a17f2236a311e7883f85a4c2', 1, 'admin', '超级管理员', '2022-08-03 10:30:16', 'admin', '超级管理员', '2022-08-03 10:30:16');
INSERT INTO `freshwater_menu` VALUES ('dd9dd6278a0059c874f2aeb9f82403d1', 1, 0, '0004', '菜单管理', '', '0004', 'b6f3e9e9a17f2236a311e7883f85a4c2', 3, 'admin', '超级管理员', '2022-08-03 10:30:55', 'admin', '超级管理员', '2022-08-03 10:30:55');

-- ----------------------------
-- Table structure for freshwater_role
-- ----------------------------
DROP TABLE IF EXISTS `freshwater_role`;
CREATE TABLE `freshwater_role`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `state` int NOT NULL COMMENT '状态:0禁用,1启用',
  `is_system` bit(1) NOT NULL COMMENT '是否是系统角色',
  `creator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人账号',
  `creator_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人姓名',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modifier` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '修改人账号',
  `modifier_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '修改人姓名',
  `modify_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UKjs9p6y8la6sq6n5qah3cnocaq`(`code`) USING BTREE,
  UNIQUE INDEX `UKjfpb8b11uifi7gwvadoct941b`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of freshwater_role
-- ----------------------------
INSERT INTO `freshwater_role` VALUES ('002d83e8d8da1a8faa4c3e6207cd484a', 'ADMIN', '超级管理员', 1, b'1', 'admin', 'admin', '2022-08-01 10:36:36', 'admin', 'admin', '2022-08-01 10:36:43');
INSERT INTO `freshwater_role` VALUES ('44b8a58d8ee880e85649133a41bf33a1', '00003', '普通管理员', 1, b'0', 'admin', '超级管理员', '2022-08-01 10:42:00', 'admin', '超级管理员', '2022-08-01 10:42:00');

-- ----------------------------
-- Table structure for freshwater_role_button_mapping
-- ----------------------------
DROP TABLE IF EXISTS `freshwater_role_button_mapping`;
CREATE TABLE `freshwater_role_button_mapping`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `role_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色ID',
  `button_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '按钮ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK70abipd7u34auo0d2nagapx69`(`role_id`, `button_id`) USING BTREE,
  INDEX `FKgekf404y5tc3laf538khhsgri`(`button_id`) USING BTREE,
  CONSTRAINT `FK30kprlp7nhcfflmtsgtfognr` FOREIGN KEY (`role_id`) REFERENCES `freshwater_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKgekf404y5tc3laf538khhsgri` FOREIGN KEY (`button_id`) REFERENCES `freshwater_button` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色按钮关联信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of freshwater_role_button_mapping
-- ----------------------------
INSERT INTO `freshwater_role_button_mapping` VALUES ('89d6c8ac97919022688ad53da6c01a30', '44b8a58d8ee880e85649133a41bf33a1', 'e3ea94e46549747d6d37095f3cb4fea2');

-- ----------------------------
-- Table structure for freshwater_role_menu_mapping
-- ----------------------------
DROP TABLE IF EXISTS `freshwater_role_menu_mapping`;
CREATE TABLE `freshwater_role_menu_mapping`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `role_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色ID',
  `menu_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UKcrxn38xwxsu0fjhbyh0op6aoq`(`role_id`, `menu_id`) USING BTREE,
  INDEX `FK6i7mb84khtkofbc277jyrfm6o`(`menu_id`) USING BTREE,
  CONSTRAINT `FK6i7mb84khtkofbc277jyrfm6o` FOREIGN KEY (`menu_id`) REFERENCES `freshwater_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKtgvwt4y52h63e25kovy46me1y` FOREIGN KEY (`role_id`) REFERENCES `freshwater_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单关联信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of freshwater_role_menu_mapping
-- ----------------------------
INSERT INTO `freshwater_role_menu_mapping` VALUES ('4f89497224ebd7d27da3008c5258c680', '002d83e8d8da1a8faa4c3e6207cd484a', '922662ee6e440e14e7d947b5b2f6ed80');
INSERT INTO `freshwater_role_menu_mapping` VALUES ('50ed7a22d3df5b949e3c3f8935593c88', '002d83e8d8da1a8faa4c3e6207cd484a', 'b6f3e9e9a17f2236a311e7883f85a4c2');
INSERT INTO `freshwater_role_menu_mapping` VALUES ('0047f5428fed811b7b5c2f3f6a93daf2', '002d83e8d8da1a8faa4c3e6207cd484a', 'c2c01dc2880affad1a4524f7426bd33d');
INSERT INTO `freshwater_role_menu_mapping` VALUES ('41fbbedbfa5ed959a1704ff8c5178546', '002d83e8d8da1a8faa4c3e6207cd484a', 'dd9dd6278a0059c874f2aeb9f82403d1');
INSERT INTO `freshwater_role_menu_mapping` VALUES ('a98668c8d9f4e9ba95ef647d4b19d689', '44b8a58d8ee880e85649133a41bf33a1', '922662ee6e440e14e7d947b5b2f6ed80');
INSERT INTO `freshwater_role_menu_mapping` VALUES ('d3fd12aa3b437e9edf78caa37340ce5a', '44b8a58d8ee880e85649133a41bf33a1', 'b6f3e9e9a17f2236a311e7883f85a4c2');
INSERT INTO `freshwater_role_menu_mapping` VALUES ('160501b29509467ecec0bc5d32e224be', '44b8a58d8ee880e85649133a41bf33a1', 'c2c01dc2880affad1a4524f7426bd33d');
INSERT INTO `freshwater_role_menu_mapping` VALUES ('af6bdc4c2b5b0c0a192b234124284fde', '44b8a58d8ee880e85649133a41bf33a1', 'dd9dd6278a0059c874f2aeb9f82403d1');

-- ----------------------------
-- Table structure for freshwater_user
-- ----------------------------
DROP TABLE IF EXISTS `freshwater_user`;
CREATE TABLE `freshwater_user`  (
  `id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `user_account` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户登录账号',
  `mobile` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户姓名',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `state` int NOT NULL COMMENT '状态:0禁用,1:启用',
  `creator` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人账号',
  `creator_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人姓名',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modifier` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '修改人账号',
  `modifier_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '修改人姓名',
  `modify_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UK56w4qes7kt02iq0cxt6n89ook`(`user_account`) USING BTREE,
  UNIQUE INDEX `UKm7464ro7ests90ek1h5qjgy9v`(`mobile`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of freshwater_user
-- ----------------------------
INSERT INTO `freshwater_user` VALUES ('8fc64511597d59600e244a7ebf681dc7', 'admin', 'admin', '68c352a2d7e0367be499dca3c8ceca66c7b4926f386c6199856db2e0c1054d2d266f42d9dcca09d4', '超级管理员', '1234@qq.com', 1, 'admin', 'admin', '2022-08-01 10:08:24', 'admin', 'admin', '2022-08-01 10:08:24');
INSERT INTO `freshwater_user` VALUES ('b9968f692b97045d3e416b179d9d406c', '18090884329', '18090884329', 'bc10b56f2a5a48c0158fe862abedd449447552d23f7f43af7f28fd882eea27852e55078538847d22', '普通账号', '98765432@qq.com', 1, '18090884329', '18090884329', '2022-08-03 10:18:25', '18090884329', '普通账号', '2022-08-03 11:13:57');

-- ----------------------------
-- Table structure for freshwater_user_role_mapping
-- ----------------------------
DROP TABLE IF EXISTS `freshwater_user_role_mapping`;
CREATE TABLE `freshwater_user_role_mapping`  (
  `id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `role_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `UKm9jja3k8ngxdxjxfx4ntids9f`(`user_id`, `role_id`) USING BTREE,
  INDEX `FKpolexvtex7mhxkib6ef8op7w5`(`role_id`) USING BTREE,
  CONSTRAINT `FK8jwx2tl8m04oj1oxypwye4c49` FOREIGN KEY (`user_id`) REFERENCES `freshwater_user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `FKpolexvtex7mhxkib6ef8op7w5` FOREIGN KEY (`role_id`) REFERENCES `freshwater_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of freshwater_user_role_mapping
-- ----------------------------
INSERT INTO `freshwater_user_role_mapping` VALUES ('8fc64231sd597d59612fe244a7ebf68dc7', '8fc64511597d59600e244a7ebf681dc7', '002d83e8d8da1a8faa4c3e6207cd484a');
INSERT INTO `freshwater_user_role_mapping` VALUES ('7f94fd6a65884b4aee0b7abed542a951', 'b9968f692b97045d3e416b179d9d406c', '44b8a58d8ee880e85649133a41bf33a1');

SET FOREIGN_KEY_CHECKS = 1;
