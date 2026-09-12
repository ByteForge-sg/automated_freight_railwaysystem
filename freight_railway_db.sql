-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: freight_railway_db
-- ------------------------------------------------------
-- Server version	8.0.46

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
-- Table structure for table `car_cargo`
--

DROP TABLE IF EXISTS `car_cargo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `car_cargo` (
  `Wagon_ID` int NOT NULL,
  `Cargo_ID` int NOT NULL,
  `Load_Date` date NOT NULL,
  PRIMARY KEY (`Wagon_ID`,`Cargo_ID`),
  KEY `Cargo_ID` (`Cargo_ID`),
  CONSTRAINT `car_cargo_ibfk_1` FOREIGN KEY (`Wagon_ID`) REFERENCES `freight_car` (`Wagon_ID`),
  CONSTRAINT `car_cargo_ibfk_2` FOREIGN KEY (`Cargo_ID`) REFERENCES `cargo` (`Cargo_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `car_cargo`
--

LOCK TABLES `car_cargo` WRITE;
/*!40000 ALTER TABLE `car_cargo` DISABLE KEYS */;
INSERT INTO `car_cargo` VALUES (401,501,'2026-09-10'),(402,503,'2026-09-10'),(403,502,'2026-09-10'),(404,504,'2026-09-10'),(405,505,'2026-09-11'),(406,506,'2026-09-11'),(407,507,'2026-09-11'),(408,508,'2026-09-11'),(409,509,'2026-09-11'),(410,510,'2026-09-11'),(411,511,'2026-09-12'),(412,512,'2026-09-12'),(413,513,'2026-09-12'),(414,514,'2026-09-12'),(415,515,'2026-09-12'),(416,501,'2026-09-12'),(417,502,'2026-09-12'),(418,503,'2026-09-12'),(419,504,'2026-09-12'),(420,505,'2026-09-12');
/*!40000 ALTER TABLE `car_cargo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cargo`
--

DROP TABLE IF EXISTS `cargo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cargo` (
  `Cargo_ID` int NOT NULL,
  `Description` varchar(100) NOT NULL,
  `Weight_Tons` decimal(8,2) NOT NULL,
  `Hazard_Class` varchar(30) NOT NULL,
  PRIMARY KEY (`Cargo_ID`),
  KEY `idx_cargo_hazard` (`Hazard_Class`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cargo`
--

LOCK TABLES `cargo` WRITE;
/*!40000 ALTER TABLE `cargo` DISABLE KEYS */;
INSERT INTO `cargo` VALUES (501,'Steel Coils',55.00,'NON-HAZARDOUS'),(502,'Cement',60.00,'NON-HAZARDOUS'),(503,'Diesel Fuel',52.00,'HAZARDOUS-3'),(504,'Industrial Chemicals',48.00,'HAZARDOUS-8'),(505,'Coal',65.00,'NON-HAZARDOUS'),(506,'Petroleum Products',50.00,'HAZARDOUS-3'),(507,'Fertilizer',58.00,'HAZARDOUS-5'),(508,'Automobile Parts',42.00,'NON-HAZARDOUS'),(509,'Iron Ore',68.00,'NON-HAZARDOUS'),(510,'LPG Cylinders',45.00,'HAZARDOUS-2'),(511,'Food Grains',40.00,'NON-HAZARDOUS'),(512,'Machine Components',35.00,'NON-HAZARDOUS'),(513,'Paint Chemicals',47.00,'HAZARDOUS-3'),(514,'Copper Sheets',38.00,'NON-HAZARDOUS'),(515,'Paper Rolls',44.00,'NON-HAZARDOUS');
/*!40000 ALTER TABLE `cargo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crew`
--

DROP TABLE IF EXISTS `crew`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `crew` (
  `Crew_ID` int NOT NULL,
  `Name` varchar(100) NOT NULL,
  `Role` varchar(30) NOT NULL,
  `License_No` varchar(50) NOT NULL,
  PRIMARY KEY (`Crew_ID`),
  UNIQUE KEY `License_No` (`License_No`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crew`
--

LOCK TABLES `crew` WRITE;
/*!40000 ALTER TABLE `crew` DISABLE KEYS */;
INSERT INTO `crew` VALUES (601,'Arun Kumar','LOCO_PILOT','LIC1001'),(602,'Vijay Sharma','LOCO_PILOT','LIC1002'),(603,'Rahul Singh','ASSISTANT','LIC1003'),(604,'Karthik Rao','GUARD','LIC1004'),(605,'Suresh Babu','LOCO_PILOT','LIC1005'),(606,'Amit Verma','ASSISTANT','LIC1006'),(607,'Manoj Das','GUARD','LIC1007'),(608,'Prakash Nair','LOCO_PILOT','LIC1008'),(609,'Rohit Mehta','ASSISTANT','LIC1009'),(610,'Deepak Jain','GUARD','LIC1010'),(611,'Naveen Kumar','LOCO_PILOT','LIC1011'),(612,'Sanjay Rao','ASSISTANT','LIC1012');
/*!40000 ALTER TABLE `crew` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crew_assignment`
--

DROP TABLE IF EXISTS `crew_assignment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `crew_assignment` (
  `Crew_ID` int NOT NULL,
  `Train_ID` int NOT NULL,
  `Role_On_Duty` varchar(30) NOT NULL,
  PRIMARY KEY (`Crew_ID`,`Train_ID`),
  KEY `Train_ID` (`Train_ID`),
  CONSTRAINT `crew_assignment_ibfk_1` FOREIGN KEY (`Crew_ID`) REFERENCES `crew` (`Crew_ID`),
  CONSTRAINT `crew_assignment_ibfk_2` FOREIGN KEY (`Train_ID`) REFERENCES `train` (`Train_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crew_assignment`
--

LOCK TABLES `crew_assignment` WRITE;
/*!40000 ALTER TABLE `crew_assignment` DISABLE KEYS */;
INSERT INTO `crew_assignment` VALUES (601,701,'LOCO_PILOT'),(601,707,'LOCO_PILOT'),(602,702,'LOCO_PILOT'),(602,706,'LOCO_PILOT'),(603,701,'ASSISTANT'),(603,704,'ASSISTANT'),(604,701,'GUARD'),(604,704,'GUARD'),(605,703,'LOCO_PILOT'),(605,708,'LOCO_PILOT'),(606,702,'ASSISTANT'),(606,705,'ASSISTANT'),(607,702,'GUARD'),(607,705,'GUARD'),(608,704,'LOCO_PILOT'),(609,703,'ASSISTANT'),(609,706,'ASSISTANT'),(610,703,'GUARD'),(610,707,'GUARD'),(611,705,'LOCO_PILOT'),(612,708,'ASSISTANT');
/*!40000 ALTER TABLE `crew_assignment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `freight_car`
--

DROP TABLE IF EXISTS `freight_car`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `freight_car` (
  `Wagon_ID` int NOT NULL,
  `Wagon_Type` varchar(30) NOT NULL,
  `Capacity_Tons` decimal(8,2) NOT NULL,
  `Current_Status` varchar(20) NOT NULL,
  PRIMARY KEY (`Wagon_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `freight_car`
--

LOCK TABLES `freight_car` WRITE;
/*!40000 ALTER TABLE `freight_car` DISABLE KEYS */;
INSERT INTO `freight_car` VALUES (401,'BOXN',70.00,'LOADED'),(402,'BOXN',70.00,'LOADED'),(403,'BCN',68.00,'AVAILABLE'),(404,'BCN',68.00,'LOADED'),(405,'BTPN',60.00,'LOADED'),(406,'BTPN',60.00,'AVAILABLE'),(407,'BRN',70.00,'LOADED'),(408,'BRN',70.00,'AVAILABLE'),(409,'BOXN',70.00,'LOADED'),(410,'BOXN',70.00,'AVAILABLE'),(411,'BCN',68.00,'LOADED'),(412,'BCN',68.00,'AVAILABLE'),(413,'BTPN',60.00,'MAINTENANCE'),(414,'BRN',70.00,'LOADED'),(415,'BOXN',70.00,'AVAILABLE'),(416,'BOXN',70.00,'LOADED'),(417,'BCN',68.00,'AVAILABLE'),(418,'BTPN',60.00,'LOADED'),(419,'BRN',70.00,'AVAILABLE'),(420,'BOXN',70.00,'LOADED');
/*!40000 ALTER TABLE `freight_car` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `hazardous_cargo`
--

DROP TABLE IF EXISTS `hazardous_cargo`;
/*!50001 DROP VIEW IF EXISTS `hazardous_cargo`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `hazardous_cargo` AS SELECT 
 1 AS `Cargo_ID`,
 1 AS `Description`,
 1 AS `Weight_Tons`,
 1 AS `Hazard_Class`,
 1 AS `Wagon_ID`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `locomotive`
--

DROP TABLE IF EXISTS `locomotive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `locomotive` (
  `Loco_ID` int NOT NULL,
  `Model` varchar(50) NOT NULL,
  `Power_Rating_HP` int NOT NULL,
  `Status` varchar(20) NOT NULL,
  PRIMARY KEY (`Loco_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locomotive`
--

LOCK TABLES `locomotive` WRITE;
/*!40000 ALTER TABLE `locomotive` DISABLE KEYS */;
INSERT INTO `locomotive` VALUES (301,'WAP-7',6350,'AVAILABLE'),(302,'WAP-7',6350,'ASSIGNED'),(303,'WAG-9',6120,'AVAILABLE'),(304,'WAG-9',6120,'MAINTENANCE'),(305,'WAG-12',12000,'AVAILABLE'),(306,'WAG-12',12000,'ASSIGNED'),(307,'WAG-9',6120,'AVAILABLE'),(308,'WAP-7',6350,'AVAILABLE'),(309,'WAG-12',12000,'AVAILABLE'),(310,'WAG-9',6120,'AVAILABLE');
/*!40000 ALTER TABLE `locomotive` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `maintenance_record`
--

DROP TABLE IF EXISTS `maintenance_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `maintenance_record` (
  `Maint_ID` int NOT NULL,
  `Asset_Type` varchar(30) NOT NULL,
  `Asset_ID` int NOT NULL,
  `Maint_Date` date NOT NULL,
  `Cost` decimal(10,2) NOT NULL,
  `Status` varchar(30) NOT NULL,
  PRIMARY KEY (`Maint_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `maintenance_record`
--

LOCK TABLES `maintenance_record` WRITE;
/*!40000 ALTER TABLE `maintenance_record` DISABLE KEYS */;
INSERT INTO `maintenance_record` VALUES (1001,'LOCOMOTIVE',304,'2026-09-10',85000.00,'IN_PROGRESS'),(1002,'FREIGHT_CAR',413,'2026-09-11',22000.00,'IN_PROGRESS'),(1003,'LOCOMOTIVE',301,'2026-08-20',45000.00,'COMPLETED'),(1004,'FREIGHT_CAR',402,'2026-08-25',12000.00,'COMPLETED'),(1005,'LOCOMOTIVE',306,'2026-09-01',30000.00,'COMPLETED'),(1006,'FREIGHT_CAR',409,'2026-09-05',15000.00,'COMPLETED');
/*!40000 ALTER TABLE `maintenance_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manifest`
--

DROP TABLE IF EXISTS `manifest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `manifest` (
  `Manifest_ID` int NOT NULL,
  `Train_ID` int NOT NULL,
  `Origin_Station_ID` int NOT NULL,
  `Dest_Station_ID` int NOT NULL,
  `Departure_Time` datetime NOT NULL,
  `Arrival_Time` datetime NOT NULL,
  PRIMARY KEY (`Manifest_ID`),
  UNIQUE KEY `Train_ID` (`Train_ID`),
  KEY `Origin_Station_ID` (`Origin_Station_ID`),
  KEY `Dest_Station_ID` (`Dest_Station_ID`),
  CONSTRAINT `manifest_ibfk_1` FOREIGN KEY (`Train_ID`) REFERENCES `train` (`Train_ID`),
  CONSTRAINT `manifest_ibfk_2` FOREIGN KEY (`Origin_Station_ID`) REFERENCES `station` (`Station_ID`),
  CONSTRAINT `manifest_ibfk_3` FOREIGN KEY (`Dest_Station_ID`) REFERENCES `station` (`Station_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manifest`
--

LOCK TABLES `manifest` WRITE;
/*!40000 ALTER TABLE `manifest` DISABLE KEYS */;
INSERT INTO `manifest` VALUES (801,701,1,5,'2026-09-12 06:00:00','2026-09-12 14:00:00'),(802,702,3,8,'2026-09-12 07:30:00','2026-09-12 13:30:00'),(803,703,4,6,'2026-09-12 08:00:00','2026-09-12 12:00:00'),(804,704,1,7,'2026-09-12 09:00:00','2026-09-12 17:00:00'),(805,705,5,8,'2026-09-12 05:30:00','2026-09-12 11:30:00'),(806,706,6,9,'2026-09-12 10:00:00','2026-09-12 16:00:00'),(807,707,8,1,'2026-09-12 11:00:00','2026-09-12 20:00:00'),(808,708,3,7,'2026-09-12 12:00:00','2026-09-12 18:00:00');
/*!40000 ALTER TABLE `manifest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `marshalling_yard`
--

DROP TABLE IF EXISTS `marshalling_yard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `marshalling_yard` (
  `Yard_ID` int NOT NULL,
  `Station_ID` int NOT NULL,
  `Num_Tracks` int NOT NULL,
  `Capacity` int NOT NULL,
  PRIMARY KEY (`Yard_ID`),
  UNIQUE KEY `Station_ID` (`Station_ID`),
  CONSTRAINT `marshalling_yard_ibfk_1` FOREIGN KEY (`Station_ID`) REFERENCES `station` (`Station_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `marshalling_yard`
--

LOCK TABLES `marshalling_yard` WRITE;
/*!40000 ALTER TABLE `marshalling_yard` DISABLE KEYS */;
INSERT INTO `marshalling_yard` VALUES (201,1,8,120),(202,3,6,90),(203,4,7,100),(204,6,8,110),(205,8,6,80);
/*!40000 ALTER TABLE `marshalling_yard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule`
--

DROP TABLE IF EXISTS `schedule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `schedule` (
  `Schedule_ID` int NOT NULL,
  `Train_ID` int NOT NULL,
  `Segment_ID` int NOT NULL,
  `Planned_Start` datetime NOT NULL,
  `Planned_End` datetime NOT NULL,
  `Actual_Start` datetime DEFAULT NULL,
  `Actual_End` datetime DEFAULT NULL,
  `Lock_Status` varchar(20) NOT NULL,
  PRIMARY KEY (`Schedule_ID`),
  KEY `idx_schedule_segment` (`Segment_ID`),
  KEY `idx_schedule_train` (`Train_ID`),
  CONSTRAINT `schedule_ibfk_1` FOREIGN KEY (`Train_ID`) REFERENCES `train` (`Train_ID`),
  CONSTRAINT `schedule_ibfk_2` FOREIGN KEY (`Segment_ID`) REFERENCES `track_segment` (`Segment_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule`
--

LOCK TABLES `schedule` WRITE;
/*!40000 ALTER TABLE `schedule` DISABLE KEYS */;
INSERT INTO `schedule` VALUES (901,701,101,'2026-09-12 06:00:00','2026-09-12 07:00:00','2026-09-12 06:05:00','2026-09-12 07:02:00','RELEASED'),(902,701,103,'2026-09-12 07:05:00','2026-09-12 08:15:00','2026-09-12 07:08:00','2026-09-12 08:18:00','RELEASED'),(903,701,105,'2026-09-12 08:20:00','2026-09-12 09:30:00',NULL,NULL,'AVAILABLE'),(904,702,103,'2026-09-12 08:30:00','2026-09-12 09:40:00',NULL,NULL,'AVAILABLE'),(905,702,105,'2026-09-12 09:45:00','2026-09-12 11:00:00',NULL,NULL,'AVAILABLE'),(906,703,105,'2026-09-12 08:00:00','2026-09-12 09:15:00',NULL,NULL,'AVAILABLE'),(907,704,101,'2026-09-12 09:00:00','2026-09-12 10:00:00',NULL,NULL,'AVAILABLE'),(908,704,103,'2026-09-12 10:05:00','2026-09-12 11:15:00',NULL,NULL,'AVAILABLE'),(909,705,108,'2026-09-12 06:00:00','2026-09-12 07:30:00','2026-09-12 06:02:00','2026-09-12 07:25:00','RELEASED'),(910,705,113,'2026-09-12 07:35:00','2026-09-12 09:00:00','2026-09-12 07:40:00','2026-09-12 09:05:00','RELEASED'),(911,706,111,'2026-09-12 10:00:00','2026-09-12 11:00:00',NULL,NULL,'BLOCKED'),(912,707,114,'2026-09-12 11:00:00','2026-09-12 12:30:00',NULL,NULL,'AVAILABLE'),(913,708,103,'2026-09-12 12:00:00','2026-09-12 13:15:00',NULL,NULL,'AVAILABLE'),(914,708,111,'2026-09-12 13:20:00','2026-09-12 14:30:00',NULL,NULL,'AVAILABLE');
/*!40000 ALTER TABLE `schedule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `station`
--

DROP TABLE IF EXISTS `station`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `station` (
  `Station_ID` int NOT NULL,
  `Station_Name` varchar(100) NOT NULL,
  `Location` varchar(100) NOT NULL,
  `Station_Type` varchar(30) NOT NULL,
  PRIMARY KEY (`Station_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `station`
--

LOCK TABLES `station` WRITE;
/*!40000 ALTER TABLE `station` DISABLE KEYS */;
INSERT INTO `station` VALUES (1,'Chennai Central','Chennai','Junction'),(2,'Arakkonam Junction','Arakkonam','Junction'),(3,'Katpadi Junction','Vellore','Junction'),(4,'Jolarpettai Junction','Jolarpettai','Junction'),(5,'Bengaluru City','Bengaluru','Terminal'),(6,'Salem Junction','Salem','Junction'),(7,'Erode Junction','Erode','Junction'),(8,'Coimbatore Junction','Coimbatore','Terminal'),(9,'Tiruchirappalli Junction','Tiruchirappalli','Junction'),(10,'Madurai Junction','Madurai','Junction');
/*!40000 ALTER TABLE `station` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `track_segment`
--

DROP TABLE IF EXISTS `track_segment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `track_segment` (
  `Segment_ID` int NOT NULL,
  `From_Station_ID` int NOT NULL,
  `To_Station_ID` int NOT NULL,
  `Length_KM` decimal(8,2) NOT NULL,
  `Max_Speed` int NOT NULL,
  `Status` varchar(20) NOT NULL,
  PRIMARY KEY (`Segment_ID`),
  KEY `To_Station_ID` (`To_Station_ID`),
  KEY `idx_track_from_to` (`From_Station_ID`,`To_Station_ID`),
  KEY `idx_track_status` (`Status`),
  CONSTRAINT `track_segment_ibfk_1` FOREIGN KEY (`From_Station_ID`) REFERENCES `station` (`Station_ID`),
  CONSTRAINT `track_segment_ibfk_2` FOREIGN KEY (`To_Station_ID`) REFERENCES `station` (`Station_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `track_segment`
--

LOCK TABLES `track_segment` WRITE;
/*!40000 ALTER TABLE `track_segment` DISABLE KEYS */;
INSERT INTO `track_segment` VALUES (101,1,2,68.00,110,'AVAILABLE'),(102,2,1,68.00,110,'AVAILABLE'),(103,2,3,75.00,100,'AVAILABLE'),(104,3,2,75.00,100,'AVAILABLE'),(105,3,4,85.00,110,'AVAILABLE'),(106,4,3,85.00,110,'AVAILABLE'),(107,4,5,145.00,100,'AVAILABLE'),(108,5,4,145.00,100,'AVAILABLE'),(109,4,6,120.00,100,'AVAILABLE'),(110,6,4,120.00,100,'AVAILABLE'),(111,6,7,65.00,110,'AVAILABLE'),(112,7,6,65.00,110,'AVAILABLE'),(113,7,8,100.00,100,'AVAILABLE'),(114,8,7,100.00,100,'AVAILABLE'),(115,6,9,150.00,90,'AVAILABLE');
/*!40000 ALTER TABLE `track_segment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `train`
--

DROP TABLE IF EXISTS `train`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `train` (
  `Train_ID` int NOT NULL,
  `Train_Number` varchar(20) NOT NULL,
  `Train_Type` varchar(30) NOT NULL,
  `Current_Status` varchar(30) NOT NULL,
  PRIMARY KEY (`Train_ID`),
  UNIQUE KEY `Train_Number` (`Train_Number`),
  KEY `idx_train_status` (`Current_Status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `train`
--

LOCK TABLES `train` WRITE;
/*!40000 ALTER TABLE `train` DISABLE KEYS */;
INSERT INTO `train` VALUES (701,'FR1001','FREIGHT','RUNNING'),(702,'FR1002','FREIGHT','SCHEDULED'),(703,'FR1003','FREIGHT','AT_YARD'),(704,'FR1004','FREIGHT','SCHEDULED'),(705,'FR1005','EXPRESS_FREIGHT','RUNNING'),(706,'FR1006','FREIGHT','MAINTENANCE'),(707,'FR1007','FREIGHT','AT_YARD'),(708,'FR1008','EXPRESS_FREIGHT','SCHEDULED');
/*!40000 ALTER TABLE `train` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `train_composition`
--

DROP TABLE IF EXISTS `train_composition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `train_composition` (
  `Train_ID` int NOT NULL,
  `Wagon_ID` int NOT NULL,
  `Sequence_No` int NOT NULL,
  PRIMARY KEY (`Train_ID`,`Wagon_ID`),
  KEY `Wagon_ID` (`Wagon_ID`),
  CONSTRAINT `train_composition_ibfk_1` FOREIGN KEY (`Train_ID`) REFERENCES `train` (`Train_ID`),
  CONSTRAINT `train_composition_ibfk_2` FOREIGN KEY (`Wagon_ID`) REFERENCES `freight_car` (`Wagon_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `train_composition`
--

LOCK TABLES `train_composition` WRITE;
/*!40000 ALTER TABLE `train_composition` DISABLE KEYS */;
INSERT INTO `train_composition` VALUES (701,401,1),(701,402,2),(701,403,3),(701,404,4),(702,405,1),(702,406,2),(702,407,3),(703,408,1),(703,409,2),(703,410,3),(704,411,1),(704,412,2),(704,413,3),(705,414,1),(705,415,2),(705,416,3),(706,417,1),(706,418,2),(707,419,1),(707,420,2);
/*!40000 ALTER TABLE `train_composition` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `train_composition_view`
--

DROP TABLE IF EXISTS `train_composition_view`;
/*!50001 DROP VIEW IF EXISTS `train_composition_view`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `train_composition_view` AS SELECT 
 1 AS `Train_Number`,
 1 AS `Sequence_No`,
 1 AS `Wagon_ID`,
 1 AS `Wagon_Type`,
 1 AS `Capacity_Tons`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `train_locomotive`
--

DROP TABLE IF EXISTS `train_locomotive`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `train_locomotive` (
  `Train_ID` int NOT NULL,
  `Loco_ID` int NOT NULL,
  `Assignment_Date` date NOT NULL,
  PRIMARY KEY (`Train_ID`,`Loco_ID`),
  KEY `Loco_ID` (`Loco_ID`),
  CONSTRAINT `train_locomotive_ibfk_1` FOREIGN KEY (`Train_ID`) REFERENCES `train` (`Train_ID`),
  CONSTRAINT `train_locomotive_ibfk_2` FOREIGN KEY (`Loco_ID`) REFERENCES `locomotive` (`Loco_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `train_locomotive`
--

LOCK TABLES `train_locomotive` WRITE;
/*!40000 ALTER TABLE `train_locomotive` DISABLE KEYS */;
INSERT INTO `train_locomotive` VALUES (701,301,'2026-09-12'),(702,303,'2026-09-12'),(703,305,'2026-09-12'),(704,307,'2026-09-12'),(705,302,'2026-09-12'),(706,304,'2026-09-12'),(707,308,'2026-09-12'),(708,309,'2026-09-12');
/*!40000 ALTER TABLE `train_locomotive` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `train_overview`
--

DROP TABLE IF EXISTS `train_overview`;
/*!50001 DROP VIEW IF EXISTS `train_overview`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `train_overview` AS SELECT 
 1 AS `Train_ID`,
 1 AS `Train_Number`,
 1 AS `Train_Type`,
 1 AS `Current_Status`,
 1 AS `Origin_Station`,
 1 AS `Destination_Station`,
 1 AS `Departure_Time`,
 1 AS `Arrival_Time`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `transaction_log`
--

DROP TABLE IF EXISTS `transaction_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transaction_log` (
  `Log_ID` int NOT NULL AUTO_INCREMENT,
  `Transaction_ID` varchar(50) NOT NULL,
  `Operation` varchar(20) NOT NULL,
  `Table_Name` varchar(50) NOT NULL,
  `Record_ID` varchar(50) NOT NULL,
  `Old_Value` varchar(500) DEFAULT NULL,
  `New_Value` varchar(500) DEFAULT NULL,
  `Log_Time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `Status` varchar(20) NOT NULL,
  PRIMARY KEY (`Log_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transaction_log`
--

LOCK TABLES `transaction_log` WRITE;
/*!40000 ALTER TABLE `transaction_log` DISABLE KEYS */;
INSERT INTO `transaction_log` VALUES (1,'TXN001','UPDATE','TRACK_SEGMENT','103','AVAILABLE','OCCUPIED','2026-09-12 14:52:11','COMMITTED'),(2,'TXN001','UPDATE','TRACK_SEGMENT','103','AVAILABLE','OCCUPIED','2026-09-12 14:52:28','STARTED'),(3,'TXN001','UPDATE','TRACK_SEGMENT','103','AVAILABLE','OCCUPIED','2026-09-12 14:53:04','STARTED'),(5,'TXN002','UPDATE','TRACK_SEGMENT','103','AVAILABLE','OCCUPIED','2026-09-12 15:00:51','ROLLED_BACK'),(6,'JAVA_TEST_001','LOCK','TRACK_SEGMENT','103','AVAILABLE','LOCKED','2026-09-12 19:40:29','STARTED'),(7,'TXN-D9F85BC2','LOCK','TRACK_SEGMENT','MULTIPLE','AVAILABLE','LOCKED','2026-09-12 20:16:17','STARTED'),(8,'TXN-D9F85BC2','COMMIT','TRACK_SEGMENT','MULTIPLE','LOCKED','OCCUPIED','2026-09-12 20:17:28','COMMITTED'),(9,'TXN-DA665243','LOCK','TRACK_SEGMENT','MULTIPLE','AVAILABLE','LOCKED','2026-09-12 20:18:26','STARTED'),(10,'TXN-DA665243','ROLLBACK','TRACK_SEGMENT','MULTIPLE','LOCKED','AVAILABLE','2026-09-12 20:18:38','ROLLED_BACK'),(11,'TXN-EE12D33E','LOCK','TRACK_SEGMENT','MULTIPLE','AVAILABLE','LOCKED','2026-09-12 21:29:13','STARTED'),(12,'TXN-EE12D33E','COMMIT','TRACK_SEGMENT','MULTIPLE','LOCKED','OCCUPIED','2026-09-12 21:29:28','COMMITTED'),(13,'TXN-009B3B3B','LOCK','TRACK_SEGMENT','MULTIPLE','AVAILABLE','LOCKED','2026-09-12 21:29:57','STARTED'),(14,'TXN-009B3B3B','ROLLBACK','TRACK_SEGMENT','MULTIPLE','LOCKED','AVAILABLE','2026-09-12 21:30:09','ROLLED_BACK');
/*!40000 ALTER TABLE `transaction_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `hazardous_cargo`
--

/*!50001 DROP VIEW IF EXISTS `hazardous_cargo`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `hazardous_cargo` AS select `c`.`Cargo_ID` AS `Cargo_ID`,`c`.`Description` AS `Description`,`c`.`Weight_Tons` AS `Weight_Tons`,`c`.`Hazard_Class` AS `Hazard_Class`,`cc`.`Wagon_ID` AS `Wagon_ID` from (`cargo` `c` join `car_cargo` `cc` on((`c`.`Cargo_ID` = `cc`.`Cargo_ID`))) where (`c`.`Hazard_Class` like 'HAZARDOUS%') */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `train_composition_view`
--

