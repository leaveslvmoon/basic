#sql("getPushDevicList")
select * from member
where (mobile like '%#(map.mobile)%')
 and  status=1
 and  logoutStatus=0
 and  devicePushToken IS NOT NULL
order by
#if(map.sort!=null) #(map.sort) #(map.order),#end
id desc
#end