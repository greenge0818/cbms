set @num=0;
set @category_uuid='';
create table tmp_common_category_norms as 
select
category_uuid,
norms_uuid,
if(@category_uuid=x.category_uuid,@num:=@num+1,@num:=1) as rank,
@category_uuid:=x.category_uuid
from common_category_norms x

update common_category_norms a, 
tmp_common_category_norms b
set a.priority=b.rank
where a.category_uuid = b.category_uuid
and a.norms_uuid = b.norms_uuid
-- select * from common_category_norms x


insert into cust_resource(account_id,category_uuid,material_uuid,factory_id,warehouse_id,
weight_concept,price,Weight,remark,status,ext3,

created,created_by,last_updated,last_updated_by) 
select a.id ,
(select uuid from common_category where name=b.nsortName),
(select uuid from common_materials where name=b.`Materials`),
(select id from base_factory where name=b.`yieldly`),
(select id from base_warehouse where name=b.`CangKu`),
`OldId`,`Price`,`Weight`,remark,1,specs,
now(),'admin',now(),'admin'
from cust_account a,green b 

where a.name=b.sellerCompany
  
delete from cust_resource where category_uuid is null or material_uuid is null or ext3 is null

#truncate table cust_resource


insert into cust_resource_norms(resource_id,norm_uuid,value,priority,created,created_by)
select *from (
select resource_id,
(select norms_uuid from common_category_norms where category_uuid=t.category_uuid and priority=t.priority) norms_uuid,
val,priority,now(),'admin'
from tmp_split t

) x where norms_uuid is not null