#sql("getApiContentList")
select
act.*,acs.`name` typeName
from
api_content act LEFT JOIN api_categories acs ON acs.id = act.type
where 1=1
#if(map.name!=null && map.name!="")
  and act.name like '%#(map.name)%'
#end
#if(map.type!=null && map.type!="")
  and type =  #(map.type)
#end
#if(map.CategoriesID!=null && map.CategoriesID!="")
  and type =  #(map.CategoriesID)
#end
order by #if(map.sort!=null&&map.sort!="") #(map.sort) #(map.order),#end id desc
#end


SELECT act.* , acs.name typeName FROM api_content act LEFT JOIN api_categories acs ON acs.id = act.type
