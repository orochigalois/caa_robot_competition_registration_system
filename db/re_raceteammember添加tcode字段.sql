alter table re_raceteammember add tcode varchar(20)  null default '0';

drop table t_raceteammenber;

#创建临时表
create table t_raceteammenber  
select rid,tid,CONCAT('Y', REPLACE(substring(createdate, 3,5),'-',''),'T',getRand(5)) as tcode,createdate from re_raceteammember group by rid,tid;


#SET SQL_SAFE_UPDATES = 0;

select * from t_raceteammenber;


#检查tcode重复 因为随机码重复 需要重新生成
select *,count(*)  from t_raceteammenber group by tcode having count(*)>1;

create table t_tmp
select *  from t_raceteammenber group by tcode having count(*)>1;

select * from t_tmp;
update t_tmp 
set tcode = CONCAT('Y', REPLACE(substring(createdate, 3,5),'-',''),'T',getRand(5));

select * from t_tmp;

#检查重复tcode
select * from t_raceteammenber a, t_tmp b
where a.tcode = b.tcode;

#修改tcode到t表
update t_raceteammenber a, t_tmp b
set a.tcode = b.tcode
where 
a.rid = b.rid 
and a.tid = b.tid
and a.createdate = b.createdate;


select * from t_raceteammenber where rid='b3d0d7e79c93457a8e7397822e0051e5' and tid='c388e3db50da45f7bdd5922fa73266c5'; --Y1707T63808
select * from t_raceteammenber where rid='28cf7c4c1b884ba08e5870e2b76aec50' and tid='ded4fe3db6344ab58c46e4915fa88b1c'; --Y1707T68052
select * from t_raceteammenber where rid='26f7bd9646ad48418441ae5486025c82' and tid='24745f6bdc2847598c215f1fe90d620a'; --Y1707T75400 2017-07-10 22:03

7754348c590c4c63bb493673f475bc35	6233273aaca9447aa8b704b09be10cf1	Y1707T38522	2017-07-05 13:02
36c6c71edf7744f09e8632db45ec920e	d9fb78915389435a9cec56df044096d3	Y1707T24823	2017-07-15 22:04
6fedcd6afb864cb2a4eaa9dca7cb9cf3	57d7946d2ad74a76ba2a345b8dbf25ed	Y1707T85470	2017-07-15 10:44
b3d0d7e79c93457a8e7397822e0051e5	c388e3db50da45f7bdd5922fa73266c5	Y1707T68267	2017-07-11 00:48
28cf7c4c1b884ba08e5870e2b76aec50	ded4fe3db6344ab58c46e4915fa88b1c	Y1707T15692	2017-07-15 21:09
26f7bd9646ad48418441ae5486025c82	24745f6bdc2847598c215f1fe90d620a	Y1707T73659	2017-07-10 22:03

SELECT * FROM re_raceteammember;

create table t_re_raceteammember
select * from re_raceteammember;

select count(*) from re_raceteammember; #7365
select count(*) from t_re_raceteammember; # 7365

select * from t_re_raceteammember;

#更新到主表
update re_raceteammember a,t_raceteammenber b set 
a.tcode = b.tcode
where  
a.rid = b.rid
and a.tid = b.tid;

select * from re_raceteammember group by rid, tid,tcode having count(*)>1 order by rid,tid;

select tcode,count(tcode) from re_raceteammember group by tcode having count(*)>1;

select * from re_raceteammember where tcode='Y1707T11243';

create table t_tm_team
select * from tm_team; #1642

select count(*) from tm_team;  #1642
select * from t_tm_team;  #1642


#删除列
select * from tm_team;

alter table tm_team drop column tcode;


#删除临时表
#drop table t_raceteammenber;
#drop table t_tmp
#drop table t_re_raceteammember;
#drop table t_tm_team;
