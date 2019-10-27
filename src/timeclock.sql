-- MySQL dump 10.13  Distrib 5.1.68, for Win64 (unknown)
--
-- Host: localhost    Database: timeclock
-- ------------------------------------------------------
-- Server version	5.1.68-community

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `clockcolor`
--

DROP TABLE IF EXISTS `clockcolor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clockcolor` (
  `type` varchar(255) DEFAULT NULL,
  `red` smallint(6) NOT NULL,
  `green` smallint(6) NOT NULL,
  `blue` smallint(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clockcolor`
--

LOCK TABLES `clockcolor` WRITE;
/*!40000 ALTER TABLE `clockcolor` DISABLE KEYS */;
INSERT INTO `clockcolor` VALUES ('clockpanel',0,255,255),('clockring',102,102,102),('clockcontent',204,204,204),('hourpointer',0,51,51),('minutepointer',0,0,204),('secondpointer',255,0,0),('dialgauge',16,208,18);
/*!40000 ALTER TABLE `clockcolor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `clockscale`
--

DROP TABLE IF EXISTS `clockscale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clockscale` (
  `scale` float NOT NULL DEFAULT '1',
  PRIMARY KEY (`scale`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clockscale`
--

LOCK TABLES `clockscale` WRITE;
/*!40000 ALTER TABLE `clockscale` DISABLE KEYS */;
INSERT INTO `clockscale` VALUES (1.4);
/*!40000 ALTER TABLE `clockscale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ringtable`
--

DROP TABLE IF EXISTS `ringtable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ringtable` (
  `hour` tinyint(4) NOT NULL,
  `minute` tinyint(4) NOT NULL,
  `second` tinyint(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ringtable`
--

LOCK TABLES `ringtable` WRITE;
/*!40000 ALTER TABLE `ringtable` DISABLE KEYS */;
INSERT INTO `ringtable` VALUES (11,40,0),(14,0,0),(17,40,0);
/*!40000 ALTER TABLE `ringtable` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-09-27 10:35:25
