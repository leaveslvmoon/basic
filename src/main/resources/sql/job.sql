#sql("getJobList")
SELECT * FROM sys_job
WHERE
 CONCAT(job_name,job_desc) LIKE '%#(map.name)%'
order by
#if(map.sort!=null) #(map.sort) #(map.order),#end
id desc
#end