CREATE TABLE t_oracle_tpt (
  tptId varchar(50) NOT NULL,
  tptTile varchar(50) DEFAULT NULL,
  tptType varchar(50) DEFAULT NULL,
  tptCategory varchar(50) DEFAULT NULL,
  tptDescrition varchar(50) DEFAULT NULL,
  publishedAt varchar(50) DEFAULT NULL,
  weeks varchar2(4000) DEFAULT NULL,
  PRIMARY KEY (tptId)
);

CREATE TABLE t_oracle_tp (
  tpId varchar(50) NOT NULL,
  tpOwnerId varchar(50) DEFAULT NULL,
  tpPublishedAt varchar(50) DEFAULT NULL,
  tpUpdateAt varchar(50) DEFAULT NULL,
  tpOwner varchar(50) DEFAULT NULL,
  tpStatus varchar(50) DEFAULT NULL,
  tpStart varchar(50) DEFAULT NULL,
  tpEnd varchar(50) DEFAULT NULL,
  tpTargetType varchar(50) DEFAULT NULL,
  tpTargetMatchid varchar(50) DEFAULT NULL,
  tpTargetMatchName varchar(50) DEFAULT NULL,
  tpVersionNo varchar(50) DEFAULT NULL,
  minKilometre varchar(50) DEFAULT NULL,
  maxKilometre varchar(50) DEFAULT NULL,
  totalDays varchar(50) DEFAULT 0,
  tptId varchar(50) DEFAULT NULL,
  tptTile varchar(50) DEFAULT NULL,
  tptType varchar(50) DEFAULT NULL,
  tptDescrition varchar(50) DEFAULT NULL,
  weeks varchar2(32767) DEFAULT NULL,
  PRIMARY KEY (tpId)
);

CREATE TABLE t_oracle_tptenum (
  tpStatus varchar2(4000) DEFAULT NULL,
  status varchar2(4000) DEFAULT NULL,
  task varchar2(4000) DEFAULT NULL,
  tpTargetType varchar2(4000) DEFAULT NULL,
  tptType varchar2(4000) DEFAULT NULL,
  pace_run varchar2(4000) DEFAULT NULL,
  pace_walk varchar2(4000) DEFAULT NULL,
  pace_easy varchar2(4000) DEFAULT NULL,
  pace_brisk varchar2(4000) DEFAULT NULL,
  pace_jog varchar2(4000) DEFAULT NULL,
  pace_fast varchar2(4000) DEFAULT NULL,
  pace_recovery varchar2(4000) DEFAULT NULL
);

INSERT INTO t_oracle_tptenum
(tpStatus,
status,
task,
tpTargetType,
tptType,
pace_run,
pace_walk,
pace_easy,
pace_brisk,
pace_jog,
pace_fast,
pace_recovery)
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

CREATE TABLE t_oracle_user (
  userId number NOT NULL,
  userName varchar(50) DEFAULT NULL,
  weight varchar(50) DEFAULT NULL,
  height varchar(50) DEFAULT NULL,
  gender number DEFAULT NULL,
  image varchar(500) DEFAULT NULL,
  PRIMARY KEY (userId)
);

INSERT INTO t_oracle_user
(userId,
userName,
weight,
height,
gender,
image)
VALUES
(100204,
'奔跑吧4505',
'140',
'170',
1,
'https://readygo-dit.fwdtech.cn//20190426/20190426183ab2da2dac0563b69ec62302e2622.png');

CREATE TABLE t_oracle_match (
  matchId varchar(100) NOT NULL,
  matchName varchar(100) DEFAULT NULL,
  matchStartTime varchar(100) DEFAULT NULL,
  matchAddress varchar(4000) DEFAULT NULL,
  matchType varchar(4000) DEFAULT NULL,
  matchDesp varchar(4000) DEFAULT NULL,
  CompetitionRule varchar(4000) DEFAULT NULL,
  EnrollGuide varchar(4000) DEFAULT NULL,
  OtherInfo varchar(4000) DEFAULT NULL,
  Banner varchar(4000) DEFAULT NULL,
  PRIMARY KEY (matchId)
);

INSERT INTO t_oracle_match
(matchId,
matchName,
matchStartTime,
matchAddress,
matchType,
matchDesp,
CompetitionRule,
EnrollGuide,
OtherInfo,
Banner)
VALUES
(10082,
'Absolute Moscow Marathon',
'2019-09-22 09:00:00',
'Moscow, Russia',
'42.2km/10km',
'The 2019 Absolute Moscow Marathon is scheduled to start on September 22, 2019. The competition will be set up for the full marathon and 10 km. The venue is located in Moscow, the capital of the Russian Federation. The entire track will take you to this famous city with a long and splendid history, including the Kremlin, the Cathedral of Christ the Savior, the Bolshoi Theatre and many more than 30 world-famous attractions.',
'Refer to the official website',
'• Participants must be at least 18 years of age. • All participants are required to pick up their own packages during the two specified time periods, September 20, 2019, 11:00-21:00 and September 21, 10:00-18:00. • All entrants are required to present a photo ID and original medical certificate issued by the government department when they receive the package. The medical certificate is used to prove that the entrant''s physical and health status can participate in the running competition. • Once the registration fee is paid, it will not be refunded.',
'Non-Russian foreigners registration fee: • Registration before January 30, 2019 – 3,500 rubles. • Registration between January 31, 2019 and July 4, 2019 – 4500 rubles. • Registration between July 5, 2019 and August 22, 2019 – 5,500 rubles. • Among them, the registration fee for the two days from August 20th to August 21st is 6,500 rubles. Note: All the above information is subject to the official website.',
'https://moscowmarathon.runc.run/uploads/race_landing_header_backgrounds/Moscow_Marathon_cover.jpg');

INSERT INTO t_oracle_match
(matchId,
matchName,
matchStartTime,
matchAddress,
matchType,
matchDesp,
CompetitionRule,
EnrollGuide,
OtherInfo,
Banner)
VALUES
(10083,
'2019 Melbourne Marathon Festival',
'2019-10-13 07:00:00',
'Melbourne, Australia',
'42.2km/21.1km/10km',
'The 2019 Melbourne Marathon is Australia''s largest marathon of the year and will provide participants with a better experience on the basis of 2018. The competition provides various lengths of competitions such as Malaysia, Half Horse, 10 km, 5 km and 3 km. The organizers encourage participants to choose a reasonable schedule according to their actual situation. The end of the competition will be at the noted Melbourne Cricket Ground, and the entire trail will show you the charm of Melbourne.',
'Refer to the official website',
'1. Once the registration is successful, the registration fee will be cancelled due to personal reasons. The registration fee will not be refunded. 2. Due to the overseas marathon, please select the passport type and the correct passport number for the type of documents in the registration order. 3. Due to traffic delay/strike/ Cancellation of the game due to force majeure/flight/flight cancellation or natural disasters, etc.',
'Registration Fee: Marathon 42.195 km - AUD 158.00, Half Marathon 21.1 km - AUD 127.00, 10 km - AUD 66.00, 5 km - AUD 49, 3 km walk - AUD 41.00',
'https://photos-images.active.com/file/3/1/original/e8/5e/e85e8d65-8234-4038-be3c-b1cb0e48974b.jpg');

CREATE TABLE t_oracle_user_match (
  regId varchar(100) NOT NULL,
  userId varchar(100) NOT NULL,
  matchId varchar(100) NOT NULL,
  matchName varchar(100) NOT NULL,
  regDate varchar(100) DEFAULT NULL,
  status varchar(100) DEFAULT NULL,
  PRIMARY KEY (regId)
);
