#sql("getErrorLogList")
SELECT *  FROM errorlog
where 1=1
and  errorInfo like '%#(map.errorInfo)%'
#if(map.startTime!=null && map.startTime!="")
  and  TO_DAYS(time) >= TO_DAYS('#(map.startTime)')
#end
#if(map.endTime!=null && map.endTime!="")
  and  TO_DAYS(time) <= TO_DAYS('#(map.endTime)')
#end
order by id desc
#end