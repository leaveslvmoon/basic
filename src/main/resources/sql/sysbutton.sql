#sql("getButtonList")
select * from sys_button where (name like '%#(map.name)%') or( methodName like '%#(map.name)%')
order by
#if(map.sort!=null) #(map.sort) #(map.order),#end
id desc
#end
