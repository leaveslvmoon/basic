#sql("getAdvertisingSpaceList")
select * from advertisingspace where (name like '%#(map.name)%')
#if(map.status!=null&&map.status!="")
and status = #(map.status)
#end
order by
#if(map.sort!=null) #(map.sort) #(map.order),#end
id desc
#end