-- phpMyAdmin SQL Dump
-- version 4.1.7
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Ott 09, 2017 alle 22:03
-- Versione del server: 5.6.33-log
-- PHP Version: 5.3.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `my_vakcinoapp`
--

-- --------------------------------------------------------

--
-- Struttura della tabella `ACCOUNT`
--

CREATE TABLE IF NOT EXISTS `ACCOUNT` (
  `email` varchar(50) NOT NULL,
  `pw` varchar(50) NOT NULL,
  PRIMARY KEY (`email`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `ACCOUNT`
--

INSERT INTO `ACCOUNT` (`email`, `pw`) VALUES
('prova@prova.it', 'provaprova'),
('ermail@fmail.it', 'ciao123A'),
('ciao@ciao.it', 'Prova1'),
('ciaso@ciao.it', 'Prova1');

-- --------------------------------------------------------

--
-- Struttura della tabella `DEVE_FARE`
--

CREATE TABLE IF NOT EXISTS `DEVE_FARE` (
  `ID` int(11) NOT NULL,
  `ID_VAC` int(11) NOT NULL,
  PRIMARY KEY (`ID`,`ID_VAC`),
  KEY `FKDEV_VAC_FK` (`ID_VAC`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `HA_FATTO`
--

CREATE TABLE IF NOT EXISTS `HA_FATTO` (
  `ID` int(11) NOT NULL,
  `ID_VAC` int(11) NOT NULL,
  `in_data` date NOT NULL,
  PRIMARY KEY (`ID`,`ID_VAC`),
  UNIQUE KEY `ID_HA_FATTO_IND` (`ID`,`ID_VAC`),
  KEY `FKHA__VAC_IND` (`ID_VAC`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Struttura della tabella `SYNC`
--

CREATE TABLE IF NOT EXISTS `SYNC` (
  `email` varchar(50) NOT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`email`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `SYNC`
--

INSERT INTO `SYNC` (`email`, `version`) VALUES
('prova@prova.it', 0);

-- --------------------------------------------------------

--
-- Struttura della tabella `TIPO_VACCINAZIONE`
--

CREATE TABLE IF NOT EXISTS `TIPO_VACCINAZIONE` (
  `ID` int(11) NOT NULL,
  `Da` int(11) NOT NULL,
  `A` int(11) NOT NULL,
  `TipoImmunizzazione` varchar(50) NOT NULL,
  `NumRichiamo` int(11) NOT NULL,
  `Antigene` varchar(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `TIPO_VACCINAZIONE`
--

INSERT INTO `TIPO_VACCINAZIONE` (`ID`, `Da`, `A`, `TipoImmunizzazione`, `NumRichiamo`, `Antigene`) VALUES
(1, 3, 4, 'primaria', 1, 'Dtpa1'),
(2, 3, 4, 'primaria', 1, 'dTpa2'),
(3, 3, 4, 'primaria', 1, 'dtPa3'),
(4, 5, 6, 'primaria', 2, 'Dtpa1'),
(5, 5, 6, 'primaria', 2, 'dTpa2'),
(6, 5, 6, 'primaria', 2, 'dtPa3'),
(7, 11, 13, 'primaria', 3, 'Dtpa1'),
(8, 11, 13, 'primaria', 3, 'dTpa2'),
(9, 11, 13, 'primaria', 3, 'dtPa3'),
(10, 3, 4, 'primaria', 1, 'IPV'),
(11, 5, 6, 'primaria', 2, 'IPV'),
(12, 11, 12, 'primaria', 3, 'IPV'),
(13, 48, 49, 'richiamo', 4, 'IPV'),
(14, 3, 4, 'primaria', 1, 'EpB'),
(15, 5, 6, 'primaria', 2, 'EpB'),
(16, 11, 13, 'primaria', 3, 'EpB'),
(17, 3, 4, 'primaria', 1, 'Hib'),
(18, 5, 6, 'primaria', 2, 'Hib'),
(19, 11, 13, 'primaria', 3, 'Hib'),
(20, 13, 15, 'primaria', 1, 'M'),
(21, 48, 72, 'richiamo', 2, 'M'),
(22, 13, 15, 'primaria', 1, 'P'),
(23, 48, 72, 'richiamo', 2, 'P'),
(24, 13, 15, 'primaria', 1, 'R'),
(25, 48, 72, 'richiamo', 2, 'R'),
(26, 13, 15, 'primaria', 1, 'V'),
(27, 48, 72, 'richiamo', 2, 'V'),
(28, 3, 4, 'primaria', 1, 'PCV'),
(29, 5, 6, 'primaria', 2, 'PCV'),
(30, 11, 13, 'primaria', 3, 'PCV'),
(31, 12, 13, 'primaria', 1, 'MenC'),
(32, 0, 0, 'primaria', 1, 'MenB'),
(33, 1, 2, 'primaria', 2, 'MenB'),
(34, 3, 4, 'primaria', 3, 'MenB'),
(35, 108, 168, 'primaria', 1, 'HPV'),
(36, 109, 168, 'primaria', 2, 'HPV'),
(38, 720, 723, 'primaria', 1, 'VZV'),
(37, 114, 168, 'primaria', 3, 'HPV'),
(39, 5, 6, 'primaria', 1, 'RV'),
(40, 6, 7, 'primaria', 2, 'RV'),
(41, 7, 8, 'primaria', 3, 'RV');

-- --------------------------------------------------------

--
-- Struttura della tabella `UTENTE`
--

CREATE TABLE IF NOT EXISTS `UTENTE` (
  `ID` int(11) NOT NULL,
  `nome` varchar(30) NOT NULL,
  `cognome` varchar(30) NOT NULL,
  `dataNascita` date NOT NULL,
  `tipo` varchar(1) NOT NULL,
  `email` varchar(50) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UTENTE_IND` (`ID`),
  KEY `FKPOSSIEDE_IND` (`email`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `UTENTE`
--

INSERT INTO `UTENTE` (`ID`, `nome`, `cognome`, `dataNascita`, `tipo`, `email`) VALUES
(1, 'Lorenzo', 'Chiana', '1995-07-29', 'p', 'prova@prova.it'),
(2, 'Fufi', 'Chiana', '2017-09-03', 'a', 'prova@prova.it');

-- --------------------------------------------------------

--
-- Struttura della tabella `VACCINAZIONE`
--

CREATE TABLE IF NOT EXISTS `VACCINAZIONE` (
  `Antigene` varchar(20) NOT NULL,
  `Nome` varchar(50) NOT NULL,
  `Descrizione` varchar(255) NOT NULL,
  `GRUPPO` varchar(20) NOT NULL,
  PRIMARY KEY (`Antigene`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dump dei dati per la tabella `VACCINAZIONE`
--

INSERT INTO `VACCINAZIONE` (`Antigene`, `Nome`, `Descrizione`, `GRUPPO`) VALUES
('dtPa3', 'Pertosse', 'Vaccinazione contro pertosse', 'ESAVALENTE'),
('EpB', 'Epatite B', 'Vaccino contro il virus dell’epatite B ', 'ESAVALENTE'),
('Hib', 'Haemophilus influenzae B', 'Vaccino contro Haemophilus influenzae tipo B', 'ESAVALENTE'),
('Dtpa1', 'Difterite', 'Vaccinazione contro difterite', 'ESAVALENTE'),
('dTpa2', 'Tetano', 'Vaccinazione contro tetano', 'ESAVALENTE'),
('IPV', 'Poliomelite', 'Vaccinazione contro la poliomielite', 'ESAVALENTE'),
('PCV', 'Pneumococco', 'Vaccino pneumococcico coniugato', 'PNEUMOCOCCO'),
('RV', 'Rotavirus', 'Vaccino contro i rotavirus', 'ROTAVIRUS'),
('MenB', 'Meningococco B', 'Vaccino contro il meningococco B', 'MENINGOCOCCO_B'),
('Influenza', 'Influenza', 'Vaccinazione contro l’influenza stagionale', 'INFLUENZA'),
('M', 'Morbillo', 'Vaccino per morbillo', 'TETRAVALENTE'),
('P', 'Parotite', 'Vaccino per parotite', 'TETRAVALENTE'),
('R', 'Rosolia', 'Vaccino per rosolia', 'TETRAVALENTE'),
('V', 'Varicella', 'Vaccino contro la varicella', 'VARICELLA'),
('MenC', 'Meningococco C', 'Vaccino contro il meningococco C coniugato o ACWY', 'ACWY'),
('HPV', 'Papilloma Virus', 'Vaccino contro i papilloma virus', 'HPV'),
('EpA', 'Epatite A', 'Vaccino contro il virus dell’epatite A', 'EPATITEA'),
('VZV', 'Herpes Zoster', 'Vaccinazione contro l’Herpes zoster per adulti', 'HERPESZOSTER');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
