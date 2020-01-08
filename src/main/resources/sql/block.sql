#sql("getBlockList")
select *
from bbs_block
where 1=1
#if(map.name!=null && map.name!="")
  and name like '%#(map.name)%'
#end
#if(map.status!=null && map.status!="")
  and status = #(map.status)
#end
order by #if(map.sort!=null&&map.sort!="") #(map.sort) #(map.order),#end id desc
#end