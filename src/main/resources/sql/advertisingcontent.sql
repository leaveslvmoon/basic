#sql("getAdvertisingContentList")
select adc.id ,adc.name,ads.name advertisingId,picurl,type,adc.sort,adc.status
from advertisingcontent adc LEFT JOIN advertisingspace ads on adc.advertisingId = ads.id
where (adc.name like '%#(map.name)%' or ads.name like '%#(map.name)%')
#if(map.status!=null&&map.status!="")
and adc.status = #(map.status)
#end
order by
#if(map.sort!=null) #(map.sort) #(map.order),#end
id desc
#end