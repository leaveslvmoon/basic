#sql("getIpWhitelist")
select * from ip_whitelist
 where ip like '%#(map.ip)%'
order by
#if(map.sort!=null) #(map.sort) #(map.order),#end
id desc
#end
