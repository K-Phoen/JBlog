-- phpMyAdmin SQL Dump
-- version 3.2.0.1
-- http://www.phpmyadmin.net
--
-- Serveur: localhost
-- Généré le : Mer 16 Mars 2011 à 11:44
-- Version du serveur: 5.1.36
-- Version de PHP: 5.3.0

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `jblog`
--

-- --------------------------------------------------------

--
-- Structure de la table `articles`
--

CREATE TABLE IF NOT EXISTS `articles` (
  `aID` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `u_ID` mediumint(8) unsigned NOT NULL,
  `c_ID` mediumint(8) unsigned NOT NULL,
  `slug` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `date` date NOT NULL,
  `nb_coms` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `valid` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`aID`),
  UNIQUE KEY `slug` (`slug`),
  KEY `c_ID_idx` (`c_ID`),
  KEY `user_id_idx` (`u_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=5 ;

--
-- Contenu de la table `articles`
--

INSERT INTO `articles` (`aID`, `u_ID`, `c_ID`, `slug`, `title`, `content`, `date`, `nb_coms`, `valid`) VALUES
(1, 1, 1, 'toto', 'Toto', '<p>\r\ncontenu de l''article !\r\n</p>', '2011-02-23', 4, 1),
(2, 1, 2, 'salut', 'Salut', '<p>Salut &ccedil;a gaze ??</p>', '2011-03-02', 8, 1),
(3, 1, 2, 'toto-au-tibet', 'Toto au tibet', '<p>J''aime les saucisses made in 42 (''cause it''s the answer.)</p>', '2011-03-15', 1, 1),
(4, 3, 3, 'cest-bon-ca-', 'C''est Bon ça !!', '<p><span style="text-decoration: underline;"><strong>C''est &ccedil;a que c''est bon !!</strong></span></p>', '2011-03-15', 0, 1);

-- --------------------------------------------------------

--
-- Structure de la table `categories`
--

CREATE TABLE IF NOT EXISTS `categories` (
  `cID` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `slug` varchar(255) NOT NULL,
  `title` varchar(255) NOT NULL,
  PRIMARY KEY (`cID`),
  UNIQUE KEY `slug` (`slug`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Contenu de la table `categories`
--

INSERT INTO `categories` (`cID`, `slug`, `title`) VALUES
(1, 'divers-trucs', 'Divers trucs'),
(2, 'kewl-stuff', 'Kewl stuff'),
(3, 'ricard', 'Ricard');

-- --------------------------------------------------------

--
-- Structure de la table `commentaires`
--

CREATE TABLE IF NOT EXISTS `commentaires` (
  `coID` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `a_ID` mediumint(8) unsigned NOT NULL,
  `pseudo` varchar(255) DEFAULT NULL,
  `mail` varchar(255) DEFAULT NULL,
  `content` text NOT NULL,
  `date` datetime NOT NULL,
  `valide` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`coID`),
  KEY `a_ID_idx` (`a_ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=17 ;

--
-- Contenu de la table `commentaires`
--

INSERT INTO `commentaires` (`coID`, `a_ID`, `pseudo`, `mail`, `content`, `date`, `valide`) VALUES
(1, 1, 'Kévin', 'contact@kevingomez.fr', 'Contenu du commentaire !', '2011-03-01 13:00:31', 1),
(3, 1, 'Kevin', 'contact@kevingomez.fr', 'tutu !', '2011-03-01 16:34:17', 1),
(6, 2, 'Kévin Gomez', 'geek63@gmail.com', 'Toto.', '2011-03-08 11:28:49', 1),
(7, 2, 'Moi', 'moi@toi.fr', 'yoooo', '2011-03-08 11:32:30', 1),
(8, 2, 'Kévin Gomez', 'geek63@gmail.com', 't [édité]', '2011-03-08 00:00:00', 1),
(9, 2, 'Kévin Gomez', 'geek63@gmail.com', 'to[s]t[/s]o', '2011-03-11 12:32:55', 1),
(10, 2, 'Kévin Gomez', 'geek63@gmail.com', ':D', '2011-03-11 18:16:21', 1),
(11, 2, 'Kévin Gomez', 'geek63@gmail.com', ':D:p smileys', '2011-03-11 18:29:58', 1),
(12, 2, 'Kévin Gomez', 'geek63@gmail.com', 'to[b][u]^^itotozk[/u][/b]t$ ert', '2011-03-14 08:46:49', 1),
(13, 2, 'Kévin Gomez', 'geek63@gmail.com', 'eztrert e\r\n[i]rt e ter[/i]<strong>titi</strong>\r\nt\r\nzet', '2011-03-14 00:00:00', 1),
(14, 1, 'Kévin Gomez', 'geek63@gmail.com', '<strong>toto</strong>\r\n\r\net[i]erte[/i]rt  sd[b]fs[/b]fd  erte[u]rtertr[/u]et reter[s]terte[/s]rt \r\n:D:o;)^^:p\r\n', '2011-03-14 22:35:28', 1),
(15, 1, 'Kévin Gomez', 'geek63@gmail.com', '[i]toto[/i]\r\n\r\n[i]tata[/i]', '2011-03-15 10:14:31', 1),
(16, 3, 'Jonathan DA SILVA', 'jonathandasilva@sfr.fr', 'Cool !!!!  :)', '2011-03-15 21:43:14', 1);

-- --------------------------------------------------------

--
-- Structure de la table `smileys`
--

CREATE TABLE IF NOT EXISTS `smileys` (
  `sID` mediumint(8) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) COLLATE utf8_bin NOT NULL,
  `img` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`sID`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_bin AUTO_INCREMENT=9 ;

--
-- Contenu de la table `smileys`
--

INSERT INTO `smileys` (`sID`, `code`, `img`) VALUES
(1, 'xD', 'emoticon_evilgrin.png'),
(2, ':)', 'emoticon_smile.png'),
(3, ':(', 'emoticon_unhappy.png'),
(4, ':D', 'emoticon_grin.png'),
(5, ':o', 'emoticon_surprised.png'),
(6, ';)', 'emoticon_wink.png'),
(7, '^^', 'emoticon_happy.png'),
(8, ':p', 'emoticon_tongue.png');

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `uID` mediumint(8) unsigned NOT NULL AUTO_INCREMENT,
  `login` varchar(255) NOT NULL,
  `pass` varchar(40) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `mail` varchar(255) NOT NULL,
  PRIMARY KEY (`uID`),
  UNIQUE KEY `login` (`login`,`pass`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=4 ;

--
-- Contenu de la table `users`
--

INSERT INTO `users` (`uID`, `login`, `pass`, `last_name`, `first_name`, `mail`) VALUES
(1, 'Kevin', '7de1291b75768fed8150cd53bb7dbd8525efea36', 'Gomez', 'Kévin', 'geek63@gmail.com'),
(2, 'test', 'a94a8fe5ccb19ba61c4c0873d391e987982fbbd3', '', '', 'geek63@gmail.com'),
(3, 'jojo', '13de8889aecf8f48d9c799a1f3fb520fa748372a', 'DA SILVA', 'Jonathan', 'jonathandasilva@sfr.fr');

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `articles`
--
ALTER TABLE `articles`
  ADD CONSTRAINT `articles_ibfk_1` FOREIGN KEY (`u_ID`) REFERENCES `users` (`uID`) ON DELETE CASCADE,
  ADD CONSTRAINT `articles_ibfk_2` FOREIGN KEY (`c_ID`) REFERENCES `categories` (`cID`) ON DELETE CASCADE;

--
-- Contraintes pour la table `commentaires`
--
ALTER TABLE `commentaires`
  ADD CONSTRAINT `commentaires_ibfk_1` FOREIGN KEY (`a_ID`) REFERENCES `articles` (`aID`) ON DELETE CASCADE;