/*!50001 DROP VIEW IF EXISTS `train_composition_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `train_composition_view` AS select `t`.`Train_Number` AS `Train_Number`,`tc`.`Sequence_No` AS `Sequence_No`,`fc`.`Wagon_ID` AS `Wagon_ID`,`fc`.`Wagon_Type` AS `Wagon_Type`,`fc`.`Capacity_Tons` AS `Capacity_Tons` from ((`train` `t` join `train_composition` `tc` on((`t`.`Train_ID` = `tc`.`Train_ID`))) join `freight_car` `fc` on((`tc`.`Wagon_ID` = `fc`.`Wagon_ID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `train_overview`
--

/*!50001 DROP VIEW IF EXISTS `train_overview`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `train_overview` AS select `t`.`Train_ID` AS `Train_ID`,`t`.`Train_Number` AS `Train_Number`,`t`.`Train_Type` AS `Train_Type`,`t`.`Current_Status` AS `Current_Status`,`s1`.`Station_Name` AS `Origin_Station`,`s2`.`Station_Name` AS `Destination_Station`,`m`.`Departure_Time` AS `Departure_Time`,`m`.`Arrival_Time` AS `Arrival_Time` from (((`train` `t` join `manifest` `m` on((`t`.`Train_ID` = `m`.`Train_ID`))) join `station` `s1` on((`m`.`Origin_Station_ID` = `s1`.`Station_ID`))) join `station` `s2` on((`m`.`Dest_Station_ID` = `s2`.`Station_ID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-09-12 23:23:26
