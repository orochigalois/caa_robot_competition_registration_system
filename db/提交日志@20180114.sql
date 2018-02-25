alter table ma_match
add (
islog varchar(1) default '0' comment '是否提交日志0否1是', 
stend varchar(19) comment '阶段一提交日志结束时间',
ndend varchar(19)  comment '阶段二提交日志结束时间', 
rdend varchar(19)  comment '阶段三提交日志结束时间');

alter table ma_race
add (
islog varchar(1) default '0' comment '是否提交日志0否1是', 
stend varchar(19) comment '阶段一提交日志结束时间',
ndend varchar(19)  comment '阶段二提交日志结束时间', 
rdend varchar(19)  comment '阶段三提交日志结束时间');

#alter table ma_match drop islog, drop firstsublogend, drop sndsublogend,drop thirdsublogend;

#alter table ma_race drop islog, drop firstsublogend, drop sndsublogend,drop thirdsublogend;

alter table re_raceteammember
add (
stlogurl varchar(1000) comment '阶段一提交日志结束时间',
ndlogurl varchar(1000)  comment '阶段二提交日志结束时间', 
rdlogurl varchar(1000)  comment '阶段三提交日志结束时间');