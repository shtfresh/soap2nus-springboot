alter table t_oracle_tp add column totalDays int(50) after maxKilometre;
update t_oracle_tp set totalDays=0;
