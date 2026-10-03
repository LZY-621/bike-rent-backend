mysqldump : mysqldump: [Warning] Using a password on the command line interface can be insecure.
At line:15 char:2789
+ ... r created"; mysqldump -uroot -p970805 --databases bike_rent --default ...
+                 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    + CategoryInfo          : NotSpecified: (mysqldump: [War...an be insecure.:String) [], RemoteException
    + FullyQualifiedErrorId : NativeCommandError
 
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: bike_rent
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `bike_rent`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `bike_rent` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `bike_rent`;

--
-- Table structure for table `bike`
--

DROP TABLE IF EXISTS `bike`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bike` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自行车ID',
  `bike_no` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '自行车编号（唯一）',
  `status` tinyint DEFAULT '0' COMMENT '状态：0-不可用，1-可用',
  `location` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '当前位置',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_bike_no` (`bike_no`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='自行车表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bike`
--

LOCK TABLES `bike` WRITE;
/*!40000 ALTER TABLE `bike` DISABLE KEYS */;
INSERT INTO `bike` VALUES (1,'B001',1,'图书馆门口','2026-10-03 20:21:06'),(2,'B002',1,'地铁站A口','2026-10-03 20:21:06'),(3,'B003',1,'科技园B栋','2026-10-03 20:21:06'),(4,'B004',0,'正在维修','2026-10-03 20:21:06'),(5,'B005',1,'万达西广场','2026-10-03 20:21:06');
/*!40000 ALTER TABLE `bike` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rent_order`
--

DROP TABLE IF EXISTS `rent_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rent_order` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `user_id` int NOT NULL COMMENT '所属用户ID',
  `bike_id` int NOT NULL COMMENT '租赁自行车ID',
  `rent_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '开始租赁时间',
  `return_time` datetime DEFAULT NULL COMMENT '实际归还时间',
  `cost` decimal(10,2) DEFAULT '0.00' COMMENT '租赁费用（元）',
  `status` tinyint DEFAULT '1' COMMENT '订单状态：1-租赁中，2-已归还',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_bike_id` (`bike_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租赁订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rent_order`
--

LOCK TABLES `rent_order` WRITE;
/*!40000 ALTER TABLE `rent_order` DISABLE KEYS */;
INSERT INTO `rent_order` VALUES (1,1,1,'2026-09-15 09:00:00','2026-09-15 10:00:00',2.00,2),(2,1,2,'2026-09-18 14:30:00','2026-09-18 15:15:00',2.00,2),(3,2,3,'2026-10-01 08:00:00','2026-10-01 08:45:00',2.00,2),(4,1,5,'2026-10-03 20:22:15','2026-10-03 20:22:15',1.00,2);
/*!40000 ALTER TABLE `rent_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名（唯一）',
  `password` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码（BCrypt 加密存储）',
  `name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '昵称',
  `phone` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `balance` decimal(10,2) DEFAULT '0.00' COMMENT '账户余额',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'zhangsan','$2a$10$jKt6ujLcLzhvldpVrCBonegcrxy3K8sJItL/RNHs3rdqNWrv.XXL2','张三','13800138001',129.00,'2026-10-03 20:21:06'),(2,'lisi','$2a$10$5Sd4wLo.XROiAo/WA.Z2aOt6oriEt7rUErh45V/i4/fDMsx9Nsjau','李四','13800138002',50.00,'2026-10-03 20:21:06'),(3,'testuser','$2a$10$xd0WDJyfGXeZCDBiXdNQYuWus42hLDX3peniagmw5xjrCLqpXXpaq','测试用户','13900139000',0.00,'2026-10-03 20:21:06');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-10-03 20:23:22
