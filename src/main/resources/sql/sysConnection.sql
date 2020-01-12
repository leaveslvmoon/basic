#sql("getConnectionList")
select * from sys_connection
order by
#if(map.sort!=null) #(map.sort) #(map.order),#end
id desc
#end