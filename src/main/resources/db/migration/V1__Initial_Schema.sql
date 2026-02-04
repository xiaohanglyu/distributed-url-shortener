-- Create short link storage table
-- Optimized with utf8mb4_bin for case-sensitive short codes
CREATE TABLE `t_short_link` (
  `id` BIGINT NOT NULL COMMENT 'Distributed unique ID',
  `short_code` VARCHAR(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'Unique short code',
  `long_url` VARCHAR(2048) NOT NULL COMMENT 'Original long URL',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation time',
  `expire_time` DATETIME DEFAULT NULL COMMENT 'Expiration time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique_short_code` (`short_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- Create distributed ID generator segment table
CREATE TABLE `t_id_generator` (
  `biz_type` VARCHAR(32) NOT NULL COMMENT 'Business type identifier',
  `max_id` BIGINT NOT NULL DEFAULT '1' COMMENT 'Last allocated ID',
  `step` INT NOT NULL DEFAULT '1000' COMMENT 'Segment size per allocation',
  PRIMARY KEY (`biz_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Initialize the segment record for short link business
INSERT INTO `t_id_generator` (`biz_type`, `max_id`, `step`) VALUES ('short_link', 1, 1000);