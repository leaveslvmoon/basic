#sql("getPushMessageList")
select pm.*,m.mobile as mobile  from push_message as pm
 left join  member as m ON pm.pushTarget = m.id
where (pushDescription like '%#(map.pushDescription)%')
 and  pm.deleteStatus=0
#if(map.pushType!=null&&map.pushType!="")
and pm.pushType = #(map.pushType)
#end
order by
#if(map.sort!=null) #(map.sort) #(map.order),#end
id desc
#end