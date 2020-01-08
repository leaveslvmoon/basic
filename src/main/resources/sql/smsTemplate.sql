#sql("getSmsTemplate")
select * from sys_smstemplate
where (title like '%#(map.title)%')
and  status=1
order by
#if(map.sort!=null) #(map.sort) #(map.order),#end
id desc
#end