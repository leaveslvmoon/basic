#sql("getMacWhitelist")
select * from mac_whitelist
 where mac like '%#(map.mac)%'
order by
#if(map.sort!=null) #(map.sort) #(map.order),#end
id desc
#end
