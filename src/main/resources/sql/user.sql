#sql("getUserList")
select * from bbs_user where status=1
order by
#if(map.sort!=null) #(map.sort) #(map.order),#end
id desc
#end