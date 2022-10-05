DROP DATABASE IF EXISTS sms_db;
CREATE DATABASE IF NOT EXISTS `sms_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `sms_db`;

-- --------------------------------------------------------

CREATE TABLE `category` (
  `id` int(5)  NOT NULL PRIMARY KEY AUTO_INCREMENT,
   `code` varchar(20),
  `nom` varchar(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

CREATE TABLE `abonement` (
  `id` int(5) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `nom` varchar(20),
   `code` varchar(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

CREATE TABLE `client` (
  `id` int(5) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `nom` varchar(20),
    `num_tele` varchar(20),
    `Adress` varchar(100),
   `code` varchar(20),
    `Code_abonement` varchar(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

CREATE TABLE `fournisseur` (
  `id` int(5) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `nom` varchar(20),
    `num_tele` varchar(20),
      `code` varchar(20),
    `adress` varchar(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

CREATE TABLE `produit` (
  `id` int(5) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `nom` varchar(20),
     `code_produit` varchar(20),
    `quantit_stock` int(5) ,
    `quantit_mininmal` int(5) ,
    `code_category` varchar(20),
    `code_fourniseur` varchar(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

CREATE TABLE `users` (
    `username` varchar(20) NOT NULL,
    `password` varchar(20) NOT NULL,
    CONSTRAINT PK_table_name PRIMARY KEY (`username`, `password`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;