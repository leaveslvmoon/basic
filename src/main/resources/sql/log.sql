#sql("getLogList")
SELECT ar.userName userName,lg.*  FROM log lg LEFT JOIN sys_administrator ar on lg.userId=ar.id
where 1=1
and CONCAT(userName,ip,ipCity,url) LIKE '%#(map.keys)%'
#if(map.startTime!=null && map.startTime!="")
  and  TO_DAYS(datetime) >= TO_DAYS('#(map.startTime)')
#end
#if(map.endTime!=null && map.endTime!="")
  and  TO_DAYS(datetime) <= TO_DAYS('#(map.endTime)')
#end
order by id desc
#end