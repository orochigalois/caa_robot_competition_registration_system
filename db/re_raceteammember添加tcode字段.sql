alter table re_raceteammember add tcode varchar(20) not null default '0';

drop table t_raceteammenber;

#创建临时表
create table t_raceteammenber  
select rid,tid,CONCAT('Y', REPLACE(substring(createdate, 3,5),'-',''),'T',getRand(5)) as tcode,createdate from re_raceteammember group by rid,tid;


#SET SQL_SAFE_UPDATES = 0;

select * from t_raceteammenber;

 
#select * from t_raceteammenber group by rid,tid having count(*)>1;

#select rid,tid,count(tid),tcode from t_raceteammenber group by tid having count(*)>1 order by rid, tid;

#select * from t_raceteammenber where tid ='815822af940144abba69505c805bc94b';

#select count(distinct tcode) from t_raceteammenber;

#select count(*) from t_raceteammenber;

#检查tcode重复 因为随机码重复 需要重新生成
select *,count(*)  from t_raceteammenber group by tcode having count(*)>1;

create table t_tmp
select *  from t_raceteammenber group by tcode having count(*)>1;

select * from t_tmp;
update t_tmp 
set tcode = CONCAT('Y', REPLACE(substring(createdate, 3,5),'-',''),'T',getRand(5));

select * from t_tmp;

#select * from t_raceteammenber where tcode='Y1707T729743';

#select getRand(6);

#select * from t_raceteammenber where tcode='Y1707T468114';

#update t_raceteammenber set tcode ='Y1707T468114' where rid='6fedcd6afb864cb2a4eaa9dca7cb9cf3' and tid='03bca93830674c3ab79e783cb4d83777';

#更新到主表
update re_raceteammember a,t_raceteammenber b set 
a.tcode = b.tcode
where  
a.rid = b.rid
and a.tid = b.tid;

select * from re_raceteammember group by rid, tid,tcode having count(*)>1 order by rid,tid;

select tcode,count(tcode) from re_raceteammember group by tcode having count(*)>1;

select * from re_raceteammember where tcode='Y1707T100041';

#删除列
select * from tm_team;

alter table tm_team drop column tcode;





