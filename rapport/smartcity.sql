-- Base de données :  `smartcity`

-- --------------------------------------------------------

-- Structure de la table `bus_schedule`
CREATE TABLE `bus_schedule` (
  `id` int(11) NOT NULL,
  `day` varchar(255) NOT NULL,
  `opening_time` time NOT NULL,
  `closing_time` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

-- Structure de la table `properties`
CREATE TABLE `properties` (
  `id` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

-- Structure de la table `rfid_subscription`
CREATE TABLE `rfid_subscription` (
  `id` int(11) NOT NULL,
  `first_name` varchar(128) NOT NULL,
  `last_name` varchar(128) NOT NULL,
  `tag` varchar(32) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

-- Structure de la table `rfid_tag`
CREATE TABLE `rfid_tag` (
  `id` int(11) NOT NULL,
  `name` varchar(128) NOT NULL,
  `tag` varchar(32) NOT NULL,
  `entry` double NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

-- Structure de la table `sensors`
CREATE TABLE `sensors` (
  `id` int(11) NOT NULL,
  `name` varchar(128) DEFAULT NULL,
  `value` double DEFAULT NULL,
  `gross_value` double NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

-- Structure de la table `zones`
CREATE TABLE `zones` (
  `id` int(11) NOT NULL,
  `name` varchar(128) NOT NULL,
  `name_full` varchar(255) NOT NULL,
  `opened` tinyint(1) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------


ALTER TABLE `bus_schedule`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `properties`
  ADD PRIMARY KEY (`id`);

ALTER TABLE `rfid_subscription`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_index_id_first_name` (`id`,`first_name`);

ALTER TABLE `rfid_tag`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_index_id_name` (`id`,`name`);

ALTER TABLE `sensors`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_index_id_name` (`id`,`name`);

ALTER TABLE `zones`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_index_id_name` (`id`,`name`);


ALTER TABLE `bus_schedule`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `rfid_subscription`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `rfid_tag`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `sensors`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `zones`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
