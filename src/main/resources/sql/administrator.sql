#sql("getSysAdministratorList")
select ad.id,userName,realName,psdErrorCount,loginCount,loginDate,loginIp,loginCity,ad.status,r.name roleId
from sys_administrator ad LEFT JOIN sys_role r on ad.roleId = r.id
where FIND_IN_SET(ad.id,getUserChildLst(#(map.id)))
and isDelete = 0
and ((userName like '%#(map.name)%') or( ad.realName like '%#(map.name)%'))
#if(map.status!=null&&map.status!="")
and adc.status = #(map.status)
#end
order by
#if(map.sort!=null) #(map.sort) #(map.order),#end
id desc
#end