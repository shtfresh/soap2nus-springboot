DROP TABLE `t_oracle_tpt`;

CREATE TABLE `t_oracle_tpt` (
  `tptId` varchar(50) NOT NULL,
  `tptTile` varchar(50) DEFAULT NULL,
  `tptType` varchar(50) DEFAULT NULL,
  `tptCategory` varchar(50) DEFAULT NULL,
  `tptDescrition` varchar(50) DEFAULT NULL,
  `publishedAt` varchar(50) DEFAULT NULL,
  `weeks` json DEFAULT NULL,
  PRIMARY KEY (`tptId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE `t_oracle_tp`;

CREATE TABLE `t_oracle_tp` (
  `tpId` varchar(50) NOT NULL,
  `tpOwnerId` varchar(50) DEFAULT NULL,
  `tpPublishedAt` varchar(50) DEFAULT NULL,
  `tpUpdateAt` varchar(50) DEFAULT NULL,
  `tpOwner` varchar(50) DEFAULT NULL,
  `tpStatus` varchar(50) DEFAULT NULL,
  `tpStart` varchar(50) DEFAULT NULL,
  `tpEnd` varchar(50) DEFAULT NULL,
  `tpTargetType` varchar(50) DEFAULT NULL,
  `tpTargetMatchid` varchar(50) DEFAULT NULL,
  `tpTargetMatchName` varchar(50) DEFAULT NULL,
  `tpVersionNo` int(50) DEFAULT NULL,
  `minKilometre` varchar(50) DEFAULT NULL,
  `maxKilometre` varchar(50) DEFAULT NULL,
  `tptId` varchar(50) DEFAULT NULL,
  `tptTile` varchar(50) DEFAULT NULL,
  `tptType` varchar(50) DEFAULT NULL,
  `tptDescrition` varchar(50) DEFAULT NULL,
  `weeks` json DEFAULT NULL,
  PRIMARY KEY (`tpId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE `t_oracle_tptenum`;

CREATE TABLE `t_oracle_tptenum` (
  `tpStatus` json DEFAULT NULL,
  `status` json DEFAULT NULL,
  `task` json DEFAULT NULL,
  `tpTargetType` json DEFAULT NULL,
  `tptType` json DEFAULT NULL,
  `pace_run` json DEFAULT NULL,
  `pace_walk` json DEFAULT NULL,
  `pace_easy` json DEFAULT NULL,
  `pace_brisk` json DEFAULT NULL,
  `pace_jog` json DEFAULT NULL,
  `pace_fast` json DEFAULT NULL,
  `pace_recovery` json DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `readygo`.`t_oracle_tptenum`
(`tpStatus`,
`status`,
`task`,
`tpTargetType`,
`tptType`,
`pace_run`,
`pace_walk`,
`pace_easy`,
`pace_brisk`,
`pace_jog`,
`pace_fast`,
`pace_recovery`)
VALUES
('["active","inActive"]',
'["planned","deferred","completed","inCompleted","cancelled","rest"]',
'["run","walk","easy","brisk","jog","fast","recovery","kilometre"]',
'["match","personal"]',
'["beginner","slim","professional","5k","10k","half-marathon","marathon"]',
'{"min":"600","max":"400"}',
'{"min":"1200","max":"720"}',  
'{"min":"400","max":"327"}', 
'{"min":"300","max":"257"}', 
'{"min":"729","max":"514"}',  
'{"min":"257","max":"225"}',  
'{"min":"1800","max":"900"}' 
);