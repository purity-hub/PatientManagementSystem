/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : patientsystem

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 26/12/2021 13:56:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for doctor
-- ----------------------------
DROP TABLE IF EXISTS `doctor`;
CREATE TABLE `doctor`  (
  `did` int NOT NULL AUTO_INCREMENT,
  `dname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `dsex` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `dage` int NULL DEFAULT NULL,
  `dtel` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `department` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`did`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of doctor
-- ----------------------------
INSERT INTO `doctor` VALUES (1, '纪默荷', '女', 32, '15100703872', '内科');
INSERT INTO `doctor` VALUES (2, '瞿鹤宸', '男', 43, '15677715023', '内科');
INSERT INTO `doctor` VALUES (3, '郭轶昭', '男', 44, '16640072861', '内科');
INSERT INTO `doctor` VALUES (4, '富丞存', '女', 34, '17552225066', '内科');
INSERT INTO `doctor` VALUES (5, '牧懿煊', '女', 54, '13338166351', '外科');
INSERT INTO `doctor` VALUES (6, '范羚苓', '女', 33, '16694078678', '外科');
INSERT INTO `doctor` VALUES (7, '樊怡冉', '女', 43, '15277984025', '皮肤科');
INSERT INTO `doctor` VALUES (8, '黎展茵', '女', 54, '13848961666', '皮肤科');
INSERT INTO `doctor` VALUES (9, '管将靖', '男', 63, '15265578156', '妇科');
INSERT INTO `doctor` VALUES (10, '江跃芬', '女', 34, '13148385022', '妇科');
INSERT INTO `doctor` VALUES (11, '卓娆海', '女', 55, '19993061138', '妇科');
INSERT INTO `doctor` VALUES (12, '曹旋品', '男', 65, '15159652927', '儿科');
INSERT INTO `doctor` VALUES (13, '韶珣菡', '女', 45, '17253967003', '儿科');
INSERT INTO `doctor` VALUES (14, '劳爽谨', '女', 34, '18808634132', '儿科');
INSERT INTO `doctor` VALUES (15, '黄琼蕾', '女', 34, '18181495837', '骨科');
INSERT INTO `doctor` VALUES (16, '屈骊嫱', '女', 56, '13218334701', '骨科');
INSERT INTO `doctor` VALUES (17, '庞霁妲', '女', 65, '14923278670', '骨科');
INSERT INTO `doctor` VALUES (18, '申骥琼', '男', 45, '13531349642', '神经科');
INSERT INTO `doctor` VALUES (19, '单麒涌', '男', 56, '15290049343', '神经科');

-- ----------------------------
-- Table structure for medical_records
-- ----------------------------
DROP TABLE IF EXISTS `medical_records`;
CREATE TABLE `medical_records`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `pid` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `pathogen` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `medical_records_patient_pid_fk`(`pid`) USING BTREE,
  CONSTRAINT `medical_records_patient_pid_fk` FOREIGN KEY (`pid`) REFERENCES `patient` (`pid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of medical_records
-- ----------------------------
INSERT INTO `medical_records` VALUES (1, '110101199003073415', '发烧');
INSERT INTO `medical_records` VALUES (2, '110101199003073415', '感冒');
INSERT INTO `medical_records` VALUES (3, '110101199003073415', '头皮瘙痒');
INSERT INTO `medical_records` VALUES (4, '360731122112015474', '发烧');
INSERT INTO `medical_records` VALUES (5, '360731122112015477', '发烧');
INSERT INTO `medical_records` VALUES (6, '222222222222222222', '测试');

-- ----------------------------
-- Table structure for nurse
-- ----------------------------
DROP TABLE IF EXISTS `nurse`;
CREATE TABLE `nurse`  (
  `username` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of nurse
-- ----------------------------
INSERT INTO `nurse` VALUES ('井珍笑', '784634');
INSERT INTO `nurse` VALUES ('任含虹', '685568');
INSERT INTO `nurse` VALUES ('奚赢行', '635813');
INSERT INTO `nurse` VALUES ('姚玺辰', '029286');
INSERT INTO `nurse` VALUES ('孙锟京', '173274');
INSERT INTO `nurse` VALUES ('宣姗珊', '835122');
INSERT INTO `nurse` VALUES ('富歌争', '463275');
INSERT INTO `nurse` VALUES ('朱灿骏', '195352');
INSERT INTO `nurse` VALUES ('梅唯斯', '621617');
INSERT INTO `nurse` VALUES ('江曼梓', '815780');
INSERT INTO `nurse` VALUES ('测试', '111111');
INSERT INTO `nurse` VALUES ('瞿寒喻', '041318');
INSERT INTO `nurse` VALUES ('祁振苏', '574650');
INSERT INTO `nurse` VALUES ('薛漫诗', '093938');
INSERT INTO `nurse` VALUES ('邓晨颉', '144570');
INSERT INTO `nurse` VALUES ('郎亚景', '253247');
INSERT INTO `nurse` VALUES ('龚嫣娜', '516595');

-- ----------------------------
-- Table structure for patient
-- ----------------------------
DROP TABLE IF EXISTS `patient`;
CREATE TABLE `patient`  (
  `pid` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `pname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `psex` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `page` int NULL DEFAULT NULL,
  `ptel` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of patient
-- ----------------------------
INSERT INTO `patient` VALUES ('11010119900307109X', '段竹福', '男', 54, '18709563709');
INSERT INTO `patient` VALUES ('11010119900307117X', '钟奎琥', '女', 74, '15317201680');
INSERT INTO `patient` VALUES ('110101199003073415', '秦美卿', '女', 14, '17760841419');
INSERT INTO `patient` VALUES ('110101199003076157', '江琼耘', '女', 5, '13926532382');
INSERT INTO `patient` VALUES ('110101199003076958', '宋杨玮', '男', 74, '17719143003');
INSERT INTO `patient` VALUES ('110101199003078398', '胡桃', '女', 19, '14557872196');
INSERT INTO `patient` VALUES ('201100121221001214', '测试', '男', 21, '18512739313');
INSERT INTO `patient` VALUES ('222222222222222222', '测试', '男', 21, '11111111111');
INSERT INTO `patient` VALUES ('360102199003072335', '龚汉操', '男', 24, '15622708130');
INSERT INTO `patient` VALUES ('360102199003074912', '祁苏钧', '女', 13, '15956185971');
INSERT INTO `patient` VALUES ('360102199003076918', '成熠斌', '男', 25, '14568362438');
INSERT INTO `patient` VALUES ('360102199003078817', '詹东翼', '男', 35, '18211904758');
INSERT INTO `patient` VALUES ('360102199003079019', '喻冉仪', '女', 46, '55555555555');
INSERT INTO `patient` VALUES ('360731122112015474', '肖宫', '女', 21, '21122145111');
INSERT INTO `patient` VALUES ('360731122112015477', '神里', '女', 18, '21122145177');
INSERT INTO `patient` VALUES ('360731200112037611', '罗还有', '男', 12, '11111111111');
INSERT INTO `patient` VALUES ('360731201212124457', '王小美', '女', 32, '12221112314');
INSERT INTO `patient` VALUES ('362144121205215478', '钟离', '男', 32, '11111111111');

-- ----------------------------
-- Table structure for queue
-- ----------------------------
DROP TABLE IF EXISTS `queue`;
CREATE TABLE `queue`  (
  `visitnumber` int NOT NULL AUTO_INCREMENT,
  `did` int NULL DEFAULT NULL,
  `pid` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `starttime` datetime NULL DEFAULT NULL,
  `endtime` datetime NULL DEFAULT NULL,
  `paymoney` double NULL DEFAULT NULL,
  PRIMARY KEY (`visitnumber`) USING BTREE,
  INDEX `queue_doctor_did_fk`(`did`) USING BTREE,
  INDEX `queue_patient_pid_fk`(`pid`) USING BTREE,
  CONSTRAINT `queue_doctor_did_fk` FOREIGN KEY (`did`) REFERENCES `doctor` (`did`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `queue_patient_pid_fk` FOREIGN KEY (`pid`) REFERENCES `patient` (`pid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of queue
-- ----------------------------
INSERT INTO `queue` VALUES (4, 1, '360731200112037611', '2021-12-25 20:20:26', NULL, NULL);
INSERT INTO `queue` VALUES (5, 8, '360731201212124457', '2021-12-25 20:32:27', '2021-12-26 13:16:23', NULL);
INSERT INTO `queue` VALUES (6, 3, '362144121205215478', '2021-12-25 20:34:22', NULL, NULL);
INSERT INTO `queue` VALUES (9, 8, '201100121221001214', '2021-12-25 20:38:47', '2021-12-25 21:36:03', 1444);
INSERT INTO `queue` VALUES (12, 8, '201100121221001214', '2021-12-25 21:07:09', '2021-12-25 21:33:15', NULL);
INSERT INTO `queue` VALUES (22, 8, '110101199003073415', '2021-12-26 12:10:46', '2021-12-26 12:42:52', 1221);
INSERT INTO `queue` VALUES (23, 11, '110101199003073415', '2021-12-26 12:11:01', NULL, NULL);
INSERT INTO `queue` VALUES (24, 10, '110101199003073415', '2021-12-26 12:18:03', NULL, NULL);
INSERT INTO `queue` VALUES (25, 3, '110101199003073415', '2021-12-26 12:18:15', '2021-12-26 12:18:20', NULL);
INSERT INTO `queue` VALUES (26, 4, '360731122112015474', '2021-12-26 12:55:49', NULL, NULL);
INSERT INTO `queue` VALUES (27, 4, '360731122112015477', '2021-12-26 12:56:36', NULL, NULL);
INSERT INTO `queue` VALUES (28, 10, '222222222222222222', '2021-12-26 13:43:48', '2021-12-26 13:44:06', 149);

SET FOREIGN_KEY_CHECKS = 1;
