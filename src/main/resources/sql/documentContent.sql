#sql("getDocumentContentList")
select
ct.*,cct.`name` typeName
from
document_content ct LEFT JOIN document_categories cct ON cct.id = ct.type
where 1=1
#if(map.types!=null && map.types!="")
and type in (#(map.types))
#end
#if(map.name!=null && map.name!="")
  and ct.name like '%#(map.name)%'
#end

order by #if(map.sort!=null&&map.sort!="") #(map.sort) #(map.order),#end id desc
#end


